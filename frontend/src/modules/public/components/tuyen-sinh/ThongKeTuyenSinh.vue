<template>
  <section class="tuyen-sinh-stats-overlap">
    <div class="public-container">
      <div class="tuyen-sinh-stats-box">
        <article v-for="item in thongKeTuyenSinh" :key="item.label">
          <div>
            <i :class="item.icon"></i>
          </div>
          <div>
            <h3>{{ item.number }}</h3>
            <p>{{ item.label }}</p>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
import { thongKeTuyenSinh } from '@/modules/public/data/tuyenSinhData.js'
</script>

<style scoped>
.tuyen-sinh-stats-overlap {
  position: relative;
  z-index: 4;
  margin-top: -76px;
  margin-bottom: 52px;
}

.tuyen-sinh-stats-box {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  overflow: hidden;
  border-radius: 20px;
  background: #063b7a;
  box-shadow: 0 18px 42px rgba(6, 59, 122, 0.22);
}

.tuyen-sinh-stats-box article {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 18px;
  min-height: 100px;
  padding: 20px;
  border-right: 1px solid rgba(255, 255, 255, 0.18);
}

.tuyen-sinh-stats-box article:last-child {
  border-right: none;
}

.tuyen-sinh-stats-box i {
  color: #dbeafe;
  font-size: 34px;
}

.tuyen-sinh-stats-box h3 {
  margin: 0 0 5px;
  color: #ffffff;
  font-size: 28px;
  line-height: 1;
  font-weight: 900;
}

.tuyen-sinh-stats-box p {
  margin: 0;
  color: #bfdbfe;
  font-size: 12px;
  line-height: 1.35;
}

@media (max-width: 1180px) {
  .tuyen-sinh-stats-box {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .tuyen-sinh-stats-overlap {
    margin-top: -58px;
  }

  .tuyen-sinh-stats-box {
    grid-template-columns: 1fr;
  }

  .tuyen-sinh-stats-box article {
    border-right: none;
    border-bottom: 1px solid rgba(255, 255, 255, 0.18);
  }

  .tuyen-sinh-stats-box article:last-child {
    border-bottom: none;
  }
}
</style>