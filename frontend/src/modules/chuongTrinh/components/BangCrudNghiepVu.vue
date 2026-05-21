<template>
  <section :class="['bang-nghiep-vu', { disabled: disabled }]">
    <div class="bang-head">
      <div>
        <h3>{{ title }}</h3>
        <p v-if="description">{{ description }}</p>
      </div>
      <div class="bang-head-actions">
        <button type="button" class="btn small" @click="resetForm">Làm mới</button>
        <button type="button" class="btn small" @click="$emit('refresh')">Tải lại</button>
      </div>
    </div>

    <div v-if="parentText" class="parent-text">{{ parentText }}</div>
    <div v-if="disabled" class="disabled-text">{{ disabledText || 'Cần lưu và chọn dữ liệu ở bảng cha trước.' }}</div>

    <form class="bang-form" @submit.prevent="saveForm">
      <template v-for="field in visibleFields" :key="field.key">
        <label class="field" :class="field.wide ? 'wide' : ''">
          <span>{{ field.label }}</span>

          <select
              v-if="field.type === 'select'"
              v-model="form[field.key]"
              :required="field.required"
              :disabled="disabled || field.locked"
          >
            <option value="">-- Chọn --</option>
            <option
                v-for="option in getOptions(field)"
                :key="option[field.valueKey || 'id']"
                :value="option[field.valueKey || 'id']"
            >
              {{ getOptionLabel(option, field) }}
            </option>
          </select>

          <textarea
              v-else-if="field.type === 'textarea'"
              v-model="form[field.key]"
              :required="field.required"
              :disabled="disabled || field.locked"
              rows="2"
          />

          <select
              v-else-if="field.type === 'boolean'"
              v-model="form[field.key]"
              :required="field.required"
              :disabled="disabled || field.locked"
          >
            <option :value="true">Có</option>
            <option :value="false">Không</option>
          </select>

          <input
              v-else
              v-model="form[field.key]"
              :type="field.type || 'text'"
              :required="field.required"
              :disabled="disabled || field.locked"
          />
        </label>
      </template>

      <div class="form-actions">
        <button type="submit" class="btn primary" :disabled="disabled || saving">
          {{ editingId ? 'Cập nhật' : 'Lưu' }}
        </button>
        <button type="button" class="btn" :disabled="disabled" @click="resetForm">Hủy</button>
      </div>
    </form>

    <div class="table-wrap mini-scroll">
      <table>
        <thead>
        <tr>
          <th class="col-action">Chọn</th>
          <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
          <th class="col-action">CRUD</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="loading">
          <td :colspan="columns.length + 2" class="empty-cell">Đang tải dữ liệu...</td>
        </tr>
        <tr v-else-if="!items.length">
          <td :colspan="columns.length + 2" class="empty-cell">Chưa có dữ liệu</td>
        </tr>
        <template v-else>
          <tr
              v-for="item in items"
              :key="item.id"
              :class="{ selected: selectedId === item.id }"
          >
            <td class="col-action">
              <button type="button" class="btn tiny" @click="$emit('select', item)">✓</button>
            </td>
            <td v-for="column in columns" :key="column.key">
              {{ displayValue(item, column) }}
            </td>
            <td class="col-action actions-cell">
              <button type="button" class="btn tiny" @click="editItem(item)">Sửa</button>
              <button type="button" class="btn tiny danger" @click="deleteItem(item)">Xóa</button>
            </td>
          </tr>
        </template>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { computed, reactive, ref, watch } from 'vue'
import { layThongBaoLoi } from '../utils/layThongBaoLoi'

const props = defineProps({
  title: { type: String, required: true },
  description: { type: String, default: '' },
  columns: { type: Array, default: () => [] },
  fields: { type: Array, default: () => [] },
  defaultForm: { type: Object, default: () => ({}) },
  parentValues: { type: Object, default: () => ({}) },
  parentText: { type: String, default: '' },
  items: { type: Array, default: () => [] },
  lookups: { type: Object, default: () => ({}) },
  service: { type: Object, required: true },
  selectedId: { type: [Number, String], default: null },
  disabled: { type: Boolean, default: false },
  disabledText: { type: String, default: '' },
  loading: { type: Boolean, default: false }
})

const emit = defineEmits(['saved', 'deleted', 'select', 'refresh', 'notify'])

const form = reactive({})
const editingId = ref(null)
const saving = ref(false)

const visibleFields = computed(() => props.fields.filter((field) => !field.hidden))

function buildEmptyForm() {
  return {
    ...props.defaultForm,
    ...props.parentValues
  }
}

function syncForm(payload) {
  Object.keys(form).forEach((key) => delete form[key])
  Object.assign(form, payload)
}

function resetForm() {
  editingId.value = null
  syncForm(buildEmptyForm())
}

watch(
    () => [props.defaultForm, props.parentValues],
    () => {
      if (!editingId.value) resetForm()
    },
    { immediate: true, deep: true }
)

function getOptions(field) {
  return props.lookups[field.lookup] || field.options || []
}

function getOptionLabel(option, field) {
  if (!option) return ''

  const keys = Array.isArray(field.labelKey) ? field.labelKey : [field.labelKey || 'ten']
  const values = keys.map((key) => option[key]).filter(Boolean)

  if (values.length) return values.join(' - ')

  return option.ten || option.name || option.ma || option.id
}

function displayValue(item, column) {
  if (column.formatter) return column.formatter(item)

  const value = item[column.key]
  if (value === true) return 'Có'
  if (value === false) return 'Không'
  if (value === null || value === undefined || value === '') return '-'

  return value
}

function preparePayload() {
  const allowedKeys = new Set([
    ...Object.keys(props.defaultForm || {}),
    ...Object.keys(props.parentValues || {}),
    ...props.fields.map((field) => field.key)
  ])

  const source = {
    ...form,
    ...props.parentValues
  }

  const payload = {}

  allowedKeys.forEach((key) => {
    const value = source[key]
    payload[key] = value === '' ? null : value
  })

  return payload
}

async function saveForm() {
  if (props.disabled) return

  saving.value = true

  try {
    const payload = preparePayload()
    const saved = editingId.value
        ? await props.service.update(editingId.value, payload)
        : await props.service.create(payload)

    emit('notify', editingId.value ? 'Cập nhật thành công.' : 'Lưu thành công.')
    emit('saved', saved || payload)
    resetForm()
  } catch (error) {
    emit('notify', layThongBaoLoi(error, 'Lưu dữ liệu thất bại.'), 'error')
  } finally {
    saving.value = false
  }
}

function editItem(item) {
  editingId.value = item.id
  syncForm({
    ...props.defaultForm,
    ...props.parentValues,
    ...item
  })
}

async function deleteItem(item) {
  if (!item?.id) return
  if (!confirm('Xóa dòng dữ liệu này?')) return

  try {
    await props.service.delete(item.id)
    emit('notify', 'Xóa thành công.')
    emit('deleted', item)
    resetForm()
  } catch (error) {
    emit('notify', layThongBaoLoi(error, 'Xóa dữ liệu thất bại.'), 'error')
  }
}
</script>

<style scoped>
.bang-nghiep-vu {
  width: 100%;
  margin-bottom: 10px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
  overflow: hidden;
}

.bang-nghiep-vu.disabled {
  opacity: 0.76;
}

.bang-head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
  padding: 8px 10px;
  border-bottom: 1px solid #e5e7eb;
  background: #f9fafb;
}

.bang-head h3 {
  margin: 0;
  color: #111827;
  font-size: 13px;
  font-weight: 700;
}

.bang-head p {
  margin: 3px 0 0;
  color: #6b7280;
  font-size: 11px;
}

.bang-head-actions {
  display: flex;
  flex: 0 0 auto;
  gap: 5px;
}

.parent-text,
.disabled-text {
  margin: 8px 10px 0;
  padding: 6px 8px;
  border-radius: 3px;
  font-size: 12px;
}

.parent-text {
  border: 1px solid #bfdbfe;
  background: #eff6ff;
  color: #1d4ed8;
}

.disabled-text {
  border: 1px solid #fde68a;
  background: #fffbeb;
  color: #92400e;
}

.bang-form {
  display: grid;
  grid-template-columns: repeat(6, minmax(120px, 1fr));
  gap: 8px;
  padding: 10px;
  border-bottom: 1px solid #e5e7eb;
}

.field {
  display: flex;
  flex-direction: column;
  min-width: 0;
  gap: 3px;
}

.field.wide {
  grid-column: span 2;
}

.field span {
  color: #374151;
  font-size: 11px;
  font-weight: 600;
}

.field input,
.field select,
.field textarea {
  width: 100%;
  min-height: 28px;
  border: 1px solid #cbd5e1;
  border-radius: 3px;
  background: #ffffff;
  color: #111827;
  padding: 4px 7px;
  font-size: 12px;
  outline: none;
}

.field textarea {
  resize: vertical;
}

.field input:focus,
.field select:focus,
.field textarea:focus {
  border-color: #2563eb;
}

.field input:disabled,
.field select:disabled,
.field textarea:disabled {
  background: #f3f4f6;
  color: #6b7280;
}

.form-actions {
  display: flex;
  align-items: end;
  gap: 5px;
}

.btn {
  min-height: 28px;
  border: 1px solid #cbd5e1;
  border-radius: 3px;
  background: #ffffff;
  color: #111827;
  padding: 4px 10px;
  font-size: 12px;
  line-height: 16px;
  cursor: pointer;
  white-space: nowrap;
}

.btn:hover {
  background: #f8fafc;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: #2563eb;
  background: #2563eb;
  color: #ffffff;
}

.btn.danger {
  border-color: #fecaca;
  background: #fff1f2;
  color: #b91c1c;
}

.btn.small,
.btn.tiny {
  min-height: 24px;
  padding: 2px 7px;
  font-size: 11px;
}

.table-wrap {
  width: 100%;
  overflow: auto;
}

table {
  width: 100%;
  min-width: 880px;
  border-collapse: collapse;
  font-size: 12px;
}

th,
td {
  border-bottom: 1px solid #e5e7eb;
  padding: 6px 8px;
  text-align: left;
  vertical-align: top;
}

th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f3f4f6;
  color: #374151;
  font-size: 11px;
  font-weight: 700;
}

td {
  color: #111827;
}

tr.selected td {
  background: #eff6ff;
}

.col-action {
  width: 86px;
  white-space: nowrap;
}

.actions-cell {
  display: flex;
  gap: 4px;
}

.empty-cell {
  color: #6b7280;
  text-align: center;
}

.mini-scroll {
  scrollbar-width: thin;
  scrollbar-color: #9ca3af #f3f4f6;
}

.mini-scroll::-webkit-scrollbar {
  height: 6px;
  width: 6px;
}

.mini-scroll::-webkit-scrollbar-thumb {
  background: #9ca3af;
  border-radius: 999px;
}

.mini-scroll::-webkit-scrollbar-track {
  background: #f3f4f6;
}

@media (max-width: 1100px) {
  .bang-form {
    grid-template-columns: repeat(3, minmax(120px, 1fr));
  }
}

@media (max-width: 700px) {
  .bang-head {
    flex-direction: column;
  }

  .bang-form {
    grid-template-columns: 1fr;
  }

  .field.wide {
    grid-column: auto;
  }
}
</style>
