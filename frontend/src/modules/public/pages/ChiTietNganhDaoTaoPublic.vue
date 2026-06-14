<template>
  <div v-if="nganh" class="chi-tiet-nganh-page">
    <section class="chi-tiet-nganh-hero">
      <div class="public-container chi-tiet-nganh-hero-grid">
        <div>
          <div class="breadcrumb">
            <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
            <span>/</span>
            <RouterLink :to="{ name: 'Public.NganhDaoTao' }">Ngành đào tạo</RouterLink>
            <span>/</span>
            <strong>{{ nganh.title }}</strong>
          </div>

          <span class="nganh-category">{{ nganh.category }}</span>

          <h1>{{ nganh.title }}</h1>

          <p>{{ gioiThieuNganh }}</p>

          <div class="hero-actions-detail">
            <RouterLink :to="{ name: 'Public.TuyenSinh' }">
              Đăng ký xét tuyển
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>

            <RouterLink :to="{ name: 'Public.LienHe' }">
              Tư vấn ngành học
              <i class="fa-solid fa-headset"></i>
            </RouterLink>
          </div>
        </div>

        <div class="chi-tiet-nganh-image">
          <img
              v-if="nganh.image"
              :src="nganh.image"
              :alt="nganh.title"
          />

          <div v-else class="chi-tiet-nganh-image-placeholder">
            <i :class="nganh.icon || 'fa-solid fa-graduation-cap'"></i>
          </div>
        </div>
      </div>
    </section>

    <section class="public-container chi-tiet-nganh-info">
      <article>
        <i class="fa-solid fa-clock"></i>
        <span>Thời gian đào tạo</span>
        <strong>{{ nganh.thoiGianDaoTao || nganh.duration || 'Đang cập nhật' }}</strong>
      </article>

      <article>
        <i class="fa-solid fa-layer-group"></i>
        <span>Nhóm ngành</span>
        <strong>{{ nganh.category || 'Đang cập nhật' }}</strong>
      </article>

      <article>
        <i class="fa-solid fa-briefcase"></i>
        <span>Cơ hội việc làm</span>
        <strong>Rộng mở</strong>
      </article>

      <article>
        <i class="fa-solid fa-certificate"></i>
        <span>Văn bằng</span>
        <strong>Trung cấp</strong>
      </article>
    </section>

    <section class="public-container chi-tiet-nganh-content">
      <main>
        <section class="detail-card">
          <h2>Mục tiêu đào tạo</h2>

          <ul>
            <li v-for="item in chiTiet.mucTieu" :key="item">
              <i class="fa-solid fa-check"></i>
              <span>{{ item }}</span>
            </li>
          </ul>
        </section>

        <section class="detail-card">
          <h2>Nội dung chương trình</h2>

          <div class="chuong-trinh-grid">
            <article v-for="item in chiTiet.noiDungDaoTao" :key="item">
              <i class="fa-solid fa-book-open"></i>
              <span>{{ item }}</span>
            </article>
          </div>
        </section>

        <section class="detail-card">
          <h2>Vị trí việc làm sau tốt nghiệp</h2>

          <div class="viec-lam-grid">
            <article v-for="item in chiTiet.viTriViecLam" :key="item">
              <i class="fa-solid fa-briefcase"></i>
              <span>{{ item }}</span>
            </article>
          </div>
        </section>
      </main>

      <aside>
        <div class="side-card">
          <h3>Thông tin tuyển sinh</h3>

          <ul>
            <li>
              <span>Hình thức:</span>
              <strong>Xét tuyển hồ sơ</strong>
            </li>

            <li>
              <span>Đối tượng:</span>
              <strong>{{ nganh.doiTuongTuyenSinh || 'Tốt nghiệp THCS/THPT' }}</strong>
            </li>

            <li>
              <span>Thời gian:</span>
              <strong>Tuyển sinh liên tục</strong>
            </li>

            <li>
              <span>Hotline:</span>
              <strong>1900 633 088</strong>
            </li>
          </ul>

          <RouterLink :to="{ name: 'Public.TuyenSinh' }">
            Xem thông tin tuyển sinh
          </RouterLink>
        </div>

        <div class="side-card">
          <h3>Ngành liên quan</h3>

          <RouterLink
              v-for="item in nganhLienQuan"
              :key="item.id"
              :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }"
              class="related-item"
          >
            <img
                v-if="item.image"
                :src="item.image"
                :alt="item.title"
            />

            <div v-else class="related-placeholder">
              <i :class="item.icon || 'fa-solid fa-graduation-cap'"></i>
            </div>

            <span>{{ item.title }}</span>
          </RouterLink>
        </div>
      </aside>
    </section>
  </div>

  <div v-else class="public-container public-not-found">
    <i class="fa-solid fa-circle-exclamation"></i>
    <h1>Không tìm thấy ngành đào tạo</h1>
    <p>Ngành đào tạo này không tồn tại hoặc đã được gỡ khỏi danh sách hiển thị.</p>

    <RouterLink :to="{ name: 'Public.NganhDaoTao' }">
      Quay lại danh sách ngành đào tạo
    </RouterLink>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'
import {
  chiTietNganhDaoTaoTheoId,
  noiDungMacDinhChiTietNganh
} from '@/modules/public/data/chiTietNganhDaoTaoData.js'

const route = useRoute()

const { danhSachNganhDaoTao } = useNoiDungPublic()

const idNganh = computed(() => String(route.params.id || ''))

const danhSachNganhHienThi = computed(() => {
  return danhSachNganhDaoTao.value.filter((item) => item.trangThaiHienThi !== false)
})

const nganh = computed(() => {
  return danhSachNganhHienThi.value.find((item) => String(item.id) === idNganh.value) || null
})

const chiTiet = computed(() => {
  if (!nganh.value) {
    return noiDungMacDinhChiTietNganh
  }

  return chiTietNganhDaoTaoTheoId[nganh.value.id] || noiDungMacDinhChiTietNganh
})

const gioiThieuNganh = computed(() => {
  if (!nganh.value) {
    return ''
  }

  return chiTiet.value.gioiThieu || nganh.value.desc || 'Thông tin chi tiết ngành đào tạo đang được cập nhật.'
})

const nganhLienQuan = computed(() => {
  if (!nganh.value) {
    return []
  }

  return danhSachNganhHienThi.value
      .filter((item) => String(item.id) !== String(nganh.value.id) && item.category === nganh.value.category)
      .slice(0, 3)
})
</script>

<style scoped>
.chi-tiet-nganh-hero {
  padding: 56px 0 70px;
  background: linear-gradient(135deg, #eff6ff, #ffffff);
}

.chi-tiet-nganh-hero-grid {
  display: grid;
  grid-template-columns: 1fr 480px;
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

.nganh-category {
  display: inline-flex;
  margin-bottom: 14px;
  padding: 7px 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #075fc8;
  font-size: 13px;
  font-weight: 800;
}

.chi-tiet-nganh-hero h1 {
  margin: 0 0 18px;
  color: #063b7a;
  font-size: clamp(38px, 4vw, 58px);
  font-weight: 800;
  line-height: 1.15;
}

.chi-tiet-nganh-hero p {
  max-width: 760px;
  margin: 0 0 28px;
  color: #365979;
  font-size: 15px;
  line-height: 1.85;
}

.hero-actions-detail {
  display: flex;
  flex-wrap: wrap;
  gap: 14px;
}

.hero-actions-detail a {
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

.hero-actions-detail a:first-child {
  background: #075fc8;
  color: #ffffff;
}

.hero-actions-detail a:last-child {
  border: 1px solid #b7cae3;
  background: #ffffff;
  color: #063b7a;
}

.chi-tiet-nganh-image {
  overflow: hidden;
  border-radius: 24px;
  box-shadow: 0 18px 40px rgba(7, 63, 131, 0.16);
}

.chi-tiet-nganh-image img,
.chi-tiet-nganh-image-placeholder {
  width: 100%;
  height: 340px;
  display: grid;
  place-items: center;
}

.chi-tiet-nganh-image img {
  object-fit: cover;
}

.chi-tiet-nganh-image-placeholder {
  background: #eff6ff;
  color: #075fc8;
  font-size: 74px;
}

.chi-tiet-nganh-info {
  position: relative;
  z-index: 2;
  margin-top: -34px;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  overflow: hidden;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 14px 32px rgba(7, 63, 131, 0.12);
}

.chi-tiet-nganh-info article {
  display: grid;
  gap: 5px;
  padding: 22px;
  border-right: 1px solid #edf4fb;
}

.chi-tiet-nganh-info article:last-child {
  border-right: none;
}

.chi-tiet-nganh-info i {
  color: #075fc8;
  font-size: 24px;
}

.chi-tiet-nganh-info span {
  color: #64748b;
  font-size: 13px;
  font-weight: 700;
}

.chi-tiet-nganh-info strong {
  color: #063b7a;
  font-size: 16px;
  font-weight: 800;
}

.chi-tiet-nganh-content {
  display: grid;
  grid-template-columns: 1fr 360px;
  gap: 28px;
  padding-top: 48px;
  padding-bottom: 70px;
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

.detail-card ul,
.side-card ul {
  display: grid;
  gap: 14px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.detail-card li {
  display: flex;
  gap: 12px;
  color: #365979;
  line-height: 1.65;
}

.detail-card li i {
  margin-top: 5px;
  color: #075fc8;
}

.chuong-trinh-grid,
.viec-lam-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px;
}

.chuong-trinh-grid article,
.viec-lam-grid article {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px;
  border-radius: 14px;
  background: #f8fbff;
  color: #365979;
  font-weight: 600;
}

.chuong-trinh-grid i,
.viec-lam-grid i {
  color: #075fc8;
}

.side-card li {
  display: grid;
  gap: 4px;
  padding-bottom: 12px;
  border-bottom: 1px solid #edf4fb;
}

.side-card li span {
  color: #64748b;
  font-size: 13px;
}

.side-card li strong {
  color: #063b7a;
  font-size: 14px;
}

.side-card > a {
  min-height: 42px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  margin-top: 20px;
  border-radius: 10px;
  background: #075fc8;
  color: #ffffff;
  font-weight: 800;
  text-decoration: none;
}

.related-item {
  display: grid;
  grid-template-columns: 72px 1fr;
  gap: 12px;
  align-items: center;
  margin-bottom: 14px;
  color: #063b7a;
  font-weight: 800;
  text-decoration: none;
}

.related-item img,
.related-placeholder {
  width: 72px;
  height: 56px;
  border-radius: 10px;
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

@media (max-width: 1180px) {
  .chi-tiet-nganh-hero-grid,
  .chi-tiet-nganh-content {
    grid-template-columns: 1fr;
  }

  .chi-tiet-nganh-info {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .chi-tiet-nganh-info,
  .chuong-trinh-grid,
  .viec-lam-grid {
    grid-template-columns: 1fr;
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