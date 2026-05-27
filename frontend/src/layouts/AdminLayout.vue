<template>
  <div class="admin-layout">
    <header class="admin-header">
      <div class="admin-brand">
        {{ appName }}
      </div>

      <nav class="admin-nav">
        <RouterLink to="/admin">Dashboard</RouterLink>
        <RouterLink to="/admin/dao-tao">Đào tạo</RouterLink>
        <RouterLink to="/admin/sinh-vien">Sinh viên</RouterLink>
        <RouterLink to="/admin/giang-day">Giảng dạy</RouterLink>
        <RouterLink to="/admin/he-thong">Hệ thống</RouterLink>

      </nav>

      <div class="admin-auth">
        <span v-if="user">
          {{ user.hoTen || user.username || user.email }}
        </span>

        <button type="button" @click="logout">
          Logout
        </button>
      </div>
    </header>

    <main class="admin-main">
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

const appName = ENV.APP_NAME
const user = computed(() => authService.getCurrentUser())

function logout() {
  authService.logout()
  router.replace('/auth/login')
}
</script>
