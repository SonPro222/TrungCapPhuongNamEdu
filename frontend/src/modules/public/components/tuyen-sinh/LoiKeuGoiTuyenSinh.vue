<template>
  <section class="tuyen-sinh-cta-section" id="dang-ky">
    <div class="public-container">
      <div class="tuyen-sinh-cta-box">
        <div class="tuyen-sinh-cta-icon">
          <i class="fa-solid fa-graduation-cap"></i>
        </div>

        <div>
          <h2>Sẵn sàng đăng ký xét tuyển?</h2>
          <p>Đừng bỏ lỡ cơ hội trở thành học viên của Trường Trung cấp Phương Nam.</p>
        </div>

        <div class="tuyen-sinh-cta-actions">
          <RouterLink :to="{ name: 'Public.LienHe' }">
            Đăng ký ngay
            <i class="fa-solid fa-arrow-right"></i>
          </RouterLink>

          <RouterLink :to="{ name: 'Public.LienHe' }">
            Liên hệ tư vấn
            <i class="fa-solid fa-headset"></i>
          </RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.tuyen-sinh-cta-section {
  padding: 34px 0 72px;
  background: #ffffff;
}

.tuyen-sinh-cta-box {
  display: grid;
  grid-template-columns: 64px 1fr auto;
  align-items: center;
  gap: 24px;
  padding: 30px 40px;
  border-radius: 18px;
  background: #063b7a;
  color: #ffffff;
  box-shadow: 0 16px 34px rgba(6, 59, 122, 0.22);
}

.tuyen-sinh-cta-icon {
  width: 58px;
  height: 58px;
  display: grid;
  place-items: center;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.16);
  font-size: 30px;
}

.tuyen-sinh-cta-box h2 {
  margin: 0 0 7px;
  font-size: 22px;
  font-weight: 900;
}

.tuyen-sinh-cta-box p {
  margin: 0;
  color: #dbeafe;
  font-size: 13px;
}

.tuyen-sinh-cta-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.tuyen-sinh-cta-actions a {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border-radius: 9px;
  padding: 0 18px;
  font-size: 13px;
  font-weight: 900;
  text-decoration: none;
}

.tuyen-sinh-cta-actions a:first-child {
  background: #ffffff;
  color: #063b7a;
}

.tuyen-sinh-cta-actions a:last-child {
  background: transparent;
  color: #ffffff;
  border: 1px solid rgba(255, 255, 255, 0.45);
}

@media (max-width: 1180px) {
  .tuyen-sinh-cta-box {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .tuyen-sinh-cta-actions {
    flex-direction: column;
  }

  .tuyen-sinh-cta-actions a {
    justify-content: center;
  }
}
</style>