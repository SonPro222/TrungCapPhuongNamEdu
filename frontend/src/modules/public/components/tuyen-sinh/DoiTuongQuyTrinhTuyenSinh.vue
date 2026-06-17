<template>
  <section class="tuyen-sinh-section two-column-section">
    <div class="public-container two-column-grid">
      <div class="info-panel">
        <h3>Đối tượng và điều kiện dự tuyển</h3>

        <div class="target-list">
          <article v-for="item in doiTuongDuTuyen" :key="item.title" class="target-item">
            <div class="target-icon">
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

      <div class="info-panel process-panel">
        <div class="tuyen-sinh-section-title small-title">
          <span></span>
          <h2>Quy trình xét tuyển</h2>
          <span></span>
        </div>

        <div class="process-wrap">
          <div class="process-line"></div>

          <div class="process-grid">
            <article v-for="(item, index) in quyTrinhXetTuyen" :key="item.title" class="process-card">
              <div class="process-icon">
                <i :class="item.icon"></i>
                <span>0{{ index + 1 }}</span>
              </div>

              <h4>{{ item.title }}</h4>
              <p>{{ item.desc }}</p>
            </article>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
const doiTuongDuTuyen = [
  {
    icon: 'fa-solid fa-user',
    title: 'Đối tượng tuyển sinh',
    points: [
      'Xét tuyển đã tốt nghiệp THCS hoặc THPT',
      'Người học có nhu cầu học nghề, chuyển đổi nghề nghiệp',
      'Không giới hạn độ tuổi'
    ]
  },
  {
    icon: 'fa-solid fa-user-graduate',
    title: 'Điều kiện xét tuyển',
    points: [
      'Tốt nghiệp THCS hoặc THPT',
      'Sức khỏe đủ điều kiện theo quy định',
      'Hồ sơ hợp lệ theo yêu cầu của trường'
    ]
  }
]

const quyTrinhXetTuyen = [
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

.target-list {
  display: grid;
  gap: 24px;
}

.target-item {
  display: flex;
  gap: 16px;
}

.target-icon {
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

.target-item h4 {
  margin: 0 0 8px;
  color: #063b7a;
  font-size: 14px;
  font-weight: 900;
}

.target-item ul {
  padding: 0;
  margin: 0;
  list-style: none;
}

.target-item li {
  position: relative;
  padding-left: 16px;
  margin-bottom: 8px;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

.target-item li span {
  position: absolute;
  left: 0;
  top: 8px;
  width: 5px;
  height: 5px;
  border-radius: 999px;
  background: #075fc8;
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

.process-wrap {
  position: relative;
}

.process-line {
  position: absolute;
  top: 28px;
  left: 14%;
  right: 14%;
  z-index: 0;
  border-top: 2px dashed #d1d5db;
}

.process-grid {
  position: relative;
  z-index: 2;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
}

.process-card {
  text-align: center;
}

.process-icon {
  position: relative;
  width: 58px;
  height: 58px;
  display: grid;
  place-items: center;
  margin: 0 auto 15px;
  border: 1px solid #dbe9f8;
  border-radius: 15px;
  background: #ffffff;
  color: #075fc8;
  font-size: 25px;
  box-shadow: 0 8px 18px rgba(7, 63, 131, 0.08);
}

.process-icon span {
  position: absolute;
  top: -10px;
  left: -10px;
  width: 26px;
  height: 26px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #063b7a;
  color: #ffffff;
  font-size: 11px;
  font-weight: 900;
}

.process-card h4 {
  margin: 0 0 7px;
  color: #063b7a;
  font-size: 13px;
  font-weight: 900;
}

.process-card p {
  margin: 0;
  color: #607894;
  font-size: 12px;
  line-height: 1.6;
}

@media (max-width: 1180px) {
  .two-column-grid {
    grid-template-columns: 1fr;
  }

  .process-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .process-line {
    display: none;
  }
}

@media (max-width: 760px) {
  .process-grid {
    grid-template-columns: 1fr;
  }
}
</style>