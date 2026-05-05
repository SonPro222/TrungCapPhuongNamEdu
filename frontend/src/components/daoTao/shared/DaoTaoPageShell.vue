<template>
  <section class="dao-tao-page">
    <div class="page-head">
      <div>
        <p class="eyebrow">Quản lý đào tạo</p>
        <h1>{{ title }}</h1>
        <p v-if="description" class="description">{{ description }}</p>
      </div>
      <div class="head-actions">
        <slot name="actions" />
      </div>
    </div>

    <div v-if="$slots.breadcrumb" class="breadcrumb-wrap">
      <slot name="breadcrumb" />
    </div>

    <slot />
  </section>
</template>

<script setup>
defineProps({
  title: { type: String, required: true },
  description: { type: String, default: '' },
});
</script>

<style scoped>
.dao-tao-page {
  padding: 22px;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 16px;
}

.eyebrow {
  margin: 0 0 4px;
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
}

h1 {
  margin: 0;
  color: #0f172a;
  font-size: 28px;
  font-weight: 800;
}

.description {
  margin: 8px 0 0;
  color: #475569;
}

.head-actions {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.breadcrumb-wrap {
  margin-bottom: 14px;
}
</style>
