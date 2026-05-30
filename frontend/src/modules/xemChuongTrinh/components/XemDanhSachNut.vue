<template>
  <div class="xem-danh-sach-nut">
    <button
      v-for="(item, index) in danhSach"
      :key="item.id || index"
      type="button"
      class="nut-card"
      @click="$emit('chon', item)"
    >
      <span class="nut-card__ten">{{ layTen(item) }}</span>
      <span v-if="moTaKeys.length" class="nut-card__phu">{{ layMoTa(item) }}</span>
    </button>
  </div>
</template>

<script setup>
import { layTenHienThi } from '../services/xemChuongTrinhService'

const props = defineProps({
  danhSach: {
    type: Array,
    default: () => []
  },
  // keys ưu tiên để lấy tên hiển thị
  tenKeys: {
    type: Array,
    default: () => []
  },
  // keys phụ hiển thị nhỏ phía dưới (không bắt buộc)
  moTaKeys: {
    type: Array,
    default: () => []
  }
})

defineEmits(['chon'])

function layTen(item) {
  return layTenHienThi(item, props.tenKeys)
}

function layMoTa(item) {
  for (const key of props.moTaKeys) {
    const value = item?.[key]
    if (value !== null && value !== undefined && value !== '') return value
  }
  return ''
}
</script>

<style scoped>
.xem-danh-sach-nut {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 12px;
}
.nut-card {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  gap: 4px;
  text-align: left;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.15s ease;
}
.nut-card:hover {
  border-color: #6366f1;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.15);
  transform: translateY(-1px);
}
.nut-card__ten {
  font-size: 15px;
  font-weight: 600;
  color: #1e293b;
}
.nut-card__phu {
  font-size: 12px;
  color: #94a3b8;
}
</style>
