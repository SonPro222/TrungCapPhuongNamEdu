<template>
  <DaoTaoPageShell :title="titleText" description="Quản lý khóa đào tạo. Bấm Lớp hành chính để đi sâu theo id khóa.">
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
        <button class="link-btn" type="button" @click="router.push({ name: 'dao-tao-khoa-lop-hanh-chinh', params: { khoaDaoTaoId: item.id } })">Lớp hành chính</button>
      </template>
    </DaoTaoCrudTable>

    <DaoTaoFormModal v-model="showForm" :title="formTitle" :saving="saving" @submit="saveItem">
      <div class="field">
        <label>Mã khóa</label>
        <input v-model.trim="form.maKhoa" required />
      </div>
      <div class="field">
        <label>Tên khóa</label>
        <input v-model.trim="form.tenKhoa" required />
      </div>
      <div class="field">
        <label>Năm bắt đầu</label>
        <input v-model.number="form.namBatDau" type="number" />
      </div>
      <div class="field">
        <label>Năm kết thúc</label>
        <input v-model.number="form.namKetThuc" type="number" />
      </div>
      <div class="field full">
        <label>Ghi chú</label>
        <textarea v-model.trim="form.ghiChu" />
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
import { getAllKhoaDaoTao, getKhoaDaoTaoById } from '@/api/daoTao/ApiRespone/KhoaDaoTaoController';
import { createKhoaDaoTao, updateKhoaDaoTao, deleteKhoaDaoTao } from '@/api/daoTao/ApiRequest/KhoaDaoTaoController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();


const titleText = 'Khóa đào tạo';
const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({ maKhoa: '', tenKhoa: '', namBatDau: null, namKetThuc: null, ghiChu: '' });
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'maKhoa', label: 'Mã khóa' },
  { key: 'tenKhoa', label: 'Tên khóa' },
  { key: 'namBatDau', label: 'Năm bắt đầu' },
  { key: 'namKetThuc', label: 'Năm kết thúc' },
  { key: 'ghiChu', label: 'Ghi chú' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật khóa đào tạo' : 'Thêm khóa đào tạo');
const filteredItems = computed(() => items.value.filter((item) => matchKeyword(item, keyword.value, ['maKhoa', 'tenKhoa', 'ghiChu'])));

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, { maKhoa: '', tenKhoa: '', namBatDau: null, namKetThuc: null, ghiChu: '' });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';
  try {
    items.value = unwrapApiList(await getAllKhoaDaoTao());
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
    maKhoa: item.maKhoa ?? '',
    tenKhoa: item.tenKhoa ?? '',
    namBatDau: item.namBatDau ?? null,
    namKetThuc: item.namKetThuc ?? null,
    ghiChu: item.ghiChu ?? '',
  });
  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;
  try {
    if (editingId.value) await updateKhoaDaoTao(editingId.value, { ...form });
    else await createKhoaDaoTao({ ...form });
    showForm.value = false;
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  } finally {
    saving.value = false;
  }
};

const removeItem = async (item) => {
  if (!window.confirm(`Xóa khóa đào tạo ID ${item.id}?`)) return;
  try {
    await deleteKhoaDaoTao(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

const goDetail = async (item) => {
  await getKhoaDaoTaoById(item.id);
  router.push({ name: 'dao-tao-khoa-dao-tao-detail', params: { id: item.id } });
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
