<template>
  <section class="home-process-section">
    <div class="public-container home-process-grid">
      <div class="home-process-left">
        <h2>Quy trình xét tuyển</h2>
        <p>Đơn giản – Minh bạch – Nhanh chóng</p>

        <RouterLink :to="{ name: 'Public.TuyenSinh' }" class="process-main-button">
          Đăng ký xét tuyển ngay
          <i class="fa-solid fa-arrow-right"></i>
        </RouterLink>
      </div>

      <div class="home-process-right">
        <div class="process-line"></div>

        <div class="process-list">
          <article
              v-for="(item, index) in quyTrinh"
              :key="item.title"
              class="process-item"
          >
            <div class="process-icon-wrap">
              <span class="process-number">
                {{ String(index + 1).padStart(2, '0') }}
              </span>

              <div class="process-icon">
                <i :class="item.icon"></i>
              </div>
            </div>

            <h3>{{ item.title }}</h3>
            <p>{{ item.desc }}</p>
          </article>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const quyTrinh = [
  {
    title: 'Đăng ký',
    desc: 'Điền thông tin đăng ký trực tuyến hoặc tại trường',
    icon: 'fa-solid fa-pen-to-square'
  },
  {
    title: 'Xét hồ sơ',
    desc: 'Nhà trường tiếp nhận và xét duyệt hồ sơ',
    icon: 'fa-solid fa-folder-open'
  },
  {
    title: 'Nhập học',
    desc: 'Nhận thông báo trúng tuyển và hoàn tất thủ tục',
    icon: 'fa-solid fa-file-signature'
  },
  {
    title: 'Bắt đầu học',
    desc: 'Tham gia học tập và trải nghiệm tại trường',
    icon: 'fa-solid fa-graduation-cap'
  }
]
</script>

<style scoped>
.home-process-section {
  padding: 42px 0;
  background: #ffffff;
}

.home-process-grid {
  display: grid;
  grid-template-columns: 260px 1fr;
  gap: 34px;
  align-items: center;
}

.home-process-left h2 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 26px;
  line-height: 1.25;
  font-weight: 800;
}

.home-process-left p {
  margin: 0 0 22px;
  color: #607894;
  font-size: 14px;
  line-height: 1.6;
}

.process-main-button {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  gap: 10px;
  border-radius: 9px;
  background: #075fc8;
  color: #ffffff;
  padding: 0 22px;
  font-size: 14px;
  font-weight: 800;
  text-decoration: none;
  box-shadow: 0 10px 24px rgba(7, 95, 200, 0.24);
}

.home-process-right {
  position: relative;
}

.process-line {
  position: absolute;
  top: 48px;
  left: 10%;
  right: 10%;
  z-index: 0;
  height: 2px;
  background-image: linear-gradient(to right, #cbd5e1 50%, transparent 50%);
  background-size: 10px 2px;
  background-repeat: repeat-x;
}

.process-list {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 26px;
}

.process-item {
  text-align: center;
}

.process-icon-wrap {
  position: relative;
  width: 64px;
  height: 64px;
  margin: 0 auto 16px;
}

.process-number {
  position: absolute;
  top: -10px;
  left: -10px;
  z-index: 3;
  width: 30px;
  height: 30px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font-size: 12px;
  font-weight: 900;
  box-shadow: 0 8px 18px rgba(7, 95, 200, 0.24);
}

.process-icon {
  position: relative;
  z-index: 2;
  width: 64px;
  height: 64px;
  display: grid;
  place-items: center;
  border: 1px solid #dbeafe;
  border-radius: 16px;
  background: #ffffff;
  color: #075fc8;
  font-size: 27px;
  box-shadow: 0 8px 20px rgba(7, 63, 131, 0.06);
}

.process-item h3 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 15px;
  font-weight: 800;
}

.process-item p {
  max-width: 170px;
  margin: 0 auto;
  color: #607894;
  font-size: 13px;
  line-height: 1.65;
}

@media (max-width: 1180px) {
  .home-process-grid {
    grid-template-columns: 1fr;
  }

  .home-process-left {
    text-align: center;
  }

  .process-list {
    grid-template-columns: repeat(2, 1fr);
    gap: 34px 26px;
  }

  .process-line {
    display: none;
  }
}

@media (max-width: 760px) {
  .home-process-section {
    padding: 34px 0;
  }

  .process-list {
    grid-template-columns: 1fr;
  }

  .home-process-left h2 {
    font-size: 22px;
  }

  .process-main-button {
    width: 100%;
    justify-content: center;
  }
}
</style>