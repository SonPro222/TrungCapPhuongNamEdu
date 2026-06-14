<template>
  <section class="nganh-hero">
    <div class="nganh-hero-image">
      <img
          src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1400&q=90"
          alt="Trường Trung cấp Phương Nam"
      />
      <div class="nganh-hero-image-overlay"></div>
    </div>

    <div class="nganh-blue-wave">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#063b7a"
        />
      </svg>
    </div>

    <div class="nganh-white-wave">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#ffffff"
        />
      </svg>
    </div>

    <div class="public-container nganh-hero-content">
      <div class="breadcrumb">
        <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
        <span>/</span>
        <strong>Ngành đào tạo</strong>
      </div>

      <h1>Ngành đào tạo</h1>

      <h3>Chương trình thực tiễn – Vững kiến thức – Sẵn sàng nghề nghiệp</h3>

      <p>
        Trường Trung cấp Phương Nam đào tạo đa dạng các ngành nghề trọng điểm,
        kết hợp lý thuyết và thực hành, gắn kết doanh nghiệp, giúp sinh viên vững tay nghề,
        tự tin bước vào thị trường lao động.
      </p>

      <div class="nganh-hero-actions">
        <RouterLink :to="{ name: 'Public.LienHe' }" class="nganh-primary-button">
          Đăng ký tư vấn
          <i class="fa-solid fa-arrow-right"></i>
        </RouterLink>

        <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="nganh-light-button">
          Xem thông tin tuyển sinh
          <i class="fa-regular fa-file-lines"></i>
        </RouterLink>
      </div>
    </div>
  </section>
</template>

<style scoped>
.nganh-hero {
  position: relative;
  overflow: hidden;
  min-height: 500px;
  padding: 70px 0 170px;
  background: #eff6ff;
}

.nganh-hero-image {
  position: absolute;
  top: 0;
  right: 0;
  width: 62%;
  height: 100%;
  z-index: 0;
}

.nganh-hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.nganh-hero-image-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(90deg, #eff6ff 0%, rgba(239, 246, 255, 0.8) 18%, rgba(239, 246, 255, 0.16) 60%, rgba(239, 246, 255, 0.02) 100%);
}

.nganh-blue-wave {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 20px;
  z-index: 1;
  line-height: 0;
}

.nganh-blue-wave svg {
  width: 100%;
  height: 120px;
  display: block;
}

.nganh-white-wave {
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  z-index: 2;
  line-height: 0;
}

.nganh-white-wave svg {
  width: 100%;
  height: 46px;
  display: block;
}

.nganh-hero-content {
  position: relative;
  z-index: 3;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 9px;
  margin-bottom: 20px;
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

.nganh-hero h1 {
  margin: 0 0 14px;
  color: #063b7a;
  font-size: clamp(42px, 4.4vw, 62px);
  line-height: 1.05;
  font-weight: 900;
  letter-spacing: -1.2px;
}

.nganh-hero h3 {
  max-width: 650px;
  margin: 0 0 18px;
  color: #245f9f;
  font-size: 22px;
  font-weight: 800;
  font-style: italic;
}

.nganh-hero p {
  max-width: 650px;
  margin: 0 0 28px;
  color: #365979;
  font-size: 14px;
  line-height: 1.75;
}

.nganh-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 13px;
}

.nganh-primary-button,
.nganh-light-button {
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

.nganh-primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 10px 24px rgba(7, 95, 200, 0.24);
}

.nganh-light-button {
  background: #ffffff;
  color: #374151;
  border: 1px solid #d1d5db;
}

@media (max-width: 1180px) {
  .nganh-hero-image {
    width: 100%;
    opacity: 0.28;
  }
}

@media (max-width: 760px) {
  .nganh-hero {
    min-height: auto;
    padding: 48px 0 135px;
  }

  .nganh-hero h1 {
    font-size: 40px;
  }

  .nganh-hero h3 {
    font-size: 18px;
  }

  .nganh-blue-wave svg {
    height: 78px;
  }

  .nganh-white-wave svg {
    height: 30px;
  }
}
</style>