<template>
  <DaoTaoPageShell :title="titleText" description="Quản lý loại chương trình 1 năm, 1.5 năm, 2 năm. Bấm Khung kỳ để đi sâu theo id loại chương trình.">
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
      <template #row-actions="{ item }">
        <button class="link-btn" type="button" @click="router.push({ name: 'dao-tao-loai-chuong-trinh-khung-ky', params: { loaiChuongTrinhId: item.id } })">Khung kỳ</button>
      </template>
    </DaoTaoCrudTable>

    <DaoTaoFormModal v-model="showForm" :title="formTitle" :saving="saving" @submit="saveItem">
      <div class="field">
        <label>Mã loại</label>
        <input v-model.trim="form.maLoai" required />
      </div>
      <div class="field">
        <label>Tên loại</label>
        <input v-model.trim="form.tenLoai" required />
      </div>
      <div class="field">
        <label>Số tháng</label>
        <input v-model.number="form.soThang" type="number" />
      </div>
      <div class="field">
        <label>Số kỳ</label>
        <input v-model.number="form.soKy" type="number" />
      </div>
      <div class="field full">
        <label>Mô tả</label>
        <textarea v-model.trim="form.moTa" />
      </div>
    </DaoTaoFormModal>
  </DaoTaoPageShell>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import DaoTaoPageShell from './shared/DaoTaoPageShell.vue';
import DaoTaoCrudTable from './shared/DaoTaoCrudTable.vue';
import DaoTaoFormModal from './shared/DaoTaoFormModal.vue';
import { getAllLoaiChuongTrinh, getLoaiChuongTrinhById } from '@/api/daoTao/ApiRespone/LoaiChuongTrinhController';
import { createLoaiChuongTrinh, updateLoaiChuongTrinh, deleteLoaiChuongTrinh } from '@/api/daoTao/ApiRequest/LoaiChuongTrinhController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();


const titleText = 'Loại chương trình';
const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({ maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: '' });
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'maLoai', label: 'Mã loại' },
  { key: 'tenLoai', label: 'Tên loại' },
  { key: 'soThang', label: 'Số tháng' },
  { key: 'soKy', label: 'Số kỳ' },
  { key: 'moTa', label: 'Mô tả' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật loại chương trình' : 'Thêm loại chương trình');
const filteredItems = computed(() => items.value.filter((item) => matchKeyword(item, keyword.value, ['maLoai', 'tenLoai', 'moTa'])));

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, { maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: '' });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';
  try {
    items.value = unwrapApiList(await getAllLoaiChuongTrinh());
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
    maLoai: item.maLoai ?? '',
    tenLoai: item.tenLoai ?? '',
    soThang: item.soThang ?? null,
    soKy: item.soKy ?? null,
    moTa: item.moTa ?? '',
  });
  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;
  try {
    if (editingId.value) await updateLoaiChuongTrinh(editingId.value, { ...form });
    else await createLoaiChuongTrinh({ ...form });
    showForm.value = false;
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  } finally {
    saving.value = false;
  }
};

const removeItem = async (item) => {
  if (!window.confirm(`Xóa loại chương trình ID ${item.id}?`)) return;
  try {
    await deleteLoaiChuongTrinh(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

const goDetail = async (item) => {
  await getLoaiChuongTrinhById(item.id);
  router.push({ name: 'dao-tao-loai-chuong-trinh-detail', params: { id: item.id } });
};

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
