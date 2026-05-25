<template>
  <section class="page">
    <header class="page-head">
      <div>
        <h1>Quản lý giảng dạy</h1>
        <p>Lọc lớp học phần theo ngành, chương trình, version, kỳ rồi gán giáo viên, tạo lịch học và theo dõi điểm danh.</p>
      </div>

      <button type="button" class="btn" @click="taiDuLieu">
        Tải lại
      </button>
    </header>

    <section class="filter-card">
      <label>
        <span>Ngành</span>
        <select v-model="boLoc.nganhId" @change="doiNganh">
          <option value="">Tất cả ngành</option>
          <option v-for="item in danhSachNganh" :key="item.id" :value="item.id">
            {{ hienThiNganh(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Chương trình</span>
        <select v-model="boLoc.chuongTrinhId" @change="doiChuongTrinh">
          <option value="">Tất cả chương trình</option>
          <option v-for="item in danhSachChuongTrinhTheoNganh" :key="item.id" :value="item.id">
            {{ hienThiChuongTrinh(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Version</span>
        <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
          <option value="">Tất cả version</option>
          <option v-for="item in danhSachVersionTheoChuongTrinh" :key="item.id" :value="item.id">
            {{ hienThiVersion(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Kỳ</span>
        <select v-model="boLoc.khungKyId" @change="locDuLieu">
          <option value="">Tất cả kỳ</option>
          <option v-for="item in danhSachKhungKyTheoVersion" :key="item.id" :value="item.id">
            {{ hienThiKhungKy(item) }}
          </option>
        </select>
      </label>

      <label>
        <span>Từ khóa lớp</span>
        <input
            v-model.trim="boLoc.keyword"
            type="text"
            placeholder="Mã lớp, tên lớp"
            @keyup.enter="locDuLieu"
        />
      </label>

      <label>
        <span>Loại lớp</span>
        <select v-model="boLoc.loaiLopHocPhan" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="CHUYEN_NGANH">Chuyên ngành</option>
          <option value="HOC_CHUNG">Học chung</option>
        </select>
      </label>

      <label>
        <span>Trạng thái lớp</span>
        <select v-model="boLoc.trangThai" @change="locDuLieu">
          <option value="">Tất cả</option>
          <option value="du_kien">Dự kiến</option>
          <option value="dang_mo">Đang mở</option>
          <option value="dang_hoc">Đang học</option>
          <option value="da_ket_thuc">Đã kết thúc</option>
          <option value="huy">Hủy</option>
        </select>
      </label>

      <div class="filter-actions">
        <button type="button" class="btn primary" @click="locDuLieu">
          Lọc
        </button>

        <button type="button" class="btn" @click="xoaLoc">
          Xóa lọc
        </button>
      </div>
    </section>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <section class="summary-grid">
      <article class="summary-card">
        <strong>{{ danhSachLopHocPhanHienThi.length }}</strong>
        <span>Lớp học phần</span>
      </article>

      <article class="summary-card">
        <strong>{{ tongSoPhanCong }}</strong>
        <span>Phân công</span>
      </article>

      <article class="summary-card">
        <strong>{{ tongSoLichHoc }}</strong>
        <span>Buổi học</span>
      </article>

      <article class="summary-card">
        <strong>{{ tongSoDiemDanh }}</strong>
        <span>Bản ghi điểm danh</span>
      </article>
    </section>

    <section class="table-card">
      <div class="table-head">
        <h2>Danh sách lớp học phần</h2>
        <span>{{ dangTai ? 'Đang tải...' : `${danhSachLopHocPhanHienThi.length} lớp` }}</span>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Mã lớp</th>
            <th>Tên lớp</th>
            <th>Môn học</th>
            <th>Kỳ</th>
            <th>Loại lớp</th>
            <th>Sĩ số</th>
            <th>GV chính</th>
            <th>Lịch học</th>
            <th>Điểm danh</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="!dangTai && danhSachLopHocPhanHienThi.length === 0">
            <td colspan="11" class="empty">
              Không có lớp học phần phù hợp bộ lọc.
            </td>
          </tr>

          <tr
              v-for="item in danhSachLopHocPhanHienThi"
              :key="item.id"
              :class="{ selected: lopDangChon?.id === item.id }"
          >
            <td>{{ item.maLop }}</td>
            <td>{{ item.tenLop }}</td>
            <td>{{ tenMonHocTheoLop(item) }}</td>
            <td>{{ tenKyTheoLop(item) }}</td>
            <td>{{ hienThiLoaiLop(item.loaiLopHocPhan) }}</td>
            <td>{{ item.soLuongHienTai || 0 }}/{{ item.soLuongToiDa || '-' }}</td>
            <td>{{ tenGiaoVienChinh(item) }}</td>
            <td>{{ demLichHoc(item.id) }}</td>
            <td>{{ demDiemDanhTheoLop(item.id) }}</td>
            <td>{{ hienThiTrangThaiLop(item.trangThai) }}</td>
            <td>
              <button type="button" class="btn small primary" @click="chonLop(item)">
                Quản lý
              </button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section v-if="lopDangChon" class="detail-card">
      <header class="detail-head">
        <div>
          <h2>{{ lopDangChon.maLop }} - {{ lopDangChon.tenLop }}</h2>
          <p>
            {{ tenMonHocTheoLop(lopDangChon) }}
            · {{ tenKyTheoLop(lopDangChon) }}
            · {{ hienThiLoaiLop(lopDangChon.loaiLopHocPhan) }}
            · Sĩ số {{ lopDangChon.soLuongHienTai || 0 }}/{{ lopDangChon.soLuongToiDa || '-' }}
          </p>
        </div>

        <button type="button" class="btn" @click="boChonLop">
          Đóng
        </button>
      </header>

      <nav class="detail-tabs">
        <button
            type="button"
            :class="{ active: tabDangChon === 'phanCong' }"
            @click="tabDangChon = 'phanCong'"
        >
          Phân công
        </button>

        <button
            type="button"
            :class="{ active: tabDangChon === 'lichHoc' }"
            @click="tabDangChon = 'lichHoc'"
        >
          Lịch học
        </button>

        <button
            type="button"
            :class="{ active: tabDangChon === 'diemDanh' }"
            @click="tabDangChon = 'diemDanh'"
        >
          Điểm danh
        </button>
      </nav>

      <section v-if="tabDangChon === 'phanCong'" class="tab-panel">
        <h3>Gán giáo viên cho lớp học phần</h3>

        <form class="form-grid" @submit.prevent="taoPhanCong">
          <label>
            <span>Giáo viên</span>
            <select v-model="formPhanCong.giaoVienId" required>
              <option value="">Chọn giáo viên</option>
              <option v-for="item in danhSachGiaoVien" :key="item.id" :value="item.id">
                {{ item.maGiaoVien ? item.maGiaoVien + ' - ' : '' }}{{ item.hoTen || item.tenGiaoVien || item.id }}
              </option>
            </select>
          </label>

          <label>
            <span>Vai trò</span>
            <select v-model="formPhanCong.vaiTro" required>
              <option value="giang_vien_chinh">Giảng viên chính</option>
              <option value="tro_giang">Trợ giảng</option>
              <option value="coi_thi">Coi thi</option>
              <option value="cham_thi">Chấm thi</option>
            </select>
          </label>

          <div class="form-actions">
            <button type="submit" class="btn primary">
              Gán giáo viên
            </button>
          </div>
        </form>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th>Giáo viên</th>
              <th>Vai trò</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="phanCongTheoLop.length === 0">
              <td colspan="2" class="empty">Chưa phân công giáo viên.</td>
            </tr>

            <tr v-for="item in phanCongTheoLop" :key="item.id">
              <td>{{ item.maGiaoVien ? item.maGiaoVien + ' - ' : '' }}{{ item.tenGiaoVien }}</td>
              <td>{{ hienThiVaiTro(item.vaiTro) }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section v-if="tabDangChon === 'lichHoc'" class="tab-panel">
        <h3>Tạo lịch học cho lớp học phần</h3>

        <form class="form-grid" @submit.prevent="taoLichHoc">
          <label>
            <span>Ngày học</span>
            <input v-model="formLichHoc.ngayHoc" type="date" required />
          </label>

          <label>
            <span>Ca học</span>
            <select v-model="formLichHoc.caHocId" required>
              <option value="">Chọn ca</option>
              <option v-for="item in danhSachCaHoc" :key="item.id" :value="item.id">
                {{ item.maCa ? item.maCa + ' - ' : '' }}{{ item.tenCa }}
              </option>
            </select>
          </label>

          <label>
            <span>Phòng học</span>
            <select v-model="formLichHoc.phongHocId" required>
              <option value="">Chọn phòng</option>
              <option v-for="item in danhSachPhongHoc" :key="item.id" :value="item.id">
                {{ item.maPhong ? item.maPhong + ' - ' : '' }}{{ item.tenPhong }}
              </option>
            </select>
          </label>

          <label>
            <span>Giáo viên dạy</span>
            <select v-model="formLichHoc.giaoVienId" required>
              <option value="">Chọn giáo viên</option>
              <option v-for="item in danhSachGiaoVienTheoPhanCong" :key="item.id" :value="item.id">
                {{ item.maGiaoVien ? item.maGiaoVien + ' - ' : '' }}{{ item.hoTen || item.tenGiaoVien || item.id }}
              </option>
            </select>
          </label>

          <label class="field-full">
            <span>Nội dung buổi học</span>
            <input v-model.trim="formLichHoc.noiDungBuoiHoc" type="text" placeholder="Buổi 1 - Nội dung học" />
          </label>

          <div class="form-actions">
            <button type="submit" class="btn primary">
              Thêm lịch học
            </button>
          </div>
        </form>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th>Ngày</th>
              <th>Ca</th>
              <th>Phòng</th>
              <th>Giáo viên</th>
              <th>Nội dung</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="lichHocTheoLop.length === 0">
              <td colspan="7" class="empty">Chưa có lịch học.</td>
            </tr>

            <tr v-for="item in lichHocTheoLop" :key="item.id">
              <td>{{ item.ngayHoc }}</td>
              <td>{{ item.tenCa || item.maCa || item.caHocId }}</td>
              <td>{{ item.tenPhong || item.maPhong || item.phongHocId }}</td>
              <td>{{ item.tenGiaoVien || item.maGiaoVien || item.giaoVienId }}</td>
              <td>{{ item.noiDungBuoiHoc || '-' }}</td>
              <td>{{ hienThiTrangThaiLich(item.trangThai) }}</td>
              <td>
                <button type="button" class="btn small" @click="chonLichDiemDanh(item)">
                  Điểm danh
                </button>
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>

      <section v-if="tabDangChon === 'diemDanh'" class="tab-panel">
        <h3>Điểm danh theo buổi học</h3>

        <div class="form-grid">
          <label>
            <span>Buổi học</span>
            <select v-model="lichDangChonId" @change="taiDiemDanhTheoLich">
              <option value="">Chọn buổi học</option>
              <option v-for="item in lichHocTheoLop" :key="item.id" :value="item.id">
                {{ item.ngayHoc }} - {{ item.tenCa || item.maCa || item.caHocId }} - {{ item.noiDungBuoiHoc || 'Buổi học' }}
              </option>
            </select>
          </label>
        </div>

        <form v-if="lichDangChonId" class="form-grid" @submit.prevent="taoDiemDanh">
          <label>
            <span>Sinh viên ID</span>
            <input v-model.trim="formDiemDanh.sinhVienId" type="number" required />
          </label>

          <label>
            <span>Trạng thái</span>
            <select v-model="formDiemDanh.trangThai" required>
              <option value="co_mat">Có mặt</option>
              <option value="vang_co_phep">Vắng có phép</option>
              <option value="vang_khong_phep">Vắng không phép</option>
              <option value="di_muon">Đi muộn</option>
            </select>
          </label>

          <label class="field-full">
            <span>Ghi chú</span>
            <input v-model.trim="formDiemDanh.ghiChu" type="text" />
          </label>

          <div class="form-actions">
            <button type="submit" class="btn primary">
              Lưu điểm danh
            </button>
          </div>
        </form>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th>Mã SV</th>
              <th>Họ tên</th>
              <th>Trạng thái</th>
              <th>Ghi chú</th>
              <th>Thời gian</th>
            </tr>
            </thead>

            <tbody>
            <tr v-if="diemDanhTheoLich.length === 0">
              <td colspan="5" class="empty">
                Chưa có dữ liệu điểm danh cho buổi học đang chọn.
              </td>
            </tr>

            <tr v-for="item in diemDanhTheoLich" :key="item.id">
              <td>{{ item.maSinhVien || item.sinhVienId }}</td>
              <td>{{ item.tenSinhVien || '-' }}</td>
              <td>{{ hienThiTrangThaiDiemDanh(item.trangThai) }}</td>
              <td>{{ item.ghiChu || '-' }}</td>
              <td>{{ item.thoiGianDiemDanh || '-' }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </section>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const dangTai = ref(false)
const loi = ref('')
const thongBao = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachGiaoVien = ref([])
const danhSachPhongHoc = ref([])
const danhSachCaHoc = ref([])
const danhSachPhanCong = ref([])
const danhSachLichHoc = ref([])
const danhSachDiemDanh = ref([])

const lopDangChon = ref(null)
const tabDangChon = ref('phanCong')
const lichDangChonId = ref('')

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  khungKyId: '',
  keyword: '',
  loaiLopHocPhan: '',
  trangThai: ''
})

const formPhanCong = reactive({
  giaoVienId: '',
  vaiTro: 'giang_vien_chinh'
})

const formLichHoc = reactive({
  ngayHoc: '',
  caHocId: '',
  phongHocId: '',
  giaoVienId: '',
  noiDungBuoiHoc: ''
})

const formDiemDanh = reactive({
  sinhVienId: '',
  trangThai: 'co_mat',
  ghiChu: ''
})

const danhSachChuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return danhSachChuongTrinh.value
  return danhSachChuongTrinh.value.filter((item) => String(item.nganhId || '') === String(boLoc.nganhId))
})

const danhSachVersionTheoChuongTrinh = computed(() => {
  const chuongTrinhIds = danhSachChuongTrinhTheoNganh.value.map((item) => Number(item.id))

  return danhSachVersion.value.filter((item) => {
    if (boLoc.chuongTrinhId) {
      return String(item.chuongTrinhId || '') === String(boLoc.chuongTrinhId)
    }

    if (boLoc.nganhId) {
      return chuongTrinhIds.includes(Number(item.chuongTrinhId))
    }

    return true
  })
})

const danhSachKhungKyTheoVersion = computed(() => {
  const khungKyIds = new Set(
      danhSachChuongTrinhMon.value
          .filter((item) => {
            if (!boLoc.chuongTrinhVersionId) return true
            return String(item.chuongTrinhVersionId || '') === String(boLoc.chuongTrinhVersionId)
          })
          .map((item) => Number(item.khungKyId))
          .filter(Boolean)
  )

  if (khungKyIds.size === 0) return danhSachKhungKy.value

  return danhSachKhungKy.value.filter((item) => khungKyIds.has(Number(item.id)))
})

const danhSachLopHocPhanHienThi = computed(() => {
  return danhSachLopHocPhan.value.filter((lop) => {
    if (boLoc.nganhId && !lopThuocNganh(lop, boLoc.nganhId)) return false
    if (boLoc.chuongTrinhId && !lopThuocChuongTrinh(lop, boLoc.chuongTrinhId)) return false
    if (boLoc.chuongTrinhVersionId && !lopThuocVersion(lop, boLoc.chuongTrinhVersionId)) return false
    if (boLoc.khungKyId && !lopThuocKhungKy(lop, boLoc.khungKyId)) return false
    return true
  })
})

const phanCongTheoLop = computed(() => {
  if (!lopDangChon.value) return []
  return danhSachPhanCong.value.filter((item) => Number(item.lopHocPhanId) === Number(lopDangChon.value.id))
})

const lichHocTheoLop = computed(() => {
  if (!lopDangChon.value) return []
  return danhSachLichHoc.value.filter((item) => Number(item.lopHocPhanId) === Number(lopDangChon.value.id))
})

const diemDanhTheoLich = computed(() => {
  if (!lichDangChonId.value) return []
  return danhSachDiemDanh.value.filter((item) => Number(item.lichHocId) === Number(lichDangChonId.value))
})

const danhSachGiaoVienTheoPhanCong = computed(() => {
  const giaoVienIds = phanCongTheoLop.value.map((item) => Number(item.giaoVienId)).filter(Boolean)

  if (giaoVienIds.length === 0) return danhSachGiaoVien.value

  return danhSachGiaoVien.value.filter((item) => giaoVienIds.includes(Number(item.id)))
})

const tongSoPhanCong = computed(() => {
  const lopIds = danhSachLopHocPhanHienThi.value.map((item) => Number(item.id))
  return danhSachPhanCong.value.filter((item) => lopIds.includes(Number(item.lopHocPhanId))).length
})

const tongSoLichHoc = computed(() => {
  const lopIds = danhSachLopHocPhanHienThi.value.map((item) => Number(item.id))
  return danhSachLichHoc.value.filter((item) => lopIds.includes(Number(item.lopHocPhanId))).length
})

const tongSoDiemDanh = computed(() => {
  const lopIds = danhSachLopHocPhanHienThi.value.map((item) => Number(item.id))
  return danhSachDiemDanh.value.filter((item) => lopIds.includes(Number(item.lopHocPhanId))).length
})

onMounted(async () => {
  await taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const [
      nganh,
      chuongTrinh,
      version,
      khungKy,
      chuongTrinhMon,
      monHoc,
      lopHocPhan,
      lopHocPhanChuongTrinhMon,
      giaoVien,
      phongHoc,
      caHoc,
      phanCong,
      lichHoc,
      diemDanh
    ] = await Promise.all([
      giangDayService.layDanhSachNganh(),
      giangDayService.layDanhSachChuongTrinh(),
      giangDayService.layDanhSachChuongTrinhVersion(),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachChuongTrinhMon(),
      giangDayService.layDanhSachMonHoc(),
      giangDayService.layTrangLopHocPhan({
        keyword: boLoc.keyword || undefined,
        loaiLopHocPhan: boLoc.loaiLopHocPhan || undefined,
        trangThai: boLoc.trangThai || undefined,
        size: 500
      }),
      giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 500 }),
      giangDayService.layTrangGiaoVien({ size: 500 }),
      giangDayService.layTrangPhongHoc({ size: 500 }),
      giangDayService.layTrangCaHoc({ size: 500 }),
      giangDayService.layTrangPhanCongGiangDay({ size: 500 }),
      giangDayService.layTrangLichHoc({ size: 500 }),
      giangDayService.layTrangDiemDanh({ size: 500 })
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
    danhSachKhungKy.value = khungKy
    danhSachChuongTrinhMon.value = chuongTrinhMon
    danhSachMonHoc.value = monHoc
    danhSachLopHocPhan.value = lopHocPhan.content
    danhSachLopHocPhanChuongTrinhMon.value = lopHocPhanChuongTrinhMon.content
    danhSachGiaoVien.value = giaoVien.content
    danhSachPhongHoc.value = phongHoc.content
    danhSachCaHoc.value = caHoc.content
    danhSachPhanCong.value = phanCong.content
    danhSachLichHoc.value = lichHoc.content
    danhSachDiemDanh.value = diemDanh.content
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu quản lý giảng dạy'
  } finally {
    dangTai.value = false
  }
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.khungKyId = ''
  locDuLieu()
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
  boLoc.khungKyId = ''
  locDuLieu()
}

function doiVersion() {
  boLoc.khungKyId = ''
  locDuLieu()
}

function locDuLieu() {
  taiDuLieu()
}

function xoaLoc() {
  boLoc.nganhId = ''
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.khungKyId = ''
  boLoc.keyword = ''
  boLoc.loaiLopHocPhan = ''
  boLoc.trangThai = ''
  taiDuLieu()
}

function chonLop(item) {
  lopDangChon.value = item
  tabDangChon.value = 'phanCong'
  lichDangChonId.value = ''
  resetFormPhanCong()
  resetFormLichHoc()
  resetFormDiemDanh()
}

function boChonLop() {
  lopDangChon.value = null
  lichDangChonId.value = ''
}

async function taoPhanCong() {
  if (!lopDangChon.value) return

  try {
    await giangDayService.taoPhanCongGiangDay({
      lopHocPhanId: Number(lopDangChon.value.id),
      giaoVienId: Number(formPhanCong.giaoVienId),
      vaiTro: formPhanCong.vaiTro
    })

    thongBao.value = 'Gán giáo viên thành công'
    resetFormPhanCong()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Gán giáo viên thất bại'
  }
}

async function taoLichHoc() {
  if (!lopDangChon.value) return

  try {
    await giangDayService.taoLichHoc({
      lopHocPhanId: Number(lopDangChon.value.id),
      giaoVienId: Number(formLichHoc.giaoVienId),
      phongHocId: Number(formLichHoc.phongHocId),
      caHocId: Number(formLichHoc.caHocId),
      ngayHoc: formLichHoc.ngayHoc,
      noiDungBuoiHoc: formLichHoc.noiDungBuoiHoc || null,
      trangThai: 'du_kien',
      ghiChu: null
    })

    thongBao.value = 'Thêm lịch học thành công'
    resetFormLichHoc()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Thêm lịch học thất bại'
  }
}

function chonLichDiemDanh(item) {
  tabDangChon.value = 'diemDanh'
  lichDangChonId.value = item.id
  taiDiemDanhTheoLich()
}

function taiDiemDanhTheoLich() {
  resetFormDiemDanh()
}

async function taoDiemDanh() {
  if (!lichDangChonId.value) return

  try {
    await giangDayService.taoDiemDanh({
      lichHocId: Number(lichDangChonId.value),
      sinhVienId: Number(formDiemDanh.sinhVienId),
      trangThai: formDiemDanh.trangThai,
      ghiChu: formDiemDanh.ghiChu || null
    })

    thongBao.value = 'Lưu điểm danh thành công'
    resetFormDiemDanh()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu điểm danh thất bại'
  }
}

function resetFormPhanCong() {
  formPhanCong.giaoVienId = ''
  formPhanCong.vaiTro = 'giang_vien_chinh'
}

function resetFormLichHoc() {
  formLichHoc.ngayHoc = ''
  formLichHoc.caHocId = ''
  formLichHoc.phongHocId = ''
  formLichHoc.giaoVienId = ''
  formLichHoc.noiDungBuoiHoc = ''
}

function resetFormDiemDanh() {
  formDiemDanh.sinhVienId = ''
  formDiemDanh.trangThai = 'co_mat'
  formDiemDanh.ghiChu = ''
}

function layChuongTrinhMonIdsTheoLop(lop) {
  const ids = []

  if (lop.chuongTrinhMonId) {
    ids.push(Number(lop.chuongTrinhMonId))
  }

  danhSachLopHocPhanChuongTrinhMon.value
      .filter((item) => Number(item.lopHocPhanId) === Number(lop.id))
      .forEach((item) => {
        if (item.chuongTrinhMonId) ids.push(Number(item.chuongTrinhMonId))
      })

  return [...new Set(ids)]
}

function layChuongTrinhMonTheoLop(lop) {
  const ids = layChuongTrinhMonIdsTheoLop(lop)
  return danhSachChuongTrinhMon.value.filter((item) => ids.includes(Number(item.id)))
}

function lopThuocNganh(lop, nganhId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    const version = timVersion(ctm.chuongTrinhVersionId)
    const chuongTrinh = timChuongTrinh(version?.chuongTrinhId)
    return String(chuongTrinh?.nganhId || '') === String(nganhId)
  })
}

function lopThuocChuongTrinh(lop, chuongTrinhId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    const version = timVersion(ctm.chuongTrinhVersionId)
    return String(version?.chuongTrinhId || '') === String(chuongTrinhId)
  })
}

function lopThuocVersion(lop, versionId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    return String(ctm.chuongTrinhVersionId || '') === String(versionId)
  })
}

function lopThuocKhungKy(lop, khungKyId) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    return String(ctm.khungKyId || '') === String(khungKyId)
  })
}

function tenMonHocTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const monHocId = lop.monHocId || ctm?.monHocId
  const monHoc = danhSachMonHoc.value.find((item) => Number(item.id) === Number(monHocId))
  return monHoc?.tenMonHoc || monHoc?.tenMon || monHoc?.name || monHocId || '-'
}

function tenKyTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const khungKy = danhSachKhungKy.value.find((item) => Number(item.id) === Number(ctm?.khungKyId))
  return khungKy?.tenKy || khungKy?.maKy || '-'
}

function tenGiaoVienChinh(lop) {
  const item = danhSachPhanCong.value.find((pc) => {
    return Number(pc.lopHocPhanId) === Number(lop.id) && pc.vaiTro === 'giang_vien_chinh'
  })

  return item?.tenGiaoVien || 'Chưa gán'
}

function demLichHoc(lopHocPhanId) {
  return danhSachLichHoc.value.filter((item) => Number(item.lopHocPhanId) === Number(lopHocPhanId)).length
}

function demDiemDanhTheoLop(lopHocPhanId) {
  return danhSachDiemDanh.value.filter((item) => Number(item.lopHocPhanId) === Number(lopHocPhanId)).length
}

function timVersion(id) {
  return danhSachVersion.value.find((item) => Number(item.id) === Number(id))
}

function timChuongTrinh(id) {
  return danhSachChuongTrinh.value.find((item) => Number(item.id) === Number(id))
}

function hienThiNganh(item) {
  return `${item.maNganh ? item.maNganh + ' - ' : ''}${item.tenNganh || item.ten || item.name || item.id}`
}

function hienThiChuongTrinh(item) {
  return `${item.maChuongTrinh ? item.maChuongTrinh + ' - ' : ''}${item.tenChuongTrinh || item.ten || item.id}`
}

function hienThiVersion(item) {
  return `${item.maVersion ? item.maVersion + ' - ' : ''}${item.tenVersion || item.id}`
}

function hienThiKhungKy(item) {
  return `${item.maKy ? item.maKy + ' - ' : ''}${item.tenKy || item.id}`
}

function hienThiLoaiLop(value) {
  const map = {
    CHUYEN_NGANH: 'Chuyên ngành',
    HOC_CHUNG: 'Học chung'
  }

  return map[value] || value || '-'
}

function hienThiVaiTro(value) {
  const map = {
    giang_vien_chinh: 'Giảng viên chính',
    tro_giang: 'Trợ giảng',
    coi_thi: 'Coi thi',
    cham_thi: 'Chấm thi'
  }

  return map[value] || value || '-'
}

function hienThiTrangThaiLop(value) {
  const map = {
    du_kien: 'Dự kiến',
    dang_mo: 'Đang mở',
    dang_hoc: 'Đang học',
    da_ket_thuc: 'Đã kết thúc',
    huy: 'Hủy'
  }

  return map[value] || value || '-'
}

function hienThiTrangThaiLich(value) {
  const map = {
    du_kien: 'Dự kiến',
    da_day: 'Đã dạy',
    nghi: 'Nghỉ',
    day_bu: 'Dạy bù',
    doi_lich: 'Đổi lịch'
  }

  return map[value] || value || '-'
}

function hienThiTrangThaiDiemDanh(value) {
  const map = {
    co_mat: 'Có mặt',
    vang_co_phep: 'Vắng có phép',
    vang_khong_phep: 'Vắng không phép',
    di_muon: 'Đi muộn'
  }

  return map[value] || value || '-'
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.page-head,
.detail-head,
.table-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.page-head h1,
.detail-head h2,
.table-head h2 {
  margin: 0;
}

.page-head p,
.detail-head p {
  margin: 6px 0 0;
  color: var(--color-muted);
}

.filter-card,
.form-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(180px, 1fr));
  gap: 12px;
}

.filter-card,
.table-card,
.detail-card,
.summary-card {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  padding: 16px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-weight: 700;
}

input,
select {
  min-height: 38px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  padding: 8px 10px;
  background: var(--color-white);
}

.field-full {
  grid-column: 1 / -1;
}

.filter-actions,
.form-actions {
  display: flex;
  align-items: flex-end;
  gap: 8px;
}

.btn {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  padding: 9px 13px;
  cursor: pointer;
  font-weight: 700;
}

.btn.primary {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: var(--color-white);
}

.btn.small {
  padding: 6px 10px;
  font-size: 13px;
}

.summary-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.summary-card {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.summary-card strong {
  font-size: 24px;
}

.summary-card span {
  color: var(--color-muted);
  font-weight: 700;
}

.table-wrap {
  overflow-x: auto;
  margin-top: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
  min-width: 1000px;
}

th,
td {
  border-bottom: 1px solid var(--color-border);
  padding: 10px;
  text-align: left;
  vertical-align: top;
}

th {
  background: var(--color-background);
  font-weight: 800;
}

tr.selected {
  background: rgba(37, 99, 235, 0.08);
}

.empty {
  text-align: center;
  color: var(--color-muted);
}

.alert {
  padding: 10px 12px;
  border-radius: var(--radius);
  font-weight: 700;
}

.alert.success {
  background: #ecfdf3;
  color: #027a48;
}

.alert.error {
  background: #fef3f2;
  color: #b42318;
}

.detail-card {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.detail-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  border-bottom: 1px solid var(--color-border);
  padding-bottom: 10px;
}

.detail-tabs button {
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background: var(--color-white);
  padding: 9px 13px;
  cursor: pointer;
  font-weight: 800;
}

.detail-tabs button.active {
  background: var(--color-primary);
  border-color: var(--color-primary);
  color: var(--color-white);
}

.tab-panel {
  display: flex;
  flex-direction: column;
  gap: 14px;
}

.tab-panel h3 {
  margin: 0;
}

@media (max-width: 1100px) {
  .filter-card,
  .form-grid,
  .summary-grid {
    grid-template-columns: repeat(2, minmax(180px, 1fr));
  }
}

@media (max-width: 700px) {
  .filter-card,
  .form-grid,
  .summary-grid {
    grid-template-columns: 1fr;
  }

  .page-head,
  .detail-head,
  .table-head {
    flex-direction: column;
  }
}
</style>