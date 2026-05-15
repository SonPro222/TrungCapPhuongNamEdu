<script setup>
defineEmits(['edit', 'delete', 'view', 'row-click']);

defineProps({
  columns: { type: Array, default: () => [] },
  rows: { type: Array, default: () => [] },
  loading: { type: Boolean, default: false },
  error: { type: String, default: '' },
  rowKey: { type: String, default: 'id' },
  emptyText: { type: String, default: 'Chưa có dữ liệu.' },
  showActions: { type: Boolean, default: true },
  compact: { type: Boolean, default: true },
});

const getValue = (row, column) => {
  if (typeof column.formatter === 'function') return column.formatter(row);
  return row?.[column.key] ?? '—';
};

const getKey = (row, index, rowKey) => row?.[rowKey] ?? row?.id ?? row?.ID ?? row?._id ?? index;
</script>

<template>
  <section class="base-table-card">
    <div v-if="loading" class="base-table-state">Đang tải dữ liệu...</div>
    <div v-else-if="error" class="base-table-state is-error">{{ error }}</div>

    <div v-else class="base-table-scroll">
      <table :class="['base-table', { compact }]">
        <thead>
          <tr>
            <th
              v-for="column in columns"
              :key="column.key"
              :style="{ width: column.width, minWidth: column.minWidth }"
              :class="{ 'text-right': column.align === 'right' }"
            >
              {{ column.label }}
            </th>
            <th v-if="showActions" class="text-right action-column">Thao tác</th>
          </tr>
        </thead>

        <tbody>
          <tr
            v-for="(row, index) in rows"
            :key="getKey(row, index, rowKey)"
            @click="$emit('row-click', row)"
          >
            <td
              v-for="column in columns"
              :key="column.key"
              :class="[{ 'text-right': column.align === 'right' }, column.class]"
            >
              <span v-if="column.type === 'badge'" :class="['base-badge', column.badgeClass?.(row)]">
                {{ getValue(row, column) }}
              </span>
              <span v-else-if="column.primary" class="cell-primary">{{ getValue(row, column) }}</span>
              <span v-else>{{ getValue(row, column) }}</span>
            </td>
            <td v-if="showActions" class="text-right">
              <div class="table-actions" @click.stop>
                <button class="mini-btn view" type="button" @click="$emit('view', row)">Mở</button>
                <button class="mini-btn" type="button" @click="$emit('edit', row)">Sửa</button>
                <button class="mini-btn danger" type="button" @click="$emit('delete', row)">Xóa</button>
              </div>
            </td>
          </tr>

          <tr v-if="!rows.length">
            <td :colspan="columns.length + (showActions ? 1 : 0)" class="empty-cell">{{ emptyText }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<style scoped>
.base-table-card{overflow:hidden;border:1px solid #dbe3ee;border-radius:12px;background:#fff;box-shadow:0 8px 22px rgba(15,23,42,.045)}
.base-table-scroll{width:100%;overflow:auto}
.base-table{width:100%;min-width:900px;border-collapse:separate;border-spacing:0;color:#172033;font-size:12.5px}
.base-table th{position:sticky;top:0;z-index:2;padding:9px 12px;border-bottom:1px solid #dbe3ee;background:#f8fafc;color:#475569;text-align:left;font-size:11px;font-weight:850;text-transform:uppercase;letter-spacing:.035em;white-space:nowrap}
.base-table td{padding:10px 12px;border-bottom:1px solid #edf1f6;vertical-align:middle;white-space:nowrap;color:#24344d}
.base-table.compact td{padding:9px 12px}
.base-table tbody tr:hover td{background:#f8fbff}
.cell-primary{font-weight:800;color:#172033;white-space:normal}
.action-column{width:130px}
.text-right{text-align:right!important}
.table-actions{display:inline-flex;gap:5px;align-items:center;justify-content:flex-end;white-space:nowrap}
.mini-btn{min-height:26px;border:1px solid #cbd7e7;border-radius:6px;background:#fff;color:#253650;padding:0 8px;font-size:11px;font-weight:750;cursor:pointer}
.mini-btn:hover{background:#f8fafc}.mini-btn.view{border-color:#dbe8ff;background:#eef4ff;color:#1e40af}.mini-btn.danger{border-color:#fecaca;background:#fff1f0;color:#b42318}
.base-badge{display:inline-flex;align-items:center;border-radius:999px;background:#eef2f7;color:#334155;padding:4px 9px;font-size:11px;font-weight:800;line-height:1.15}.base-badge.ok{background:#dcfce7;color:#166534}.base-badge.warn{background:#fef3c7;color:#92400e}
.empty-cell,.base-table-state{padding:24px 14px;text-align:center;color:#66748a;font-size:12.5px}.base-table-state.is-error{color:#b91c1c;background:#fef2f2}
</style>
