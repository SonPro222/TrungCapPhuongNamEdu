<template>
  <div class="xay-dung-page">
    <header class="page-head">
      <div>
        <h2>Xây dựng chương trình đào tạo</h2>
        <p>Trang này dùng để chọn dữ liệu có sẵn hoặc tạo mới theo luồng. Tạo/chọn cha trước rồi tiếp tục tạo/chọn con.</p>
      </div>
      <div class="head-actions">
        <button type="button" class="btn" @click="lamMoiLuon">Làm mới luồng nhập</button>
        <button type="button" class="btn primary" @click="luuChuongTrinhTong">Lưu chương trình</button>
      </div>
    </header>

    <div v-if="thongBao" :class="['notice', loaiThongBao]">{{ thongBao }}</div>
    <div v-if="errorMessage" class="notice error">{{ errorMessage }}</div>

    <div class="flow-summary mini-scroll">
      <span>Ngành: <b>{{ selected.nganh?.tenNganh || '-' }}</b></span>
      <span>Trình độ: <b>{{ selected.trinhDoDaoTao?.tenTrinhDo || '-' }}</b></span>
      <span>Loại CT: <b>{{ selected.loaiChuongTrinh?.tenLoai || '-' }}</b></span>
      <span>CTĐT: <b>{{ selected.chuongTrinh?.tenChuongTrinh || '-' }}</b></span>
      <span>Version: <b>{{ selected.chuongTrinhVersion?.tenVersion || '-' }}</b></span>

      <span>Đang làm: <b>{{ tenKhuVucDangLam || '-' }}</b></span>

      <span>Kỳ: <b>{{ selected.khungKy?.tenKy || '-' }}</b></span>
      <span>Nhóm KT: <b>{{ selected.nhomKienThuc?.ten || '-' }}</b></span>
      <span>Nhóm tự chọn: <b>{{ selected.nhomTuChon?.ten || '-' }}</b></span>
      <span>Môn học: <b>{{ selected.monHoc?.tenMon || '-' }}</b></span>
      <span>Môn trong CT: <b>{{ selected.chuongTrinhMon?.maMonTrongCt || selected.chuongTrinhMon?.tenMon || '-' }}</b></span>
      <span>Syllabus môn: <b>{{ selected.syllabusMonHoc?.id || '-' }}</b></span>
    </div>

    <section v-for="group in groups" :key="group.key" class="flow-group">
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
          :viewed-id="bang.viewedId"
          :parent-values="bang.parentValues"
          :parent-text="bang.parentText"
          :disabled="bang.disabled"
          :disabled-text="bang.disabledText"
          :loai-bang="bang.loaiBang"
          @select="chonDongBang(bang, $event)"
          @toggle-save="luuDongBang(bang, $event)"
          @saved="sauKhiLuu(bang.key, $event)"
          @deleted="sauKhiXoa(bang.key, $event)"
          @notify="baoTin"
      />
    </section>

    <footer class="page-foot">
      <button type="button" class="btn primary" @click="luuChuongTrinhTong">Lưu chương trình</button>
    </footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import BangThemNghiepVu from '../components/BangThemNghiepVu.vue'
import { useXayDungChuongTrinh } from '../composables/useXayDungChuongTrinh'

const {
  thongBao,
  loaiThongBao,
  errorMessage,
  tableMessages,
  selected,
  viewed,
  duLieu,
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
} = useXayDungChuongTrinh()

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

function laySelectedIdsTheoBang(key, allRows = [], rows = [], parentValues = {}, filterValues = {}) {
  if (key === 'trinhDoDaoTao' || key === 'loaiChuongTrinh') {
    const nganhId = dongDangDung('nganh')?.id
    if (!nganhId) return []
    const field = key === 'trinhDoDaoTao' ? 'trinhDoId' : 'loaiChuongTrinhId'
    return Array.from(new Set((duLieu.value.chuongTrinh || [])
        .filter((row) => String(row.nganhId || '') === String(nganhId || ''))
        .map((row) => row[field])
        .filter((id) => id !== null && id !== undefined && id !== '')))
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
    const syllabusMonId = filterValues.syllabusMonId || parentValues.syllabusMonId || selected.syllabusMonHoc?.id
    if (!syllabusMonId) return []

    return (duLieu.value.syllabusMonHocDieuKien || [])
        .filter((row) => String(row.syllabusMonId || '') === String(syllabusMonId || ''))
        .map((row) => row.dieuKienGocId)
        .filter((id) => id !== null && id !== undefined && id !== '')
  }

  if (key === 'taiLieuGoc') {
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
  'chuongTrinh',
  'chuongTrinhVersion',
  'khungKy',
  'nhomKienThuc',
  'monHoc',
  'chuongTrinhMon',
  'syllabusMonHoc'
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


function chonDongBang(bang, item) {
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
}


const bangCoNutLuuLienKet = new Set([
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
  toggleLuuBangPhu(bang.key, item, bang.parentValues, bang.service)
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

// Các bảng mẫu/gốc dùng chung không được lọc trực tiếp theo cha.
// Ví dụ quy_doi_diem_mau là kho mẫu tái sử dụng, không có chuongTrinhMonId.
// Khi chọn 5.2 Chương trình môn, chỉ dùng chuongTrinhMonId để đánh dấu/gán qua bảng nối,
// không dùng để lọc rows của bảng mẫu.
const bangDocLapKhongLocTheoCha = new Set([
  'quyDoiDiemMau'
])

function taoBang(key, options = {}) {
  const parentValues = options.parentValues || {}
  const filterValues = options.filterValues || parentValues
  const allRows = duLieu.value[key] || []

  const loaiBang = bangGocMauKeys.has(key) ? 'goc-mau' : (bangXuongSongKeys.has(key) ? 'xuong-song' : 'phu')
  const hienDayDuBangPhu = loaiBang === 'phu' && bangPhuHienDuLieuDayDu.has(key)
  const khongLocTheoCha = bangDocLapKhongLocTheoCha.has(key)

  const rows = khongLocTheoCha || hienDayDuBangPhu ? allRows : locDongTheoCha(allRows, filterValues)

  return {
    key,
    config: configs[key],
    rows,
    allRows,
    service: services[key],
    selectedId: selected[key]?.id || null,
    viewedId: viewed[key]?.id || null,
    selectedIds: laySelectedIdsTheoBang(key, allRows, rows, parentValues, filterValues),
    savedIds: laySelectedIdsTheoBang(key, allRows, rows, parentValues, filterValues),
    canToggleSave: key !== 'nganh',
    tableMessage: tableMessages?.[key] || null,
    parentValues,
    parentText: options.parentText || '',
    loaiBang,
    disabled: Boolean(options.disabled),
    disabledText: options.disabledText || ''
  }
}

const groups = computed(() => [
  {
    key: 'nen-tang',
    title: '1. Tạo nền tảng chương trình',
    description: 'Đi đúng thứ tự: Ngành → Trình độ đào tạo → Loại chương trình → Chương trình đào tạo → Phiên bản chương trình.',
    tables: [
      taoBang('nganh'),
      taoBang('trinhDoDaoTao'),
      taoBang('loaiChuongTrinh'),
      taoBang('chuongTrinh', {
        parentValues: chuongTrinhParent.value,
        filterValues: chuongTrinhFilter.value,
        parentText: chuongTrinhParentText.value,
        disabled: !selected.nganh || !selected.trinhDoDaoTao || !selected.loaiChuongTrinh,
        disabledText: 'Cần lưu và chọn Ngành, Trình độ, Loại chương trình trước khi tạo Chương trình đào tạo.'
      }),
      taoBang('chuongTrinhVersion', {
        parentValues: chuongTrinhVersionParent.value,
        filterValues: chuongTrinhVersionFilter.value,
        parentText: chuongTrinhVersionParentText.value,
        disabled: !selected.chuongTrinh,
        disabledText: 'Cần lưu và chọn Chương trình đào tạo trước khi tạo Version.'
      })
    ]
  },
  {
    key: 'noi-dung-version',
    title: '2. Tạo nội dung thuộc Phiên bản chương trình',
    description: 'Đi đúng thứ tự flow version: bảng gốc/mẫu trước, bảng lưu/áp dụng ngay bên dưới.',
    tables: [
      taoBang('syllabusChuongTrinhGoc', { parentText: `${versionParentText.value} | Kho syllabus chương trình gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('syllabusChuongTrinh', { parentValues: syllabusChuongTrinhParent.value, filterValues: versionFilter.value, parentText: syllabusChuongTrinhParentText.value, disabled: !selected.chuongTrinhVersion || !selected.syllabusChuongTrinhGoc, disabledText: 'Cần lưu/chọn Version và chọn Syllabus chương trình gốc trước.' }),

      taoBang('mucTieuChuongTrinhGoc', { parentText: `${versionParentText.value} | Kho mục tiêu gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('chuongTrinhVersionMucTieu', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Mục tiêu gốc đã gán vào Version thật.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('mucTieuChuongTrinh', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Mục tiêu nhập riêng trực tiếp cho Version.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),

      taoBang('nangLucDauRaGoc', { parentText: `${versionParentText.value} | Kho năng lực đầu ra gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('chuongTrinhVersionNangLuc', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Năng lực gốc đã gán vào Version thật.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('nangLucDauRa', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Năng lực nhập riêng trực tiếp cho Version.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),

      taoBang('viTriViecLamGoc', { parentText: `${versionParentText.value} | Kho vị trí việc làm gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('chuongTrinhVersionViTriViecLam', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Vị trí việc làm gốc đã gán vào Version thật.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('viTriViecLam', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Vị trí việc làm nhập riêng trực tiếp cho Version.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),

      taoBang('dieuKienTotNghiepGoc', { parentText: `${versionParentText.value} | Kho điều kiện tốt nghiệp gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('chuongTrinhVersionDieuKienTotNghiep', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Điều kiện tốt nghiệp gốc đã gán vào Version thật.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('dieuKienTotNghiep', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: `${versionParentText.value} | Điều kiện tốt nghiệp nhập riêng trực tiếp cho Version.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),

      taoBang('khungKyGoc', { parentText: `${versionParentText.value} | Kho khung kỳ gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('khungKy', { parentValues: khungKyParent.value, filterValues: khungKyFilter.value, parentText: khungKyParentText.value, disabled: !selected.chuongTrinhVersion || !selected.khungKyGoc || !khungKyParent.value.loaiChuongTrinhId, disabledText: 'Cần lưu/chọn Version, Loại chương trình và Khung kỳ gốc trước khi gán kỳ vào Version.' }),

      taoBang('nhomKienThucGoc', { parentText: `${versionParentText.value} | Kho nhóm kiến thức gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('nhomKienThuc', { parentValues: nhomKienThucParent.value, filterValues: versionFilter.value, parentText: nhomKienThucParentText.value, disabled: !selected.chuongTrinhVersion || !selected.nhomKienThucGoc, disabledText: 'Cần lưu/chọn Version và chọn Nhóm kiến thức gốc trước.' }),

      taoBang('nhomTuChonGoc', { parentText: `${versionParentText.value} | Kho nhóm tự chọn gốc dùng chung.`, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('nhomTuChon', { parentValues: nhomTuChonParent.value, filterValues: versionFilter.value, parentText: nhomTuChonParentText.value, disabled: !selected.chuongTrinhVersion || !selected.nhomTuChonGoc, disabledText: 'Cần lưu/chọn Version và chọn Nhóm tự chọn gốc trước.' })
    ]
  },
  {
    key: 'mon',
    title: '3. Tạo môn và mẫu syllabus môn học',
    description: 'Đi đúng thứ tự: Môn học → Syllabus môn học gốc → Chương/bài gốc → Điều kiện gốc → Gán điều kiện gốc → Tài liệu gốc → Gán tài liệu gốc → Chương trình môn.',
    tables: [
      taoBang('monHoc', {
        parentText: monHocParentText.value,
        disabled: !selected.chuongTrinhVersion,
        disabledText: 'Cần lưu và chọn Version trước khi tạo Môn học gốc cho luồng này.'
      }),
      taoBang('syllabusMonHocGoc', {
        parentValues: syllabusMonHocGocParent.value,
        filterValues: syllabusMonHocGocFilter.value,
        parentText: `${syllabusMonHocGocParentText.value} | Tạo/chọn syllabus môn học gốc để tái sử dụng cho các môn CT.`,
        disabled: !selected.monHoc,
        disabledText: 'Cần lưu và chọn Môn học gốc trước khi tạo Syllabus môn học gốc.'
      }),
      taoBang('syllabusMonHocGocChuongBai', {
        parentValues: syllabusMonHocGocChiTietParent.value,
        filterValues: syllabusMonHocGocChiTietFilter.value,
        parentText: syllabusMonHocGocChiTietText.value,
        disabled: !selected.syllabusMonHocGoc,
        disabledText: 'Cần lưu và chọn Syllabus môn học gốc trước.'
      }),
      taoBang('dieuKienMonHocGoc', {
        parentText: `${syllabusMonHocGocChiTietText.value} | Kho điều kiện môn học gốc dùng chung.`,
        disabled: !selected.syllabusMonHocGoc,
        disabledText: 'Cần lưu và chọn Syllabus môn học gốc trước khi tạo/chọn Điều kiện môn học gốc.'
      }),
      taoBang('syllabusMonHocGocDieuKien', {
        parentValues: syllabusMonHocGocChiTietParent.value,
        filterValues: syllabusMonHocGocChiTietFilter.value,
        parentText: syllabusMonHocGocChiTietText.value,
        disabled: !selected.syllabusMonHocGoc,
        disabledText: 'Cần lưu và chọn Syllabus môn học gốc trước.'
      }),
      taoBang('taiLieuGoc', {
        parentText: `${syllabusMonHocGocChiTietText.value} | Kho tài liệu gốc dùng chung.`,
        disabled: !selected.syllabusMonHocGoc,
        disabledText: 'Cần lưu và chọn Syllabus môn học gốc trước khi tạo/chọn Tài liệu gốc.'
      }),
      taoBang('syllabusMonHocGocTaiLieu', {
        parentValues: syllabusMonHocGocChiTietParent.value,
        filterValues: syllabusMonHocGocChiTietFilter.value,
        parentText: syllabusMonHocGocChiTietText.value,
        disabled: !selected.syllabusMonHocGoc,
        disabledText: 'Cần lưu và chọn Syllabus môn học gốc trước.'
      }),
      taoBang('chuongTrinhMon', {
        parentValues: chuongTrinhMonParent.value,
        filterValues: chuongTrinhMonViewFilter.value,
        parentText: chuongTrinhMonParentText.value,
        disabled: !selected.chuongTrinhVersion,
        disabledText: 'Cần lưu và chọn Version trước. Nếu chọn Môn học gốc thì khi lưu sẽ tự gắn monHocId; nếu không chọn thì bảng hiển thị tất cả môn CT theo Version/Kỳ.'
      }),
      taoBang('monTuChon', {
        parentValues: monTuChonParent.value,
        parentText: monTuChonParentText.value,
        disabled: !selected.nhomTuChon || !selected.chuongTrinhMon,
        disabledText: 'Cần lưu và chọn Nhóm tự chọn + Môn trong chương trình trước.'
      }),
      taoBang('monTienQuyet', {
        parentValues: monTienQuyetParent.value,
        filterValues: chuongTrinhMonOnlyFilter.value,
        parentText: chuongTrinhMonOnlyParentText.value,
        disabled: !selected.chuongTrinhMon,
        disabledText: 'Cần lưu và chọn Môn trong chương trình trước.'
      }),
      taoBang('quyDoiDiemMau', {
        // Quy đổi điểm mẫu là bảng gốc/kho mẫu dùng chung.
        // Không truyền chuongTrinhMonId vào parentValues để tránh POST sai payload
        // và tránh lọc mất dữ liệu khi chọn/lưu 5.2 Chương trình môn.
        parentValues: {},
        filterValues: chuongTrinhMonOnlyFilter.value,
        parentText: `${chuongTrinhMonOnlyParentText.value} | Kho quy đổi điểm mẫu dùng chung. Chọn/lưu để gán mẫu vào Môn trong chương trình đang chọn.`,
        disabled: false,
        disabledText: 'Cần lưu và chọn Môn trong chương trình trước nếu muốn gán Quy đổi điểm mẫu vào môn CT.'
      }),
      taoBang('quyDoiDiem', {
        parentValues: syllabusMonHocApDungParent.value,
        filterValues: chuongTrinhMonOnlyFilter.value,
        parentText: chuongTrinhMonOnlyParentText.value,
        disabled: !selected.chuongTrinhMon,
        disabledText: 'Cần lưu và chọn Môn trong chương trình trước.'
      }),
      taoBang('syllabusMonHoc', {
        parentValues: syllabusMonHocApDungParent.value,
        filterValues: chuongTrinhMonOnlyFilter.value,
        parentText: chuongTrinhMonOnlyParentText.value,
        disabled: !selected.chuongTrinhMon,
        disabledText: 'Cần lưu và chọn Môn trong chương trình trước.'
      })
    ]
  },
  {
    key: 'syllabus-mon',
    title: '4. Tạo chi tiết Syllabus môn học áp dụng',
    description: 'Đi đúng thứ tự sau Syllabus môn học áp dụng.',
    tables: [
      taoBang('syllabusChuongBai', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' }),
      taoBang('syllabusMonHocDieuKien', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' }),
      taoBang('syllabusMonHocTaiLieu', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' }),
      taoBang('dieuKienMonHoc', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' }),
      taoBang('syllabusTaiLieu', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' })
    ]
  }
])
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
    title: '5.2. Chương trình môn - Môn trong chương trình',
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

.flow-summary span {
  padding-right: 8px;
  border-right: 1px solid #e5e7eb;
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
