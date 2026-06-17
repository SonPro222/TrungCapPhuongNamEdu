<template>
  <section class="lien-he-hero">
    <div class="lien-he-hero-bg">
      <img
          src="https://images.unsplash.com/photo-1562774053-701939374585?auto=format&fit=crop&w=1920&q=90"
          alt="Trường Trung cấp Phương Nam"
      />
      <div class="lien-he-hero-overlay"></div>
    </div>

    <div class="public-container lien-he-hero-content">
      <div class="breadcrumb">
        <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
        <span>/</span>
        <strong>Liên hệ</strong>
      </div>

      <h1>Liên hệ</h1>
      <h2>Kết nối cùng Trường Trung cấp Phương Nam</h2>

      <p>
        Chúng tôi luôn sẵn sàng lắng nghe và hỗ trợ bạn. Mọi câu hỏi, góp ý hoặc nhu cầu tư vấn,
        vui lòng liên hệ với chúng tôi qua các kênh bên dưới.
      </p>

      <div class="lien-he-hero-actions">
        <a href="tel:1900633088" class="lien-he-primary-button">
          <i class="fa-solid fa-phone"></i>
          Gọi ngay
        </a>

        <a href="#form-lien-he" class="lien-he-outline-button">
          <i class="fa-regular fa-paper-plane"></i>
          Gửi liên hệ
        </a>
      </div>
    </div>
  </section>
</template>

<style scoped>
.lien-he-hero {
  position: relative;
  overflow: hidden;
  min-height: 470px;
  padding: 78px 0 130px;
  background: #063b7a;
}

.lien-he-hero-bg {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.lien-he-hero-bg img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  opacity: 0.22;
}

.lien-he-hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(90deg, #063b7a 0%, rgba(6, 59, 122, 0.92) 48%, rgba(6, 59, 122, 0.1) 100%);
}

.lien-he-hero-content {
  position: relative;
  z-index: 2;
  color: #ffffff;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 9px;
  margin-bottom: 18px;
  color: #dbeafe;
  font-size: 13px;
  font-weight: 700;
}

.breadcrumb a {
  color: #dbeafe;
  text-decoration: none;
}

.breadcrumb strong {
  color: #ffffff;
}

.lien-he-hero h1 {
  margin: 0 0 16px;
  font-size: clamp(42px, 4.5vw, 62px);
  line-height: 1.05;
  font-weight: 900;
}

.lien-he-hero h2 {
  max-width: 760px;
  margin: 0 0 18px;
  color: #dbeafe;
  font-size: 30px;
  line-height: 1.35;
  font-weight: 800;
}

.lien-he-hero p {
  max-width: 720px;
  margin: 0 0 30px;
  color: #eff6ff;
  font-size: 16px;
  line-height: 1.75;
}

.lien-he-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
}

.lien-he-primary-button,
.lien-he-outline-button {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  padding: 0 26px;
  border-radius: 999px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.lien-he-primary-button {
  background: #ffffff;
  color: #063b7a;
}

.lien-he-outline-button {
  border: 1px solid rgba(255, 255, 255, 0.38);
  color: #ffffff;
}

@media (max-width: 760px) {
  .lien-he-hero {
    min-height: auto;
    padding: 52px 0 110px;
  }

  .lien-he-hero h1 {
    font-size: 40px;
  }

  .lien-he-hero h2 {
    font-size: 22px;
  }
}
</style>