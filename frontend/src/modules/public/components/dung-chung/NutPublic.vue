<template>
  <RouterLink
      v-if="to"
      :to="to"
      class="nut-public"
      :class="loai"
  >
    <slot />
  </RouterLink>

  <a
      v-else-if="href"
      :href="href"
      class="nut-public"
      :class="loai"
  >
    <slot />
  </a>

  <button
      v-else
      type="button"
      class="nut-public"
      :class="loai"
      @click="$emit('click')"
  >
    <slot />
  </button>
</template>

<script setup>
defineEmits(['click'])

defineProps({
  to: {
    type: [String, Object],
    default: null
  },
  href: {
    type: String,
    default: ''
  },
  loai: {
    type: String,
    default: 'chinh'
  }
})
</script>