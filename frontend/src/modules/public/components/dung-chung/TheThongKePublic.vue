<template>
  <article class="the-thong-ke-public">
    <div class="the-thong-ke-icon">
      <i v-if="iconClass" :class="iconClass"></i>
      <span v-else>{{ icon }}</span>
    </div>

    <div>
      <strong>{{ soLieu }}</strong>
      <p>{{ noiDung }}</p>
    </div>
  </article>
</template>

<script setup>
defineProps({
  icon: {
    type: String,
    default: ''
  },
  iconClass: {
    type: String,
    default: ''
  },
  soLieu: {
    type: String,
    required: true
  },
  noiDung: {
    type: String,
    required: true
  }
})
</script>