<template>
  <section class="tuyen-sinh-section two-column-section" id="ho-so">
    <div class="public-container two-column-grid">
      <div class="info-panel">
        <h3>Hồ sơ cần chuẩn bị</h3>

        <ul class="document-list">
          <li v-for="item in hoSoCanChuanBi" :key="item">
            <span>
              <i class="fa-solid fa-check"></i>
            </span>
            <p v-html="item"></p>
          </li>
        </ul>
      </div>

      <div class="info-panel">
        <div class="tuyen-sinh-section-title small-title">
          <span></span>
          <h2>Mốc thời gian tuyển sinh</h2>
          <span></span>
        </div>

        <div class="timeline-grid-admission">
          <article v-for="item in mocThoiGianTuyenSinh" :key="item.title">
            <i :class="item.icon"></i>
            <h4>{{ item.title }}</h4>
            <strong>{{ item.date }}</strong>
            <p>{{ item.desc }}</p>
          </article>
        </div>

        <div class="note-box">
          <i class="fa-solid fa-circle-info"></i>
          <p>
            Lịch xét tuyển có thể thay đổi theo kế hoạch tuyển sinh của trường.
            Vui lòng theo dõi thông báo mới nhất trên website và fanpage chính thức.
          </p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const hoSoCanChuanBi = [
  'Phiếu đăng ký xét tuyển <i>(theo mẫu của trường)</i>',
  'Bản sao công chứng bằng tốt nghiệp THCS/THPT hoặc giấy chứng nhận tốt nghiệp tạm thời',
  'Bản sao công chứng học bạ THCS/THPT',
  'Bản sao CCCD/CMND',
  'Ảnh 3x4 (4 ảnh)',
  'Giấy tờ ưu tiên nếu có: giấy chứng nhận đối tượng ưu tiên, hộ nghèo, cận nghèo'
]

const mocThoiGianTuyenSinh = [
  {
    icon: 'fa-regular fa-calendar-days',
    title: 'Đợt 1',
    date: '01/03 - 30/04',
    desc: 'Nhận hồ sơ và xét tuyển'
  },
  {
    icon: 'fa-regular fa-calendar-days',
    title: 'Đợt 2',
    date: '01/05 - 30/06',
    desc: 'Nhận hồ sơ và xét tuyển'
  },
  {
    icon: 'fa-regular fa-calendar-check',
    title: 'Đợt 3',
    date: '01/07 - 31/08',
    desc: 'Nhận hồ sơ và xét tuyển'
  },
  {
    icon: 'fa-regular fa-calendar-plus',
    title: 'Đợt bổ sung',
    date: '01/09 - 30/09',
    desc: 'Nhận hồ sơ và xét tuyển'
  }
]
</script>

<style scoped>
.tuyen-sinh-section {
  padding: 34px 0;
  background: #ffffff;
}

.two-column-grid {
  display: grid;
  grid-template-columns: 5fr 7fr;
  gap: 24px;
}

.info-panel {
  padding: 30px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
}

.info-panel > h3 {
  margin: 0 0 24px;
  color: #063b7a;
  font-size: 20px;
  font-weight: 900;
}

.document-list {
  display: grid;
  gap: 13px;
  padding: 0;
  margin: 0;
  list-style: none;
}

.document-list li {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.document-list li > span {
  flex: 0 0 22px;
  width: 22px;
  height: 22px;
  display: grid;
  place-items: center;
  margin-top: 2px;
  border-radius: 999px;
  background: #075fc8;
  color: #ffffff;
  font-size: 11px;
}

.document-list p {
  margin: 0;
  color: #607894;
  font-size: 13px;
  line-height: 1.6;
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
  font-size: 18px;
  font-weight: 900;
}

.timeline-grid-admission {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 14px;
}

.timeline-grid-admission article {
  padding: 18px 14px;
  border: 1px solid #e6eef8;
  border-radius: 16px;
  background: #ffffff;
  text-align: center;
  box-shadow: 0 6px 18px rgba(7, 63, 131, 0.04);
}

.timeline-grid-admission i {
  margin-bottom: 10px;
  color: #075fc8;
  font-size: 24px;
}

.timeline-grid-admission h4 {
  margin: 0 0 5px;
  color: #123b68;
  font-size: 13px;
  font-weight: 900;
}

.timeline-grid-admission strong {
  display: block;
  margin-bottom: 8px;
  color: #063b7a;
  font-size: 11px;
  font-weight: 900;
}

.timeline-grid-admission p {
  margin: 0;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

.note-box {
  display: flex;
  gap: 12px;
  margin-top: 18px;
  padding: 14px 16px;
  border: 1px solid #bfdbfe;
  border-radius: 12px;
  background: #eff6ff;
}

.note-box i {
  color: #075fc8;
  margin-top: 2px;
}

.note-box p {
  margin: 0;
  color: #063b7a;
  font-size: 12px;
  line-height: 1.55;
}

@media (max-width: 1180px) {
  .two-column-grid {
    grid-template-columns: 1fr;
  }

  .timeline-grid-admission {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .timeline-grid-admission {
    grid-template-columns: 1fr;
  }
}
</style>