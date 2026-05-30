<template>
  <section class="dsv-page">

    <!-- BỘ LỌC -->
    <div class="dsv-filter-card">
      <div class="dsv-filter-row">
        <div class="dsv-filter-title">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2">
            <polygon points="22 3 2 3 10 12.46 10 19 14 21 14 12.46 22 3"/>
          </svg>
          Bộ lọc
        </div>
        <span class="dsv-total-badge">
          Tổng: <strong>{{ svHienThi.length }}</strong> / {{ danhSachMerged.length }}
        </span>
      </div>

      <div class="dsv-filter-grid">
        <label class="dsv-label">
          Ngành
          <select v-model="locNganhId" @change="khiDoiNganh" class="dsv-select">
            <option value="">— Tất cả ngành —</option>
            <option v-for="ng in danhSachNganh" :key="ng.id" :value="ng.id">
              {{ ng.maNganh }} – {{ ng.tenNganh }}
            </option>
          </select>
        </label>

        <label class="dsv-label">
          Chương trình
          <select v-model="locChuongTrinhId" @change="khiDoiChuongTrinh" class="dsv-select">
            <option value="">— Tất cả chương trình —</option>
            <option v-for="ct in chuongTrinhTheoNganh" :key="ct.id" :value="ct.id">
              {{ ct.maChuongTrinh }} – {{ ct.tenChuongTrinh }}
            </option>
          </select>
        </label>

        <label class="dsv-label">
          Version
          <select v-model="locVersionId" class="dsv-select">
            <option value="">— Tất cả version —</option>
            <option v-for="v in versionTheoCT" :key="v.id" :value="v.id">
              {{ v.maVersion }} – {{ v.tenVersion }}
            </option>
          </select>
        </label>

        <label class="dsv-label">
          Mã sinh viên
          <input v-model.trim="locMaSV" class="dsv-input" placeholder="Tìm mã SV..." />
        </label>

        <label class="dsv-label">
          Họ tên
          <input v-model.trim="locTen" class="dsv-input" placeholder="Tìm họ tên..." />
        </label>

        <label class="dsv-label">
          Trạng thái
          <select v-model="locTrangThai" class="dsv-select">
            <option value="">— Tất cả —</option>
            <option value="dang_hoc">Đang học</option>
            <option value="bao_luu">Bảo lưu</option>
            <option value="thoi_hoc">Thôi học</option>
            <option value="tot_nghiep">Tốt nghiệp</option>
          </select>
        </label>
      </div>

      <div class="dsv-filter-footer">
        <button class="dsv-btn-ghost" @click="datLaiLoc">
          <svg width="13" height="13" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="1 4 1 10 7 10"/><path d="M3.51 15a9 9 0 1 0 .49-4.5"/></svg>
          Đặt lại
        </button>
        <span v-if="dangTai" class="dsv-loading-text">
          <span class="dsv-dot-spin"></span> Đang tải dữ liệu...
        </span>
      </div>
    </div>

    <!-- LỖI -->
    <div v-if="loiTai" class="dsv-error">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><line x1="12" y1="8" x2="12" y2="12"/><line x1="12" y1="16" x2="12.01" y2="16"/></svg>
      {{ loiTai }}
    </div>

    <!-- BẢNG -->
    <div class="dsv-table-card">
      <div class="dsv-table-header">
        <h2 class="dsv-table-title">Danh sách sinh viên toàn trường</h2>
        <div class="dsv-header-right">
          <span v-if="dangTai" class="dsv-skeleton-badge"></span>
          <span v-else class="dsv-badge">{{ svHienThi.length }} sinh viên</span>
        </div>
      </div>

      <div class="dsv-table-wrap">
        <!-- SKELETON khi đang tải -->
        <table v-if="dangTai" class="dsv-table">
          <thead>
          <tr>
            <th class="col-stt">STT</th>
            <th class="col-masv">Mã SV</th>
            <th class="col-ten">Họ và tên</th>
            <th class="col-email">Gmail</th>
            <th class="col-nganh">Ngành</th>
            <th class="col-ct">Chương trình</th>
            <th class="col-ver">Version</th>
            <th class="col-ngay">Nhập học</th>
            <th class="col-tt">Trạng thái</th>
            <th class="col-action"></th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="i in 8" :key="i" class="dsv-skeleton-row">
            <td><span class="dsv-skel dsv-skel-sm"></span></td>
            <td><span class="dsv-skel dsv-skel-md"></span></td>
            <td>
              <div class="dsv-name-cell">
                <span class="dsv-skel dsv-skel-circle"></span>
                <span class="dsv-skel dsv-skel-lg"></span>
              </div>
            </td>
            <td><span class="dsv-skel dsv-skel-xl"></span></td>
            <td><span class="dsv-skel dsv-skel-md"></span></td>
            <td><span class="dsv-skel dsv-skel-lg"></span></td>
            <td><span class="dsv-skel dsv-skel-sm"></span></td>
            <td><span class="dsv-skel dsv-skel-md"></span></td>
            <td><span class="dsv-skel dsv-skel-sm"></span></td>
            <td><span class="dsv-skel dsv-skel-btn"></span></td>
          </tr>
          </tbody>
        </table>

        <!-- DỮ LIỆU THẬT -->
        <table v-else class="dsv-table">
          <thead>
          <tr>
            <th class="col-stt">STT</th>
            <th class="col-masv">Mã SV</th>
            <th class="col-ten">Họ và tên</th>
            <th class="col-email">Gmail</th>
            <th class="col-nganh">Ngành</th>
            <th class="col-ct">Chương trình</th>
            <th class="col-ver">Version</th>
            <th class="col-ngay">Nhập học</th>
            <th class="col-tt">Trạng thái</th>
            <th class="col-action"></th>
          </tr>
          </thead>
          <tbody>
          <tr
              v-for="(sv, idx) in svHienThi"
              :key="sv.id"
              class="dsv-row"
              :class="{ 'dsv-row-odd': idx % 2 !== 0 }"
          >
            <td class="col-stt td-center td-muted">{{ idx + 1 }}</td>

            <td class="col-masv">
              <span class="dsv-masv-chip">{{ sv.maSinhVien || '—' }}</span>
            </td>

            <td class="col-ten">
              <div class="dsv-name-cell">
                  <span class="dsv-avatar" :style="{ background: avatarColor(sv.hoTen) }">
                    {{ layChuCai(sv.hoTen) }}
                  </span>
                <div>
                  <div class="dsv-name-text">{{ sv.hoTen || '—' }}</div>
                  <div class="dsv-name-sub">ID #{{ sv.id }}</div>
                </div>
              </div>
            </td>

            <td class="col-email td-muted">{{ sv.email || '—' }}</td>

            <td class="col-nganh">
                <span class="dsv-tag dsv-tag-nganh" :title="layTenNganh(sv)">
                  {{ layTenNganh(sv) }}
                </span>
            </td>

            <td class="col-ct">
                <span class="dsv-tag dsv-tag-ct" :title="layTenChuongTrinh(sv)">
                  {{ layMaChuongTrinh(sv) }}
                </span>
            </td>

            <td class="col-ver">
              <span class="dsv-tag dsv-tag-ver">{{ layMaVersion(sv) }}</span>
            </td>

            <td class="col-ngay td-muted">{{ dinhDangNgay(sv.ngayNhapHoc) }}</td>

            <td class="col-tt">
                <span class="dsv-status" :class="trangThaiClass(sv.trangThai)">
                  {{ trangThaiLabel(sv.trangThai) }}
                </span>
            </td>

            <td class="col-action">
              <RouterLink
                  :to="{ name: 'AdminSinhVienChiTiet', params: { id: sv.id } }"
                  class="dsv-btn-xem"
              >
                Xem chi tiết
                <svg width="11" height="11" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.5">
                  <polyline points="9 18 15 12 9 6"/>
                </svg>
              </RouterLink>
            </td>
          </tr>

          <tr v-if="!svHienThi.length && !dangTai">
            <td colspan="10" class="dsv-empty">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#cbd5e1" stroke-width="1.2">
                <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
              </svg>
              <p>Không tìm thấy sinh viên nào phù hợp.</p>
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
import { sinhVienService } from '../services/sinhVienService'

// ─── STATE ────────────────────────────────────────────────────────────────────
const danhSachNganh       = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion     = ref([])
const danhSachSV          = ref([])   // SinhVienResponse[]
const danhSachSVCT        = ref([])   // SinhVienChuongTrinhResponse[]

const locNganhId       = ref('')
const locChuongTrinhId = ref('')
const locVersionId     = ref('')
const locMaSV          = ref('')
const locTen           = ref('')
const locTrangThai     = ref('')

const dangTai = ref(false)
const loiTai  = ref('')

// ─── LOOKUP MAPS ──────────────────────────────────────────────────────────────
// version → chuongTrinh → nganh
const versionToCT  = computed(() => {
  const m = {}
  danhSachVersion.value.forEach(v => { m[v.id] = v.chuongTrinhId })
  return m
})
const ctToNganh = computed(() => {
  const m = {}
  danhSachChuongTrinh.value.forEach(ct => { m[ct.id] = ct.nganhId })
  return m
})
// sinhVienId → SinhVienChuongTrinh (lấy bản mới nhất)
const svCtMap = computed(() => {
  const m = {}
  danhSachSVCT.value.forEach(svct => {
    // nếu một SV có nhiều chương trình, ưu tiên da_dang_ky hoặc bản mới nhất
    if (!m[svct.sinhVienId] || svct.id > m[svct.sinhVienId].id) {
      m[svct.sinhVienId] = svct
    }
  })
  return m
})

// ─── MERGE SV + SVCT ──────────────────────────────────────────────────────────
const danhSachMerged = computed(() => {
  return danhSachSV.value.map(sv => {
    const svct = svCtMap.value[sv.id] || null
    const versionId      = svct?.chuongTrinhVersionId || null
    const chuongTrinhId  = versionId ? versionToCT.value[versionId] : null
    const nganhId        = chuongTrinhId ? ctToNganh.value[chuongTrinhId] : null
    return {
      ...sv,
      chuongTrinhVersionId: versionId,
      chuongTrinhId,
      nganhId,
      ngayNhapHoc: svct?.ngayNhapHoc || null,
    }
  })
})

// ─── FILTER DROPDOWNS ─────────────────────────────────────────────────────────
const chuongTrinhTheoNganh = computed(() => {
  if (!locNganhId.value) return danhSachChuongTrinh.value
  return danhSachChuongTrinh.value.filter(
      ct => String(ct.nganhId) === String(locNganhId.value)
  )
})
const versionTheoCT = computed(() => {
  if (!locChuongTrinhId.value) return danhSachVersion.value
  return danhSachVersion.value.filter(
      v => String(v.chuongTrinhId) === String(locChuongTrinhId.value)
  )
})

// ─── FILTERED LIST ────────────────────────────────────────────────────────────
const svHienThi = computed(() => {
  let ds = danhSachMerged.value

  if (locNganhId.value)
    ds = ds.filter(sv => String(sv.nganhId) === String(locNganhId.value))

  if (locChuongTrinhId.value)
    ds = ds.filter(sv => String(sv.chuongTrinhId) === String(locChuongTrinhId.value))

  if (locVersionId.value)
    ds = ds.filter(sv => String(sv.chuongTrinhVersionId) === String(locVersionId.value))

  if (locMaSV.value) {
    const kw = locMaSV.value.toLowerCase()
    ds = ds.filter(sv => String(sv.maSinhVien || '').toLowerCase().includes(kw))
  }

  if (locTen.value) {
    const kw = locTen.value.toLowerCase()
    ds = ds.filter(sv => String(sv.hoTen || '').toLowerCase().includes(kw))
  }

  if (locTrangThai.value)
    ds = ds.filter(sv => sv.trangThai === locTrangThai.value)

  return ds
})

// ─── HELPERS ──────────────────────────────────────────────────────────────────
function layTenNganh(sv) {
  if (!sv.nganhId) return '—'
  const ng = danhSachNganh.value.find(n => String(n.id) === String(sv.nganhId))
  return ng ? ng.tenNganh : '—'
}
function layTenChuongTrinh(sv) {
  if (!sv.chuongTrinhId) return '—'
  const ct = danhSachChuongTrinh.value.find(c => String(c.id) === String(sv.chuongTrinhId))
  return ct ? ct.tenChuongTrinh : '—'
}
function layMaChuongTrinh(sv) {
  if (!sv.chuongTrinhId) return '—'
  const ct = danhSachChuongTrinh.value.find(c => String(c.id) === String(sv.chuongTrinhId))
  return ct ? ct.maChuongTrinh : '—'
}
function layMaVersion(sv) {
  if (!sv.chuongTrinhVersionId) return '—'
  const ver = danhSachVersion.value.find(v => String(v.id) === String(sv.chuongTrinhVersionId))
  return ver ? (ver.maVersion || ver.tenVersion || '—') : '—'
}
function dinhDangNgay(val) {
  if (!val) return '—'
  try {
    const d = new Date(val)
    if (isNaN(d)) return val
    return d.toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' })
  } catch { return val }
}
function layChuCai(hoTen) {
  if (!hoTen) return '?'
  const parts = hoTen.trim().split(' ')
  return parts[parts.length - 1].charAt(0).toUpperCase()
}
const AVATAR_COLORS = ['#1d4ed8','#0891b2','#7c3aed','#be185d','#b45309','#047857','#9333ea','#0369a1']
function avatarColor(name) {
  if (!name) return '#1d4ed8'
  let hash = 0
  for (let c of name) hash = c.charCodeAt(0) + ((hash << 5) - hash)
  return AVATAR_COLORS[Math.abs(hash) % AVATAR_COLORS.length]
}
function trangThaiLabel(tt) {
  const map = { dang_hoc: 'Đang học', bao_luu: 'Bảo lưu', thoi_hoc: 'Thôi học', tot_nghiep: 'Tốt nghiệp' }
  return map[tt] || tt || '—'
}
function trangThaiClass(tt) {
  return {
    'tt-dang-hoc':  tt === 'dang_hoc',
    'tt-bao-luu':   tt === 'bao_luu',
    'tt-thoi-hoc':  tt === 'thoi_hoc',
    'tt-tot-nghiep':tt === 'tot_nghiep',
  }
}

// ─── ACTIONS ──────────────────────────────────────────────────────────────────
function khiDoiNganh() { locChuongTrinhId.value = ''; locVersionId.value = '' }
function khiDoiChuongTrinh() { locVersionId.value = '' }
function datLaiLoc() {
  locNganhId.value = ''; locChuongTrinhId.value = ''; locVersionId.value = ''
  locMaSV.value = ''; locTen.value = ''; locTrangThai.value = ''
}

// ─── MOUNTED ──────────────────────────────────────────────────────────────────
onMounted(async () => {
  dangTai.value = true
  loiTai.value  = ''
  try {
    const [nganh, ct, ver, svList, svctList] = await Promise.all([
      sinhVienService.layNganh(),
      sinhVienService.layChuongTrinh(),
      sinhVienService.layVersion(),
      sinhVienService.layTatCaSinhVien(),
      sinhVienService.laySinhVienChuongTrinh({ page: 0, size: 1000 }),
    ])
    danhSachNganh.value       = nganh
    danhSachChuongTrinh.value = ct
    danhSachVersion.value     = ver
    danhSachSV.value          = svList
    danhSachSVCT.value        = svctList
  } catch (e) {
    loiTai.value = 'Lỗi tải dữ liệu: ' + (e?.message || 'Không xác định')
  } finally {
    dangTai.value = false
  }
})
</script>

<style scoped>
.dsv-page { display: flex; flex-direction: column; gap: 14px; }

/* ── FILTER CARD ── */
.dsv-filter-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  padding: 16px 20px;
  box-shadow: 0 1px 6px rgba(15,23,42,.04);
}
.dsv-filter-row {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 12px;
}
.dsv-filter-title {
  display: flex; align-items: center; gap: 6px;
  font-size: 12px; font-weight: 700; color: #64748b;
  text-transform: uppercase; letter-spacing: .06em;
}
.dsv-total-badge { font-size: 12px; color: #64748b; }
.dsv-total-badge strong { color: #1d4ed8; font-size: 13px; }
.dsv-filter-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(190px, 1fr));
  gap: 10px; margin-bottom: 12px;
}
.dsv-label { display: flex; flex-direction: column; gap: 4px; font-size: 11.5px; font-weight: 600; color: #475569; }
.dsv-select, .dsv-input {
  height: 34px; padding: 0 10px;
  border: 1px solid #e2e8f0; border-radius: 8px;
  font-size: 13px; color: #1e293b; background: #f8fafc;
  width: 100%; box-sizing: border-box;
  transition: border-color .14s, box-shadow .14s;
}
.dsv-select:focus, .dsv-input:focus {
  outline: none; border-color: #3b82f6;
  box-shadow: 0 0 0 3px rgba(59,130,246,.1); background: #fff;
}
.dsv-filter-footer { display: flex; align-items: center; gap: 12px; }
.dsv-btn-ghost {
  display: inline-flex; align-items: center; gap: 5px;
  height: 32px; padding: 0 12px;
  background: transparent; border: 1px solid #e2e8f0; border-radius: 8px;
  font-size: 12px; font-weight: 500; color: #64748b; cursor: pointer;
  transition: background .14s;
}
.dsv-btn-ghost:hover { background: #f1f5f9; }
.dsv-loading-text { font-size: 12px; color: #94a3b8; display: flex; align-items: center; gap: 6px; }
.dsv-dot-spin {
  display: inline-block; width: 10px; height: 10px;
  border: 2px solid #e2e8f0; border-top-color: #3b82f6; border-radius: 50%;
  animation: spin .7s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

/* ── ERROR ── */
.dsv-error {
  display: flex; align-items: center; gap: 8px;
  background: #fef2f2; border: 1px solid #fecaca; border-radius: 10px;
  padding: 11px 16px; font-size: 13px; color: #dc2626;
}

/* ── TABLE CARD ── */
.dsv-table-card {
  background: #fff;
  border: 1px solid #e2e8f0; border-radius: 14px; overflow: hidden;
  box-shadow: 0 1px 6px rgba(15,23,42,.04);
}
.dsv-table-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 20px; border-bottom: 1px solid #f1f5f9;
}
.dsv-table-title { font-size: 14px; font-weight: 700; color: #0f172a; margin: 0; }
.dsv-header-right { display: flex; align-items: center; }
.dsv-badge {
  display: inline-flex; align-items: center;
  height: 22px; padding: 0 10px;
  background: #eff6ff; border: 1px solid #bfdbfe; border-radius: 20px;
  font-size: 11.5px; font-weight: 600; color: #1d4ed8;
}
.dsv-skeleton-badge {
  display: inline-block; width: 80px; height: 22px;
  background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%);
  background-size: 200% 100%; animation: shimmer 1.4s infinite;
  border-radius: 20px;
}
@keyframes shimmer { to { background-position: -200% 0; } }

/* ── TABLE ── */
.dsv-table-wrap { overflow-x: auto; }
.dsv-table { width: 100%; border-collapse: collapse; font-size: 13px; }
.dsv-table thead tr { background: #f8fafc; }
.dsv-table th {
  padding: 9px 14px; text-align: left;
  font-size: 11px; font-weight: 700; color: #94a3b8;
  text-transform: uppercase; letter-spacing: .05em;
  border-bottom: 1px solid #e2e8f0; white-space: nowrap;
}
.dsv-table td {
  padding: 11px 14px; color: #1e293b;
  border-bottom: 1px solid #f1f5f9; vertical-align: middle;
}
.dsv-row:last-child td { border-bottom: none; }
.dsv-row-odd td { background: #fafafa; }
.dsv-row:hover td { background: #f0f7ff !important; transition: background .12s; }

/* ── COL ── */
.col-stt    { width: 44px; }
.col-masv   { width: 110px; }
.col-ten    { min-width: 180px; }
.col-email  { min-width: 170px; }
.col-nganh  { min-width: 130px; }
.col-ct     { width: 110px; }
.col-ver    { width: 100px; }
.col-ngay   { width: 110px; }
.col-tt     { width: 100px; }
.col-action { width: 120px; text-align: right; padding-right: 16px; }
.td-center  { text-align: center; }
.td-muted   { color: #64748b; }

/* ── CELLS ── */
.dsv-masv-chip {
  display: inline-block; padding: 2px 8px;
  background: #f1f5f9; border: 1px solid #e2e8f0; border-radius: 6px;
  font-family: 'Courier New', monospace; font-size: 12px;
  font-weight: 600; color: #334155;
}
.dsv-name-cell { display: flex; align-items: center; gap: 9px; }
.dsv-avatar {
  flex-shrink: 0; width: 32px; height: 32px; border-radius: 50%;
  color: #fff; font-size: 12px; font-weight: 700;
  display: flex; align-items: center; justify-content: center;
}
.dsv-name-text { font-weight: 600; color: #0f172a; line-height: 1.3; }
.dsv-name-sub  { font-size: 11px; color: #94a3b8; }
.dsv-tag {
  display: inline-block; padding: 3px 8px; border-radius: 6px;
  font-size: 11.5px; font-weight: 500; max-width: 140px;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
}
.dsv-tag-nganh { background: #f0fdf4; color: #15803d; border: 1px solid #bbf7d0; }
.dsv-tag-ct    { background: #fef9c3; color: #92400e; border: 1px solid #fde68a; }
.dsv-tag-ver   { background: #faf5ff; color: #7e22ce; border: 1px solid #e9d5ff; }

.dsv-status {
  display: inline-flex; align-items: center; gap: 5px;
  padding: 3px 8px; border-radius: 20px;
  font-size: 11.5px; font-weight: 600;
}
.dsv-status::before {
  content: ''; display: inline-block;
  width: 6px; height: 6px; border-radius: 50%;
}
.tt-dang-hoc  { background: #dcfce7; color: #15803d; }
.tt-dang-hoc::before  { background: #16a34a; }
.tt-bao-luu   { background: #fef9c3; color: #92400e; }
.tt-bao-luu::before   { background: #d97706; }
.tt-thoi-hoc  { background: #fee2e2; color: #b91c1c; }
.tt-thoi-hoc::before  { background: #dc2626; }
.tt-tot-nghiep{ background: #eff6ff; color: #1d4ed8; }
.tt-tot-nghiep::before{ background: #3b82f6; }

.dsv-btn-xem {
  display: inline-flex; align-items: center; gap: 4px;
  height: 28px; padding: 0 10px;
  background: #eff6ff; border: 1px solid #bfdbfe; border-radius: 7px;
  font-size: 11.5px; font-weight: 600; color: #1d4ed8;
  text-decoration: none; white-space: nowrap;
  transition: background .14s, color .14s;
}
.dsv-btn-xem:hover { background: #dbeafe; color: #1e40af; }

/* ── SKELETON ROWS ── */
.dsv-skel {
  display: inline-block; height: 12px; border-radius: 6px;
  background: linear-gradient(90deg, #f1f5f9 25%, #e2e8f0 50%, #f1f5f9 75%);
  background-size: 200% 100%; animation: shimmer 1.4s infinite;
}
.dsv-skel-sm  { width: 28px; }
.dsv-skel-md  { width: 70px; }
.dsv-skel-lg  { width: 110px; }
.dsv-skel-xl  { width: 150px; }
.dsv-skel-btn { width: 80px; height: 26px; border-radius: 7px; }
.dsv-skel-circle { width: 32px; height: 32px; border-radius: 50%; flex-shrink: 0; }
.dsv-skeleton-row td { padding: 12px 14px; }

/* ── EMPTY ── */
.dsv-empty {
  text-align: center; padding: 48px 24px !important; color: #94a3b8;
}
.dsv-empty svg { display: block; margin: 0 auto 12px; }
.dsv-empty p { margin: 0; font-size: 13px; }

/* ── RESPONSIVE ── */
@media (max-width: 700px) {
  .dsv-filter-grid { grid-template-columns: 1fr 1fr; }
  .col-email, .col-ver { display: none; }
}
</style>