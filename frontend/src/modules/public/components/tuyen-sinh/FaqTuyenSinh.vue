<template>
  <section class="tuyen-sinh-section faq-tuyen-sinh-section">
    <div class="public-container faq-tuyen-sinh-container">
      <div class="tuyen-sinh-section-title">
        <span></span>
        <h2>Câu hỏi thường gặp</h2>
        <span></span>
      </div>

      <div class="faq-tuyen-sinh-grid">
        <article v-for="item in cauHoiThuongGap" :key="item" class="faq-tuyen-sinh-item">
          <span>{{ item }}</span>
          <i class="fa-solid fa-plus"></i>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const cauHoiThuongGap = [
  'Thời gian xét tuyển và nhập học như thế nào?',
  'Sau khi tốt nghiệp có được giới thiệu việc làm không?',
  'Có thể đăng ký nhiều ngành cùng lúc không?',
  'Hồ sơ nộp online có cần bản công chứng không?',
  'Học phí có thay đổi trong quá trình học không?',
  'Chính sách miễn giảm học phí gồm những đối tượng nào?'
]
</script>

<style scoped>
.tuyen-sinh-section {
  padding: 34px 0;
  background: #ffffff;
}

.faq-tuyen-sinh-container {
  max-width: 980px;
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

.faq-tuyen-sinh-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  column-gap: 46px;
}

.faq-tuyen-sinh-item {
  min-height: 58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  border-bottom: 1px solid #e5e7eb;
  color: #374151;
  cursor: pointer;
}

.faq-tuyen-sinh-item:hover span,
.faq-tuyen-sinh-item:hover i {
  color: #075fc8;
}

.faq-tuyen-sinh-item span {
  font-size: 13px;
  font-weight: 800;
}

.faq-tuyen-sinh-item i {
  color: #075fc8;
  font-size: 12px;
}

@media (max-width: 760px) {
  .faq-tuyen-sinh-grid {
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