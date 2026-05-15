<script setup>
const emit = defineEmits(['update:modelValue', 'submit', 'cancel']);

const props = defineProps({
  fields: { type: Array, default: () => [] },
  modelValue: { type: Object, required: true },
  saving: { type: Boolean, default: false },
});

const updateField = (field, rawValue) => {
  let value = rawValue;

  if (field.type === 'number') {
    value = rawValue === '' || rawValue === null || rawValue === undefined ? '' : Number(rawValue);
  }

  if (field.type === 'checkbox') {
    value = Boolean(rawValue);
  }

  emit('update:modelValue', { ...props.modelValue, [field.key]: value });
};
</script>

<template>
  <form class="ct-form" @submit.prevent="$emit('submit')">
    <label v-for="field in fields" :key="field.key" :class="['ct-field', { 'ct-field--wide': field.wide || field.type === 'textarea' }]">
      <span class="ct-label">{{ field.label }} <b v-if="field.required">*</b></span>

      <textarea
        v-if="field.type === 'textarea'"
        :value="modelValue[field.key]"
        :required="field.required"
        rows="3"
        class="ct-control"
        @input="updateField(field, $event.target.value)"
      />

      <label v-else-if="field.type === 'checkbox'" class="ct-check">
        <input
          type="checkbox"
          :checked="Boolean(modelValue[field.key])"
          @change="updateField(field, $event.target.checked)"
        />
        <span>Kích hoạt</span>
      </label>

      <input
        v-else
        :type="field.type || 'text'"
        :value="modelValue[field.key]"
        :required="field.required"
        class="ct-control"
        @input="updateField(field, $event.target.value)"
      />
    </label>

    <div class="ct-form-actions">
      <button type="button" class="ct-btn ct-btn--light" @click="$emit('cancel')">Hủy</button>
      <button type="submit" class="ct-btn ct-btn--primary" :disabled="saving">{{ saving ? 'Đang lưu...' : 'Lưu dữ liệu' }}</button>
    </div>
  </form>
</template>

<style scoped>
.ct-form{display:grid;grid-template-columns:repeat(2,minmax(0,1fr));gap:12px}.ct-field{display:grid;gap:5px}.ct-field--wide{grid-column:1/-1}.ct-label{font-size:12px;font-weight:700;color:#475569}.ct-label b{color:#dc2626}.ct-control{width:100%;border:1px solid #dbe3ef;border-radius:9px;padding:8px 10px;background:#fff;font-size:13px;color:#111827;outline:none}.ct-control:focus{border-color:#2563eb;box-shadow:0 0 0 3px rgba(37,99,235,.1)}.ct-check{display:flex;align-items:center;gap:8px;min-height:36px;font-size:13px;color:#334155}.ct-form-actions{grid-column:1/-1;display:flex;justify-content:flex-end;gap:8px;padding-top:6px}.ct-btn{border:0;border-radius:8px;padding:8px 12px;font-size:13px;font-weight:700;cursor:pointer}.ct-btn:disabled{opacity:.65;cursor:not-allowed}.ct-btn--light{background:#f1f5f9;color:#334155}.ct-btn--primary{background:#2563eb;color:#fff}@media(max-width:680px){.ct-form{grid-template-columns:1fr}}
</style>
