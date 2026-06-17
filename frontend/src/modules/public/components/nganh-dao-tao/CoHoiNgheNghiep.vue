<template>
  <section class="nganh-section nganh-border-top">
    <div class="public-container">
      <div class="nganh-section-title">
        <span></span>
        <h2>Cơ hội nghề nghiệp sau tốt nghiệp</h2>
        <span></span>
      </div>

      <div class="career-list">
        <article
            v-for="item in coHoiNgheNghiep"
            :key="item.title"
            class="career-item"
        >
          <div>
            <i :class="item.icon"></i>
          </div>
          <h4 v-html="item.title"></h4>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const coHoiNgheNghiep = [
  {
    icon: 'fa-solid fa-briefcase',
    title: 'Doanh nghiệp<br>sản xuất & dịch vụ'
  },
  {
    icon: 'fa-solid fa-square-plus',
    title: 'Bệnh viện,<br>phòng khám'
  },
  {
    icon: 'fa-solid fa-capsules',
    title: 'Nhà thuốc,<br>công ty dược'
  },
  {
    icon: 'fa-regular fa-building',
    title: 'Khách sạn,<br>nhà hàng, resort'
  },
  {
    icon: 'fa-solid fa-chart-line',
    title: 'Văn phòng,<br>công ty tài chính'
  },
  {
    icon: 'fa-solid fa-code',
    title: 'Công ty<br>công nghệ'
  }
]
</script>

<style scoped>
.nganh-section {
  padding: 64px 0;
  background: #ffffff;
}

.nganh-border-top {
  border-top: 1px solid #f1f5f9;
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

.career-list {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  gap: 18px;
}

.career-item {
  min-width: 240px;
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
  border: 1px solid #dbe9f8;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 6px 18px rgba(7, 63, 131, 0.05);
  transition: 0.25s;
}

.career-item:hover {
  border-color: #bfdbfe;
  box-shadow: 0 10px 24px rgba(7, 63, 131, 0.09);
}

.career-item div {
  color: #063b7a;
  font-size: 28px;
}

.career-item h4 {
  margin: 0;
  color: #063b7a;
  font-size: 13px;
  line-height: 1.35;
  font-weight: 900;
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

  .career-item {
    width: 100%;
  }
}
</style>