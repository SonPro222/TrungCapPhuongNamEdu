<template>
  <section class="ct-page">
    <!-- BACK -->
    <nav class="ct-back-bar">
      <RouterLink :to="{ name: 'AdminSinhVienDanhSach' }" class="ct-back-link">
        <svg
            width="14"
            height="14"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            stroke-width="2.5"
        >
          <polyline points="15 18 9 12 15 6"/>
        </svg>
        Danh sách sinh viên
      </RouterLink>

      <span class="ct-sep">›</span>
      <span class="ct-cur">{{ sinhVien?.hoTen || 'Chi tiết sinh viên' }}</span>
    </nav>

    <div v-if="dangTai" class="ct-loading">
      <div class="ct-spinner"></div>
      Đang tải hồ sơ...
    </div>

    <div v-else-if="loiTai" class="ct-error">
      {{ loiTai }}
    </div>

    <template v-else-if="sinhVien">
      <!-- HERO -->
      <div class="ct-hero">
        <div class="ct-hero-left">
          <div
              class="ct-big-avatar"
              :style="{ background: avatarColor(sinhVien.hoTen) }"
          >
            {{ layChuCai(sinhVien.hoTen) }}
          </div>

          <div class="ct-hero-info">
            <h1 class="ct-hero-name">{{ sinhVien.hoTen }}</h1>

            <div class="ct-hero-meta">
              <span class="ct-masv-chip">{{ sinhVien.maSinhVien }}</span>
              <span class="ct-dot">·</span>
              <span class="ct-email-txt">{{ sinhVien.email }}</span>
              <span class="ct-dot">·</span>
              <span class="ct-status" :class="ttClass(sinhVien.trangThai)">
                {{ ttLabel(sinhVien.trangThai) }}
              </span>
            </div>

            <div class="ct-hero-tags" v-if="infoHoc">
              <span class="ct-tag-nganh">{{ infoHoc.tenNganh }}</span>
              <span class="ct-tag-ct">{{ infoHoc.tenChuongTrinh }}</span>
              <span class="ct-tag-ver">{{ infoHoc.maVersion }}</span>
            </div>
          </div>
        </div>

        <div class="ct-stats">
          <div class="ct-stat">
            <div class="ct-stat-val blue">{{ stats.dangHoc }}</div>
            <div class="ct-stat-lbl">Đang học</div>
          </div>

          <div class="ct-stat">
            <div class="ct-stat-val green">{{ stats.hoanThanh }}</div>
            <div class="ct-stat-lbl">Hoàn thành</div>
          </div>

          <div class="ct-stat">
            <div class="ct-stat-val red">{{ stats.rot }}</div>
            <div class="ct-stat-lbl">Rớt môn</div>
          </div>

          <div class="ct-stat">
            <div class="ct-stat-val gray">{{ stats.huy }}</div>
            <div class="ct-stat-lbl">Đã hủy</div>
          </div>
        </div>
      </div>

      <!-- TABS -->
      <div class="ct-tabs">
        <button
            v-for="t in tabs"
            :key="t.key"
            class="ct-tab"
            :class="{ active: tabActive === t.key }"
            @click="doiTab(t.key)"
        >
          {{ t.icon }} {{ t.label }}
          <span v-if="t.count != null" class="ct-tab-cnt">
            {{ t.count }}
          </span>
        </button>
      </div>

      <!-- TAB HỒ SƠ -->
      <div v-if="tabActive === 'hoSo'" class="ct-panel">
        <div class="ct-2col">
          <div class="ct-section">
            <div class="ct-sec-title">Thông tin cá nhân</div>

            <div class="ct-info-grid">
              <div class="ct-row">
                <span class="ct-lbl">Họ và tên</span>
                <span class="ct-val">{{ sinhVien.hoTen || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Ngày sinh</span>
                <span class="ct-val">{{ fmt(sinhVien.ngaySinh) }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Giới tính</span>
                <span class="ct-val">{{ gtLabel(sinhVien.gioiTinh) }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">CCCD/CMND</span>
                <span class="ct-val mono">{{ sinhVien.soCccd || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Ngày cấp</span>
                <span class="ct-val">{{ fmt(sinhVien.ngayCapCccd) }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Nơi cấp</span>
                <span class="ct-val">{{ sinhVien.noiCapCccd || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">SĐT</span>
                <span class="ct-val mono">{{ sinhVien.soDienThoai || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Địa chỉ</span>
                <span class="ct-val">{{ sinhVien.diaChi || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Thường trú</span>
                <span class="ct-val">{{ sinhVien.diaChiThuongTru || '—' }}</span>
              </div>
            </div>
          </div>

          <div class="ct-section">
            <div class="ct-sec-title">Thông tin học tập</div>

            <div class="ct-info-grid">
              <div class="ct-row">
                <span class="ct-lbl">Mã sinh viên</span>
                <span class="ct-val">
                  <span class="ct-masv-chip dark">{{ sinhVien.maSinhVien }}</span>
                </span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Gmail</span>
                <span class="ct-val">{{ sinhVien.email || '—' }}</span>
              </div>

              <div class="ct-row" v-if="svChuongTrinh">
                <span class="ct-lbl">Nhập học</span>
                <span class="ct-val">{{ fmt(svChuongTrinh.ngayNhapHoc) }}</span>
              </div>

              <div class="ct-row" v-if="infoHoc">
                <span class="ct-lbl">Ngành</span>
                <span class="ct-val">{{ infoHoc.tenNganh }}</span>
              </div>

              <div class="ct-row" v-if="infoHoc">
                <span class="ct-lbl">Chương trình</span>
                <span class="ct-val">{{ infoHoc.tenChuongTrinh }}</span>
              </div>

              <div class="ct-row" v-if="infoHoc">
                <span class="ct-lbl">Version</span>
                <span class="ct-val">{{ infoHoc.maVersion }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Bằng cấp vào</span>
                <span class="ct-val">{{ sinhVien.bangCap || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Năm TN THPT</span>
                <span class="ct-val">{{ sinhVien.namTotNghiep || '—' }}</span>
              </div>

              <div class="ct-row">
                <span class="ct-lbl">Trường THPT</span>
                <span class="ct-val">{{ sinhVien.truongTotNghiep || '—' }}</span>
              </div>
            </div>
          </div>

          <div class="ct-section ct-full">
            <div class="ct-sec-title">Thông tin gia đình</div>

            <div class="ct-family">
              <div class="ct-fam-block">
                <div class="ct-fam-head">Cha</div>

                <div class="ct-info-grid">
                  <div class="ct-row">
                    <span class="ct-lbl">Họ tên</span>
                    <span class="ct-val">{{ sinhVien.hoTenCha || '—' }}</span>
                  </div>

                  <div class="ct-row">
                    <span class="ct-lbl">Nghề nghiệp</span>
                    <span class="ct-val">{{ sinhVien.ngheNghiepCha || '—' }}</span>
                  </div>

                  <div class="ct-row">
                    <span class="ct-lbl">SĐT</span>
                    <span class="ct-val mono">{{ sinhVien.sdtCha || '—' }}</span>
                  </div>
                </div>
              </div>

              <div class="ct-fam-block">
                <div class="ct-fam-head">Mẹ</div>

                <div class="ct-info-grid">
                  <div class="ct-row">
                    <span class="ct-lbl">Họ tên</span>
                    <span class="ct-val">{{ sinhVien.hoTenMe || '—' }}</span>
                  </div>

                  <div class="ct-row">
                    <span class="ct-lbl">Nghề nghiệp</span>
                    <span class="ct-val">{{ sinhVien.ngheNghiepMe || '—' }}</span>
                  </div>

                  <div class="ct-row">
                    <span class="ct-lbl">SĐT</span>
                    <span class="ct-val mono">{{ sinhVien.sdtMe || '—' }}</span>
                  </div>
                </div>
              </div>

              <div class="ct-fam-block">
                <div class="ct-fam-head">Người thân khẩn cấp</div>

                <div class="ct-info-grid">
                  <div class="ct-row">
                    <span class="ct-lbl">Họ tên</span>
                    <span class="ct-val">{{ sinhVien.hoTenNguoiThan || '—' }}</span>
                  </div>

                  <div class="ct-row">
                    <span class="ct-lbl">Quan hệ</span>
                    <span class="ct-val">{{ sinhVien.quanHeNguoiThan || '—' }}</span>
                  </div>

                  <div class="ct-row">
                    <span class="ct-lbl">SĐT</span>
                    <span class="ct-val mono">{{ sinhVien.sdtNguoiThan || '—' }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- TAB MÔN HỌC -->
      <div v-if="tabActive === 'monHoc'" class="ct-panel">
        <div v-if="dangTaiMonHoc" class="ct-loading-sm">
          <div class="ct-spin-sm"></div>
          Đang tải...
        </div>

        <div v-else class="ct-card">
          <div class="ct-table-wrap">
            <table class="ct-table" style="table-layout: fixed; width: 100%;">
              <colgroup>
                <col class="col-stt"/>
                <col class="col-ma-mon"/>
                <col class="col-ten-mon"/>
                <col class="col-ma-lop"/>
                <col class="col-ten-lop"/>
                <col class="col-dang-ky"/>
                <col class="col-trang-thai"/>
                <col class="col-diem"/>
                <col class="col-ket-qua"/>
                <col class="col-diem-chu"/>
                <col class="col-cc"/>
                <col class="col-qt"/>
                <col class="col-thi"/>
                <col class="col-tin-chi"/>
                <col class="col-thao-tac"/>
              </colgroup>

              <thead>
              <tr>
                <th class="tc">STT</th>
                <th>Mã môn</th>
                <th>Tên môn</th>
                <th>Mã lớp</th>
                <th>Tên lớp học phần</th>
                <th>Đăng ký</th>
                <th>Trạng thái</th>
                <th class="tc">Điểm TK</th>
                <th class="tc">Kết quả</th>
                <th class="tc">Điểm chữ</th>
                <th class="tc">CC</th>
                <th class="tc">QT</th>
                <th class="tc">Thi</th>
                <th class="tc">Số tín chỉ</th>
                <th class="tc">Thao tác</th>
              </tr>
              </thead>

              <tbody>
              <template v-for="(row, i) in danhSachMonHienThi" :key="row.svlhp.id">
                <tr class="mon-row">
                  <td class="tc muted">{{ i + 1 }}</td>

                  <td>
                    <span class="ma-chip">{{ layMaMonHoc(row) }}</span>
                  </td>

                  <td class="bold" :title="layTenMonHoc(row)">
                    {{ layTenMonHoc(row) }}
                  </td>

                  <td>
                    <span class="ma-chip">{{ row.lhp?.maLop || '—' }}</span>
                  </td>

                  <td class="bold" :title="row.lhp?.tenLop || ''">
                    {{ row.lhp?.tenLop || '—' }}
                  </td>

                  <td class="muted">
                    {{ fmtDate(row.svlhp.ngayDangKy) }}
                  </td>

                  <td>
                      <span class="mon-status" :class="msClass(row.svlhp.trangThai)">
                        {{ msLabel(row.svlhp.trangThai) }}
                      </span>
                  </td>

                  <td class="tc">
                      <span
                          v-if="row.kq?.diemTongKet != null"
                          class="diem-val"
                          :class="diemCls(row.kq.diemTongKet)"
                      >
                        {{ Number(row.kq.diemTongKet).toFixed(1) }}
                      </span>
                    <span v-else class="muted">—</span>
                  </td>

                  <td class="tc">
                      <span
                          v-if="row.kq?.ketQua"
                          class="kq-badge"
                          :class="kqCls(row.kq.ketQua)"
                      >
                        {{ row.kq.ketQua }}
                      </span>
                    <span v-else class="muted">—</span>
                  </td>

                  <td class="tc muted">
                    {{ row.kq?.diemQuyDoi ?? '—' }}
                  </td>

                  <td class="tc muted">
                    {{ row.kq?.diemChuyenCan != null ? Number(row.kq.diemChuyenCan).toFixed(1) : '—' }}
                  </td>

                  <td class="tc muted">
                    {{ row.kq?.diemQuaTrinh != null ? Number(row.kq.diemQuaTrinh).toFixed(1) : '—' }}
                  </td>

                  <td class="tc muted">
                    {{ row.kq?.diemThi != null ? Number(row.kq.diemThi).toFixed(1) : '—' }}
                  </td>
                  <td class="tc muted">
                    {{ laySoTinChi(row) }}
                  </td>
                  <td class="tc">
                    <RouterLink
                        class="btn-detail"
                        :to="{
      name: 'AdminSinhVienChiTietDiem',
      params: {
        id: svId,
        lopHocPhanId: row.svlhp.lopHocPhanId
      }
    }"
                    >
                      Chi tiết điểm
                    </RouterLink>
                  </td>
                </tr>
              </template>

              <tr v-if="!danhSachMonHienThi.length">
                <td colspan="15" class="empty-cell">
                  Chưa có dữ liệu môn học.
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- TAB LỊCH HỌC -->
      <div v-if="tabActive === 'lichHoc'" class="ct-panel">
        <div class="lh-toolbar">
          <label class="lh-label">
            Lớp học phần:
            <select
                v-model="lopHocPhanChon"
                @change="taiLichHoc"
                class="lh-select"
            >
              <option value="">— Chọn lớp —</option>

              <option
                  v-for="lhp in danhSachSVLHP"
                  :key="lhp.lopHocPhanId"
                  :value="lhp.lopHocPhanId"
              >
                {{ layTenLHP(lhp.lopHocPhanId) }}
              </option>
            </select>
          </label>
        </div>

        <div v-if="dangTaiLichHoc" class="ct-loading-sm">
          <div class="ct-spin-sm"></div>
          Đang tải lịch học...
        </div>

        <div v-else-if="!lopHocPhanChon" class="lh-hint">
          <svg
              width="32"
              height="32"
              viewBox="0 0 24 24"
              fill="none"
              stroke="#bfdbfe"
              stroke-width="1.4"
          >
            <rect x="3" y="4" width="18" height="18" rx="2"/>
            <line x1="16" y1="2" x2="16" y2="6"/>
            <line x1="8" y1="2" x2="8" y2="6"/>
            <line x1="3" y1="10" x2="21" y2="10"/>
          </svg>

          <p>Chọn lớp học phần để xem lịch học.</p>
        </div>

        <div v-else class="ct-card">
          <div class="ct-table-wrap">
            <table class="ct-table" style="table-layout: fixed; width: 100%;">
              <colgroup>
                <col style="width: 44px"/>
                <col style="width: 110px"/>
                <col style="width: 120px"/>
                <col style="width: 110px"/>
                <col style="width: 110px"/>
                <col style="width: 150px"/>
                <col/>
                <col style="width: 110px"/>
              </colgroup>

              <thead>
              <tr>
                <th>STT</th>
                <th>Ngày học</th>
                <th>Ca học</th>
                <th>Giờ</th>
                <th>Phòng</th>
                <th>Giáo viên</th>
                <th>Nội dung</th>
                <th>Trạng thái</th>
              </tr>
              </thead>

              <tbody>
              <tr
                  v-for="(lh, i) in danhSachLichHoc"
                  :key="lh.id"
                  class="mon-row"
              >
                <td class="tc muted">{{ i + 1 }}</td>

                <td class="bold">
                  {{ fmt(lh.ngayHoc) }}
                </td>

                <td>
                  {{ lh.tenCa || lh.maCa || '—' }}
                </td>

                <td class="muted mono">
                  {{ lh.gioBatDau ? lh.gioBatDau.substring(0, 5) : '—' }}
                  {{ lh.gioKetThuc ? ' – ' + lh.gioKetThuc.substring(0, 5) : '' }}
                </td>

                <td>
                  {{ lh.tenPhong || lh.maPhong || '—' }}
                </td>

                <td>
                  {{ lh.tenGiaoVien || '—' }}
                </td>

                <td class="muted nxgv">
                  {{ lh.noiDungBuoiHoc || '—' }}
                </td>

                <td>
                    <span class="lh-status" :class="lhCls(lh.trangThai)">
                      {{ lhLabel(lh.trangThai) }}
                    </span>
                </td>
              </tr>

              <tr v-if="!danhSachLichHoc.length">
                <td colspan="8" class="empty-cell">
                  Chưa có lịch học.
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </template>
  </section>
</template>

<script setup>
import {ref, computed, onMounted, watch} from 'vue'
import {useRoute, useRouter} from 'vue-router'
import {sinhVienService} from '../services/sinhVienService'

const route = useRoute()
const router = useRouter()
const svId = computed(() => Number(route.params.id))

const sinhVien = ref(null)
const svChuongTrinh = ref(null)
const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachLopHocPhan = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachSVLHP = ref([])
const danhSachKetQua = ref([])
const danhSachLichHoc = ref([])

const dangTai = ref(false)
const dangTaiMonHoc = ref(false)
const dangTaiLichHoc = ref(false)
const dangTaiDetail = ref(false)
const loiTai = ref('')

const tabActive = ref(route.query.tab || 'hoSo')
watch(
    () => route.query.tab,
    (tab) => {
      if (['hoSo', 'monHoc', 'lichHoc'].includes(tab)) {
        tabActive.value = tab
      }
    }
)
const lopHocPhanChon = ref('')

// Chi tiết expand
// const expandedLhpId      = ref(null)
// const innerTab           = ref('cotDiem')
// const cauHinhDanhGiaList = ref([])
// const baiKiemTraList     = ref([])
// const diemChiTietList    = ref([])
// const diemDanhList       = ref([])

// Tabs
const tabs = computed(() => [
  {key: 'hoSo', label: 'Hồ sơ', icon: '👤'},
  {key: 'monHoc', label: 'Môn học', icon: '📚', count: danhSachSVLHP.value.length},
  {key: 'lichHoc', label: 'Lịch học', icon: '📅'},
])

// Derived
const infoHoc = computed(() => {
  if (!svChuongTrinh.value) return null
  const ver = danhSachVersion.value.find(v => String(v.id) === String(svChuongTrinh.value.chuongTrinhVersionId))
  if (!ver) return null
  const ct = danhSachChuongTrinh.value.find(c => String(c.id) === String(ver.chuongTrinhId))
  const ng = ct ? danhSachNganh.value.find(n => String(n.id) === String(ct.nganhId)) : null
  return {
    maVersion: ver.maVersion || ver.tenVersion || '—',
    tenChuongTrinh: ct?.tenChuongTrinh || '—',
    tenNganh: ng?.tenNganh || '—'
  }
})

const stats = computed(() => ({
  dangHoc: danhSachSVLHP.value.filter(x => ['da_dang_ky', 'dang_hoc'].includes(x.trangThai)).length,
  hoanThanh: danhSachSVLHP.value.filter(x => x.trangThai === 'hoan_thanh').length,
  rot: danhSachSVLHP.value.filter(x => x.trangThai === 'rot').length,
  huy: danhSachSVLHP.value.filter(x => x.trangThai === 'huy').length,
}))

const ketQuaMap = computed(() => {
  const m = {};
  danhSachKetQua.value.forEach(k => {
    m[k.lopHocPhanId] = k
  });
  return m
})
const lhpMap = computed(() => {
  const m = {};
  danhSachLopHocPhan.value.forEach(l => {
    m[l.id] = l
  });
  return m
})
const ctmMap = computed(() => {
  const m = {};
  danhSachChuongTrinhMon.value.forEach(ctm => {
    m[ctm.id] = ctm
  });
  return m
})

const danhSachMonHienThi = computed(() =>
    danhSachSVLHP.value.map(svlhp => ({
      svlhp,
      lhp: lhpMap.value[svlhp.lopHocPhanId] || null,
      kq: ketQuaMap.value[svlhp.lopHocPhanId] || null,
    }))
)

// const diemDanhStats = computed(() => {
//   const list = diemDanhList.value
//   return {
//     tong:          list.length,
//     coMat:         list.filter(d => d.trangThai === 'co_mat').length,
//     vangPhep:      list.filter(d => d.trangThai === 'vang_co_phep').length,
//     vangKhongPhep: list.filter(d => d.trangThai === 'vang_khong_phep').length,
//     diMuon:        list.filter(d => d.trangThai === 'di_muon').length,
//   }
// })

function doiTab(tab) {
  tabActive.value = tab

  router.replace({
    name: 'AdminSinhVienChiTiet',
    params: {id: svId.value},
    query: {tab}
  })
}

// Lookups
function layDiemChiTiet(cauHinhDanhGiaId) {
  return diemChiTietList.value.find(d => String(d.cauHinhDanhGiaId) === String(cauHinhDanhGiaId)) || null
}

function layBaiKiemTra(cauHinhDanhGiaId) {
  return baiKiemTraList.value.find(b => String(b.cauHinhDanhGiaId) === String(cauHinhDanhGiaId)) || null
}

function layTenLHP(id) {
  const l = lhpMap.value[id];
  return l ? `${l.maLop} – ${l.tenLop}` : `LHP #${id}`
}

function layMaMonHoc(row) {
  const lhp = row?.lhp || {}
  const ctm = lhp.chuongTrinhMonId ? ctmMap.value[lhp.chuongTrinhMonId] : null
  return lhp.maMonHoc
      || lhp.monHocMa
      || lhp.maMon
      || ctm?.maMonTrongCt
      || '—'
}

function layTenMonHoc(row) {
  const lhp = row?.lhp || {}
  const ctm = lhp.chuongTrinhMonId ? ctmMap.value[lhp.chuongTrinhMonId] : null

  return lhp.tenMonHoc
      || lhp.monHocTen
      || lhp.tenMon
      || ctm?.tenMonHoc
      || ctm?.monHocTen
      || '—'
}
function fmt(val) {
  if (!val) return '—'
  try {
    const d = new Date(val);
    return isNaN(d) ? val : d.toLocaleDateString('vi-VN', {day: '2-digit', month: '2-digit', year: 'numeric'})
  } catch {
    return val
  }
}

function fmtDate(val) {
  return val ? fmt(val) : '—'
}

function fmtDateTime(val) {
  if (!val) return '—'
  try {
    const d = new Date(val);
    return isNaN(d) ? val : d.toLocaleString('vi-VN', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  } catch {
    return val
  }
}

function layChuCai(n) {
  if (!n) return '?';
  const p = n.trim().split(' ');
  return p[p.length - 1].charAt(0).toUpperCase()
}

const COLORS = ['#1d4ed8', '#0891b2', '#7c3aed', '#be185d', '#b45309', '#047857']

function avatarColor(n) {
  if (!n) return '#1d4ed8';
  let h = 0;
  for (const c of n) h = c.charCodeAt(0) + ((h << 5) - h);
  return COLORS[Math.abs(h) % COLORS.length]
}

function gtLabel(g) {
  return {NAM: 'Nam', NU: 'Nữ', KHAC: 'Khác'}[g] || g || '—'
}

function ttLabel(t) {
  return {dang_hoc: 'Đang học', bao_luu: 'Bảo lưu', thoi_hoc: 'Thôi học', tot_nghiep: 'Tốt nghiệp'}[t] || t || '—'
}

function ttClass(t) {
  return {'tt-dh': t === 'dang_hoc', 'tt-bl': t === 'bao_luu', 'tt-th': t === 'thoi_hoc', 'tt-tn': t === 'tot_nghiep'}
}

function msLabel(t) {
  return {
    da_dang_ky: 'Đã đăng ký',
    dang_hoc: 'Đang học',
    hoan_thanh: 'Hoàn thành',
    rot: 'Rớt',
    huy: 'Đã hủy',
    hoc_lai: 'Học lại'
  }[t] || t || '—'
}

function msClass(t) {
  return {
    'ms-ddk': t === 'da_dang_ky',
    'ms-dh': t === 'dang_hoc',
    'ms-ht': t === 'hoan_thanh',
    'ms-rot': t === 'rot',
    'ms-huy': t === 'huy',
    'ms-hl': t === 'hoc_lai'
  }
}

function diemCls(d) {
  if (d == null) return '';
  const v = Number(d);
  return v >= 8 ? 'diem-tot' : v >= 5 ? 'diem-tb' : 'diem-yeu'
}

function kqCls(k) {
  if (!k) return '';
  const u = k.toUpperCase();
  return u === 'DAT' || u === 'PASS' ? 'kq-dat' : (u.includes('KHONG') || u === 'FAIL') ? 'kq-fail' : ''
}

function loaiLabel(l) {
  return {
    chuyen_can: 'Chuyên cần',
    qua_trinh: 'Quá trình',
    thi: 'Thi',
    bai_tap: 'Bài tập',
    giua_ky: 'Giữa kỳ',
    cuoi_ky: 'Cuối kỳ',
    thuc_hanh: 'Thực hành'
  }[l] || l || '—'
}

function loaiCls(l) {
  return {
    'ld-cc': l === 'chuyen_can',
    'ld-qt': l === 'qua_trinh',
    'ld-thi': l === 'thi',
    'ld-bt': l === 'bai_tap',
    'ld-gk': l === 'giua_ky',
    'ld-ck': l === 'cuoi_ky',
    'ld-th': l === 'thuc_hanh'
  }
}
function laySoTinChi(row) {
  const lhp = row?.lhp || {}
  const ctm = lhp.chuongTrinhMonId ? ctmMap.value[lhp.chuongTrinhMonId] : null

  return lhp.soTinChi
      ?? ctm?.soTinChi
      ?? '—'
}
function ddLabel(t) {
  return {
    chua_diem_danh: 'Chưa ĐD',
    co_mat: 'Có mặt',
    vang_co_phep: 'Vắng có phép',
    vang_khong_phep: 'Vắng KP',
    di_muon: 'Đi muộn'
  }[t] || t || '—'
}

function ddCls(t) {
  return {
    'dd-cm': t === 'co_mat',
    'dd-vp': t === 'vang_co_phep',
    'dd-vk': t === 'vang_khong_phep',
    'dd-dm': t === 'di_muon',
    'dd-cd': t === 'chua_diem_danh'
  }
}

function lhLabel(t) {
  return {da_day: 'Đã dạy', chua_day: 'Chưa dạy', vang: 'Vắng', huy: 'Đã hủy', bu_lich: 'Bù lịch'}[t] || t || '—'
}

function lhCls(t) {
  return {'lhs-dd': t === 'da_day', 'lhs-cd': t === 'chua_day', 'lhs-v': t === 'vang', 'lhs-h': t === 'huy'}
}

onMounted(async () => {
  dangTai.value = true
  try {
    const id = svId.value
    const [sv, ng, ct, ver, svct, svlhp, kq, lhpAll, ctmAll] = await Promise.all([
      sinhVienService.laySinhVienTheoId(id),
      sinhVienService.layNganh(),
      sinhVienService.layChuongTrinh(),
      sinhVienService.layVersion(),
      sinhVienService.laySinhVienChuongTrinhTheoSinhVienId(id),
      sinhVienService.laySinhVienLopHocPhanTheoSinhVien(id),
      sinhVienService.layKetQuaLopHocPhanTheoSinhVien(id),
      sinhVienService.layLopHocPhan({page: 0, size: 500}),
      sinhVienService.layChuongTrinhMon(),
    ])
    sinhVien.value = sv
    danhSachNganh.value = ng
    danhSachChuongTrinh.value = ct
    danhSachVersion.value = ver
    svChuongTrinh.value = svct?.[0] || null
    danhSachSVLHP.value = svlhp
    danhSachKetQua.value = kq
    danhSachLopHocPhan.value = lhpAll
    danhSachChuongTrinhMon.value = ctmAll
  } catch (e) {
    loiTai.value = 'Lỗi tải hồ sơ: ' + (e?.message || e)
  } finally {
    dangTai.value = false
  }
})

async function taiLichHoc() {
  if (!lopHocPhanChon.value) return
  dangTaiLichHoc.value = true
  try {
    danhSachLichHoc.value = await sinhVienService.layLichHocTheoLop(lopHocPhanChon.value)
  } catch {
    danhSachLichHoc.value = []
  } finally {
    dangTaiLichHoc.value = false
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');

.ct-page,
.ct-page * {
  font-family: 'Roboto', Arial, sans-serif;
  letter-spacing: normal;
  box-sizing: border-box;
}

.ct-page {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.ct-back-bar {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 13px;
}

.ct-back-link {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  color: #1d4ed8;
  text-decoration: none;
  font-weight: 500;
}

.ct-back-link:hover {
  text-decoration: underline;
}

.ct-sep {
  color: #94a3b8;
}

.ct-cur {
  color: #475569;
  font-weight: 600;
}

.ct-loading {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  padding: 60px;
  font-size: 14px;
  color: #64748b;
}

.ct-spinner {
  width: 22px;
  height: 22px;
  border: 2.5px solid #e2e8f0;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin .7s linear infinite;
}

.ct-spin-sm {
  width: 14px;
  height: 14px;
  border: 2px solid #e2e8f0;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin .7s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

.ct-loading-sm {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 20px;
  font-size: 13px;
  color: #64748b;
}

.ct-error {
  background: #fef2f2;
  border: 1px solid #fecaca;
  border-radius: 10px;
  padding: 12px 16px;
  font-size: 13px;
  color: #dc2626;
}

/* HERO */
.ct-hero {
  background: linear-gradient(135deg, #0f172a 0%, #1e3a8a 100%);
  border-radius: 16px;
  padding: 22px 26px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 20px;
  flex-wrap: wrap;
  box-shadow: 0 8px 24px rgba(15, 23, 42, .18);
}

.ct-hero-left {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  flex: 1;
  min-width: 260px;
}

.ct-big-avatar {
  flex-shrink: 0;
  width: 60px;
  height: 60px;
  border-radius: 50%;
  color: #fff;
  font-size: 24px;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 3px solid rgba(255, 255, 255, .2);
}

.ct-hero-info {
  flex: 1;
}

.ct-hero-name {
  margin: 0 0 6px;
  font-size: 20px;
  font-weight: 700;
  color: #fff;
}

.ct-hero-meta {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 7px;
  margin-bottom: 9px;
}

.ct-masv-chip {
  display: inline-block;
  padding: 2px 8px;
  background: rgba(255, 255, 255, .15);
  border: 1px solid rgba(255, 255, 255, .2);
  border-radius: 6px;
  font-family: 'Roboto', Arial, sans-serif;
  font-size: 12px;
  color: #fff;
}

.ct-masv-chip.dark {
  background: #f1f5f9;
  border-color: #e2e8f0;
  color: #334155;
}

.ct-dot {
  color: rgba(255, 255, 255, .4);
}

.ct-email-txt {
  font-size: 13px;
  color: #bfdbfe;
}

.ct-status {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 2px 8px;
  border-radius: 20px;
  font-size: 11.5px;
  font-weight: 600;
}

.tt-dh {
  background: rgba(16, 185, 129, .2);
  color: #6ee7b7;
}

.tt-bl {
  background: rgba(245, 158, 11, .2);
  color: #fde68a;
}

.tt-th {
  background: rgba(239, 68, 68, .2);
  color: #fca5a5;
}

.tt-tn {
  background: rgba(59, 130, 246, .2);
  color: #93c5fd;
}

.ct-hero-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.ct-tag-nganh, .ct-tag-ct, .ct-tag-ver {
  display: inline-block;
  padding: 3px 8px;
  border-radius: 6px;
  font-size: 11.5px;
  font-weight: 500;
}

.ct-tag-nganh {
  background: rgba(16, 185, 129, .15);
  color: #6ee7b7;
  border: 1px solid rgba(16, 185, 129, .2);
}

.ct-tag-ct {
  background: rgba(251, 191, 36, .15);
  color: #fde68a;
  border: 1px solid rgba(251, 191, 36, .2);
}

.ct-tag-ver {
  background: rgba(167, 139, 250, .15);
  color: #c4b5fd;
  border: 1px solid rgba(167, 139, 250, .2);
}

.ct-stats {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.ct-stat {
  background: rgba(255, 255, 255, .08);
  border: 1px solid rgba(255, 255, 255, .1);
  border-radius: 12px;
  padding: 12px 18px;
  text-align: center;
  min-width: 70px;
}

.ct-stat-val {
  font-size: 26px;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 4px;
}

.ct-stat-lbl {
  font-size: 10.5px;
  color: rgba(255, 255, 255, .5);
  text-transform: uppercase;
  letter-spacing: .05em;
}

.blue {
  color: #60a5fa;
}

.green {
  color: #4ade80;
}

.red {
  color: #f87171;
}

.gray {
  color: #94a3b8;
}

/* TABS */
.ct-tabs {
  display: flex;
  gap: 4px;
  padding: 5px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  box-shadow: 0 1px 4px rgba(15, 23, 42, .04);
}

.ct-tab {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  height: 34px;
  padding: 0 12px;
  background: transparent;
  border: 1px solid transparent;
  border-radius: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all .14s;
}

.ct-tab:hover {
  background: #f8fafc;
}

.ct-tab.active {
  background: #eff6ff;
  border-color: #bfdbfe;
  color: #1d4ed8;
}

.ct-tab-cnt {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 17px;
  padding: 0 4px;
  background: #e2e8f0;
  border-radius: 9px;
  font-size: 10.5px;
  font-weight: 700;
  color: #64748b;
}

.ct-tab.active .ct-tab-cnt {
  background: #bfdbfe;
  color: #1d4ed8;
}

.ct-panel {
  animation: fadeIn .18s ease;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(4px);
  }
  to {
    opacity: 1;
    transform: none;
  }
}

/* HỒ SƠ */
.ct-2col {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.ct-section {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  padding: 16px 18px;
  box-shadow: 0 1px 4px rgba(15, 23, 42, .04);
}

.ct-full {
  grid-column: 1/-1;
}

.ct-sec-title {
  font-size: 11.5px;
  font-weight: 700;
  color: #64748b;
  text-transform: uppercase;
  letter-spacing: .06em;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #f1f5f9;
}

.ct-info-grid {
  display: flex;
  flex-direction: column;
  gap: 7px;
}

.ct-row {
  display: flex;
  gap: 10px;
}

.ct-lbl {
  flex-shrink: 0;
  width: 130px;
  font-size: 12px;
  color: #94a3b8;
  font-weight: 500;
  padding-top: 1px;
}

.ct-val {
  flex: 1;
  font-size: 13px;
  color: #1e293b;
  font-weight: 500;
  line-height: 1.4;
  word-break: break-word;
}

.mono {
  font-family: 'Roboto', Arial, sans-serif;
}

.ct-family {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(210px, 1fr));
  gap: 12px;
}

.ct-fam-block {
  background: #f8fafc;
  border-radius: 10px;
  padding: 11px 13px;
}

.ct-fam-head {
  font-size: 12px;
  font-weight: 700;
  color: #334155;
  margin-bottom: 8px;
}

/* CARD / TABLE */
/* CARD / TABLE */
.ct-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  overflow: hidden;
  box-shadow: 0 1px 4px rgba(15, 23, 42, .04);
}

.ct-table-wrap {
  width: 100%;
  overflow-x: auto;
}

.ct-table {
  width: 100%;
  min-width: 1370px;
  border-collapse: collapse;
  table-layout: fixed;
  font-size: 13px;
}

.ct-table .col-stt {
  width: 52px;
}

.ct-table .col-ma-mon {
  width: 115px;
}

.ct-table .col-ten-mon {
  width: 240px;
}

.ct-table .col-ma-lop {
  width: 135px;
}

.ct-table .col-ten-lop {
  width: 230px;
}

.ct-table .col-dang-ky {
  width: 105px;
}

.ct-table .col-trang-thai {
  width: 120px;
}

.ct-table .col-diem {
  width: 85px;
}

.ct-table .col-ket-qua {
  width: 90px;
}

.ct-table .col-diem-chu {
  width: 90px;
}

.ct-table .col-cc,
.ct-table .col-qt,
.ct-table .col-thi {
  width: 70px;
}

.ct-table .col-thao-tac {
  width: 130px;
}

.ct-table thead tr {
  background: #f8fafc;
}

.ct-table th {
  height: 42px;
  padding: 9px 10px;
  text-align: left;
  font-size: 11px;
  font-weight: 700;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: .04em;
  border-bottom: 1px solid #e2e8f0;
  white-space: nowrap;
  vertical-align: middle;
}

.ct-table td {
  height: 46px;
  padding: 9px 10px;
  color: #1e293b;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ct-table th.tc,
.ct-table td.tc {
  text-align: center;
}

.ct-table th.tr,
.ct-table td.tr {
  text-align: right;
}

.mon-row:hover td {
  background: #f8fafc;
}

.mon-row:last-child td {
  border-bottom: none;
}

.bold {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
}

.mon-row:last-child td {
  border-bottom: none;
}

.mon-row:hover td {
  background: #f8fafc;
}

.row-expanded td {
  background: #f0f7ff !important;
  border-bottom: none !important;
}

.tc {
  text-align: center;
}

.tr {
  text-align: right;
}

.muted {
  color: #64748b;
}

.bold {
  font-weight: 600;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Cột tên: giới hạn chiều rộng, cắt bằng ellipsis thay vì wrap xuống dòng */
.bold {
  font-weight: 600;
  max-width: 200px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.nxgv {
  max-width: 180px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.w40 {
  width: 40px;
}

.w70 {
  width: 70px;
}

.w75 {
  width: 75px;
}

.w80 {
  width: 80px;
}

.w85 {
  width: 85px;
}

.w95 {
  width: 95px;
}

.w100 {
  width: 100px;
}

.w110 {
  width: 110px;
}

.w120 {
  width: 120px;
}

.w130 {
  width: 130px;
}

.w140 {
  width: 140px;
}

.w150 {
  width: 150px;
}

/* BADGES */
.ma-chip {
  display: inline-block;
  padding: 2px 7px;
  background: #f1f5f9;
  border: 1px solid #e2e8f0;
  border-radius: 5px;
  font-family: 'Roboto', Arial, sans-serif;
  font-size: 12px;
  font-weight: 600;
  color: #334155;
}

.mon-status {
  display: inline-flex;
  align-items: center;
  padding: 3px 8px;
  border-radius: 20px;
  font-size: 11.5px;
  font-weight: 600;
}

.ms-ddk {
  background: #f1f5f9;
  color: #475569;
}

.ms-dh {
  background: #eff6ff;
  color: #1d4ed8;
}

.ms-ht {
  background: #dcfce7;
  color: #15803d;
}

.ms-rot {
  background: #fee2e2;
  color: #b91c1c;
}

.ms-huy {
  background: #f1f5f9;
  color: #94a3b8;
}

.ms-hl {
  background: #fef9c3;
  color: #92400e;
}

.diem-val {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 13px;
  font-weight: 700;
}

.diem-val.lg {
  font-size: 15px;
  padding: 3px 10px;
}

.diem-tot {
  background: #dcfce7;
  color: #15803d;
}

.diem-tb {
  background: #fef9c3;
  color: #92400e;
}

.diem-yeu {
  background: #fee2e2;
  color: #b91c1c;
}

.kq-badge {
  display: inline-block;
  padding: 2px 8px;
  border-radius: 6px;
  font-size: 12px;
  font-weight: 600;
}

.kq-badge.lg {
  font-size: 13px;
  padding: 3px 10px;
}

.kq-dat {
  background: #dcfce7;
  color: #15803d;
}

.kq-fail {
  background: #fee2e2;
  color: #b91c1c;
}

/* EXPAND */
.expand-row > td {
  padding: 0 !important;
  border-bottom: 2px solid #bfdbfe !important;
  border-top: none !important;
}

.expand-cell {
  background: #f0f7ff !important;
  padding: 0 !important;
}

.expand-panel {
  padding: 14px 18px 18px;
  border-top: 2px solid #bfdbfe;
}

.inner-tabs-bar {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  flex-wrap: wrap;
}

.inner-tabs {
  display: flex;
  gap: 6px;
}

.inner-tab {
  height: 30px;
  padding: 0 12px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  transition: all .13s;
}

.inner-tab:hover {
  background: #f1f5f9;
}

.inner-tab.active {
  background: #eff6ff;
  border-color: #bfdbfe;
  color: #1d4ed8;
}

/* ĐIỂM DANH STATS */
.dd-stats-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
}

.dd-st {
  font-size: 12px;
  padding: 3px 9px;
  border-radius: 20px;
  font-weight: 500;
  white-space: nowrap;
}

.dd-st b {
  font-weight: 700;
}

.co-mat {
  background: #dcfce7;
  color: #15803d;
}

.vang-phep {
  background: #fef9c3;
  color: #92400e;
}

.vang-ko {
  background: #fee2e2;
  color: #b91c1c;
}

.di-muon {
  background: #eff6ff;
  color: #1d4ed8;
}

.tong-st {
  background: #f1f5f9;
  color: #334155;
}

/* DETAIL TABLE */
.det-wrap {
  overflow-x: auto;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  background: #fff;
}

.det-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.det-table thead tr {
  background: #f1f5f9;
}

.det-table th {
  padding: 8px 12px;
  text-align: left;
  font-size: 11px;
  font-weight: 700;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: .04em;
  border-bottom: 1px solid #e2e8f0;
  white-space: nowrap;
}

.det-table td {
  padding: 10px 12px;
  color: #1e293b;
  border-bottom: 1px solid #f1f5f9;
  vertical-align: middle;
  white-space: nowrap;
}

.det-row:last-child td {
  border-bottom: none;
}

.det-row:hover td {
  background: #fafafa;
}

.row-chua-cham {
  opacity: .8;
}

.loai-tag {
  display: inline-block;
  padding: 2px 7px;
  border-radius: 6px;
  font-size: 11.5px;
  font-weight: 600;
}

.ld-cc {
  background: #dcfce7;
  color: #15803d;
}

.ld-qt {
  background: #eff6ff;
  color: #1d4ed8;
}

.ld-thi {
  background: #fee2e2;
  color: #b91c1c;
}

.ld-bt {
  background: #fef9c3;
  color: #92400e;
}

.ld-gk {
  background: #faf5ff;
  color: #7e22ce;
}

.ld-ck {
  background: #fff7ed;
  color: #c2410c;
}

.ld-th {
  background: #f0fdf4;
  color: #15803d;
}

.tyle-chip {
  display: inline-block;
  padding: 2px 7px;
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 5px;
  font-size: 12px;
  font-weight: 600;
  color: #475569;
}

.chua-cham {
  display: inline-block;
  padding: 2px 8px;
  background: #f1f5f9;
  border: 1px dashed #cbd5e1;
  border-radius: 6px;
  font-size: 11.5px;
  color: #94a3b8;
}

.tong-ket-row td {
  background: #fffbeb !important;
  border-top: 2px solid #fde68a;
}

.tong-label {
  font-size: 12px;
  font-weight: 700;
  color: #92400e;
  text-transform: uppercase;
  letter-spacing: .04em;
}

.dd-status {
  display: inline-flex;
  align-items: center;
  padding: 3px 9px;
  border-radius: 20px;
  font-size: 11.5px;
  font-weight: 600;
}

.dd-cm {
  background: #dcfce7;
  color: #15803d;
}

.dd-vp {
  background: #fef9c3;
  color: #92400e;
}

.dd-vk {
  background: #fee2e2;
  color: #b91c1c;
}

.dd-dm {
  background: #eff6ff;
  color: #1d4ed8;
}

.dd-cd {
  background: #f1f5f9;
  color: #94a3b8;
}

/* BTN CHI TIẾT */
.btn-detail {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  height: 26px;
  padding: 0 9px;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  border-radius: 7px;
  font-size: 11.5px;
  font-weight: 600;
  color: #1d4ed8;
  cursor: pointer;
  transition: all .13s;
  white-space: nowrap;
}

.btn-detail:hover {
  background: #dbeafe;
}

.btn-detail.active {
  background: #1d4ed8;
  border-color: #1d4ed8;
  color: #fff;
}

/* LỊCH HỌC */
.lh-toolbar {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}

.lh-label {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 13px;
  font-weight: 600;
  color: #475569;
}

.lh-select {
  height: 34px;
  padding: 0 10px;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  font-size: 13px;
  background: #f8fafc;
  min-width: 260px;
}

.lh-status {
  display: inline-flex;
  align-items: center;
  padding: 3px 8px;
  border-radius: 20px;
  font-size: 11.5px;
  font-weight: 600;
}

.lhs-dd {
  background: #dcfce7;
  color: #15803d;
}

.lhs-cd {
  background: #f1f5f9;
  color: #64748b;
}

.lhs-v {
  background: #fef9c3;
  color: #92400e;
}

.lhs-h {
  background: #fee2e2;
  color: #b91c1c;
}

.lh-hint {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  padding: 48px;
  background: #fff;
  border: 1px dashed #bfdbfe;
  border-radius: 14px;
  text-align: center;
}

.lh-hint p {
  margin: 0;
  font-size: 13px;
  color: #64748b;
}

.det-loading {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 20px;
  font-size: 13px;
  color: #64748b;
}

.empty-cell {
  text-align: center;
  padding: 32px !important;
  color: #94a3b8;
  font-size: 13px;
}

@media (max-width: 768px) {
  .ct-hero {
    flex-direction: column;
  }

  .ct-2col {
    grid-template-columns: 1fr;
  }

  .ct-stats {
    justify-content: stretch;
  }

  .ct-stat {
    flex: 1;
  }
}
</style>