<template>
  <section class="admin-public-banner-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Banner trang chủ</h1>

        <p>
          Quản lý nội dung hero/banner đầu trang chủ: tiêu đề, mô tả, ảnh nền,
          nút điều hướng và thống kê nổi bật.
        </p>
      </div>

      <RouterLink :to="{ name: 'Public.TrangChu' }" target="_blank" class="preview-button">
        Xem trang chủ
        <i class="fa-solid fa-arrow-up-right-from-square"></i>
      </RouterLink>
    </div>

    <div v-if="thongBao" class="alert-success">
      <i class="fa-solid fa-circle-check"></i>
      <span>{{ thongBao }}</span>
    </div>

    <form class="form-card" @submit.prevent="luuDuLieu">
      <div class="form-grid">
        <label>
          <span>Nhãn nhỏ</span>
          <input v-model.trim="form.nhanNho" type="text" placeholder="Tuyển sinh 2024" />
        </label>

        <label>
          <span>Trạng thái</span>
          <select v-model="form.trangThaiHienThi">
            <option :value="true">Hiển thị</option>
            <option :value="false">Ẩn banner</option>
          </select>
        </label>

        <label class="full">
          <span>Tiêu đề chính</span>
          <input v-model.trim="form.tieuDe" type="text" placeholder="Trường Trung cấp Phương Nam" />
        </label>

        <label class="full">
          <span>Tiêu đề phụ</span>
          <input v-model.trim="form.tieuDePhu" type="text" placeholder="Tri thức vững vàng – Tương lai rộng mở" />
        </label>

        <label class="full">
          <span>Mô tả</span>
          <textarea
              v-model.trim="form.moTa"
              rows="4"
              placeholder="Mô tả ngắn hiển thị ở banner trang chủ"
          ></textarea>
        </label>

        <label class="full">
          <span>Ảnh nền</span>
          <input v-model.trim="form.anhNen" type="url" placeholder="https://..." />
        </label>

        <label>
          <span>Nút chính - nội dung</span>
          <input v-model.trim="form.nutChinhText" type="text" placeholder="Đăng ký tư vấn" />
        </label>

        <label>
          <span>Nút chính - route name</span>
          <input v-model.trim="form.nutChinhRoute" type="text" placeholder="Public.LienHe" />
        </label>

        <label>
          <span>Nút phụ - nội dung</span>
          <input v-model.trim="form.nutPhuText" type="text" placeholder="Xem ngành đào tạo" />
        </label>

        <label>
          <span>Nút phụ - route name</span>
          <input v-model.trim="form.nutPhuRoute" type="text" placeholder="Public.NganhDaoTao" />
        </label>

        <label class="full">
          <span>Thống kê nổi bật</span>
          <textarea
              v-model.trim="form.thongKeText"
              rows="5"
              placeholder="Mỗi dòng theo mẫu: 20+ | Ngành đào tạo"
          ></textarea>
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="secondary-button" @click="khoiPhucMacDinh">
          Khôi phục mặc định
        </button>

        <button type="submit" class="primary-button">
          Lưu banner trang chủ
        </button>
      </div>
    </form>

    <div class="preview-card">
      <h2>Xem trước banner</h2>

      <div
          class="banner-preview"
          :style="{ backgroundImage: `linear-gradient(90deg, rgba(3, 59, 122, 0.86), rgba(3, 59, 122, 0.45)), url(${form.anhNen})` }"
      >
        <div class="banner-preview-content">
          <span>{{ form.nhanNho }}</span>
          <h3>{{ form.tieuDe }}</h3>
          <h4>{{ form.tieuDePhu }}</h4>
          <p>{{ form.moTa }}</p>

          <div class="preview-actions">
            <button type="button">{{ form.nutChinhText }}</button>
            <button type="button" class="outline">{{ form.nutPhuText }}</button>
          </div>

          <div class="preview-stats">
            <div v-for="item in thongKeXemTruoc" :key="`${item.soLieu}-${item.noiDung}`">
              <strong>{{ item.soLieu }}</strong>
              <small>{{ item.noiDung }}</small>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { publicBannerTrangChuStore } from '@/modules/public/admin/stores/publicBannerTrangChuStore.js'

const thongBao = ref('')

const banner = publicBannerTrangChuStore.layBannerTrangChu()

const form = reactive({
  nhanNho: banner.nhanNho || '',
  tieuDe: banner.tieuDe || '',
  tieuDePhu: banner.tieuDePhu || '',
  moTa: banner.moTa || '',
  anhNen: banner.anhNen || '',
  nutChinhText: banner.nutChinhText || '',
  nutChinhRoute: banner.nutChinhRoute || '',
  nutPhuText: banner.nutPhuText || '',
  nutPhuRoute: banner.nutPhuRoute || '',
  thongKeText: Array.isArray(banner.thongKe)
      ? banner.thongKe.map((item) => `${item.soLieu} | ${item.noiDung}`).join('\n')
      : '',
  trangThaiHienThi: banner.trangThaiHienThi !== false
})

const tachThongKe = (noiDung) => {
  return String(noiDung || '')
      .split('\n')
      .map((dong) => dong.trim())
      .filter(Boolean)
      .map((dong) => {
        const [soLieu, noiDungThongKe] = dong.split('|').map((item) => item.trim())

        return {
          soLieu: soLieu || '',
          noiDung: noiDungThongKe || ''
        }
      })
      .filter((item) => item.soLieu || item.noiDung)
}

const thongKeXemTruoc = computed(() => {
  return tachThongKe(form.thongKeText)
})

const hienThongBao = (noiDung) => {
  thongBao.value = noiDung

  setTimeout(() => {
    thongBao.value = ''
  }, 2500)
}

const ganForm = (duLieu) => {
  Object.assign(form, {
    nhanNho: duLieu.nhanNho || '',
    tieuDe: duLieu.tieuDe || '',
    tieuDePhu: duLieu.tieuDePhu || '',
    moTa: duLieu.moTa || '',
    anhNen: duLieu.anhNen || '',
    nutChinhText: duLieu.nutChinhText || '',
    nutChinhRoute: duLieu.nutChinhRoute || '',
    nutPhuText: duLieu.nutPhuText || '',
    nutPhuRoute: duLieu.nutPhuRoute || '',
    thongKeText: Array.isArray(duLieu.thongKe)
        ? duLieu.thongKe.map((item) => `${item.soLieu} | ${item.noiDung}`).join('\n')
        : '',
    trangThaiHienThi: duLieu.trangThaiHienThi !== false
  })
}

const luuDuLieu = () => {
  publicBannerTrangChuStore.luuBannerTrangChu({
    nhanNho: form.nhanNho,
    tieuDe: form.tieuDe,
    tieuDePhu: form.tieuDePhu,
    moTa: form.moTa,
    anhNen: form.anhNen,
    nutChinhText: form.nutChinhText,
    nutChinhRoute: form.nutChinhRoute,
    nutPhuText: form.nutPhuText,
    nutPhuRoute: form.nutPhuRoute,
    thongKe: tachThongKe(form.thongKeText),
    trangThaiHienThi: form.trangThaiHienThi
  })

  hienThongBao('Đã lưu banner trang chủ.')
}

const khoiPhucMacDinh = () => {
  const dongY = confirm('Khôi phục banner trang chủ mặc định? Dữ liệu bạn đã sửa sẽ bị thay thế.')

  if (!dongY) {
    return
  }

  const duLieu = publicBannerTrangChuStore.khoiPhucMacDinh()
  ganForm(duLieu)
  hienThongBao('Đã khôi phục banner mặc định.')
}
</script>

<style scoped>
.admin-public-banner-page {
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

.preview-button {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 18px;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
  white-space: nowrap;
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

.form-card,
.preview-card {
  padding: 26px;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 18px;
}

.full {
  grid-column: 1 / -1;
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

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 22px;
}

.primary-button,
.secondary-button {
  min-height: 42px;
  padding: 0 18px;
  border-radius: 999px;
  font: inherit;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
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

.preview-card h2 {
  margin: 0 0 18px;
  color: #063b7a;
  font-size: 22px;
  font-weight: 900;
}

.banner-preview {
  min-height: 440px;
  overflow: hidden;
  border-radius: 22px;
  background-position: center;
  background-size: cover;
}

.banner-preview-content {
  max-width: 780px;
  padding: 54px;
  color: #ffffff;
}

.banner-preview-content > span {
  display: inline-flex;
  margin-bottom: 14px;
  padding: 8px 14px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 13px;
  font-weight: 900;
}

.banner-preview-content h3 {
  margin: 0 0 10px;
  font-size: 44px;
  line-height: 1.12;
  font-weight: 900;
}

.banner-preview-content h4 {
  margin: 0 0 16px;
  font-size: 25px;
  line-height: 1.3;
  font-weight: 800;
}

.banner-preview-content p {
  max-width: 680px;
  margin: 0;
  color: #dbeafe;
  font-size: 16px;
  line-height: 1.8;
}

.preview-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 26px;
}

.preview-actions button {
  min-height: 44px;
  padding: 0 18px;
  border: none;
  border-radius: 999px;
  background: #ffffff;
  color: #063b7a;
  font-weight: 900;
}

.preview-actions button.outline {
  border: 1px solid rgba(255, 255, 255, 0.68);
  background: transparent;
  color: #ffffff;
}

.preview-stats {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
  margin-top: 34px;
}

.preview-stats div {
  min-width: 130px;
  padding: 16px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.16);
  backdrop-filter: blur(6px);
}

.preview-stats strong {
  display: block;
  font-size: 28px;
  font-weight: 900;
}

.preview-stats small {
  display: block;
  margin-top: 4px;
  color: #dbeafe;
  font-size: 13px;
  font-weight: 800;
}

@media (max-width: 900px) {
  .page-top {
    display: grid;
  }

  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    display: grid;
  }

  .banner-preview-content {
    padding: 32px;
  }

  .banner-preview-content h3 {
    font-size: 32px;
  }
}
</style>