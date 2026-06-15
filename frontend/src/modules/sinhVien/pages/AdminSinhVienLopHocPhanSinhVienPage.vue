<template>
  <section class="sv-page">
    <!-- Breadcrumb / back -->
    <div class="sv-page-header">
      <button type="button" class="btn-back" @click="quayLai">← Quay lại</button>
      <div class="sv-page-title">
        <h1>Sinh viên lớp học phần</h1>
        <p v-if="tenLop" class="sv-page-sub">{{ tenLop }}</p>
      </div>
      <RouterLink
          v-if="lopHocPhanId"
          class="sv-btn-primary"
          :to="{ name: 'GiangDay.ChiTietLopHocPhan', params: { id: lopHocPhanId } }"
      >
        Giảng dạy →
      </RouterLink>
    </div>

    <!-- Loading -->
    <div v-if="dangTai" class="sv-card sv-empty-state">
      <p>Đang tải danh sách sinh viên...</p>
    </div>

    <!-- Error -->
    <div v-else-if="thongBaoLoi" class="sv-card sv-empty-state">
      <p class="sv-loi">{{ thongBaoLoi }}</p>
    </div>

    <!-- Bảng sinh viên -->
    <div v-else class="sv-card">
      <div class="sv-card-title sv-card-title-toolbar">
        <div>
          <h2>Danh sách sinh viên ({{ danhSachSV.length }} người)</h2>
          <p>Sinh viên đã được Auto xếp vào lớp học phần này.</p>
        </div>
        <input
            v-model.trim="tuKhoa"
            class="sv-search-input"
            placeholder="Lọc mã SV, họ tên, email..."
        />
      </div>

      <div class="sv-table-wrap">
        <table class="sv-table">
          <thead>
          <tr>
            <th>STT</th>
            <th>Mã SV</th>
            <th>Họ tên</th>
            <th>Email / Gmail</th>
            <th>SĐT</th>
            <th>Trạng thái</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="(sv, i) in danhSachHienThi" :key="sv.id">
            <td>{{ i + 1 }}</td>
            <td><span class="sv-code">{{ sv.maSinhVien }}</span></td>
            <td>{{ sv.hoTen }}</td>
            <td>{{ sv.email }}</td>
            <td>{{ sv.soDienThoai }}</td>
            <td>
              <span class="sv-status-pill" :class="`status-${sv.trangThai || 'none'}`">
                {{ sv.trangThai || '—' }}
              </span>
            </td>
          </tr>
          <tr v-if="!danhSachHienThi.length">
            <td colspan="6" class="empty">
              {{ tuKhoa ? 'Không tìm thấy sinh viên phù hợp.' : 'Lớp học phần này chưa có sinh viên.' }}
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </section>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { sinhVienService } from '../services/sinhVienService'

const route = useRoute()
const router = useRouter()

const lopHocPhanId = computed(() => route.params.lopHocPhanId)

const dangTai = ref(false)
const thongBaoLoi = ref('')
const danhSachSV = ref([])
const tenLop = ref('')
const tuKhoa = ref('')

const danhSachHienThi = computed(() => {
  const kw = tuKhoa.value.trim().toLowerCase()
  if (!kw) return danhSachSV.value
  return danhSachSV.value.filter(sv =>
      [sv.maSinhVien, sv.hoTen, sv.email, sv.soDienThoai]
          .some(v => String(v || '').toLowerCase().includes(kw))
  )
})

async function taiDuLieu() {
  if (!lopHocPhanId.value) return
  dangTai.value = true
  thongBaoLoi.value = ''
  try {
    danhSachSV.value = await sinhVienService.laySinhVienTrongLopHocPhan(lopHocPhanId.value)
  } catch (e) {
    thongBaoLoi.value = e?.response?.data?.message || e.message || 'Không tải được danh sách sinh viên.'
  } finally {
    dangTai.value = false
  }
}

function quayLai() {
  router.push({
    path: '/admin/sinh-vien',
    query: {
      nganhId: route.query.nganhId,
      chuongTrinhId: route.query.chuongTrinhId,
      versionId: route.query.versionId,
    }
  })
}

onMounted(taiDuLieu)
</script>

<style scoped>
.sv-page {
  padding: 16px 24px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.sv-page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.sv-page-title {
  flex: 1;
}

.sv-page-title h1 {
  font-size: 18px;
  font-weight: 700;
  margin: 0;
}

.sv-page-sub {
  font-size: 13px;
  color: #6b7280;
  margin: 2px 0 0;
}

.btn-back {
  background: #f3f4f6;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 7px 14px;
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  color: #374151;
  white-space: nowrap;
}

.btn-back:hover { background: #e5e7eb; }

.sv-btn-primary {
  background: #1d4ed8;
  color: #fff;
  border-radius: 8px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 600;
  text-decoration: none;
  white-space: nowrap;
}

.sv-btn-primary:hover { background: #1e40af; }

.sv-card {
  background: #fff;
  border-radius: 12px;
  border: 1px solid #e5e7eb;
  padding: 16px 20px;
}

.sv-card-title { margin-bottom: 12px; }
.sv-card-title h2 { font-size: 15px; font-weight: 700; margin: 0 0 2px; }
.sv-card-title p { font-size: 13px; color: #6b7280; margin: 0; }

.sv-card-title-toolbar {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
  flex-wrap: wrap;
}

.sv-search-input {
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  padding: 7px 12px;
  font-size: 13px;
  outline: none;
  min-width: 220px;
}

.sv-search-input:focus { border-color: #1d4ed8; }

.sv-table-wrap { overflow-x: auto; }

.sv-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.sv-table th, .sv-table td {
  padding: 8px 10px;
  text-align: left;
  border-bottom: 1px solid #f3f4f6;
}

.sv-table th {
  background: #f9fafb;
  font-weight: 600;
  color: #374151;
  font-size: 12px;
  text-transform: uppercase;
  letter-spacing: 0.02em;
}

.sv-table tr:hover td { background: #f9fafb; }
.sv-table .empty { text-align: center; color: #9ca3af; padding: 24px; }

.sv-code {
  font-family: monospace;
  font-size: 12px;
  background: #f3f4f6;
  padding: 2px 6px;
  border-radius: 4px;
}

.sv-status-pill {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 999px;
  font-size: 11px;
  font-weight: 600;
}

.sv-loi { color: #dc2626; }

.sv-empty-state {
  text-align: center;
  padding: 40px;
  color: #6b7280;
}
</style>
