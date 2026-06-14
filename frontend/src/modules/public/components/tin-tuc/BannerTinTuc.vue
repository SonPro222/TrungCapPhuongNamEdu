<template>
  <section class="tin-tuc-hero">
    <div class="tin-tuc-hero-image">
      <img
          src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1400&q=90"
          alt="Trường Trung cấp Phương Nam"
      />
      <div class="tin-tuc-hero-overlay"></div>
    </div>

    <div class="tin-tuc-blue-wave">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#063b7a"
        />
      </svg>
    </div>

    <div class="tin-tuc-white-wave">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#ffffff"
        />
      </svg>
    </div>

    <div class="public-container tin-tuc-hero-content">
      <div class="breadcrumb">
        <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
        <span>/</span>
        <strong>Tin tức</strong>
      </div>

      <h1>Tin tức & Sự kiện</h1>

      <p>
        Cập nhật nhanh chóng những tin tức mới nhất về nhà trường, hoạt động đào tạo,
        tuyển sinh, sự kiện và hợp tác doanh nghiệp.
      </p>

      <div class="tin-tuc-hero-actions">
        <a href="#su-kien" class="tin-tuc-primary-button">
          Xem sự kiện sắp diễn ra
          <i class="fa-solid fa-arrow-right"></i>
        </a>

        <a href="#dang-ky-nhan-tin" class="tin-tuc-light-button">
          Đăng ký nhận tin
          <i class="fa-regular fa-bell"></i>
        </a>
      </div>
    </div>
  </section>
</template>

<style scoped>
.tin-tuc-hero {
  position: relative;
  overflow: hidden;
  min-height: 500px;
  padding: 64px 0 170px;
  background: #eff6ff;
}

.tin-tuc-hero-image {
  position: absolute;
  top: 0;
  right: 0;
  width: 62%;
  height: 100%;
  z-index: 0;
}

.tin-tuc-hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tin-tuc-hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(90deg, #eff6ff 0%, rgba(239, 246, 255, 0.84) 22%, rgba(239, 246, 255, 0.18) 66%, rgba(239, 246, 255, 0.02) 100%);
}

.tin-tuc-blue-wave {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 20px;
  z-index: 1;
  line-height: 0;
}

.tin-tuc-blue-wave svg {
  width: 100%;
  height: 120px;
  display: block;
}

.tin-tuc-white-wave {
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  z-index: 2;
  line-height: 0;
}

.tin-tuc-white-wave svg {
  width: 100%;
  height: 46px;
  display: block;
}

.tin-tuc-hero-content {
  position: relative;
  z-index: 3;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 9px;
  margin-bottom: 18px;
  color: #6b7280;
  font-size: 13px;
  font-weight: 700;
}

.breadcrumb a {
  color: #6b7280;
  text-decoration: none;
}

.breadcrumb strong {
  color: #075fc8;
}

.tin-tuc-hero h1 {
  margin: 0 0 18px;
  color: #063b7a;
  font-size: clamp(42px, 4.6vw, 62px);
  line-height: 1.06;
  font-weight: 900;
  letter-spacing: -1.2px;
}

.tin-tuc-hero p {
  max-width: 570px;
  margin: 0 0 28px;
  color: #365979;
  font-size: 15px;
  line-height: 1.75;
}

.tin-tuc-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 13px;
}

.tin-tuc-primary-button,
.tin-tuc-light-button {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  padding: 0 22px;
  border-radius: 9px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.tin-tuc-primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 10px 24px rgba(7, 95, 200, 0.24);
}

.tin-tuc-light-button {
  background: #ffffff;
  color: #374151;
  border: 1px solid #d1d5db;
}

@media (max-width: 1180px) {
  .tin-tuc-hero-image {
    width: 100%;
    opacity: 0.25;
  }
}

@media (max-width: 760px) {
  .tin-tuc-hero {
    min-height: auto;
    padding: 44px 0 135px;
  }

  .tin-tuc-hero h1 {
    font-size: 38px;
  }

  .tin-tuc-blue-wave svg {
    height: 78px;
  }

  .tin-tuc-white-wave svg {
    height: 30px;
  }
}
</style>