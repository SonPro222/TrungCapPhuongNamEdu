<template>
  <span :class="['lms-badge', colorClass]">{{ label }}</span>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  value: String,
  map: Object,  // { value: label }
  colorFn: Function, // value => colorClass
  label: String, // override label
})

const label = computed(() => props.label || (props.map && props.map[props.value]) || props.value || '—')
const colorClass = computed(() => props.colorFn ? props.colorFn(props.value) : 'badge-gray')
</script>

<style scoped>
.lms-badge {
  display: inline-flex;
  align-items: center;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 11.5px;
  font-weight: 700;
  letter-spacing: 0.02em;
  white-space: nowrap;
}
.badge-gray   { background: #f1f5f9; color: #64748b; }
.badge-green  { background: #dcfce7; color: #15803d; }
.badge-blue   { background: #dbeafe; color: #1d4ed8; }
.badge-indigo { background: #e0e7ff; color: #4338ca; }
.badge-purple { background: #f3e8ff; color: #7c3aed; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.badge-red    { background: #fee2e2; color: #b91c1c; }
.badge-yellow { background: #fef9c3; color: #a16207; }
</style>
