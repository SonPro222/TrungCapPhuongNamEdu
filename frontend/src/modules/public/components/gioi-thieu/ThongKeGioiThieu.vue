<template>
  <section class="about-stats-section">
    <div class="public-container about-stats-grid">
      <article v-for="item in stats" :key="item.label">
        <div>
          <i :class="item.icon"></i>
          <strong>{{ item.number }}</strong>
        </div>
        <p>{{ item.label }}</p>
      </article>
    </div>
  </section>
</template>

<script setup>
const stats = [
  {
    icon: 'fa-solid fa-calendar-check',
    number: '20+',
    label: 'Năm hình thành và phát triển'
  },
  {
    icon: 'fa-solid fa-user-graduate',
    number: '10.000+',
    label: 'Học viên đã và đang học'
  },
  {
    icon: 'fa-solid fa-book-open',
    number: '30+',
    label: 'Ngành đào tạo đa dạng'
  },
  {
    icon: 'fa-solid fa-handshake',
    number: '200+',
    label: 'Doanh nghiệp đối tác uy tín'
  },
  {
    icon: 'fa-solid fa-chart-line',
    number: '95%+',
    label: 'Tỷ lệ học viên có việc làm'
  }
]
</script>

<style scoped>
.about-stats-section {
  background: #063b7a;
  padding: 34px 0;
}

.about-stats-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 0;
}

.about-stats-grid article {
  text-align: center;
  padding: 8px 18px;
  border-right: 1px solid rgba(255, 255, 255, 0.18);
}

.about-stats-grid article:last-child {
  border-right: none;
}

.about-stats-grid div {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 14px;
  margin-bottom: 8px;
}

.about-stats-grid i {
  color: #93c5fd;
  font-size: 26px;
}

.about-stats-grid strong {
  color: #ffffff;
  font-size: 30px;
  font-weight: 900;
}

.about-stats-grid p {
  margin: 0;
  color: #dbeafe;
  font-size: 12px;
  text-transform: uppercase;
  line-height: 1.5;
}

@media (max-width: 1180px) {
  .about-stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .about-stats-grid article {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.18);
  }
}

@media (max-width: 760px) {
  .about-stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>