<template>
  <section class="tuyen-sinh-section">
    <div class="public-container">
      <div class="tuyen-sinh-section-title">
        <span></span>
        <h2>Phương thức tuyển sinh</h2>
        <span></span>
      </div>

      <div class="method-grid">
        <article v-for="item in phuongThucTuyenSinh" :key="item.title" class="method-card">
          <div class="method-icon">
            <i :class="item.icon"></i>
          </div>

          <div>
            <h4>{{ item.title }}</h4>
            <p>{{ item.desc }}</p>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const phuongThucTuyenSinh = [
  {
    icon: 'fa-regular fa-file-lines',
    title: 'Xét học bạ THCS/THPT',
    desc: 'Xét tuyển dựa trên kết quả học tập bậc THCS hoặc THPT theo quy định.'
  },
  {
    icon: 'fa-solid fa-bullseye',
    title: 'Tư vấn định hướng',
    desc: 'Được tư vấn ngành nghề phù hợp năng lực, sở thích và xu hướng thị trường.'
  },
  {
    icon: 'fa-solid fa-cloud-arrow-up',
    title: 'Nộp hồ sơ trực tiếp hoặc online',
    desc: 'Linh hoạt nộp hồ sơ tại trường hoặc qua hệ thống đăng ký online nhanh chóng.'
  },
  {
    icon: 'fa-solid fa-user-check',
    title: 'Nhập học nhanh',
    desc: 'Quy trình đơn giản, duyệt hồ sơ nhanh và hỗ trợ nhập học kịp thời.'
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

.method-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.method-card {
  display: flex;
  gap: 16px;
  padding: 22px;
  border: 1px solid #e6eef8;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
  transition: 0.25s;
}

.method-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 30px rgba(7, 63, 131, 0.1);
}

.method-icon {
  flex: 0 0 50px;
  width: 50px;
  height: 50px;
  display: grid;
  place-items: center;
  border: 1px solid #dbeafe;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 21px;
}

.method-card h4 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.method-card p {
  margin: 0;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

@media (max-width: 1180px) {
  .method-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .method-grid {
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