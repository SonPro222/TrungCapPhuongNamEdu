<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>Version chương trình</h1>
        <p>Quản lý dữ liệu bảng chuong_trinh_version theo chương trình đào tạo.</p>
      </div>
      <button class="btn btn-secondary" type="button" @click="resetForm">Tạo mới</button>
    </div>

    <div v-if="error" class="alert alert-error">{{ error }}</div>
    <div v-if="success" class="alert alert-success">{{ success }}</div>

    <div class="card">
      <h2>{{ isEdit ? 'Cập nhật' : 'Thêm mới' }} version chương trình</h2>
      <form @submit.prevent="saveData">
        <div class="form-grid">
          <label class="field">
            <span>Chương trình ID</span>
            <input v-model="form.chuongTrinhId" type="number" step="0.01" placeholder="Nhập chương trình id" />
          </label>
          <label class="field">
            <span>Mã version</span>
            <input v-model="form.maVersion" type="text" placeholder="Nhập mã version" />
          </label>
          <label class="field">
            <span>Tên version</span>
            <input v-model="form.tenVersion" type="text" placeholder="Nhập tên version" />
          </label>
          <label class="field">
            <span>Ngày áp dụng</span>
            <input v-model="form.ngayApDung" type="date" placeholder="Nhập ngày áp dụng" />
          </label>
          <label class="field">
            <span>Ngày hết hiệu lực</span>
            <input v-model="form.ngayHetHieuLuc" type="date" placeholder="Nhập ngày hết hiệu lực" />
          </label>
          <label class="field">
            <span>Số quyết định</span>
            <input v-model="form.soQuyetDinh" type="text" placeholder="Nhập số quyết định" />
          </label>
          <label class="field">
            <span>Ngày quyết định</span>
            <input v-model="form.ngayQuyetDinh" type="date" placeholder="Nhập ngày quyết định" />
          </label>
          <label class="field">
            <span>Người ký</span>
            <input v-model="form.nguoiKy" type="text" placeholder="Nhập người ký" />
          </label>
          <label class="field">
            <span>Cơ quan ban hành</span>
            <input v-model="form.coQuanBanHanh" type="text" placeholder="Nhập cơ quan ban hành" />
          </label>
          <label class="field">
            <span>File quyết định</span>
            <input v-model="form.fileQuyetDinh" type="text" placeholder="Nhập file quyết định" />
          </label>
          <label class="field">
            <span>Tổng tín chỉ</span>
            <input v-model="form.tongTinChi" type="number" step="0.01" placeholder="Nhập tổng tín chỉ" />
          </label>
          <label class="field">
            <span>Tổng số giờ</span>
            <input v-model="form.tongSoGio" type="number" step="0.01" placeholder="Nhập tổng số giờ" />
          </label>
          <label class="field">
            <span>Tổng giờ lý thuyết</span>
            <input v-model="form.tongGioLyThuyet" type="number" step="0.01" placeholder="Nhập tổng giờ lý thuyết" />
          </label>
          <label class="field">
            <span>Tổng giờ thực hành</span>
            <input v-model="form.tongGioThucHanh" type="number" step="0.01" placeholder="Nhập tổng giờ thực hành" />
          </label>
          <label class="field">
            <span>Tổng giờ kiểm tra</span>
            <input v-model="form.tongGioKiemTra" type="number" step="0.01" placeholder="Nhập tổng giờ kiểm tra" />
          </label>
          <label class="field field-check">
            <input v-model="form.laHienHanh" type="checkbox" />
            <span>Hiện hành</span>
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
        <h2>Danh sách version chương trình</h2>
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
            <th>chuongTrinhId</th>
            <th>maVersion</th>
            <th>tenVersion</th>
            <th>soQuyetDinh</th>
            <th>ngayQuyetDinh</th>
            <th>laHienHanh</th>
            <th>Thao tác</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in filteredItems" :key="item.id">
            <td>{{ formatValue(item.id) }}</td>
            <td>{{ formatValue(item.chuongTrinhId) }}</td>
            <td>{{ formatValue(item.maVersion) }}</td>
            <td>{{ formatValue(item.tenVersion) }}</td>
            <td>{{ formatValue(item.soQuyetDinh) }}</td>
            <td>{{ formatValue(item.ngayQuyetDinh) }}</td>
            <td>{{ formatValue(item.laHienHanh) }}</td>
            <td class="row-actions">
              <button class="btn btn-small" type="button" @click="editItem(item)">Sửa</button>
                            <button class="btn btn-small" type="button" @click="goSyllabusChuongTrinh(item)">Syllabus CT</button>
              <button class="btn btn-small" type="button" @click="goMucTieu(item)">Mục tiêu</button>
              <button class="btn btn-small" type="button" @click="goNangLuc(item)">Năng lực</button>
              <button class="btn btn-small" type="button" @click="goNhomKienThuc(item)">Nhóm KT</button>
              <button class="btn btn-small" type="button" @click="goChuongTrinhMon(item)">Môn CT</button>
              <button class="btn btn-small" type="button" @click="goLopHanhChinh(item)">Lớp HC</button>
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
import { getAllChuongTrinhVersion } from '@/api/chuongTrinh/ApiRespone/ChuongTrinhVersionController.js';
import { createChuongTrinhVersion, updateChuongTrinhVersion, deleteChuongTrinhVersion } from '@/api/chuongTrinh/ApiRequest/ChuongTrinhVersionController.js';

const route = useRoute();
const router = useRouter();
const items = ref([]);
const loading = ref(false);
const error = ref('');
const success = ref('');
const keyword = ref('');
const isEdit = ref(false);
const selectedId = ref(null);

const currentParentId = computed(() => route.params.chuongTrinhId ? Number(route.params.chuongTrinhId) : null);

const emptyForm = () => ({
  chuongTrinhId: currentParentId.value || '',
  maVersion: '',
  tenVersion: '',
  ngayApDung: '',
  ngayHetHieuLuc: '',
  soQuyetDinh: '',
  ngayQuyetDinh: '',
  nguoiKy: '',
  coQuanBanHanh: '',
  fileQuyetDinh: '',
  tongTinChi: '',
  tongSoGio: '',
  tongGioLyThuyet: '',
  tongGioThucHanh: '',
  tongGioKiemTra: '',
  laHienHanh: false
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
    const response = await getAllChuongTrinhVersion();
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
    const matchedParent = !currentParentId.value || Number(item.chuongTrinhId) === currentParentId.value;
    const matchedKeyword = !keywordValue || [item.maVersion, item.tenVersion, item.soQuyetDinh, item.nguoiKy]
      .some((value) => String(value || '').toLowerCase().includes(keywordValue));
    return matchedParent && matchedKeyword;
  });
});

const saveData = async () => {
  loading.value = true;
  error.value = '';
  success.value = '';
  try {
    const payload = cleanPayload();
    if (isEdit.value && selectedId.value) {
      await updateChuongTrinhVersion(selectedId.value, payload);
      success.value = 'Cập nhật thành công';
    } else {
      await createChuongTrinhVersion(payload);
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
    await deleteChuongTrinhVersion(id);
    success.value = 'Xóa thành công';
    await fetchData();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Xóa dữ liệu thất bại';
  } finally {
    loading.value = false;
  }
};


const goSyllabusChuongTrinh = (item) => {
  router.push({ name: 'dao-tao-version-syllabus-chuong-trinh', params: { chuongTrinhVersionId: item.id } });
};

const goMucTieu = (item) => {
  router.push({ name: 'dao-tao-version-muc-tieu', params: { chuongTrinhVersionId: item.id } });
};

const goNangLuc = (item) => {
  router.push({ name: 'dao-tao-version-nang-luc', params: { chuongTrinhVersionId: item.id } });
};

const goNhomKienThuc = (item) => {
  router.push({ name: 'dao-tao-version-nhom-kien-thuc', params: { chuongTrinhVersionId: item.id } });
};

const goChuongTrinhMon = (item) => {
  router.push({ name: 'dao-tao-version-chuong-trinh-mon', params: { chuongTrinhVersionId: item.id } });
};

const goLopHanhChinh = (item) => {
  router.push({ name: 'dao-tao-version-lop-hanh-chinh', params: { chuongTrinhVersionId: item.id } });
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
