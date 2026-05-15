<template>
  <nav v-if="items.length" class="pn-breadcrumb" aria-label="breadcrumb">
    <RouterLink v-for="(item, index) in items" :key="`${item.label}-${index}`" :to="item.to || route.fullPath" class="pn-breadcrumb-item" :class="{ current: index === items.length - 1 || !item.to }">
      <span>{{ item.label }}</span>
    </RouterLink>
  </nav>
</template>

<script setup>
import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { buildBreadcrumbItems } from '@/services/breadcrumbService.js';

const route = useRoute();
const items = ref([]);
let requestId = 0;

watch(
  () => route.fullPath,
  async () => {
    const currentRequestId = ++requestId;
    const result = await buildBreadcrumbItems(route);
    if (currentRequestId === requestId) items.value = result;
  },
  { immediate: true },
);
</script>

<style scoped>
.pn-breadcrumb{height:40px;display:flex;align-items:center;gap:0;padding:0 24px;border-bottom:1px solid #d8dee9;background:#fff;overflow-x:auto;white-space:nowrap}.pn-breadcrumb-item{position:relative;display:inline-flex;align-items:center;min-height:40px;padding:0 18px 0 0;margin-right:18px;color:#1e3a8a;font-size:14px;font-weight:850;text-decoration:none}.pn-breadcrumb-item::after{content:'›';position:absolute;right:-2px;top:50%;transform:translateY(-52%);color:#64748b;font-size:24px;font-weight:300}.pn-breadcrumb-item.current{color:#334155;pointer-events:none}.pn-breadcrumb-item.current::after{display:none}.pn-breadcrumb-item:hover span{text-decoration:underline}@media(max-width:768px){.pn-breadcrumb{padding:0 12px}.pn-breadcrumb-item{font-size:13px;margin-right:12px;padding-right:12px}}
</style>
