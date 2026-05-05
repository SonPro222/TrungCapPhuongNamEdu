<template>
  <div v-if="modelValue" class="modal-backdrop">
    <form class="modal-card" @submit.prevent="$emit('submit')">
      <div class="modal-head">
        <h2>{{ title }}</h2>
        <button type="button" class="close-btn" @click="$emit('update:modelValue', false)">×</button>
      </div>

      <div class="form-grid">
        <slot />
      </div>

      <div class="modal-actions">
        <button class="btn secondary" type="button" @click="$emit('update:modelValue', false)">Hủy</button>
        <button class="btn primary" type="submit" :disabled="saving">
          {{ saving ? 'Đang lưu...' : 'Lưu' }}
        </button>
      </div>
    </form>
  </div>
</template>

<script setup>
defineProps({
  modelValue: { type: Boolean, default: false },
  title: { type: String, required: true },
  saving: { type: Boolean, default: false },
});

defineEmits(['update:modelValue', 'submit']);
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  inset: 0;
  z-index: 1000;
  display: grid;
  place-items: center;
  padding: 20px;
  background: rgba(15, 23, 42, 0.45);
}

.modal-card {
  width: min(760px, 100%);
  max-height: 90vh;
  overflow-y: auto;
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.3);
}

.modal-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 20px;
  border-bottom: 1px solid #e2e8f0;
}

h2 {
  margin: 0;
  font-size: 20px;
}

.close-btn {
  width: 34px;
  height: 34px;
  border: 0;
  border-radius: 999px;
  background: #e2e8f0;
  cursor: pointer;
  font-size: 22px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  padding: 20px;
}

:deep(.field.full) {
  grid-column: 1 / -1;
}

:deep(.field label) {
  display: block;
  margin-bottom: 6px;
  color: #334155;
  font-size: 13px;
  font-weight: 700;
}

:deep(.field input),
:deep(.field textarea),
:deep(.field select) {
  width: 100%;
  box-sizing: border-box;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  outline: none;
  font: inherit;
}

:deep(.field textarea) {
  min-height: 88px;
  resize: vertical;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  padding: 16px 20px 20px;
  border-top: 1px solid #e2e8f0;
}

.btn {
  cursor: pointer;
  border: 0;
  border-radius: 10px;
  padding: 10px 16px;
  font-weight: 800;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.7;
}

.primary {
  color: #fff;
  background: #2563eb;
}

.secondary {
  color: #0f172a;
  background: #e2e8f0;
}

@media (max-width: 720px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
