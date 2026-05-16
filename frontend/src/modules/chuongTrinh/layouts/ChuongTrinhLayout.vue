<template>
  <section class="chuong-trinh-layout">
    <PageHeader
        title="Module chương trình"
        description="Xây dựng chương trình đào tạo, phiên bản, môn học, mục tiêu, năng lực và điều kiện tốt nghiệp"
    />

    <ChuongTrinhMenu />
    <div class="chuong-trinh-content">
      <RouterView :key="$route.meta.entity" />
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