<template>
  <div v-if="coSo" class="chi-tiet-co-so-page">
    <section class="facility-detail-hero">
      <div class="public-container facility-detail-hero-grid">
        <div>
          <div class="breadcrumb">
            <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
            <span>/</span>
            <RouterLink :to="{ name: 'Public.GioiThieu' }">Giới thiệu</RouterLink>
            <span>/</span>
            <strong>{{ coSo.title }}</strong>
          </div>

          <span class="facility-label">
            <i :class="coSo.icon"></i>
            Cơ sở vật chất
          </span>

          <h1>{{ coSo.title }}</h1>

          <p>{{ coSo.moTaChiTiet }}</p>

          <div class="facility-hero-actions">
            <RouterLink :to="{ name: 'Public.LienHe' }">
              Liên hệ tư vấn
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>

            <RouterLink :to="{ name: 'Public.GioiThieu' }">
              Xem giới thiệu trường
              <i class="fa-solid fa-building-columns"></i>
            </RouterLink>
          </div>
        </div>

        <div class="facility-detail-image">
          <img :src="coSo.image" :alt="coSo.title" />
        </div>
      </div>
    </section>

    <section class="public-container facility-overview">
      <article
          v-for="item in coSo.thongTin"
          :key="item.label"
      >
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
      </article>
    </section>

    <section class="public-container facility-detail-content">
      <main>
        <section class="detail-card">
          <h2>Điểm nổi bật</h2>

          <ul class="highlight-list">
            <li v-for="item in coSo.diemNoiBat" :key="item">
              <i class="fa-solid fa-check"></i>
              <span>{{ item }}</span>
            </li>
          </ul>
        </section>

        <section class="detail-card">
          <h2>Hình ảnh cơ sở vật chất</h2>

          <div class="facility-gallery">
            <img
                v-for="image in coSo.hinhAnhPhu"
                :key="image"
                :src="image"
                :alt="coSo.title"
            />
          </div>
        </section>
      </main>

      <aside>
        <div class="side-card">
          <h3>Cơ sở vật chất khác</h3>

          <RouterLink
              v-for="item in coSoKhac"
              :key="item.id"
              :to="{ name: 'Public.ChiTietCoSoVatChat', params: { id: item.id } }"
              class="related-facility"
          >
            <img :src="item.image" :alt="item.title" />

            <div>
              <strong>{{ item.title }}</strong>
              <span>{{ item.moTaNgan }}</span>
            </div>
          </RouterLink>
        </div>

        <div class="side-card contact-card">
          <h3>Bạn muốn tham quan trường?</h3>

          <p>
            Liên hệ bộ phận tuyển sinh để được tư vấn và đặt lịch tham quan cơ sở vật chất.
          </p>

          <RouterLink :to="{ name: 'Public.LienHe' }">
            Đăng ký tư vấn
          </RouterLink>
        </div>
      </aside>
    </section>
  </div>


  <div v-else class="public-container public-not-found">
    <i class="fa-solid fa-circle-exclamation"></i>
    <h1>Không tìm thấy cơ sở vật chất</h1>
    <p>Thông tin cơ sở vật chất này không tồn tại hoặc đã được gỡ khỏi danh sách hiển thị.</p>
    <RouterLink :to="{ name: 'Public.GioiThieu' }">Quay lại trang giới thiệu</RouterLink>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { danhSachCoSoVatChat } from '@/modules/public/data/coSoVatChatData.js'

const route = useRoute()

const idCoSo = computed(() => Number(route.params.id))

const coSo = computed(() => {
  return danhSachCoSoVatChat.find((item) => item.id === idCoSo.value) || null
})

const coSoKhac = computed(() => {
  if (!coSo.value) {
    return []
  }

  return danhSachCoSoVatChat
      .filter((item) => item.id !== coSo.value.id)
      .slice(0, 4)
})
</script>

<style scoped>
.facility-detail-hero {
  padding: 56px 0 72px;
  background: linear-gradient(135deg, #eff6ff, #ffffff);
}

.facility-detail-hero-grid {
  display: grid;
  grid-template-columns: 1fr 500px;
  gap: 46px;
  align-items: center;
}

.breadcrumb {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 9px;
  margin-bottom: 18px;
  color: #64748b;
  font-size: 13px;
  font-weight: 700;
}

.breadcrumb a {
  color: #64748b;
  text-decoration: none;
}

.breadcrumb strong {
  color: #075fc8;
}

.facility-label {
  width: fit-content;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
  padding: 7px 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #075fc8;
  font-size: 13px;
  font-weight: 800;
}

.facility-detail-hero h1 {
  margin: 0 0 18px;
  color: #063b7a;
  font-size: clamp(38px, 4vw, 58px);
  line-height: 1.15;
  font-weight: 800;
}

.facility-detail-hero p {
  max-width: 760px;
  margin: 0 0 28px;
  color: #365979;
  font-size: 15px;
  line-height: 1.85;
}

.facility-hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
}

.facility-hero-actions a {
  min-height: 46px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  border-radius: 10px;
  padding: 0 22px;
  font-size: 14px;
  font-weight: 800;
  text-decoration: none;
}

.facility-hero-actions a:first-child {
  background: #075fc8;
  color: #ffffff;
}

.facility-hero-actions a:last-child {
  border: 1px solid #b7cae3;
  background: #ffffff;
  color: #063b7a;
}

.facility-detail-image {
  overflow: hidden;
  border-radius: 24px;
  box-shadow: 0 18px 40px rgba(7, 63, 131, 0.16);
}

.facility-detail-image img {
  width: 100%;
  height: 350px;
  display: block;
  object-fit: cover;
}

.facility-overview {
  position: relative;
  z-index: 2;
  margin-top: -36px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  overflow: hidden;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 14px 32px rgba(7, 63, 131, 0.12);
}

.facility-overview article {
  display: grid;
  gap: 6px;
  padding: 22px;
  border-right: 1px solid #edf4fb;
}

.facility-overview article:last-child {
  border-right: none;
}

.facility-overview span {
  color: #64748b;
  font-size: 13px;
  font-weight: 700;
}

.facility-overview strong {
  color: #063b7a;
  font-size: 15px;
  font-weight: 800;
}

.facility-detail-content {
  display: grid;
  grid-template-columns: 1fr 380px;
  gap: 28px;
  padding-top: 48px;
  padding-bottom: 72px;
}

.detail-card,
.side-card {
  margin-bottom: 24px;
  padding: 28px;
  border: 1px solid #e6eef8;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.detail-card h2,
.side-card h3 {
  margin: 0 0 20px;
  color: #063b7a;
  font-size: 24px;
  font-weight: 800;
}

.highlight-list {
  display: grid;
  gap: 14px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.highlight-list li {
  display: flex;
  gap: 12px;
  color: #365979;
  line-height: 1.65;
}

.highlight-list i {
  margin-top: 5px;
  color: #075fc8;
}

.facility-gallery {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px;
}

.facility-gallery img {
  width: 100%;
  height: 230px;
  border-radius: 16px;
  object-fit: cover;
}

.related-facility {
  display: grid;
  grid-template-columns: 88px 1fr;
  gap: 12px;
  padding: 14px 0;
  border-bottom: 1px solid #edf4fb;
  text-decoration: none;
}

.related-facility:last-child {
  border-bottom: none;
}

.related-facility img {
  width: 88px;
  height: 68px;
  border-radius: 10px;
  object-fit: cover;
}

.related-facility strong {
  display: block;
  margin-bottom: 4px;
  color: #063b7a;
  font-size: 14px;
  line-height: 1.35;
}

.related-facility span {
  display: block;
  color: #607894;
  font-size: 12px;
  line-height: 1.45;
}

.contact-card {
  background: linear-gradient(135deg, #075fc8, #063b7a);
  color: #ffffff;
}

.contact-card h3 {
  color: #ffffff;
}

.contact-card p {
  color: #dbeafe;
  font-size: 14px;
  line-height: 1.7;
}

.contact-card a {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  margin-top: 14px;
  border-radius: 10px;
  background: #ffffff;
  color: #063b7a;
  font-weight: 800;
  text-decoration: none;
}

@media (max-width: 1180px) {
  .facility-detail-hero-grid,
  .facility-detail-content {
    grid-template-columns: 1fr;
  }

  .facility-overview {
    grid-template-columns: 1fr;
  }

  .facility-overview article {
    border-right: none;
    border-bottom: 1px solid #edf4fb;
  }

  .facility-overview article:last-child {
    border-bottom: none;
  }
}

@media (max-width: 760px) {
  .facility-detail-image img {
    height: 260px;
  }

  .facility-gallery {
    grid-template-columns: 1fr;
  }

  .facility-gallery img {
    height: 220px;
  }
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

</style>