<template>
  <section class="nganh-main">
    <div class="public-container filter-box">
      <div class="filter-tabs">
        <button
            v-for="item in danhMuc"
            :key="item"
            type="button"
            :class="{ active: danhMucDangChon === item }"
            @click="chonDanhMuc(item)"
        >
          <i :class="layIconDanhMuc(item)"></i>
          <span>{{ item }}</span>
        </button>
      </div>

      <div class="search-box-nganh">
        <input
            :value="tuKhoa"
            type="text"
            placeholder="Tìm ngành đào tạo..."
            @input="capNhatTuKhoa"
        />
        <i class="fa-solid fa-magnifying-glass"></i>
      </div>
    </div>
  </section>
</template>

<script setup>
import { danhMucNganhDaoTao } from '@/modules/public/data/nganhDaoTaoData.js'

defineProps({
  danhMucDangChon: {
    type: String,
    required: true
  },
  tuKhoa: {
    type: String,
    required: true
  }
})

const emit = defineEmits([
  'update:danhMucDangChon',
  'update:tuKhoa'
])

const danhMuc = danhMucNganhDaoTao

const chonDanhMuc = (item) => {
  emit('update:danhMucDangChon', item)
}

const capNhatTuKhoa = (event) => {
  emit('update:tuKhoa', event.target.value)
}

const layIconDanhMuc = (item) => {
  const icons = {
    'Tất cả ngành': 'fa-solid fa-table-cells-large',
    'Khối sức khỏe': 'fa-solid fa-heart-pulse',
    'Kinh tế - Dịch vụ': 'fa-solid fa-chart-pie',
    'Công nghệ - Kỹ thuật': 'fa-solid fa-microchip'
  }

  return icons[item] || 'fa-solid fa-folder'
}
</script>

<style scoped>
.nganh-main {
  background: #ffffff;
}

.filter-box {
  position: relative;
  z-index: 5;
  margin-top: -58px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 18px;
  padding: 10px;
  border: 1px solid #e6eef8;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 18px 40px rgba(7, 63, 131, 0.12);
}

.filter-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.filter-tabs button {
  min-height: 44px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  border: none;
  border-radius: 11px;
  background: transparent;
  color: #607894;
  padding: 0 18px;
  font-size: 14px;
  font-weight: 900;
  cursor: pointer;
}

.filter-tabs button:hover {
  background: #f1f5f9;
}

.filter-tabs button.active {
  background: #063b7a;
  color: #ffffff;
  box-shadow: 0 8px 18px rgba(6, 59, 122, 0.22);
}

.search-box-nganh {
  position: relative;
  flex: 0 0 310px;
}

.search-box-nganh input {
  width: 100%;
  height: 44px;
  border: 1px solid #dbe9f8;
  border-radius: 11px;
  outline: none;
  background: #f8fbff;
  color: #123b68;
  padding: 0 38px 0 14px;
  font-size: 13px;
  font-weight: 600;
}

.search-box-nganh i {
  position: absolute;
  right: 14px;
  top: 50%;
  color: #94a3b8;
  font-size: 13px;
  transform: translateY(-50%);
}

@media (max-width: 1180px) {
  .filter-box {
    display: block;
  }

  .search-box-nganh {
    flex: none;
    width: 100%;
    margin-top: 10px;
  }
}

@media (max-width: 760px) {
  .filter-tabs {
    display: grid;
    grid-template-columns: 1fr;
  }
}
</style>