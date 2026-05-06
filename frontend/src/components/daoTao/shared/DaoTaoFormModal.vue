<template>
  <Teleport to="body">
    <div v-if="modelValue" class="modal-backdrop" @click.self="$emit('update:modelValue', false)">
      <form class="modal-card" @submit.prevent="$emit('submit')">
        <div class="modal-head">
          <div>
            <p class="mini-label">Biểu mẫu</p>
            <h2>{{ title }}</h2>
          </div>
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
  </Teleport>
</template>

<script setup>
import { Teleport } from 'vue';

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
  z-index: 2000;
  display: grid;
  place-items: center;
  padding: 20px;
  background: rgba(10, 28, 40, 0.42);
}

.modal-card {
  width: min(760px, 100%);
  max-height: 90vh;
  overflow-y: auto;
  background: rgba(255, 255, 255, 0.98);
  border: 1px solid rgba(18, 72, 104, 0.08);
  border-radius: 20px;
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.28);
}

.modal-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  padding: 18px 20px;
  border-bottom: 1px solid #e5edf3;
}

.mini-label {
  margin: 0 0 6px;
  font-size: 12px;
  font-weight: 800;
  letter-spacing: 0.08em;
  text-transform: uppercase;
  color: #27739e;
}

h2 {
  margin: 0;
  font-size: 22px;
  color: #123046;
  font-weight: 800;
}

.close-btn {
  width: 34px;
  height: 34px;
  border: 0;
  border-radius: 999px;
  background: #e9f3fa;
  color: #123046;
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
  color: #2e4b60;
  font-size: 13px;
  font-weight: 700;
}

:deep(.field input),
:deep(.field textarea),
:deep(.field select) {
  width: 100%;
  box-sizing: border-box;
  padding: 11px 13px;
  border: 1px solid #c9d9e6;
  border-radius: 14px;
  outline: none;
  font: inherit;
  color: #123046;
  background: #fff;
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
  border-top: 1px solid #e5edf3;
}

.btn {
  cursor: pointer;
  border: 0;
  border-radius: 12px;
  padding: 10px 16px;
  font-weight: 800;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.7;
}

.primary {
  color: #fff;
  background: #0f766e;
}

.secondary {
  color: #123046;
  background: #e9f3fa;
}

@media (max-width: 720px) {
  .form-grid {
    grid-template-columns: 1fr;
  }
}
</style>
