<template>
  <div class="admin-layout">
    <header class="admin-header">
      <RouterLink to="/dashboard" class="admin-brand">
        {{ appName }}
      </RouterLink>

      <nav class="admin-nav">
        <RouterLink to="/admin/dao-tao/xem-chuong-trinh">Quản lý đào tạo</RouterLink>
        <RouterLink to="/admin/sinh-vien">Quản lý sinh viên</RouterLink>
        <RouterLink to="/admin/giang-day/lop-hoc-phan">Quản lý giảng dạy</RouterLink>
        <RouterLink to="/admin/he-thong/tai-khoan">Quản lý Hệ thống</RouterLink>
        <RouterLink to="/admin/lms/tong-quan">LMS</RouterLink>
        <RouterLink to="/chuong-trinh/tong-quan">
          Xem tổng quan CTĐT - Kho học liệu
        </RouterLink>
      </nav>

      <div class="admin-auth">
        <div v-if="user" class="admin-auth-user">
          <span class="admin-auth-avatar">
            {{ userInitial }}
          </span>

          <span class="admin-auth-name">
            {{ user.hoTen || user.username || user.email }}
          </span>
        </div>

        <button type="button" class="admin-logout-btn" @click="logout">
          Đăng xuất
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

const userInitial = computed(() => {
  const currentUser = user.value
  const displayName = currentUser?.hoTen || currentUser?.username || currentUser?.email || 'A'

  return displayName.trim().charAt(0).toUpperCase()
})

function logout() {
  authService.logout()
  router.replace('/auth/login')
}
</script>