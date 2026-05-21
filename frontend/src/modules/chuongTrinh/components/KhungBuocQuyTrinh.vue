<template>
  <section :class="['khung-buoc', { inactive: !active }]">
    <div :class="['khung-header', softClass]">
      <div :class="['step-num', bgClass]">{{ stepNum }}</div>
      <h2 :class="textClass">{{ title }}</h2>
      <span v-if="dbName" class="db-name">({{ dbName }})</span>
    </div>
    <div class="khung-body">
      <slot />
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  stepNum: { type: [String, Number], required: true },
  title: { type: String, required: true },
  dbName: { type: String, default: '' },
  theme: { type: String, default: 'blue' },
  active: { type: Boolean, default: true }
})

const textClass = computed(() => `text-${props.theme}`)
const bgClass = computed(() => `bg-${props.theme}`)
const softClass = computed(() => `soft-${props.theme}`)
</script>

<style scoped>
.khung-buoc {
  margin-bottom: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  background: #ffffff;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.05);
  transition: opacity 0.2s ease;
}

.khung-buoc.inactive {
  opacity: 0.42;
  pointer-events: none;
  border-style: dashed;
}

.khung-header {
  display: flex;
  align-items: center;
  min-height: 43px;
  padding: 8px 16px;
  border-bottom: 1px solid #f1f5f9;
}

.step-num {
  width: 24px;
  height: 24px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 5px;
  color: #ffffff;
  font-size: 13px;
  font-weight: 800;
  margin-right: 8px;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.18);
}

h2 {
  margin: 0;
  font-size: 14px;
  line-height: 20px;
  font-weight: 800;
  text-transform: uppercase;
}

.db-name {
  margin-left: 6px;
  font-size: 12px;
  color: #64748b;
  font-weight: 600;
}

.khung-body {
  padding: 12px;
  background: #f8fafc;
}

.text-blue { color: #2563eb; }
.text-indigo { color: #1e40af; }
.text-purple { color: #7e22ce; }
.text-orange { color: #ea580c; }
.text-green { color: #16a34a; }
.text-slate { color: #475569; }

.bg-blue { background: #2563eb; }
.bg-indigo { background: #1e40af; }
.bg-purple { background: #7e22ce; }
.bg-orange { background: #ea580c; }
.bg-green { background: #16a34a; }
.bg-slate { background: #475569; }

.soft-blue { background: rgba(239, 246, 255, 0.55); }
.soft-indigo { background: rgba(239, 246, 255, 0.65); }
.soft-purple { background: rgba(250, 245, 255, 0.75); }
.soft-orange { background: rgba(255, 247, 237, 0.78); }
.soft-green { background: rgba(240, 253, 244, 0.78); }
.soft-slate { background: rgba(248, 250, 252, 0.9); }
</style>
