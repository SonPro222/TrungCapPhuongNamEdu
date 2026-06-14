<template>
  <section class="public-section gray-section">
    <div class="public-container">
      <div class="section-title-row">
        <h2>Học viên nói về chúng tôi</h2>

        <RouterLink
            v-if="danhSachCamNhanHienThi.length"
            :to="{ name: 'Public.ChiTietCamNhanHocVien', params: { slug: danhSachCamNhanHienThi[0].slug } }"
        >
          Xem thêm →
        </RouterLink>
      </div>

      <div class="testimonial-grid">
        <RouterLink
            v-for="item in danhSachCamNhanHienThi"
            :key="item.slug"
            :to="{ name: 'Public.ChiTietCamNhanHocVien', params: { slug: item.slug } }"
            class="testimonial-card"
        >
          <div class="testimonial-head">
            <img
                v-if="item.avatar"
                :src="item.avatar"
                :alt="item.ten"
            />

            <div v-else class="testimonial-avatar-placeholder">
              <i class="fa-solid fa-user-graduate"></i>
            </div>

            <div>
              <h4>{{ item.ten }}</h4>
              <p>{{ item.khoa }}</p>
            </div>

            <span>{{ taoSao(item.danhGia) }}</span>
          </div>

          <blockquote>“{{ item.cauNoiNgan }}”</blockquote>
        </RouterLink>
      </div>

      <div v-if="!danhSachCamNhanHienThi.length" class="empty-state">
        Chưa có cảm nhận học viên nào đang hiển thị.
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { RouterLink } from 'vue-router'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const { danhSachCamNhanHocVien } = useNoiDungPublic()

const danhSachCamNhanHienThi = computed(() => {
  return danhSachCamNhanHocVien.value
      .filter((item) => item.trangThaiHienThi !== false)
      .slice(0, 3)
})

const taoSao = (soSao) => {
  return '★'.repeat(Number(soSao || 5))
}
</script>

<style scoped>
.testimonial-card {
  color: inherit;
  text-decoration: none;
  transition: 0.22s ease;
}

.testimonial-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 16px 34px rgba(7, 63, 131, 0.14);
}

.testimonial-card:hover h4 {
  color: #075fc8;
}

.testimonial-avatar-placeholder {
  width: 54px;
  height: 54px;
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
}

.empty-state {
  padding: 40px 0;
  color: #607894;
  text-align: center;
}
</style>