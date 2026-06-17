<template>
  <section class="sv-bl-page">
    <div class="page-title">
      <div>
        <p class="page-eyebrow">Quản lý học vụ</p>
        <h2>Bảo lưu sinh viên</h2>
        <p>
          Quản lý bảo lưu toàn chương trình, đi học lại, chuyển version học lại
          và lớp hành chính học lại.
        </p>
      </div>
    </div>

    <div v-if="thongBao" class="message" :class="thongBaoLoai">
      {{ thongBao }}
    </div>

    <section class="panel create-panel">
      <div class="panel-title">
        <div>
          <h2>Tạo hồ sơ bảo lưu</h2>
          <p>Tìm sinh viên, chọn hồ sơ chương trình và khai báo thời gian bảo lưu.</p>
        </div>
        <span>Bảo lưu tối đa 2 kỳ</span>
      </div>

      <form class="create-form" @submit.prevent="taoBaoLuu">
        <div class="quick-row">
          <label class="student-search-field">
            Nhập mã sinh viên hoặc Gmail xin bảo lưu
            <div class="search-inline">
              <input
                  v-model.trim="tuKhoaTaoBaoLuu"
                  placeholder="Nhập mã sinh viên hoặc Gmail"
                  @keyup.enter="timSinhVienTaoBaoLuu"
              />
              <button type="button" class="btn-search" @click="timSinhVienTaoBaoLuu">
                Tìm sinh viên
              </button>
            </div>
          </label>

          <label>
            Hồ sơ chương trình
            <select v-model="form.sinhVienChuongTrinhId" required>
              <option value="">Chọn hồ sơ chương trình</option>
              <option
                  v-for="item in sinhVienChuongTrinhTheoSinhVien"
                  :key="item.id"
                  :value="item.id"
              >
                Version {{ item.chuongTrinhVersionId }} - Lớp HC
                {{ item.lopHanhChinhId || 'chưa có' }} - {{ item.trangThai }}
              </option>
            </select>
          </label>

          <label>
            Ngày bắt đầu
            <input v-model="form.ngayBatDau" type="date" required />
          </label>

          <label>
            Số kỳ
            <select v-model.number="form.soKyBaoLuu" required>
              <option :value="1">1 kỳ</option>
              <option :value="2">2 kỳ</option>
            </select>
          </label>

          <label>
            Ngày kết thúc dự kiến
            <input v-model="form.ngayKetThucDuKien" type="date" />
          </label>
        </div>

        <div v-if="sinhVienTimThay" class="sinh-vien-da-chon">
          <div>
            <span>Sinh viên đã chọn</span>
            <strong>{{ sinhVienTimThay.maSinhVien }} - {{ sinhVienTimThay.hoTen }}</strong>
          </div>
          <div>
            <span>Gmail</span>
            <strong>{{ sinhVienTimThay.email || '-' }}</strong>
          </div>
        </div>

        <div class="note-row">
          <label>
            Lý do bảo lưu
            <textarea
                v-model.trim="form.lyDo"
                rows="2"
                placeholder="Nhập lý do bảo lưu"
                required
            ></textarea>
          </label>

          <label>
            Ghi chú
            <textarea
                v-model.trim="form.ghiChu"
                rows="2"
                placeholder="Ghi chú thêm nếu có"
            ></textarea>
          </label>
        </div>

        <div class="actions">
          <button type="submit" :disabled="dangLuu">
            {{ dangLuu ? 'Đang lưu...' : 'Tạo bảo lưu' }}
          </button>
          <button type="button" class="secondary" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <section class="panel list-panel">
      <div class="panel-title">
        <div>
          <h2>Danh sách sinh viên bảo lưu</h2>
          <p>Lọc nhanh theo ngành, chương trình, version, lớp hành chính và trạng thái.</p>
        </div>
        <span>{{ danhSachBaoLuuHienThi.length }} hồ sơ</span>
      </div>

      <div class="filter-grid bao-luu-filter">
        <label>
          Ngành
          <select v-model="boLoc.nganhId" @change="doiNganh">
            <option value="">Tất cả ngành</option>
            <option v-for="nganh in danhSachNganh" :key="nganh.id" :value="nganh.id">
              {{ nganh.maNganh }} - {{ nganh.tenNganh }}
            </option>
          </select>
        </label>

        <label>
          Chương trình
          <select v-model="boLoc.chuongTrinhId" @change="doiChuongTrinh">
            <option value="">Tất cả chương trình</option>
            <option v-for="ct in chuongTrinhTheoNganh" :key="ct.id" :value="ct.id">
              {{ ct.maChuongTrinh }} - {{ ct.tenChuongTrinh }}
            </option>
          </select>
        </label>

        <label>
          Version
          <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
            <option value="">Tất cả version</option>
            <option v-for="version in versionTheoChuongTrinh" :key="version.id" :value="version.id">
              {{ version.maVersion }} - {{ version.tenVersion || 'Version' }}
            </option>
          </select>
        </label>

        <label>
          Lớp hành chính
          <select v-model="boLoc.lopHanhChinhId">
            <option value="">Tất cả lớp hành chính</option>
            <option v-for="lop in lopHanhChinhTheoVersion" :key="lop.id" :value="lop.id">
              {{ lop.maLop }} - {{ lop.tenLop }}
            </option>
          </select>
        </label>

        <label>
          Tìm sinh viên
          <input v-model.trim="tuKhoa" placeholder="Mã SV, họ tên, Gmail" />
        </label>

        <label>
          Trạng thái
          <select v-model="trangThaiLoc" @change="taiDanhSachBaoLuu">
            <option value="">Tất cả</option>
            <option value="dang_bao_luu">Đang bảo lưu</option>
            <option value="da_di_hoc_lai">Đã đi học lại</option>
            <option value="da_huy">Đã hủy</option>
          </select>
        </label>

        <button type="button" class="btn-tai-lai" @click="taiDanhSachBaoLuu">
          Tải lại
        </button>
      </div>

      <div class="table-wrap">
        <table>
          <thead>
          <tr>
            <th>Mã SV</th>
            <th>Họ tên</th>
            <th>Version cũ</th>
            <th>Lớp HC cũ</th>
            <th>Thời gian bảo lưu</th>
            <th>Học lại</th>
            <th>Lý do</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
          </thead>

          <tbody>
          <tr v-for="item in danhSachBaoLuuHienThi" :key="item.id">
            <td class="student-code">{{ item.maSinhVien }}</td>
            <td class="student-name">{{ item.hoTen }}</td>
            <td>{{ item.chuongTrinhVersionIdCu }}</td>
            <td>{{ item.lopHanhChinhIdCu || '-' }}</td>
            <td>
              <div class="time-cell">
                <span>{{ item.ngayBatDau }} → {{ item.ngayKetThucDuKien }}</span>
                <small>{{ item.soKyBaoLuu }} kỳ</small>
              </div>
            </td>
            <td>
              <template v-if="item.trangThai === 'da_di_hoc_lai'">
                <div class="hoc-lai-info">
                  <span>Ngày: {{ item.ngayDiHocLai || '-' }}</span>
                  <span>Version: {{ item.chuongTrinhVersionIdHocLai || '-' }}</span>
                  <span>Kỳ: {{ item.khungKyIdHocLai || '-' }}</span>
                  <span>Lớp HC: {{ item.lopHanhChinhIdHocLai || '-' }}</span>
                  <span>Công nhận: {{ item.soMonDuocCongNhan || 0 }} môn</span>
                </div>
              </template>
              <template v-else>
                -
              </template>
            </td>
            <td class="reason-cell">{{ item.lyDo }}</td>
            <td>
                <span class="status" :class="item.trangThai">
                  {{ hienThiTrangThai(item.trangThai) }}
                </span>
            </td>
            <td>
              <div class="row-actions">
                <button
                    v-if="item.trangThai === 'dang_bao_luu'"
                    type="button"
                    class="small"
                    @click="moDiHocLai(item)"
                >
                  Đi học lại
                </button>

                <button
                    v-if="item.trangThai === 'dang_bao_luu'"
                    type="button"
                    class="danger small"
                    @click="huyBaoLuu(item)"
                >
                  Hủy
                </button>
              </div>
            </td>
          </tr>

          <tr v-if="!danhSachBaoLuuHienThi.length">
            <td colspan="9" class="empty">Chưa có hồ sơ bảo lưu.</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <div v-if="baoLuuDangDiHocLai" class="modal-overlay" @click.self="dongDiHocLai">
      <div class="modal">
        <div class="modal-header">
          <div>
            <h2>Cho sinh viên đi học lại</h2>
            <p>
              {{ baoLuuDangDiHocLai.maSinhVien }} - {{ baoLuuDangDiHocLai.hoTen }}
            </p>
          </div>
          <button type="button" class="modal-close" @click="dongDiHocLai">×</button>
        </div>

        <form class="form-grid" @submit.prevent="diHocLai">
          <label>
            Ngày đi học lại
            <input v-model="formDiHocLai.ngayDiHocLai" type="date" required />
          </label>

          <label>
            Version học lại
            <select
                v-model="formDiHocLai.chuongTrinhVersionIdHocLai"
                required
                @change="doiVersionHocLai"
            >
              <option value="">Chọn version</option>
              <option v-for="version in danhSachVersion" :key="version.id" :value="version.id">
                {{ version.maVersion }} - {{ version.tenVersion || 'Version' }}
              </option>
            </select>
          </label>

          <label>
            Kỳ học lại
            <select v-model="formDiHocLai.khungKyIdHocLai" required>
              <option value="">Chọn kỳ</option>
              <option v-for="ky in khungKyTheoVersionHocLai" :key="ky.id" :value="ky.id">
                {{ ky.maKy }} - {{ ky.tenKy }}
              </option>
            </select>
          </label>

          <label>
            Lớp hành chính học lại
            <select v-model="formDiHocLai.lopHanhChinhIdHocLai" required>
              <option value="">Chọn lớp hành chính</option>
              <option v-for="lop in lopHanhChinhTheoVersionHocLai" :key="lop.id" :value="lop.id">
                {{ lop.maLop }} - {{ lop.tenLop }}
              </option>
            </select>
          </label>

          <label class="span-2">
            Ghi chú
            <textarea v-model.trim="formDiHocLai.ghiChu" rows="2"></textarea>
          </label>

          <div class="span-2 actions">
            <button type="submit" :disabled="dangLuu">
              {{ dangLuu ? 'Đang xử lý...' : 'Xác nhận đi học lại' }}
            </button>
            <button type="button" class="secondary" @click="dongDiHocLai">
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
import { useRoute } from 'vue-router'
import { sinhVienService } from '../services/sinhVienService'

const route = useRoute()

const dangLuu = ref(false)
const thongBao = ref('')
const thongBaoLoai = ref('success')
const tuKhoa = ref('')
const tuKhoaTaoBaoLuu = ref('')
const trangThaiLoc = ref('')

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachSinhVien = ref([])
const danhSachSinhVienChuongTrinh = ref([])
const danhSachBaoLuu = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachLopHanhChinh = ref([])

const sinhVienTimThay = ref(null)
const baoLuuDangDiHocLai = ref(null)

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  lopHanhChinhId: ''
})

const form = reactive({
  sinhVienId: '',
  sinhVienChuongTrinhId: '',
  ngayBatDau: homNay(),
  ngayKetThucDuKien: '',
  soKyBaoLuu: 1,
  lyDo: '',
  ghiChu: ''
})

const formDiHocLai = reactive({
  ngayDiHocLai: homNay(),
  chuongTrinhVersionIdHocLai: '',
  khungKyIdHocLai: '',
  lopHanhChinhIdHocLai: '',
  ghiChu: ''
})

const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return danhSachChuongTrinh.value
  return danhSachChuongTrinh.value.filter(item => String(item.nganhId) === String(boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!boLoc.chuongTrinhId) return danhSachVersion.value
  return danhSachVersion.value.filter(item => String(item.chuongTrinhId) === String(boLoc.chuongTrinhId))
})

const lopHanhChinhTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) return danhSachLopHanhChinh.value
  return danhSachLopHanhChinh.value.filter(item => String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId))
})

const sinhVienChuongTrinhTheoSinhVien = computed(() => {
  if (!form.sinhVienId) return []

  return danhSachSinhVienChuongTrinh.value.filter(item => {
    return String(item.sinhVienId) === String(form.sinhVienId)
  })
})

const danhSachBaoLuuHienThi = computed(() => {
  const key = tuKhoa.value.trim().toLowerCase()

  return danhSachBaoLuu.value.filter(item => {
    const sinhVienChuongTrinh = timSinhVienChuongTrinhTheoBaoLuu(item)

    if (boLoc.nganhId && !thuocNganh(sinhVienChuongTrinh, boLoc.nganhId)) return false
    if (boLoc.chuongTrinhId && !thuocChuongTrinh(sinhVienChuongTrinh, boLoc.chuongTrinhId)) return false
    if (boLoc.chuongTrinhVersionId && String(item.chuongTrinhVersionIdCu) !== String(boLoc.chuongTrinhVersionId)) return false
    if (boLoc.lopHanhChinhId && String(item.lopHanhChinhIdCu) !== String(boLoc.lopHanhChinhId)) return false

    if (!key) return true

    return String(item.maSinhVien || '').toLowerCase().includes(key)
        || String(item.hoTen || '').toLowerCase().includes(key)
        || String(timSinhVienTheoId(item.sinhVienId)?.email || '').toLowerCase().includes(key)
  })
})

const khungKyTheoVersionHocLai = computed(() => {
  if (!formDiHocLai.chuongTrinhVersionIdHocLai) return []

  return danhSachKhungKy.value
      .filter(item => String(item.chuongTrinhVersionId) === String(formDiHocLai.chuongTrinhVersionIdHocLai))
      .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
})

const lopHanhChinhTheoVersionHocLai = computed(() => {
  if (!formDiHocLai.chuongTrinhVersionIdHocLai) return []

  return danhSachLopHanhChinh.value.filter(item => {
    return String(item.chuongTrinhVersionId) === String(formDiHocLai.chuongTrinhVersionIdHocLai)
  })
})

onMounted(async () => {
  await taiDuLieuNen()

  const sinhVienIdTuQuery = route.query.sinhVienId
  if (sinhVienIdTuQuery) {
    chonSinhVienTheoId(sinhVienIdTuQuery)
  }
})

async function taiDuLieuNen() {
  try {
    const [
      nganh,
      chuongTrinh,
      sinhVien,
      sinhVienChuongTrinh,
      version,
      khungKy,
      lopHanhChinh
    ] = await Promise.all([
      sinhVienService.layNganh(),
      sinhVienService.layChuongTrinh(),
      sinhVienService.laySinhVienTheoNganhChuongTrinhVersion(null, null, null),
      sinhVienService.laySinhVienChuongTrinh(),
      sinhVienService.layVersion(),
      sinhVienService.layKhungKy(),
      sinhVienService.layLopHanhChinh()
    ])

    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = chuongTrinh
    danhSachSinhVien.value = sinhVien
    danhSachSinhVienChuongTrinh.value = sinhVienChuongTrinh
    danhSachVersion.value = version
    danhSachKhungKy.value = khungKy
    danhSachLopHanhChinh.value = lopHanhChinh

    await taiDanhSachBaoLuu()
  } catch (error) {
    baoLoi(error?.message || 'Không tải được dữ liệu bảo lưu')
  }
}

async function taiDanhSachBaoLuu() {
  try {
    const params = {}

    if (trangThaiLoc.value) {
      params.trangThai = trangThaiLoc.value
    }

    danhSachBaoLuu.value = await sinhVienService.layBaoLuu(params)
  } catch (error) {
    baoLoi(error?.message || 'Không tải được danh sách bảo lưu')
  }
}

function timSinhVienTaoBaoLuu() {
  const key = tuKhoaTaoBaoLuu.value.trim().toLowerCase()

  if (!key) {
    baoLoi('Phải nhập mã sinh viên hoặc Gmail')
    return
  }

  const sinhVien = danhSachSinhVien.value.find(item => {
    return String(item.maSinhVien || '').toLowerCase() === key
        || String(item.email || '').toLowerCase() === key
  })

  if (!sinhVien) {
    sinhVienTimThay.value = null
    form.sinhVienId = ''
    form.sinhVienChuongTrinhId = ''
    baoLoi('Không tìm thấy sinh viên theo mã hoặc Gmail đã nhập')
    return
  }

  chonSinhVien(sinhVien)
}

function chonSinhVienTheoId(id) {
  const sinhVien = danhSachSinhVien.value.find(item => String(item.id) === String(id))

  if (!sinhVien) {
    baoLoi('Không tìm thấy sinh viên cần bảo lưu')
    return
  }

  tuKhoaTaoBaoLuu.value = sinhVien.maSinhVien || sinhVien.email || ''
  chonSinhVien(sinhVien)
}

function chonSinhVien(sinhVien) {
  sinhVienTimThay.value = sinhVien
  form.sinhVienId = sinhVien.id
  doiSinhVien()
  baoThanhCong(`Đã chọn sinh viên ${sinhVien.maSinhVien} - ${sinhVien.hoTen}`)
}

function doiSinhVien() {
  const ds = sinhVienChuongTrinhTheoSinhVien.value
  const dangHoc = ds.find(item => String(item.trangThai) === 'dang_hoc')
  const daDangKy = ds.find(item => String(item.trangThai) === 'da_dang_ky')
  const dauTien = ds[0]

  form.sinhVienChuongTrinhId = dangHoc?.id || daDangKy?.id || dauTien?.id || ''
}

function doiNganh() {
  boLoc.chuongTrinhId = ''
  doiChuongTrinh()
}

function doiChuongTrinh() {
  boLoc.chuongTrinhVersionId = ''
  doiVersion()
}

function doiVersion() {
  boLoc.lopHanhChinhId = ''
}

async function taoBaoLuu() {
  if (!form.sinhVienId || !form.sinhVienChuongTrinhId) {
    baoLoi('Phải tìm sinh viên và chọn hồ sơ chương trình')
    return
  }

  if (!form.lyDo) {
    baoLoi('Phải nhập lý do bảo lưu')
    return
  }

  dangLuu.value = true

  try {
    await sinhVienService.taoBaoLuu(lamSachPayload({
      sinhVienId: Number(form.sinhVienId),
      sinhVienChuongTrinhId: Number(form.sinhVienChuongTrinhId),
      ngayBatDau: form.ngayBatDau,
      ngayKetThucDuKien: form.ngayKetThucDuKien,
      soKyBaoLuu: Number(form.soKyBaoLuu),
      lyDo: form.lyDo,
      ghiChu: form.ghiChu
    }))

    baoThanhCong('Đã tạo hồ sơ bảo lưu cho sinh viên')
    resetForm()
    await taiDuLieuNen()
  } catch (error) {
    baoLoi(error?.message || 'Tạo bảo lưu thất bại')
  } finally {
    dangLuu.value = false
  }
}

function moDiHocLai(item) {
  baoLuuDangDiHocLai.value = item
  formDiHocLai.ngayDiHocLai = homNay()
  formDiHocLai.chuongTrinhVersionIdHocLai = item.chuongTrinhVersionIdCu || ''
  formDiHocLai.khungKyIdHocLai = ''
  formDiHocLai.lopHanhChinhIdHocLai = ''
  formDiHocLai.ghiChu = ''
}

function dongDiHocLai() {
  baoLuuDangDiHocLai.value = null
}

function doiVersionHocLai() {
  formDiHocLai.khungKyIdHocLai = ''
  formDiHocLai.lopHanhChinhIdHocLai = ''
}

async function diHocLai() {
  if (!baoLuuDangDiHocLai.value) return

  if (!formDiHocLai.ngayDiHocLai || !formDiHocLai.chuongTrinhVersionIdHocLai || !formDiHocLai.khungKyIdHocLai || !formDiHocLai.lopHanhChinhIdHocLai) {
    baoLoi('Phải chọn ngày học lại, version, kỳ và lớp hành chính học lại')
    return
  }

  dangLuu.value = true

  try {
    const response = await sinhVienService.choSinhVienDiHocLai(baoLuuDangDiHocLai.value.id, {
      ngayDiHocLai: formDiHocLai.ngayDiHocLai,
      chuongTrinhVersionIdHocLai: Number(formDiHocLai.chuongTrinhVersionIdHocLai),
      khungKyIdHocLai: Number(formDiHocLai.khungKyIdHocLai),
      lopHanhChinhIdHocLai: Number(formDiHocLai.lopHanhChinhIdHocLai),
      ghiChu: formDiHocLai.ghiChu
    })

    const soMon = response?.data?.soMonDuocCongNhan || response?.soMonDuocCongNhan || 0
    baoThanhCong(`Đã cho sinh viên đi học lại. Hệ thống công nhận ${soMon} môn đã hoàn thành.`)
    dongDiHocLai()
    await taiDanhSachBaoLuu()
  } catch (error) {
    baoLoi(error?.message || 'Xử lý đi học lại thất bại')
  } finally {
    dangLuu.value = false
  }
}

async function huyBaoLuu(item) {
  const ok = window.confirm(`Hủy hồ sơ bảo lưu của ${item.maSinhVien} - ${item.hoTen}?`)
  if (!ok) return

  try {
    await sinhVienService.huyBaoLuu(item.id)
    baoThanhCong('Đã hủy hồ sơ bảo lưu')
    await taiDanhSachBaoLuu()
  } catch (error) {
    baoLoi(error?.message || 'Hủy bảo lưu thất bại')
  }
}

function timSinhVienTheoId(id) {
  return danhSachSinhVien.value.find(item => String(item.id) === String(id)) || null
}

function timSinhVienChuongTrinhTheoBaoLuu(item) {
  if (!item?.sinhVienChuongTrinhId) return null

  return danhSachSinhVienChuongTrinh.value.find(row => {
    return String(row.id) === String(item.sinhVienChuongTrinhId)
  }) || null
}

function thuocChuongTrinh(sinhVienChuongTrinh, chuongTrinhId) {
  if (!sinhVienChuongTrinh) return false

  const version = danhSachVersion.value.find(item => {
    return String(item.id) === String(sinhVienChuongTrinh.chuongTrinhVersionId)
  })

  return version && String(version.chuongTrinhId) === String(chuongTrinhId)
}

function thuocNganh(sinhVienChuongTrinh, nganhId) {
  if (!sinhVienChuongTrinh) return false

  const version = danhSachVersion.value.find(item => {
    return String(item.id) === String(sinhVienChuongTrinh.chuongTrinhVersionId)
  })

  if (!version) return false

  const chuongTrinh = danhSachChuongTrinh.value.find(item => {
    return String(item.id) === String(version.chuongTrinhId)
  })

  return chuongTrinh && String(chuongTrinh.nganhId) === String(nganhId)
}

function resetForm() {
  sinhVienTimThay.value = null
  tuKhoaTaoBaoLuu.value = ''
  form.sinhVienId = ''
  form.sinhVienChuongTrinhId = ''
  form.ngayBatDau = homNay()
  form.ngayKetThucDuKien = ''
  form.soKyBaoLuu = 1
  form.lyDo = ''
  form.ghiChu = ''
}

function hienThiTrangThai(trangThai) {
  if (trangThai === 'dang_bao_luu') return 'Đang bảo lưu'
  if (trangThai === 'da_di_hoc_lai') return 'Đã đi học lại'
  if (trangThai === 'da_huy') return 'Đã hủy'
  return trangThai || '-'
}

function lamSachPayload(payload) {
  return Object.fromEntries(
      Object.entries(payload).filter(([, value]) => value !== '' && value !== null && value !== undefined)
  )
}

function homNay() {
  return new Date().toISOString().slice(0, 10)
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
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');

.sv-bl-page,
.sv-bl-page * {
  box-sizing: border-box;
  font-family: 'Roboto', Arial, sans-serif;
  letter-spacing: normal;
}

.sv-bl-page {
  --primary: #077149;
  --primary-dark: #045b3a;
  --primary-soft: #e8f6ef;
  --primary-border: #b9e4ce;
  --text-main: #0f172a;
  --text-muted: #64748b;
  --border: #dbe5ef;
  --border-strong: #cbd5e1;
  --bg-soft: #f5f8fa;
  --danger: #dc2626;

  display: grid;
  gap: 14px;
  padding: 0;
  color: var(--text-main);
}

/* Header */
.page-title,
.panel {
  background: #ffffff;
  border: 1px solid var(--border);
  border-radius: 16px;
  box-shadow: 0 10px 26px rgba(15, 23, 42, 0.055);
}

.page-title {
  position: relative;
  overflow: hidden;
  padding: 16px 18px;
  border-color: var(--primary-border);
  background:
      linear-gradient(135deg, rgba(7, 113, 73, 0.1), rgba(255, 255, 255, 0.9)),
      #ffffff;
}

.page-title::before {
  content: '';
  position: absolute;
  inset: 0 auto 0 0;
  width: 5px;
  background: var(--primary);
}

.page-eyebrow {
  margin: 0 0 4px;
  color: var(--primary);
  font-size: 12px;
  font-weight: 800;
  text-transform: uppercase;
}

.page-title h2,
.panel-title h2,
.modal-header h2 {
  margin: 0;
  color: var(--text-main);
  font-size: 20px;
  font-weight: 800;
  line-height: 1.25;
}

.page-title p,
.panel-title p,
.modal-header p {
  margin: 5px 0 0;
  color: var(--text-muted);
  font-size: 13px;
  font-weight: 400;
  line-height: 1.45;
}

/* Panels */
.panel {
  padding: 16px;
}

.panel-title {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 14px;
  margin-bottom: 14px;
}

.panel-title span {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  padding: 5px 10px;
  border: 1px solid var(--primary-border);
  border-radius: 999px;
  background: var(--primary-soft);
  color: var(--primary-dark);
  font-size: 12px;
  font-weight: 800;
  white-space: nowrap;
}

/* Form */
.create-form {
  display: grid;
  gap: 12px;
}

.quick-row {
  display: grid;
  grid-template-columns: minmax(280px, 1.45fr) minmax(240px, 1.15fr) minmax(150px, 0.7fr) minmax(120px, 0.55fr) minmax(170px, 0.75fr);
  gap: 10px;
  align-items: end;
}

.note-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 10px;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
}

.span-2 {
  grid-column: span 2;
}

label {
  display: grid;
  gap: 5px;
  color: #243449;
  font-size: 12.5px;
  font-weight: 800;
}

input,
select,
textarea {
  width: 100%;
  min-height: 36px;
  border: 1px solid var(--border-strong);
  border-radius: 10px;
  padding: 7px 10px;
  background: #ffffff;
  color: var(--text-main);
  font: inherit;
  font-size: 13.5px;
  font-weight: 400;
  outline: none;
  transition:
      border-color 0.15s ease,
      box-shadow 0.15s ease,
      background-color 0.15s ease;
}

textarea {
  min-height: 64px;
  max-height: 120px;
  resize: vertical;
  line-height: 1.45;
}

input::placeholder,
textarea::placeholder {
  color: #94a3b8;
}

input:focus,
select:focus,
textarea:focus {
  border-color: var(--primary);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, 0.13);
}

input:disabled,
select:disabled,
textarea:disabled {
  background: #f8fafc;
  color: var(--text-muted);
  cursor: not-allowed;
}

.search-inline {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 8px;
  align-items: center;
}

.student-search-field input {
  min-width: 0;
}

/* Selected student */
.sinh-vien-da-chon {
  display: grid;
  grid-template-columns: minmax(220px, 0.8fr) minmax(260px, 1fr);
  gap: 10px;
  padding: 10px 12px;
  border: 1px solid var(--primary-border);
  border-radius: 12px;
  background: linear-gradient(135deg, var(--primary-soft), #ffffff);
}

.sinh-vien-da-chon div {
  display: grid;
  gap: 2px;
}

.sinh-vien-da-chon span {
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 600;
}

.sinh-vien-da-chon strong {
  color: var(--primary-dark);
  font-size: 13.5px;
  font-weight: 800;
}

/* Buttons */
.actions,
.row-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

.actions {
  margin-top: 2px;
}

button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 36px;
  border: 0;
  border-radius: 10px;
  padding: 8px 14px;
  background: var(--primary);
  color: #ffffff;
  font-size: 13.5px;
  font-weight: 800;
  line-height: 1;
  cursor: pointer;
  transition:
      transform 0.12s ease,
      box-shadow 0.12s ease,
      background 0.12s ease,
      opacity 0.12s ease;
}

button:hover {
  background: var(--primary-dark);
  box-shadow: 0 8px 18px rgba(7, 113, 73, 0.22);
  transform: translateY(-1px);
}

button:disabled {
  opacity: 0.62;
  cursor: not-allowed;
  box-shadow: none;
  transform: none;
}

button.secondary {
  background: #e7edf3;
  color: #334155;
}

button.secondary:hover {
  background: #dbe5ef;
  box-shadow: none;
}

button.danger {
  background: var(--danger);
  color: #ffffff;
}

button.danger:hover {
  background: #b91c1c;
  box-shadow: 0 8px 18px rgba(220, 38, 38, 0.18);
}

button.small {
  min-height: 29px;
  padding: 6px 9px;
  border-radius: 8px;
  font-size: 12.5px;
}

.btn-search {
  min-width: 108px;
  white-space: nowrap;
}

.btn-tai-lai {
  width: auto;
  min-width: 86px;
  min-height: 36px;
  align-self: end;
  justify-self: start;
  padding: 8px 13px;
  border-radius: 10px;
}

/* Message */
.message {
  border-radius: 12px;
  padding: 10px 12px;
  font-size: 13px;
  font-weight: 700;
}

.message.success {
  border: 1px solid var(--primary-border);
  background: var(--primary-soft);
  color: var(--primary-dark);
}

.message.error {
  border: 1px solid #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

/* Filters */
.bao-luu-filter {
  display: grid;
  grid-template-columns:
    minmax(170px, 1fr)
    minmax(190px, 1.1fr)
    minmax(160px, 0.9fr)
    minmax(180px, 1fr)
    minmax(190px, 1fr)
    minmax(145px, 0.75fr)
    auto;
  gap: 10px;
  align-items: end;
  margin-bottom: 12px;
}

/* Table */
.table-wrap {
  width: 100%;
  overflow: auto;
  border: 1px solid var(--border);
  border-radius: 14px;
  background: #ffffff;
}

table {
  width: 100%;
  min-width: 1080px;
  border-collapse: collapse;
  font-size: 13px;
}

th,
td {
  border-bottom: 1px solid #e8eef5;
  padding: 9px 10px;
  text-align: left;
  vertical-align: top;
}

th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f3f8f6;
  color: #123225;
  font-size: 12.5px;
  font-weight: 800;
  white-space: nowrap;
}

td {
  color: #334155;
  font-size: 13px;
  font-weight: 400;
  line-height: 1.45;
}

tbody tr:hover td {
  background: #fbfdfc;
}

tbody tr:last-child td {
  border-bottom: 0;
}

.student-code {
  color: var(--primary-dark);
  font-weight: 800;
  white-space: nowrap;
}

.student-name {
  color: var(--text-main);
  font-weight: 600;
}

.time-cell,
.hoc-lai-info {
  display: grid;
  gap: 2px;
}

.time-cell small {
  color: var(--text-muted);
  font-size: 12px;
  font-weight: 600;
}

.hoc-lai-info span {
  white-space: nowrap;
}

.reason-cell {
  max-width: 220px;
  color: #475569;
}

.empty {
  padding: 22px 12px;
  text-align: center;
  color: var(--text-muted);
  font-weight: 700;
}

/* Status */
.status {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 25px;
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 12px;
  font-weight: 800;
  white-space: nowrap;
}

.status.dang_bao_luu {
  background: #fff7ed;
  color: #c2410c;
}

.status.da_di_hoc_lai {
  background: var(--primary-soft);
  color: var(--primary-dark);
}

.status.da_huy {
  background: #fef2f2;
  color: #b91c1c;
}

/* Modal */
.modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: rgba(15, 23, 42, 0.55);
}

.modal {
  width: min(900px, 100%);
  max-height: calc(100vh - 40px);
  overflow: auto;
  border-radius: 18px;
  padding: 16px;
  background: #ffffff;
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.32);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 14px;
  margin-bottom: 14px;
  padding-bottom: 12px;
  border-bottom: 1px solid var(--border);
}

.modal-close {
  width: 34px;
  height: 34px;
  min-height: 34px;
  border-radius: 999px;
  padding: 0;
  background: #e7edf3;
  color: var(--text-main);
  font-size: 22px;
  font-weight: 400;
  line-height: 1;
}

.modal-close:hover {
  background: #dbe5ef;
  box-shadow: none;
}

/* Responsive */
@media (max-width: 1500px) {
  .quick-row {
    grid-template-columns: minmax(280px, 1.4fr) minmax(240px, 1.1fr) repeat(3, minmax(140px, 0.75fr));
  }

  .bao-luu-filter {
    grid-template-columns: repeat(4, minmax(0, 1fr));
  }

  .btn-tai-lai {
    justify-self: end;
  }
}

@media (max-width: 1200px) {
  .quick-row {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .student-search-field {
    grid-column: span 2;
  }

  .note-row,
  .form-grid,
  .bao-luu-filter,
  .sinh-vien-da-chon {
    grid-template-columns: 1fr;
  }

  .span-2 {
    grid-column: span 1;
  }

  .btn-tai-lai {
    justify-self: stretch;
  }
}

@media (max-width: 700px) {
  .page-title,
  .panel {
    border-radius: 14px;
    padding: 13px;
  }

  .page-title h2,
  .panel-title h2 {
    font-size: 18px;
  }

  .panel-title {
    display: grid;
  }

  .panel-title span {
    width: fit-content;
  }

  .quick-row {
    grid-template-columns: 1fr;
  }

  .student-search-field {
    grid-column: span 1;
  }

  .search-inline {
    grid-template-columns: 1fr;
  }

  .actions,
  .row-actions {
    justify-content: stretch;
  }

  button,
  .btn-search,
  .btn-tai-lai {
    width: 100%;
  }

  textarea {
    min-height: 58px;
  }
}
</style>