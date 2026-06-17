<template>
  <section class="newsletter-section" id="dang-ky-nhan-tin">
    <div class="public-container">
      <div class="newsletter-box">
        <div class="newsletter-left">
          <div class="newsletter-icon">
            <i class="fa-regular fa-envelope-open"></i>
          </div>

          <div>
            <h2>Không bỏ lỡ tin tức từ Phương Nam!</h2>
            <p>
              Đăng ký để nhận bản tin, thông báo và cập nhật mới nhất về tuyển sinh,
              sự kiện và hoạt động của nhà trường.
            </p>
          </div>
        </div>

        <form class="newsletter-form">
          <input type="email" placeholder="Nhập email của bạn..." />
          <button type="button">
            Đăng ký nhận tin
            <i class="fa-solid fa-arrow-right"></i>
          </button>
        </form>
      </div>
    </div>
  </section>
</template>

<style scoped>
.newsletter-section {
  padding: 34px 0 76px;
  background: #ffffff;
}

.newsletter-box {
  position: relative;
  overflow: hidden;
  display: grid;
  grid-template-columns: 1fr 420px;
  align-items: center;
  gap: 28px;
  padding: 34px 38px;
  border-radius: 22px;
  background: #063b7a;
  color: #ffffff;
  box-shadow: 0 16px 34px rgba(6, 59, 122, 0.22);
}

.newsletter-box::after {
  content: '';
  position: absolute;
  right: -70px;
  top: -90px;
  width: 260px;
  height: 260px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.06);
}

.newsletter-left {
  position: relative;
  z-index: 2;
  display: flex;
  align-items: center;
  gap: 18px;
}

.newsletter-icon {
  width: 58px;
  height: 58px;
  display: grid;
  place-items: center;
  border: 1px solid rgba(255, 255, 255, 0.22);
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.16);
  font-size: 27px;
}

.newsletter-left h2 {
  margin: 0 0 7px;
  font-size: 22px;
  font-weight: 900;
}

.newsletter-left p {
  max-width: 520px;
  margin: 0;
  color: #dbeafe;
  font-size: 12px;
  line-height: 1.6;
}

.newsletter-form {
  position: relative;
  z-index: 2;
}

.newsletter-form input {
  width: 100%;
  height: 48px;
  padding: 0 150px 0 16px;
  border: none;
  border-radius: 13px;
  outline: none;
  color: #111827;
  font-size: 13px;
}

.newsletter-form button {
  position: absolute;
  top: 5px;
  right: 5px;
  height: 38px;
  border: 1px solid #1d7df2;
  border-radius: 10px;
  background: #075fc8;
  color: #ffffff;
  padding: 0 14px;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
}

@media (max-width: 1180px) {
  .newsletter-box {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 760px) {
  .newsletter-left {
    display: block;
  }

  .newsletter-icon {
    margin-bottom: 16px;
  }

  .newsletter-form input {
    padding-right: 16px;
  }

  .newsletter-form button {
    position: static;
    width: 100%;
    margin-top: 10px;
  }
}
</style>