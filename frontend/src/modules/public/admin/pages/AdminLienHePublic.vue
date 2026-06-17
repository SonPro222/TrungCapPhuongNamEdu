<template>
  <section class="admin-public-lien-he-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Liên hệ / Tư vấn public</h1>

        <p>
          Quản lý danh sách thông tin người dùng gửi từ form liên hệ trên website public.
          Dữ liệu hiện đang lưu tạm trong localStorage.
        </p>
      </div>

      <button
          type="button"
          class="danger-outline-button"
          :disabled="!danhSach.length"
          @click="xoaTatCa"
      >
        <i class="fa-solid fa-trash"></i>
        Xóa tất cả
      </button>
    </div>

    <div class="stats-grid">
      <div class="stat-card">
        <span>Tổng liên hệ</span>
        <strong>{{ thongKe.tong }}</strong>
      </div>

      <div class="stat-card">
        <span>Mới</span>
        <strong>{{ thongKe.moi }}</strong>
      </div>

      <div class="stat-card">
        <span>Đã liên hệ</span>
        <strong>{{ thongKe.daLienHe }}</strong>
      </div>

      <div class="stat-card">
        <span>Đã xử lý</span>
        <strong>{{ thongKe.daXuLy }}</strong>
      </div>
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
            placeholder="Nhập họ tên, số điện thoại, email hoặc ngành quan tâm"
        />
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="trangThaiLoc">
          <option value="tat-ca">Tất cả</option>
          <option value="moi">Mới</option>
          <option value="da-lien-he">Đã liên hệ</option>
          <option value="da-xu-ly">Đã xử lý</option>
        </select>
      </label>
    </div>

    <div class="table-card">
      <div class="table-header">
        <h2>Danh sách liên hệ</h2>
        <span>{{ danhSachDaLoc.length }} liên hệ</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Người gửi</th>
            <th>Ngành quan tâm</th>
            <th>Nội dung</th>
            <th>Thời gian</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="item in danhSachDaLoc" :key="item.id">
            <td>
              <div class="person-cell">
                <div class="person-icon">
                  <i class="fa-solid fa-user"></i>
                </div>

                <div>
                  <strong>{{ item.hoTen }}</strong>
                  <p>{{ item.soDienThoai }}</p>
                  <p>{{ item.email || 'Chưa có email' }}</p>
                </div>
              </div>
            </td>

            <td>
              {{ item.nganhQuanTam || 'Chưa chọn' }}
            </td>

            <td>
              <p class="message-text">
                {{ item.noiDung || 'Không có nội dung' }}
              </p>
            </td>

            <td>
              {{ dinhDangThoiGian(item.thoiGianGui) }}
            </td>

            <td>
                <span
                    class="status-badge"
                    :class="`status-${item.trangThai || 'moi'}`"
                >
                  {{ hienThiTrangThai(item.trangThai) }}
                </span>
            </td>

            <td>
              <div class="action-buttons">
                <button
                    type="button"
                    class="contacted-button"
                    @click="capNhatTrangThai(item, 'da-lien-he')"
                >
                  Đã liên hệ
                </button>

                <button
                    type="button"
                    class="done-button"
                    @click="capNhatTrangThai(item, 'da-xu-ly')"
                >
                  Đã xử lý
                </button>

                <button
                    type="button"
                    class="delete-button"
                    @click="xoaLienHe(item)"
                >
                  Xóa
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="!danhSachDaLoc.length">
            <td colspan="6">
              <div class="empty-state">
                Chưa có liên hệ phù hợp.
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
import { computed, ref } from 'vue'
import { publicLienHeStore } from '@/modules/public/admin/stores/publicLienHeStore.js'

const thongBao = ref('')
const tuKhoa = ref('')
const trangThaiLoc = ref('tat-ca')
const danhSach = ref([...publicLienHeStore.layDanhSachLienHe()])

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSach.value.filter((item) => {
    const dungTuKhoa =
        !keyword ||
        String(item.hoTen || '').toLowerCase().includes(keyword) ||
        String(item.soDienThoai || '').toLowerCase().includes(keyword) ||
        String(item.email || '').toLowerCase().includes(keyword) ||
        String(item.nganhQuanTam || '').toLowerCase().includes(keyword) ||
        String(item.noiDung || '').toLowerCase().includes(keyword)

    const dungTrangThai =
        trangThaiLoc.value === 'tat-ca' ||
        item.trangThai === trangThaiLoc.value

    return dungTuKhoa && dungTrangThai
  })
})

const thongKe = computed(() => {
  return {
    tong: danhSach.value.length,
    moi: danhSach.value.filter((item) => item.trangThai === 'moi' || !item.trangThai).length,
    daLienHe: danhSach.value.filter((item) => item.trangThai === 'da-lien-he').length,
    daXuLy: danhSach.value.filter((item) => item.trangThai === 'da-xu-ly').length
  }
})

const napLaiDanhSach = () => {
  danhSach.value = [...publicLienHeStore.layDanhSachLienHe()]
}

const hienThongBao = (noiDung) => {
  thongBao.value = noiDung

  setTimeout(() => {
    thongBao.value = ''
  }, 2500)
}

const hienThiTrangThai = (trangThai) => {
  if (trangThai === 'da-lien-he') {
    return 'Đã liên hệ'
  }

  if (trangThai === 'da-xu-ly') {
    return 'Đã xử lý'
  }

  return 'Mới'
}

const dinhDangThoiGian = (giaTri) => {
  if (!giaTri) {
    return 'Không rõ'
  }

  const ngay = new Date(giaTri)

  if (Number.isNaN(ngay.getTime())) {
    return 'Không rõ'
  }

  return ngay.toLocaleString('vi-VN')
}

const capNhatTrangThai = (item, trangThai) => {
  publicLienHeStore.capNhatTrangThai(item.id, trangThai)
  napLaiDanhSach()
  hienThongBao('Đã cập nhật trạng thái liên hệ.')
}

const xoaLienHe = (item) => {
  const dongY = confirm(`Xóa liên hệ của "${item.hoTen}"?`)

  if (!dongY) {
    return
  }

  publicLienHeStore.xoaLienHe(item.id)
  napLaiDanhSach()
  hienThongBao('Đã xóa liên hệ.')
}

const xoaTatCa = () => {
  const dongY = confirm('Xóa toàn bộ danh sách liên hệ?')

  if (!dongY) {
    return
  }

  publicLienHeStore.xoaTatCa()
  napLaiDanhSach()
  hienThongBao('Đã xóa toàn bộ liên hệ.')
}
</script>

<style scoped>
.admin-public-lien-he-page {
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

.danger-outline-button {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 18px;
  border: 1px solid #fecaca;
  border-radius: 999px;
  background: #ffffff;
  color: #dc2626;
  font: inherit;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
}

.danger-outline-button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}

.stat-card {
  padding: 20px;
  border: 1px solid #dbe7f5;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.stat-card span {
  display: block;
  margin-bottom: 8px;
  color: #607894;
  font-size: 13px;
  font-weight: 900;
}

.stat-card strong {
  color: #063b7a;
  font-size: 28px;
  font-weight: 900;
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
.table-card {
  padding: 24px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.toolbar-card {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 220px;
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
select {
  width: 100%;
  height: 44px;
  padding: 0 14px;
  border: 1px solid #cfe1f6;
  border-radius: 12px;
  outline: none;
  background: #ffffff;
  color: #123b68;
  font: inherit;
}

input:focus,
select:focus {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.12);
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
  min-width: 1100px;
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

.person-cell {
  display: grid;
  grid-template-columns: 46px 1fr;
  gap: 12px;
}

.person-icon {
  width: 46px;
  height: 46px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
}

.person-cell strong {
  display: block;
  margin-bottom: 5px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.person-cell p {
  margin: 0 0 4px;
  color: #607894;
  font-size: 13px;
}

.message-text {
  max-width: 340px;
  display: -webkit-box;
  overflow: hidden;
  margin: 0;
  color: #486581;
  line-height: 1.55;
  -webkit-line-clamp: 3;
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

.status-moi {
  background: #dbeafe;
  color: #075fc8;
}

.status-da-lien-he {
  background: #fef3c7;
  color: #92400e;
}

.status-da-xu-ly {
  background: #dcfce7;
  color: #166534;
}

.action-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.action-buttons button {
  min-height: 32px;
  padding: 0 10px;
  border: none;
  border-radius: 999px;
  font: inherit;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
}

.contacted-button {
  background: #f59e0b;
  color: #ffffff;
}

.done-button {
  background: #16a34a;
  color: #ffffff;
}

.delete-button {
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
    display: grid;
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 560px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>