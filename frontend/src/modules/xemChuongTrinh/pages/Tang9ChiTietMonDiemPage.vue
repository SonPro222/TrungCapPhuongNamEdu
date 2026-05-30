<template>
  <div class="tang-page">
    <XemTomTatHuongDi tieu-de="Tầng 9 - Chi tiết môn và điểm" :danh-sach="summary" />

    <div class="tang-head">
      <h2 class="tang-title">Chi tiết môn học và điểm</h2>
      <button type="button" class="btn btn-tai-lieu" @click="moTaiLieu">Xem tài liệu syllabus môn</button>
    </div>

    <XemTrangDangTai v-if="loading" />
    <p v-else-if="loi" class="loi-text">{{ loi }}</p>

    <template v-else-if="chiTiet">
      <!-- Khối 1 -->
      <XemBangThongTin
        tieu-de="Khối 1: Thông tin môn học"
        kieu="thongTin"
        :du-lieu="monHoc"
        :truong="truongMonHoc"
        thong-bao-rong="Chưa có dữ liệu môn học."
      />

      <!-- Khối 2 -->
      <XemBangThongTin
        tieu-de="Khối 2: Thông tin môn trong chương trình"
        kieu="thongTin"
        :du-lieu="chuongTrinhMon"
        :truong="truongChuongTrinhMon"
        thong-bao-rong="Chưa có dữ liệu môn trong chương trình."
      />

      <!-- Khối 3 -->
      <XemBangThongTin
        tieu-de="Khối 3: Môn tiên quyết"
        :cot="cotMonTienQuyet"
        :dong="chiTiet.monTienQuyet"
        thong-bao-rong="Môn này không có môn tiên quyết đã gán."
      />

      <!-- Khối 4 -->
      <XemBangThongTin
        tieu-de="Khối 4: Quy đổi điểm"
        :cot="cotQuyDoiDiem"
        :dong="chiTiet.quyDoiDiem"
        thong-bao-rong="Chưa có quy đổi điểm đã gán."
      />

      <!-- Khối 5 -->
      <XemBangThongTin
        tieu-de="Khối 5: Quy đổi điểm mẫu đã gán"
        :cot="cotQuyDoiDiemMauGan"
        :dong="quyDoiDiemMauGanMap"
        thong-bao-rong="Chưa có quy đổi điểm mẫu đã gán."
      />

      <!-- Khối 6 -->
      <XemBangThongTin
        tieu-de="Khối 6: Cấu hình đánh giá mẫu"
        :cot="cotCauHinhDanhGiaMau"
        :dong="chiTiet.cauHinhDanhGiaMau"
        thong-bao-rong="Chưa có cấu hình đánh giá mẫu đã gán."
      />

      <!-- Khối 7 -->
      <XemBangThongTin
        tieu-de="Khối 7: Chương bài syllabus"
        :cot="cotChuongBai"
        :dong="chiTiet.syllabusChuongBai"
        thong-bao-rong="Chưa có chương bài syllabus đã gán."
      />

      <!-- Khối 8 -->
      <XemBangThongTin
        tieu-de="Khối 8: Điều kiện môn học (trực tiếp)"
        :cot="cotDieuKienMonHoc"
        :dong="chiTiet.dieuKienMonHoc"
        thong-bao-rong="Chưa có điều kiện môn học trực tiếp đã gán."
      />
      <XemBangThongTin
        tieu-de="Khối 8: Điều kiện môn học (qua syllabus)"
        :cot="cotSyllabusDieuKien"
        :dong="syllabusDieuKienMap"
        thong-bao-rong="Chưa có điều kiện môn học gán qua syllabus."
      />
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { xemChuongTrinhService, layTenHienThi } from '../services/xemChuongTrinhService'
import XemTomTatHuongDi from '../components/XemTomTatHuongDi.vue'
import XemBangThongTin from '../components/XemBangThongTin.vue'
import XemTrangDangTai from '../components/XemTrangDangTai.vue'

const route = useRoute()
const router = useRouter()

const khungKyId = computed(() => route.params.khungKyId)
const nhomKienThucId = computed(() => route.params.nhomKienThucId)
const chuongTrinhMonId = computed(() => route.params.chuongTrinhMonId)
const syllabusMonId = computed(() => route.params.syllabusMonId)

const loading = ref(false)
const loi = ref('')
const chiTiet = ref(null)
const tenKy = ref('')
const tenNhom = ref('')
const tenMonTrongCt = ref('')
const tenMonHoc = ref('')
const tenSyllabus = ref('')

const summary = computed(() => [
  { nhan: 'Kỳ', giaTri: tenKy.value || khungKyId.value },
  { nhan: 'Nhóm kiến thức', giaTri: tenNhom.value || nhomKienThucId.value },
  { nhan: 'Môn trong chương trình', giaTri: tenMonTrongCt.value || chuongTrinhMonId.value },
  { nhan: 'Môn học', giaTri: tenMonHoc.value || '-' },
  { nhan: 'Syllabus môn', giaTri: tenSyllabus.value || syllabusMonId.value }
])

const monHoc = computed(() => chiTiet.value?.monInfo?.monHoc || {})
const chuongTrinhMon = computed(() => chiTiet.value?.monInfo?.chuongTrinhMon || {})

const truongMonHoc = [
  { key: 'maMon', nhan: 'Mã môn' },
  { key: 'tenMon', nhan: 'Tên môn' },
  { key: 'soTinChi', nhan: 'Số tín chỉ' },
  { key: 'soTietLyThuyet', nhan: 'Số tiết lý thuyết' },
  { key: 'soTietThucHanh', nhan: 'Số tiết thực hành' },
  { key: 'soTietTuHoc', nhan: 'Số tiết tự học' },
  { key: 'loaiMon', nhan: 'Loại môn' },
  { key: 'moTa', nhan: 'Mô tả' }
]

const truongChuongTrinhMon = [
  { key: 'maMonTrongCt', nhan: 'Mã môn trong CT' },
  { key: 'tenMon', nhan: 'Tên môn' },
  { key: 'chuongTrinhVersionId', nhan: 'Version ID' },
  { key: 'khungKyId', nhan: 'Kỳ ID' },
  { key: 'nhomKienThucId', nhan: 'Nhóm kiến thức ID' },
  { key: 'soTinChi', nhan: 'Số tín chỉ' },
  { key: 'batBuoc', nhan: 'Bắt buộc' },
  { key: 'loaiMon', nhan: 'Loại môn' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

const cotMonTienQuyet = [
  { key: 'tenMon', nhan: 'Tên môn' },
  { key: 'tenMonDieuKien', nhan: 'Môn điều kiện' },
  { key: 'loaiDieuKien', nhan: 'Loại điều kiện' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

const cotQuyDoiDiem = [
  { key: 'tenCotDiemMau', nhan: 'Tên cột điểm mẫu' },
  { key: 'loaiCotDiem', nhan: 'Loại cột điểm' },
  { key: 'tyLe', nhan: 'Tỷ lệ' },
  { key: 'diemToiDa', nhan: 'Điểm tối đa' },
  { key: 'diemDat', nhan: 'Điểm đạt' },
  { key: 'batBuoc', nhan: 'Bắt buộc' },
  { key: 'congThuc', nhan: 'Công thức' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

const cotQuyDoiDiemMauGan = [
  { key: 'maQuyDoiDiemMau', nhan: 'Mã mẫu' },
  { key: 'tenQuyDoiDiemMau', nhan: 'Tên mẫu' },
  { key: 'loaiMau', nhan: 'Loại mẫu' },
  { key: 'nguongTu', nhan: 'Ngưỡng từ' },
  { key: 'nguongDen', nhan: 'Ngưỡng đến' },
  { key: 'diemQuyDoi', nhan: 'Điểm quy đổi' },
  { key: 'ketQua', nhan: 'Kết quả' },
  { key: 'tyLe', nhan: 'Tỷ lệ' },
  { key: 'diemToiDa', nhan: 'Điểm tối đa' },
  { key: 'batBuoc', nhan: 'Bắt buộc' },
  { key: 'congThuc', nhan: 'Công thức' }
]

const cotCauHinhDanhGiaMau = [
  { key: 'tenChuongTrinhMon', nhan: 'Môn trong CT' },
  { key: 'tenCotDiem', nhan: 'Tên cột điểm' },
  { key: 'loaiCotDiem', nhan: 'Loại cột điểm' },
  { key: 'tyLe', nhan: 'Tỷ lệ' },
  { key: 'diemToiDa', nhan: 'Điểm tối đa' },
  { key: 'batBuoc', nhan: 'Bắt buộc' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

const cotChuongBai = [
  { key: 'thuTu', nhan: 'Thứ tự' },
  { key: 'tieuDe', nhan: 'Tiêu đề' },
  { key: 'tenChuong', nhan: 'Tên chương' },
  { key: 'tenBai', nhan: 'Tên bài' },
  { key: 'noiDung', nhan: 'Nội dung' },
  { key: 'soTietLyThuyet', nhan: 'Tiết LT' },
  { key: 'soTietThucHanh', nhan: 'Tiết TH' },
  { key: 'soTietTuHoc', nhan: 'Tiết tự học' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

const cotDieuKienMonHoc = [
  { key: 'loaiDieuKien', nhan: 'Loại điều kiện' },
  { key: 'noiDung', nhan: 'Nội dung' },
  { key: 'mucDoBatBuoc', nhan: 'Mức độ bắt buộc' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

const cotSyllabusDieuKien = [
  { key: 'tenDieuKienGoc', nhan: 'Tên điều kiện gốc' },
  { key: 'noiDung', nhan: 'Nội dung' },
  { key: 'thuTu', nhan: 'Thứ tự' },
  { key: 'ghiChu', nhan: 'Ghi chú' }
]

// Khối 5: map quyDoiDiemMauGan với quyDoiDiemMau để lấy mã/tên mẫu
const quyDoiDiemMauGanMap = computed(() => {
  const mau = chiTiet.value?.quyDoiDiemMau || []
  return (chiTiet.value?.quyDoiDiemMauGan || []).map(item => {
    const m = mau.find(x => String(x.id || '') === String(item.quyDoiDiemMauId || '')) || {}
    return {
      ...item,
      maQuyDoiDiemMau: item.maQuyDoiDiemMau || m.ma || m.maQuyDoiDiemMau,
      tenQuyDoiDiemMau: item.tenQuyDoiDiemMau || m.ten || m.tenQuyDoiDiemMau,
      loaiMau: item.loaiMau || m.loaiMau,
      nguongTu: item.nguongTu ?? m.nguongTu,
      nguongDen: item.nguongDen ?? m.nguongDen,
      diemQuyDoi: item.diemQuyDoi ?? m.diemQuyDoi,
      ketQua: item.ketQua ?? m.ketQua
    }
  })
})

// Khối 8: map syllabusMonHocDieuKien với dieuKienMonHocGoc
const syllabusDieuKienMap = computed(() => {
  const goc = chiTiet.value?.dieuKienMonHocGoc || []
  return (chiTiet.value?.syllabusMonHocDieuKien || []).map(item => {
    const g = goc.find(x => String(x.id || '') === String(item.dieuKienMonHocGocId || '')) || {}
    return {
      ...item,
      tenDieuKienGoc: item.tenDieuKienGoc || g.ten || g.tenDieuKien || g.noiDung,
      noiDung: item.noiDung || g.noiDung
    }
  })
})

async function taiDuLieu() {
  loading.value = true
  loi.value = ''
  try {
    const [ky, nhom, syllabus, data] = await Promise.all([
      xemChuongTrinhService.layKyById(khungKyId.value),
      xemChuongTrinhService.layNhomKienThucById(nhomKienThucId.value),
      xemChuongTrinhService.laySyllabusMonById(syllabusMonId.value),
      xemChuongTrinhService.layChiTietMonDiem(chuongTrinhMonId.value, syllabusMonId.value)
    ])
    tenKy.value = ky ? layTenHienThi(ky, ['tenKy', 'maKy', 'tenKhungKyGoc']) : ''
    tenNhom.value = nhom ? layTenHienThi(nhom, ['ten', 'tenNhomKienThuc', 'tenNhomKienThucGoc', 'ma']) : ''
    tenSyllabus.value = syllabus
      ? layTenHienThi(syllabus, ['ten', 'tenSyllabusMonHocGoc', 'maSyllabusMonHocGoc', 'mucTieu'])
      : ''
    chiTiet.value = data
    tenMonTrongCt.value = data?.monInfo?.chuongTrinhMon
      ? layTenHienThi(data.monInfo.chuongTrinhMon, ['tenMon', 'tenChuongTrinhMon', 'maMonTrongCt'])
      : ''
    tenMonHoc.value = data?.monInfo?.monHoc
      ? layTenHienThi(data.monInfo.monHoc, ['tenMon', 'maMon'])
      : ''
  } catch (error) {
    loi.value = error?.message || 'Không tải được chi tiết môn và điểm.'
  } finally {
    loading.value = false
  }
}

function moTaiLieu() {
  router.push({
    name: 'XemChuongTrinh.Tang10TaiLieuSyllabusMon',
    params: { ...route.params }
  })
}

onMounted(taiDuLieu)
</script>

<style scoped>
.tang-page {
  padding: 24px;
  min-height: calc(100vh - 80px);
  background: #f8fafc;
}
.tang-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 16px;
}
.tang-title {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}
.loi-text {
  color: #dc2626;
  font-size: 14px;
}
.btn {
  border: none;
  border-radius: 8px;
  padding: 8px 14px;
  font-size: 13px;
  cursor: pointer;
}
.btn-tai-lieu {
  background: #dcfce7;
  color: #15803d;
}
</style>
