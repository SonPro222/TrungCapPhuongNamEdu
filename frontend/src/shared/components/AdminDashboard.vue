<template>
  <div class="dashboard-page">

    <div class="dashboard-hero">
      <img src="/logoPhuongNam/logo-full.png" alt="Phương Nam" class="dashboard-logo" />
      <h1 class="dashboard-title">Trường Trung Cấp Phương Nam</h1>
      <p class="dashboard-subtitle">Hệ thống quản lý đào tạo</p>
    </div>

    <div class="dashboard-cards">
      <RouterLink to="/admin" class="dash-card dash-card--green">
        <div class="dash-card__icon">🎓</div>
        <div class="dash-card__label">Đào Tạo</div>
        <div class="dash-card__desc">Quản lý chương trình, môn học, ngành đào tạo</div>
      </RouterLink>

      <RouterLink to="/user/trien-khai/chuong-trinh-dao-tao" class="dash-card dash-card--orange">
        <div class="dash-card__icon">📚</div>
        <div class="dash-card__label">Sinh Viên - Giảng Viên</div>
        <div class="dash-card__desc">Xem chương trình đào tạo và lớp học phần</div>
      </RouterLink>
    </div>

  </div>
</template>

<script setup>
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f0faf4 0%, #fff8f0 100%);
  padding: 40px 20px;
}

.dashboard-hero {
  text-align: center;
  margin-bottom: 48px;
}

.dashboard-logo {
  width: 280px;
  height: auto;
  object-fit: contain;
  margin-bottom: 16px;
  filter: drop-shadow(0 4px 12px rgba(0,0,0,0.12));
}

.dashboard-title {
  font-size: 28px;
  font-weight: 700;
  color: #1a5c36;
  margin: 0 0 8px;
  letter-spacing: 0.5px;
}

.dashboard-subtitle {
  font-size: 15px;
  color: #6b7280;
  margin: 0;
}

.dashboard-cards {
  display: flex;
  gap: 32px;
  flex-wrap: wrap;
  justify-content: center;
}

.dash-card {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 240px;
  padding: 40px 24px;
  border-radius: 20px;
  text-decoration: none;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  box-shadow: 0 4px 20px rgba(0,0,0,0.08);
  cursor: pointer;
}

.dash-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.15);
}

.dash-card--green {
  background: linear-gradient(145deg, #1a5c36, #2d8a55);
  color: #fff;
}

.dash-card--orange {
  background: linear-gradient(145deg, #c45a00, #e07820);
  color: #fff;
}

.dash-card__icon {
  font-size: 48px;
  margin-bottom: 16px;
  line-height: 1;
}

.dash-card__label {
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 10px;
  text-align: center;
  letter-spacing: 0.3px;
}

.dash-card__desc {
  font-size: 13px;
  opacity: 0.85;
  text-align: center;
  line-height: 1.5;
}
</style>
