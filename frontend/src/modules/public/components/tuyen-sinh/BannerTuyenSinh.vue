<template>
  <section class="tuyen-sinh-hero">
    <div class="tuyen-sinh-hero-image">
      <img
          src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1400&q=90"
          alt="Trường Trung cấp Phương Nam"
      />
      <div class="tuyen-sinh-hero-overlay"></div>
    </div>

    <div class="tuyen-sinh-blue-wave">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#063b7a"
        />
      </svg>
    </div>

    <div class="tuyen-sinh-white-wave">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#ffffff"
        />
      </svg>
    </div>

    <div class="public-container tuyen-sinh-hero-content">
      <div class="tuyen-sinh-badge">
        <span>🌟</span>
        <strong>Xét tuyển nhanh - Đơn giản - Minh bạch</strong>
      </div>

      <div class="breadcrumb">
        <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
        <span>/</span>
        <strong>Tuyển sinh</strong>
      </div>

      <h1>Tuyển sinh</h1>

      <h3>
        Mở cánh cửa tương lai nghề nghiệp<br />
        cùng Trường Trung cấp Phương Nam
      </h3>

      <p>
        Chúng tôi mang đến chương trình đào tạo chất lượng, gắn liền với thực tiễn,
        cơ hội việc làm rộng mở và môi trường học tập hiện đại, thân thiện.
      </p>

      <div class="tuyen-sinh-hero-actions">
        <a href="#dang-ky" class="tuyen-sinh-primary-button">
          Đăng ký xét tuyển
          <i class="fa-solid fa-arrow-right"></i>
        </a>

        <a href="#ho-so" class="tuyen-sinh-light-button">
          Tải hồ sơ tuyển sinh
          <i class="fa-solid fa-download"></i>
        </a>
      </div>
    </div>
  </section>
</template>

<style scoped>
.tuyen-sinh-hero {
  position: relative;
  overflow: hidden;
  min-height: 510px;
  padding: 64px 0 170px;
  background: #eff6ff;
}

.tuyen-sinh-hero-image {
  position: absolute;
  top: 0;
  right: 0;
  width: 62%;
  height: 100%;
  z-index: 0;
}

.tuyen-sinh-hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tuyen-sinh-hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(90deg, #eff6ff 0%, rgba(239, 246, 255, 0.84) 22%, rgba(239, 246, 255, 0.18) 66%, rgba(239, 246, 255, 0.02) 100%);
}

.tuyen-sinh-blue-wave {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 20px;
  z-index: 1;
  line-height: 0;
}

.tuyen-sinh-blue-wave svg {
  width: 100%;
  height: 120px;
  display: block;
}

.tuyen-sinh-white-wave {
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  z-index: 2;
  line-height: 0;
}

.tuyen-sinh-white-wave svg {
  width: 100%;
  height: 46px;
  display: block;
}

.tuyen-sinh-hero-content {
  position: relative;
  z-index: 3;
}

.tuyen-sinh-badge {
  width: fit-content;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  padding: 8px 16px;
  border: 1px solid #fde68a;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 8px 20px rgba(7, 63, 131, 0.08);
}

.tuyen-sinh-badge strong {
  color: #063b7a;
  font-size: 11px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.4px;
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

.tuyen-sinh-hero h1 {
  margin: 0 0 12px;
  color: #063b7a;
  font-size: clamp(46px, 5vw, 68px);
  line-height: 1.02;
  font-weight: 900;
  letter-spacing: -1.4px;
}

.tuyen-sinh-hero h3 {
  max-width: 660px;
  margin: 0 0 18px;
  color: #245f9f;
  font-size: 22px;
  line-height: 1.4;
  font-weight: 800;
  font-style: italic;
}

.tuyen-sinh-hero p {
  max-width: 570px;
  margin: 0 0 28px;
  color: #365979;
  font-size: 14px;
  line-height: 1.75;
}

.tuyen-sinh-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 13px;
}

.tuyen-sinh-primary-button,
.tuyen-sinh-light-button {
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

.tuyen-sinh-primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 10px 24px rgba(7, 95, 200, 0.24);
}

.tuyen-sinh-light-button {
  background: #ffffff;
  color: #374151;
  border: 1px solid #d1d5db;
}

@media (max-width: 1180px) {
  .tuyen-sinh-hero-image {
    width: 100%;
    opacity: 0.25;
  }
}

@media (max-width: 760px) {
  .tuyen-sinh-hero {
    min-height: auto;
    padding: 44px 0 135px;
  }

  .tuyen-sinh-hero h1 {
    font-size: 42px;
  }

  .tuyen-sinh-hero h3 {
    font-size: 18px;
  }

  .tuyen-sinh-blue-wave svg {
    height: 78px;
  }

  .tuyen-sinh-white-wave svg {
    height: 30px;
  }
}
</style>