<template>
  <section class="lms-page">
    <div class="lp-head">
      <div>
        <h1 class="lp-h1">Tổng quan LMS</h1>
        <p class="lp-sub">Thống kê toàn bộ hệ thống quản lý học tập trực tuyến</p>
      </div>
      <button class="lms-btn ghost" @click="taiLai">↻ Tải lại</button>
    </div>

    <!-- Cards thống kê -->
    <div class="stat-grid" v-if="!dangTai">
      <div class="stat-card blue">
        <div class="stat-icon">❓</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.tongCauHoi }}</div>
          <div class="stat-label">Tổng câu hỏi</div>
        </div>
      </div>
      <div class="stat-card indigo">
        <div class="stat-icon">📄</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.baiTapOnline }}</div>
          <div class="stat-label">Bài tập online</div>
        </div>
      </div>
      <div class="stat-card green">
        <div class="stat-icon">🟢</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.baiTapDangMo }}</div>
          <div class="stat-label">Bài tập đang mở</div>
        </div>
      </div>
      <div class="stat-card purple">
        <div class="stat-icon">🏫</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.lopDaGiaoBai }}</div>
          <div class="stat-label">Lớp đã giao bài</div>
        </div>
      </div>
      <div class="stat-card teal">
        <div class="stat-icon">📤</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.sinhVienDaNop }}</div>
          <div class="stat-label">Sinh viên đã nộp</div>
        </div>
      </div>
      <div class="stat-card orange">
        <div class="stat-icon">⏳</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.baiCanCham }}</div>
          <div class="stat-label">Bài cần chấm</div>
        </div>
      </div>
      <div class="stat-card rose">
        <div class="stat-icon">✅</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.ketQuaDaChot }}</div>
          <div class="stat-label">Kết quả đã chốt</div>
        </div>
      </div>
      <div class="stat-card amber">
        <div class="stat-icon">📊</div>
        <div class="stat-body">
          <div class="stat-num">{{ thongKe.diemDaDaySangBangDiem }}</div>
          <div class="stat-label">Đã đẩy sang bảng điểm</div>
        </div>
      </div>
    </div>
    <div v-else class="stat-grid-skeleton">
      <div v-for="i in 8" :key="i" class="stat-skel"></div>
    </div>

    <div class="dashboard-grid">
      <!-- Bài tập lớp đang hoạt động -->
      <div class="lms-card full">
        <div class="card-head">
          <h2 class="card-title">📋 Bài tập lớp đang hoạt động</h2>
          <RouterLink :to="{ name: 'Lms.BaiTapLop' }" class="lms-btn sm">Xem tất cả</RouterLink>
        </div>
        <div class="table-wrap">
          <table class="lms-table">
            <thead>
              <tr>
                <th>Tên bài</th>
                <th>Môn học</th>
                <th>Lớp học phần</th>
                <th>Thời gian mở</th>
                <th>Thời gian đóng</th>
                <th>Đã nộp</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-if="dangTaiBaiTap">
                <td colspan="8" class="empty-cell">Đang tải...</td>
              </tr>
              <tr v-else-if="baiTapDangMo.length === 0">
                <td colspan="8" class="empty-cell">Không có bài tập đang hoạt động</td>
              </tr>
              <tr v-for="b in baiTapDangMo" :key="b.id" v-else>
                <td><b>{{ b.tenBaiTap }}</b></td>
                <td>{{ b.tenMonHoc }}</td>
                <td>{{ b.maLop }}</td>
                <td class="nowrap">{{ fmt(b.thoiGianMo) }}</td>
                <td class="nowrap">{{ fmt(b.thoiGianDong) }}</td>
                <td>
                  <span class="progress-text">{{ b.soSinhVienNop }}/{{ b.tongSinhVien }}</span>
                  <div class="progress-bar">
                    <div class="progress-fill" :style="{ width: pct(b.soSinhVienNop, b.tongSinhVien) }"></div>
                  </div>
                </td>
                <td>
                  <span :class="['lms-badge', mauTrangThai(b.trangThai)]">{{ tenTrangThai(b.trangThai) }}</span>
                </td>
                <td>
                  <RouterLink :to="{ name: 'Lms.DanhSachNop', params: { id: b.id } }" class="lms-btn sm primary">Danh sách nộp</RouterLink>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- Việc cần xử lý -->
      <div class="lms-card">
        <div class="card-head">
          <h2 class="card-title">⚡ Việc cần xử lý</h2>
        </div>
        <div class="todo-list">
          <div class="todo-item orange" @click="$router.push({ name: 'Lms.BaiTapLop' })">
            <div class="todo-icon">✍️</div>
            <div class="todo-body">
              <div class="todo-title">Bài cần chấm tự luận</div>
              <div class="todo-sub">{{ thongKe.baiCanCham }} bài chưa chấm</div>
            </div>
            <div class="todo-arrow">→</div>
          </div>
          <div class="todo-item blue" @click="$router.push({ name: 'Lms.BaiTapLop' })">
            <div class="todo-icon">⏰</div>
            <div class="todo-body">
              <div class="todo-title">Bài sắp hết hạn</div>
              <div class="todo-sub">{{ baiSapHetHan }} bài đóng trong 24h</div>
            </div>
            <div class="todo-arrow">→</div>
          </div>
          <div class="todo-item green" @click="$router.push({ name: 'Lms.BaiTapLop' })">
            <div class="todo-icon">📤</div>
            <div class="todo-body">
              <div class="todo-title">Đã chốt chưa đẩy điểm</div>
              <div class="todo-sub">{{ thongKe.ketQuaDaChot - thongKe.diemDaDaySangBangDiem }} bài chờ đẩy</div>
            </div>
            <div class="todo-arrow">→</div>
          </div>
        </div>
      </div>

      <!-- Quick links -->
      <div class="lms-card">
        <div class="card-head">
          <h2 class="card-title">🔗 Thao tác nhanh</h2>
        </div>
        <div class="quick-links">
          <RouterLink :to="{ name: 'Lms.NganHangCauHoiThem' }" class="quick-link indigo">
            <span class="ql-icon">+</span>Thêm câu hỏi
          </RouterLink>
          <RouterLink :to="{ name: 'Lms.BaiTapOnlineThem' }" class="quick-link blue">
            <span class="ql-icon">+</span>Tạo bài tập
          </RouterLink>
          <RouterLink :to="{ name: 'Lms.GiaoBai' }" class="quick-link green">
            <span class="ql-icon">📨</span>Giao bài cho lớp
          </RouterLink>
          <RouterLink :to="{ name: 'Lms.LichSu' }" class="quick-link gray">
            <span class="ql-icon">🕐</span>Xem lịch sử
          </RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { dashboardLmsService } from '../services/lmsService'
import { TRANG_THAI_BAI_TAP_LOP, mauTrangThaiBaiTapLop, formatDateTime } from '../services/lmsEnum'

const dangTai = ref(true)
const dangTaiBaiTap = ref(true)
const thongKe = ref({ tongCauHoi: 0, baiTapOnline: 0, baiTapDangMo: 0, lopDaGiaoBai: 0, sinhVienDaNop: 0, baiCanCham: 0, ketQuaDaChot: 0, diemDaDaySangBangDiem: 0 })
const baiTapDangMo = ref([])

const baiSapHetHan = computed(() => {
  const now = Date.now()
  return baiTapDangMo.value.filter(b => {
    const dong = new Date(b.thoiGianDong).getTime()
    return dong > now && dong - now < 86400000
  }).length
})

function fmt(dt) { return formatDateTime(dt) }
function pct(a, b) { return b ? Math.round((a / b) * 100) + '%' : '0%' }
function mauTrangThai(tt) { return mauTrangThaiBaiTapLop(tt) }
function tenTrangThai(tt) { return TRANG_THAI_BAI_TAP_LOP[tt] || tt }

async function taiLai() {
  dangTai.value = true
  dangTaiBaiTap.value = true
  try {
    thongKe.value = await dashboardLmsService.layThongKe()
    const res = await dashboardLmsService.layBaiTapDangHoatDong()
    baiTapDangMo.value = res.content || []
  } finally {
    dangTai.value = false
    dangTaiBaiTap.value = false
  }
}

onMounted(taiLai)
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
*, *::before, *::after { box-sizing: border-box; font-family: 'Roboto', sans-serif; }

.lms-page { display: flex; flex-direction: column; gap: 20px; }
.lp-head { display: flex; justify-content: space-between; align-items: flex-start; }
.lp-h1 { margin: 0 0 4px; font-size: 22px; font-weight: 800; color: #1e293b; }
.lp-sub { margin: 0; color: #64748b; font-size: 13px; }

.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 14px; }
.stat-card { display: flex; align-items: center; gap: 14px; padding: 16px 18px; border-radius: 12px; border: 1px solid transparent; cursor: default; }
.stat-icon { font-size: 26px; }
.stat-num { font-size: 28px; font-weight: 800; line-height: 1; }
.stat-label { font-size: 12px; font-weight: 600; opacity: 0.75; margin-top: 3px; }
.stat-card.blue { background: #eff6ff; border-color: #bfdbfe; color: #1d4ed8; }
.stat-card.indigo { background: #eef2ff; border-color: #c7d2fe; color: #4338ca; }
.stat-card.green { background: #f0fdf4; border-color: #bbf7d0; color: #15803d; }
.stat-card.purple { background: #faf5ff; border-color: #e9d5ff; color: #7c3aed; }
.stat-card.teal { background: #f0fdfa; border-color: #99f6e4; color: #0f766e; }
.stat-card.orange { background: #fff7ed; border-color: #fed7aa; color: #c2410c; }
.stat-card.rose { background: #fff1f2; border-color: #fecdd3; color: #be123c; }
.stat-card.amber { background: #fffbeb; border-color: #fde68a; color: #b45309; }

.stat-grid-skeleton { display: grid; grid-template-columns: repeat(4, 1fr); gap: 14px; }
.stat-skel { height: 80px; border-radius: 12px; background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%); background-size: 200%; animation: shimmer 1.4s infinite; }
@keyframes shimmer { 0% { background-position: 200% center; } 100% { background-position: -200% center; } }

.dashboard-grid { display: grid; grid-template-columns: 1fr 280px; gap: 16px; align-items: start; }
.dashboard-grid .full { grid-column: 1 / -1; }

.lms-card { background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; padding: 18px; }
.card-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; }
.card-title { margin: 0; font-size: 15px; font-weight: 700; color: #1e293b; }

.table-wrap { overflow-x: auto; }
.lms-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.lms-table th { background: #f8fafc; padding: 8px 12px; text-align: left; font-weight: 700; color: #475569; font-size: 12px; border-bottom: 1px solid #e2e8f0; white-space: nowrap; }
.lms-table td { padding: 10px 12px; border-bottom: 1px solid #f1f5f9; color: #334155; vertical-align: middle; }
.lms-table tr:last-child td { border-bottom: none; }
.lms-table tr:hover td { background: #f8fafc; }
.empty-cell { text-align: center; color: #94a3b8; padding: 24px !important; }
.nowrap { white-space: nowrap; }

.progress-text { font-size: 12px; font-weight: 600; color: #475569; display: block; }
.progress-bar { height: 5px; background: #e2e8f0; border-radius: 3px; margin-top: 4px; width: 80px; }
.progress-fill { height: 100%; background: #1d4ed8; border-radius: 3px; transition: width 0.3s; }

.lms-badge { display: inline-flex; align-items: center; padding: 3px 10px; border-radius: 20px; font-size: 11px; font-weight: 700; }
.badge-gray { background: #f1f5f9; color: #64748b; }
.badge-green { background: #dcfce7; color: #15803d; }
.badge-blue { background: #dbeafe; color: #1d4ed8; }
.badge-indigo { background: #e0e7ff; color: #4338ca; }
.badge-purple { background: #f3e8ff; color: #7c3aed; }
.badge-orange { background: #fff7ed; color: #c2410c; }
.badge-red { background: #fee2e2; color: #b91c1c; }

.todo-list { display: flex; flex-direction: column; gap: 10px; }
.todo-item { display: flex; align-items: center; gap: 12px; padding: 12px 14px; border-radius: 10px; cursor: pointer; transition: opacity 0.15s; }
.todo-item:hover { opacity: 0.85; }
.todo-item.orange { background: #fff7ed; border: 1px solid #fed7aa; }
.todo-item.blue { background: #eff6ff; border: 1px solid #bfdbfe; }
.todo-item.green { background: #f0fdf4; border: 1px solid #bbf7d0; }
.todo-icon { font-size: 20px; }
.todo-body { flex: 1; }
.todo-title { font-size: 13px; font-weight: 700; color: #1e293b; }
.todo-sub { font-size: 12px; color: #64748b; margin-top: 2px; }
.todo-arrow { font-size: 16px; color: #94a3b8; }

.quick-links { display: flex; flex-direction: column; gap: 8px; }
.quick-link { display: flex; align-items: center; gap: 10px; padding: 10px 14px; border-radius: 10px; text-decoration: none; font-size: 13px; font-weight: 700; transition: opacity 0.15s; }
.quick-link:hover { opacity: 0.85; }
.quick-link.indigo { background: #eef2ff; color: #4338ca; border: 1px solid #c7d2fe; }
.quick-link.blue { background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; }
.quick-link.green { background: #f0fdf4; color: #15803d; border: 1px solid #bbf7d0; }
.quick-link.gray { background: #f8fafc; color: #475569; border: 1px solid #e2e8f0; }
.ql-icon { font-size: 16px; font-weight: 800; }

.lms-btn { display: inline-flex; align-items: center; gap: 6px; padding: 8px 16px; border: 1px solid #e2e8f0; border-radius: 8px; background: #fff; color: #475569; font-size: 13px; font-weight: 600; cursor: pointer; text-decoration: none; transition: all 0.15s; }
.lms-btn:hover { border-color: #1d4ed8; color: #1d4ed8; background: #eff6ff; }
.lms-btn.sm { padding: 5px 12px; font-size: 12px; }
.lms-btn.primary { background: #1d4ed8; color: #fff; border-color: #1d4ed8; }
.lms-btn.primary:hover { background: #1e40af; }
.lms-btn.ghost { background: transparent; border-color: transparent; color: #64748b; }
.lms-btn.ghost:hover { background: #f1f5f9; border-color: #e2e8f0; color: #1e293b; }

@media (max-width: 1100px) { .stat-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 768px) { .stat-grid { grid-template-columns: 1fr 1fr; } .dashboard-grid { grid-template-columns: 1fr; } }
</style>
