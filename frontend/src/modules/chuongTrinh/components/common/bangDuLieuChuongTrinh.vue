<template>
  <div class="ct-table-wrap">
    <table class="ct-table">
      <thead>
        <tr>
          <th v-for="column in columns" :key="column.key" :class="{ 'ct-right': column.align === 'right' }">{{ column.label }}</th>
          <th v-if="actions" class="ct-action-col">Thao tác</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="!rows.length">
          <td :colspan="columns.length + (actions ? 1 : 0)" class="ct-empty">Chưa có dữ liệu phù hợp.</td>
        </tr>
        <tr v-for="row in rows" :key="row.id" :class="{ 'ct-clickable-row': clickable }" @click="$emit('row-click', row)">
          <td v-for="column in columns" :key="column.key" :class="{ 'ct-right': column.align === 'right' }">
            <span v-if="column.type === 'boolean'" :class="['ct-badge', row[column.key] ? 'ok' : 'warn']">{{ row[column.key] ? 'Có' : 'Không' }}</span>
            <span v-else-if="column.type === 'date'">{{ dinhDangNgay(row[column.key]) }}</span>
            <span v-else>{{ row[column.key] ?? '—' }}</span>
          </td>
          <td v-if="actions" class="ct-action-col" @click.stop>
            <div class="ct-row-actions"><slot name="actions" :row="row" /></div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import { dinhDangNgay } from '@/modules/chuongTrinh/utils/hienThiChuongTrinh.js';

defineProps({
  rows: { type: Array, default: () => [] },
  columns: { type: Array, default: () => [] },
  actions: { type: Boolean, default: true },
  clickable: { type: Boolean, default: false },
});

defineEmits(['row-click']);
</script>
