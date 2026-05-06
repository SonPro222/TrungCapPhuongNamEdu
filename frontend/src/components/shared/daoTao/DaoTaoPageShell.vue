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
  padding: 24px;
  color: #123046;
}

.page-head {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 18px;
}

.eyebrow {
  margin: 0 0 8px;
  color: #27739e;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
}

h1 {
  margin: 0;
  color: #123046;
  font-size: 30px;
  font-weight: 800;
  line-height: 1.15;
}

.description {
  margin: 12px 0 0;
  color: #4d6780;
  line-height: 1.6;
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
