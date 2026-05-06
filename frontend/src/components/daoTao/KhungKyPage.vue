<template>
  <DaoTaoPageShell :title="titleText" :description="descriptionText">
    <template #breadcrumb>
      <RouterLink :to="{ name: 'dao-tao-loai-chuong-trinh' }">Loại chương trình</RouterLink>
      <span v-if="loaiChuongTrinhId"> / Khung kỳ của loại {{ loaiChuongTrinhId }}</span>
      <span v-else> / Khung kỳ</span>
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
        <label>ID loại chương trình</label>
        <input v-model.number="form.loaiChuongTrinhId" type="number" required />
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
import DaoTaoPageShell from '@/components/shared/daoTao/DaoTaoPageShell.vue';
import DaoTaoCrudTable from '@/components/shared/daoTao/DaoTaoCrudTable.vue';
import DaoTaoFormModal from '@/components/shared/daoTao/DaoTaoFormModal.vue';
import { getAllKhungKy } from '@/api/daoTao/ApiRespone/KhungKyController';
import { createKhungKy, updateKhungKy, deleteKhungKy } from '@/api/daoTao/ApiRequest/KhungKyController';
import { getErrorMessage, matchKeyword, unwrapApiList } from '../../api/apiResponse.js';

const router = useRouter();
const route = useRoute();

const loaiChuongTrinhId = computed(() => route.params.loaiChuongTrinhId || null);
const titleText = computed(() => loaiChuongTrinhId.value ? 'Khung kỳ theo loại chương trình' : 'Khung kỳ');
const descriptionText = computed(() => loaiChuongTrinhId.value ? 'Danh sách khung kỳ được lọc theo id loại chương trình.' : 'Danh sách toàn bộ khung kỳ.');

const items = ref([]);
const keyword = ref('');
const loading = ref(false);
const saving = ref(false);
const error = ref('');
const showForm = ref(false);
const editingId = ref(null);

const form = reactive({
  loaiChuongTrinhId: null,
  maKy: '',
  tenKy: '',
  thuTu: null,
});

const columns = [
  { key: 'id', label: 'ID' },
  { key: 'loaiChuongTrinhId', label: 'ID loại chương trình' },
  { key: 'maKy', label: 'Mã kỳ' },
  { key: 'tenKy', label: 'Tên kỳ' },
  { key: 'thuTu', label: 'Thứ tự' },
];

const formTitle = computed(() => editingId.value ? 'Cập nhật khung kỳ' : 'Thêm khung kỳ');

const filteredItems = computed(() => {
  return items.value
      .filter((item) => !loaiChuongTrinhId.value || String(item.loaiChuongTrinhId) === String(loaiChuongTrinhId.value))
      .filter((item) => matchKeyword(item, keyword.value, ['maKy', 'tenKy', 'loaiChuongTrinhId']));
});

const resetForm = () => {
  editingId.value = null;
  Object.assign(form, {
    loaiChuongTrinhId: loaiChuongTrinhId.value ? Number(loaiChuongTrinhId.value) : null,
    maKy: '',
    tenKy: '',
    thuTu: null,
  });
};

const fetchItems = async () => {
  loading.value = true;
  error.value = '';

  try {
    items.value = unwrapApiList(await getAllKhungKy());
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
    const payload = { ...form };

    if (editingId.value) {
      await updateKhungKy(editingId.value, payload);
    } else {
      await createKhungKy(payload);
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
  if (!window.confirm(`Xóa khung kỳ ID ${item.id}?`)) return;

  try {
    await deleteKhungKy(item.id);
    await fetchItems();
  } catch (err) {
    alert(getErrorMessage(err));
  }
};

// const goDetail = (item) => {
//   router.push({ name: 'dao-tao-khung-ky-detail', params: { id: item.id } });
// };

const goDetail = (item) => {
  router.push({
    name: 'dao-tao-khung-ky-detail',
    params: { khungKyId: item.id }
  })
}

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
