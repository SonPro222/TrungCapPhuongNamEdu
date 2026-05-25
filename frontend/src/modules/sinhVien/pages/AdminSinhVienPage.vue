<template>
  <section class="sv-page">
    <div class="sv-header">
      <div>
        <p class="sv-eyebrow">Module sinh viên</p>
        <h1>Quản lý sinh viên theo ngành và version chương trình</h1>
        <p>Tiếp nhận sinh viên theo version hiện hành do Đào tạo tạo sẵn, tự sinh mã sinh viên, tự cấp tài khoản Gmail và mật khẩu tạm.</p>
      </div>
    </div>

    <div v-if="thongBao" class="sv-message" :class="thongBaoLoai">
      {{ thongBao }}
    </div>

    <div v-if="taiKhoanMoi" class="sv-account-box">
      <strong>Tài khoản vừa cấp:</strong>
      <span>Mã sinh viên: {{ taiKhoanMoi.maSinhVien }}</span>
      <span>Gmail: {{ taiKhoanMoi.emailTaiKhoan }}</span>
      <span>Mật khẩu tạm: {{ taiKhoanMoi.matKhauTam }}</span>
    </div>

    <div class="sv-filter-card">
      <label>
        Ngành
        <select v-model="boLoc.nganhId" @change="doiNganh">
          <option value="">Chọn ngành</option>
          <option v-for="nganh in danhSachNganh" :key="nganh.id" :value="nganh.id">
            {{ nganh.maNganh }} - {{ nganh.tenNganh }}
          </option>
        </select>
      </label>

      <label>
        Chương trình
        <select v-model="boLoc.chuongTrinhId" @change="doiChuongTrinh">
          <option value="">Chọn chương trình</option>
          <option v-for="ct in chuongTrinhTheoNganh" :key="ct.id" :value="ct.id">
            {{ ct.maChuongTrinh }} - {{ ct.tenChuongTrinh }}
          </option>
        </select>
      </label>

      <label>
        Version chương trình hiện hành
        <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
          <option value="">Chọn version</option>
          <option v-for="item in versionTheoChuongTrinh" :key="item.id" :value="item.id">
            {{ item.maVersion }} - {{ item.tenVersion || 'Version' }}
          </option>
        </select>
      </label>

      <label>
        Lớp hành chính
        <select v-model="boLoc.lopHanhChinhId" @change="taiDanhSachSinhVien">
          <option value="">Chọn lớp hành chính</option>
          <option v-for="lop in lopHanhChinhTheoVersion" :key="lop.id" :value="lop.id">
            {{ lop.maLop }} - {{ lop.tenLop }}
          </option>
        </select>
      </label>

      <button type="button" @click="taiDanhSachSinhVien">Tải danh sách</button>
    </div>

    <div class="sv-tabs">
      <button type="button" :class="{ active: tab === 'tiepNhan' }" @click="moTabTiepNhan">
        Tiếp nhận hồ sơ sinh viên
      </button>

      <button type="button" :class="{ active: tab === 'danhSach' }" @click="tab = 'danhSach'">
        Danh sách sinh viên  theo lớp hành chính
      </button>
    </div>

    <form v-if="tab === 'tiepNhan'" class="sv-card" @submit.prevent="tiepNhanSinhVien">
      <div class="sv-card-title">
        <h2>{{ idSinhVienDangSua ? 'Cập nhật hồ sơ sinh viên' : 'Tiếp nhận hồ sơ sinh viên' }}</h2>
        <p>Không nhập mã sinh viên và không nhập mật khẩu. Hệ thống tự sinh theo cấu hình mã sinh viên gốc của ngành + version.</p>
      </div>

      <div class="sv-grid three">
        <label>Họ tên sinh viên<input v-model.trim="form.hoTen" required /></label>
        <label>Gmail sinh viên<input v-model.trim="form.email" type="email" required /></label>
        <label>Số điện thoại<input v-model.trim="form.soDienThoai" required /></label>
        <label>Ngày sinh<input v-model="form.ngaySinh" type="date" required /></label>
        <label>Giới tính
          <select v-model="form.gioiTinh" required>
            <option value="">Chọn giới tính</option>
            <option value="nam">Nam</option>
            <option value="nu">Nữ</option>
            <option value="khac">Khác</option>
          </select>
        </label>
        <label>Ngày nhập học<input v-model="form.ngayNhapHoc" type="date" /></label>
      </div>

      <div class="sv-grid two">
        <label>Địa chỉ liên hệ<textarea v-model.trim="form.diaChi" rows="2" required></textarea></label>
        <label>Địa chỉ thường trú<textarea v-model.trim="form.diaChiThuongTru" rows="2" required></textarea></label>
      </div>

      <h3>CCCD và bằng cấp</h3>
      <div class="sv-grid three">
        <label>Số CCCD<input v-model.trim="form.soCccd" required /></label>
        <label>Ngày cấp CCCD<input v-model="form.ngayCapCccd" type="date" required /></label>
        <label>Nơi cấp CCCD<input v-model.trim="form.noiCapCccd" required /></label>
        <label>Bằng cấp<input v-model.trim="form.bangCap" required /></label>
        <label>Năm tốt nghiệp<input v-model.number="form.namTotNghiep" type="number" min="1900" required /></label>
        <label>Trường tốt nghiệp<input v-model.trim="form.truongTotNghiep" required /></label>
      </div>

      <h3>Thông tin cha mẹ và người thân</h3>
      <div class="sv-grid three">
        <label>Họ tên cha<input v-model.trim="form.hoTenCha" required /></label>
        <label>Nghề nghiệp cha<input v-model.trim="form.ngheNghiepCha" /></label>
        <label>SĐT cha<input v-model.trim="form.sdtCha" required /></label>
        <label>Gmail cha<input v-model.trim="form.emailCha" type="email" /></label>
        <label>Họ tên mẹ<input v-model.trim="form.hoTenMe" required /></label>
        <label>Nghề nghiệp mẹ<input v-model.trim="form.ngheNghiepMe" /></label>
        <label>SĐT mẹ<input v-model.trim="form.sdtMe" required /></label>
        <label>Gmail mẹ<input v-model.trim="form.emailMe" type="email" /></label>
        <label>Họ tên người thân<input v-model.trim="form.hoTenNguoiThan" required /></label>
        <label>Quan hệ người thân<input v-model.trim="form.quanHeNguoiThan" required /></label>
        <label>SĐT người thân<input v-model.trim="form.sdtNguoiThan" required /></label>
        <label>Gmail người thân<input v-model.trim="form.emailNguoiThan" type="email" required /></label>
      </div>

      <h3>Hình ảnh và giấy tờ</h3>
      <div class="sv-grid four">
        <label>Ảnh chân dung<input type="file" accept="image/*" @change="chonTep($event, 'anhChanDung')" /></label>
        <label>CCCD mặt trước<input type="file" accept="image/*,.pdf" @change="chonTep($event, 'cccdTruoc')" /></label>
        <label>CCCD mặt sau<input type="file" accept="image/*,.pdf" @change="chonTep($event, 'cccdSau')" /></label>
        <label>Bằng cấp<input type="file" accept="image/*,.pdf" @change="chonTep($event, 'bangCap')" /></label>
      </div>
      <label>Giấy tờ khác<input type="file" multiple @change="chonTepKhac" /></label>

      <label>Ghi chú hồ sơ<textarea v-model.trim="form.ghiChuHoSo" rows="3"></textarea></label>

      <div class="sv-actions">
        <button type="submit" :disabled="dangLuu">
          {{ dangLuu ? 'Đang lưu...' : (idSinhVienDangSua ? 'Cập nhật sinh viên' : 'Tiếp nhận sinh viên') }}
        </button>
        <button type="button" class="secondary" @click="resetForm">Làm mới form</button>
      </div>
    </form>

    <div v-if="tab === 'danhSach'" class="sv-card">
      <div class="sv-card-title">
        <h2>Danh sách sinh viên theo lớp hành chính</h2>
        <p>Sinh viên phải thuộc lớp hành chính trước. Sau đó mới phân bổ lớp học phần theo môn.</p>
      </div>
      <div class="sv-class-summary">
        <div>
          <span>Lớp hành chính</span>
          <strong>{{ lopHanhChinhDangChon?.maLop || 'Chưa chọn' }} - {{ lopHanhChinhDangChon?.tenLop || '' }}</strong>
        </div>
        <div>
          <span>Sĩ số hiện tại</span>
          <strong>{{ siSoLopHanhChinhHienTai }} / {{ siSoLopHanhChinhToiDa || 'Chưa cấu hình' }}</strong>
        </div>
        <div>
          <span>Trạng thái sĩ số</span>
          <strong>{{ trangThaiSiSoLopHanhChinh }}</strong>
        </div>
        <div>
          <span>Số môn cần phân bổ</span>
          <strong>{{ soMonCanPhanBo }}</strong>
        </div>
      </div>
      <div class="sv-list-filter">
        <label>
          Tìm sinh viên
          <input
              v-model.trim="boLoc.tuKhoaSinhVien"
              placeholder="Nhập mã sinh viên, họ tên, Gmail hoặc SĐT"
              @keyup.enter="taiDanhSachSinhVien"
          />
        </label>

        <button type="button" @click="taiDanhSachSinhVien">
         Tải lại
        </button>
      </div>
      <div class="sv-table-wrap">
        <table class="sv-table">
          <thead>
          <tr>
            <th>Ảnh</th>
            <th>Mã SV</th>
            <th>Họ tên</th>
            <th>Gmail</th>
            <th>SĐT</th>
            <th>CCCD</th>
            <th>Người thân</th>
            <th>Trạng thái hồ sơ</th>
            <th>Phân bổ lớp học phần</th>
            <th>Số lớp đã phân bổ</th>
            <th>Thao tác</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="sv in danhSachSinhVienHienThi" :key="sv.id">
            <td>
              <img v-if="sv.anhChanDungTepId" class="sv-avatar" :src="urlTep(sv.anhChanDungTepId)" alt="Ảnh sinh viên" />
              <span v-else>Chưa có</span>
            </td>
            <td>{{ sv.maSinhVien }}</td>
            <td>{{ sv.hoTen }}</td>
            <td>{{ sv.email }}</td>
            <td>{{ sv.soDienThoai }}</td>
            <td>{{ sv.soCccd }}</td>
            <td>{{ sv.hoTenNguoiThan }} {{ sv.sdtNguoiThan ? '- ' + sv.sdtNguoiThan : '' }}</td>
            <td>{{ sv.trangThai }}</td>
            <td>
      <span class="sv-status" :class="classTrangThaiPhanBo(sv)">
        {{ trangThaiPhanBo(sv) }}
      </span>
            </td>
            <td>
              {{ soLopDaPhanBo(sv) }} / {{ soMonCanPhanBo }}
            </td>
            <td>
              <div class="sv-row-actions">
                <button type="button" class="secondary small" @click="chonSuaSinhVien(sv)">
                  Sửa
                </button>

                <button type="button" class="small" @click="moMienTruBaoLuu(sv)">
                  Miễn trừ môn
                </button>

                <RouterLink
                    class="sv-action-link"
                    :to="{ name: 'AdminSinhVienBaoLuu', query: { sinhVienId: sv.id } }"
                >
                  Bảo lưu
                </RouterLink>

                <button type="button" class="danger small" @click="xoaSinhVien(sv)">
                  Xóa
                </button>
              </div>
            </td>
          </tr>
          <tr v-if="!danhSachSinhVienHienThi.length">
            <td colspan="11">Chưa có sinh viên theo lớp hành chính đã chọn.</td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>



    <div v-if="sinhVienDangChon && hienFormMienTru" class="sv-modal-overlay" @click.self="dongMienTruBaoLuu">
      <div class="sv-modal">
        <div class="sv-modal-header">
          <div>
            <h2>Miễn trừ / bảo lưu cho sinh viên</h2>
            <p>
              Sinh viên:
              <strong>{{ sinhVienDangChon.maSinhVien }} - {{ sinhVienDangChon.hoTen }}</strong>
            </p>
          </div>

          <button type="button" class="sv-modal-close" @click="dongMienTruBaoLuu">
            ×
          </button>
        </div>

        <form class="sv-grid three" @submit.prevent="taoMienTruBaoLuu">
          <label>Loại
            <select v-model="formMienTru.loai" required>
              <option value="mien_hoc">Miễn học</option>
              <option value="mien_thi">Miễn thi</option>
              <option value="bao_luu">Bảo lưu</option>
              <option value="cong_nhan">Công nhận</option>
            </select>
          </label>

          <label>Trạng thái phê duyệt
            <select v-model="formMienTru.trangThai">
              <option value="cho_duyet">Chờ duyệt</option>
              <option value="da_duyet">Đã duyệt</option>
              <option value="tu_choi">Từ chối</option>
            </select>
          </label>

          <label>Môn trong chương trình
            <select v-model="formMienTru.chuongTrinhMonId" required>
              <option value="">Chọn môn trong chương trình</option>
              <option v-for="mon in chuongTrinhMonTheoVersion" :key="mon.id" :value="mon.id">
                {{ mon.maMonTrongCt || mon.id }} - {{ mon.ghiChu || 'Môn trong chương trình' }}
              </option>
            </select>
          </label>

          <label class="span-3">
            Lý do
            <textarea v-model.trim="formMienTru.lyDo" rows="3" required></textarea>
          </label>

          <label class="span-3">
            Ghi chú
            <textarea v-model.trim="formMienTru.ghiChu" rows="2"></textarea>
          </label>

          <div class="span-3 sv-modal-actions">
            <button type="submit">Tạo yêu cầu</button>
            <button type="button" class="secondary" @click="dongMienTruBaoLuu">
              Đóng
            </button>
          </div>
        </form>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { sinhVienService } from '../services/sinhVienService'

const tab = ref('tiepNhan')
const dangLuu = ref(false)
const thongBao = ref('')
const thongBaoLoai = ref('success')
const taiKhoanMoi = ref(null)
const sinhVienDangChon = ref(null)
const idSinhVienDangSua = ref(null)
const hienFormMienTru = ref(false)

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachLopHanhChinh = ref([])
const danhSachSinhVienChuongTrinh = ref([])
const danhSachSinhVien = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachSinhVienLopHocPhan = ref([])
const danhSachChuongTrinhMon = ref([])

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  lopHanhChinhId: '',
  tuKhoaSinhVien: ''
})

const form = reactive(taoFormMacDinh())

const files = reactive({
  anhChanDung: null,
  cccdTruoc: null,
  cccdSau: null,
  bangCap: null,
  giayToKhac: []
})

const formMienTru = reactive({
  loai: 'mien_hoc',
  trangThai: 'cho_duyet',
  chuongTrinhMonId: '',
  lyDo: '',
  ghiChu: ''
})

const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return []
  return danhSachChuongTrinh.value.filter(item => String(item.nganhId) === String(boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!boLoc.chuongTrinhId) return []
  return danhSachVersion.value.filter(item => {
    return String(item.chuongTrinhId) === String(boLoc.chuongTrinhId) && item.laHienHanh === true
  })
})

const lopHanhChinhTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) return []
  return danhSachLopHanhChinh.value.filter(item => {
    return String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId)
  })
})

const chuongTrinhMonTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) return []
  return danhSachChuongTrinhMon.value.filter(item => {
    return String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId)
  })
})

const lopHocPhanTheoVersion = computed(() => {
  const chuongTrinhMonIds = new Set(
      chuongTrinhMonTheoVersion.value.map(item => String(item.id))
  )

  return danhSachLopHocPhan.value.filter(lop => {
    if (laLopHocChung(lop)) {
      return danhSachLopHocPhanChuongTrinhMon.value.some(item => {
        return String(item.lopHocPhanId) === String(lop.id)
            && chuongTrinhMonIds.has(String(item.chuongTrinhMonId))
      })
    }

    return chuongTrinhMonIds.has(String(lop.chuongTrinhMonId))
  })
})

const soMonCanPhanBo = computed(() => {
  return chuongTrinhMonTheoVersion.value.length
})

const lopHocPhanIdsTheoVersion = computed(() => {
  return new Set(lopHocPhanTheoVersion.value.map(item => String(item.id)))
})

const lopHanhChinhDangChon = computed(() => {
  if (!boLoc.lopHanhChinhId) return null
  return danhSachLopHanhChinh.value.find(item => String(item.id) === String(boLoc.lopHanhChinhId)) || null
})

const siSoLopHanhChinhHienTai = computed(() => {
  return danhSachSinhVien.value.length
})

const siSoLopHanhChinhToiDa = computed(() => {
  return Number(lopHanhChinhDangChon.value?.siSo || 0)
})

const trangThaiSiSoLopHanhChinh = computed(() => {
  if (!lopHanhChinhDangChon.value) return 'Chưa chọn lớp hành chính'
  if (!siSoLopHanhChinhToiDa.value) return 'Chưa cấu hình sĩ số'
  if (siSoLopHanhChinhHienTai.value >= siSoLopHanhChinhToiDa.value) return 'Đủ sĩ số'
  return 'Chưa đủ sĩ số'
})

const danhSachSinhVienHienThi = computed(() => {
  const tuKhoa = boLoc.tuKhoaSinhVien.trim().toLowerCase()

  if (!tuKhoa) return danhSachSinhVien.value

  return danhSachSinhVien.value.filter(sv => {
    return String(sv.maSinhVien || '').toLowerCase().includes(tuKhoa)
        || String(sv.hoTen || '').toLowerCase().includes(tuKhoa)
        || String(sv.email || '').toLowerCase().includes(tuKhoa)
        || String(sv.soDienThoai || '').toLowerCase().includes(tuKhoa)
  })
})

onMounted(async () => {
  await taiDuLieuNen()
})
async function taiDuLieuNen() {
  try {
    const [
      nganh,
      chuongTrinh,
      version,
      lopHanhChinh,
      chuongTrinhMon
    ] = await Promise.all([
      sinhVienService.layNganh(),
      sinhVienService.layChuongTrinh(),
      sinhVienService.layVersion(),
      sinhVienService.layLopHanhChinh(),
      sinhVienService.layChuongTrinhMon()
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachVersion.value = version
    danhSachLopHanhChinh.value = lopHanhChinh
    danhSachChuongTrinhMon.value = chuongTrinhMon

    danhSachLopHocPhan.value = []
    danhSachLopHocPhanChuongTrinhMon.value = []
    danhSachSinhVienLopHocPhan.value = []
    danhSachSinhVienChuongTrinh.value = []
  } catch (error) {
    baoLoi(error.message || 'Không tải được dữ liệu nền')
  }
}

function laLopHocChung(lop) {
  return String(lop?.loaiLopHocPhan || '').toUpperCase() === 'HOC_CHUNG'
}

function hienThiLoaiLopHocPhan(lop) {
  return laLopHocChung(lop) ? 'Học chung' : 'Chuyên ngành'
}

function timChuongTrinhMonTheoId(id) {
  return danhSachChuongTrinhMon.value.find(item => String(item.id) === String(id))
}

function layMonDaiDienCuaLop(lop) {
  if (!laLopHocChung(lop)) {
    return timChuongTrinhMonTheoId(lop.chuongTrinhMonId)
  }

  const chuongTrinhMonIdsTrongVersion = new Set(
      chuongTrinhMonTheoVersion.value.map(item => String(item.id))
  )

  const dongNoiTheoVersionDangChon = danhSachLopHocPhanChuongTrinhMon.value.find(item => {
    return String(item.lopHocPhanId) === String(lop.id)
        && chuongTrinhMonIdsTrongVersion.has(String(item.chuongTrinhMonId))
  })

  return dongNoiTheoVersionDangChon
      ? timChuongTrinhMonTheoId(dongNoiTheoVersionDangChon.chuongTrinhMonId)
      : null
}

function demSoMonChuongTrinhGhepLop(lop) {
  if (!laLopHocChung(lop)) {
    return lop.chuongTrinhMonId ? 1 : 0
  }

  return danhSachLopHocPhanChuongTrinhMon.value.filter(item => {
    return String(item.lopHocPhanId) === String(lop.id)
  }).length
}

function hienThiLopHocPhan(lop) {
  const mon = layMonDaiDienCuaLop(lop)
  const maLop = lop.maLopHocPhan || lop.maLop || lop.id
  const tenLop = lop.tenLopHocPhan || lop.tenLop || lop.tenMonHoc || 'Lớp học phần'
  const loai = hienThiLoaiLopHocPhan(lop)
  const soMonGhep = demSoMonChuongTrinhGhepLop(lop)
  const maMon = mon?.maMonTrongCt ? ` - ${mon.maMonTrongCt}` : ''

  return `[${loai}] ${maLop} - ${tenLop}${maMon} - ${soMonGhep} môn chương trình`
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  boLoc.chuongTrinhVersionId = ''
  boLoc.lopHanhChinhId = ''
  danhSachSinhVien.value = []
  sinhVienDangChon.value = null
  hienFormMienTru.value = false
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
  boLoc.lopHanhChinhId = ''
  danhSachSinhVien.value = []
  sinhVienDangChon.value = null
  hienFormMienTru.value = false
}

function doiVersion() {
  boLoc.lopHanhChinhId = ''
  danhSachSinhVien.value = []
  sinhVienDangChon.value = null
  hienFormMienTru.value = false
}

async function taiDanhSachSinhVien() {
  if (!boLoc.chuongTrinhVersionId) {
    baoLoi('Phải chọn version chương trình trước khi tải danh sách sinh viên')
    return
  }

  if (!boLoc.lopHanhChinhId) {
    baoLoi('Phải chọn lớp hành chính trước khi tải danh sách sinh viên')
    return
  }

  try {
    const [sinhVien, sinhVienChuongTrinh] = await Promise.all([
      sinhVienService.laySinhVienTheoNganhChuongTrinhVersion(
          boLoc.nganhId || null,
          boLoc.chuongTrinhId || null,
          boLoc.chuongTrinhVersionId || null,
          0,
          100
      ),
      sinhVienService.laySinhVienChuongTrinh({ page: 0, size: 100 })
    ])

    danhSachSinhVienChuongTrinh.value = sinhVienChuongTrinh

    const sinhVienIdsThuocLop = new Set(
        sinhVienChuongTrinh
            .filter(item => String(item.lopHanhChinhId) === String(boLoc.lopHanhChinhId))
            .filter(item => String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId))
            .map(item => String(item.sinhVienId))
    )

    danhSachSinhVien.value = sinhVien.filter(item => {
      return sinhVienIdsThuocLop.has(String(item.id))
    })

    sinhVienDangChon.value = null
    hienFormMienTru.value = false
  } catch (error) {
    baoLoi(error.message || 'Không tải được danh sách sinh viên theo lớp hành chính')
  }
}

async function tiepNhanSinhVien() {
  if (!boLoc.nganhId || !boLoc.chuongTrinhId || !boLoc.chuongTrinhVersionId || !boLoc.lopHanhChinhId) {
    baoLoi('Phải chọn ngành, chương trình, version và lớp hành chính trước khi lưu hồ sơ sinh viên')
    return
  }

  dangLuu.value = true
  taiKhoanMoi.value = null

  try {
    const payload = lamSachPayload({
      ...form,
      nganhId: Number(boLoc.nganhId),
      chuongTrinhId: Number(boLoc.chuongTrinhId),
      chuongTrinhVersionId: Number(boLoc.chuongTrinhVersionId),
      lopHanhChinhId: Number(boLoc.lopHanhChinhId)
    })

    if (idSinhVienDangSua.value) {
      await sinhVienService.capNhatSinhVien(idSinhVienDangSua.value, payload)

      baoThanhCong('Đã cập nhật hồ sơ sinh viên')
      resetForm()
      await taiDanhSachSinhVien()
      tab.value = 'danhSach'
      return
    }

    const response = await sinhVienService.tiepNhanSinhVienCoFile(payload, files)
    const sinhVien = response.sinhVien

    taiKhoanMoi.value = {
      maSinhVien: sinhVien.maSinhVien,
      emailTaiKhoan: response.emailTaiKhoan,
      matKhauTam: response.matKhauTam
    }

    baoThanhCong('Đã tiếp nhận sinh viên, tự sinh mã, cấp tài khoản và lưu hồ sơ thành công')
    resetForm()
    await taiDanhSachSinhVien()
    tab.value = 'danhSach'
  } catch (error) {
    baoLoi(error.message || 'Lưu hồ sơ sinh viên thất bại')
  } finally {
    dangLuu.value = false
  }
}

function chonSinhVien(sv) {
  sinhVienDangChon.value = sv
  baoThanhCong(`Đã chọn sinh viên ${sv.maSinhVien} - ${sv.hoTen}. Sinh viên đang nằm trong lớp hành chính đã chọn.`)
}

function chonSuaSinhVien(sv) {
  idSinhVienDangSua.value = sv.id
  sinhVienDangChon.value = sv
  hienFormMienTru.value = false

  Object.assign(form, {
    ...taoFormMacDinh(),
    ...sv
  })

  tab.value = 'tiepNhan'
  baoThanhCong(`Đang sửa hồ sơ sinh viên ${sv.maSinhVien} - ${sv.hoTen}`)
}

function moMienTruBaoLuu(sv) {
  sinhVienDangChon.value = sv
  hienFormMienTru.value = true
  tab.value = 'danhSach'

  formMienTru.loai = 'mien_hoc'
  formMienTru.trangThai = 'cho_duyet'
  formMienTru.chuongTrinhMonId = ''
  formMienTru.lyDo = ''
  formMienTru.ghiChu = ''
}

function dongMienTruBaoLuu() {
  hienFormMienTru.value = false
  sinhVienDangChon.value = null
}

function moTabTiepNhan() {
  tab.value = 'tiepNhan'
  hienFormMienTru.value = false
}

function soLopDaPhanBo(sv) {
  return danhSachSinhVienLopHocPhan.value.filter(item => {
    return String(item.sinhVienId) === String(sv.id)
        && lopHocPhanIdsTheoVersion.value.has(String(item.lopHocPhanId))
        && item.trangThai !== 'huy'
  }).length
}

function trangThaiPhanBo(sv) {
  const daPhanBo = soLopDaPhanBo(sv)

  if (!soMonCanPhanBo.value) return 'Chưa có môn để phân bổ'
  if (daPhanBo === 0) return 'Chưa phân bổ'
  if (daPhanBo >= soMonCanPhanBo.value) return 'Đã phân bổ đủ'
  return 'Đã phân bổ một phần'
}

function classTrangThaiPhanBo(sv) {
  const trangThai = trangThaiPhanBo(sv)

  if (trangThai === 'Đã phân bổ đủ') return 'done'
  if (trangThai === 'Đã phân bổ một phần') return 'partial'
  return 'pending'
}

async function xoaSinhVien(sv) {
  const ok = window.confirm(`Xóa sinh viên ${sv.maSinhVien} - ${sv.hoTen}?`)
  if (!ok) return

  try {
    await sinhVienService.xoaSinhVien(sv.id)
    baoThanhCong('Đã xóa sinh viên')
    await taiDanhSachSinhVien()
  } catch (error) {
    baoLoi(error.message || 'Xóa sinh viên thất bại')
  }
}

async function taoMienTruBaoLuu() {
  if (!sinhVienDangChon.value) {
    baoLoi('Phải chọn sinh viên trước khi tạo miễn trừ / bảo lưu')
    return
  }

  try {
    await sinhVienService.taoMienTruBaoLuu(lamSachPayload({
      sinhVienId: sinhVienDangChon.value.id,
      loai: formMienTru.loai,
      lyDo: formMienTru.lyDo,
      chuongTrinhMonId: Number(formMienTru.chuongTrinhMonId),
      trangThai: formMienTru.trangThai,
      ghiChu: formMienTru.ghiChu
    }))

    baoThanhCong('Đã tạo yêu cầu miễn trừ / bảo lưu')
    formMienTru.lyDo = ''
    formMienTru.ghiChu = ''
    dongMienTruBaoLuu()
  } catch (error) {
    baoLoi(error.message || 'Tạo miễn trừ / bảo lưu thất bại')
  }
}

function chonTep(event, key) {
  files[key] = event.target.files?.[0] || null
}

function chonTepKhac(event) {
  files.giayToKhac = Array.from(event.target.files || [])
}

function urlTep(id) {
  return sinhVienService.layUrlTaiTep(id)
}

function resetForm() {
  idSinhVienDangSua.value = null
  Object.assign(form, taoFormMacDinh())
  files.anhChanDung = null
  files.cccdTruoc = null
  files.cccdSau = null
  files.bangCap = null
  files.giayToKhac = []
}

function taoFormMacDinh() {
  return {
    hoTen: '',
    email: '',
    soDienThoai: '',
    ngaySinh: '',
    gioiTinh: '',
    ngayNhapHoc: '',
    diaChi: '',
    diaChiThuongTru: '',
    soCccd: '',
    ngayCapCccd: '',
    noiCapCccd: '',
    bangCap: '',
    namTotNghiep: null,
    truongTotNghiep: '',
    hoTenCha: '',
    ngheNghiepCha: '',
    sdtCha: '',
    emailCha: '',
    hoTenMe: '',
    ngheNghiepMe: '',
    sdtMe: '',
    emailMe: '',
    hoTenNguoiThan: '',
    quanHeNguoiThan: '',
    sdtNguoiThan: '',
    emailNguoiThan: '',
    ghiChuHoSo: '',
    ghiChuChuongTrinh: ''
  }
}

function lamSachPayload(payload) {
  const ketQua = {}

  Object.entries(payload).forEach(([key, value]) => {
    if (value === '' || value === undefined || value === null) return
    if (['id', 'createdAt', 'updatedAt'].includes(key)) return
    ketQua[key] = value
  })

  return ketQua
}

function baoThanhCong(message) {
  thongBao.value = message
  thongBaoLoai.value = 'success'
}

function baoLoi(message) {
  thongBao.value = message
  thongBaoLoai.value = 'error'
}
</script>

<style scoped>
.sv-page {
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.sv-header,
.sv-card,
.sv-filter-card,
.sv-account-box {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 16px;
  padding: 18px;
}

.sv-header h1,
.sv-card h2 {
  margin: 0 0 8px;
}

.sv-header p,
.sv-card p,
.sv-flow-item span {
  margin: 0;
  color: #6b7280;
}

.sv-eyebrow {
  color: #2563eb !important;
  font-weight: 700;
  text-transform: uppercase;
  letter-spacing: .08em;
  font-size: 12px;
}

.sv-flow {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 12px;
}

.sv-flow-item {
  border: 1px solid #d1d5db;
  border-radius: 14px;
  padding: 14px;
  background: #fafafa;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.sv-flow-item.active {
  border-color: #2563eb;
  background: #eff6ff;
}

.sv-message {
  border-radius: 12px;
  padding: 12px 14px;
  font-weight: 600;
}

.sv-message.success {
  background: #ecfdf5;
  color: #047857;
}

.sv-message.error {
  background: #fef2f2;
  color: #b91c1c;
}

.sv-account-box {
  display: flex;
  gap: 16px;
  flex-wrap: wrap;
  background: #fffbeb;
  border-color: #f59e0b;
}
.sv-class-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  margin-bottom: 16px;
}

.sv-class-summary div {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  padding: 12px;
  background: #f9fafb;
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.sv-class-summary span {
  color: #6b7280;
  font-size: 13px;
}

.sv-status {
  display: inline-flex;
  border-radius: 999px;
  padding: 5px 10px;
  font-size: 13px;
  font-weight: 700;
}

.sv-status.done {
  background: #ecfdf5;
  color: #047857;
}

.sv-status.partial {
  background: #fffbeb;
  color: #b45309;
}

.sv-status.pending {
  background: #fef2f2;
  color: #b91c1c;
}

button.selected {
  background: #047857;
}
.sv-filter-card,
.sv-grid {
  display: grid;
  gap: 12px;
}

.sv-filter-card {
  grid-template-columns: 1fr 1.3fr 1.3fr 1.3fr auto;
  align-items: end;
}

.sv-grid.two { grid-template-columns: repeat(2, minmax(0, 1fr)); }
.sv-grid.three { grid-template-columns: repeat(3, minmax(0, 1fr)); }
.sv-grid.four { grid-template-columns: repeat(4, minmax(0, 1fr)); }
.span-3 { grid-column: span 3; }

label {
  display: flex;
  flex-direction: column;
  gap: 6px;
  font-weight: 600;
  color: #374151;
}

input,
select,
textarea {
  border: 1px solid #d1d5db;
  border-radius: 10px;
  padding: 10px 12px;
  font: inherit;
}

button {
  border: 0;
  border-radius: 10px;
  padding: 10px 14px;
  background: #2563eb;
  color: #fff;
  font-weight: 700;
  cursor: pointer;
}

button:disabled {
  opacity: .65;
  cursor: not-allowed;
}

button.secondary {
  background: #6b7280;
}

button.danger {
  background: #dc2626;
}

button.small {
  padding: 7px 10px;
  font-size: 13px;
}

.sv-tabs {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}

.sv-tabs button {
  background: #e5e7eb;
  color: #111827;
}

.sv-tabs button.active {
  background: #2563eb;
  color: #fff;
}

.sv-card-title {
  margin-bottom: 16px;
}

.sv-row-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
}

.sv-row-actions button:nth-child(2) {
  margin-left: 6px;
  margin-right: 6px;
}
.sv-table-wrap {
  overflow-x: auto;
}

.sv-table {
  width: 100%;
  border-collapse: collapse;
}

.sv-table th,
.sv-table td {
  border-bottom: 1px solid #e5e7eb;
  padding: 10px;
  text-align: left;
  vertical-align: top;
}

.sv-table th {
  background: #f9fafb;
}

.sv-table tr.selected {
  background: #eff6ff;
}

.sv-selected {
  padding: 12px;
  border-radius: 12px;
  background: #f3f4f6;
  margin-bottom: 14px;
}

.sv-avatar {
  width: 48px;
  height: 48px;
  object-fit: cover;
  border-radius: 10px;
  border: 1px solid #e5e7eb;
}

@media (max-width: 900px) {
  .sv-flow,
  .sv-filter-card,
  .sv-class-summary,
  .sv-grid.two,
  .sv-grid.three,
  .sv-grid.four {
    grid-template-columns: 1fr;
  }

  .span-3 {
    grid-column: auto;
  }
}
.sv-modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  background: rgba(15, 23, 42, 0.55);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 24px;
}

.sv-modal {
  width: min(920px, 100%);
  max-height: calc(100vh - 48px);
  overflow: auto;
  background: #ffffff;
  border-radius: 18px;
  padding: 20px;
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.35);
}

.sv-modal-header {
  display: flex;
  justify-content: space-between;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 18px;
}

.sv-modal-header h2 {
  margin: 0 0 8px;
}

.sv-modal-header p {
  margin: 0;
  color: #6b7280;
}

.sv-modal-close {
  width: 38px;
  height: 38px;
  border-radius: 999px;
  padding: 0;
  background: #e5e7eb;
  color: #111827;
  font-size: 24px;
  line-height: 1;
}

.sv-modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 14px;
}
.sv-action-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  padding: 7px 10px;
  font-size: 13px;
  font-weight: 700;
  background: #f59e0b;
  color: #ffffff;
  text-decoration: none;
}
</style>
