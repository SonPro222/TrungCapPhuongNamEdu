<template>
  <div class="data-table-wrap">
    <table class="data-table">
      <thead>
      <tr>
        <th v-for="col in columns" :key="col.key">
          {{ col.label }}
        </th>

        <th v-if="$slots.actions">
          Thao tác
        </th>
      </tr>
      </thead>

      <tbody>
      <tr v-if="loading">
        <td :colspan="columns.length + 1">
          Đang tải dữ liệu...
        </td>
      </tr>

      <tr v-else-if="!items.length">
        <td :colspan="columns.length + 1">
          Không có dữ liệu
        </td>
      </tr>

      <tr v-for="item in items" v-else :key="item[idKey]">
        <td v-for="col in columns" :key="col.key">
          {{ item[col.key] }}
        </td>

        <td v-if="$slots.actions">
          <slot name="actions" :item="item" />
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
defineProps({
  columns: {
    type: Array,
    required: true
  },
  items: {
    type: Array,
    default: () => []
  },
  idKey: {
    type: String,
    default: 'id'
  },
  loading: {
    type: Boolean,
    default: false
  }
})
</script>