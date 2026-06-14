<template>
  <section class="faq-section">
    <div class="public-container faq-container">
      <div class="nganh-section-title">
        <span></span>
        <h2>Câu hỏi thường gặp</h2>
        <span></span>
      </div>

      <div class="faq-grid">
        <article
            v-for="item in cauHoiThuongGap"
            :key="item"
            class="faq-item"
        >
          <span>{{ item }}</span>
          <i class="fa-solid fa-plus"></i>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const cauHoiThuongGap = [
  'Thời gian đào tạo của các ngành là bao lâu?',
  'Hồ sơ đăng ký xét tuyển gồm những gì?',
  'Học phí tại Trường Trung cấp Phương Nam như thế nào?',
  'Nhà trường có chính sách hỗ trợ tài chính/học bổng không?',
  'Sinh viên có được thực tập tại doanh nghiệp không?'
]
</script>

<style scoped>
.faq-section {
  padding: 64px 0;
  background: #ffffff;
}

.faq-container {
  max-width: 980px;
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

.faq-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0 40px;
}

.faq-item {
  min-height: 58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  border-bottom: 1px solid #e5e7eb;
  color: #374151;
  cursor: pointer;
}

.faq-item:hover span,
.faq-item:hover i {
  color: #075fc8;
}

.faq-item span {
  font-size: 13px;
  font-weight: 800;
}

.faq-item i {
  color: #94a3b8;
  font-size: 12px;
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

  .faq-grid {
    grid-template-columns: 1fr;
  }
}
</style>