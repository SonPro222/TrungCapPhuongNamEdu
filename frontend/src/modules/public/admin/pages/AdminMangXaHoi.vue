<template>
  <section class="admin-public-form-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Mạng xã hội</h1>
        <p>
          Cập nhật đường dẫn mạng xã hội dùng cho footer public. Link nên là link thật
          của trường, mở tab mới khi người dùng bấm vào.
        </p>
      </div>
    </div>

    <div v-if="thongBao" class="alert-success">
      <i class="fa-solid fa-circle-check"></i>
      <span>{{ thongBao }}</span>
    </div>

    <form class="form-card" @submit.prevent="luuDuLieu">
      <div class="form-grid">
        <label>
          <span>Facebook</span>
          <input v-model.trim="form.facebook" type="url" placeholder="https://www.facebook.com/..." />
        </label>

        <label>
          <span>Youtube</span>
          <input v-model.trim="form.youtube" type="url" placeholder="https://www.youtube.com/..." />
        </label>

        <label>
          <span>TikTok</span>
          <input v-model.trim="form.tiktok" type="url" placeholder="https://www.tiktok.com/@..." />
        </label>

        <label>
          <span>Zalo</span>
          <input v-model.trim="form.zalo" type="url" placeholder="https://zalo.me/..." />
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="secondary-button" @click="khoiPhucMacDinh">
          Khôi phục mặc định
        </button>

        <button type="submit" class="primary-button">
          Lưu mạng xã hội
        </button>
      </div>
    </form>

    <div class="preview-card">
      <h2>Xem trước icon footer</h2>

      <div class="footer-social-preview">
        <a
            :href="form.facebook"
            target="_blank"
            rel="noopener noreferrer"
            aria-label="Facebook"
            title="Facebook"
        >
          <i class="fa-brands fa-facebook-f"></i>
        </a>

        <a
            :href="form.youtube"
            target="_blank"
            rel="noopener noreferrer"
            aria-label="Youtube"
            title="Youtube"
        >
          <i class="fa-brands fa-youtube"></i>
        </a>

        <a
            :href="form.tiktok"
            target="_blank"
            rel="noopener noreferrer"
            aria-label="TikTok"
            title="TikTok"
        >
          <i class="fa-brands fa-tiktok"></i>
        </a>

        <a
            :href="form.zalo"
            target="_blank"
            rel="noopener noreferrer"
            aria-label="Zalo"
            title="Zalo"
        >
          <span>Zalo</span>
        </a>
      </div>

      <div class="link-list">
        <p>
          <strong>Facebook:</strong>
          <span>{{ form.facebook }}</span>
        </p>

        <p>
          <strong>Youtube:</strong>
          <span>{{ form.youtube }}</span>
        </p>

        <p>
          <strong>TikTok:</strong>
          <span>{{ form.tiktok }}</span>
        </p>

        <p>
          <strong>Zalo:</strong>
          <span>{{ form.zalo }}</span>
        </p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { publicNoiDungStore } from '@/modules/public/admin/stores/publicNoiDungStore.js'

const thongBao = ref('')

const form = reactive({
  ...publicNoiDungStore.layMangXaHoi()
})

const hienThongBao = (noiDung) => {
  thongBao.value = noiDung

  setTimeout(() => {
    thongBao.value = ''
  }, 2500)
}

const ganForm = (duLieu) => {
  Object.assign(form, duLieu)
}

const luuDuLieu = () => {
  publicNoiDungStore.luuMangXaHoi({
    ...form
  })

  hienThongBao('Đã lưu mạng xã hội.')
}

const khoiPhucMacDinh = () => {
  const duLieu = publicNoiDungStore.khoiPhucMangXaHoiMacDinh()
  ganForm(duLieu)
  hienThongBao('Đã khôi phục link mạng xã hội mặc định.')
}
</script>

<style scoped>
.admin-public-form-page {
  display: grid;
  gap: 22px;
}

.page-top {
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

label {
  display: grid;
  gap: 8px;
}

label span {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

input {
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

input:focus {
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

.footer-social-preview {
  display: flex;
  gap: 12px;
  padding: 18px;
  border-radius: 18px;
  background: #063b7a;
}

.footer-social-preview a {
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  text-decoration: none;
}

.footer-social-preview span {
  font-size: 11px;
  font-weight: 900;
}

.link-list {
  display: grid;
  gap: 10px;
  margin-top: 18px;
}

.link-list p {
  display: grid;
  grid-template-columns: 100px 1fr;
  gap: 12px;
  margin: 0;
  color: #486581;
  font-size: 14px;
  word-break: break-all;
}

.link-list strong {
  color: #063b7a;
}

@media (max-width: 760px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    display: grid;
  }

  .link-list p {
    grid-template-columns: 1fr;
  }
}
</style>