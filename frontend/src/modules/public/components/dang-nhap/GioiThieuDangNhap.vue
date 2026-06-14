<template>
  <section class="dang-nhap-left">
    <div class="dang-nhap-left-overlay"></div>

    <div class="dang-nhap-school">
      <div class="dang-nhap-logo">
        <div class="logo-circle">
          <span>PN</span>
        </div>
      </div>

      <div class="school-title">
        <h2>TRƯỜNG</h2>
        <h1>
          TRUNG CẤP<br />
          PHƯƠNG NAM
        </h1>
        <h3>Tri thức vững vàng – Tương lai rộng mở</h3>
      </div>

      <p>
        Trường Trung cấp Phương Nam là cơ sở đào tạo uy tín, đào tạo nguồn nhân lực
        chất lượng, đáp ứng nhu cầu xã hội và đồng hành cùng học viên trên con đường
        lập thân – lập nghiệp.
      </p>

      <div class="login-feature-grid">
        <article>
          <i class="fa-solid fa-graduation-cap"></i>
          <span>ĐÀO TẠO<br />CHẤT LƯỢNG</span>
        </article>

        <article>
          <i class="fa-solid fa-book-open"></i>
          <span>CHƯƠNG TRÌNH<br />THỰC TIỄN</span>
        </article>

        <article>
          <i class="fa-solid fa-handshake"></i>
          <span>KẾT NỐI<br />DOANH NGHIỆP</span>
        </article>

        <article>
          <i class="fa-solid fa-chart-line"></i>
          <span>CƠ HỘI<br />VIỆC LÀM RỘNG MỞ</span>
        </article>
      </div>
    </div>

    <div class="school-building">
      <img
          src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1600&q=90"
          alt="Trường Trung cấp Phương Nam"
      />

      <div class="school-label">
        TRƯỜNG TRUNG CẤP PHƯƠNG NAM
      </div>
    </div>

    <div class="blue-wave"></div>

    <div class="bottom-benefits">
      <article>
        <i class="fa-solid fa-users"></i>
        <span>Đội ngũ giảng viên<br />Giàu kinh nghiệm</span>
      </article>

      <article>
        <i class="fa-solid fa-award"></i>
        <span>Chương trình đào tạo<br />Chuẩn thực tiễn</span>
      </article>

      <article>
        <i class="fa-solid fa-building-columns"></i>
        <span>Học đi đôi với hành<br />Vững nghề - chắc nghiệp</span>
      </article>

      <article>
        <i class="fa-solid fa-medal"></i>
        <span>Uy tín - Chất lượng<br />Tạo dựng tương lai</span>
      </article>
    </div>
  </section>
</template>

<style scoped>
.dang-nhap-left {
  position: relative;
  overflow: hidden;
  min-height: calc(100vh - 44px);
  border-radius: 28px 0 0 28px;
  background:
      linear-gradient(180deg, rgba(231, 246, 255, 0.95), rgba(204, 232, 255, 0.82)),
      url('https://images.unsplash.com/photo-1562774053-701939374585?auto=format&fit=crop&w=1600&q=90');
  background-size: cover;
  background-position: center;
}

.dang-nhap-left-overlay {
  position: absolute;
  inset: 0;
  background:
      radial-gradient(circle at 25% 18%, rgba(255, 255, 255, 0.95), rgba(255, 255, 255, 0.24) 42%, transparent 72%),
      linear-gradient(180deg, rgba(239, 249, 255, 0.65), rgba(255, 255, 255, 0.04));
  z-index: 1;
}

.dang-nhap-school {
  position: relative;
  z-index: 3;
  display: grid;
  grid-template-columns: 145px 1fr;
  column-gap: 34px;
  padding: 74px 76px 0;
}

.dang-nhap-logo {
  padding-top: 4px;
}

.logo-circle {
  width: 128px;
  height: 128px;
  border: 4px solid #075fc8;
  border-radius: 999px;
  display: grid;
  place-items: center;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: 0 12px 30px rgba(7, 63, 131, 0.18);
}

.logo-circle span {
  width: 92px;
  height: 92px;
  border-radius: 999px;
  display: grid;
  place-items: center;
  border: 2px dashed #0b6fe8;
  color: #063b7a;
  font-size: 38px;
  font-weight: 900;
}

.school-title h2 {
  margin: 0 0 8px;
  color: #075fc8;
  font-size: 30px;
  line-height: 1;
  font-weight: 900;
  letter-spacing: 1px;
}

.school-title h1 {
  margin: 0;
  color: #063b7a;
  font-size: clamp(42px, 4.2vw, 70px);
  line-height: 1.05;
  font-weight: 900;
  letter-spacing: -1.2px;
}

.school-title h3 {
  margin: 16px 0 0;
  color: #123b68;
  font-size: 25px;
  font-style: italic;
  font-weight: 800;
}

.dang-nhap-school > p {
  grid-column: 2;
  width: min(100%, 570px);
  margin: 28px 0 0;
  color: #334155;
  font-size: 18px;
  line-height: 1.55;
  font-weight: 500;
}

.login-feature-grid {
  grid-column: 2;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  width: min(100%, 700px);
  margin-top: 30px;
}

.login-feature-grid article {
  min-height: 126px;
  display: grid;
  place-items: center;
  align-content: center;
  gap: 12px;
  padding: 18px 10px;
  border: 1px solid rgba(7, 95, 200, 0.12);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.42);
  backdrop-filter: blur(8px);
  box-shadow: 0 10px 26px rgba(7, 63, 131, 0.08);
  text-align: center;
}

.login-feature-grid i {
  color: #075fc8;
  font-size: 36px;
}

.login-feature-grid span {
  color: #0f2f5f;
  font-size: 13px;
  line-height: 1.3;
  font-weight: 900;
}

.school-building {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 132px;
  z-index: 2;
  height: 360px;
}

.school-building img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  object-position: center bottom;
}

.school-label {
  position: absolute;
  left: 50%;
  top: 48%;
  transform: translateX(-50%);
  background: #075fc8;
  color: #ffffff;
  padding: 9px 26px;
  border-radius: 2px;
  font-size: 13px;
  font-weight: 900;
  white-space: nowrap;
}

.blue-wave {
  position: absolute;
  left: -6%;
  right: -6%;
  bottom: 106px;
  z-index: 4;
  height: 140px;
  background: rgba(0, 64, 145, 0.88);
  border-top-left-radius: 55% 90px;
  border-top-right-radius: 40% 80px;
  transform: rotate(-3deg);
}

.bottom-benefits {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  z-index: 5;
  min-height: 165px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  align-items: center;
  gap: 26px;
  padding: 42px 64px 34px;
  background: linear-gradient(135deg, #063b7a, #023071);
  color: #ffffff;
}

.bottom-benefits article {
  display: grid;
  grid-template-columns: 40px 1fr;
  gap: 14px;
  align-items: center;
}

.bottom-benefits i {
  font-size: 32px;
  color: #ffffff;
}

.bottom-benefits span {
  color: #ffffff;
  font-size: 14px;
  line-height: 1.35;
  font-weight: 800;
}

@media (max-width: 1280px) {
  .dang-nhap-left {
    display: none;
  }
}
</style>