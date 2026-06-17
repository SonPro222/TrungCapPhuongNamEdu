<template>
  <section class="lms-page">
    <div class="lp-head">
      <div>
        <h1 class="lp-h1">Điểm của tôi</h1>
        <p class="lp-sub">Xem điểm online từ LMS, điểm GV nhập trực tiếp và điểm tổng kết theo lớp học phần.</p>
      </div>
      <RouterLink :to="{ name: 'Lms.SinhVienBaiDuocGiao' }" class="lms-btn ghost">← Bài được giao</RouterLink>
    </div>

    <div class="panel filter">
      <label class="fl">Sinh viên ID
        <input v-model="sinhVienId" class="fi" placeholder="Nhập sinhVienId nếu tài khoản chưa map tự động" @keyup.enter="loadData" />
      </label>
      <button class="lms-btn primary" @click="loadData" :disabled="!sinhVienId || dangTai">Xem điểm</button>
    </div>

    <div v-if="dangTai" class="loading-state">Đang tải điểm...</div>
    <div v-else-if="!sinhVienId" class="empty-state">Nhập sinhVienId để xem điểm.</div>
    <div v-else-if="!nhomDiem.length" class="empty-state">Chưa có điểm nào được ghi nhận.</div>

    <div v-for="lop in nhomDiem" :key="lop.lopHocPhanId" class="panel">
      <div class="lop-head">
        <div>
          <h2>{{ lop.tenLopHocPhan }}</h2>
          <p>Lớp học phần #{{ lop.lopHocPhanId }}</p>
        </div>
        <div class="total">
          <span>Tổng kết</span>
          <b>{{ fmt(lop.ketQua?.diemTongKet) }}</b>
          <small>{{ lop.ketQua?.ketQua || 'chưa có kết quả' }}</small>
        </div>
      </div>
      <table class="lms-table">
        <thead><tr><th>Cột điểm</th><th>Loại</th><th>Tỷ lệ</th><th>Điểm</th><th>Nguồn</th><th>Nhận xét</th></tr></thead>
        <tbody>
          <tr v-for="cot in lop.cauHinhs" :key="cot.id">
            <td><b>{{ cot.tenCotDiem || cot.tenCauHinh || ('Cột #' + cot.id) }}</b></td>
            <td>{{ cot.loaiDiem }}</td>
            <td>{{ cot.tyLe }}%</td>
            <td>{{ fmt(findDiem(lop, cot.id)?.diem) }}</td>
            <td><span :class="['source', isOnlineCot(cot) ? 'online' : 'manual']">{{ isOnlineCot(cot) ? 'LMS online' : 'GV nhập' }}</span></td>
            <td>{{ findDiem(lop, cot.id)?.nhanXet || '-' }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { bangDiemLopService } from '../services/lmsService'
import { formatDiem } from '../services/lmsEnum'

const route = useRoute()
const dangTai = ref(false)
const sinhVienId = ref(route.query.sinhVienId || '')
const nhomDiem = ref([])

function fmt(v) { return v === null || v === undefined || v === '' ? '-' : formatDiem(v) }
function findDiem(lop, cauHinhDanhGiaId) { return lop.diemChiTiet.find(d => String(d.cauHinhDanhGiaId) === String(cauHinhDanhGiaId)) }
function isOnlineCot(cot) {
  const text = `${cot.loaiDiem || ''} ${cot.tenCotDiem || cot.tenCauHinh || ''}`.toLowerCase()
  return text.includes('bai_tap') || text.includes('online') || text.includes('lms')
}
async function loadData() {
  if (!sinhVienId.value) return
  dangTai.value = true
  try { nhomDiem.value = await bangDiemLopService.layDiemSinhVien(sinhVienId.value) }
  finally { dangTai.value = false }
}

onMounted(loadData)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display:flex; flex-direction:column; gap:18px; }
.lp-head,.filter,.lop-head { display:flex; align-items:flex-start; gap:12px; }
.lp-head > div,.lop-head > div:first-child { flex:1; }
.lp-h1 { margin:0 0 4px; font-size:22px; font-weight:800; color:#1e293b; }
.lp-sub,.lop-head p { margin:0; color:#64748b; font-size:13px; }
.panel { background:#fff; border:1px solid #e2e8f0; border-radius:12px; padding:16px; }
.fl { display:flex; flex-direction:column; gap:6px; font-size:12px; font-weight:700; color:#475569; min-width:280px; }
.fi { border:1px solid #e2e8f0; border-radius:8px; padding:9px 10px; font-size:13px; }
.loading-state,.empty-state { text-align:center; padding:40px; color:#64748b; background:#fff; border:1px dashed #cbd5e1; border-radius:12px; }
.lop-head h2 { margin:0 0 4px; font-size:18px; color:#1e293b; }
.total { text-align:right; background:#f8fafc; border-radius:10px; padding:10px 14px; min-width:120px; }
.total span,.total small { display:block; color:#64748b; font-size:12px; }
.total b { display:block; font-size:22px; color:#1e293b; }
.lms-table { width:100%; border-collapse:collapse; font-size:13px; margin-top:14px; }
.lms-table th { background:#f8fafc; padding:10px; text-align:left; color:#475569; border-bottom:2px solid #e2e8f0; }
.lms-table td { padding:10px; border-bottom:1px solid #f1f5f9; }
.source { padding:4px 8px; border-radius:999px; font-size:12px; font-weight:800; background:#f1f5f9; }
.source.online { background:#dcfce7; color:#166534; }
.source.manual { background:#e0f2fe; color:#075985; }
.lms-btn { border:1px solid #cbd5e1; border-radius:8px; padding:8px 12px; text-decoration:none; color:#334155; font-weight:700; background:#fff; cursor:pointer; }
.lms-btn.primary { background:#1d4ed8; color:#fff; border-color:#1d4ed8; }
</style>
