<template>
  <section class="bang-chon">
    <div class="bang-header">
      <div>
        <h3>{{ title }}</h3>
        <small v-if="dbName">{{ dbName }}</small>
      </div>
      <span class="count">{{ rows.length }} dòng</span>
    </div>

    <div class="table-wrap">
      <table>
        <thead>
          <tr>
            <th class="action-col">Chọn</th>
            <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="loading">
            <td :colspan="columns.length + 1" class="empty-cell">Đang tải dữ liệu...</td>
          </tr>
          <tr v-else-if="!rows.length">
            <td :colspan="columns.length + 1" class="empty-cell">Chưa có dữ liệu</td>
          </tr>
          <tr
              v-for="row in rows"
              v-else
              :key="row.id"
              :class="{ selected: selectedId === row.id, linked: isLinked(row) }"
          >
            <td class="action-col">
              <button
                  type="button"
                  class="tick-btn"
                  :class="{ active: selectedId === row.id || isLinked(row) }"
                  @click="$emit('select-row', row)"
              >
                ✓
              </button>
            </td>
            <td v-for="column in columns" :key="column.key">
              {{ formatValue(row[column.key]) }}
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
defineEmits(['select-row'])

const props = defineProps({
  title: { type: String, required: true },
  dbName: { type: String, default: '' },
  columns: { type: Array, default: () => [] },
  rows: { type: Array, default: () => [] },
  selectedId: { type: [String, Number, null], default: null },
  linkedKey: { type: String, default: '' },
  linkedValue: { type: [String, Number, null], default: null },
  loading: { type: Boolean, default: false }
})

function isLinked(row) {
  if (!props.linkedKey || props.linkedValue === null || props.linkedValue === undefined) return false
  return row?.[props.linkedKey] === props.linkedValue
}

function formatValue(value) {
  if (value === true) return 'Có'
  if (value === false) return 'Không'
  if (value === null || value === undefined || value === '') return '-'
  return value
}
</script>

<style scoped>
.bang-chon {
  min-width: 0;
  border: 1px solid #e5e7eb;
  border-radius: 10px;
  background: #ffffff;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.06);
}

.bang-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 10px 12px;
  border-bottom: 1px solid #e5e7eb;
  background: #f8fafc;
}

.bang-header h3 {
  margin: 0;
  color: #0f172a;
  font-size: 13px;
  font-weight: 800;
}

.bang-header small {
  display: block;
  margin-top: 2px;
  color: #64748b;
  font-size: 11px;
  font-weight: 700;
}

.count {
  flex: 0 0 auto;
  border-radius: 999px;
  background: #eff6ff;
  color: #1d4ed8;
  padding: 3px 8px;
  font-size: 11px;
  font-weight: 800;
}

.table-wrap {
  max-height: 320px;
  overflow: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  font-size: 12px;
}

th,
td {
  padding: 8px 10px;
  border-bottom: 1px solid #f1f5f9;
  text-align: left;
  white-space: nowrap;
}

th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f8fafc;
  color: #475569;
  font-size: 11px;
  font-weight: 800;
}

tr.selected td {
  background: #ecfdf5;
}

tr.linked td {
  background: #eff6ff;
}

.action-col {
  width: 58px;
  text-align: center;
}

.tick-btn {
  width: 28px;
  height: 28px;
  border: 1px solid #cbd5e1;
  border-radius: 999px;
  background: #ffffff;
  color: #94a3b8;
  cursor: pointer;
  font-weight: 900;
}

.tick-btn.active {
  border-color: #16a34a;
  background: #16a34a;
  color: #ffffff;
}

.empty-cell {
  padding: 22px 12px;
  color: #94a3b8;
  text-align: center;
}
</style>
