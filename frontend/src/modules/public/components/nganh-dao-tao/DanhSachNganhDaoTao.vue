<template>
  <section class="nganh-section">
    <div class="public-container">
      <div class="nganh-section-title">
        <span></span>
        <h2>{{ tieuDeDanhSach }}</h2>
        <span></span>
      </div>

      <div v-if="danhSachNganhLoc.length > 0" class="nganh-card-grid">
        <article
            v-for="item in danhSachNganhLoc"
            :key="item.id"
            class="nganh-card"
        >
          <div class="nganh-card-image">
            <img :src="item.image" :alt="item.title" />
          </div>

          <div class="nganh-card-content">
            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>

            <div class="nganh-card-meta">
              <span>Thời gian đào tạo: {{ item.duration }}</span>
              <strong :class="layClassDanhMuc(item.category)">
                {{ layTenNganDanhMuc(item.category) }}
              </strong>
            </div>

            <RouterLink :to="{ name: 'Public.ChiTietNganhDaoTao', params: { id: item.id } }">
              Xem chi tiết
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>
          </div>
        </article>
      </div>

      <div v-else class="khong-co-nganh">
        <i class="fa-solid fa-magnifying-glass"></i>
        <h3>Không tìm thấy ngành phù hợp</h3>
        <p>Vui lòng chọn danh mục khác hoặc đổi từ khóa tìm kiếm.</p>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'
import { danhSachNganhDaoTao } from '@/modules/public/data/nganhDaoTaoData.js'

const props = defineProps({
  danhMucDangChon: {
    type: String,
    default: 'Tất cả ngành'
  },
  tuKhoa: {
    type: String,
    default: ''
  }
})

const danhSachNganhLoc = computed(() => {
  const tuKhoaChuanHoa = props.tuKhoa.trim().toLowerCase()

  return danhSachNganhDaoTao.filter((item) => {
    const dungDanhMuc =
        props.danhMucDangChon === 'Tất cả ngành' ||
        item.category === props.danhMucDangChon

    const dungTuKhoa =
        tuKhoaChuanHoa === '' ||
        item.title.toLowerCase().includes(tuKhoaChuanHoa) ||
        item.category.toLowerCase().includes(tuKhoaChuanHoa) ||
        item.desc.toLowerCase().includes(tuKhoaChuanHoa)

    return dungDanhMuc && dungTuKhoa
  })
})

const tieuDeDanhSach = computed(() => {
  if (props.danhMucDangChon === 'Tất cả ngành') {
    return 'Các ngành đào tạo nổi bật'
  }

  return props.danhMucDangChon
})

const layClassDanhMuc = (item) => {
  const classes = {
    'Khối sức khỏe': 'badge-health',
    'Kinh tế - Dịch vụ': 'badge-business',
    'Công nghệ - Kỹ thuật': 'badge-tech'
  }

  return classes[item] || 'badge-default'
}

const layTenNganDanhMuc = (item) => {
  const names = {
    'Khối sức khỏe': 'Sức khỏe',
    'Kinh tế - Dịch vụ': 'Kinh tế',
    'Công nghệ - Kỹ thuật': 'Kỹ thuật'
  }

  return names[item] || item
}
</script>

<style scoped>
.nganh-section {
  padding: 64px 0;
  background: #ffffff;
}

.nganh-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 18px;
  margin-bottom: 42px;
  text-align: center;
}

.nganh-section-title span {
  width: 92px;
  height: 1px;
  background: #cbd5e1;
}

.nganh-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 27px;
  font-weight: 900;
}

.nganh-card-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
}

.nganh-card {
  overflow: hidden;
  display: flex;
  flex-direction: column;
  border: 1px solid #e6eef8;
  border-radius: 18px;
  background: #ffffff;
  transition: 0.25s;
}

.nganh-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 16px 34px rgba(7, 63, 131, 0.11);
}

.nganh-card-image {
  height: 175px;
  overflow: hidden;
}

.nganh-card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: 0.35s;
}

.nganh-card:hover .nganh-card-image img {
  transform: scale(1.06);
}

.nganh-card-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 22px;
}

.nganh-card-content h3 {
  margin: 0 0 10px;
  color: #063b7a;
  font-size: 17px;
  font-weight: 900;
}

.nganh-card-content p {
  flex: 1;
  margin: 0 0 16px;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

.nganh-card-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 16px;
}

.nganh-card-meta span {
  color: #64748b;
  font-size: 11px;
  font-weight: 700;
}

.nganh-card-meta strong {
  white-space: nowrap;
  border-radius: 999px;
  border: 1px solid;
  padding: 4px 8px;
  font-size: 11px;
  font-weight: 900;
}

.badge-health {
  border-color: #22c55e;
  background: #f0fdf4;
  color: #16a34a;
}

.badge-business {
  border-color: #fb923c;
  background: #fff7ed;
  color: #ea580c;
}

.badge-tech {
  border-color: #60a5fa;
  background: #eff6ff;
  color: #2563eb;
}

.badge-default {
  border-color: #cbd5e1;
  background: #f8fafc;
  color: #475569;
}

.nganh-card-content a {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
  text-decoration: none;
}

.khong-co-nganh {
  padding: 54px 24px;
  border: 1px dashed #cbd5e1;
  border-radius: 18px;
  background: #f8fbff;
  text-align: center;
}

.khong-co-nganh i {
  color: #94a3b8;
  font-size: 34px;
  margin-bottom: 14px;
}

.khong-co-nganh h3 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 20px;
  font-weight: 900;
}

.khong-co-nganh p {
  margin: 0;
  color: #607894;
  font-size: 14px;
}

@media (max-width: 1180px) {
  .nganh-card-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .nganh-section-title {
    gap: 12px;
  }

  .nganh-section-title span {
    width: 36px;
  }

  .nganh-section-title h2 {
    font-size: 22px;
  }

  .nganh-card-grid {
    grid-template-columns: 1fr;
  }
}
</style>