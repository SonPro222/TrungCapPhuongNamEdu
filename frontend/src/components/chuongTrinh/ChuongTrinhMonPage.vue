<template>
  <section class="page">
    <div class="page-header">
      <div>
        <h1>Môn trong chương trình</h1>
        <p>Quản lý dữ liệu bảng chuong_trinh_mon theo chương trình đào tạo.</p>
      </div>
      <button class="btn btn-secondary" type="button" @click="resetForm">Tạo mới</button>
    </div>

    <div v-if="error" class="alert alert-error">{{ error }}</div>
    <div v-if="success" class="alert alert-success">{{ success }}</div>

    <div class="card">
      <h2>{{ isEdit ? 'Cập nhật' : 'Thêm mới' }} môn trong chương trình</h2>
      <form @submit.prevent="saveData">
        <div class="form-grid">
          <label class="field">
            <span>Version chương trình ID</span>
            <input v-model="form.chuongTrinhVersionId" type="number" step="0.01" placeholder="Nhập version chương trình id" />
          </label>
          <label class="field">
            <span>Môn học ID</span>
            <input v-model="form.monHocId" type="number" step="0.01" placeholder="Nhập môn học id" />
          </label>
          <label class="field">
            <span>Mã môn trong CT</span>
            <input v-model="form.maMonTrongCt" type="text" placeholder="Nhập mã môn trong ct" />
          </label>
          <label class="field">
            <span>Khung kỳ ID</span>
            <input v-model="form.khungKyId" type="number" step="0.01" placeholder="Nhập khung kỳ id" />
          </label>
          <label class="field">
            <span>Nhóm kiến thức ID</span>
            <input v-model="form.nhomKienThucId" type="number" step="0.01" placeholder="Nhập nhóm kiến thức id" />
          </label>
          <label class="field">
            <span>Loại</span>
            <select v-model="form.loai">
              <option value="">-- Chọn --</option>
              <option value="bat_buoc">bat_buoc</option>
              <option value="tu_chon">tu_chon</option>
            </select>
          </label>
          <label class="field">
            <span>Loại học phần</span>
            <select v-model="form.loaiHocPhan">
              <option value="">-- Chọn --</option>
              <option value="mon_hoc">mon_hoc</option>
              <option value="mo_dun">mo_dun</option>
            </select>
          </label>
          <label class="field field-check">
            <input v-model="form.batBuoc" type="checkbox" />
            <span>Bắt buộc</span>
          </label>
          <label class="field field-check">
            <input v-model="form.laMonDieuKien" type="checkbox" />
            <span>Là môn điều kiện</span>
          </label>
          <label class="field">
            <span>Thứ tự</span>
            <input v-model="form.thuTu" type="number" step="0.01" placeholder="Nhập thứ tự" />
          </label>
          <label class="field">
            <span>Số tín chỉ</span>
            <input v-model="form.soTinChi" type="number" step="0.01" placeholder="Nhập số tín chỉ" />
          </label>
          <label class="field">
            <span>Tổng giờ</span>
            <input v-model="form.tongGio" type="number" step="0.01" placeholder="Nhập tổng giờ" />
          </label>
          <label class="field">
            <span>Giờ lý thuyết</span>
            <input v-model="form.gioLyThuyet" type="number" step="0.01" placeholder="Nhập giờ lý thuyết" />
          </label>
          <label class="field">
            <span>Giờ thực hành</span>
            <input v-model="form.gioThucHanh" type="number" step="0.01" placeholder="Nhập giờ thực hành" />
          </label>
          <label class="field">
            <span>Giờ kiểm tra</span>
            <input v-model="form.gioKiemTra" type="number" step="0.01" placeholder="Nhập giờ kiểm tra" />
          </label>
          <label class="field field-full">
            <span>Ghi chú</span>
            <textarea v-model="form.ghiChu" rows="3" placeholder="Nhập ghi chú"></textarea>
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
        <h2>Danh sách môn trong chương trình</h2>
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
            <th>chuongTrinhVersionId</th>
            <th>monHocId</th>
            <th>maMonTrongCt</th>
            <th>loai</th>
            <th>loaiHocPhan</th>
            <th>soTinChi</th>
            <th>tongGio</th>
            <th>Thao tác</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="item in filteredItems" :key="item.id">
            <td>{{ formatValue(item.id) }}</td>
            <td>{{ formatValue(item.chuongTrinhVersionId) }}</td>
            <td>{{ formatValue(item.monHocId) }}</td>
            <td>{{ formatValue(item.maMonTrongCt) }}</td>
            <td>{{ formatValue(item.loai) }}</td>
            <td>{{ formatValue(item.loaiHocPhan) }}</td>
            <td>{{ formatValue(item.soTinChi) }}</td>
            <td>{{ formatValue(item.tongGio) }}</td>
            <td class="row-actions">
              <button class="btn btn-small" type="button" @click="editItem(item)">Sửa</button>
                            <button class="btn btn-small" type="button" @click="goSyllabusMon(item)">Syllabus môn</button>
              <button class="btn btn-small" type="button" @click="goMonTienQuyet(item)">Tiên quyết</button>
              <button class="btn btn-small" type="button" @click="goDieuKien(item)">Điều kiện</button>
              <button class="btn btn-small" type="button" @click="goQuyDoiDiem(item)">Quy đổi điểm</button>
              <button class="btn btn-small btn-danger" type="button" @click="removeItem(item.id)">Xóa</button>
            </td>
          </tr>
          <tr v-if="!filteredItems.length">
            <td :colspan="9" class="empty">Không có dữ liệu</td>
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
import { getAllChuongTrinhMon } from '@/api/chuongTrinh/ApiRespone/ChuongTrinhMonController.js';
import { createChuongTrinhMon, updateChuongTrinhMon, deleteChuongTrinhMon } from '@/api/chuongTrinh/ApiRequest/ChuongTrinhMonController.js';

const route = useRoute();
const router = useRouter();
const items = ref([]);
const loading = ref(false);
const error = ref('');
const success = ref('');
const keyword = ref('');
const isEdit = ref(false);
const selectedId = ref(null);

const currentParentId = computed(() => route.params.chuongTrinhVersionId ? Number(route.params.chuongTrinhVersionId) : null);

const emptyForm = () => ({
  chuongTrinhVersionId: currentParentId.value || '',
  monHocId: '',
  maMonTrongCt: '',
  khungKyId: '',
  nhomKienThucId: '',
  loai: '',
  loaiHocPhan: '',
  batBuoc: false,
  laMonDieuKien: false,
  thuTu: '',
  soTinChi: '',
  tongGio: '',
  gioLyThuyet: '',
  gioThucHanh: '',
  gioKiemTra: '',
  ghiChu: ''
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
    const response = await getAllChuongTrinhMon();
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
    const matchedParent = !currentParentId.value || Number(item.chuongTrinhVersionId) === currentParentId.value;
    const matchedKeyword = !keywordValue || [item.maMonTrongCt, item.loai, item.loaiHocPhan, item.ghiChu]
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
      await updateChuongTrinhMon(selectedId.value, payload);
      success.value = 'Cập nhật thành công';
    } else {
      await createChuongTrinhMon(payload);
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
    await deleteChuongTrinhMon(id);
    success.value = 'Xóa thành công';
    await fetchData();
  } catch (err) {
    error.value = err?.response?.data?.message || err?.message || 'Xóa dữ liệu thất bại';
  } finally {
    loading.value = false;
  }
};


const goSyllabusMon = (item) => {
  router.push({ name: 'dao-tao-chuong-trinh-mon-syllabus', params: { chuongTrinhMonId: item.id } });
};

const goMonTienQuyet = (item) => {
  router.push({ name: 'dao-tao-chuong-trinh-mon-tien-quyet', params: { chuongTrinhMonId: item.id } });
};

const goDieuKien = (item) => {
  router.push({ name: 'dao-tao-chuong-trinh-mon-dieu-kien', params: { chuongTrinhMonId: item.id } });
};

const goQuyDoiDiem = (item) => {
  router.push({ name: 'dao-tao-chuong-trinh-mon-quy-doi-diem', params: { chuongTrinhMonId: item.id } });
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
