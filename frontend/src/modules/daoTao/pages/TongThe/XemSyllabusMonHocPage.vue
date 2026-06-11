<template>
  <div class="dao-tao-xem-syllabus-page">
    <section class="page-header">
      <div>
        <button type="button" class="back-btn" @click="quayLai">
          ← Quay lại
        </button>

        <p class="eyebrow">Syllabus môn học</p>
        <h1>{{ tenMonHoc }}</h1>
        <p class="subtitle">
          Thông tin syllabus của môn học. Các mã kỹ thuật nội bộ không được hiển thị trên giao diện.
        </p>
      </div>
    </section>

    <section v-if="errorMessage" class="state-card error">
      <h3>Không tải được syllabus</h3>
      <p>{{ errorMessage }}</p>
      <button type="button" class="btn primary" @click="quayLai">
        Quay lại
      </button>
    </section>

    <section v-else-if="loading" class="state-card loading-card">
      <h3>Đang tải chi tiết syllabus...</h3>
      <p>Hệ thống đang lấy chuẩn đầu ra, tài liệu, cấu hình đánh giá, quy đổi điểm và điều kiện.</p>
    </section>

    <section v-else-if="!payload" class="state-card empty">
      <h3>Không có dữ liệu syllabus</h3>
      <p>Vui lòng quay lại trang kỳ và chọn lại môn học cần xem syllabus.</p>
      <button type="button" class="btn primary" @click="quayLai">
        Quay lại
      </button>
    </section>

    <template v-else>
      <section class="summary-card">
        <div class="summary-main">
          <p class="eyebrow">Môn học</p>
          <h2>{{ tenMonHoc }}</h2>

          <div class="summary-badges">
            <span :class="['status-badge', coSyllabus ? 'success' : 'muted']">
              {{ coSyllabus ? 'Có syllabus' : 'Chưa có syllabus' }}
            </span>

            <span v-if="payload.maMon" class="status-badge neutral">
              Mã môn: {{ payload.maMon }}
            </span>
          </div>
        </div>

        <div class="metric-grid">
          <article>
            <span>Số tín chỉ</span>
            <strong>{{ hienThi(payload.soTinChi) }}</strong>
          </article>

          <article>
            <span>Lý thuyết</span>
            <strong>{{ hienThi(payload.gioLyThuyet) }}</strong>
          </article>

          <article>
            <span>Thực hành</span>
            <strong>{{ hienThi(payload.gioThucHanh) }}</strong>
          </article>

          <article>
            <span>Kiểm tra</span>
            <strong>{{ hienThi(payload.gioKiemTra) }}</strong>
          </article>

          <article>
            <span>Số buổi học</span>
            <strong>{{ hienThi(payload.soBuoiHoc) }}</strong>
          </article>
        </div>
      </section>

      <section v-if="!coSyllabus" class="state-card empty">
        <h3>Môn học này chưa có syllabus</h3>
        <p>Hiện chưa có nội dung syllabus áp dụng cho môn học này.</p>
      </section>

      <section v-else class="content-grid">
        <article class="info-card">
          <h3>Thông tin syllabus</h3>

          <dl class="info-list">
            <template v-for="item in thongTinSyllabus" :key="item.label">
              <dt>{{ item.label }}</dt>
              <dd>{{ item.value }}</dd>
            </template>
          </dl>

          <div v-if="!thongTinSyllabus.length" class="empty-inline">
            Chưa có thông tin mô tả syllabus.
          </div>
        </article>

        <article class="info-card">
          <h3>Mục tiêu môn học</h3>
          <div v-if="mucTieuMonHoc" class="rich-text">
            {{ mucTieuMonHoc }}
          </div>
          <div v-else class="empty-inline">
            Chưa có mục tiêu môn học.
          </div>
        </article>

        <article class="info-card full">
          <h3>Nội dung syllabus / chương bài</h3>

          <div v-if="!danhSachChuongBai.length" class="empty-inline">
            Chưa có nội dung chương bài.
          </div>

          <div v-else class="chapter-list">
            <article v-for="(chuong, index) in danhSachChuongBai" :key="index" class="chapter-card">
              <div class="chapter-head">
                <strong>{{ layTenChuong(chuong, index) }}</strong>
                <span v-if="laySoTietChuong(chuong)">Số tiết: {{ laySoTietChuong(chuong) }}</span>
              </div>

              <p v-if="layNoiDungChuong(chuong)">
                {{ layNoiDungChuong(chuong) }}
              </p>
            </article>
          </div>
        </article>

        <article class="info-card full">
          <h3>Chuẩn đầu ra</h3>

          <div v-if="!danhSachChuanDauRa.length" class="empty-inline">
            Chưa có chuẩn đầu ra theo syllabus áp dụng.
          </div>

          <div v-else class="table-wrap">
            <table>
              <thead>
              <tr>
                <th>STT</th>
                <th>Mã</th>
                <th>Chuẩn đầu ra</th>
                <th>Mức độ</th>
                <th>Ghi chú</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in danhSachChuanDauRa" :key="`cdr-${index}`">
                <td>{{ index + 1 }}</td>
                <td>{{ hienThi(layTruong(item, ['ma', 'maChuanDauRa', 'code'])) }}</td>
                <td>{{ hienThiNoiDung(item) }}</td>
                <td>{{ hienThi(layTruong(item, ['mucDo', 'capDo', 'level'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['ghiChu', 'moTa'])) }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </article>

        <article class="info-card full">
          <h3>Cấu hình đánh giá</h3>

          <div v-if="!danhSachCauHinhDanhGia.length" class="empty-inline">
            Chưa có cấu hình đánh giá theo syllabus áp dụng.
          </div>

          <div v-else>
            <div :class="['table-note', tongTyLeDanhGia === 100 ? 'success' : 'warning']">
              Tổng tỷ lệ: <strong>{{ tongTyLeDanhGia }}%</strong>
              <span v-if="tongTyLeDanhGia !== 100"> - Chưa đủ 100%.</span>
            </div>

            <div class="table-wrap">
              <table>
                <thead>
                <tr>
                  <th>STT</th>
                  <th>Tên cột điểm</th>
                  <th>Loại điểm</th>
                  <th>Tỷ lệ %</th>
                  <th>Điểm tối đa</th>
                  <th>Bắt buộc</th>
                  <th>Ghi chú</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(item, index) in danhSachCauHinhDanhGia" :key="`dg-${index}`">
                  <td>{{ layThuTu(item, index) }}</td>
                  <td>{{ hienThi(layTruong(item, ['tenCotDiem', 'ten', 'ma'])) }}</td>
                  <td>{{ hienThi(layTruong(item, ['loaiDiem', 'loai', 'loaiMau'])) }}</td>
                  <td>{{ hienThi(layTruong(item, ['tyLe', 'tiLe'])) }}</td>
                  <td>{{ hienThi(layTruong(item, ['diemToiDa', 'diemMax'])) }}</td>
                  <td>{{ hienThiBoolean(layTruong(item, ['batBuoc', 'required'])) }}</td>
                  <td>{{ hienThi(layTruong(item, ['ghiChu', 'moTa'])) }}</td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </article>

        <article class="info-card full">
          <h3>Quy đổi điểm theo chương trình</h3>

          <div v-if="!danhSachQuyDoiDiem.length" class="empty-inline">
            Chưa có quy đổi điểm theo chương trình/syllabus áp dụng.
          </div>

          <div v-else class="table-wrap">
            <table>
              <thead>
              <tr>
                <th>STT</th>
                <th>Mã</th>
                <th>Tên</th>
                <th>Ngưỡng từ</th>
                <th>Ngưỡng đến</th>
                <th>Điểm quy đổi</th>
                <th>Kết quả</th>
                <th>Công thức</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in danhSachQuyDoiDiem" :key="`qdd-${index}`">
                <td>{{ layThuTu(item, index) }}</td>
                <td>{{ hienThi(layTruong(item, ['ma', 'code'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['ten', 'tenMau', 'moTa'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['nguongTu', 'diemTu', 'tuDiem'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['nguongDen', 'diemDen', 'denDiem'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['diemQuyDoi', 'diemHe4', 'diem'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['ketQua', 'xepLoai', 'trangThai'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['congThuc', 'ghiChu'])) }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </article>

        <article class="info-card full">
          <h3>Điều kiện của syllabus</h3>

          <div v-if="!danhSachDieuKien.length" class="empty-inline">
            Chưa có điều kiện theo syllabus áp dụng.
          </div>

          <div v-else class="table-wrap">
            <table>
              <thead>
              <tr>
                <th>STT</th>
                <th>Loại</th>
                <th>Tên/Nội dung</th>
                <th>Số lượng</th>
                <th>Yêu cầu</th>
                <th>Ghi chú</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in danhSachDieuKien" :key="`dk-${index}`">
                <td>{{ layThuTu(item, index) }}</td>
                <td>{{ hienThi(layTruong(item, ['loai', 'loaiDieuKien', 'type'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['ten', 'noiDung', 'moTa'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['soLuong', 'quantity'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['yeuCau', 'requirement'])) }}</td>
                <td>{{ hienThi(layTruong(item, ['ghiChu'])) }}</td>
              </tr>
              </tbody>
            </table>
          </div>
        </article>

        <article class="info-card">
          <h3>Tài liệu học tập</h3>

          <div v-if="!danhSachTaiLieu.length" class="empty-inline">
            Chưa có tài liệu học tập theo syllabus áp dụng.
          </div>

          <div v-else class="document-list">
            <article
                v-for="(taiLieu, index) in danhSachTaiLieu"
                :key="index"
                class="document-item"
            >
              <div class="document-info">
                <strong>{{ layTenTaiLieu(taiLieu, index) }}</strong>
                <span v-if="layLinkTaiLieu(taiLieu)">
        Có file/link đính kèm
      </span>
                <span v-else>
        Chưa có đường dẫn file
      </span>
              </div>

              <div class="document-actions">
                <button
                    type="button"
                    class="btn"
                    :disabled="!layLinkTaiLieu(taiLieu)"
                    @click="xemTaiLieu(taiLieu)"
                >
                  Xem
                </button>

                <button
                    type="button"
                    class="btn primary"
                    :disabled="!layLinkTaiLieu(taiLieu)"
                    @click="taiTaiLieu(taiLieu)"
                >
                  Tải
                </button>
              </div>
            </article>
          </div>
        </article>

        <article class="info-card">
          <h3>File / link syllabus</h3>

          <div v-if="!linkSyllabus" class="empty-inline">
            Chưa có file hoặc link syllabus.
          </div>

          <button v-else type="button" class="btn primary" @click="moLienKet(linkSyllabus)">
            Xem / tải syllabus
          </button>
        </article>
      </section>
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { daoTaoService } from '../../services/daoTaoService.js'
import { layThongBaoLoi } from '../../utils/layThongBaoLoi.js'

const route = useRoute()
const router = useRouter()

const payload = ref(null)
const errorMessage = ref('')
const loading = ref(false)

const tenMonHoc = computed(() => {
  return payload.value?.tenMon || 'Syllabus môn học'
})

const coSyllabus = computed(() => {
  return Boolean(payload.value?.coSyllabus)
})

const syllabus = computed(() => {
  return payload.value?.syllabus || {}
})

const mucTieuMonHoc = computed(() => {
  return layTruong(syllabus.value, [
    'mucTieu',
    'mucTieuMonHoc',
    'mucTieuHocPhan',
    'objectives'
  ])
})

const thongTinSyllabus = computed(() => {
  const item = syllabus.value || {}

  return [
    { label: 'Tên syllabus', value: layTruong(item, ['tenSyllabus', 'ten', 'tieuDe']) },
    { label: 'Mô tả', value: layTruong(item, ['moTa', 'description']) },
    { label: 'Vị trí môn học', value: layTruong(item, ['viTri', 'viTriMonHoc']) },
    { label: 'Tính chất', value: layTruong(item, ['tinhChat']) },
    { label: 'Phương pháp đánh giá', value: layTruong(item, ['phuongPhapDanhGia', 'danhGia']) },
    { label: 'Điều kiện hoàn thành', value: layTruong(item, ['dieuKienHoanThanh']) },
    { label: 'Hướng dẫn thực hiện', value: layTruong(item, ['huongDan', 'huongDanThucHien']) },
    { label: 'Ghi chú', value: layTruong(item, ['ghiChu']) }
  ].filter((row) => coGiaTri(row.value))
})

const danhSachChuongBai = computed(() => {
  return layMangDauTien(payload.value, ['chuongBai', 'danhSachChuongBai', 'noiDung', 'noiDungSyllabus'])
})

const danhSachChuanDauRa = computed(() => {
  return layMangDauTien(payload.value, ['chuanDauRa', 'nangLucDauRa', 'learningOutcomes', 'syllabus.chuanDauRa', 'syllabus.nangLucDauRa', 'syllabus.learningOutcomes'])
})

const danhSachTaiLieu = computed(() => {
  return layMangDauTien(payload.value, ['taiLieu', 'taiLieuHocTap', 'syllabusTaiLieu', 'tepDinhKem', 'files', 'syllabus.taiLieu', 'syllabus.taiLieuHocTap', 'syllabus.syllabusTaiLieu'])
})

const danhSachCauHinhDanhGia = computed(() => {
  return sapXepTheoThuTu(layMangDauTien(payload.value, ['cauHinhDanhGia', 'cotDiem', 'danhSachCotDiem', 'syllabus.cauHinhDanhGia', 'syllabus.cotDiem', 'syllabus.danhSachCotDiem']))
})

const tongTyLeDanhGia = computed(() => {
  const total = danhSachCauHinhDanhGia.value.reduce((sum, item) => {
    const value = Number(layTruong(item, ['tyLe', 'tiLe']) ?? 0)
    return sum + (Number.isNaN(value) ? 0 : value)
  }, 0)

  return Number(total.toFixed(2))
})

const danhSachQuyDoiDiem = computed(() => {
  return sapXepTheoThuTu(layMangDauTien(payload.value, ['quyDoiDiem', 'quyDoiDiemTheoChuongTrinh', 'quyDoiKetQua', 'bangQuyDoiDiem', 'syllabus.quyDoiDiem', 'syllabus.quyDoiDiemTheoChuongTrinh', 'syllabus.quyDoiKetQua']))
})

const danhSachDieuKien = computed(() => {
  return sapXepTheoThuTu(layMangDauTien(payload.value, ['dieuKien', 'dieuKienMonHoc', 'syllabusMonHocDieuKien', 'dieuKienSyllabus', 'syllabus.dieuKien', 'syllabus.dieuKienMonHoc', 'syllabus.syllabusMonHocDieuKien']))
})

const linkSyllabus = computed(() => {
  return (
      layTruong(payload.value, ['duongDan', 'url', 'link', 'fileUrl']) ||
      layTruong(syllabus.value, ['duongDan', 'url', 'link', 'fileUrl'])
  )
})

onMounted(() => {
  docPayloadTuSession()
})

async function taiChiTietSyllabusNeuCan(basePayload = {}) {
  const syllabusId = basePayload?.syllabusId || basePayload?.syllabus?.id || route.query.syllabusMonHocId || route.query.syllabusId || null

  if (!syllabusId) {
    return basePayload
  }

  loading.value = true

  try {
    const res = await daoTaoService.syllabusMonHoc.getChiTietXem(syllabusId)
    const chiTiet = layDataTuApiResponse(res)
    const syllabus = chiTiet?.syllabusMonHoc || basePayload.syllabus || {}

    return {
      ...basePayload,
      coSyllabus: true,
      syllabusId,
      syllabus,
      soTinChi: basePayload.soTinChi ?? syllabus.soTinChi,
      gioLyThuyet: basePayload.gioLyThuyet ?? syllabus.gioLyThuyet,
      gioThucHanh: basePayload.gioThucHanh ?? syllabus.gioThucHanh,
      gioKiemTra: basePayload.gioKiemTra ?? syllabus.gioKiemTra,
      soBuoiHoc: basePayload.soBuoiHoc ?? syllabus.soBuoiHoc,
      chuongBai: chiTiet?.chuongBai || [],
      taiLieu: chiTiet?.taiLieu || [],
      cauHinhDanhGia: chiTiet?.cauHinhDanhGia || [],
      quyDoiDiemTheoChuongTrinh: chiTiet?.quyDoiDiemTheoChuongTrinh || [],
      quyDoiDiem: chiTiet?.quyDoiDiem || [],
      dieuKien: chiTiet?.dieuKienMonHoc || [],
      dieuKienMonHoc: chiTiet?.dieuKienMonHoc || []
    }
  } catch (error) {
    throw new Error(layThongBaoLoi(error, 'Không tải được chi tiết syllabus.'))
  } finally {
    loading.value = false
  }
}

async function docPayloadTuSession() {
  const refKey = route.query.ref

  try {
    let basePayload = {}

    if (refKey) {
      const raw = sessionStorage.getItem(String(refKey))

      if (!raw) {
        errorMessage.value = 'Không tìm thấy dữ liệu syllabus. Vui lòng quay lại và chọn lại môn học.'
        return
      }

      basePayload = JSON.parse(raw)
    } else if (route.query.syllabusMonHocId || route.query.syllabusId) {
      basePayload = {
        coSyllabus: true,
        syllabusId: route.query.syllabusMonHocId || route.query.syllabusId
      }
    } else {
      errorMessage.value = 'Thiếu tham chiếu dữ liệu syllabus.'
      return
    }

    payload.value = await taiChiTietSyllabusNeuCan(basePayload)
  } catch (error) {
    errorMessage.value = error?.message || 'Dữ liệu syllabus không hợp lệ.'
  }
}

function layDataTuApiResponse(res) {
  if (res?.success !== undefined && res?.data !== undefined) return res.data
  if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
  return res?.data || res
}

function quayLai() {
  router.back()
}

function layTruong(item, keys = []) {
  if (!item) return null

  for (const key of keys) {
    const value = layNestedValue(item, key)
    if (coGiaTri(value)) return value
  }

  return null
}

function layNestedValue(item, path) {
  if (!path.includes('.')) return item?.[path]
  return path.split('.').reduce((current, key) => current?.[key], item)
}

function layMangDauTien(item, keys = []) {
  if (!item) return []

  for (const key of keys) {
    const value = layNestedValue(item, key)
    if (Array.isArray(value)) return value
  }

  return []
}

function layTenChuong(chuong, index) {
  return (
      chuong?.tenChuong ||
      chuong?.tieuDe ||
      chuong?.ten ||
      chuong?.chuDe ||
      `Chương ${index + 1}`
  )
}

function layNoiDungChuong(chuong) {
  return chuong?.noiDung || chuong?.moTa || chuong?.ghiChu || ''
}

function laySoTietChuong(chuong) {
  return chuong?.soTiet || chuong?.thoiLuong || chuong?.tongGio || null
}

function hienThiNoiDung(item) {
  if (typeof item === 'string') return item
  return item?.noiDung || item?.moTa || item?.ten || item?.ma || '—'
}

function layTenTaiLieu(taiLieu, index) {
  return (
      taiLieu?.tenTaiLieu ||
      taiLieu?.ten ||
      taiLieu?.tieuDe ||
      taiLieu?.duongDan ||
      taiLieu?.url ||
      taiLieu?.link ||
      `Tài liệu ${index + 1}`
  )
}

function layThuTu(item, index) {
  return item?.thuTu ?? item?.stt ?? index + 1
}

function sapXepTheoThuTu(rows = []) {
  return [...rows].sort((a, b) => {
    const thuTuA = Number(a?.thuTu ?? a?.stt ?? 999999)
    const thuTuB = Number(b?.thuTu ?? b?.stt ?? 999999)

    if (thuTuA !== thuTuB) return thuTuA - thuTuB

    const tenA = String(a?.tenCotDiem || a?.ten || a?.ma || '')
    const tenB = String(b?.tenCotDiem || b?.ten || b?.ma || '')
    return tenA.localeCompare(tenB, 'vi')
  })
}

function hienThiBoolean(value) {
  if (value === true) return 'Có'
  if (value === false) return 'Không'
  return hienThi(value)
}

function layLinkTaiLieu(taiLieu) {
  return (
      taiLieu?.duongDan ||
      taiLieu?.url ||
      taiLieu?.link ||
      taiLieu?.fileUrl ||
      taiLieu?.duongDanTaiVe ||
      taiLieu?.duongDanCongKhai ||
      ''
  )
}

function xemTaiLieu(taiLieu) {
  const link = layLinkTaiLieu(taiLieu)
  if (!link) return

  moLienKet(chuyenLinkDownloadThanhPreview(link))
}

function taiTaiLieu(taiLieu) {
  const link = layLinkTaiLieu(taiLieu)
  if (!link) return

  moLienKet(chuyenLinkPreviewThanhDownload(link))
}

function chuyenLinkDownloadThanhPreview(link) {
  const rawLink = String(link || '').trim()

  if (!rawLink) return ''

  if (rawLink.includes('/download')) {
    return rawLink.replace('/download', '/preview')
  }

  return rawLink
}

function chuyenLinkPreviewThanhDownload(link) {
  const rawLink = String(link || '').trim()

  if (!rawLink) return ''

  if (rawLink.includes('/preview')) {
    return rawLink.replace('/preview', '/download')
  }

  return rawLink
}

function moLienKet(link) {
  if (!link) return
  window.open(link, '_blank')
}

function coGiaTri(value) {
  return value !== null && value !== undefined && value !== ''
}

function hienThi(value) {
  if (!coGiaTri(value)) return '—'
  return value
}
</script>

<style scoped>
.dao-tao-xem-syllabus-page {
  min-height: calc(100vh - var(--header-height, 60px));
  padding: 12px 16px 20px;
  background: #f1f7ff;
  color: #0f172a;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

/* =========================
   CARD BASE
========================= */

.page-header,
.summary-card,
.info-card,
.state-card {
  border: 1px solid #bfdbfe;
  border-radius: 18px;
  background: #ffffff;
  box-shadow: 0 8px 24px rgba(15, 82, 143, 0.10);
}

/* =========================
   HEADER
========================= */

.page-header {
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

.page-header h1,
.summary-main h2 {
  margin: 0;
  color: #0f172a;
  line-height: 1.3;
  font-weight: 700;
}

.page-header h1 {
  font-size: 22px;
}

.summary-main h2 {
  font-size: 20px;
}

.subtitle {
  margin: 6px 0 0;
  color: #42637f;
  font-size: 13px;
  line-height: 1.45;
}

/* =========================
   SUMMARY
========================= */

.summary-card {
  display: grid;
  gap: 14px;
  padding: 18px;
  margin-bottom: 14px;
  border-color: #9fc5e8;
  background: linear-gradient(135deg, #ffffff 0%, #f8fbff 100%);
}

.summary-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.status-badge {
  display: inline-flex;
  align-items: center;
  min-height: 24px;
  border-radius: 999px;
  padding: 3px 10px;
  font-size: 12px;
  font-weight: 900;
  white-space: nowrap;
}

.status-badge.success {
  border: 1px solid #86efac;
  background: #dcfce7;
  color: #166534;
}

.status-badge.muted {
  border: 1px solid #d1d5db;
  background: #f8fafc;
  color: #64748b;
}

.status-badge.neutral {
  border: 1px solid #b7d3ec;
  background: #eaf4ff;
  color: #0b5a92;
}

/* =========================
   METRICS
========================= */

.metric-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 10px;
}

.metric-grid article {
  border: 1px solid #bfdbfe;
  border-radius: 14px;
  background: #eaf4ff;
  padding: 11px;
}

.metric-grid span {
  display: block;
  color: #0f3d64;
  font-size: 11px;
  font-weight: 900;
}

.metric-grid strong {
  display: block;
  margin-top: 4px;
  color: #0b5a92;
  font-size: 19px;
  font-weight: 900;
}

/* =========================
   CONTENT GRID
========================= */

.content-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}

.info-card {
  padding: 16px;
  border-color: #bfdbfe;
}

.info-card.full {
  grid-column: 1 / -1;
}

.info-card h3 {
  margin: 0 0 12px;
  color: #0b5a92;
  font-size: 16px;
  font-weight: 900;
}

/* =========================
   INFO LIST
========================= */

.info-list {
  display: grid;
  grid-template-columns: 180px minmax(0, 1fr);
  gap: 8px 12px;
  margin: 0;
}

.info-list dt {
  color: #0f3d64;
  font-size: 12px;
  font-weight: 900;
}

.info-list dd {
  margin: 0;
  color: #0f172a;
  font-size: 13px;
  line-height: 1.45;
  white-space: pre-line;
  word-break: break-word;
}

.rich-text {
  color: #0f172a;
  font-size: 13px;
  line-height: 1.55;
  white-space: pre-line;
}

.empty-inline {
  padding: 12px;
  border: 1px dashed #9fc5e8;
  border-radius: 14px;
  background: #f8fbff;
  color: #42637f;
  font-size: 13px;
}

/* =========================
   CHAPTERS
========================= */

.chapter-list {
  display: grid;
  gap: 10px;
}

.chapter-card {
  border: 1px solid #bfdbfe;
  border-radius: 14px;
  background: #f8fbff;
  padding: 12px;
}

.chapter-head {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  color: #0f172a;
  font-size: 13px;
}

.chapter-head strong {
  color: #0b5a92;
  font-weight: 900;
}

.chapter-head span {
  color: #42637f;
  font-size: 12px;
  font-weight: 800;
}

.chapter-card p {
  margin: 8px 0 0;
  color: #0f3d64;
  font-size: 13px;
  line-height: 1.5;
  white-space: pre-line;
}

/* =========================
   LIST / DOCUMENTS
========================= */

.plain-list {
  margin: 0;
  padding-left: 18px;
  color: #0f172a;
  font-size: 13px;
  line-height: 1.55;
}

.document-list {
  display: grid;
  gap: 8px;
}

.doc-link {
  border: 1px solid #bfdbfe;
  border-radius: 12px;
  background: #eaf4ff;
  color: #0b5a92;
  padding: 8px 10px;
  text-align: left;
  font-size: 12px;
  font-weight: 800;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease;
}

.doc-link:hover {
  border-color: #0b5a92;
  background: #d8ecff;
  color: #08456f;
}

/* =========================
   BUTTONS / STATE
========================= */

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

.btn.primary {
  border-color: #0b5a92;
  background: #0b5a92;
  color: #ffffff;
}

.btn.primary:hover {
  border-color: #08456f;
  background: #08456f;
}

.state-card {
  padding: 16px;
  margin-bottom: 14px;
}

.state-card h3 {
  margin: 0 0 4px;
  color: #0f172a;
  font-size: 16px;
}

.state-card p {
  margin: 0 0 10px;
  color: #42637f;
  font-size: 13px;
}

.state-card.error {
  border-color: #fecaca;
  background: #fef2f2;
}

.state-card.error p {
  color: #991b1b;
}

.state-card.empty {
  border-color: #bfdbfe;
  background: #f8fbff;
}


/* =========================
   READONLY TABLES
========================= */

.table-wrap {
  width: 100%;
  overflow: auto;
  border: 1px solid #dbeafe;
  border-radius: 14px;
  background: #ffffff;
}

.table-wrap table {
  width: 100%;
  min-width: 760px;
  border-collapse: collapse;
  font-size: 13px;
}

.table-wrap th,
.table-wrap td {
  border-bottom: 1px solid #e5edf8;
  padding: 9px 10px;
  text-align: left;
  vertical-align: top;
}

.table-wrap th {
  background: #eaf4ff;
  color: #0f3d64;
  font-size: 12px;
  font-weight: 900;
  white-space: nowrap;
}

.table-wrap tr:last-child td {
  border-bottom: 0;
}

.table-note {
  margin-bottom: 8px;
  padding: 8px 10px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: 700;
}

.table-note.success {
  border: 1px solid #86efac;
  background: #dcfce7;
  color: #166534;
}

.table-note.warning {
  border: 1px solid #fcd34d;
  background: #fffbeb;
  color: #92400e;
}

/* =========================
   RESPONSIVE
========================= */

@media (max-width: 980px) {
  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .content-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .dao-tao-xem-syllabus-page {
    padding: 10px;
  }

  .metric-grid {
    grid-template-columns: 1fr;
  }

  .info-list {
    grid-template-columns: 1fr;
  }

  .info-list dt {
    margin-top: 6px;
  }

  .chapter-head {
    flex-direction: column;
  }
}

.document-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border: 1px solid #bfdbfe;
  border-radius: 14px;
  background: #f8fbff;
  padding: 10px;
}

.document-info {
  display: grid;
  gap: 3px;
  min-width: 0;
}

.document-info strong {
  color: #0b5a92;
  font-size: 13px;
  font-weight: 900;
  line-height: 1.4;
  word-break: break-word;
}

.document-info span {
  color: #42637f;
  font-size: 12px;
  font-weight: 700;
}

.document-actions {
  display: flex;
  flex-shrink: 0;
  gap: 8px;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.55;
  box-shadow: none;
}

@media (max-width: 640px) {
  .document-item {
    align-items: stretch;
    flex-direction: column;
  }

  .document-actions {
    justify-content: flex-start;
  }
}
</style>