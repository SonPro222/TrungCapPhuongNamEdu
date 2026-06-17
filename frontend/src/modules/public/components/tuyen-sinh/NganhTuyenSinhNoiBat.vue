<template>
  <section class="tuyen-sinh-section">
    <div class="public-container">
      <div class="tuyen-sinh-section-title">
        <span></span>
        <h2>Ngành tuyển sinh nổi bật</h2>
        <span></span>
      </div>

      <div class="admission-program-grid">
        <article v-for="item in nganhTuyenSinhNoiBat" :key="item.title" class="admission-program-card">
          <img :src="item.image" :alt="item.title" />

          <div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
            <RouterLink :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }">
              Xem chi tiết
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { danhSachNganhDaoTao } from '@/modules/public/data/nganhDaoTaoData.js'

const nganhTuyenSinhNoiBat = computed(() => danhSachNganhDaoTao.slice(0, 6))
</script>

<style scoped>
.tuyen-sinh-section {
  padding: 34px 0;
  background: #ffffff;
}

.tuyen-sinh-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 18px;
  margin-bottom: 34px;
  text-align: center;
}

.tuyen-sinh-section-title span {
  width: 88px;
  height: 1px;
  background: #cbd5e1;
}

.tuyen-sinh-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 25px;
  font-weight: 900;
}

.admission-program-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 22px;
}

.admission-program-card {
  display: grid;
  grid-template-columns: 112px 1fr;
  gap: 16px;
  padding: 15px;
  border: 1px solid #e6eef8;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
  transition: 0.25s;
}

.admission-program-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 30px rgba(7, 63, 131, 0.1);
}

.admission-program-card img {
  width: 112px;
  height: 96px;
  border-radius: 12px;
  object-fit: cover;
}

.admission-program-card h3 {
  margin: 0 0 7px;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.admission-program-card p {
  margin: 0 0 9px;
  color: #607894;
  font-size: 12px;
  line-height: 1.45;
}

.admission-program-card a {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #075fc8;
  font-size: 11px;
  font-weight: 900;
  text-decoration: none;
  text-transform: uppercase;
  letter-spacing: 0.3px;
}

@media (max-width: 1180px) {
  .admission-program-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .admission-program-grid {
    grid-template-columns: 1fr;
  }

  .admission-program-card {
    grid-template-columns: 1fr;
  }

  .admission-program-card img {
    width: 100%;
    height: 170px;
  }
}
</style>