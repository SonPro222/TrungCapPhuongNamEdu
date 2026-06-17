<template>
  <section class="gd-page">
    <div class="gd-page-head">
      <div>
        <h1>Giảng viên đăng ký số tiết</h1>
        <p>Phòng đào tạo ghi nhận số tiết giảng viên đăng ký hoặc được phân bổ trong kỳ. Số tiết đã phân công được cập nhật tự động từ chức năng phân công giảng dạy.</p>
      </div>
      <button class="btn btn-primary" @click="moFormThem">+ Thêm đăng ký</button>
    </div>

    <!-- Form -->
    <div v-if="hienForm" class="gd-card">
      <h2>{{ dangSua ? 'Cập nhật đăng ký' : 'Thêm đăng ký mới' }}</h2>
      <div class="gd-form-grid">
        <label class="gd-field gd-span-2">
          <span>Giảng viên <em>*</em></span>
          <select v-model="form.giaoVienId">
            <option value="">-- Chọn giảng viên --</option>
            <option v-for="gv in danhSachGiaoVien" :key="gv.id" :value="gv.id">
              {{ gv.maGiaoVien }} - {{ gv.hoTen }}
            </option>
          </select>
        </label>
        <label class="gd-field gd-span-2">
          <span>Khung kỳ <em>*</em></span>
          <select v-model="form.khungKyId">
            <option value="">-- Chọn kỳ --</option>
            <option v-for="ky in danhSachKhungKy" :key="ky.id" :value="ky.id">
              {{ ky.maKy }}{{ ky.tenKy ? ' - ' + ky.tenKy : '' }}
            </option>
          </select>
        </label>
        <label class="gd-field">
          <span>Số tiết đăng ký <em>*</em></span>
          <input type="number" v-model.number="form.soTietDangKy" min="1" placeholder="VD: 90" />
        </label>
        <label class="gd-field">
          <span>Trạng thái</span>
          <select v-model="form.trangThai">
            <option value="dang_hieu_luc">Đang hiệu lực</option>
            <option value="ngung_ap_dung">Ngưng áp dụng</option>
          </select>
        </label>
        <label class="gd-field gd-span-2">
          <span>Ghi chú</span>
          <input v-model="form.ghiChu" placeholder="Ghi chú (tùy chọn)" />
        </label>
        <div v-if="dangSua" class="gd-info-row gd-span-2">
          <span>Số tiết đã phân công: <strong>{{ form.soTietDaPhanCong }}</strong></span>
          <span>Số tiết còn lại: <strong>{{ (form.soTietDangKy || 0) - (form.soTietDaPhanCong || 0) }}</strong></span>
        </div>
      </div>
      <div v-if="thongBaoLoi" class="gd-alert gd-alert-error">{{ thongBaoLoi }}</div>
      <div class="gd-form-actions">
        <button class="btn btn-secondary" @click="dongForm">Hủy</button>
        <button class="btn btn-primary" @click="luu" :disabled="dangLuu">
          {{ dangLuu ? 'Đang lưu...' : (dangSua ? 'Cập nhật' : 'Thêm mới') }}
        </button>
      </div>
    </div>

    <!-- Bộ lọc -->
    <div class="gd-filter-card">
      <input v-model="boLoc.keyword" @input="timKiem" class="gd-search" placeholder="Tìm kiếm..." />
      <select v-model="boLoc.giaoVienId" @change="timKiem">
        <option value="">Tất cả giảng viên</option>
        <option v-for="gv in danhSachGiaoVien" :key="gv.id" :value="gv.id">{{ gv.hoTen }}</option>
      </select>
      <select v-model="boLoc.khungKyId" @change="timKiem">
        <option value="">Tất cả kỳ</option>
        <option v-for="ky in danhSachKhungKy" :key="ky.id" :value="ky.id">{{ ky.maKy }}</option>
      </select>
      <select v-model="boLoc.trangThai" @change="timKiem">
        <option value="">Tất cả trạng thái</option>
        <option value="dang_hieu_luc">Đang hiệu lực</option>
        <option value="ngung_ap_dung">Ngưng áp dụng</option>
      </select>
    </div>

    <!-- Bảng -->
    <div class="gd-card">
      <div v-if="dangTai" class="gd-loading">Đang tải...</div>
      <div v-else class="gd-table-wrap">
        <table class="gd-table">
          <thead>
            <tr>
              <th>STT</th>
              <th>Mã GV</th>
              <th>Tên giảng viên</th>
              <th>Kỳ</th>
              <th>Số tiết đăng ký</th>
              <th>Đã phân công</th>
              <th>Còn lại</th>
              <th>Trạng thái</th>
              <th>Ghi chú</th>
              <th>Thao tác</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, i) in danhSach" :key="item.id">
              <td>{{ (trang - 1) * kichThuocTrang + i + 1 }}</td>
              <td><code>{{ item.maGiaoVien }}</code></td>
              <td>{{ item.hoTenGiaoVien }}</td>
              <td>{{ item.maKy }}</td>
              <td class="text-center">{{ item.soTietDangKy }}</td>
              <td class="text-center">{{ item.soTietDaPhanCong }}</td>
              <td class="text-center">
                <span :class="{ 'text-canh-bao': item.soTietConLai < 10 }">{{ item.soTietConLai }}</span>
              </td>
              <td><span class="gd-badge" :class="item.trangThai">{{ hienThiTrangThai(item.trangThai) }}</span></td>
              <td>{{ item.ghiChu }}</td>
              <td>
                <button class="btn btn-sm btn-secondary" @click="chonSua(item)">Sửa</button>
                <button class="btn btn-sm btn-danger" @click="xoa(item.id)">Xóa</button>
              </td>
            </tr>
            <tr v-if="!danhSach.length">
              <td colspan="10" class="gd-empty">Không có dữ liệu</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div v-if="tongTrang > 1" class="gd-pagination">
        <button @click="doiTrang(trang - 1)" :disabled="trang <= 1">‹</button>
        <span>Trang {{ trang }} / {{ tongTrang }}</span>
        <button @click="doiTrang(trang + 1)" :disabled="trang >= tongTrang">›</button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { giangDayService } from '../services/giangDayService'

const danhSachGiaoVien = ref([])
const danhSachKhungKy = ref([])
const danhSach = ref([])
const dangTai = ref(false)
const dangLuu = ref(false)
const hienForm = ref(false)
const dangSua = ref(false)
const thongBaoLoi = ref('')
const trang = ref(1)
const tongTrang = ref(1)
const kichThuocTrang = 20

const boLoc = reactive({ keyword: '', giaoVienId: '', khungKyId: '', trangThai: '' })
const form = reactive({ id: null, giaoVienId: '', khungKyId: '', soTietDangKy: null, soTietDaPhanCong: 0, trangThai: 'dang_hieu_luc', ghiChu: '' })

function hienThiTrangThai(tt) {
  return { dang_hieu_luc: 'Đang hiệu lực', ngung_ap_dung: 'Ngưng áp dụng' }[tt] || tt
}

async function taiNen() {
  try {
    danhSachGiaoVien.value = await giangDayService.layDanhSachGiaoVien()
    danhSachKhungKy.value = await giangDayService.layDanhSachKhungKy()
  } catch (e) { console.warn(e) }
}

async function taiDuLieu() {
  dangTai.value = true
  try {
    const params = { page: trang.value - 1, size: kichThuocTrang }
    if (boLoc.keyword) params.keyword = boLoc.keyword
    if (boLoc.giaoVienId) params.giaoVienId = boLoc.giaoVienId
    if (boLoc.khungKyId) params.khungKyId = boLoc.khungKyId
    if (boLoc.trangThai) params.trangThai = boLoc.trangThai
    const res = await giangDayService.layTrangGiangVienDangKyGiangDay(params)
    const data = res?.data?.data
    danhSach.value = data?.content || []
    tongTrang.value = data?.totalPages || 1
  } catch (e) { danhSach.value = [] } finally { dangTai.value = false }
}

function timKiem() { trang.value = 1; taiDuLieu() }
function doiTrang(p) { if (p < 1 || p > tongTrang.value) return; trang.value = p; taiDuLieu() }

function moFormThem() {
  Object.assign(form, { id: null, giaoVienId: '', khungKyId: '', soTietDangKy: null, soTietDaPhanCong: 0, trangThai: 'dang_hieu_luc', ghiChu: '' })
  dangSua.value = false; thongBaoLoi.value = ''; hienForm.value = true
}
function chonSua(item) {
  Object.assign(form, { id: item.id, giaoVienId: item.giaoVienId, khungKyId: item.khungKyId, soTietDangKy: item.soTietDangKy, soTietDaPhanCong: item.soTietDaPhanCong || 0, trangThai: item.trangThai || 'dang_hieu_luc', ghiChu: item.ghiChu || '' })
  dangSua.value = true; thongBaoLoi.value = ''; hienForm.value = true
}
function dongForm() { hienForm.value = false; thongBaoLoi.value = '' }

async function luu() {
  thongBaoLoi.value = ''
  if (!form.giaoVienId) { thongBaoLoi.value = 'Vui lòng chọn giảng viên'; return }
  if (!form.khungKyId) { thongBaoLoi.value = 'Vui lòng chọn khung kỳ'; return }
  if (!form.soTietDangKy || form.soTietDangKy <= 0) { thongBaoLoi.value = 'Số tiết đăng ký phải lớn hơn 0'; return }
  if (dangSua.value && form.soTietDangKy < form.soTietDaPhanCong) {
    thongBaoLoi.value = `Số tiết đăng ký không thể nhỏ hơn số tiết đã phân công (${form.soTietDaPhanCong} tiết)`
    return
  }
  dangLuu.value = true
  try {
    const payload = { giaoVienId: form.giaoVienId, khungKyId: form.khungKyId, soTietDangKy: form.soTietDangKy, trangThai: form.trangThai, ghiChu: form.ghiChu }
    if (dangSua.value) await giangDayService.capNhatGiangVienDangKyGiangDay(form.id, payload)
    else await giangDayService.taoGiangVienDangKyGiangDay(payload)
    dongForm(); taiDuLieu()
  } catch (e) { thongBaoLoi.value = e?.response?.data?.message || e.message || 'Có lỗi xảy ra'
  } finally { dangLuu.value = false }
}

async function xoa(id) {
  if (!confirm('Xác nhận xóa đăng ký này?')) return
  try { await giangDayService.xoaGiangVienDangKyGiangDay(id); taiDuLieu() }
  catch (e) { alert(e?.response?.data?.message || e.message || 'Không thể xóa') }
}

onMounted(async () => { await taiNen(); taiDuLieu() })
</script>

<style scoped>
.gd-page { padding: 16px 24px; display: flex; flex-direction: column; gap: 16px; }
.gd-page-head { display: flex; align-items: flex-start; justify-content: space-between; gap: 12px; }
.gd-page-head h1 { font-size: 18px; font-weight: 700; margin: 0 0 4px; }
.gd-page-head p { font-size: 13px; color: #6b7280; margin: 0; max-width: 600px; }
.gd-card { background: #fff; border-radius: 12px; border: 1px solid #e5e7eb; padding: 16px 20px; }
.gd-card h2 { font-size: 15px; font-weight: 700; margin: 0 0 14px; }
.gd-form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.gd-field { display: flex; flex-direction: column; gap: 4px; font-size: 13px; }
.gd-field span { font-weight: 500; color: #374151; }
.gd-field em { color: #dc2626; font-style: normal; }
.gd-field input, .gd-field select { border: 1px solid #e5e7eb; border-radius: 8px; padding: 7px 10px; font-size: 13px; outline: none; }
.gd-field input:focus, .gd-field select:focus { border-color: #1d4ed8; }
.gd-span-2 { grid-column: span 2; }
.gd-info-row { display: flex; gap: 24px; font-size: 13px; padding: 10px 14px; background: #f0f9ff; border-radius: 8px; border: 1px solid #bae6fd; }
.gd-info-row strong { color: #1d4ed8; }
.gd-form-actions { display: flex; gap: 10px; justify-content: flex-end; margin-top: 14px; }
.gd-alert { border-radius: 8px; padding: 10px 14px; font-size: 13px; margin-top: 10px; }
.gd-alert-error { background: #fef2f2; color: #dc2626; border: 1px solid #fca5a5; }
.gd-filter-card { display: flex; gap: 10px; flex-wrap: wrap; }
.gd-search, .gd-filter-card select { border: 1px solid #e5e7eb; border-radius: 8px; padding: 8px 12px; font-size: 13px; outline: none; }
.gd-search { min-width: 200px; }
.gd-table-wrap { overflow-x: auto; }
.gd-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.gd-table th, .gd-table td { padding: 9px 10px; text-align: left; border-bottom: 1px solid #f3f4f6; }
.gd-table th { background: #f9fafb; font-weight: 600; font-size: 12px; color: #374151; text-transform: uppercase; }
.gd-table tr:hover td { background: #f9fafb; }
.gd-empty { text-align: center; color: #9ca3af; padding: 24px; }
.gd-loading { text-align: center; padding: 20px; color: #6b7280; }
.gd-badge { display: inline-block; padding: 2px 8px; border-radius: 999px; font-size: 11px; font-weight: 600; background: #e5e7eb; }
.gd-badge.dang_hieu_luc { background: #dcfce7; color: #16a34a; }
.gd-badge.ngung_ap_dung { background: #f3f4f6; color: #6b7280; }
.gd-pagination { display: flex; align-items: center; gap: 12px; justify-content: center; padding-top: 14px; font-size: 13px; }
.btn { border: none; border-radius: 8px; padding: 8px 16px; font-size: 13px; font-weight: 600; cursor: pointer; }
.btn-primary { background: #1d4ed8; color: #fff; }
.btn-secondary { background: #f3f4f6; color: #374151; }
.btn-danger { background: #fef2f2; color: #dc2626; border: 1px solid #fca5a5; }
.btn-sm { padding: 5px 10px; font-size: 12px; margin-right: 4px; }
.btn:disabled { opacity: 0.6; cursor: not-allowed; }
code { font-family: monospace; background: #f3f4f6; padding: 1px 5px; border-radius: 4px; font-size: 12px; }
.text-center { text-align: center; }
.text-canh-bao { color: #dc2626; font-weight: 600; }
</style>
