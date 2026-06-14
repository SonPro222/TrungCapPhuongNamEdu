<template>
  <section class="public-section white-section compact-section">
    <div class="public-container">
      <div class="stats-banner">
        <article v-for="item in thongKeTrangChu" :key="item.label">
          <div>
            <span>{{ item.icon }}</span>
            <strong>{{ item.number }}</strong>
          </div>
          <p>{{ item.label }}</p>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
import { thongKeTrangChu } from '@/modules/public/data/publicData.js'
</script>