<template>
  <div class="form-input">
    <label v-if="label">{{ label }}</label>

    <select
        :value="modelValue"
        :required="required"
        @change="handleChange"
    >
      <option value="">
        {{ placeholder }}
      </option>

      <option
          v-for="option in options"
          :key="option[valueKey]"
          :value="option[valueKey]"
      >
        {{ option[labelKey] }}
      </option>
    </select>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: [String, Number, null],
    default: null
  },
  label: {
    type: String,
    default: ''
  },
  placeholder: {
    type: String,
    default: 'Chọn dữ liệu'
  },
  options: {
    type: Array,
    default: () => []
  },
  valueKey: {
    type: String,
    default: 'id'
  },
  labelKey: {
    type: String,
    default: 'ten'
  },
  valueType: {
    type: String,
    default: 'number'
  },
  required: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['update:modelValue'])

function handleChange(event) {
  const value = event.target.value

  if (value === '') {
    emit('update:modelValue', null)
    return
  }

  if (props.valueType === 'number') {
    emit('update:modelValue', Number(value))
    return
  }

  emit('update:modelValue', value)
}
</script>