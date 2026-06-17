<template>
  <div class="user-layout">
    <header class="user-header">
      <RouterLink to="/dashboard" class="user-brand">
        <img src="/logoPhuongNam/logo-full.png" alt="Phương Nam" class="brand-logo" />
        <span class="brand-text">Trung Cấp Phương Nam</span>
      </RouterLink>

      <nav class="user-nav">
        <RouterLink to="/dashboard" class="nav-link">
          <span class="nav-icon">⊞</span> Dashboard
        </RouterLink>
        <RouterLink to="/user/trien-khai/chuong-trinh-dao-tao" class="nav-link">
          <span class="nav-icon">📋</span> Triển khai
        </RouterLink>
      </nav>

      <div class="user-auth">
        <div class="user-info" v-if="user">
          <span class="user-avatar">{{ initials }}</span>
          <span class="user-name">{{ user.hoTen || user.username || user.email }}</span>
        </div>
        <button type="button" class="logout-btn" @click="logout">Đăng xuất</button>
      </div>
    </header>

    <main class="user-main">
      <RouterView />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { ENV } from '../core/config/env'
import { authService } from '../core/services/authService'

const router = useRouter()
const user = computed(() => authService.getCurrentUser())
const initials = computed(() => {
  const name = user.value?.hoTen || user.value?.username || user.value?.email || ''
  return name.charAt(0).toUpperCase()
})

function logout() {
  authService.logout()
  router.replace('/auth/login')
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  background: #f4f7f4;
  font-family: 'Roboto', Arial, sans-serif;
}

.user-header {
  height: 60px;
  background: #1a5c36;
  color: #fff;
  display: flex;
  align-items: center;
  padding: 0 24px;
  gap: 24px;
  position: sticky;
  top: 0;
  z-index: 3000;
  box-shadow: 0 2px 8px rgba(0,0,0,0.18);
}

.user-brand {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: #fff;
  white-space: nowrap;
  flex-shrink: 0;
}

.brand-logo {
  height: 38px;
  width: auto;
  object-fit: contain;
  border-radius: 4px;
}

.brand-text {
  font-size: 15px;
  font-weight: 600;
  letter-spacing: 0.2px;
}

.user-nav {
  display: flex;
  gap: 4px;
  flex: 1;
}

.nav-link {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  height: 60px;
  padding: 0 14px;
  color: rgba(255,255,255,0.85);
  font-size: 14px;
  font-weight: 500;
  text-decoration: none;
  border-bottom: 3px solid transparent;
  transition: color 0.15s, border-color 0.15s;
}

.nav-link:hover,
.nav-link.router-link-active {
  color: #fff;
  border-bottom-color: #f59e0b;
}

.nav-icon {
  font-size: 15px;
  opacity: 0.85;
}

.user-auth {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-shrink: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.user-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: rgba(255,255,255,0.2);
  border: 1px solid rgba(255,255,255,0.35);
  display: grid;
  place-items: center;
  font-size: 13px;
  font-weight: 600;
}

.user-name {
  font-size: 13px;
  font-weight: 500;
  color: rgba(255,255,255,0.9);
  max-width: 160px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.logout-btn {
  height: 32px;
  padding: 0 14px;
  border: 1px solid rgba(255,255,255,0.35);
  border-radius: 6px;
  background: transparent;
  color: rgba(255,255,255,0.9);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.15s;
  font-family: 'Roboto', Arial, sans-serif;
}

.logout-btn:hover {
  background: rgba(255,255,255,0.12);
}

.user-main {
  position: relative;
}
</style>
