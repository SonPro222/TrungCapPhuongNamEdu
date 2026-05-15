<script setup>
defineEmits(['cancel', 'confirm']);

defineProps({
  open: { type: Boolean, default: false },
  title: { type: String, default: 'Xác nhận xóa' },
  message: { type: String, default: 'Bạn có chắc chắn muốn xóa bản ghi này không?' },
});
</script>

<template>
  <div v-if="open" class="ct-modal-backdrop" @click.self="$emit('cancel')">
    <section class="ct-confirm-card">
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>
      <div class="ct-confirm-actions">
        <button type="button" class="ct-btn ct-btn--light" @click="$emit('cancel')">Hủy</button>
        <button type="button" class="ct-btn ct-btn--danger" @click="$emit('confirm')">Xóa</button>
      </div>
    </section>
  </div>
</template>

<style scoped>
.ct-modal-backdrop{position:fixed;inset:0;z-index:60;display:grid;place-items:center;background:rgba(15,23,42,.42);padding:16px}.ct-confirm-card{width:min(420px,100%);border-radius:14px;background:#fff;padding:18px;box-shadow:0 22px 60px rgba(15,23,42,.22)}.ct-confirm-card h3{margin:0 0 8px;font-size:17px;color:#0f172a}.ct-confirm-card p{margin:0;color:#64748b;font-size:13px;line-height:1.5}.ct-confirm-actions{display:flex;justify-content:flex-end;gap:8px;margin-top:18px}.ct-btn{border:0;border-radius:8px;padding:8px 12px;font-size:13px;font-weight:700;cursor:pointer}.ct-btn--light{background:#f1f5f9;color:#334155}.ct-btn--danger{background:#dc2626;color:#fff}
</style>
