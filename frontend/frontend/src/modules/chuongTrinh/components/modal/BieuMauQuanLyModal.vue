<template>
  <div v-if="open" class="ct-modal-backdrop" @click.self="$emit('close')">
    <section class="ct-modal">
      <header class="ct-modal-head">
        <div>
          <div class="ct-kicker">{{ mode === 'edit' ? 'Cập nhật dữ liệu' : 'Thêm dữ liệu' }}</div>
          <h3>{{ title }}</h3>
        </div>
        <button class="ct-close" type="button" @click="$emit('close')">×</button>
      </header>

      <form @submit.prevent="$emit('submit')">
        <div class="ct-modal-body">
          <div class="ct-form-grid">
            <label v-for="field in fields" :key="field.key" :class="['ct-field', { wide: field.wide || field.type === 'textarea' }]">
              <span>{{ field.label }} <b v-if="field.required" class="ct-danger-text">*</b></span>
              <textarea
                v-if="field.type === 'textarea'"
                class="ct-input"
                :required="field.required"
                :value="modelValue[field.key] ?? ''"
                @input="updateField(field, $event.target.value)"
              />
              <label v-else-if="field.type === 'checkbox'" class="ct-field-check">
                <input
                  type="checkbox"
                  :checked="Boolean(modelValue[field.key])"
                  @change="updateField(field, $event.target.checked)"
                />
                <span>Kích hoạt</span>
              </label>
              <input
                v-else
                class="ct-input"
                :type="field.type || 'text'"
                :required="field.required"
                :value="modelValue[field.key] ?? ''"
                @input="updateField(field, $event.target.value)"
              />
            </label>
          </div>
        </div>

        <footer class="ct-modal-actions">
          <button class="ct-btn light" type="button" @click="$emit('close')">Hủy</button>
          <button class="ct-btn" type="submit" :disabled="saving">{{ saving ? 'Đang lưu...' : 'Lưu' }}</button>
        </footer>
      </form>
    </section>
  </div>
</template>

<script setup>
const props = defineProps({
  open: { type: Boolean, default: false },
  title: { type: String, default: 'Biểu mẫu' },
  mode: { type: String, default: 'create' },
  fields: { type: Array, default: () => [] },
  modelValue: { type: Object, default: () => ({}) },
  saving: { type: Boolean, default: false },
});

const emit = defineEmits(['update:modelValue', 'submit', 'close']);

const updateField = (field, rawValue) => {
  let value = rawValue;
  if (field.type === 'number') value = rawValue === '' ? '' : Number(rawValue);
  if (field.type === 'checkbox') value = Boolean(rawValue);
  emit('update:modelValue', { ...props.modelValue, [field.key]: value });
};
</script>

<style scoped>
.ct-field-check{display:flex;align-items:center;gap:10px;min-height:42px;border:1px solid #cbd5e1;border-radius:8px;padding:0 12px;background:#fff}.ct-field-check input{width:16px;height:16px}.ct-field-check span{font-size:14px;color:#334155;text-transform:none;letter-spacing:0}
</style>
