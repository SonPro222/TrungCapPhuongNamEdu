<template>
  <section class="sv-lhp-page">
    <div class="page-title">
      <div>
        <h2>Tạo lớp học phần</h2>
        <p>Tạo lớp học phần theo ngành, chương trình, version, kỳ và môn trong chương trình trước khi phân bổ sinh viên.</p>
      </div>
    </div>

    <div v-if="thongBao" class="message" :class="thongBaoLoai">
      {{ thongBao }}
    </div>

    <section class="panel">
      <div class="panel-title">
        <h2>Chọn chương trình đào tạo</h2>
      </div>

      <div class="filter-grid">
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
          Version
          <select v-model="boLoc.chuongTrinhVersionId" @change="doiVersion">
            <option value="">Chọn version</option>
            <option v-for="version in versionTheoChuongTrinh" :key="version.id" :value="version.id">
              {{ version.maVersion }} - {{ version.tenVersion || 'Version' }}
            </option>
          </select>
        </label>

        <label>
          Kỳ / khung kỳ
          <select v-model="boLoc.khungKyId" @change="doiKhungKy">
            <option value="">Chọn kỳ</option>
            <option v-for="ky in khungKyTheoVersion" :key="ky.id" :value="ky.id">
              {{ ky.maKy }} - {{ ky.tenKy }}
            </option>
          </select>
        </label>
      </div>
    </section>

    <section class="content-grid">
      <div class="panel">
        <div class="panel-title">
          <h2>Môn trong kỳ</h2>
          <span>{{ monTrongKy.length }} môn</span>
        </div>

        <div class="table-wrap">
          <table>
            <thead>
            <tr>
              <th>Thứ tự</th>
              <th>ID CT môn</th>
              <th>Mã môn CT</th>
              <th>Môn học ID</th>
              <th>Tên môn</th>
              <th>Tín chỉ</th>
              <th></th>
            </tr>
            </thead>
            <tbody>
            <tr
                v-for="mon in monTrongKy"
                :key="mon.id"
                class="clickable-row"
                :class="{ selected: monDangChon?.id === mon.id }"
                @click="chonMon(mon)"
            >
              <td>{{ mon.thuTu }}</td>
              <td>{{ mon.id }}</td>
              <td>{{ mon.maMonTrongCt }}</td>
              <td>{{ layMonHocId(mon) || '-' }}</td>
              <td>{{ tenMonHoc(mon) }}</td>
              <td>{{ mon.soTinChi }}</td>
              <td>
                <button type="button" @click.stop="chonMon(mon)">Chọn</button>
              </td>
            </tr>
            <tr v-if="!monTrongKy.length">
              <td colspan="7" class="empty">Chưa có môn trong kỳ đã chọn</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="panel">
        <div class="panel-title">
          <div>
            <h2>Lớp học phần theo môn</h2>
            <p v-if="monDangChon" class="panel-subtitle">
              {{ monDangChon.maMonTrongCt }} - {{ tenMonHoc(monDangChon) }}
            </p>
            <p v-else class="panel-subtitle">
              Chọn môn bên trái để xem lớp học phần của môn đó
            </p>
          </div>
          <span>{{ lopHocPhanTheoMon.length }}/1 lớp</span>
        </div>

        <div class="table-wrap">
          <table class="bang-lop-hoc-phan">
            <thead>
            <tr>
              <th>ID</th>
              <th>CT môn ID</th>
              <th>Môn học ID</th>
              <th>Mã lớp</th>
              <th>Tên lớp</th>
              <th>Loại lớp</th>
              <th>Sĩ số tối thiểu</th>
              <th>Sĩ số hiện tại</th>
              <th>Sĩ số tối đa</th>
              <th>Số buổi học</th>
              <th>Ngày bắt đầu</th>
              <th>Ngày kết thúc</th>
              <th>Trạng thái</th>
              <th>Ngày tạo</th>
              <th>Ngày sửa</th>
              <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="lop in lopHocPhanTheoMon" :key="lop.id">
              <td>{{ lop.id }}</td>
              <td>{{ lop.chuongTrinhMonId || '-' }}</td>
              <td>{{ lop.monHocId || '-' }}</td>
              <td>{{ lop.maLop }}</td>
              <td>{{ lop.tenLop }}</td>
              <td>{{ hienThiLoaiLop(lop.loaiLopHocPhan) }}</td>
              <td>{{ lop.siSoToiThieu ?? '-' }}</td>
              <td>{{ lop.soLuongHienTai ?? 0 }}</td>
              <td>{{ lop.soLuongToiDa ?? '-' }}</td>
              <td>{{ lop.soBuoiHoc ?? '-' }}</td>
              <td>{{ lop.ngayBatDau || '-' }}</td>
              <td>{{ lop.ngayKetThuc || '-' }}</td>
              <td>{{ hienThiTrangThai(lop.trangThai) }}</td>
              <td>{{ lop.createdAt || lop.created_at || '-' }}</td>
              <td>{{ lop.updatedAt || lop.updated_at || '-' }}</td>
              <td>
                <div class="row-actions">
                  <button type="button" class="secondary" @click="chonSuaLopHocPhan(lop)">
                    Sửa
                  </button>
                  <button type="button" class="danger" @click="xoaLopHocPhan(lop)">
                    Xóa
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="!monDangChon">
              <td colspan="16" class="empty">Chọn môn để xem lớp học phần.</td>
            </tr>
            <tr v-else-if="!lopHocPhanTheoMon.length">
              <td colspan="16" class="empty">Môn này chưa có lớp học phần.</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </section>

    <section class="panel" v-if="monDangChon && (!daCoLopHocPhanChoMon || idLopHocPhanDangSua)">
      <div class="panel-title">
        <div>
          <h2>{{ idLopHocPhanDangSua ? 'Cập nhật lớp học phần' : 'Tạo lớp học phần cho môn đang chọn' }}</h2>
          <p class="panel-subtitle">
            CT môn ID: {{ monDangChon.id }} / Môn học ID: {{ layMonHocId(monDangChon) || '-' }}
          </p>
        </div>
        <span>{{ monDangChon.maMonTrongCt }}</span>
      </div>

      <form class="form-grid" @submit.prevent="taoLopHocPhan">
        <label>
          Loại lớp
          <select v-model="form.loaiLopHocPhan">
            <option value="CHUYEN_NGANH">Chuyên ngành</option>
            <option value="HOC_CHUNG">Học chung</option>
          </select>
        </label>

        <label>
          Mã lớp học phần
          <input v-model.trim="form.maLop" required />
        </label>

        <label>
          Tên lớp học phần
          <input v-model.trim="form.tenLop" required />
        </label>

        <label>
          Sĩ số tối thiểu
          <input v-model.number="form.siSoToiThieu" type="number" min="1" />
        </label>

        <label>
          Sĩ số tối đa
          <input v-model.number="form.soLuongToiDa" type="number" min="1" />
        </label>

        <label>
          Số buổi học
          <input v-model.number="form.soBuoiHoc" type="number" min="1" />
        </label>

        <label>
          Ngày bắt đầu
          <input v-model="form.ngayBatDau" type="date" />
        </label>

        <label>
          Ngày kết thúc
          <input v-model="form.ngayKetThuc" type="date" />
        </label>

        <label>
          Trạng thái
          <select v-model="form.trangThai">
            <option value="du_kien">Dự kiến</option>
            <option value="dang_mo">Đang mở</option>
            <option value="dang_hoc">Đang học</option>
            <option value="da_ket_thuc">Đã kết thúc</option>
            <option value="huy">Hủy</option>
          </select>
        </label>

        <div class="span-4 form-actions">
          <button type="submit" :disabled="dangLuu">
            {{ dangLuu ? 'Đang lưu...' : (idLopHocPhanDangSua ? 'Cập nhật lớp học phần' : 'Tạo lớp học phần') }}
          </button>

          <button type="button" class="secondary" @click="resetForm">
            Làm mới
          </button>
        </div>
      </form>
    </section>

    <section class="panel" v-else-if="monDangChon && daCoLopHocPhanChoMon">
      <div class="empty">
        Môn này đã có lớp học phần. Một môn chỉ được tạo một lớp học phần.
      </div>
    </section>

    <section class="panel" v-else>
      <div class="empty">Chọn môn trong kỳ để tạo lớp học phần.</div>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { sinhVienApi } from '../api/sinhVienApi'

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])

const monDangChon = ref(null)
const dangLuu = ref(false)
const idLopHocPhanDangSua = ref(null)
const thongBao = ref('')
const thongBaoLoai = ref('success')

const boLoc = reactive({
  nganhId: '',
  chuongTrinhId: '',
  chuongTrinhVersionId: '',
  khungKyId: ''
})

const form = reactive({
  loaiLopHocPhan: 'CHUYEN_NGANH',
  maLop: '',
  tenLop: '',
  siSoToiThieu: 1,
  soLuongToiDa: 40,
  soBuoiHoc: 1,
  ngayBatDau: '',
  ngayKetThuc: '',
  trangThai: 'dang_mo'
})

const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return []
  return danhSachChuongTrinh.value.filter(item => String(item.nganhId) === String(boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  if (!boLoc.chuongTrinhId) return []
  return danhSachVersion.value.filter(item => String(item.chuongTrinhId) === String(boLoc.chuongTrinhId))
})

const khungKyTheoVersion = computed(() => {
  if (!boLoc.chuongTrinhVersionId) return []

  return danhSachKhungKy.value
      .filter(item => String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId))
      .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
})

const monTrongKy = computed(() => {
  if (!boLoc.chuongTrinhVersionId || !boLoc.khungKyId) return []

  return danhSachChuongTrinhMon.value
      .filter(item =>
          String(item.chuongTrinhVersionId) === String(boLoc.chuongTrinhVersionId)
          && String(item.khungKyId) === String(boLoc.khungKyId)
      )
      .sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
})

const lopHocPhanTheoMon = computed(() => {
  if (!monDangChon.value?.id) return []

  return danhSachLopHocPhan.value.filter(lop => {
    if (laLopHocChung(lop)) {
      return danhSachLopHocPhanChuongTrinhMon.value.some(item =>
          String(item.lopHocPhanId) === String(lop.id)
          && String(item.chuongTrinhMonId) === String(monDangChon.value.id)
      )
    }

    return String(lop.chuongTrinhMonId) === String(monDangChon.value.id)
  })
})

const daCoLopHocPhanChoMon = computed(() => {
  return lopHocPhanTheoMon.value.length > 0
})

onMounted(async () => {
  await taiDuLieuBanDau()
})

async function taiDuLieuBanDau() {
  try {
    const [
      nganh,
      chuongTrinh,
      version,
      khungKy,
      chuongTrinhMon,
      lopHocPhan,
      lopHocPhanChuongTrinhMon
    ] = await Promise.all([
      sinhVienApi.nganh.getAll({ size: 1000 }),
      sinhVienApi.chuongTrinh.getAll({ size: 1000 }),
      sinhVienApi.chuongTrinhVersion.getAll({ size: 1000 }),
      sinhVienApi.khungKy.getAll({ size: 1000 }),
      sinhVienApi.chuongTrinhMon.getAll({ size: 1000 }),
      sinhVienApi.lopHocPhan.getAll({ size: 1000 }),
      sinhVienApi.lopHocPhanChuongTrinhMon.getAll({ size: 1000 })
    ])

    danhSachNganh.value = layDanhSach(nganh)
    danhSachChuongTrinh.value = layDanhSach(chuongTrinh)
    danhSachVersion.value = layDanhSach(version)
    danhSachKhungKy.value = layDanhSach(khungKy)
    danhSachChuongTrinhMon.value = layDanhSach(chuongTrinhMon)
    danhSachLopHocPhan.value = layDanhSach(lopHocPhan)
    danhSachLopHocPhanChuongTrinhMon.value = layDanhSach(lopHocPhanChuongTrinhMon)
  } catch (error) {
    baoLoi(error?.message || 'Không tải được dữ liệu tạo lớp học phần')
  }
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
  boLoc.khungKyId = ''
  doiKhungKy()
}

function doiKhungKy() {
  monDangChon.value = null
  resetForm()
}

function chonMon(mon) {
  monDangChon.value = mon
  resetForm()
}

async function taoLopHocPhan() {
  if (!monDangChon.value?.id) {
    baoLoi('Phải chọn môn trong chương trình trước khi tạo lớp học phần')
    return
  }

  if (!idLopHocPhanDangSua.value && daCoLopHocPhanChoMon.value) {
    baoLoi('Môn này đã có lớp học phần, không được tạo thêm lớp mới')
    return
  }

  if (!form.maLop || !form.tenLop) {
    baoLoi('Phải nhập mã lớp và tên lớp học phần')
    return
  }

  if (Number(form.siSoToiThieu) < 1) {
    baoLoi('Sĩ số tối thiểu phải lớn hơn 0')
    return
  }

  if (Number(form.soLuongToiDa) < 1) {
    baoLoi('Sĩ số tối đa phải lớn hơn 0')
    return
  }

  if (Number(form.soLuongToiDa) < Number(form.siSoToiThieu)) {
    baoLoi('Sĩ số tối đa phải lớn hơn hoặc bằng sĩ số tối thiểu')
    return
  }

  if (Number(form.soBuoiHoc) < 1) {
    baoLoi('Số buổi học phải lớn hơn 0')
    return
  }

  const monHocId = layMonHocId(monDangChon.value)

  if (!monHocId) {
    baoLoi('Môn trong chương trình chưa có monHocId, không tạo được lớp học phần')
    return
  }

  dangLuu.value = true

  try {
    const payload = lamSachPayload({
      chuongTrinhMonId: Number(monDangChon.value.id),
      monHocId: Number(monHocId),
      loaiLopHocPhan: form.loaiLopHocPhan,
      maLop: form.maLop,
      tenLop: form.tenLop,
      siSoToiThieu: Number(form.siSoToiThieu),
      soLuongToiDa: Number(form.soLuongToiDa),
      soLuongHienTai: idLopHocPhanDangSua.value ? undefined : 0,
      soBuoiHoc: Number(form.soBuoiHoc),
      ngayBatDau: form.ngayBatDau,
      ngayKetThuc: form.ngayKetThuc,
      trangThai: form.trangThai
    })

    if (idLopHocPhanDangSua.value) {
      const response = await sinhVienApi.lopHocPhan.update(idLopHocPhanDangSua.value, payload)
      const lopCapNhat = layMotBanGhi(response)

      if (lopCapNhat?.id) {
        const index = danhSachLopHocPhan.value.findIndex(item => String(item.id) === String(lopCapNhat.id))
        if (index !== -1) {
          danhSachLopHocPhan.value[index] = lopCapNhat
        }
      } else {
        await taiDuLieuBanDau()
      }

      resetForm()
      baoThanhCong('Đã cập nhật lớp học phần')
      return
    }

    const lopMoiResponse = await sinhVienApi.lopHocPhan.create(payload)
    const lopMoi = layMotBanGhi(lopMoiResponse)

    if (!lopMoi?.id) {
      await taiDuLieuBanDau()
      resetForm()
      baoThanhCong('Đã tạo lớp học phần')
      return
    }

    danhSachLopHocPhan.value.push(lopMoi)

    if (form.loaiLopHocPhan === 'HOC_CHUNG') {
      const ganResponse = await sinhVienApi.lopHocPhanChuongTrinhMon.create({
        lopHocPhanId: Number(lopMoi.id),
        chuongTrinhMonId: Number(monDangChon.value.id),
        ghiChu: ''
      })

      const banGhiGan = layMotBanGhi(ganResponse)

      if (banGhiGan?.id) {
        danhSachLopHocPhanChuongTrinhMon.value.push(banGhiGan)
      } else {
        await taiDuLieuBanDau()
      }
    }

    resetForm()
    baoThanhCong('Đã tạo lớp học phần')
  } catch (error) {
    baoLoi(error?.message || 'Không tạo được lớp học phần')
  } finally {
    dangLuu.value = false
  }
}

function chonSuaLopHocPhan(lop) {
  idLopHocPhanDangSua.value = lop.id
  form.loaiLopHocPhan = lop.loaiLopHocPhan || 'CHUYEN_NGANH'
  form.maLop = lop.maLop || ''
  form.tenLop = lop.tenLop || ''
  form.siSoToiThieu = Number(lop.siSoToiThieu || 1)
  form.soLuongToiDa = Number(lop.soLuongToiDa || 40)
  form.soBuoiHoc = Number(lop.soBuoiHoc || 1)
  form.ngayBatDau = lop.ngayBatDau || ''
  form.ngayKetThuc = lop.ngayKetThuc || ''
  form.trangThai = lop.trangThai || 'dang_mo'
}

async function xoaLopHocPhan(lop) {
  const dongY = window.confirm(`Xóa lớp học phần "${lop.maLop}"?`)
  if (!dongY) return

  try {
    await sinhVienApi.lopHocPhan.delete(lop.id)

    danhSachLopHocPhan.value = danhSachLopHocPhan.value.filter(item => String(item.id) !== String(lop.id))
    danhSachLopHocPhanChuongTrinhMon.value = danhSachLopHocPhanChuongTrinhMon.value
        .filter(item => String(item.lopHocPhanId) !== String(lop.id))

    if (String(idLopHocPhanDangSua.value) === String(lop.id)) {
      resetForm()
    }

    baoThanhCong('Đã xóa lớp học phần')
  } catch (error) {
    baoLoi(error?.message || 'Không xóa được lớp học phần')
  }
}

function resetForm() {
  idLopHocPhanDangSua.value = null
  form.loaiLopHocPhan = 'CHUYEN_NGANH'
  form.maLop = ''
  form.tenLop = ''
  form.siSoToiThieu = 1
  form.soLuongToiDa = 40
  form.soBuoiHoc = 1
  form.ngayBatDau = ''
  form.ngayKetThuc = ''
  form.trangThai = 'dang_mo'
}

function laLopHocChung(lop) {
  return String(lop?.loaiLopHocPhan || '').toUpperCase() === 'HOC_CHUNG'
}

function layMonHocId(mon) {
  return mon?.monHocId
      || mon?.monHoc?.id
      || mon?.mon_hoc_id
      || null
}

function tenMonHoc(mon) {
  return mon?.tenMonHoc
      || mon?.monHoc?.tenMonHoc
      || mon?.tenMon
      || mon?.monHoc?.tenMon
      || mon?.maMonTrongCt
      || 'Môn học'
}

function hienThiLoaiLop(value) {
  const map = {
    CHUYEN_NGANH: 'Chuyên ngành',
    HOC_CHUNG: 'Học chung'
  }

  return map[value] || value || '-'
}

function hienThiTrangThai(value) {
  const map = {
    du_kien: 'Dự kiến',
    dang_mo: 'Đang mở',
    dang_hoc: 'Đang học',
    da_ket_thuc: 'Đã kết thúc',
    huy: 'Hủy'
  }

  return map[value] || value || '-'
}

function layDanhSach(response) {
  if (Array.isArray(response)) return response
  if (Array.isArray(response?.content)) return response.content
  if (Array.isArray(response?.data?.content)) return response.data.content
  if (Array.isArray(response?.data)) return response.data
  return []
}

function layMotBanGhi(response) {
  if (response?.data && !Array.isArray(response.data)) return response.data
  if (response && !Array.isArray(response)) return response
  return null
}

function lamSachPayload(payload) {
  return Object.fromEntries(
      Object.entries(payload).filter(([, value]) => value !== '' && value !== null && value !== undefined)
  )
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
.sv-lhp-page {
  display: grid;
  gap: 18px;
}

.page-title,
.panel {
  background: #ffffff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 10px 30px rgba(15, 23, 42, 0.06);
}

.page-title h2,
.panel-title h2 {
  margin: 0;
  color: #0f172a;
}

.page-title h2 {
  font-size: 22px;
}

.page-title p {
  margin: 6px 0 0;
  color: #64748b;
}

.panel-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.panel-title h2 {
  font-size: 18px;
}

.panel-title span {
  color: #64748b;
  font-weight: 700;
}

.panel-subtitle {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
}

.filter-grid,
.form-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}

.content-grid {
  display: grid;
  grid-template-columns: minmax(420px, 0.9fr) minmax(0, 1.4fr);
  gap: 18px;
}

.span-4 {
  grid-column: span 4;
}

label {
  display: grid;
  gap: 8px;
  font-weight: 700;
  color: #334155;
}

select,
input {
  min-height: 42px;
  border: 1px solid #cbd5e1;
  border-radius: 12px;
  padding: 9px 12px;
  outline: none;
  background: #ffffff;
}

select:focus,
input:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.12);
}

.table-wrap {
  width: 100%;
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

.bang-lop-hoc-phan {
  min-width: 1600px;
}

th,
td {
  border-bottom: 1px solid #e2e8f0;
  padding: 10px;
  text-align: left;
  vertical-align: top;
  white-space: nowrap;
}

th {
  font-size: 12px;
  text-transform: uppercase;
  color: #64748b;
  background: #f8fafc;
}

tr.selected td {
  background: #eff6ff;
}

button {
  border: 0;
  border-radius: 10px;
  padding: 9px 13px;
  font-weight: 700;
  cursor: pointer;
  background: #1d4ed8;
  color: #ffffff;
}

button.secondary {
  background: #e2e8f0;
  color: #334155;
}

button.danger {
  background: #dc2626;
  color: #ffffff;
}

button:disabled {
  opacity: 0.65;
  cursor: not-allowed;
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.empty {
  text-align: center;
  color: #64748b;
  padding: 20px;
}

.message {
  border-radius: 14px;
  padding: 12px 14px;
  font-weight: 700;
}

.message.success {
  background: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.message.error {
  background: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

.clickable-row {
  cursor: pointer;
}

.clickable-row:hover td {
  background: #f8fafc;
}

.row-actions {
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}

@media (max-width: 1100px) {
  .filter-grid,
  .form-grid,
  .content-grid {
    grid-template-columns: 1fr;
  }

  .span-4 {
    grid-column: span 1;
  }
}
</style>