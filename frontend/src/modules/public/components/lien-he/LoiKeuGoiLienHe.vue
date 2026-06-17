<template>
  <section class="public-container contact-cta-section">
    <div class="contact-cta-box">
      <div class="contact-cta-left">
        <div>
          <i class="fa-solid fa-headset"></i>
        </div>

        <div>
          <h3>Bạn cần tư vấn thêm?</h3>
          <p>
            Đội ngũ tư vấn viên của chúng tôi luôn sẵn sàng hỗ trợ bạn 24/7.
            Đừng ngần ngại liên hệ ngay!
          </p>
        </div>
      </div>

      <div class="contact-cta-actions">
        <a href="#form-lien-he">Đăng ký tư vấn →</a>
        <a href="tel:1900633088">
          <i class="fa-solid fa-phone"></i>
          Gọi hotline: 1900 633 088
        </a>
      </div>
    </div>
  </section>
</template>

<style scoped>
.contact-cta-section {
  margin-bottom: 46px;
}

.contact-cta-box {
  position: relative;
  overflow: hidden;
  display: grid;
  grid-template-columns: 1fr auto;
  align-items: center;
  gap: 24px;
  padding: 34px 40px;
  border-radius: 22px;
  background: #063b7a;
  color: #ffffff;
  box-shadow: 0 16px 34px rgba(6, 59, 122, 0.22);
}

.contact-cta-box::after {
  content: '🎓';
  position: absolute;
  top: -48px;
  right: -30px;
  opacity: 0.08;
  font-size: 160px;
}

.contact-cta-left {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 22px;
}

.contact-cta-left > div:first-child {
  width: 68px;
  height: 68px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 32px;
}

.contact-cta-left h3 {
  margin: 0 0 8px;
  font-size: 26px;
  font-weight: 900;
}

.contact-cta-left p {
  margin: 0;
  color: #dbeafe;
  font-size: 14px;
}

.contact-cta-actions {
  position: relative;
  z-index: 2;
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
}

.contact-cta-actions a {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
  border-radius: 10px;
  padding: 0 22px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.contact-cta-actions a:first-child {
  background: #ffffff;
  color: #063b7a;
}

.contact-cta-actions a:last-child {
  border: 1px solid rgba(255, 255, 255, 0.45);
  color: #ffffff;
}

@media (max-width: 1180px) {
  .contact-cta-box {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .contact-cta-left {
    display: block;
  }

  .contact-cta-left > div:first-child {
    margin-bottom: 16px;
  }

  .contact-cta-actions {
    flex-direction: column;
  }
}
</style>