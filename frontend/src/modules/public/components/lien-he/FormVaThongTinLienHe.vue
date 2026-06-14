<template>
  <section class="contact-form-section">
    <div class="form-card">
      <div class="section-heading">
        <span>Gửi thông tin</span>
        <h2>Đăng ký tư vấn</h2>
        <p>
          Để lại thông tin, bộ phận tuyển sinh sẽ liên hệ hỗ trợ bạn trong thời gian sớm nhất.
        </p>
      </div>

      <div v-if="thongBao" class="alert-success">
        <i class="fa-solid fa-circle-check"></i>
        <span>{{ thongBao }}</span>
      </div>

      <form class="contact-form" @submit.prevent="guiLienHe">
        <label>
          <span>Họ và tên</span>
          <input
              v-model.trim="form.hoTen"
              type="text"
              placeholder="Nhập họ và tên"
              required
          />
        </label>

        <label>
          <span>Số điện thoại</span>
          <input
              v-model.trim="form.soDienThoai"
              type="tel"
              placeholder="Nhập số điện thoại"
              required
          />
        </label>

        <label>
          <span>Email</span>
          <input
              v-model.trim="form.email"
              type="email"
              placeholder="Nhập email"
          />
        </label>

        <label>
          <span>Ngành quan tâm</span>
          <select v-model="form.nganhQuanTam">
            <option value="">Chọn ngành quan tâm</option>
            <option
                v-for="item in danhSachNganhHienThi"
                :key="item.id"
                :value="item.title"
            >
              {{ item.title }}
            </option>
          </select>
        </label>

        <label class="full">
          <span>Nội dung cần tư vấn</span>
          <textarea
              v-model.trim="form.noiDung"
              rows="5"
              placeholder="Nhập nội dung bạn cần tư vấn"
          ></textarea>
        </label>

        <button type="submit">
          Gửi thông tin tư vấn
          <i class="fa-solid fa-paper-plane"></i>
        </button>
      </form>
    </div>

    <div class="info-card">
      <div class="section-heading">
        <span>Thông tin liên hệ</span>
        <h2>Liên hệ nhà trường</h2>
        <p>
          Nhà trường luôn sẵn sàng hỗ trợ học viên, phụ huynh và đối tác.
        </p>
      </div>

      <ul class="contact-list">
        <li>
          <i class="fa-solid fa-location-dot"></i>
          <div>
            <strong>Địa chỉ</strong>
            <span>{{ thongTinTruong.diaChi }}</span>
          </div>
        </li>

        <li>
          <i class="fa-solid fa-phone"></i>
          <div>
            <strong>Hotline</strong>
            <span>{{ thongTinTruong.hotline }}</span>
          </div>
        </li>

        <li>
          <i class="fa-solid fa-envelope"></i>
          <div>
            <strong>Email</strong>
            <span>{{ thongTinTruong.email }}</span>
          </div>
        </li>

        <li>
          <i class="fa-solid fa-globe"></i>
          <div>
            <strong>Website</strong>
            <span>{{ thongTinTruong.website }}</span>
          </div>
        </li>
      </ul>

      <div class="social-box">
        <h3>Kết nối với chúng tôi</h3>

        <div class="social-list">
          <a
              :href="mangXaHoi.facebook"
              target="_blank"
              rel="noopener noreferrer"
              class="facebook"
              aria-label="Facebook"
          >
            <i class="fa-brands fa-facebook-f"></i>
          </a>

          <a
              :href="mangXaHoi.youtube"
              target="_blank"
              rel="noopener noreferrer"
              class="youtube"
              aria-label="Youtube"
          >
            <i class="fa-brands fa-youtube"></i>
          </a>

          <a
              :href="mangXaHoi.zalo"
              target="_blank"
              rel="noopener noreferrer"
              class="zalo"
              aria-label="Zalo"
          >
            <i class="fa-solid fa-comment-dots"></i>
          </a>

          <a
              :href="mangXaHoi.tiktok"
              target="_blank"
              rel="noopener noreferrer"
              class="tiktok"
              aria-label="TikTok"
          >
            <i class="fa-brands fa-tiktok"></i>
          </a>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useLienHePublic } from '@/modules/public/composables/useLienHePublic.js'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const { themLienHe } = useLienHePublic()

const {
  thongTinTruong: thongTinTruongRef,
  mangXaHoi: mangXaHoiRef,
  danhSachNganhDaoTao
} = useNoiDungPublic()

const thongBao = ref('')

const form = reactive({
  hoTen: '',
  soDienThoai: '',
  email: '',
  nganhQuanTam: '',
  noiDung: ''
})

const thongTinTruong = computed(() => thongTinTruongRef.value)
const mangXaHoi = computed(() => mangXaHoiRef.value)

const danhSachNganhHienThi = computed(() => {
  return danhSachNganhDaoTao.value.filter((item) => item.trangThaiHienThi !== false)
})

const resetForm = () => {
  Object.assign(form, {
    hoTen: '',
    soDienThoai: '',
    email: '',
    nganhQuanTam: '',
    noiDung: ''
  })
}

const guiLienHe = () => {
  themLienHe({
    ...form
  })

  thongBao.value = 'Đã gửi thông tin tư vấn. Nhà trường sẽ liên hệ lại sớm nhất.'

  resetForm()

  setTimeout(() => {
    thongBao.value = ''
  }, 3500)
}
</script>

<style scoped>
.contact-form-section {
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) 0.9fr;
  gap: 26px;
}

.form-card,
.info-card {
  padding: 30px;
  border: 1px solid #e6eef8;
  border-radius: 24px;
  background: #ffffff;
  box-shadow: 0 10px 26px rgba(7, 63, 131, 0.06);
}

.section-heading {
  margin-bottom: 22px;
}

.section-heading span {
  display: inline-flex;
  margin-bottom: 8px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.section-heading h2 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 28px;
  font-weight: 900;
}

.section-heading p {
  margin: 0;
  color: #607894;
  line-height: 1.7;
}

.alert-success {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 18px;
  padding: 14px 16px;
  border: 1px solid #bbf7d0;
  border-radius: 14px;
  background: #f0fdf4;
  color: #166534;
  font-size: 14px;
  font-weight: 800;
}

.contact-form {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
}

.contact-form label {
  display: grid;
  gap: 8px;
}

.contact-form label.full {
  grid-column: 1 / -1;
}

.contact-form label span {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.contact-form input,
.contact-form select,
.contact-form textarea {
  width: 100%;
  border: 1px solid #cfe1f6;
  border-radius: 12px;
  outline: none;
  background: #ffffff;
  color: #123b68;
  font: inherit;
}

.contact-form input,
.contact-form select {
  height: 46px;
  padding: 0 14px;
}

.contact-form textarea {
  resize: vertical;
  padding: 13px 14px;
  line-height: 1.6;
}

.contact-form input:focus,
.contact-form select:focus,
.contact-form textarea:focus {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.12);
}

.contact-form button {
  grid-column: 1 / -1;
  min-height: 48px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
  border: none;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font: inherit;
  font-size: 15px;
  font-weight: 900;
  cursor: pointer;
}

.contact-list {
  display: grid;
  gap: 16px;
  padding: 0;
  margin: 0 0 24px;
  list-style: none;
}

.contact-list li {
  display: grid;
  grid-template-columns: 44px 1fr;
  gap: 12px;
}

.contact-list i {
  width: 44px;
  height: 44px;
  display: grid;
  place-items: center;
  border-radius: 14px;
  background: #eff6ff;
  color: #075fc8;
}

.contact-list strong {
  display: block;
  margin-bottom: 4px;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.contact-list span {
  color: #607894;
  font-size: 14px;
  line-height: 1.55;
}

.social-box {
  padding: 20px;
  border-radius: 18px;
  background: #eff6ff;
}

.social-box h3 {
  margin: 0 0 14px;
  color: #063b7a;
  font-size: 18px;
  font-weight: 900;
}

.social-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.social-list a {
  width: 40px;
  height: 40px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  text-decoration: none;
}

@media (max-width: 1180px) {
  .contact-form-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 700px) {
  .form-card,
  .info-card {
    padding: 22px;
  }

  .contact-form {
    grid-template-columns: 1fr;
  }
}
</style>