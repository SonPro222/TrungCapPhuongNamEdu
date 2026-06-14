<template>
  <div v-if="camNhan" class="chi-tiet-cam-nhan-page">
    <section class="cam-nhan-hero">
      <div class="public-container">
        <div class="breadcrumb">
          <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
          <span>/</span>
          <strong>Học viên nói về chúng tôi</strong>
        </div>

        <div class="hero-card">
          <div class="hoc-vien-avatar">
            <img
                v-if="camNhan.avatar"
                :src="camNhan.avatar"
                :alt="camNhan.ten"
            />

            <div v-else class="hoc-vien-avatar-placeholder">
              <i class="fa-solid fa-user-graduate"></i>
            </div>
          </div>

          <div class="hoc-vien-info">
            <div class="rating">
              <i
                  v-for="sao in Number(camNhan.danhGia || 5)"
                  :key="sao"
                  class="fa-solid fa-star"
              ></i>
            </div>

            <h1>{{ camNhan.tieuDe }}</h1>

            <p class="quote">“{{ camNhan.cauNoiNgan }}”</p>

            <div class="hoc-vien-meta">
              <div>
                <span>Học viên</span>
                <strong>{{ camNhan.ten }}</strong>
              </div>

              <div>
                <span>Khoa</span>
                <strong>{{ camNhan.khoa }}</strong>
              </div>

              <div>
                <span>Niên khóa</span>
                <strong>{{ camNhan.namHoc || 'Đang cập nhật' }}</strong>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <section class="cam-nhan-content">
      <div class="public-container content-grid">
        <main class="content-main">
          <article class="content-box">
            <h2>Câu chuyện học tập</h2>

            <p
                v-for="doan in noiDungChiTiet"
                :key="doan"
            >
              {{ doan }}
            </p>
          </article>

          <article class="content-box">
            <h2>Điểm nổi bật trong quá trình học</h2>

            <ul class="diem-noi-bat-list">
              <li
                  v-for="item in diemNoiBat"
                  :key="item"
              >
                <i class="fa-solid fa-circle-check"></i>
                <span>{{ item }}</span>
              </li>
            </ul>
          </article>
        </main>

        <aside class="content-sidebar">
          <div class="sidebar-card">
            <h3>Thông tin học viên</h3>

            <div class="sidebar-profile">
              <img
                  v-if="camNhan.avatar"
                  :src="camNhan.avatar"
                  :alt="camNhan.ten"
              />

              <div v-else class="sidebar-avatar-placeholder">
                <i class="fa-solid fa-user-graduate"></i>
              </div>

              <div>
                <strong>{{ camNhan.ten }}</strong>
                <span>{{ camNhan.khoa }}</span>
              </div>
            </div>

            <ul>
              <li>
                <i class="fa-solid fa-graduation-cap"></i>
                <span>{{ camNhan.namHoc || 'Đang cập nhật' }}</span>
              </li>

              <li>
                <i class="fa-solid fa-briefcase"></i>
                <span>{{ camNhan.hienTai || 'Đang cập nhật' }}</span>
              </li>
            </ul>
          </div>

          <div class="sidebar-card">
            <h3>Cảm nhận khác</h3>

            <RouterLink
                v-for="item in camNhanKhac"
                :key="item.slug"
                :to="{ name: 'Public.ChiTietCamNhanHocVien', params: { slug: item.slug } }"
                class="cam-nhan-khac-item"
            >
              <img
                  v-if="item.avatar"
                  :src="item.avatar"
                  :alt="item.ten"
              />

              <div v-else class="related-avatar-placeholder">
                <i class="fa-solid fa-user-graduate"></i>
              </div>

              <span>
                <strong>{{ item.ten }}</strong>
                <small>{{ item.khoa }}</small>
              </span>
            </RouterLink>
          </div>

          <div class="sidebar-contact">
            <h3>Bạn cần tư vấn ngành học?</h3>
            <p>Để lại thông tin để nhà trường hỗ trợ chọn ngành phù hợp.</p>

            <RouterLink :to="{ name: 'Public.LienHe' }">
              Liên hệ tư vấn
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>
          </div>
        </aside>
      </div>
    </section>
  </div>

  <div v-else class="public-container public-not-found">
    <i class="fa-solid fa-circle-exclamation"></i>
    <h1>Không tìm thấy cảm nhận học viên</h1>
    <p>Cảm nhận này không tồn tại hoặc đã được gỡ khỏi danh sách hiển thị.</p>
    <RouterLink :to="{ name: 'Public.TrangChu' }">Quay lại trang chủ</RouterLink>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const route = useRoute()
const { danhSachCamNhanHocVien } = useNoiDungPublic()

const slugHienTai = computed(() => String(route.params.slug || ''))

const danhSachHienThi = computed(() => {
  return danhSachCamNhanHocVien.value.filter((item) => item.trangThaiHienThi !== false)
})

const camNhan = computed(() => {
  return danhSachHienThi.value.find((item) => item.slug === slugHienTai.value) || null
})

const noiDungChiTiet = computed(() => {
  if (!camNhan.value) {
    return []
  }

  if (Array.isArray(camNhan.value.noiDungChiTiet) && camNhan.value.noiDungChiTiet.length) {
    return camNhan.value.noiDungChiTiet
  }

  return [
    camNhan.value.cauNoiNgan,
    'Nội dung chi tiết cảm nhận học viên đang được nhà trường cập nhật thêm.'
  ]
})

const diemNoiBat = computed(() => {
  if (!camNhan.value) {
    return []
  }

  if (Array.isArray(camNhan.value.diemNoiBat) && camNhan.value.diemNoiBat.length) {
    return camNhan.value.diemNoiBat
  }

  return [
    'Môi trường học tập thân thiện.',
    'Giảng viên tận tâm, hỗ trợ học viên.',
    'Chương trình học gắn với thực hành.'
  ]
})

const camNhanKhac = computed(() => {
  if (!camNhan.value) {
    return []
  }

  return danhSachHienThi.value
      .filter((item) => item.slug !== camNhan.value.slug)
      .slice(0, 3)
})
</script>

<style scoped>
.chi-tiet-cam-nhan-page {
  background: #ffffff;
}

.cam-nhan-hero {
  padding: 48px 0 58px;
  background:
      radial-gradient(circle at top right, rgba(7, 95, 200, 0.12), transparent 34%),
      linear-gradient(135deg, #eff6ff 0%, #ffffff 60%, #f8fbff 100%);
}

.breadcrumb {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 9px;
  margin-bottom: 26px;
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

.hero-card {
  display: grid;
  grid-template-columns: 180px minmax(0, 1fr);
  gap: 30px;
  align-items: center;
  padding: 32px;
  border: 1px solid #dbeafe;
  border-radius: 26px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 18px 42px rgba(7, 63, 131, 0.12);
}

.hoc-vien-avatar {
  width: 180px;
  height: 180px;
  padding: 8px;
  border-radius: 999px;
  background: #eff6ff;
}

.hoc-vien-avatar img,
.hoc-vien-avatar-placeholder {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  border-radius: 999px;
  object-fit: cover;
}

.hoc-vien-avatar-placeholder {
  color: #075fc8;
  font-size: 54px;
}

.rating {
  display: flex;
  gap: 4px;
  margin-bottom: 12px;
  color: #f5b400;
  font-size: 15px;
}

.hoc-vien-info h1 {
  max-width: 820px;
  margin: 0 0 14px;
  color: #063b7a;
  font-size: 38px;
  line-height: 1.2;
  font-weight: 900;
}

.quote {
  max-width: 820px;
  margin: 0;
  color: #486581;
  font-size: 17px;
  line-height: 1.8;
}

.hoc-vien-meta {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 14px;
  margin-top: 24px;
}

.hoc-vien-meta div {
  padding: 16px;
  border: 1px solid #e6eef8;
  border-radius: 16px;
  background: #f8fbff;
}

.hoc-vien-meta span {
  display: block;
  margin-bottom: 5px;
  color: #607894;
  font-size: 12px;
  font-weight: 800;
}

.hoc-vien-meta strong {
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.cam-nhan-content {
  padding: 56px 0 76px;
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

.content-box,
.sidebar-card,
.sidebar-contact {
  padding: 28px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
}

.content-box h2 {
  margin: 0 0 16px;
  color: #063b7a;
  font-size: 25px;
  font-weight: 900;
}

.content-box p {
  margin: 0 0 16px;
  color: #486581;
  font-size: 15px;
  line-height: 1.85;
}

.content-box p:last-child {
  margin-bottom: 0;
}

.diem-noi-bat-list {
  display: grid;
  gap: 12px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.diem-noi-bat-list li {
  display: grid;
  grid-template-columns: 22px 1fr;
  gap: 10px;
  color: #3d5f83;
  font-size: 15px;
  line-height: 1.65;
}

.diem-noi-bat-list i {
  margin-top: 4px;
  color: #075fc8;
}

.content-sidebar {
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

.sidebar-profile {
  display: grid;
  grid-template-columns: 58px 1fr;
  gap: 12px;
  align-items: center;
  margin-bottom: 18px;
}

.sidebar-profile img,
.sidebar-avatar-placeholder {
  width: 58px;
  height: 58px;
  border-radius: 999px;
}

.sidebar-profile img {
  object-fit: cover;
}

.sidebar-avatar-placeholder {
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
}

.sidebar-profile strong {
  display: block;
  color: #063b7a;
  font-size: 15px;
  font-weight: 900;
}

.sidebar-profile span {
  display: block;
  margin-top: 3px;
  color: #607894;
  font-size: 13px;
  font-weight: 700;
}

.sidebar-card ul {
  display: grid;
  gap: 12px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.sidebar-card li {
  display: grid;
  grid-template-columns: 22px 1fr;
  gap: 10px;
  color: #486581;
  font-size: 14px;
  line-height: 1.55;
}

.sidebar-card li i {
  margin-top: 3px;
  color: #075fc8;
}

.cam-nhan-khac-item {
  display: grid;
  grid-template-columns: 46px 1fr;
  gap: 10px;
  align-items: center;
  padding: 10px;
  border-radius: 14px;
  color: #063b7a;
  text-decoration: none;
}

.cam-nhan-khac-item:hover {
  background: #eff6ff;
}

.cam-nhan-khac-item img,
.related-avatar-placeholder {
  width: 46px;
  height: 46px;
  border-radius: 999px;
}

.cam-nhan-khac-item img {
  object-fit: cover;
}

.related-avatar-placeholder {
  display: grid;
  place-items: center;
  background: #eff6ff;
  color: #075fc8;
}

.cam-nhan-khac-item strong {
  display: block;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.cam-nhan-khac-item small {
  display: block;
  margin-top: 3px;
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

  .content-sidebar {
    position: static;
  }
}

@media (max-width: 760px) {
  .cam-nhan-hero {
    padding: 34px 0 46px;
  }

  .hero-card {
    grid-template-columns: 1fr;
    padding: 24px;
    text-align: center;
  }

  .hoc-vien-avatar {
    width: 150px;
    height: 150px;
    margin: 0 auto;
  }

  .rating {
    justify-content: center;
  }

  .hoc-vien-info h1 {
    font-size: 30px;
  }

  .hoc-vien-meta {
    grid-template-columns: 1fr;
  }

  .content-box,
  .sidebar-card,
  .sidebar-contact {
    padding: 22px;
  }
}
</style>