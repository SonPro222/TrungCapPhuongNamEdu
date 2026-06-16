<template>
  <section class="ca-hoc-page">
    <header class="page-head compact-head">
      <div class="page-title">
        <div class="title-line">
          <span class="page-kicker">Giảng dạy</span>
          <h1>Quản lý ca học</h1>
        </div>

        <p>Quản lý mã ca, thời gian bắt đầu, kết thúc dùng cho lịch học và lịch thi.</p>
      </div>

      <button
          type="button"
          class="btn btn-soft reload-btn"
          @click="taiDuLieu"
          :disabled="dangTai"
      >
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label class="filter-field keyword-field">
        <span>Từ khóa</span>
        <input
            v-model.trim="boLoc.keyword"
            type="text"
            placeholder="Mã ca, tên ca, mô tả"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label class="filter-field">
        <span>Sắp xếp</span>
        <select v-model="boLoc.sortBy" @change="locDuLieu">
          <option value="id">ID</option>
          <option value="maCa">Mã ca</option>
          <option value="tenCa">Tên ca</option>
          <option value="gioBatDau">Giờ bắt đầu</option>
          <option value="gioKetThuc">Giờ kết thúc</option>
        </select>
      </label>

      <label class="filter-field">
        <span>Chiều</span>
        <select v-model="boLoc.sortDir" @change="locDuLieu">
          <option value="desc">Giảm dần</option>
          <option value="asc">Tăng dần</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu" :disabled="dangTai">
          Lọc
        </button>
        <button type="button" class="btn" @click="xoaLoc" :disabled="dangTai">
          Xóa lọc
        </button>
      </div>
    </section>

    <section class="form-card">
      <div class="section-head">
        <div>

          <h2>{{ form.id ? 'Cập nhật ca học' : 'Thêm ca học' }}</h2>
        </div>
      </div>

      <form class="form-grid" @submit.prevent="luuCaHoc">
        <label class="field">
          <span>Mã ca</span>
          <input v-model.trim="form.maCa" type="text" required placeholder="CA01" />
        </label>

        <label class="field name-field">
          <span>Tên ca</span>
          <input v-model.trim="form.tenCa" type="text" required placeholder="Ca 1 - Sáng" />
        </label>

        <label class="field">
          <span>Giờ bắt đầu</span>
          <input v-model="form.gioBatDau" type="time" required />
        </label>

        <label class="field">
          <span>Giờ kết thúc</span>
          <input v-model="form.gioKetThuc" type="time" required />
        </label>

        <label class="field">
          <span>Thứ tự ca</span>
          <input type="number" v-model.number="form.thuTu" min="1" placeholder="1, 2..." />
        </label>

        <label class="field desc-field">
          <span>Mô tả</span>
          <textarea v-model.trim="form.moTa" rows="1" placeholder="Mô tả ngắn về ca học" />
        </label>

        <div class="form-actions">
          <button type="submit" class="btn primary" :disabled="dangTai">
            {{ form.id ? 'Cập nhật' : 'Thêm mới' }}
          </button>
          <button type="button" class="btn" @click="resetForm" :disabled="dangTai">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="list-card">
      <div class="list-head">
        <div class="list-title">
          <h2>Danh sách ca học</h2>
        </div>

        <div class="list-right">
          <div class="list-meta">
            <div class="meta-item">
              <span>Hiển thị</span>
              <strong>{{ danhSach.length }}</strong>
            </div>

            <div class="meta-item">
              <span>Tổng</span>
              <strong>{{ tongBanGhi }}</strong>
            </div>

            <div class="meta-item">
              <span>Trang</span>
              <strong>{{ trangHienTai + 1 }}/{{ tongTrang || 1 }}</strong>
            </div>
          </div>

          <div class="pagination-bar">
            <button
                type="button"
                class="btn icon-btn"
                :disabled="trangHienTai <= 0 || dangTai"
                @click="doiTrang(trangHienTai - 1)"
                title="Trang trước"
            >
              ‹
            </button>

            <button
                type="button"
                class="btn icon-btn"
                :disabled="tongTrang === 0 || trangHienTai + 1 >= tongTrang || dangTai"
                @click="doiTrang(trangHienTai + 1)"
                title="Trang sau"
            >
              ›
            </button>
          </div>
        </div>
      </div>

      <div class="compact-table">
        <div class="table-row table-header">
          <div class="cell id-cell">ID</div>
          <div class="cell code-cell">Mã ca</div>
          <div class="cell name-cell">Tên ca</div>
          <div class="cell start-cell">Giờ bắt đầu</div>
          <div class="cell end-cell">Giờ kết thúc</div>
          <div class="cell desc-cell">Mô tả</div>
          <div class="cell order-cell">Thứ tự</div>
          <div class="cell action-cell">Thao tác</div>
        </div>

        <div v-if="dangTai" class="table-state">
          Đang tải dữ liệu...
        </div>

        <div v-else-if="!danhSach.length" class="table-state">
          Không có dữ liệu
        </div>

        <div
            v-for="item in danhSach"
            v-else
            :key="item.id"
            class="table-row table-body-row"
        >
          <div class="cell id-cell id-text" data-label="ID">
            {{ item.id }}
          </div>

          <div class="cell code-cell" data-label="Mã ca">
            <span class="code-pill">{{ item.maCa }}</span>
          </div>

          <div class="cell name-cell name-text" data-label="Tên ca" :title="item.tenCa">
            {{ item.tenCa }}
          </div>

          <div class="cell start-cell time-text" data-label="Giờ bắt đầu">
            {{ item.gioBatDau }}
          </div>

          <div class="cell end-cell time-text" data-label="Giờ kết thúc">
            {{ item.gioKetThuc }}
          </div>

          <div class="cell desc-cell desc-text" data-label="Mô tả" :title="item.moTa || '-'">
            {{ item.moTa || '-' }}
          </div>

          <div class="cell order-cell" data-label="Thứ tự">
            <span class="order-pill">{{ item.thuTu ?? '-' }}</span>
          </div>

          <div class="cell action-cell" data-label="Thao tác">
            <div class="row-actions">
              <button type="button" class="btn tiny edit-btn" @click="chonSua(item)">
                Sửa
              </button>
              <button type="button" class="btn tiny danger" @click="xoaCaHoc(item)">
                Xóa
              </button>
            </div>
          </div>
        </div>
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
  sortBy: 'id',
  sortDir: 'desc'
})

const form = reactive({
  id: null,
  maCa: '',
  tenCa: '',
  gioBatDau: '',
  gioKetThuc: '',
  moTa: '',
  thuTu: null
})

onMounted(() => taiDuLieu())

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const page = await giangDayService.layTrangCaHoc({
      page: trangHienTai.value,
      size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      sortBy: boLoc.sortBy,
      sortDir: boLoc.sortDir
    })

    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách ca học'
  } finally {
    dangTai.value = false
  }
}

async function luuCaHoc() {
  const payload = {
    maCa: form.maCa,
    tenCa: form.tenCa,
    gioBatDau: form.gioBatDau,
    gioKetThuc: form.gioKetThuc,
    moTa: form.moTa || null,
    thuTu: form.thuTu
  }

  try {
    if (form.id) {
      await giangDayService.capNhatCaHoc(form.id, payload)
      thongBao.value = 'Cập nhật ca học thành công'
    } else {
      await giangDayService.taoCaHoc(payload)
      thongBao.value = 'Thêm ca học thành công'
    }

    resetForm()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu ca học thất bại'
  }
}

function chonSua(item) {
  form.id = item.id
  form.maCa = item.maCa || ''
  form.tenCa = item.tenCa || ''
  form.gioBatDau = item.gioBatDau || ''
  form.gioKetThuc = item.gioKetThuc || ''
  form.moTa = item.moTa || ''
  form.thuTu = item.thuTu ?? null
}

async function xoaCaHoc(item) {
  const ok = window.confirm(`Xóa ca học "${item.tenCa}"?`)
  if (!ok) return

  try {
    await giangDayService.xoaCaHoc(item.id)
    thongBao.value = 'Xóa ca học thành công'
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa ca học thất bại'
  }
}

function resetForm() {
  form.id = null
  form.maCa = ''
  form.tenCa = ''
  form.gioBatDau = ''
  form.gioKetThuc = ''
  form.moTa = ''
  form.thuTu = null
}

function locDuLieu() {
  trangHienTai.value = 0
  taiDuLieu()
}

function xoaLoc() {
  boLoc.keyword = ''
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
</script>

<style scoped>
.ca-hoc-page {
  --pn-green: #077149;
  --pn-green-dark: #055a3a;
  --pn-green-soft: #e7f6ef;
  --pn-green-light: #f4fbf8;

  --pn-border: #d9e8e0;
  --pn-border-strong: #c4dbd1;
  --pn-text: #05291d;
  --pn-muted: #60766d;
  --pn-white: #ffffff;

  --pn-danger: #dc2626;
  --pn-danger-soft: #fff1f2;

  --col-id-bg: #f2f8fb;
  --col-code-bg: #eaf8f2;
  --col-name-bg: #f4fbf8;
  --col-start-bg: #eef7ff;
  --col-end-bg: #fff8e8;
  --col-desc-bg: #f6faf8;
  --col-order-bg: #fff9e9;
  --col-action-bg: #fff7f7;

  --col-id-text: #335d6b;
  --col-code-text: #04613f;
  --col-name-text: #05291d;
  --col-start-text: #0f5d81;
  --col-end-text: #795200;
  --col-desc-text: #526b62;
  --col-order-text: #7a4b00;
  --col-action-text: #7a1c1c;

  --pn-shadow: 0 5px 16px rgba(7, 113, 73, 0.055);

  width: 100%;
  min-width: 0;
  max-width: 100%;
  display: flex;
  flex-direction: column;
  gap: clamp(6px, 0.85vw, 10px);
  color: var(--pn-text);
}

.ca-hoc-page,
.ca-hoc-page * {
  box-sizing: border-box;
}

.page-head,
.filter-card,
.form-card,
.list-card {
  width: 100%;
  min-width: 0;
  max-width: 100%;
  border: 1px solid var(--pn-border);
  border-radius: clamp(8px, 0.9vw, 13px);
  background: var(--pn-white);
  box-shadow: var(--pn-shadow);
  overflow: hidden;
}

.page-head.compact-head {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: clamp(6px, 0.8vw, 10px);
  min-height: 0;
  padding: clamp(6px, 0.72vw, 9px) clamp(8px, 0.95vw, 12px);
}

.page-title,
.list-title {
  min-width: 0;
  max-width: 100%;
  overflow: hidden;
}

.title-line {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: clamp(5px, 0.65vw, 8px);
  max-width: 100%;
  overflow: hidden;
}

.page-kicker,
.section-kicker {
  flex: 0 1 auto;
  display: inline-flex;
  align-items: center;
  max-width: 92px;
  height: clamp(18px, 1.9vw, 23px);
  padding: 0 clamp(6px, 0.75vw, 9px);
  border: 1px solid rgba(7, 113, 73, 0.14);
  border-radius: 999px;
  background: var(--pn-green-soft);
  color: var(--pn-green);
  font-size: clamp(8.5px, 0.78vw, 10.5px);
  line-height: 1;
  font-weight: 950;
  text-transform: uppercase;
  letter-spacing: 0.02em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.page-head h1,
.section-head h2,
.list-title h2 {
  min-width: 0;
  margin: 0;
  color: #06291d;
  font-weight: 950;
  letter-spacing: -0.035em;
  line-height: 1.08;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.page-head h1 {
  font-size: clamp(15px, 1.25vw, 20px);
}

.page-head p {
  margin: 2px 0 0;
  color: var(--pn-muted);
  font-size: clamp(9.5px, 0.82vw, 11.5px);
  line-height: 1.2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.reload-btn {
  min-height: clamp(26px, 2.35vw, 32px);
  padding: 0 clamp(8px, 0.85vw, 10px);
  font-size: clamp(10px, 0.8vw, 12px);
  border-radius: clamp(7px, 0.7vw, 9px);
}

.filter-card {
  display: grid;
  grid-template-columns:
    minmax(0, 1.8fr)
    minmax(0, 0.85fr)
    minmax(0, 0.85fr)
    auto;
  align-items: end;
  gap: clamp(6px, 0.8vw, 10px);
  padding: clamp(7px, 0.9vw, 10px);
}

.filter-field,
.field {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.filter-field span,
.field span {
  color: #173d31;
  font-size: clamp(10px, 0.9vw, 12px);
  line-height: 1.15;
  font-weight: 900;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

input,
select,
textarea {
  width: 100%;
  min-width: 0;
  height: clamp(29px, 3vw, 36px);
  border: 1px solid var(--pn-border-strong);
  border-radius: clamp(7px, 0.75vw, 10px);
  background: #fff;
  color: var(--pn-text);
  padding: 0 clamp(7px, 0.85vw, 10px);
  font: inherit;
  font-size: clamp(10.5px, 0.95vw, 13px);
  line-height: 1.2;
  outline: none;
  transition: border-color 0.16s ease, box-shadow 0.16s ease, background-color 0.16s ease;
}

textarea {
  display: block;
  min-height: clamp(29px, 3vw, 36px);
  height: clamp(29px, 3vw, 36px);
  max-height: 64px;
  padding-top: clamp(6px, 0.75vw, 8px);
  padding-bottom: clamp(6px, 0.75vw, 8px);
  resize: vertical;
}

input:hover,
select:hover,
textarea:hover {
  border-color: rgba(7, 113, 73, 0.42);
}

input:focus,
select:focus,
textarea:focus {
  border-color: var(--pn-green);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.1);
}

.filter-actions,
.form-actions,
.row-actions,
.pagination-bar,
.list-right,
.list-meta {
  min-width: 0;
  display: flex;
  align-items: center;
  gap: clamp(4px, 0.6vw, 7px);
}

.filter-actions {
  justify-content: flex-end;
}

.btn {
  min-width: 0;
  min-height: clamp(29px, 3vw, 36px);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid var(--pn-border-strong);
  border-radius: clamp(7px, 0.75vw, 10px);
  background: #fff;
  color: #11382c;
  padding: 0 clamp(7px, 0.9vw, 11px);
  font-size: clamp(10.5px, 0.9vw, 13px);
  line-height: 1;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
  transition: transform 0.14s ease, border-color 0.14s ease, background-color 0.14s ease, color 0.14s ease;
}

.btn:hover:not(:disabled) {
  transform: translateY(-1px);
  border-color: rgba(7, 113, 73, 0.45);
}

.btn.primary {
  background: var(--pn-green);
  color: #fff;
  border-color: var(--pn-green);
}

.btn.primary:hover:not(:disabled) {
  background: var(--pn-green-dark);
  border-color: var(--pn-green-dark);
}

.btn-soft {
  color: var(--pn-green);
  background: var(--pn-green-soft);
  border-color: rgba(7, 113, 73, 0.18);
}

.btn.tiny,
.btn.icon-btn {
  min-height: clamp(24px, 2.35vw, 29px);
  padding: 0 clamp(5px, 0.72vw, 8px);
  border-radius: clamp(6px, 0.7vw, 8px);
  font-size: clamp(9.5px, 0.82vw, 11.5px);
}

.btn.icon-btn {
  width: clamp(25px, 2.65vw, 32px);
  flex: 0 0 clamp(25px, 2.65vw, 32px);
  padding: 0;
  font-size: clamp(15px, 1.35vw, 19px);
}

.btn.danger {
  color: var(--pn-danger);
  background: var(--pn-danger-soft);
  border-color: #fecdd3;
}

.btn.danger:hover:not(:disabled) {
  background: #ffe4e6;
  border-color: #fda4af;
}

.btn:disabled {
  opacity: 0.48;
  cursor: not-allowed;
  transform: none;
}

.form-card {
  padding: clamp(8px, 1.05vw, 12px);
}

.section-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: clamp(7px, 0.9vw, 10px);
}

.section-head h2,
.list-title h2 {
  font-size: clamp(15px, 1.35vw, 19px);
}

.form-grid {
  display: grid;
  grid-template-columns:
    minmax(0, 0.8fr)
    minmax(0, 1.2fr)
    minmax(0, 0.9fr)
    minmax(0, 0.9fr)
    minmax(0, 0.72fr);
  gap: clamp(6px, 0.8vw, 10px);
  align-items: end;
  min-width: 0;
}

.desc-field {
  grid-column: 1 / span 4;
}

.form-actions {
  grid-column: 5;
  justify-content: flex-end;
}

.alert {
  border-radius: 10px;
  padding: clamp(7px, 0.85vw, 9px) clamp(8px, 0.95vw, 11px);
  font-size: clamp(10.5px, 0.95vw, 12.5px);
  font-weight: 800;
  border: 1px solid transparent;
}

.alert.success {
  color: #05603a;
  background: #ecfdf3;
  border-color: #bbf7d0;
}

.alert.error {
  color: #991b1b;
  background: #fef2f2;
  border-color: #fecaca;
}

/* =========================
   DANH SÁCH CA HỌC
   ========================= */

.list-card {
  background: #fff;
}

.list-head {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, auto);
  align-items: center;
  gap: clamp(5px, 0.75vw, 9px);
  padding: clamp(6px, 0.75vw, 9px);
  border-bottom: 1px solid var(--pn-border);
  background: linear-gradient(90deg, #f8fcfa, #ffffff);
}

.list-title {
  min-width: 0;
  max-width: 100%;
  overflow: hidden;
}

.list-title h2 {
  margin: 0;
  color: #06291d;
  font-size: clamp(15px, 1.25vw, 18px);
  font-weight: 950;
  line-height: 1.1;
  letter-spacing: -0.035em;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.list-right {
  justify-content: flex-end;
}

.list-meta {
  justify-content: flex-end;
}

.meta-item {
  min-width: 0;
  height: clamp(24px, 2.4vw, 29px);
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 0 clamp(5px, 0.75vw, 8px);
  border: 1px solid rgba(7, 113, 73, 0.14);
  border-radius: 999px;
  background: #fff;
  white-space: nowrap;
}

.meta-item span {
  color: var(--pn-muted);
  font-size: clamp(8.5px, 0.74vw, 10.5px);
  font-weight: 900;
  overflow: hidden;
  text-overflow: ellipsis;
}

.meta-item strong {
  color: var(--pn-green);
  font-size: clamp(9.5px, 0.85vw, 12px);
  line-height: 1;
  font-weight: 950;
  white-space: nowrap;
}

.compact-table {
  width: 100%;
  min-width: 0;
  max-width: 100%;
}

.table-row {
  width: 100%;
  min-width: 0;
  max-width: 100%;
  display: grid;
  grid-template-columns:
    minmax(18px, 0.28fr)
    minmax(46px, 0.7fr)
    minmax(72px, 1.14fr)
    minmax(56px, 0.82fr)
    minmax(56px, 0.82fr)
    minmax(76px, 1.22fr)
    minmax(36px, 0.5fr)
    minmax(62px, 0.78fr);
  align-items: stretch;
}

.table-header {
  min-height: clamp(28px, 2.6vw, 34px);
  border-bottom: 1px solid #dcebe3;
  background: #ffffff;
}

.table-body-row {
  min-height: clamp(31px, 2.95vw, 38px);
  border-bottom: 1px solid #edf3ef;
  background: #ffffff;
  transition: filter 0.14s ease, transform 0.14s ease;
}

.table-body-row:hover {
  filter: brightness(0.985);
}

.table-body-row:last-child {
  border-bottom: 0;
}

.cell {
  min-width: 0;
  display: flex;
  align-items: center;
  padding: 0 clamp(4px, 0.58vw, 8px);
  font-size: clamp(9.5px, 0.82vw, 12px);
  line-height: 1.15;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  border-right: 1px solid rgba(217, 232, 224, 0.55);
}

.cell:last-child {
  border-right: 0;
}

.table-header .cell {
  min-height: clamp(28px, 2.6vw, 34px);
  font-size: clamp(8.8px, 0.75vw, 10.8px);
  font-weight: 950;
  letter-spacing: 0;
  text-transform: none;
}

/* Nền cột header */
.table-header .id-cell {
  background: #edf6fb;
  color: var(--col-id-text);
}

.table-header .code-cell {
  background: #e4f6ee;
  color: var(--col-code-text);
}

.table-header .name-cell {
  background: #edf8f3;
  color: var(--col-name-text);
}

.table-header .start-cell {
  background: #eaf5ff;
  color: var(--col-start-text);
}

.table-header .end-cell {
  background: #fff3d8;
  color: var(--col-end-text);
}

.table-header .desc-cell {
  background: #f2f8f5;
  color: var(--col-desc-text);
}

.table-header .order-cell {
  background: #fff6dd;
  color: var(--col-order-text);
}

.table-header .action-cell {
  background: #fff0f0;
  color: var(--col-action-text);
}

/* Nền cột body */
.table-body-row .id-cell {
  background: var(--col-id-bg);
}

.table-body-row .code-cell {
  background: var(--col-code-bg);
}

.table-body-row .name-cell {
  background: var(--col-name-bg);
}

.table-body-row .start-cell {
  background: var(--col-start-bg);
}

.table-body-row .end-cell {
  background: var(--col-end-bg);
}

.table-body-row .desc-cell {
  background: var(--col-desc-bg);
}

.table-body-row .order-cell {
  background: var(--col-order-bg);
}

.table-body-row .action-cell {
  background: var(--col-action-bg);
}

.table-body-row:nth-child(even) .id-cell {
  background: #eef7fb;
}

.table-body-row:nth-child(even) .code-cell {
  background: #e4f5ed;
}

.table-body-row:nth-child(even) .name-cell {
  background: #f0faf5;
}

.table-body-row:nth-child(even) .start-cell {
  background: #e8f4ff;
}

.table-body-row:nth-child(even) .end-cell {
  background: #fff1cf;
}

.table-body-row:nth-child(even) .desc-cell {
  background: #f0f8f4;
}

.table-body-row:nth-child(even) .order-cell {
  background: #fff4d4;
}

.table-body-row:nth-child(even) .action-cell {
  background: #fff4f4;
}

/* Màu chữ dữ liệu */
.id-text {
  color: var(--col-id-text);
  font-weight: 900;
  font-variant-numeric: tabular-nums;
}

.name-text {
  color: var(--col-name-text);
  font-weight: 950;
}

.time-text {
  color: var(--col-start-text);
  font-weight: 900;
  font-variant-numeric: tabular-nums;
}

.end-cell.time-text,
.end-cell .time-text {
  color: var(--col-end-text);
}

.desc-text {
  color: var(--col-desc-text);
  font-weight: 750;
}

.code-pill {
  display: inline-flex;
  align-items: center;
  max-width: 100%;
  height: clamp(18px, 1.95vw, 23px);
  padding: 0 clamp(5px, 0.66vw, 8px);
  border: 1px solid rgba(7, 113, 73, 0.24);
  border-radius: 999px;
  background: #d8f2e6;
  color: var(--col-code-text);
  font-size: clamp(9px, 0.78vw, 11px);
  font-weight: 950;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.order-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: clamp(22px, 2vw, 28px);
  height: clamp(18px, 1.95vw, 23px);
  padding: 0 6px;
  border-radius: 999px;
  background: #fff2c8;
  border: 1px solid #edce7a;
  color: var(--col-order-text);
  font-size: clamp(9px, 0.78vw, 11px);
  font-weight: 950;
  font-variant-numeric: tabular-nums;
}

.row-actions {
  justify-content: flex-start;
}

.btn.edit-btn {
  color: #075f40;
  background: #f4fbf8;
  border-color: #c9e3d8;
}

.btn.edit-btn:hover:not(:disabled) {
  background: #e7f6ef;
  border-color: rgba(7, 113, 73, 0.38);
}

.table-state {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 58px;
  padding: 10px;
  color: var(--pn-muted);
  font-size: clamp(10.5px, 0.95vw, 12.5px);
  font-weight: 800;
  text-align: center;
}

@media (max-width: 980px) {
  .filter-card {
    grid-template-columns:
      minmax(0, 1.4fr)
      minmax(0, 0.75fr)
      minmax(0, 0.75fr)
      auto;
  }

  .form-grid {
    grid-template-columns:
      minmax(0, 0.8fr)
      minmax(0, 1.1fr)
      minmax(0, 0.85fr)
      minmax(0, 0.85fr)
      minmax(0, 0.65fr);
  }

  .list-head {
    grid-template-columns: minmax(0, 1fr);
  }

  .list-right {
    justify-content: space-between;
  }

  .table-row {
    grid-template-columns:
      minmax(16px, 0.26fr)
      minmax(42px, 0.62fr)
      minmax(62px, 1fr)
      minmax(50px, 0.72fr)
      minmax(50px, 0.72fr)
      minmax(58px, 0.92fr)
      minmax(30px, 0.46fr)
      minmax(54px, 0.68fr);
  }
}

@media (max-width: 760px) {
  .page-head.compact-head {
    padding: 7px 8px;
  }

  .page-head p {
    display: none;
  }

  .filter-card {
    grid-template-columns: minmax(0, 1fr) minmax(0, 0.75fr);
  }

  .keyword-field {
    grid-column: 1 / -1;
  }

  .filter-actions {
    grid-column: 1 / -1;
    display: grid;
    grid-template-columns: 1fr 1fr;
  }

  .filter-actions .btn {
    width: 100%;
  }

  .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .name-field,
  .desc-field {
    grid-column: 1 / -1;
  }

  .form-actions {
    grid-column: 1 / -1;
    display: grid;
    grid-template-columns: 1fr 1fr;
  }

  .form-actions .btn {
    width: 100%;
  }

  .list-right {
    display: grid;
    grid-template-columns: minmax(0, 1fr) auto;
    width: 100%;
  }

  .list-meta {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .meta-item {
    justify-content: center;
    padding: 0 4px;
  }

  .compact-table {
    display: grid;
    gap: 7px;
    padding: 8px;
    background: #f7fbf9;
  }

  .table-header {
    display: none;
  }

  .table-body-row {
    display: grid;
    grid-template-columns: minmax(0, 1fr) auto;
    gap: 5px 8px;
    min-height: 0;
    padding: 7px;
    border: 1px solid var(--pn-border);
    border-radius: 11px;
    background: #fff;
    overflow: hidden;
  }

  .cell {
    padding: 0;
    border-right: 0;
    background: transparent !important;
  }

  .cell::before {
    content: attr(data-label);
    display: inline;
    margin-right: 5px;
    color: var(--pn-muted);
    font-size: 9.5px;
    font-weight: 900;
  }

  .id-cell {
    grid-column: 1;
    grid-row: 1;
  }

  .code-cell {
    grid-column: 1;
    grid-row: 2;
  }

  .name-cell {
    grid-column: 1;
    grid-row: 3;
  }

  .start-cell {
    grid-column: 1;
    grid-row: 4;
  }

  .end-cell {
    grid-column: 1;
    grid-row: 5;
  }

  .desc-cell {
    grid-column: 1;
    grid-row: 6;
  }

  .order-cell {
    grid-column: 1;
    grid-row: 7;
  }

  .action-cell {
    grid-column: 2;
    grid-row: 1 / span 7;
    align-self: center;
  }

  .row-actions {
    display: grid;
    grid-template-columns: 1fr;
    gap: 5px;
  }

  .btn.tiny {
    width: 40px;
  }
}

@media (max-width: 480px) {
  .ca-hoc-page {
    gap: 7px;
  }

  .page-head.compact-head {
    grid-template-columns: minmax(0, 1fr) auto;
    padding: 6px 7px;
  }

  .title-line {
    gap: 5px;
  }

  .page-kicker {
    max-width: 70px;
    height: 18px;
    padding: 0 6px;
    font-size: 8.5px;
  }

  .page-head h1 {
    font-size: 15.5px;
  }

  .reload-btn {
    min-height: 26px;
    padding: 0 7px;
    font-size: 10px;
  }

  .filter-card,
  .form-card,
  .list-head {
    padding: 7px;
  }

  .filter-card {
    grid-template-columns: 1fr;
  }

  .filter-field,
  .keyword-field {
    grid-column: 1;
  }

  .list-right {
    grid-template-columns: 1fr;
  }

  .pagination-bar {
    justify-content: flex-end;
  }

  .meta-item span {
    display: none;
  }

  .meta-item {
    min-width: 0;
  }

  .form-grid {
    gap: 7px;
  }

  .section-head h2,
  .list-title h2 {
    font-size: 16px;
  }

  .table-body-row {
    grid-template-columns: minmax(0, 1fr) auto;
    padding: 7px;
  }
}
</style>