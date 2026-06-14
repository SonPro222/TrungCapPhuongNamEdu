<template>
  <section class="public-section white-section">
    <div class="public-container">
      <div class="section-heading">
        <span></span>
        <h2>Ngành đào tạo</h2>
        <span></span>
      </div>

      <div class="program-list-grid">
        <article
            v-for="item in danhSachHienThi"
            :key="item.id"
            class="program-row-card"
        >
          <img
              v-if="item.image"
              :src="item.image"
              :alt="item.title"
          />

          <div v-else class="program-image-placeholder">
            <i :class="item.icon || 'fa-solid fa-graduation-cap'"></i>
          </div>

          <div>
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>

            <RouterLink :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }">
              Xem chi tiết →
            </RouterLink>
          </div>
        </article>
      </div>

      <div v-if="!danhSachHienThi.length" class="empty-state">
        Chưa có ngành đào tạo nào đang hiển thị.
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const { danhSachNganhDaoTao } = useNoiDungPublic()

const danhSachHienThi = computed(() => {
  return danhSachNganhDaoTao.value
      .filter((item) => item.trangThaiHienThi !== false)
      .slice(0, 6)
})
</script>

<style scoped>
.program-image-placeholder {
  width: 100%;
  min-height: 160px;
  display: grid;
  place-items: center;
  border-radius: 16px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 42px;
}

.empty-state {
  padding: 34px 0;
  color: #607894;
  text-align: center;
}
</style>