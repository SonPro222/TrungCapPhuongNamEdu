<template>
  <section class="public-container contact-faq-section">
    <div class="section-title-contact">
      <div>
        <i class="fa-solid fa-comments"></i>
      </div>

      <div>
        <h3>Câu hỏi thường gặp</h3>
        <p>Những thắc mắc phổ biến từ học sinh và phụ huynh.</p>
      </div>
    </div>

    <div class="contact-faq-grid">
      <article v-for="(item, index) in cauHoiThuongGap" :key="item.question" class="contact-faq-item">
        <button type="button" @click="doiCauHoi(index)">
          <span>{{ item.question }}</span>
          <i :class="activeFaq === index ? 'fa-solid fa-chevron-up' : 'fa-solid fa-chevron-down'"></i>
        </button>

        <p v-if="activeFaq === index">
          {{ item.answer }}
        </p>
      </article>
    </div>
  </section>
</template>

<script setup>
import { ref } from 'vue'

const activeFaq = ref(null)

const cauHoiThuongGap = [
  {
    question: 'Giờ làm việc của trường như thế nào?',
    answer: 'Trường làm việc từ Thứ 2 đến Thứ 7, sáng từ 07:30 đến 11:30, chiều từ 13:00 đến 16:30.'
  },
  {
    question: 'Hồ sơ đăng ký xét tuyển gồm những gì?',
    answer: 'Hồ sơ gồm sơ yếu lý lịch, học bạ, bằng tốt nghiệp hoặc giấy chứng nhận tốt nghiệp tạm thời, CCCD và ảnh 3x4.'
  },
  {
    question: 'Trường có hỗ trợ tư vấn trực tuyến không?',
    answer: 'Có. Nhà trường hỗ trợ tư vấn trực tuyến qua Zalo, Fanpage Facebook hoặc Hotline 1900 633 088.'
  },
  {
    question: 'Làm thế nào để theo dõi tiến trình hồ sơ?',
    answer: 'Sau khi nộp hồ sơ, bạn sẽ được tư vấn viên hỗ trợ kiểm tra và cập nhật tiến trình xử lý hồ sơ.'
  }
]

const doiCauHoi = (index) => {
  activeFaq.value = activeFaq.value === index ? null : index
}
</script>

<style scoped>
.contact-faq-section {
  margin-bottom: 46px;
}

.section-title-contact {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 24px;
}

.section-title-contact > div:first-child {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  border-radius: 8px;
  background: #eff6ff;
  color: #075fc8;
}

.section-title-contact h3 {
  margin: 0;
  color: #1e293b;
  font-size: 22px;
  font-weight: 900;
}

.section-title-contact p {
  margin: 3px 0 0;
  color: #64748b;
  font-size: 13px;
}

.contact-faq-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 14px 28px;
}

.contact-faq-item {
  overflow: hidden;
  border: 1px solid #e2e8f0;
  border-radius: 13px;
  background: #ffffff;
}

.contact-faq-item button {
  width: 100%;
  min-height: 58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  border: none;
  background: #ffffff;
  color: #1e293b;
  padding: 0 18px;
  text-align: left;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
}

.contact-faq-item button i {
  color: #075fc8;
}

.contact-faq-item p {
  margin: 0;
  padding: 16px 18px;
  border-top: 1px solid #edf2f7;
  background: #f8fafc;
  color: #64748b;
  font-size: 13px;
  line-height: 1.6;
}

@media (max-width: 760px) {
  .contact-faq-grid {
    grid-template-columns: 1fr;
  }
}
</style>