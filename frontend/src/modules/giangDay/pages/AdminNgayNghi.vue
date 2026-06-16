<template>
  <section class="page admin-ngay-nghi-page">
    <header class="page-head">
      <div class="page-title">
        <h1>Quản lý lịch nghỉ</h1>
        <p>Khai báo ngày nghỉ để hệ thống bỏ qua khi xếp lịch học.</p>
      </div>

      <button type="button" class="btn ghost" :disabled="dangTai" @click="taiDuLieu">
        {{ dangTai ? 'Đang tải...' : 'Tải lại' }}
      </button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>
    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>

    <section class="card form-card">
      <div class="card-head">
        <div>
          <h2>{{ form.id ? 'Cập nhật lịch nghỉ' : 'Thêm lịch nghỉ' }}</h2>
          <p>Nhập ngày nghỉ, phạm vi áp dụng và trạng thái sử dụng.</p>
        </div>

        <span v-if="form.id" class="editing-badge">
          Đang sửa #{{ form.id }}
        </span>
      </div>

      <div class="holiday-form">
        <div class="form-row form-row-info">
          <label class="field field-name">
            <span>Tên lịch nghỉ <b>*</b></span>
            <input
                v-model.trim="form.tenNgayNghi"
                placeholder="VD: Nghỉ lễ 30/4..."
            />
          </label>

          <label class="field">
            <span>Ngày bắt đầu <b>*</b></span>
            <input v-model="form.ngayBatDau" type="date" @change="dongBoNgayBatDau" />
          </label>

          <label class="field">
            <span>Ngày kết thúc <b>*</b></span>
            <input v-model="form.ngayKetThuc" type="date" />
          </label>

          <label class="field">
            <span>Loại ngày nghỉ</span>
            <select v-model="form.loaiNgayNghi">
              <option v-for="loai in dsLoaiNgayNghi" :key="loai.value" :value="loai.value">
                {{ loai.label }}
              </option>
            </select>
          </label>

          <label class="field">
            <span>Phạm vi</span>
            <select v-model="phamViApDung" @change="doiPhamViApDung">
              <option value="TOAN_TRUONG">Toàn trường</option>
              <option value="CHUONG_TRINH">Theo chương trình</option>
              <option value="KHUNG_KY">Theo kỳ</option>
            </select>
          </label>

          <label v-if="phamViApDung !== 'TOAN_TRUONG'" class="field">
            <span>Chương trình</span>
            <select v-model.number="form.chuongTrinhVersionId" @change="doiVersion">
              <option :value="null">-- Chọn chương trình --</option>
              <option v-for="version in danhSachVersion" :key="version.id" :value="version.id">
                {{ hienThiVersion(version) }}
              </option>
            </select>
          </label>

          <label v-if="phamViApDung === 'KHUNG_KY'" class="field">
            <span>Kỳ áp dụng</span>
            <select v-model.number="form.khungKyId">
              <option :value="null">-- Chọn kỳ --</option>
              <option v-for="ky in danhSachKyTheoVersion" :key="ky.id" :value="ky.id">
                {{ hienThiKy(ky) }}
              </option>
            </select>
          </label>

          <label class="field field-note">
            <span>Ghi chú</span>
            <input v-model.trim="form.ghiChu" placeholder="Ghi chú nội bộ..." />
          </label>
        </div>

        <div class="form-row form-row-options">
          <label class="toggle-card">
            <input v-model="form.tinhLaNgayKhongHoc" type="checkbox" />
            <span>
              <strong>Không xếp lịch học</strong>
              <small>Bỏ qua khi xếp lịch.</small>
            </span>
          </label>

          <label class="toggle-card">
            <input v-model="form.lapLaiHangNam" type="checkbox" />
            <span>
              <strong>Lặp lại hằng năm</strong>
              <small>Ngày nghỉ cố định.</small>
            </span>
          </label>

          <label class="toggle-card">
            <input v-model="form.trangThai" type="checkbox" />
            <span>
              <strong>Đang áp dụng</strong>
              <small>Tắt để lưu nháp.</small>
            </span>
          </label>

          <div class="actions">
            <button type="button" class="btn primary" :disabled="dangLuu" @click="luuNgayNghi">
              {{ dangLuu ? 'Đang lưu...' : form.id ? 'Cập nhật' : 'Thêm mới' }}
            </button>

            <button type="button" class="btn soft" @click="resetForm">
              Làm mới
            </button>
          </div>
        </div>
      </div>
    </section>

    <section class="card list-card">
      <div class="table-head">
        <div>
          <h2>Danh sách lịch nghỉ</h2>
          <p>{{ danhSachHienThi.length }} / {{ danhSachNgayNghi.length }} bản ghi</p>
        </div>

        <div class="filter-row">
          <input v-model.trim="tuKhoa" placeholder="Tìm tên, loại, ghi chú..." />
          <select v-model="locTrangThai">
            <option value="ALL">Tất cả</option>
            <option value="ACTIVE">Đang áp dụng</option>
            <option value="INACTIVE">Tạm tắt</option>
          </select>
        </div>
      </div>

      <div class="table-wrap">
        <table class="data-table">
          <thead>
          <tr>
            <th class="col-date">Khoảng nghỉ</th>
            <th class="col-name">Tên lịch nghỉ</th>
            <th class="col-type">Loại</th>
            <th class="col-scope">Phạm vi</th>
            <th class="col-block">Chặn lịch</th>
            <th class="col-status">Trạng thái</th>
            <th class="col-actions">Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="!danhSachHienThi.length">
            <td colspan="7" class="empty">Chưa có lịch nghỉ phù hợp.</td>
          </tr>

          <tr
              v-for="item in danhSachHienThi"
              :key="item.id"
              :class="[
                'data-row',
                dangChonId === item.id ? 'is-selected' : '',
                dangChonId === item.id && dangChonAction === 'edit' ? 'is-editing' : '',
                dangChonId === item.id && dangChonAction === 'delete' ? 'is-deleting' : ''
              ]"
              @click="chonDong(item)"
          >
            <td class="col-date">
              <span class="cell-main">{{ hienThiKhoangNgay(item) }}</span>
              <small v-if="item.lapLaiHangNam" class="cell-sub">Lặp lại hằng năm</small>
            </td>

            <td class="col-name">
              <span class="cell-main name-text">{{ item.tenNgayNghi || '-' }}</span>
              <small v-if="item.ghiChu" class="cell-sub">{{ item.ghiChu }}</small>
            </td>

            <td class="col-type">
              <span class="pill pill-type">{{ tenLoaiNgayNghi(item.loaiNgayNghi) }}</span>
            </td>

            <td class="col-scope">
              <span class="pill pill-scope">{{ hienThiPhamVi(item) }}</span>
            </td>

            <td class="col-block">
                <span :class="['pill', item.tinhLaNgayKhongHoc ? 'pill-warning' : 'pill-muted']">
                  {{ item.tinhLaNgayKhongHoc ? 'Có chặn' : 'Không chặn' }}
                </span>
            </td>

            <td class="col-status">
                <span :class="['pill', item.trangThai ? 'pill-active' : 'pill-off']">
                  {{ item.trangThai ? 'Đang áp dụng' : 'Tạm tắt' }}
                </span>
            </td>

            <td class="col-actions">
              <div class="row-actions">
                <button type="button" class="mini-btn edit" @click.stop="suaNgayNghi(item)">
                  Sửa
                </button>

                <button type="button" class="mini-btn delete" @click.stop="xoaNgayNghi(item)">
                  Xóa
                </button>
              </div>
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
  --pn: #077149;
  --pn-900: #033924;
  --pn-800: #044b31;
  --pn-700: #05583a;
  --pn-600: #066842;
  --pn-100: #e7f5ef;
  --pn-050: #f3fbf7;

  --text: #102019;
  --muted: #60736b;
  --line: #d8e7df;
  --line-soft: #edf5f1;
  --card: #ffffff;
  --bg: #f5faf7;

  --danger: #b42318;
  --warning: #b54708;
  --amber-bg: #fff8e6;
  --red-bg: #fff1f2;

  --shadow: 0 4px 12px rgba(15, 23, 42, .035);
  --radius: 12px;

  width: 100%;
  max-width: 100%;
  min-width: 0;
  color: var(--text);
  font-family: Roboto, Arial, sans-serif;
  font-size: clamp(9px, .64vw, 12px);
  line-height: 1.25;
  overflow-x: hidden;
}

.admin-ngay-nghi-page *,
.admin-ngay-nghi-page *::before,
.admin-ngay-nghi-page *::after {
  box-sizing: border-box;
}

.admin-ngay-nghi-page button,
.admin-ngay-nghi-page input,
.admin-ngay-nghi-page select,
.admin-ngay-nghi-page textarea {
  font-family: inherit;
}

.page {
  display: flex;
  flex-direction: column;
  gap: 7px;
  min-width: 0;
  width: 100%;
  max-width: 100%;
  padding: 7px;
  background: var(--bg);
  overflow-x: hidden;
}

/* HEADER */
.page-head {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
  min-width: 0;
  min-height: 44px;
  padding: 7px 10px;
  border: 1px solid var(--line);
  border-left: 4px solid var(--pn);
  border-radius: var(--radius);
  background: linear-gradient(135deg, #ffffff 0%, var(--pn-050) 100%);
  box-shadow: var(--shadow);
  overflow: hidden;
}

.page-title,
.card-head > div,
.table-head > div {
  min-width: 0;
  overflow: hidden;
}

h1,
h2,
p {
  margin: 0;
}

h1 {
  color: var(--pn-800);
  font-size: clamp(17px, 1.42vw, 25px);
  font-weight: 900;
  line-height: 1.1;
  letter-spacing: -.025em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

h2 {
  color: #101828;
  font-size: clamp(14px, 1vw, 19px);
  font-weight: 900;
  line-height: 1.1;
  letter-spacing: -.02em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

p {
  margin-top: 2px;
  color: var(--muted);
  font-size: clamp(8.5px, .58vw, 10.8px);
  font-weight: 600;
  line-height: 1.28;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* CARD */
.card {
  width: 100%;
  max-width: 100%;
  min-width: 0;
  border: 1px solid var(--line);
  border-radius: var(--radius);
  background: var(--card);
  box-shadow: var(--shadow);
  overflow: hidden;
}

.form-card,
.list-card {
  padding: 8px;
}

.card-head,
.table-head {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
  min-width: 0;
  margin-bottom: 7px;
  padding-bottom: 6px;
  border-bottom: 1px solid var(--line-soft);
  overflow: hidden;
}

.editing-badge {
  flex: 0 0 auto;
  max-width: 160px;
  padding: 4px 8px;
  border: 1px solid #bfe8d4;
  border-radius: 999px;
  background: var(--pn-100);
  color: var(--pn-700);
  font-size: clamp(8px, .52vw, 10px);
  font-weight: 900;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* FORM */
.holiday-form {
  display: grid;
  gap: 6px;
  min-width: 0;
  width: 100%;
  overflow: hidden;
}

.form-row {
  display: grid;
  gap: 6px;
  min-width: 0;
  overflow: hidden;
}

.form-row-info {
  grid-template-columns:
    minmax(0, 1.55fr)
    minmax(0, .9fr)
    minmax(0, .9fr)
    minmax(0, .95fr)
    minmax(0, .95fr)
    minmax(0, 1.1fr);
  align-items: end;
}

.form-row-options {
  grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
  align-items: stretch;
}

.field,
.toggle-card {
  min-width: 0;
  overflow: hidden;
}

.field {
  display: grid;
  gap: 3px;
  font-weight: 800;
}

.field span {
  min-width: 0;
  color: #52655d;
  font-size: clamp(7px, .48vw, 8.8px);
  font-weight: 900;
  line-height: 1;
  text-transform: uppercase;
  letter-spacing: .018em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.field b {
  color: var(--danger);
}

input,
select,
textarea {
  width: 100%;
  min-width: 0;
  height: clamp(25px, 1.9vw, 30px);
  border: 1px solid var(--line);
  border-radius: 8px;
  padding: 4px 7px;
  background: #fbfdfc;
  color: var(--text);
  font-size: clamp(8.8px, .58vw, 11px);
  font-weight: 700;
  line-height: 1.15;
  outline: none;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: border-color .14s ease, box-shadow .14s ease, background .14s ease;
}

textarea {
  resize: vertical;
}

input::placeholder,
textarea::placeholder {
  color: #9aa9a2;
  font-weight: 600;
}

input:focus,
select:focus,
textarea:focus {
  border-color: var(--pn);
  background: #ffffff;
  box-shadow: 0 0 0 2px rgba(7, 113, 73, .12);
}

.toggle-card {
  display: grid;
  grid-template-columns: auto minmax(0, 1fr);
  align-items: center;
  gap: 6px;
  min-height: 34px;
  padding: 6px 7px;
  border: 1px solid var(--line);
  border-radius: 9px;
  background: linear-gradient(180deg, #ffffff 0%, #fbfdfc 100%);
  cursor: pointer;
}

.toggle-card:hover {
  border-color: #bfe8d4;
  background: var(--pn-050);
}

.toggle-card input {
  width: 14px;
  height: 14px;
  min-height: auto;
  padding: 0;
  accent-color: var(--pn);
}

.toggle-card span {
  display: grid;
  gap: 1px;
  min-width: 0;
  overflow: hidden;
}

.toggle-card strong,
.toggle-card small {
  min-width: 0;
  display: block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.toggle-card strong {
  color: var(--text);
  font-size: clamp(8.8px, .58vw, 11px);
  font-weight: 900;
}

.toggle-card small {
  color: var(--muted);
  font-size: clamp(7.6px, .5vw, 9.3px);
  font-weight: 650;
}

/* BUTTONS */
.actions,
.row-actions,
.filter-row {
  display: flex;
  align-items: center;
  gap: 5px;
  min-width: 0;
  white-space: nowrap;
  overflow: hidden;
}

.actions {
  justify-content: flex-end;
  align-self: center;
}

.btn,
.mini-btn {
  flex: 0 0 auto;
  min-width: 0;
  border: 1px solid var(--line);
  border-radius: 8px;
  background: #ffffff;
  color: #2d4138;
  cursor: pointer;
  font-family: inherit;
  font-weight: 900;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  transition: background .14s ease, border-color .14s ease, color .14s ease, box-shadow .14s ease;
}

.btn {
  height: clamp(25px, 1.9vw, 30px);
  padding: 0 8px;
  font-size: clamp(8.8px, .56vw, 10.8px);
}

.mini-btn {
  height: clamp(22px, 1.65vw, 26px);
  padding: 0 7px;
  font-size: clamp(8px, .52vw, 10px);
}

.btn:hover:not(:disabled),
.mini-btn:hover:not(:disabled) {
  border-color: #bfe8d4;
  background: var(--pn-050);
  color: var(--pn-700);
  box-shadow: 0 4px 10px rgba(7, 113, 73, .08);
}

.btn:disabled {
  opacity: .55;
  cursor: not-allowed;
}

.btn.primary {
  border-color: var(--pn);
  background: var(--pn);
  color: #ffffff;
}

.btn.primary:hover:not(:disabled) {
  border-color: var(--pn-700);
  background: var(--pn-700);
  color: #ffffff;
}

.btn.soft,
.btn.ghost {
  border-color: #bfe8d4;
  background: var(--pn-100);
  color: var(--pn-700);
}

.mini-btn.edit {
  border-color: #bfe8d4;
  background: var(--pn-100);
  color: var(--pn-700);
}

.mini-btn.delete {
  border-color: #fecaca;
  background: #fff5f5;
  color: var(--danger);
}

.mini-btn.delete:hover:not(:disabled) {
  border-color: #fca5a5;
  background: #fee2e2;
  color: #991b1b;
}

/* ALERT */
.alert {
  padding: 7px 9px;
  border-radius: 9px;
  font-size: clamp(8.8px, .56vw, 10.8px);
  font-weight: 900;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.alert.error {
  border: 1px solid #fecaca;
  background: #fff5f5;
  color: #b91c1c;
}

.alert.success {
  border: 1px solid #bfe8d4;
  background: #eaf8f1;
  color: var(--pn);
}

/* LIST HEAD */
.table-head {
  margin-bottom: 6px;
}

.filter-row {
  justify-content: flex-end;
  flex: 0 1 390px;
}

.filter-row input {
  max-width: 260px;
}

.filter-row select {
  max-width: 118px;
}

/* TABLE */
.table-wrap {
  width: 100%;
  max-width: 100%;
  min-width: 0;
  overflow-x: hidden;
  overflow-y: auto;
  border: 1px solid var(--line-soft);
  border-radius: 10px;
  background: #ffffff;
}

.data-table {
  width: 100%;
  min-width: 0 !important;
  table-layout: fixed;
  border-collapse: separate;
  border-spacing: 0;
}

.data-table th,
.data-table td {
  min-width: 0;
  height: clamp(32px, 2.45vw, 39px);
  padding: 5px 5px;
  border-bottom: 1px solid #edf4f0;
  text-align: left;
  vertical-align: middle;
  line-height: 1.14;
  overflow: hidden;
  text-overflow: ellipsis;
}

.data-table th {
  position: sticky;
  top: 0;
  z-index: 2;
  color: #445b51;
  font-size: clamp(6.8px, .46vw, 8.8px);
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: .012em;
  white-space: normal;
  word-break: break-word;
  border-bottom: 1px solid var(--line);
}

.data-table td {
  color: #14251d;
  font-size: clamp(8.8px, .56vw, 11.2px);
  font-weight: 650;
  white-space: nowrap;
  word-break: normal;
}

.data-table tr:last-child td {
  border-bottom: 0;
}

/* COLUMN WIDTHS */
.col-date {
  width: 15%;
}

.col-name {
  width: 22%;
}

.col-type {
  width: 13%;
}

.col-scope {
  width: 18%;
}

.col-block {
  width: 11%;
}

.col-status {
  width: 12%;
}

.col-actions {
  width: 9%;
  text-align: right;
}

/* COLUMN COLORS - cùng tông xanh chủ đạo */
th.col-date,
td.col-date {
  background: #f7fbff;
}

th.col-name,
td.col-name {
  background: #fbfefc;
}

th.col-type,
td.col-type {
  background: #f6fbf8;
}

th.col-scope,
td.col-scope {
  background: #f3fbf7;
}

th.col-block,
td.col-block {
  background: #fffdf5;
}

th.col-status,
td.col-status {
  background: #eaf8f1;
}

th.col-actions,
td.col-actions {
  background: #f8fafc;
}

.data-row {
  cursor: pointer;
  transition: background .12s ease, box-shadow .12s ease, outline-color .12s ease;
}

.data-row:hover td {
  background: var(--pn-050) !important;
}

.data-row.is-selected td {
  background: #e4f5ed !important;
  color: #102019;
  box-shadow:
      inset 0 1px 0 rgba(7, 113, 73, .10),
      inset 0 -1px 0 rgba(7, 113, 73, .10);
}

.data-row.is-selected td:first-child {
  box-shadow:
      inset 4px 0 0 var(--pn),
      inset 0 1px 0 rgba(7, 113, 73, .10),
      inset 0 -1px 0 rgba(7, 113, 73, .10);
}

.data-row.is-editing td {
  background: #d4f0e2 !important;
  color: #08291b;
  outline: 1px solid rgba(7, 113, 73, .22);
  outline-offset: -1px;
}

.data-row.is-editing td:first-child {
  box-shadow:
      inset 6px 0 0 var(--pn-700),
      inset 0 1px 0 rgba(7, 113, 73, .18),
      inset 0 -1px 0 rgba(7, 113, 73, .18);
}

.data-row.is-deleting td {
  background: #fff1f2 !important;
  color: #391313;
  outline: 1px solid rgba(180, 35, 24, .18);
  outline-offset: -1px;
}

.data-row.is-deleting td:first-child {
  box-shadow:
      inset 6px 0 0 var(--danger),
      inset 0 1px 0 rgba(180, 35, 24, .14),
      inset 0 -1px 0 rgba(180, 35, 24, .14);
}

/* CELL TEXT */
.cell-main,
.cell-sub,
.pill {
  max-width: 100%;
  min-width: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.cell-main {
  display: block;
  color: #102019;
  font-size: clamp(8.8px, .58vw, 11.3px);
  font-weight: 900;
}

.name-text {
  color: var(--pn-700);
}

.cell-sub {
  display: block;
  margin-top: 2px;
  color: var(--muted);
  font-size: clamp(7.6px, .5vw, 9.6px);
  font-weight: 700;
}

/* PILLS */
.pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: clamp(20px, 1.55vw, 24px);
  padding: 0 6px;
  border-radius: 999px;
  border: 1px solid;
  font-size: clamp(7.8px, .5vw, 9.6px);
  font-weight: 900;
  line-height: 1;
  text-align: center;
}

.pill-type {
  border-color: #c9ead8;
  background: #f0faf5;
  color: #05603a;
}

.pill-scope {
  border-color: #bfe8d4;
  background: #eaf8f1;
  color: var(--pn-700);
}

.pill-warning {
  border-color: #fde68a;
  background: #fff8e6;
  color: #b45309;
}

.pill-muted {
  border-color: #d0d5dd;
  background: #f2f4f7;
  color: #475467;
}

.pill-active {
  border-color: #bfe8d4;
  background: #eaf8f1;
  color: var(--pn);
}

.pill-off {
  border-color: #fecaca;
  background: #fff5f5;
  color: #b91c1c;
}

/* ACTION COLUMN */
.row-actions {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  justify-content: end;
  gap: 3px;
}

.col-actions .row-actions {
  justify-items: end;
}

/* EMPTY */
.empty {
  height: 42px !important;
  padding: 12px !important;
  color: var(--muted);
  text-align: center !important;
  font-weight: 900;
  background: #ffffff !important;
}

/* ZOOM / RESPONSIVE SAFE */
@media (max-width: 1380px) {
  .form-row-info {
    grid-template-columns:
      minmax(0, 1.45fr)
      minmax(0, .85fr)
      minmax(0, .85fr)
      minmax(0, .9fr)
      minmax(0, .9fr)
      minmax(0, 1fr);
  }

  .form-row-options {
    grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
  }

  .data-table th,
  .data-table td {
    padding-left: 4px;
    padding-right: 4px;
  }

  .pill {
    padding: 0 5px;
  }
}

@media (max-width: 1160px) {
  .form-row-info {
    grid-template-columns:
      minmax(0, 1.38fr)
      minmax(0, .82fr)
      minmax(0, .82fr)
      minmax(0, .86fr)
      minmax(0, .86fr)
      minmax(0, .94fr);
  }

  .form-row-options {
    grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
  }

  .toggle-card small {
    display: none;
  }

  .filter-row {
    flex: 0 1 340px;
  }

  .filter-row input {
    max-width: 220px;
  }

  .filter-row select {
    max-width: 110px;
  }

  .col-date {
    width: 15%;
  }

  .col-name {
    width: 21%;
  }

  .col-type {
    width: 13%;
  }

  .col-scope {
    width: 17%;
  }

  .col-block {
    width: 11%;
  }

  .col-status {
    width: 13%;
  }

  .col-actions {
    width: 10%;
  }
}

@media (max-width: 900px) {
  .page {
    padding: 6px;
    gap: 6px;
  }

  .page-head,
  .card-head,
  .table-head {
    grid-template-columns: minmax(0, 1fr) auto;
    gap: 6px;
  }

  h1 {
    font-size: clamp(15px, 2.8vw, 19px);
  }

  h2 {
    font-size: clamp(13px, 2.4vw, 16px);
  }

  .page-title p,
  .card-head p,
  .table-head p {
    display: none;
  }

  .form-card,
  .list-card {
    padding: 7px;
  }

  .form-row-info {
    grid-template-columns:
      minmax(0, 1.25fr)
      minmax(0, .82fr)
      minmax(0, .82fr)
      minmax(0, .82fr)
      minmax(0, .82fr)
      minmax(0, .95fr);
    gap: 5px;
  }

  .form-row-options {
    grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
    gap: 5px;
  }

  .toggle-card {
    justify-content: center;
    padding: 5px;
  }

  .toggle-card span {
    display: none;
  }

  .filter-row {
    flex: 0 1 270px;
    gap: 4px;
  }

  .filter-row input {
    max-width: 170px;
  }

  .filter-row select {
    max-width: 92px;
  }

  .data-table th,
  .data-table td {
    height: 32px;
    padding: 4px 3px;
  }

  .cell-sub {
    display: none;
  }

  .mini-btn {
    padding: 0 5px;
  }

  .row-actions {
    gap: 2px;
  }

  .col-date {
    width: 14%;
  }

  .col-name {
    width: 20%;
  }

  .col-type {
    width: 13%;
  }

  .col-scope {
    width: 16%;
  }

  .col-block {
    width: 11%;
  }

  .col-status {
    width: 13%;
  }

  .col-actions {
    width: 13%;
  }
}

@media (max-width: 680px) {
  .admin-ngay-nghi-page {
    font-size: 8.8px;
  }

  .page {
    padding: 5px;
    gap: 5px;
  }

  .page-head {
    min-height: 38px;
    padding: 6px 7px;
    border-radius: 10px;
  }

  .form-card,
  .list-card {
    padding: 6px;
    border-radius: 10px;
  }

  .card-head,
  .table-head {
    margin-bottom: 5px;
    padding-bottom: 5px;
  }

  .editing-badge {
    max-width: 94px;
    padding: 3px 6px;
  }

  .form-row-info {
    grid-template-columns:
      minmax(0, 1.18fr)
      minmax(0, .8fr)
      minmax(0, .8fr)
      minmax(0, .8fr)
      minmax(0, .8fr)
      minmax(0, .9fr);
    gap: 4px;
  }

  .form-row-options {
    grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
    gap: 4px;
  }

  .field span {
    display: none;
  }

  input,
  select {
    height: 26px;
    padding: 3px 5px;
    border-radius: 7px;
  }

  .toggle-card {
    min-height: 27px;
    padding: 4px;
    border-radius: 7px;
  }

  .toggle-card input {
    width: 13px;
    height: 13px;
  }

  .btn {
    height: 26px;
    padding: 0 6px;
  }

  .mini-btn {
    height: 22px;
    padding: 0 4px;
  }

  .filter-row {
    flex: 0 1 210px;
    gap: 3px;
  }

  .filter-row input {
    max-width: 130px;
  }

  .filter-row select {
    max-width: 76px;
  }

  .table-wrap {
    border-radius: 8px;
  }

  .data-table th,
  .data-table td {
    height: 29px;
    padding: 3px 2px;
  }

  .data-table th {
    font-size: 7px;
    line-height: 1.08;
  }

  .cell-main {
    font-size: 8.8px;
  }

  .pill {
    height: 19px;
    padding: 0 4px;
    font-size: 7.4px;
  }

  .col-date {
    width: 14%;
  }

  .col-name {
    width: 19%;
  }

  .col-type {
    width: 13%;
  }

  .col-scope {
    width: 16%;
  }

  .col-block {
    width: 11%;
  }

  .col-status {
    width: 13%;
  }

  .col-actions {
    width: 14%;
  }
}

@media (max-width: 520px) {
  .admin-ngay-nghi-page {
    font-size: 8px;
  }

  .page-head {
    grid-template-columns: minmax(0, 1fr) auto;
  }

  .page-head .btn {
    max-width: 58px;
  }

  .form-row-info {
    grid-template-columns:
      minmax(0, 1.15fr)
      minmax(0, .78fr)
      minmax(0, .78fr)
      minmax(0, .78fr)
      minmax(0, .78fr)
      minmax(0, .85fr);
    gap: 3px;
  }

  .form-row-options {
    grid-template-columns: repeat(3, minmax(0, 1fr)) auto;
    gap: 3px;
  }

  .actions {
    gap: 3px;
  }

  .btn {
    max-width: 66px;
    padding-inline: 4px;
  }

  .actions .btn {
    max-width: 58px;
  }

  .filter-row input {
    max-width: 112px;
  }

  .filter-row select {
    max-width: 66px;
  }

  .data-table th,
  .data-table td {
    padding-left: 2px;
    padding-right: 2px;
  }

  .col-date {
    width: 14%;
  }

  .col-name {
    width: 18%;
  }

  .col-type {
    width: 13%;
  }

  .col-scope {
    width: 15%;
  }

  .col-block {
    width: 11%;
  }

  .col-status {
    width: 14%;
  }

  .col-actions {
    width: 15%;
  }

  .mini-btn {
    max-width: 27px;
    padding-inline: 3px;
  }
}
</style>