<template>
  <div class="dao-tao-xem-syllabus-chuong-trinh-page">
    <section class="top-shell">
      <div class="top-left">
        <button type="button" class="back-btn" @click="quayLaiTongQuan">
          ← Quay lại tổng quan
        </button>

        <div class="top-title-row">
          <p class="eyebrow">Syllabus chương trình</p>
          <h1>{{ tenChuongTrinh }}</h1>
        </div>

        <div class="top-meta-row">
          <span>Version: <strong>{{ tenVersion }}</strong></span>
          <span v-if="maChuongTrinh">Mã CT: <strong>{{ maChuongTrinh }}</strong></span>
          <span v-if="maSyllabus">Mã SYL: <strong>{{ maSyllabus }}</strong></span>
          <span :class="['status-badge', syllabusChuongTrinh ? 'success' : 'muted']">
            {{ syllabusChuongTrinh ? 'Có syllabus' : 'Chưa có syllabus' }}
          </span>
        </div>
      </div>

      <div class="top-actions">
        <button type="button" class="btn ghost" :disabled="loading || !chuongTrinhId" @click="taiDuLieu">
          Tải lại
        </button>
      </div>
    </section>

    <section v-if="errorMessage" class="state-card error">
      <h3>Không tải được syllabus chương trình</h3>
      <p>{{ errorMessage }}</p>
      <button type="button" class="btn primary" :disabled="!chuongTrinhId" @click="taiDuLieu">
        Thử lại
      </button>
    </section>

    <section v-else-if="loading" class="state-card loading-card">
      <div class="spinner"></div>
      <div>
        <h3>Đang tải syllabus chương trình...</h3>
        <p>Hệ thống đang lấy syllabus chương trình và các bảng liên quan.</p>
      </div>
    </section>

    <section v-else-if="!payload" class="state-card empty">
      <h3>Không có dữ liệu</h3>
      <p>Vui lòng quay lại trang tổng quan và chọn lại chương trình/version.</p>
    </section>

    <template v-else>
      <section class="overview-strip">
        <div class="overview-name">
          <p class="eyebrow">Chương trình đào tạo</p>
          <h2>{{ tenChuongTrinh }}</h2>
        </div>

        <div class="metric-grid">
          <article>
            <span>Mục tiêu</span>
            <strong>{{ danhSachMucTieu.length }}</strong>
          </article>
          <article>
            <span>Năng lực</span>
            <strong>{{ danhSachNangLuc.length }}</strong>
          </article>
          <article>
            <span>Việc làm</span>
            <strong>{{ danhSachViTri.length }}</strong>
          </article>
          <article>
            <span>ĐK TN</span>
            <strong>{{ danhSachDieuKien.length }}</strong>
          </article>
          <article>
            <span>Môn kỳ</span>
            <strong>{{ danhSachMonTrongKy.length }}</strong>
          </article>
          <article class="total">
            <span>Tổng TC</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.soTinChi) }}</strong>
          </article>
          <article class="total">
            <span>Tổng giờ</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.tongGio) }}</strong>
          </article>
          <article class="total">
            <span>Giờ LT</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.gioLyThuyet) }}</strong>
          </article>
          <article class="total">
            <span>Giờ TH</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.gioThucHanh) }}</strong>
          </article>
          <article class="total">
            <span>Giờ KT</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.gioKiemTra) }}</strong>
          </article>
        </div>
      </section>

      <section v-if="soMonChuaCoSyllabus > 0" class="state-card warning compact-warning">
        <strong>⚠ Tổng giờ/tín chỉ chưa đầy đủ</strong>
        <span>Có {{ soMonChuaCoSyllabus }} môn trong kỳ chưa có syllabus áp dụng.</span>
      </section>

      <section v-if="!syllabusChuongTrinh" class="state-card empty">
        <h3>Version này chưa có syllabus chương trình</h3>
        <p>Hiện chưa có bản syllabus chương trình áp dụng cho version đang chọn.</p>
      </section>

      <template v-else>
        <section class="compact-file-bar">
          <div class="file-main">
            <span class="file-label">Tệp syllabus</span>
            <strong>{{ tenSyllabus }}</strong>
          </div>

          <div v-if="duongDanTep" class="file-actions">
            <button type="button" class="file-button primary" @click="xemTep">
              Xem tệp
            </button>
            <a class="file-button" :href="duongDanTep" download>
              Tải về
            </a>
            <span class="file-path" :title="duongDanTep">{{ duongDanTep }}</span>
          </div>

          <div v-else class="file-empty">
            Chưa có đường dẫn tệp.
          </div>
        </section>

        <section class="accordion-list compact-info-list">
          <article :class="['table-card', { open: isBangDangMo('thongTinChinh') }]">
            <button type="button" class="accordion-header" @click="toggleBang('thongTinChinh')">
              <span class="toggle-icon">{{ isBangDangMo('thongTinChinh') ? '−' : '+' }}</span>
              <span class="accordion-title">
                <strong>Thông tin chính</strong>
                <small>{{ tenSyllabus }}</small>
              </span>
              <span class="count-pill">{{ thongTinChinh.length }}</span>
            </button>

            <div v-show="isBangDangMo('thongTinChinh')" class="accordion-body">
              <div class="info-table">
                <div v-for="item in thongTinChinh" :key="item.label" class="info-row">
                  <div class="info-label">{{ item.label }}</div>
                  <div class="info-value">{{ item.value }}</div>
                </div>
              </div>
            </div>
          </article>

          <article :class="['table-card', { open: isBangDangMo('noiDungSyllabus') }]">
            <button type="button" class="accordion-header" @click="toggleBang('noiDungSyllabus')">
              <span class="toggle-icon">{{ isBangDangMo('noiDungSyllabus') ? '−' : '+' }}</span>
              <span class="accordion-title">
                <strong>Nội dung syllabus</strong>
                <small>Mục tiêu, điều kiện, phương pháp, hướng dẫn, ghi chú...</small>
              </span>
              <span class="count-pill">{{ noiDungSyllabusCoNoiDung.length }}</span>
            </button>

            <div v-show="isBangDangMo('noiDungSyllabus')" class="accordion-body">
              <div v-if="noiDungSyllabusCoNoiDung.length" class="text-data-table">
                <div class="text-data-head">
                  <span>Mục</span>
                  <span>Nội dung</span>
                </div>

                <div
                    v-for="block in noiDungSyllabusCoNoiDung"
                    :key="block.key"
                    class="text-data-row"
                >
                  <div class="text-data-title">{{ block.title }}</div>
                  <div class="text-data-content">{{ block.value }}</div>
                </div>
              </div>

              <div v-else class="empty-inline small">
                Chưa có nội dung syllabus.
              </div>
            </div>
          </article>
        </section>
      </template>

      <section class="related-section">
        <div class="section-title">
          <div>
            <p class="eyebrow">Dữ liệu liên quan</p>
            <h2>Các bảng theo version chương trình</h2>
          </div>

          <div class="section-actions">
            <button type="button" class="mini-btn" @click="moTatCaBang">Mở tất cả</button>
            <button type="button" class="mini-btn" @click="thuGonTatCaBang">Thu gọn</button>
            <span class="badge neutral">{{ tongBangLienQuan }} bảng có dữ liệu</span>
          </div>
        </div>

        <div class="accordion-list">
          <article
              v-for="block in bangLienQuan"
              :key="block.key"
              :class="['table-card', { open: isBangDangMo(block.key) }]"
          >
            <button type="button" class="accordion-header" @click="toggleBang(block.key)">
              <span class="toggle-icon">{{ isBangDangMo(block.key) ? '−' : '+' }}</span>

              <span class="accordion-title">
                <strong>{{ block.title }}</strong>
                <small v-if="block.description">{{ block.description }}</small>
                <small v-if="block.key === 'monTrongKy' && soMonChuaCoSyllabus > 0" class="canh-bao-inline">
                  ⚠ {{ soMonChuaCoSyllabus }} môn chưa có syllabus
                </small>
              </span>

              <span class="count-pill">{{ block.rows.length }}</span>
            </button>

            <div v-show="isBangDangMo(block.key)" class="accordion-body">
              <SimpleTable :rows="block.rows" :columns="block.columns" :empty-text="block.emptyText" />
            </div>
          </article>
        </div>
      </section>
    </template>
  </div>
</template>


<script setup>
import { computed, defineComponent, h, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { daoTaoService } from '../../services/daoTaoService.js'
import { layThongBaoLoi } from '../../utils/layThongBaoLoi.js'

const SimpleTable = defineComponent({
  name: 'SimpleTable',
  props: {
    rows: { type: Array, default: () => [] },
    columns: { type: Array, default: () => [] },
    emptyText: { type: String, default: 'Chưa có dữ liệu.' }
  },
  setup(props) {
    return () => {
      if (!props.rows.length) {
        return h('div', { class: 'empty-inline small' }, props.emptyText)
      }

      return h('div', { class: 'table-wrap' }, [
        h('table', [
          h('thead', [
            h('tr', props.columns.map((column) => h('th', { key: column.key }, column.label)))
          ]),
          h('tbody', props.rows.map((row, rowIndex) => h('tr', { key: `row-${rowIndex}` },
              props.columns.map((column) => h('td', { key: `${rowIndex}-${column.key}` }, column.render(row, rowIndex)))
          )))
        ])
      ])
    }
  }
})

const route = useRoute()
const router = useRouter()

const TEN_ROUTE_TONG_QUAN = 'dao-tao-chuong-trinh-tong-quan'

const loading = ref(false)
const errorMessage = ref('')
const payload = ref(null)
const bangDangMo = ref(['monTrongKy'])

const chuongTrinhId = computed(() => route.params.chuongTrinhId || route.query.chuongTrinhId || null)
const versionId = computed(() => route.params.versionId || route.query.versionId || route.query.chuongTrinhVersionId || null)

const cauTruc = computed(() => payload.value?.cauTruc || null)
const chuongTrinh = computed(() => payload.value?.chuongTrinh || cauTruc.value?.chuongTrinh || null)
const versionDangXem = computed(() => payload.value?.versionDangXem || cauTruc.value?.version || null)

const tenChuongTrinh = computed(() => layTen(chuongTrinh.value, ['tenChuongTrinh', 'ten', 'name', 'maChuongTrinh'], 'Chương trình đào tạo'))
const maChuongTrinh = computed(() => layTruong(chuongTrinh.value, ['maChuongTrinh', 'ma', 'code']))
const tenVersion = computed(() => layTen(versionDangXem.value, ['tenVersion', 'ten', 'maVersion', 'ma'], 'Version đang chọn'))

const danhSachSyllabusChuongTrinh = computed(() => layMangDauTien(cauTruc.value, ['syllabusChuongTrinh', 'danhSachSyllabusChuongTrinh']))
const syllabusChuongTrinh = computed(() => {
  if (!danhSachSyllabusChuongTrinh.value.length) return null

  const idFromPayload = payload.value?.syllabusChuongTrinhId
  if (idFromPayload) {
    const found = danhSachSyllabusChuongTrinh.value.find((item) => String(layId(item)) === String(idFromPayload))
    if (found) return found
  }

  return danhSachSyllabusChuongTrinh.value[0]
})

const tenSyllabus = computed(() => layTen(syllabusChuongTrinh.value, ['ten', 'tenSyllabus', 'ma'], 'Syllabus chương trình'))
const maSyllabus = computed(() => layTruong(syllabusChuongTrinh.value, ['ma', 'maSyllabus']))
const duongDanTep = computed(() => layTruong(syllabusChuongTrinh.value, ['duongDan', 'fileUrl', 'tepDinhKem', 'url']))

const danhSachMucTieu = computed(() => layMangDauTien(cauTruc.value, ['mucTieuChuongTrinh', 'mucTieu', 'danhSachMucTieu']))
const danhSachNangLuc = computed(() => layMangDauTien(cauTruc.value, ['nangLucDauRa', 'nangLuc', 'danhSachNangLuc']))
const danhSachViTri = computed(() => layMangDauTien(cauTruc.value, ['viTriViecLam', 'viTri', 'danhSachViTri']))
const danhSachDieuKien = computed(() => layMangDauTien(cauTruc.value, ['dieuKienTotNghiep', 'dieuKien', 'danhSachDieuKien']))
const danhSachNhomKienThuc = computed(() => layMangDauTien(cauTruc.value, ['nhomKienThuc', 'danhSachNhomKienThuc']))
const danhSachNhomTuChon = computed(() => layMangDauTien(cauTruc.value, ['nhomTuChon', 'danhSachNhomTuChon']))
const danhSachMonTrongChuongTrinh = computed(() => layMangDauTien(cauTruc.value, ['monTrongChuongTrinh', 'danhSachMonHoc', 'monHocList']))

function laySyllabusApDung(row) {
  const list = Array.isArray(row?.syllabusMonHoc) ? row.syllabusMonHoc : []
  return list?.[0]?.syllabusMonHoc || null
}

const danhSachMonTrongKy = computed(() => {
  const danhSachKy = layMangDauTien(cauTruc.value, ['khungKy', 'danhSachKhungKy'])
  return danhSachKy.flatMap((ky) => {
    const monTrongKy = layMangDauTien(ky, ['monTrongKy', 'danhSachMonTrongKy'])
    return monTrongKy.map((row) => ({ ...row, _tenKy: ky.tenKy || ky.maKy || null }))
  })
})

const soMonChuaCoSyllabus = computed(() =>
    danhSachMonTrongKy.value.filter((row) => !laySyllabusApDung(row)).length
)

const tongHopChuongTrinh = computed(() =>
    danhSachMonTrongKy.value.reduce(
        (acc, row) => {
          const syllabus = laySyllabusApDung(row)
          if (!syllabus) return acc
          return {
            soTinChi: acc.soTinChi + (Number(syllabus.soTinChi) || 0),
            tongGio: acc.tongGio + (Number(syllabus.tongGio) || 0),
            gioLyThuyet: acc.gioLyThuyet + (Number(syllabus.gioLyThuyet) || 0),
            gioThucHanh: acc.gioThucHanh + (Number(syllabus.gioThucHanh) || 0),
            gioKiemTra: acc.gioKiemTra + (Number(syllabus.gioKiemTra) || 0),
          }
        },
        { soTinChi: 0, tongGio: 0, gioLyThuyet: 0, gioThucHanh: 0, gioKiemTra: 0 }
    )
)

function dinhDangSo(value) {
  const n = Number(value)
  if (!n && n !== 0) return '—'
  return n % 1 === 0 ? String(n) : n.toFixed(1)
}

const tongBangLienQuan = computed(() => [
  danhSachMucTieu.value,
  danhSachNangLuc.value,
  danhSachViTri.value,
  danhSachDieuKien.value,
  danhSachNhomKienThuc.value,
  danhSachNhomTuChon.value,
  danhSachMonTrongKy.value
].filter((items) => items.length > 0).length)

const thongTinChinh = computed(() => locDongCoGiaTri([
  { label: 'Mã syllabus', value: maSyllabus.value },
  { label: 'Tên syllabus', value: tenSyllabus.value },
  { label: 'Đối tượng tuyển sinh', value: layTruong(syllabusChuongTrinh.value, ['doiTuongTuyenSinh']) },
  { label: 'Thời gian đào tạo', value: layTruong(syllabusChuongTrinh.value, ['thoiGianDaoTao']) },
  { label: 'Khối lượng kiến thức', value: layTruong(syllabusChuongTrinh.value, ['khoiLuongKienThuc']) },
  { label: 'Cập nhật lần cuối', value: dinhDangNgay(layTruong(syllabusChuongTrinh.value, ['updatedAt'])) }
]))

const noiDungSyllabus = computed(() => [
  { key: 'moTaTongQuan', title: 'Mô tả tổng quan', value: layTruong(syllabusChuongTrinh.value, ['moTaTongQuan']) },
  { key: 'mucDich', title: 'Mục đích', value: layTruong(syllabusChuongTrinh.value, ['mucDich']) },
  { key: 'mucTieu', title: 'Mục tiêu', value: layTruong(syllabusChuongTrinh.value, ['mucTieu']) },
  { key: 'yeuCauDaoTao', title: 'Yêu cầu đào tạo', value: layTruong(syllabusChuongTrinh.value, ['yeuCauDaoTao']) },
  { key: 'dieuKienTotNghiep', title: 'Điều kiện tốt nghiệp', value: layTruong(syllabusChuongTrinh.value, ['dieuKienTotNghiep']) },
  { key: 'phuongPhapDaoTao', title: 'Phương pháp đào tạo', value: layTruong(syllabusChuongTrinh.value, ['phuongPhapDaoTao']) },
  { key: 'phuongPhapDanhGia', title: 'Phương pháp đánh giá', value: layTruong(syllabusChuongTrinh.value, ['phuongPhapDanhGia']) },
  { key: 'huongDanThucHien', title: 'Hướng dẫn thực hiện', value: layTruong(syllabusChuongTrinh.value, ['huongDanThucHien']) },
  { key: 'ghiChu', title: 'Ghi chú', value: layTruong(syllabusChuongTrinh.value, ['ghiChu']) }
])

const noiDungSyllabusCoNoiDung = computed(() => noiDungSyllabus.value.filter((item) => coGiaTri(item.value)))

const bangLienQuan = computed(() => [
  {
    key: 'mucTieu',
    title: 'Mục tiêu chương trình',
    description: 'Các mục tiêu đào tạo gắn với version đang xem.',
    rows: danhSachMucTieu.value,
    columns: cotMucTieu,
    emptyText: 'Chưa có mục tiêu chương trình.'
  },
  {
    key: 'nangLuc',
    title: 'Năng lực đầu ra',
    description: 'Chuẩn năng lực đầu ra của chương trình.',
    rows: danhSachNangLuc.value,
    columns: cotNangLuc,
    emptyText: 'Chưa có năng lực đầu ra.'
  },
  {
    key: 'viTri',
    title: 'Vị trí việc làm',
    description: 'Vị trí/chức danh việc làm sau đào tạo.',
    rows: danhSachViTri.value,
    columns: cotViTri,
    emptyText: 'Chưa có vị trí việc làm.'
  },
  {
    key: 'dieuKien',
    title: 'Điều kiện tốt nghiệp',
    description: 'Các điều kiện xét tốt nghiệp.',
    rows: danhSachDieuKien.value,
    columns: cotDieuKien,
    emptyText: 'Chưa có điều kiện tốt nghiệp.'
  },
  {
    key: 'nhomKienThuc',
    title: 'Nhóm kiến thức',
    description: 'Nhóm kiến thức và tổng hợp tín chỉ/giờ.',
    rows: danhSachNhomKienThuc.value,
    columns: cotNhomKienThuc,
    emptyText: 'Chưa có nhóm kiến thức.'
  },
  {
    key: 'nhomTuChon',
    title: 'Nhóm tự chọn',
    description: 'Nhóm môn học tự chọn của chương trình.',
    rows: danhSachNhomTuChon.value,
    columns: cotNhomTuChon,
    emptyText: 'Chưa có nhóm tự chọn.'
  },
  {
    key: 'monTrongKy',
    title: 'Môn trong kỳ',
    description: 'Tổng theo Kỳ → Môn → Syllabus áp dụng.',
    rows: danhSachMonTrongKy.value,
    columns: cotMonTrongKy,
    emptyText: 'Chưa có môn nào được xếp vào kỳ.'
  }
])

const cotMucTieu = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ma', label: 'Mã', render: (row) => hienThi(layTruong(row, ['ma', 'maMucTieu'])) },
  { key: 'noiDung', label: 'Mục tiêu', render: (row) => hienThi(layNoiDung(row)) },
  { key: 'ghiChu', label: 'Ghi chú', render: (row) => hienThi(layTruong(row, ['ghiChu', 'moTa'])) }
]

const cotNangLuc = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ma', label: 'Mã', render: (row) => hienThi(layTruong(row, ['ma', 'maNangLuc'])) },
  { key: 'noiDung', label: 'Năng lực đầu ra', render: (row) => hienThi(layNoiDung(row)) },
  { key: 'mucDo', label: 'Mức độ', render: (row) => hienThi(layTruong(row, ['mucDo', 'capDo', 'level'])) }
]

const cotViTri = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ten', label: 'Vị trí', render: (row) => hienThi(layTruong(row, ['ten', 'tenViTri', 'chucDanh']) || layNoiDung(row)) },
  { key: 'moTa', label: 'Mô tả', render: (row) => hienThi(layTruong(row, ['moTa', 'noiDung', 'ghiChu'])) }
]

const cotDieuKien = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ma', label: 'Mã', render: (row) => hienThi(layTruong(row, ['ma', 'maDieuKien'])) },
  { key: 'noiDung', label: 'Điều kiện', render: (row) => hienThi(layNoiDung(row)) },
  { key: 'batBuoc', label: 'Bắt buộc', render: (row) => hienThiBoolean(layTruong(row, ['batBuoc', 'required'])) }
]

const cotNhomKienThuc = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ma', label: 'Mã', render: (row) => hienThi(layTruong(row, ['nhomKienThuc.ma', 'ma'])) },
  { key: 'ten', label: 'Tên nhóm', render: (row) => hienThi(layTruong(row, ['nhomKienThuc.ten', 'ten', 'tenNhom'])) },
  { key: 'tinChi', label: 'Tín chỉ', render: (row) => hienThi(layTruong(row, ['tongTinChi', 'soTinChi'])) },
  { key: 'gio', label: 'Tổng giờ', render: (row) => hienThi(layTruong(row, ['tongSoGio', 'tongGio'])) }
]

const cotNhomTuChon = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ma', label: 'Mã', render: (row) => hienThi(layTruong(row, ['nhomTuChon.ma', 'ma'])) },
  { key: 'ten', label: 'Tên nhóm', render: (row) => hienThi(layTruong(row, ['nhomTuChon.ten', 'ten', 'tenNhom'])) },
  { key: 'soMon', label: 'Số môn', render: (row) => hienThi((layMangDauTien(row, ['monTuChon', 'monHoc']).length || null)) }
]

const cotMonTrongChuongTrinh = []

const cotMonTrongKy = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ky', label: 'Kỳ', render: (row) => hienThi(row._tenKy) },
  { key: 'ma', label: 'Mã môn', render: (row) => hienThi(layTruong(row, ['monHoc.maMon', 'monHoc.ma', 'chuongTrinhMon.maMonTrongCt', 'maMon', 'ma'])) },
  { key: 'ten', label: 'Tên môn', render: (row) => hienThi(layTruong(row, ['monHoc.tenMon', 'monHoc.ten', 'tenMon', 'ten'])) },
  {
    key: 'tinChi', label: 'TC',
    render: (row) => {
      const s = laySyllabusApDung(row)
      return s ? hienThi(s.soTinChi) : h('span', { class: 'text-muted' }, '—')
    }
  },
  {
    key: 'tongGio', label: 'Tổng giờ',
    render: (row) => {
      const s = laySyllabusApDung(row)
      return s ? hienThi(s.tongGio) : h('span', { class: 'text-muted' }, '—')
    }
  },
  {
    key: 'gioLyThuyet', label: 'Giờ LT',
    render: (row) => {
      const s = laySyllabusApDung(row)
      return s ? hienThi(s.gioLyThuyet) : h('span', { class: 'text-muted' }, '—')
    }
  },
  {
    key: 'gioThucHanh', label: 'Giờ TH',
    render: (row) => {
      const s = laySyllabusApDung(row)
      return s ? hienThi(s.gioThucHanh) : h('span', { class: 'text-muted' }, '—')
    }
  },
  {
    key: 'gioKiemTra', label: 'Giờ KT',
    render: (row) => {
      const s = laySyllabusApDung(row)
      return s ? hienThi(s.gioKiemTra) : h('span', { class: 'text-muted' }, '—')
    }
  },
  {
    key: 'syllabus', label: 'Syllabus môn',
    render: (row) => {
      const syllabusId = laySyllabusApDung(row)?.id
      if (!syllabusId) return h('span', { class: 'missing-pill' }, 'Chưa có')
      return h('button', {
        type: 'button',
        class: 'btn-xem-syllabus',
        onClick: () => router.push({ path: '/syllabus-mon-hoc/xem', query: { syllabusMonHocId: syllabusId } })
      }, 'Xem syllabus')
    }
  }
]

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  if (!chuongTrinhId.value) {
    errorMessage.value = 'Thiếu thông tin chương trình đào tạo để xem syllabus chương trình.'
    return
  }

  loading.value = true
  errorMessage.value = ''
  payload.value = null

  try {
    const res = await daoTaoService.chuongTrinh.getTongThe(chuongTrinhId.value, versionId.value || null, {
      includeSyllabusDetail: false
    })
    payload.value = layDataTuApiResponse(res)
  } catch (error) {
    errorMessage.value = layThongBaoLoi(error, 'Không tải được syllabus chương trình.')
  } finally {
    loading.value = false
  }
}

function quayLaiTongQuan() {
  router.push({ name: TEN_ROUTE_TONG_QUAN }).catch(() => {})
}

function xemTep() {
  const syllabusId = syllabusChuongTrinh.value?.id
  if (!syllabusId) return

  router.push({
    name: 'DaoTao.XemChuongTrinh.SyllabusTep',
    params: {
      nganhId: 0,
      chuongTrinhId: chuongTrinhId.value || 0,
      versionId: versionId.value || 0,
      chuongTrinhMonId: 0
    },
    query: {
      tepNguon: 'syllabusChuongTrinh',
      syllabusChuongTrinhId: syllabusId
    }
  })
}

function toggleBang(key) {
  if (isBangDangMo(key)) {
    bangDangMo.value = bangDangMo.value.filter((item) => item !== key)
    return
  }
  bangDangMo.value = [...bangDangMo.value, key]
}

function isBangDangMo(key) {
  return bangDangMo.value.includes(key)
}

function moTatCaBang() {
  bangDangMo.value = bangLienQuan.value.map((item) => item.key)
}

function thuGonTatCaBang() {
  bangDangMo.value = []
}

function layDataTuApiResponse(res) {
  if (res?.success !== undefined && res?.data !== undefined) return res.data
  if (res?.data?.success !== undefined && res?.data?.data !== undefined) return res.data.data
  if (res?.data !== undefined) return res.data
  return res
}

function layMangDauTien(source, keys = []) {
  for (const key of keys) {
    const value = layTruong(source, [key])
    if (Array.isArray(value)) return value
    if (Array.isArray(value?.content)) return value.content
    if (Array.isArray(value?.items)) return value.items
  }
  return []
}

function layId(item) {
  return item?.id ?? item?.value ?? null
}

function layTen(item, keys = [], fallback = '—') {
  return layTruong(item, keys) || fallback
}

function layNoiDung(row) {
  return layTruong(row, ['noiDung', 'moTa', 'ten', 'mucTieu', 'ghiChu'])
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

function locDongCoGiaTri(items = []) {
  return items.filter((item) => coGiaTri(item.value))
}

function coGiaTri(value) {
  return value !== null && value !== undefined && value !== ''
}

function hienThi(value) {
  if (!coGiaTri(value)) return '—'
  return value
}

function hienThiBoolean(value) {
  if (value === true || value === 1) return 'Có'
  if (value === false || value === 0) return 'Không'

  if (typeof value === 'string') {
    const normalized = value.trim().toLowerCase()
    if (['true', '1', 'co', 'có', 'yes'].includes(normalized)) return 'Có'
    if (['false', '0', 'khong', 'không', 'no'].includes(normalized)) return 'Không'
  }

  return '—'
}

function dinhDangNgay(value) {
  if (!value) return null
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return value
  return date.toLocaleDateString('vi-VN')
}
</script>

<style scoped>
.dao-tao-xem-syllabus-chuong-trinh-page {
  min-height: calc(100vh - var(--header-height, 60px));
  padding: 8px 10px 18px;
  background:
      radial-gradient(circle at top left, rgba(7, 113, 73, 0.08), transparent 28%),
      linear-gradient(180deg, #f3fbf7 0%, #f8fafc 46%, #ffffff 100%);
  color: #0f172a;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.top-shell,
.overview-strip,
.compact-file-bar,
.table-card,
.state-card {
  border: 1px solid #d5eadf;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.97);
  box-shadow: 0 6px 18px rgba(15, 23, 42, 0.045);
}

.top-shell {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 10px;
  align-items: start;
  padding: 9px 12px;
  margin-bottom: 8px;
  border-color: #b8dcc8;
  background: linear-gradient(135deg, #ffffff 0%, #f1fbf5 58%, #e5f5eb 100%);
}

.top-left {
  min-width: 0;
}

.back-btn {
  border: 0;
  background: transparent;
  color: #077149;
  padding: 0 0 3px;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.back-btn:hover {
  color: #055d3d;
  text-decoration: underline;
}

.top-title-row {
  display: flex;
  flex-wrap: wrap;
  gap: 7px 10px;
  align-items: baseline;
}

.eyebrow {
  margin: 0;
  color: #077149;
  font-size: 10px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.top-shell h1,
.overview-name h2,
.section-title h2 {
  margin: 0;
  color: #0f172a;
  font-weight: 900;
  line-height: 1.2;
}

.top-shell h1 {
  font-size: 20px;
}

.overview-name h2 {
  margin-top: 2px;
  font-size: 17px;
}

.section-title h2 {
  margin-top: 2px;
  font-size: 16px;
}

.top-meta-row {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
  margin-top: 5px;
  color: #475569;
  font-size: 11.5px;
}

.top-actions,
.section-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  justify-content: flex-end;
  align-items: center;
}

.btn,
.file-button,
.mini-btn {
  min-height: 28px;
  border: 1px solid #a9d5bd;
  border-radius: 9px;
  background: #ffffff;
  color: #064e3b;
  padding: 5px 10px;
  font-size: 11.5px;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
  text-decoration: none;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease, box-shadow 0.15s ease;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.btn:hover,
.file-button:hover,
.mini-btn:hover {
  border-color: #077149;
  background: #effaf4;
  color: #077149;
  box-shadow: 0 3px 9px rgba(7, 113, 73, 0.11);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary,
.file-button.primary {
  border-color: #077149;
  background: #077149;
  color: #ffffff;
}

.btn.ghost {
  background: rgba(255, 255, 255, 0.9);
}

.mini-btn {
  min-height: 25px;
  padding: 4px 9px;
  font-size: 10.8px;
  border-radius: 999px;
}

.status-badge,
.badge,
.count-pill {
  display: inline-flex;
  align-items: center;
  min-height: 21px;
  border-radius: 999px;
  padding: 3px 8px;
  font-size: 10.5px;
  font-weight: 900;
  white-space: nowrap;
}

.status-badge.success {
  border: 1px solid #8fd4aa;
  background: #dcfce7;
  color: #07623f;
}

.status-badge.muted {
  border: 1px solid #d1d5db;
  background: #f8fafc;
  color: #64748b;
}

.badge.neutral,
.count-pill {
  border: 1px solid #b7dcc8;
  background: #f7fdf9;
  color: #065f46;
}

.overview-strip {
  display: grid;
  grid-template-columns: minmax(260px, 0.75fr) minmax(720px, 1.6fr);
  gap: 10px;
  align-items: stretch;
  padding: 9px 12px;
  margin-bottom: 8px;
  border-color: #b8dcc8;
  background: linear-gradient(135deg, #ffffff 0%, #f3fbf7 100%);
}

.overview-name {
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(10, minmax(64px, 1fr));
  gap: 5px;
}

.metric-grid article {
  min-width: 0;
  border: 1px solid #d8ede1;
  border-radius: 9px;
  background: #ffffff;
  padding: 6px 7px;
}

.metric-grid span {
  display: block;
  overflow: hidden;
  color: #486456;
  font-size: 9.2px;
  font-weight: 900;
  text-overflow: ellipsis;
  text-transform: uppercase;
  white-space: nowrap;
  letter-spacing: 0.02em;
}

.metric-grid strong {
  display: block;
  margin-top: 2px;
  color: #077149;
  font-size: 15px;
  font-weight: 900;
  line-height: 1.1;
}

.metric-grid article.total {
  border-color: #a9d5bd;
  background: linear-gradient(135deg, #f1fbf5 0%, #e6f6ec 100%);
}

.metric-grid article.total strong {
  color: #055d3d;
}

.state-card {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  margin-bottom: 8px;
}

.state-card h3 {
  margin: 0 0 3px;
  font-size: 14px;
}

.state-card p,
.state-card span {
  margin: 0;
  color: #475569;
  font-size: 12px;
}

.state-card.error {
  display: block;
  border-color: #fecaca;
  background: #fef2f2;
}

.state-card.error p {
  margin-bottom: 9px;
  color: #991b1b;
}

.state-card.empty {
  display: block;
  background: #f8fafc;
}

.state-card.warning {
  border-color: #fde68a;
  background: #fffbeb;
  color: #92400e;
}

.compact-warning {
  justify-content: flex-start;
  padding: 7px 10px;
}

.compact-warning strong {
  font-size: 12px;
  white-space: nowrap;
}

.compact-warning span {
  color: #78350f;
}

.spinner {
  width: 23px;
  height: 23px;
  border: 3px solid #dff4e8;
  border-top-color: #077149;
  border-radius: 999px;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.compact-file-bar {
  display: grid;
  grid-template-columns: minmax(220px, 0.7fr) minmax(360px, 1fr);
  gap: 10px;
  align-items: center;
  padding: 8px 10px;
  margin-bottom: 8px;
  background: #ffffff;
}

.file-main {
  min-width: 0;
}

.file-label {
  display: block;
  color: #077149;
  font-size: 10px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.file-main strong {
  display: block;
  margin-top: 2px;
  overflow: hidden;
  color: #0f172a;
  font-size: 13px;
  font-weight: 900;
  line-height: 1.25;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-actions {
  display: flex;
  gap: 6px;
  align-items: center;
  min-width: 0;
}

.file-path {
  flex: 1;
  min-width: 140px;
  overflow: hidden;
  border: 1px solid #d7ecdf;
  border-radius: 9px;
  background: #f8fdf9;
  color: #476455;
  padding: 6px 8px;
  font-size: 11px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.file-empty {
  color: #64748b;
  font-size: 12px;
  font-weight: 700;
}

.compact-info-list {
  margin-bottom: 8px;
}

.related-section {
  display: grid;
  gap: 8px;
}

.section-title {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  align-items: flex-start;
  padding: 2px 1px;
}

.accordion-list {
  display: grid;
  gap: 7px;
}

.table-card {
  padding: 0;
  overflow: hidden;
  border-color: #d6eadf;
  box-shadow: 0 5px 14px rgba(15, 23, 42, 0.04);
}

.table-card.open {
  border-color: #9fd1b5;
}

.accordion-header {
  display: grid;
  grid-template-columns: 26px minmax(0, 1fr) auto;
  gap: 8px;
  align-items: center;
  width: 100%;
  border: 0;
  background: linear-gradient(135deg, #ffffff 0%, #f6fcf8 100%);
  color: #0f172a;
  padding: 7px 9px;
  text-align: left;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.accordion-header:hover {
  background: #effaf4;
}

.toggle-icon {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 23px;
  height: 23px;
  border: 1px solid #a9d5bd;
  border-radius: 7px;
  background: #077149;
  color: #ffffff;
  font-size: 16px;
  font-weight: 900;
  line-height: 1;
}

.accordion-title {
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.accordion-title strong {
  overflow: hidden;
  color: #0f172a;
  font-size: 13px;
  font-weight: 900;
  line-height: 1.2;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.accordion-title small {
  margin-top: 1px;
  overflow: hidden;
  color: #64748b;
  font-size: 10.8px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.canh-bao-inline {
  color: #b45309 !important;
  font-weight: 800;
}

.accordion-body {
  border-top: 1px solid #e2f2e8;
  padding: 7px;
  background: #ffffff;
}

.info-table {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 6px;
}

.info-row {
  display: grid;
  grid-template-columns: 150px minmax(0, 1fr);
  gap: 8px;
  align-items: start;
  min-width: 0;
  border: 1px solid #e2f2e8;
  border-radius: 9px;
  background: #fbfefc;
  padding: 7px 8px;
}

.info-label {
  color: #065f46;
  font-size: 10.5px;
  font-weight: 900;
  text-transform: uppercase;
}

.info-value {
  color: #0f172a;
  font-size: 12px;
  line-height: 1.35;
  word-break: break-word;
}

.text-data-table {
  overflow: hidden;
  border: 1px solid #d7ecdf;
  border-radius: 10px;
}

.text-data-head,
.text-data-row {
  display: grid;
  grid-template-columns: 190px minmax(0, 1fr);
}

.text-data-head {
  background: #e7f6ed;
  color: #065f46;
  font-size: 11px;
  font-weight: 900;
  text-transform: uppercase;
}

.text-data-head span,
.text-data-title,
.text-data-content {
  padding: 7px 8px;
  border-bottom: 1px solid #edf3f0;
}

.text-data-row:last-child .text-data-title,
.text-data-row:last-child .text-data-content {
  border-bottom: 0;
}

.text-data-title {
  background: #fbfefc;
  color: #065f46;
  font-size: 12px;
  font-weight: 900;
}

.text-data-content {
  color: #0f172a;
  font-size: 12px;
  line-height: 1.45;
  white-space: pre-line;
}

.empty-inline {
  border: 1px dashed #b7dcc8;
  border-radius: 10px;
  background: #f8fdf9;
  color: #476455;
  padding: 9px;
  font-size: 12px;
  font-weight: 700;
}

.empty-inline.small {
  padding: 7px 8px;
  font-size: 11.5px;
}

.table-wrap {
  width: 100%;
  max-height: 70vh;
  overflow: auto;
  border: 1px solid #d7ecdf;
  border-radius: 10px;
}

:deep(table) {
  width: 100%;
  min-width: 980px;
  border-collapse: separate;
  border-spacing: 0;
  background: #ffffff;
}

:deep(th),
:deep(td) {
  border-bottom: 1px solid #edf3f0;
  padding: 6px 8px;
  text-align: left;
  vertical-align: top;
  font-size: 11.5px;
  line-height: 1.35;
}

:deep(th) {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #e7f6ed;
  color: #065f46;
  font-weight: 900;
  white-space: nowrap;
  box-shadow: inset 0 -1px 0 #cfe8da;
}

:deep(td) {
  color: #0f172a;
}

:deep(td:first-child),
:deep(th:first-child) {
  width: 52px;
  text-align: center;
}

:deep(tbody tr:hover) {
  background: #f7fdf9;
}

:deep(tbody tr:last-child td) {
  border-bottom: 0;
}

:deep(.text-muted) {
  color: #94a3b8;
}

:deep(.missing-pill) {
  display: inline-flex;
  align-items: center;
  min-height: 22px;
  border: 1px solid #e2e8f0;
  border-radius: 999px;
  background: #f8fafc;
  color: #94a3b8;
  padding: 2px 8px;
  font-size: 10.5px;
  font-weight: 800;
  white-space: nowrap;
}

.btn-xem-syllabus {
  display: inline-flex;
  align-items: center;
  min-height: 23px;
  border: 1px solid #9fd1b5;
  border-radius: 999px;
  background: #effaf4;
  color: #077149;
  padding: 3px 10px;
  font-size: 10.5px;
  font-weight: 900;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
  white-space: nowrap;
}

.btn-xem-syllabus:hover {
  border-color: #077149;
  background: #077149;
  color: #ffffff;
}

@media (max-width: 1280px) {
  .overview-strip {
    grid-template-columns: 1fr;
  }

  .metric-grid {
    grid-template-columns: repeat(5, minmax(0, 1fr));
  }

  .compact-file-bar {
    grid-template-columns: 1fr;
  }

  .info-table {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 860px) {
  .top-shell,
  .section-title {
    grid-template-columns: 1fr;
  }

  .top-actions,
  .section-actions {
    justify-content: flex-start;
  }

  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .accordion-header {
    grid-template-columns: 26px minmax(0, 1fr);
  }

  .accordion-header .count-pill {
    grid-column: 2;
    justify-self: start;
  }

  .info-row,
  .text-data-head,
  .text-data-row {
    grid-template-columns: 1fr;
  }

  .text-data-title {
    border-bottom: 0;
  }
}

@media (max-width: 640px) {
  .dao-tao-xem-syllabus-chuong-trinh-page {
    padding: 8px;
  }

  .top-shell h1 {
    font-size: 18px;
  }

  .metric-grid {
    grid-template-columns: 1fr;
  }

  .file-actions {
    flex-wrap: wrap;
  }

  .file-path {
    flex-basis: 100%;
  }
}
</style>

