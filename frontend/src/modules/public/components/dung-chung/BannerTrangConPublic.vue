<template>
  <section class="banner-trang-con">
    <div class="banner-image">
      <img
          :src="anhNen"
          :alt="tieuDe"
      />
      <div class="banner-overlay"></div>
    </div>

    <div class="public-container banner-content">
      <div class="breadcrumb">
        <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
        <span>/</span>
        <strong>{{ tenTrang }}</strong>
      </div>

      <span class="banner-label">
        {{ nhan }}
      </span>

      <h1>{{ tieuDe }}</h1>

      <p>{{ moTa }}</p>

      <div v-if="$slots.actions" class="banner-actions">
        <slot name="actions"></slot>
      </div>
    </div>
  </section>
</template>

<script setup>
defineProps({
  tenTrang: {
    type: String,
    required: true
  },
  nhan: {
    type: String,
    required: true
  },
  tieuDe: {
    type: String,
    required: true
  },
  moTa: {
    type: String,
    required: true
  },
  anhNen: {
    type: String,
    default: 'https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1400&q=90'
  }
})
</script>

<style scoped>
.banner-trang-con {
  position: relative;
  overflow: hidden;
  min-height: 520px;
  padding: 70px 0 90px;
  background: #eff6ff;
}

.banner-image {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.banner-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  inset: 0;
  background:
      linear-gradient(90deg, #eff6ff 0%, rgba(239, 246, 255, 0.92) 34%, rgba(239, 246, 255, 0.34) 100%),
      rgba(255, 255, 255, 0.22);
}

.banner-content {
  position: relative;
  z-index: 2;
}

.breadcrumb {
  display: flex;
  align-items: center;
  gap: 9px;
  margin-bottom: 20px;
  color: #607894;
  font-size: 13px;
  font-weight: 800;
}

.breadcrumb a {
  color: #607894;
  text-decoration: none;
}

.breadcrumb strong {
  color: #075fc8;
}

.banner-label {
  display: inline-flex;
  width: fit-content;
  min-height: 32px;
  align-items: center;
  padding: 0 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.banner-content h1 {
  max-width: 760px;
  margin: 16px 0 16px;
  color: #063b7a;
  font-size: 52px;
  line-height: 1.1;
  font-weight: 900;
}

.banner-content p {
  max-width: 650px;
  margin: 0;
  color: #365979;
  font-size: 16px;
  line-height: 1.8;
}

.banner-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
}

:slotted(a) {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  padding: 0 20px;
  border-radius: 999px;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

:slotted(.primary-button) {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 12px 24px rgba(7, 95, 200, 0.22);
}

:slotted(.secondary-button) {
  border: 1px solid #cfe1f6;
  background: #ffffff;
  color: #063b7a;
}

@media (max-width: 760px) {
  .banner-trang-con {
    min-height: auto;
    padding: 44px 0 56px;
  }

  .banner-content h1 {
    font-size: 34px;
  }

  .banner-actions {
    display: grid;
  }

  :slotted(a) {
    justify-content: center;
  }
}
</style>