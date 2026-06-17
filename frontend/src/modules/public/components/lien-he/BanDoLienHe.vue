<template>
  <section class="public-container map-section">
    <div class="map-card">
      <div class="map-title-row">
        <div class="contact-card-title">
          <div>
            <i class="fa-solid fa-map-location-dot"></i>
          </div>

          <div>
            <h3>Bản đồ trường</h3>
            <p>Vị trí Trường Trung cấp Phương Nam - Thuận tiện di chuyển, dễ dàng tìm đến.</p>
          </div>
        </div>

        <a href="https://www.google.com/maps" target="_blank" rel="noopener noreferrer">
          <i class="fa-solid fa-location-arrow"></i>
          Chỉ đường trên Google Maps
        </a>
      </div>

      <div class="map-frame">
        <iframe
            title="Bản đồ Trường Trung cấp Phương Nam"
            src="https://www.google.com/maps?q=Tr%C6%B0%E1%BB%9Dng%20Trung%20c%E1%BA%A5p%20Ph%C6%B0%C6%A1ng%20Nam&output=embed"
            loading="lazy"
            referrerpolicy="no-referrer-when-downgrade"
        ></iframe>

        <div class="map-school-card">
          <img
              src="https://images.unsplash.com/photo-1541829070764-84a7d30dd3f3?auto=format&fit=crop&w=180&q=80"
              alt="Trường Phương Nam"
          />

          <div>
            <h4>Trường Trung cấp Phương Nam</h4>
            <p>Số 12, Đường 3/2, P. Xuân Hòa, TP. Hồ Chí Minh</p>
            <a href="#" title="Đang cập nhật" @click.prevent>Xem chi tiết →</a>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.map-section {
  margin-bottom: 46px;
}

.map-card {
  padding: 30px;
  border: 1px solid #e2e8f0;
  border-radius: 22px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(7, 63, 131, 0.05);
}

.map-title-row {
  display: flex;
  justify-content: space-between;
  gap: 18px;
  align-items: center;
  margin-bottom: 22px;
}

.contact-card-title {
  display: flex;
  align-items: center;
  gap: 12px;
}

.contact-card-title > div:first-child {
  width: 34px;
  height: 34px;
  display: grid;
  place-items: center;
  border-radius: 8px;
  background: #eff6ff;
  color: #075fc8;
  font-size: 17px;
}

.contact-card-title h3 {
  margin: 0;
  color: #1e293b;
  font-size: 21px;
  font-weight: 900;
}

.contact-card-title p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 13px;
}

.map-title-row > a {
  min-height: 40px;
  display: inline-flex;
  align-items: center;
  gap: 9px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  color: #334155;
  padding: 0 16px;
  font-size: 13px;
  font-weight: 900;
  text-decoration: none;
}

.map-frame {
  position: relative;
  overflow: hidden;
  height: 400px;
  border: 1px solid #e2e8f0;
  border-radius: 18px;
  background: #e2e8f0;
}

.map-frame iframe {
  width: 100%;
  height: 100%;
  border: 0;
}

.map-school-card {
  position: absolute;
  left: 24px;
  bottom: 24px;
  width: 330px;
  display: grid;
  grid-template-columns: 70px 1fr;
  gap: 14px;
  padding: 14px;
  border: 1px solid #edf2f7;
  border-radius: 16px;
  background: #ffffff;
  box-shadow: 0 12px 26px rgba(15, 23, 42, 0.15);
}

.map-school-card img {
  width: 70px;
  height: 70px;
  border-radius: 10px;
  object-fit: cover;
}

.map-school-card h4 {
  margin: 0 0 5px;
  color: #1e293b;
  font-size: 14px;
  font-weight: 900;
}

.map-school-card p {
  margin: 0 0 7px;
  color: #64748b;
  font-size: 12px;
  line-height: 1.4;
}

.map-school-card a {
  color: #075fc8;
  font-size: 12px;
  font-weight: 900;
  text-decoration: none;
}

@media (max-width: 1180px) {
  .map-title-row {
    align-items: flex-start;
    flex-direction: column;
  }
}

@media (max-width: 760px) {
  .map-card {
    padding: 22px;
  }

  .map-frame {
    height: 330px;
  }

  .map-school-card {
    left: 14px;
    right: 14px;
    bottom: 14px;
    width: auto;
  }
}
</style>
