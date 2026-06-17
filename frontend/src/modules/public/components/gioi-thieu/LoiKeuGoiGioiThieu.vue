<template>
  <section class="about-cta-section">
    <div class="public-container">
      <div class="about-cta-box">
        <div class="about-cta-icon">
          <i class="fa-solid fa-graduation-cap"></i>
        </div>

        <div>
          <h2>Sẵn sàng trở thành học viên của Phương Nam?</h2>
          <p>Đăng ký xét tuyển ngay hôm nay để mở khóa cơ hội nghề nghiệp vững chắc!</p>
        </div>

        <div class="about-cta-actions">
          <RouterLink :to="{ name: 'Public.TuyenSinh' }">Đăng ký xét tuyển ngay</RouterLink>
          <RouterLink :to="{ name: 'Public.LienHe' }">
            Liên hệ tư vấn <i class="fa-solid fa-headset"></i>
          </RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.about-cta-section {
  background: #ffffff;
  padding: 64px 0;
}

.about-cta-box {
  display: grid;
  grid-template-columns: 68px 1fr auto;
  align-items: center;
  gap: 24px;
  padding: 30px 40px;
  border-radius: 18px;
  background: linear-gradient(135deg, #075fc8, #063b7a);
  color: #ffffff;
  box-shadow: 0 16px 34px rgba(7, 95, 200, 0.22);
}

.about-cta-icon {
  width: 62px;
  height: 62px;
  display: grid;
  place-items: center;
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.1);
  font-size: 30px;
}

.about-cta-box h2 {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 900;
}

.about-cta-box p {
  margin: 0;
  color: #dbeafe;
  font-size: 13px;
}

.about-cta-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.about-cta-actions a {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border-radius: 9px;
  padding: 0 20px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.about-cta-actions a:first-child {
  background: #ffffff;
  color: #063b7a;
}

.about-cta-actions a:last-child {
  background: #075fc8;
  color: #ffffff;
  border: 1px solid #1d7df2;
}

@media (max-width: 1180px) {
  .about-cta-box {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .about-cta-actions {
    flex-direction: column;
  }

  .about-cta-actions a {
    justify-content: center;
  }
}
</style>