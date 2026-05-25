<template>
  <section :class="['bang-them-nghiep-vu', `bang-${loaiBang}`, { disabled, collapsed: !moBang }]">
    <div class="bang-head">
      <div>
        <h3>{{ title }}</h3>
        <p v-if="description">{{ description }}</p>
      </div>
      <div class="bang-head-actions">
        <button type="button" class="btn tiny" :disabled="disabled" @click="resetForm">Làm mới form</button>
        <button type="button" class="btn tiny toggle-btn" @click="moBang = !moBang">
          {{ moBang ? '⌃ Đóng' : '⌄ Mở' }}
        </button>
      </div>
    </div>

    <div v-show="moBang">
      <div v-if="parentText" class="parent-text">{{ parentText }}</div>
      <div v-if="disabled" class="disabled-text">{{ disabledText || 'Cần tạo và chọn dữ liệu bảng cha trước.' }}</div>

      <form class="bang-form" novalidate @submit.prevent="saveForm">
      <template v-for="field in visibleFields" :key="field.key">
        <label class="field" :class="[field.wide ? 'wide' : '', fieldErrors[field.key] ? 'has-error' : '']">
          <span>{{ field.label }}<em v-if="isFieldRequired(field)">*</em></span>

          <select
              v-if="field.type === 'select'"
              v-model="form[field.key]"
              :required="field.required"
              :disabled="disabled || field.locked"
              @change="validateField(field)"
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
              @input="validateField(field)"
          />

          <select
              v-else-if="field.type === 'boolean'"
              v-model="form[field.key]"
              :required="field.required"
              :disabled="disabled || field.locked"
              @change="validateField(field)"
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
              :min="field.type === 'number' ? layMin(field) : undefined"
              :max="field.type === 'number' ? layMax(field) : undefined"
              :step="field.type === 'number' ? layStep(field) : undefined"
              @input="validateField(field)"
          />

          <small v-if="fieldErrors[field.key]" class="field-error">{{ fieldErrors[field.key] }}</small>
        </label>
      </template>

      <div class="form-actions">
        <button type="submit" class="btn primary" :disabled="disabled || saving">
          {{ editingId ? 'Cập nhật' : 'Lưu' }}
        </button>
        <button type="button" class="btn" :disabled="disabled" @click="resetForm">Hủy</button>
      </div>
    </form>

    <div v-if="thongBaoBang" :class="['table-message', loaiThongBaoBang]">{{ thongBaoBang }}</div>
    <div v-if="canToggleSave" class="save-status">
      <span>Trạng thái lưu:</span>
      <b>Đã lưu</b> là đã gắn vào luồng đang chọn, <b>Chưa lưu</b> là chưa gắn. Bấm nút để lưu hoặc bỏ lưu.
    </div>
    <div class="table-title">Dữ liệu có sẵn / vừa tạo trong luồng này</div>
    <div class="table-wrap mini-scroll">
      <table>
        <thead>
        <tr>
          <th v-if="hienCotTrangThai" class="col-action col-action-wide">Trạng thái</th>
          <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
          <th class="col-action">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="!hienThiItems.length">
          <td :colspan="columns.length + (hienCotTrangThai ? 2 : 1)" class="empty-cell">Bảng đang trống. Nhập form phía trên rồi bấm Lưu.</td>
        </tr>
        <template v-else>
          <tr
              v-for="item in hienThiItems"
              :key="item.id || item.__localId"
              :class="{ selected: isSelected(item), related: isRelated(item), viewing: isViewing(item) }"
          >
            <td v-if="hienCotTrangThai" class="col-action col-action-wide action-pair status-action-cell">
              <span :class="['status-badge', layTrangThaiDong(item).className]">{{ layTrangThaiDong(item).label }}</span>
              <button
                  v-if="canSelect"
                  type="button"
                  :class="['btn tiny choose-btn', { chosen: isSelected(item) }]"
                  title="Chọn dòng này trong ngữ cảnh hiện tại"
                  @click="selectItem(item)"
              >✓</button>
              <button
                  v-if="canToggleSave"
                  type="button"
                  :class="['btn tiny save-link-btn', { saved: isSaved(item) }]"
                  :title="isSaved(item) ? 'Bỏ lưu khỏi ngữ cảnh đang chọn' : 'Lưu/gán vào ngữ cảnh đang chọn'"
                  @click="toggleSaveItem(item)"
              >
                {{ isSaved(item) ? 'Bỏ lưu' : 'Lưu' }}
              </button>
            </td>
            <td v-for="column in columns" :key="column.key" :class="cellClass(column)">
              {{ displayValue(item, column) }}
            </td>
            <td class="col-action actions-cell">
              <button v-if="canView" type="button" class="btn tiny view-btn" @click="viewItem(item)">{{ viewLabel }}</button>
              <button type="button" class="btn tiny" @click="editItem(item)">Sửa</button>
              <button type="button" class="btn tiny danger" @click="deleteItem(item)">Xóa</button>
            </td>
          </tr>
        </template>
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
  columns: { type: Array, default: () => [] },
  fields: { type: Array, default: () => [] },
  defaultForm: { type: Object, default: () => ({}) },
  parentValues: { type: Object, default: () => ({}) },
  parentText: { type: String, default: '' },
  items: { type: Array, default: () => [] },
  allItems: { type: Array, default: () => [] },
  lookups: { type: Object, default: () => ({}) },
  service: { type: Object, required: true },
  selectedId: { type: [Number, String], default: null },
  selectedIds: { type: Array, default: () => [] },
  savedIds: { type: Array, default: () => [] },
  tableMessage: { type: Object, default: () => ({}) },
  canToggleSave: { type: Boolean, default: false },
  canSelect: { type: Boolean, default: true },
  canShowSavedStatus: { type: Boolean, default: false },
  canView: { type: Boolean, default: false },
  viewLabel: { type: String, default: 'Xem thêm' },
  viewedId: { type: [Number, String], default: null },
  excludedSelectedIds: { type: Array, default: () => [] },
  uniqueRules: { type: Array, default: () => [] },
  requiredParentKeys: { type: Array, default: () => [] },
  allowToggleSelect: { type: Boolean, default: true },
  loaiBang: {
    type: String,
    default: 'phu',
    validator: (value) => ['xuong-song', 'phu', 'goc-mau'].includes(value)
  },
  disabled: { type: Boolean, default: false },
  disabledText: { type: String, default: '' }
})

const emit = defineEmits(['saved', 'deleted', 'select', 'view', 'toggle-save', 'notify'])

const form = reactive({})
const fieldErrors = reactive({})
const localMessage = ref('')
const localMessageType = ref('success')
const editingId = ref(null)
const saving = ref(false)
const moBang = ref(true)

const cachedItems = ref([])

function canGiuDanhSachKhiPropsRong() {
  return props.loaiBang === 'goc-mau' && String(props.title || '').includes('Quy đổi điểm mẫu')
}

watch(
    () => props.items,
    (items) => {
      if (Array.isArray(items) && items.length) {
        cachedItems.value = items.map((item) => ({ ...item }))
      }
    },
    { immediate: true, deep: true }
)

const hienThiItems = computed(() => {
  if (Array.isArray(props.items) && props.items.length) return props.items
  if (canGiuDanhSachKhiPropsRong() && cachedItems.value.length) return cachedItems.value
  return props.items || []
})

const visibleFields = computed(() => props.fields.filter((field) => !field.hidden))
const thongBaoBang = computed(() => props.tableMessage?.message || localMessage.value)
const loaiThongBaoBang = computed(() => props.tableMessage?.type || localMessageType.value)
const hienCotTrangThai = computed(() => props.canSelect || props.canToggleSave || props.canShowSavedStatus)

function baoTinTaiBang(message, type = 'success') {
  localMessage.value = message
  localMessageType.value = type
  setTimeout(() => {
    if (localMessage.value === message) localMessage.value = ''
  }, 5000)
}

function buildEmptyForm() {
  return {
    ...props.defaultForm,
    ...props.parentValues
  }
}

function syncForm(payload) {
  Object.keys(form).forEach((key) => delete form[key])
  Object.keys(fieldErrors).forEach((key) => delete fieldErrors[key])
  localMessage.value = ''
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

function formatDateTime(value) {
  if (!value) return '-'
  const text = String(value)
  const match = text.match(/^(\d{4})-(\d{2})-(\d{2})[T\s](\d{2}):(\d{2}):(\d{2})/)
  if (!match) return text
  const [, year, month, day, hour, minute, second] = match
  return `${day}/${month}/${year} ${hour}:${minute}:${second}`
}

function cellClass(column) {
  const key = String(column?.key || '').toLowerCase()
  const label = String(column?.label || '').toLowerCase()
  return {
    'content-cell': key.includes('noidung') || key.includes('noi_dung') || label.includes('nội dung')
  }
}

function displayValue(item, column) {
  if (column.formatter) return column.formatter(item)

  const value = item[column.key]
  if (value === true) return 'Có'
  if (value === false) return 'Không'
  if (value === null || value === undefined || value === '') return '-'
  if (['createdAt', 'updatedAt', 'created_at', 'updated_at', 'ngayTao', 'ngayCapNhat'].includes(column.key)) {
    return formatDateTime(value)
  }

  return value
}

function fieldByKey(key) {
  return props.fields.find((field) => field.key === key)
}

function isCodeField(field) {
  if (!field) return false
  const key = String(field.key || '').toLowerCase()
  const label = String(field.label || '').toLowerCase()
  return key.startsWith('ma') || label.includes('mã')
}

function setFieldError(key, message = '') {
  if (!key) return
  if (message) fieldErrors[key] = message
  else delete fieldErrors[key]
}

function isFieldRequired(field) {
  if (!field || field.hidden) return false
  if (field.locked) return Boolean(field.required)
  if (field.optional === true) return false
  return field.required !== false
}

function layGiaTriDong(field, configName, fallback) {
  const config = field?.[configName]
  if (!config) return fallback

  for (const [watchKey, values] of Object.entries(config)) {
    const watchValue = form[watchKey]
    if (watchValue !== null && watchValue !== undefined && Object.prototype.hasOwnProperty.call(values, watchValue)) {
      return values[watchValue]
    }
  }

  return fallback
}

function layMin(field) {
  return layGiaTriDong(field, 'minBy', field?.min ?? (field?.key === 'thuTu' ? 1 : 0))
}

function layMax(field) {
  return layGiaTriDong(field, 'maxBy', field?.max ?? 999999.9)
}

function layStep(field) {
  return layGiaTriDong(field, 'stepBy', field?.step ?? (field?.key === 'thuTu' ? 1 : 0.1))
}

function validateField(field) {
  if (!field || field.hidden) return true

  const value = form[field.key]
  const empty = value === '' || value === null || value === undefined

  if (isFieldRequired(field) && empty) {
    setFieldError(field.key, `${field.label} không được để trống.`)
    return false
  }

  if (empty) {
    setFieldError(field.key)
    return true
  }

  if (field.maxLength) {
    const maxLength = field.maxLength
    if (String(value).length > maxLength) {
      setFieldError(field.key, `${field.label} tối đa ${maxLength} ký tự.`)
      return false
    }
  }

  if (field.type === 'number') {
    const numberValue = Number(value)
    if (Number.isNaN(numberValue)) {
      setFieldError(field.key, `${field.label} phải là số.`)
      return false
    }

    const min = layMin(field)
    const max = layMax(field)
    if (numberValue < min || numberValue > max) {
      setFieldError(field.key, `${field.label} phải nằm trong khoảng ${min} đến ${max}.`)
      return false
    }
  }

  if (field.type === 'select') {
    const options = getOptions(field)
    const valueKey = field.valueKey || 'id'
    const hasOption = options.some((option) => String(option[valueKey]) === String(value))
    if (value !== '' && value !== null && value !== undefined && options.length && !hasOption) {
      setFieldError(field.key, `${field.label} không đúng danh sách lựa chọn.`)
      return false
    }
  }

  setFieldError(field.key)
  return validateUniqueField(field.key)
}

function layGiaTriSoSanh(value, field) {
  if (value === null || value === undefined) return ''

  if (field?.type === 'number') {
    const numberValue = Number(value)
    return Number.isNaN(numberValue) ? String(value).trim().toLowerCase() : String(numberValue)
  }

  return String(value).trim().toLowerCase()
}

function layCacDongKiemTraTrung() {
  if (Array.isArray(props.allItems) && props.allItems.length) return props.allItems
  if (Array.isArray(props.items) && props.items.length) return props.items
  return hienThiItems.value || []
}

function kiemTraTrungTheoRule(rule, shouldNotify = false) {
  const source = {
    ...form,
    ...props.parentValues
  }

  const fieldKey = rule.field
  const field = fieldByKey(fieldKey)
  const value = source[fieldKey]
  const empty = value === '' || value === null || value === undefined

  if (!fieldKey || empty) return true

  const scopeKeys = Array.isArray(rule.scopeKeys) ? rule.scopeKeys : []
  const allRows = layCacDongKiemTraTrung()

  const duplicated = allRows.some((row) => {
    if (!row) return false
    if (editingId.value && String(row.id || '') === String(editingId.value)) return false
    if (layGiaTriSoSanh(row[fieldKey], field) !== layGiaTriSoSanh(value, field)) return false

    return scopeKeys.every((scopeKey) => {
      const scopeValue = source[scopeKey]
      if (scopeValue === '' || scopeValue === null || scopeValue === undefined) return true
      return layGiaTriSoSanh(row[scopeKey]) === layGiaTriSoSanh(scopeValue)
    })
  })

  if (!duplicated) return true

  const message = rule.message || `${field?.label || fieldKey} đã tồn tại.`
  setFieldError(fieldKey, message)
  if (shouldNotify) emit('notify', message, 'error')
  return false
}

function validateUniqueField(fieldKey) {
  if (!Array.isArray(props.uniqueRules) || !props.uniqueRules.length) return true

  let ok = true
  props.uniqueRules
      .filter((rule) => rule.field === fieldKey)
      .forEach((rule) => {
        if (!kiemTraTrungTheoRule(rule, false)) ok = false
      })

  return ok
}

function validateUniqueRules() {
  if (!Array.isArray(props.uniqueRules) || !props.uniqueRules.length) return true

  for (const rule of props.uniqueRules) {
    if (!kiemTraTrungTheoRule(rule, true)) return false
  }

  return true
}

function validateRequiredParents() {
  if (!Array.isArray(props.requiredParentKeys) || !props.requiredParentKeys.length) return true

  const source = {
    ...form,
    ...props.parentValues
  }

  for (const item of props.requiredParentKeys) {
    const key = typeof item === 'string' ? item : item.key
    const label = typeof item === 'string' ? item : item.label
    const value = source[key]

    if (value === '' || value === null || value === undefined) {
      const message = `${label || key} không được để trống.`
      setFieldError(key, message)
      emit('notify', message, 'error')
      return false
    }
  }

  return true
}

function validateForm() {
  Object.keys(fieldErrors).forEach((key) => delete fieldErrors[key])
  const results = visibleFields.value.map((field) => validateField(field))
  const ok = results.every(Boolean) && validateRequiredParents() && validateUniqueRules()

  if (!ok) {
    const firstError = Object.values(fieldErrors)[0]
    const message = firstError || 'Vui lòng kiểm tra lại các ô đang báo lỗi.'
    baoTinTaiBang(message, 'error')
    emit('notify', message, 'error')
  }

  return ok
}

function chuanHoaSo(value, field) {
  if (value === '' || value === null || value === undefined) return null

  const numberValue = Number(value)

  if (Number.isNaN(numberValue)) {
    throw new Error(`${field?.label || 'Giá trị'} phải là số.`)
  }

  const min = layMin(field)
  const max = layMax(field)

  if (numberValue < min || numberValue > max) {
    throw new Error(`${field?.label || 'Giá trị số'} phải nằm trong khoảng ${min} đến ${max}.`)
  }

  if (field?.key === 'thuTu') {
    return Math.round(numberValue)
  }

  const step = Number(layStep(field) ?? 0.1)

  if (step === 0.01) {
    return Math.round(numberValue * 100) / 100
  }

  return Math.round(numberValue * 10) / 10
}

function preparePayload() {
  if (!validateForm()) return null
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
    const field = fieldByKey(key)
    const value = source[key]

    if (field?.type === 'number') {
      try {
        payload[key] = chuanHoaSo(value, field)
      } catch (error) {
        setFieldError(key, error?.message || 'Giá trị số không hợp lệ.')
        throw error
      }
      return
    }

    payload[key] = value === '' ? null : value
  })

  return payload
}

function isSelected(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  return String(props.selectedId || '') === String(itemId)
}

function isRelated(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  if (isSelected(item)) return false

  const biBoChonThuCong = props.excludedSelectedIds.some((id) => String(id || '') === String(itemId))
  if (biBoChonThuCong) return false

  return props.selectedIds.some((id) => String(id || '') === String(itemId))
}

function isSaved(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  return props.savedIds.some((id) => String(id || '') === String(itemId))
}

function layTrangThaiDong(item) {
  if (isSaved(item)) return { label: '✓ Đã lưu', className: 'da-luu' }
  if (isSelected(item)) return { label: '● Đang chọn', className: 'dang-chon' }
  if (props.canToggleSave) return { label: '+ Chưa lưu', className: 'chua-luu' }
  if (props.canSelect) return { label: '+ Chưa chọn', className: 'co-the-chon' }
  if (props.canShowSavedStatus) return { label: '+ Chưa lưu', className: 'chua-luu' }
  return { label: 'Trạng thái', className: 'co-the-chon' }
}

function isViewing(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  return String(props.viewedId || '') === String(itemId)
}

function isDangChonTrucTiep(item) {
  return isSelected(item)
}

function selectItem(item) {
  if (!props.canSelect) return

  if (props.allowToggleSelect && isDangChonTrucTiep(item)) {
    emit('select', null)
    resetForm()
    emit('notify', `Đã bỏ chọn dòng trong bảng ${props.title}.`)
    return
  }

  emit('select', item)
  emit('notify', `Đã chọn dòng trong bảng ${props.title}.`)
}

function viewItem(item) {
  if (isViewing(item)) {
    emit('view', null)
    emit('notify', `Đã bỏ xem dòng trong bảng ${props.title}.`)
    return
  }

  emit('view', item)
  emit('notify', `Đang xem dữ liệu con theo dòng trong bảng ${props.title}.`)
}

function toggleSaveItem(item) {
  emit('toggle-save', item)
}

function layDuLieuLuuTuResponse(response) {
  if (!response) return null

  if (response.success !== undefined && response.data !== undefined) {
    return response.data
  }

  if (response.data?.success !== undefined && response.data?.data !== undefined) {
    return response.data.data
  }

  if (response.data?.id) {
    return response.data
  }

  return response
}

async function saveForm() {
  if (props.disabled) return

  saving.value = true

  try {
    const payload = preparePayload()
    if (!payload) return

    const saved = editingId.value
        ? await props.service.update(editingId.value, payload)
        : await props.service.create(payload)

    const savedData = layDuLieuLuuTuResponse(saved)
    const rowDaLuu = savedData && typeof savedData === 'object'
        ? { ...payload, ...savedData, id: savedData.id || editingId.value || payload.id }
        : { ...payload, id: editingId.value || payload.id }

    const message = editingId.value ? 'Cập nhật thành công.' : 'Lưu thành công.'
    baoTinTaiBang(message)
    emit('notify', message)
    emit('saved', rowDaLuu)
    resetForm()
  } catch (error) {
    const message = layThongBaoLoi(error, 'Lưu dữ liệu thất bại.')
    baoTinTaiBang(message, 'error')
    emit('notify', message, 'error')
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
    baoTinTaiBang('Xóa thành công.')
    emit('notify', 'Xóa thành công.')
    emit('deleted', item)
    resetForm()
  } catch (error) {
    const message = layThongBaoLoi(error, 'Xóa dữ liệu thất bại.')
    baoTinTaiBang(message, 'error')
    emit('notify', message, 'error')
  }
}
</script>

<style scoped>
.bang-them-nghiep-vu {
  width: 100%;
  margin-bottom: 10px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
  overflow: hidden;
}

.bang-them-nghiep-vu.bang-xuong-song {
  background: #ffffff;
}

.bang-them-nghiep-vu.bang-phu {
  background: #f3f4f6;
  border-color: #cbd5e1;
}

.bang-them-nghiep-vu.bang-phu .bang-head {
  background: #e5e7eb;
}

.bang-them-nghiep-vu.bang-phu .bang-form,
.bang-them-nghiep-vu.bang-phu .table-title,
.bang-them-nghiep-vu.bang-phu .table-wrap {
  background: #f8fafc;
}


.bang-them-nghiep-vu.bang-goc-mau {
  background: #f5f3ff;
  border-color: #c4b5fd;
}

.bang-them-nghiep-vu.bang-goc-mau .bang-head {
  background: #ede9fe;
}

.bang-them-nghiep-vu.bang-goc-mau .bang-form,
.bang-them-nghiep-vu.bang-goc-mau .table-title,
.bang-them-nghiep-vu.bang-goc-mau .table-wrap {
  background: #faf5ff;
}

.bang-them-nghiep-vu.disabled {
  opacity: 0.72;
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
  align-items: center;
  gap: 5px;
}

.toggle-btn {
  min-width: 58px;
}

.bang-them-nghiep-vu.collapsed .bang-head {
  border-bottom: 0;
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

.field span em {
  margin-left: 2px;
  color: #dc2626;
  font-style: normal;
}

.field.has-error input,
.field.has-error select,
.field.has-error textarea {
  border-color: #dc2626;
  background: #fff7f7;
}

.field-error {
  color: #dc2626;
  font-size: 10px;
  line-height: 13px;
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

.btn.tiny {
  min-height: 24px;
  padding: 2px 7px;
  font-size: 11px;
}

.table-message {
  margin: 8px 10px 0;
  padding: 7px 9px;
  border: 1px solid #bbf7d0;
  border-radius: 4px;
  background: #f0fdf4;
  color: #166534;
  font-size: 12px;
  white-space: pre-line;
}

.table-message.error {
  border-color: #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

.table-title {
  margin: 8px 10px 0;
  padding: 7px 10px;
  border: 1px solid #bbf7d0;
  border-radius: 4px;
  background: #dcfce7;
  color: #166534;
  font-size: 11px;
  font-weight: 700;
}

.table-wrap {
  width: 100%;
  max-width: 100%;
  overflow: auto;
  padding-top: 6px;
}
.bang-them-nghiep-vu.bang-phu .table-wrap {
  max-height: 340px;
}

.bang-them-nghiep-vu.bang-xuong-song .table-wrap {
  max-height: 430px;
}

table {
  width: 100%;
  min-width: 860px;
  border-collapse: collapse;
  font-size: 12px;
}

th,
td {
  border-top: 1px solid #e5e7eb;
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

tr.related td {
  background: #eff6ff;
}

tr.related td:first-child {
  border-left: 3px solid #2563eb;
}

tr.viewing td {
  background: #dbeafe;
}

tr.viewing td:first-child {
  border-left: 3px solid #1d4ed8;
}

tr.selected td {
  background: #ecfdf5;
}

tr.selected td:first-child {
  border-left: 3px solid #16a34a;
}

.btn.choose-btn.chosen {
  border-color: #16a34a;
  background: #16a34a;
  color: #ffffff;
}

.save-status {
  margin: 7px 10px 0;
  padding: 6px 8px;
  border: 1px solid #d1fae5;
  border-radius: 3px;
  background: #f0fdf4;
  color: #166534;
  font-size: 11px;
}

.save-status b {
  color: #047857;
}

.btn.save-link-btn.saved {
  border-color: #16a34a;
  background: #dcfce7;
  color: #166534;
}

.btn.view-btn {
  border-color: #1d4ed8;
  background: #1d4ed8;
  color: #ffffff;
  font-weight: 700;
}

.btn.view-btn:hover {
  border-color: #1e40af;
  background: #1e40af;
}

.btn.view-btn.related,
.btn.view-btn.viewing {
  border-color: #1e40af;
  background: #1e40af;
  color: #ffffff;
}

.col-action {
  width: 82px;
  white-space: nowrap;
}

.col-action-wide {
  width: 190px;
}

.action-pair {
  display: flex;
  align-items: center;
  gap: 4px;
}


.status-action-cell {
  flex-wrap: wrap;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  min-height: 22px;
  border-radius: 999px;
  padding: 2px 8px;
  font-size: 11px;
  font-weight: 700;
  white-space: nowrap;
}

.status-badge.da-luu {
  border: 1px solid #86efac;
  background: #dcfce7;
  color: #166534;
}

.status-badge.chua-luu {
  border: 1px solid #fde68a;
  background: #fffbeb;
  color: #92400e;
}

.status-badge.dang-chon {
  border: 1px solid #93c5fd;
  background: #dbeafe;
  color: #1d4ed8;
}

.status-badge.co-the-chon {
  border: 1px solid #d1d5db;
  background: #f9fafb;
  color: #374151;
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

.content-cell {
  min-width: 420px;
  max-width: 640px;
  white-space: normal;
  line-height: 1.45;
}

</style>
