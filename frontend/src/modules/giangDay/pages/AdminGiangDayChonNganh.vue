<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Chọn ngành</h1>
        <p>Đi theo flow: Ngành → Chương trình → Version → Kỳ → Lớp học phần.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="search-card">
      <label>
        <span>Tìm ngành</span>
        <input v-model.trim="keyword" type="text" placeholder="Mã ngành, tên ngành" />
      </label>
    </section>

    <section class="grid-card">
      <article v-if="dangTai" class="empty-card">Đang tải danh sách ngành...</article>
      <article v-else-if="danhSachHienThi.length === 0" class="empty-card">Không có ngành phù hợp.</article>

      <article v-for="item in danhSachHienThi" v-else :key="item.id" class="item-card">
        <div>
          <strong>{{ item.maNganh || item.ma || '-' }}</strong>
          <h3>{{ item.tenNganh || item.ten || item.name || '-' }}</h3>
          <p>ID: {{ item.id }}</p>
        </div>

        <button type="button" class="btn primary" @click="chonNganh(item)">
          Chọn ngành
        </button>
      </article>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const router = useRouter()

const dangTai = ref(false)
const loi = ref('')
const keyword = ref('')
const danhSach = ref([])

const danhSachHienThi = computed(() => {
  const tuKhoa = keyword.value.toLowerCase()
  if (!tuKhoa) return danhSach.value

  return danhSach.value.filter((item) => {
    return `${item.maNganh || item.ma || ''} ${item.tenNganh || item.ten || item.name || ''}`
        .toLowerCase()
        .includes(tuKhoa)
  })
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    danhSach.value = await giangDayService.layDanhSachNganh()
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách ngành'
  } finally {
    dangTai.value = false
  }
}

function chonNganh(item) {
  router.push({
    name: 'GiangDay.ChonChuongTrinh',
    params: {
      nganhId: item.id
    }
  })
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
.page-head h1 { margin: 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.search-card { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 16px; background: var(--color-white); }
label { display: flex; flex-direction: column; gap: 6px; }
label span { font-weight: 700; color: #334155; }
input { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 9px 11px; }
.grid-card { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }
.item-card, .empty-card { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 16px; background: var(--color-white); display: flex; flex-direction: column; gap: 12px; justify-content: space-between; }
.item-card strong { color: var(--color-primary); }
.item-card h3 { margin: 4px 0; }
.item-card p { margin: 0; color: #64748b; }
.btn { border: 1px solid var(--color-border); background: var(--color-white); color: var(--color-text); border-radius: var(--radius); padding: 8px 12px; cursor: pointer; font-weight: 700; }
.btn.primary { background: var(--color-primary); color: var(--color-white); border-color: var(--color-primary); }
.alert { padding: 10px 12px; border-radius: var(--radius); }
.alert.error { background: #fee2e2; color: #991b1b; }
</style>
