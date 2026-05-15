<template>
  <div class="client-layout">
    <header class="client-header">
      <RouterLink to="/" class="brand">Trung cấp Phương Nam</RouterLink>
      <RouterLink to="/chuong-trinh" class="link">Chương trình đào tạo</RouterLink>
    </header>
    <main class="client-main">
      <router-view />
    </main>
  </div>
</template>

<style scoped>
.client-layout{min-height:100vh;background:#f8fafc}.client-header{height:56px;display:flex;align-items:center;justify-content:space-between;padding:0 20px;background:#fff;border-bottom:1px solid #e2e8f0}.brand{font-weight:900;color:#0f172a;text-decoration:none}.link{font-size:13px;color:#2563eb;text-decoration:none;font-weight:700}.client-main{padding:16px;max-width:1180px;margin:0 auto}
</style>
