<template>
  <div class="dao-tao-xem-syllabus-chuong-trinh-page">
    <section class="page-header">
      <div>
        <button type="button" class="back-btn" @click="quayLaiTongQuan">
          ← Quay lại tổng quan
        </button>

        <p class="eyebrow">Syllabus chương trình</p>
        <h1>{{ tenChuongTrinh }}</h1>
        <p class="version-text">
          Version: <strong>{{ tenVersion }}</strong>
        </p>
        <p class="subtitle">
          Trang chỉ xem syllabus chương trình, tệp đính kèm và các bảng cùng tầng của version. Không có thao tác thêm, sửa, xóa.
        </p>
      </div>

      <div class="header-actions">
        <button type="button" class="btn" :disabled="loading || !chuongTrinhId" @click="taiDuLieu">
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
        <p>Hệ thống đang lấy syllabus chương trình và các bảng liên quan cùng tầng.</p>
      </div>
    </section>

    <section v-else-if="!payload" class="state-card empty">
      <h3>Không có dữ liệu</h3>
      <p>Vui lòng quay lại trang tổng quan và chọn lại chương trình/version.</p>
    </section>

    <template v-else>
      <section class="summary-card">
        <div class="summary-main">
          <p class="eyebrow">Chương trình đào tạo</p>
          <h2>{{ tenChuongTrinh }}</h2>
          <div class="summary-badges">
            <span :class="['status-badge', syllabusChuongTrinh ? 'success' : 'muted']">
              {{ syllabusChuongTrinh ? 'Có syllabus chương trình' : 'Chưa có syllabus chương trình' }}
            </span>
            <span v-if="maChuongTrinh" class="status-badge neutral">Mã chương trình: {{ maChuongTrinh }}</span>
            <span v-if="maSyllabus" class="status-badge neutral">Mã syllabus: {{ maSyllabus }}</span>
          </div>
        </div>

        <div class="metric-grid">
          <article>
            <span>Mục tiêu</span>
            <strong>{{ danhSachMucTieu.length }}</strong>
          </article>
          <article>
            <span>Năng lực đầu ra</span>
            <strong>{{ danhSachNangLuc.length }}</strong>
          </article>
          <article>
            <span>Vị trí việc làm</span>
            <strong>{{ danhSachViTri.length }}</strong>
          </article>
          <article>
            <span>Điều kiện TN</span>
            <strong>{{ danhSachDieuKien.length }}</strong>
          </article>
          <article>
            <span>Môn trong CT</span>
            <strong>{{ danhSachMonTrongChuongTrinh.length }}</strong>
          </article>

          <article class="tong-hop">
            <span>Tổng TC</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.soTinChi) }}</strong>
          </article>
          <article class="tong-hop">
            <span>Tổng giờ</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.tongGio) }}</strong>
          </article>
          <article class="tong-hop">
            <span>Giờ LT</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.gioLyThuyet) }}</strong>
          </article>
          <article class="tong-hop">
            <span>Giờ TH</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.gioThucHanh) }}</strong>
          </article>
          <article class="tong-hop">
            <span>Giờ KT</span>
            <strong>{{ dinhDangSo(tongHopChuongTrinh.gioKiemTra) }}</strong>
          </article>
        </div>
      </section>

      <section v-if="!syllabusChuongTrinh" class="state-card empty">
        <h3>Version này chưa có syllabus chương trình</h3>
        <p>Hiện chưa có bản syllabus chương trình áp dụng cho version đang chọn.</p>
      </section>

      <section v-else class="content-grid">
        <article class="info-card full hero-card">
          <div class="card-title-row">
            <div>
              <p class="card-eyebrow">Thông tin chính</p>
              <h3>{{ tenSyllabus }}</h3>
            </div>
            <span class="readonly-pill">Chỉ xem</span>
          </div>

          <dl class="info-list compact">
            <template v-for="item in thongTinChinh" :key="item.label">
              <dt>{{ item.label }}</dt>
              <dd>{{ item.value }}</dd>
            </template>
          </dl>
        </article>

        <article class="info-card full file-card">
          <div class="card-title-row">
            <div>
              <p class="card-eyebrow">Tệp syllabus</p>
              <h3>Xem tệp / tải tệp</h3>
            </div>
          </div>

          <div v-if="duongDanTep" class="file-actions">
            <button type="button" class="file-button primary" @click="xemTep">
              Xem tệp
            </button>
            <a class="file-button" :href="duongDanTep" download>
              Tải về
            </a>
            <span class="file-path">{{ duongDanTep }}</span>
          </div>

          <div v-else class="empty-inline">
            Chưa có đường dẫn tệp syllabus chương trình.
          </div>
        </article>

        <article v-for="block in noiDungSyllabus" :key="block.key" class="info-card">
          <h3>{{ block.title }}</h3>
          <div v-if="block.value" class="rich-text">
            {{ block.value }}
          </div>
          <div v-else class="empty-inline">
            Chưa có nội dung.
          </div>
        </article>
      </section>

      <section class="related-section">
        <div class="section-title">
          <div>
            <p>Dữ liệu cùng gắn với version chương trình đang xem.</p>
          </div>
          <span class="badge neutral">{{ tongBangLienQuan }} bảng có dữ liệu</span>
        </div>

        <div class="related-grid">
          <article class="table-card full">
            <header>
              <div>
                <h3>Mục tiêu chương trình</h3>
              </div>
              <span class="count-pill">{{ danhSachMucTieu.length }}</span>
            </header>
            <SimpleTable :rows="danhSachMucTieu" :columns="cotMucTieu" empty-text="Chưa có mục tiêu chương trình." />
          </article>

          <article class="table-card full">
            <header>
              <div>
                <p class="card-eyebrow">Năng lực đầu ra</p>
              </div>
              <span class="count-pill">{{ danhSachNangLuc.length }}</span>
            </header>
            <SimpleTable :rows="danhSachNangLuc" :columns="cotNangLuc" empty-text="Chưa có năng lực đầu ra." />
          </article>

          <article class="table-card full">
            <header>
              <div>
                <p class="card-eyebrow">Vị trí việc làm</p>
              </div>
              <span class="count-pill">{{ danhSachViTri.length }}</span>
            </header>
            <SimpleTable :rows="danhSachViTri" :columns="cotViTri" empty-text="Chưa có vị trí việc làm." />
          </article>

          <article class="table-card full">
            <header>
              <div>
                <p class="card-eyebrow">Điệu kiện tốt nghiệp</p>
              </div>
              <span class="count-pill">{{ danhSachDieuKien.length }}</span>
            </header>
            <SimpleTable :rows="danhSachDieuKien" :columns="cotDieuKien" empty-text="Chưa có điều kiện tốt nghiệp." />
          </article>

          <article class="table-card full">
            <header>
              <div>
                <p class="card-eyebrow">Nhóm kiến thức</p>
              </div>
              <span class="count-pill">{{ danhSachNhomKienThuc.length }}</span>
            </header>
            <SimpleTable :rows="danhSachNhomKienThuc" :columns="cotNhomKienThuc" empty-text="Chưa có nhóm kiến thức." />
          </article>

          <article class="table-card full">
            <header>
              <div>
                <p class="card-eyebrow">Nhóm tự chọn</p>
              </div>
              <span class="count-pill">{{ danhSachNhomTuChon.length }}</span>
            </header>
            <SimpleTable :rows="danhSachNhomTuChon" :columns="cotNhomTuChon" empty-text="Chưa có nhóm tự chọn." />
          </article>

          <article class="table-card full">
            <header>
              <div>
                <p class="card-eyebrow">Bảng cùng tầng</p>
                <h3>Môn trong chương trình</h3>
              </div>
              <span class="count-pill">{{ danhSachMonTrongChuongTrinh.length }}</span>
            </header>
            <SimpleTable :rows="danhSachMonTrongChuongTrinh" :columns="cotMonTrongChuongTrinh" empty-text="Chưa có môn trong chương trình." />
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
        return h('div', { class: 'empty-inline' }, props.emptyText)
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

const tongHopChuongTrinh = computed(() =>
  danhSachMonTrongChuongTrinh.value.reduce(
    (acc, row) => {
      const mon = row.chuongTrinhMon || {}
      return {
        soTinChi: acc.soTinChi + (Number(mon.soTinChi) || 0),
        tongGio: acc.tongGio + (Number(mon.tongGio) || 0),
        gioLyThuyet: acc.gioLyThuyet + (Number(mon.gioLyThuyet) || 0),
        gioThucHanh: acc.gioThucHanh + (Number(mon.gioThucHanh) || 0),
        gioKiemTra: acc.gioKiemTra + (Number(mon.gioKiemTra) || 0),
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
  danhSachMonTrongChuongTrinh.value
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

const cotMonTrongChuongTrinh = [
  { key: 'stt', label: 'STT', render: (_row, index) => index + 1 },
  { key: 'ma', label: 'Mã môn', render: (row) => hienThi(layTruong(row, ['monHoc.maMon', 'monHoc.ma', 'maMon', 'ma'])) },
  { key: 'ten', label: 'Tên môn', render: (row) => hienThi(layTruong(row, ['monHoc.tenMon', 'monHoc.ten', 'tenMon', 'ten'])) },
  { key: 'tinChi', label: 'TC', render: (row) => hienThi(layTruong(row, ['chuongTrinhMon.soTinChi', 'soTinChi'])) },
  { key: 'tongGio', label: 'Tổng giờ', render: (row) => hienThi(layTruong(row, ['chuongTrinhMon.tongGio', 'tongGio'])) },
  { key: 'gioLyThuyet', label: 'Giờ LT', render: (row) => hienThi(layTruong(row, ['chuongTrinhMon.gioLyThuyet'])) },
  { key: 'gioThucHanh', label: 'Giờ TH', render: (row) => hienThi(layTruong(row, ['chuongTrinhMon.gioThucHanh'])) },
  { key: 'gioKiemTra', label: 'Giờ KT', render: (row) => hienThi(layTruong(row, ['chuongTrinhMon.gioKiemTra'])) },
  { key: 'syllabus', label: 'Syllabus môn', render: (row) => {
    const syllabusId = row.syllabusMonHoc?.[0]?.syllabusMonHoc?.id
    if (!syllabusId) return h('span', { style: 'color:#94a3b8;font-size:12px;' }, 'Chưa có')
    return h('button', { type: 'button', class: 'btn-xem-syllabus', onClick: () => router.push({ path: '/syllabus-mon-hoc/xem', query: { syllabusMonHocId: syllabusId } }) }, 'Xem syllabus')
  }}
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
  padding: 12px 16px 24px;
  background: linear-gradient(180deg, #eef7ff 0%, #f8fbff 42%, #ffffff 100%);
  color: #0f172a;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.page-header,
.summary-card,
.info-card,
.table-card,
.state-card {
  border: 1px solid #bfdbfe;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 10px 28px rgba(15, 82, 143, 0.10);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 16px;
  padding: 18px;
  margin-bottom: 14px;
  background: linear-gradient(135deg, #ffffff 0%, #eaf4ff 56%, #dff0ff 100%);
}

.back-btn {
  border: 0;
  background: transparent;
  color: #0b5a92;
  padding: 0 0 8px;
  font-size: 13px;
  font-weight: 900;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.back-btn:hover {
  color: #08456f;
  text-decoration: underline;
}

.eyebrow,
.card-eyebrow {
  margin: 0 0 4px;
  color: #0b5a92;
  font-size: 12px;
  font-weight: 900;
  text-transform: uppercase;
  letter-spacing: 0.04em;
}

.page-header h1,
.summary-main h2,
.info-card h3,
.table-card h3,
.section-title h2 {
  margin: 0;
  color: #0f172a;
  font-weight: 800;
  line-height: 1.25;
}

.page-header h1 {
  font-size: 24px;
}

.summary-main h2 {
  font-size: 21px;
}

.info-card h3,
.table-card h3 {
  font-size: 16px;
}

.version-text,
.subtitle,
.section-title p {
  margin: 6px 0 0;
  color: #42637f;
  font-size: 13px;
  line-height: 1.45;
}

.header-actions {
  display: flex;
  gap: 8px;
  flex-shrink: 0;
}

.btn,
.file-button {
  min-height: 34px;
  border: 1px solid #9fc5e8;
  border-radius: 12px;
  background: #ffffff;
  color: #0f3d64;
  padding: 8px 13px;
  font-size: 12px;
  font-weight: 900;
  cursor: pointer;
  white-space: nowrap;
  text-decoration: none;
  transition: background 0.15s ease, border-color 0.15s ease, color 0.15s ease, box-shadow 0.15s ease;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
}

.btn:hover,
.file-button:hover {
  border-color: #0b5a92;
  background: #eaf4ff;
  color: #0b5a92;
  box-shadow: 0 3px 10px rgba(15, 82, 143, 0.12);
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn.primary,
.file-button.primary {
  border-color: #0b5a92;
  background: #0b5a92;
  color: #ffffff;
}

.summary-card {
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(420px, 1fr);
  gap: 16px;
  align-items: stretch;
  padding: 18px;
  margin-bottom: 14px;
  background: linear-gradient(135deg, #ffffff 0%, #f2f8ff 100%);
}

.summary-main {
  min-width: 0;
}

.summary-badges {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.status-badge,
.badge,
.count-pill,
.readonly-pill {
  display: inline-flex;
  align-items: center;
  min-height: 24px;
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 11px;
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

.status-badge.neutral,
.badge.neutral,
.count-pill,
.readonly-pill {
  border: 1px solid #b7d3ec;
  background: #f8fbff;
  color: #0f3d64;
}

.metric-grid {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 8px;
}

.metric-grid article {
  border: 1px solid #d8ecff;
  border-radius: 12px;
  background: #ffffff;
  padding: 8px 10px;
}

.metric-grid span {
  display: block;
  color: #42637f;
  font-size: 10px;
  font-weight: 800;
  text-transform: uppercase;
  letter-spacing: 0.03em;
}

.metric-grid strong {
  display: block;
  margin-top: 3px;
  color: #0b5a92;
  font-size: 16px;
  font-weight: 900;
}

.metric-grid article.tong-hop {
  border-color: #bfdbfe;
  background: linear-gradient(135deg, #f0f8ff 0%, #e8f4ff 100%);
}

.metric-grid article.tong-hop strong {
  color: #1e40af;
}

.state-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px;
  margin-bottom: 14px;
}

.state-card h3 {
  margin: 0 0 4px;
  font-size: 16px;
}

.state-card p {
  margin: 0;
  color: #42637f;
  font-size: 13px;
}

.state-card.error {
  display: block;
  border-color: #fecaca;
  background: #fef2f2;
}

.state-card.error p {
  margin-bottom: 10px;
  color: #991b1b;
}

.state-card.empty {
  display: block;
  background: #f8fbff;
}

.spinner {
  width: 28px;
  height: 28px;
  border: 3px solid #dbeafe;
  border-top-color: #0b5a92;
  border-radius: 999px;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.content-grid,
.related-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
  align-items: start;
}

.content-grid {
  margin-bottom: 16px;
}

.info-card,
.table-card {
  min-width: 0;
  padding: 16px;
}

.info-card.full,
.table-card.full {
  grid-column: 1 / -1;
}

.hero-card {
  background: linear-gradient(135deg, #ffffff 0%, #eef7ff 100%);
}

.card-title-row,
.table-card header,
.section-title {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.info-list {
  display: grid;
  grid-template-columns: 190px minmax(0, 1fr);
  gap: 8px 14px;
  margin: 14px 0 0;
}

.info-list.compact {
  grid-template-columns: repeat(2, minmax(180px, 1fr));
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
  word-break: break-word;
}

.info-list.compact dt {
  margin-bottom: 3px;
}

.info-list.compact dd {
  margin-bottom: 8px;
}

.rich-text {
  margin-top: 10px;
  color: #1e293b;
  font-size: 13px;
  line-height: 1.7;
  white-space: pre-line;
}

.empty-inline {
  border: 1px dashed #b7d3ec;
  border-radius: 14px;
  background: #f8fbff;
  color: #42637f;
  padding: 12px;
  font-size: 13px;
  font-weight: 700;
}

.file-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
  margin-top: 12px;
}

.file-path {
  flex: 1;
  min-width: 240px;
  border: 1px solid #d8ecff;
  border-radius: 12px;
  background: #f8fbff;
  color: #42637f;
  padding: 8px 10px;
  font-size: 12px;
  word-break: break-all;
}

.related-section {
  display: grid;
  gap: 12px;
}

.table-card header {
  margin-bottom: 12px;
}

.table-wrap {
  width: 100%;
  overflow-x: auto;
  border: 1px solid #d8ecff;
  border-radius: 14px;
}

:deep(table) {
  width: 100%;
  border-collapse: collapse;
  background: #ffffff;
}

:deep(th),
:deep(td) {
  border-bottom: 1px solid #e5effa;
  padding: 10px;
  text-align: left;
  vertical-align: top;
  font-size: 12px;
  line-height: 1.45;
}

:deep(th) {
  background: #eaf4ff;
  color: #0b5a92;
  font-weight: 900;
  white-space: nowrap;
}

:deep(td) {
  color: #0f172a;
}

:deep(tbody tr:hover) {
  background: #f8fbff;
}

:deep(tbody tr:last-child td) {
  border-bottom: 0;
}

.btn-xem-syllabus {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  min-height: 26px;
  border: 1px solid #93c5fd;
  border-radius: 999px;
  background: linear-gradient(135deg, #eff6ff 0%, #dbeafe 100%);
  color: #1d4ed8;
  padding: 3px 12px;
  font-size: 11px;
  font-weight: 700;
  cursor: pointer;
  font-family: 'Roboto', Arial, Helvetica, sans-serif;
  letter-spacing: 0.02em;
  box-shadow: 0 1px 3px rgba(59, 130, 246, 0.15);
  transition: background 0.15s ease, border-color 0.15s ease, box-shadow 0.15s ease, transform 0.1s ease;
  white-space: nowrap;
}

.btn-xem-syllabus:hover {
  border-color: #3b82f6;
  background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%);
  box-shadow: 0 2px 6px rgba(59, 130, 246, 0.25);
  transform: translateY(-1px);
}

.btn-xem-syllabus:active {
  transform: translateY(0);
  box-shadow: none;
}

@media (max-width: 1180px) {
  .summary-card {
    grid-template-columns: 1fr;
  }

  .metric-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}

@media (max-width: 860px) {
  .page-header,
  .card-title-row,
  .table-card header,
  .section-title {
    flex-direction: column;
  }

  .header-actions {
    width: 100%;
  }

  .content-grid,
  .related-grid {
    grid-template-columns: 1fr;
  }

  .metric-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .info-list,
  .info-list.compact {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .dao-tao-xem-syllabus-chuong-trinh-page {
    padding: 10px;
  }

  .page-header h1 {
    font-size: 20px;
  }

  .metric-grid {
    grid-template-columns: 1fr;
  }
}
</style>
