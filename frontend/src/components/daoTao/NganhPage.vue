<template>
  <DaoTaoPageShell
    title="Ngành đào tạo"
    description="Trang cha của luồng chương trình đào tạo. Bấm vào một ngành để đi sâu theo id ngành."
  >
    <template #actions>
      <RouterLink class="nav-btn" :to="{ name: 'dao-tao-trinh-do' }">Trình độ</RouterLink>
      <RouterLink class="nav-btn" :to="{ name: 'dao-tao-loai-chuong-trinh' }">Loại chương trình</RouterLink>
      <RouterLink class="nav-btn" :to="{ name: 'dao-tao-khoa-dao-tao' }">Khóa đào tạo</RouterLink>
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
        <button class="link-btn" type="button" @click="goChuongTrinh(item)">Chương trình</button>
      </template>
    </DaoTaoCrudTable>

    <DaoTaoFormModal v-model="showForm" :title="formTitle" :saving="saving" @submit="saveItem">
      <div class="field">
        <label>Mã ngành</label>
        <input v-model.trim="form.maNganh" required />
      </div>
      <div class="field">
        <label>Tên ngành</label>
        <input v-model.trim="form.tenNganh" required />
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
import { useRouter } from 'vue-router';
import DaoTaoPageShell from './shared/DaoTaoPageShell.vue';
import DaoTaoCrudTable from './shared/DaoTaoCrudTable.vue';
import DaoTaoFormModal from './shared/DaoTaoFormModal.vue';
import { getAllNganh, getNganhById } from '@/api/daoTao/ApiRespone/NganhController';
import { createNganh, updateNganh, deleteNganh } from '@/api/daoTao/ApiRequest/NganhController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();
const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({ maNganh: '', tenNganh: '', moTa: '' });
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'maNganh', label: 'Mã ngành' },
  { key: 'tenNganh', label: 'Tên ngành' },
  { key: 'moTa', label: 'Mô tả' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật ngành' : 'Thêm ngành');
const filteredItems = computed(() => items.value.filter((item) => matchKeyword(item, keyword.value, ['maNganh', 'tenNganh', 'moTa'])));

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, { maNganh: '', tenNganh: '', moTa: '' });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';
  try {
    items.value = unwrapApiList(await getAllNganh());
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
    maNganh: item.maNganh ?? '',
    tenNganh: item.tenNganh ?? '',
    moTa: item.moTa ?? '',
  });
  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;
  try {
    if (editingId.value) await updateNganh(editingId.value, { ...form });
    else await createNganh({ ...form });
    showForm.value = false;
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  } finally {
    saving.value = false;
  }
};

const removeItem = async (item) => {
  if (!window.confirm(`Xóa ngành ${item.tenNganh || item.maNganh}?`)) return;
  try {
    await deleteNganh(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

const goDetail = async (item) => {
  await getNganhById(item.id);
  router.push({ name: 'dao-tao-nganh-detail', params: { nganhId: item.id } });
};

const goChuongTrinh = (item) => {
  router.push({ name: 'dao-tao-nganh-chuong-trinh', params: { nganhId: item.id } });
};

onMounted(fetchItems);
</script>

<style scoped>
.nav-btn,
.link-btn {
  color: #2563eb;
  background: transparent;
  border: 0;
  cursor: pointer;
  font-weight: 800;
  text-decoration: none;
}

.nav-btn {
  padding: 10px 12px;
  border-radius: 10px;
  background: #dbeafe;
}
</style>
