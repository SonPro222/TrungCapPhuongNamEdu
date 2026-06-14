<template>
  <section class="about-partner-section">
    <div class="public-container">
      <div class="about-section-title">
        <span></span>
        <h2>Đối tác & Doanh nghiệp đồng hành</h2>
        <span></span>
      </div>

      <div class="partner-list">
        <div v-for="item in partners" :key="item.name" class="partner-item">
          <img :src="item.logo" :alt="item.name" />
        </div>

        <button type="button">Xem tất cả đối tác</button>
      </div>
    </div>
  </section>
</template>

<script setup>
const partners = [
  {
    name: 'FPT Software',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/a/ad/FPT_logo_2010.svg/512px-FPT_logo_2010.svg.png'
  },
  {
    name: 'Viettel',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/e/e0/Viettel_logo_2021.svg/512px-Viettel_logo_2021.svg.png'
  },
  {
    name: 'VNPT',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/1/1a/VNPT_Logo.svg/512px-VNPT_Logo.svg.png'
  },
  {
    name: 'Samsung',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/24/Samsung_Logo.svg/512px-Samsung_Logo.svg.png'
  },
  {
    name: 'BIDV',
    logo: 'https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/BIDV_Logo.svg/512px-BIDV_Logo.svg.png'
  }
]
</script>

<style scoped>
.about-partner-section {
  background: #ffffff;
  padding: 64px 0;
  border-bottom: 1px solid #f1f5f9;
}

.about-section-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 22px;
  margin-bottom: 46px;
  text-align: center;
}

.about-section-title span {
  width: 110px;
  height: 1px;
  background: #cbd5e1;
}

.about-section-title h2 {
  margin: 0;
  color: #063b7a;
  font-size: 30px;
  font-weight: 900;
}

.partner-list {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;
  gap: 28px;
}

.partner-item {
  height: 52px;
  max-width: 150px;
  opacity: 0.58;
  filter: grayscale(1);
  transition: 0.25s;
}

.partner-item:hover {
  opacity: 1;
  filter: grayscale(0);
}

.partner-item img {
  height: 100%;
  max-width: 150px;
  object-fit: contain;
}

.partner-list button {
  border: 1px solid #bfdbfe;
  background: #ffffff;
  color: #075fc8;
  border-radius: 8px;
  padding: 10px 16px;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
}

@media (max-width: 760px) {
  .about-section-title {
    gap: 12px;
  }

  .about-section-title span {
    width: 38px;
  }

  .about-section-title h2 {
    font-size: 22px;
  }
}
</style>