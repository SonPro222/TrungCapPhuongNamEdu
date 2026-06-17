<template>
  <section class="tin-tuc-stats-overlap">
    <div class="public-container">
      <div class="tin-tuc-stats-box">
        <article v-for="item in thongKeTinTuc" :key="item.title">
          <div>
            <i :class="item.icon"></i>
          </div>

          <h3>{{ item.number }}</h3>
          <p>{{ item.title }}</p>
          <small>{{ item.desc }}</small>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const thongKeTinTuc = [
  {
    icon: 'fa-regular fa-newspaper',
    number: '128+',
    title: 'Tin tức mới',
    desc: 'Trong 30 ngày qua'
  },
  {
    icon: 'fa-regular fa-calendar-check',
    number: '18+',
    title: 'Sự kiện',
    desc: 'Sắp diễn ra'
  },
  {
    icon: 'fa-solid fa-graduation-cap',
    number: '30+',
    title: 'Ngành đào tạo',
    desc: 'Đa dạng lĩnh vực'
  },
  {
    icon: 'fa-regular fa-handshake',
    number: '20+',
    title: 'Đối tác',
    desc: 'Doanh nghiệp đồng hành'
  },
  {
    icon: 'fa-solid fa-users',
    number: '200+',
    title: 'Hoạt động sinh viên',
    desc: 'Năng động - Sáng tạo'
  }
]
</script>

<style scoped>
.tin-tuc-stats-overlap {
  position: relative;
  z-index: 4;
  margin-top: -76px;
  margin-bottom: 50px;
}

.tin-tuc-stats-box {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  overflow: hidden;
  border: 1px solid #e6eef8;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 18px 42px rgba(7, 63, 131, 0.12);
}

.tin-tuc-stats-box article {
  min-height: 120px;
  display: grid;
  place-items: center;
  padding: 18px;
  text-align: center;
  border-right: 1px solid #edf4fb;
}

.tin-tuc-stats-box article:last-child {
  border-right: none;
}

.tin-tuc-stats-box article > div {
  width: 52px;
  height: 52px;
  display: grid;
  place-items: center;
  margin-bottom: 8px;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 25px;
}

.tin-tuc-stats-box h3 {
  margin: 0 0 5px;
  color: #063b7a;
  font-size: 22px;
  font-weight: 900;
}

.tin-tuc-stats-box p {
  margin: 0 0 3px;
  color: #365979;
  font-size: 12px;
  font-weight: 900;
}

.tin-tuc-stats-box small {
  color: #94a3b8;
  font-size: 10px;
}

@media (max-width: 1180px) {
  .tin-tuc-stats-box {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .tin-tuc-stats-overlap {
    margin-top: -58px;
  }

  .tin-tuc-stats-box {
    grid-template-columns: 1fr;
  }

  .tin-tuc-stats-box article {
    border-right: none;
    border-bottom: 1px solid #edf4fb;
  }
}
</style>