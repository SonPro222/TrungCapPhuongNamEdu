<template>
  <main class="dao-tao-view-page">
    <header class="mini-head">
      <div>
        <h2>Luồng đào tạo xem</h2>
        <p>Bắt đầu từ Ngành đào tạo, sau đó đi tiếp vào Chương trình, Version và các bảng con.</p>
      </div>
    </header>

    <BangQuanLyDaoTao
      title="1. Ngành đào tạo"
      description="Tầng đầu tiên của luồng đào tạo. Bấm Xem để đi vào chương trình của ngành."
      :service="crudServices.nganh"
      :fields="configs.nganh.fields"
      :columns="configs.nganh.columns"
      :default-form="configs.nganh.defaultForm"
      :rows="nganh"
      :lookups="lookups"
      @saved="load"
      @deleted="load"
      @view="xemChuongTrinh"
    />
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import BangQuanLyDaoTao from '../components/BangQuanLyDaoTao.vue'
import { configs, crudServices, luaChonDaoTao, taiBang } from '../services/daoTaoXemChuongTrinhService'

const router = useRouter()
const nganh = ref([])
const lookups = computed(() => ({ nganh: nganh.value, ...luaChonDaoTao }))
async function load() { nganh.value = await taiBang('nganh') }
function xemChuongTrinh(item) {
  router.push({ name: 'ChuongTrinh.DaoTaoChuongTrinhTheoNganh', params: { nganhId: item.id }, query: { nganhTen: item.tenNganh } })
}
onMounted(load)
</script>

<style scoped>
.dao-tao-view-page {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.mini-head {
  display: flex;
  gap: 10px;
  align-items: center;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  border-radius: 4px;
  padding: 10px;
  color: #1e3a8a;
}

.mini-head h2 {
  margin: 0;
  font-size: 18px;
  color: #1e3a8a;
}

.mini-head p {
  margin: 4px 0 0;
  color: #1d4ed8;
  font-size: 13px;
}

.btn {
  border: 1px solid #93c5fd;
  background: #ffffff;
  color: #1e40af;
  border-radius: 3px;
  padding: 6px 10px;
  cursor: pointer;
  font-weight: 700;
}

.btn:hover {
  background: #dbeafe;
}
</style>
