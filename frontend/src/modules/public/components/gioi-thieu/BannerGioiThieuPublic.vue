<template>
  <section class="gioi-thieu-hero">
    <div class="gioi-thieu-hero-bg">
      <img
          src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1920&q=90"
          alt="Trường Trung cấp Phương Nam"
      />
      <div class="gioi-thieu-hero-overlay"></div>
    </div>

    <div class="gioi-thieu-curve">
      <svg viewBox="0 0 1200 120" preserveAspectRatio="none">
        <path
            d="M0,0V46.29c47.79,22.2,103.59,32.17,158,28,70.36-5.37,136.33-33.31,206.8-37.5C438.64,32.43,512.34,53.67,583,72.05c69.27,18,138.3,24.88,209.4,13.08,36.15-6,69.85-17.84,104.45-29.34C989.49,25,1113-14.29,1200,52.47V120H0Z"
            fill="#ffffff"
        />
      </svg>
    </div>

    <div class="public-container gioi-thieu-hero-content">
      <div class="breadcrumb">
        <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
        <span>/</span>
        <strong>Giới thiệu</strong>
      </div>

      <h1>
        Giới thiệu về<br />
        Trường Trung cấp Phương Nam
      </h1>

      <h3>Tri thức vững vàng – Tương lai rộng mở</h3>

      <p>
        Với phương châm đào tạo gắn liền thực tiễn, chúng tôi không ngừng nâng cao chất lượng giáo dục,
        phát triển kỹ năng nghề nghiệp, đồng hành cùng thế hệ trẻ kiến tạo tương lai bền vững.
      </p>

      <div class="hero-actions-about">
        <RouterLink :to="{ name: 'Public.NganhDaoTao' }" class="about-primary-button">
          Xem ngành đào tạo
          <i class="fa-solid fa-arrow-right"></i>
        </RouterLink>

        <RouterLink :to="{ name: 'Public.LienHe' }" class="about-light-button">
          Liên hệ tư vấn
          <i class="fa-solid fa-headset"></i>
        </RouterLink>
      </div>
    </div>
  </section>
</template>

<style scoped>
.gioi-thieu-hero {
  position: relative;
  overflow: hidden;
  min-height: 520px;
  padding: 72px 0 170px;
}

.gioi-thieu-hero-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.gioi-thieu-hero-bg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.gioi-thieu-hero-overlay {
  position: absolute;
  inset: 0;
  background:
      linear-gradient(90deg, rgba(255, 255, 255, 0.97) 0%, rgba(255, 255, 255, 0.86) 44%, rgba(255, 255, 255, 0.28) 72%, rgba(255, 255, 255, 0.04) 100%),
      rgba(30, 64, 175, 0.08);
}

.gioi-thieu-curve {
  position: absolute;
  left: 0;
  right: 0;
  bottom: -1px;
  z-index: 1;
  line-height: 0;
}

.gioi-thieu-curve svg {
  width: 100%;
  height: 130px;
  display: block;
}

.gioi-thieu-hero-content {
  position: relative;
  z-index: 2;
  max-width: 1340px;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 9px;
  margin-bottom: 28px;
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

.gioi-thieu-hero h1 {
  max-width: 760px;
  margin: 0 0 18px;
  color: #063b7a;
  font-size: clamp(42px, 4.4vw, 64px);
  line-height: 1.08;
  font-weight: 900;
  letter-spacing: -1.3px;
}

.gioi-thieu-hero h3 {
  margin: 0 0 20px;
  color: #245f9f;
  font-size: 24px;
  font-weight: 800;
  font-style: italic;
}

.gioi-thieu-hero p {
  max-width: 690px;
  margin: 0 0 30px;
  color: #365979;
  font-size: 15px;
  line-height: 1.75;
}

.hero-actions-about {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
}

.about-primary-button,
.about-light-button {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 0 23px;
  border-radius: 9px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.about-primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 10px 24px rgba(7, 95, 200, 0.22);
}

.about-light-button {
  background: #ffffff;
  color: #075fc8;
  border: 1px solid #cfe1f7;
}

@media (max-width: 760px) {
  .gioi-thieu-hero {
    padding: 46px 0 120px;
    min-height: auto;
  }

  .gioi-thieu-hero h1 {
    font-size: 38px;
  }

  .gioi-thieu-hero h3 {
    font-size: 18px;
  }

  .gioi-thieu-curve svg {
    height: 80px;
  }
}
</style>