<template>
  <section class="about-section-white">
    <div class="public-container">
      <div class="about-section-title">
        <span></span>
        <h2>Cơ sở vật chất hiện đại</h2>
        <span></span>
      </div>

      <div class="about-facility-grid">
        <RouterLink
            v-for="item in facilities"
            :key="item.id"
            :to="{ name: 'Public.ChiTietCoSoVatChat', params: { id: item.id } }"
            class="about-facility-card"
        >
          <img :src="item.image" :alt="item.title" />

          <p>
            <i :class="item.icon"></i>
            {{ item.title }}
          </p>
        </RouterLink>
      </div>
    </div>
  </section>
</template>

<script setup>
import { danhSachCoSoVatChat } from '@/modules/public/data/coSoVatChatData.js'

const facilities = danhSachCoSoVatChat
</script>

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

.about-facility-grid {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 16px;
}

.about-facility-card {
  color: inherit;
  text-decoration: none;
  cursor: pointer;
}

.about-facility-card img {
  display: block;
  width: 100%;
  height: 142px;
  object-fit: cover;
  border: 1px solid #e6eef8;
  border-radius: 12px;
  filter: grayscale(1);
  transition: 0.3s;
}

.about-facility-card:hover img {
  filter: grayscale(0);
  transform: scale(1.02);
}

.about-facility-card p {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 7px;
  margin: 10px 0 0;
  color: #475569;
  font-size: 12px;
  font-weight: 900;
  text-align: center;
}

.about-facility-card i {
  color: #075fc8;
}

.about-facility-card:hover p {
  color: #075fc8;
}

@media (max-width: 1180px) {
  .about-facility-grid {
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

  .about-facility-grid {
    grid-template-columns: 1fr;
  }
}
</style>