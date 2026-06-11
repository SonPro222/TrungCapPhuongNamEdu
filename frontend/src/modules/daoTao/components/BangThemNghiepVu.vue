<template>
  <section :class="['bang-them-nghiep-vu', `bang-${loaiBang}`, { disabled, collapsed: !moBang }]">
    <div class="bang-head">
      <div>
        <h3>{{ title }}</h3>
        <p v-if="description">{{ description }}</p>
      </div>
      <div class="bang-head-actions">
        <button v-if="!readOnly" type="button" class="btn tiny" :disabled="disabled" @click="resetForm">Làm mới form
        </button>
        <button type="button" class="btn tiny toggle-btn" @click="moBang = !moBang">
          {{ moBang ? '⌃ Đóng' : '⌄ Mở' }}
        </button>
      </div>
    </div>

    <div v-show="moBang">
      <div v-if="parentText" class="parent-text">{{ parentText }}</div>
      <div v-if="disabled" class="disabled-text">{{ disabledText || 'Cần tạo và chọn dữ liệu bảng cha trước.' }}</div>

      <div v-if="readonlyInfoFields.length" class="readonly-info-form">
        <label
            v-for="field in readonlyInfoFields"
            :key="field.key"
            class="field readonly-info-field"
            :class="field.wide ? 'wide' : ''"
        >
          <span>{{ field.label }}</span>
          <input :value="field.value || '-'" disabled/>
        </label>
      </div>

      <form v-if="!readOnly" class="bang-form" novalidate @submit.prevent="saveForm">
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

            <div v-else-if="field.type === 'file'" :key="`file-group-${field.key}-${formKey}`" class="file-input-group">
              <div v-if="field.themNhieuO === true" class="file-input-list">
                <div
                    v-for="index in laySoOFile(field)"
                    :key="`${field.key}-${index}`"
                    class="file-input-row"
                >
                  <input
                      type="file"
                      :required="field.required && index === 1"
                      :disabled="disabled || field.locked"
                      :accept="field.accept || undefined"
                      @change="chonFileUpload(field, $event, index - 1)"
                  />

                  <button
                      v-if="index === 1"
                      type="button"
                      class="btn tiny file-add-btn"
                      :disabled="disabled || field.locked"
                      title="Thêm ô chọn file"
                      @click="themOFileUpload(field)"
                  >
                    +
                  </button>

                  <button
                      v-else
                      type="button"
                      class="btn tiny danger file-add-btn"
                      :disabled="disabled || field.locked"
                      title="Xóa ô chọn file này"
                      @click="xoaOFileUpload(field, index - 1)"
                  >
                    -
                  </button>
                </div>

                <small class="file-help">
                  Bấm + để thêm nhiều ô chọn file. Mỗi ô chọn 1 file, hệ thống sẽ lưu chung vào dòng tài liệu này.
                </small>
              </div>

              <template v-else>
                <input
                    type="file"
                    :required="field.required"
                    :disabled="disabled || field.locked"
                    :accept="field.accept || undefined"
                    :multiple="field.multiple === true || field.allowFolder === true"
                    @change="chonFileUpload(field, $event)"
                />

                <input
                    v-if="field.allowFolder"
                    type="file"
                    :disabled="disabled || field.locked"
                    :accept="field.accept || undefined"
                    multiple
                    webkitdirectory
                    directory
                    @change="chonFileUpload(field, $event)"
                />

                <small v-if="field.allowFolder" class="file-help">
                  Có thể chọn nhiều tệp hoặc chọn cả thư mục.
                </small>
              </template>
            </div>

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
        {{
          saveStatusText || 'Trạng thái gán: Đã gán là dữ liệu đã được gán vào ngữ cảnh đang chọn. Bấm Gán vào để lưu qua bảng nối.'
        }}
      </div>

      <div class="table-title">
        {{
          tableTitle || (readOnly ? 'Dữ liệu đã lưu vào chương trình/version hiện tại' : 'Dữ liệu có sẵn / vừa tạo trong luồng này')
        }}
      </div>

      <div class="table-wrap mini-scroll">
        <table>
          <thead>
          <tr>
            <th v-if="hienCotTrangThai" class="col-action col-action-wide">Trạng thái</th>
            <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
            <th v-if="hienCotThaoTac" class="col-action">Thao tác</th>
          </tr>
          </thead>
          <tbody>
          <tr v-if="!hienThiItems.length">
            <td :colspan="soCotBang" class="empty-cell">
              {{
                emptyText || (readOnly ? 'Chưa có dữ liệu nào được lưu/gắn vào chương trình hiện tại.' : 'Bảng đang trống. Nhập form phía trên rồi bấm Lưu.')
              }}
            </td>
          </tr>

          <template v-else>
            <tr
                v-for="item in hienThiItems"
                :key="item.id || item.__localId"
                :class="{
      selected: isSelected(item) || isMultiSelected(item),
      related: isRelated(item),
      viewing: isViewing(item),
      clickable: canToggleSave || canSelect,
      'is-expanded': isExpanded(item)
    }"
                @click="clickDong(item, $event)"
            >
              <td v-if="hienCotTrangThai" class="col-action col-action-wide action-pair status-action-cell">
                <span :class="['status-badge', layTrangThaiDong(item).className]">
                  {{ layTrangThaiDong(item).label }}
                </span>

                <button
                    v-if="canSelect"
                    type="button"
                    :class="['btn tiny choose-btn', { chosen: isSelected(item) || isMultiSelected(item) }]"
                    :title="layTieuDeNutChon(item)"
                    @click.stop="selectItem(item)"
                >
                  {{ layNhanNutChon(item) }}
                </button>

                <button
                    v-if="canToggleSave"
                    type="button"
                    :class="['btn tiny save-link-btn', { saved: isSaved(item) }]"
                    :title="isSaved(item) ? toggleSavedTitle : toggleSaveTitle"
                    @click.prevent.stop="toggleSaveItem(item)"
                    @mousedown.stop
                    @mouseup.stop
                >
                  {{ isSaved(item) ? toggleSavedLabel : toggleSaveLabel }}
                </button>
              </td>

              <td
                  v-for="column in columns"
                  :key="column.key"
                  :class="cellClass(column)"
                  @click="column.openFile ? chanClickTrongDong($event) : null"
                  @mousedown="column.openFile ? chanClickTrongDong($event) : null"
                  @mouseup="column.openFile ? chanClickTrongDong($event) : null"
              >
                <button
                    v-if="column.openFile"
                    type="button"
                    class="btn tiny view-file-btn"
                    :disabled="!column.openFilePage && !item[column.key]"
                    @click.prevent.stop="bamXemTep(item, column)"
                    @mousedown.stop
                    @mouseup.stop
                >
                  Xem tệp
                </button>

                <template v-else>
                  {{ displayValue(item, column) }}
                </template>
              </td>

              <td v-if="hienCotThaoTac" class="col-action actions-cell">
                <button
                    type="button"
                    :class="['btn tiny expand-row-btn', { active: isExpanded(item) }]"
                    :title="isExpanded(item) ? 'Thu gọn nội dung hàng này' : 'Xem đầy đủ nội dung hàng này'"
                    @click.stop="toggleExpandedRow(item)"
                >
                  {{ isExpanded(item) ? 'Thu gọn ▴' : 'Xem thêm ▾' }}
                </button>

                <button v-if="canView" type="button" class="btn tiny view-btn" @click.stop="viewItem(item)">
                  {{ viewLabel }}
                </button>
                <button v-if="!readOnly" type="button" class="btn tiny" @click.stop="editItem(item)">Sửa</button>
                <button v-if="!readOnly" type="button" class="btn tiny danger" @click.stop="deleteItem(item)">Xóa
                </button>
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
import {computed, reactive, ref, watch} from 'vue'
import {layThongBaoLoi} from '../utils/layThongBaoLoi'
import {ENV} from '@/core/config/env'

const props = defineProps({
  title: {type: String, required: true},
  description: {type: String, default: ''},
  columns: {type: Array, default: () => []},
  fields: {type: Array, default: () => []},
  defaultForm: {type: Object, default: () => ({})},
  parentValues: {type: Object, default: () => ({})},
  parentText: {type: String, default: ''},
  readonlyInfoFields: {type: Array, default: () => []},
  items: {type: Array, default: () => []},
  allItems: {type: Array, default: () => []},
  lookups: {type: Object, default: () => ({})},
  service: {type: Object, required: true},
  selectedId: {type: [Number, String], default: null},
  selectedIds: {type: Array, default: () => []},
  savedIds: {type: Array, default: () => []},
  tableMessage: {type: Object, default: () => ({})},

  canToggleSave: {type: Boolean, default: false},
  canSelect: {type: Boolean, default: true},
  canShowSavedStatus: {type: Boolean, default: false},
  multiSelect: {type: Boolean, default: false},
  multiSelectIds: {type: Array, default: () => []},
  multiSelectLabel: {type: String, default: 'Tích chọn'},
  multiSelectedLabel: {type: String, default: 'Bỏ tích'},

  toggleSaveLabel: {type: String, default: 'Gán vào'},
  toggleSavedLabel: {type: String, default: 'Đã gán'},
  toggleSaveTitle: {type: String, default: 'Gán dữ liệu hàng này vào ngữ cảnh đang chọn'},
  toggleSavedTitle: {type: String, default: 'Dữ liệu này đã được gán vào ngữ cảnh đang chọn'},
  saveStatusText: {type: String, default: ''},
  statusSavedLabel: {type: String, default: '✓ Đã gán'},
  statusUnsavedLabel: {type: String, default: '+ Chưa gán'},

  canView: {type: Boolean, default: false},
  viewLabel: {type: String, default: 'Xem thêm'},
  viewedId: {type: [Number, String], default: null},
  excludedSelectedIds: {type: Array, default: () => []},
  uniqueRules: {type: Array, default: () => []},
  requiredParentKeys: {type: Array, default: () => []},
  allowToggleSelect: {type: Boolean, default: true},
  loaiBang: {
    type: String,
    default: 'phu',
    validator: (value) => ['xuong-song', 'phu', 'mau-mau'].includes(value)
  },
  disabled: {type: Boolean, default: false},
  disabledText: {type: String, default: ''},
  readOnly: {type: Boolean, default: false},
  tableTitle: {type: String, default: ''},
  emptyText: {type: String, default: ''},
  resetAfterSave: {type: Boolean, default: true},
  uploadFile: {type: Function, default: null}
})
const emit = defineEmits(['saved', 'deleted', 'select', 'view', 'toggle-save', 'notify', 'file-view', 'multi-select'])

const form = reactive({})
const fieldErrors = reactive({})
const localMessage = ref('')
const localMessageType = ref('success')
const editingId = ref(null)
const formKey = ref(0)
const saving = ref(false)
const moBang = ref(true)
const cachedItems = ref([])
const expandedRowKeys = ref(new Set())
const fileInputs = reactive({})
const fileInputCounts = reactive({})

function canGiuDanhSachKhiPropsRong() {
  return props.loaiBang === 'mau-mau' && String(props.title || '').includes('Quy đổi điểm mẫu')
}

function taoFormWatchKey() {
  return JSON.stringify({
    defaultForm: props.defaultForm || {},
    parentValues: props.parentValues || {}
  })
}

watch(
    () => taoFormWatchKey(),
    () => {
      if (!editingId.value) resetForm()
    },
    {immediate: true}
)

const hienThiItems = computed(() => {
  if (Array.isArray(props.items) && props.items.length) return props.items
  if (canGiuDanhSachKhiPropsRong() && cachedItems.value.length) return cachedItems.value
  return props.items || []
})

const visibleFields = computed(() => props.fields.filter((field) => !field.hidden))
const thongBaoBang = computed(() => props.tableMessage?.message || localMessage.value)
const loaiThongBaoBang = computed(() => props.tableMessage?.type || localMessageType.value)
const hienCotTrangThai = computed(() => props.canToggleSave || props.canShowSavedStatus)

/*
  Luôn giữ cột thao tác khi bảng có cột dữ liệu để đặt nút Xem thêm/Thu gọn.
  Không đổi logic Xem/Sửa/Xóa cũ; chỉ thêm nút mở rộng nội dung hàng.
*/
const hienCotThaoTac = computed(() => props.columns.length > 0 || props.canView || !props.readOnly)

const soCotBang = computed(() => props.columns.length + (hienCotTrangThai.value ? 1 : 0) + (hienCotThaoTac.value ? 1 : 0))

function baoTinTaiBang(message, type = 'success') {
  localMessage.value = message
  localMessageType.value = type
  setTimeout(() => {
    if (localMessage.value === message) localMessage.value = ''
  }, 5000)
}

function emitThongBao(message, type = 'success') {
  emit('notify', {
    message,
    type
  })
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
  Object.keys(fileInputs).forEach((key) => delete fileInputs[key])
  Object.keys(fileInputCounts).forEach((key) => delete fileInputCounts[key])
  localMessage.value = ''
  Object.assign(form, payload)
  formKey.value++
}

function resetForm() {
  editingId.value = null
  syncForm(buildEmptyForm())
}

function laySoOFile(field) {
  if (field?.themNhieuO !== true) return 1

  const soLuong = Number(fileInputCounts[field.key] || 1)

  return soLuong > 0 ? soLuong : 1
}

function layDanhSachFileDaChon(field) {
  const value = fileInputs[field.key]

  if (Array.isArray(value)) {
    return value.filter(Boolean)
  }

  return value ? [value] : []
}

function capNhatTenHienThiFile(field) {
  const files = layDanhSachFileDaChon(field)

  if (!files.length) {
    form[field.key] = null
    return
  }

  form[field.key] = files.length === 1
      ? (files[0].webkitRelativePath || files[0].name)
      : `${files.length} tệp đã chọn`
}

function tuDongDienTenVaMaTuFile(field, fileDauTien) {
  if (!fileDauTien) return

  if (!form.ten && field.autoFillTen !== false) {
    form.ten = fileDauTien.name.replace(/\.[^/.]+$/, '')
  }

  if (!form.ma && field.autoFillMa !== false) {
    const nameWithoutExt = fileDauTien.name.replace(/\.[^/.]+$/, '')
    form.ma = nameWithoutExt
        .normalize('NFD')
        .replace(/[\u0300-\u036f]/g, '')
        .replace(/[^a-zA-Z0-9]+/g, '_')
        .replace(/^_+|_+$/g, '')
        .toUpperCase()
  }
}

function themOFileUpload(field) {
  fileInputCounts[field.key] = laySoOFile(field) + 1
}

function xoaOFileUpload(field, index) {
  const danhSachHienTai = layDanhSachFileDaChon(field)
  danhSachHienTai.splice(index, 1)

  if (danhSachHienTai.length) {
    fileInputs[field.key] = danhSachHienTai
  } else {
    delete fileInputs[field.key]
  }

  fileInputCounts[field.key] = Math.max(1, laySoOFile(field) - 1)
  capNhatTenHienThiFile(field)
  validateField(field)
}

function chonFileUpload(field, event, index = null) {
  const files = Array.from(event?.target?.files || [])

  if (field.themNhieuO === true) {
    const danhSachHienTai = layDanhSachFileDaChon(field)

    if (files.length) {
      danhSachHienTai[index ?? 0] = files[0]
    } else {
      danhSachHienTai.splice(index ?? 0, 1)
    }

    const danhSachHopLe = danhSachHienTai.filter(Boolean)

    if (danhSachHopLe.length) {
      fileInputs[field.key] = danhSachHopLe
    } else {
      delete fileInputs[field.key]
    }

    capNhatTenHienThiFile(field)
    tuDongDienTenVaMaTuFile(field, danhSachHopLe[0])
    validateField(field)
    return
  }

  if (!files.length) {
    delete fileInputs[field.key]
    form[field.key] = null
    return
  }

  const giaTriLuu = field.multiple === true || field.allowFolder === true ? files : files[0]
  const tenHienThi = files.length === 1
      ? (files[0].webkitRelativePath || files[0].name)
      : `${files.length} tệp đã chọn`

  fileInputs[field.key] = giaTriLuu
  form[field.key] = tenHienThi

  tuDongDienTenVaMaTuFile(field, files[0])
  validateField(field)
}

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

function layBackendOrigin() {
  return String(ENV.API_BASE_URL || '')
      .replace(/\/api\/?$/, '')
      .replace(/\/$/, '')
}

function moTepDaLuu(duongDan) {
  if (!duongDan) return

  const url = String(duongDan)

  if (url.startsWith('http://') || url.startsWith('https://')) {
    window.open(url, '_blank')
    return
  }

  if (url.startsWith('/api/')) {
    window.open(`${layBackendOrigin()}${url}`, '_blank')
    return
  }

  window.open(url, '_blank')
}

function bamXemTep(item, column) {
  if (!item || !column?.key || (!item[column.key] && !column.openFilePage)) return

  if (column.openFilePage) {
    emit('file-view', {
      item,
      column,
      duongDan: item[column.key]
    })
    return
  }

  moTepDaLuu(item[column.key])
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
  if (typeof field.requiredIf === 'function') return field.requiredIf(form)
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

  const value = field.type === 'file' ? fileInputs[field.key] : form[field.key]
  const empty = Array.isArray(value)
      ? !value.filter(Boolean).length
      : value === '' || value === null || value === undefined

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

  if (typeof field.validate === 'function') {
    const error = field.validate(value, form)
    if (error) {
      setFieldError(field.key, error)
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
  if (shouldNotify) emitThongBao(message, 'error')
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
      emitThongBao(message, 'error')
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
    emitThongBao(message, 'error')
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

    if (field?.type === 'file') {
      return
    }

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

async function xuLyUploadFileTruocKhiLuu(payload) {
  if (!props.uploadFile) return payload

  const fileFields = props.fields.filter((field) => field.type === 'file')
  if (!fileFields.length) return payload

  const nextPayload = {...payload}

  for (const field of fileFields) {
    const fileValue = fileInputs[field.key]

    if (!fileValue) continue

    const files = Array.isArray(fileValue) ? fileValue : [fileValue]

    const result = await props.uploadFile({
      field,
      file: files[0],
      files,
      form: {...form},
      payload: nextPayload,
      editingId: editingId.value
    })

    if (result && typeof result === 'object') {
      Object.assign(nextPayload, result)
    }
  }

  return nextPayload
}

function isSelected(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  return String(props.selectedId || '') === String(itemId)
}

function isMultiSelected(item) {
  const itemId = item?.id
  if (!props.multiSelect || itemId === null || itemId === undefined || itemId === '') return false
  return props.multiSelectIds.some((id) => String(id || '') === String(itemId))
}

function isRelated(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  if (isSelected(item) || isMultiSelected(item)) return false

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
  if (isSaved(item)) return {label: props.statusSavedLabel, className: 'da-luu'}
  if (isMultiSelected(item)) return {label: '☑ Đã tích chọn', className: 'dang-chon'}
  if (isSelected(item)) return {label: '● Đang chọn', className: 'dang-chon'}
  if (props.canToggleSave) return {label: props.statusUnsavedLabel, className: 'chua-luu'}
  if (props.canSelect) return {label: '+ Chưa chọn', className: 'co-the-chon'}
  if (props.canShowSavedStatus) return {label: props.statusUnsavedLabel, className: 'chua-luu'}
  return {label: 'Trạng thái', className: 'co-the-chon'}
}

function layNhanNutChon(item) {
  if (props.multiSelect) return isMultiSelected(item) ? props.multiSelectedLabel : props.multiSelectLabel
  return isSelected(item) ? 'Bỏ chọn' : 'Chọn'
}

function layTieuDeNutChon(item) {
  if (props.multiSelect) return isMultiSelected(item) ? 'Bỏ tích chọn dòng này' : 'Tích chọn dòng này để gán theo lô'
  return isSelected(item) ? 'Bỏ chọn dòng này' : 'Tích chọn dữ liệu hàng này'
}

function isViewing(item) {
  const itemId = item?.id
  if (itemId === null || itemId === undefined || itemId === '') return false
  return String(props.viewedId || '') === String(itemId)
}

function layRowKey(item) {
  const key =
      item?.id ||
      item?.__localId ||
      item?.maNganh ||
      item?.maChuongTrinh ||
      item?.maVersion ||
      item?.maMon ||
      item?.maMonTrongCt ||
      item?.ma ||
      hienThiItems.value.indexOf(item)

  return String(`${props.title || 'bang'}-${key}`)
}

function isExpanded(item) {
  return expandedRowKeys.value.has(layRowKey(item))
}

function toggleExpandedRow(item) {
  const key = layRowKey(item)
  const next = new Set(expandedRowKeys.value)

  if (next.has(key)) {
    next.delete(key)
  } else {
    next.add(key)
  }

  expandedRowKeys.value = next
}

function isDangChonTrucTiep(item) {
  return isSelected(item)
}

function selectItem(item) {
  if (!props.canSelect) return

  if (props.multiSelect) {
    const checked = !isMultiSelected(item)
    emit('multi-select', {item, checked})
    emitThongBao(checked ? `Đã tích chọn dòng trong bảng ${props.title}.` : `Đã bỏ tích chọn dòng trong bảng ${props.title}.`, 'success')
    return
  }

  if (props.allowToggleSelect && isDangChonTrucTiep(item)) {
    emit('select', null)
    resetForm()
    emitThongBao(`Đã bỏ chọn dòng trong bảng ${props.title}.`, 'success')
    return
  }

  emit('select', item)
  emitThongBao(`Đã chọn dòng trong bảng ${props.title}.`, 'success')
}

function viewItem(item) {
  if (isViewing(item)) {
    emit('view', null)
    emitThongBao(`Đã bỏ xem dòng trong bảng ${props.title}.`, 'success')
    return
  }

  emit('view', item)
  emitThongBao(`Đang xem dữ liệu con theo dòng trong bảng ${props.title}.`, 'success')
}

function toggleSaveItem(item) {
  emit('toggle-save', item)
}

function chanClickTrongDong(event) {
  if (!event) return
  event.preventDefault()
  event.stopPropagation()
}

function laClickVaoNutHoacForm(event) {
  const target = event?.target

  if (!target || typeof target.closest !== 'function') {
    return false
  }

  return Boolean(
      target.closest('button') ||
      target.closest('a') ||
      target.closest('input') ||
      target.closest('select') ||
      target.closest('textarea') ||
      target.closest('.btn') ||
      target.closest('.view-file-btn') ||
      target.closest('.actions-cell') ||
      target.closest('.status-action-cell')
  )
}

function clickDong(item, event) {
  if (props.disabled || props.readOnly) return

  if (laClickVaoNutHoacForm(event)) {
    return
  }

  if (props.canToggleSave) {
    return
  }

  if (props.canSelect) {
    selectItem(item)
  }
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
  if (props.disabled || props.readOnly) return

  saving.value = true

  try {
    const payloadmau = preparePayload()
    if (!payloadmau) return

    const payload = await xuLyUploadFileTruocKhiLuu(payloadmau)

    const saved = editingId.value
        ? await props.service.update(editingId.value, payload)
        : await props.service.create(payload)

    const savedData = layDuLieuLuuTuResponse(saved)
    const rowDaLuu = savedData && typeof savedData === 'object'
        ? {...payload, ...savedData, id: savedData.id || editingId.value || payload.id}
        : {...payload, id: editingId.value || payload.id}

    const message = editingId.value ? 'Cập nhật thành công.' : 'Lưu thành công.'
    baoTinTaiBang(message)
    emitThongBao(message, 'success')
    emit('saved', rowDaLuu)

    if (props.resetAfterSave) {
      resetForm()
    } else {
      editingId.value = null
    }
  } catch (error) {
    const message = layThongBaoLoi(error, 'Lưu dữ liệu thất bại.')
    baoTinTaiBang(message, 'error')
    emitThongBao(message, 'error')
  } finally {
    saving.value = false
  }
}

function editItem(item) {
  if (props.readOnly) return

  editingId.value = item.id
  syncForm({
    ...props.defaultForm,
    ...props.parentValues,
    ...item
  })
}

async function deleteItem(item) {
  if (props.readOnly) return
  if (!item?.id) return
  if (!confirm('Xóa dòng dữ liệu này?')) return

  try {
    await props.service.delete(item.id)
    baoTinTaiBang('Xóa thành công.')
    emitThongBao('Xóa thành công.', 'success')
    emit('deleted', item)
    resetForm()
  } catch (error) {
    const message = layThongBaoLoi(error, 'Xóa dữ liệu thất bại.')
    baoTinTaiBang(message, 'error')
    emitThongBao(message, 'error')
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

.bang-them-nghiep-vu.bang-mau-mau {
  background: #f5f3ff;
  border-color: #c4b5fd;
}

.bang-them-nghiep-vu.bang-mau-mau .bang-head {
  background: #ede9fe;
}

.bang-them-nghiep-vu.bang-mau-mau .bang-form,
.bang-them-nghiep-vu.bang-mau-mau .table-title,
.bang-them-nghiep-vu.bang-mau-mau .table-wrap {
  background: #faf5ff;
}

/* Tầng 5: cuộn dữ liệu bảng gốc/mẫu, giữ cố định tên cột */
.bang-them-nghiep-vu.bang-mau-mau .table-wrap {
  max-height: 360px;
  overflow-y: auto;
  overflow-x: auto;
}

.bang-them-nghiep-vu.bang-mau-mau .table-wrap thead th {
  position: sticky;
  top: 0;
  z-index: 4;
  background: #f5f3ff;
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
  white-space: pre-line;
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

.readonly-info-form {
  display: grid;
  grid-template-columns: repeat(6, minmax(120px, 1fr));
  gap: 8px;
  padding: 10px 10px 0;
}

.readonly-info-field input {
  background: #f3f4f6;
  color: #374151;
  cursor: not-allowed;
  font-weight: 600;
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
  min-height: 22px;
  padding: 2px 6px;
  font-size: 10px;
  white-space: nowrap;
  flex-shrink: 0;
}

tr.clickable {
  cursor: pointer;
}

tr.clickable:hover {
  background: #eef2ff;
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
  width: 72px;
  white-space: nowrap;
}

.col-action-wide {
  width: 260px;
  min-width: 260px;
  max-width: 320px;
}

.action-pair {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-wrap: wrap;
}

.status-action-cell {
  white-space: normal;
  flex-wrap: wrap;
  align-content: center;
  row-gap: 4px;
}

.status-action-cell .btn {
  flex-shrink: 0;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  min-height: 20px;
  border-radius: 999px;
  padding: 2px 6px;
  font-size: 10px;
  font-weight: 700;
  white-space: nowrap;
  flex-shrink: 0;
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
  align-items: center;
  flex-wrap: wrap;
}

.expand-row-btn {
  border-color: #cbd5e1;
  background: #f8fafc;
  color: #334155;
  font-weight: 700;
}

.expand-row-btn:hover {
  border-color: #93c5fd;
  background: #eff6ff;
  color: #1d4ed8;
}

.expand-row-btn.active {
  border-color: #2563eb;
  background: #dbeafe;
  color: #1d4ed8;
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

.content-cell {
  min-width: 420px;
  max-width: 640px;
  white-space: normal;
  line-height: 1.45;
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

.file-link-input {
  display: flex;
  align-items: center;
  gap: 6px;
}

.file-link-input input {
  flex: 1;
  min-width: 0;
}

.view-file-btn {
  white-space: nowrap;
}

.file-input-group {
  display: grid;
  gap: 6px;
}

.file-help {
  color: #64748b;
  font-size: 11px;
}

.file-input-list {
  display: grid;
  gap: 6px;
}

.file-input-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 32px;
  gap: 5px;
  align-items: center;
}

.file-add-btn {
  width: 32px;
  min-width: 32px;
  padding-left: 0;
  padding-right: 0;
  font-weight: 700;
}

</style>