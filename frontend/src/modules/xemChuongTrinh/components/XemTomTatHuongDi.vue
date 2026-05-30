<template>
  <div class="xem-tom-tat">
    <div class="xem-tom-tat__title">{{ tieuDe }}</div>
    <nav class="xem-tom-tat__crumbs">
      <template v-for="(item, index) in danhSach" :key="index">
        <span class="crumb">
          <span class="crumb__label">{{ item.nhan }}</span>
          <span class="crumb__value">{{ item.giaTri || '-' }}</span>
        </span>
        <span v-if="index < danhSach.length - 1" class="crumb__sep">→</span>
      </template>
    </nav>
  </div>
</template>

<script setup>
defineProps({
  tieuDe: {
    type: String,
    default: 'Đường đi'
  },
  // danhSach: [{ nhan: 'Ngành', giaTri: 'CNTT' }, ...]
  danhSach: {
    type: Array,
    default: () => []
  }
})
</script>

<style scoped>
.xem-tom-tat {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 12px 16px;
  margin-bottom: 16px;
}
.xem-tom-tat__title {
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  margin-bottom: 8px;
}
.xem-tom-tat__crumbs {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 8px;
}
.crumb {
  display: inline-flex;
  flex-direction: column;
  background: #f1f5f9;
  border-radius: 8px;
  padding: 4px 10px;
}
.crumb__label {
  font-size: 11px;
  color: #94a3b8;
}
.crumb__value {
  font-size: 13px;
  font-weight: 600;
  color: #1e293b;
}
.crumb__sep {
  color: #cbd5e1;
  font-weight: 700;
}
</style>
