<template>
  <section class="nganh-section nganh-border-top">
    <div class="public-container">
      <div class="nganh-section-title">
        <span></span>
        <h2>Vì sao chọn học tại Phương Nam?</h2>
        <span></span>
      </div>

      <div class="why-grid">
        <article
            v-for="item in lyDoChonTruong"
            :key="item.title"
            class="why-card"
        >
          <div>
            <i :class="item.icon"></i>
          </div>
          <h4>{{ item.title }}</h4>
          <p>{{ item.desc }}</p>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const lyDoChonTruong = [
  {
    icon: 'fa-solid fa-users',
    title: 'Giảng viên giàu kinh nghiệm',
    desc: 'Đội ngũ giảng viên tận tâm, chuyên môn cao, gắn bó với thực tiễn nghề nghiệp.'
  },
  {
    icon: 'fa-regular fa-building',
    title: 'Cơ sở vật chất hiện đại',
    desc: 'Phòng học, phòng lab, xưởng thực hành đạt chuẩn, trang thiết bị tiên tiến.'
  },
  {
    icon: 'fa-regular fa-handshake',
    title: 'Liên kết doanh nghiệp',
    desc: 'Liên kết hơn 200+ doanh nghiệp tạo cơ hội thực tập và việc làm cho sinh viên.'
  },
  {
    icon: 'fa-solid fa-briefcase',
    title: 'Hỗ trợ việc làm',
    desc: 'Hỗ trợ giới thiệu việc làm sau tốt nghiệp với mức thu nhập hấp dẫn.'
  },
  {
    icon: 'fa-solid fa-users-gear',
    title: 'Môi trường năng động',
    desc: 'Môi trường học tập thân thiện, nhiều hoạt động ngoại khóa phát triển toàn diện.'
  },
  {
    icon: 'fa-solid fa-dollar-sign',
    title: 'Chi phí hợp lý',
    desc: 'Học phí phù hợp, nhiều chính sách học bổng và hỗ trợ tài chính cho sinh viên.'
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

.why-grid {
  display: grid;
  grid-template-columns: repeat(6, 1fr);
  gap: 18px;
}

.why-card {
  min-height: 210px;
  padding: 24px 16px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  text-align: center;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.06);
  transition: 0.25s;
}

.why-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 30px rgba(7, 63, 131, 0.1);
}

.why-card div {
  margin-bottom: 16px;
  color: #063b7a;
  font-size: 32px;
}

.why-card h4 {
  margin: 0 0 9px;
  color: #063b7a;
  font-size: 13px;
  font-weight: 900;
}

.why-card p {
  margin: 0;
  color: #607894;
  font-size: 11px;
  line-height: 1.6;
}

@media (max-width: 1180px) {
  .why-grid {
    grid-template-columns: repeat(3, 1fr);
  }
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

  .why-grid {
    grid-template-columns: 1fr;
  }
}
</style>