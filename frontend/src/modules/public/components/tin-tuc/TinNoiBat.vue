<template>
  <section class="tin-tuc-section">
    <div class="public-container">
      <div class="tin-tuc-title-row">
        <h2>Tin nổi bật</h2>

        <RouterLink :to="{ name: 'Public.TinTuc' }">
          Xem tất cả
          <i class="fa-solid fa-arrow-right"></i>
        </RouterLink>
      </div>

      <div class="featured-news-grid">
        <article class="featured-main-card">
          <div class="featured-main-image">
            <img :src="tinNoiBatChinh.image" :alt="tinNoiBatChinh.title" />

            <div class="news-date-badge">
              <strong>{{ tinNoiBatChinh.day }}</strong>
              <span>{{ tinNoiBatChinh.monthYear }}</span>
            </div>
          </div>

          <div class="featured-main-content">
            <div class="news-tag-list">
              <span>{{ tinNoiBatChinh.category }}</span>
              <span>Nổi bật</span>
            </div>

            <h3>{{ tinNoiBatChinh.title }}</h3>
            <p>{{ tinNoiBatChinh.desc }}</p>

            <RouterLink :to="{ name: 'Public.ChiTietTinTuc', params: { id: tinNoiBatChinh.id } }">
              Đọc thêm
              <i class="fa-solid fa-arrow-right"></i>
            </RouterLink>
          </div>
        </article>

        <div class="featured-side-list">
          <article
              v-for="item in tinNoiBatPhu"
              :key="item.id"
              class="featured-side-card"
          >
            <div class="featured-side-image">
              <img :src="item.image" :alt="item.title" />

              <div class="news-date-badge small">
                <strong>{{ item.day }}</strong>
                <span>{{ item.monthYear }}</span>
              </div>
            </div>

            <div>
              <span :class="layClassDanhMucTin(item.category)">
                {{ item.category }}
              </span>

              <h4>{{ item.title }}</h4>

              <RouterLink :to="{ name: 'Public.ChiTietTinTuc', params: { id: item.id } }">
                Đọc thêm
                <i class="fa-solid fa-arrow-right"></i>
              </RouterLink>
            </div>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const tinNoiBatChinh = {
  id: 1,
  day: '08',
  monthYear: '06/2024',
  category: 'Sự kiện',
  title: 'Lễ tốt nghiệp và trao bằng năm 2024 – Dấu mốc trưởng thành và thành công',
  desc: 'Buổi lễ trang trọng đánh dấu hành trình nỗ lực không ngừng của các tân học viên.',
  image: 'https://images.unsplash.com/photo-1523050854058-8df90110c9f1?auto=format&fit=crop&w=900&q=90'
}

const tinNoiBatPhu = [
  {
    id: 2,
    day: '02',
    monthYear: '05/2024',
    category: 'Tuyển sinh',
    title: 'Ngày hội Tư vấn tuyển sinh 2024 – Cơ hội định hướng tương lai',
    image: 'https://images.unsplash.com/photo-1540317580384-e5d43616b9aa?auto=format&fit=crop&w=400&q=90'
  },
  {
    id: 3,
    day: '25',
    monthYear: '04/2024',
    category: 'Hoạt động sinh viên',
    title: 'Sinh viên Phương Nam đạt giải cao tại cuộc thi Kỹ năng nghề',
    image: 'https://images.unsplash.com/photo-1523240795612-9a054b0db644?auto=format&fit=crop&w=400&q=90'
  },
  {
    id: 5,
    day: '18',
    monthYear: '04/2024',
    category: 'Hợp tác doanh nghiệp',
    title: 'Ký kết hợp tác với doanh nghiệp – Mở rộng cơ hội việc làm',
    image: 'https://images.unsplash.com/photo-1577415124269-fc1140a69e91?auto=format&fit=crop&w=400&q=90'
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

.tin-tuc-title-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 26px;
}

.tin-tuc-title-row h2 {
  margin: 0;
  color: #063b7a;
  font-size: 22px;
  font-weight: 800;
}

.tin-tuc-title-row a {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  color: #075fc8;
  font-size: 13px;
  font-weight: 800;
  text-decoration: none;
}

.featured-news-grid {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 26px;
}

.featured-main-image {
  position: relative;
  overflow: hidden;
  height: 350px;
  border-radius: 22px;
}

.featured-main-image img,
.featured-side-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: 0.45s;
}

.featured-main-card:hover img,
.featured-side-card:hover img {
  transform: scale(1.05);
}

.news-date-badge {
  position: absolute;
  top: 16px;
  left: 16px;
  min-width: 58px;
  padding: 9px 8px;
  border-radius: 12px;
  background: #063b7a;
  color: #ffffff;
  text-align: center;
  box-shadow: 0 8px 18px rgba(6, 59, 122, 0.24);
}

.news-date-badge strong {
  display: block;
  font-size: 22px;
  line-height: 1;
  font-weight: 900;
}

.news-date-badge span {
  display: block;
  margin-top: 4px;
  font-size: 10px;
}

.news-date-badge.small {
  top: 0;
  left: 0;
  min-width: 46px;
  padding: 6px 5px;
  border-top-right-radius: 0;
  border-bottom-left-radius: 0;
}

.news-date-badge.small strong {
  font-size: 15px;
}

.news-date-badge.small span {
  font-size: 8px;
}

.featured-main-content {
  padding: 20px 4px 0;
}

.news-tag-list {
  display: flex;
  gap: 8px;
  margin-bottom: 12px;
}

.news-tag-list span {
  padding: 5px 10px;
  border-radius: 6px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 10px;
  font-weight: 800;
}

.news-tag-list span:last-child {
  background: #fff7ed;
  color: #ea580c;
}

.featured-main-content h3 {
  margin: 0 0 12px;
  color: #111827;
  font-size: 25px;
  line-height: 1.3;
  font-weight: 800;
}

.featured-main-content p {
  margin: 0 0 16px;
  color: #607894;
  font-size: 13px;
  line-height: 1.65;
}

.featured-main-content a,
.featured-side-card a {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  color: #075fc8;
  font-size: 12px;
  font-weight: 800;
  text-decoration: none;
}

.featured-side-list {
  display: grid;
  gap: 18px;
}

.featured-side-card {
  display: grid;
  grid-template-columns: 120px 1fr;
  gap: 16px;
  align-items: center;
  padding: 12px;
  border: 1px solid #edf4fb;
  border-radius: 16px;
  background: #ffffff;
  transition: 0.25s;
}

.featured-side-card:hover {
  box-shadow: 0 10px 24px rgba(7, 63, 131, 0.08);
}

.featured-side-image {
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 92px;
  border-radius: 12px;
}

.featured-side-card span {
  display: inline-block;
  margin-bottom: 6px;
  font-size: 10px;
  font-weight: 800;
}

.featured-side-card h4 {
  margin: 0 0 8px;
  color: #111827;
  font-size: 13px;
  line-height: 1.35;
  font-weight: 800;
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

@media (max-width: 1180px) {
  .featured-news-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .tin-tuc-title-row {
    align-items: flex-start;
    flex-direction: column;
    gap: 10px;
  }

  .featured-main-image {
    height: 250px;
  }

  .featured-side-card {
    grid-template-columns: 1fr;
  }

  .featured-side-image {
    width: 100%;
    height: 170px;
  }
}
</style>