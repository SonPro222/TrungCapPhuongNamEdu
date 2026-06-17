<template>
  <div class="breadcrumb-public">
    <RouterLink :to="{ name: 'Public.TrangChu' }">Trang chủ</RouterLink>
    <span>/</span>
    <strong>{{ hienTai }}</strong>
  </div>
</template>

<script setup>
defineProps({
  hienTai: {
    type: String,
    required: true
  }
})
</script>