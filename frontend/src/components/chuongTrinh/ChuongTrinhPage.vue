<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>Chương trình đào tạo</h1>
        <p>Quản lý dữ liệu bảng chuong_trinh theo chương trình đào tạo.</p>
      </div>
      <button class="btn btn-secondary" type="button" @click="resetForm">Tạo mới</button>
    </div>

    <div v-if="error" class="alert alert-error">{{ error }}</div>
    <div v-if="success" class="alert alert-success">{{ success }}</div>

    <div class="card">
      <h2>{{ isEdit ? 'Cập nhật' : 'Thêm mới' }} chương trình đào tạo</h2>
      <form @submit.prevent="saveData">
        <div class="form-grid">
          <label class="field">
            <span>Ngành ID</span>
            <input v-model="form.nganhId" type="number" step="0.01" placeholder="Nhập ngành id" />
          </label>
          <label class="field">
            <span>Trình độ ID</span>
            <input v-model="form.trinhDoId" type="number" step="0.01" placeholder="Nhập trình độ id" />
          </label>
          <label class="field">
            <span>Loại chương trình ID</span>
            <input v-model="form.loaiChuongTrinhId" type="number" step="0.01" placeholder="Nhập loại chương trình id" />
          </label>
          <label class="field">
            <span>Mã chương trình</span>
            <input v-model="form.maChuongTrinh" type="text" placeholder="Nhập mã chương trình" />
          </label>
          <label class="field">
            <span>Tên chương trình</span>
            <input v-model="form.tenChuongTrinh" type="text" placeholder="Nhập tên chương trình" />
          </label>
          <label class="field field-full">
            <span>Đối tượng tuyển sinh</span>
            <textarea v-model="form.doiTuongTuyenSinh" rows="3" placeholder="Nhập đối tượng tuyển sinh"></textarea>
          </label>
          <label class="field">
            <span>Thời gian đào tạo</span>
            <input v-model="form.thoiGianDaoTao" type="text" placeholder="Nhập thời gian đào tạo" />
          </label>
        </div>
        <div class="actions">
          <button class="btn btn-primary" type="submit" :disabled="loading">
            {{ loading ? 'Đang lưu...' : (isEdit ? 'Cập nhật' : 'Thêm mới') }}
          </button>
          <button class="btn btn-light" type="button" @click="resetForm">Reset</button>
        </div>
      </form>
    </div>

    <div class="card">
      <div class="table-toolbar">
        <h2>Danh sách chương trình đào tạo</h2>
        <div class="toolbar-actions">
          <input v-model="keyword" class="search" placeholder="Tìm kiếm..." />
          <button class="btn btn-light" type="button" @click="fetchData">Tải lại</button>
        </div>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>id</th>
            <th>maChuongTrinh</th>
            <th>tenChuongTrinh</th>
            <th>nganhId</th>
            <th>trinhDoId</th>
            <th>loaiChuongTrinhId</th>
            <th>thoiGianDaoTao</th>
            <th>Thao tác</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in filteredItems" :key="item.id">
            <td>{{ formatValue(item.id) }}</td>
            <td>{{ formatValue(item.maChuongTrinh) }}</td>
            <td>{{ formatValue(item.tenChuongTrinh) }}</td>
            <td>{{ formatValue(item.nganhId) }}</td>
            <td>{{ formatValue(item.trinhDoId) }}</td>
            <td>{{ formatValue(item.loaiChuongTrinhId) }}</td>
            <td>{{ formatValue(item.thoiGianDaoTao) }}</td>
            <td class="row-actions">
              <button class="btn btn-small" type="button" @click="editItem(item)">Sửa</button>
              <button class="btn btn-small" type="button" @click="goVersion(item)">Phiên bản</button>
              <button class="btn btn-small btn-danger" type="button" @click="removeItem(item.id)">Xóa</button>
            </td>
          </tr>
          <tr v-if="!filteredItems.length">
            <td :colspan="8" class="empty">Không có dữ liệu</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getAllChuongTrinh } from '@/api/chuongTrinh/ApiRespone/ChuongTrinhController.js';
import { createChuongTrinh, updateChuongTrinh, deleteChuongTrinh } from '@/api/chuongTrinh/ApiRequest/ChuongTrinhController.js';

const route = useRoute();
const router = useRouter();
const items = ref([]);
const loading = ref(false);
const error = ref('');
const success = ref('');
const keyword = ref('');
const isEdit = ref(false);
const selectedId = ref(null);

const currentNganhId = computed(() => route.params.nganhId ? Number(route.params.nganhId) : null);

const emptyForm = () => ({
  nganhId: currentNganhId.value || '',
  trinhDoId: '',
  loaiChuongTrinhId: '',
  maChuongTrinh: '',
  tenChuongTrinh: '',
  doiTuongTuyenSinh: '',
  thoiGianDaoTao: ''
});

const form = ref(emptyForm());

const normalizeList = (response) => {
  const data = response?.data ?? response;
  if (Array.isArray(data)) return data;
  if (Array.isArray(data?.data)) return data.data;
  if (Array.isArray(data?.content)) return data.content;
  if (Array.isArray(data?.items)) return data.items;
  return [];
};

const cleanPayload = () => {
  const payload = { ...form.value };
  Object.keys(payload).forEach((key) => {
    if (payload[key] === '') payload[key] = null;
  });
  return payload;
};

const fetchData = async () => {
  loading.value = true;
  error.value = '';
  try {
    const response = await getAllChuongTrinh();
    items.value = normalizeList(response);
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Không tải được dữ liệu';
  } finally {
    loading.value = false;
  }
};

const filteredItems = computed(() => {
  const keywordValue = keyword.value.trim().toLowerCase();
  return items.value.filter((item) => {
    const matchedNganh = !currentNganhId.value || Number(item.nganhId) === currentNganhId.value;
    const matchedKeyword = !keywordValue || [item.maChuongTrinh, item.tenChuongTrinh, item.doiTuongTuyenSinh, item.thoiGianDaoTao]
      .some((value) => String(value || '').toLowerCase().includes(keywordValue));
    return matchedNganh && matchedKeyword;
  });
});

const saveData = async () => {
  loading.value = true;
  error.value = '';
  success.value = '';
  try {
    const payload = cleanPayload();
    if (isEdit.value && selectedId.value) {
      await updateChuongTrinh(selectedId.value, payload);
      success.value = 'Cập nhật thành công';
    } else {
      await createChuongTrinh(payload);
      success.value = 'Thêm mới thành công';
    }
    resetForm();
    await fetchData();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Lưu dữ liệu thất bại';
  } finally {
    loading.value = false;
  }
};

const editItem = (item) => {
  selectedId.value = item.id;
  isEdit.value = true;
  form.value = { ...emptyForm(), ...item };
  window.scrollTo({ top: 0, behavior: 'smooth' });
};

const removeItem = async (id) => {
  if (!id || !confirm('Xóa dữ liệu này?')) return;
  loading.value = true;
  error.value = '';
  success.value = '';
  try {
    await deleteChuongTrinh(id);
    success.value = 'Xóa thành công';
    await fetchData();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Xóa dữ liệu thất bại';
  } finally {
    loading.value = false;
  }
};



const goVersion = (item) => {
  router.push({ name: 'dao-tao-chuong-trinh-version', params: { chuongTrinhId: item.id } });
};

const resetForm = () => {
  form.value = emptyForm();
  selectedId.value = null;
  isEdit.value = false;
};

const formatValue = (value) => {
  if (value === true) return 'Có';
  if (value === false) return 'Không';
  if (value === null || value === undefined || value === '') return '-';
  return value;
};

onMounted(fetchData);
</script>

<style scoped>
.page { padding: 24px; }
.page-header { display: flex; align-items: flex-start; justify-content: space-between; gap: 16px; margin-bottom: 18px; }
h1 { margin: 0; font-size: 26px; font-weight: 700; color: #172033; }
p { margin: 6px 0 0; color: #667085; }
.card { background: #fff; border: 1px solid #e5e7eb; border-radius: 14px; padding: 18px; margin-bottom: 18px; box-shadow: 0 8px 24px rgba(15, 23, 42, 0.04); }
h2 { margin: 0 0 14px; font-size: 18px; color: #172033; }
.form-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 14px; }
.field { display: flex; flex-direction: column; gap: 6px; font-size: 14px; color: #344054; }
.field-full { grid-column: span 3; }
.field-check { justify-content: center; flex-direction: row; align-items: center; margin-top: 24px; }
input, select, textarea { border: 1px solid #d0d5dd; border-radius: 10px; padding: 10px 12px; outline: none; font-size: 14px; }
textarea { resize: vertical; }
.actions, .toolbar-actions, .row-actions { display: flex; gap: 8px; align-items: center; }
.actions { margin-top: 16px; }
.table-toolbar { display: flex; align-items: center; justify-content: space-between; gap: 12px; margin-bottom: 12px; }
.search { min-width: 260px; }
.table-wrap { overflow-x: auto; }
table { width: 100%; border-collapse: collapse; font-size: 14px; }
th, td { border-bottom: 1px solid #e5e7eb; padding: 10px; text-align: left; vertical-align: top; }
th { background: #f8fafc; color: #344054; font-weight: 700; white-space: nowrap; }
.empty { text-align: center; color: #667085; padding: 24px; }
.btn { border: 0; border-radius: 10px; padding: 10px 14px; cursor: pointer; font-weight: 600; }
.btn-primary { background: #2563eb; color: #fff; }
.btn-secondary { background: #0f172a; color: #fff; }
.btn-light { background: #f2f4f7; color: #344054; }
.btn-danger { background: #fee2e2; color: #b42318; }
.btn-small { padding: 7px 10px; }
.alert { padding: 12px 14px; border-radius: 10px; margin-bottom: 12px; }
.alert-error { background: #fee2e2; color: #991b1b; }
.alert-success { background: #dcfce7; color: #166534; }
@media (max-width: 900px) {
  .form-grid { grid-template-columns: 1fr; }
  .field-full { grid-column: span 1; }
  .page-header, .table-toolbar { flex-direction: column; align-items: stretch; }
  .search { min-width: unset; width: 100%; }
}
</style>
