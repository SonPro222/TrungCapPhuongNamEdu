<template>
  <section
      v-if="banner.trangThaiHienThi !== false"
      class="home-hero"
      :style="styleBanner"
  >
    <div class="public-container hero-grid">
      <div class="hero-content">
        <span class="hero-badge">
          {{ banner.nhanNho }}
        </span>

        <h1 class="hero-title">
          <span>{{ banner.tieuDe }}</span>
        </h1>

        <h3>{{ banner.tieuDePhu }}</h3>

        <p>{{ banner.moTa }}</p>

        <div class="hero-actions">
          <RouterLink
              v-if="banner.nutChinhText"
              :to="{ name: banner.nutChinhRoute || 'Public.LienHe' }"
              class="primary-button"
          >
            {{ banner.nutChinhText }}
            <i class="fa-solid fa-arrow-right"></i>
          </RouterLink>

          <RouterLink
              v-if="banner.nutPhuText"
              :to="{ name: banner.nutPhuRoute || 'Public.NganhDaoTao' }"
              class="light-button"
          >
            {{ banner.nutPhuText }}
            <i class="fa-regular fa-star"></i>
          </RouterLink>
        </div>

        <div v-if="banner.thongKe && banner.thongKe.length" class="hero-stats-inline">
          <div
              v-for="item in banner.thongKe"
              :key="`${item.soLieu}-${item.noiDung}`"
              class="hero-stat-item"
          >
            <strong>{{ item.soLieu }}</strong>
            <span>{{ item.noiDung }}</span>
          </div>
        </div>
      </div>

      <div class="hero-image-wrap">
        <img
            :src="banner.anhNen"
            :alt="banner.tieuDe"
        />

        <div class="hero-school-label">
          {{ tenTruong }}
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { useBannerTrangChuPublic } from '@/modules/public/composables/useBannerTrangChuPublic.js'

const { bannerTrangChu } = useBannerTrangChuPublic()

const banner = computed(() => bannerTrangChu.value)

const tenTruong = computed(() => {
  return String(banner.value.tieuDe || 'TRƯỜNG TRUNG CẤP PHƯƠNG NAM').toUpperCase()
})

const styleBanner = computed(() => {
  if (!banner.value.anhNen) {
    return {}
  }

  return {
    backgroundImage: `linear-gradient(135deg, rgba(239, 246, 255, 0.96), rgba(255, 255, 255, 0.9)), url(${banner.value.anhNen})`,
    backgroundSize: 'cover',
    backgroundPosition: 'center'
  }
})
</script>

<style scoped>
.hero-title {
  max-width: 860px;
  margin: 0 0 14px;
  color: #063b7a;
  font-size: clamp(52px, 5.1vw, 76px);
  line-height: 1.15;
  font-weight: 800;
  letter-spacing: -1.8px;
}

.hero-title span {
  display: block;
}

.hero-content h3 {
  margin: 0 0 18px;
  color: #245f9f;
  font-size: clamp(18px, 1.55vw, 23px);
  line-height: 1.45;
  font-weight: 700;
  font-style: italic;
  letter-spacing: -0.35px;
}

.hero-content p {
  max-width: 650px;
  margin: 0 0 28px;
  color: #365979;
  font-size: 15px;
  line-height: 1.85;
  font-weight: 400;
}

.hero-badge {
  width: fit-content;
  display: inline-flex;
  align-items: center;
  min-height: 30px;
  padding: 0 18px;
  margin-bottom: 24px;
  border-radius: 999px;
  background: #ffffff;
  color: #075fc8;
  font-size: 13px;
  font-weight: 700;
  letter-spacing: -0.15px;
  box-shadow: 0 8px 18px rgba(7, 63, 131, 0.08);
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 14px;
}

.primary-button,
.light-button {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-radius: 9px;
  padding: 0 24px;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
}

.primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 10px 24px rgba(7, 95, 200, 0.24);
}

.light-button {
  border: 1px solid #b7cae3;
  background: #ffffff;
  color: #123d70;
}

.hero-school-label {
  position: absolute;
  right: 24px;
  bottom: 92px;
  z-index: 3;
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  padding: 0 28px;
  background: #075fc8;
  color: #ffffff;
  font-size: 13px;
  font-weight: 800;
  letter-spacing: -0.2px;
}

.hero-stats-inline {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
}

.hero-stat-item {
  min-width: 118px;
  padding: 13px 16px;
  border: 1px solid #dbeafe;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.82);
  box-shadow: 0 8px 20px rgba(7, 63, 131, 0.08);
}

.hero-stat-item strong {
  display: block;
  color: #063b7a;
  font-size: 24px;
  line-height: 1;
  font-weight: 900;
}

.hero-stat-item span {
  display: block;
  margin-top: 5px;
  color: #486581;
  font-size: 12px;
  line-height: 1.35;
  font-weight: 800;
}

@media (max-width: 1180px) {
  .hero-title {
    font-size: clamp(46px, 6vw, 64px);
  }
}

@media (max-width: 760px) {
  .hero-title {
    font-size: 40px;
    line-height: 1.18;
    letter-spacing: -1px;
  }

  .hero-content h3 {
    font-size: 17px;
  }

  .hero-content p {
    font-size: 14px;
  }

  .hero-actions {
    align-items: stretch;
    flex-direction: column;
  }

  .primary-button,
  .light-button {
    width: 100%;
  }

  .hero-stats-inline {
    display: grid;
    grid-template-columns: 1fr;
  }
}
</style>