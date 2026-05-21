<template>
  <div class="bang-quy-trinh">
    <div class="bang-header">
      <div class="bang-title-wrap">
        <h3 :class="['bang-title', mauChu]">{{ title }}</h3>
        <span v-if="dbName" class="bang-db">({{ dbName }})</span>
      </div>

      <div class="bang-actions">
        <div class="bang-search">
          <span>⌕</span>
          <input v-model="search" type="text" placeholder="Tìm kiếm..." />
        </div>
        <button v-if="showAdd" type="button" :class="['btn-them', mauNen]">
          <span>+</span> Thêm
        </button>
      </div>
    </div>

    <div class="bang-body">
      <table>
        <thead>
          <tr>
            <th class="stt">#</th>
            <th
                v-for="(header, index) in headers"
                :key="header.key"
                :class="{ 'with-border': index < headers.length - 1 }"
            >
              {{ header.label }}
            </th>
            <th v-if="actionButtons" class="cot-thao-tac">Thao tác</th>
          </tr>
        </thead>
        <tbody>
          <tr
              v-for="(row, index) in filteredRows"
              :key="row.id || index"
              :class="['data-row', { active: selectedId === row.id }]"
              :style="selectedId === row.id ? activeStyle : null"
              @click="$emit('row-click', row)"
          >
            <td class="stt text-muted">{{ index + 1 }}</td>
            <td
                v-for="(header, colIndex) in headers"
                :key="header.key"
                :class="{ 'with-border': colIndex < headers.length - 1 }"
            >
              <span v-if="header.key === 'trang_thai'" :class="['status', isActiveStatus(row[header.key]) ? 'success' : 'danger']">
                {{ row[header.key] || '-' }}
              </span>
              <span v-else>{{ displayValue(row[header.key]) }}</span>
            </td>
            <td v-if="actionButtons" class="cot-thao-tac">
              <button type="button" class="icon-btn" @click.stop>👁</button>
              <button type="button" class="icon-btn" @click.stop>✎</button>
              <button type="button" class="icon-btn danger" @click.stop>🗑</button>
            </td>
          </tr>
          <tr v-if="filteredRows.length === 0">
            <td :colspan="headers.length + (actionButtons ? 2 : 1)" class="empty-cell">
              {{ loading ? 'Đang tải dữ liệu...' : 'Không có dữ liệu' }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'

const props = defineProps({
  title: { type: String, required: true },
  dbName: { type: String, default: '' },
  headers: { type: Array, default: () => [] },
  rows: { type: Array, default: () => [] },
  selectedId: { type: [String, Number], default: null },
  theme: { type: String, default: 'blue' },
  actionButtons: { type: Boolean, default: true },
  showAdd: { type: Boolean, default: true },
  loading: { type: Boolean, default: false }
})

defineEmits(['row-click'])

const search = ref('')

const mau = {
  blue: { text: 'mau-blue', bg: 'nen-blue', border: '#2563eb', soft: '#eff6ff' },
  indigo: { text: 'mau-indigo', bg: 'nen-indigo', border: '#1e40af', soft: '#eff6ff' },
  purple: { text: 'mau-purple', bg: 'nen-purple', border: '#7e22ce', soft: '#faf5ff' },
  orange: { text: 'mau-orange', bg: 'nen-orange', border: '#ea580c', soft: '#fff7ed' },
  green: { text: 'mau-green', bg: 'nen-green', border: '#16a34a', soft: '#f0fdf4' },
  slate: { text: 'mau-slate', bg: 'nen-slate', border: '#475569', soft: '#f8fafc' }
}

const color = computed(() => mau[props.theme] || mau.blue)
const mauChu = computed(() => color.value.text)
const mauNen = computed(() => color.value.bg)
const activeStyle = computed(() => ({
  background: color.value.soft,
  borderLeftColor: color.value.border
}))

const filteredRows = computed(() => {
  if (!search.value) return props.rows
  const key = search.value.toLowerCase()
  return props.rows.filter((row) => Object.values(row || {}).some((value) => String(value ?? '').toLowerCase().includes(key)))
})

function displayValue(value) {
  if (value === null || value === undefined || value === '') return '-'
  return value
}

function isActiveStatus(value) {
  return ['Đang hoạt động', 'Đang áp dụng', 'HOAT_DONG', 'ACTIVE', 'DANG_HOAT_DONG'].includes(value)
}
</script>

<style scoped>
.bang-quy-trinh {
  flex: 1;
  min-width: 0;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.05);
}

.bang-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  padding: 8px;
  border-bottom: 1px solid #e5e7eb;
  background: #f8fafc;
}

.bang-title-wrap,
.bang-actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.bang-title {
  margin: 0;
  font-size: 13px;
  line-height: 18px;
  font-weight: 800;
  text-transform: uppercase;
}

.bang-db {
  font-size: 11px;
  color: #64748b;
  font-weight: 600;
}

.bang-search {
  position: relative;
}

.bang-search span {
  position: absolute;
  left: 9px;
  top: 50%;
  transform: translateY(-50%);
  color: #94a3b8;
  font-size: 13px;
}

.bang-search input {
  width: 160px;
  height: 28px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  padding: 0 8px 0 27px;
  font-size: 12px;
  outline: none;
}

.bang-search input:focus {
  border-color: #2563eb;
}

.btn-them {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  height: 28px;
  padding: 0 9px;
  border: 0;
  border-radius: 4px;
  color: #ffffff;
  font-size: 11px;
  font-weight: 700;
  cursor: pointer;
}

.bang-body {
  overflow: auto;
  max-height: 280px;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
  white-space: nowrap;
}

thead {
  position: sticky;
  top: 0;
  z-index: 2;
  background: #f8fafc;
  color: #334155;
  box-shadow: 0 1px 0 #e5e7eb;
}

th,
td {
  padding: 8px 12px;
  text-align: left;
  border-bottom: 1px solid #f1f5f9;
}

th {
  font-weight: 700;
}

.with-border,
.stt {
  border-right: 1px solid #f1f5f9;
}

.stt {
  width: 34px;
  text-align: center;
}

.data-row {
  cursor: pointer;
  border-left: 3px solid transparent;
  transition: background 0.15s ease, border-color 0.15s ease;
}

.data-row:hover {
  background: #f8fafc;
}

.text-muted {
  color: #64748b;
}

.status {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 2px 7px;
  font-size: 10px;
  font-weight: 700;
  border: 1px solid transparent;
}

.status.success {
  background: #dcfce7;
  color: #15803d;
  border-color: #bbf7d0;
}

.status.danger {
  background: #fee2e2;
  color: #b91c1c;
  border-color: #fecaca;
}

.cot-thao-tac {
  width: 96px;
  text-align: center;
  border-left: 1px solid #f1f5f9;
}

.icon-btn {
  border: 0;
  background: transparent;
  color: #64748b;
  cursor: pointer;
  padding: 0 3px;
  font-size: 13px;
}

.icon-btn:hover {
  color: #2563eb;
}

.icon-btn.danger:hover {
  color: #dc2626;
}

.empty-cell {
  padding: 18px;
  text-align: center;
  color: #94a3b8;
}

.mau-blue { color: #2563eb; }
.mau-indigo { color: #1e40af; }
.mau-purple { color: #7e22ce; }
.mau-orange { color: #ea580c; }
.mau-green { color: #16a34a; }
.mau-slate { color: #475569; }

.nen-blue { background: #2563eb; }
.nen-indigo { background: #1e40af; }
.nen-purple { background: #7e22ce; }
.nen-orange { background: #ea580c; }
.nen-green { background: #16a34a; }
.nen-slate { background: #475569; }

@media (max-width: 900px) {
  .bang-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .bang-search input {
    width: 140px;
  }
}
</style>
