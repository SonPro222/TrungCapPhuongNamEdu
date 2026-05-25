<template>
  <div class="xay-dung-page dao-tao-xem-page">
    <header class="page-head dao-tao-head">
      <div>
        <p class="eyebrow">Đào tạo xem chương trình</p>
        <h2>Luồng xem và cập nhật chương trình theo 9 tầng</h2>
        <p>Màn hình được tách từng tầng để người mới nhìn đúng flow: chọn cha → xem thêm → chuyển sang tầng con.</p>
      </div>
      <div class="head-actions">
        <button type="button" class="btn" @click="lamMoiLuon">Làm mới chọn</button>
        <button type="button" class="btn primary" @click="luuChuongTrinhTong">Lưu chương trình</button>
      </div>
    </header>

    <div v-if="thongBao" :class="['notice', loaiThongBao]">{{ thongBao }}</div>
    <div v-if="errorMessage" class="notice error">{{ errorMessage }}</div>

    <nav class="tang-nav mini-scroll">
      <button
          v-for="tang in cacTangDaoTao"
          :key="tang.so"
          type="button"
          :class="['tang-btn', { active: tang.so === tangHienTai }]"
          @click="veTang(tang.so)"
      >
        <b>Tầng {{ tang.so }}</b>
        <span>{{ tang.ten }}</span>
      </button>
    </nav>

    <div class="flow-summary breadcrumb-flow mini-scroll">
      <button
          v-for="muc in tomTatDieuHuong"
          :key="muc.key"
          type="button"
          :class="['breadcrumb-pill', { active: muc.tang === tangHienTai, disabled: !muc.enabled }]"
          :disabled="!muc.enabled"
          :title="muc.enabled ? `Bấm để quay về ${muc.tenTang}` : 'Chưa có dữ liệu để đi tới mục này'"
          @click="diChuyenTheoTomTat(muc)"
      >
        <span>{{ muc.label }}:</span>
        <b>{{ muc.value || '-' }}</b>
      </button>
    </div>

    <section class="tang-current">
      <div>
        <h3>Tầng {{ thongTinTangHienTai.so }}: {{ thongTinTangHienTai.ten }}</h3>
        <p>{{ thongTinTangHienTai.moTa }}</p>
      </div>
      <div class="tang-note">Dùng thanh điều hướng nhanh bên dưới để đi tiếp hoặc quay lại tầng trước.</div>
    </section>

    <div v-if="cacNutDieuHuongTang.length" class="continue-panel continue-panel-top">
      <div>
        <b>Điều hướng tầng {{ tangHienTai }}</b>
        <span>{{ moTaDieuHuongTang }}</span>
      </div>
      <div class="continue-actions">
        <button
            v-for="nut in cacNutDieuHuongTang"
            :key="`top-${nut.key}`"
            type="button"
            :class="['btn', nut.primary ? 'primary deep-action' : 'secondary-action']"
            :disabled="!nut.enabled"
            @click="bamNutDieuHuongTang(nut)"
        >
          {{ nut.label }}
        </button>
      </div>
    </div>

    <section v-for="group in groups" :key="group.key" :class="['flow-group', group.mau]">
      <div class="group-title">
        <h3>{{ group.title }}</h3>
        <p>{{ group.description }}</p>
      </div>

      <BangThemNghiepVu
          v-for="bang in group.tables"
          :key="bang.key"
          v-bind="bang.config"
          :items="bang.rows"
          :all-items="bang.allRows"
          :lookups="lookups"
          :service="bang.service"
          :selected-id="bang.selectedId"
          :selected-ids="bang.selectedIds"
          :saved-ids="bang.savedIds"
          :table-message="bang.tableMessage"
          :can-toggle-save="bang.canToggleSave"
          :can-select="bang.canSelect"
          :can-show-saved-status="bang.canShowSavedStatus"
          :can-view="bang.canView"
          :view-label="bang.viewLabel"
          :viewed-id="bang.viewedId"
          :parent-values="bang.parentValues"
          :parent-text="bang.parentText"
          :disabled="bang.disabled"
          :disabled-text="bang.disabledText"
          :loai-bang="bang.loaiBang"
          @select="chonDongBang(bang, $event)"
          @view="xemDongBang(bang, $event)"
          @toggle-save="luuDongBang(bang, $event)"
          @saved="sauKhiLuu(bang.key, $event)"
          @deleted="sauKhiXoa(bang.key, $event)"
          @notify="baoTin"
      />
    </section>

    <div v-if="cacNutDieuHuongTang.length" class="continue-panel continue-panel-bottom">
      <div>
        <b>Điều hướng tầng {{ tangHienTai }}</b>
        <span>{{ moTaDieuHuongTang }}</span>
      </div>
      <div class="continue-actions">
        <button
            v-for="nut in cacNutDieuHuongTang"
            :key="`bottom-${nut.key}`"
            type="button"
            :class="['btn', nut.primary ? 'primary deep-action' : 'secondary-action']"
            :disabled="!nut.enabled"
            @click="bamNutDieuHuongTang(nut)"
        >
          {{ nut.label }}
        </button>
      </div>
    </div>

    <footer class="page-foot">
      <button type="button" class="btn" @click="veTang(Math.max(1, tangHienTai - 1))">Quay lại tầng trước</button>
      <button type="button" class="btn primary" @click="luuChuongTrinhTong">Lưu chương trình</button>
    </footer>
  </div>
</template>
<script setup>
import { computed, nextTick, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import BangThemNghiepVu from '../components/BangThemNghiepVu.vue'
import { useDaoTaoXemChuongTrinh } from '../composables/useDaoTaoXemChuongTrinh'

const route = useRoute()
const router = useRouter()

const {
  thongBao,
  loaiThongBao,
  errorMessage,
  tableMessages,
  selected,
  viewed,
  duLieu,
  quyDoiDiemMauGocRows,
  lookups,
  services,
  baoTin,
  selectEntity,
  viewEntity,
  chonBangPhuVaoLuong,
  toggleLuuBangPhu,
  sauKhiLuu,
  sauKhiXoa,
  lamMoiLuon,
  luuChuongTrinhTong
} = useDaoTaoXemChuongTrinh()

const chuongTrinhParent = computed(() => ({
  nganhId: selected.nganh?.id || null,
  trinhDoId: selected.trinhDoDaoTao?.id || null,
  loaiChuongTrinhId: selected.loaiChuongTrinh?.id || null
}))

const chuongTrinhParentText = computed(() => {
  return `Đang chọn: Ngành ${selected.nganh?.tenNganh || '-'} | Trình độ ${selected.trinhDoDaoTao?.tenTrinhDo || '-'} | Loại CT ${selected.loaiChuongTrinh?.tenLoai || '-'}`
})

const chuongTrinhVersionParent = computed(() => ({
  chuongTrinhId: selected.chuongTrinh?.id || null
}))

const chuongTrinhVersionParentText = computed(() => {
  return `Đang chọn: Chương trình ${selected.chuongTrinh?.maChuongTrinh || '-'} - ${selected.chuongTrinh?.tenChuongTrinh || '-'}`
})

const versionParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null
}))

const versionParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.maVersion || '-'} - ${selected.chuongTrinhVersion?.tenVersion || '-'}`
})

const syllabusChuongTrinhParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  syllabusChuongTrinhGocId: selected.syllabusChuongTrinhGoc?.id || null
}))

const syllabusChuongTrinhParentText = computed(() => {
  return `${versionParentText.value} | Syllabus chương trình gốc ${selected.syllabusChuongTrinhGoc?.ma || '-'} - ${selected.syllabusChuongTrinhGoc?.ten || '-'}`
})

const nhomKienThucParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  nhomKienThucGocId: selected.nhomKienThucGoc?.id || null
}))

const nhomKienThucParentText = computed(() => {
  return `${versionParentText.value} | Nhóm kiến thức gốc ${selected.nhomKienThucGoc?.ma || '-'} - ${selected.nhomKienThucGoc?.ten || '-'}`
})

const nhomTuChonParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  nhomTuChonGocId: selected.nhomTuChonGoc?.id || null
}))

const nhomTuChonParentText = computed(() => {
  return `${versionParentText.value} | Nhóm tự chọn gốc ${selected.nhomTuChonGoc?.ma || '-'} - ${selected.nhomTuChonGoc?.ten || '-'}`
})

const khungKyParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  loaiChuongTrinhId: selected.loaiChuongTrinh?.id || selected.chuongTrinh?.loaiChuongTrinhId || null,
  khungKyGocId: selected.khungKyGoc?.id || null,
  maKy: selected.khungKyGoc?.maKy || '',
  tenKy: selected.khungKyGoc?.tenKy || '',
  thuTu: selected.khungKyGoc?.thuTu || null
}))

const khungKyParentText = computed(() => {
  return `${versionParentText.value} | Loại CT ${selected.loaiChuongTrinh?.tenLoai || selected.chuongTrinh?.tenLoaiChuongTrinh || selected.chuongTrinh?.loaiChuongTrinhId || '-'} | Khung kỳ gốc ${selected.khungKyGoc?.maKy || '-'} - ${selected.khungKyGoc?.tenKy || '-'}`
})

const monHocParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Tạo môn học gốc trước khi tạo Chương trình môn`
})

const syllabusMonHocGocParent = computed(() => ({
  monHocId: selected.monHoc?.id || null
}))

const syllabusMonHocGocFilter = computed(() => ({
  monHocId: dongDangDung('monHoc')?.id || null
}))

const syllabusMonHocGocParentText = computed(() => {
  return `Đang chọn: Môn học gốc ${selected.monHoc?.maMon || '-'} - ${selected.monHoc?.tenMon || '-'}`
})

const syllabusMonHocGocChiTietParent = computed(() => ({
  syllabusMonHocGocId: selected.syllabusMonHocGoc?.id || null
}))

const syllabusMonHocGocChiTietFilter = computed(() => ({
  syllabusMonHocGocId: dongDangDung('syllabusMonHocGoc')?.id || null
}))

const syllabusMonHocGocChiTietText = computed(() => {
  return `Đang chọn: Syllabus môn học gốc ${selected.syllabusMonHocGoc?.ma || '-'} - ${selected.syllabusMonHocGoc?.ten || '-'}`
})

const chuongTrinhMonParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  monHocId: selected.monHoc?.id || null,
  khungKyId: selected.khungKy?.id || null,
  nhomKienThucId: selected.nhomKienThuc?.id || null
}))

const chuongTrinhMonFilter = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  khungKyId: selected.khungKy?.id || null,
  nhomKienThucId: selected.nhomKienThuc?.id || null
}))

const chuongTrinhMonParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Môn học ${selected.monHoc?.tenMon || '-'} | Kỳ ${selected.khungKy?.tenKy || '-'} | Nhóm KT ${selected.nhomKienThuc?.ten || '-'}`
})

const monTuChonParent = computed(() => ({
  nhomId: selected.nhomTuChon?.id || null,
  chuongTrinhMonId: selected.chuongTrinhMon?.id || null
}))

const monTuChonParentText = computed(() => {
  return `Đang chọn: Nhóm tự chọn ${selected.nhomTuChon?.ten || '-'} | Môn CT ${selected.chuongTrinhMon?.maMonTrongCt || '-'}`
})

const chuongTrinhMonOnlyParent = computed(() => ({
  chuongTrinhMonId: selected.chuongTrinhMon?.id || null
}))

const syllabusMonHocApDungParent = computed(() => ({
  chuongTrinhMonId: selected.chuongTrinhMon?.id || null,
  syllabusMonHocGocId: selected.syllabusMonHocGoc?.id || null
}))

const chuongTrinhMonOnlyParentText = computed(() => {
  return `Đang chọn: Môn trong chương trình ${selected.chuongTrinhMon?.maMonTrongCt || '-'} - ${selected.chuongTrinhMon?.tenMon || '-'}`
})

const monTienQuyetParent = computed(() => ({
  monId: selected.chuongTrinhMon?.id || null
}))

const syllabusMonParent = computed(() => ({
  syllabusMonId: selected.syllabusMonHoc?.id || null
}))

const syllabusMonParentText = computed(() => {
  return `Đang chọn: Syllabus môn học ID ${selected.syllabusMonHoc?.id || '-'} | Môn CT ${selected.chuongTrinhMon?.maMonTrongCt || '-'}`
})

function dongDangDung(key) {
  return viewed[key] || selected[key] || null
}

const chuongTrinhFilter = computed(() => ({
  nganhId: dongDangDung('nganh')?.id || null,
  trinhDoId: dongDangDung('trinhDoDaoTao')?.id || null,
  loaiChuongTrinhId: dongDangDung('loaiChuongTrinh')?.id || null
}))

const chuongTrinhVersionFilter = computed(() => ({
  chuongTrinhId: dongDangDung('chuongTrinh')?.id || null
}))

const versionFilter = computed(() => ({
  chuongTrinhVersionId: dongDangDung('chuongTrinhVersion')?.id || null
}))

const khungKyFilter = computed(() => ({
  chuongTrinhVersionId: dongDangDung('chuongTrinhVersion')?.id || null,
  loaiChuongTrinhId: dongDangDung('loaiChuongTrinh')?.id || selected.chuongTrinh?.loaiChuongTrinhId || null
}))

const chuongTrinhMonViewFilter = computed(() => ({
  chuongTrinhVersionId: dongDangDung('chuongTrinhVersion')?.id || null,
  khungKyId: dongDangDung('khungKy')?.id || null,
  nhomKienThucId: dongDangDung('nhomKienThuc')?.id || null,
  monHocId: dongDangDung('monHoc')?.id || null
}))

const chuongTrinhMonOnlyFilter = computed(() => ({
  chuongTrinhMonId: dongDangDung('chuongTrinhMon')?.id || null
}))

const syllabusMonFilter = computed(() => ({
  syllabusMonId: dongDangDung('syllabusMonHoc')?.id || null
}))

function coGiaTriCha(value) {
  return value !== null && value !== undefined && value !== ''
}

function locDongTheoCha(rows = [], parentValues = {}) {
  const dieuKien = Object.entries(parentValues).filter(([, value]) => coGiaTriCha(value))
  if (!dieuKien.length) return rows

  return rows.filter((row) => {
    return dieuKien.every(([key, value]) => String(row?.[key] ?? '') === String(value))
  })
}

function layIdDongTheoCha(rows = [], parentValues = {}) {
  const dieuKien = Object.entries(parentValues).filter(([, value]) => coGiaTriCha(value))
  if (!dieuKien.length) return []

  return rows
      .filter((row) => {
        return dieuKien.every(([key, value]) => String(row?.[key] ?? '') === String(value))
      })
      .map((row) => row?.id)
      .filter((id) => id !== null && id !== undefined && id !== '')
}

function laySavedIdsTheoBang(key, allRows = [], rows = [], parentValues = {}, filterValues = {}) {
  if (key === 'trinhDoDaoTao' || key === 'loaiChuongTrinh') {
    const field = key === 'trinhDoDaoTao' ? 'trinhDoId' : 'loaiChuongTrinhId'
    const chuongTrinhDangXem = selected.chuongTrinh
    const nganhId = dongDangDung('nganh')?.id || route.params.nganhId || parentValues.nganhId || filterValues.nganhId

    if (chuongTrinhDangXem?.id && String(chuongTrinhDangXem.nganhId || '') === String(nganhId || '')) {
      const idDaLuu = chuongTrinhDangXem[field]
      return idDaLuu !== null && idDaLuu !== undefined && idDaLuu !== '' ? [idDaLuu] : []
    }

    if (nganhId) {
      const idsDaLuuTheoNganh = new Set(
          (duLieu.value.chuongTrinh || [])
              .filter((row) => String(row.nganhId || '') === String(nganhId || ''))
              .map((row) => row?.[field])
              .filter((id) => id !== null && id !== undefined && id !== '')
      )

      if (idsDaLuuTheoNganh.size) return Array.from(idsDaLuuTheoNganh)
    }

    const idDangChon = selected[key]?.id
    return idDangChon !== null && idDangChon !== undefined && idDangChon !== '' ? [idDangChon] : []
  }

  return laySelectedIdsTheoBang(key, allRows, rows, parentValues, filterValues)
}

function laySelectedIdsTheoBang(key, allRows = [], rows = [], parentValues = {}, filterValues = {}) {
  if (key === 'trinhDoDaoTao' || key === 'loaiChuongTrinh') {
    return []
  }

  const bangGocNoiVersion = {
    mucTieuChuongTrinhGoc: { joinKey: 'chuongTrinhVersionMucTieu', gocIdKey: 'mucTieuGocId' },
    nangLucDauRaGoc: { joinKey: 'chuongTrinhVersionNangLuc', gocIdKey: 'nangLucGocId' },
    viTriViecLamGoc: { joinKey: 'chuongTrinhVersionViTriViecLam', gocIdKey: 'viTriGocId' },
    dieuKienTotNghiepGoc: { joinKey: 'chuongTrinhVersionDieuKienTotNghiep', gocIdKey: 'dieuKienGocId' }
  }


  if (key === 'syllabusChuongTrinhGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.syllabusChuongTrinh || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.syllabusChuongTrinhGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'khungKyGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.khungKy || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.khungKyGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nhomKienThucGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.nhomKienThuc || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.nhomKienThucGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nhomTuChonGoc') {
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id
    if (!chuongTrinhVersionId) return []
    return (duLieu.value.nhomTuChon || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row.nhomTuChonGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'dieuKienMonHocGoc') {
    const syllabusMonHocGocId = filterValues.syllabusMonHocGocId || parentValues.syllabusMonHocGocId || selected.syllabusMonHocGoc?.id
    if (syllabusMonHocGocId) {
      return (duLieu.value.syllabusMonHocGocDieuKien || [])
          .filter((row) => String(row.syllabusMonHocGocId || '') === String(syllabusMonHocGocId || ''))
          .map((row) => row.dieuKienGocId)
          .filter((id) => id !== null && id !== undefined && id !== '')
    }

    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    return (duLieu.value.syllabusMonHocDieuKien || [])
        .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
        .map((row) => row.dieuKienGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'taiLieuGoc') {
    const syllabusMonHocGocId = filterValues.syllabusMonHocGocId || parentValues.syllabusMonHocGocId || selected.syllabusMonHocGoc?.id
    if (syllabusMonHocGocId) {
      return (duLieu.value.syllabusMonHocGocTaiLieu || [])
          .filter((row) => String(row.syllabusMonHocGocId || '') === String(syllabusMonHocGocId || ''))
          .map((row) => row.taiLieuGocId)
          .filter((id) => id !== null && id !== undefined && id !== '')
    }

    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    return (duLieu.value.syllabusMonHocTaiLieu || [])
        .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
        .map((row) => row.taiLieuGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (bangGocNoiVersion[key]) {
    const config = bangGocNoiVersion[key]
    const chuongTrinhVersionId = filterValues.chuongTrinhVersionId || parentValues.chuongTrinhVersionId || selected.chuongTrinhVersion?.id

    if (!chuongTrinhVersionId) return []

    return (duLieu.value[config.joinKey] || [])
        .filter((row) => String(row.chuongTrinhVersionId || '') === String(chuongTrinhVersionId || ''))
        .map((row) => row[config.gocIdKey])
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'quyDoiDiemMau') {
    // Chỉ lấy danh sách ID đã gắn với Chương trình môn để tô trạng thái Đã lưu.
    // Không dùng đoạn này để lọc dữ liệu hiển thị vì quyDoiDiemMau là bảng gốc/tái sử dụng dùng chung.
    const chuongTrinhMonId = filterValues.chuongTrinhMonId || parentValues.chuongTrinhMonId || selected.chuongTrinhMon?.id
    if (!chuongTrinhMonId) return []

    return (duLieu.value.chuongTrinhMonQuyDoiDiemMau || [])
        .filter((row) => String(row.chuongTrinhMonId || '') === String(chuongTrinhMonId || ''))
        .map((row) => row.quyDoiDiemMauId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'nganh' || key === 'monHoc') {
    return []
  }

  if (key === 'chuongTrinhMon') {
    return layIdDongTheoCha(allRows, filterValues)
  }

  return layIdDongTheoCha(allRows.length ? allRows : rows, filterValues)
}

const bangXuongSongKeys = new Set([
  'nganh',
  'trinhDoDaoTao',
  'loaiChuongTrinh',
  'chuongTrinh',
  'chuongTrinhVersion',
  'khungKy',
  'nhomKienThuc',
  'nhomTuChon',
  'monHoc',
  'chuongTrinhMon',
  'syllabusMonHoc'
])


const bangDocLapKhongLocTheoCha = new Set([
  // Các bảng gốc/kho mẫu dùng chung phải luôn hiện toàn bộ dữ liệu từ API gốc.
  // Việc chọn Chương trình môn chỉ dùng cho nút Lưu để gắn qua bảng nối, không được làm mất dữ liệu mẫu.
  'quyDoiDiemMau'
])

const bangGocMauKeys = new Set([
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'syllabusChuongTrinhGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'syllabusMonHocGoc',
  'dieuKienMonHocGoc',
  'taiLieuGoc',
  'quyDoiDiemMau'
])

const nhanNutXemTheoBang = {
  nganh: 'Xem chương trình',
  chuongTrinh: 'Xem version',
  chuongTrinhVersion: 'Xem chi tiết version',
  monHoc: 'Xem syllabus gốc',
  chuongTrinhMon: 'Xem syllabus áp dụng'
}

const bangKhongCanChon = new Set([
  'syllabusChuongTrinh',
  'chuongTrinhVersionMucTieu',
  'mucTieuChuongTrinh',
  'chuongTrinhVersionNangLuc',
  'nangLucDauRa',
  'chuongTrinhVersionViTriViecLam',
  'viTriViecLam',
  'chuongTrinhVersionDieuKienTotNghiep',
  'dieuKienTotNghiep',
  'monTuChon',
  'monTienQuyet',
  'chuongTrinhMonQuyDoiDiemMau',
  'quyDoiDiem',
  'syllabusMonHocGocChuongBai',
  'syllabusMonHocGocDieuKien',
  'syllabusMonHocGocTaiLieu',
  'syllabusChuongBai',
  'syllabusMonHocDieuKien',
  'dieuKienMonHoc',
  'syllabusMonHocTaiLieu',
  'syllabusTaiLieu'
])

const bangChiLuuGanKhongChon = new Set([
  'syllabusChuongTrinhGoc',
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc'
])

const bangCoNutLuuBoLuuMacDinh = new Set([
  'syllabusChuongTrinhGoc',
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc'
])

const bangChiHienTrangThaiDaLuuMacDinh = new Set([
  'trinhDoDaoTao',
  'loaiChuongTrinh',
  'chuongTrinh',
  'chuongTrinhVersion',
  'khungKy',
  'nhomKienThuc',
  'nhomTuChon',
  'monHoc',
  'chuongTrinhMon',
  'syllabusMonHoc',
  'syllabusChuongTrinh',
  'chuongTrinhVersionMucTieu',
  'mucTieuChuongTrinh',
  'chuongTrinhVersionNangLuc',
  'nangLucDauRa',
  'chuongTrinhVersionViTriViecLam',
  'viTriViecLam',
  'chuongTrinhVersionDieuKienTotNghiep',
  'dieuKienTotNghiep',
  'monTuChon',
  'monTienQuyet',
  'chuongTrinhMonQuyDoiDiemMau',
  'quyDoiDiem',
  'syllabusMonHocGocChuongBai',
  'syllabusMonHocGocDieuKien',
  'syllabusMonHocGocTaiLieu',
  'syllabusChuongBai',
  'syllabusMonHocDieuKien',
  'dieuKienMonHoc',
  'syllabusMonHocTaiLieu',
  'syllabusTaiLieu'
])


function chonDongBang(bang, item) {
  if (bang.key === 'chuongTrinhMon') {
    selectEntity(bang.key, item)
    chonMonHocTheoChuongTrinhMon(item)
    return
  }

  if (bangCoNutLuuLienKet.has(bang.key)) {
    selectEntity(bang.key, item)
    return
  }

  if (bang.loaiBang === 'phu') {
    chonBangPhuVaoLuong(bang.key, item, bang.parentValues, bang.service)
    return
  }

  selectEntity(bang.key, item)
}

function xemDongBang(bang, item) {
  viewEntity(bang.key, item)

  if (!item) return

  selectEntity(bang.key, item)

  if (bang.key === 'nganh') {
    router.push({ name: 'DaoTao.XemChuongTrinh.CauHinh', params: { nganhId: item.id } })
    return
  }

  if (bang.key === 'trinhDoDaoTao' || bang.key === 'loaiChuongTrinh') {
    const trinhDoId = bang.key === 'trinhDoDaoTao' ? item.id : selected.trinhDoDaoTao?.id
    const loaiChuongTrinhId = bang.key === 'loaiChuongTrinh' ? item.id : selected.loaiChuongTrinh?.id
    if (selected.nganh?.id && trinhDoId && loaiChuongTrinhId) {
      router.push({ name: 'DaoTao.XemChuongTrinh.ChuongTrinh', params: { nganhId: selected.nganh.id }, query: { trinhDoId, loaiChuongTrinhId } })
    }
    return
  }

  if (bang.key === 'chuongTrinh') {
    router.push({ name: 'DaoTao.XemChuongTrinh.Version', params: { nganhId: item.nganhId || selected.nganh?.id, chuongTrinhId: item.id } })
    return
  }

  if (bang.key === 'chuongTrinhVersion') {
    router.push({ name: 'DaoTao.XemChuongTrinh.TongQuan', params: { nganhId: selected.nganh?.id, chuongTrinhId: item.chuongTrinhId || selected.chuongTrinh?.id, versionId: item.id } })
    return
  }

  if (bang.key === 'khungKy') {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Mon',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id
      },
      query: {
        khungKyId: item.id
      }
    })
    return
  }

  if (['nhomKienThuc', 'nhomTuChon'].includes(bang.key)) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.Mon',
      params: {
        nganhId: selected.nganh?.id,
        chuongTrinhId: selected.chuongTrinh?.id,
        versionId: selected.chuongTrinhVersion?.id
      }
    })
    return
  }

  if (bang.key === 'monHoc') {
    router.push({ name: 'DaoTao.XemChuongTrinh.SyllabusGoc', params: { nganhId: selected.nganh?.id, chuongTrinhId: selected.chuongTrinh?.id, versionId: selected.chuongTrinhVersion?.id, monHocId: item.id } })
    return
  }

  if (bang.key === 'chuongTrinhMon') {
    chonMonHocTheoChuongTrinhMon(item)
    router.push({ name: 'DaoTao.XemChuongTrinh.SyllabusApDung', params: { nganhId: selected.nganh?.id, chuongTrinhId: selected.chuongTrinh?.id, versionId: selected.chuongTrinhVersion?.id, chuongTrinhMonId: item.id } })
  }
}


const bangCoNutLuuLienKet = new Set([
  'syllabusChuongTrinhGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'syllabusMonHocGoc',
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'chuongTrinhMon',
  'monTienQuyet',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function luuDongBang(bang, item) {
  const parentValues = bang.linkParentValues || bang.parentValues || {}

  if (bang.key === 'quyDoiDiemMau' && !parentValues.chuongTrinhMonId) {
    baoTin('Cần chọn Môn trong chương trình trước khi lưu/gắn Quy đổi điểm mẫu.', 'error')
    return
  }

  toggleLuuBangPhu(bang.key, item, parentValues, bang.service)
}

const bangPhuHienDuLieuDayDu = new Set([
  'mucTieuChuongTrinhGoc',
  'nangLucDauRaGoc',
  'viTriViecLamGoc',
  'dieuKienTotNghiepGoc',
  'syllabusChuongTrinhGoc',
  'khungKyGoc',
  'nhomKienThucGoc',
  'nhomTuChonGoc',
  'syllabusChuongTrinh',
  'monTienQuyet',
  'quyDoiDiemMau',
  'dieuKienMonHocGoc',
  'taiLieuGoc',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function taoBang(key, options = {}) {
  const parentValues = options.parentValues || {}
  const linkParentValues = options.linkParentValues || parentValues
  const filterValues = options.filterValues || parentValues
  const allRows = key === 'quyDoiDiemMau'
      ? (quyDoiDiemMauGocRows.value || [])
      : (duLieu.value[key] || [])

  const loaiBang = bangGocMauKeys.has(key) ? 'goc-mau' : (bangXuongSongKeys.has(key) ? 'xuong-song' : 'phu')
  const hienDayDuBangPhu = loaiBang === 'phu' && bangPhuHienDuLieuDayDu.has(key)
  const hienDayDuBangGocMau = loaiBang === 'goc-mau'
  const khongLocTheoCha = bangDocLapKhongLocTheoCha.has(key)

  const rows = khongLocTheoCha || hienDayDuBangGocMau || hienDayDuBangPhu ? allRows : locDongTheoCha(allRows, filterValues)

  const coNutLuuBoLuu = options.canToggleSave === undefined ? bangCoNutLuuBoLuuMacDinh.has(key) : Boolean(options.canToggleSave)
  const coNutChon = options.canSelect === undefined ? !bangKhongCanChon.has(key) && !bangChiLuuGanKhongChon.has(key) : Boolean(options.canSelect)
  const coHienTrangThaiDaLuu = Boolean(options.canShowSavedStatus) || coNutLuuBoLuu || bangChiHienTrangThaiDaLuuMacDinh.has(key)
  const idsDaLuuTheoNguCanh = coHienTrangThaiDaLuu ? laySavedIdsTheoBang(key, allRows, rows, linkParentValues, linkParentValues) : []
  const idsHienThiDaLuu = coHienTrangThaiDaLuu && !coNutLuuBoLuu && idsDaLuuTheoNguCanh.length === 0 && !['trinhDoDaoTao', 'loaiChuongTrinh'].includes(key)
      ? rows.map((row) => row?.id).filter((id) => id !== null && id !== undefined && id !== '')
      : idsDaLuuTheoNguCanh
  const idsLienQuanTheoNguCanh = coNutChon ? laySelectedIdsTheoBang(key, allRows, rows, linkParentValues, linkParentValues) : []

  return {
    key,
    config: { ...configs[key], canView: Boolean(options.canView), viewLabel: options.viewLabel || nhanNutXemTheoBang[key] || 'Xem thêm' },
    rows,
    allRows,
    service: services[key],
    selectedId: selected[key]?.id || null,
    viewedId: viewed[key]?.id || null,
    selectedIds: idsLienQuanTheoNguCanh,
    savedIds: idsHienThiDaLuu,
    canToggleSave: coNutLuuBoLuu,
    canSelect: coNutChon,
    canShowSavedStatus: coHienTrangThaiDaLuu,
    canView: Boolean(options.canView),
    viewLabel: options.viewLabel || nhanNutXemTheoBang[key] || 'Xem thêm',
    tableMessage: tableMessages?.[key] || null,
    parentValues,
    linkParentValues,
    parentText: options.parentText || '',
    loaiBang,
    disabled: Boolean(options.disabled),
    disabledText: options.disabledText || ''
  }
}


const cacTangDaoTao = [
  { so: 1, ten: 'Ngành', route: 'DaoTao.XemChuongTrinh', moTa: 'Chọn ngành để bắt đầu xem/xây dựng chương trình.' },
  { so: 2, ten: 'Cấu hình chương trình', route: 'DaoTao.XemChuongTrinh.CauHinh', moTa: 'Chọn trình độ đào tạo và loại chương trình.' },
  { so: 3, ten: 'Chương trình', route: 'DaoTao.XemChuongTrinh.ChuongTrinh', moTa: 'Lọc chương trình theo ngành, trình độ và loại chương trình.' },
  { so: 4, ten: 'Version chương trình', route: 'DaoTao.XemChuongTrinh.Version', moTa: 'Chọn phiên bản chương trình để đi vào chi tiết.' },
  { so: 5, ten: 'Thông tin tổng quan version', route: 'DaoTao.XemChuongTrinh.TongQuan', moTa: 'Syllabus chương trình, mục tiêu, năng lực, vị trí việc làm, điều kiện tốt nghiệp.' },
  { so: 6, ten: 'Khung cấu trúc đào tạo', route: 'DaoTao.XemChuongTrinh.KhungCauTruc', moTa: 'Khung kỳ, nhóm kiến thức, nhóm tự chọn theo version.' },
  { so: 7, ten: 'Môn trong chương trình', route: 'DaoTao.XemChuongTrinh.Mon', moTa: 'Môn học, chương trình môn, môn tự chọn, tiên quyết, quy đổi điểm.' },
  { so: 8, ten: 'Syllabus môn học gốc', route: 'DaoTao.XemChuongTrinh.SyllabusGoc', moTa: 'Mẫu syllabus gốc, chương bài gốc, điều kiện gốc và tài liệu gốc.' },
  { so: 9, ten: 'Syllabus môn học áp dụng', route: 'DaoTao.XemChuongTrinh.SyllabusApDung', moTa: 'Syllabus áp dụng cho môn trong chương trình.' }
]

const tangHienTai = computed(() => cacTangDaoTao.find((tang) => tang.route === route.name)?.so || 1)
const thongTinTangHienTai = computed(() => cacTangDaoTao.find((tang) => tang.so === tangHienTai.value) || cacTangDaoTao[0])

const coTheVeTangTongQuan = computed(() => Boolean(selected.nganh?.id && selected.chuongTrinh?.id && selected.chuongTrinhVersion?.id))
const coTheTiepTucTang2 = computed(() => Boolean(selected.nganh?.id && selected.trinhDoDaoTao?.id && selected.loaiChuongTrinh?.id))
const coTheTiepTucTang6 = computed(() => coTheVeTangTongQuan.value)
const hienNutTiepTucChung = computed(() => tangHienTai.value === 2 || tangHienTai.value === 6)
const tieuDeNutTiepTuc = computed(() => tangHienTai.value === 2 ? 'Tiếp tục xem Chương trình' : 'Tiếp tục xem Môn trong chương trình')
const coTheBamNutTiepTuc = computed(() => tangHienTai.value === 2 ? coTheTiepTucTang2.value : coTheTiepTucTang6.value)
const moTaNutTiepTuc = computed(() => {
  if (tangHienTai.value === 2) {
    return coTheTiepTucTang2.value
        ? 'Đã chọn đủ Ngành, Trình độ và Loại chương trình. Bấm để xem danh sách chương trình phù hợp.'
        : 'Cần chọn đủ Ngành, Trình độ và Loại chương trình trước khi đi tiếp.'
  }

  return coTheTiepTucTang6.value
      ? 'Đã có Version. Bấm để xem các môn trong chương trình của Version đang chọn.'
      : 'Cần chọn Version trước khi đi tiếp.'
})

const tomTatDieuHuong = computed(() => [
  { key: 'nganh', label: 'Ngành', value: selected.nganh?.tenNganh || '', tang: 1, tenTang: 'Tầng 1 - Ngành', enabled: Boolean(selected.nganh?.id) || tangHienTai.value === 1 },
  { key: 'trinhDoDaoTao', label: 'Trình độ', value: selected.trinhDoDaoTao?.tenTrinhDo || '', tang: 2, tenTang: 'Tầng 2 - Cấu hình chương trình', enabled: Boolean(selected.nganh?.id) },
  { key: 'loaiChuongTrinh', label: 'Loại CT', value: selected.loaiChuongTrinh?.tenLoai || '', tang: 2, tenTang: 'Tầng 2 - Cấu hình chương trình', enabled: Boolean(selected.nganh?.id) },
  { key: 'chuongTrinh', label: 'CTĐT', value: selected.chuongTrinh?.tenChuongTrinh || '', tang: 3, tenTang: 'Tầng 3 - Chương trình', enabled: Boolean(selected.nganh?.id) },
  { key: 'chuongTrinhVersion', label: 'Version', value: selected.chuongTrinhVersion?.tenVersion || '', tang: 4, tenTang: 'Tầng 4 - Version chương trình', enabled: Boolean(selected.nganh?.id && selected.chuongTrinh?.id) },
  { key: 'khungKy', label: 'Kỳ', value: selected.khungKy?.tenKy || '', tang: 6, tenTang: 'Tầng 6 - Khung cấu trúc đào tạo', enabled: coTheVeTangTongQuan.value },
  { key: 'nhomKienThuc', label: 'Nhóm KT', value: selected.nhomKienThuc?.ten || '', tang: 6, tenTang: 'Tầng 6 - Khung cấu trúc đào tạo', enabled: coTheVeTangTongQuan.value },
  { key: 'nhomTuChon', label: 'Nhóm tự chọn', value: selected.nhomTuChon?.ten || '', tang: 6, tenTang: 'Tầng 6 - Khung cấu trúc đào tạo', enabled: coTheVeTangTongQuan.value },
  { key: 'monHoc', label: 'Môn học', value: selected.monHoc?.tenMon || '', tang: 7, tenTang: 'Tầng 7 - Môn trong chương trình', enabled: coTheVeTangTongQuan.value },
  { key: 'chuongTrinhMon', label: 'Môn trong CT', value: selected.chuongTrinhMon?.maMonTrongCt || selected.chuongTrinhMon?.tenMon || '', tang: 7, tenTang: 'Tầng 7 - Môn trong chương trình', enabled: coTheVeTangTongQuan.value }
])

function diChuyenTheoTomTat(muc) {
  if (!muc?.enabled) return
  veTang(muc.tang)
}

const coTheVeTang8 = computed(() => Boolean(coTheVeTangTongQuan.value && (selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || selected.chuongTrinhMon?.monId)))
const coTheVeTang9 = computed(() => Boolean(coTheVeTangTongQuan.value && selected.chuongTrinhMon?.id))

const cacNutDieuHuongTang = computed(() => {
  const so = tangHienTai.value
  const taoNut = (key, label, tang, enabled = true, primary = false, action = 'tang') => ({ key, label, tang, enabled: Boolean(enabled), primary, action })

  if (so === 1) return [taoNut('tiep-cau-hinh', 'Tiếp tục sang Cấu hình chương trình →', 2, Boolean(selected.nganh?.id), true)]
  if (so === 2) return [
    taoNut('ve-nganh', '← Về Ngành', 1, true),
    taoNut('tiep-chuong-trinh', 'Tiếp tục xem Chương trình →', 3, coTheTiepTucTang2.value, true)
  ]
  if (so === 3) return [
    taoNut('ve-cau-hinh', '← Về Cấu hình', 2, Boolean(selected.nganh?.id)),
    taoNut('tiep-version', 'Tiếp tục xem Version →', 4, Boolean(selected.chuongTrinh?.id), true)
  ]
  if (so === 4) return [
    taoNut('ve-chuong-trinh', '← Về Chương trình', 3, Boolean(selected.nganh?.id)),
    taoNut('tiep-tong-quan', 'Sang Tổng quan Version →', 5, coTheVeTangTongQuan.value, true)
  ]
  if (so === 5) return [
    taoNut('ve-version', '← Về Version', 4, Boolean(selected.nganh?.id && selected.chuongTrinh?.id)),
    taoNut('tiep-khung', 'Sang Khung cấu trúc đào tạo →', 6, coTheVeTangTongQuan.value, true)
  ]
  if (so === 6) return [
    taoNut('ve-tong-quan', '← Về Tổng quan Version', 5, coTheVeTangTongQuan.value),
    taoNut('tiep-mon', 'Sang Môn trong chương trình →', 7, coTheVeTangTongQuan.value, true)
  ]
  if (so === 7) return [
    taoNut('ve-khung', '← Về Khung cấu trúc', 6, coTheVeTangTongQuan.value),
    taoNut('xem-syllabus-goc', 'Xem Syllabus gốc →', 8, coTheVeTang8.value, true),
    taoNut('xem-syllabus-ap-dung', 'Xem Syllabus áp dụng →', 9, coTheVeTang9.value, true)
  ]
  if (so === 8) return [
    taoNut('ve-mon', '← Về Môn trong chương trình', 7, coTheVeTangTongQuan.value),
    taoNut('tiep-syllabus-ap-dung', 'Sang Syllabus áp dụng →', 9, coTheVeTang9.value, true)
  ]
  return [
    taoNut('ve-syllabus-goc', '← Về Syllabus gốc', 8, coTheVeTangTongQuan.value),
    taoNut('ve-mon', '← Về Môn trong chương trình', 7, coTheVeTangTongQuan.value),
    taoNut('luu-chuong-trinh', 'Lưu chương trình', null, true, true, 'luu')
  ]
})

const moTaDieuHuongTang = computed(() => {
  if (tangHienTai.value === 1) return selected.nganh?.id ? 'Đã chọn ngành. Có thể đi tiếp sang cấu hình chương trình.' : 'Chọn một ngành trước khi đi tiếp.'
  if (tangHienTai.value === 2) return moTaNutTiepTuc.value
  if (tangHienTai.value === 5) return 'Tầng tổng quan dài, nút chuyển tầng được đặt cả trên và dưới để không cần kéo lên đầu trang.'
  if (tangHienTai.value === 7) return 'Chọn Môn học để xem syllabus gốc, hoặc chọn Môn trong CT để xem syllabus áp dụng.'
  if (tangHienTai.value === 8) return coTheVeTang9.value ? 'Có thể sang syllabus áp dụng của Môn trong CT đang chọn.' : 'Muốn sang tầng 9 cần chọn Môn trong chương trình ở tầng 7.'
  if (tangHienTai.value === 9) return 'Đây là tầng cuối. Có thể quay lại tầng 8 hoặc tầng 7 để kiểm tra dữ liệu.'
  return 'Dùng các nút này để đi theo flow mà không cần kéo lên thanh tầng phía trên.'
})

function cuonLenDauTrangSauKhiDoiTang() {
  nextTick(() => {
    window.setTimeout(() => {
      const diemDau = document.querySelector('.dao-tao-xem-page')
      if (diemDau) {
        diemDau.scrollIntoView({ behavior: 'smooth', block: 'start' })
        return
      }

      window.scrollTo({ top: 0, behavior: 'smooth' })
    }, 80)
  })
}

function bamNutDieuHuongTang(nut) {
  if (!nut?.enabled) return
  if (nut.action === 'luu') {
    luuChuongTrinhTong()
    cuonLenDauTrangSauKhiDoiTang()
    return
  }
  veTang(nut.tang)
  cuonLenDauTrangSauKhiDoiTang()
}

function bamNutTiepTucChung() {
  if (!coTheBamNutTiepTuc.value) return
  if (tangHienTai.value === 2) {
    veTang(3)
    cuonLenDauTrangSauKhiDoiTang()
    return
  }
  if (tangHienTai.value === 6) {
    denMonTrongChuongTrinh()
    cuonLenDauTrangSauKhiDoiTang()
  }
}

function timDongTheoId(key, id) {
  if (!id) return null
  return (duLieu.value[key] || []).find((item) => String(item.id) === String(id)) || null
}

function timMonHocCuaChuongTrinhMon(chuongTrinhMon) {
  const monHocId = chuongTrinhMon?.monHocId || chuongTrinhMon?.monId || chuongTrinhMon?.monHoc?.id || null
  return timDongTheoId('monHoc', monHocId)
}

function chonMonHocTheoChuongTrinhMon(chuongTrinhMon) {
  const monHoc = timMonHocCuaChuongTrinhMon(chuongTrinhMon)
  if (monHoc && String(selected.monHoc?.id || '') !== String(monHoc.id)) {
    selectEntity('monHoc', monHoc)
  }
}

function taoParamsTangSau(params = {}) {
  return Object.fromEntries(
      Object.entries(params).filter(([, value]) => value !== null && value !== undefined && value !== '')
  )
}

function napLuaChonTheoRoute() {
  const nganh = timDongTheoId('nganh', route.params.nganhId)
  if (nganh && String(selected.nganh?.id || '') !== String(nganh.id)) selectEntity('nganh', nganh)

  const chuongTrinh = timDongTheoId('chuongTrinh', route.params.chuongTrinhId)
  if (chuongTrinh && String(selected.chuongTrinh?.id || '') !== String(chuongTrinh.id)) {
    selectEntity('chuongTrinh', chuongTrinh)

    const trinhDo = timDongTheoId('trinhDoDaoTao', chuongTrinh.trinhDoId)
    if (trinhDo) selectEntity('trinhDoDaoTao', trinhDo)

    const loai = timDongTheoId('loaiChuongTrinh', chuongTrinh.loaiChuongTrinhId)
    if (loai) selectEntity('loaiChuongTrinh', loai)
  }
  const khungKyTheoQuery = timDongTheoId('khungKy', route.query.khungKyId)
  if (khungKyTheoQuery && String(selected.khungKy?.id || '') !== String(khungKyTheoQuery.id)) {
    selectEntity('khungKy', khungKyTheoQuery)
  }
  const version = timDongTheoId('chuongTrinhVersion', route.params.versionId)
  if (version && String(selected.chuongTrinhVersion?.id || '') !== String(version.id)) selectEntity('chuongTrinhVersion', version)

  const monHoc = timDongTheoId('monHoc', route.params.monHocId)
  if (monHoc && String(selected.monHoc?.id || '') !== String(monHoc.id)) selectEntity('monHoc', monHoc)

  const chuongTrinhMon = timDongTheoId('chuongTrinhMon', route.params.chuongTrinhMonId)
  if (chuongTrinhMon && String(selected.chuongTrinhMon?.id || '') !== String(chuongTrinhMon.id)) {
    selectEntity('chuongTrinhMon', chuongTrinhMon)
    chonMonHocTheoChuongTrinhMon(chuongTrinhMon)
  }

  if (!route.params.monHocId && selected.chuongTrinhMon?.id && !selected.monHoc?.id) {
    chonMonHocTheoChuongTrinhMon(selected.chuongTrinhMon)
  }

  const trinhDoTheoQuery = timDongTheoId('trinhDoDaoTao', route.query.trinhDoId)
  if (trinhDoTheoQuery) selectEntity('trinhDoDaoTao', trinhDoTheoQuery)

  const loaiTheoQuery = timDongTheoId('loaiChuongTrinh', route.query.loaiChuongTrinhId)
  if (loaiTheoQuery) selectEntity('loaiChuongTrinh', loaiTheoQuery)
}

watch(
    () => [route.fullPath, duLieu.value.nganh.length, duLieu.value.chuongTrinh.length, duLieu.value.chuongTrinhVersion.length, duLieu.value.monHoc.length, duLieu.value.chuongTrinhMon.length],
    () => napLuaChonTheoRoute(),
    { immediate: true }
)

function veTang(soTang) {
  if (soTang <= 1) {
    router.push({ name: 'DaoTao.XemChuongTrinh' })
    return
  }

  if (soTang === 2 && selected.nganh?.id) {
    router.push({ name: 'DaoTao.XemChuongTrinh.CauHinh', params: { nganhId: selected.nganh.id } })
    return
  }

  if (soTang === 3 && selected.nganh?.id) {
    router.push({ name: 'DaoTao.XemChuongTrinh.ChuongTrinh', params: { nganhId: selected.nganh.id }, query: { trinhDoId: selected.trinhDoDaoTao?.id || undefined, loaiChuongTrinhId: selected.loaiChuongTrinh?.id || undefined } })
    return
  }

  if (soTang === 4 && selected.nganh?.id && selected.chuongTrinh?.id) {
    router.push({ name: 'DaoTao.XemChuongTrinh.Version', params: { nganhId: selected.nganh.id, chuongTrinhId: selected.chuongTrinh.id } })
    return
  }

  if (soTang >= 5 && soTang <= 7 && coTheVeTangTongQuan.value) {
    const routeName = soTang === 5 ? 'DaoTao.XemChuongTrinh.TongQuan' : (soTang === 6 ? 'DaoTao.XemChuongTrinh.KhungCauTruc' : 'DaoTao.XemChuongTrinh.Mon')
    router.push({ name: routeName, params: { nganhId: selected.nganh.id, chuongTrinhId: selected.chuongTrinh.id, versionId: selected.chuongTrinhVersion.id } })
    return
  }

  if (soTang === 8 && coTheVeTangTongQuan.value) {
    const monHocId = selected.monHoc?.id || selected.chuongTrinhMon?.monHocId || selected.chuongTrinhMon?.monId || null
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusGoc',
      params: taoParamsTangSau({
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id,
        monHocId
      })
    })
    return
  }

  if (soTang === 9 && coTheVeTangTongQuan.value) {
    router.push({
      name: 'DaoTao.XemChuongTrinh.SyllabusApDung',
      params: taoParamsTangSau({
        nganhId: selected.nganh.id,
        chuongTrinhId: selected.chuongTrinh.id,
        versionId: selected.chuongTrinhVersion.id,
        chuongTrinhMonId: selected.chuongTrinhMon?.id || null
      })
    })
  }
}

function denKhungCauTruc() {
  if (!coTheVeTangTongQuan.value) return
  router.push({ name: 'DaoTao.XemChuongTrinh.KhungCauTruc', params: { nganhId: selected.nganh.id, chuongTrinhId: selected.chuongTrinh.id, versionId: selected.chuongTrinhVersion.id } })
}

function denMonTrongChuongTrinh() {
  if (!coTheVeTangTongQuan.value) return
  router.push({ name: 'DaoTao.XemChuongTrinh.Mon', params: { nganhId: selected.nganh.id, chuongTrinhId: selected.chuongTrinh.id, versionId: selected.chuongTrinhVersion.id } })
}

const groups = computed(() => {
  if (tangHienTai.value === 1) {
    return [{
      key: 'tang-1-nganh',
      title: 'Tầng 1: Ngành',
      description: 'Bấm Xem thêm tại một ngành để chuyển sang tầng cấu hình chương trình.',
      mau: 'mau-xanh',
      tables: [taoBang('nganh', { canView: true })]
    }]
  }

  if (tangHienTai.value === 2) {
    return [{
      key: 'tang-2-cau-hinh',
      title: 'Tầng 2: Cấu hình chương trình',
      description: 'Chọn trình độ đào tạo và loại chương trình. Khi đủ 2 lựa chọn, dùng nút Tiếp tục xem Chương trình ở đầu hoặc cuối tầng.',
      mau: 'mau-tim',
      tables: [
        taoBang('trinhDoDaoTao', { canView: false, canToggleSave: false, canShowSavedStatus: true, parentText: `Ngành đang xem: ${selected.nganh?.tenNganh || '-'}` }),
        taoBang('loaiChuongTrinh', { canView: false, canToggleSave: false, canShowSavedStatus: true, parentText: `Ngành đang xem: ${selected.nganh?.tenNganh || '-'}` })
      ]
    }]
  }

  if (tangHienTai.value === 3) {
    return [{
      key: 'tang-3-chuong-trinh',
      title: 'Tầng 3: Chương trình',
      description: 'Chỉ hiển thị chương trình thuộc ngành, trình độ và loại chương trình đang chọn.',
      mau: 'mau-luc',
      tables: [
        taoBang('chuongTrinh', {
          parentValues: chuongTrinhParent.value,
          filterValues: chuongTrinhFilter.value,
          parentText: chuongTrinhParentText.value,
          canView: true,
          disabled: !selected.nganh || !selected.trinhDoDaoTao || !selected.loaiChuongTrinh,
          disabledText: 'Cần chọn Ngành, Trình độ, Loại chương trình trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 4) {
    return [{
      key: 'tang-4-version',
      title: 'Tầng 4: Version chương trình',
      description: 'Bấm Xem thêm tại version để vào tổng quan version.',
      mau: 'mau-cam',
      tables: [
        taoBang('chuongTrinhVersion', {
          parentValues: chuongTrinhVersionParent.value,
          filterValues: chuongTrinhVersionFilter.value,
          parentText: chuongTrinhVersionParentText.value,
          canView: true,
          disabled: !selected.chuongTrinh,
          disabledText: 'Cần chọn Chương trình trước.'
        })
      ]
    }]
  }

  if (tangHienTai.value === 5) {
    return [{
      key: 'tang-5-tong-quan',
      title: 'Tầng 5: Thông tin tổng quan version',
      description: 'Các bảng gốc dùng chung và bảng áp dụng vào version. Dữ liệu được lọc theo version đang chọn.',
      mau: 'mau-hong',
      tables: [
        taoBang('syllabusChuongTrinhGoc', { linkParentValues: versionParent.value, parentText: `${versionParentText.value} | Kho syllabus chương trình gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('syllabusChuongTrinh', { parentValues: syllabusChuongTrinhParent.value, filterValues: versionFilter.value, parentText: syllabusChuongTrinhParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('mucTieuChuongTrinhGoc', { parentText: `${versionParentText.value} | Kho mục tiêu gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('chuongTrinhVersionMucTieu', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('mucTieuChuongTrinh', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('nangLucDauRaGoc', { parentText: `${versionParentText.value} | Kho năng lực gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('chuongTrinhVersionNangLuc', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('nangLucDauRa', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('viTriViecLamGoc', { parentText: `${versionParentText.value} | Kho vị trí việc làm gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('chuongTrinhVersionViTriViecLam', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('viTriViecLam', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('dieuKienTotNghiepGoc', { parentText: `${versionParentText.value} | Kho điều kiện tốt nghiệp gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('chuongTrinhVersionDieuKienTotNghiep', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('dieuKienTotNghiep', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' })
      ]
    }]
  }

  if (tangHienTai.value === 6) {
    return [{
      key: 'tang-6-khung-cau-truc',
      title: 'Tầng 6: Khung cấu trúc đào tạo',
      description: 'Khung kỳ, nhóm kiến thức và nhóm tự chọn được lọc theo version. Bấm Xem thêm để sang môn trong chương trình.',
      mau: 'mau-vang',
      tables: [
        taoBang('khungKyGoc', { linkParentValues: khungKyParent.value, parentText: `${versionParentText.value} | Kho khung kỳ gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('khungKy', {
          parentValues: khungKyParent.value,
          filterValues: khungKyFilter.value,
          parentText: khungKyParentText.value,
          canView: true,
          viewLabel: 'Xem môn của kỳ',
          disabled: !selected.chuongTrinhVersion,
          disabledText: 'Cần chọn Version trước.'
        }),
        taoBang('nhomKienThucGoc', { linkParentValues: versionParent.value, parentText: `${versionParentText.value} | Kho nhóm kiến thức gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('nhomKienThuc', { parentValues: nhomKienThucParent.value, filterValues: versionFilter.value, parentText: nhomKienThucParentText.value, canView: false, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('nhomTuChonGoc', { linkParentValues: versionParent.value, parentText: `${versionParentText.value} | Kho nhóm tự chọn gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('nhomTuChon', { parentValues: nhomTuChonParent.value, filterValues: versionFilter.value, parentText: nhomTuChonParentText.value, canView: false, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' })
      ]
    }]
  }

  if (tangHienTai.value === 7) {
    return [{
      key: 'tang-7-mon-trong-chuong-trinh',
      title: 'Tầng 7: Môn trong chương trình',
      description: 'Lọc môn trong chương trình theo version/kỳ/nhóm. Bấm Xem thêm ở Môn học để xem syllabus gốc, hoặc ở Chương trình môn để xem syllabus áp dụng.',
      mau: 'mau-ngoc',
      tables: [
        taoBang('monHoc', { parentText: monHocParentText.value, canView: true, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('chuongTrinhMon', { parentValues: chuongTrinhMonParent.value, filterValues: chuongTrinhMonViewFilter.value, parentText: chuongTrinhMonParentText.value, canView: true, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần chọn Version trước.' }),
        taoBang('monTuChon', { parentValues: monTuChonParent.value, parentText: monTuChonParentText.value, disabled: !selected.nhomTuChon || !selected.chuongTrinhMon, disabledText: 'Cần chọn Nhóm tự chọn và Môn trong chương trình trước.' }),
        taoBang('monTienQuyet', { parentValues: monTienQuyetParent.value, filterValues: chuongTrinhMonOnlyFilter.value, parentText: chuongTrinhMonOnlyParentText.value, disabled: !selected.chuongTrinhMon, disabledText: 'Cần chọn Môn trong chương trình trước.' }),
        taoBang('quyDoiDiemMau', { parentValues: {}, filterValues: {}, linkParentValues: chuongTrinhMonOnlyParent.value, parentText: `${chuongTrinhMonOnlyParentText.value} | Kho Quy đổi điểm mẫu gốc dùng chung. Chọn Môn trong chương trình rồi bấm Lưu tại mẫu để đẩy thẳng dữ liệu xuống bảng 5.5 Quy đổi điểm.`, disabled: false, canToggleSave: true, canSelect: false }),
        taoBang('quyDoiDiem', { parentValues: chuongTrinhMonOnlyParent.value, filterValues: chuongTrinhMonOnlyFilter.value, parentText: chuongTrinhMonOnlyParentText.value, disabled: !selected.chuongTrinhMon, disabledText: 'Cần chọn Môn trong chương trình trước.' })
      ]
    }]
  }

  if (tangHienTai.value === 8) {
    return [{
      key: 'tang-8-syllabus-goc',
      title: 'Tầng 8: Syllabus môn học gốc',
      description: 'Mẫu syllabus gốc tái sử dụng cho môn học, kèm chương/bài gốc, điều kiện gốc, tài liệu gốc.',
      mau: 'mau-troi',
      tables: [
        taoBang('syllabusMonHocGoc', { parentValues: syllabusMonHocGocParent.value, filterValues: syllabusMonHocGocFilter.value, parentText: syllabusMonHocGocParentText.value, canToggleSave: false, canShowSavedStatus: true, disabled: !selected.monHoc, disabledText: 'Cần chọn Môn học trước.' }),
        taoBang('syllabusMonHocGocChuongBai', { parentValues: syllabusMonHocGocChiTietParent.value, filterValues: syllabusMonHocGocChiTietFilter.value, parentText: syllabusMonHocGocChiTietText.value, disabled: !selected.syllabusMonHocGoc, disabledText: 'Cần chọn Syllabus môn học gốc trước.' }),
        taoBang('dieuKienMonHocGoc', { linkParentValues: syllabusMonHocGocChiTietParent.value, parentText: `${syllabusMonHocGocChiTietText.value} | Kho điều kiện môn học gốc dùng chung.`, disabled: !selected.syllabusMonHocGoc, disabledText: 'Cần chọn Syllabus môn học gốc trước.' }),
        taoBang('syllabusMonHocGocDieuKien', { parentValues: syllabusMonHocGocChiTietParent.value, filterValues: syllabusMonHocGocChiTietFilter.value, parentText: syllabusMonHocGocChiTietText.value, disabled: !selected.syllabusMonHocGoc, disabledText: 'Cần chọn Syllabus môn học gốc trước.' }),
        taoBang('taiLieuGoc', { linkParentValues: syllabusMonHocGocChiTietParent.value, parentText: `${syllabusMonHocGocChiTietText.value} | Kho tài liệu gốc dùng chung.`, disabled: !selected.syllabusMonHocGoc, disabledText: 'Cần chọn Syllabus môn học gốc trước.' }),
        taoBang('syllabusMonHocGocTaiLieu', { parentValues: syllabusMonHocGocChiTietParent.value, filterValues: syllabusMonHocGocChiTietFilter.value, parentText: syllabusMonHocGocChiTietText.value, disabled: !selected.syllabusMonHocGoc, disabledText: 'Cần chọn Syllabus môn học gốc trước.' })
      ]
    }]
  }

  return [{
    key: 'tang-9-syllabus-ap-dung',
    title: 'Tầng 9: Syllabus môn học áp dụng',
    description: 'Syllabus áp dụng theo môn trong chương trình, lọc bằng chương trình môn đang chọn.',
    mau: 'mau-xam',
    tables: [
      taoBang('syllabusMonHoc', { parentValues: syllabusMonHocApDungParent.value, filterValues: chuongTrinhMonOnlyFilter.value, parentText: chuongTrinhMonOnlyParentText.value, disabled: !selected.chuongTrinhMon, disabledText: 'Cần chọn Môn trong chương trình trước.' }),
      taoBang('syllabusChuongBai', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' }),
      taoBang('dieuKienMonHocGoc', { linkParentValues: syllabusMonParent.value, parentText: `${syllabusMonParentText.value} | Kho điều kiện môn học gốc dùng chung để gán vào Syllabus áp dụng.`, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' }),
      taoBang('syllabusMonHocDieuKien', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' }),
      taoBang('dieuKienMonHoc', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' }),
      taoBang('taiLieuGoc', { linkParentValues: syllabusMonParent.value, parentText: `${syllabusMonParentText.value} | Kho tài liệu gốc dùng chung để gán vào Syllabus áp dụng.`, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' }),
      taoBang('syllabusMonHocTaiLieu', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' }),
      taoBang('syllabusTaiLieu', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần chọn Syllabus môn học áp dụng trước.' })
    ]
  }]
})

const configs = {
  nganh: {
    title: '1. Ngành',
    description: 'Tạo hoặc chọn ngành làm cha đầu tiên của chương trình.',
    defaultForm: { maNganh: '', tenNganh: '', moTa: '' },
    fields: [
      { key: 'maNganh', label: 'Mã ngành', required: true },
      { key: 'tenNganh', label: 'Tên ngành', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'maNganh', message: 'Mã ngành đã tồn tại.' }
    ],
    columns: [
      { key: 'maNganh', label: 'Mã ngành' },
      { key: 'tenNganh', label: 'Tên ngành' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  trinhDoDaoTao: {
    title: '2.1. Trình độ đào tạo',
    defaultForm: { maTrinhDo: '', tenTrinhDo: '', moTa: '' },
    fields: [
      { key: 'maTrinhDo', label: 'Mã trình độ', required: true },
      { key: 'tenTrinhDo', label: 'Tên trình độ', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'maTrinhDo', message: 'Mã trình độ đã tồn tại.' }
    ],
    columns: [
      { key: 'maTrinhDo', label: 'Mã' },
      { key: 'tenTrinhDo', label: 'Tên trình độ' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  loaiChuongTrinh: {
    title: '2.2. Loại chương trình',
    defaultForm: { maLoai: '', tenLoai: '', soThang: null, soKy: null, moTa: '' },
    fields: [
      { key: 'maLoai', label: 'Mã loại', required: true },
      { key: 'tenLoai', label: 'Tên loại', required: true },
      { key: 'soThang', label: 'Số tháng', type: 'number' },
      { key: 'soKy', label: 'Số kỳ', type: 'number' },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'maLoai', message: 'Mã loại chương trình đã tồn tại.' }
    ],
    columns: [
      { key: 'maLoai', label: 'Mã' },
      { key: 'tenLoai', label: 'Tên loại' },
      { key: 'soThang', label: 'Số tháng' },
      { key: 'soKy', label: 'Số kỳ' }
    ]
  },
  chuongTrinh: {
    title: '2.3. Chương trình đào tạo',
    defaultForm: { nganhId: null, trinhDoId: null, loaiChuongTrinhId: null, maChuongTrinh: '', tenChuongTrinh: '', doiTuongTuyenSinh: '', thoiGianDaoTao: '' },
    fields: [
      { key: 'nganhId', required: true, label: 'Ngành', type: 'select', lookup: 'nganh', labelKey: ['maNganh', 'tenNganh'], locked: true },
      { key: 'trinhDoId', required: true, label: 'Trình độ', type: 'select', lookup: 'trinhDoDaoTao', labelKey: ['maTrinhDo', 'tenTrinhDo'], locked: true },
      { key: 'loaiChuongTrinhId', required: true, label: 'Loại CT', type: 'select', lookup: 'loaiChuongTrinh', labelKey: ['maLoai', 'tenLoai'], locked: true },
      { key: 'maChuongTrinh', label: 'Mã CTĐT', required: true },
      { key: 'tenChuongTrinh', label: 'Tên CTĐT', required: true },
      { key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh' },
      { key: 'thoiGianDaoTao', label: 'Thời gian đào tạo' }
    ],
    uniqueRules: [
      { field: 'maChuongTrinh', message: 'Mã chương trình đào tạo đã tồn tại.' }
    ],
    columns: [
      { key: 'maChuongTrinh', label: 'Mã CTĐT' },
      { key: 'tenChuongTrinh', label: 'Tên chương trình' },
      { key: 'tenNganh', label: 'Ngành' },
      { key: 'tenTrinhDo', label: 'Trình độ' },
      { key: 'tenLoaiChuongTrinh', label: 'Loại CT' }
    ]
  },
  chuongTrinhVersion: {
    title: '3. Phiên bản chương trình',
    defaultForm: { chuongTrinhId: null, maVersion: '', tenVersion: '', ngayApDung: '', ngayHetHieuLuc: '', soQuyetDinh: '', ngayQuyetDinh: '', nguoiKy: '', coQuanBanHanh: '', fileQuyetDinh: '', tongTinChi: null, tongSoGio: null, tongGioLyThuyet: null, tongGioThucHanh: null, tongGioKiemTra: null, laHienHanh: false },
    fields: [
      { key: 'chuongTrinhId', required: true, label: 'Chương trình', type: 'select', lookup: 'chuongTrinh', labelKey: ['maChuongTrinh', 'tenChuongTrinh'], locked: true },
      { key: 'maVersion', label: 'Mã version', required: true },
      { key: 'tenVersion', label: 'Tên version', required: true },
      { key: 'ngayApDung', label: 'Ngày áp dụng', type: 'date' },
      { key: 'ngayHetHieuLuc', label: 'Ngày hết hiệu lực', type: 'date' },
      { key: 'soQuyetDinh', label: 'Số quyết định' },
      { key: 'ngayQuyetDinh', label: 'Ngày quyết định', type: 'date' },
      { key: 'nguoiKy', label: 'Người ký' },
      { key: 'coQuanBanHanh', label: 'Cơ quan ban hành' },
      { key: 'tongTinChi', label: 'Tổng tín chỉ', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'tongSoGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'tongGioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'tongGioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'tongGioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'laHienHanh', label: 'Hiện hành', type: 'boolean' }
    ],
    uniqueRules: [
      { field: 'maVersion', scopeKeys: ['chuongTrinhId'], message: 'Mã version đã tồn tại trong Chương trình đang chọn.' }
    ],
    columns: [
      { key: 'maVersion', label: 'Mã version' },
      { key: 'tenVersion', label: 'Tên version' },
      { key: 'tenChuongTrinh', label: 'Chương trình' },
      { key: 'ngayApDung', label: 'Ngày áp dụng' },
      { key: 'tongTinChi', label: 'Tín chỉ' },
      { key: 'laHienHanh', label: 'Hiện hành' }
    ]
  },
  mucTieuChuongTrinhGoc: {
    title: '4.1. 8. Mục tiêu chương trình gốc / mẫu',
    description: 'Kho mục tiêu gốc dùng chung. Khi bấm ✓, hệ thống gán mục tiêu gốc vào Version qua bảng chuong_trinh_version_muc_tieu.',
    defaultForm: { ma: '', loai: 'chung', noiDung: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã mục tiêu', required: true },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã mục tiêu gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  mucTieuChuongTrinh: {
    title: '10. Mục tiêu chương trình nhập riêng - KHÔNG XÀI GỐC',
    description: 'Bảng nhập riêng trực tiếp theo Version/chương trình, không lấy từ bảng gốc và không dùng cơ chế tái sử dụng.',
    defaultForm: { chuongTrinhVersionId: null, loai: 'chung', noiDung: '', thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiMucTieu', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenVersion', label: 'Version' },
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  nangLucDauRaGoc: {
    title: '4.2. 11. Năng lực đầu ra gốc / mẫu',
    description: 'Kho năng lực gốc dùng chung. Khi bấm ✓, hệ thống gán năng lực gốc vào Version qua bảng chuong_trinh_version_nang_luc.',
    defaultForm: { ma: '', loai: 'co_ban', noiDung: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã năng lực', required: true },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiNangLuc', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', scopeKeys: ['loai'], message: 'Mã năng lực gốc đã tồn tại với loại năng lực này.' },
      { field: 'loai', scopeKeys: ['ma'], message: 'Loại năng lực đã tồn tại với mã năng lực này.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  nangLucDauRa: {
    title: '13. Năng lực đầu ra nhập riêng - KHÔNG XÀI GỐC',
    description: 'Bảng nhập riêng trực tiếp theo Version/chương trình, không lấy từ bảng gốc và không dùng cơ chế tái sử dụng.',
    defaultForm: { chuongTrinhVersionId: null, ma: '', loai: 'co_ban', noiDung: '', thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'ma', label: 'Mã năng lực', required: true },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiNangLuc', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenVersion', label: 'Version' },
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  viTriViecLamGoc: {
    title: '4.3. 14. Vị trí việc làm gốc / mẫu',
    description: 'Kho vị trí việc làm gốc dùng chung. Khi bấm ✓, hệ thống gán vị trí vào Version qua bảng chuong_trinh_version_vi_tri_viec_lam.',
    defaultForm: { ma: '', ten: '', moTa: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã vị trí', required: true },
      { key: 'ten', label: 'Tên vị trí', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã vị trí việc làm gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Vị trí' },
      { key: 'moTa', label: 'Mô tả' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  viTriViecLam: {
    title: '16. Vị trí việc làm nhập riêng - KHÔNG XÀI GỐC',
    description: 'Bảng nhập riêng trực tiếp theo Version/chương trình, không lấy từ bảng gốc và không dùng cơ chế tái sử dụng.',
    defaultForm: { chuongTrinhVersionId: null, ten: '', moTa: '', thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'ten', label: 'Tên vị trí', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenVersion', label: 'Version' },
      { key: 'ten', label: 'Vị trí' },
      { key: 'moTa', label: 'Mô tả' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  dieuKienTotNghiepGoc: {
    title: '4.4. 17. Điều kiện tốt nghiệp gốc / mẫu',
    description: 'Kho điều kiện tốt nghiệp gốc dùng chung. Khi bấm ✓, hệ thống gán điều kiện vào Version qua bảng chuong_trinh_version_dieu_kien_tot_nghiep.',
    defaultForm: { ma: '', noiDung: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã điều kiện', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã điều kiện tốt nghiệp gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'noiDung', label: 'Điều kiện' },
      { key: 'ghiChu', label: 'Ghi chú' },
      { key: 'createdAt', label: 'Ngày tạo' },
      { key: 'updatedAt', label: 'Ngày cập nhật' }
    ]
  },
  dieuKienTotNghiep: {
    title: '19. Điều kiện tốt nghiệp nhập riêng - KHÔNG XÀI GỐC',
    description: 'Bảng nhập riêng trực tiếp theo Version/chương trình, không lấy từ bảng gốc và không dùng cơ chế tái sử dụng.',
    defaultForm: { chuongTrinhVersionId: null, noiDung: '', thuTu: null, ghiChu: '' },
    fields: [
      { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenVersion', label: 'Version' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  chuongTrinhVersionMucTieu: { title: '4.4.1. Mục tiêu gán vào Version - TÁI SỬ DỤNG', description: 'Bảng nối dùng để chọn mục tiêu gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.', defaultForm: { chuongTrinhVersionId: null, mucTieuGocId: null, thuTu: null, ghiChu: '' }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'mucTieuGocId', label: 'Mục tiêu gốc', type: 'select', lookup: 'mucTieuChuongTrinhGoc', labelKey: ['ma', 'noiDung'], required: true }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenMucTieuGoc', label: 'Mục tiêu gốc' }, { key: 'thuTu', label: 'Thứ tự' }, { key: 'ghiChu', label: 'Ghi chú' } ] },
  chuongTrinhVersionNangLuc: { title: '4.5.1. Năng lực gán vào Version - TÁI SỬ DỤNG', description: 'Bảng nối dùng để chọn năng lực gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.', defaultForm: { chuongTrinhVersionId: null, nangLucGocId: null, thuTu: null, ghiChu: '' }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'nangLucGocId', label: 'Năng lực gốc', type: 'select', lookup: 'nangLucDauRaGoc', labelKey: ['ma', 'noiDung'], required: true }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenNangLucGoc', label: 'Năng lực gốc' }, { key: 'thuTu', label: 'Thứ tự' }, { key: 'ghiChu', label: 'Ghi chú' } ] },
  chuongTrinhVersionViTriViecLam: { title: '4.6.1. Vị trí việc làm gán vào Version - TÁI SỬ DỤNG', description: 'Bảng nối dùng để chọn vị trí việc làm gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.', defaultForm: { chuongTrinhVersionId: null, viTriGocId: null, thuTu: null, ghiChu: '' }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'viTriGocId', label: 'Vị trí gốc', type: 'select', lookup: 'viTriViecLamGoc', labelKey: ['ma', 'ten'], required: true }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenViTriGoc', label: 'Vị trí gốc' }, { key: 'thuTu', label: 'Thứ tự' }, { key: 'ghiChu', label: 'Ghi chú' } ] },
  chuongTrinhVersionDieuKienTotNghiep: { title: '4.7.1. Điều kiện tốt nghiệp gán vào Version - TÁI SỬ DỤNG', description: 'Bảng nối dùng để chọn điều kiện tốt nghiệp gốc và gán/tái sử dụng vào Version, không phải bảng nhập riêng.', defaultForm: { chuongTrinhVersionId: null, dieuKienGocId: null, thuTu: null, ghiChu: '' }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'dieuKienGocId', label: 'Điều kiện gốc', type: 'select', lookup: 'dieuKienTotNghiepGoc', labelKey: ['ma', 'noiDung'], required: true }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenDieuKienGoc', label: 'Điều kiện gốc' }, { key: 'thuTu', label: 'Thứ tự' }, { key: 'ghiChu', label: 'Ghi chú' } ] },
  syllabusChuongTrinhGoc: { title: '4.3.1. Syllabus chương trình gốc / mẫu', description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.', defaultForm: { ma: '', ten: '', mucTieu: '', doiTuongTuyenSinh: '', thoiGianDaoTao: '', khoiLuongKienThuc: '', dieuKienTotNghiep: '', phuongPhapDaoTao: '', phuongPhapDanhGia: '', huongDanThucHien: '', ghiChu: '' }, fields: [ { key: 'ma', label: 'Mã', required: true }, { key: 'ten', label: 'Tên', required: true }, { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true }, { key: 'doiTuongTuyenSinh', label: 'Đối tượng tuyển sinh' }, { key: 'thoiGianDaoTao', label: 'Thời gian đào tạo' }, { key: 'khoiLuongKienThuc', label: 'Khối lượng kiến thức' }, { key: 'dieuKienTotNghiep', label: 'Điều kiện tốt nghiệp', type: 'textarea', wide: true }, { key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo', type: 'textarea', wide: true }, { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true }, { key: 'huongDanThucHien', label: 'Hướng dẫn thực hiện', type: 'textarea', wide: true }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], uniqueRules: [ { field: 'ma', message: 'Mã syllabus chương trình gốc đã tồn tại.' } ], columns: [ { key: 'ma', label: 'Mã' }, { key: 'ten', label: 'Tên' }, { key: 'mucTieu', label: 'Mục tiêu' }, { key: 'createdAt', label: 'Ngày tạo' }, { key: 'updatedAt', label: 'Ngày cập nhật' } ] },
  syllabusChuongTrinh: { title: '4.3.2. Syllabus chương trình áp dụng', description: 'Bảng lưu/áp dụng đã gắn vào Version thật.', defaultForm: { chuongTrinhVersionId: null, syllabusChuongTrinhGocId: null, moTaTongQuan: '', mucDich: '', yeuCauDaoTao: '', phuongPhapDaoTao: '', ghiChu: '' }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'syllabusChuongTrinhGocId', label: 'Syllabus gốc', type: 'select', lookup: 'syllabusChuongTrinhGoc', labelKey: ['ma', 'ten'], locked: true }, { key: 'moTaTongQuan', label: 'Mô tả tổng quan', type: 'textarea', wide: true }, { key: 'mucDich', label: 'Mục đích', type: 'textarea', wide: true }, { key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo', type: 'textarea', wide: true }, { key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo', type: 'textarea', wide: true }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenSyllabusChuongTrinhGoc', label: 'Syllabus gốc' }, { key: 'moTaTongQuan', label: 'Mô tả' }, { key: 'mucDich', label: 'Mục đích' }, { key: 'phuongPhapDaoTao', label: 'Phương pháp' } ] },
  nhomKienThucGoc: { title: '4.2. Nhóm kiến thức gốc / mẫu', description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.', defaultForm: { ma: '', ten: '', loaiNhom: 'chung', moTa: '' }, fields: [ { key: 'ma', label: 'Mã nhóm gốc', required: true }, { key: 'ten', label: 'Tên nhóm gốc', required: true }, { key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom' }, { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true } ], uniqueRules: [ { field: 'ma', message: 'Mã nhóm kiến thức gốc đã tồn tại.' } ], columns: [ { key: 'ma', label: 'Mã' }, { key: 'ten', label: 'Tên nhóm gốc' }, { key: 'loaiNhom', label: 'Loại' }, { key: 'moTa', label: 'Mô tả' }, { key: 'createdAt', label: 'Ngày tạo' }, { key: 'updatedAt', label: 'Ngày cập nhật' } ] },
  nhomKienThuc: { title: '4.2.1. Nhóm kiến thức gán vào Version - TÁI SỬ DỤNG', description: 'Bảng lưu/áp dụng đã gắn vào Version thật.', defaultForm: { chuongTrinhVersionId: null, nhomKienThucGocId: null, ma: '', ten: '', thuTu: null, loaiNhom: 'chung', tongTinChi: null, tongSoGio: null, tongGioLyThuyet: null, tongGioThucHanh: null, tongGioKiemTra: null }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'nhomKienThucGocId', label: 'Nhóm kiến thức gốc', type: 'select', lookup: 'nhomKienThucGoc', labelKey: ['ma', 'ten'], locked: true }, { key: 'ma', label: 'Mã nhóm', required: true }, { key: 'ten', label: 'Tên nhóm', required: true }, { key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom' }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 }, { key: 'tongTinChi', label: 'Tổng TC', type: 'number', max: 9999.9, step: 0.1 }, { key: 'tongSoGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1 }, { key: 'tongGioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1 }, { key: 'tongGioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1 }, { key: 'tongGioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1 } ], uniqueRules: [ { field: 'ma', scopeKeys: ['chuongTrinhVersionId'], message: 'Mã nhóm kiến thức đã tồn tại trong Version đang chọn.' }, { field: 'thuTu', scopeKeys: ['chuongTrinhVersionId'], message: 'Thứ tự nhóm kiến thức đã tồn tại trong Version đang chọn.' }, { field: 'nhomKienThucGocId', scopeKeys: ['chuongTrinhVersionId'], message: 'Nhóm kiến thức gốc này đã được gán vào Version đang chọn.' } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenNhomKienThucGoc', label: 'Nhóm gốc' }, { key: 'ma', label: 'Mã' }, { key: 'ten', label: 'Tên nhóm' }, { key: 'loaiNhom', label: 'Loại' }, { key: 'tongTinChi', label: 'TC' }, { key: 'tongSoGio', label: 'Giờ' } ] },
  nhomTuChonGoc: { title: '4.8. Nhóm tự chọn gốc / mẫu', description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.', defaultForm: { ma: '', ten: '', moTa: '' }, fields: [ { key: 'ma', label: 'Mã nhóm tự chọn gốc', required: true }, { key: 'ten', label: 'Tên nhóm tự chọn gốc', required: true }, { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true } ], uniqueRules: [ { field: 'ma', message: 'Mã nhóm tự chọn gốc đã tồn tại.' } ], columns: [ { key: 'ma', label: 'Mã' }, { key: 'ten', label: 'Tên nhóm tự chọn gốc' }, { key: 'moTa', label: 'Mô tả' }, { key: 'createdAt', label: 'Ngày tạo' }, { key: 'updatedAt', label: 'Ngày cập nhật' } ] },
  nhomTuChon: { title: '4.8.1. Nhóm tự chọn gán vào Version - TÁI SỬ DỤNG', description: 'Bảng lưu/áp dụng đã gắn vào Version thật.', defaultForm: { chuongTrinhVersionId: null, nhomTuChonGocId: null, ten: '', soMonChon: null, soTinChiCanDat: null, ghiChu: '' }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'nhomTuChonGocId', label: 'Nhóm tự chọn gốc', type: 'select', lookup: 'nhomTuChonGoc', labelKey: ['ma', 'ten'], locked: true }, { key: 'ten', label: 'Tên nhóm', required: true }, { key: 'soMonChon', label: 'Số môn chọn', type: 'number' }, { key: 'soTinChiCanDat', label: 'Số TC cần đạt', type: 'number', max: 9999.9, step: 0.1 }, { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true } ], uniqueRules: [ { field: 'ten', scopeKeys: ['chuongTrinhVersionId'], message: 'Tên nhóm tự chọn đã tồn tại trong Version đang chọn.' }, { field: 'nhomTuChonGocId', scopeKeys: ['chuongTrinhVersionId'], message: 'Nhóm tự chọn gốc này đã được gán vào Version đang chọn.' } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenNhomTuChonGoc', label: 'Nhóm gốc' }, { key: 'ten', label: 'Tên nhóm' }, { key: 'soMonChon', label: 'Số môn chọn' }, { key: 'soTinChiCanDat', label: 'TC cần đạt' }, { key: 'ghiChu', label: 'Ghi chú' } ] },
  khungKyGoc: { title: '4.1. Khung kỳ gốc / mẫu', description: 'Bảng gốc/mẫu dùng chung để chọn, tái sử dụng.', defaultForm: { maKy: '', tenKy: '', thuTu: null, moTa: '' }, fields: [ { key: 'maKy', label: 'Mã kỳ', required: true }, { key: 'tenKy', label: 'Tên kỳ', required: true }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1, required: true }, { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true } ], uniqueRules: [ { field: 'maKy', message: 'Mã kỳ gốc đã tồn tại.' }, { field: 'thuTu', message: 'Thứ tự kỳ gốc đã tồn tại.' } ], columns: [ { key: 'maKy', label: 'Mã kỳ' }, { key: 'tenKy', label: 'Tên kỳ' }, { key: 'thuTu', label: 'Thứ tự' }, { key: 'moTa', label: 'Mô tả' }, { key: 'createdAt', label: 'Ngày tạo' }, { key: 'updatedAt', label: 'Ngày cập nhật' } ] },
  khungKy: { title: '4.1.1. Khung kỳ gán vào Version - TÁI SỬ DỤNG', description: 'Bảng lưu/áp dụng đã gắn vào Version thật.', defaultForm: { chuongTrinhVersionId: null, loaiChuongTrinhId: null, khungKyGocId: null, maKy: '', tenKy: '', thuTu: null }, fields: [ { key: 'chuongTrinhVersionId', label: 'Version', type: 'select', lookup: 'chuongTrinhVersion', labelKey: ['maVersion', 'tenVersion'], locked: true }, { key: 'loaiChuongTrinhId', label: 'Loại CT', type: 'select', lookup: 'loaiChuongTrinh', labelKey: ['maLoai', 'tenLoai'], locked: true }, { key: 'khungKyGocId', label: 'Khung kỳ gốc', type: 'select', lookup: 'khungKyGoc', labelKey: ['maKy', 'tenKy'], locked: true }, { key: 'maKy', label: 'Mã kỳ', required: true }, { key: 'tenKy', label: 'Tên kỳ', required: true }, { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1, required: true } ], uniqueRules: [ { field: 'maKy', scopeKeys: ['chuongTrinhVersionId'], message: 'Mã kỳ đã tồn tại trong Version đang chọn.' }, { field: 'thuTu', scopeKeys: ['chuongTrinhVersionId'], message: 'Thứ tự khung kỳ đã tồn tại trong Version đang chọn.' }, { field: 'khungKyGocId', scopeKeys: ['chuongTrinhVersionId'], message: 'Khung kỳ gốc này đã được gán vào Version đang chọn.' } ], columns: [ { key: 'tenVersion', label: 'Version' }, { key: 'tenKhungKyGoc', label: 'Kỳ gốc' }, { key: 'maKy', label: 'Mã kỳ' }, { key: 'tenKy', label: 'Tên kỳ' }, { key: 'thuTu', label: 'Thứ tự' } ] },
  monHoc: {
    title: '5.1. Môn học gốc',
    allowToggleSelect: true,
    defaultForm: { maMon: '', tenMon: '', moTa: '' },
    fields: [
      { key: 'maMon', label: 'Mã môn', required: true },
      { key: 'tenMon', label: 'Tên môn', required: true },
      { key: 'moTa', label: 'Mô tả', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'maMon', message: 'Mã môn học gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'maMon', label: 'Mã môn' },
      { key: 'tenMon', label: 'Tên môn' },
      { key: 'moTa', label: 'Mô tả' }
    ]
  },
  chuongTrinhMon: {
    title: '5.2. Chương trình môn',
    defaultForm: { chuongTrinhVersionId: null, monHocId: null, maMonTrongCt: '', khungKyId: null, nhomKienThucId: null, loai: 'bat_buoc', loaiHocPhan: 'mon_hoc', loaiPhamVi: 'mon_chuyen_nganh', batBuoc: true, laMonDieuKien: false, thuTu: null, soTinChi: null, tongGio: null, gioLyThuyet: null, gioThucHanh: null, gioKiemTra: null, ghiChu: '' },
    fields: [
      { key: 'maMonTrongCt', label: 'Mã môn CT', required: true },
      { key: 'loai', label: 'Loại môn', type: 'select', lookup: 'loaiMon' },
      { key: 'loaiHocPhan', label: 'Loại học phần', type: 'select', lookup: 'loaiHocPhan' },
      { key: 'loaiPhamVi', label: 'Phạm vi môn', type: 'select', lookup: 'loaiPhamViMon' },
      { key: 'batBuoc', label: 'Bắt buộc', type: 'boolean' },
      { key: 'laMonDieuKien', label: 'Môn điều kiện', type: 'boolean' },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'soTinChi', label: 'Số tín chỉ', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'tongGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'gioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'gioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'gioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    requiredParentKeys: [
      { key: 'chuongTrinhVersionId', label: 'Version chương trình' },
      { key: 'monHocId', label: 'Môn học gốc' },
      { key: 'khungKyId', label: 'Khung kỳ' }
    ],
    uniqueRules: [
      { field: 'maMonTrongCt', scopeKeys: ['chuongTrinhVersionId'], message: 'Mã môn CT đã tồn tại trong Version chương trình này.' },
      { field: 'thuTu', scopeKeys: ['chuongTrinhVersionId', 'khungKyId'], message: 'Thứ tự môn trong Chương trình môn đã tồn tại trong Version/Kỳ đang chọn.' }
    ],
    columns: [
      { key: 'maMonTrongCt', label: 'Mã môn CT' },
      { key: 'tenMon', label: 'Môn học' },
      { key: 'tenVersion', label: 'Version' },
      { key: 'tenKy', label: 'Kỳ' },
      { key: 'tenNhomKienThuc', label: 'Nhóm KT' },
      { key: 'loai', label: 'Loại' },
      { key: 'loaiPhamVi', label: 'Phạm vi' },
      { key: 'soTinChi', label: 'TC' },
      { key: 'tongGio', label: 'Giờ' }
    ]
  },
  monTuChon: {
    title: '5.3. Môn tự chọn',
    defaultForm: { nhomId: null, chuongTrinhMonId: null },
    fields: [
      { key: 'nhomId', required: true, label: 'Nhóm tự chọn', type: 'select', lookup: 'nhomTuChon', labelKey: ['ten'], locked: true },
      { key: 'chuongTrinhMonId', required: true, label: 'Môn CT', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMon'], locked: true }
    ],
    uniqueRules: [
      { field: 'chuongTrinhMonId', scopeKeys: ['nhomId'], message: 'Môn tự chọn này đã tồn tại trong Nhóm tự chọn đang chọn.' }
    ],
    columns: [
      { key: 'tenNhomTuChon', label: 'Nhóm tự chọn' },
      { key: 'tenChuongTrinhMon', label: 'Môn CT' }
    ]
  },
  monTienQuyet: {
    title: '5.4. Môn tiên quyết',
    defaultForm: { monId: null, monDieuKienId: null, loai: 'tien_quyet', ghiChu: '' },
    fields: [
      { key: 'monDieuKienId', label: 'Môn điều kiện', type: 'select', lookup: 'chuongTrinhMon', labelKey: ['maMonTrongCt', 'tenMon'], required: true },
      { key: 'loai', label: 'Loại điều kiện', type: 'select', lookup: 'loaiDieuKien' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'monDieuKienId', scopeKeys: ['monId'], message: 'Môn điều kiện đã tồn tại cho Môn trong chương trình đang chọn.' }
    ],
    columns: [
      { key: 'tenMon', label: 'Môn chính' },
      { key: 'tenMonDieuKien', label: 'Môn điều kiện' },
      { key: 'loai', label: 'Loại' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  quyDoiDiem: {
    title: '5.5. Quy đổi điểm',
    defaultForm: { chuongTrinhMonId: null, nguongTu: null, nguongDen: null, diemQuyDoi: null, ketQua: 'dat', congThuc: '', ghiChu: '' },
    fields: [
      { key: 'nguongTu', label: 'Ngưỡng từ', type: 'number', max: 999.99, step: 0.01 },
      { key: 'nguongDen', label: 'Ngưỡng đến', type: 'number', max: 999.99, step: 0.01 },
      { key: 'diemQuyDoi', label: 'Điểm quy đổi', type: 'number', max: 999.99, step: 0.01 },
      { key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua' },
      { key: 'congThuc', label: 'Công thức' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenChuongTrinhMon', label: 'Môn CT' },
      { key: 'nguongTu', label: 'Từ' },
      { key: 'nguongDen', label: 'Đến' },
      { key: 'diemQuyDoi', label: 'Điểm' },
      { key: 'ketQua', label: 'Kết quả' }
    ]
  },
  quyDoiDiemMau: {
    title: '5.5.1. Quy đổi điểm mẫu gốc - TÁI SỬ DỤNG',
    defaultForm: { ma: '', ten: '', nguongTu: null, nguongDen: null, diemQuyDoi: null, ketQua: 'dat', congThuc: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã mẫu', required: true },
      { key: 'ten', label: 'Tên mẫu', required: true },
      { key: 'nguongTu', label: 'Ngưỡng từ', type: 'number', max: 999.99, step: 0.01 },
      { key: 'nguongDen', label: 'Ngưỡng đến', type: 'number', max: 999.99, step: 0.01 },
      { key: 'diemQuyDoi', label: 'Điểm quy đổi', type: 'number', max: 999.99, step: 0.01 },
      { key: 'ketQua', label: 'Kết quả', type: 'select', lookup: 'ketQua' },
      { key: 'congThuc', label: 'Công thức' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã quy đổi điểm mẫu đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã mẫu' },
      { key: 'ten', label: 'Tên mẫu' },
      { key: 'nguongTu', label: 'Từ' },
      { key: 'nguongDen', label: 'Đến' },
      { key: 'diemQuyDoi', label: 'Điểm' },
      { key: 'ketQua', label: 'Kết quả' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },

  syllabusMonHocGoc: {
    title: '5.1.1. Syllabus môn học gốc - TÁI SỬ DỤNG',
    defaultForm: { monHocId: null, ma: '', ten: '', viTri: '', tinhChat: '', mucTieu: '', phuongPhapDanhGia: '', dieuKienHoanThanh: '', huongDan: '', diemDatToiThieu: null, donViDiem: 'thang_10', tyLeChuyenCanToiThieu: null, batBuocDuThi: true, congThucQuyDoi: '', ghiChu: '' },
    fields: [
      { key: 'monHocId', label: 'Môn học gốc', type: 'select', lookup: 'monHoc', labelKey: ['maMon', 'tenMon'], locked: true },
      { key: 'ma', label: 'Mã syllabus gốc', required: true },
      { key: 'ten', label: 'Tên syllabus gốc', required: true },
      { key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true },
      { key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true },
      { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true },
      { key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true },
      { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu', type: 'number', max: 10, step: 0.01, maxBy: { donViDiem: { thang_10: 10, phan_tram: 100 } } },
      { key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem' },
      { key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', type: 'number', max: 100, step: 0.01 },
      { key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean' },
      { key: 'congThucQuyDoi', label: 'Công thức quy đổi' },
      { key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã syllabus môn học gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên syllabus gốc' },
      { key: 'tenMonHoc', label: 'Môn học' },
      { key: 'diemDatToiThieu', label: 'Điểm đạt' },
      { key: 'donViDiem', label: 'Đơn vị điểm' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusMonHocGocChuongBai: {
    title: '5.1.1.1. Chương/bài của syllabus gốc - TÁI SỬ DỤNG',
    defaultForm: { syllabusMonHocGocId: null, ten: '', noiDung: '', thuTu: null, tongGio: null, gioLyThuyet: null, gioThucHanh: null, gioKiemTra: null, ghiChu: '' },
    fields: [
      { key: 'syllabusMonHocGocId', label: 'Syllabus gốc', type: 'select', lookup: 'syllabusMonHocGoc', labelKey: ['ma', 'ten'], locked: true },
      { key: 'ten', label: 'Tên chương/bài', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'tongGio', label: 'Tổng giờ', type: 'number', max: 9999.99, step: 0.01 },
      { key: 'gioLyThuyet', label: 'Giờ LT', type: 'number', max: 9999.99, step: 0.01 },
      { key: 'gioThucHanh', label: 'Giờ TH', type: 'number', max: 9999.99, step: 0.01 },
      { key: 'gioKiemTra', label: 'Giờ KT', type: 'number', max: 9999.99, step: 0.01 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc' },
      { key: 'ten', label: 'Tên chương/bài' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'tongGio', label: 'Tổng giờ' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusMonHocGocDieuKien: {
    title: '5.1.1.2. Điều kiện của syllabus gốc - TÁI SỬ DỤNG',
    defaultForm: { syllabusMonHocGocId: null, dieuKienGocId: null, thuTu: null, ghiChu: '' },
    fields: [
      { key: 'syllabusMonHocGocId', label: 'Syllabus gốc', type: 'select', lookup: 'syllabusMonHocGoc', labelKey: ['ma', 'ten'], locked: true },
      { key: 'dieuKienGocId', label: 'Điều kiện gốc', type: 'select', lookup: 'dieuKienMonHocGoc', labelKey: ['ma', 'noiDung'], required: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc' },
      { key: 'tenDieuKienGoc', label: 'Điều kiện gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusMonHocGocTaiLieu: {
    title: '5.1.1.3. Tài liệu của syllabus gốc - TÁI SỬ DỤNG',
    defaultForm: { syllabusMonHocGocId: null, taiLieuGocId: null, thuTu: null, batBuoc: false, ghiChu: '' },
    fields: [
      { key: 'syllabusMonHocGocId', label: 'Syllabus gốc', type: 'select', lookup: 'syllabusMonHocGoc', labelKey: ['ma', 'ten'], locked: true },
      { key: 'taiLieuGocId', label: 'Tài liệu gốc', type: 'select', lookup: 'taiLieuGoc', labelKey: ['ma', 'ten'], required: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'batBuoc', label: 'Bắt buộc', type: 'boolean' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc' },
      { key: 'tenTaiLieuGoc', label: 'Tài liệu gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'batBuoc', label: 'Bắt buộc' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusMonHoc: {
    title: '5.6. Syllabus môn học',
    defaultForm: { chuongTrinhMonId: null, syllabusMonHocGocId: null, viTri: '', tinhChat: '', mucTieu: '', phuongPhapDanhGia: '', dieuKienHoanThanh: '', huongDan: '', diemDatToiThieu: null, donViDiem: 'thang_10', tyLeChuyenCanToiThieu: null, batBuocDuThi: true, congThucQuyDoi: '' },
    fields: [
      { key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true },
      { key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true },
      { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true },
      { key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true },
      { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu', type: 'number', max: 10, step: 0.01, maxBy: { donViDiem: { thang_10: 10, phan_tram: 100 } } },
      { key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem' },
      { key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', type: 'number', max: 999.99, step: 0.01 },
      { key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean' },
      { key: 'congThucQuyDoi', label: 'Công thức quy đổi' },
      { key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenChuongTrinhMon', label: 'Môn CT' },
      { key: 'tenSyllabusMonHocGoc', label: 'Syllabus gốc' },
      { key: 'viTri', label: 'Vị trí' },
      { key: 'tinhChat', label: 'Tính chất' },
      { key: 'diemDatToiThieu', label: 'Điểm đạt' },
      { key: 'donViDiem', label: 'Đơn vị điểm' }
    ]
  },
  dieuKienMonHoc: {
    title: '6.1. Điều kiện môn học cũ',
    defaultForm: { syllabusMonId: null, loai: 'khac', noiDung: '', thuTu: null },
    fields: [
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiDieuKienMonHoc' },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number' }
    ],
    uniqueRules: [
      { field: 'thuTu', scopeKeys: ['syllabusMonId'], message: 'Thứ tự điều kiện môn học đã tồn tại trong Syllabus môn đang chọn.' }
    ],
    columns: [
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
  dieuKienMonHocGoc: {
    title: '6.1. Điều kiện môn học gốc',
    defaultForm: { ma: '', loai: 'khac', noiDung: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã điều kiện' },
      { key: 'loai', label: 'Loại', type: 'select', lookup: 'loaiDieuKienMonHoc', required: true },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', required: true, wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã điều kiện môn học gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'loai', label: 'Loại' },
      { key: 'noiDung', label: 'Nội dung' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusMonHocDieuKien: {
    title: '6.1.1. Điều kiện gán vào syllabus môn - TÁI SỬ DỤNG',
    defaultForm: { syllabusMonId: null, dieuKienGocId: null, thuTu: null, ghiChu: '' },
    fields: [
      { key: 'syllabusMonId', label: 'Syllabus môn', type: 'select', lookup: 'syllabusMonHoc', labelKey: ['viTri', 'mucTieu'], locked: true },
      { key: 'dieuKienGocId', label: 'Điều kiện gốc', type: 'select', lookup: 'dieuKienMonHocGoc', labelKey: ['ma', 'noiDung'], required: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'dieuKienGocId', scopeKeys: ['syllabusMonId'], message: 'Điều kiện này đã được gán vào Syllabus môn đang chọn.' },
      { field: 'thuTu', scopeKeys: ['syllabusMonId'], message: 'Thứ tự điều kiện đã tồn tại trong Syllabus môn đang chọn.' }
    ],
    columns: [
      { key: 'tenDieuKienGoc', label: 'Điều kiện gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusChuongBai: {
    title: '6.2. Syllabus chương bài',
    defaultForm: { syllabusMonId: null, ten: '', tongGio: null, gioLyThuyet: null, gioThucHanh: null, gioKiemTra: null, noiDung: '', mucTieu: '', thuTu: null },
    fields: [
      { key: 'ten', label: 'Tên chương/bài', required: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number' },
      { key: 'tongGio', label: 'Tổng giờ', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'gioLyThuyet', label: 'Giờ LT', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'gioThucHanh', label: 'Giờ TH', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'gioKiemTra', label: 'Giờ KT', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'noiDung', label: 'Nội dung', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ten', scopeKeys: ['syllabusMonId'], message: 'Tên chương/bài đã tồn tại trong Syllabus môn đang chọn.' },
      { field: 'thuTu', scopeKeys: ['syllabusMonId'], message: 'Thứ tự chương/bài đã tồn tại trong Syllabus môn đang chọn.' }
    ],
    columns: [
      { key: 'ten', label: 'Tên chương/bài' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'tongGio', label: 'Tổng giờ' },
      { key: 'gioLyThuyet', label: 'LT' },
      { key: 'gioThucHanh', label: 'TH' },
      { key: 'gioKiemTra', label: 'KT' }
    ]
  },

  taiLieuGoc: {
    title: '6.3. Tài liệu gốc',
    defaultForm: { ma: '', ten: '', loai: 'GIAO_TRINH', tacGia: '', nhaXuatBan: '', namXuatBan: null, duongDan: '', ghiChu: '' },
    fields: [
      { key: 'ma', label: 'Mã tài liệu', required: true },
      { key: 'ten', label: 'Tên tài liệu', required: true },
      { key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'namXuatBan', label: 'Năm xuất bản', type: 'number', step: 1, min: 0 },
      { key: 'duongDan', label: 'Đường dẫn', wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ma', message: 'Mã tài liệu gốc đã tồn tại.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên tài liệu' },
      { key: 'loai', label: 'Loại' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'nhaXuatBan', label: 'NXB' },
      { key: 'namXuatBan', label: 'Năm' },
      { key: 'duongDan', label: 'Đường dẫn' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusMonHocTaiLieu: {
    title: '6.3.1. Tài liệu gán vào syllabus môn - TÁI SỬ DỤNG',
    defaultForm: { syllabusMonId: null, taiLieuGocId: null, thuTu: null, batBuoc: false, ghiChu: '' },
    fields: [
      { key: 'syllabusMonId', label: 'Syllabus môn', type: 'select', lookup: 'syllabusMonHoc', labelKey: ['viTri', 'mucTieu'], locked: true },
      { key: 'taiLieuGocId', label: 'Tài liệu gốc', type: 'select', lookup: 'taiLieuGoc', labelKey: ['ma', 'ten'], required: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 },
      { key: 'batBuoc', label: 'Bắt buộc', type: 'boolean' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'taiLieuGocId', scopeKeys: ['syllabusMonId'], message: 'Tài liệu này đã được gán vào Syllabus môn đang chọn.' },
      { field: 'thuTu', scopeKeys: ['syllabusMonId'], message: 'Thứ tự tài liệu đã tồn tại trong Syllabus môn đang chọn.' }
    ],
    columns: [
      { key: 'tenTaiLieuGoc', label: 'Tài liệu gốc' },
      { key: 'thuTu', label: 'Thứ tự' },
      { key: 'batBuoc', label: 'Bắt buộc' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  syllabusTaiLieu: {
    title: '6.4. Syllabus tài liệu cũ',
    defaultForm: { syllabusMonId: null, ten: '', tacGia: '', namXuatBan: null, nhaXuatBan: '', loai: 'GIAO_TRINH', ghiChu: '' },
    fields: [
      { key: 'ten', label: 'Tên tài liệu', required: true },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'namXuatBan', label: 'Năm xuất bản', type: 'number' },
      { key: 'nhaXuatBan', label: 'Nhà xuất bản' },
      { key: 'loai', label: 'Loại tài liệu', type: 'select', lookup: 'loaiTaiLieu' },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ten', scopeKeys: ['syllabusMonId'], message: 'Tên tài liệu đã tồn tại trong Syllabus môn đang chọn.' }
    ],
    columns: [
      { key: 'ten', label: 'Tên tài liệu' },
      { key: 'tacGia', label: 'Tác giả' },
      { key: 'namXuatBan', label: 'Năm' },
      { key: 'nhaXuatBan', label: 'NXB' },
      { key: 'loai', label: 'Loại' }
    ]
  }
}

</script>
<style scoped>
.xay-dung-page {
  width: 100%;
  min-height: calc(100vh - 56px);
  padding: 12px;
  background: #f3f4f6;
  color: #111827;
  font-family: Arial, Helvetica, sans-serif;
}

.page-head,
.page-foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  padding: 12px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
  margin-bottom: 10px;
}

.page-head h2 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
}

.page-head p {
  margin: 4px 0 0;
  color: #6b7280;
  font-size: 12px;
}

.head-actions {
  display: flex;
  gap: 6px;
}

.btn {
  min-height: 28px;
  border: 1px solid #cbd5e1;
  border-radius: 3px;
  background: #ffffff;
  color: #111827;
  padding: 4px 10px;
  font-size: 12px;
  line-height: 16px;
  cursor: pointer;
}

.btn:hover {
  background: #f8fafc;
}

.btn.primary {
  border-color: #2563eb;
  background: #2563eb;
  color: #ffffff;
}

.notice {
  margin-bottom: 10px;
  padding: 8px 10px;
  border: 1px solid #bbf7d0;
  border-radius: 4px;
  background: #f0fdf4;
  color: #166534;
  font-size: 13px;
}

.notice.error {
  border-color: #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

.flow-summary {
  position: sticky;
  top: 0;
  z-index: 50;

  display: flex;
  flex-wrap: wrap;
  gap: 6px 10px;
  overflow-x: visible;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #bfdbfe;
  border-radius: 4px;
  background: #eff6ff;
  font-size: 12px;
  white-space: normal;
  box-shadow: 0 2px 6px rgba(15, 23, 42, 0.08);
}

.breadcrumb-flow {
  align-items: center;
}

.breadcrumb-pill {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  max-width: 260px;
  border: 1px solid #bfdbfe;
  border-radius: 999px;
  background: #ffffff;
  color: #1e40af;
  padding: 5px 9px;
  font-size: 12px;
  line-height: 1.25;
  cursor: pointer;
  transition: background .15s ease, border-color .15s ease, color .15s ease, transform .15s ease;
}

.breadcrumb-pill span {
  color: #475569;
  font-weight: 600;
  white-space: nowrap;
}

.breadcrumb-pill b {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.breadcrumb-pill:hover:not(:disabled) {
  border-color: #2563eb;
  background: #dbeafe;
  color: #1e3a8a;
  transform: translateY(-1px);
}

.breadcrumb-pill.active:not(:disabled) {
  border-color: #1d4ed8;
  background: #bfdbfe;
  color: #1e3a8a;
}

.breadcrumb-pill.disabled,
.breadcrumb-pill:disabled {
  border-color: #e5e7eb;
  background: #f8fafc;
  color: #94a3b8;
  cursor: not-allowed;
}

.breadcrumb-pill.disabled span,
.breadcrumb-pill:disabled span {
  color: #94a3b8;
}

.flow-group {
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #cbd5e1;
  border-radius: 4px;
  background: #e5e7eb;
}

.group-title {
  margin-bottom: 8px;
  padding: 8px 10px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
}

.group-title h3 {
  margin: 0;
  font-size: 14px;
}

.group-title p {
  margin: 4px 0 0;
  color: #4b5563;
  font-size: 12px;
}

.mini-scroll {
  scrollbar-width: thin;
  scrollbar-color: #9ca3af #f3f4f6;
}

.mini-scroll::-webkit-scrollbar {
  height: 6px;
  width: 6px;
}

.mini-scroll::-webkit-scrollbar-thumb {
  background: #9ca3af;
  border-radius: 999px;
}

.mini-scroll::-webkit-scrollbar-track {
  background: #f3f4f6;
}

@media (max-width: 800px) {
  .page-head,
  .page-foot {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>


<style scoped>
.dao-tao-xem-page .eyebrow {
  margin: 0 0 4px;
  color: #1d4ed8;
  font-size: 11px;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .04em;
}

.tang-nav {
  display: grid;
  grid-template-columns: repeat(9, minmax(135px, 1fr));
  gap: 8px;
  overflow-x: auto;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #dbeafe;
  border-radius: 6px;
  background: #ffffff;
}

.tang-btn {
  min-height: 58px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  background: #f8fafc;
  color: #334155;
  padding: 8px;
  text-align: left;
  cursor: pointer;
}

.tang-btn b,
.tang-btn span {
  display: block;
}

.tang-btn b {
  font-size: 11px;
}

.tang-btn span {
  margin-top: 3px;
  font-size: 12px;
}

.tang-btn.active {
  border-color: #2563eb;
  background: #dbeafe;
  color: #1e3a8a;
}

.tang-current {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
  padding: 12px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
}

.tang-current h3 {
  margin: 0;
  font-size: 15px;
}

.tang-current p {
  margin: 4px 0 0;
  color: #475569;
  font-size: 12px;
}

.continue-panel {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin: 10px 8px;
  padding: 10px 12px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #eff6ff;
}

.continue-panel div {
  display: flex;
  flex-direction: column;
  gap: 3px;
  color: #1e3a8a;
  font-size: 12px;
}

.continue-panel b {
  font-size: 13px;
}

.continue-actions {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-end;
  gap: 8px;
}

.continue-panel .deep-action {
  min-width: 180px;
  border-color: #1d4ed8;
  background: #1d4ed8;
  color: #ffffff;
  font-weight: 700;
}

.continue-panel .deep-action:hover:not(:disabled) {
  border-color: #1e40af;
  background: #1e40af;
}

.continue-panel .secondary-action {
  border-color: #cbd5e1;
  background: #ffffff;
  color: #0f172a;
  font-weight: 600;
}

.continue-panel .secondary-action:hover:not(:disabled) {
  border-color: #2563eb;
  background: #eff6ff;
  color: #1d4ed8;
}

.tang-actions {
  display: flex;
  gap: 6px;
}

.tang-note {
  max-width: 360px;
  color: #475569;
  font-size: 12px;
  text-align: right;
}

.flow-group.mau-xanh { background: #e0f2fe; border-color: #bae6fd; }
.flow-group.mau-tim { background: #ede9fe; border-color: #ddd6fe; }
.flow-group.mau-luc { background: #dcfce7; border-color: #bbf7d0; }
.flow-group.mau-cam { background: #ffedd5; border-color: #fed7aa; }
.flow-group.mau-hong { background: #fce7f3; border-color: #fbcfe8; }
.flow-group.mau-vang { background: #fef9c3; border-color: #fde68a; }
.flow-group.mau-ngoc { background: #ccfbf1; border-color: #99f6e4; }
.flow-group.mau-troi { background: #dbeafe; border-color: #bfdbfe; }
.flow-group.mau-xam { background: #e5e7eb; border-color: #cbd5e1; }

@media (max-width: 1000px) {
  .tang-nav {
    grid-template-columns: repeat(9, 150px);
  }

  .tang-current,
  .continue-panel {
    align-items: flex-start;
    flex-direction: column;
  }

  .continue-actions {
    justify-content: flex-start;
    width: 100%;
  }

  .continue-panel .deep-action,
  .continue-panel .secondary-action {
    min-width: 0;
  }
}
</style>
