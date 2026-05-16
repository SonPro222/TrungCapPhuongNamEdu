<template>
  <section class="build-section" :class="{ locked }">
    <header class="build-section-header">
      <div>
        <h3>{{ title }}</h3>
        <p v-if="description">{{ description }}</p>
      </div>

      <div class="build-section-actions">
        <BaseButton
            label="Lưu bước"
            variant="primary"
            :disabled="disabled"
            @click="$emit('save')"
        />

        <BaseButton
            :label="locked ? 'Mở khóa' : 'Khóa bước'"
            :disabled="disabled"
            @click="$emit('toggle-lock')"
        />
      </div>
    </header>

    <div class="build-section-body">
      <slot />
    </div>
  </section>
</template>

<script setup>
import BaseButton from '@/shared/components/BaseButton.vue'

defineProps({
  title: {
    type: String,
    required: true
  },
  description: {
    type: String,
    default: ''
  },
  locked: {
    type: Boolean,
    default: false
  },
  disabled: {
    type: Boolean,
    default: false
  }
})

defineEmits(['save', 'toggle-lock'])
</script>

<style scoped>
.build-section {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  margin-bottom: 12px;
  overflow: hidden;
  background: var(--color-white);
}

.build-section.locked {
  border-color: var(--color-primary);
}

.build-section-header {
  background: #f9fafb;
  padding: 12px 14px;
  border-bottom: 1px solid var(--color-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.build-section-header h3 {
  margin: 0;
  font-size: 15px;
}

.build-section-header p {
  margin: 4px 0 0;
  color: var(--color-muted);
  font-size: 13px;
}

.build-section-actions {
  display: flex;
  gap: 8px;
}

.build-section-body {
  padding: 14px;
}
</style>