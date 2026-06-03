<template>
  <section class="sv-bl-page">
    <div class="page-title">
      <div>
        <h2>Bảo lưu sinh viên</h2>
        <p>Quản lý bảo lưu toàn chương trình, đi học lại, chuyển version học lại và lớp hành chính học lại.</p>
      </div>
    </div>

    <div v-if="thongBao" class="message" :class="thongBaoLoai">
      {{ thongBao }}
    </div>

    <section class="panel">
      <div class="panel-title">
        <h2>Tạo hồ sơ bảo lưu</h2>
        <span>Bảo lưu tối đa 2 kỳ</span>
      </div>

      <form class="form-grid" @submit.prevent="taoBaoLuu">
        <div class="span-2 tim-sinh-vien-box">
          <label>
            Nhập mã sinh viên hoặc Gmail xin bảo lưu
            <input
                v-model.trim="tuKhoaTaoBaoLuu"
                placeholder="Nhập mã sinh viên hoặc Gmail"
                @keyup.enter="timSinhVienTaoBaoLuu"
            />
          </label>

          <button type="button" @click="timSinhVienTaoBaoLuu">
            Tìm sinh viên
          </button>
        </div>

        <div v-if="sinhVienTimThay" class="span-2 sinh-vien-da-chon">
          <strong>Sinh viên đã chọn:</strong>
          <span>{{ sinhVienTimThay.maSinhVien }} - {{ sinhVienTimThay.hoTen }}</span>
          <span>Gmail: {{ sinhVienTimThay.email }}</span>
        </div>

        <label>
          Hồ sơ chương trình
          <select v-model="form.sinhVienChuongTrinhId" required>
            <option value="">Chọn hồ sơ chương trình</option>
            <option v-for="item in sinhVienChuongTrinhTheoSinhVien" :key="item.id" :value="item.id">
              Version {{ item.chuongTrinhVersionId }} - Lớp HC {{ item.lopHanhChinhId || 'chưa có' }} - {{ item.trangThai }}
            </option>
          </select>
        </label>

        <label>
          Ngày bắt đầu bảo lưu
          <input v-model="form.ngayBatDau" type="date" required />
        </label>

        <label>
          Số kỳ bảo lưu
          <select v-model.number="form.soKyBaoLuu" required>
            <option :value="1">1 kỳ</option>
            <option :value="2">2 kỳ</option>
          </select>
        </label>

        <label>
          Ngày kết thúc dự kiến
          <input v-model="form.ngayKetThucDuKien" type="date" />
        </label>

        <label class="span-2">
          Lý do bảo lưu
          <textarea v-model.trim="form.lyDo" rows="3" required></textarea>
        </label>

        <label class="span-2">
          Ghi chú
          <textarea v-model.trim="form.ghiChu" rows="2"></textarea>
        </label>

        <div class="span-2 actions">
          <button type="submit" :disabled="dangLuu">
            {{ dangLuu ? 'Đang lưu...' : 'Tạo bảo lưu' }}
          </button>
          <button type="button" class="secondary" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <section class="panel">
      <div class="panel-title">
        <h2>Danh sách sinh viên bảo lưu</h2>
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
          <input v-model.trim="tuKhoa" placeholder="Mã sinh viên, họ tên, Gmail" />
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
            <td>{{ item.maSinhVien }}</td>
            <td>{{ item.hoTen }}</td>
            <td>{{ item.chuongTrinhVersionIdCu }}</td>
            <td>{{ item.lopHanhChinhIdCu || '-' }}</td>
            <td>
              {{ item.ngayBatDau }} -> {{ item.ngayKetThucDuKien }}
              <br />
              <small>{{ item.soKyBaoLuu }} kỳ</small>
            </td>
            <td>
              <template v-if="item.trangThai === 'da_di_hoc_lai'">
                Ngày: {{ item.ngayDiHocLai || '-' }}
                <br />
                Version: {{ item.chuongTrinhVersionIdHocLai || '-' }}
                <br />
                Kỳ: {{ item.khungKyIdHocLai || '-' }}
                <br />
                Lớp HC: {{ item.lopHanhChinhIdHocLai || '-' }}
                <br />
                Công nhận: {{ item.soMonDuocCongNhan || 0 }} môn
              </template>
              <template v-else>
                -
              </template>
            </td>
            <td>{{ item.lyDo }}</td>
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
            <select v-model="formDiHocLai.chuongTrinhVersionIdHocLai" required @change="doiVersionHocLai">
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
.sv-page,
.sv-bl-page,
.sv-lhp-page,
.sv-page *,
.sv-bl-page *,
.sv-lhp-page * {
  font-family: 'Roboto', Arial, sans-serif;
  letter-spacing: normal;
  box-sizing: border-box;
}

.sv-page,
.sv-bl-page,
.sv-lhp-page {
  display: grid;
  gap: 14px;
}

.sv-header,
.sv-card,
.sv-filter-card,
.sv-account-box,
.page-title,
.panel {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  padding: 14px;
  box-shadow: 0 8px 22px rgba(15, 23, 42, 0.05);
}

.sv-header {
  background: linear-gradient(135deg, #f8fafc, #eff6ff);
  border-color: #bfdbfe;
}

.sv-header h1,
.page-title h2,
.sv-card-title h2,
.panel-title h2,
.sv-card h2,
.modal-header h2,
.sv-modal-header h2 {
  margin: 0;
  color: #0f172a;
  font-size: 18px;
  font-weight: 700;
  line-height: 1.35;
  letter-spacing: normal;
}

.sv-header p,
.page-title p,
.sv-card-title p,
.panel-title p,
.panel-subtitle,
.sv-card p,
.modal-header p,
.sv-modal-header p {
  margin: 5px 0 0;
  color: #64748b;
  font-size: 13px;
  font-weight: 400;
  line-height: 1.45;
  letter-spacing: normal;
}

.sv-eyebrow {
  margin: 0 0 4px;
  color: #1d4ed8 !important;
  font-size: 13px;
  font-weight: 700;
  text-transform: none;
  letter-spacing: normal;
}

.sv-card-title,
.panel-title {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.panel-title span {
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
}

.sv-grid,
.form-grid,
.filter-grid {
  display: grid;
  gap: 10px;
}

.sv-grid.two,
.form-grid {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.sv-grid.three {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.sv-grid.four,
.filter-grid,
.bao-luu-filter {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.sv-filter-card {
  display: grid;
  grid-template-columns: 1fr 1.2fr 1.2fr 1.2fr auto;
  gap: 10px;
  align-items: end;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(360px, 0.9fr) minmax(0, 1.35fr);
  gap: 14px;
}

.span-2 {
  grid-column: span 2;
}

.span-3 {
  grid-column: span 3;
}

.span-4 {
  grid-column: span 4;
}

label {
  display: grid;
  gap: 5px;
  color: #334155;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: normal;
}

input,
select,
textarea {
  width: 100%;
  min-height: 38px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 8px 10px;
  background: #ffffff;
  color: #0f172a;
  font: inherit;
  font-size: 14px;
  font-weight: 400;
  letter-spacing: normal;
  outline: none;
  transition: border-color 0.15s ease, box-shadow 0.15s ease;
}

textarea {
  min-height: 76px;
  resize: vertical;
}

input:focus,
select:focus,
textarea:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

input:disabled,
select:disabled,
textarea:disabled {
  background: #f8fafc;
  color: #64748b;
  cursor: not-allowed;
}

.sv-actions,
.actions,
.form-actions,
.row-actions,
.sv-row-actions,
.sv-modal-actions {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
}

button,
.sv-action-link {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 36px;
  border: 0;
  border-radius: 10px;
  padding: 8px 12px;
  background: #1d4ed8;
  color: #ffffff;
  font-size: 14px;
  font-weight: 700;
  letter-spacing: normal;
  text-transform: none;
  line-height: 1;
  text-decoration: none;
  cursor: pointer;
  transition: transform 0.12s ease, filter 0.12s ease, background 0.12s ease;
}

button:hover,
.sv-action-link:hover {
  filter: brightness(0.97);
  transform: translateY(-1px);
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
  transform: none;
}

button.secondary {
  background: #e2e8f0;
  color: #334155;
}

button.danger {
  background: #dc2626;
  color: #ffffff;
}

button.small {
  min-height: 30px;
  padding: 6px 9px;
  font-size: 13px;
}

button.selected {
  background: #047857;
  color: #ffffff;
}

.sv-action-link {
  background: #f59e0b;
  color: #ffffff;
}

.sv-tabs {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

.sv-tabs button {
  background: #e2e8f0;
  color: #334155;
}

.sv-tabs button.active {
  background: #1d4ed8;
  color: #ffffff;
}

.sv-message,
.message {
  border-radius: 12px;
  padding: 10px 12px;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: normal;
}

.sv-message.success,
.message.success {
  background: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.sv-message.error,
.message.error {
  background: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

.sv-account-box {
  display: flex;
  gap: 12px;
  flex-wrap: wrap;
  background: #fffbeb;
  border-color: #fbbf24;
  color: #78350f;
}

.sv-class-summary {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px;
  margin-bottom: 12px;
}

.sv-class-summary div,
.sv-flow-item,
.sv-selected,
.sinh-vien-da-chon {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 10px;
  background: #f8fafc;
}

.sv-class-summary span {
  color: #64748b;
  font-size: 12px;
  font-weight: 400;
}

.sv-class-summary strong {
  color: #0f172a;
  font-size: 14px;
  font-weight: 700;
}

.sv-flow {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  gap: 10px;
}

.sv-flow-item {
  display: grid;
  gap: 5px;
}

.sv-flow-item.active {
  border-color: #93c5fd;
  background: #eff6ff;
}

.sv-list-filter {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 10px;
}

.sv-table-wrap,
.table-wrap {
  width: 100%;
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #ffffff;
}

.sv-table,
table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
}

.bang-lop-hoc-phan {
  min-width: 1500px;
}

th,
td,
.sv-table th,
.sv-table td {
  border-bottom: 1px solid #e2e8f0;
  padding: 8px 10px;
  text-align: left;
  vertical-align: top;
}

th,
.sv-table th {
  position: sticky;
  top: 0;
  z-index: 1;
  background: #f8fafc;
  color: #334155;
  font-size: 13px;
  font-weight: 700;
  text-transform: none;
  letter-spacing: normal;
  white-space: nowrap;
}

td,
.sv-table td {
  color: #334155;
  font-size: 13px;
  font-weight: 400;
  letter-spacing: normal;
}

td.diem,
td.cot-diem,
td[data-label*='Điểm'],
td[data-label*='điểm'],
.sv-table td.diem,
.sv-table td.cot-diem,
.sv-table td[data-label*='Điểm'],
.sv-table td[data-label*='điểm'] {
  font-weight: 700;
  color: #0f172a;
}

tbody tr:hover td,
.clickable-row:hover td {
  background: #f8fafc;
}

tr.selected td,
.sv-table tr.selected td {
  background: #eff6ff;
}

.clickable-row {
  cursor: pointer;
}

.empty {
  text-align: center;
  color: #64748b;
  padding: 18px;
  font-weight: 600;
}

.sv-avatar {
  width: 42px;
  height: 42px;
  object-fit: cover;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  background: #f8fafc;
}

.sv-status,
.status {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 4px 9px;
  font-size: 12px;
  font-weight: 600;
  letter-spacing: normal;
  white-space: nowrap;
}

.sv-status.done,
.status.da_di_hoc_lai {
  background: #ecfdf5;
  color: #047857;
}

.sv-status.partial,
.status.dang_bao_luu {
  background: #fffbeb;
  color: #b45309;
}

.sv-status.pending,
.status.da_huy {
  background: #fef2f2;
  color: #b91c1c;
}

.tim-sinh-vien-box {
  display: grid;
  grid-template-columns: minmax(260px, 1fr) auto;
  gap: 10px;
  align-items: end;
}

.sinh-vien-da-chon {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  border-color: #bfdbfe;
  background: #eff6ff;
  color: #1e3a8a;
}

.modal-overlay,
.sv-modal-overlay {
  position: fixed;
  inset: 0;
  z-index: 9999;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
  background: rgba(15, 23, 42, 0.55);
}

.modal,
.sv-modal {
  width: min(900px, 100%);
  max-height: calc(100vh - 40px);
  overflow: auto;
  border-radius: 16px;
  padding: 16px;
  background: #ffffff;
  box-shadow: 0 24px 80px rgba(15, 23, 42, 0.32);
}

.modal-header,
.sv-modal-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 14px;
  margin-bottom: 14px;
}

.modal-close,
.sv-modal-close {
  width: 34px;
  height: 34px;
  min-height: 34px;
  border-radius: 999px;
  padding: 0;
  background: #e2e8f0;
  color: #0f172a;
  font-size: 22px;
  font-weight: 400;
  line-height: 1;
}
.btn-tai-lai {
  width: auto;
  min-width: 84px;
  min-height: 34px;
  align-self: end;
  justify-self: end;
  padding: 7px 12px;
  font-size: 13px;
  font-weight: 700;
  border-radius: 9px;
}
@media (max-width: 1200px) {
  .sv-filter-card,
  .sv-grid.four,
  .filter-grid,
  .form-grid,
  .bao-luu-filter,
  .content-grid,
  .sv-class-summary,
  .sv-flow {
    grid-template-columns: 1fr;
  }

  .span-2,
  .span-3,
  .span-4 {
    grid-column: span 1;
  }
}

@media (max-width: 700px) {
  .sv-header,
  .sv-card,
  .sv-filter-card,
  .sv-account-box,
  .page-title,
  .panel {
    padding: 12px;
    border-radius: 12px;
  }

  .sv-card-title,
  .panel-title {
    display: grid;
  }

  .sv-actions,
  .actions,
  .form-actions,
  .row-actions,
  .sv-row-actions,
  .sv-modal-actions {
    justify-content: stretch;
  }

  button,
  .sv-action-link {
    width: 100%;
  }

  .tim-sinh-vien-box {
    grid-template-columns: 1fr;
  }
}
</style>