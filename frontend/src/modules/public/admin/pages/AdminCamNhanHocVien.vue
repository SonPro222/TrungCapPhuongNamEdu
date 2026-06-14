<template>
  <section class="admin-public-cam-nhan-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Cảm nhận học viên</h1>

        <p>
          Quản lý các cảm nhận học viên hiển thị ở trang chủ và trang chi tiết cảm nhận học viên.
        </p>
      </div>

      <button type="button" class="primary-button" @click="moFormThemMoi">
        <i class="fa-solid fa-plus"></i>
        Thêm cảm nhận
      </button>
    </div>

    <div v-if="thongBao" class="alert-success">
      <i class="fa-solid fa-circle-check"></i>
      <span>{{ thongBao }}</span>
    </div>

    <div class="toolbar-card">
      <label>
        <span>Tìm kiếm</span>
        <input
            v-model.trim="tuKhoa"
            type="text"
            placeholder="Nhập tên học viên, khoa hoặc tiêu đề"
        />
      </label>

      <label>
        <span>Lọc trạng thái</span>
        <select v-model="trangThaiLoc">
          <option value="tat-ca">Tất cả</option>
          <option value="hien-thi">Đang hiển thị</option>
          <option value="an">Đang ẩn</option>
        </select>
      </label>

      <button type="button" class="secondary-button" @click="khoiPhucMacDinh">
        Khôi phục mặc định
      </button>
    </div>

    <form v-if="dangMoForm" class="form-card" @submit.prevent="luuCamNhan">
      <div class="form-title-row">
        <div>
          <h2>{{ slugDangSua ? 'Sửa cảm nhận học viên' : 'Thêm cảm nhận học viên' }}</h2>
          <p>Dữ liệu này đang lưu tạm trong localStorage.</p>
        </div>

        <button type="button" class="close-button" @click="dongForm">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <div class="form-grid">
        <label>
          <span>Tên học viên</span>
          <input v-model.trim="form.ten" type="text" required placeholder="Nguyễn Thị Mai" />
        </label>

        <label>
          <span>Khoa/ngành</span>
          <input v-model.trim="form.khoa" type="text" required placeholder="Khoa Điều dưỡng" />
        </label>

        <label>
          <span>Ảnh đại diện</span>
          <input v-model.trim="form.avatar" type="url" placeholder="https://..." />
        </label>

        <label>
          <span>Đánh giá</span>
          <select v-model.number="form.danhGia">
            <option :value="5">5 sao</option>
            <option :value="4">4 sao</option>
            <option :value="3">3 sao</option>
            <option :value="2">2 sao</option>
            <option :value="1">1 sao</option>
          </select>
        </label>

        <label>
          <span>Niên khóa</span>
          <input v-model.trim="form.namHoc" type="text" placeholder="2021 - 2023" />
        </label>

        <label>
          <span>Hiện tại</span>
          <input v-model.trim="form.hienTai" type="text" placeholder="Đang làm việc tại doanh nghiệp" />
        </label>

        <label class="full">
          <span>Tiêu đề chi tiết</span>
          <input
              v-model.trim="form.tieuDe"
              type="text"
              required
              placeholder="Tự tin hơn nhờ môi trường học thực hành sát thực tế"
          />
        </label>

        <label class="full">
          <span>Câu nói ngắn</span>
          <textarea
              v-model.trim="form.cauNoiNgan"
              rows="3"
              required
              placeholder="Câu nói ngắn hiển thị ở trang chủ"
          ></textarea>
        </label>

        <label class="full">
          <span>Nội dung chi tiết</span>
          <textarea
              v-model.trim="form.noiDung"
              rows="7"
              placeholder="Nhập nội dung chi tiết. Mỗi đoạn cách nhau bằng một dòng trống."
          ></textarea>
        </label>

        <label class="full">
          <span>Điểm nổi bật</span>
          <textarea
              v-model.trim="form.diemNoiBatText"
              rows="5"
              placeholder="Mỗi dòng là một điểm nổi bật."
          ></textarea>
        </label>

        <label class="checkbox-line full">
          <input v-model="form.trangThaiHienThi" type="checkbox" />
          <span>Hiển thị cảm nhận này trên website public</span>
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="secondary-button" @click="dongForm">
          Hủy
        </button>

        <button type="submit" class="primary-button">
          {{ slugDangSua ? 'Lưu thay đổi' : 'Thêm cảm nhận' }}
        </button>
      </div>
    </form>

    <div class="table-card">
      <div class="table-header">
        <h2>Danh sách cảm nhận</h2>
        <span>{{ danhSachDaLoc.length }} cảm nhận</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Học viên</th>
            <th>Khoa/ngành</th>
            <th>Đánh giá</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="item in danhSachDaLoc" :key="item.slug">
            <td>
              <div class="hoc-vien-cell">
                <img
                    v-if="item.avatar"
                    :src="item.avatar"
                    :alt="item.ten"
                />

                <div v-else class="avatar-placeholder">
                  <i class="fa-solid fa-user-graduate"></i>
                </div>

                <div>
                  <strong>{{ item.ten }}</strong>
                  <p>{{ item.cauNoiNgan }}</p>
                </div>
              </div>
            </td>

            <td>{{ item.khoa }}</td>

            <td>
                <span class="rating-text">
                  {{ item.danhGia || 5 }} sao
                </span>
            </td>

            <td>
                <span
                    class="status-badge"
                    :class="item.trangThaiHienThi === false ? 'is-hidden' : 'is-showing'"
                >
                  {{ item.trangThaiHienThi === false ? 'Đang ẩn' : 'Hiển thị' }}
                </span>
            </td>

            <td>
              <div class="action-buttons">
                <RouterLink
                    :to="{ name: 'Public.ChiTietCamNhanHocVien', params: { slug: item.slug } }"
                    target="_blank"
                    class="view-button"
                >
                  Xem
                </RouterLink>

                <button type="button" class="edit-button" @click="moFormSua(item)">
                  Sửa
                </button>

                <button type="button" class="delete-button" @click="xoaCamNhan(item)">
                  Xóa
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="!danhSachDaLoc.length">
            <td colspan="5">
              <div class="empty-state">
                Không có cảm nhận học viên phù hợp.
              </div>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { publicNoiDungStore } from '@/modules/public/admin/stores/publicNoiDungStore.js'

const thongBao = ref('')
const tuKhoa = ref('')
const trangThaiLoc = ref('tat-ca')
const dangMoForm = ref(false)
const slugDangSua = ref('')

const danhSach = ref([...publicNoiDungStore.layDanhSachCamNhanHocVien()])

const form = reactive({
  ten: '',
  khoa: '',
  avatar: '',
  danhGia: 5,
  cauNoiNgan: '',
  tieuDe: '',
  namHoc: '',
  hienTai: '',
  noiDung: '',
  diemNoiBatText: '',
  trangThaiHienThi: true
})

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSach.value.filter((item) => {
    const dungTuKhoa =
        !keyword ||
        String(item.ten || '').toLowerCase().includes(keyword) ||
        String(item.khoa || '').toLowerCase().includes(keyword) ||
        String(item.tieuDe || '').toLowerCase().includes(keyword) ||
        String(item.cauNoiNgan || '').toLowerCase().includes(keyword)

    const dungTrangThai =
        trangThaiLoc.value === 'tat-ca' ||
        (trangThaiLoc.value === 'hien-thi' && item.trangThaiHienThi !== false) ||
        (trangThaiLoc.value === 'an' && item.trangThaiHienThi === false)

    return dungTuKhoa && dungTrangThai
  })
})

const tachDoan = (noiDung) => {
  return String(noiDung || '')
      .split(/\n\s*\n/)
      .map((item) => item.trim())
      .filter(Boolean)
}

const tachDong = (noiDung) => {
  return String(noiDung || '')
      .split('\n')
      .map((item) => item.trim())
      .filter(Boolean)
}

const noiDoan = (danhSachDoan) => {
  return Array.isArray(danhSachDoan)
      ? danhSachDoan.join('\n\n')
      : ''
}

const noiDong = (danhSachDong) => {
  return Array.isArray(danhSachDong)
      ? danhSachDong.join('\n')
      : ''
}

const hienThongBao = (noiDung) => {
  thongBao.value = noiDung

  setTimeout(() => {
    thongBao.value = ''
  }, 2500)
}

const resetForm = () => {
  Object.assign(form, {
    ten: '',
    khoa: '',
    avatar: '',
    danhGia: 5,
    cauNoiNgan: '',
    tieuDe: '',
    namHoc: '',
    hienTai: '',
    noiDung: '',
    diemNoiBatText: '',
    trangThaiHienThi: true
  })
}

const napLaiDanhSach = () => {
  danhSach.value = [...publicNoiDungStore.layDanhSachCamNhanHocVien()]
}

const moFormThemMoi = () => {
  slugDangSua.value = ''
  resetForm()
  dangMoForm.value = true
}

const moFormSua = (item) => {
  slugDangSua.value = item.slug

  Object.assign(form, {
    ten: item.ten || '',
    khoa: item.khoa || '',
    avatar: item.avatar || '',
    danhGia: Number(item.danhGia || 5),
    cauNoiNgan: item.cauNoiNgan || '',
    tieuDe: item.tieuDe || '',
    namHoc: item.namHoc || '',
    hienTai: item.hienTai || '',
    noiDung: noiDoan(item.noiDungChiTiet),
    diemNoiBatText: noiDong(item.diemNoiBat),
    trangThaiHienThi: item.trangThaiHienThi !== false
  })

  dangMoForm.value = true
}

const dongForm = () => {
  slugDangSua.value = ''
  resetForm()
  dangMoForm.value = false
}

const luuCamNhan = () => {
  const duLieu = {
    ...form,
    noiDungChiTiet: tachDoan(form.noiDung),
    diemNoiBat: tachDong(form.diemNoiBatText)
  }

  delete duLieu.noiDung
  delete duLieu.diemNoiBatText

  if (slugDangSua.value) {
    publicNoiDungStore.suaCamNhanHocVien(slugDangSua.value, duLieu)
    hienThongBao('Đã cập nhật cảm nhận học viên.')
  } else {
    publicNoiDungStore.themCamNhanHocVien(duLieu)
    hienThongBao('Đã thêm cảm nhận học viên.')
  }

  napLaiDanhSach()
  dongForm()
}

const xoaCamNhan = (item) => {
  const dongY = confirm(`Xóa cảm nhận của "${item.ten}" khỏi website public?`)

  if (!dongY) {
    return
  }

  publicNoiDungStore.xoaCamNhanHocVien(item.slug)
  napLaiDanhSach()
  hienThongBao('Đã xóa cảm nhận học viên.')
}

const khoiPhucMacDinh = () => {
  const dongY = confirm('Khôi phục danh sách cảm nhận học viên mặc định? Dữ liệu bạn đã sửa sẽ bị thay thế.')

  if (!dongY) {
    return
  }

  publicNoiDungStore.khoiPhucCamNhanHocVienMacDinh()
  napLaiDanhSach()
  dongForm()
  hienThongBao('Đã khôi phục danh sách cảm nhận học viên mặc định.')
}
</script>

<style scoped>
.admin-public-cam-nhan-page {
  display: grid;
  gap: 22px;
}

.page-top {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 18px;
  padding: 26px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.back-link {
  width: fit-content;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
  color: #075fc8;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.page-top h1 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 30px;
  font-weight: 900;
}

.page-top p {
  max-width: 820px;
  margin: 0;
  color: #607894;
  line-height: 1.7;
}

.alert-success {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 16px;
  border: 1px solid #bbf7d0;
  border-radius: 14px;
  background: #f0fdf4;
  color: #166534;
  font-size: 14px;
  font-weight: 800;
}

.toolbar-card,
.form-card,
.table-card {
  padding: 24px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.toolbar-card {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 220px auto;
  gap: 16px;
  align-items: end;
}

label {
  display: grid;
  gap: 8px;
}

label span {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

input,
select,
textarea {
  width: 100%;
  border: 1px solid #cfe1f6;
  border-radius: 12px;
  outline: none;
  background: #ffffff;
  color: #123b68;
  font: inherit;
}

input,
select {
  height: 44px;
  padding: 0 14px;
}

textarea {
  resize: vertical;
  padding: 13px 14px;
  line-height: 1.6;
}

input:focus,
select:focus,
textarea:focus {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.12);
}

.primary-button,
.secondary-button {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  padding: 0 18px;
  border-radius: 999px;
  font: inherit;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
  text-decoration: none;
  white-space: nowrap;
}

.primary-button {
  border: none;
  background: #075fc8;
  color: #ffffff;
}

.secondary-button {
  border: 1px solid #cfe1f6;
  background: #ffffff;
  color: #063b7a;
}

.form-title-row {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 20px;
}

.form-title-row h2 {
  margin: 0 0 6px;
  color: #063b7a;
  font-size: 22px;
  font-weight: 900;
}

.form-title-row p {
  margin: 0;
  color: #607894;
  font-size: 14px;
}

.close-button {
  width: 38px;
  height: 38px;
  display: grid;
  place-items: center;
  border: none;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  cursor: pointer;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.full {
  grid-column: 1 / -1;
}

.checkbox-line {
  display: flex;
  align-items: center;
  gap: 10px;
}

.checkbox-line input {
  width: 18px;
  height: 18px;
  accent-color: #075fc8;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 22px;
}

.table-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 16px;
}

.table-header h2 {
  margin: 0;
  color: #063b7a;
  font-size: 22px;
  font-weight: 900;
}

.table-header span {
  color: #607894;
  font-size: 14px;
  font-weight: 800;
}

.table-wrap {
  overflow-x: auto;
}

table {
  width: 100%;
  min-width: 900px;
  border-collapse: collapse;
}

th {
  padding: 14px 16px;
  background: #eff6ff;
  color: #063b7a;
  font-size: 13px;
  font-weight: 900;
  text-align: left;
}

td {
  padding: 14px 16px;
  border-top: 1px solid #e6eef8;
  color: #486581;
  font-size: 14px;
  vertical-align: top;
}

.hoc-vien-cell {
  display: grid;
  grid-template-columns: 58px 1fr;
  gap: 12px;
}

.hoc-vien-cell img,
.avatar-placeholder {
  width: 58px;
  height: 58px;
  border-radius: 999px;
}

.hoc-vien-cell img {
  object-fit: cover;
}

.avatar-placeholder {
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
}

.hoc-vien-cell strong {
  display: block;
  margin-bottom: 5px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.hoc-vien-cell p {
  max-width: 520px;
  display: -webkit-box;
  overflow: hidden;
  margin: 0;
  color: #607894;
  font-size: 13px;
  line-height: 1.55;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.rating-text {
  color: #f59e0b;
  font-weight: 900;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  padding: 0 10px;
  border-radius: 999px;
  font-size: 12px;
  font-weight: 900;
}

.status-badge.is-showing {
  background: #dcfce7;
  color: #166534;
}

.status-badge.is-hidden {
  background: #fee2e2;
  color: #991b1b;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.action-buttons a,
.action-buttons button {
  min-height: 32px;
  padding: 0 10px;
  border-radius: 999px;
  font: inherit;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
  text-decoration: none;
}

.view-button {
  display: inline-flex;
  align-items: center;
  border: 1px solid #cfe1f6;
  background: #ffffff;
  color: #075fc8;
}

.edit-button {
  border: none;
  background: #075fc8;
  color: #ffffff;
}

.delete-button {
  border: none;
  background: #ef4444;
  color: #ffffff;
}

.empty-state {
  padding: 30px;
  color: #607894;
  text-align: center;
}

@media (max-width: 900px) {
  .page-top,
  .toolbar-card {
    grid-template-columns: 1fr;
    display: grid;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    display: grid;
  }
}
</style>