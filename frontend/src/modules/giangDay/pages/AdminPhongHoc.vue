<template>
  <section class="page phong-hoc-page">
    <header class="page-head">
      <div class="page-title">
        <span class="eyebrow">Giảng dạy</span>
        <h1>Quản lý phòng học</h1>
        <p>Quản lý phòng lý thuyết, phòng máy, phòng thi, xưởng và trạng thái sử dụng.</p>
      </div>

      <button type="button" class="btn btn-soft" :disabled="dangTai" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label class="field keyword-field">
        <span>Từ khóa</span>
        <input
            v-model.trim="boLoc.keyword"
            type="text"
            placeholder="Mã phòng, tên phòng, địa điểm"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label class="field">
        <span>Loại phòng</span>
        <select v-model="boLoc.loaiPhong" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="ly_thuyet">Lý thuyết</option>
          <option value="thuc_hanh">Thực hành</option>
          <option value="phong_may">Phòng máy</option>
          <option value="phong_thi">Phòng thi</option>
          <option value="xuong">Xưởng</option>
          <option value="khac">Khác</option>
        </select>
      </label>

      <label class="field">
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="dang_su_dung">Đang sử dụng</option>
          <option value="bao_tri">Bảo trì</option>
          <option value="ngung_su_dung">Ngưng sử dụng</option>
        </select>
      </label>

      <label class="field">
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="id">ID</option>
          <option value="maPhong">Mã phòng</option>
          <option value="tenPhong">Tên phòng</option>
          <option value="sucChua">Sức chứa</option>
          <option value="trangThai">Trạng thái</option>
        </select>
      </label>

      <label class="field">
        <span>Chiều</span>
        <select v-model="boLoc.sortDir" @change="locDuLieu">
          <option value="desc">Giảm dần</option>
          <option value="asc">Tăng dần</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn btn-primary" :disabled="dangTai" @click="locDuLieu">
          Lọc
        </button>
        <button type="button" class="btn btn-ghost" :disabled="dangTai" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <section class="form-card" :class="{ 'is-editing-form': form.id }">
      <div class="section-head">
        <div>
          <span class="section-kicker">
            {{ form.id ? 'Đang chỉnh sửa dữ liệu' : 'Thông tin phòng' }}
          </span>
          <h2>{{ form.id ? 'Cập nhật phòng học' : 'Thêm phòng học' }}</h2>
        </div>

        <span v-if="form.id" class="editing-badge">
          ID #{{ form.id }}
        </span>
      </div>

      <form class="form-grid" @submit.prevent="luuPhongHoc">
        <label class="field">
          <span>Mã phòng</span>
          <input v-model.trim="form.maPhong" type="text" required />
        </label>

        <label class="field">
          <span>Tên phòng</span>
          <input v-model.trim="form.tenPhong" type="text" required />
        </label>

        <label class="field">
          <span>Loại phòng</span>
          <select v-model="form.loaiPhong">
            <option value="">-- Chọn --</option>
            <option value="ly_thuyet">Lý thuyết</option>
            <option value="thuc_hanh">Thực hành</option>
            <option value="phong_may">Phòng máy</option>
            <option value="phong_thi">Phòng thi</option>
            <option value="xuong">Xưởng</option>
            <option value="khac">Khác</option>
          </select>
        </label>

        <label class="field">
          <span>Sức chứa</span>
          <input v-model.trim="form.sucChua" type="number" min="0" />
        </label>

        <label class="field">
          <span>Trạng thái</span>
          <select v-model="form.trangThai" required>
            <option value="dang_su_dung">Đang sử dụng</option>
            <option value="bao_tri">Bảo trì</option>
            <option value="ngung_su_dung">Ngưng sử dụng</option>
          </select>
        </label>

        <label class="field field-location">
          <span>Địa điểm</span>
          <textarea v-model.trim="form.diaDiem" rows="2" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">
            {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
          </button>
          <button type="button" class="btn btn-ghost" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="list-card">
      <div class="list-toolbar">
        <div class="list-title">
          <span class="section-kicker">Danh sách phòng học</span>
          <h2>Danh sách</h2>
        </div>

        <div class="summary-inline">
          <div class="summary-item">
            <span>Hiển thị</span>
            <strong>{{ danhSach.length }}</strong>
          </div>

          <div class="summary-item">
            <span>Tổng</span>
            <strong>{{ tongBanGhi }}</strong>
          </div>

          <div class="summary-item">
            <span>Trang</span>
            <strong>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</strong>
          </div>
        </div>

        <div class="pagination-bar">
          <button
              type="button"
              class="btn btn-ghost btn-compact"
              :disabled="trangHienTai <= 0 || dangTai"
              @click="doiTrang(trangHienTai - 1)"
          >
            Trước
          </button>

          <button
              type="button"
              class="btn btn-ghost btn-compact"
              :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
              @click="doiTrang(trangHienTai + 1)"
          >
            Sau
          </button>
        </div>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th class="col-id">ID</th>
            <th class="col-code">Mã phòng</th>
            <th class="col-name">Tên phòng</th>
            <th class="col-type">Loại</th>
            <th class="col-capacity">Sức chứa</th>
            <th class="col-location">Địa điểm</th>
            <th class="col-status">Trạng thái</th>
            <th class="col-actions">Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="dangTai">
            <td colspan="8" class="empty-cell">Đang tải dữ liệu...</td>
          </tr>

          <tr v-else-if="!danhSach.length">
            <td colspan="8" class="empty-cell">Không có dữ liệu</td>
          </tr>

          <tr
              v-for="item in danhSach"
              v-else
              :key="item.id"
              :class="{ 'is-editing-row': form.id === item.id }"
          >
            <td class="col-id">
                <span class="id-chip" :class="{ active: form.id === item.id }">
                  {{ item.id }}
                </span>
            </td>

            <td class="room-code">
              {{ item.maPhong }}
            </td>

            <td class="truncate-cell" :title="item.tenPhong">
              {{ item.tenPhong }}
            </td>

            <td class="truncate-cell" :title="hienThiLoaiPhong(item.loaiPhong)">
              {{ hienThiLoaiPhong(item.loaiPhong) }}
            </td>

            <td>{{ item.sucChua ?? '-' }}</td>

            <td class="truncate-cell" :title="item.diaDiem || '-'">
              {{ item.diaDiem || '-' }}
            </td>

            <td>
                <span class="status-pill" :class="item.trangThai">
                  {{ hienThiTrangThai(item.trangThai) }}
                </span>
            </td>

            <td>
              <div class="row-actions">
                <button
                    type="button"
                    class="btn btn-small"
                    :class="form.id === item.id ? 'btn-editing' : 'btn-ghost'"
                    @click="chonSua(item)"
                >
                  {{ form.id === item.id ? 'Đang sửa' : 'Sửa' }}
                </button>

                <button type="button" class="btn btn-small btn-danger" @click="xoaPhongHoc(item)">
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
import { onMounted, reactive, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const danhSach = ref([])
const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 100

const boLoc = reactive({
  keyword: '',
  loaiPhong: '',
  trangThai: '',
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  maPhong: '',
  tenPhong: '',
  loaiPhong: '',
  sucChua: '',
  diaDiem: '',
  trangThai: 'dang_su_dung'
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangPhongHoc({
      page: trangHienTai.value,
      size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      loaiPhong: boLoc.loaiPhong || undefined,
      trangThai: boLoc.trangThai || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách phòng học'
  } finally {
    dangTai.value = false
  }
}

async function luuPhongHoc() {
  const payload = {
    maPhong: form.maPhong,
    tenPhong: form.tenPhong,
    loaiPhong: form.loaiPhong || null,
    sucChua: form.sucChua === '' ? null : Number(form.sucChua),
    diaDiem: form.diaDiem || null,
    trangThai: form.trangThai
  }

  try {
    if (form.id) {
      await giangDayService.capNhatPhongHoc(form.id, payload)
      thongBao.value = 'Cập nhật phòng học thành công'
    } else {
      await giangDayService.taoPhongHoc(payload)
      thongBao.value = 'Thêm phòng học thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu phòng học thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.maPhong = item.maPhong || ''
  form.tenPhong = item.tenPhong || ''
  form.loaiPhong = item.loaiPhong || ''
  form.sucChua = item.sucChua ?? ''
  form.diaDiem = item.diaDiem || ''
  form.trangThai = item.trangThai || 'dang_su_dung'
}

async function xoaPhongHoc(item) {
  const ok = window.confirm(`Xóa phòng học "${item.tenPhong}"?`)
  if (!ok) return

  try {
    await giangDayService.xoaPhongHoc(item.id)
    thongBao.value = 'Xóa phòng học thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa phòng học thất bại'
  }
}

function resetForm() {
  form.id = null
  form.maPhong = ''
  form.tenPhong = ''
  form.loaiPhong = ''
  form.sucChua = ''
  form.diaDiem = ''
  form.trangThai = 'dang_su_dung'
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keyword = ''
  boLoc.loaiPhong = ''
  boLoc.trangThai = ''
  boLoc.sortBy = 'id'
  boLoc.sortDir = 'desc'
  trangHienTai.value = 0
  taiDuLieu()
}

function doiTrang(page) {
  if (page < 0) return
  if (tongTrang.value && page >= tongTrang.value) return
  trangHienTai.value = page
  taiDuLieu()
}

function hienThiLoaiPhong(value) {
  const map = {
    ly_thuyet: 'Lý thuyết',
    thuc_hanh: 'Thực hành',
    phong_may: 'Phòng máy',
    phong_thi: 'Phòng thi',
    xuong: 'Xưởng',
    khac: 'Khác'
  }
  return map[value] || value || '-'
}

function hienThiTrangThai(value) {
  const map = {
    dang_su_dung: 'Đang sử dụng',
    bao_tri: 'Bảo trì',
    ngung_su_dung: 'Ngưng sử dụng'
  }
  return map[value] || value || '-'
}
</script>

<style scoped>
.phong-hoc-page {
  --brand: #077149;
  --brand-dark: #055c3b;
  --brand-deep: #034c31;
  --brand-soft: #e7f5ef;
  --brand-soft-2: #f3fbf7;
  --brand-soft-3: #d9f2e7;
  --brand-line: #a7d8c4;

  --col-id: #eef8f3;
  --col-code: #e6f5ef;
  --col-name: #f3faf7;
  --col-type: #f7fbf9;
  --col-capacity: #fff8e9;
  --col-location: #eef9f4;
  --col-status: #fff4f1;
  --col-actions: #f4fbf8;

  --text-main: #0f172a;
  --text-muted: #64748b;
  --line: #dbe5df;
  --line-soft: #edf2ef;

  --danger: #dc2626;
  --danger-soft: #fff1f2;
  --warning: #b7791f;
  --warning-soft: #fff8e1;

  --radius-lg: 12px;
  --radius-md: 9px;
  --radius-sm: 7px;

  display: flex;
  flex-direction: column;
  gap: 10px;
  min-width: 0;
  color: var(--text-main);
  font-family: Roboto, Arial, sans-serif;
}

/* Header */
.page-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  min-width: 0;
  padding: 8px 12px;
  border: 1px solid var(--line);
  border-radius: var(--radius-lg);
  background:
      linear-gradient(135deg, rgba(7, 113, 73, 0.055), rgba(255, 255, 255, 0.96) 46%),
      #fff;
  box-shadow: 0 3px 10px rgba(15, 23, 42, 0.03);
}

.page-title {
  min-width: 0;
}

.eyebrow,
.section-kicker {
  display: inline-flex;
  align-items: center;
  width: max-content;
  max-width: 100%;
  color: var(--brand);
  font-size: 11px;
  font-weight: 900;
  line-height: 1;
  letter-spacing: 0.01em;
  white-space: nowrap;
}

.page-head h1 {
  margin: 3px 0 0;
  color: var(--text-main);
  font-size: clamp(21px, 1.8vw, 25px);
  font-weight: 900;
  line-height: 1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.page-head p {
  margin: 5px 0 0;
  max-width: 760px;
  color: var(--text-muted);
  font-size: 12px;
  line-height: 1.2;
}

/* Card */
.filter-card,
.form-card,
.list-card {
  min-width: 0;
  border: 1px solid var(--line);
  border-radius: var(--radius-lg);
  background: #fff;
  box-shadow: 0 5px 16px rgba(15, 23, 42, 0.03);
}

/* Filter */
.filter-card {
  display: grid;
  grid-template-columns:
    minmax(220px, 1.35fr)
    minmax(140px, 0.9fr)
    minmax(140px, 0.9fr)
    minmax(120px, 0.75fr)
    minmax(120px, 0.75fr)
    auto;
  align-items: end;
  gap: 9px;
  padding: 9px 10px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}

.field span {
  color: #1f3d34;
  font-size: 11.5px;
  font-weight: 900;
  line-height: 1.1;
  white-space: nowrap;
}

input,
select,
textarea {
  width: 100%;
  min-width: 0;
  border: 1px solid var(--line);
  border-radius: var(--radius-sm);
  background: #fff;
  color: var(--text-main);
  font: inherit;
  font-size: 12.5px;
  outline: none;
  transition:
      border-color 0.16s ease,
      box-shadow 0.16s ease,
      background 0.16s ease;
}

input,
select {
  height: 32px;
  padding: 0 9px;
}

textarea {
  min-height: 32px;
  max-height: 64px;
  padding: 7px 9px;
  resize: vertical;
}

input:focus,
select:focus,
textarea:focus {
  border-color: var(--brand);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.12);
}

input::placeholder {
  color: #94a3b8;
}

/* Buttons */
.filter-actions,
.form-actions,
.row-actions,
.pagination-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  min-width: 0;
  white-space: nowrap;
}

.filter-actions {
  justify-content: flex-end;
}

.btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  min-height: 32px;
  border: 1px solid var(--line);
  border-radius: var(--radius-sm);
  background: #fff;
  color: var(--text-main);
  padding: 0 11px;
  font-size: 12.5px;
  font-weight: 900;
  line-height: 1;
  cursor: pointer;
  white-space: nowrap;
  user-select: none;
  transition:
      transform 0.14s ease,
      border-color 0.14s ease,
      background 0.14s ease,
      color 0.14s ease,
      box-shadow 0.14s ease;
}

.btn:hover:not(:disabled) {
  transform: translateY(-1px);
  border-color: rgba(7, 113, 73, 0.35);
  box-shadow: 0 5px 14px rgba(15, 23, 42, 0.075);
}

.btn:active:not(:disabled) {
  transform: translateY(0);
}

.btn:disabled {
  opacity: 0.55;
  cursor: not-allowed;
  box-shadow: none;
}

.btn-primary {
  border-color: var(--brand);
  background: var(--brand);
  color: #fff;
}

.btn-primary:hover:not(:disabled) {
  border-color: var(--brand-dark);
  background: var(--brand-dark);
}

.btn-soft {
  min-height: 30px;
  border-color: rgba(7, 113, 73, 0.18);
  background: var(--brand-soft);
  color: var(--brand);
  padding: 0 11px;
  font-size: 12px;
}

.btn-ghost {
  background: #fff;
  color: #243b35;
}

.btn-danger {
  border-color: #fecdd3;
  background: var(--danger-soft);
  color: var(--danger);
}

.btn-small {
  min-height: 25px;
  padding: 0 8px;
  font-size: 11.5px;
  border-radius: 6px;
}

.btn-compact {
  min-height: 28px;
  padding: 0 9px;
  font-size: 11.5px;
}

.btn-editing {
  border-color: var(--brand);
  background: var(--brand);
  color: #fff;
  box-shadow: 0 0 0 2px rgba(7, 113, 73, 0.12);
}

/* Form */
.form-card {
  position: relative;
  padding: 12px;
  overflow: hidden;
}

.form-card::before {
  content: "";
  position: absolute;
  inset: 0 auto 0 0;
  width: 0;
  background: var(--brand);
  transition: width 0.16s ease;
}

.form-card.is-editing-form {
  border-color: var(--brand-line);
  background:
      linear-gradient(135deg, rgba(7, 113, 73, 0.055), transparent 42%),
      #fff;
  box-shadow:
      0 5px 18px rgba(7, 113, 73, 0.08),
      0 0 0 3px rgba(7, 113, 73, 0.055);
}

.form-card.is-editing-form::before {
  width: 4px;
}

.form-card.is-editing-form input,
.form-card.is-editing-form select,
.form-card.is-editing-form textarea {
  border-color: rgba(7, 113, 73, 0.28);
}

.section-head {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 9px;
}

.section-head h2 {
  margin: 3px 0 0;
  color: var(--text-main);
  font-size: clamp(19px, 1.8vw, 24px);
  font-weight: 900;
  line-height: 1.1;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.editing-badge {
  display: inline-flex;
  align-items: center;
  min-height: 26px;
  padding: 0 10px;
  border: 1px solid rgba(7, 113, 73, 0.18);
  border-radius: 999px;
  background: var(--brand);
  color: #fff;
  font-size: 11.5px;
  font-weight: 900;
  white-space: nowrap;
}

.form-grid {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns:
    minmax(120px, 0.9fr)
    minmax(180px, 1.25fr)
    minmax(145px, 1fr)
    minmax(105px, 0.68fr)
    minmax(145px, 1fr);
  gap: 9px;
  align-items: end;
  min-width: 0;
}

.field-location {
  grid-column: 1 / 5;
}

.form-actions {
  grid-column: 5 / 6;
  align-self: end;
  justify-content: flex-start;
}

/* Alert */
.alert {
  padding: 8px 10px;
  border-radius: var(--radius-md);
  border: 1px solid transparent;
  font-size: 12.5px;
  font-weight: 800;
}

.alert.success {
  border-color: #bbf7d0;
  background: #ecfdf5;
  color: #047857;
}

.alert.error {
  border-color: #fecaca;
  background: #fef2f2;
  color: #991b1b;
}

/* List */
.list-card {
  overflow: hidden;
}

.list-toolbar {
  display: grid;
  grid-template-columns: minmax(120px, 1fr) auto auto;
  align-items: center;
  gap: 8px;
  padding: 7px 9px;
  border-bottom: 1px solid var(--brand-line);
  background:
      linear-gradient(180deg, #fff, var(--brand-soft-2));
}

.list-title {
  min-width: 0;
}

.list-title h2 {
  margin: 2px 0 0;
  color: var(--text-main);
  font-size: 15px;
  font-weight: 900;
  line-height: 1.05;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.summary-inline {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
  min-width: 0;
}

.summary-item {
  display: grid;
  grid-template-columns: auto auto;
  align-items: center;
  gap: 5px;
  min-height: 28px;
  padding: 4px 8px;
  border: 1px solid rgba(7, 113, 73, 0.16);
  border-radius: 7px;
  background: #fff;
  white-space: nowrap;
}

.summary-item span {
  color: var(--text-muted);
  font-size: 11px;
  font-weight: 900;
}

.summary-item strong {
  color: var(--brand);
  font-size: 14px;
  font-weight: 900;
  line-height: 1;
}

.pagination-bar {
  justify-content: flex-end;
}

/* Table */
.table-wrap {
  width: 100%;
  overflow: auto;
  background: #fff;
}

table {
  width: 100%;
  min-width: 920px;
  border-collapse: separate;
  border-spacing: 0;
  table-layout: fixed;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 1;
  height: 30px;
  border-bottom: 1px solid rgba(7, 113, 73, 0.28);
  color: var(--brand-deep);
  font-size: 10.5px;
  font-weight: 900;
  letter-spacing: 0.01em;
  text-align: left;
  text-transform: uppercase;
  white-space: nowrap;
}

/* Màu từng cột phần header */
thead th:nth-child(1) {
  background: #dff3eb;
}

thead th:nth-child(2) {
  background: #d8efe6;
}

thead th:nth-child(3) {
  background: #eaf7f1;
}

thead th:nth-child(4) {
  background: #f2f9f5;
}

thead th:nth-child(5) {
  background: #fff1d8;
}

thead th:nth-child(6) {
  background: #e0f5ec;
}

thead th:nth-child(7) {
  background: #ffe8df;
}

thead th:nth-child(8) {
  background: #eaf7f1;
}

th,
td {
  padding: 5px 8px;
  border-bottom: 1px solid rgba(7, 113, 73, 0.08);
  color: var(--text-main);
  font-size: 11.7px;
  line-height: 1.15;
  vertical-align: middle;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Màu từng cột dữ liệu giống kiểu ảnh mẫu */
tbody td:nth-child(1) {
  background: var(--col-id);
}

tbody td:nth-child(2) {
  background: var(--col-code);
}

tbody td:nth-child(3) {
  background: var(--col-name);
}

tbody td:nth-child(4) {
  background: var(--col-type);
}

tbody td:nth-child(5) {
  background: var(--col-capacity);
  text-align: center;
  font-weight: 900;
}

tbody td:nth-child(6) {
  background: var(--col-location);
}

tbody td:nth-child(7) {
  background: var(--col-status);
}

tbody td:nth-child(8) {
  background: var(--col-actions);
}

/* Kẻ cột nhẹ để nhìn rõ nhưng không rối */
thead th + th,
tbody td + td {
  border-left: 1px solid rgba(7, 113, 73, 0.045);
}

tbody tr {
  position: relative;
  height: 34px;
  transition:
      background 0.14s ease,
      box-shadow 0.14s ease;
}

tbody tr:hover td {
  filter: saturate(1.08) brightness(0.985);
}

tbody tr:last-child td {
  border-bottom: 0;
}

/* Dòng đang sửa nổi bật hơn */
tbody tr.is-editing-row {
  box-shadow:
      inset 4px 0 0 var(--brand),
      inset 0 1px 0 rgba(7, 113, 73, 0.22),
      inset 0 -1px 0 rgba(7, 113, 73, 0.22);
}

tbody tr.is-editing-row td {
  color: #062f22;
  font-weight: 900;
  border-top: 1px solid rgba(7, 113, 73, 0.22);
  border-bottom: 1px solid rgba(7, 113, 73, 0.22);
}

tbody tr.is-editing-row td:nth-child(1) {
  background: #c5eadb;
}

tbody tr.is-editing-row td:nth-child(2) {
  background: #bce5d4;
}

tbody tr.is-editing-row td:nth-child(3) {
  background: #d8f1e7;
}

tbody tr.is-editing-row td:nth-child(4) {
  background: #e4f6ee;
}

tbody tr.is-editing-row td:nth-child(5) {
  background: #ffe6b5;
}

tbody tr.is-editing-row td:nth-child(6) {
  background: #ccefe0;
}

tbody tr.is-editing-row td:nth-child(7) {
  background: #ffd8ca;
}

tbody tr.is-editing-row td:nth-child(8) {
  background: #d8f1e7;
}

.col-id {
  width: 48px;
}

.col-code {
  width: 95px;
}

.col-name {
  width: 230px;
}

.col-type {
  width: 100px;
}

.col-capacity {
  width: 78px;
}

.col-location {
  width: 210px;
}

.col-status {
  width: 120px;
}

.col-actions {
  width: 112px;
}

.id-chip {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 26px;
  height: 22px;
  padding: 0 6px;
  border-radius: 999px;
  background: #fff;
  color: #475569;
  font-size: 11px;
  font-weight: 900;
  box-shadow: inset 0 0 0 1px rgba(7, 113, 73, 0.08);
}

.id-chip.active {
  background: var(--brand);
  color: #fff;
  box-shadow: none;
}

.room-code {
  color: var(--brand-dark);
  font-weight: 900;
}

.truncate-cell {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.row-actions {
  justify-content: flex-start;
}

.status-pill {
  display: inline-flex;
  align-items: center;
  max-width: 100%;
  min-height: 21px;
  padding: 0 7px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 900;
  white-space: nowrap;
}

.status-pill.dang_su_dung {
  background: var(--brand-soft);
  color: var(--brand-dark);
  border: 1px solid rgba(7, 113, 73, 0.16);
}

.status-pill.bao_tri {
  background: var(--warning-soft);
  color: var(--warning);
  border: 1px solid rgba(245, 158, 11, 0.25);
}

.status-pill.ngung_su_dung {
  background: #f1f5f9;
  color: #475569;
  border: 1px solid #e2e8f0;
}

.empty-cell {
  height: 46px;
  color: var(--text-muted);
  font-weight: 900;
  text-align: center;
  background: #fff !important;
}

/* Tablet */
@media (max-width: 1200px) {
  .filter-card {
    grid-template-columns:
      minmax(220px, 1.3fr)
      repeat(2, minmax(135px, 1fr))
      repeat(2, minmax(115px, 0.8fr))
      auto;
  }

  .form-grid {
    grid-template-columns:
      minmax(125px, 1fr)
      minmax(170px, 1.3fr)
      minmax(145px, 1fr)
      minmax(105px, 0.8fr)
      minmax(135px, 1fr);
  }

  .list-toolbar {
    grid-template-columns: 1fr;
  }

  .summary-inline,
  .pagination-bar {
    justify-content: flex-start;
  }
}

@media (max-width: 980px) {
  .page-head {
    align-items: center;
  }

  .filter-card {
    grid-template-columns: repeat(3, minmax(155px, 1fr));
  }

  .keyword-field {
    grid-column: 1 / -1;
  }

  .filter-actions {
    justify-content: flex-start;
  }

  .form-grid {
    grid-template-columns: repeat(2, minmax(170px, 1fr));
  }

  .field-location {
    grid-column: 1 / -1;
  }

  .form-actions {
    grid-column: 1 / -1;
  }

  table {
    min-width: 880px;
  }
}

/* Mobile */
@media (max-width: 640px) {
  .phong-hoc-page {
    gap: 9px;
  }

  .page-head {
    flex-direction: column;
    align-items: stretch;
    padding: 10px;
  }

  .page-head h1 {
    font-size: 21px;
  }

  .page-head p {
    font-size: 11.8px;
  }

  .filter-card,
  .form-card {
    padding: 10px;
  }

  .filter-card,
  .form-grid {
    grid-template-columns: 1fr;
  }

  .keyword-field,
  .field-location,
  .form-actions {
    grid-column: auto;
  }

  .filter-actions,
  .form-actions {
    display: grid;
    grid-template-columns: 1fr 1fr;
  }

  .section-head {
    align-items: flex-start;
  }

  .editing-badge {
    min-height: 24px;
    padding: 0 8px;
    font-size: 11px;
  }

  .btn {
    width: 100%;
  }

  .list-toolbar {
    padding: 8px;
  }

  .summary-inline {
    display: grid;
    grid-template-columns: 1fr;
    width: 100%;
  }

  .summary-item {
    grid-template-columns: 1fr auto;
  }

  .pagination-bar {
    display: grid;
    grid-template-columns: 1fr 1fr;
    width: 100%;
  }

  table {
    min-width: 820px;
  }

  th,
  td {
    padding: 5px 7px;
    font-size: 11.4px;
  }
}

/* Mobile nhỏ */
@media (max-width: 420px) {
  .filter-actions,
  .form-actions,
  .pagination-bar {
    grid-template-columns: 1fr;
  }

  .section-head {
    flex-direction: column;
    gap: 7px;
  }

  .page-head h1 {
    font-size: 20px;
  }

  input,
  select,
  textarea,
  .btn {
    font-size: 12px;
  }
}
</style>