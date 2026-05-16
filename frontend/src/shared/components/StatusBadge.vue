<template>
  <span class="status-badge" :class="statusClass">
    {{ label }}
  </span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  status: {
    type: String,
    default: ''
  },
  label: {
    type: String,
    default: ''
  }
})

const statusClass = computed(() => {
  return `status-${props.status?.toLowerCase()}`
})
</script>