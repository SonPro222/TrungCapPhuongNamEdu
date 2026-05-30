<template>
  <section class="xem-bang-thong-tin">
    <h3 v-if="tieuDe" class="khoi-title">{{ tieuDe }}</h3>

    <!-- Chế độ thông tin: 1 bản ghi, hiển thị từng trường -->
    <div v-if="kieu === 'thongTin'" class="thong-tin-grid">
      <template v-if="coDuLieuThongTin">
        <div v-for="truong in truongHienThi" :key="truong.key" class="thong-tin-item">
          <span class="thong-tin-label">{{ truong.nhan }}</span>
          <span class="thong-tin-value">{{ dinhDang(duLieu?.[truong.key]) }}</span>
        </div>
      </template>
      <p v-else class="trong-text">{{ thongBaoRong }}</p>
    </div>

    <!-- Chế độ bảng: nhiều dòng, nhiều cột -->
    <div v-else class="bang-wrap">
      <table v-if="dong && dong.length" class="bang">
        <thead>
          <tr>
            <th v-for="c in cot" :key="c.key">{{ c.nhan }}</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="(row, index) in dong" :key="row.id || index">
            <td v-for="c in cot" :key="c.key">{{ dinhDang(layGiaTri(row, c.key)) }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else class="trong-text">{{ thongBaoRong }}</p>
    </div>
  </section>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  tieuDe: { type: String, default: '' },
  // 'bang' | 'thongTin'
  kieu: { type: String, default: 'bang' },

  // dùng cho kieu = 'bang'
  cot: { type: Array, default: () => [] }, // [{ key, nhan }]
  dong: { type: Array, default: () => [] },

  // dùng cho kieu = 'thongTin'
  truong: { type: Array, default: () => [] }, // [{ key, nhan }]
  duLieu: { type: Object, default: () => ({}) },

  thongBaoRong: { type: String, default: 'Chưa có dữ liệu.' }
})

// chỉ hiển thị trường có dữ liệu
const truongHienThi = computed(() =>
  props.truong.filter(t => {
    const v = props.duLieu?.[t.key]
    return v !== null && v !== undefined && v !== ''
  })
)

const coDuLieuThongTin = computed(() => truongHienThi.value.length > 0)

function layGiaTri(row, key) {
  return row?.[key]
}

function dinhDang(value) {
  if (value === null || value === undefined || value === '') return '-'
  if (typeof value === 'boolean') return value ? 'Có' : 'Không'
  return value
}
</script>

<style scoped>
.xem-bang-thong-tin {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 16px;
}
.khoi-title {
  margin: 0 0 12px;
  font-size: 15px;
  font-weight: 700;
  color: #1e293b;
}
.thong-tin-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 10px;
}
.thong-tin-item {
  display: flex;
  flex-direction: column;
  gap: 2px;
  background: #f8fafc;
  border-radius: 8px;
  padding: 8px 10px;
}
.thong-tin-label {
  font-size: 11px;
  color: #94a3b8;
}
.thong-tin-value {
  font-size: 14px;
  color: #1e293b;
  font-weight: 500;
  word-break: break-word;
}
.bang-wrap {
  overflow-x: auto;
}
.bang {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}
.bang th,
.bang td {
  border: 1px solid #e2e8f0;
  padding: 8px 10px;
  text-align: left;
  vertical-align: top;
}
.bang th {
  background: #f1f5f9;
  color: #475569;
  font-weight: 600;
  white-space: nowrap;
}
.trong-text {
  margin: 0;
  color: #94a3b8;
  font-size: 13px;
}
</style>
