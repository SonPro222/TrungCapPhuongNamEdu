<template>
  <div class="xem-trang-dang-tai">
    <span class="spinner" aria-hidden="true"></span>
    <span class="text">{{ thongBao }}</span>
  </div>
</template>

<script setup>
defineProps({
  thongBao: {
    type: String,
    default: 'Đang tải dữ liệu...'
  }
})
</script>

<style scoped>
.xem-trang-dang-tai {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 32px 16px;
  color: #64748b;
  font-size: 14px;
}
.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid #e2e8f0;
  border-top-color: #6366f1;
  border-radius: 50%;
  animation: xem-spin 0.7s linear infinite;
}
@keyframes xem-spin {
  to {
    transform: rotate(360deg);
  }
}
</style>
