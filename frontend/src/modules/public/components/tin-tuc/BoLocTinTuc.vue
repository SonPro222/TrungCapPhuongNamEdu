<template>
  <section class="tin-tuc-filter-section">
    <div class="public-container">
      <div class="news-filter-tabs">
        <button
            v-for="item in danhMucTin"
            :key="item"
            type="button"
            :class="{ active: danhMucDangChon === item }"
            @click="chonDanhMuc(item)"
        >
          {{ item }}
        </button>
      </div>
    </div>
  </section>
</template>

<script setup>
import { danhMucTinTuc } from '@/modules/public/data/tinTucData.js'

defineProps({
  danhMucDangChon: {
    type: String,
    required: true
  }
})

const emit = defineEmits(['update:danhMucDangChon'])

const danhMucTin = danhMucTinTuc

const chonDanhMuc = (item) => {
  emit('update:danhMucDangChon', item)
}
</script>

<style scoped>
.tin-tuc-filter-section {
  background: #ffffff;
}

.news-filter-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  padding-bottom: 16px;
  border-bottom: 1px solid #edf4fb;
}

.news-filter-tabs button {
  min-height: 38px;
  padding: 0 18px;
  border: 1px solid #dbe9f8;
  border-radius: 999px;
  background: #ffffff;
  color: #607894;
  font-size: 13px;
  font-weight: 900;
  cursor: pointer;
  transition: 0.2s;
}

.news-filter-tabs button.active,
.news-filter-tabs button:hover {
  background: #075fc8;
  border-color: #075fc8;
  color: #ffffff;
  box-shadow: 0 8px 18px rgba(7, 95, 200, 0.18);
}
</style>