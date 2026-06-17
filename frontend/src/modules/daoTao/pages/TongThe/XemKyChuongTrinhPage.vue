<template>
  <div class="dao-tao-xem-ky-page">
    <section class="page-header">
      <div class="page-title">
        <div class="top-line">
          <button type="button" class="back-btn" @click="quayLaiTongQuan">
            ← Quay lại tổng quan
          </button>

          <span class="version-pill">
            Version: <strong>{{ tenVersion }}</strong>
          </span>
        </div>

        <p class="eyebrow">Chương trình đào tạo</p>

        <h1>{{ tenChuongTrinh }}</h1>

        <p class="subtitle">
          Mỗi block là một kỳ, mỗi dòng là một môn học thuộc kỳ đó.
        </p>
      </div>

      <div class="header-actions">
        <button
            type="button"
            class="btn"
            :disabled="loading || !chuongTrinhId"
            @click="taiDuLieu"
        >
          Tải lại
        </button>
      </div>
    </section>

    <section v-if="errorMessage" class="state-card error">
      <h3>Không tải được dữ liệu</h3>
      <p>{{ errorMessage }}</p>
      <button
          type="button"
          class="btn primary"
          :disabled="!chuongTrinhId"
          @click="taiDuLieu"
      >
        Thử lại
      </button>
    </section>

    <section v-if="thongBao" class="notice-card">
      {{ thongBao }}
    </section>

    <section class="overview-strip">
      <article>
        <span>Số kỳ</span>
        <strong>{{ danhSachKy.length }}</strong>
      </article>

      <article>
        <span>Tổng môn</span>
        <strong>{{ tongSoMon }}</strong>
      </article>

      <article>
        <span>Tổng tín chỉ</span>
        <strong>{{ hienThi(tongTinChi) }}</strong>
      </article>
    </section>

    <section class="semester-section">
      <div class="section-title">
        <div>
          <h2>Danh sách kỳ</h2>
          <p>Các kỳ của chương trình/version đang chọn.</p>
        </div>

        <span class="badge neutral">{{ danhSachKy.length }} kỳ</span>
      </div>

      <div v-if="loading" class="state-card loading-card">
        <div class="spinner"></div>
        <div>
          <h3>Đang tải danh sách kỳ...</h3>
          <p>Hệ thống chỉ lấy kỳ và môn trong kỳ. Chi tiết syllabus tải riêng khi bấm Xem.</p>
        </div>
      </div>

      <div v-else-if="!chuongTrinhId" class="empty-card">
        Thiếu thông tin chương trình đào tạo để xem kỳ.
      </div>

      <div v-else-if="!danhSachKy.length" class="empty-card">
        Chương trình/version này chưa có kỳ học.
      </div>

      <div v-else class="semester-grid">
        <article
            v-for="(ky, index) in danhSachKy"
            :key="ky.rowKey"
            class="semester-card"
        >
          <header class="semester-header">
            <div class="semester-title">
              <p class="semester-eyebrow">Kỳ học</p>
              <h3>{{ layTenKyHienThi(ky, index) }}</h3>
            </div>

            <span class="subject-count">
              {{ ky.monHoc.length }} môn
            </span>
          </header>

          <div class="semester-summary">
            <span>TC: <b>{{ hienThi(ky.tongTinChi) }}</b></span>
            <span>Giờ: <b>{{ hienThi(ky.tongGio) }}</b></span>
            <span>Buổi: <b>{{ hienThi(ky.tongBuoiHoc) }}</b></span>
          </div>

          <div class="table-wrap">
            <table>
              <thead>
              <tr>
                <th class="col-subject">Môn học</th>
                <th>Buổi</th>
                <th>Giờ</th>
                <th>LT</th>
                <th>TH</th>
                <th>KT</th>
                <th>TC</th>
                <th>Syllabus</th>
              </tr>
              </thead>

              <tbody>
              <tr v-if="!ky.monHoc.length">
                <td colspan="8" class="empty-cell">
                  Kỳ này chưa có môn học.
                </td>
              </tr>

              <tr v-for="mon in ky.monHoc" v-else :key="mon.rowKey">
                <td class="subject-cell">
                  <strong>{{ mon.tenMon }}</strong>
                </td>

                <td>{{ hienThi(mon.soBuoiHoc) }}</td>
                <td>{{ hienThi(mon.tongGio) }}</td>
                <td>{{ hienThi(mon.gioLyThuyet) }}</td>
                <td>{{ hienThi(mon.gioThucHanh) }}</td>
                <td>{{ hienThi(mon.gioKiemTra) }}</td>
                <td>{{ hienThi(mon.soTinChi) }}</td>

                <td>
                  <button
                      v-if="mon.coSyllabus"
                      type="button"
                      class="link-btn"
                      @click="diDenTrangSyllabus(mon)"
                  >
                    Xem
                  </button>

                  <span v-else class="muted-text">
                    Chưa có
                  </span>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </article>
      </div>
    </section>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { daoTaoService } from '../../services/daoTaoService.js'
import { layThongBaoLoi } from '../../utils/layThongBaoLoi.js'

const route = useRoute()
const router = useRouter()

const TEN_ROUTE_TONG_QUAN = 'dao-tao-chuong-trinh-tong-quan'
const TEN_ROUTE_SYLLABUS = 'dao-tao-syllabus-mon-hoc-xem'

const loading = ref(false)
const errorMessage = ref('')
const thongBao = ref('')
const tongThe = ref(null)

const chuongTrinhId = computed(() => {
  return route.params.chuongTrinhId || route.query.chuongTrinhId || null
})

const versionId = computed(() => {
  return route.query.versionId || route.params.versionId || null
})

const cauTruc = computed(() => {
  return tongThe.value?.cauTruc || null
})

const chuongTrinh = computed(() => {
  return tongThe.value?.chuongTrinh || cauTruc.value?.chuongTrinh || null
})

const versionDangXem = computed(() => {
  return tongThe.value?.versionDangXem || cauTruc.value?.version || null
})

const tenChuongTrinh = computed(() => {
  return layTenChuongTrinh(chuongTrinh.value)
})

const tenVersion = computed(() => {
  return layTenVersion(versionDangXem.value)
})

const danhSachKy = computed(() => {
  const rawKy = layMangDauTien(cauTruc.value, ['khungKy', 'danhSachKy', 'ky', 'hocKyList'])

  if (rawKy.length) {
    return rawKy
        .map((item, index) => normalizeKy(item, index))
        .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
  }

  const monChuaPhanKy = layMangDauTien(cauTruc.value, ['monTrongChuongTrinh', 'danhSachMonHoc', 'monHocList'])

  if (monChuaPhanKy.length) {
    return [
      normalizeKy({
        id: 'chua-phan-ky',
        maKy: 'Chưa phân kỳ',
        tenKy: 'Chưa phân kỳ',
        moTa: 'Các môn chưa được gắn vào kỳ cụ thể.',
        monTrongKy: monChuaPhanKy
      }, 0)
    ]
  }

  return []
})

const tongSoMon = computed(() => {
  return danhSachKy.value.reduce((sum, ky) => sum + ky.monHoc.length, 0)
})

const tongTinChi = computed(() => {
  const total = danhSachKy.value.reduce((sum, ky) => sum + Number(ky.tongTinChi || 0), 0)
  return total > 0 ? total : null
})

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  if (!chuongTrinhId.value) {
    errorMessage.value = 'Thiếu thông tin chương trình đào tạo để xem kỳ.'
    return
  }

  loading.value = true
  errorMessage.value = ''
  thongBao.value = ''
  tongThe.value = null

  try {
    const res = await daoTaoService.chuongTrinh.getTongThe(chuongTrinhId.value, versionId.value || null, {
      khungKyId: route.query.khungKyId || null,
      includeSyllabusDetail: false
    })
    tongThe.value = layDataTuApiResponse(res)

    if (!tongThe.value?.coVersion && tongThe.value?.message) {
      thongBao.value = tongThe.value.message
    }
  } catch (error) {
    errorMessage.value = layThongBaoLoi(error, 'Không tải được danh sách kỳ của chương trình.')
  } finally {
    loading.value = false
  }
}

function quayLaiTongQuan() {
  router.push({ name: TEN_ROUTE_TONG_QUAN }).catch(() => {
    router.back()
  })
}

function diDenTrangSyllabus(mon) {
  if (!mon?.coSyllabus) return

  const refKey = `syllabus-mon-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`

  const payload = {
    tenMon: mon.tenMon,
    maMon: mon.maMon,
    soTinChi: mon.soTinChi,
    gioLyThuyet: mon.gioLyThuyet,
    gioThucHanh: mon.gioThucHanh,
    gioKiemTra: mon.gioKiemTra,
    soBuoiHoc: mon.soBuoiHoc,
    coSyllabus: mon.coSyllabus,
    syllabusId: mon.syllabusId,
    chuongTrinhMonId: mon.chuongTrinhMonId,
    syllabus: mon.syllabus || {}
  }

  sessionStorage.setItem(refKey, JSON.stringify(payload))

  router.push({
    name: TEN_ROUTE_SYLLABUS,
    query: {
      ref: refKey,
      syllabusMonHocId: mon.syllabusId || undefined
    }
  }).catch(() => {
    thongBao.value = 'Route xem syllabus môn học chưa được cấu hình.'
  })
}

function normalizeKy(item, index) {
  const ky = item?.khungKy || item || {}
  const monRaw = layMangDauTien(item, ['monTrongKy', 'monHoc', 'danhSachMonHoc', 'monHocList'])
  const monHoc = monRaw.map((mon, monIndex) => normalizeMon(mon, monIndex))

  const tongTinChi = tinhTong(monHoc, 'soTinChi')
  const tongGio = tinhTong(monHoc, 'tongGio')
  const tongBuoiHoc = tinhTong(monHoc, 'soBuoiHoc')

  return {
    rowKey: `ky-${layId(ky) || ky.maKy || index}`,
    raw: item,
    maKy: ky.maKy || ky.ma || '',
    tenKy: ky.tenKy || ky.ten || ky.maKy || `Kỳ ${index + 1}`,
    thuTu: ky.thuTu ?? index + 1,
    ghiChu: ky.moTa || ky.ghiChu || '',
    monHoc,
    tongTinChi: tongTinChi > 0 ? tongTinChi : null,
    tongGio: tongGio > 0 ? tongGio : null,
    tongBuoiHoc: tongBuoiHoc > 0 ? tongBuoiHoc : null
  }
}

function normalizeMon(item, index) {
  const chuongTrinhMon = item?.chuongTrinhMon || item || {}
  const monHoc = item?.monHoc || {}

  /*
    Chỉ lấy syllabus đã tạo mới hoặc đã gán vào môn trong chương trình/version.
    Không lấy syllabus từ monHoc gốc.
  */
  const syllabusList = layDanhSachSyllabusDaGan(item)
  const syllabusDauTien = syllabusList[0] || null
  const syllabus = laySyllabusApDungTuBanGhi(syllabusDauTien)
  const taiLieuDaGan = layTaiLieuTuSyllabusDaGan(syllabusDauTien, syllabus)
  const chuanDauRaDaGan = layChuanDauRaTuSyllabusDaGan(syllabusDauTien, syllabus)
  const cauHinhDanhGiaDaGan = layCauHinhDanhGiaTuSyllabusDaGan(syllabusDauTien, syllabus)
  const quyDoiDiemDaGan = layQuyDoiDiemTuSyllabusDaGan(syllabusDauTien, syllabus, item)
  const dieuKienDaGan = layDieuKienTuSyllabusDaGan(syllabusDauTien, syllabus)

  return {
    rowKey: `mon-${layId(chuongTrinhMon) || layId(monHoc) || index}`,
    raw: item,
    chuongTrinhMonId: layId(chuongTrinhMon),
    monHocId: layId(monHoc) || chuongTrinhMon.monHocId || null,
    syllabusId: layId(syllabus) || layId(syllabusDauTien),
    maMon: chuongTrinhMon.maMonHoc || monHoc.maMon || monHoc.ma || '',
    tenMon: chuongTrinhMon.tenMonHoc || monHoc.tenMon || monHoc.ten || chuongTrinhMon.ten || 'Chưa có tên môn',
    soBuoiHoc: syllabus?.soBuoiHoc ?? chuongTrinhMon.soBuoiHoc ?? null,
    tongGio: chuongTrinhMon.tongGio ?? tinhTongGioTuThanhPhan(chuongTrinhMon),
    gioLyThuyet: chuongTrinhMon.gioLyThuyet ?? null,
    gioThucHanh: chuongTrinhMon.gioThucHanh ?? null,
    gioKiemTra: chuongTrinhMon.gioKiemTra ?? null,
    soTinChi: chuongTrinhMon.soTinChi ?? syllabus?.soTinChi ?? null,
    coSyllabus: syllabusList.length > 0,
    syllabus,
    chuongBai: layMangDauTien(syllabusDauTien, ['chuongBai', 'danhSachChuongBai', 'noiDung', 'noiDungSyllabus']),
    taiLieu: taiLieuDaGan,
    chuanDauRa: chuanDauRaDaGan,
    cauHinhDanhGia: cauHinhDanhGiaDaGan,
    quyDoiDiem: quyDoiDiemDaGan,
    dieuKien: dieuKienDaGan
  }
}

function tinhTongGioTuThanhPhan(chuongTrinhMon) {
  const lt = Number(chuongTrinhMon.gioLyThuyet || 0)
  const th = Number(chuongTrinhMon.gioThucHanh || 0)
  const kt = Number(chuongTrinhMon.gioKiemTra || 0)
  const total = lt + th + kt

  return total > 0 ? total : null
}

function tinhTong(list, key) {
  return list.reduce((sum, item) => sum + Number(item[key] || 0), 0)
}

function layDanhSachSyllabusDaGan(item) {
  /*
    Chỉ đọc dữ liệu syllabus thuộc bản ghi môn trong chương trình/version.
    Tuyệt đối không đọc item.monHoc.syllabus, item.monHoc.syllabusMonHoc,
    hoặc dữ liệu syllabus môn học gốc.
  */
  const chuongTrinhMon = item?.chuongTrinhMon || {}

  const candidates = [
    item?.syllabusDaGan,
    item?.syllabusApDung,
    item?.syllabusMonApDung,
    item?.syllabusMonHocApDung,
    item?.syllabusChuongTrinhMon,
    item?.syllabusMonTrongChuongTrinh,
    item?.syllabusTheoChuongTrinh,
    item?.syllabusCuaChuongTrinhMon,
    item?.syllabusMonHoc,
    item?.syllabus,

    chuongTrinhMon?.syllabusDaGan,
    chuongTrinhMon?.syllabusApDung,
    chuongTrinhMon?.syllabusMonApDung,
    chuongTrinhMon?.syllabusMonHocApDung,
    chuongTrinhMon?.syllabusChuongTrinhMon,
    chuongTrinhMon?.syllabusMonTrongChuongTrinh,
    chuongTrinhMon?.syllabusTheoChuongTrinh,
    chuongTrinhMon?.syllabusCuaChuongTrinhMon,
    chuongTrinhMon?.syllabusMonHoc,
    chuongTrinhMon?.syllabus
  ]

  for (const value of candidates) {
    const normalized = normalizeObjectList(value)
    if (normalized.length) return normalized
  }

  return []
}

function laySyllabusApDungTuBanGhi(record) {
  if (!record) return null

  return (
      record.syllabusDaGan ||
      record.syllabusApDung ||
      record.syllabusMonApDung ||
      record.syllabusMonHocApDung ||
      record.syllabusChuongTrinhMon ||
      record.syllabusMonTrongChuongTrinh ||
      record.syllabusTheoChuongTrinh ||
      record.syllabusCuaChuongTrinhMon ||
      record.syllabusMonHoc ||
      record.syllabus ||
      record
  )
}

function layTaiLieuTuSyllabusDaGan(syllabusRecord, syllabus) {
  /*
    Chỉ lấy tài liệu nằm trong syllabus đã gán/tạo.
    Không lấy tài liệu từ môn học gốc.
  */
  const candidates = [
    syllabusRecord?.taiLieumau,
    syllabusRecord?.taiLieu,
    syllabusRecord?.taiLieuHocTap,
    syllabusRecord?.syllabusTaiLieu,
    syllabusRecord?.tepDinhKem,
    syllabusRecord?.tep,
    syllabusRecord?.files,
    syllabusRecord?.fileList,

    syllabus?.taiLieumau,
    syllabus?.taiLieu,
    syllabus?.taiLieuHocTap,
    syllabus?.syllabusTaiLieu,
    syllabus?.tepDinhKem,
    syllabus?.tep,
    syllabus?.files,
    syllabus?.fileList
  ]

  for (const value of candidates) {
    const normalized = normalizeObjectList(value)
    if (normalized.length) return normalized
  }

  return []
}

function layChuanDauRaTuSyllabusDaGan(syllabusRecord, syllabus) {
  /*
    Chỉ lấy chuẩn đầu ra theo syllabus môn học áp dụng trong chương trình.
    Không lấy chuẩn đầu ra từ syllabus môn học gốc.
  */
  const candidates = [
    syllabusRecord?.chuanDauRa,
    syllabusRecord?.chuanDauRaMonHoc,
    syllabusRecord?.nangLucDauRa,
    syllabusRecord?.learningOutcomes,
    syllabusRecord?.outcomes,

    syllabus?.chuanDauRa,
    syllabus?.chuanDauRaMonHoc,
    syllabus?.nangLucDauRa,
    syllabus?.learningOutcomes,
    syllabus?.outcomes
  ]

  for (const value of candidates) {
    const normalized = normalizeObjectList(value)
    if (normalized.length) return normalized
  }

  return []
}

function layCauHinhDanhGiaTuSyllabusDaGan(syllabusRecord, syllabus) {
  /*
    Cấu hình đánh giá thật thuộc syllabus_mon_hoc áp dụng theo chương trình.
    Không đọc cau_hinh_danh_gia_mau của syllabus gốc.
  */
  const candidates = [
    syllabusRecord?.cauHinhDanhGia,
    syllabusRecord?.cotDiem,
    syllabusRecord?.cotDiemSyllabus,
    syllabusRecord?.danhSachCotDiem,
    syllabusRecord?.evaluationColumns,

    syllabus?.cauHinhDanhGia,
    syllabus?.cotDiem,
    syllabus?.cotDiemSyllabus,
    syllabus?.danhSachCotDiem,
    syllabus?.evaluationColumns
  ]

  for (const value of candidates) {
    const normalized = normalizeObjectList(value)
    if (normalized.length) return normalized
  }

  return []
}

function layQuyDoiDiemTuSyllabusDaGan(syllabusRecord, syllabus, monTrongKyRecord = null) {
  /*
    Quy đổi điểm theo chương trình/syllabus áp dụng.
    Không đọc quy_doi_diem_mau của syllabus gốc.
  */
  const rawRecord = monTrongKyRecord || syllabusRecord?.raw || {}
  const rawChuongTrinhMon = rawRecord?.chuongTrinhMon || {}

  const candidates = [
    rawRecord?.quyDoiDiem,
    rawRecord?.quyDoiDiemTheoChuongTrinh,
    rawRecord?.chuongTrinhMonQuyDoiDiemMau,
    rawChuongTrinhMon?.quyDoiDiem,
    rawChuongTrinhMon?.quyDoiDiemTheoChuongTrinh,
    rawChuongTrinhMon?.chuongTrinhMonQuyDoiDiemMau,
    syllabusRecord?.quyDoiDiem,
    syllabusRecord?.quyDoiDiemTheoChuongTrinh,
    syllabusRecord?.chuongTrinhMonQuyDoiDiemMau,
    syllabusRecord?.quyDoiKetQua,
    syllabusRecord?.bangQuyDoiDiem,

    syllabus?.quyDoiDiem,
    syllabus?.quyDoiDiemTheoChuongTrinh,
    syllabus?.chuongTrinhMonQuyDoiDiemMau,
    syllabus?.quyDoiKetQua,
    syllabus?.bangQuyDoiDiem
  ]

  for (const value of candidates) {
    const normalized = normalizeObjectList(value)
    if (normalized.length) return normalized
  }

  return []
}

function layDieuKienTuSyllabusDaGan(syllabusRecord, syllabus) {
  /*
    Điều kiện của syllabus áp dụng theo chương trình.
    Không đọc điều kiện gốc/mẫu.
  */
  const candidates = [
    syllabusRecord?.dieuKien,
    syllabusRecord?.dieuKienMonHoc,
    syllabusRecord?.syllabusMonHocDieuKien,
    syllabusRecord?.dieuKienSyllabus,
    syllabusRecord?.conditions,

    syllabus?.dieuKien,
    syllabus?.dieuKienMonHoc,
    syllabus?.syllabusMonHocDieuKien,
    syllabus?.dieuKienSyllabus,
    syllabus?.conditions
  ]

  for (const value of candidates) {
    const normalized = normalizeObjectList(value)
    if (normalized.length) return normalized
  }

  return []
}

function normalizeObjectList(value) {
  if (!value) return []
  if (Array.isArray(value)) return value.filter(Boolean)
  if (typeof value === 'object') return [value]
  return []
}

function layMangDauTien(item, keys = []) {
  if (!item) return []

  for (const key of keys) {
    if (Array.isArray(item[key])) return item[key]
  }

  return []
}

function layDataTuApiResponse(res) {
  if (res?.success !== undefined && res?.data !== undefined) return res.data
  if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
  if (res?.data !== undefined) return res.data
  return res
}

function layTenChuongTrinh(item) {
  if (!item) return 'Chương trình đào tạo'
  return item.tenChuongTrinh || item.ten || item.name || item.maChuongTrinh || 'Chương trình đào tạo'
}

function layTenVersion(item) {
  if (!item) return 'Backend tự chọn version phù hợp'

  const ten = item.tenVersion || item.ten || ''
  const ma = item.maVersion || item.ma || ''

  if (ten) return ten
  if (ma) return ma

  return 'Version chưa đặt tên'
}

function layTenKyHienThi(ky, index) {
  return ky.tenKy || ky.maKy || `Kỳ ${index + 1}`
}

function layId(item) {
  return item?.id ?? item?.value ?? null
}

function hienThi(value) {
  if (value === null || value === undefined || value === '') return '—'
  return value
}
</script>

<style scoped>
.dao-tao-xem-ky-page {
  --primary: #077149;
  --primary-dark: #045f3c;
  --primary-deep: #034d31;
  --primary-light: #149565;
  --primary-soft: #e8f6ef;
  --primary-soft-2: #f5fcf8;
  --primary-border: #a8dbc4;
  --primary-border-2: #d8ece2;
  --text-main: #102018;
  --text-muted: #5f7469;
  --surface: #ffffff;
  --danger-bg: #fef2f2;
  --danger-border: #fecaca;
  --danger-text: #991b1b;
  --warning-bg: #fff9e8;
  --warning-border: #f4d37f;
  --warning-text: #875c09;

  min-height: calc(100vh - var(--header-height, 60px));
  padding: 8px 10px 14px;
  background:
      radial-gradient(circle at top left, rgba(7, 113, 73, 0.08), transparent 28%),
      linear-gradient(180deg, #f5fbf8 0%, #eef8f3 100%);
  color: var(--text-main);
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

/* =========================
   BASE
========================= */

.page-header,
.state-card,
.empty-card,
.semester-card,
.overview-strip article {
  border: 1px solid var(--primary-border-2);
  border-radius: 12px;
  background: var(--surface);
  box-shadow: 0 6px 16px rgba(7, 113, 73, 0.055);
}

/* =========================
   COMPACT HEADER
========================= */

.page-header {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: start;
  gap: 10px;
  padding: 9px 11px;
  margin-bottom: 7px;
  background: linear-gradient(135deg, #ffffff 0%, #eef9f4 100%);
}

.page-title {
  min-width: 0;
}

.top-line {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 10px;
  margin-bottom: 5px;
}

.back-btn {
  border: 0;
  background: transparent;
  color: var(--primary-dark);
  font-size: 10px;
  font-weight: 900;
  cursor: pointer;
  padding: 0;
  line-height: 1.2;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.back-btn:hover {
  color: var(--primary);
  text-decoration: underline;
}

.eyebrow {
  margin: 0 0 2px;
  color: var(--primary);
  font-size: 9px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.page-header h1 {
  margin: 0;
  color: var(--text-main);
  font-size: 16px;
  line-height: 1.22;
  font-weight: 900;
  word-break: break-word;
}

.subtitle {
  margin: 3px 0 0;
  color: var(--text-muted);
  font-size: 10px;
  line-height: 1.3;
}

.version-pill {
  flex-shrink: 0;
  max-width: 300px;
  min-height: 21px;
  border: 1px solid var(--primary-border);
  border-radius: 999px;
  background: #ffffff;
  color: var(--primary-dark);
  padding: 3px 8px;
  font-size: 9px;
  font-weight: 800;
  line-height: 1.2;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.version-pill strong {
  font-weight: 900;
}

.header-actions {
  display: flex;
  align-items: flex-start;
  flex-shrink: 0;
}

.btn {
  min-height: 27px;
  border: 1px solid var(--primary-border);
  border-radius: 8px;
  background: #ffffff;
  color: var(--primary-dark);
  padding: 5px 10px;
  font-size: 10px;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease, box-shadow 0.15s ease;
}

.btn:hover {
  border-color: var(--primary);
  background: var(--primary-soft);
  color: var(--primary);
  box-shadow: 0 3px 10px rgba(7, 113, 73, 0.12);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: var(--primary);
  background: var(--primary);
  color: #ffffff;
}

.btn.primary:hover {
  border-color: var(--primary-dark);
  background: var(--primary-dark);
}

/* =========================
   NOTICE / STATE
========================= */

.notice-card {
  margin-bottom: 7px;
  padding: 7px 9px;
  border: 1px solid var(--warning-border);
  border-radius: 10px;
  background: var(--warning-bg);
  color: var(--warning-text);
  font-size: 11px;
  font-weight: 800;
}

.state-card {
  display: flex;
  align-items: center;
  gap: 9px;
  padding: 10px;
}

.state-card h3 {
  margin: 0 0 3px;
  color: var(--text-main);
  font-size: 13px;
}

.state-card p {
  margin: 0;
  color: var(--text-muted);
  font-size: 11px;
  line-height: 1.35;
}

.state-card.error {
  display: block;
  margin-bottom: 7px;
  border-color: var(--danger-border);
  background: var(--danger-bg);
}

.state-card.error p {
  margin-bottom: 8px;
  color: var(--danger-text);
}

.loading-card {
  min-height: 70px;
}

.empty-card {
  padding: 12px;
  color: var(--text-muted);
  font-size: 11px;
  font-weight: 800;
  text-align: center;
}

.spinner {
  width: 22px;
  height: 22px;
  border: 3px solid #d9f0e5;
  border-top-color: var(--primary);
  border-radius: 999px;
  animation: spin 0.8s linear infinite;
  flex-shrink: 0;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}

/* =========================
   COMPACT STATS
========================= */

.overview-strip {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 7px;
  margin-bottom: 8px;
}

.overview-strip article {
  min-height: 33px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  padding: 5px 10px;
  background: #ffffff;
}

.overview-strip span {
  color: var(--text-muted);
  font-size: 10px;
  font-weight: 900;
}

.overview-strip strong {
  color: var(--primary);
  font-size: 17px;
  line-height: 1;
  font-weight: 900;
}

/* =========================
   SECTION TITLE
========================= */

.semester-section {
  display: grid;
  gap: 8px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: flex-start;
}

.section-title h2 {
  margin: 0;
  color: var(--text-main);
  font-size: 14px;
  font-weight: 900;
}

.section-title p {
  margin: 2px 0 0;
  color: var(--text-muted);
  font-size: 10px;
}

.badge,
.subject-count {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  font-weight: 900;
  white-space: nowrap;
}

.badge.neutral {
  min-height: 20px;
  border: 1px solid var(--primary-border);
  background: var(--primary-soft-2);
  color: var(--primary-dark);
  padding: 2px 7px;
  font-size: 9px;
}

/* =========================
   SEMESTER GRID
========================= */

.semester-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
  align-items: start;
}

.semester-card {
  min-width: 0;
  overflow: hidden;
  border-color: var(--primary-border);
  background: #ffffff;
  box-shadow: 0 7px 18px rgba(7, 113, 73, 0.07);
  transition: border-color 0.15s ease, box-shadow 0.15s ease, transform 0.15s ease;
}

.semester-card:hover {
  transform: translateY(-1px);
  border-color: var(--primary);
  box-shadow: 0 10px 22px rgba(7, 113, 73, 0.105);
}

.semester-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 8px;
  padding: 7px 9px;
  background: linear-gradient(135deg, var(--primary-deep) 0%, var(--primary) 68%, var(--primary-light) 100%);
}

.semester-title {
  min-width: 0;
}

.semester-eyebrow {
  margin: 0 0 2px;
  color: #dff6eb;
  font-size: 8px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.semester-header h3 {
  margin: 0;
  color: #ffffff;
  font-size: 12px;
  line-height: 1.2;
  font-weight: 900;
  word-break: break-word;
}

.subject-count {
  flex-shrink: 0;
  min-height: 20px;
  border: 1px solid rgba(255, 255, 255, 0.84);
  background: #ffffff;
  color: var(--primary);
  padding: 2px 7px;
  font-size: 9px;
}

/* =========================
   SEMESTER SUMMARY
========================= */

.semester-summary {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  border-bottom: 1px solid var(--primary-border-2);
  background: var(--primary-soft-2);
}

.semester-summary span {
  padding: 4px 5px;
  color: var(--primary-dark);
  font-size: 9px;
  font-weight: 900;
  text-align: center;
  line-height: 1.2;
}

.semester-summary span + span {
  border-left: 1px solid var(--primary-border-2);
}

.semester-summary b {
  color: var(--text-main);
}

/* =========================
   TABLE
========================= */

.table-wrap {
  width: 100%;
  overflow-x: auto;
  background: #ffffff;
}

table {
  width: 100%;
  min-width: 640px;
  border-collapse: collapse;
  font-size: 10px;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

th,
td {
  padding: 4px 5px;
  border-top: 1px solid #e7f2ec;
  border-right: 1px solid #e7f2ec;
  text-align: center;
  vertical-align: middle;
}

th:last-child,
td:last-child {
  border-right: 0;
}

thead th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f4fbf8;
  color: var(--primary-dark);
  font-size: 9px;
  font-weight: 900;
  white-space: nowrap;
}

td {
  color: var(--text-main);
}

tbody tr:hover {
  background: #f8fdfb;
}

.col-subject {
  width: 220px;
  text-align: left;
}

.subject-cell {
  text-align: left;
}

.subject-cell strong {
  display: -webkit-box;
  color: var(--text-main);
  line-height: 1.25;
  font-weight: 900;
  word-break: break-word;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.link-btn {
  min-height: 21px;
  border: 1px solid var(--primary-border);
  border-radius: 6px;
  background: #ffffff;
  color: var(--primary-dark);
  padding: 2px 7px;
  font-size: 9px;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease;
}

.link-btn:hover {
  border-color: var(--primary);
  background: var(--primary-soft);
  color: var(--primary);
}

.muted-text {
  color: #7a8b83;
  font-size: 9px;
  font-weight: 800;
  white-space: nowrap;
}

.empty-cell {
  color: var(--text-muted);
  text-align: center;
  font-size: 10px;
  font-weight: 800;
}

/* =========================
   RESPONSIVE
========================= */

@media (max-width: 1080px) {
  .semester-grid {
    grid-template-columns: 1fr;
  }

  table {
    min-width: 700px;
  }
}

@media (max-width: 860px) {
  .page-header {
    grid-template-columns: 1fr;
  }

  .top-line {
    align-items: flex-start;
  }

  .version-pill {
    max-width: 100%;
  }

  .header-actions {
    width: 100%;
  }

  .overview-strip {
    grid-template-columns: 1fr;
  }

  .section-title {
    flex-direction: column;
  }
}

@media (max-width: 640px) {
  .dao-tao-xem-ky-page {
    padding: 8px;
  }

  .page-header h1 {
    font-size: 15px;
  }

  .semester-summary {
    grid-template-columns: 1fr;
  }

  .semester-summary span + span {
    border-left: 0;
    border-top: 1px solid var(--primary-border-2);
  }

  table {
    min-width: 700px;
  }
}
</style>