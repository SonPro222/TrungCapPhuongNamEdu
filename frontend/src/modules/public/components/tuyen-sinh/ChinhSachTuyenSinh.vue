<template>
  <section class="tuyen-sinh-section">
    <div class="public-container">
      <div class="tuyen-sinh-section-title">
        <span></span>
        <h2>Học phí – Học bổng – Chính sách hỗ trợ</h2>
        <span></span>
      </div>

      <div class="policy-grid">
        <article v-for="item in chinhSachHocPhi" :key="item.title" class="policy-card">
          <div>
            <i :class="item.icon"></i>
          </div>

          <div>
            <h4>{{ item.title }}</h4>
            <ul>
              <li v-for="point in item.points" :key="point">
                <span></span>
                {{ point }}
              </li>
            </ul>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const chinhSachHocPhi = [
  {
    icon: 'fa-solid fa-money-bill-wave',
    title: 'Học phí hợp lý',
    points: [
      'Học phí công khai, minh bạch',
      'Mức học phí phù hợp với điều kiện của người học',
      'Nhiều chính sách miễn giảm'
    ]
  },
  {
    icon: 'fa-solid fa-award',
    title: 'Học bổng hấp dẫn',
    points: [
      'Học bổng cho tân sinh viên xuất sắc',
      'Học bổng khuyến khích học tập',
      'Hỗ trợ học viên có hoàn cảnh khó khăn'
    ]
  },
  {
    icon: 'fa-solid fa-credit-card',
    title: 'Hỗ trợ trả góp',
    points: [
      'Hỗ trợ trả góp học phí linh hoạt',
      'Thủ tục đơn giản, xét duyệt nhanh',
      'Giúp học viên yên tâm theo học'
    ]
  }
]
</script>

<style scoped>
.tuyen-sinh-section {
  padding: 34px 0;
  background: #ffffff;
}

.tuyen-sinh-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 18px;
  margin-bottom: 34px;
  text-align: center;
}

.tuyen-sinh-section-title span {
  width: 88px;
  height: 1px;
  background: #cbd5e1;
}

.tuyen-sinh-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 25px;
  font-weight: 900;
}

.policy-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 22px;
}

.policy-card {
  display: flex;
  gap: 18px;
  padding: 26px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
  transition: 0.25s;
}

.policy-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 30px rgba(7, 63, 131, 0.1);
}

.policy-card > div:first-child {
  color: #075fc8;
  font-size: 32px;
}

.policy-card h4 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.policy-card ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

.policy-card li {
  position: relative;
  padding-left: 16px;
  margin-bottom: 8px;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

.policy-card li span {
  position: absolute;
  left: 0;
  top: 8px;
  width: 5px;
  height: 5px;
  border-radius: 999px;
  background: #075fc8;
}

@media (max-width: 1180px) {
  .policy-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .policy-grid {
    grid-template-columns: 1fr;
  }

  .tuyen-sinh-section-title {
    gap: 12px;
  }

  .tuyen-sinh-section-title span {
    width: 36px;
  }

  .tuyen-sinh-section-title h2 {
    font-size: 21px;
  }
}
</style>