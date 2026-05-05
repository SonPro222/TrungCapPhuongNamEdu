<template>
  <div class="public-layout">
    <header class="public-nav">
      <router-link to="/" class="brand">
        <span class="logo">PN</span>
        <span>Trung Cấp Phương Nam</span>
      </router-link>

      <nav>
        <router-link to="/" class="nav-link">Trang chủ</router-link>
      </nav>
    </header>

    <main class="public-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
</script>

<style scoped>
.public-layout {
  width: 100%;
  min-height: 100vh;
  background:
      radial-gradient(circle at top left, rgba(37, 99, 235, 0.14), transparent 35%),
      linear-gradient(135deg, #f8fafc, #eef2ff);
}

.public-nav {
  width: 100%;
  height: 64px;
  padding: 0 clamp(16px, 4vw, 60px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: rgba(255, 255, 255, 0.92);
  border-bottom: 1px solid #e2e8f0;
  backdrop-filter: blur(12px);
  position: sticky;
  top: 0;
  z-index: 20;
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #0f172a;
  text-decoration: none;
  font-weight: 900;
  min-width: 0;
}

.logo {
  width: 38px;
  height: 38px;
  border-radius: 12px;
  background: linear-gradient(135deg, #2563eb, #06b6d4);
  color: #fff;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  flex: 0 0 auto;
}

.nav-link {
  color: #2563eb;
  font-weight: 800;
  text-decoration: none;
}

.public-content {
  width: 100%;
  min-height: calc(100vh - 64px);
}
</style>