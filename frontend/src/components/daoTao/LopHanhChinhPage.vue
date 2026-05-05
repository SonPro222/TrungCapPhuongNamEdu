<template>
  <DaoTaoPageShell :title="titleText" description="Quản lý lớp hành chính theo chương trình version và khóa đào tạo. Nếu vào từ khóa, danh sách được lọc theo khoaDaoTaoId.">
    <template #breadcrumb>
      <RouterLink :to="{ name: 'dao-tao-nganh' }">Ngành</RouterLink>
      <span> / {{ titleText }}</span>
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
    >

    </DaoTaoCrudTable>

    <DaoTaoFormModal v-model="showForm" :title="formTitle" :saving="saving" @submit="saveItem">
      <div class="field">
        <label>Mã lớp</label>
        <input v-model.trim="form.maLop" required />
      </div>
      <div class="field">
        <label>Tên lớp</label>
        <input v-model.trim="form.tenLop" required />
      </div>
      <div class="field">
        <label>ID version chương trình</label>
        <input v-model.number="form.chuongTrinhVersionId" type="number" />
      </div>
      <div class="field">
        <label>ID khóa đào tạo</label>
        <input v-model.number="form.khoaDaoTaoId" type="number" />
      </div>
      <div class="field">
        <label>Sĩ số</label>
        <input v-model.number="form.siSo" type="number" />
      </div>
      <div class="field">
        <label>Trạng thái</label>
        <input v-model.trim="form.trangThai" required />
      </div>
      <div class="field full">
        <label>Ghi chú</label>
        <textarea v-model.trim="form.ghiChu" />
      </div>
    </DaoTaoFormModal>
  </DaoTaoPageShell>
</template>

<script setup>
import { computed, onMounted, reactive, ref, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DaoTaoPageShell from './shared/DaoTaoPageShell.vue';
import DaoTaoCrudTable from './shared/DaoTaoCrudTable.vue';
import DaoTaoFormModal from './shared/DaoTaoFormModal.vue';
import { getAllLopHanhChinh, getLopHanhChinhById } from '@/api/daoTao/ApiRespone/LopHanhChinhController';
import { createLopHanhChinh, updateLopHanhChinh, deleteLopHanhChinh } from '@/api/daoTao/ApiRequest/LopHanhChinhController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();
const route = useRoute();
const khoaDaoTaoId = computed(() => route.params.khoaDaoTaoId || null);
const chuongTrinhVersionId = computed(() => route.params.chuongTrinhVersionId || null);
const titleText = 'Lớp hành chính';
const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({ maLop: '', tenLop: '', chuongTrinhVersionId: null, khoaDaoTaoId: null, siSo: null, trangThai: '', ghiChu: '' });
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'maLop', label: 'Mã lớp' },
  { key: 'tenLop', label: 'Tên lớp' },
  { key: 'chuongTrinhVersionId', label: 'ID version chương trình' },
  { key: 'khoaDaoTaoId', label: 'ID khóa đào tạo' },
  { key: 'siSo', label: 'Sĩ số' },
  { key: 'trangThai', label: 'Trạng thái' },
  { key: 'ghiChu', label: 'Ghi chú' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật lớp hành chính' : 'Thêm lớp hành chính');
const filteredItems = computed(() => items.value
  .filter((item) => (!khoaDaoTaoId.value || String(item.khoaDaoTaoId) === String(khoaDaoTaoId.value))
    && (!chuongTrinhVersionId.value || String(item.chuongTrinhVersionId) === String(chuongTrinhVersionId.value)))
  .filter((item) => matchKeyword(item, keyword.value, ['maLop', 'tenLop', 'trangThai', 'ghiChu'])));

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, {
    maLop: '',
    tenLop: '',
    chuongTrinhVersionId: chuongTrinhVersionId.value ? Number(chuongTrinhVersionId.value) : null,
    khoaDaoTaoId: khoaDaoTaoId.value ? Number(khoaDaoTaoId.value) : null,
    siSo: null,
    trangThai: 'du_kien',
    ghiChu: '',
  });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';
  try {
    const params = {
      ...(khoaDaoTaoId.value ? { khoaDaoTaoId: khoaDaoTaoId.value } : {}),
      ...(chuongTrinhVersionId.value ? { chuongTrinhVersionId: chuongTrinhVersionId.value } : {}),
    };
    items.value = unwrapApiList(await getAllLopHanhChinh(params));
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
    maLop: item.maLop ?? '',
    tenLop: item.tenLop ?? '',
    chuongTrinhVersionId: item.chuongTrinhVersionId ?? null,
    khoaDaoTaoId: item.khoaDaoTaoId ?? null,
    siSo: item.siSo ?? null,
    trangThai: item.trangThai ?? '',
    ghiChu: item.ghiChu ?? '',
  });
  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;
  try {
    if (editingId.value) await updateLopHanhChinh(editingId.value, { ...form });
    else await createLopHanhChinh({ ...form });
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
  await getLopHanhChinhById(item.id);
  router.push({ name: 'dao-tao-lop-hanh-chinh-detail', params: { id: item.id } });
};

watch(() => [route.params.khoaDaoTaoId, route.params.chuongTrinhVersionId], fetchItems);
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
