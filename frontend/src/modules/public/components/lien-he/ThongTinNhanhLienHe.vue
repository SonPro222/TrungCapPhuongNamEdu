<template>
  <section class="quick-contact-section">
    <div class="public-container">
      <div class="quick-contact-box">
        <article v-for="item in thongTinNhanh" :key="item.title">
          <div class="quick-contact-icon">
            <i :class="item.icon"></i>
          </div>

          <div>
            <span>{{ item.title }}</span>
            <strong>{{ item.desc }}</strong>
            <small>{{ item.sub }}</small>
          </div>
        </article>
      </div>
    </div>
  </section>
</template>

<script setup>
const thongTinNhanh = [
  {
    icon: 'fa-solid fa-headset',
    title: 'Hotline tư vấn',
    desc: '1900 633 088',
    sub: 'Hỗ trợ 24/7'
  },
  {
    icon: 'fa-solid fa-envelope-open-text',
    title: 'Email liên hệ',
    desc: 'tuyensinh@phuongnam.edu.vn',
    sub: 'Phản hồi trong 24h'
  },
  {
    icon: 'fa-solid fa-map-location-dot',
    title: 'Địa chỉ',
    desc: 'Số 12, Đường 3/2',
    sub: 'TP. Hồ Chí Minh'
  },
  {
    icon: 'fa-solid fa-clock',
    title: 'Giờ làm việc',
    desc: 'Thứ 2 - Thứ 7',
    sub: '07:30 - 16:30'
  },
  {
    icon: 'fa-solid fa-thumbs-up',
    title: 'Fanpage',
    desc: 'fb.com/tcphuongnam',
    sub: 'Nhắn tin ngay'
  }
]
</script>

<style scoped>
.quick-contact-section {
  position: relative;
  z-index: 5;
  margin-top: -48px;
  margin-bottom: 42px;
}

.quick-contact-box {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  overflow: hidden;
  padding: 8px;
  border: 1px solid #e6eef8;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 18px 42px rgba(7, 63, 131, 0.12);
}

.quick-contact-box article {
  display: flex;
  align-items: center;
  gap: 13px;
  min-height: 82px;
  padding: 14px;
  border-right: 1px solid #edf4fb;
  border-radius: 14px;
}

.quick-contact-box article:last-child {
  border-right: none;
}

.quick-contact-box article:hover {
  background: #f8fbff;
}

.quick-contact-icon {
  flex: 0 0 42px;
  width: 42px;
  height: 42px;
  display: grid;
  place-items: center;
  border-radius: 999px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 18px;
}

.quick-contact-box span {
  display: block;
  color: #64748b;
  font-size: 12px;
  font-weight: 700;
}

.quick-contact-box strong {
  display: block;
  margin-top: 2px;
  color: #1e293b;
  font-size: 13px;
  font-weight: 900;
  white-space: nowrap;
}

.quick-contact-box small {
  display: block;
  margin-top: 2px;
  color: #94a3b8;
  font-size: 10px;
}

@media (max-width: 1180px) {
  .quick-contact-box {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 760px) {
  .quick-contact-section {
    margin-top: -36px;
  }

  .quick-contact-box {
    grid-template-columns: 1fr;
  }

  .quick-contact-box article {
    border-right: none;
    border-bottom: 1px solid #edf4fb;
  }

  .quick-contact-box article:last-child {
    border-bottom: none;
  }
}
</style>