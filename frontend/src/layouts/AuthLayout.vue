<template>
  <main class="auth-layout">
    <section class="auth-card">
      <div class="auth-brand">
        <span class="auth-logo">PN</span>
        <div>
          <h1>Trung cấp Phương Nam</h1>
          <p>Hệ thống quản lý đào tạo</p>
        </div>
      </div>
      <router-view />
    </section>
  </main>
</template>

<style scoped>
.auth-layout{min-height:100vh;display:flex;align-items:center;justify-content:center;padding:24px;background:linear-gradient(135deg,#eff6ff,#f8fafc 55%,#fff7ed)}.auth-card{width:min(440px,100%);padding:24px;border:1px solid #e2e8f0;border-radius:20px;background:#fff;box-shadow:0 20px 50px rgba(15,23,42,.1)}.auth-brand{display:flex;align-items:center;gap:12px;margin-bottom:18px}.auth-logo{width:42px;height:42px;border-radius:14px;display:inline-flex;align-items:center;justify-content:center;background:#2563eb;color:#fff;font-weight:900}.auth-brand h1{font-size:18px;margin:0}.auth-brand p{margin:2px 0 0;color:#64748b;font-size:12px}
</style>
