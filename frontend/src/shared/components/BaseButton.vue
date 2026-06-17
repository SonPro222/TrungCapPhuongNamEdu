<template>
  <button
      :type="type"
      class="base-button"
      :class="buttonClass"
      :disabled="disabled || loading"
  >
    {{ loading ? loadingText : label }}
  </button>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  label: {
    type: String,
    default: 'Button'
  },
  type: {
    type: String,
    default: 'button'
  },
  variant: {
    type: String,
    default: 'default'
  },
  disabled: {
    type: Boolean,
    default: false
  },
  loading: {
    type: Boolean,
    default: false
  },
  loadingText: {
    type: String,
    default: 'Đang xử lý...'
  }
})

const buttonClass = computed(() => `base-button--${props.variant}`)
</script>