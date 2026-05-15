<script setup>
defineProps({
  title: { type: String, required: true },
  value: { type: [String, Number], default: 0 },
  hint: { type: String, default: '' },
});
</script>

<template>
  <article class="ct-stat-card">
    <span>{{ title }}</span>
    <strong>{{ value }}</strong>
    <small>{{ hint }}</small>
  </article>
</template>

<style scoped>
.ct-stat-card{border:1px solid #e5e7eb;border-radius:12px;background:#fff;padding:12px 14px;box-shadow:0 6px 18px rgba(15,23,42,.04)}.ct-stat-card span{display:block;color:#64748b;font-size:12px;font-weight:700}.ct-stat-card strong{display:block;margin-top:3px;color:#0f172a;font-size:22px;line-height:1.2}.ct-stat-card small{display:block;margin-top:2px;color:#94a3b8;font-size:11px}
</style>
