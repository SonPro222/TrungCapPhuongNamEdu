<template>
  <section class="nganh-section">
    <div class="public-container">
      <div class="nganh-section-title">
        <span></span>
        <h2>Lộ trình đào tạo</h2>
        <span></span>
      </div>

      <div class="roadmap-wrap">
        <div class="roadmap-line">
          <i class="fa-solid fa-chevron-right roadmap-arrow arrow-1"></i>
          <i class="fa-solid fa-chevron-right roadmap-arrow arrow-2"></i>
          <i class="fa-solid fa-chevron-right roadmap-arrow arrow-3"></i>
        </div>

        <div class="roadmap-grid">
          <article
              v-for="(item, index) in loTrinhDaoTao"
              :key="item.title"
              class="roadmap-card"
          >
            <div class="roadmap-number">0{{ index + 1 }}</div>
            <div class="roadmap-icon">
              <i :class="item.icon"></i>
            </div>
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const loTrinhDaoTao = [
  {
    icon: 'fa-solid fa-graduation-cap',
    title: 'Học kiến thức nền tảng',
    desc: 'Trang bị kiến thức cơ bản, nền tảng ngành học vững chắc.'
  },
  {
    icon: 'fa-solid fa-screwdriver-wrench',
    title: 'Thực hành phòng lab/xưởng',
    desc: 'Thực hành tại phòng lab, xưởng thực hành hiện đại của nhà trường.'
  },
  {
    icon: 'fa-regular fa-building',
    title: 'Thực tập doanh nghiệp',
    desc: 'Thực tập tại doanh nghiệp, áp dụng kiến thức vào thực tế công việc.'
  },
  {
    icon: 'fa-solid fa-user-graduate',
    title: 'Tốt nghiệp & việc làm',
    desc: 'Tốt nghiệp, được hỗ trợ giới thiệu việc làm phù hợp với chuyên ngành.'
  }
]
</script>

<style scoped>
.nganh-section {
  padding: 64px 0;
  background: #ffffff;
}

.nganh-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 18px;
  margin-bottom: 42px;
  text-align: center;
}

.nganh-section-title span {
  width: 92px;
  height: 1px;
  background: #cbd5e1;
}

.nganh-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 27px;
  font-weight: 900;
}

.roadmap-wrap {
  position: relative;
  max-width: 1180px;
  margin: 0 auto;
}

.roadmap-line {
  position: absolute;
  top: 50%;
  left: 12%;
  right: 12%;
  z-index: 0;
  border-top: 2px dashed #d1d5db;
  transform: translateY(-50%);
}

.roadmap-arrow {
  position: absolute;
  top: -7px;
  color: #cbd5e1;
  font-size: 12px;
}

.arrow-1 {
  left: 32%;
}

.arrow-2 {
  left: 65%;
}

.arrow-3 {
  right: 0;
}

.roadmap-grid {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 32px;
}

.roadmap-card {
  position: relative;
  min-height: 230px;
  padding: 34px 22px 26px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  text-align: center;
  box-shadow: 0 10px 26px rgba(7, 63, 131, 0.07);
  transition: 0.25s;
}

.roadmap-card:hover {
  transform: translateY(-4px);
}

.roadmap-number {
  position: absolute;
  top: -14px;
  left: -14px;
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #063b7a;
  color: #ffffff;
  font-size: 12px;
  font-weight: 900;
  box-shadow: 0 8px 18px rgba(6, 59, 122, 0.22);
}

.roadmap-icon {
  margin-bottom: 18px;
  color: #063b7a;
  font-size: 40px;
}

.roadmap-card h4 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.roadmap-card p {
  margin: 0;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

@media (max-width: 1180px) {
  .roadmap-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .roadmap-line {
    display: none;
  }
}

@media (max-width: 760px) {
  .nganh-section-title {
    gap: 12px;
  }

  .nganh-section-title span {
    width: 36px;
  }

  .nganh-section-title h2 {
    font-size: 22px;
  }

  .roadmap-grid {
    grid-template-columns: 1fr;
  }
}
</style>