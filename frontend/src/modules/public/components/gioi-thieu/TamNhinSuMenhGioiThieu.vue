<template>
  <section class="about-section-white">
    <div class="public-container">
      <div class="about-section-title">
        <span></span>
        <h2>Tầm nhìn – Sứ mệnh – Giá trị cốt lõi</h2>
        <span></span>
      </div>

      <div class="vision-grid">
        <article class="vision-card">
          <div class="vision-icon">
            <i class="fa-solid fa-eye"></i>
          </div>
          <div>
            <h3>Tầm nhìn</h3>
            <p>
              Trở thành cơ sở giáo dục nghề nghiệp uy tín hàng đầu, tiên phong trong đổi mới đào tạo,
              góp phần nâng cao chất lượng nguồn nhân lực và phát triển cộng đồng.
            </p>
          </div>
        </article>

        <article class="vision-card">
          <div class="vision-icon">
            <i class="fa-solid fa-bullseye"></i>
          </div>
          <div>
            <h3>Sứ mệnh</h3>
            <p>
              Đào tạo nguồn nhân lực có kỹ năng nghề vững vàng, đạo đức tốt, tác phong chuyên nghiệp,
              đáp ứng nhu cầu của xã hội và doanh nghiệp trong thời kỳ công nghiệp hóa.
            </p>
          </div>
        </article>

        <article class="vision-card">
          <div class="vision-icon">
            <i class="fa-regular fa-gem"></i>
          </div>
          <div>
            <h3>Giá trị cốt lõi</h3>
            <ul>
              <li>Tôn trọng – Chính trực – Trách nhiệm</li>
              <li>Hợp tác – Chia sẻ – Phát triển</li>
              <li>Chất lượng – Hiệu quả – Bền vững</li>
            </ul>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<style scoped>
.about-section-white {
  background: #ffffff;
  padding: 64px 0;
}

.about-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 22px;
  margin-bottom: 46px;
  text-align: center;
}

.about-section-title span {
  width: 110px;
  height: 1px;
  background: #cbd5e1;
}

.about-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 30px;
  font-weight: 900;
}

.vision-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 26px;
}

.vision-card {
  display: flex;
  gap: 18px;
  padding: 26px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.06);
}

.vision-icon {
  flex: 0 0 58px;
  width: 58px;
  height: 58px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #edf6ff;
  color: #075fc8;
  font-size: 24px;
}

.vision-card h3 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 20px;
  font-weight: 900;
}

.vision-card p,
.vision-card li {
  color: #607894;
  font-size: 13px;
  line-height: 1.65;
}

.vision-card ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

.vision-card li {
  position: relative;
  padding-left: 16px;
  margin-bottom: 7px;
}

.vision-card li::before {
  content: '•';
  position: absolute;
  left: 0;
  color: #075fc8;
  font-weight: 900;
}

@media (max-width: 1180px) {
  .vision-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .about-section-title {
    gap: 12px;
  }

  .about-section-title span {
    width: 38px;
  }

  .about-section-title h2 {
    font-size: 22px;
  }

  .vision-grid {
    grid-template-columns: 1fr;
  }

  .vision-card {
    display: block;
  }

  .vision-icon {
    margin-bottom: 14px;
  }
}
</style>