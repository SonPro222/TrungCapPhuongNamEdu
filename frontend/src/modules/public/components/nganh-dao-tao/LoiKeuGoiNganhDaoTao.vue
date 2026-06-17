<template>
  <section class="nganh-cta-section" id="tu-van">
    <div class="public-container">
      <div class="nganh-cta-box">
        <div class="nganh-cta-icon">
          <i class="fa-solid fa-graduation-cap"></i>
        </div>

        <div>
          <h2>Chọn ngành phù hợp - Bắt đầu tương lai ngay hôm nay</h2>
          <p>
            Đăng ký tư vấn miễn phí để được hỗ trợ chọn ngành phù hợp với định hướng nghề nghiệp.
          </p>
        </div>

        <div class="nganh-cta-actions">
          <RouterLink :to="{ name: 'Public.TuyenSinh' }">
            Đăng ký xét tuyển
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
.nganh-cta-section {
  padding: 32px 0 72px;
  background: #ffffff;
}

.nganh-cta-box {
  max-width: 1080px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 58px 1fr auto;
  align-items: center;
  gap: 22px;
  padding: 30px 40px;
  border-radius: 18px;
  background: #063b7a;
  color: #ffffff;
  box-shadow: 0 16px 34px rgba(6, 59, 122, 0.22);
}

.nganh-cta-icon {
  width: 54px;
  height: 54px;
  display: grid;
  place-items: center;
  border-radius: 13px;
  background: rgba(255, 255, 255, 0.18);
  font-size: 26px;
}

.nganh-cta-box h2 {
  margin: 0 0 7px;
  font-size: 21px;
  font-weight: 900;
}

.nganh-cta-box p {
  margin: 0;
  color: #dbeafe;
  font-size: 12px;
}

.nganh-cta-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
}

.nganh-cta-actions a {
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

.nganh-cta-actions a:first-child {
  background: #ffffff;
  color: #063b7a;
}

.nganh-cta-actions a:last-child {
  background: #075fc8;
  color: #ffffff;
  border: 1px solid #1d7df2;
}

@media (max-width: 1180px) {
  .nganh-cta-box {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .nganh-cta-actions {
    flex-direction: column;
  }

  .nganh-cta-actions a {
    justify-content: center;
  }
}
</style>