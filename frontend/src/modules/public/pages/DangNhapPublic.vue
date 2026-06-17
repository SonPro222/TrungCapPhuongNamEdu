<template>
  <div class="dang-nhap-page">
    <GioiThieuDangNhap />
    <FormDangNhapPublic />
  </div>
</template>

<script setup>
import GioiThieuDangNhap from '@/modules/public/components/dang-nhap/GioiThieuDangNhap.vue'
import FormDangNhapPublic from '@/modules/public/components/dang-nhap/FormDangNhapPublic.vue'
</script>

<style scoped>
.dang-nhap-page {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 1.42fr 0.88fr;
  background: #0056c9;
  padding: 22px;
  font-family: 'Be Vietnam Pro', Inter, Arial, sans-serif;
}

@media (max-width: 1280px) {
  .dang-nhap-page {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 700px) {
  .dang-nhap-page {
    padding: 0;
  }
}
</style>