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
      <span>Kỳ: <b>{{ selected.khungKy?.tenKy || '-' }}</b></span>
      <span>Nhóm KT: <b>{{ selected.nhomKienThuc?.ten || '-' }}</b></span>
      <span>Nhóm tự chọn: <b>{{ selected.nhomTuChon?.ten || '-' }}</b></span>
      <span>Môn học: <b>{{ selected.monHoc?.tenMon || '-' }}</b></span>
      <span>Môn CT: <b>{{ selected.chuongTrinhMon?.maMonTrongCt || '-' }}</b></span>
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
          :can-toggle-save="bang.canToggleSave"
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

const khungKyParent = computed(() => ({
  chuongTrinhVersionId: selected.chuongTrinhVersion?.id || null,
  loaiChuongTrinhId: selected.loaiChuongTrinh?.id || null
}))

const khungKyParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Loại CT ${selected.loaiChuongTrinh?.tenLoai || '-'}`
})

const monHocParentText = computed(() => {
  return `Đang chọn: Version ${selected.chuongTrinhVersion?.tenVersion || '-'} | Tạo môn học gốc trước khi tạo Chương trình môn`
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
  loaiChuongTrinhId: dongDangDung('loaiChuongTrinh')?.id || null
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
    mucTieuChuongTrinh: { joinKey: 'chuongTrinhVersionMucTieu', gocIdKey: 'mucTieuGocId' },
    nangLucDauRa: { joinKey: 'chuongTrinhVersionNangLuc', gocIdKey: 'nangLucGocId' },
    viTriViecLam: { joinKey: 'chuongTrinhVersionViTriViecLam', gocIdKey: 'viTriGocId' },
    dieuKienTotNghiep: { joinKey: 'chuongTrinhVersionDieuKienTotNghiep', gocIdKey: 'dieuKienGocId' }
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
  'chuongTrinhMon',
  'mucTieuChuongTrinh',
  'nangLucDauRa',
  'viTriViecLam',
  'dieuKienTotNghiep',
  'monTienQuyet',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function luuDongBang(bang, item) {
  toggleLuuBangPhu(bang.key, item, bang.parentValues, bang.service)
}

const bangPhuHienDuLieuDayDu = new Set([
  'mucTieuChuongTrinh',
  'nangLucDauRa',
  'viTriViecLam',
  'dieuKienTotNghiep',
  'syllabusChuongTrinh',
  'monTienQuyet',
  'quyDoiDiem',
  'dieuKienMonHoc',
  'syllabusChuongBai',
  'syllabusTaiLieu'
])

function taoBang(key, options = {}) {
  const parentValues = options.parentValues || {}
  const filterValues = options.filterValues || parentValues
  const allRows = duLieu.value[key] || []

  const loaiBang = bangXuongSongKeys.has(key) ? 'xuong-song' : 'phu'
  const hienDayDuBangPhu = loaiBang === 'phu' && bangPhuHienDuLieuDayDu.has(key)

  const rows = hienDayDuBangPhu ? allRows : locDongTheoCha(allRows, filterValues)

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
    canToggleSave: bangCoNutLuuLienKet.has(key),
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
    description: 'Tạo Ngành, Trình độ đào tạo, Loại chương trình trước. Sau đó tạo Chương trình đào tạo gắn theo 3 id này.',
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
      })
    ]
  },
  {
    key: 'version',
    title: '2. Tạo phiên bản chương trình',
    description: 'Version được tạo sau Chương trình đào tạo và tự nhận chuongTrinhId từ chương trình đang chọn.',
    tables: [
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
    title: '3. Tạo các bảng thuộc Version',
    description: 'Các bảng ngang tầng này đều tự nhận chuongTrinhVersionId từ Version đang chọn.',
    tables: [
      taoBang('mucTieuChuongTrinh', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('nangLucDauRa', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('viTriViecLam', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('dieuKienTotNghiep', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('syllabusChuongTrinh', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('nhomKienThuc', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('nhomTuChon', { parentValues: versionParent.value, filterValues: versionFilter.value, parentText: versionParentText.value, disabled: !selected.chuongTrinhVersion, disabledText: 'Cần lưu và chọn Version trước.' }),
      taoBang('khungKy', { parentValues: khungKyParent.value, filterValues: khungKyFilter.value, parentText: khungKyParentText.value, disabled: !selected.chuongTrinhVersion || !selected.loaiChuongTrinh, disabledText: 'Cần lưu và chọn Version + Loại chương trình trước khi tạo Khung kỳ.' })
    ]
  },
  {
    key: 'mon',
    title: '4. Tạo môn trong chương trình',
    description: 'Tạo Môn học gốc trước, sau đó tạo Chương trình môn theo Version, Khung kỳ và Nhóm kiến thức đang chọn.',
    tables: [
      taoBang('monHoc', {
        parentText: monHocParentText.value,
        disabled: !selected.chuongTrinhVersion,
        disabledText: 'Cần lưu và chọn Version trước khi tạo Môn học gốc cho luồng này.'
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
      taoBang('quyDoiDiem', {
        parentValues: chuongTrinhMonOnlyParent.value,
        filterValues: chuongTrinhMonOnlyFilter.value,
        parentText: chuongTrinhMonOnlyParentText.value,
        disabled: !selected.chuongTrinhMon,
        disabledText: 'Cần lưu và chọn Môn trong chương trình trước.'
      }),
      taoBang('syllabusMonHoc', {
        parentValues: chuongTrinhMonOnlyParent.value,
        filterValues: chuongTrinhMonOnlyFilter.value,
        parentText: chuongTrinhMonOnlyParentText.value,
        disabled: !selected.chuongTrinhMon,
        disabledText: 'Cần lưu và chọn Môn trong chương trình trước.'
      })
    ]
  },
  {
    key: 'syllabus-mon',
    title: '5. Tạo chi tiết Syllabus môn học',
    description: 'Các bảng này tự nhận syllabusMonId từ Syllabus môn học đang chọn.',
    tables: [
      taoBang('dieuKienMonHoc', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' }),
      taoBang('syllabusChuongBai', { parentValues: syllabusMonParent.value, filterValues: syllabusMonFilter.value, parentText: syllabusMonParentText.value, disabled: !selected.syllabusMonHoc, disabledText: 'Cần lưu và chọn Syllabus môn học trước.' }),
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
  mucTieuChuongTrinh: {
    title: '4.1. Mục tiêu chương trình',
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
  nangLucDauRa: {
    title: '4.2. Năng lực đầu ra',
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
  viTriViecLam: {
    title: '4.3. Vị trí việc làm',
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
  dieuKienTotNghiep: {
    title: '4.4. Điều kiện tốt nghiệp',
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
  syllabusChuongTrinh: {
    title: '4.5. Syllabus chương trình',
    defaultForm: { chuongTrinhVersionId: null, moTaTongQuan: '', mucDich: '', yeuCauDaoTao: '', phuongPhapDaoTao: '', ghiChu: '' },
    fields: [
      { key: 'moTaTongQuan', label: 'Mô tả tổng quan', type: 'textarea', wide: true },
      { key: 'mucDich', label: 'Mục đích', type: 'textarea', wide: true },
      { key: 'yeuCauDaoTao', label: 'Yêu cầu đào tạo', type: 'textarea', wide: true },
      { key: 'phuongPhapDaoTao', label: 'Phương pháp đào tạo', type: 'textarea', wide: true },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'moTaTongQuan', label: 'Mô tả' },
      { key: 'mucDich', label: 'Mục đích' },
      { key: 'phuongPhapDaoTao', label: 'Phương pháp' }
    ]
  },
  nhomKienThuc: {
    title: '4.6. Nhóm kiến thức',
    defaultForm: { chuongTrinhVersionId: null, ma: '', ten: '', thuTu: null, loaiNhom: 'chung', tongTinChi: null, tongSoGio: null, tongGioLyThuyet: null, tongGioThucHanh: null, tongGioKiemTra: null },
    fields: [
      { key: 'ma', label: 'Mã nhóm', required: true },
      { key: 'ten', label: 'Tên nhóm', required: true },
      { key: 'loaiNhom', label: 'Loại nhóm', type: 'select', lookup: 'loaiNhom' },
      { key: 'thuTu', label: 'Thứ tự', type: 'number' },
      { key: 'tongTinChi', label: 'Tổng TC', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'tongSoGio', label: 'Tổng giờ', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'tongGioLyThuyet', label: 'Giờ LT', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'tongGioThucHanh', label: 'Giờ TH', type: 'number', max: 999999.9, step: 0.1 },
      { key: 'tongGioKiemTra', label: 'Giờ KT', type: 'number', max: 999999.9, step: 0.1 }
    ],
    uniqueRules: [
      { field: 'ma', scopeKeys: ['chuongTrinhVersionId'], message: 'Mã nhóm kiến thức đã tồn tại trong Version đang chọn.' },
      { field: 'thuTu', scopeKeys: ['chuongTrinhVersionId'], message: 'Thứ tự nhóm kiến thức đã tồn tại trong Version đang chọn.' }
    ],
    columns: [
      { key: 'ma', label: 'Mã' },
      { key: 'ten', label: 'Tên nhóm' },
      { key: 'loaiNhom', label: 'Loại' },
      { key: 'tongTinChi', label: 'TC' },
      { key: 'tongSoGio', label: 'Giờ' }
    ]
  },
  nhomTuChon: {
    title: '4.7. Nhóm tự chọn',
    defaultForm: { chuongTrinhVersionId: null, ten: '', soMonChon: null, soTinChiCanDat: null, ghiChu: '' },
    fields: [
      { key: 'ten', label: 'Tên nhóm', required: true },
      { key: 'soMonChon', label: 'Số môn chọn', type: 'number' },
      { key: 'soTinChiCanDat', label: 'Số TC cần đạt', type: 'number', max: 9999.9, step: 0.1 },
      { key: 'ghiChu', label: 'Ghi chú', type: 'textarea', wide: true }
    ],
    uniqueRules: [
      { field: 'ten', scopeKeys: ['chuongTrinhVersionId'], message: 'Tên nhóm tự chọn đã tồn tại trong Version đang chọn.' }
    ],
    columns: [
      { key: 'ten', label: 'Tên nhóm' },
      { key: 'soMonChon', label: 'Số môn chọn' },
      { key: 'soTinChiCanDat', label: 'TC cần đạt' },
      { key: 'ghiChu', label: 'Ghi chú' }
    ]
  },
  khungKy: {
    title: '4.8. Khung kỳ',
    defaultForm: { chuongTrinhVersionId: null, loaiChuongTrinhId: null, maKy: '', tenKy: '', thuTu: null },
    fields: [
      { key: 'maKy', label: 'Mã kỳ', required: true },
      { key: 'tenKy', label: 'Tên kỳ', required: true },
      { key: 'thuTu', label: 'Thứ tự', type: 'number', step: 1 }
    ],
    uniqueRules: [
      { field: 'maKy', scopeKeys: ['chuongTrinhVersionId', 'loaiChuongTrinhId'], message: 'Mã kỳ đã tồn tại trong Version và Loại chương trình đang chọn.' },
      { field: 'thuTu', scopeKeys: ['chuongTrinhVersionId', 'loaiChuongTrinhId'], message: 'Thứ tự khung kỳ đã tồn tại trong Version và Loại chương trình đang chọn.' }
    ],
    columns: [
      { key: 'maKy', label: 'Mã kỳ' },
      { key: 'tenKy', label: 'Tên kỳ' },
      { key: 'tenVersion', label: 'Version' },
      { key: 'tenLoaiChuongTrinh', label: 'Loại CT' },
      { key: 'thuTu', label: 'Thứ tự' }
    ]
  },
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
    defaultForm: { chuongTrinhVersionId: null, monHocId: null, maMonTrongCt: '', khungKyId: null, nhomKienThucId: null, loai: 'bat_buoc', loaiHocPhan: 'mon_hoc', batBuoc: true, laMonDieuKien: false, thuTu: null, soTinChi: null, tongGio: null, gioLyThuyet: null, gioThucHanh: null, gioKiemTra: null, ghiChu: '' },
    fields: [
      { key: 'maMonTrongCt', label: 'Mã môn CT', required: true },
      { key: 'loai', label: 'Loại môn', type: 'select', lookup: 'loaiMon' },
      { key: 'loaiHocPhan', label: 'Loại học phần', type: 'select', lookup: 'loaiHocPhan' },
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
  syllabusMonHoc: {
    title: '5.6. Syllabus môn học',
    defaultForm: { chuongTrinhMonId: null, viTri: '', tinhChat: '', mucTieu: '', phuongPhapDanhGia: '', dieuKienHoanThanh: '', huongDan: '', diemDatToiThieu: null, donViDiem: 'thang_10', tyLeChuyenCanToiThieu: null, batBuocDuThi: true, congThucQuyDoi: '' },
    fields: [
      { key: 'viTri', label: 'Vị trí', type: 'textarea', wide: true },
      { key: 'tinhChat', label: 'Tính chất', type: 'textarea', wide: true },
      { key: 'mucTieu', label: 'Mục tiêu', type: 'textarea', wide: true },
      { key: 'phuongPhapDanhGia', label: 'Phương pháp đánh giá', type: 'textarea', wide: true },
      { key: 'dieuKienHoanThanh', label: 'Điều kiện hoàn thành', type: 'textarea', wide: true },
      { key: 'diemDatToiThieu', label: 'Điểm đạt tối thiểu', type: 'number', max: 999.99, step: 0.01 },
      { key: 'donViDiem', label: 'Đơn vị điểm', type: 'select', lookup: 'donViDiem' },
      { key: 'tyLeChuyenCanToiThieu', label: 'Tỷ lệ chuyên cần tối thiểu', type: 'number', max: 999.99, step: 0.01 },
      { key: 'batBuocDuThi', label: 'Bắt buộc dự thi', type: 'boolean' },
      { key: 'congThucQuyDoi', label: 'Công thức quy đổi' },
      { key: 'huongDan', label: 'Hướng dẫn', type: 'textarea', wide: true }
    ],
    columns: [
      { key: 'tenChuongTrinhMon', label: 'Môn CT' },
      { key: 'viTri', label: 'Vị trí' },
      { key: 'tinhChat', label: 'Tính chất' },
      { key: 'diemDatToiThieu', label: 'Điểm đạt' },
      { key: 'donViDiem', label: 'Đơn vị điểm' }
    ]
  },
  dieuKienMonHoc: {
    title: '6.1. Điều kiện môn học',
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
  syllabusTaiLieu: {
    title: '6.3. Syllabus tài liệu',
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
  display: flex;
  gap: 8px;
  overflow-x: auto;
  margin-bottom: 10px;
  padding: 8px;
  border: 1px solid #d1d5db;
  border-radius: 4px;
  background: #ffffff;
  font-size: 12px;
  white-space: nowrap;
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
