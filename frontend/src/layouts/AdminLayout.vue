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
        <span v-if="user">
          {{ user.hoTen || user.username || user.email }}
        </span>

        <button type="button" @click="logout">
          Logout
        </button>
      </div>
    </header>

    <main class="admin-main">
      <RouterView/>
    </main>
  </div>
</template>

<script setup>
import {computed} from 'vue'
import {useRouter} from 'vue-router'
import {ENV} from '../core/config/env'
import {authService} from '../core/services/authService'

const router = useRouter()

const appName = ENV.APP_NAME
const user = computed(() => authService.getCurrentUser())

function logout() {
  authService.logout()
  router.replace('/auth/login')
}
</script>
