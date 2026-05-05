<template>
  <DaoTaoPageShell :title="titleText" description="Quản lý học kỳ theo loại chương trình. Nếu vào từ loại chương trình, danh sách được lọc theo loaiChuongTrinhId.">
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
        <label>ID loại chương trình</label>
        <input v-model.number="form.loaiChuongTrinhId" type="number" />
      </div>
      <div class="field">
        <label>Mã kỳ</label>
        <input v-model.trim="form.maKy" required />
      </div>
      <div class="field">
        <label>Tên kỳ</label>
        <input v-model.trim="form.tenKy" required />
      </div>
      <div class="field">
        <label>Thứ tự</label>
        <input v-model.number="form.thuTu" type="number" />
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
import { getAllKhungKy, getKhungKyById } from '@/api/daoTao/ApiRespone/KhungKyController';
import { createKhungKy, updateKhungKy, deleteKhungKy } from '@/api/daoTao/ApiRequest/KhungKyController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();
const route = useRoute();
const loaiChuongTrinhId = computed(() => route.params.loaiChuongTrinhId || null);
const titleText = 'Khung kỳ';
const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({ loaiChuongTrinhId: null, maKy: '', tenKy: '', thuTu: null });
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'loaiChuongTrinhId', label: 'ID loại chương trình' },
  { key: 'maKy', label: 'Mã kỳ' },
  { key: 'tenKy', label: 'Tên kỳ' },
  { key: 'thuTu', label: 'Thứ tự' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật khung kỳ' : 'Thêm khung kỳ');
const filteredItems = computed(() => items.value
  .filter((item) => !loaiChuongTrinhId.value || String(item.loaiChuongTrinhId) === String(loaiChuongTrinhId.value))
  .filter((item) => matchKeyword(item, keyword.value, ['maKy', 'tenKy', 'loaiChuongTrinhId'])));

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, { loaiChuongTrinhId: loaiChuongTrinhId.value ? Number(loaiChuongTrinhId.value) : null, maKy: '', tenKy: '', thuTu: null });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';
  try {
    const params = loaiChuongTrinhId.value ? { loaiChuongTrinhId: loaiChuongTrinhId.value } : {};
    items.value = unwrapApiList(await getAllKhungKy(params));
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
    loaiChuongTrinhId: item.loaiChuongTrinhId ?? null,
    maKy: item.maKy ?? '',
    tenKy: item.tenKy ?? '',
    thuTu: item.thuTu ?? null,
  });
  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;
  try {
    if (editingId.value) await updateKhungKy(editingId.value, { ...form });
    else await createKhungKy({ ...form });
    showForm.value = false;
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  } finally {
    saving.value = false;
  }
};

const removeItem = async (item) => {
  if (!window.confirm(`Xóa khung kỳ ID ${item.id}?`)) return;
  try {
    await deleteKhungKy(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

const goDetail = async (item) => {
  await getKhungKyById(item.id);
  router.push({ name: 'dao-tao-khung-ky-detail', params: { id: item.id } });
};

watch(() => route.params.loaiChuongTrinhId, fetchItems);
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
