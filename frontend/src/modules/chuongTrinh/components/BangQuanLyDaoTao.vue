<template>
  <section class="bang-quan-ly">
    <div class="bang-head">
      <div>
        <h3>{{ title }}</h3>
        <p v-if="description">{{ description }}</p>
      </div>
      <div class="head-actions">
        <button type="button" class="btn tiny" @click="resetForm">Làm mới form</button>
        <button type="button" class="btn tiny" @click="collapsed = !collapsed">{{ collapsed ? 'Mở' : 'Đóng' }}</button>
      </div>
    </div>

    <div v-show="!collapsed">
      <div v-if="parentText" class="parent-text">{{ parentText }}</div>
      <div v-if="disabled" class="disabled-text">{{ disabledText || 'Cần chọn dữ liệu tầng cha trước.' }}</div>
      <div v-if="message" :class="['message', messageType]">{{ message }}</div>

      <form class="form-grid" @submit.prevent="save">
        <label v-for="field in visibleFields" :key="field.key" :class="['field', { wide: field.wide }]">
          <span>{{ field.label }}<em v-if="field.required">*</em></span>
          <select v-if="field.type === 'select'" v-model="form[field.key]" :disabled="disabled || field.locked">
            <option value="">-- Chọn --</option>
            <option v-for="option in getOptions(field)" :key="getOptionValue(option, field)" :value="getOptionValue(option, field)">
              {{ getOptionLabel(option, field) }}
            </option>
          </select>
          <textarea v-else-if="field.type === 'textarea'" v-model="form[field.key]" :disabled="disabled || field.locked" rows="2" />
          <select v-else-if="field.type === 'boolean'" v-model="form[field.key]" :disabled="disabled || field.locked">
            <option :value="true">Có</option>
            <option :value="false">Không</option>
          </select>
          <input v-else v-model="form[field.key]" :type="field.type || 'text'" :disabled="disabled || field.locked" :min="field.type === 'number' ? (field.min ?? 0) : undefined" :max="field.type === 'number' ? (field.max ?? 999999.9) : undefined" :step="field.type === 'number' ? (field.step ?? (field.key === 'thuTu' ? 1 : 0.1)) : undefined" />
        </label>

        <div class="form-actions">
          <button class="btn primary" :disabled="disabled || saving" type="submit">{{ editingId ? 'Cập nhật' : 'Lưu' }}</button>
          <button class="btn" :disabled="disabled" type="button" @click="resetForm">Hủy</button>
        </div>
      </form>

      <div class="table-title">Dữ liệu tại tầng này</div>
      <div class="table-wrap mini-scroll">
        <table>
          <thead>
          <tr>
            <th class="col-action">Thao tác</th>
            <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
          </tr>
          </thead>
          <tbody>
          <tr v-if="!rows.length">
            <td :colspan="columns.length + 1" class="empty">Chưa có dữ liệu.</td>
          </tr>
          <tr v-for="item in rows" :key="item.id" :class="{ selected: String(selectedId || '') === String(item.id || '') }">
            <td class="action-cell">
              <button v-if="showSelect" type="button" class="btn tiny" @click="$emit('select', item)">Chọn</button>
              <button v-if="showView" type="button" class="btn tiny view" @click="$emit('view', item)">Xem</button>
              <button type="button" class="btn tiny" @click="edit(item)">Sửa</button>
              <button type="button" class="btn tiny danger" @click="remove(item)">Xóa</button>
            </td>
            <td v-for="column in columns" :key="column.key">{{ displayValue(item, column) }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { layThongBaoLoi } from '../utils/layThongBaoLoi'

const props = defineProps({
  title: { type: String, required: true },
  description: { type: String, default: '' },
  fields: { type: Array, default: () => [] },
  columns: { type: Array, default: () => [] },
  rows: { type: Array, default: () => [] },
  service: { type: Object, required: true },
  defaultForm: { type: Object, default: () => ({}) },
  parentValues: { type: Object, default: () => ({}) },
  parentText: { type: String, default: '' },
  lookups: { type: Object, default: () => ({}) },
  disabled: { type: Boolean, default: false },
  disabledText: { type: String, default: '' },
  selectedId: { type: [String, Number], default: null },
  showView: { type: Boolean, default: true },
  showSelect: { type: Boolean, default: true }
})

const emit = defineEmits(['saved', 'deleted', 'select', 'view'])
const form = reactive({})
const editingId = ref(null)
const saving = ref(false)
const collapsed = ref(false)
const message = ref('')
const messageType = ref('success')
const visibleFields = computed(() => props.fields.filter((field) => !field.hidden))

function buildForm() {
  return { ...props.defaultForm, ...props.parentValues }
}
function syncForm(payload) {
  Object.keys(form).forEach((key) => delete form[key])
  Object.assign(form, payload)
}
function resetForm() {
  editingId.value = null
  syncForm(buildForm())
}
watch(() => [props.defaultForm, props.parentValues], () => { if (!editingId.value) resetForm() }, { immediate: true, deep: true })

function notify(text, type = 'success') {
  message.value = text
  messageType.value = type
  setTimeout(() => { if (message.value === text) message.value = '' }, 3000)
}
function getOptions(field) {
  return field.options || props.lookups[field.lookup] || []
}
function getOptionValue(option, field) {
  return option?.[field.valueKey || 'id'] ?? option?.value
}
function getOptionLabel(option, field) {
  if (!option) return ''
  const keys = Array.isArray(field.labelKey) ? field.labelKey : [field.labelKey || 'ten']
  const label = keys.map((key) => option[key]).filter(Boolean).join(' - ')
  return label || option.ten || option.label || option.name || option.ma || option.id || option.value
}
function displayValue(item, column) {
  if (column.formatter) return column.formatter(item)
  const value = item?.[column.key]
  if (value === true) return 'Có'
  if (value === false) return 'Không'
  return value === null || value === undefined || value === '' ? '-' : value
}
function normalizePayload() {
  const payload = { ...form, ...props.parentValues }
  props.fields.forEach((field) => {
    if (field.type === 'number' && payload[field.key] !== '' && payload[field.key] !== null && payload[field.key] !== undefined) {
      payload[field.key] = Number(payload[field.key])
    }
    if (payload[field.key] === '') payload[field.key] = null
  })
  return payload
}
async function save() {
  if (props.disabled) return
  try {
    saving.value = true
    const payload = normalizePayload()
    const saved = editingId.value ? await props.service.update(editingId.value, payload) : await props.service.create(payload)
    notify(editingId.value ? 'Cập nhật thành công.' : 'Lưu thành công.')
    emit('saved', saved?.data || saved)
    resetForm()
  } catch (error) {
    notify(layThongBaoLoi(error, 'Lưu thất bại.'), 'error')
  } finally {
    saving.value = false
  }
}
function edit(item) {
  editingId.value = item.id
  syncForm({ ...props.defaultForm, ...props.parentValues, ...item })
}
async function remove(item) {
  if (!item?.id || !confirm('Xóa dòng này?')) return
  try {
    await props.service.delete(item.id)
    notify('Xóa thành công.')
    emit('deleted', item)
  } catch (error) {
    notify(layThongBaoLoi(error, 'Xóa thất bại.'), 'error')
  }
}
</script>

<style scoped>
.bang-quan-ly{background:#fff;border:1px solid #d1d5db;border-radius:4px;margin-bottom:10px;overflow:hidden}.bang-head{display:flex;justify-content:space-between;gap:10px;padding:9px 10px;background:#f8fafc;border-bottom:1px solid #e5e7eb}.bang-head h3{margin:0;font-size:14px}.bang-head p{margin:3px 0 0;color:#64748b;font-size:12px}.head-actions,.form-actions,.action-cell{display:flex;gap:5px;flex-wrap:wrap}.parent-text,.disabled-text,.message{margin:8px 10px 0;padding:6px 8px;border-radius:3px;font-size:12px}.parent-text{background:#eff6ff;color:#1d4ed8;border:1px solid #bfdbfe}.disabled-text{background:#fffbeb;color:#92400e;border:1px solid #fde68a}.message{background:#f0fdf4;color:#166534;border:1px solid #bbf7d0}.message.error{background:#fef2f2;color:#b91c1c;border-color:#fecaca}.form-grid{display:grid;grid-template-columns:repeat(6,minmax(120px,1fr));gap:8px;padding:10px;border-bottom:1px solid #e5e7eb}.field{display:flex;flex-direction:column;gap:3px}.field.wide{grid-column:span 2}.field span{font-size:11px;font-weight:700;color:#374151}.field em{color:#dc2626;font-style:normal}.field input,.field select,.field textarea{min-height:28px;border:1px solid #cbd5e1;border-radius:3px;padding:4px 7px;font-size:12px;background:#fff}.form-actions{align-items:end}.btn{border:1px solid #cbd5e1;background:#fff;border-radius:3px;padding:4px 9px;font-size:12px;cursor:pointer}.btn.tiny{font-size:11px;padding:3px 7px}.btn.primary{background:#2563eb;border-color:#2563eb;color:#fff}.btn.danger{background:#fff1f2;border-color:#fecaca;color:#b91c1c}.btn.view{color:#1d4ed8}.table-title{padding:7px 10px 0;color:#64748b;font-size:11px;font-weight:700}.table-wrap{overflow:auto;max-height:360px;padding-top:6px}.mini-scroll{scrollbar-width:thin;scrollbar-color:#9ca3af #f3f4f6}.mini-scroll::-webkit-scrollbar{height:6px;width:6px}.mini-scroll::-webkit-scrollbar-thumb{background:#9ca3af;border-radius:999px}table{width:100%;min-width:860px;border-collapse:collapse;font-size:12px}th,td{border-top:1px solid #e5e7eb;padding:6px 8px;text-align:left;vertical-align:top}th{position:sticky;top:0;background:#f3f4f6;z-index:1}.col-action{width:190px}.selected td{background:#ecfdf5}.empty{text-align:center;color:#64748b}@media(max-width:1000px){.form-grid{grid-template-columns:repeat(3,minmax(120px,1fr))}}@media(max-width:700px){.form-grid{grid-template-columns:1fr}.field.wide{grid-column:auto}.bang-head{flex-direction:column}}
</style>
