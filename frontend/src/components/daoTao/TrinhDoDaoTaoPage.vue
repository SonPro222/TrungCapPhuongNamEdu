<template>
  <DaoTaoPageShell :title="titleText" description="Quản lý danh mục trình độ: Trung cấp, Cao đẳng, Sơ cấp...">
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
        <label>Mã trình độ</label>
        <input v-model.trim="form.maTrinhDo" required />
      </div>
      <div class="field">
        <label>Tên trình độ</label>
        <input v-model.trim="form.tenTrinhDo" required />
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
import { getAllTrinhDoDaoTao, getTrinhDoDaoTaoById } from '@/api/daoTao/ApiRespone/TrinhDoDaoTaoController';
import { createTrinhDoDaoTao, updateTrinhDoDaoTao, deleteTrinhDoDaoTao } from '@/api/daoTao/ApiRequest/TrinhDoDaoTaoController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();


const titleText = 'Trình độ đào tạo';
const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({ maTrinhDo: '', tenTrinhDo: '', moTa: '' });
const columns = [
  { key: 'id', label: 'ID' },
  { key: 'maTrinhDo', label: 'Mã trình độ' },
  { key: 'tenTrinhDo', label: 'Tên trình độ' },
  { key: 'moTa', label: 'Mô tả' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật trình độ' : 'Thêm trình độ');
const filteredItems = computed(() => items.value.filter((item) => matchKeyword(item, keyword.value, ['maTrinhDo', 'tenTrinhDo', 'moTa'])));

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, { maTrinhDo: '', tenTrinhDo: '', moTa: '' });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';
  try {
    items.value = unwrapApiList(await getAllTrinhDoDaoTao());
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
    maTrinhDo: item.maTrinhDo ?? '',
    tenTrinhDo: item.tenTrinhDo ?? '',
    moTa: item.moTa ?? '',
  });
  showForm.value = true;
};

const saveItem = async () => {
  saving.value = true;
  try {
    if (editingId.value) await updateTrinhDoDaoTao(editingId.value, { ...form });
    else await createTrinhDoDaoTao({ ...form });
    showForm.value = false;
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  } finally {
    saving.value = false;
  }
};

const removeItem = async (item) => {
  if (!window.confirm(`Xóa trình độ ID ${item.id}?`)) return;
  try {
    await deleteTrinhDoDaoTao(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

const goDetail = async (item) => {
  await getTrinhDoDaoTaoById(item.id);
  router.push({ name: 'dao-tao-trinh-do-detail', params: { id: item.id } });
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
