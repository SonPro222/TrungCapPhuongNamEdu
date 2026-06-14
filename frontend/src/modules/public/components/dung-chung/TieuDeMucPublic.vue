<template>
  <div class="tieu-de-muc-public" :class="{ 'can-trai': canTrai }">
    <span></span>
    <h2>{{ tieuDe }}</h2>
    <span></span>
  </div>
</template>

<script setup>
defineProps({
  tieuDe: {
    type: String,
    required: true
  },
  canTrai: {
    type: Boolean,
    default: false
  }
})
</script>