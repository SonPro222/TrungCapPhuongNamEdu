<template>
  <section class="admin-public-form-page">
    <div class="page-top">
      <div>
        <RouterLink :to="{ name: 'Admin.Public' }" class="back-link">
          <i class="fa-solid fa-arrow-left"></i>
          Quay lại quản lý website
        </RouterLink>

        <h1>Thông tin trường</h1>
        <p>
          Cập nhật thông tin chung của trường dùng cho header, footer, trang liên hệ
          và các khu vực giới thiệu trên website public.
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
          <span>Tên ngắn</span>
          <input v-model.trim="form.tenNgan" type="text" placeholder="Trường Trung Cấp Phương Nam" />
        </label>

        <label>
          <span>Tên dòng 1</span>
          <input v-model.trim="form.tenDong1" type="text" placeholder="Trường Trung Cấp" />
        </label>

        <label>
          <span>Tên dòng 2</span>
          <input v-model.trim="form.tenDong2" type="text" placeholder="Phương Nam" />
        </label>

        <label>
          <span>Slogan</span>
          <input v-model.trim="form.slogan" type="text" placeholder="Tri thức vững vàng – Tương lai rộng mở" />
        </label>

        <label>
          <span>Hotline</span>
          <input v-model.trim="form.hotline" type="text" placeholder="1900 633 088" />
        </label>

        <label>
          <span>Điện thoại</span>
          <input v-model.trim="form.dienThoai" type="text" placeholder="(028) 38 680 886" />
        </label>

        <label>
          <span>Email</span>
          <input v-model.trim="form.email" type="email" placeholder="tuyensinh@phuongnam.edu.vn" />
        </label>

        <label>
          <span>Website</span>
          <input v-model.trim="form.website" type="text" placeholder="www.phuongnam.edu.vn" />
        </label>

        <label class="full">
          <span>Địa chỉ</span>
          <input v-model.trim="form.diaChi" type="text" placeholder="Địa chỉ trường" />
        </label>

        <label class="full">
          <span>Mô tả</span>
          <textarea
              v-model.trim="form.moTa"
              rows="4"
              placeholder="Mô tả ngắn về trường"
          ></textarea>
        </label>
      </div>

      <div class="form-actions">
        <button type="button" class="secondary-button" @click="khoiPhucMacDinh">
          Khôi phục mặc định
        </button>

        <button type="submit" class="primary-button">
          Lưu thông tin trường
        </button>
      </div>
    </form>

    <div class="preview-card">
      <h2>Xem trước</h2>

      <div class="preview-box">
        <div class="preview-logo">PN</div>

        <div>
          <h3>{{ form.tenDong1 }}</h3>
          <h4>{{ form.tenDong2 }}</h4>
          <p>{{ form.slogan }}</p>
        </div>
      </div>

      <ul>
        <li>
          <strong>Hotline:</strong>
          <span>{{ form.hotline }}</span>
        </li>

        <li>
          <strong>Email:</strong>
          <span>{{ form.email }}</span>
        </li>

        <li>
          <strong>Website:</strong>
          <span>{{ form.website }}</span>
        </li>

        <li>
          <strong>Địa chỉ:</strong>
          <span>{{ form.diaChi }}</span>
        </li>
      </ul>
    </div>
  </section>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { publicNoiDungStore } from '@/modules/public/admin/stores/publicNoiDungStore.js'

const thongBao = ref('')

const form = reactive({
  ...publicNoiDungStore.layThongTinTruong()
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
  publicNoiDungStore.luuThongTinTruong({
    ...form
  })

  hienThongBao('Đã lưu thông tin trường.')
}

const khoiPhucMacDinh = () => {
  const duLieu = publicNoiDungStore.khoiPhucThongTinTruongMacDinh()
  ganForm(duLieu)
  hienThongBao('Đã khôi phục thông tin trường mặc định.')
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

label.full {
  grid-column: 1 / -1;
}

label span {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

input,
textarea {
  width: 100%;
  border: 1px solid #cfe1f6;
  border-radius: 12px;
  outline: none;
  background: #ffffff;
  color: #123b68;
  font: inherit;
}

input {
  height: 44px;
  padding: 0 14px;
}

textarea {
  resize: vertical;
  padding: 13px 14px;
  line-height: 1.6;
}

input:focus,
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

.preview-box {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 18px;
  border: 1px solid #dbe7f5;
  border-radius: 18px;
  background: #f8fbff;
}

.preview-logo {
  width: 58px;
  height: 58px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  border: 2px solid #075fc8;
  border-radius: 999px;
  color: #075fc8;
  font-weight: 900;
}

.preview-box h3 {
  margin: 0 0 4px;
  color: #075fc8;
  font-size: 13px;
  text-transform: uppercase;
}

.preview-box h4 {
  margin: 0 0 5px;
  color: #063b7a;
  font-size: 24px;
  font-weight: 900;
}

.preview-box p {
  margin: 0;
  color: #607894;
  font-size: 14px;
}

.preview-card ul {
  display: grid;
  gap: 10px;
  padding: 0;
  margin: 18px 0 0;
  list-style: none;
}

.preview-card li {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 12px;
  color: #486581;
  font-size: 14px;
}

.preview-card strong {
  color: #063b7a;
}

@media (max-width: 760px) {
  .form-grid {
    grid-template-columns: 1fr;
  }

  .form-actions {
    display: grid;
  }

  .preview-card li {
    grid-template-columns: 1fr;
  }
}
</style>