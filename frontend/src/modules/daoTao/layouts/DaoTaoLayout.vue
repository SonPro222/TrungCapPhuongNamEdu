<template>
  <section class="dao-tao-layout">
    <PageHeader
        title="Nghiệp vụ đào tạo"
        description="Quản lý ngành, trình độ, loại chương trình, khóa đào tạo, khung kỳ và lớp hành chính"
    />

    <DaoTaoMenu />

    <div class="dao-tao-content">
      <RouterView />
    </div>
  </section>
</template>

<script setup>
import PageHeader from '@/shared/components/PageHeader.vue'
import DaoTaoMenu from '../components/DaoTaoMenu.vue'
</script>