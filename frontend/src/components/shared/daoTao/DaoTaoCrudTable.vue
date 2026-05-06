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
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
}

.toolbar {
  display: flex;
  gap: 10px;
  padding: 14px;
  border-bottom: 1px solid #e2e8f0;
  background: #f8fafc;
}

.search-input {
  flex: 1;
  min-width: 220px;
  padding: 10px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  outline: none;
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
  background: #2563eb;
}

.secondary {
  color: #0f172a;
  background: #e2e8f0;
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
  border-bottom: 1px solid #e2e8f0;
  vertical-align: top;
}

th {
  color: #475569;
  background: #f8fafc;
  font-size: 13px;
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
  color: #2563eb;
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
