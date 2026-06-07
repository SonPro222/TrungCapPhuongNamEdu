<template>
  <section class="lms-page">
    <div class="lp-head">
      <div>
        <h1 class="lp-h1">Bảng điểm lớp học phần</h1>
        <p class="lp-sub">Ma trận sinh viên x cột điểm. Điểm LMS sau khi đẩy sẽ nằm cùng bảng với điểm giảng viên nhập tay.</p>
      </div>
      <RouterLink :to="{ name: 'Lms.BaiTapLop' }" class="lms-btn ghost">← Về LMS</RouterLink>
    </div>

    <div class="panel">
      <label class="fl">Chọn lớp học phần
        <select v-model="lopHocPhanId" class="fi" @change="loadData">
          <option value="">-- Chọn lớp --</option>
          <option v-for="lop in lopHocPhans" :key="lop.id" :value="lop.id">{{ lop.maLop }} - {{ lop.tenLopHocPhan }}</option>
        </select>
      </label>
      <div class="hint">Cột có loại <b>bai_tap</b> hoặc tên chứa LMS/online là điểm online; các cột còn lại là điểm GV nhập trực tiếp.</div>
    </div>

    <div v-if="dangTai" class="loading-state">Đang tải bảng điểm...</div>
    <div v-else-if="!lopHocPhanId" class="empty-state">Chọn lớp học phần để xem bảng điểm.</div>
    <div v-else class="panel table-wrap">
      <table class="lms-table">
        <thead>
          <tr>
            <th style="min-width:120px">Mã SV</th>
            <th style="min-width:220px">Sinh viên</th>
            <th v-for="cot in cauHinhs" :key="cot.id">
              <div>{{ cot.tenCotDiem || cot.tenCauHinh || ('Cột #' + cot.id) }}</div>
              <small>{{ cot.loaiDiem }} · {{ cot.tyLe }}%</small>
            </th>
            <th>TK</th>
            <th>Kết quả</th>
            <th>Trạng thái</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in rows" :key="row.sinhVienId">
            <td>{{ row.maSinhVien }}</td>
            <td><b>{{ row.tenSinhVien }}</b></td>
            <td v-for="cot in cauHinhs" :key="cot.id">
              <span :class="['score-pill', isOnlineCot(cot) ? 'online' : 'manual']">
                {{ fmt(row.diemTheoCot[cot.id]?.diem) }}
              </span>
            </td>
            <td><b>{{ fmt(row.ketQua?.diemTongKet) }}</b></td>
            <td>{{ row.ketQua?.ketQua || '-' }}</td>
            <td>{{ row.ketQua?.trangThai || '-' }}</td>
          </tr>
          <tr v-if="!rows.length"><td :colspan="cauHinhs.length + 5" class="empty-cell">Chưa có điểm chi tiết trong lớp này.</td></tr>
        </tbody>
      </table>
    </div>
  </section>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { lmsDanhMucService, bangDiemLopService } from '../services/lmsService'
import { formatDiem } from '../services/lmsEnum'

const dangTai = ref(false)
const lopHocPhanId = ref('')
const lopHocPhans = ref([])
const cauHinhs = ref([])
const rows = ref([])

function fmt(v) { return v === null || v === undefined || v === '' ? '-' : formatDiem(v) }
function isOnlineCot(cot) {
  const text = `${cot.loaiDiem || ''} ${cot.tenCotDiem || cot.tenCauHinh || ''}`.toLowerCase()
  return text.includes('bai_tap') || text.includes('online') || text.includes('lms')
}
async function loadData() {
  if (!lopHocPhanId.value) { cauHinhs.value = []; rows.value = []; return }
  dangTai.value = true
  try {
    const data = await bangDiemLopService.layBangDiem(lopHocPhanId.value)
    cauHinhs.value = data.cauHinhs
    rows.value = data.rows
  } finally { dangTai.value = false }
}

onMounted(async () => {
  lopHocPhans.value = await lmsDanhMucService.layLopHocPhan()
})
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }
.lms-page { display: flex; flex-direction: column; gap: 18px; }
.lp-head { display:flex; align-items:flex-start; gap:12px; }
.lp-head > div { flex:1; }
.lp-h1 { margin:0 0 4px; font-size:22px; font-weight:800; color:#1e293b; }
.lp-sub { margin:0; color:#64748b; font-size:13px; }
.panel { background:#fff; border:1px solid #e2e8f0; border-radius:12px; padding:16px; }
.fl { display:flex; flex-direction:column; gap:6px; font-size:12px; font-weight:700; color:#475569; max-width:520px; }
.fi { border:1px solid #e2e8f0; border-radius:8px; padding:9px 10px; font-size:13px; }
.hint { margin-top:10px; color:#64748b; font-size:13px; }
.loading-state,.empty-state { text-align:center; padding:40px; color:#64748b; background:#fff; border:1px dashed #cbd5e1; border-radius:12px; }
.table-wrap { overflow:auto; }
.lms-table { width:100%; border-collapse:collapse; font-size:13px; }
.lms-table th { background:#f8fafc; padding:10px; text-align:left; color:#475569; border-bottom:2px solid #e2e8f0; white-space:nowrap; }
.lms-table th small { display:block; color:#94a3b8; font-weight:600; }
.lms-table td { padding:10px; border-bottom:1px solid #f1f5f9; white-space:nowrap; }
.empty-cell { text-align:center; color:#94a3b8; padding:28px !important; }
.score-pill { display:inline-flex; min-width:46px; justify-content:center; padding:4px 8px; border-radius:999px; font-weight:800; background:#f1f5f9; color:#334155; }
.score-pill.online { background:#dcfce7; color:#166534; }
.score-pill.manual { background:#e0f2fe; color:#075985; }
.lms-btn { border:1px solid #cbd5e1; border-radius:8px; padding:8px 12px; text-decoration:none; color:#334155; font-weight:700; background:#fff; }
</style>
