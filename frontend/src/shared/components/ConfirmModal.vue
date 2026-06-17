<template>
  <div v-if="modelValue" class="modal-backdrop">
    <div class="confirm-modal">
      <h3>{{ title }}</h3>
      <p>{{ message }}</p>

      <div class="modal-actions">
        <button type="button" @click="$emit('update:modelValue', false)">
          Hủy
        </button>

        <button type="button" class="danger" @click="$emit('confirm')">
          Xác nhận
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  modelValue: {
    type: Boolean,
    required: true
  },
  title: {
    type: String,
    default: 'Xác nhận'
  },
  message: {
    type: String,
    default: 'Bạn có chắc muốn thực hiện thao tác này?'
  }
})

defineEmits(['update:modelValue', 'confirm'])
</script>