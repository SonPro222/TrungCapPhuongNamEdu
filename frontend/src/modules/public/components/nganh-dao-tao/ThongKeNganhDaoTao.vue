<template>
  <section class="nganh-stats-section">
    <div class="public-container nganh-stats-grid">
      <article v-for="item in thongKeNganh" :key="item.label">
        <div>
          <i :class="item.icon"></i>
        </div>
        <div>
          <h3>{{ item.number }}</h3>
          <p>{{ item.label }}</p>
        </div>
      </article>
    </div>
  </section>
</template>

<script setup>
const thongKeNganh = [
  {
    icon: 'fa-solid fa-book-open',
    number: '30+',
    label: 'ngành đào tạo'
  },
  {
    icon: 'fa-solid fa-flask',
    number: '70%',
    label: 'thời lượng thực hành'
  },
  {
    icon: 'fa-solid fa-handshake',
    number: '200+',
    label: 'doanh nghiệp đối tác'
  },
  {
    icon: 'fa-solid fa-briefcase',
    number: '95%',
    label: 'hỗ trợ việc làm'
  }
]
</script>

<style scoped>
.nganh-stats-section {
  padding: 36px 0;
  background: #063b7a;
}

.nganh-stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
}

.nganh-stats-grid article {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 18px;
  padding: 8px 18px;
  border-right: 1px solid rgba(255, 255, 255, 0.18);
}

.nganh-stats-grid article:last-child {
  border-right: none;
}

.nganh-stats-grid i {
  color: #dbeafe;
  font-size: 34px;
}

.nganh-stats-grid h3 {
  margin: 0 0 5px;
  color: #ffffff;
  font-size: 30px;
  font-weight: 900;
  line-height: 1;
}

.nganh-stats-grid p {
  margin: 0;
  color: #bfdbfe;
  font-size: 12px;
  line-height: 1.35;
}

@media (max-width: 1180px) {
  .nganh-stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .nganh-stats-grid {
    grid-template-columns: 1fr;
  }

  .nganh-stats-grid article {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.18);
  }

  .nganh-stats-grid article:last-child {
    border-bottom: none;
  }
}
</style>