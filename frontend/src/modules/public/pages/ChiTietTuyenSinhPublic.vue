<template>
  <div v-if="noiDung" class="chi-tiet-tuyen-sinh-page">
    <section class="chi-tiet-tuyen-sinh-hero">
      <div class="public-container">
        <div class="breadcrumb">
          <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
          <span>/</span>
          <RouterLink :to="{ name: 'Public.TuyenSinh' }">Tuyển sinh</RouterLink>
          <span>/</span>
          <strong>{{ noiDung.tieuDe }}</strong>
        </div>

        <div class="hero-grid">
          <div>
            <span class="hero-label">Tuyển sinh</span>
            <h1>{{ noiDung.tieuDe }}</h1>
            <p>{{ noiDung.moTa }}</p>

            <div class="hero-actions">
              <RouterLink :to="{ name: 'Public.LienHe' }" class="primary-button">
                Đăng ký tư vấn
                <i class="fa-solid fa-arrow-right"></i>
              </RouterLink>

              <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="secondary-button">
                Xem trang tuyển sinh
                <i class="fa-solid fa-list"></i>
              </RouterLink>
            </div>
          </div>

          <div class="hero-card">
            <div class="hero-card-icon">
              <i :class="noiDung.icon || 'fa-solid fa-circle-info'"></i>
            </div>

            <h3>Thông tin nhanh</h3>

            <ul>
              <li v-for="item in noiDung.thongTinNhanh" :key="item">
                <i class="fa-solid fa-check"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </section>

    <section class="chi-tiet-tuyen-sinh-content">
      <div class="public-container content-grid">
        <main class="content-main">
          <section
              v-for="khoi in noiDung.noiDungChiTiet"
              :key="khoi.tieuDe"
              class="content-box"
          >
            <div class="content-box-title">
              <div>
                <i :class="khoi.icon || 'fa-solid fa-circle-check'"></i>
              </div>

              <h2>{{ khoi.tieuDe }}</h2>
            </div>

            <p v-if="khoi.moTa" class="content-desc">
              {{ khoi.moTa }}
            </p>

            <ul v-if="khoi.danhSach && khoi.danhSach.length" class="content-list">
              <li v-for="item in khoi.danhSach" :key="item">
                <i class="fa-solid fa-circle-check"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </section>
        </main>

        <aside class="content-sidebar">
          <div class="sidebar-card">
            <h3>Danh mục tuyển sinh</h3>

            <RouterLink
                v-for="item in danhMucTuyenSinh"
                :key="item.slug"
                :to="{ name: 'Public.ChiTietTuyenSinh', params: { slug: item.slug } }"
                :class="{ active: item.slug === slugHienTai }"
            >
              <i :class="item.icon || 'fa-solid fa-circle-info'"></i>
              <span>{{ item.tieuDe }}</span>
            </RouterLink>
          </div>

          <div class="sidebar-contact">
            <div>
              <i class="fa-solid fa-headset"></i>
            </div>

            <h3>Cần tư vấn tuyển sinh?</h3>
            <p>Đội ngũ tư vấn luôn sẵn sàng hỗ trợ học viên và phụ huynh.</p>

            <RouterLink :to="{ name: 'Public.LienHe' }">
              Liên hệ ngay
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>
          </div>
        </aside>
      </div>
    </section>
  </div>

  <div v-else class="public-container public-not-found">
    <i class="fa-solid fa-circle-exclamation"></i>
    <h1>Không tìm thấy thông tin tuyển sinh</h1>
    <p>Nội dung này không tồn tại hoặc đã được gỡ khỏi danh sách hiển thị.</p>
    <RouterLink :to="{ name: 'Public.TuyenSinh' }">Quay lại trang tuyển sinh</RouterLink>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useTuyenSinhPublic } from '@/modules/public/composables/useTuyenSinhPublic.js'

const route = useRoute()

const { danhSachTuyenSinh } = useTuyenSinhPublic()

const slugHienTai = computed(() => String(route.params.slug || ''))

const danhMucTuyenSinh = computed(() => {
  return danhSachTuyenSinh.value.filter((item) => item.trangThaiHienThi !== false)
})

const noiDung = computed(() => {
  return danhMucTuyenSinh.value.find((item) => item.slug === slugHienTai.value) || null
})
</script>

<style scoped>
.chi-tiet-tuyen-sinh-page {
  background: #ffffff;
}

.chi-tiet-tuyen-sinh-hero {
  position: relative;
  overflow: hidden;
  padding: 52px 0 64px;
  background:
      radial-gradient(circle at top right, rgba(7, 95, 200, 0.12), transparent 34%),
      linear-gradient(135deg, #eff6ff 0%, #ffffff 58%, #f8fbff 100%);
}

.breadcrumb {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 9px;
  margin-bottom: 28px;
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

.hero-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 380px;
  gap: 34px;
  align-items: stretch;
}

.hero-label {
  width: fit-content;
  display: inline-flex;
  align-items: center;
  min-height: 34px;
  padding: 0 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.hero-grid h1 {
  max-width: 760px;
  margin: 16px 0 14px;
  color: #063b7a;
  font-size: 42px;
  line-height: 1.14;
  font-weight: 900;
}

.hero-grid p {
  max-width: 760px;
  margin: 0;
  color: #486581;
  font-size: 16px;
  line-height: 1.75;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 26px;
}

.primary-button,
.secondary-button {
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

.primary-button {
  background: #075fc8;
  color: #ffffff;
  box-shadow: 0 12px 24px rgba(7, 95, 200, 0.22);
}

.secondary-button {
  border: 1px solid #cfe1f6;
  background: #ffffff;
  color: #063b7a;
}

.hero-card {
  padding: 28px;
  border: 1px solid #dbeafe;
  border-radius: 24px;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 18px 42px rgba(7, 63, 131, 0.12);
  backdrop-filter: blur(8px);
}

.hero-card-icon {
  width: 62px;
  height: 62px;
  display: grid;
  place-items: center;
  border-radius: 18px;
  background: #075fc8;
  color: #ffffff;
  font-size: 28px;
}

.hero-card h3 {
  margin: 18px 0 14px;
  color: #063b7a;
  font-size: 20px;
  font-weight: 900;
}

.hero-card ul {
  display: grid;
  gap: 12px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.hero-card li {
  display: grid;
  grid-template-columns: 20px 1fr;
  gap: 9px;
  color: #486581;
  font-size: 14px;
  line-height: 1.55;
}

.hero-card li i {
  margin-top: 3px;
  color: #075fc8;
}

.chi-tiet-tuyen-sinh-content {
  padding: 56px 0 76px;
  background: #ffffff;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 330px;
  gap: 28px;
  align-items: flex-start;
}

.content-main {
  display: grid;
  gap: 24px;
}

.content-box {
  padding: 30px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
}

.content-box-title {
  display: flex;
  align-items: center;
  gap: 14px;
  margin-bottom: 14px;
}

.content-box-title > div {
  width: 46px;
  height: 46px;
  display: grid;
  place-items: center;
  border-radius: 14px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 22px;
}

.content-box-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 24px;
  line-height: 1.3;
  font-weight: 900;
}

.content-desc {
  margin: 0 0 18px;
  color: #486581;
  font-size: 15px;
  line-height: 1.75;
}

.content-list {
  display: grid;
  gap: 12px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.content-list li {
  display: grid;
  grid-template-columns: 22px 1fr;
  gap: 10px;
  color: #3d5f83;
  font-size: 14px;
  line-height: 1.65;
}

.content-list li i {
  margin-top: 4px;
  color: #075fc8;
}

.content-sidebar {
  position: sticky;
  top: 96px;
  display: grid;
  gap: 18px;
}

.sidebar-card,
.sidebar-contact {
  padding: 22px;
  border: 1px solid #e6eef8;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
}

.sidebar-card h3,
.sidebar-contact h3 {
  margin: 0 0 16px;
  color: #063b7a;
  font-size: 18px;
  font-weight: 900;
}

.sidebar-card {
  display: grid;
  gap: 8px;
}

.sidebar-card a {
  display: grid;
  grid-template-columns: 24px 1fr;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border-radius: 12px;
  color: #486581;
  font-size: 14px;
  font-weight: 800;
  text-decoration: none;
  transition: 0.2s ease;
}

.sidebar-card a:hover,
.sidebar-card a.active {
  background: #eff6ff;
  color: #075fc8;
}

.sidebar-contact {
  background: #063b7a;
  color: #ffffff;
}

.sidebar-contact > div {
  width: 52px;
  height: 52px;
  display: grid;
  place-items: center;
  margin-bottom: 16px;
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.14);
  font-size: 24px;
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
  .hero-grid,
  .content-grid {
    grid-template-columns: 1fr;
  }

  .content-sidebar {
    position: static;
  }
}

@media (max-width: 760px) {
  .chi-tiet-tuyen-sinh-hero {
    padding: 34px 0 46px;
  }

  .hero-grid h1 {
    font-size: 32px;
  }

  .hero-card,
  .content-box {
    padding: 22px;
  }

  .content-box-title {
    align-items: flex-start;
  }

  .content-box-title h2 {
    font-size: 21px;
  }
}
</style>