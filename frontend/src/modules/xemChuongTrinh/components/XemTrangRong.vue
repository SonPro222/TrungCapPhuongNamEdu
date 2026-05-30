<template>
  <div class="xem-trang-rong">
    <div class="xem-trang-rong__icon">∅</div>
    <p class="xem-trang-rong__text">{{ thongBao }}</p>
  </div>
</template>

<script setup>
defineProps({
  thongBao: {
    type: String,
    default: 'Chưa có dữ liệu đã gán để hiển thị.'
  }
})
</script>

<style scoped>
.xem-trang-rong {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 16px;
  background: #ffffff;
  border: 1px dashed #cbd5e1;
  border-radius: 12px;
  color: #94a3b8;
}
.xem-trang-rong__icon {
  font-size: 32px;
  margin-bottom: 8px;
}
.xem-trang-rong__text {
  margin: 0;
  font-size: 14px;
}
</style>
