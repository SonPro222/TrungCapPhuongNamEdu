<template>
  <section class="page">
    <header class="page-head">
      <div>
        <button type="button" class="btn ghost" @click="quayLai">
          ← Quay lại danh sách lớp học phần
        </button>

        <h1>Điều phối giảng dạy</h1>

        <p v-if="lopHocPhan">
          Phân phòng, ca học và sinh lịch học cho lớp {{ lopHocPhan.maLop }}.
        </p>
      </div>

      <div class="header-actions">
        <button type="button" class="btn" @click="diDenMaTranLich">
          Xem lịch học
        </button>

        <button type="button" class="btn" @click="taiDuLieu">
          Tải lại
        </button>
      </div>
    </header>

    <div v-if="loi" class="alert error">
      {{ loi }}
    </div>

    <div v-if="thongBao" class="alert success">
      {{ thongBao }}
    </div>

    <section v-if="lopHocPhan" class="class-strip">
      <div class="class-main">
        <strong>{{ lopHocPhan.maLop || '-' }}</strong>
        <span>{{ lopHocPhan.tenLop || '-' }}</span>
      </div>

      <div class="class-meta">
        <span><b>Môn:</b> {{ tenMonHocTheoLop(lopHocPhan) }}</span>
        <span><b>Kỳ:</b> {{ tenKyTheoLop(lopHocPhan) }}</span>
        <span><b>Loại:</b> {{ hienThiLoaiLop(lopHocPhan.loaiLopHocPhan) }}</span>
        <span><b>Sĩ số:</b> {{ lopHocPhan.soLuongHienTai ?? 0 }}/{{ lopHocPhan.soLuongToiDa || '-' }}</span>
        <span><b>Buổi:</b> {{ soBuoiDaXep }}/{{ lopHocPhan.soBuoiHoc || '-' }}</span>
        <span><b>Còn thiếu:</b> {{ soBuoiConLai }}</span>
        <span><b>Cột điểm:</b> {{ cotDiemTheoLop.length }}</span>
      </div>
    </section>

    <section class="table-card">
      <div class="table-head">
        <div>
          <h2>Sinh lịch học tự động</h2>
          <p>Giáo viên lấy từ phân công giảng dạy. Tại đây chỉ chọn phòng học, ca học, ngày học và thứ học trong tuần.</p>
        </div>
      </div>

      <div class="assigned-teacher-card" :class="{ warning: !coGiaoVienChinh }">
        <div>
          <span>Giảng viên đã phân công</span>
          <strong>{{ coGiaoVienChinh ? hienThiGiaoVien(giaoVienChinhDieuPhoi) : 'Chưa có giảng viên chính' }}</strong>
          <small v-if="coGiaoVienChinh">
            {{ giaoVienChinhDieuPhoi.email || 'Chưa có email' }}
            <template v-if="giaoVienChinhDieuPhoi.chuyenMon">
              · {{ giaoVienChinhDieuPhoi.chuyenMon }}
            </template>
          </small>
          <small v-else>Cần phân công giảng viên chính trước khi sinh lịch học.</small>
        </div>

        <button type="button" class="btn" @click="diDenPhanCongGiangDay">
          {{ coGiaoVienChinh ? 'Đổi phân công' : 'Phân công giáo viên' }}
        </button>
      </div>

      <div class="form-grid schedule-form">
        <label>
          <span>Phòng học</span>
          <select v-model.number="formSinhLich.phongHocId">
            <option :value="null">-- Chọn phòng --</option>
            <option v-for="phong in danhSachPhongHoc" :key="phong.id" :value="phong.id">
              {{ hienThiPhongHoc(phong) }}
            </option>
          </select>
        </label>

        <label>
          <span>Ca học</span>
          <select v-model.number="formSinhLich.caHocId">
            <option :value="null">-- Chọn ca --</option>
            <option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">
              {{ hienThiCaHoc(ca) }}
            </option>
          </select>
        </label>

        <label>
          <span>Từ ngày</span>
          <input v-model="formSinhLich.tuNgay" type="date" />
        </label>

        <label>
          <span>Đến ngày</span>
          <input v-model="formSinhLich.denNgay" type="date" />
        </label>
      </div>

      <div class="weekday-card">
        <strong>Thứ học trong tuần</strong>

        <div class="weekday-row">
          <label><input type="checkbox" :value="1" v-model="formSinhLich.thuTrongTuan" /> Thứ 2</label>
          <label><input type="checkbox" :value="2" v-model="formSinhLich.thuTrongTuan" /> Thứ 3</label>
          <label><input type="checkbox" :value="3" v-model="formSinhLich.thuTrongTuan" /> Thứ 4</label>
          <label><input type="checkbox" :value="4" v-model="formSinhLich.thuTrongTuan" /> Thứ 5</label>
          <label><input type="checkbox" :value="5" v-model="formSinhLich.thuTrongTuan" /> Thứ 6</label>
          <label><input type="checkbox" :value="6" v-model="formSinhLich.thuTrongTuan" /> Thứ 7</label>
          <label><input type="checkbox" :value="7" v-model="formSinhLich.thuTrongTuan" /> Chủ nhật</label>
        </div>
      </div>

      <div v-if="!coGiaoVienChinh" class="alert error warning-list">
        Lớp học phần chưa có giảng viên chính. Vào Phân công giáo viên để gán giảng viên trước.
      </div>

      <div v-else-if="soBuoiConLai <= 0" class="alert success warning-list">
        Lớp học phần đã được xếp đủ số buổi học.
      </div>

      <div class="actions">
        <button
            type="button"
            class="btn primary"
            :disabled="dangXuLy || !coGiaoVienChinh || soBuoiConLai <= 0"
            @click="xemTruocSinhLich"
        >
          {{ dangXuLy ? 'Đang xử lý...' : 'Xem trước lịch' }}
        </button>

        <button
            type="button"
            class="btn"
            :disabled="dangXuLy || !coGiaoVienChinh || soBuoiConLai <= 0"
            @click="goiYLichThongMinh"
        >
          Gợi ý lịch thông minh
        </button>

        <button
            v-if="previewLich?.hopLe"
            type="button"
            class="btn success-btn"
            :disabled="dangXuLy"
            @click="luuLichTuDong"
        >
          Lưu lịch tự động
        </button>
      </div>
    </section>

    <section v-if="goiYLich.length" class="table-card">
      <div class="table-head">
        <div>
          <h2>Gợi ý lịch thông minh</h2>
          <p>Hệ thống thử nhiều phòng và ca học, sau đó xếp hạng theo số buổi xếp được, độ ít cảnh báo và sức chứa phòng.</p>
        </div>
      </div>

      <div class="suggestion-grid">
        <article v-for="item in goiYLich" :key="`${item.phongHocId}-${item.caHocId}-${item.thuTuPhuongAn}`" class="suggestion-card">
          <div class="suggestion-top">
            <strong>Phương án {{ item.thuTuPhuongAn }}</strong>
            <span :class="['status', item.hopLe ? 'done' : 'pending']">
              {{ item.hopLe ? 'Đủ lịch' : 'Chưa đủ' }}
            </span>
          </div>

          <div class="buoi-info">
            <span><b>Phòng:</b> {{ tenPhongTheoId(item.phongHocId) }}</span>
            <span><b>Ca:</b> {{ tenCaTheoId(item.caHocId) }}</span>
            <span><b>Thứ:</b> {{ hienThiThuTrongTuan(item.thuTrongTuan) }}</span>
            <span><b>Xếp được:</b> {{ item.soBuoiXepDuoc }}/{{ item.soBuoiCanXep }}</span>
            <span><b>Điểm ưu tiên:</b> {{ item.diemUuTien }}</span>
          </div>

          <div v-if="item.canhBao?.length" class="mini-warning">
            {{ item.canhBao.slice(0, 2).join(' · ') }}
            <template v-if="item.canhBao.length > 2">...</template>
          </div>

          <button type="button" class="btn primary full" @click="chonPhuongAnGoiY(item)">
            Dùng phương án này
          </button>
        </article>
      </div>
    </section>

    <section v-if="previewLich" class="table-card">
      <div class="table-head">
        <div>
          <h2>Preview lịch học</h2>
          <p>Xếp được {{ previewLich.soBuoiXepDuoc }}/{{ previewLich.soBuoiConLai }} buổi còn lại.</p>
        </div>

        <span :class="['status', previewLich.hopLe ? 'done' : 'cancel']">
          {{ previewLich.hopLe ? 'Hợp lệ' : 'Chưa hợp lệ' }}
        </span>
      </div>

      <div v-if="previewLich.canhBao?.length" class="alert error warning-list">
        <div v-for="msg in previewLich.canhBao" :key="msg">
          {{ msg }}
        </div>
      </div>

      <div class="table-wrap preview-wrap">
        <table class="data-table">
          <thead>
          <tr>
            <th>Buổi</th>
            <th>Ngày</th>
            <th>Ca</th>
            <th>Phòng</th>
            <th>Giáo viên</th>
            <th>Nội dung</th>
          </tr>
          </thead>

          <tbody>
          <tr v-if="!previewLich.items?.length">
            <td colspan="6" class="empty">Không có buổi học nào được xếp.</td>
          </tr>

          <tr v-for="item in previewLich.items" v-else :key="`${item.stt}-${item.ngayHoc}`">
            <td>B{{ item.stt }}</td>
            <td>{{ item.ngayHoc }}</td>
            <td>{{ tenCaTheoId(item.caHocId) }}</td>
            <td>{{ tenPhongTheoId(item.phongHocId) }}</td>
            <td>{{ tenGiaoVienTheoId(item.giaoVienId) }}</td>
            <td>{{ item.noiDungBuoiHoc || '-' }}</td>
          </tr>
          </tbody>
        </table>
      </div>
    </section>

    <section class="table-card">
      <div class="table-head">
        <div>
          <h2>Buổi học của lớp</h2>
          <p>Bấm vào từng buổi để xem danh sách sinh viên, điểm của lớp và điểm danh đúng buổi đó.</p>
        </div>
      </div>

      <div v-if="dangTai" class="empty-box">
        Đang tải dữ liệu...
      </div>

      <div v-else-if="lichHocTheoLop.length === 0" class="empty-box">
        Lớp học phần này chưa có buổi học. Cần xếp lịch học trước khi xem điểm danh.
      </div>

      <div v-else class="buoi-grid">
        <article
            v-for="(buoi, index) in lichHocTheoLop"
            :key="buoi.id"
            class="buoi-card"
        >
          <div class="buoi-top">
            <strong>B{{ index + 1 }}</strong>
            <span :class="['status', classTrangThaiLichHoc(buoi.trangThai)]">
              {{ hienThiTrangThaiLichHoc(buoi.trangThai) }}
            </span>
          </div>

          <h3>{{ buoi.ngayHoc || '-' }}</h3>

          <div class="buoi-info">
            <span><b>Ca:</b> {{ buoi.tenCa || buoi.maCa || tenCaTheoId(buoi.caHocId) }}</span>
            <span><b>Phòng:</b> {{ buoi.tenPhong || buoi.maPhong || tenPhongTheoId(buoi.phongHocId) }}</span>
            <span><b>Giáo viên:</b> {{ buoi.tenGiaoVien || tenGiaoVienTheoId(buoi.giaoVienId) }}</span>
            <span><b>Nội dung:</b> {{ buoi.noiDungBuoiHoc || 'Buổi học' }}</span>
          </div>

          <button type="button" class="btn primary full" @click="xemBuoiHoc(buoi)">
            Xem buổi học
          </button>
        </article>
      </div>
    </section>

    <section class="note-card">
      <strong>Cột điểm của lớp:</strong>

      <span v-if="cotDiemTheoLop.length === 0">
        Chưa có cấu hình cột điểm.
      </span>

      <span v-for="cotDiem in cotDiemTheoLop" :key="cotDiem.id">
        {{ tenCotDiem(cotDiem) }}: {{ tieuDeCotDiem(cotDiem) }}
      </span>
    </section>
  </section>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

const lopHocPhanId = computed(() => Number(route.params.id))

const dangTai = ref(false)
const dangXuLy = ref(false)
const loi = ref('')
const thongBao = ref('')

const lopHocPhan = ref(null)
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachLichHoc = ref([])
const danhSachCauHinhDanhGia = ref([])
const danhSachGiaoVien = ref([])
const danhSachPhongHoc = ref([])
const danhSachCaHoc = ref([])
const previewLich = ref(null)
const goiYLich = ref([])
const danhSachPhanCong = ref([])
const formSinhLich = ref({
  giaoVienId: null,
  phongHocId: null,
  caHocId: null,
  tuNgay: '',
  denNgay: '',
  thuTrongTuan: [],
  tuDongGanNoiDungSyllabus: true
})

const lichHocTheoLop = computed(() => {
  return danhSachLichHoc.value
      .filter((item) => Number(item.lopHocPhanId) === Number(lopHocPhanId.value))
      .sort((a, b) => {
        const ngayA = a.ngayHoc || ''
        const ngayB = b.ngayHoc || ''

        if (ngayA !== ngayB) {
          return ngayA.localeCompare(ngayB)
        }

        return Number(a.caHocId || 0) - Number(b.caHocId || 0)
      })
})

const soBuoiDaXep = computed(() => lichHocTheoLop.value.filter((item) => item.trangThai !== 'nghi').length)

const soBuoiConLai = computed(() => {
  const tong = Number(lopHocPhan.value?.soBuoiHoc || 0)
  return Math.max(0, tong - soBuoiDaXep.value)
})

const cotDiemTheoLop = computed(() => {
  return danhSachCauHinhDanhGia.value
      .filter((item) => {
        if (item.lopHocPhanId !== undefined && item.lopHocPhanId !== null) {
          return Number(item.lopHocPhanId) === Number(lopHocPhanId.value)
        }

        if (item.lopHocPhan?.id !== undefined && item.lopHocPhan?.id !== null) {
          return Number(item.lopHocPhan.id) === Number(lopHocPhanId.value)
        }

        return true
      })
      .sort((a, b) => {
        const thuTuA = Number(a.thuTu || a.thuTuCot || 0)
        const thuTuB = Number(b.thuTu || b.thuTuCot || 0)

        if (thuTuA !== thuTuB) {
          return thuTuA - thuTuB
        }

        return Number(a.id || 0) - Number(b.id || 0)
      })
})

const phanCongTheoLop = computed(() => {
  return danhSachPhanCong.value.filter((item) => {
    return Number(item.lopHocPhanId) === Number(lopHocPhanId.value)
  })
})

const phanCongGiangVienChinh = computed(() => {
  return phanCongTheoLop.value.find((item) => item.vaiTro === 'giang_vien_chinh') || null
})

const giaoVienChinhDieuPhoi = computed(() => {
  if (!phanCongGiangVienChinh.value) return null
  return layGiaoVienTuPhanCong(phanCongGiangVienChinh.value)
})

const coGiaoVienChinh = computed(() => Boolean(giaoVienChinhDieuPhoi.value?.id))

onMounted(() => {
  taiDuLieu()
})

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    const [
      lop,
      khungKy,
      chuongTrinhMon,
      monHoc,
      lopHocPhanChuongTrinhMon,
      lichHoc,
      cauHinhDanhGia,
      phanCong,
      giaoVien,
      phongHoc,
      caHoc
    ] = await Promise.all([
      giangDayService.layLopHocPhanTheoId(lopHocPhanId.value),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachChuongTrinhMon(),
      giangDayService.layDanhSachMonHoc(),
      giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 500 }),
      giangDayService.layTrangLichHoc({ lopHocPhanId: lopHocPhanId.value, size: 500 }),
      giangDayService.layTrangCauHinhDanhGia({ lopHocPhanId: lopHocPhanId.value, size: 1000 }),
      giangDayService.layTrangPhanCongGiangDay({ lopHocPhanId: lopHocPhanId.value, size: 500 }),
      giangDayService.layDanhSachGiaoVien(),
      giangDayService.layDanhSachPhongHoc(),
      giangDayService.layDanhSachCaHoc()
    ])

    lopHocPhan.value = lop
    danhSachKhungKy.value = khungKy
    danhSachChuongTrinhMon.value = chuongTrinhMon
    danhSachMonHoc.value = monHoc
    danhSachLopHocPhanChuongTrinhMon.value = lopHocPhanChuongTrinhMon.content
    danhSachLichHoc.value = lichHoc.content
    danhSachCauHinhDanhGia.value = cauHinhDanhGia.content
    danhSachPhanCong.value = phanCong.content
    danhSachGiaoVien.value = giaoVien
    danhSachPhongHoc.value = phongHoc
    danhSachCaHoc.value = caHoc

    ganMacDinhFormSinhLich(lop)
    ganGiaoVienChinhVaoForm()
  } catch (error) {
    loi.value = layThongBaoLoi(error, 'Không tải được dữ liệu điều phối giảng dạy')
  } finally {
    dangTai.value = false
  }
}

function ganMacDinhFormSinhLich(lop) {
  if (!lop) return

  if (!formSinhLich.value.tuNgay) {
    formSinhLich.value.tuNgay = lop.ngayBatDau || ''
  }

  if (!formSinhLich.value.denNgay) {
    formSinhLich.value.denNgay = lop.ngayKetThuc || ''
  }
}

async function xemTruocSinhLich() {
  loi.value = ''
  thongBao.value = ''
  previewLich.value = null
  goiYLich.value = []

  if (!coGiaoVienChinh.value) {
    loi.value = 'Lớp học phần chưa có giảng viên chính. Cần phân công giáo viên trước khi sinh lịch.'
    return
  }

  if (soBuoiConLai.value <= 0) {
    loi.value = 'Lớp học phần đã được xếp đủ số buổi học.'
    return
  }

  ganGiaoVienChinhVaoForm()
  dangXuLy.value = true

  try {
    previewLich.value = await giangDayService.previewSinhLich(lopHocPhanId.value, taoPayloadSinhLich())
  } catch (error) {
    loi.value = layThongBaoLoi(error, 'Không xem trước được lịch học')
  } finally {
    dangXuLy.value = false
  }
}

async function luuLichTuDong() {
  dangXuLy.value = true
  loi.value = ''
  thongBao.value = ''

  try {
    ganGiaoVienChinhVaoForm()
    await giangDayService.taoLichTuDong(lopHocPhanId.value, taoPayloadSinhLich())
    previewLich.value = null
    goiYLich.value = []
    thongBao.value = 'Đã lưu lịch học tự động'
    await taiDuLieu()
  } catch (error) {
    loi.value = layThongBaoLoi(error, 'Không lưu được lịch học tự động')
  } finally {
    dangXuLy.value = false
  }
}

async function goiYLichThongMinh() {
  loi.value = ''
  thongBao.value = ''
  previewLich.value = null
  goiYLich.value = []

  if (!coGiaoVienChinh.value) {
    loi.value = 'Lớp học phần chưa có giảng viên chính. Cần phân công giáo viên trước khi gợi ý lịch.'
    return
  }

  ganGiaoVienChinhVaoForm()
  dangXuLy.value = true

  try {
    const payload = taoPayloadSinhLich()
    payload.phongHocId = null
    payload.caHocId = null
    payload.soPhuongAn = 6
    goiYLich.value = await giangDayService.goiYLichHoc(lopHocPhanId.value, payload)
    if (!goiYLich.value.length) {
      loi.value = 'Chưa tìm được phương án phù hợp. Kiểm tra lại khoảng ngày, giáo viên, sĩ số hoặc dữ liệu phòng học.'
    }
  } catch (error) {
    loi.value = layThongBaoLoi(error, 'Không gợi ý được lịch học')
  } finally {
    dangXuLy.value = false
  }
}

function chonPhuongAnGoiY(item) {
  formSinhLich.value.phongHocId = item.phongHocId || null
  formSinhLich.value.caHocId = item.caHocId || null
  formSinhLich.value.thuTrongTuan = Array.isArray(item.thuTrongTuan) ? item.thuTrongTuan.map(Number) : []

  previewLich.value = {
    hopLe: item.hopLe,
    soBuoiCanXep: item.soBuoiCanXep,
    soBuoiDaXep: soBuoiDaXep.value,
    soBuoiConLai: item.soBuoiCanXep,
    soBuoiXepDuoc: item.soBuoiXepDuoc,
    canhBao: item.canhBao || [],
    items: item.items || []
  }

  thongBao.value = 'Đã áp dụng phương án gợi ý vào form. Kiểm tra preview rồi bấm Lưu lịch tự động.'
}

function taoPayloadSinhLich() {
  return {
    giaoVienId: giaoVienChinhDieuPhoi.value?.id || formSinhLich.value.giaoVienId || null,
    phongHocId: formSinhLich.value.phongHocId || null,
    caHocId: formSinhLich.value.caHocId || null,
    tuNgay: formSinhLich.value.tuNgay || null,
    denNgay: formSinhLich.value.denNgay || null,
    thuTrongTuan: formSinhLich.value.thuTrongTuan.map((item) => Number(item)),
    tuDongGanNoiDungSyllabus: true
  }
}

function ganGiaoVienChinhVaoForm() {
  formSinhLich.value.giaoVienId = giaoVienChinhDieuPhoi.value?.id || null
}

function layGiaoVienTuPhanCong(phanCong) {
  if (!phanCong) return null

  const gv = danhSachGiaoVien.value.find((item) => {
    return Number(item.id) === Number(phanCong.giaoVienId)
  })

  return {
    id: phanCong.giaoVienId || gv?.id,
    maGiaoVien: phanCong.maGiaoVien || gv?.maGiaoVien,
    hoTen: phanCong.tenGiaoVien || phanCong.hoTen || gv?.hoTen || gv?.tenGiaoVien,
    tenGiaoVien: phanCong.tenGiaoVien || gv?.tenGiaoVien || gv?.hoTen,
    email: phanCong.emailGiaoVien || phanCong.email || gv?.email,
    chuyenMon: phanCong.chuyenMon || gv?.chuyenMon,
    vaiTro: phanCong.vaiTro
  }
}

function diDenPhanCongGiangDay() {
  router.push({
    name: 'GiangDay.PhanCong',
    query: {
      lopHocPhanId: lopHocPhanId.value,
      nganhId: route.query.nganhId,
      chuongTrinhId: route.query.chuongTrinhId,
      versionId: route.query.versionId,
      khungKyId: route.query.khungKyId
    }
  })
}

function diDenMaTranLich() {
  router.push({
    name: 'GiangDay.LichHoc',
    query: route.query
  })
}

function hienThiThuTrongTuan(list) {
  const map = { 1: 'Thứ 2', 2: 'Thứ 3', 3: 'Thứ 4', 4: 'Thứ 5', 5: 'Thứ 6', 6: 'Thứ 7', 7: 'Chủ nhật' }
  return (list || []).map((item) => map[Number(item)] || item).join(', ') || '-'
}

function quayLai() {
  const query = {}

  if (route.query.nganhId) query.nganhId = route.query.nganhId
  if (route.query.chuongTrinhId) query.chuongTrinhId = route.query.chuongTrinhId
  if (route.query.versionId) query.versionId = route.query.versionId
  if (route.query.khungKyId) query.khungKyId = route.query.khungKyId

  router.push({
    name: 'GiangDay.LopHocPhan',
    query
  })
}

function xemBuoiHoc(buoi) {
  router.push({
    name: 'GiangDay.ChiTietBuoiHoc',
    params: {
      lopHocPhanId: lopHocPhanId.value,
      lichHocId: buoi.id
    },
    query: route.query
  })
}

function layChuongTrinhMonIdsTheoLop(lop) {
  if (!lop) return []

  const ids = []

  if (lop.chuongTrinhMonId) {
    ids.push(Number(lop.chuongTrinhMonId))
  }

  danhSachLopHocPhanChuongTrinhMon.value
      .filter((item) => Number(item.lopHocPhanId) === Number(lop.id))
      .forEach((item) => {
        if (item.chuongTrinhMonId) {
          ids.push(Number(item.chuongTrinhMonId))
        }
      })

  return [...new Set(ids)]
}

function layChuongTrinhMonTheoLop(lop) {
  const ids = layChuongTrinhMonIdsTheoLop(lop)

  return danhSachChuongTrinhMon.value.filter((item) => {
    return ids.includes(Number(item.id))
  })
}

function tenMonHocTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const monHocId = lop?.monHocId || ctm?.monHocId

  const monHoc = danhSachMonHoc.value.find((item) => {
    return Number(item.id) === Number(monHocId)
  })

  return monHoc?.tenMonHoc || monHoc?.tenMon || monHoc?.name || monHocId || '-'
}

function tenKyTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]

  const khungKy = danhSachKhungKy.value.find((item) => {
    return Number(item.id) === Number(ctm?.khungKyId)
  })

  return khungKy?.tenKy || khungKy?.maKy || ctm?.khungKyId || '-'
}

function tenCotDiem(cotDiem) {
  return cotDiem.tenCotDiem ||
      cotDiem.tenCot ||
      cotDiem.tenThanhPhan ||
      cotDiem.tenCauHinh ||
      `Cột ${cotDiem.thuTu || cotDiem.thuTuCot || cotDiem.id}`
}

function tieuDeCotDiem(cotDiem) {
  const tenCot = tenCotDiem(cotDiem)
  const tyLe = cotDiem.tyLe !== null && cotDiem.tyLe !== undefined ? ` - ${cotDiem.tyLe}%` : ''
  const diemToiDa = cotDiem.diemToiDa !== null && cotDiem.diemToiDa !== undefined ? ` - Tối đa ${cotDiem.diemToiDa}` : ''

  return `${tenCot}${tyLe}${diemToiDa}`
}

function hienThiLoaiLop(value) {
  const map = {
    CHUYEN_NGANH: 'Chuyên ngành',
    HOC_CHUNG: 'Học chung'
  }

  return map[value] || value || '-'
}

function hienThiTrangThaiLichHoc(value) {
  const map = {
    du_kien: 'Dự kiến',
    da_day: 'Đã dạy',
    nghi: 'Nghỉ',
    day_bu: 'Dạy bù'
  }

  return map[value] || value || 'Dự kiến'
}

function classTrangThaiLichHoc(value) {
  const map = {
    du_kien: 'pending',
    da_day: 'done',
    nghi: 'cancel',
    day_bu: 'makeup'
  }

  return map[value] || 'pending'
}

function hienThiGiaoVien(item) {
  if (!item) return '-'

  const ma = item.maGiaoVien ? `${item.maGiaoVien} - ` : ''
  const ten = item.hoTen || item.tenGiaoVien || item.name || 'Chưa có tên'

  return `${ma}${ten}`
}

function hienThiPhongHoc(item) {
  if (!item) return '-'
  const sucChua = item.sucChua ? ` - ${item.sucChua} chỗ` : ''
  return `${item.maPhong ? item.maPhong + ' - ' : ''}${item.tenPhong || item.name || item.id}${sucChua}`
}

function hienThiCaHoc(item) {
  if (!item) return '-'
  const gio = item.gioBatDau && item.gioKetThuc ? ` (${item.gioBatDau} - ${item.gioKetThuc})` : ''
  return `${item.maCa ? item.maCa + ' - ' : ''}${item.tenCa || item.name || item.id}${gio}`
}

function tenGiaoVienTheoId(id) {
  const item = danhSachGiaoVien.value.find((gv) => Number(gv.id) === Number(id))
  return hienThiGiaoVien(item) || id || '-'
}

function tenPhongTheoId(id) {
  const item = danhSachPhongHoc.value.find((phong) => Number(phong.id) === Number(id))
  return hienThiPhongHoc(item) || id || '-'
}

function tenCaTheoId(id) {
  const item = danhSachCaHoc.value.find((ca) => Number(ca.id) === Number(id))
  return hienThiCaHoc(item) || id || '-'
}

function layThongBaoLoi(error, fallback) {
  return error?.response?.data?.message ||
      error?.response?.data?.error ||
      error?.message ||
      fallback
}
</script>

<style scoped>
.page {
  display: flex;
  flex-direction: column;
  gap: 14px;
  color: #344054;
  font-size: 14px;
}

.page-head,
.table-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: flex-start;
}

.page-head h1,
.table-head h2 {
  margin: 8px 0 0;
  color: #101828;
  font-size: 22px;
  font-weight: 650;
  line-height: 1.3;
}

.page-head p,
.table-head p {
  margin: 6px 0 0;
  color: #667085;
}

.class-strip,
.table-card,
.note-card {
  border: 1px solid #eaecf0;
  border-radius: 12px;
  background: #ffffff;
  padding: 14px;
}

.class-strip {
  display: grid;
  grid-template-columns: minmax(220px, 320px) 1fr;
  gap: 12px;
  align-items: center;
}

.class-main {
  display: grid;
  gap: 4px;
}

.class-main strong {
  color: #101828;
  font-size: 18px;
  font-weight: 700;
}

.class-main span {
  color: #475467;
  font-size: 14px;
  font-weight: 500;
}

.class-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.class-meta span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  min-height: 30px;
  border: 1px solid #eaecf0;
  border-radius: 999px;
  background: #f9fafb;
  padding: 5px 10px;
  color: #475467;
  font-size: 13px;
  font-weight: 500;
}

.class-meta b {
  color: #101828;
  font-weight: 650;
}

.header-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  justify-content: flex-end;
}

.suggestion-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.suggestion-card {
  border: 1px solid #eaecf0;
  border-radius: 12px;
  padding: 12px;
  display: grid;
  gap: 10px;
  background: #ffffff;
}

.suggestion-top {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  align-items: center;
}

.suggestion-top strong {
  color: #101828;
  font-weight: 700;
}

.mini-warning {
  border-radius: 10px;
  background: #fffaeb;
  color: #b54708;
  padding: 8px;
  font-size: 12px;
  font-weight: 500;
}

.assigned-teacher-card {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  align-items: center;
  margin-top: 12px;
  border: 1px solid #bfdbfe;
  border-radius: 12px;
  background: #eff6ff;
  padding: 12px;
}

.assigned-teacher-card.warning {
  border-color: #fed7aa;
  background: #fff7ed;
}

.assigned-teacher-card div {
  display: grid;
  gap: 4px;
}

.assigned-teacher-card span {
  color: #475467;
  font-size: 13px;
  font-weight: 650;
}

.assigned-teacher-card strong {
  color: #101828;
  font-size: 16px;
  font-weight: 700;
}

.assigned-teacher-card small {
  color: #667085;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
  gap: 12px;
}

.schedule-form {
  margin-top: 12px;
}

.form-grid label,
.weekday-card {
  display: grid;
  gap: 6px;
}

.form-grid label span,
.weekday-card strong {
  color: #344054;
  font-weight: 650;
}

input,
select {
  width: 100%;
  border: 1px solid #d0d5dd;
  border-radius: 10px;
  background: #ffffff;
  color: #101828;
  padding: 9px 11px;
  font-size: 14px;
}

.weekday-card {
  margin-top: 12px;
  border: 1px dashed #d0d5dd;
  border-radius: 12px;
  background: #f9fafb;
  padding: 12px;
}

.weekday-row,
.actions {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  align-items: center;
}

.weekday-row label {
  display: inline-flex;
  gap: 6px;
  align-items: center;
  color: #475467;
  font-weight: 500;
}

.weekday-row input {
  width: auto;
}

.actions {
  margin-top: 12px;
}

.btn {
  border: 1px solid #d0d5dd;
  border-radius: 10px;
  background: #ffffff;
  color: #344054;
  padding: 9px 13px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
}

.btn:disabled {
  cursor: not-allowed;
  opacity: 0.65;
}

.btn:hover:not(:disabled) {
  background: #f9fafb;
}

.btn.primary {
  background: #2563eb;
  border-color: #2563eb;
  color: #ffffff;
}

.btn.primary:hover:not(:disabled) {
  background: #1d4ed8;
}

.btn.success-btn {
  background: #16a34a;
  border-color: #16a34a;
  color: #ffffff;
}

.btn.success-btn:hover:not(:disabled) {
  background: #15803d;
}

.btn.full {
  width: 100%;
}

.btn.ghost {
  margin-bottom: 4px;
}

.alert {
  padding: 10px 12px;
  border-radius: 10px;
  font-weight: 500;
}

.alert.error {
  background: #fef3f2;
  color: #b42318;
}

.alert.success {
  background: #ecfdf3;
  color: #027a48;
}

.warning-list {
  margin-top: 12px;
  display: grid;
  gap: 4px;
}

.empty-box,
.empty {
  color: #667085;
  text-align: center;
  font-weight: 500;
}

.empty-box {
  margin-top: 12px;
  border: 1px dashed #d0d5dd;
  border-radius: 12px;
  background: #f9fafb;
  padding: 14px;
}

.table-wrap {
  overflow: auto;
}

.preview-wrap {
  margin-top: 12px;
}

.data-table {
  width: 100%;
  min-width: 820px;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  border-bottom: 1px solid #eaecf0;
  padding: 10px 12px;
  text-align: left;
  vertical-align: top;
}

.data-table th {
  background: #f9fafb;
  color: #475467;
  font-size: 12px;
  text-transform: uppercase;
}

.buoi-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(230px, 1fr));
  gap: 12px;
  margin-top: 12px;
}

.buoi-card {
  border: 1px solid #eaecf0;
  border-radius: 12px;
  background: #ffffff;
  padding: 12px;
  display: grid;
  gap: 10px;
}

.buoi-card:hover {
  border-color: #bfdbfe;
  background: #f8fbff;
}

.buoi-top {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  align-items: center;
}

.buoi-top strong {
  color: #2563eb;
  font-size: 16px;
  font-weight: 700;
}

.buoi-card h3 {
  margin: 0;
  color: #101828;
  font-size: 17px;
  font-weight: 700;
}

.buoi-info {
  display: grid;
  gap: 5px;
  color: #475467;
  font-size: 13px;
}

.buoi-info b {
  color: #101828;
}

.status {
  border-radius: 999px;
  padding: 4px 8px;
  font-size: 12px;
  font-weight: 600;
  background: #f2f4f7;
  color: #475467;
}

.status.done {
  background: #ecfdf3;
  color: #027a48;
}

.status.cancel {
  background: #fef3f2;
  color: #b42318;
}

.status.makeup {
  background: #eff8ff;
  color: #175cd3;
}

.status.pending {
  background: #fffaeb;
  color: #b54708;
}

.note-card {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  color: #344054;
  font-size: 13px;
}

.note-card strong {
  color: #101828;
  font-weight: 600;
}

.note-card span {
  color: #667085;
}

@media (max-width: 1100px) {
  .class-strip {
    grid-template-columns: 1fr;
  }

  .page-head,
  .table-head {
    flex-direction: column;
  }
}

@media (max-width: 700px) {
  .class-meta span {
    width: 100%;
    border-radius: 10px;
  }
}
</style>
