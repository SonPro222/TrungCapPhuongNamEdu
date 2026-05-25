<template>
  <section class="he-thong-layout">
    <PageHeader
        title="Quản lý hệ thống"
        description="Quản lý tài khoản, phân quyền, nhân viên, nhật ký và tệp đính kèm."
    />

    <HeThongMenu />

    <div class="he-thong-content">
      <RouterView :key="$route.fullPath" />
    </div>
  </section>
</template>

<script setup>
import PageHeader from '@/shared/components/PageHeader.vue'
import HeThongMenu from '../components/HeThongMenu.vue'
</script>

<style scoped>
.he-thong-layout {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.he-thong-content {
  background: var(--color-white);
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 20px;
}
</style>