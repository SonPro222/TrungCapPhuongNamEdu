<template>
  <section class="tin-tuc-section tin-tuc-layout-section">
    <div class="public-container tin-tuc-layout-grid">
      <div>
        <div class="tin-tuc-title-row left-title">
          <div>
            <h2>{{ tieuDeDanhSach }}</h2>
            <p>Hiển thị {{ danhSachHienTai.length }} / {{ tongSoTin }} bài viết</p>
          </div>
        </div>

        <div v-if="danhSachHienTai.length > 0" class="latest-news-grid">
          <article
              v-for="item in danhSachHienTai"
              :key="item.id"
              class="latest-news-card"
          >
            <div class="latest-news-image">
              <img :src="item.image" :alt="item.title" />

              <div class="news-date-badge small">
                <strong>{{ item.day }}</strong>
                <span>{{ item.monthYear }}</span>
              </div>

              <span class="latest-category" :class="layClassDanhMucTin(item.category)">
                {{ item.category }}
              </span>
            </div>

            <div class="latest-news-content">
              <h3>{{ item.title }}</h3>
              <p>{{ item.desc }}</p>

              <RouterLink :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }">
                Đọc thêm
                <i class="fa-solid fa-arrow-right"></i>
              </RouterLink>
            </div>
          </article>
        </div>

        <div v-else class="khong-co-tin">
          <i class="fa-regular fa-newspaper"></i>
          <h3>Không có bài viết</h3>
          <p>Danh mục này hiện chưa có bài viết phù hợp.</p>
        </div>

        <div v-if="tongSoTrang > 1" class="pagination-box">
          <button
              type="button"
              :disabled="trangHienTai === 1"
              @click="doiTrang(trangHienTai - 1)"
          >
            <i class="fa-solid fa-chevron-left"></i>
          </button>

          <button
              v-for="trang in tongSoTrang"
              :key="trang"
              type="button"
              :class="{ active: trangHienTai === trang }"
              @click="doiTrang(trang)"
          >
            {{ trang }}
          </button>

          <button
              type="button"
              :disabled="trangHienTai === tongSoTrang"
              @click="doiTrang(trangHienTai + 1)"
          >
            <i class="fa-solid fa-chevron-right"></i>
          </button>
        </div>
      </div>

      <aside class="news-sidebar">
        <section class="sidebar-box">
          <div class="sidebar-title">
            <i class="fa-solid fa-bullhorn"></i>
            <h3>Thông báo mới</h3>
          </div>

          <ul class="announcement-list">
            <li v-for="item in thongBaoMoi" :key="item.title">
              <div>
                <strong>{{ item.day }}</strong>
                <span>{{ item.year }}</span>
              </div>

              <p>{{ item.title }}</p>
            </li>
          </ul>

          <a href="#" class="sidebar-more">
            Xem tất cả
            <i class="fa-solid fa-arrow-right"></i>
          </a>
        </section>

        <section class="sidebar-box" id="su-kien">
          <div class="sidebar-title">
            <i class="fa-regular fa-calendar-days"></i>
            <h3>Sự kiện sắp diễn ra</h3>
          </div>

          <div class="event-list">
            <article v-for="item in suKienSapDienRa" :key="item.title">
              <div class="event-date">
                <strong>{{ item.day }}</strong>
                <span>{{ item.monthYear }}</span>
              </div>

              <div>
                <h4>{{ item.title }}</h4>
                <p>
                  <i class="fa-regular fa-clock"></i>
                  {{ item.time }}
                </p>
                <p>
                  <i class="fa-solid fa-location-dot"></i>
                  {{ item.location }}
                </p>
              </div>
            </article>
          </div>

          <a href="#" class="sidebar-event-more">
            Xem tất cả sự kiện
            <i class="fa-solid fa-arrow-right"></i>
          </a>
        </section>
      </aside>
    </div>
  </section>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { danhSachTinTuc } from '@/modules/public/data/tinTucData.js'

const props = defineProps({
  danhMucDangChon: {
    type: String,
    default: 'Tất cả'
  }
})

const soBaiMoiTrang = 6
const trangHienTai = ref(1)

const danhSachDaLoc = computed(() => {
  if (props.danhMucDangChon === 'Tất cả') {
    return danhSachTinTuc
  }

  return danhSachTinTuc.filter((item) => item.category === props.danhMucDangChon)
})

const tongSoTin = computed(() => danhSachDaLoc.value.length)

const tongSoTrang = computed(() => {
  return Math.ceil(danhSachDaLoc.value.length / soBaiMoiTrang)
})

const danhSachHienTai = computed(() => {
  const batDau = (trangHienTai.value - 1) * soBaiMoiTrang
  const ketThuc = batDau + soBaiMoiTrang

  return danhSachDaLoc.value.slice(batDau, ketThuc)
})

const tieuDeDanhSach = computed(() => {
  if (props.danhMucDangChon === 'Tất cả') {
    return 'Tin tức mới nhất'
  }

  return props.danhMucDangChon
})

watch(
    () => props.danhMucDangChon,
    () => {
      trangHienTai.value = 1
    }
)

const doiTrang = (trang) => {
  if (trang < 1 || trang > tongSoTrang.value) {
    return
  }

  trangHienTai.value = trang

  window.scrollTo({
    top: 520,
    behavior: 'smooth'
  })
}

const thongBaoMoi = [
  {
    day: '07/05',
    year: '2024',
    title: 'Thông báo nghỉ lễ 30/4 và 01/5 năm 2024'
  },
  {
    day: '26/04',
    year: '2024',
    title: 'Thông báo học phí học kỳ II năm 2023 – 2024'
  },
  {
    day: '08/04',
    year: '2024',
    title: 'Thông báo lịch thi kết thúc học phần học kỳ II'
  },
  {
    day: '30/03',
    year: '2024',
    title: 'Thông báo khảo sát đánh giá chất lượng đào tạo'
  }
]

const suKienSapDienRa = [
  {
    day: '15',
    monthYear: '05/2024',
    title: 'Ngày hội việc làm 2024',
    time: '08:00 – 16:00',
    location: 'Hội trường lớn – Cơ sở 1'
  },
  {
    day: '22',
    monthYear: '05/2024',
    title: 'Tọa đàm xu hướng nghề nghiệp trong thời đại AI',
    time: '13:30 – 16:00',
    location: 'Hội trường A – Cơ sở 1'
  },
  {
    day: '05',
    monthYear: '06/2024',
    title: 'Lễ tổng kết năm học 2023 - 2024',
    time: '08:00 – 11:00',
    location: 'Hội trường lớn – Cơ sở 1'
  }
]

const layClassDanhMucTin = (category) => {
  const classes = {
    'Thông báo': 'news-category-gray',
    'Sự kiện': 'news-category-blue',
    'Tuyển sinh': 'news-category-orange',
    'Hoạt động sinh viên': 'news-category-green',
    'Hợp tác doanh nghiệp': 'news-category-purple'
  }

  return classes[category] || 'news-category-gray'
}
</script>

<style scoped>
.tin-tuc-section {
  padding: 28px 0 42px;
  background: #ffffff;
}

.tin-tuc-layout-grid {
  display: grid;
  grid-template-columns: 8fr 4fr;
  gap: 36px;
}

.tin-tuc-title-row {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 26px;
}

.tin-tuc-title-row h2 {
  margin: 0;
  color: #063b7a;
  font-size: 22px;
  font-weight: 900;
}

.tin-tuc-title-row p {
  margin: 6px 0 0;
  color: #607894;
  font-size: 13px;
  font-weight: 800;
}

.latest-news-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 18px;
}

.latest-news-card {
  overflow: hidden;
  border: 1px solid #e6eef8;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
  transition: 0.25s;
}

.latest-news-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 14px 30px rgba(7, 63, 131, 0.1);
}

.latest-news-image {
  position: relative;
  overflow: hidden;
  height: 160px;
}

.latest-news-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.news-date-badge {
  position: absolute;
  top: 0;
  left: 0;
  min-width: 46px;
  padding: 6px 5px;
  border-radius: 0 0 12px 0;
  background: #063b7a;
  color: #ffffff;
  text-align: center;
}

.news-date-badge strong {
  display: block;
  font-size: 15px;
}

.news-date-badge span {
  font-size: 8px;
}

.latest-category {
  position: absolute;
  left: 10px;
  bottom: 10px;
  display: inline-flex;
  padding: 5px 9px;
  border-radius: 6px;
  background: rgba(255, 255, 255, 0.92);
  font-size: 9px;
  font-weight: 900;
  box-shadow: 0 4px 12px rgba(15, 23, 42, 0.12);
}

.latest-news-content {
  display: flex;
  flex-direction: column;
  min-height: 174px;
  padding: 16px;
}

.latest-news-content h3 {
  margin: 0 0 9px;
  color: #111827;
  font-size: 14px;
  line-height: 1.4;
  font-weight: 900;
}

.latest-news-content p {
  flex: 1;
  margin: 0 0 14px;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

.latest-news-content a {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  color: #075fc8;
  font-size: 12px;
  font-weight: 900;
  text-decoration: none;
}

.news-category-gray {
  color: #475569;
}

.news-category-blue {
  color: #075fc8;
}

.news-category-orange {
  color: #ea580c;
}

.news-category-green {
  color: #16a34a;
}

.news-category-purple {
  color: #7e22ce;
}

.khong-co-tin {
  padding: 54px 24px;
  border: 1px dashed #cbd5e1;
  border-radius: 18px;
  background: #f8fbff;
  text-align: center;
}

.khong-co-tin i {
  color: #94a3b8;
  font-size: 34px;
  margin-bottom: 14px;
}

.khong-co-tin h3 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 20px;
  font-weight: 900;
}

.khong-co-tin p {
  margin: 0;
  color: #607894;
  font-size: 14px;
}

.pagination-box {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  margin-top: 34px;
}

.pagination-box button {
  min-width: 36px;
  height: 36px;
  border: 1px solid transparent;
  border-radius: 9px;
  background: #ffffff;
  color: #607894;
  font-weight: 900;
  cursor: pointer;
}

.pagination-box button.active {
  background: #075fc8;
  color: #ffffff;
}

.pagination-box button:hover:not(:disabled) {
  border-color: #dbe9f8;
  color: #075fc8;
}

.pagination-box button:disabled {
  opacity: 0.45;
  cursor: not-allowed;
}

.news-sidebar {
  display: grid;
  gap: 28px;
  align-content: start;
}

.sidebar-box {
  padding: 22px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #f8fbff;
  box-shadow: 0 8px 22px rgba(7, 63, 131, 0.05);
}

.sidebar-title {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 14px;
  margin-bottom: 18px;
  border-bottom: 1px solid #e5e7eb;
}

.sidebar-title i {
  color: #075fc8;
  font-size: 19px;
}

.sidebar-title h3 {
  margin: 0;
  color: #063b7a;
  font-size: 16px;
  font-weight: 900;
}

.announcement-list {
  display: grid;
  gap: 16px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.announcement-list li {
  display: grid;
  grid-template-columns: 52px 1fr;
  gap: 14px;
}

.announcement-list strong {
  display: block;
  color: #111827;
  font-size: 13px;
  font-weight: 900;
}

.announcement-list span {
  color: #64748b;
  font-size: 10px;
}

.announcement-list p {
  margin: 0;
  padding-left: 14px;
  border-left: 1px solid #dbe9f8;
  color: #365979;
  font-size: 13px;
  line-height: 1.45;
}

.sidebar-more,
.sidebar-event-more {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 900;
  text-decoration: none;
}

.sidebar-more {
  justify-content: flex-end;
  width: 100%;
  margin-top: 18px;
}

.event-list {
  display: grid;
  gap: 20px;
}

.event-list article {
  display: grid;
  grid-template-columns: 54px 1fr;
  gap: 14px;
}

.event-date {
  width: 54px;
  height: 60px;
  display: grid;
  place-items: center;
  align-content: center;
  border-radius: 12px;
  background: #063b7a;
  color: #ffffff;
}

.event-date strong {
  font-size: 19px;
  line-height: 1;
}

.event-date span {
  margin-top: 4px;
  font-size: 9px;
}

.event-list h4 {
  margin: 0 0 8px;
  color: #111827;
  font-size: 13px;
  line-height: 1.35;
  font-weight: 900;
}

.event-list p {
  margin: 0 0 5px;
  color: #607894;
  font-size: 11px;
}

.event-list p i {
  width: 14px;
  color: #075fc8;
}

.sidebar-event-more {
  justify-content: center;
  width: 100%;
  min-height: 40px;
  margin-top: 20px;
  border: 1px solid #dbe9f8;
  border-radius: 10px;
  background: #ffffff;
}

@media (max-width: 1180px) {
  .tin-tuc-layout-grid {
    grid-template-columns: 1fr;
  }

  .latest-news-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .tin-tuc-title-row {
    align-items: flex-start;
    flex-direction: column;
  }

  .latest-news-grid {
    grid-template-columns: 1fr;
  }
}
</style>