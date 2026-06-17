<template>
  <section class="admin-public-tin-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Tin tức public</h1>
        <p>
          Quản lý danh sách tin tức hiển thị ở website public, trang danh sách tin
          và trang chi tiết tin tức.
        </p>
      </div>

      <button type="button" class="primary-button" @click="moFormThemMoi">
        <i class="fa-solid fa-plus"></i>
        Thêm tin tức
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
            placeholder="Nhập tiêu đề hoặc danh mục"
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

    <form v-if="dangMoForm" class="form-card" @submit.prevent="luuTinTuc">
      <div class="form-title-row">
        <div>
          <h2>{{ idDangSua ? 'Sửa tin tức' : 'Thêm tin tức' }}</h2>
          <p>Dữ liệu này đang lưu tạm trong localStorage.</p>
        </div>

        <button type="button" class="close-button" @click="dongForm">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <div class="form-grid">
        <label>
          <span>Tiêu đề</span>
          <input v-model.trim="form.title" type="text" required placeholder="Nhập tiêu đề tin tức" />
        </label>

        <label>
          <span>Danh mục</span>
          <input v-model.trim="form.category" type="text" required placeholder="Tuyển sinh" />
        </label>

        <label>
          <span>Ngày</span>
          <input v-model.trim="form.day" type="text" placeholder="01" />
        </label>

        <label>
          <span>Tháng</span>
          <input v-model.trim="form.month" type="text" placeholder="TH05" />
        </label>

        <label>
          <span>Tác giả</span>
          <input v-model.trim="form.author" type="text" placeholder="Ban truyền thông" />
        </label>

        <label>
          <span>Link hình ảnh</span>
          <input v-model.trim="form.image" type="url" placeholder="https://..." />
        </label>

        <label class="full">
          <span>Mô tả ngắn</span>
          <textarea
              v-model.trim="form.desc"
              rows="3"
              required
              placeholder="Mô tả ngắn hiển thị ở danh sách tin"
          ></textarea>
        </label>

        <label class="full">
          <span>Nội dung chi tiết</span>
          <textarea
              v-model.trim="form.noiDung"
              rows="8"
              placeholder="Nhập nội dung chi tiết. Mỗi đoạn cách nhau bằng một dòng trống."
          ></textarea>
        </label>

        <label class="checkbox-line full">
          <input v-model="form.trangThaiHienThi" type="checkbox" />
          <span>Hiển thị tin này trên website public</span>
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="secondary-button" @click="dongForm">
          Hủy
        </button>

        <button type="submit" class="primary-button">
          {{ idDangSua ? 'Lưu thay đổi' : 'Thêm tin tức' }}
        </button>
      </div>
    </form>

    <div class="table-card">
      <div class="table-header">
        <h2>Danh sách tin tức</h2>
        <span>{{ danhSachDaLoc.length }} tin</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Tin tức</th>
            <th>Danh mục</th>
            <th>Ngày</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="item in danhSachDaLoc" :key="item.id">
            <td>
              <div class="tin-cell">
                <div class="tin-image">
                  <img v-if="item.image" :src="item.image" :alt="item.title" />
                  <i v-else class="fa-solid fa-newspaper"></i>
                </div>

                <div>
                  <strong>{{ item.title }}</strong>
                  <p>{{ item.desc }}</p>
                </div>
              </div>
            </td>

            <td>{{ item.category }}</td>

            <td>{{ item.day }} {{ item.month }}</td>

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
                    :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }"
                    target="_blank"
                    class="view-button"
                >
                  Xem
                </RouterLink>

                <button type="button" class="edit-button" @click="moFormSua(item)">
                  Sửa
                </button>

                <button type="button" class="delete-button" @click="xoaTinTuc(item)">
                  Xóa
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="!danhSachDaLoc.length">
            <td colspan="5">
              <div class="empty-state">
                Không có tin tức phù hợp.
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
const idDangSua = ref('')

const danhSach = ref([...publicNoiDungStore.layDanhSachTinTuc()])

const form = reactive({
  title: '',
  category: '',
  desc: '',
  image: '',
  day: '',
  month: '',
  author: 'Ban truyền thông',
  noiDung: '',
  trangThaiHienThi: true
})

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSach.value.filter((item) => {
    const dungTuKhoa =
        !keyword ||
        String(item.title || '').toLowerCase().includes(keyword) ||
        String(item.category || '').toLowerCase().includes(keyword) ||
        String(item.desc || '').toLowerCase().includes(keyword)

    const dungTrangThai =
        trangThaiLoc.value === 'tat-ca' ||
        (trangThaiLoc.value === 'hien-thi' && item.trangThaiHienThi !== false) ||
        (trangThaiLoc.value === 'an' && item.trangThaiHienThi === false)

    return dungTuKhoa && dungTrangThai
  })
})

const tachNoiDungThanhDoan = (noiDung) => {
  return String(noiDung || '')
      .split(/\n\s*\n/)
      .map((item) => item.trim())
      .filter(Boolean)
}

const noiDoanThanhNoiDung = (item) => {
  if (Array.isArray(item.noiDungChiTiet) && item.noiDungChiTiet.length) {
    return item.noiDungChiTiet.join('\n\n')
  }

  return item.noiDung || ''
}

const hienThongBao = (noiDung) => {
  thongBao.value = noiDung

  setTimeout(() => {
    thongBao.value = ''
  }, 2500)
}

const resetForm = () => {
  Object.assign(form, {
    title: '',
    category: '',
    desc: '',
    image: '',
    day: '',
    month: '',
    author: 'Ban truyền thông',
    noiDung: '',
    trangThaiHienThi: true
  })
}

const napLaiDanhSach = () => {
  danhSach.value = [...publicNoiDungStore.layDanhSachTinTuc()]
}

const moFormThemMoi = () => {
  idDangSua.value = ''
  resetForm()
  dangMoForm.value = true
}

const moFormSua = (item) => {
  idDangSua.value = item.id

  Object.assign(form, {
    title: item.title || '',
    category: item.category || '',
    desc: item.desc || '',
    image: item.image || '',
    day: item.day || '',
    month: item.month || '',
    author: item.author || 'Ban truyền thông',
    noiDung: noiDoanThanhNoiDung(item),
    trangThaiHienThi: item.trangThaiHienThi !== false
  })

  dangMoForm.value = true
}

const dongForm = () => {
  idDangSua.value = ''
  resetForm()
  dangMoForm.value = false
}

const luuTinTuc = () => {
  const duLieu = {
    ...form,
    noiDungChiTiet: tachNoiDungThanhDoan(form.noiDung)
  }

  if (idDangSua.value) {
    publicNoiDungStore.suaTinTuc(idDangSua.value, duLieu)
    hienThongBao('Đã cập nhật tin tức.')
  } else {
    publicNoiDungStore.themTinTuc(duLieu)
    hienThongBao('Đã thêm tin tức.')
  }

  napLaiDanhSach()
  dongForm()
}

const xoaTinTuc = (item) => {
  const dongY = confirm(`Xóa tin "${item.title}" khỏi website public?`)

  if (!dongY) {
    return
  }

  publicNoiDungStore.xoaTinTuc(item.id)
  napLaiDanhSach()
  hienThongBao('Đã xóa tin tức.')
}

const khoiPhucMacDinh = () => {
  const dongY = confirm('Khôi phục danh sách tin tức mặc định? Dữ liệu bạn đã sửa sẽ bị thay thế.')

  if (!dongY) {
    return
  }

  publicNoiDungStore.khoiPhucTinTucMacDinh()
  napLaiDanhSach()
  dongForm()
  hienThongBao('Đã khôi phục danh sách tin tức mặc định.')
}
</script>

<style scoped>
.admin-public-tin-page {
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

.tin-cell {
  display: grid;
  grid-template-columns: 70px 1fr;
  gap: 12px;
}

.tin-image {
  width: 70px;
  height: 54px;
  display: grid;
  place-items: center;
  overflow: hidden;
  border-radius: 12px;
  background: #eff6ff;
  color: #075fc8;
}

.tin-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tin-cell strong {
  display: block;
  margin-bottom: 5px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.tin-cell p {
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