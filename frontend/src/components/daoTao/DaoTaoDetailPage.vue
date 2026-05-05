<template>
  <DaoTaoPageShell :title="title" description="Trang chi tiết dùng chung theo id bản ghi.">
    <template #breadcrumb>
      <RouterLink :to="backTo">Quay lại danh sách</RouterLink>
      <span> / ID {{ id }}</span>
    </template>

    <div class="card">
      <h2>{{ title }}</h2>
      <p><b>ID:</b> {{ id }}</p>
      <p>Trang này giữ route chi tiết cho từng controller. Dữ liệu đã được gọi bằng API getById trước khi chuyển route ở trang danh sách.</p>
    </div>
  </DaoTaoPageShell>
</template>

<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import DaoTaoPageShell from './shared/DaoTaoPageShell.vue';

const route = useRoute();
const id = computed(() => route.params.id);
const title = computed(() => route.meta?.title || 'Chi tiết');
const backTo = computed(() => route.meta?.backTo || { name: 'dao-tao-nganh' });
</script>

<style scoped>
.card {
  padding: 18px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
}
</style>
