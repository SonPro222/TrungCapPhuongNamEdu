<template>
  <section class="page admin-ngay-nghi-page">
    <header class="page-head">
      <div>
        <h1>Quản lý lịch nghỉ</h1>
        <p>
          Khai báo ngày nghỉ dùng cho xếp lịch học. Hệ thống sinh lịch/gợi ý lịch sẽ bỏ qua
          các ngày có bật "Không xếp lịch học".
        </p>
      </div>
      <button type="button" class="btn" :disabled="dangTai" @click="taiDuLieu">
        {{ dangTai ? 'Đang tải...' : 'Tải lại' }}
      </button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>
    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>

    <section class="content-grid">
      <article class="card form-card">
        <div class="card-head">
          <div>
            <h2>{{ form.id ? 'Cập nhật lịch nghỉ' : 'Thêm lịch nghỉ' }}</h2>
            <p>Nhập một ngày hoặc một khoảng nghỉ. Phạm vi trống nghĩa là áp dụng toàn trường.</p>
          </div>
        </div>

        <div class="form-grid">
          <label class="wide">
            <span>Tên lịch nghỉ <b>*</b></span>
            <input v-model.trim="form.tenNgayNghi" placeholder="VD: Nghỉ lễ 30/4, Nghỉ giữa kỳ 1..." />
          </label>

          <label>
            <span>Ngày bắt đầu <b>*</b></span>
            <input v-model="form.ngayBatDau" type="date" @change="dongBoNgayBatDau" />
          </label>

          <label>
            <span>Ngày kết thúc <b>*</b></span>
            <input v-model="form.ngayKetThuc" type="date" />
          </label>

          <label>
            <span>Loại ngày nghỉ</span>
            <select v-model="form.loaiNgayNghi">
              <option v-for="loai in dsLoaiNgayNghi" :key="loai.value" :value="loai.value">
                {{ loai.label }}
              </option>
            </select>
          </label>

          <label>
            <span>Phạm vi áp dụng</span>
            <select v-model="phamViApDung" @change="doiPhamViApDung">
              <option value="TOAN_TRUONG">Toàn trường</option>
              <option value="CHUONG_TRINH">Theo chương trình/version</option>
              <option value="KHUNG_KY">Theo kỳ</option>
            </select>
          </label>

          <label v-if="phamViApDung !== 'TOAN_TRUONG'">
            <span>Chương trình/version</span>
            <select v-model.number="form.chuongTrinhVersionId" @change="doiVersion">
              <option :value="null">-- Chọn version --</option>
              <option v-for="version in danhSachVersion" :key="version.id" :value="version.id">
                {{ hienThiVersion(version) }}
              </option>
            </select>
          </label>

          <label v-if="phamViApDung === 'KHUNG_KY'">
            <span>Kỳ áp dụng</span>
            <select v-model.number="form.khungKyId">
              <option :value="null">-- Chọn kỳ --</option>
              <option v-for="ky in danhSachKyTheoVersion" :key="ky.id" :value="ky.id">
                {{ hienThiKy(ky) }}
              </option>
            </select>
          </label>

          <label class="toggle-card">
            <input v-model="form.tinhLaNgayKhongHoc" type="checkbox" />
            <span>
              <strong>Không xếp lịch học</strong>
              <small>Bật để auto/gợi ý lịch bỏ qua ngày này.</small>
            </span>
          </label>

          <label class="toggle-card">
            <input v-model="form.lapLaiHangNam" type="checkbox" />
            <span>
              <strong>Lặp lại hằng năm</strong>
              <small>Dùng cho ngày lễ cố định như 01/01, 30/04.</small>
            </span>
          </label>

          <label class="toggle-card">
            <input v-model="form.trangThai" type="checkbox" />
            <span>
              <strong>Đang áp dụng</strong>
              <small>Tắt nếu chỉ muốn lưu nháp, chưa chặn lịch.</small>
            </span>
          </label>

          <label class="wide">
            <span>Ghi chú</span>
            <textarea v-model.trim="form.ghiChu" rows="3" placeholder="Ghi chú nội bộ nếu có..."></textarea>
          </label>
        </div>

        <div class="actions">
          <button type="button" class="btn primary" :disabled="dangLuu" @click="luuNgayNghi">
            {{ dangLuu ? 'Đang lưu...' : form.id ? 'Cập nhật lịch nghỉ' : 'Thêm lịch nghỉ' }}
          </button>
          <button type="button" class="btn" @click="resetForm">Làm mới</button>
        </div>
      </article>

      <article class="card guide-card">
        <h2>Cách dùng đúng</h2>
        <ul>
          <li><strong>Toàn trường:</strong> dùng cho lễ, Tết, hoạt động chung.</li>
          <li><strong>Theo chương trình/version:</strong> dùng khi chỉ một chương trình nghỉ.</li>
          <li><strong>Theo kỳ:</strong> dùng cho nghỉ giữa kỳ hoặc nghỉ riêng trong một học kỳ.</li>
        </ul>
        <div class="note">
          Khi xếp lịch, BE cần kiểm tra ngày nghỉ theo lớp học phần:
          <code>ngày + chuongTrinhVersionId + khungKyId</code>.
        </div>
      </article>
    </section>

    <section class="card">
      <div class="table-head">
        <div>
          <h2>Danh sách lịch nghỉ</h2>
          <p>{{ danhSachNgayNghi.length }} bản ghi lịch nghỉ đang tải trên màn hình.</p>
        </div>
        <div class="filter-row">
          <input v-model.trim="tuKhoa" placeholder="Tìm theo tên, ghi chú..." />
          <select v-model="locTrangThai">
            <option value="ALL">Tất cả trạng thái</option>
            <option value="ACTIVE">Đang áp dụng</option>
            <option value="INACTIVE">Tạm tắt</option>
          </select>
        </div>
      </div>

      <div class="table-wrap">
        <table class="data-table">
          <thead>
          <tr>
            <th>Khoảng nghỉ</th>
            <th>Tên lịch nghỉ</th>
            <th>Loại</th>
            <th>Phạm vi</th>
            <th>Chặn lịch</th>
            <th>Trạng thái</th>
            <th></th>
          </tr>
          </thead>
          <tbody>
          <tr v-if="!danhSachHienThi.length">
            <td colspan="7" class="empty">Chưa có lịch nghỉ phù hợp.</td>
          </tr>
          <tr v-for="item in danhSachHienThi" :key="item.id">
            <td>
              <strong>{{ hienThiKhoangNgay(item) }}</strong>
              <small v-if="item.lapLaiHangNam" class="sub-text">Lặp lại hằng năm</small>
            </td>
            <td>
              {{ item.tenNgayNghi || '-' }}
              <small v-if="item.ghiChu" class="sub-text">{{ item.ghiChu }}</small>
            </td>
            <td>{{ tenLoaiNgayNghi(item.loaiNgayNghi) }}</td>
            <td>{{ hienThiPhamVi(item) }}</td>
            <td>
                <span :class="['status', item.tinhLaNgayKhongHoc ? 'warning' : 'muted']">
                  {{ item.tinhLaNgayKhongHoc ? 'Có chặn' : 'Không chặn' }}
                </span>
            </td>
            <td>
                <span :class="['status', item.trangThai ? 'done' : 'cancel']">
                  {{ item.trangThai ? 'Đang áp dụng' : 'Tạm tắt' }}
                </span>
            </td>
            <td class="row-actions">
              <button type="button" class="btn" @click="suaNgayNghi(item)">Sửa</button>
              <button type="button" class="btn danger" @click="xoaNgayNghi(item)">Xóa</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'
import { daoTaoService } from '@/modules/daoTao/services/daoTaoService'

const loi = ref('')
const thongBao = ref('')
const dangTai = ref(false)
const dangLuu = ref(false)
const tuKhoa = ref('')
const locTrangThai = ref('ALL')
const phamViApDung = ref('TOAN_TRUONG')

const danhSachNgayNghi = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const form = ref(taoForm())

const dsLoaiNgayNghi = [
  { value: 'LE_CO_DINH', label: 'Lễ cố định' },
  { value: 'LE_AM_LICH', label: 'Lễ âm lịch' },
  { value: 'TET', label: 'Tết' },
  { value: 'NGHI_HE', label: 'Nghỉ hè' },
  { value: 'NGHI_GIUA_KY', label: 'Nghỉ giữa kỳ' },
  { value: 'HOAT_DONG_TRUONG', label: 'Hoạt động trường' },
  { value: 'KHAC', label: 'Khác' }
]

const danhSachKyTheoVersion = computed(() => {
  if (!form.value.chuongTrinhVersionId) return []
  return danhSachKhungKy.value.filter((item) => Number(item.chuongTrinhVersionId) === Number(form.value.chuongTrinhVersionId))
})

const danhSachHienThi = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()
  return danhSachNgayNghi.value.filter((item) => {
    const dungTuKhoa = !keyword || [item.tenNgayNghi, item.ghiChu, item.loaiNgayNghi]
        .filter(Boolean)
        .some((value) => String(value).toLowerCase().includes(keyword))

    const dungTrangThai = locTrangThai.value === 'ALL'
        || (locTrangThai.value === 'ACTIVE' && item.trangThai)
        || (locTrangThai.value === 'INACTIVE' && !item.trangThai)

    return dungTuKhoa && dungTrangThai
  })
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  loi.value = ''
  thongBao.value = ''
  dangTai.value = true
  try {
    const [ngayNghiPage, versionPage, khungKyPage] = await Promise.all([
      giangDayService.layTrangNgayNghi({ size: 500, sortBy: 'ngayBatDau', sortDir: 'asc' }),
      daoTaoService.chuongTrinhVersion.getAll({ page: 0, size: 500, sortBy: 'id', sortDir: 'desc' }),
      daoTaoService.khungKy.getAll({ page: 0, size: 1000, sortBy: 'id', sortDir: 'asc' })
    ])

    danhSachNgayNghi.value = ngayNghiPage.content || []
    danhSachVersion.value = versionPage.items || versionPage.content || []
    danhSachKhungKy.value = khungKyPage.items || khungKyPage.content || []
  } catch (error) {
    loi.value = layLoi(error, 'Không tải được dữ liệu lịch nghỉ')
  } finally {
    dangTai.value = false
  }
}

function dongBoNgayBatDau() {
  if (!form.value.ngayKetThuc) form.value.ngayKetThuc = form.value.ngayBatDau
}

function doiPhamViApDung() {
  if (phamViApDung.value === 'TOAN_TRUONG') {
    form.value.chuongTrinhVersionId = null
    form.value.khungKyId = null
  }
  if (phamViApDung.value === 'CHUONG_TRINH') {
    form.value.khungKyId = null
  }
}

function doiVersion() {
  form.value.khungKyId = null
}

async function luuNgayNghi() {
  loi.value = ''
  thongBao.value = ''
  try {
    validateForm()
    dangLuu.value = true
    const payload = taoPayload()

    if (form.value.id) {
      await giangDayService.capNhatNgayNghi(form.value.id, payload)
      thongBao.value = 'Đã cập nhật lịch nghỉ'
    } else {
      await giangDayService.taoNgayNghi(payload)
      thongBao.value = 'Đã thêm lịch nghỉ'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = layLoi(error, 'Không lưu được lịch nghỉ')
  } finally {
    dangLuu.value = false
  }
}

async function xoaNgayNghi(item) {
  if (!window.confirm(`Xóa lịch nghỉ "${item.tenNgayNghi || item.ngay || item.id}"?`)) return
  try {
    await giangDayService.xoaNgayNghi(item.id)
    thongBao.value = 'Đã xóa lịch nghỉ'
    await taiDuLieu()
  } catch (error) {
    loi.value = layLoi(error, 'Không xóa được lịch nghỉ')
  }
}

function suaNgayNghi(item) {
  form.value = {
    id: item.id || null,
    ngay: item.ngay || item.ngayBatDau || '',
    ngayBatDau: item.ngayBatDau || item.ngay || '',
    ngayKetThuc: item.ngayKetThuc || item.ngayBatDau || item.ngay || '',
    tenNgayNghi: item.tenNgayNghi || '',
    loaiNgayNghi: item.loaiNgayNghi || 'KHAC',
    lapLaiHangNam: Boolean(item.lapLaiHangNam),
    tinhLaNgayKhongHoc: item.tinhLaNgayKhongHoc !== false,
    trangThai: item.trangThai !== false,
    chuongTrinhVersionId: item.chuongTrinhVersionId || null,
    khungKyId: item.khungKyId || null,
    ghiChu: item.ghiChu || ''
  }

  if (form.value.khungKyId) phamViApDung.value = 'KHUNG_KY'
  else if (form.value.chuongTrinhVersionId) phamViApDung.value = 'CHUONG_TRINH'
  else phamViApDung.value = 'TOAN_TRUONG'

  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function resetForm() {
  form.value = taoForm()
  phamViApDung.value = 'TOAN_TRUONG'
}

function validateForm() {
  if (!form.value.tenNgayNghi) throw new Error('Phải nhập tên lịch nghỉ')
  if (!form.value.ngayBatDau) throw new Error('Phải chọn ngày bắt đầu')
  if (!form.value.ngayKetThuc) throw new Error('Phải chọn ngày kết thúc')
  if (form.value.ngayKetThuc < form.value.ngayBatDau) throw new Error('Ngày kết thúc không được trước ngày bắt đầu')
  if (phamViApDung.value !== 'TOAN_TRUONG' && !form.value.chuongTrinhVersionId) throw new Error('Phải chọn chương trình/version')
  if (phamViApDung.value === 'KHUNG_KY' && !form.value.khungKyId) throw new Error('Phải chọn kỳ áp dụng')
}

function taoPayload() {
  const payload = {
    ngay: form.value.ngayBatDau,
    ngayBatDau: form.value.ngayBatDau,
    ngayKetThuc: form.value.ngayKetThuc,
    tenNgayNghi: form.value.tenNgayNghi,
    loaiNgayNghi: form.value.loaiNgayNghi || 'KHAC',
    lapLaiHangNam: Boolean(form.value.lapLaiHangNam),
    tinhLaNgayKhongHoc: Boolean(form.value.tinhLaNgayKhongHoc),
    trangThai: Boolean(form.value.trangThai),
    chuongTrinhVersionId: phamViApDung.value === 'TOAN_TRUONG' ? null : form.value.chuongTrinhVersionId,
    khungKyId: phamViApDung.value === 'KHUNG_KY' ? form.value.khungKyId : null,
    ghiChu: form.value.ghiChu || null
  }
  return payload
}

function taoForm() {
  return {
    id: null,
    ngay: '',
    ngayBatDau: '',
    ngayKetThuc: '',
    tenNgayNghi: '',
    loaiNgayNghi: 'KHAC',
    lapLaiHangNam: false,
    tinhLaNgayKhongHoc: true,
    trangThai: true,
    chuongTrinhVersionId: null,
    khungKyId: null,
    ghiChu: ''
  }
}

function hienThiKhoangNgay(item) {
  const tuNgay = item.ngayBatDau || item.ngay || '-'
  const denNgay = item.ngayKetThuc || item.ngayBatDau || item.ngay || '-'
  return tuNgay === denNgay ? tuNgay : `${tuNgay} → ${denNgay}`
}

function tenLoaiNgayNghi(value) {
  return dsLoaiNgayNghi.find((item) => item.value === value)?.label || value || '-'
}

function hienThiPhamVi(item) {
  if (item.khungKyId) return `Theo kỳ: ${tenKyTheoId(item.khungKyId)}`
  if (item.chuongTrinhVersionId) return `Theo version: ${tenVersionTheoId(item.chuongTrinhVersionId)}`
  return 'Toàn trường'
}

function tenVersionTheoId(id) {
  const version = danhSachVersion.value.find((item) => Number(item.id) === Number(id))
  return version ? hienThiVersion(version) : `#${id}`
}

function tenKyTheoId(id) {
  const ky = danhSachKhungKy.value.find((item) => Number(item.id) === Number(id))
  return ky ? hienThiKy(ky) : `#${id}`
}

function hienThiVersion(item) {
  if (!item) return '-'
  const ma = item.maVersion || item.ma || item.id
  const ten = item.tenVersion || item.ten || item.tenChuongTrinh || ''
  return `${ma}${ten ? ' - ' + ten : ''}`
}

function hienThiKy(item) {
  if (!item) return '-'
  const ma = item.maKy || item.ma || item.id
  const ten = item.tenKy || item.ten || ''
  const khoang = item.ngayBatDau && item.ngayKetThuc ? ` (${item.ngayBatDau} → ${item.ngayKetThuc})` : ''
  return `${ma}${ten ? ' - ' + ten : ''}${khoang}`
}

function layLoi(error, fallback) {
  return error?.response?.data?.message
      || error?.response?.data?.error
      || error?.message
      || fallback
}
</script>

<style scoped>
.admin-ngay-nghi-page {
  font-family: Roboto, Arial, sans-serif;
}

.page {
  display: flex;
  flex-direction: column;
  gap: 14px;
  color: #344054;
  font-size: 14px;
}

.page-head,
.card-head,
.table-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

h1,
h2 {
  margin: 0;
  color: #101828;
}

p {
  margin: 6px 0 0;
  color: #667085;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.35fr) minmax(280px, .65fr);
  gap: 14px;
  align-items: start;
}

.card {
  border: 1px solid #eaecf0;
  border-radius: 14px;
  background: #fff;
  padding: 14px;
  box-shadow: 0 1px 2px rgba(16, 24, 40, .04);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 12px;
  margin-top: 14px;
}

.wide {
  grid-column: 1 / -1;
}

label {
  display: grid;
  gap: 6px;
  font-weight: 650;
}

label b {
  color: #b42318;
}

input,
select,
textarea {
  border: 1px solid #d0d5dd;
  border-radius: 10px;
  padding: 9px 11px;
  font-size: 14px;
  font-family: inherit;
  outline: none;
  background: #fff;
}

textarea {
  resize: vertical;
}

input:focus,
select:focus,
textarea:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, .12);
}

.toggle-card {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  padding: 10px;
  border: 1px solid #eaecf0;
  border-radius: 12px;
  background: #f9fafb;
}

.toggle-card input {
  margin-top: 3px;
}

.toggle-card span {
  display: grid;
  gap: 2px;
}

.toggle-card small,
.sub-text {
  display: block;
  margin-top: 4px;
  color: #667085;
  font-weight: 400;
}

.actions,
.row-actions,
.filter-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.actions {
  margin-top: 14px;
}

.btn {
  border: 1px solid #d0d5dd;
  border-radius: 10px;
  background: #fff;
  color: #344054;
  padding: 8px 11px;
  cursor: pointer;
  font-weight: 650;
}

.btn:disabled {
  opacity: .55;
  cursor: not-allowed;
}

.btn.primary {
  background: #2563eb;
  border-color: #2563eb;
  color: #fff;
}

.btn.danger {
  color: #b42318;
  border-color: #fecdca;
}

.alert {
  padding: 10px 12px;
  border-radius: 10px;
  font-weight: 650;
}

.alert.error {
  background: #fef3f2;
  color: #b42318;
}

.alert.success {
  background: #ecfdf3;
  color: #027a48;
}

.guide-card ul {
  margin: 12px 0;
  padding-left: 18px;
  color: #475467;
}

.guide-card li {
  margin-bottom: 8px;
}

.note {
  border-radius: 12px;
  background: #eff6ff;
  color: #1d4ed8;
  padding: 10px;
  line-height: 1.5;
}

.note code {
  display: inline-block;
  margin-top: 4px;
  padding: 2px 6px;
  border-radius: 6px;
  background: #dbeafe;
}

.table-wrap {
  margin-top: 12px;
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 10px;
  border-bottom: 1px solid #eaecf0;
  text-align: left;
  vertical-align: top;
}

.data-table th {
  color: #475467;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: .02em;
  background: #f9fafb;
}

.empty {
  text-align: center;
  color: #667085;
  padding: 24px !important;
}

.status {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 700;
}

.status.done {
  background: #ecfdf3;
  color: #027a48;
}

.status.cancel {
  background: #fef3f2;
  color: #b42318;
}

.status.warning {
  background: #fffaeb;
  color: #b54708;
}

.status.muted {
  background: #f2f4f7;
  color: #475467;
}

@media (max-width: 960px) {
  .content-grid {
    grid-template-columns: 1fr;
  }

  .page-head,
  .table-head {
    flex-direction: column;
  }

  .filter-row input,
  .filter-row select {
    width: 100%;
  }
}
</style>
