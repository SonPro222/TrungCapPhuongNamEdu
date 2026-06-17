<!--<template>-->
<!--  <section class="page">-->
<!--    <header class="page-head">-->
<!--      <div>-->
<!--        <h1>Nhật ký hệ thống</h1>-->
<!--        <p>Theo dõi thao tác tạo, cập nhật, xóa trên toàn hệ thống theo từng tầng nghiệp vụ.</p>-->
<!--      </div>-->

<!--      <button type="button" class="btn" @click="taiDuLieu">-->
<!--        Tải lại-->
<!--      </button>-->
<!--    </header>-->

<!--    <section class="filter-card">-->
<!--      <label>-->
<!--        <span>Tìm kiếm</span>-->
<!--        <input-->
<!--            v-model.trim="keyword"-->
<!--            type="text"-->
<!--            placeholder="Tài khoản, người thao tác, đường dẫn, IP, nội dung"-->
<!--        />-->
<!--      </label>-->

<!--      <label>-->
<!--        <span>Module nghiệp vụ</span>-->
<!--        <select v-model="moduleNghiepVu">-->
<!--          <option value="">Tất cả</option>-->
<!--          <option value="SINH_VIEN">Sinh viên</option>-->
<!--          <option value="DAO_TAO">Đào tạo</option>-->
<!--          <option value="CHUONG_TRINH">Chương trình</option>-->
<!--          <option value="GIANG_DAY">Giảng dạy</option>-->
<!--          <option value="DIEM">Điểm</option>-->
<!--          <option value="HOC_PHI">Học phí</option>-->
<!--          <option value="THI">Thi</option>-->
<!--          <option value="LMS">LMS</option>-->
<!--          <option value="HE_THONG">Hệ thống</option>-->
<!--          <option value="KHAC">Khác</option>-->
<!--        </select>-->
<!--      </label>-->

<!--      <label>-->
<!--        <span>Hành động</span>-->
<!--        <select v-model="hanhDong">-->
<!--          <option value="">Tất cả</option>-->
<!--          <option value="TAO_MOI">Tạo mới</option>-->
<!--          <option value="CAP_NHAT">Cập nhật</option>-->
<!--          <option value="XOA">Xóa</option>-->
<!--        </select>-->
<!--      </label>-->

<!--      <label>-->
<!--        <span>Kết quả</span>-->
<!--        <select v-model="ketQua">-->
<!--          <option value="">Tất cả</option>-->
<!--          <option value="THANH_CONG">Thành công</option>-->
<!--          <option value="THAT_BAI">Thất bại</option>-->
<!--        </select>-->
<!--      </label>-->
<!--      <label>-->
<!--        <span>Từ ngày</span>-->
<!--        <input v-model="tuNgay" type="date" />-->
<!--      </label>-->

<!--      <label>-->
<!--        <span>Đến ngày</span>-->
<!--        <input v-model="denNgay" type="date" />-->
<!--      </label>-->

<!--      <label>-->
<!--        <span>Tài khoản / Gmail</span>-->
<!--        <input-->
<!--            v-model.trim="taiKhoanEmail"-->
<!--            type="text"-->
<!--            placeholder="admin@gmail.com"-->
<!--        />-->
<!--      </label>-->

<!--      <label>-->
<!--        <span>Bản ghi ID</span>-->
<!--        <input-->
<!--            v-model.trim="banGhiId"-->
<!--            type="number"-->
<!--            placeholder="ID bản ghi"-->
<!--        />-->
<!--      </label>-->
<!--      <div class="filter-actions">-->
<!--        <button type="button" class="btn primary" @click="locClient">-->
<!--          Lọc-->
<!--        </button>-->

<!--        <button type="button" class="btn" @click="xoaLoc">-->
<!--          Xóa lọc-->
<!--        </button>-->
<!--      </div>-->
<!--    </section>-->

<!--    <div class="summary-grid">-->
<!--      <div>-->
<!--        <span>Tổng thao tác</span>-->
<!--        <strong>{{ danhSach.length }}</strong>-->
<!--      </div>-->
<!--      <div>-->
<!--        <span>Thành công</span>-->
<!--        <strong>{{ tongThanhCong }}</strong>-->
<!--      </div>-->
<!--      <div>-->
<!--        <span>Thất bại</span>-->
<!--        <strong>{{ tongThatBai }}</strong>-->
<!--      </div>-->
<!--      <div>-->
<!--        <span>Đang hiển thị</span>-->
<!--        <strong>{{ danhSachHienThi.length }}</strong>-->
<!--      </div>-->
<!--    </div>-->
<!--    <div class="pagination-bar">-->
<!--      <button-->
<!--          type="button"-->
<!--          class="btn"-->
<!--          :disabled="trangHienTai <= 0 || dangTai"-->
<!--          @click="doiTrang(trangHienTai - 1)"-->
<!--      >-->
<!--        Trang trước-->
<!--      </button>-->

<!--      <span>-->
<!--    Trang {{ trangHienTai + 1 }} / {{ tongTrang || 1 }}-->
<!--    - Tổng {{ tongBanGhi }} bản ghi-->
<!--  </span>-->

<!--      <button-->
<!--          type="button"-->
<!--          class="btn"-->
<!--          :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"-->
<!--          @click="doiTrang(trangHienTai + 1)"-->
<!--      >-->
<!--        Trang sau-->
<!--      </button>-->
<!--    </div>-->
<!--    <div v-if="loi" class="alert error">{{ loi }}</div>-->

<!--    <div class="table-wrap">-->
<!--      <table>-->
<!--        <thead>-->
<!--        <tr>-->
<!--          <th>ID</th>-->
<!--          <th>Thời gian</th>-->
<!--          <th>Tài khoản</th>-->
<!--          <th>Đối tượng thao tác</th>-->
<!--          <th>Module</th>-->
<!--          <th>Chức năng</th>-->
<!--          <th>Hành động</th>-->
<!--          <th>Kết quả</th>-->
<!--          <th>Phương thức</th>-->
<!--          <th>Đường dẫn</th>-->
<!--          <th>Bảng tác động</th>-->
<!--          <th>Bản ghi ID</th>-->
<!--          <th>IP</th>-->
<!--          <th>Thiết bị / trình duyệt</th>-->
<!--          <th>Mô tả</th>-->
<!--          <th>Nội dung cũ</th>-->
<!--          <th>Nội dung mới</th>-->
<!--        </tr>-->
<!--        </thead>-->

<!--        <tbody>-->
<!--        <tr v-if="dangTai">-->
<!--          <td colspan="17">Đang tải dữ liệu...</td>-->
<!--        </tr>-->

<!--        <tr v-else-if="!danhSachHienThi.length">-->
<!--          <td colspan="17">Chưa có dữ liệu</td>-->
<!--        </tr>-->

<!--        <tr v-for="item in danhSachHienThi" v-else :key="item.id">-->
<!--          <td>{{ item.id }}</td>-->
<!--          <td>{{ item.createdAt || item.thoiGian || '-' }}</td>-->
<!--          <td>-->
<!--            <div class="object-cell">-->
<!--              <strong>{{ item.taiKhoanEmail || item.taiKhoanId || '-' }}</strong>-->
<!--              <span>ID: {{ item.taiKhoanId || '-' }}</span>-->
<!--              <span>{{ item.tenTaiKhoan || '-' }}</span>-->
<!--              <span>{{ hienThiLoaiTaiKhoan(item.loaiTaiKhoan) }}</span>-->
<!--            </div>-->
<!--          </td>-->
<!--          <td>-->
<!--            <div class="object-cell">-->
<!--              <strong>{{ item.doiTuongTen || '-' }}</strong>-->
<!--              <span>{{ item.doiTuongLoai || '-' }} {{ item.doiTuongId ? '#' + item.doiTuongId : '' }}</span>-->
<!--            </div>-->
<!--          </td>-->
<!--          <td>{{ item.moduleNghiepVu || '-' }}</td>-->
<!--          <td>{{ item.chucNang || '-' }}</td>-->
<!--          <td>-->
<!--            <span class="action" :class="item.hanhDong">-->
<!--              {{ hienThiHanhDong(item.hanhDong) }}-->
<!--            </span>-->
<!--          </td>-->
<!--          <td>-->
<!--            <span class="status" :class="item.ketQua === 'THANH_CONG' ? 'success' : 'error'">-->
<!--              {{ hienThiKetQua(item.ketQua) }}-->
<!--            </span>-->
<!--          </td>-->
<!--          <td>{{ item.phuongThuc || '-' }}</td>-->
<!--          <td class="path-cell">{{ item.duongDan || '-' }}</td>-->
<!--          <td>{{ item.bangTacDong || '-' }}</td>-->
<!--          <td>{{ item.banGhiId || '-' }}</td>-->
<!--          <td>{{ item.ipAddress || '-' }}</td>-->
<!--          <td class="user-agent">{{ item.userAgent || '-' }}</td>-->
<!--          <td class="desc-cell">{{ item.moTa || '-' }}</td>-->
<!--          <td><pre>{{ item.noiDungCu || '-' }}</pre></td>-->
<!--          <td><pre>{{ item.noiDungMoi || '-' }}</pre></td>-->
<!--        </tr>-->
<!--        </tbody>-->
<!--      </table>-->
<!--    </div>-->
<!--  </section>-->
<!--</template>-->

<!--<script setup>-->
<!--import { computed, onMounted, ref } from 'vue'-->
<!--import { heThongApi } from '../api/heThongApi'-->

<!--const danhSach = ref([])-->
<!--const keyword = ref('')-->
<!--const moduleNghiepVu = ref('')-->
<!--const hanhDong = ref('')-->
<!--const ketQua = ref('')-->
<!--const tuNgay = ref('')-->
<!--const denNgay = ref('')-->
<!--const taiKhoanEmail = ref('')-->
<!--const banGhiId = ref('')-->
<!--const dangTai = ref(false)-->
<!--const loi = ref('')-->
<!--const trangHienTai = ref(0)-->
<!--const tongTrang = ref(0)-->
<!--const tongBanGhi = ref(0)-->
<!--const kichThuocTrang = 100-->
<!--const danhSachHienThi = computed(() => {-->
<!--  const key = keyword.value.trim().toLowerCase()-->
<!--  const module = moduleNghiepVu.value.trim().toLowerCase()-->
<!--  const hanh = hanhDong.value.trim().toLowerCase()-->
<!--  const kq = ketQua.value.trim().toLowerCase()-->

<!--  return danhSach.value.filter((item) => {-->
<!--    const json = JSON.stringify(item || {}).toLowerCase()-->

<!--    const dungKeyword = !key || json.includes(key)-->

<!--    const dungModule = !module-->
<!--        || String(item.moduleNghiepVu || '').toLowerCase() === module-->

<!--    const dungHanhDong = !hanh-->
<!--        || String(item.hanhDong || '').toLowerCase() === hanh-->

<!--    const dungKetQua = !kq-->
<!--        || String(item.ketQua || '').toLowerCase() === kq-->

<!--    return dungKeyword && dungModule && dungHanhDong && dungKetQua-->
<!--  })-->
<!--})-->

<!--const tongThanhCong = computed(() => {-->
<!--  return danhSach.value.filter(item => item.ketQua === 'THANH_CONG').length-->
<!--})-->

<!--const tongThatBai = computed(() => {-->
<!--  return danhSach.value.filter(item => item.ketQua === 'THAT_BAI').length-->
<!--})-->

<!--onMounted(() => {-->
<!--  taiDuLieu()-->
<!--})-->

<!--async function taiDuLieu() {-->
<!--  dangTai.value = true-->
<!--  loi.value = ''-->

<!--  try {-->
<!--    const params = {-->
<!--      page: trangHienTai.value,-->
<!--      size: kichThuocTrang,-->
<!--      sort: 'id,desc'-->
<!--    }-->
<!--    if (keyword.value) params.keyword = keyword.value-->
<!--    if (moduleNghiepVu.value) params.moduleNghiepVu = moduleNghiepVu.value-->
<!--    if (hanhDong.value) params.hanhDong = hanhDong.value-->
<!--    if (ketQua.value) params.ketQua = ketQua.value-->
<!--    if (tuNgay.value) params.tuNgay = tuNgay.value-->
<!--    if (denNgay.value) params.denNgay = denNgay.value-->
<!--    if (taiKhoanEmail.value) params.taiKhoanEmail = taiKhoanEmail.value-->
<!--    if (banGhiId.value) params.banGhiId = banGhiId.value-->

<!--    const response = await heThongApi.nhatKyHeThong.getAll(params)-->
<!--    const pageData = layPage(response)-->

<!--    danhSach.value = pageData.content-->
<!--    trangHienTai.value = pageData.number-->
<!--    tongTrang.value = pageData.totalPages-->
<!--    tongBanGhi.value = pageData.totalElements-->
<!--  } catch (error) {-->
<!--    loi.value = error?.response?.data?.message || error?.message || 'Không tải được nhật ký hệ thống'-->
<!--  } finally {-->
<!--    dangTai.value = false-->
<!--  }-->
<!--}-->
<!--function layPage(result) {-->
<!--  const data = result?.data?.data || result?.data || result-->

<!--  if (Array.isArray(data)) {-->
<!--    return {-->
<!--      content: data,-->
<!--      number: 0,-->
<!--      totalPages: 1,-->
<!--      totalElements: data.length-->
<!--    }-->
<!--  }-->

<!--  if (Array.isArray(data?.content)) {-->
<!--    return {-->
<!--      content: data.content,-->
<!--      number: data.number || 0,-->
<!--      totalPages: data.totalPages || 1,-->
<!--      totalElements: data.totalElements || data.content.length-->
<!--    }-->
<!--  }-->

<!--  return {-->
<!--    content: [],-->
<!--    number: 0,-->
<!--    totalPages: 0,-->
<!--    totalElements: 0-->
<!--  }-->
<!--}-->
<!--function doiTrang(page) {-->
<!--  if (page < 0) return-->
<!--  if (tongTrang.value && page >= tongTrang.value) return-->

<!--  trangHienTai.value = page-->
<!--  taiDuLieu()-->
<!--}-->
<!--function layDanhSach(result) {-->
<!--  if (Array.isArray(result)) return result-->
<!--  if (Array.isArray(result?.content)) return result.content-->
<!--  if (Array.isArray(result?.data?.content)) return result.data.content-->
<!--  if (Array.isArray(result?.data)) return result.data-->
<!--  return []-->
<!--}-->

<!--function hienThiLoaiTaiKhoan(value) {-->
<!--  if (!value) return '-'-->

<!--  const text = String(value)-->

<!--  if (text === 'nhan_vien') return 'Nhân viên'-->
<!--  if (text === 'sinh_vien') return 'Sinh viên'-->
<!--  if (text === 'giao_vien') return 'Giáo viên'-->
<!--  if (text === 'admin') return 'Admin'-->

<!--  return text-->
<!--}-->

<!--function hienThiHanhDong(value) {-->
<!--  if (value === 'TAO_MOI') return 'Tạo mới'-->
<!--  if (value === 'CAP_NHAT') return 'Cập nhật'-->
<!--  if (value === 'XOA') return 'Xóa'-->
<!--  return value || '-'-->
<!--}-->

<!--function hienThiKetQua(value) {-->
<!--  if (value === 'THANH_CONG') return 'Thành công'-->
<!--  if (value === 'THAT_BAI') return 'Thất bại'-->
<!--  return value || '-'-->
<!--}-->

<!--function locClient() {-->
<!--  trangHienTai.value = 0-->
<!--  taiDuLieu()-->
<!--}-->
<!--function xoaLoc() {-->
<!--  keyword.value = ''-->
<!--  moduleNghiepVu.value = ''-->
<!--  hanhDong.value = ''-->
<!--  ketQua.value = ''-->
<!--  tuNgay.value = ''-->
<!--  denNgay.value = ''-->
<!--  taiKhoanEmail.value = ''-->
<!--  banGhiId.value = ''-->
<!--  trangHienTai.value = 0-->
<!--  taiDuLieu()-->
<!--}-->
<!--</script>-->

<!--<style scoped>-->
<!--/* ================= BỐ CỤC CHUNG ================= */-->
<!--.page {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  gap: 16px;-->
<!--}-->

<!--.page-head {-->
<!--  display: flex;-->
<!--  justify-content: space-between;-->
<!--  align-items: center;-->
<!--  gap: 16px;-->
<!--  flex-wrap: wrap;-->
<!--}-->

<!--.page-head h1 {-->
<!--  margin: 0;-->
<!--  font-size: 20px;-->
<!--  color: #111827;-->
<!--}-->

<!--.page-head p {-->
<!--  margin: 6px 0 0;-->
<!--  color: #64748b;-->
<!--  font-size: 14px;-->
<!--}-->

<!--/* ================= BỘ LỌC (FILTER) ================= */-->
<!--.filter-card {-->
<!--  display: grid;-->
<!--  /* Dùng auto-fit để các ô tự động dàn đều và rớt dòng mượt mà */-->
<!--  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));-->
<!--  gap: 16px;-->
<!--  padding: 20px;-->
<!--  border: 1px solid #e2e8f0;-->
<!--  border-radius: 12px;-->
<!--  background: #fff;-->
<!--  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);-->
<!--}-->

<!--.filter-card label {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  gap: 8px;-->
<!--}-->

<!--.filter-card span {-->
<!--  font-size: 13px;-->
<!--  font-weight: 600;-->
<!--  color: #475467;-->
<!--}-->

<!--input,-->
<!--select {-->
<!--  border: 1px solid #d0d5dd;-->
<!--  border-radius: 8px;-->
<!--  padding: 10px 12px;-->
<!--  font-size: 14px;-->
<!--  color: #111827;-->
<!--  background: #fff;-->
<!--  outline: none;-->
<!--  transition: all 0.2s ease;-->
<!--  box-sizing: border-box;-->
<!--  width: 100%;-->
<!--}-->

<!--input:hover,-->
<!--select:hover {-->
<!--  border-color: #94a3b8;-->
<!--}-->

<!--input:focus,-->
<!--select:focus {-->
<!--  border-color: #3b82f6;-->
<!--  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.15);-->
<!--}-->

<!--.filter-actions {-->
<!--  display: flex;-->
<!--  align-items: flex-end;-->
<!--  gap: 10px;-->
<!--  /* Ép nút bấm nằm gọn trong 1 cột hoặc kéo dài tùy màn hình */-->
<!--  min-width: 180px;-->
<!--}-->

<!--/* ================= BUTTONS ================= */-->
<!--.btn {-->
<!--  border: 1px solid #cbd5e1;-->
<!--  background: #fff;-->
<!--  color: #475467;-->
<!--  border-radius: 8px;-->
<!--  padding: 8px 14px;-->
<!--  cursor: pointer;-->
<!--  font-weight: 600;-->
<!--  font-size: 14px;-->
<!--  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);-->
<!--  display: inline-flex;-->
<!--  align-items: center;-->
<!--  justify-content: center;-->
<!--}-->

<!--.btn:hover:not(:disabled) {-->
<!--  background: #f8fafc;-->
<!--  color: #0f172a;-->
<!--  border-color: #94a3b8;-->
<!--}-->

<!--.btn:disabled {-->
<!--  opacity: 0.5;-->
<!--  cursor: not-allowed;-->
<!--}-->

<!--.btn.primary {-->
<!--  background: #0f172a;-->
<!--  color: #fff;-->
<!--  border-color: #0f172a;-->
<!--}-->

<!--.btn.primary:hover:not(:disabled) {-->
<!--  background: #1e293b;-->
<!--  border-color: #1e293b;-->
<!--  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);-->
<!--}-->

<!--/* ================= THỐNG KÊ (SUMMARY GRID) ================= */-->
<!--.summary-grid {-->
<!--  display: grid;-->
<!--  grid-template-columns: repeat(4, minmax(0, 1fr));-->
<!--  gap: 16px;-->
<!--}-->

<!--.summary-grid div {-->
<!--  border: 1px solid #e2e8f0;-->
<!--  background: #ffffff;-->
<!--  border-radius: 12px;-->
<!--  padding: 16px 20px;-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  gap: 8px;-->
<!--  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);-->
<!--  transition: transform 0.2s ease, box-shadow 0.2s ease;-->
<!--}-->

<!--.summary-grid div:hover {-->
<!--  transform: translateY(-2px);-->
<!--  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1);-->
<!--}-->

<!--.summary-grid span {-->
<!--  color: #64748b;-->
<!--  font-weight: 600;-->
<!--  font-size: 13px;-->
<!--}-->

<!--.summary-grid strong {-->
<!--  font-size: 24px;-->
<!--  color: #111827;-->
<!--  line-height: 1;-->
<!--}-->

<!--/* ================= THÔNG BÁO (ALERTS) ================= */-->
<!--.alert {-->
<!--  padding: 12px 16px;-->
<!--  border-radius: 8px;-->
<!--  font-size: 14px;-->
<!--  font-weight: 500;-->
<!--}-->

<!--.alert.error {-->
<!--  background: #fef2f2;-->
<!--  color: #991b1b;-->
<!--  border: 1px solid #fecaca;-->
<!--}-->

<!--/* ================= PHÂN TRANG (PAGINATION) ================= */-->
<!--.pagination-bar {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: flex-end;-->
<!--  gap: 16px;-->
<!--  padding: 4px 0;-->
<!--}-->

<!--.pagination-bar span {-->
<!--  color: #475569;-->
<!--  font-weight: 500;-->
<!--  font-size: 14px;-->
<!--}-->

<!--/* ================= BẢNG DỮ LIỆU (TABLE) ================= */-->
<!--.table-wrap {-->
<!--  overflow-x: auto;-->
<!--  border: 1px solid #e2e8f0;-->
<!--  border-radius: 12px;-->
<!--  background: #ffffff;-->
<!--  box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);-->
<!--}-->

<!--table {-->
<!--  width: 100%;-->
<!--  border-collapse: collapse;-->
<!--  min-width: 2300px; /* Giữ nguyên độ rộng khổng lồ của trang này */-->
<!--}-->

<!--th,-->
<!--td {-->
<!--  padding: 14px 16px;-->
<!--  border-bottom: 1px solid #e2e8f0;-->
<!--  text-align: left;-->
<!--  vertical-align: middle;-->
<!--  font-size: 14px;-->
<!--  color: #334155;-->
<!--}-->

<!--th {-->
<!--  background: #f8fafc;-->
<!--  color: #475467;-->
<!--  font-size: 13px;-->
<!--  font-weight: 600;-->
<!--  white-space: nowrap;-->
<!--}-->

<!--/* Hiệu ứng rê chuột highlight dòng cứu rỗi đôi mắt */-->
<!--tbody tr {-->
<!--  transition: background-color 0.15s ease;-->
<!--}-->

<!--tbody tr:hover {-->
<!--  background-color: #f1f5f9;-->
<!--}-->

<!--/* ================= THÀNH PHẦN TRONG BẢNG ================= */-->
<!--.object-cell {-->
<!--  display: grid;-->
<!--  gap: 4px;-->
<!--}-->

<!--.object-cell strong {-->
<!--  color: #111827;-->
<!--  font-weight: 600;-->
<!--}-->

<!--.object-cell span {-->
<!--  color: #64748b;-->
<!--  font-size: 12px;-->
<!--}-->

<!--.path-cell {-->
<!--  max-width: 280px;-->
<!--  white-space: normal;-->
<!--  color: #1d4ed8; /* Màu xanh liên kết cho đường dẫn */-->
<!--  font-family: ui-monospace, monospace; /* Font code cho URL nhìn Pro hơn */-->
<!--  font-size: 13px;-->
<!--  word-break: break-all;-->
<!--}-->

<!--.user-agent {-->
<!--  max-width: 250px;-->
<!--  white-space: normal;-->
<!--  color: #475569;-->
<!--  font-size: 13px;-->
<!--  line-height: 1.4;-->
<!--}-->

<!--.desc-cell {-->
<!--  max-width: 250px;-->
<!--  white-space: normal;-->
<!--  line-height: 1.4;-->
<!--}-->

<!--/* JSON Log */-->
<!--pre {-->
<!--  margin: 0;-->
<!--  max-width: 300px;-->
<!--  white-space: pre-wrap;-->
<!--  word-break: break-word;-->
<!--  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;-->
<!--  font-size: 12px;-->
<!--  color: #1e293b;-->
<!--  background: #f8fafc;-->
<!--  padding: 8px 10px;-->
<!--  border-radius: 6px;-->
<!--  border: 1px solid #e2e8f0;-->
<!--  max-height: 150px; /* Giới hạn chiều cao nếu JSON quá dài */-->
<!--  overflow-y: auto;-->
<!--}-->

<!--/* Badges */-->
<!--.status,-->
<!--.action {-->
<!--  display: inline-flex;-->
<!--  border-radius: 999px;-->
<!--  padding: 5px 10px;-->
<!--  font-size: 12px;-->
<!--  font-weight: 700;-->
<!--  white-space: nowrap;-->
<!--}-->

<!--.status.success { background: #ecfdf5; color: #065f46; border: 1px solid #a7f3d0; }-->
<!--.status.error { background: #fef2f2; color: #991b1b; border: 1px solid #fecaca; }-->

<!--.action.TAO_MOI { background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; }-->
<!--.action.CAP_NHAT { background: #fef3c7; color: #b45309; border: 1px solid #fde68a; }-->
<!--.action.XOA { background: #fef2f2; color: #b91c1c; border: 1px solid #fecaca; }-->

<!--/* ================= RESPONSIVE (MÀN HÌNH NHỎ) ================= */-->
<!--@media (max-width: 1200px) {-->
<!--  .summary-grid {-->
<!--    grid-template-columns: repeat(2, minmax(0, 1fr));-->
<!--  }-->
<!--}-->

<!--@media (max-width: 640px) {-->
<!--  .filter-card {-->
<!--    grid-template-columns: 1fr; /* Tự động xếp dọc các ô tìm kiếm */-->
<!--  }-->

<!--  .summary-grid {-->
<!--    grid-template-columns: 1fr;-->
<!--  }-->

<!--  .filter-actions {-->
<!--    justify-content: flex-start;-->
<!--  }-->

<!--  .pagination-bar {-->
<!--    flex-direction: column;-->
<!--    align-items: center;-->
<!--    gap: 12px;-->
<!--  }-->
<!--}-->
<!--</style>-->

<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Nhật ký hệ thống</h1>
        <p>Theo dõi thao tác tạo, cập nhật, xóa trên toàn hệ thống.</p>
      </div>

      <button type="button" class="btn btn-ghost" :disabled="dangTai" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <div class="filter-row">
        <label class="filter-item keyword">
          <span>Tìm kiếm</span>
          <input
              v-model.trim="keyword"
              type="text"
              placeholder="Tài khoản, đường dẫn, IP, nội dung"
              @keyup.enter="locDuLieu"
          />
        </label>

        <label class="filter-item">
          <span>Module</span>
          <select v-model="moduleNghiepVu">
            <option value="">Tất cả</option>
            <option value="SINH_VIEN">Sinh viên</option>
            <option value="DAO_TAO">Đào tạo</option>
            <option value="CHUONG_TRINH">Chương trình</option>
            <option value="GIANG_DAY">Giảng dạy</option>
            <option value="DIEM">Điểm</option>
            <option value="HOC_PHI">Học phí</option>
            <option value="THI">Thi</option>
            <option value="LMS">LMS</option>
            <option value="HE_THONG">Hệ thống</option>
            <option value="KHAC">Khác</option>
          </select>
        </label>

        <label class="filter-item">
          <span>Hành động</span>
          <select v-model="hanhDong">
            <option value="">Tất cả</option>
            <option value="TAO_MOI">Tạo mới</option>
            <option value="CAP_NHAT">Cập nhật</option>
            <option value="XOA">Xóa</option>
          </select>
        </label>

        <label class="filter-item">
          <span>Kết quả</span>
          <select v-model="ketQua">
            <option value="">Tất cả</option>
            <option value="THANH_CONG">Thành công</option>
            <option value="THAT_BAI">Thất bại</option>
          </select>
        </label>

        <label class="filter-item date">
          <span>Từ ngày</span>
          <input v-model="tuNgay" type="date" />
        </label>

        <label class="filter-item date">
          <span>Đến ngày</span>
          <input v-model="denNgay" type="date" />
        </label>

        <label class="filter-item email">
          <span>Tài khoản</span>
          <input
              v-model.trim="taiKhoanEmail"
              type="text"
              placeholder="admin@gmail.com"
              @keyup.enter="locDuLieu"
          />
        </label>

        <label class="filter-item record-id">
          <span>Bản ghi</span>
          <input
              v-model.trim="banGhiId"
              type="number"
              placeholder="ID"
              @keyup.enter="locDuLieu"
          />
        </label>

        <div class="filter-actions inline">
          <button type="button" class="btn" :disabled="dangTai" @click="xoaLoc">
            Xóa
          </button>

          <button type="button" class="btn primary" :disabled="dangTai" @click="locDuLieu">
            Lọc
          </button>
        </div>
      </div>
    </section>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="table-card">
      <div class="table-toolbar">
        <div class="mini-stats">
          <span>
            Tổng:
            <strong>{{ tongBanGhi || danhSach.length }}</strong>
          </span>

          <span class="success">
            Thành công:
            <strong>{{ tongThanhCong }}</strong>
          </span>

          <span class="danger">
            Thất bại:
            <strong>{{ tongThatBai }}</strong>
          </span>

          <span>
            Hiển thị:
            <strong>{{ danhSachHienThi.length }}</strong>
          </span>
        </div>

        <div class="pagination-mini">
          <button
              type="button"
              class="btn btn-xs"
              :disabled="trangHienTai <= 0 || dangTai"
              @click="doiTrang(trangHienTai - 1)"
          >
            ‹
          </button>

          <span>
            Trang {{ trangHienTai + 1 }}/{{ tongTrang || 1 }}
            · {{ tongBanGhi }} bản ghi
          </span>

          <button
              type="button"
              class="btn btn-xs"
              :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
              @click="doiTrang(trangHienTai + 1)"
          >
            ›
          </button>
        </div>
      </div>

      <div class="table-scroll">
        <table class="log-table">
          <colgroup>
            <col style="width: 38px" />
            <col style="width: 150px" />
            <col style="width: 260px" />
            <col style="width: 220px" />
            <col style="width: 170px" />
            <col style="width: 105px" />
            <col style="width: 105px" />
          </colgroup>

          <thead>
          <tr>
            <th></th>
            <th>Thời gian</th>
            <th>Tài khoản</th>
            <th>Đối tượng</th>
            <th>Module</th>
            <th>Hành động</th>
            <th>Kết quả</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="7" class="empty-cell">Đang tải dữ liệu...</td>
          </tr>

          <tr v-else-if="!danhSachHienThi.length">
            <td colspan="7" class="empty-cell">Chưa có dữ liệu</td>
          </tr>

          <template v-else>
            <template v-for="item in danhSachHienThi" :key="item.id">
              <tr
                  class="master-row"
                  :class="{ 'is-expanded': laDongDangMo(item.id) }"
                  @click="toggleChiTiet(item.id)"
              >
                <td class="text-center">
                  <button type="button" class="expand-btn">
                    {{ laDongDangMo(item.id) ? '▼' : '▶' }}
                  </button>
                </td>

                <td>
                  <div class="cell-main">#{{ item.id }}</div>
                  <div class="cell-sub ellipsis">
                    {{ item.createdAt || item.thoiGian || '-' }}
                  </div>
                </td>

                <td>
                  <div class="cell-main ellipsis">
                    {{ item.taiKhoanEmail || item.taiKhoanId || '-' }}
                  </div>
                  <div class="cell-sub ellipsis">
                    {{ item.tenTaiKhoan || '-' }} · {{ hienThiLoaiTaiKhoan(item.loaiTaiKhoan) }}
                  </div>
                </td>

                <td>
                  <div class="cell-main ellipsis">
                    {{ item.doiTuongTen || '-' }}
                  </div>
                  <div class="cell-sub ellipsis">
                    {{ item.doiTuongLoai || '-' }}
                    {{ item.doiTuongId ? '#' + item.doiTuongId : '' }}
                  </div>
                </td>

                <td>
                  <div class="cell-main ellipsis">
                    {{ item.moduleNghiepVu || '-' }}
                  </div>
                  <div class="cell-sub ellipsis">
                    {{ item.chucNang || '-' }}
                  </div>
                </td>

                <td>
                    <span class="action" :class="item.hanhDong">
                      {{ hienThiHanhDong(item.hanhDong) }}
                    </span>
                </td>

                <td>
                    <span
                        class="status"
                        :class="item.ketQua === 'THANH_CONG' ? 'success' : 'error'"
                    >
                      {{ hienThiKetQua(item.ketQua) }}
                    </span>
                </td>
              </tr>

              <tr v-if="laDongDangMo(item.id)" class="detail-row">
                <td colspan="7">
                  <div class="detail-content">
                    <div class="detail-left">
                      <div><b>Phương thức:</b> <span class="badge">{{ item.phuongThuc || '-' }}</span></div>
                      <div><b>Đường dẫn:</b> <code class="path">{{ item.duongDan || '-' }}</code></div>
                      <div><b>Bảng:</b> {{ item.bangTacDong || '-' }} · <b>ID:</b> {{ item.banGhiId || '-' }}</div>
                      <div><b>IP:</b> {{ item.ipAddress || '-' }}</div>
                      <div><b>Thiết bị:</b> <span class="muted-text">{{ item.userAgent || '-' }}</span></div>
                      <div><b>Mô tả:</b> {{ item.moTa || '-' }}</div>
                    </div>

                    <div class="detail-right">
                      <div class="data-box">
                        <strong>Cũ</strong>
                        <pre>{{ item.noiDungCu || 'Không có dữ liệu' }}</pre>
                      </div>

                      <div class="data-box">
                        <strong>Mới</strong>
                        <pre>{{ item.noiDungMoi || 'Không có dữ liệu' }}</pre>
                      </div>
                    </div>
                  </div>
                </td>
              </tr>
            </template>
          </template>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { heThongApi } from '../api/heThongApi'

const danhSach = ref([])
const keyword = ref('')
const moduleNghiepVu = ref('')
const hanhDong = ref('')
const ketQua = ref('')
const tuNgay = ref('')
const denNgay = ref('')
const taiKhoanEmail = ref('')
const banGhiId = ref('')

const dangTai = ref(false)
const loi = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const dongMoRong = ref([])

const danhSachHienThi = computed(() => {
  const key = keyword.value.trim().toLowerCase()
  const module = moduleNghiepVu.value.trim().toLowerCase()
  const hanh = hanhDong.value.trim().toLowerCase()
  const kq = ketQua.value.trim().toLowerCase()

  return danhSach.value.filter((item) => {
    const json = JSON.stringify(item || {}).toLowerCase()

    const dungKeyword = !key || json.includes(key)

    const dungModule = !module
        || String(item.moduleNghiepVu || '').toLowerCase() === module

    const dungHanhDong = !hanh
        || String(item.hanhDong || '').toLowerCase() === hanh

    const dungKetQua = !kq
        || String(item.ketQua || '').toLowerCase() === kq

    return dungKeyword && dungModule && dungHanhDong && dungKetQua
  })
})

const tongThanhCong = computed(() => {
  return danhSach.value.filter(item => item.ketQua === 'THANH_CONG').length
})

const tongThatBai = computed(() => {
  return danhSach.value.filter(item => item.ketQua === 'THAT_BAI').length
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    const params = {
      page: trangHienTai.value,
      size: kichThuocTrang,
      sort: 'id,desc'
    }

    if (keyword.value) params.keyword = keyword.value
    if (moduleNghiepVu.value) params.moduleNghiepVu = moduleNghiepVu.value
    if (hanhDong.value) params.hanhDong = hanhDong.value
    if (ketQua.value) params.ketQua = ketQua.value
    if (tuNgay.value) params.tuNgay = tuNgay.value
    if (denNgay.value) params.denNgay = denNgay.value
    if (taiKhoanEmail.value) params.taiKhoanEmail = taiKhoanEmail.value
    if (banGhiId.value) params.banGhiId = banGhiId.value

    const response = await heThongApi.nhatKyHeThong.getAll(params)
    const pageData = layPage(response)

    danhSach.value = pageData.content
    trangHienTai.value = pageData.number
    tongTrang.value = pageData.totalPages
    tongBanGhi.value = pageData.totalElements

    dongMoRong.value = []
  } catch (error) {
    loi.value = error?.response?.data?.message
        || error?.message
        || 'Không tải được nhật ký hệ thống'
  } finally {
    dangTai.value = false
  }
}

function layPage(result) {
  const data = result?.data?.data || result?.data || result

  if (Array.isArray(data)) {
    return {
      content: data,
      number: 0,
      totalPages: 1,
      totalElements: data.length
    }
  }

  if (Array.isArray(data?.content)) {
    return {
      content: data.content,
      number: data.number || 0,
      totalPages: data.totalPages || 1,
      totalElements: data.totalElements || data.content.length
    }
  }

  return {
    content: [],
    number: 0,
    totalPages: 0,
    totalElements: 0
  }
}

function doiTrang(page) {
  if (page < 0) return
  if (tongTrang.value && page >= tongTrang.value) return

  trangHienTai.value = page
  taiDuLieu()
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  keyword.value = ''
  moduleNghiepVu.value = ''
  hanhDong.value = ''
  ketQua.value = ''
  tuNgay.value = ''
  denNgay.value = ''
  taiKhoanEmail.value = ''
  banGhiId.value = ''
  trangHienTai.value = 0
  dongMoRong.value = []
  taiDuLieu()
}

function laDongDangMo(id) {
  return dongMoRong.value.includes(id)
}

function toggleChiTiet(id) {
  if (!id) return

  const index = dongMoRong.value.indexOf(id)

  if (index > -1) {
    dongMoRong.value.splice(index, 1)
  } else {
    dongMoRong.value.push(id)
  }
}

function hienThiLoaiTaiKhoan(value) {
  if (!value) return '-'

  const text = String(value)

  if (text === 'nhan_vien') return 'Nhân viên'
  if (text === 'sinh_vien') return 'Sinh viên'
  if (text === 'giao_vien') return 'Giáo viên'
  if (text === 'admin') return 'Admin'

  return text
}

function hienThiHanhDong(value) {
  if (value === 'TAO_MOI') return 'Tạo mới'
  if (value === 'CAP_NHAT') return 'Cập nhật'
  if (value === 'XOA') return 'Xóa'
  return value || '-'
}

function hienThiKetQua(value) {
  if (value === 'THANH_CONG') return 'Thành công'
  if (value === 'THAT_BAI') return 'Thất bại'
  return value || '-'
}
</script>

<style scoped>
:deep(*) {
  box-sizing: border-box;
}

.page {
  --pn-primary: #077149;
  --pn-primary-hover: #055c3b;
  --pn-primary-dark: #064e3b;
  --pn-primary-soft: #eef8f3;
  --pn-primary-soft-2: #f8fcfa;
  --pn-primary-border: #b7dfce;
  --pn-border: #d9e7df;
  --pn-text: #123326;
  --pn-muted: #667085;
  --pn-danger-bg: #fef2f2;
  --pn-danger-text: #b91c1c;
  --pn-danger-border: #fecaca;
  --pn-warning-bg: #fff7e6;
  --pn-warning-text: #9a5b00;
  --pn-warning-border: #ffd88a;

  display: flex;
  flex-direction: column;
  gap: 8px;
  color: var(--pn-text);
  font-family: Roboto, system-ui, -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
}

/* HEADER */
.page-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  min-height: 44px;
  padding: 7px 10px;
  border: 1px solid var(--pn-border);
  border-radius: 8px;
  background: linear-gradient(135deg, rgba(7, 113, 73, 0.07), rgba(7, 113, 73, 0.01)), #ffffff;
}

.page-head h1 {
  margin: 0;
  font-size: 16px;
  line-height: 1.2;
  color: var(--pn-primary-dark);
}

.page-head p {
  margin: 1px 0 0;
  color: var(--pn-muted);
  font-size: 11px;
  line-height: 1.25;
}

/* FILTER 1 HÀNG */
.filter-card {
  padding: 8px;
  border: 1px solid var(--pn-border);
  border-radius: 8px;
  background: #ffffff;
}

.filter-row {
  display: grid;
  grid-template-columns:
    minmax(210px, 1.55fr)
    minmax(125px, 0.85fr)
    minmax(105px, 0.75fr)
    minmax(105px, 0.75fr)
    minmax(125px, 0.8fr)
    minmax(125px, 0.8fr)
    minmax(150px, 1fr)
    minmax(85px, 0.55fr)
    auto;
  gap: 6px;
  align-items: end;
}

.filter-item {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.filter-item span {
  font-size: 11px;
  font-weight: 700;
  color: var(--pn-primary-dark);
  line-height: 1.1;
}

input,
select {
  width: 100%;
  height: 30px;
  min-height: 30px;
  border: 1px solid var(--pn-border);
  border-radius: 6px;
  padding: 5px 7px;
  font-size: 12px;
  line-height: 1.2;
  color: var(--pn-text);
  background: #ffffff;
  outline: none;
}

input:hover,
select:hover {
  border-color: var(--pn-primary-border);
}

input:focus,
select:focus {
  border-color: var(--pn-primary);
  box-shadow: 0 0 0 2px rgba(7, 113, 73, 0.1);
}

.filter-actions.inline {
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
  gap: 5px;
  white-space: nowrap;
}

/* BUTTON */
.btn {
  height: 30px;
  min-height: 30px;
  border: 1px solid var(--pn-primary-border);
  background: #ffffff;
  color: var(--pn-primary-dark);
  border-radius: 6px;
  padding: 5px 10px;
  cursor: pointer;
  font-weight: 700;
  font-size: 12px;
  line-height: 1;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}

.btn:hover:not(:disabled) {
  background: var(--pn-primary-soft);
  border-color: var(--pn-primary);
}

.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.btn.primary {
  background: var(--pn-primary);
  border-color: var(--pn-primary);
  color: #ffffff;
}

.btn.primary:hover:not(:disabled) {
  background: var(--pn-primary-hover);
  border-color: var(--pn-primary-hover);
}

.btn-ghost {
  min-width: 62px;
}

.btn-xs {
  width: 26px;
  min-width: 26px;
  padding: 0;
  font-size: 15px;
}

/* ALERT */
.alert {
  padding: 7px 10px;
  border-radius: 7px;
  font-size: 12px;
  font-weight: 600;
}

.alert.error {
  background: var(--pn-danger-bg);
  color: var(--pn-danger-text);
  border: 1px solid var(--pn-danger-border);
}

/* TABLE CARD + TOOLBAR */
.table-card {
  border: 1px solid var(--pn-border);
  border-radius: 8px;
  background: #ffffff;
  overflow: hidden;
}

.table-toolbar {
  min-height: 34px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 5px 8px;
  border-bottom: 1px solid var(--pn-border);
  background: var(--pn-primary-soft-2);
}

.mini-stats {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
  flex-wrap: wrap;
}

.mini-stats span {
  font-size: 12px;
  color: var(--pn-muted);
  white-space: nowrap;
}

.mini-stats strong {
  margin-left: 2px;
  font-size: 13px;
  color: var(--pn-primary-dark);
}

.mini-stats .success strong {
  color: var(--pn-primary);
}

.mini-stats .danger strong {
  color: #dc2626;
}

.pagination-mini {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 5px;
  white-space: nowrap;
}

.pagination-mini span {
  font-size: 12px;
  font-weight: 600;
  color: var(--pn-primary-dark);
}

/* TABLE */
.table-scroll {
  width: 100%;
  overflow-x: auto;
}

.log-table {
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
}

.log-table th {
  height: 30px;
  padding: 6px 8px;
  background: var(--pn-primary);
  color: #ffffff;
  border-bottom: 1px solid var(--pn-primary-hover);
  text-align: left;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
}

.log-table td {
  height: 38px;
  padding: 5px 8px;
  border-bottom: 1px solid var(--pn-border);
  vertical-align: middle;
  font-size: 12px;
  color: var(--pn-text);
}

.master-row {
  cursor: pointer;
  background: #ffffff;
}

.master-row:hover {
  background: var(--pn-primary-soft-2);
}

.master-row.is-expanded {
  background: var(--pn-primary-soft);
}

.text-center {
  text-align: center;
}

.expand-btn {
  width: 20px;
  height: 20px;
  border: 1px solid var(--pn-primary-border);
  border-radius: 5px;
  background: #ffffff;
  color: var(--pn-primary);
  cursor: pointer;
  font-size: 10px;
  line-height: 1;
}

.master-row:hover .expand-btn,
.master-row.is-expanded .expand-btn {
  background: var(--pn-primary);
  color: #ffffff;
}

.cell-main {
  min-width: 0;
  font-size: 12px;
  font-weight: 700;
  color: var(--pn-primary-dark);
  line-height: 1.25;
}

.cell-sub {
  min-width: 0;
  margin-top: 1px;
  font-size: 11px;
  color: var(--pn-muted);
  line-height: 1.2;
}

.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.empty-cell {
  padding: 14px 10px !important;
  text-align: center;
  color: var(--pn-muted);
}

/* BADGE */
.status,
.action {
  display: inline-flex;
  max-width: 100%;
  align-items: center;
  justify-content: center;
  border-radius: 999px;
  padding: 3px 7px;
  font-size: 11px;
  font-weight: 700;
  line-height: 1.1;
  white-space: nowrap;
}

.status.success {
  background: var(--pn-primary-soft);
  color: var(--pn-primary-dark);
  border: 1px solid var(--pn-primary-border);
}

.status.error {
  background: var(--pn-danger-bg);
  color: var(--pn-danger-text);
  border: 1px solid var(--pn-danger-border);
}

.action.TAO_MOI {
  background: var(--pn-primary-soft);
  color: var(--pn-primary-dark);
  border: 1px solid var(--pn-primary-border);
}

.action.CAP_NHAT {
  background: var(--pn-warning-bg);
  color: var(--pn-warning-text);
  border: 1px solid var(--pn-warning-border);
}

.action.XOA {
  background: var(--pn-danger-bg);
  color: var(--pn-danger-text);
  border: 1px solid var(--pn-danger-border);
}

/* DETAIL */
.detail-row {
  background: var(--pn-primary-soft-2);
}

.detail-row td {
  height: auto;
  padding: 0 !important;
}

.detail-content {
  display: grid;
  grid-template-columns: minmax(260px, 0.9fr) minmax(420px, 1.4fr);
  gap: 10px;
  padding: 8px 10px 10px 38px;
  border-top: 1px dashed var(--pn-primary-border);
}

.detail-left {
  display: grid;
  gap: 4px;
  font-size: 12px;
  line-height: 1.35;
}

.muted-text {
  color: var(--pn-muted);
}

.badge {
  display: inline-flex;
  border: 1px solid var(--pn-primary-border);
  border-radius: 999px;
  padding: 1px 6px;
  background: var(--pn-primary-soft);
  color: var(--pn-primary-dark);
  font-size: 10px;
  font-weight: 700;
}

.path {
  display: inline-block;
  max-width: 100%;
  padding: 1px 5px;
  border: 1px solid var(--pn-primary-border);
  border-radius: 5px;
  background: var(--pn-primary-soft);
  color: var(--pn-primary-dark);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 11px;
  word-break: break-all;
}

.detail-right {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
}

.data-box {
  min-width: 0;
  border: 1px solid var(--pn-border);
  border-radius: 7px;
  background: #ffffff;
  padding: 7px;
}

.data-box strong {
  display: block;
  margin-bottom: 4px;
  font-size: 11px;
  color: var(--pn-primary-dark);
}

.data-box pre {
  margin: 0;
  max-height: 120px;
  overflow: auto;
  padding: 6px;
  border: 1px solid var(--pn-border);
  border-radius: 6px;
  background: #f8fcfa;
  color: var(--pn-text);
  font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, monospace;
  font-size: 10.5px;
  line-height: 1.3;
  white-space: pre-wrap;
  word-break: break-word;
}

/* RESPONSIVE */
@media (max-width: 1400px) {
  .filter-row {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }

  .filter-actions.inline {
    grid-column: span 4;
    justify-content: flex-end;
  }
}

@media (max-width: 900px) {
  .filter-row {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .filter-actions.inline {
    grid-column: span 2;
  }

  .table-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .pagination-mini {
    width: 100%;
    justify-content: flex-end;
  }

  .log-table {
    min-width: 980px;
  }

  .detail-content {
    grid-template-columns: 1fr;
    padding-left: 10px;
  }

  .detail-right {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 560px) {
  .filter-row {
    grid-template-columns: 1fr;
  }

  .filter-actions.inline {
    grid-column: span 1;
    justify-content: flex-start;
  }

  .mini-stats {
    gap: 8px;
  }
}
</style>