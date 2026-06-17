<template>
  <section class="public-section white-section">
    <div class="public-container">
      <div class="cta-box">
        <div class="cta-icon">
          <i class="fa-solid fa-graduation-cap"></i>
        </div>

        <div>
          <h2>Sẵn sàng cho tương lai của bạn?</h2>
          <p>Đăng ký xét tuyển ngay hôm nay để trở thành học viên của Trường Trung cấp Phương Nam!</p>
        </div>

        <div class="cta-actions">
          <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="white-button">
            Đăng ký xét tuyển ngay →
          </RouterLink>
          <a href="tel:1900633088" class="outline-white-button">Hotline: 1900 633 088</a>
        </div>
      </div>
    </div>
  </section>
</template>