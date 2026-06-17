<template>
  <button type="button" class="google-login-btn" @click="loginGoogle">
    Đăng nhập Google
  </button>
</template>

<script setup>
function loginGoogle() {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}
</script>