<template>
  <main v-if="tin" class="chi-tiet-tin-page">
    <section class="hero-section">
      <div class="public-container">
        <div class="breadcrumb">
          <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
          <span>/</span>
          <RouterLink :to="{ name: 'Public.TinTuc' }">Tin tức</RouterLink>
          <span>/</span>
          <strong>{{ tin.title }}</strong>
        </div>

        <span class="category-label">{{ tin.category }}</span>
        <h1>{{ tin.title }}</h1>

        <div class="meta-row">
          <span>
            <i class="fa-solid fa-calendar-days"></i>
            {{ tin.day }} {{ tin.month }}
          </span>

          <span>
            <i class="fa-solid fa-user"></i>
            {{ tin.author || 'Ban truyền thông' }}
          </span>
        </div>
      </div>
    </section>

    <section class="content-section">
      <div class="public-container content-grid">
        <article class="article-card">
          <img
              v-if="tin.image"
              :src="tin.image"
              :alt="tin.title"
              class="article-image"
          />

          <p class="article-desc">
            {{ tin.desc }}
          </p>

          <div class="article-content">
            <p
                v-for="doan in noiDungChiTiet"
                :key="doan"
            >
              {{ doan }}
            </p>
          </div>
        </article>

        <aside class="sidebar">
          <div class="sidebar-card">
            <h3>Tin liên quan</h3>

            <RouterLink
                v-for="item in tinLienQuan"
                :key="item.id"
                :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }"
                class="related-item"
            >
              <img v-if="item.image" :src="item.image" :alt="item.title" />

              <div v-else class="related-placeholder">
                <i class="fa-solid fa-newspaper"></i>
              </div>

              <span>
                <strong>{{ item.title }}</strong>
                <small>{{ item.day }} {{ item.month }}</small>
              </span>
            </RouterLink>
          </div>

          <div class="sidebar-contact">
            <h3>Cần tư vấn tuyển sinh?</h3>
            <p>Để lại thông tin để nhà trường hỗ trợ bạn nhanh nhất.</p>

            <RouterLink :to="{ name: 'Public.LienHe' }">
              Liên hệ ngay
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>
          </div>
        </aside>
      </div>
    </section>
  </main>

  <div v-else class="public-container public-not-found">
    <i class="fa-solid fa-circle-exclamation"></i>
    <h1>Không tìm thấy tin tức</h1>
    <p>Bài viết này không tồn tại hoặc đã được gỡ khỏi danh sách hiển thị.</p>
    <RouterLink :to="{ name: 'Public.TinTuc' }">Quay lại danh sách tin tức</RouterLink>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const route = useRoute()
const { danhSachTinTuc } = useNoiDungPublic()

const idTin = computed(() => String(route.params.id || ''))

const danhSachHienThi = computed(() => {
  return danhSachTinTuc.value.filter((item) => item.trangThaiHienThi !== false)
})

const tin = computed(() => {
  return danhSachHienThi.value.find((item) => item.id === idTin.value) || null
})

const noiDungChiTiet = computed(() => {
  if (!tin.value) {
    return []
  }

  if (Array.isArray(tin.value.noiDungChiTiet) && tin.value.noiDungChiTiet.length) {
    return tin.value.noiDungChiTiet
  }

  if (tin.value.noiDung) {
    return String(tin.value.noiDung)
        .split(/\n\s*\n/)
        .map((item) => item.trim())
        .filter(Boolean)
  }

  return [
    tin.value.desc,
    'Nội dung chi tiết của bài viết đang được nhà trường cập nhật thêm.'
  ]
})

const tinLienQuan = computed(() => {
  if (!tin.value) {
    return []
  }

  return danhSachHienThi.value
      .filter((item) => item.id !== tin.value.id && item.category === tin.value.category)
      .slice(0, 4)
})
</script>

<style scoped>
.chi-tiet-tin-page {
  background: #ffffff;
}

.hero-section {
  padding: 52px 0 62px;
  background:
      radial-gradient(circle at top right, rgba(7, 95, 200, 0.12), transparent 34%),
      linear-gradient(135deg, #eff6ff 0%, #ffffff 60%, #f8fbff 100%);
}

.breadcrumb {
  display: flex;
  flex-wrap: wrap;
  gap: 9px;
  align-items: center;
  margin-bottom: 24px;
  color: #607894;
  font-size: 13px;
  font-weight: 700;
}

.breadcrumb a {
  color: #075fc8;
  text-decoration: none;
}

.breadcrumb strong {
  color: #063b7a;
}

.category-label {
  display: inline-flex;
  min-height: 34px;
  align-items: center;
  padding: 0 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.hero-section h1 {
  max-width: 920px;
  margin: 16px 0 18px;
  color: #063b7a;
  font-size: 42px;
  line-height: 1.18;
  font-weight: 900;
}

.meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 18px;
  color: #486581;
  font-size: 14px;
  font-weight: 800;
}

.meta-row span {
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.meta-row i {
  color: #075fc8;
}

.content-section {
  padding: 56px 0 76px;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 330px;
  gap: 28px;
  align-items: flex-start;
}

.article-card,
.sidebar-card,
.sidebar-contact {
  padding: 28px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
}

.article-image {
  width: 100%;
  max-height: 440px;
  display: block;
  object-fit: cover;
  border-radius: 18px;
  margin-bottom: 24px;
}

.article-desc {
  margin: 0 0 22px;
  padding: 18px 20px;
  border-left: 4px solid #075fc8;
  border-radius: 12px;
  background: #eff6ff;
  color: #063b7a;
  font-size: 17px;
  line-height: 1.75;
  font-weight: 800;
}

.article-content p {
  margin: 0 0 18px;
  color: #3d5f83;
  font-size: 16px;
  line-height: 1.9;
}

.sidebar {
  position: sticky;
  top: 96px;
  display: grid;
  gap: 18px;
}

.sidebar-card h3,
.sidebar-contact h3 {
  margin: 0 0 16px;
  color: #063b7a;
  font-size: 18px;
  font-weight: 900;
}

.related-item {
  display: grid;
  grid-template-columns: 70px 1fr;
  gap: 10px;
  align-items: center;
  padding: 10px;
  border-radius: 14px;
  color: inherit;
  text-decoration: none;
}

.related-item:hover {
  background: #eff6ff;
}

.related-item img,
.related-placeholder {
  width: 70px;
  height: 54px;
  border-radius: 12px;
}

.related-item img {
  object-fit: cover;
}

.related-placeholder {
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
}

.related-item strong {
  display: block;
  color: #063b7a;
  font-size: 14px;
  line-height: 1.4;
  font-weight: 900;
}

.related-item small {
  display: block;
  margin-top: 4px;
  color: #607894;
  font-size: 12px;
  font-weight: 700;
}

.sidebar-contact {
  background: #063b7a;
}

.sidebar-contact h3 {
  color: #ffffff;
}

.sidebar-contact p {
  margin: 0 0 16px;
  color: #dbeafe;
  font-size: 14px;
  line-height: 1.65;
}

.sidebar-contact a {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 0 16px;
  border-radius: 999px;
  background: #ffffff;
  color: #063b7a;
  font-size: 13px;
  font-weight: 900;
  text-decoration: none;
}

.public-not-found {
  min-height: 60vh;
  display: grid;
  place-items: center;
  align-content: center;
  gap: 14px;
  padding: 80px 0;
  text-align: center;
}

.public-not-found i {
  color: #f59e0b;
  font-size: 48px;
}

.public-not-found h1 {
  margin: 0;
  color: #063b7a;
  font-size: 34px;
}

.public-not-found p {
  max-width: 620px;
  margin: 0;
  color: #607894;
  line-height: 1.7;
}

.public-not-found a {
  margin-top: 8px;
  padding: 12px 20px;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font-weight: 800;
  text-decoration: none;
}

@media (max-width: 1180px) {
  .content-grid {
    grid-template-columns: 1fr;
  }

  .sidebar {
    position: static;
  }
}

@media (max-width: 760px) {
  .hero-section h1 {
    font-size: 32px;
  }

  .article-card,
  .sidebar-card,
  .sidebar-contact {
    padding: 22px;
  }
}
</style>