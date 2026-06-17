<template>
  <footer class="public-footer">
    <div class="footer-main">
      <div class="public-container footer-grid">
        <div class="footer-about">
          <div class="footer-brand">
            <div class="footer-logo">
              <span>PN</span>
            </div>

            <div>
              <h2>{{ thongTinTruong.tenDong1 }}</h2>
              <h3>{{ thongTinTruong.tenDong2 }}</h3>
            </div>
          </div>

          <p>
            {{ thongTinTruong.moTa }}
          </p>

          <div class="footer-social">
            <a
                :href="mangXaHoi.facebook"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Facebook"
                title="Facebook"
            >
              <i class="fa-brands fa-facebook-f"></i>
            </a>

            <a
                :href="mangXaHoi.youtube"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Youtube"
                title="Youtube"
            >
              <i class="fa-brands fa-youtube"></i>
            </a>

            <a
                :href="mangXaHoi.tiktok"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="TikTok"
                title="TikTok"
            >
              <i class="fa-brands fa-tiktok"></i>
            </a>

            <a
                :href="mangXaHoi.zalo"
                target="_blank"
                rel="noopener noreferrer"
                aria-label="Zalo"
                title="Zalo"
            >
              <span>Zalo</span>
            </a>
          </div>
        </div>

        <div class="footer-column">
          <h4>Liên kết nhanh</h4>

          <RouterLink :to="{ name: 'Public.TrangChu' }">
            Trang chủ
          </RouterLink>

          <RouterLink :to="{ name: 'Public.GioiThieu' }">
            Giới thiệu
          </RouterLink>

          <RouterLink :to="{ name: 'Public.NganhDaoTao' }">
            Ngành đào tạo
          </RouterLink>

          <RouterLink :to="{ name: 'Public.TuyenSinh' }">
            Tuyển sinh
          </RouterLink>

          <RouterLink :to="{ name: 'Public.TinTuc' }">
            Tin tức
          </RouterLink>

          <RouterLink :to="{ name: 'Public.LienHe' }">
            Liên hệ
          </RouterLink>
        </div>

        <div class="footer-column">
          <h4>Tuyển sinh</h4>

          <RouterLink
              :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: 'thong-tin-tuyen-sinh' } }"
          >
            Thông tin tuyển sinh
          </RouterLink>

          <RouterLink
              :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: 'ho-so-xet-tuyen' } }"
          >
            Hồ sơ xét tuyển
          </RouterLink>

          <RouterLink
              :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: 'lich-tuyen-sinh' } }"
          >
            Lịch tuyển sinh
          </RouterLink>

          <RouterLink
              :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: 'hoc-phi-chinh-sach' } }"
          >
            Học phí & chính sách
          </RouterLink>

          <RouterLink
              :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: 'hoi-dap-tuyen-sinh' } }"
          >
            Hỏi đáp tuyển sinh
          </RouterLink>
        </div>

        <div class="footer-contact">
          <h4>Thông tin liên hệ</h4>

          <ul>
            <li>
              <i class="fa-solid fa-location-dot"></i>
              <span>Địa chỉ: {{ thongTinTruong.diaChi }}</span>
            </li>

            <li>
              <i class="fa-solid fa-phone"></i>
              <span>Điện thoại: {{ thongTinTruong.hotline }}</span>
            </li>

            <li>
              <i class="fa-solid fa-envelope"></i>
              <span>Email: {{ thongTinTruong.email }}</span>
            </li>

            <li>
              <i class="fa-solid fa-globe"></i>
              <span>Website: {{ thongTinTruong.website }}</span>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="footer-bottom">
      <div class="public-container">
        © 2024 {{ thongTinTruong.tenNgan }}. Tất cả quyền được bảo lưu.
      </div>
    </div>
  </footer>
</template>

<script setup>
import { computed } from 'vue'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const {
  thongTinTruong: thongTinTruongRef,
  mangXaHoi: mangXaHoiRef
} = useNoiDungPublic()

const thongTinTruong = computed(() => thongTinTruongRef.value)
const mangXaHoi = computed(() => mangXaHoiRef.value)
</script>

<style scoped>
.public-footer {
  background: #063b7a;
  color: #ffffff;
}

.footer-main {
  padding: 44px 0 34px;
}

.footer-grid {
  display: grid;
  grid-template-columns: 1.6fr 0.85fr 1fr 1.35fr;
  gap: 42px;
}

.footer-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 18px;
}

.footer-logo {
  width: 54px;
  height: 54px;
  display: grid;
  place-items: center;
  border: 2px solid #ffffff;
  border-radius: 999px;
  color: #ffffff;
  font-weight: 900;
}

.footer-brand h2 {
  margin: 0 0 3px;
  color: #ffffff;
  font-size: 13px;
  line-height: 1;
  font-weight: 800;
}

.footer-brand h3 {
  margin: 0;
  color: #ffffff;
  font-size: 22px;
  line-height: 1;
  font-weight: 900;
}

.footer-about p {
  max-width: 360px;
  margin: 0 0 20px;
  color: #dbeafe;
  font-size: 14px;
  line-height: 1.75;
}

.footer-social {
  display: flex;
  gap: 10px;
}

.footer-social a {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.12);
  color: #ffffff;
  text-decoration: none;
  transition: 0.2s ease;
}

.footer-social a:hover {
  background: #075fc8;
  transform: translateY(-2px);
}

.footer-social span {
  font-size: 10px;
  font-weight: 900;
}

.footer-column h4,
.footer-contact h4 {
  margin: 0 0 16px;
  color: #ffffff;
  font-size: 18px;
  font-weight: 900;
}

.footer-column {
  display: flex;
  flex-direction: column;
}

.footer-column a {
  width: fit-content;
  margin-bottom: 9px;
  color: #dbeafe;
  font-size: 14px;
  line-height: 1.35;
  text-decoration: none;
  transition: 0.2s ease;
}

.footer-column a:hover {
  color: #ffffff;
  transform: translateX(4px);
}

.footer-contact ul {
  display: grid;
  gap: 13px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.footer-contact li {
  display: grid;
  grid-template-columns: 18px 1fr;
  gap: 10px;
  color: #dbeafe;
  font-size: 13px;
  line-height: 1.55;
}

.footer-contact i {
  margin-top: 4px;
  color: #93c5fd;
}

.footer-bottom {
  padding: 16px 0;
  border-top: 1px solid rgba(255, 255, 255, 0.14);
  background: #052f63;
  color: #dbeafe;
  font-size: 13px;
  text-align: center;
}

@media (max-width: 1180px) {
  .footer-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .footer-main {
    padding: 36px 0 28px;
  }

  .footer-grid {
    grid-template-columns: 1fr;
    gap: 28px;
  }
}
</style>