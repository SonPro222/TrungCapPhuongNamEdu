<template>
  <section class="public-section white-section" id="tin-tuc">
    <div class="public-container">
      <div class="section-title-row">
        <h2>Tin tức & Sự kiện</h2>

        <RouterLink :to="{ name: 'Public.TinTuc' }">
          Xem thêm →
        </RouterLink>
      </div>

      <div class="news-grid">
        <article
            v-for="item in tinTuc"
            :key="item.id"
            class="news-card"
        >
          <div class="news-image">
            <img
                v-if="item.image"
                :src="item.image"
                :alt="item.title"
            />

            <div v-else class="news-image-placeholder">
              <i class="fa-solid fa-newspaper"></i>
            </div>

            <div class="date-badge">
              <strong>{{ item.day || item.date || '01' }}</strong>
              <span>{{ item.month || item.monthYear || 'TH01' }}</span>
            </div>
          </div>

          <div class="news-content">
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>

            <RouterLink :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }">
              Đọc thêm →
            </RouterLink>
          </div>
        </article>
      </div>

      <div v-if="!tinTuc.length" class="empty-state">
        Chưa có tin tức nào đang hiển thị.
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const { danhSachTinTuc } = useNoiDungPublic()

const tinTuc = computed(() => {
  return danhSachTinTuc.value
      .filter((item) => item.trangThaiHienThi !== false)
      .slice(0, 3)
})
</script>

<style scoped>
.news-image-placeholder {
  width: 100%;
  height: 100%;
  min-height: 190px;
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
  font-size: 44px;
}

.empty-state {
  padding: 34px 0;
  color: #607894;
  text-align: center;
}
</style>