<template>
  <section class="about-section-white about-border-top">
    <div class="public-container">
      <div class="about-section-title">
        <span></span>
        <h2>Lịch sử hình thành và phát triển</h2>
        <span></span>
      </div>

      <div class="history-wrap">
        <div class="history-line"></div>

        <div class="history-grid">
          <article v-for="(item, index) in history" :key="item.year" class="history-card">
            <div class="history-number">0{{ index + 1 }}</div>
            <h3>{{ item.year }}</h3>
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const history = [
  {
    year: '2004',
    title: 'Thành lập',
    desc: 'Thành lập cơ sở giáo dục Phương Nam, đặt nền móng cho hành trình giáo dục nghề nghiệp.'
  },
  {
    year: '2010',
    title: 'Mở rộng quy mô',
    desc: 'Mở rộng cơ sở vật chất, đa dạng hóa ngành đào tạo, tăng cường hợp tác doanh nghiệp.'
  },
  {
    year: '2016',
    title: 'Nâng tầm chất lượng',
    desc: 'Đổi mới chương trình đào tạo, chuẩn hóa theo nhu cầu thị trường lao động.'
  },
  {
    year: '2024',
    title: 'Phát triển bền vững',
    desc: 'Tiếp tục đổi mới, ứng dụng công nghệ, hướng tới môi trường giáo dục thông minh.'
  }
]
</script>

<style scoped>
.about-section-white {
  background: #ffffff;
  padding: 64px 0;
}

.about-border-top {
  border-top: 1px solid #f1f5f9;
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

.history-wrap {
  position: relative;
  max-width: 1100px;
  margin: 0 auto;
}

.history-line {
  position: absolute;
  top: 20px;
  left: 11%;
  right: 11%;
  border-top: 2px dashed #cbd5e1;
}

.history-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 34px;
  position: relative;
  z-index: 2;
}

.history-card {
  text-align: center;
}

.history-number {
  width: 46px;
  height: 46px;
  display: grid;
  place-items: center;
  margin: 0 auto 18px;
  border: 4px solid #ffffff;
  border-radius: 999px;
  background: #075fc8;
  color: white;
  font-size: 14px;
  font-weight: 900;
  box-shadow: 0 8px 18px rgba(7, 95, 200, 0.2);
}

.history-card h3 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 28px;
  font-weight: 900;
}

.history-card h4 {
  margin: 0 0 8px;
  color: #111827;
  font-size: 15px;
  font-weight: 900;
}

.history-card p {
  margin: 0;
  color: #607894;
  font-size: 13px;
  line-height: 1.65;
}

@media (max-width: 1180px) {
  .history-line {
    display: none;
  }

  .history-grid {
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

  .history-grid {
    grid-template-columns: 1fr;
  }
}
</style>