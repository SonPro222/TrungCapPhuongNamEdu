<template>
  <div class="dao-tao-xem-ky-page">
    <section class="page-header">
      <div>
        <button type="button" class="back-btn" @click="quayLaiTongQuan">
          ← Quay lại tổng quan
        </button>

        <p class="eyebrow">Chương trình đào tạo</p>
        <h1>{{ tenChuongTrinh }}</h1>
        <p class="version-text">
          Version: <strong>{{ tenVersion }}</strong>
        </p>
        <p class="subtitle">
          Mỗi block là một kỳ, mỗi dòng trong bảng là một môn học thuộc kỳ đó.
        </p>
      </div>

      <div class="header-actions">
        <button type="button" class="btn" :disabled="loading || !chuongTrinhId" @click="taiDuLieu">
          Tải lại
        </button>
      </div>
    </section>

    <section v-if="errorMessage" class="state-card error">
      <h3>Không tải được dữ liệu</h3>
      <p>{{ errorMessage }}</p>
      <button type="button" class="btn primary" :disabled="!chuongTrinhId" @click="taiDuLieu">
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
          <p>Hệ thống chỉ lấy kỳ và môn trong kỳ. Chi tiết syllabus sẽ tải riêng khi bấm Xem syllabus.</p>
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
            <div>
              <p class="semester-eyebrow">Kỳ học</p>
              <h3>{{ layTenKyHienThi(ky, index) }}</h3>
            </div>

            <span class="subject-count">
              {{ ky.monHoc.length }} môn
            </span>
          </header>

          <div class="semester-summary">
            <span>Tín chỉ: <b>{{ hienThi(ky.tongTinChi) }}</b></span>
            <span>Tổng giờ: <b>{{ hienThi(ky.tongGio) }}</b></span>
            <span>Buổi học: <b>{{ hienThi(ky.tongBuoiHoc) }}</b></span>
          </div>

          <div class="table-wrap">
            <table>
              <thead>
              <tr>
                <th class="col-subject">Môn học</th>
                <th>Buổi</th>
                <th>Tổng giờ</th>
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
                    Xem syllabus
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
  min-height: calc(100vh - var(--header-height, 60px));
  padding: 12px 16px 20px;
  background: #f1f7ff;
  color: #0f172a;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

/* =========================
   HEADER / STATE / CARD BASE
========================= */

.page-header,
.state-card,
.empty-card,
.semester-card,
.overview-strip article {
  border: 1px solid #bfdbfe;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(15, 82, 143, 0.10);
}

.page-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  padding: 18px;
  margin-bottom: 12px;
  background: linear-gradient(135deg, #ffffff 0%, #eaf4ff 100%);
}

.back-btn {
  margin-bottom: 10px;
  border: 0;
  background: transparent;
  color: #0b5a92;
  font-size: 13px;
  font-weight: 800;
  cursor: pointer;
  padding: 0;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.back-btn:hover {
  color: #08456f;
  text-decoration: underline;
}

.eyebrow {
  margin: 0 0 4px;
  color: #0b5a92;
  font-size: 12px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.page-header h1 {
  margin: 0;
  color: #0f172a;
  font-size: 22px;
  line-height: 1.3;
  font-weight: 700;
}

.version-text {
  margin: 6px 0 0;
  color: #0f3d64;
  font-size: 13px;
}

.subtitle {
  margin: 6px 0 0;
  color: #42637f;
  font-size: 13px;
  line-height: 1.45;
}

.header-actions {
  display: flex;
  flex-shrink: 0;
}

.btn {
  min-height: 32px;
  border: 1px solid #9fc5e8;
  border-radius: 12px;
  background: #ffffff;
  color: #0f3d64;
  padding: 7px 12px;
  font-size: 12px;
  font-weight: 800;
  cursor: pointer;
  white-space: nowrap;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease, box-shadow 0.15s ease;
}

.btn:hover {
  border-color: #0b5a92;
  background: #eaf4ff;
  color: #0b5a92;
  box-shadow: 0 3px 10px rgba(15, 82, 143, 0.12);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary {
  border-color: #0b5a92;
  background: #0b5a92;
  color: #ffffff;
}

.btn.primary:hover {
  border-color: #08456f;
  background: #08456f;
  color: #ffffff;
}

/* =========================
   NOTICE / OVERVIEW
========================= */

.notice-card {
  margin-bottom: 12px;
  padding: 10px 12px;
  border: 1px solid #fde68a;
  border-radius: 14px;
  background: #fffbeb;
  color: #92400e;
  font-size: 13px;
}

.overview-strip {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 14px;
}

.overview-strip article {
  padding: 14px;
  background: linear-gradient(135deg, #ffffff 0%, #f8fbff 100%);
}

.overview-strip span {
  display: block;
  color: #42637f;
  font-size: 12px;
  font-weight: 800;
}

.overview-strip strong {
  display: block;
  margin-top: 4px;
  color: #0b5a92;
  font-size: 21px;
  font-weight: 900;
}

/* =========================
   SECTION TITLE
========================= */

.semester-section {
  display: grid;
  gap: 14px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.section-title h2 {
  margin: 0;
  color: #0f172a;
  font-size: 17px;
  font-weight: 700;
}

.section-title p {
  margin: 4px 0 0;
  color: #42637f;
  font-size: 12px;
}

.badge,
.subject-count {
  display: inline-flex;
  align-items: center;
  min-height: 23px;
  border-radius: 999px;
  padding: 3px 9px;
  font-size: 11px;
  font-weight: 900;
  white-space: nowrap;
}

.badge.neutral,
.subject-count {
  border: 1px solid #b7d3ec;
  background: #f8fbff;
  color: #0f3d64;
}

/* =========================
   KỲ GRID
========================= */

.semester-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 16px;
  align-items: start;
}

.semester-card {
  min-width: 0;
  overflow: hidden;
  border-color: #9fc5e8;
  background: #ffffff;
  box-shadow: 0 10px 28px rgba(15, 82, 143, 0.13);
  transition: transform 0.15s ease, box-shadow 0.15s ease, border-color 0.15s ease;
}

.semester-card:hover {
  transform: translateY(-2px);
  border-color: #0b5a92;
  box-shadow: 0 16px 34px rgba(15, 82, 143, 0.18);
}

.semester-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 10px;
  padding: 16px;
  border-bottom: 1px solid #9fc5e8;
  border-radius: 18px 18px 0 0;
  background: linear-gradient(135deg, #0b5a92 0%, #106ba8 55%, #2d93d1 100%);
}

.semester-eyebrow {
  margin: 0 0 4px;
  color: #d8ecff;
  font-size: 11px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.semester-header h3 {
  margin: 0;
  color: #ffffff;
  font-size: 17px;
  line-height: 1.35;
  font-weight: 700;
  word-break: break-word;
}

.subject-count {
  border-color: #bfdbfe;
  background: #ffffff;
  color: #0b5a92;
}

/* =========================
   KỲ SUMMARY
========================= */

.semester-summary {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  border-bottom: 1px solid #bfdbfe;
  background: #eaf4ff;
}

.semester-summary span {
  padding: 8px;
  color: #0f3d64;
  font-size: 11px;
  font-weight: 700;
  text-align: center;
}

.semester-summary span + span {
  border-left: 1px solid #bfdbfe;
}

.semester-summary b {
  color: #0f172a;
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
  min-width: 760px;
  border-collapse: collapse;
  font-size: 12px;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

th,
td {
  padding: 8px 9px;
  border-top: 1px solid #e5effa;
  border-right: 1px solid #e5effa;
  text-align: center;
  vertical-align: top;
}

th:last-child,
td:last-child {
  border-right: 0;
}

thead th {
  background: #f8fbff;
  color: #0f3d64;
  font-size: 11px;
  font-weight: 900;
  white-space: nowrap;
}

td {
  color: #0f172a;
}

tbody tr:hover {
  background: #f8fbff;
}

.col-subject {
  width: 190px;
  text-align: left;
}

.subject-cell {
  text-align: left;
}

.subject-cell strong {
  color: #0f172a;
  line-height: 1.35;
  word-break: break-word;
}

.link-btn {
  border: 0;
  background: transparent;
  color: #0b5a92;
  padding: 0;
  font-size: 12px;
  font-weight: 800;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.link-btn:hover {
  color: #08456f;
  text-decoration: underline;
}

.muted-text {
  color: #64748b;
  font-size: 12px;
  font-weight: 700;
}

.empty-cell {
  color: #64748b;
  text-align: center;
}

/* =========================
   STATE
========================= */

.state-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
}

.state-card h3 {
  margin: 0 0 4px;
  color: #0f172a;
  font-size: 16px;
}

.state-card p {
  margin: 0;
  color: #42637f;
  font-size: 13px;
  line-height: 1.45;
}

.state-card.error {
  display: block;
  margin-bottom: 12px;
  border-color: #fecaca;
  background: #fef2f2;
}

.state-card.error p {
  margin-bottom: 10px;
  color: #991b1b;
}

.loading-card {
  min-height: 90px;
}

.empty-card {
  padding: 18px;
  color: #42637f;
  font-size: 13px;
  text-align: center;
}

.spinner {
  width: 26px;
  height: 26px;
  border: 3px solid #dbeafe;
  border-top-color: #0b5a92;
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
   RESPONSIVE
========================= */

@media (max-width: 860px) {
  .page-header,
  .section-title {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
  }

  .overview-strip {
    grid-template-columns: 1fr;
  }

  .semester-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .dao-tao-xem-ky-page {
    padding: 10px;
  }

  .semester-grid {
    grid-template-columns: 1fr;
  }

  .page-header h1 {
    font-size: 19px;
  }

  .semester-summary {
    grid-template-columns: 1fr;
  }

  .semester-summary span + span {
    border-left: 0;
    border-top: 1px solid #bfdbfe;
  }
}
</style>