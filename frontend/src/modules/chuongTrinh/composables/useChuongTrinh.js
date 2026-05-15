import { computed, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { DEFAULT_CHUONG_TRINH_RESOURCE, CHUONG_TRINH_RESOURCES } from '@/modules/chuongTrinh/constants/trangThaiChuongTrinh.js';
import { getRecordId } from '@/modules/chuongTrinh/api/chuongTrinhApi.js';
import {
  buildInitialForm,
  buildPayload,
  createChuongTrinhRecord,
  deleteChuongTrinhRecord,
  fetchChuongTrinhList,
  updateChuongTrinhRecord,
} from '@/modules/chuongTrinh/services/chuongTrinhService.js';

export const useChuongTrinh = () => {
  const route = useRoute();
  const router = useRouter();

  const rows = ref([]);
  const loading = ref(false);
  const saving = ref(false);
  const deleting = ref(false);
  const error = ref('');
  const keyword = ref('');
  const filters = reactive({});
  const form = reactive({});
  const editingRecord = ref(null);
  const isFormOpen = ref(false);
  const deleteTarget = ref(null);

  const activeResource = computed(() => {
    const segment = route.params.resource || route.path.split('/').filter(Boolean).at(-1);
    return CHUONG_TRINH_RESOURCES.find((item) => item.segment === segment || item.key === segment) || DEFAULT_CHUONG_TRINH_RESOURCE;
  });

  const activeResourceKey = computed(() => activeResource.value.key);

  const serverFilters = computed(() => {
    const params = {};
    for (const filter of activeResource.value.filters || []) params[filter.key] = filters[filter.key];
    return params;
  });

  const filteredRows = computed(() => {
    const search = keyword.value.trim().toLowerCase();
    if (!search) return rows.value;
    const fields = activeResource.value.searchFields || activeResource.value.columns.map((col) => col.key);
    return rows.value.filter((row) => fields.some((field) => String(row[field] ?? '').toLowerCase().includes(search)));
  });

  const syncFiltersFromQuery = () => {
    const allowedKeys = new Set((activeResource.value.filters || []).map((filter) => filter.key));

    Object.keys(filters).forEach((key) => {
      if (!allowedKeys.has(key)) delete filters[key];
    });

    for (const filter of activeResource.value.filters || []) {
      filters[filter.key] = route.query[filter.key] ?? '';
    }
  };

  const resetForm = (source = {}) => {
    const nextForm = buildInitialForm(activeResource.value, source, route.query);
    Object.keys(form).forEach((key) => delete form[key]);
    Object.assign(form, nextForm);
  };

  const loadRows = async () => {
    loading.value = true;
    error.value = '';

    try {
      rows.value = await fetchChuongTrinhList(activeResource.value, serverFilters.value);
    } catch (err) {
      error.value = err?.response?.data?.message || err?.response?.data?.error || err?.message || 'Không tải được dữ liệu từ backend.';
      rows.value = [];
    } finally {
      loading.value = false;
    }
  };

  const changeResource = (resource) => {
    if (resource.segment !== activeResource.value.segment) {
      router.push({ path: `/du-lieu/chuong-trinh/${resource.segment}` });
    }
  };

  const applyFilters = () => {
    router.replace({ path: route.path, query: { ...serverFilters.value } });
  };

  const clearFilters = () => {
    for (const filter of activeResource.value.filters || []) filters[filter.key] = '';
    keyword.value = '';
    router.replace({ path: route.path, query: {} });
  };

  const openCreate = () => {
    editingRecord.value = null;
    resetForm();
    isFormOpen.value = true;
  };

  const selectRecordInFlow = (record) => {
    const id = getRecordId(record, activeResource.value.idKey);
    const query = {};

    const addQuery = (key, value) => {
      if (value !== undefined && value !== null && value !== '') query[key] = value;
    };

    if (activeResourceKey.value === 'chuong-trinh') {
      addQuery('chuongTrinhId', id);
    } else if (activeResourceKey.value === 'chuong-trinh-version') {
      addQuery('chuongTrinhId', record.chuongTrinhId);
      addQuery('chuongTrinhVersionId', id);
    } else if (activeResourceKey.value === 'nhom-kien-thuc') {
      addQuery('chuongTrinhVersionId', record.chuongTrinhVersionId);
      addQuery('nhomKienThucId', id);
    } else if (activeResourceKey.value === 'chuong-trinh-mon') {
      addQuery('chuongTrinhVersionId', record.chuongTrinhVersionId);
      addQuery('nhomKienThucId', record.nhomKienThucId);
      addQuery('chuongTrinhMonId', id);
      addQuery('monHocId', record.monHocId);
    } else if (activeResourceKey.value === 'mon-hoc') {
      addQuery('monHocId', id);
    } else {
      addQuery('chuongTrinhId', record.chuongTrinhId);
      addQuery('chuongTrinhVersionId', record.chuongTrinhVersionId);
      addQuery('nhomKienThucId', record.nhomKienThucId);
      addQuery('chuongTrinhMonId', record.chuongTrinhMonId);
      addQuery('monHocId', record.monHocId);
      addQuery('syllabusMonId', record.syllabusMonId);
    }

    router.replace({ path: route.path, query });
  };

  const openEdit = (record) => {
    editingRecord.value = record;
    resetForm(record);
    isFormOpen.value = true;
  };

  const closeForm = () => {
    isFormOpen.value = false;
  };

  const saveForm = async () => {
    saving.value = true;
    error.value = '';

    try {
      const payload = buildPayload(activeResource.value, form);
      const id = getRecordId(editingRecord.value, activeResource.value.idKey);

      if (id) await updateChuongTrinhRecord(activeResource.value, id, payload);
      else await createChuongTrinhRecord(activeResource.value, payload);

      closeForm();
      await loadRows();
    } catch (err) {
      error.value = err?.response?.data?.message || err?.response?.data?.error || err?.message || 'Không lưu được dữ liệu.';
    } finally {
      saving.value = false;
    }
  };

  const requestDelete = (record) => {
    deleteTarget.value = record;
  };

  const cancelDelete = () => {
    deleteTarget.value = null;
  };

  const confirmDelete = async () => {
    if (!deleteTarget.value) return;

    const id = getRecordId(deleteTarget.value, activeResource.value.idKey);
    if (!id) return;

    deleting.value = true;
    error.value = '';

    try {
      await deleteChuongTrinhRecord(activeResource.value, id);
      deleteTarget.value = null;
      await loadRows();
    } catch (err) {
      error.value = err?.response?.data?.message || err?.response?.data?.error || err?.message || 'Không xóa được dữ liệu.';
    } finally {
      deleting.value = false;
    }
  };

  const formatCell = (record, column) => {
    const value = record[column.key];
    if (value === null || value === undefined || value === '') return '—';
    if (column.type === 'boolean') return value ? 'Có' : 'Không';
    if (column.type === 'date') return new Date(value).toLocaleDateString('vi-VN');
    return value;
  };

  watch(
    () => route.fullPath,
    () => {
      syncFiltersFromQuery();
      resetForm();
      loadRows();
    },
    { immediate: true },
  );

  return {
    activeResource,
    activeResourceKey,
    applyFilters,
    cancelDelete,
    changeResource,
    clearFilters,
    closeForm,
    confirmDelete,
    deleteTarget,
    deleting,
    error,
    filteredRows,
    filters,
    form,
    formatCell,
    isFormOpen,
    keyword,
    loadRows,
    loading,
    openCreate,
    openEdit,
    requestDelete,
    selectRecordInFlow,
    resources: CHUONG_TRINH_RESOURCES,
    saveForm,
    saving,
  };
};
