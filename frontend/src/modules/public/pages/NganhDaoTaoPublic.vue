<template>
  <main class="nganh-dao-tao-page">
    <BannerTrangConPublic
        ten-trang="Ngành đào tạo"
        nhan="Ngành đào tạo"
        tieu-de="Chọn ngành học phù hợp với định hướng tương lai"
        mo-ta="Khám phá các ngành đào tạo đang được Trường Trung cấp Phương Nam tuyển sinh và tổ chức đào tạo."
        anh-nen="https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=1400&q=90"
    >
      <template #actions>
        <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="primary-button">
          Xem tuyển sinh
          <i class="fa-solid fa-arrow-right"></i>
        </RouterLink>

        <RouterLink :to="{ name: 'Public.LienHe' }" class="secondary-button">
          Tư vấn chọn ngành
          <i class="fa-solid fa-headset"></i>
        </RouterLink>
      </template>
    </BannerTrangConPublic>

    <section class="public-section">
      <div class="public-container">
        <div class="filter-card">
          <label>
            <span>Tìm kiếm ngành</span>
            <input
                v-model.trim="tuKhoa"
                type="text"
                placeholder="Nhập tên ngành hoặc khối ngành"
            />
          </label>

          <label>
            <span>Khối ngành</span>
            <select v-model="khoiNganhDangChon">
              <option value="tat-ca">Tất cả khối ngành</option>
              <option
                  v-for="item in danhSachKhoiNganh"
                  :key="item"
                  :value="item"
              >
                {{ item }}
              </option>
            </select>
          </label>
        </div>

        <div class="nganh-grid">
          <RouterLink
              v-for="item in danhSachDaLoc"
              :key="item.id"
              :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }"
              class="nganh-card"
          >
            <div class="nganh-image">
              <img
                  v-if="item.image"
                  :src="item.image"
                  :alt="item.title"
              />

              <div v-else class="nganh-image-placeholder">
                <i :class="item.icon || 'fa-solid fa-graduation-cap'"></i>
              </div>
            </div>

            <div class="nganh-content">
              <span>{{ item.category }}</span>
              <h2>{{ item.title }}</h2>
              <p>{{ item.desc }}</p>

              <div class="nganh-meta">
                <small>
                  <i class="fa-solid fa-clock"></i>
                  {{ item.thoiGianDaoTao || 'Đang cập nhật' }}
                </small>

                <small>
                  <i class="fa-solid fa-user-graduate"></i>
                  {{ item.doiTuongTuyenSinh || 'THCS/THPT' }}
                </small>
              </div>

              <strong>
                Xem chi tiết
                <i class="fa-solid fa-arrow-right"></i>
              </strong>
            </div>
          </RouterLink>
        </div>

        <div v-if="!danhSachDaLoc.length" class="empty-state">
          <i class="fa-solid fa-circle-info"></i>
          <h2>Không có ngành đào tạo phù hợp</h2>
          <p>Vui lòng thử lại với từ khóa hoặc khối ngành khác.</p>
        </div>
      </div>
    </section>
  </main>
</template>

<script setup>
import { computed, ref } from 'vue'
import BannerTrangConPublic from '@/modules/public/components/dung-chung/BannerTrangConPublic.vue'
import { useNoiDungPublic } from '@/modules/public/composables/useNoiDungPublic.js'

const { danhSachNganhDaoTao } = useNoiDungPublic()

const tuKhoa = ref('')
const khoiNganhDangChon = ref('tat-ca')

const danhSachHienThi = computed(() => {
  return danhSachNganhDaoTao.value.filter((item) => item.trangThaiHienThi !== false)
})

const danhSachKhoiNganh = computed(() => {
  return [...new Set(danhSachHienThi.value.map((item) => item.category).filter(Boolean))]
})

const danhSachDaLoc = computed(() => {
  const keyword = tuKhoa.value.toLowerCase()

  return danhSachHienThi.value.filter((item) => {
    const dungTuKhoa =
        !keyword ||
        String(item.title || '').toLowerCase().includes(keyword) ||
        String(item.category || '').toLowerCase().includes(keyword) ||
        String(item.desc || '').toLowerCase().includes(keyword)

    const dungKhoiNganh =
        khoiNganhDangChon.value === 'tat-ca' ||
        item.category === khoiNganhDangChon.value

    return dungTuKhoa && dungKhoiNganh
  })
})
</script>

<style scoped>
.nganh-dao-tao-page {
  background: #ffffff;
}

.filter-card {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 260px;
  gap: 18px;
  margin-bottom: 28px;
  padding: 22px;
  border: 1px solid #dbe7f5;
  border-radius: 20px;
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
  color: #123b68;
  font: inherit;
}

.filter-card input:focus,
.filter-card select:focus {
  border-color: #075fc8;
  box-shadow: 0 0 0 3px rgba(7, 95, 200, 0.12);
}

.nganh-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 22px;
}

.nganh-card {
  overflow: hidden;
  border: 1px solid #dbe7f5;
  border-radius: 22px;
  background: #ffffff;
  color: inherit;
  text-decoration: none;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
  transition: 0.22s ease;
}

.nganh-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 16px 34px rgba(7, 63, 131, 0.12);
}

.nganh-image {
  height: 210px;
  overflow: hidden;
  background: #eff6ff;
}

.nganh-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.nganh-image-placeholder {
  height: 100%;
  display: grid;
  place-items: center;
  color: #075fc8;
  font-size: 58px;
}

.nganh-content {
  padding: 22px;
}

.nganh-content > span {
  display: inline-flex;
  margin-bottom: 10px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
}

.nganh-content h2 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 21px;
  line-height: 1.35;
  font-weight: 900;
}

.nganh-content p {
  display: -webkit-box;
  overflow: hidden;
  min-height: 68px;
  margin: 0 0 16px;
  color: #607894;
  font-size: 14px;
  line-height: 1.65;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}

.nganh-meta {
  display: grid;
  gap: 7px;
  margin-bottom: 16px;
}

.nganh-meta small {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #486581;
  font-size: 13px;
  font-weight: 800;
}

.nganh-meta i {
  color: #075fc8;
}

.nganh-content strong {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  color: #075fc8;
  font-size: 14px;
  font-weight: 900;
}

.empty-state {
  display: grid;
  place-items: center;
  gap: 10px;
  padding: 60px 0;
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
  .nganh-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .filter-card,
  .nganh-grid {
    grid-template-columns: 1fr;
  }
}
</style>