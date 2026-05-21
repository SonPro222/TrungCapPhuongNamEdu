<template>
  <section class="chuong-trinh-layout">
    <PageHeader
        title="Quản lý chương trình đào tạo"
        description="Chọn Xây dựng chương trình để quản lý dữ liệu, hoặc Đào tạo xem để xem/sửa chương trình theo luồng cha con."
    />

    <ChuongTrinhMenu />
    <div class="chuong-trinh-content">
      <RouterView :key="$route.fullPath" />
    </div>
  </section>
</template>

<script setup>
import PageHeader from '@/shared/components/PageHeader.vue'
import ChuongTrinhMenu from '../components/ChuongTrinhMenu.vue'
</script>

<style scoped>
.chuong-trinh-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.chuong-trinh-content {
  background: var(--color-white);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 20px;
}
</style>