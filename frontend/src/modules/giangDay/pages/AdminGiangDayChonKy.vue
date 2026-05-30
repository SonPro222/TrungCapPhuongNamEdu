<template>
  <section class="page">
    <header class="page-head">
      <div>
        <button type="button" class="btn ghost" @click="quayLai">← Quay lại chọn version</button>
        <h1>Chọn kỳ</h1>
        <p>Hiển thị các kỳ để quản lý lớp học phần theo version đã chọn.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="info-card">
      <span><b>Ngành:</b> {{ tenNganhDaChon }}</span>
      <span><b>Chương trình:</b> {{ tenChuongTrinhDaChon }}</span>
      <span><b>Version:</b> {{ tenVersionDaChon }}</span>
    </section>

    <section class="grid-card">
      <article v-if="dangTai" class="empty-card">Đang tải danh sách kỳ...</article>
      <article v-else-if="danhSachKhungKyTheoVersion.length === 0" class="empty-card">Version này chưa có kỳ/môn trong chương trình.</article>

      <article v-for="item in danhSachKhungKyTheoVersion" v-else :key="item.id" class="item-card">
        <div>
          <strong>{{ item.maKy || item.ma || '-' }}</strong>
          <h3>{{ item.tenKy || item.ten || item.name || `Kỳ ${item.id}` }}</h3>
          <p>ID: {{ item.id }}</p>
        </div>

        <button type="button" class="btn primary" @click="chonKy(item)">Quản lý lớp học phần</button>
      </article>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

const nganhId = computed(() => Number(route.params.nganhId))
const chuongTrinhId = computed(() => Number(route.params.chuongTrinhId))
const versionId = computed(() => Number(route.params.versionId))
const dangTai = ref(false)
const loi = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])

const nganhDaChon = computed(() => danhSachNganh.value.find((item) => Number(item.id) === nganhId.value))
const chuongTrinhDaChon = computed(() => danhSachChuongTrinh.value.find((item) => Number(item.id) === chuongTrinhId.value))
const versionDaChon = computed(() => danhSachVersion.value.find((item) => Number(item.id) === versionId.value))

const tenNganhDaChon = computed(() => {
  const item = nganhDaChon.value
  if (!item) return `ID ${nganhId.value}`
  return `${item.maNganh ? item.maNganh + ' - ' : ''}${item.tenNganh || item.ten || item.name || item.id}`
})

const tenChuongTrinhDaChon = computed(() => {
  const item = chuongTrinhDaChon.value
  if (!item) return `ID ${chuongTrinhId.value}`
  return `${item.maChuongTrinh ? item.maChuongTrinh + ' - ' : ''}${item.tenChuongTrinh || item.ten || item.name || item.id}`
})

const tenVersionDaChon = computed(() => {
  const item = versionDaChon.value
  if (!item) return `ID ${versionId.value}`
  return `${item.maVersion ? item.maVersion + ' - ' : ''}${item.tenVersion || item.ten || item.name || item.id}`
})

const danhSachKhungKyTheoVersion = computed(() => {
  return danhSachKhungKy.value
      .filter((item) => {
        const itemVersionId =
            item.chuongTrinhVersionId ??
            item.versionId ??
            item.chuong_trinh_version_id

        if (itemVersionId) {
          return Number(itemVersionId) === versionId.value
        }

        return true
      })
      .sort((a, b) => {
        const thuTuA = Number(a.thuTu ?? a.thu_tu ?? a.soThuTu ?? a.id ?? 0)
        const thuTuB = Number(b.thuTu ?? b.thu_tu ?? b.soThuTu ?? b.id ?? 0)
        return thuTuA - thuTuB
      })
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''

  try {
    const [nganh, chuongTrinh, version, khungKy, chuongTrinhMon] = await Promise.all([
      giangDayService.layDanhSachNganh(),
      giangDayService.layDanhSachChuongTrinh(),
      giangDayService.layDanhSachChuongTrinhVersion(),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachChuongTrinhMon()
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
    danhSachKhungKy.value = khungKy
    danhSachChuongTrinhMon.value = chuongTrinhMon
  } catch (error) {
    loi.value = error?.message || 'Không tải được danh sách kỳ'
  } finally {
    dangTai.value = false
  }
}

function quayLai() {
  router.push({
    name: 'GiangDay.ChonVersion',
    params: {
      nganhId: nganhId.value,
      chuongTrinhId: chuongTrinhId.value
    }
  })
}

function chonKy(item) {
  router.push({
    name: 'GiangDay.LopHocPhanTheoKy',
    params: {
      nganhId: nganhId.value,
      chuongTrinhId: chuongTrinhId.value,
      versionId: versionId.value,
      khungKyId: item.id
    }
  })
}
</script>

<style scoped>
.page { display: flex; flex-direction: column; gap: 16px; }
.page-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
.page-head h1 { margin: 8px 0 0; }
.page-head p { margin: 6px 0 0; color: #64748b; }
.info-card, .item-card, .empty-card { border: 1px solid var(--color-border); border-radius: var(--radius); padding: 16px; background: var(--color-white); }
.info-card { display: flex; gap: 12px; flex-wrap: wrap; }
.grid-card { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }
.item-card { display: flex; flex-direction: column; gap: 12px; justify-content: space-between; }
.item-card strong { color: var(--color-primary); }
.item-card h3 { margin: 4px 0; }
.item-card p { margin: 0; color: #64748b; }
.btn { border: 1px solid var(--color-border); background: var(--color-white); color: var(--color-text); border-radius: var(--radius); padding: 8px 12px; cursor: pointer; font-weight: 700; }
.btn.primary { background: var(--color-primary); color: var(--color-white); border-color: var(--color-primary); }
.btn.ghost { border-color: transparent; padding-left: 0; }
.alert { padding: 10px 12px; border-radius: var(--radius); }
.alert.error { background: #fee2e2; color: #991b1b; }
</style>
