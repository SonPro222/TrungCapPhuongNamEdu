<template>
  <div class="crud-card">
    <div class="toolbar">
      <input
          :value="keyword"
          class="search-input"
          type="text"
          placeholder="Tìm kiếm..."
          @input="$emit('update:keyword', $event.target.value)"
      />
      <button class="btn secondary" type="button" @click="$emit('reload')">Tải lại</button>
      <button v-if="showCreate" class="btn primary" type="button" @click="$emit('create')">Thêm mới</button>
    </div>

    <div v-if="loading" class="state">Đang tải dữ liệu...</div>
    <div v-else-if="error" class="state error">{{ error }}</div>
    <div v-else class="table-wrap">
      <table>
        <thead>
        <tr>
          <th v-for="column in columns" :key="column.key">{{ column.label }}</th>
          <th class="action-col">Thao tác</th>
        </tr>
        </thead>
        <tbody>
        <tr v-if="!items.length">
          <td :colspan="columns.length + 1" class="empty">Không có dữ liệu</td>
        </tr>
        <tr v-for="item in items" :key="item.id">
          <td v-for="column in columns" :key="column.key">
            <slot :name="`cell-${column.key}`" :item="item" :value="item[column.key]">
              {{ formatValue(item[column.key]) }}
            </slot>
          </td>
          <td class="actions">
            <button v-if="showView" class="link-btn" type="button" @click="$emit('view', item)">Xem</button>
            <button v-if="showEdit" class="link-btn" type="button" @click="$emit('edit', item)">Sửa</button>
            <button v-if="showDelete" class="link-btn danger" type="button" @click="$emit('remove', item)">Xóa</button>
            <slot name="row-actions" :item="item" />
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
defineProps({
  columns: { type: Array, required: true },
  items: { type: Array, default: () => [] },
  keyword: { type: String, default: '' },
  loading: { type: Boolean, default: false },
  error: { type: String, default: '' },
  showCreate: { type: Boolean, default: true },
  showView: { type: Boolean, default: true },
  showEdit: { type: Boolean, default: true },
  showDelete: { type: Boolean, default: true },
});

defineEmits(['update:keyword', 'reload', 'create', 'view', 'edit', 'remove']);

const formatValue = (value) => {
  if (value === true) return 'Có';
  if (value === false) return 'Không';
  if (value === null || value === undefined || value === '') return '-';
  return value;
};
</script>

<style scoped>
.crud-card {
  background: rgba(255, 255, 255, 0.92);
  border: 1px solid rgba(18, 72, 104, 0.08);
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 16px 40px rgba(19, 57, 79, 0.08);
}

.toolbar {
  display: flex;
  gap: 10px;
  padding: 16px;
  border-bottom: 1px solid #e5edf3;
  background: #f7fbfe;
}

.search-input {
  flex: 1;
  min-width: 220px;
  padding: 11px 13px;
  border: 1px solid #c9d9e6;
  border-radius: 14px;
  outline: none;
  color: #123046;
}

.btn,
.link-btn {
  cursor: pointer;
  border: 0;
  border-radius: 10px;
  font-weight: 700;
}

.btn {
  padding: 10px 14px;
}

.primary {
  color: #fff;
  background: #0f766e;
}

.secondary {
  color: #123046;
  background: #e9f3fa;
}

.table-wrap {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  padding: 13px 14px;
  text-align: left;
  border-bottom: 1px solid #e5edf3;
  vertical-align: top;
}

th {
  color: #496176;
  background: #f7fbfe;
  font-size: 13px;
  font-weight: 800;
  white-space: nowrap;
}

.actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
  min-width: 190px;
}

.action-col {
  width: 220px;
}

.link-btn {
  color: #0f766e;
  background: transparent;
  padding: 0;
}

.link-btn.danger {
  color: #dc2626;
}

.state,
.empty {
  padding: 24px;
  color: #64748b;
  text-align: center;
}

.error {
  color: #dc2626;
}
</style>
