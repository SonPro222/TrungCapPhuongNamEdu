<script setup>
defineProps({ monHoc: { type: Object, required: true } });
</script>

<template>
  <div class="mon-item">
    <b>{{ monHoc.maMon || monHoc.maMonTrongCt || '—' }}</b>
    <span>{{ monHoc.tenMon || monHoc.ten || monHoc.loaiHocPhan || 'Chưa có tên môn' }}</span>
  </div>
</template>

<style scoped>
.mon-item{display:flex;align-items:center;gap:8px;border:1px solid #e5e7eb;border-radius:9px;padding:8px 10px;background:#fff;font-size:13px}.mon-item b{color:#2563eb}.mon-item span{color:#334155;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
</style>
