<!-- src/components/home/HomePage.vue -->
<template>
  <section class="home-page">
    <div class="home-left">
      <div class="badge">Hệ thống quản lý đào tạo</div>

      <h1>Trường Trung Cấp Phương Nam</h1>

      <p class="subtitle">
        Nền tảng quản lý chương trình đào tạo, sinh viên, giảng dạy,
        điểm, học phí, thi và LMS trong một hệ thống thống nhất.
      </p>

      <div class="cards">
        <div class="card">
          <h3>Quản lý đào tạo</h3>
          <p>Chương trình, ngành, môn học, lớp học phần và kế hoạch đào tạo.</p>
        </div>

        <div class="card">
          <h3>Quản lý sinh viên</h3>
          <p>Hồ sơ sinh viên, lớp hành chính, đăng ký học phần và tiến độ học.</p>
        </div>

        <div class="card">
          <h3>Hệ thống LMS</h3>
          <p>Bài tập online, ngân hàng câu hỏi, bài làm và đẩy điểm về bảng điểm.</p>
        </div>
      </div>
    </div>

    <aside class="home-login">
      <LoginPanel />
    </aside>
  </section>
</template>

<script setup>
import LoginPanel from '@/components/login/LoginPage.vue';
</script>

<style scoped>
.home-page {
  width: 100%;
  min-height: calc(100vh - 64px);
  padding: clamp(20px, 5vw, 70px);
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(300px, 380px);
  align-items: center;
  gap: clamp(24px, 5vw, 70px);
  overflow-x: hidden;
}

.home-left {
  min-width: 0;
}

.badge {
  width: fit-content;
  padding: 9px 14px;
  border-radius: 999px;
  background: #dbeafe;
  color: #1d4ed8;
  font-weight: 900;
  font-size: 13px;
  margin-bottom: 18px;
}

h1 {
  margin: 0;
  color: #0f172a;
  font-size: clamp(34px, 5vw, 64px);
  line-height: 1.05;
  font-weight: 950;
  letter-spacing: -1.5px;
}

.subtitle {
  max-width: 760px;
  color: #475569;
  font-size: clamp(16px, 2vw, 20px);
  line-height: 1.7;
  margin: 22px 0 28px;
}

.cards {
  display: grid;
  grid-template-columns: repeat(3, minmax(180px, 1fr));
  gap: 16px;
}

.card {
  min-width: 0;
  padding: 20px;
  border-radius: 22px;
  background: white;
  border: 1px solid #e2e8f0;
  box-shadow: 0 18px 48px rgba(15, 23, 42, 0.08);
}

.card h3 {
  margin: 0 0 10px;
  color: #0f172a;
  font-size: 17px;
}

.card p {
  margin: 0;
  color: #64748b;
  line-height: 1.55;
  font-size: 14px;
}

.home-login {
  width: 100%;
  display: flex;
  justify-content: flex-end;
  min-width: 0;
}

@media (max-width: 1080px) {
  .home-page {
    grid-template-columns: 1fr;
    align-items: start;
  }

  .home-login {
    justify-content: flex-start;
  }
}

@media (max-width: 760px) {
  .cards {
    grid-template-columns: 1fr;
  }

  .home-page {
    padding: 18px;
  }
}
</style>