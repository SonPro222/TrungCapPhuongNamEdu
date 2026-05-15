<script setup>
defineEmits(['edit', 'delete', 'select']);

defineProps({
  columns: { type: Array, required: true },
  rows: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  error: { type: String, default: '' },
  formatCell: { type: Function, required: true },
});
</script>

<template>
  <section class="ct-table-card">
    <div v-if="loading" class="ct-state">Đang tải dữ liệu...</div>
    <div v-else-if="error" class="ct-state ct-state--danger">{{ error }}</div>

    <div v-else class="ct-table-wrap">
      <table class="ct-table">
        <thead>
          <tr>
            <th
              v-for="column in columns"
              :key="column.key"
              :style="{ width: column.width, minWidth: column.minWidth }"
              :class="{ 'text-end': column.align === 'right' }"
            >
              {{ column.label }}
            </th>
            <th class="ct-action-col">Thao tác</th>
          </tr>
        </thead>

        <tbody>
          <tr v-for="row in rows" :key="row.id || row.ID || row._id || JSON.stringify(row)" class="ct-data-row" @dblclick="$emit('select', row)">
            <td
              v-for="column in columns"
              :key="column.key"
              :title="formatCell(row, column)"
              :class="{ 'text-end': column.align === 'right', 'ct-muted': formatCell(row, column) === '—' }"
            >
              <span v-if="column.type === 'boolean'" :class="['ct-badge', row[column.key] ? 'ct-badge--success' : 'ct-badge--muted']">
                {{ formatCell(row, column) }}
              </span>
              <span v-else>{{ formatCell(row, column) }}</span>
            </td>

            <td class="ct-actions">
              <button type="button" class="ct-btn ct-btn--view" @click="$emit('select', row)">Mở</button>
              <button type="button" class="ct-btn ct-btn--soft" @click="$emit('edit', row)">Sửa</button>
              <button type="button" class="ct-btn ct-btn--danger-soft" @click="$emit('delete', row)">Xóa</button>
            </td>
          </tr>

          <tr v-if="!rows.length">
            <td :colspan="columns.length + 1" class="ct-empty">Chưa có dữ liệu phù hợp.</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<style scoped>
.ct-table-card{border:1px solid #e5e7eb;border-radius:12px;background:#fff;box-shadow:0 6px 18px rgba(15,23,42,.05);overflow:hidden}.ct-table-wrap{width:100%;overflow:auto}.ct-table{width:100%;min-width:920px;border-collapse:collapse;font-size:13px;color:#1f2937}.ct-table th{position:sticky;top:0;z-index:1;background:#f8fafc;color:#475569;font-size:12px;font-weight:700;text-align:left;padding:10px 12px;border-bottom:1px solid #e5e7eb;white-space:nowrap}.ct-table td{padding:9px 12px;border-bottom:1px solid #eef2f7;vertical-align:middle;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;max-width:280px}.ct-table tbody tr:hover td{background:#f9fafb}.ct-data-row{cursor:pointer}.ct-action-col{width:112px;text-align:right!important}.ct-actions{display:flex;justify-content:flex-end;gap:6px}.ct-btn{border:0;border-radius:7px;padding:5px 9px;font-size:12px;font-weight:700;line-height:1.2;cursor:pointer}.ct-btn--view{background:#eef2ff;color:#3730a3}.ct-btn--soft{background:#e0f2fe;color:#0369a1}.ct-btn--danger-soft{background:#fee2e2;color:#b91c1c}.ct-badge{display:inline-flex;align-items:center;border-radius:999px;padding:3px 8px;font-size:11px;font-weight:700}.ct-badge--success{background:#dcfce7;color:#166534}.ct-badge--muted{background:#f1f5f9;color:#64748b}.ct-muted{color:#94a3b8}.text-end{text-align:right}.ct-state,.ct-empty{padding:28px 16px;text-align:center;color:#64748b;font-size:13px;font-weight:600}.ct-state--danger{color:#b91c1c;background:#fef2f2}
</style>
