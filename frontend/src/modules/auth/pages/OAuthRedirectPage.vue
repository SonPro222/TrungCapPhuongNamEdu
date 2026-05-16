<template>
  <div class="login-box">
    <h1>Đang đăng nhập...</h1>
    <p>Vui lòng chờ hệ thống xác thực Google.</p>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { tokenService } from '@/core/services/tokenService.js'

const route = useRoute()
const router = useRouter()

onMounted(() => {
  const token = route.query.token

  if (!token) {
    router.replace('/auth/login')
    return
  }

  tokenService.setToken(token)

  router.replace('/admin')
})
</script>