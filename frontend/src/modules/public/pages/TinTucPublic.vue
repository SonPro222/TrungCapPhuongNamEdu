<template>
  <main class="tin-tuc-page">
    <section class="tin-tuc-hero">
      <div class="tin-tuc-hero-image">
        <img
            src="https://images.unsplash.com/photo-1541339907198-e08756dedf3f?auto=format&fit=crop&w=1400&q=90"
            alt="Tin tức Trường Trung cấp Phương Nam"
        />
        <div class="tin-tuc-hero-overlay"></div>
      </div>

      <div class="public-container tin-tuc-hero-content">
        <div class="breadcrumb">
          <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
          <span>/</span>
          <strong>Tin tức</strong>
        </div>

        <span class="hero-label">Tin tức & Sự kiện</span>

        <h1>Cập nhật tin tức và hoạt động mới nhất</h1>

        <p>
          Theo dõi thông tin tuyển sinh, hoạt động đào tạo, sự kiện,
          thông báo và các chương trình hợp tác của Trường Trung cấp Phương Nam.
        </p>

        <div class="hero-actions">
          <a href="#danh-sach-tin" class="primary-button">
            Xem tin mới
            <i class="fa-solid fa-arrow-right"></i>
          </a>

          <a href="#tin-noi-bat" class="secondary-button">
            Tin nổi bật
            <i class="fa-regular fa-newspaper"></i>
          </a>
        </div>
      </div>
    </section>

    <section id="tin-noi-bat" class="public-section featured-section">
      <div class="public-container">
        <div class="section-title-row">
          <div>
            <span class="section-eyebrow">Nổi bật</span>
            <h2>Tin tức nổi bật</h2>
          </div>

          <RouterLink :to="{ name: 'Public.TuyenSinh' }">
            Xem tuyển sinh →
          </RouterLink>
        </div>

        <div class="featured-grid">
          <RouterLink
              v-if="tinNoiBatChinh"
              :to="{ name: 'Public.ChiTietTinTuc', params: { id: tinNoiBatChinh.id } }"
              class="featured-main-card"
          >
            <div class="featured-main-image">
              <img
                  v-if="tinNoiBatChinh.image"
                  :src="tinNoiBatChinh.image"
                  :alt="tinNoiBatChinh.title"
              />

              <div v-else class="image-placeholder">
                <i class="fa-solid fa-newspaper"></i>
              </div>

              <div class="date-badge">
                <strong>{{ layNgay(tinNoiBatChinh) }}</strong>
                <span>{{ layThang(tinNoiBatChinh) }}</span>
              </div>
            </div>

            <div class="featured-main-content">
              <span>{{ tinNoiBatChinh.category }}</span>
              <h3>{{ tinNoiBatChinh.title }}</h3>
              <p>{{ tinNoiBatChinh.desc }}</p>

              <strong>
                Đọc thêm
                <i class="fa-solid fa-arrow-right"></i>
              </strong>
            </div>
          </RouterLink>

          <div class="featured-side-list">
            <RouterLink
                v-for="item in tinNoiBatPhu"
                :key="item.id"
                :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }"
                class="featured-side-item"
            >
              <img
                  v-if="item.image"
                  :src="item.image"
                  :alt="item.title"
              />

              <div v-else class="side-image-placeholder">
                <i class="fa-solid fa-newspaper"></i>
              </div>

              <div>
                <span>{{ item.category }}</span>
                <h4>{{ item.title }}</h4>
                <small>{{ layNgay(item) }} {{ layThang(item) }}</small>
              </div>
            </RouterLink>
          </div>
        </div>
      </div>
    </section>

    <section id="danh-sach-tin" class="public-section news-list-section">
      <div class="public-container news-layout">
        <main>
          <div class="section-title-row">
            <div>
              <span class="section-eyebrow">Danh sách</span>
              <h2>Tin tức mới nhất</h2>
            </div>

            <span class="count-text">
              {{ danhSachDaLoc.length }} bài viết
            </span>
          </div>

          <div class="filter-card">
            <label>
              <span>Tìm kiếm tin tức</span>
              <input
                  v-model.trim="tuKhoa"
                  type="text"
                  placeholder="Nhập tiêu đề, mô tả hoặc danh mục"
              />
            </label>

            <label>
              <span>Danh mục</span>
              <select v-model="danhMucDangChon">
                <option value="tat-ca">Tất cả danh mục</option>
                <option
                    v-for="item in danhSachDanhMuc"
                    :key="item"
                    :value="item"
                >
                  {{ item }}
                </option>
              </select>
            </label>
          </div>

          <div class="news-grid">
            <RouterLink
                v-for="item in danhSachDaLoc"
                :key="item.id"
                :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }"
                class="news-card"
            >
              <div class="news-image">
                <img
                    v-if="item.image"
                    :src="item.image"
                    :alt="item.title"
                />

                <div v-else class="image-placeholder">
                  <i class="fa-solid fa-newspaper"></i>
                </div>

                <div class="date-badge small">
                  <strong>{{ layNgay(item) }}</strong>
                  <span>{{ layThang(item) }}</span>
                </div>
              </div>

              <div class="news-content">
                <span>{{ item.category }}</span>
                <h3>{{ item.title }}</h3>
                <p>{{ item.desc }}</p>

                <strong>
                  Đọc thêm
                  <i class="fa-solid fa-arrow-right"></i>
                </strong>
              </div>
            </RouterLink>
          </div>

          <div v-if="!danhSachDaLoc.length" class="empty-state">
            <i class="fa-solid fa-circle-info"></i>
            <h2>Không có tin tức phù hợp</h2>
            <p>Vui lòng thử lại với từ khóa hoặc danh mục khác.</p>
          </div>
        </main>

        <aside class="news-sidebar">
          <div class="sidebar-card">
            <h3>Danh mục tin tức</h3>

            <button
                type="button"
                :class="{ active: danhMucDangChon === 'tat-ca' }"
                @click="danhMucDangChon = 'tat-ca'"
            >
              Tất cả danh mục
            </button>

            <button
                v-for="item in danhSachDanhMuc"
                :key="item"
                type="button"
                :class="{ active: danhMucDangChon === item }"
                @click="danhMucDangChon = item"
            >
              {{ item }}
            </button>
          </div>

          <div class="sidebar-card">
            <h3>Thông báo mới</h3>

            <RouterLink
                v-for="item in tinMoiSidebar"
                :key="item.id"
                :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }"
                class="sidebar-news-item"
            >
              <div>
                <strong>{{ layNgay(item) }}</strong>
                <span>{{ layThang(item) }}</span>
              </div>

              <p>{{ item.title }}</p>
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
</template>

<script setup>
import { computed, ref } from 'vue'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const { danhSachTinTuc } = useNoiDungPublic()

const tuKhoa = ref('')
const danhMucDangChon = ref('tat-ca')

const layNgay = (item) => {
  return item.day || item.date || '01'
}

const layThang = (item) => {
  return item.month || item.monthYear || 'TH01'
}

const danhSachHienThi = computed(() => {
  return danhSachTinTuc.value.filter((item) => item.trangThaiHienThi !== false)
})

const danhSachDanhMuc = computed(() => {
  return [
    ...new Set(
        danhSachHienThi.value
            .map((item) => item.category)
            .filter(Boolean)
    )
  ]
})

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSachHienThi.value.filter((item) => {
    const dungTuKhoa =
        !keyword ||
        String(item.title || '').toLowerCase().includes(keyword) ||
        String(item.category || '').toLowerCase().includes(keyword) ||
        String(item.desc || '').toLowerCase().includes(keyword)

    const dungDanhMuc =
        danhMucDangChon.value === 'tat-ca' ||
        item.category === danhMucDangChon.value

    return dungTuKhoa && dungDanhMuc
  })
})

const tinNoiBatChinh = computed(() => {
  return danhSachHienThi.value[0] || null
})

const tinNoiBatPhu = computed(() => {
  return danhSachHienThi.value.slice(1, 4)
})

const tinMoiSidebar = computed(() => {
  return danhSachHienThi.value.slice(0, 5)
})
</script>

<style scoped>
.tin-tuc-page {
  background: #ffffff;
}

.tin-tuc-hero {
  position: relative;
  overflow: hidden;
  min-height: 520px;
  padding: 70px 0 90px;
  background: #eff6ff;
}

.tin-tuc-hero-image {
  position: absolute;
  inset: 0;
  z-index: 0;
}

.tin-tuc-hero-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tin-tuc-hero-overlay {
  position: absolute;
  inset: 0;
  background:
      linear-gradient(90deg, #eff6ff 0%, rgba(239, 246, 255, 0.92) 34%, rgba(239, 246, 255, 0.34) 100%),
      rgba(255, 255, 255, 0.22);
}

.tin-tuc-hero-content {
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

.hero-label,
.section-eyebrow {
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

.tin-tuc-hero h1 {
  max-width: 760px;
  margin: 16px 0 16px;
  color: #063b7a;
  font-size: 52px;
  line-height: 1.1;
  font-weight: 900;
}

.tin-tuc-hero p {
  max-width: 650px;
  margin: 0;
  color: #365979;
  font-size: 16px;
  line-height: 1.8;
}

.hero-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 28px;
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

.section-title-row {
  display: flex;
  align-items: end;
  justify-content: space-between;
  gap: 18px;
  margin-bottom: 24px;
}

.section-title-row h2 {
  margin: 8px 0 0;
  color: #063b7a;
  font-size: 34px;
  font-weight: 900;
}

.section-title-row a {
  color: #075fc8;
  font-size: 14px;
  font-weight: 900;
  text-decoration: none;
}

.count-text {
  color: #607894;
  font-size: 14px;
  font-weight: 800;
}

.featured-section {
  background: #ffffff;
}

.featured-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.25fr) 0.75fr;
  gap: 24px;
}

.featured-main-card,
.news-card,
.featured-side-item,
.sidebar-news-item {
  color: inherit;
  text-decoration: none;
}

.featured-main-card {
  overflow: hidden;
  border: 1px solid #dbe7f5;
  border-radius: 24px;
  background: #ffffff;
  box-shadow: 0 10px 26px rgba(7, 63, 131, 0.07);
  transition: 0.22s ease;
}

.featured-main-card:hover,
.news-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 16px 34px rgba(7, 63, 131, 0.12);
}

.featured-main-image,
.news-image {
  position: relative;
  overflow: hidden;
  background: #eff6ff;
}

.featured-main-image {
  height: 360px;
}

.news-image {
  height: 210px;
}

.featured-main-image img,
.news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-placeholder {
  width: 100%;
  height: 100%;
  min-height: 210px;
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
  font-size: 56px;
}

.date-badge {
  position: absolute;
  left: 18px;
  bottom: 18px;
  min-width: 68px;
  overflow: hidden;
  border-radius: 14px;
  background: #ffffff;
  text-align: center;
  box-shadow: 0 10px 24px rgba(7, 63, 131, 0.18);
}

.date-badge strong {
  display: block;
  padding: 8px 10px 4px;
  color: #063b7a;
  font-size: 24px;
  line-height: 1;
  font-weight: 900;
}

.date-badge span {
  display: block;
  padding: 4px 10px 8px;
  color: #075fc8;
  font-size: 12px;
  font-weight: 900;
}

.date-badge.small {
  min-width: 62px;
}

.date-badge.small strong {
  font-size: 20px;
}

.featured-main-content,
.news-content {
  padding: 24px;
}

.featured-main-content > span,
.news-content > span,
.featured-side-item span {
  display: inline-flex;
  margin-bottom: 10px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.featured-main-content h3 {
  margin: 0 0 12px;
  color: #063b7a;
  font-size: 27px;
  line-height: 1.32;
  font-weight: 900;
}

.news-content h3 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 20px;
  line-height: 1.35;
  font-weight: 900;
}

.featured-main-content p,
.news-content p {
  display: -webkit-box;
  overflow: hidden;
  margin: 0 0 16px;
  color: #607894;
  font-size: 14px;
  line-height: 1.65;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.featured-main-content strong,
.news-content strong {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #075fc8;
  font-size: 14px;
  font-weight: 900;
}

.featured-side-list {
  display: grid;
  gap: 16px;
}

.featured-side-item {
  display: grid;
  grid-template-columns: 130px 1fr;
  gap: 14px;
  padding: 14px;
  border: 1px solid #dbe7f5;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
  transition: 0.2s ease;
}

.featured-side-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 26px rgba(7, 63, 131, 0.1);
}

.featured-side-item img,
.side-image-placeholder {
  width: 130px;
  height: 96px;
  border-radius: 14px;
}

.featured-side-item img {
  object-fit: cover;
}

.side-image-placeholder {
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
  font-size: 28px;
}

.featured-side-item h4 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 15px;
  line-height: 1.4;
  font-weight: 900;
}

.featured-side-item small {
  color: #607894;
  font-size: 12px;
  font-weight: 800;
}

.news-list-section {
  background: #f8fbff;
}

.news-layout {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 330px;
  gap: 28px;
  align-items: flex-start;
}

.filter-card {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 240px;
  gap: 16px;
  margin-bottom: 24px;
  padding: 20px;
  border: 1px solid #dbe7f5;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.filter-card label {
  display: grid;
  gap: 8px;
}

.filter-card span {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.filter-card input,
.filter-card select {
  width: 100%;
  height: 44px;
  padding: 0 14px;
  border: 1px solid #cfe1f6;
  border-radius: 12px;
  outline: none;
  background: #ffffff;
  color: #123b68;
  font: inherit;
}

.filter-card input:focus,
.filter-card select:focus {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.12);
}

.news-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 20px;
}

.news-card {
  overflow: hidden;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
  transition: 0.22s ease;
}

.news-sidebar {
  position: sticky;
  top: 92px;
  display: grid;
  gap: 18px;
}

.sidebar-card,
.sidebar-contact {
  padding: 22px;
  border: 1px solid #dbe7f5;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.sidebar-card h3,
.sidebar-contact h3 {
  margin: 0 0 16px;
  color: #063b7a;
  font-size: 18px;
  font-weight: 900;
}

.sidebar-card button {
  width: 100%;
  min-height: 40px;
  display: flex;
  align-items: center;
  margin-bottom: 8px;
  padding: 0 12px;
  border: none;
  border-radius: 12px;
  background: #f8fbff;
  color: #486581;
  font: inherit;
  font-size: 14px;
  font-weight: 800;
  cursor: pointer;
  text-align: left;
}

.sidebar-card button:hover,
.sidebar-card button.active {
  background: #eff6ff;
  color: #075fc8;
}

.sidebar-news-item {
  display: grid;
  grid-template-columns: 56px 1fr;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #edf4fb;
}

.sidebar-news-item:last-child {
  border-bottom: none;
}

.sidebar-news-item div {
  min-height: 50px;
  display: grid;
  place-items: center;
  border-radius: 12px;
  background: #eff6ff;
}

.sidebar-news-item strong {
  color: #063b7a;
  font-size: 18px;
  line-height: 1;
}

.sidebar-news-item span {
  color: #075fc8;
  font-size: 10px;
  font-weight: 900;
}

.sidebar-news-item p {
  margin: 0;
  color: #063b7a;
  font-size: 13px;
  line-height: 1.45;
  font-weight: 900;
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

.empty-state {
  display: grid;
  place-items: center;
  gap: 10px;
  padding: 56px 0;
  text-align: center;
}

.empty-state i {
  color: #075fc8;
  font-size: 42px;
}

.empty-state h2 {
  margin: 0;
  color: #063b7a;
}

.empty-state p {
  margin: 0;
  color: #607894;
}

@media (max-width: 1180px) {
  .featured-grid,
  .news-layout {
    grid-template-columns: 1fr;
  }

  .news-sidebar {
    position: static;
  }
}

@media (max-width: 760px) {
  .tin-tuc-hero {
    min-height: auto;
    padding: 44px 0 56px;
  }

  .tin-tuc-hero h1 {
    font-size: 34px;
  }

  .hero-actions,
  .filter-card,
  .news-grid {
    grid-template-columns: 1fr;
    display: grid;
  }

  .primary-button,
  .secondary-button {
    justify-content: center;
  }

  .section-title-row {
    align-items: flex-start;
    flex-direction: column;
  }

  .featured-main-image {
    height: 250px;
  }

  .featured-side-item {
    grid-template-columns: 90px 1fr;
  }

  .featured-side-item img,
  .side-image-placeholder {
    width: 90px;
    height: 76px;
  }
}
</style>