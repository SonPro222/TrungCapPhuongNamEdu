<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Phân công giảng dạy</h1>
        <p>Gắn giáo viên vào lớp học phần theo vai trò giảng dạy. Chọn chuyên môn trước, sau đó chọn giáo viên theo mã và email.</p>
      </div>

      <div class="head-actions">
        <button v-if="coFlowLopHocPhan" type="button" class="btn" @click="quayLaiLopHocPhan">
          Quay lại điều phối
        </button>

        <button type="button" class="btn" @click="taiDuLieu">
          Tải lại
        </button>
      </div>
    </header>

    <section class="filter-card">
      <label>
        <span>Lọc lớp học phần</span>
        <input
            v-model.trim="boLoc.keywordLop"
            type="text"
            placeholder="Mã lớp hoặc tên lớp"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Lọc giáo viên</span>
        <input
            v-model.trim="boLoc.keywordGiaoVien"
            type="text"
            placeholder="Mã giáo viên, email hoặc tên giáo viên"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Vai trò</span>
        <select v-model="boLoc.vaiTro" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="giang_vien_chinh">Giảng viên chính</option>
          <option value="tro_giang">Trợ giảng</option>
          <option value="coi_thi">Coi thi</option>
          <option value="cham_thi">Chấm thi</option>
        </select>
      </label>

      <label>
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="id">ID</option>
          <option value="lopHocPhanId">Lớp học phần</option>
          <option value="giaoVienId">Giáo viên</option>
          <option value="vaiTro">Vai trò</option>
          <option value="createdAt">Ngày tạo</option>
        </select>
      </label>

      <label>
        <span>Chiều</span>
        <select v-model="boLoc.sortDir" @change="locDuLieu">
          <option value="desc">Giảm dần</option>
          <option value="asc">Tăng dần</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <section class="form-card">
      <h2>{{ form.id ? 'Cập nhật phân công' : 'Thêm phân công' }}</h2>

      <form class="form-grid" @submit.prevent="luuPhanCong">
        <label>
          <span>Lớp học phần</span>
          <select v-model="form.lopHocPhanId" required :disabled="Boolean(route.query.lopHocPhanId)">
            <option value="">-- Chọn lớp học phần --</option>
            <option
                v-for="lop in danhSachLopHocPhan"
                :key="lop.id"
                :value="lop.id"
            >
              {{ lop.maLop || ('LHP' + lop.id) }} - {{ lop.tenLop || 'Chưa có tên' }}
              | Sĩ số: {{ lop.soLuongHienTai || 0 }}/{{ lop.soLuongToiDa || 0 }}
              | Buổi: {{ lop.soBuoiHoc || 0 }}
            </option>
          </select>
          <small>Chọn lớp cần phân công giáo viên, không nhập ID thủ công.</small>
        </label>

        <label>
          <span>Chuyên môn giáo viên</span>
          <select v-model="form.chuyenMon" required @change="doiChuyenMon">
            <option value="">-- Chọn chuyên môn --</option>
            <option
                v-for="chuyenMon in danhSachChuyenMonGiaoVien"
                :key="chuyenMon"
                :value="chuyenMon"
            >
              {{ chuyenMon }}
            </option>
          </select>
          <small>Chọn chuyên môn trước, sau đó mới chọn giáo viên.</small>
        </label>

        <label>
          <span>Giáo viên</span>
          <select v-model="form.giaoVienId" required :disabled="!form.chuyenMon">
            <option value="">-- Chọn giáo viên --</option>
            <option
                v-for="gv in danhSachGiaoVienTheoChuyenMon"
                :key="gv.id"
                :value="gv.id"
            >
              {{ hienThiGiaoVienOption(gv) }}
            </option>
          </select>
          <small>{{ moTaGiaoVienDangChon }}</small>
        </label>

        <label>
          <span>Vai trò</span>
          <select v-model="form.vaiTro" required>
            <option value="giang_vien_chinh">Giảng viên chính</option>
            <option value="tro_giang">Trợ giảng</option>
            <option value="coi_thi">Coi thi</option>
            <option value="cham_thi">Chấm thi</option>
          </select>
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary">
            {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
          </button>

          <button type="button" class="btn" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section class="summary-grid">
      <div>
        <span>Đang hiển thị</span>
        <strong>{{ danhSach.length }}</strong>
      </div>

      <div>
        <span>Tổng bản ghi</span>
        <strong>{{ tongBanGhi }}</strong>
      </div>

      <div>
        <span>Trang</span>
        <strong>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</strong>
      </div>
    </section>

    <div class="pagination-bar">
      <button
          type="button"
          class="btn"
          :disabled="trangHienTai <= 0 || dangTai"
          @click="doiTrang(trangHienTai - 1)"
      >
        Trang trước
      </button>

      <button
          type="button"
          class="btn"
          :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
          @click="doiTrang(trangHienTai + 1)"
      >
        Trang sau
      </button>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
        <tr>
          <th>ID</th>
          <th>Lớp học phần</th>
          <th>Giáo viên</th>
          <th>Vai trò</th>
          <th>Ngày tạo</th>
          <th>Thao tác</th>
        </tr>
        </thead>

        <tbody>
        <tr v-if="dangTai">
          <td colspan="6">Đang tải dữ liệu...</td>
        </tr>

        <tr v-else-if="!danhSach.length">
          <td colspan="6">Không có dữ liệu</td>
        </tr>

        <tr v-for="item in danhSach" v-else :key="item.id">
          <td>{{ item.id }}</td>

          <td>
            <div class="cell-main">
              <strong>{{ item.maLop || '-' }}</strong>
              <span>{{ item.tenLop || '-' }}</span>
              <small>ID: {{ item.lopHocPhanId }}</small>
            </div>
          </td>

          <td>
            <div class="cell-main">
              <strong>{{ item.maGiaoVien || '-' }}</strong>
              <span>{{ item.emailGiaoVien || item.email || layEmailGiaoVien(item.giaoVienId) || '-' }}</span>
              <small>{{ item.tenGiaoVien || layTenGiaoVien(item.giaoVienId) || '-' }}</small>
            </div>
          </td>

          <td>{{ hienThiVaiTro(item.vaiTro) }}</td>
          <td>{{ item.createdAt || '-' }}</td>

          <td>
            <div class="row-actions">
              <button type="button" class="btn small" @click="chonSua(item)">
                Sửa
              </button>

              <button type="button" class="btn small danger" @click="xoaPhanCong(item)">
                Xóa
              </button>
            </div>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import {computed, onMounted, reactive, ref} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {giangDayService} from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

const danhSach = ref([])
const danhSachLopHocPhan = ref([])
const danhSachGiaoVien = ref([])
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const boLoc = reactive({
  keywordLop: '',
  keywordGiaoVien: '',
  vaiTro: '',
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  lopHocPhanId: '',
  chuyenMon: '',
  giaoVienId: '',
  vaiTro: 'giang_vien_chinh'
})

const coFlowLopHocPhan = computed(() => Boolean(route.query.lopHocPhanId))

const danhSachGiaoVienDangDay = computed(() => {
  return danhSachGiaoVien.value.filter((gv) => gv.trangThai === 'dang_day')
})

const danhSachChuyenMonGiaoVien = computed(() => {
  const set = new Set()

  danhSachGiaoVienDangDay.value.forEach((gv) => {
    set.add(chuanHoaChuyenMon(gv.chuyenMon))
  })

  return [...set].sort((a, b) => a.localeCompare(b))
})

const danhSachGiaoVienTheoChuyenMon = computed(() => {
  if (!form.chuyenMon) return []

  return danhSachGiaoVienDangDay.value.filter((gv) => {
    return chuanHoaChuyenMon(gv.chuyenMon) === form.chuyenMon
  })
})

const giaoVienDangChon = computed(() => {
  return danhSachGiaoVien.value.find((gv) => String(gv.id) === String(form.giaoVienId)) || null
})

const moTaGiaoVienDangChon = computed(() => {
  if (!form.chuyenMon) return 'Chọn chuyên môn để lọc giáo viên đang dạy.'
  if (!form.giaoVienId) return 'Không nhập ID giáo viên. Chọn giáo viên bằng mã giáo viên và email.'

  const gv = giaoVienDangChon.value
  if (!gv) return 'Giáo viên không nằm trong danh sách đang dạy.'

  return `${gv.maGiaoVien || '-'} - ${gv.email || 'Chưa có email'} - ${gv.hoTen || 'Chưa có tên'}`
})
async function taiDuLieuNen() {
  try {
    const [lopHocPhan, giaoVien] = await Promise.all([
      giangDayService.layDanhSachLopHocPhan(),
      giangDayService.layDanhSachGiaoVien()
    ])

    danhSachLopHocPhan.value = lopHocPhan
    danhSachGiaoVien.value = giaoVien
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu lớp học phần / giáo viên'
  }
}
onMounted(async () => {
  if (route.query.lopHocPhanId) {
    form.lopHocPhanId = route.query.lopHocPhanId
  }

  await taiDuLieuNen()
  await taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangPhanCongGiangDay({
      page: trangHienTai.value,
      size: kichThuocTrang,
      lopHocPhanId: route.query.lopHocPhanId || undefined,
      keywordLop: boLoc.keywordLop || undefined,
      keywordGiaoVien: boLoc.keywordGiaoVien || undefined,
      vaiTro: boLoc.vaiTro || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    const content = route.query.lopHocPhanId
        ? page.content.filter((item) => String(item.lopHocPhanId) === String(route.query.lopHocPhanId))
        : page.content

    danhSach.value = content
    trangHienTai.value = page.number
    tongTrang.value = route.query.lopHocPhanId ? 1 : page.totalPages
    tongBanGhi.value = route.query.lopHocPhanId ? content.length : page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách phân công'
  } finally {
    dangTai.value = false
  }
}

async function luuPhanCong() {
  const payload = {
    lopHocPhanId: Number(form.lopHocPhanId),
    giaoVienId: Number(form.giaoVienId),
    vaiTro: form.vaiTro
  }

  try {
    if (form.id) {
      await giangDayService.capNhatPhanCongGiangDay(form.id, payload)
      thongBao.value = 'Cập nhật phân công thành công'
    } else {
      await giangDayService.taoPhanCongGiangDay(payload)
      thongBao.value = 'Thêm phân công thành công'
    }

    resetForm()
    await taiDuLieu()

    if (route.query.lopHocPhanId) {
      quayLaiLopHocPhan()
    }
  } catch (error) {
    loi.value = error?.message || 'Lưu phân công thất bại'
  }
}

function chonSua(item) {
  const gv = danhSachGiaoVien.value.find((row) => String(row.id) === String(item.giaoVienId))

  form.id = item.id
  form.lopHocPhanId = item.lopHocPhanId || ''
  form.chuyenMon = chuanHoaChuyenMon(gv?.chuyenMon || item.chuyenMon)
  form.giaoVienId = item.giaoVienId || ''
  form.vaiTro = item.vaiTro || 'giang_vien_chinh'
}

async function xoaPhanCong(item) {
  const ok = window.confirm(`Xóa phân công ${item.maGiaoVien || ''} - ${item.tenGiaoVien || ''} khỏi lớp ${item.maLop || ''}?`)
  if (!ok) return

  try {
    await giangDayService.xoaPhanCongGiangDay(item.id)
    thongBao.value = 'Xóa phân công thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa phân công thất bại'
  }
}
function resetForm() {
  form.id = null
  form.lopHocPhanId = route.query.lopHocPhanId || ''
  form.chuyenMon = ''
  form.giaoVienId = ''
  form.vaiTro = 'giang_vien_chinh'
}
function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keywordLop = ''
  boLoc.keywordGiaoVien = ''
  boLoc.vaiTro = ''
  boLoc.sortBy = 'id'
  boLoc.sortDir = 'desc'
  trangHienTai.value = 0
  taiDuLieu()
}

function quayLaiLopHocPhan() {
  if (route.query.lopHocPhanId) {
    router.push({
      name: 'GiangDay.ChiTietLopHocPhan',
      params: { id: route.query.lopHocPhanId },
      query: {
        nganhId: route.query.nganhId,
        chuongTrinhId: route.query.chuongTrinhId,
        versionId: route.query.versionId,
        khungKyId: route.query.khungKyId
      }
    })
    return
  }

  router.push({ name: 'GiangDay.LopHocPhan' })
}

function doiTrang(page) {
  if (page < 0) return
  if (tongTrang.value && page >= tongTrang.value) return

  trangHienTai.value = page
  taiDuLieu()
}

function doiChuyenMon() {
  form.giaoVienId = ''
}

function chuanHoaChuyenMon(value) {
  const text = String(value || '').trim()
  return text || 'Chưa có chuyên môn'
}

function hienThiGiaoVienOption(gv) {
  if (!gv) return '-'

  const ma = gv.maGiaoVien || `GV${gv.id}`
  const email = gv.email || 'Chưa có email'
  const ten = gv.hoTen || gv.tenGiaoVien || 'Chưa có tên'

  return `${ma} - ${email} | ${ten}`
}

function layGiaoVienTheoId(id) {
  return danhSachGiaoVien.value.find((gv) => String(gv.id) === String(id)) || null
}

function layEmailGiaoVien(id) {
  return layGiaoVienTheoId(id)?.email || ''
}

function layTenGiaoVien(id) {
  const gv = layGiaoVienTheoId(id)
  return gv?.hoTen || gv?.tenGiaoVien || ''
}

function hienThiVaiTro(value) {
  const map = {
    giang_vien_chinh: 'Giảng viên chính',
    tro_giang: 'Trợ giảng',
    coi_thi: 'Coi thi',
    cham_thi: 'Chấm thi'
  }

  return map[value] || value || '-'
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
}

.head-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.page-head h1 {
  margin: 0;
}

.page-head p {
  margin: 6px 0 0;
  color: #64748b;
}

.filter-card, .form-card {
  padding: 16px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
}

.filter-card {
  display: grid;
  grid-template-columns: repeat(5, minmax(140px, 1fr)) auto;
  gap: 12px;
}

.form-card h2 {
  margin: 0 0 12px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(160px, 1fr));
  gap: 12px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

label span {
  font-size: 13px;
  font-weight: 700;
  color: #334155;
}

label small {
  color: #64748b;
  font-size: 12px;
}

input, select, textarea {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 8px 10px;
}

.filter-actions, .form-actions, .row-actions, .pagination-bar {
  display: flex;
  gap: 8px;
}

.filter-actions {
  align-items: flex-end;
}

.form-actions {
  grid-column: 1 / -1;
}

.pagination-bar {
  justify-content: flex-end;
}

.btn {
  border: 1px solid var(--color-border);
  background: var(--color-white);
  color: var(--color-text);
  border-radius: var(--radius);
  padding: 8px 12px;
  cursor: pointer;
  font-weight: 700;
}

.btn.primary {
  background: var(--color-primary);
  color: var(--color-white);
  border-color: var(--color-primary);
}

.btn.small {
  padding: 6px 10px;
  font-size: 12px;
}

.btn.danger {
  color: #b91c1c;
  border-color: #fecaca;
  background: #fff5f5;
}

.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
}

.alert {
  padding: 10px 12px;
  border-radius: var(--radius);
}

.alert.success {
  background: #ecfdf5;
  color: #047857;
}

.alert.error {
  background: #fee2e2;
  color: #991b1b;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
}

.summary-grid div {
  border: 1px solid var(--color-border);
  background: var(--color-white);
  border-radius: var(--radius);
  padding: 14px;
  display: grid;
  gap: 6px;
}

.summary-grid span {
  color: #64748b;
  font-weight: 700;
  font-size: 13px;
}

.summary-grid strong {
  font-size: 22px;
}

.cell-main {
  display: grid;
  gap: 4px;
}

.cell-main strong {
  color: #0f172a;
}

.cell-main span {
  color: #334155;
}

.cell-main small {
  color: #64748b;
  font-size: 12px;
}

.table-wrap {
  overflow: auto;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1000px;
}

th, td {
  padding: 10px 12px;
  border-bottom: 1px solid var(--color-border);
  text-align: left;
  vertical-align: top;
}

th {
  background: #f8fafc;
  color: #475569;
  font-size: 12px;
  text-transform: uppercase;
}

@media (max-width: 1100px) {
  .filter-card, .form-grid, .summary-grid {
    grid-template-columns: 1fr;
  }

  .filter-actions {
    align-items: stretch;
  }
}
</style>
