<template>
  <DaoTaoPageShell :title="titleText" :description="descriptionText">
    <template #breadcrumb>
      <RouterLink :to="{ name: 'dao-tao-khoa-dao-tao' }">Khóa đào tạo</RouterLink>
      <span v-if="khoaDaoTaoId"> / Lớp hành chính của khóa {{ khoaDaoTaoId }}</span>
      <span v-else> / Lớp hành chính</span>
    </template>

    <DaoTaoCrudTable
        v-model:keyword="keyword"
        :columns="columns"
        :items="filteredItems"
        :loading="loading"
        :error="error"
        @reload="fetchItems"
        @create="openCreate"
        @edit="openEdit"
        @remove="removeItem"
        @view="goDetail"
    />

    <DaoTaoFormModal v-model="showForm" :title="formTitle" :saving="saving" @submit="saveItem">
      <div class="field">
        <label>ID khóa đào tạo</label>
        <input v-model.number="form.khoaDaoTaoId" type="number" required />
      </div>

      <div class="field">
        <label>Mã lớp</label>
        <input v-model.trim="form.maLop" required />
      </div>

      <div class="field">
        <label>Tên lớp</label>
        <input v-model.trim="form.tenLop" required />
      </div>

      <div class="field">
        <label>Sĩ số</label>
        <input v-model.number="form.siSo" type="number" />
      </div>

      <div class="field">
        <label>Trạng thái</label>
        <input v-model.trim="form.trangThai" />
      </div>
    </DaoTaoFormModal>
  </DaoTaoPageShell>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { RouterLink, useRoute, useRouter } from 'vue-router';
import DaoTaoPageShell from '@/components/shared/daoTao/DaoTaoPageShell.vue';
import DaoTaoCrudTable from '@/components/shared/daoTao/DaoTaoCrudTable.vue';
import DaoTaoFormModal from '@/components/shared/daoTao/DaoTaoFormModal.vue';
import { getAllLopHanhChinh, getLopHanhChinhById } from '@/api/daoTao/ApiRespone/LopHanhChinhController';
import { createLopHanhChinh, updateLopHanhChinh, deleteLopHanhChinh } from '@/api/daoTao/ApiRequest/LopHanhChinhController';
import { getErrorMessage, matchKeyword, unwrapApiData, unwrapApiList } from '../../api/apiResponse.js';

const route = useRoute();
const router = useRouter();

const khoaDaoTaoId = computed(() => route.params.khoaDaoTaoId || null);

const titleText = computed(() => {
  return khoaDaoTaoId.value ? 'Lớp hành chính theo khóa đào tạo' : 'Lớp hành chính';
});

const descriptionText = computed(() => {
  return khoaDaoTaoId.value
      ? 'Danh sách lớp hành chính được lọc theo id khóa đào tạo.'
      : 'Quản lý danh sách lớp hành chính.';
});

const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({
  khoaDaoTaoId: null,
  maLop: '',
  tenLop: '',
  siSo: null,
  trangThai: '',
});

const columns = [
  { key: 'id', label: 'ID' },
  { key: 'khoaDaoTaoId', label: 'ID khóa đào tạo' },
  { key: 'maLop', label: 'Mã lớp' },
  { key: 'tenLop', label: 'Tên lớp' },
  { key: 'siSo', label: 'Sĩ số' },
  { key: 'trangThai', label: 'Trạng thái' },
];

const formTitle = computed(() => {
  return editingId.value ? 'Cập nhật lớp hành chính' : 'Thêm lớp hành chính';
});

const filteredItems = computed(() => {
  return items.value
      .filter((item) => !khoaDaoTaoId.value || String(item.khoaDaoTaoId) === String(khoaDaoTaoId.value))
      .filter((item) => matchKeyword(item, keyword.value, ['maLop', 'tenLop', 'khoaDaoTaoId', 'trangThai']));
});

const resetForm = () => {
  editingId.value = null;

  Object.assign(form, {
    khoaDaoTaoId: khoaDaoTaoId.value ? Number(khoaDaoTaoId.value) : null,
    maLop: '',
    tenLop: '',
    siSo: null,
    trangThai: '',
  });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';

  try {
    items.value = unwrapApiList(await getAllLopHanhChinh());
  } catch (err) {
    error.value = getErrorMessage(err);
  } finally {
    loading.value = false;
  }
};

const openCreate = () => {
  resetForm();
  showForm.value = true;
};

const openEdit = (item) => {
  editingId.value = item.id;

  Object.assign(form, {
    khoaDaoTaoId: item.khoaDaoTaoId ?? null,
    maLop: item.maLop ?? '',
    tenLop: item.tenLop ?? '',
    siSo: item.siSo ?? null,
    trangThai: item.trangThai ?? '',
  });

  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;

  try {
    const payload = { ...form };

    if (editingId.value) {
      await updateLopHanhChinh(editingId.value, payload);
    } else {
      await createLopHanhChinh(payload);
    }

    showForm.value = false;
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  } finally {
    saving.value = false;
  }
};

const removeItem = async (item) => {
  if (!window.confirm(`Xóa lớp hành chính ID ${item.id}?`)) return;

  try {
    await deleteLopHanhChinh(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

const goDetail = async (item) => {
  try {
    const response = await getLopHanhChinhById(item.id);
    const detail = unwrapApiData(response);

    router.push({
      name: 'dao-tao-lop-hanh-chinh-detail',
      params: { lopHanhChinhId: item.id },
      state: { lopHanhChinh: detail },
    });
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

watch(() => route.params.khoaDaoTaoId, fetchItems);
onMounted(fetchItems);
</script>

<style scoped>
.link-btn {
  color: #2563eb;
  background: transparent;
  border: 0;
  cursor: pointer;
  font-weight: 800;
}
</style>
