<template>
  <section class="admin-public-tuyen-sinh-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Tuyển sinh public</h1>

        <p>
          Quản lý các trang chi tiết tuyển sinh như thông tin tuyển sinh,
          hồ sơ xét tuyển, lịch tuyển sinh, học phí chính sách và hỏi đáp tuyển sinh.
        </p>
      </div>

      <button type="button" class="primary-button" @click="moFormThemMoi">
        <i class="fa-solid fa-plus"></i>
        Thêm mục tuyển sinh
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
            placeholder="Nhập tiêu đề hoặc mô tả"
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

    <form v-if="dangMoForm" class="form-card" @submit.prevent="luuTuyenSinh">
      <div class="form-title-row">
        <div>
          <h2>{{ slugDangSua ? 'Sửa mục tuyển sinh' : 'Thêm mục tuyển sinh' }}</h2>
          <p>Dữ liệu này đang lưu tạm trong localStorage.</p>
        </div>

        <button type="button" class="close-button" @click="dongForm">
          <i class="fa-solid fa-xmark"></i>
        </button>
      </div>

      <div class="form-grid">
        <label>
          <span>Tiêu đề</span>
          <input
              v-model.trim="form.tieuDe"
              type="text"
              required
              placeholder="Thông tin tuyển sinh"
          />
        </label>

        <label>
          <span>Icon FontAwesome</span>
          <input
              v-model.trim="form.icon"
              type="text"
              placeholder="fa-solid fa-circle-info"
          />
        </label>

        <label class="full">
          <span>Mô tả</span>
          <textarea
              v-model.trim="form.moTa"
              rows="3"
              required
              placeholder="Mô tả ngắn hiển thị ở đầu trang chi tiết tuyển sinh"
          ></textarea>
        </label>

        <label class="full">
          <span>Thông tin nhanh</span>
          <textarea
              v-model.trim="form.thongTinNhanhText"
              rows="5"
              placeholder="Mỗi dòng là một thông tin nhanh."
          ></textarea>
        </label>

        <label class="full">
          <span>Khối nội dung chi tiết</span>
          <textarea
              v-model.trim="form.noiDungChiTietText"
              rows="12"
              placeholder="Nhập theo mẫu:
Đối tượng tuyển sinh
Nhà trường tiếp nhận hồ sơ của người học.
- Học sinh đã tốt nghiệp THCS.
- Học sinh đã tốt nghiệp THPT.

Phương thức xét tuyển
Nhà trường áp dụng phương thức xét tuyển đơn giản.
- Xét tuyển dựa trên học bạ.
- Tư vấn định hướng ngành học."
          ></textarea>
        </label>

        <label class="checkbox-line full">
          <input v-model="form.trangThaiHienThi" type="checkbox" />
          <span>Hiển thị mục này trên website public</span>
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="secondary-button" @click="dongForm">
          Hủy
        </button>

        <button type="submit" class="primary-button">
          {{ slugDangSua ? 'Lưu thay đổi' : 'Thêm mục tuyển sinh' }}
        </button>
      </div>
    </form>

    <div class="table-card">
      <div class="table-header">
        <h2>Danh sách mục tuyển sinh</h2>
        <span>{{ danhSachDaLoc.length }} mục</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Nội dung</th>
            <th>Đường dẫn</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="item in danhSachDaLoc" :key="item.slug">
            <td>
              <div class="item-cell">
                <div class="item-icon">
                  <i :class="item.icon || 'fa-solid fa-circle-info'"></i>
                </div>

                <div>
                  <strong>{{ item.tieuDe }}</strong>
                  <p>{{ item.moTa }}</p>
                </div>
              </div>
            </td>

            <td>
              <code>/tuyen-sinh/{{ item.slug }}</code>
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
                    :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: item.slug } }"
                    target="_blank"
                    class="view-button"
                >
                  Xem
                </RouterLink>

                <button type="button" class="edit-button" @click="moFormSua(item)">
                  Sửa
                </button>

                <button type="button" class="delete-button" @click="xoaTuyenSinh(item)">
                  Xóa
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="!danhSachDaLoc.length">
            <td colspan="4">
              <div class="empty-state">
                Không có mục tuyển sinh phù hợp.
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
import { publicTuyenSinhStore } from '@/modules/public/admin/stores/publicTuyenSinhStore.js'

const thongBao = ref('')
const tuKhoa = ref('')
const trangThaiLoc = ref('tat-ca')
const dangMoForm = ref(false)
const slugDangSua = ref('')

const danhSach = ref([...publicTuyenSinhStore.layDanhSachTuyenSinh()])

const form = reactive({
  tieuDe: '',
  moTa: '',
  icon: 'fa-solid fa-circle-info',
  thongTinNhanhText: '',
  noiDungChiTietText: '',
  trangThaiHienThi: true
})

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSach.value.filter((item) => {
    const dungTuKhoa =
        !keyword ||
        String(item.tieuDe || '').toLowerCase().includes(keyword) ||
        String(item.moTa || '').toLowerCase().includes(keyword)

    const dungTrangThai =
        trangThaiLoc.value === 'tat-ca' ||
        (trangThaiLoc.value === 'hien-thi' && item.trangThaiHienThi !== false) ||
        (trangThaiLoc.value === 'an' && item.trangThaiHienThi === false)

    return dungTuKhoa && dungTrangThai
  })
})

const tachDong = (noiDung) => {
  return String(noiDung || '')
      .split('\n')
      .map((item) => item.trim())
      .filter(Boolean)
}

const noiDong = (danhSachDong) => {
  return Array.isArray(danhSachDong)
      ? danhSachDong.join('\n')
      : ''
}

const tachNoiDungChiTiet = (noiDung) => {
  return String(noiDung || '')
      .split(/\n\s*\n/)
      .map((khoi) => khoi.trim())
      .filter(Boolean)
      .map((khoi) => {
        const dong = khoi
            .split('\n')
            .map((item) => item.trim())
            .filter(Boolean)

        const tieuDe = dong[0] || 'Nội dung'
        const moTa = dong[1] && !dong[1].startsWith('-') ? dong[1] : ''
        const danhSach = dong
            .slice(moTa ? 2 : 1)
            .map((item) => item.replace(/^-+\s*/, '').trim())
            .filter(Boolean)

        return {
          tieuDe,
          icon: 'fa-solid fa-circle-check',
          moTa,
          danhSach
        }
      })
}

const noiNoiDungChiTiet = (danhSachKhoi) => {
  if (!Array.isArray(danhSachKhoi)) {
    return ''
  }

  return danhSachKhoi
      .map((khoi) => {
        const dong = [
          khoi.tieuDe || '',
          khoi.moTa || '',
          ...(Array.isArray(khoi.danhSach)
              ? khoi.danhSach.map((item) => `- ${item}`)
              : [])
        ]

        return dong.filter(Boolean).join('\n')
      })
      .join('\n\n')
}

const hienThongBao = (noiDung) => {
  thongBao.value = noiDung

  setTimeout(() => {
    thongBao.value = ''
  }, 2500)
}

const resetForm = () => {
  Object.assign(form, {
    tieuDe: '',
    moTa: '',
    icon: 'fa-solid fa-circle-info',
    thongTinNhanhText: '',
    noiDungChiTietText: '',
    trangThaiHienThi: true
  })
}

const napLaiDanhSach = () => {
  danhSach.value = [...publicTuyenSinhStore.layDanhSachTuyenSinh()]
}

const moFormThemMoi = () => {
  slugDangSua.value = ''
  resetForm()
  dangMoForm.value = true
}

const moFormSua = (item) => {
  slugDangSua.value = item.slug

  Object.assign(form, {
    tieuDe: item.tieuDe || '',
    moTa: item.moTa || '',
    icon: item.icon || 'fa-solid fa-circle-info',
    thongTinNhanhText: noiDong(item.thongTinNhanh),
    noiDungChiTietText: noiNoiDungChiTiet(item.noiDungChiTiet),
    trangThaiHienThi: item.trangThaiHienThi !== false
  })

  dangMoForm.value = true
}

const dongForm = () => {
  slugDangSua.value = ''
  resetForm()
  dangMoForm.value = false
}

const luuTuyenSinh = () => {
  const duLieu = {
    tieuDe: form.tieuDe,
    moTa: form.moTa,
    icon: form.icon,
    thongTinNhanh: tachDong(form.thongTinNhanhText),
    noiDungChiTiet: tachNoiDungChiTiet(form.noiDungChiTietText),
    trangThaiHienThi: form.trangThaiHienThi
  }

  if (slugDangSua.value) {
    publicTuyenSinhStore.suaTuyenSinh(slugDangSua.value, duLieu)
    hienThongBao('Đã cập nhật mục tuyển sinh.')
  } else {
    publicTuyenSinhStore.themTuyenSinh(duLieu)
    hienThongBao('Đã thêm mục tuyển sinh.')
  }

  napLaiDanhSach()
  dongForm()
}

const xoaTuyenSinh = (item) => {
  const dongY = confirm(`Xóa mục "${item.tieuDe}" khỏi website public?`)

  if (!dongY) {
    return
  }

  publicTuyenSinhStore.xoaTuyenSinh(item.slug)
  napLaiDanhSach()
  hienThongBao('Đã xóa mục tuyển sinh.')
}

const khoiPhucMacDinh = () => {
  const dongY = confirm('Khôi phục danh sách tuyển sinh mặc định? Dữ liệu bạn đã sửa sẽ bị thay thế.')

  if (!dongY) {
    return
  }

  publicTuyenSinhStore.khoiPhucMacDinh()
  napLaiDanhSach()
  dongForm()
  hienThongBao('Đã khôi phục dữ liệu tuyển sinh mặc định.')
}
</script>

<style scoped>
.admin-public-tuyen-sinh-page {
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

.item-cell {
  display: grid;
  grid-template-columns: 46px 1fr;
  gap: 12px;
}

.item-icon {
  width: 46px;
  height: 46px;
  display: grid;
  place-items: center;
  border-radius: 14px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 20px;
}

.item-cell strong {
  display: block;
  margin-bottom: 5px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.item-cell p {
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

code {
  color: #075fc8;
  font-weight: 800;
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