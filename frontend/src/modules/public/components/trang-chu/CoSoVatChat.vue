<template>
  <section class="public-section white-section">
    <div class="public-container">
      <div class="section-heading">
        <span></span>
        <h2>Cơ sở vật chất</h2>
        <span></span>
      </div>

      <div class="facility-grid">
        <RouterLink
            v-for="item in coSoVatChat"
            :key="item.id"
            :to="{ name: 'Public.ChiTietCoSoVatChat', params: { id: item.id } }"
            class="facility-card"
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
import { coSoVatChat } from '@/modules/public/data/publicData.js'
</script>

<style scoped>
.facility-card {
  color: inherit;
  text-decoration: none;
  transition: 0.25s ease;
}

.facility-card:hover {
  transform: translateY(-4px);
}

.facility-card p {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
}

.facility-card i {
  color: #075fc8;
  font-size: 16px;
}
</style>