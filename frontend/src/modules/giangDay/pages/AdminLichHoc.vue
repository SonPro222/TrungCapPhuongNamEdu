<template>
  <section class="schedule-page">
    <header class="page-head">
      <div>
        <p class="eyebrow">Admin đào tạo</p>
        <h1>Quản lý ma trận lịch học</h1>
        <p>Xếp lịch theo lớp/môn, giảng viên, phòng, ca học, thứ trong tuần và ngày trong tháng.</p>
      </div>

      <div class="head-actions">
        <button type="button" class="btn" @click="veTuanHienTai">Tuần này</button>
        <button type="button" class="btn primary" @click="taiTatCaDuLieu">Tải lại</button>
      </div>
    </header>

    <section class="toolbar-card">
      <div class="week-switcher">
        <button type="button" class="btn icon" @click="doiTuan(-1)">‹</button>
        <label>
          <span>Tuần bắt đầu</span>
          <input v-model="boLoc.tuNgay" type="date" @change="chonTuanTuNgay" />
        </label>
        <button type="button" class="btn icon" @click="doiTuan(1)">›</button>
      </div>

      <label>
        <span>Lớp học phần</span>
        <select v-model="boLoc.lopHocPhanId">
          <option value="">Tất cả lớp</option>
          <option v-for="lop in danhSachLopHocPhan" :key="lop.id" :value="lop.id">
            {{ hienThiLop(lop) }}
          </option>
        </select>
      </label>

      <label>
        <span>Giảng viên</span>
        <select v-model="boLoc.giaoVienId">
          <option value="">Tất cả giảng viên</option>
          <option v-for="gv in danhSachGiaoVienDangDay" :key="gv.id" :value="gv.id">
            {{ hienThiGiaoVien(gv) }}
          </option>
        </select>
      </label>

      <label>
        <span>Phòng học</span>
        <select v-model="boLoc.phongHocId">
          <option value="">Tất cả phòng</option>
          <option v-for="phong in danhSachPhongHoc" :key="phong.id" :value="phong.id">
            {{ hienThiPhong(phong) }}
          </option>
        </select>
      </label>

      <label>
        <span>Trạng thái</span>
        <select v-model="boLoc.trangThai">
          <option value="">Tất cả</option>
          <option value="du_kien">Dự kiến</option>
          <option value="da_day">Đã dạy</option>
          <option value="day_bu">Dạy bù</option>
          <option value="nghi">Nghỉ</option>
          <option value="doi_lich">Đổi lịch</option>
        </select>
      </label>

      <div class="toolbar-actions">
        <button type="button" class="btn primary" @click="locDuLieu">Lọc lịch</button>
        <button type="button" class="btn" @click="xoaLoc">Xóa lọc</button>
      </div>
    </section>

    <section class="summary-grid">
      <div>
        <span>Lịch trong tuần</span>
        <strong>{{ danhSachTheoBoLoc.length }}</strong>
      </div>
      <div>
        <span>Ngày đang xem</span>
        <strong>{{ danhSachNgayTrongTuan[0]?.label }} - {{ danhSachNgayTrongTuan[6]?.label }}</strong>
      </div>
      <div>
        <span>Phân bố số buổi</span>
        <strong>{{ soBuoiDaXepLop }}/{{ tongSoBuoiLop }} buổi</strong>
      </div>
      <div>
        <span>Còn lại</span>
        <strong>{{ soBuoiConLaiLop }} buổi</strong>
      </div>
      <div>
        <span>Quy tắc ca</span>
        <strong>Có thể chọn nhiều ca/ngày</strong>
      </div>
    </section>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="auto-scheduler-card">
      <div class="auto-head">
        <div>
          <h2>Xếp lịch học tự động</h2>
          <p>Gộp xếp 1 lớp và nhiều lớp vào một luồng. Có thể chọn thủ công giảng viên/phòng/ca hoặc để hệ thống tự tìm phương án phù hợp.</p>
        </div>
        <button type="button" class="btn" @click="layDuLieuTuBoLocChoHangLoat">Lấy theo bộ lọc</button>
      </div>

      <div class="auto-mode-grid">
        <div class="mode-group">
          <span>Phạm vi xếp lịch</span>
          <div class="segmented">
            <button type="button" :class="{ active: autoForm.phamVi === 'mot_lop' }" @click="autoForm.phamVi = 'mot_lop'">Một lớp học phần</button>
            <button type="button" :class="{ active: autoForm.phamVi === 'nhieu_lop' }" @click="autoForm.phamVi = 'nhieu_lop'">Nhiều lớp học phần</button>
          </div>
        </div>

        <div class="mode-group">
          <span>Chế độ xếp</span>
          <div class="segmented">
            <button type="button" :class="{ active: autoForm.cheDo === 'thu_cong' }" @click="autoForm.cheDo = 'thu_cong'">Thủ công</button>
            <button type="button" :class="{ active: autoForm.cheDo === 'tu_dong' }" @click="autoForm.cheDo = 'tu_dong'">Tự động</button>
          </div>
        </div>
      </div>

      <div class="auto-grid">
        <label v-if="autoForm.phamVi === 'mot_lop'">
          <span>Lớp học phần</span>
          <select v-model="autoForm.lopHocPhanId">
            <option value="">-- Chọn lớp --</option>
            <option v-for="lop in danhSachLopHocPhan" :key="lop.id" :value="lop.id">
              {{ hienThiLop(lop) }}
            </option>
          </select>
        </label>

        <div v-else class="lop-picker">
          <div class="lop-picker-head">
            <span>Chọn lớp học phần</span>
            <div>
              <button type="button" class="link-btn" @click="chonTatCaLopAuto">Chọn tất cả</button>
              <button type="button" class="link-btn" @click="autoForm.lopHocPhanIds = []">Bỏ chọn</button>
            </div>
          </div>
          <div class="lop-list">
            <label v-for="lop in danhSachLopHocPhan" :key="lop.id" class="lop-check">
              <input v-model="autoForm.lopHocPhanIds" type="checkbox" :value="lop.id" />
              <span>
                <b>{{ lop.maLop || ('LHP' + lop.id) }}</b>
                <small>{{ lop.tenLop || lop.tenLopHocPhan || 'Chưa có tên' }} · {{ lop.soBuoiHoc || 0 }} buổi · {{ lop.soLuongHienTai || 0 }}/{{ lop.soLuongToiDa || 0 }} SV</small>
              </span>
            </label>
          </div>
          <small class="hint">Đã chọn {{ soLopAutoDaChon }} lớp.</small>
        </div>

        <label>
          <span>Từ ngày</span>
          <input v-model="autoForm.tuNgay" type="date" />
        </label>

        <label>
          <span>Đến ngày</span>
          <input v-model="autoForm.denNgay" type="date" />
        </label>

        <label>
          <span>Số phương án/lớp</span>
          <input v-model.number="autoForm.soPhuongAn" type="number" min="1" max="20" />
        </label>
      </div>

      <div class="auto-config-grid">
        <div class="weekday-picker">
          <span>Thứ được phép xếp</span>
          <label v-for="thu in danhSachThuHangLoat" :key="thu.value" class="week-chip" :class="{ active: autoForm.thuTrongTuan.includes(thu.value) }">
            <input v-model="autoForm.thuTrongTuan" type="checkbox" :value="thu.value" />
            {{ thu.label }}
          </label>
        </div>

        <div class="auto-manual-box" :class="{ muted: autoForm.cheDo === 'tu_dong' }">
          <div class="manual-head">
            <span>{{ autoForm.cheDo === 'tu_dong' ? 'Ưu tiên tùy chọn' : 'Thông tin thủ công' }}</span>
            <small>{{ autoForm.cheDo === 'tu_dong' ? 'Có thể bỏ trống để hệ thống tự tìm.' : 'Chọn phòng và ca để xếp đúng cấu hình mong muốn.' }}</small>
          </div>

          <div class="manual-grid">
            <label>
              <span>Giảng viên</span>
              <select v-model="autoForm.giaoVienId">
                <option value="">-- Tự lấy giáo viên chính --</option>
                <option v-for="gv in danhSachGiaoVienDangDay" :key="gv.id" :value="gv.id">
                  {{ hienThiGiaoVien(gv) }}
                </option>
              </select>
            </label>

            <label>
              <span>Phòng học</span>
              <select v-model="autoForm.phongHocId">
                <option value="">-- Tự tìm phòng phù hợp --</option>
                <option v-for="phong in danhSachPhongHoc" :key="phong.id" :value="phong.id">
                  {{ hienThiPhong(phong) }}
                </option>
              </select>
            </label>
          </div>

          <div class="ca-picker bulk-ca-picker">
            <span>{{ autoForm.cheDo === 'tu_dong' ? 'Ca học ưu tiên' : 'Ca học' }}</span>
            <div class="ca-list">
              <label
                v-for="ca in danhSachCaHocSapXep"
                :key="ca.id"
                class="ca-chip"
                :class="{ active: autoForm.caHocIds.map(String).includes(String(ca.id)) }"
              >
                <input v-model="autoForm.caHocIds" type="checkbox" :value="ca.id" />
                <b>{{ ca.tenCa || ca.maCa || ('Ca ' + thuTuCa(ca)) }}</b>
                <small>{{ hienThiGioCa(ca) }}</small>
              </label>
            </div>
            <small class="hint">Có thể chọn nhiều ca. Mỗi ca là 1 buổi. Ở chế độ tự động có thể bỏ trống để hệ thống tự chọn ca.</small>
          </div>
        </div>
      </div>

      <div class="bulk-actions">
        <button type="button" class="btn" :disabled="dangXuLyHangLoat" @click="xepLichHangLoat(true)">Xem trước lịch</button>
        <button type="button" class="btn primary" :disabled="dangXuLyHangLoat" @click="xepLichHangLoat(false)">Xác nhận xếp lịch</button>
      </div>

      <div v-if="ketQuaHangLoat" class="bulk-result">
        <strong>{{ ketQuaHangLoat.chiPreview ? 'Kết quả xem trước' : 'Kết quả xếp lịch' }}</strong>
        <p>Thành công {{ ketQuaHangLoat.soLopThanhCong || 0 }}/{{ ketQuaHangLoat.tongLop || 0 }} lớp.</p>
        <ul>
          <li v-for="item in ketQuaHangLoat.items || []" :key="item.lopHocPhanId" :class="{ ok: item.thanhCong, fail: !item.thanhCong }">
            {{ item.maLop || item.lopHocPhanId }} - {{ item.tenLop || '' }}: {{ item.thongBao }}
          </li>
        </ul>
      </div>
    </section>

    <section ref="workspaceRef" class="workspace">
      <div class="matrix-card">
        <div class="matrix-head">
          <div>
            <h2>Ma trận lịch học tuần</h2>
            <p>Click ô trống để thêm lịch. Click thẻ lịch để sửa/xóa. Ô đỏ nhạt là có nhiều lịch cùng ca/ngày.</p>
          </div>
          <div class="legend">
            <span><i class="dot planned"></i>Dự kiến</span>
            <span><i class="dot done"></i>Đã dạy</span>
            <span><i class="dot warning"></i>Đổi/nghỉ</span>
          </div>
        </div>

        <div class="matrix-wrap">
          <div class="matrix-grid" :style="matrixGridStyle">
            <div class="matrix-corner">Ca / Ngày</div>

            <div v-for="ngay in danhSachNgayTrongTuan" :key="ngay.iso" class="matrix-day">
              <strong>{{ ngay.tenThu }}</strong>
              <span>{{ ngay.label }}</span>
            </div>

            <template v-for="ca in danhSachCaHocSapXep" :key="ca.id">
              <div class="matrix-slot">
                <strong>{{ ca.tenCa || ca.maCa || ('Ca ' + thuTuCa(ca)) }}</strong>
                <span>{{ hienThiGioCa(ca) }}</span>
              </div>

              <div
                v-for="ngay in danhSachNgayTrongTuan"
                :key="`${ca.id}-${ngay.iso}`"
                class="matrix-cell"
                :class="{
                  conflict: lichTheoNgayVaCa(ngay.iso, ca.id).length > 1,
                  selected: oDangDuocChon(ngay.iso, ca.id)
                }"
                @click="moThemTheoO(ngay.iso, ca.id)"
              >
                <button
                  v-if="!lichTheoNgayVaCa(ngay.iso, ca.id).length"
                  type="button"
                  class="empty-cell"
                  :class="{ selected: oDangDuocChon(ngay.iso, ca.id) }"
                >
                  {{ oDangDuocChon(ngay.iso, ca.id) ? '✓ Đang chọn' : '+ Thêm lịch' }}
                </button>

                <article
                  v-for="item in lichTheoNgayVaCa(ngay.iso, ca.id)"
                  :key="item.id"
                  class="schedule-card"
                  :class="trangThaiClass(item.trangThai)"
                  @click.stop="chonSua(item)"
                >
                  <div class="card-title">{{ item.tenLop || item.maLop || ('Lớp ' + item.lopHocPhanId) }}</div>
                  <div class="card-line">GV: {{ item.tenGiaoVien || item.maGiaoVien || item.giaoVienId || '-' }}</div>
                  <div class="card-line">Phòng: {{ item.tenPhong || item.maPhong || item.phongHocId || '-' }}</div>
                  <div class="card-status">{{ hienThiTrangThai(item.trangThai) }}</div>
                </article>
              </div>
            </template>
          </div>
        </div>
      </div>

      <aside class="form-card">
        <div class="form-head">
          <div>
            <h2>{{ form.id ? 'Cập nhật lịch học' : 'Thêm lịch học' }}</h2>
            <p>{{ form.id ? 'Đang sửa từng buổi học một ca.' : 'Có thể chọn nhiều ca học trong cùng một ngày.' }}</p>
          </div>
          <button v-if="form.id" type="button" class="btn danger small" @click="xoaLichHocDangSua">Xóa</button>
        </div>

        <form class="form-grid" @submit.prevent="luuLichHoc">
          <label>
            <span>Lớp học phần</span>
            <select v-model="form.lopHocPhanId" required>
              <option value="">-- Chọn lớp học phần --</option>
              <option v-for="lop in danhSachLopHocPhan" :key="lop.id" :value="lop.id">
                {{ hienThiLop(lop) }}
              </option>
            </select>
          </label>

          <div v-if="lopHocPhanDangChon" class="progress-box">
            <div>
              <span>Tổng số buổi cần xếp</span>
              <strong>{{ tongSoBuoiLop }}</strong>
            </div>
            <div>
              <span>Đã xếp</span>
              <strong>{{ soBuoiDaXepLop }}</strong>
            </div>
            <div>
              <span>Còn lại</span>
              <strong>{{ soBuoiConLaiLop }}</strong>
            </div>
            <small>Lấy từ lớp học phần. Mỗi ca học được tính là 1 buổi.</small>
          </div>

          <label>
            <span>Giảng viên</span>
            <select v-model="form.giaoVienId" required>
              <option value="">-- Chọn giảng viên --</option>
              <option v-for="gv in danhSachGiaoVienDangDay" :key="gv.id" :value="gv.id">
                {{ hienThiGiaoVien(gv) }}
              </option>
            </select>
          </label>

          <label>
            <span>Phòng học</span>
            <select v-model="form.phongHocId" required>
              <option value="">-- Chọn phòng --</option>
              <option v-for="phong in danhSachPhongPhuHop" :key="phong.id" :value="phong.id">
                {{ hienThiPhong(phong) }}
              </option>
            </select>
          </label>

          <label>
            <span>Ngày học</span>
            <input v-model="form.ngayHoc" type="date" required />
            <small v-if="form.ngayHoc">{{ hienThiThuNgay(form.ngayHoc) }}</small>
          </label>

          <div class="ca-picker">
            <span>Ca học</span>
            <div class="ca-list">
              <label
                v-for="ca in danhSachCaHocSapXep"
                :key="ca.id"
                class="ca-chip"
                :class="{ active: form.caHocIds.map(String).includes(String(ca.id)) }"
                @click.prevent="chonCaTrongForm(ca.id)"
              >
                <input
                  :checked="form.caHocIds.map(String).includes(String(ca.id))"
                  type="checkbox"
                  :value="ca.id"
                  readonly
                />
                <b>{{ ca.tenCa || ca.maCa || ('Ca ' + thuTuCa(ca)) }}</b>
                <small>{{ hienThiGioCa(ca) }}</small>
              </label>
            </div>
            <small class="hint">Thêm mới: có thể chọn nhiều ca, mỗi ca tạo một buổi học riêng. Cập nhật: bấm ca khác để đổi ca của buổi học đang sửa.</small>
          </div>

          <label>
            <span>Trạng thái</span>
            <select v-model="form.trangThai" required>
              <option value="du_kien">Dự kiến</option>
              <option value="da_day">Đã dạy</option>
              <option value="nghi">Nghỉ</option>
              <option value="day_bu">Dạy bù</option>
              <option value="doi_lich">Đổi lịch</option>
            </select>
          </label>

          <label>
            <span>Nội dung buổi học</span>
            <textarea v-model.trim="form.noiDungBuoiHoc" rows="2" placeholder="Ví dụ: Lý thuyết chương 1" />
          </label>

          <label>
            <span>Ghi chú</span>
            <textarea v-model.trim="form.ghiChu" rows="2" placeholder="Ghi chú nếu có" />
          </label>

          <div v-if="canhBaoForm.length" class="warning-box">
            <strong>Cảnh báo trước khi lưu</strong>
            <ul>
              <li v-for="msg in canhBaoForm" :key="msg">{{ msg }}</li>
            </ul>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn primary" :disabled="dangXuLy">
              {{ form.id ? 'Cập nhật lịch' : 'Thêm lịch' }}
            </button>
            <button type="button" class="btn" @click="resetForm">Làm mới</button>
          </div>
        </form>
      </aside>
    </section>
  </section>
</template>

<script setup>
import { computed, nextTick, onMounted, reactive, ref } from 'vue'
import { giangDayService } from '../services/giangDayService'

const danhSach = ref([])
const danhSachLopHocPhan = ref([])
const danhSachGiaoVien = ref([])
const danhSachPhongHoc = ref([])
const danhSachCaHoc = ref([])
const dangTai = ref(false)
const dangXuLy = ref(false)
const dangXuLyHangLoat = ref(false)
const thongKeSoBuoiTheoLop = ref({})
const loi = ref('')
const thongBao = ref('')
const ketQuaHangLoat = ref(null)
const workspaceRef = ref(null)

const boLoc = reactive({
  lopHocPhanId: '',
  giaoVienId: '',
  phongHocId: '',
  trangThai: '',
  tuNgay: dauTuanIso(new Date()),
  denNgay: congNgayIso(dauTuanIso(new Date()), 6)
})

const form = reactive({
  id: null,
  lopHocPhanId: '',
  giaoVienId: '',
  phongHocId: '',
  caHocIds: [],
  ngayHoc: '',
  noiDungBuoiHoc: '',
  trangThai: 'du_kien',
  ghiChu: ''
})

const autoForm = reactive({
  phamVi: 'mot_lop',
  cheDo: 'thu_cong',
  lopHocPhanId: '',
  lopHocPhanIds: [],
  giaoVienId: '',
  phongHocId: '',
  caHocIds: [],
  tuNgay: dauTuanIso(new Date()),
  denNgay: congNgayIso(dauTuanIso(new Date()), 35),
  thuTrongTuan: [2, 4],
  soPhuongAn: 5
})

const danhSachThuHangLoat = [
  { value: 2, label: 'Thứ 2' },
  { value: 3, label: 'Thứ 3' },
  { value: 4, label: 'Thứ 4' },
  { value: 5, label: 'Thứ 5' },
  { value: 6, label: 'Thứ 6' },
  { value: 7, label: 'Thứ 7' },
  { value: 8, label: 'CN' }
]

const danhSachNgayTrongTuan = computed(() => {
  return Array.from({ length: 7 }, (_, index) => {
    const iso = congNgayIso(boLoc.tuNgay, index)
    return {
      iso,
      tenThu: tenThuTheoIso(iso),
      label: dinhDangNgayNgan(iso)
    }
  })
})

const danhSachCaHocSapXep = computed(() => {
  return [...danhSachCaHoc.value].sort((a, b) => thuTuCa(a) - thuTuCa(b))
})

const matrixGridStyle = computed(() => ({
  gridTemplateColumns: `130px repeat(${danhSachNgayTrongTuan.value.length}, minmax(145px, 1fr))`
}))

const danhSachGiaoVienDangDay = computed(() => {
  return danhSachGiaoVien.value.filter((gv) => !gv.trangThai || gv.trangThai === 'dang_day')
})

const lopHocPhanDangChon = computed(() => {
  return danhSachLopHocPhan.value.find((lop) => String(lop.id) === String(form.lopHocPhanId))
})

const danhSachPhongPhuHop = computed(() => {
  const lop = lopHocPhanDangChon.value
  const siSo = Number(lop?.soLuongHienTai || lop?.soLuongToiDa || 0)
  return danhSachPhongHoc.value
    .filter((phong) => !phong.trangThai || phong.trangThai === 'dang_su_dung')
    .filter((phong) => !siSo || Number(phong.sucChua || 0) >= siSo)
    .sort((a, b) => Number(a.sucChua || 0) - Number(b.sucChua || 0))
})

const lopIdDangTinh = computed(() => form.lopHocPhanId || boLoc.lopHocPhanId || '')

const tongSoBuoiLop = computed(() => {
  const lop = danhSachLopHocPhan.value.find((item) => String(item.id) === String(lopIdDangTinh.value))
  return Number(lop?.soBuoiHoc || 0)
})

const soBuoiDaXepLop = computed(() => {
  if (!lopIdDangTinh.value) return 0
  return Number(thongKeSoBuoiTheoLop.value[String(lopIdDangTinh.value)] || 0)
})

const soBuoiConLaiLop = computed(() => {
  return Math.max(tongSoBuoiLop.value - soBuoiDaXepLop.value, 0)
})

const soBuoiDangChon = computed(() => form.caHocIds.length)

const danhSachLopAutoDangChon = computed(() => {
  if (autoForm.phamVi === 'mot_lop') {
    return autoForm.lopHocPhanId ? [Number(autoForm.lopHocPhanId)] : []
  }
  return autoForm.lopHocPhanIds.map(Number)
})

const soLopAutoDaChon = computed(() => danhSachLopAutoDangChon.value.length)

const danhSachTheoBoLoc = computed(() => {
  return danhSach.value.filter((item) => {
    const dungLop = !boLoc.lopHocPhanId || String(item.lopHocPhanId) === String(boLoc.lopHocPhanId)
    const dungGv = !boLoc.giaoVienId || String(item.giaoVienId) === String(boLoc.giaoVienId)
    const dungPhong = !boLoc.phongHocId || String(item.phongHocId) === String(boLoc.phongHocId)
    const dungTrangThai = !boLoc.trangThai || item.trangThai === boLoc.trangThai
    return dungLop && dungGv && dungPhong && dungTrangThai
  })
})

const canhBaoForm = computed(() => {
  const messages = []
  if (!form.ngayHoc || !form.caHocIds.length) return messages

  const caIds = form.caHocIds.map(String)
  const trungGv = danhSach.value.some((item) =>
    String(item.id) !== String(form.id) &&
    String(item.giaoVienId) === String(form.giaoVienId) &&
    item.ngayHoc === form.ngayHoc &&
    caIds.includes(String(item.caHocId))
  )
  const trungPhong = danhSach.value.some((item) =>
    String(item.id) !== String(form.id) &&
    String(item.phongHocId) === String(form.phongHocId) &&
    item.ngayHoc === form.ngayHoc &&
    caIds.includes(String(item.caHocId))
  )
  const trungLop = danhSach.value.some((item) =>
    String(item.id) !== String(form.id) &&
    String(item.lopHocPhanId) === String(form.lopHocPhanId) &&
    item.ngayHoc === form.ngayHoc &&
    caIds.includes(String(item.caHocId))
  )

  if (trungGv) messages.push('Giảng viên đang có lịch trong một ca đã chọn.')
  if (trungPhong) messages.push('Phòng học đang được sử dụng trong một ca đã chọn.')
  if (trungLop) messages.push('Lớp học phần đang có lịch trong một ca đã chọn.')

  if (!form.id && tongSoBuoiLop.value && soBuoiDangChon.value > soBuoiConLaiLop.value) {
    messages.push(`Lớp học phần chỉ còn ${soBuoiConLaiLop.value} buổi chưa xếp.`)
  }

  return messages
})

onMounted(taiTatCaDuLieu)

async function taiTatCaDuLieu() {
  dangTai.value = true
  loi.value = ''
  thongBao.value = ''
  try {
    const [lopHocPhan, giaoVien, phongHoc, caHoc] = await Promise.all([
      giangDayService.layDanhSachLopHocPhan(),
      giangDayService.layDanhSachGiaoVien(),
      giangDayService.layDanhSachPhongHoc(),
      giangDayService.layDanhSachCaHoc()
    ])
    danhSachLopHocPhan.value = lopHocPhan || []
    danhSachGiaoVien.value = giaoVien || []
    danhSachPhongHoc.value = phongHoc || []
    danhSachCaHoc.value = caHoc || []
    await taiThongKeSoBuoi()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Không tải được dữ liệu lịch học'
  } finally {
    dangTai.value = false
  }
}

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  try {
    const page = await giangDayService.layTrangLichHocAdmin({
      page: 0,
      size: 1000,
      tuNgay: boLoc.tuNgay,
      denNgay: boLoc.denNgay,
      sortBy: 'ngayHoc',
      sortDir: 'asc'
    })
    danhSach.value = page.content || []
  } catch (error) {
    loi.value = error?.message || 'Không tải được ma trận lịch học'
  } finally {
    dangTai.value = false
  }
}

async function taiThongKeSoBuoi() {
  try {
    const page = await giangDayService.layTrangLichHocAdmin({
      page: 0,
      size: 5000,
      sortBy: 'ngayHoc',
      sortDir: 'asc'
    })

    const thongKe = {}
    ;(page.content || []).forEach((item) => {
      if (!item.lopHocPhanId || item.trangThai === 'nghi') return
      const key = String(item.lopHocPhanId)
      thongKe[key] = Number(thongKe[key] || 0) + 1
    })

    thongKeSoBuoiTheoLop.value = thongKe
  } catch (error) {
    // Không chặn màn hình nếu thống kê số buổi chưa tải được. Backend vẫn kiểm tra khi lưu.
    thongKeSoBuoiTheoLop.value = {}
  }
}

function locDuLieu() {
  taiDuLieu()
}

function xoaLoc() {
  boLoc.lopHocPhanId = ''
  boLoc.giaoVienId = ''
  boLoc.phongHocId = ''
  boLoc.trangThai = ''
  taiDuLieu()
}

function chonTuanTuNgay() {
  boLoc.tuNgay = dauTuanIso(boLoc.tuNgay ? new Date(`${boLoc.tuNgay}T00:00:00`) : new Date())
  boLoc.denNgay = congNgayIso(boLoc.tuNgay, 6)
  taiDuLieu()
}

function doiTuan(delta) {
  boLoc.tuNgay = congNgayIso(boLoc.tuNgay, delta * 7)
  boLoc.denNgay = congNgayIso(boLoc.tuNgay, 6)
  taiDuLieu()
}

function veTuanHienTai() {
  boLoc.tuNgay = dauTuanIso(new Date())
  boLoc.denNgay = congNgayIso(boLoc.tuNgay, 6)
  taiDuLieu()
}

function lichTheoNgayVaCa(ngayHoc, caHocId) {
  return danhSachTheoBoLoc.value.filter((item) =>
    item.ngayHoc === ngayHoc && String(item.caHocId) === String(caHocId)
  )
}

function oDangDuocChon(ngayHoc, caHocId) {
  return form.ngayHoc === ngayHoc && form.caHocIds.map(String).includes(String(caHocId))
}

function chonCaTrongForm(caHocId) {
  const id = Number(caHocId)
  if (!id) return

  if (form.id) {
    form.caHocIds = [id]
    return
  }

  const ids = form.caHocIds.map(Number)
  form.caHocIds = ids.includes(id)
    ? ids.filter((item) => item !== id)
    : [...ids, id]
}

function moThemTheoO(ngayHoc, caHocId) {
  if (form.id) resetForm()
  form.ngayHoc = ngayHoc
  form.caHocIds = [caHocId]
  if (boLoc.lopHocPhanId) form.lopHocPhanId = boLoc.lopHocPhanId
  if (boLoc.giaoVienId) form.giaoVienId = boLoc.giaoVienId
  if (boLoc.phongHocId) form.phongHocId = boLoc.phongHocId
  thongBao.value = 'Đã chọn ô lịch, hãy nhập đủ thông tin rồi bấm Thêm lịch.'
}


function layDuLieuTuBoLocChoHangLoat() {
  if (boLoc.lopHocPhanId) {
    autoForm.phamVi = 'mot_lop'
    autoForm.lopHocPhanId = boLoc.lopHocPhanId
  }
  if (boLoc.giaoVienId) autoForm.giaoVienId = boLoc.giaoVienId
  if (boLoc.phongHocId) autoForm.phongHocId = boLoc.phongHocId
  autoForm.tuNgay = boLoc.tuNgay
  autoForm.denNgay = congNgayIso(boLoc.tuNgay, 35)
}

function chonTatCaLopAuto() {
  autoForm.lopHocPhanIds = danhSachLopHocPhan.value.map((lop) => lop.id)
}

async function xepLichHangLoat(chiPreview) {
  loi.value = ''
  thongBao.value = ''
  ketQuaHangLoat.value = null

  const lopHocPhanIds = danhSachLopAutoDangChon.value
  if (!lopHocPhanIds.length) {
    loi.value = autoForm.phamVi === 'mot_lop'
      ? 'Vui lòng chọn lớp học phần để xếp lịch'
      : 'Vui lòng chọn ít nhất một lớp học phần để xếp lịch'
    return
  }
  if (!autoForm.tuNgay || !autoForm.denNgay) {
    loi.value = 'Vui lòng chọn từ ngày và đến ngày'
    return
  }
  if (autoForm.tuNgay > autoForm.denNgay) {
    loi.value = 'Từ ngày không được sau đến ngày'
    return
  }
  if (!autoForm.thuTrongTuan.length) {
    loi.value = 'Vui lòng chọn ít nhất một thứ trong tuần'
    return
  }

  const caHocIds = autoForm.caHocIds.map(Number)
  const cheDoThuCong = autoForm.cheDo === 'thu_cong'
  if (cheDoThuCong && !autoForm.phongHocId) {
    loi.value = 'Chế độ thủ công cần chọn phòng học'
    return
  }
  if (cheDoThuCong) {
    const loiCa = kiemTraCaHocDaChon(caHocIds)
    if (loiCa) {
      loi.value = loiCa
      return
    }
  }

  dangXuLyHangLoat.value = true
  try {
    const payload = {
      lopHocPhanIds,
      giaoVienId: autoForm.giaoVienId ? Number(autoForm.giaoVienId) : null,
      phongHocId: autoForm.phongHocId ? Number(autoForm.phongHocId) : null,
      caHocId: caHocIds.length === 1 ? caHocIds[0] : null,
      caHocIds: caHocIds.length ? caHocIds : null,
      tuNgay: autoForm.tuNgay,
      denNgay: autoForm.denNgay,
      thuTrongTuan: autoForm.thuTrongTuan.map(Number),
      tuDongChonPhong: autoForm.cheDo === 'tu_dong' || !autoForm.phongHocId,
      tuDongChonCa: autoForm.cheDo === 'tu_dong' && caHocIds.length === 0,
      soPhuongAn: Number(autoForm.soPhuongAn || 5),
      chiPreview,
      tuDongGanNoiDungSyllabus: true
    }

    const result = await giangDayService.xepLichHangLoat(lopHocPhanIds[0], payload)
    ketQuaHangLoat.value = result

    const items = result?.items || []
    const coThanhCong = items.some((item) => item.thanhCong)
    const loiDauTien = items.find((item) => !item.thanhCong)?.thongBao

    if (chiPreview) {
      thongBao.value = coThanhCong
        ? 'Đã xem trước lịch. Bấm Xác nhận xếp lịch để lưu vào ma trận.'
        : (loiDauTien || 'Không có lớp nào có thể xếp lịch với cấu hình hiện tại')
      return
    }

    if (!coThanhCong) {
      loi.value = loiDauTien || 'Xếp lịch thất bại, ma trận được giữ nguyên.'
      return
    }

    // Khong tu doi bo loc sau khi xep tu dong.
    // Lich cu tren ma tran phai duoc giu nguyen; lich moi chi duoc bo sung neu nam trong tuan/bo loc hien tai.
    thongBao.value = 'Đã xếp lịch thành công. Hệ thống chỉ bổ sung lịch còn thiếu, không xóa hoặc ghi đè lịch cũ.'

    const lichDaTao = items.flatMap((item) => item.lichDaTao || [])
    if (lichDaTao.length) {
      const idsMoi = new Set(lichDaTao.map((item) => String(item.id)))
      danhSach.value = [
        ...danhSach.value.filter((item) => !idsMoi.has(String(item.id))),
        ...lichDaTao
      ]
    }

    await taiThongKeSoBuoi()
    await taiDuLieu()
    await nextTick()
    workspaceRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  } catch (error) {
    loi.value = error?.message || 'Xếp lịch thất bại'
  } finally {
    dangXuLyHangLoat.value = false
  }
}

async function luuLichHoc() {
  loi.value = ''
  thongBao.value = ''

  const loiCa = kiemTraCaHocDaChon(form.caHocIds)
  if (loiCa) {
    loi.value = loiCa
    return
  }

  if (!form.id && tongSoBuoiLop.value && soBuoiDangChon.value > soBuoiConLaiLop.value) {
    loi.value = `Lớp học phần chỉ còn ${soBuoiConLaiLop.value} buổi chưa xếp, không thể xếp ${soBuoiDangChon.value} buổi.`
    return
  }

  dangXuLy.value = true
  try {
    const payload = {
      lopHocPhanId: Number(form.lopHocPhanId),
      giaoVienId: Number(form.giaoVienId),
      phongHocId: Number(form.phongHocId),
      caHocId: form.caHocIds.length === 1 ? Number(form.caHocIds[0]) : null,
      caHocIds: form.caHocIds.map(Number),
      thuTrongTuan: tinhThuTrongTuan(form.ngayHoc),
      ngayHoc: form.ngayHoc,
      noiDungBuoiHoc: form.noiDungBuoiHoc || null,
      trangThai: form.trangThai,
      ghiChu: form.ghiChu || null
    }

    if (form.id) {
      await giangDayService.capNhatLichHoc(form.id, payload)
      thongBao.value = 'Cập nhật lịch học thành công'
    } else {
      await giangDayService.taoLichHoc(payload)
      thongBao.value = 'Thêm lịch học thành công'
    }

    resetForm()
    await taiThongKeSoBuoi()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Lưu lịch học thất bại'
  } finally {
    dangXuLy.value = false
  }
}

function chonSua(item) {
  form.id = item.id
  form.lopHocPhanId = item.lopHocPhanId || ''
  form.giaoVienId = item.giaoVienId || ''
  form.phongHocId = item.phongHocId || ''
  form.caHocIds = item.caHocId ? [item.caHocId] : []
  form.ngayHoc = item.ngayHoc || ''
  form.noiDungBuoiHoc = item.noiDungBuoiHoc || ''
  form.trangThai = item.trangThai || 'du_kien'
  form.ghiChu = item.ghiChu || ''
}

async function xoaLichHocDangSua() {
  const item = danhSach.value.find((row) => String(row.id) === String(form.id))
  if (!item) return
  await xoaLichHoc(item)
}

async function xoaLichHoc(item) {
  const ok = window.confirm(`Xóa lịch học ${item.tenLop || item.maLop || item.lopHocPhanId} ngày ${item.ngayHoc}?`)
  if (!ok) return
  try {
    await giangDayService.xoaLichHoc(item.id)
    thongBao.value = 'Xóa lịch học thành công'
    resetForm()
    await taiThongKeSoBuoi()
    await taiDuLieu()
  } catch (error) {
    loi.value = error?.message || 'Xóa lịch học thất bại'
  }
}

function resetForm() {
  form.id = null
  form.lopHocPhanId = ''
  form.giaoVienId = ''
  form.phongHocId = ''
  form.caHocIds = []
  form.ngayHoc = ''
  form.noiDungBuoiHoc = ''
  form.trangThai = 'du_kien'
  form.ghiChu = ''
}

function kiemTraCaHocDaChon(caHocIds) {
  if (!caHocIds || caHocIds.length === 0) return 'Vui lòng chọn ít nhất một ca học'
  return ''
}

function hienThiLop(lop) {
  return `${lop.maLop || 'LHP' + lop.id} - ${lop.tenLop || lop.tenLopHocPhan || 'Chưa có tên'}`
}

function hienThiGiaoVien(gv) {
  return `${gv.maGiaoVien || 'GV' + gv.id} - ${gv.hoTen || gv.tenGiaoVien || 'Chưa có tên'}`
}

function hienThiPhong(phong) {
  const sucChua = phong.sucChua ? ` | ${phong.sucChua} chỗ` : ''
  return `${phong.maPhong || 'P' + phong.id} - ${phong.tenPhong || 'Chưa có tên'}${sucChua}`
}

function hienThiTrangThai(value) {
  return { du_kien: 'Dự kiến', da_day: 'Đã dạy', nghi: 'Nghỉ', day_bu: 'Dạy bù', doi_lich: 'Đổi lịch' }[value] || value || '-'
}

function trangThaiClass(value) {
  if (value === 'da_day') return 'done'
  if (value === 'nghi' || value === 'doi_lich' || value === 'day_bu') return 'warning'
  return 'planned'
}

function thuTuCa(ca) {
  const text = `${ca?.maCa || ''} ${ca?.tenCa || ''}`
  const digits = text.replace(/\D+/g, '')
  return digits ? Number(digits) : Number(ca?.thuTu || ca?.id || 0)
}

function hienThiGioCa(item) {
  if (!item?.gioBatDau && !item?.gioKetThuc) return ''
  return `${item.gioBatDau || '?'} - ${item.gioKetThuc || '?'}`
}

function tinhThuTrongTuan(ngayHoc) {
  if (!ngayHoc) return null
  const day = new Date(`${ngayHoc}T00:00:00`).getDay()
  return day === 0 ? 8 : day + 1
}

function tenThuTheoIso(iso) {
  const thu = tinhThuTrongTuan(iso)
  return thu === 8 ? 'Chủ nhật' : `Thứ ${thu}`
}

function hienThiThuNgay(ngayHoc) {
  return `${tenThuTheoIso(ngayHoc)}, ngày ${new Date(`${ngayHoc}T00:00:00`).getDate()} trong tháng`
}

function dinhDangNgayNgan(iso) {
  const date = new Date(`${iso}T00:00:00`)
  return `${String(date.getDate()).padStart(2, '0')}/${String(date.getMonth() + 1).padStart(2, '0')}`
}

function formatIso(date) {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function dauTuanIso(date) {
  const current = new Date(date)
  const day = current.getDay()
  const diff = day === 0 ? -6 : 1 - day
  current.setDate(current.getDate() + diff)
  return formatIso(current)
}

function congNgayIso(iso, days) {
  const date = new Date(`${iso}T00:00:00`)
  date.setDate(date.getDate() + days)
  return formatIso(date)
}
</script>

<style scoped>
.schedule-page { display: flex; flex-direction: column; gap: 16px; width: 100%; font-size: 14px; }
.page-head { display: flex; justify-content: space-between; gap: 16px; align-items: flex-start; }
.eyebrow { margin: 0 0 4px; color: #2563eb; font-weight: 800; text-transform: uppercase; letter-spacing: .04em; font-size: 12px; }
.page-head h1, .matrix-head h2, .form-head h2 { margin: 0; color: #0f172a; }
.page-head p, .matrix-head p, .form-head p { margin: 6px 0 0; color: #64748b; }
.head-actions, .toolbar-actions, .form-actions, .legend, .week-switcher { display: flex; gap: 8px; align-items: center; }
.toolbar-card, .matrix-card, .form-card, .summary-grid > div { border: 1px solid var(--color-border, #e2e8f0); background: var(--color-white, #fff); border-radius: var(--radius, 14px); }
.toolbar-card { padding: 14px; display: grid; grid-template-columns: 1.4fr repeat(4, minmax(150px, 1fr)) auto; gap: 12px; align-items: end; }
label { display: flex; flex-direction: column; gap: 6px; }
label span, .ca-picker > span { font-size: 13px; font-weight: 800; color: #334155; }
input, select, textarea { border: 1px solid var(--color-border, #cbd5e1); border-radius: 10px; padding: 9px 10px; font: inherit; min-width: 0; }
textarea { resize: vertical; }
.btn { border: 1px solid var(--color-border, #cbd5e1); background: #fff; color: #0f172a; border-radius: 10px; padding: 9px 12px; cursor: pointer; font-weight: 800; }
.btn.primary { background: var(--color-primary, #2563eb); border-color: var(--color-primary, #2563eb); color: white; }
.btn.danger { color: #b91c1c; border-color: #fecaca; background: #fff5f5; }
.btn.small { padding: 7px 10px; font-size: 12px; }
.btn.icon { width: 38px; height: 38px; padding: 0; font-size: 22px; }
.btn:disabled { opacity: .6; cursor: not-allowed; }
.summary-grid { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 12px; }
.summary-grid > div { padding: 14px; display: grid; gap: 6px; }
.summary-grid span { color: #64748b; font-size: 13px; font-weight: 800; }
.summary-grid strong { color: #0f172a; font-size: 20px; }
.alert { padding: 11px 13px; border-radius: 12px; font-weight: 700; }
.alert.success { background: #ecfdf5; color: #047857; }
.alert.error { background: #fee2e2; color: #991b1b; }
.workspace { display: grid; grid-template-columns: minmax(0, 1fr) minmax(360px, 420px); gap: 16px; align-items: start; }
.matrix-card { min-width: 0; overflow: hidden; }
.matrix-head { padding: 16px; display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; border-bottom: 1px solid #e2e8f0; }
.legend span { color: #475569; font-weight: 700; font-size: 12px; }
.dot { display: inline-block; width: 10px; height: 10px; border-radius: 999px; margin-right: 5px; }
.dot.planned { background: #2563eb; }
.dot.done { background: #059669; }
.dot.warning { background: #f59e0b; }
.matrix-wrap { overflow: auto; }
.matrix-grid { display: grid; min-width: 1100px; }
.matrix-corner, .matrix-day, .matrix-slot, .matrix-cell { border-right: 1px solid #e2e8f0; border-bottom: 1px solid #e2e8f0; }
.matrix-corner, .matrix-day { background: #f8fafc; padding: 12px; position: sticky; top: 0; z-index: 2; }
.matrix-corner { left: 0; z-index: 3; font-weight: 900; color: #475569; }
.matrix-day { display: grid; gap: 3px; }
.matrix-day strong { color: #0f172a; }
.matrix-day span { color: #64748b; font-size: 12px; }
.matrix-slot { background: #f8fafc; padding: 12px; position: sticky; left: 0; z-index: 1; display: grid; gap: 3px; align-content: start; }
.matrix-slot strong { color: #0f172a; }
.matrix-slot span { color: #64748b; font-size: 12px; }
.matrix-cell { min-height: 108px; padding: 8px; background: #fff; display: grid; gap: 8px; align-content: start; cursor: pointer; }
.matrix-cell:hover { background: #f8fbff; }
.matrix-cell.selected { background: #dbeafe; box-shadow: inset 0 0 0 2px #2563eb; }
.matrix-cell.selected:hover { background: #bfdbfe; }
.matrix-cell.conflict { background: #fff7ed; box-shadow: inset 0 0 0 2px #fdba74; }
.empty-cell { width: 100%; height: 100%; min-height: 68px; border: 1px dashed #cbd5e1; background: #f8fafc; color: #64748b; border-radius: 12px; cursor: pointer; font-weight: 800; transition: .16s ease; }
.empty-cell.selected { border-color: #2563eb; background: #2563eb; color: #fff; box-shadow: 0 10px 22px rgba(37, 99, 235, .25); }
.schedule-card { border-left: 4px solid #2563eb; border-radius: 12px; padding: 9px; background: #eff6ff; box-shadow: 0 8px 18px rgba(15, 23, 42, .06); display: grid; gap: 4px; }
.schedule-card.done { border-left-color: #059669; background: #ecfdf5; }
.schedule-card.warning { border-left-color: #f59e0b; background: #fffbeb; }
.card-title { color: #0f172a; font-weight: 900; }
.card-line { color: #475569; font-size: 12px; }
.card-status { margin-top: 2px; color: #334155; font-size: 11px; font-weight: 900; text-transform: uppercase; }
.form-card { padding: 16px; position: sticky; top: 12px; }
.form-head { display: flex; justify-content: space-between; gap: 10px; margin-bottom: 12px; }
.form-grid { display: grid; gap: 12px; }
.ca-picker { display: grid; gap: 8px; }
.ca-list { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px; }
.ca-chip { border: 1px solid #cbd5e1; border-radius: 12px; padding: 9px; cursor: pointer; background: #fff; display: grid; gap: 3px; }
.ca-chip input { display: none; }
.ca-chip.active { border-color: #2563eb; background: #eff6ff; color: #1d4ed8; }
.ca-chip.disabled { opacity: .55; cursor: not-allowed; }
.ca-chip small, .hint { color: #64748b; font-size: 12px; }
.warning-box { border: 1px solid #fdba74; background: #fff7ed; color: #9a3412; border-radius: 12px; padding: 10px 12px; }
.warning-box ul { margin: 6px 0 0; padding-left: 18px; }

.bulk-card { border: 1px solid var(--color-border, #e2e8f0); background: #fff; border-radius: var(--radius, 14px); padding: 16px; display: grid; gap: 14px; }
.bulk-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
.bulk-head h2 { margin: 0; color: #0f172a; }
.bulk-head p { margin: 6px 0 0; color: #64748b; }
.bulk-grid { display: grid; grid-template-columns: repeat(5, minmax(0, 1fr)); gap: 12px; }
.bulk-options { display: grid; grid-template-columns: minmax(260px, .8fr) minmax(0, 1.2fr); gap: 14px; align-items: start; }
.weekday-picker { display: flex; flex-wrap: wrap; gap: 8px; align-content: start; }
.weekday-picker > span { width: 100%; font-size: 13px; font-weight: 800; color: #334155; }
.week-chip { border: 1px solid #cbd5e1; border-radius: 999px; padding: 8px 11px; cursor: pointer; background: #fff; display: inline-flex; align-items: center; gap: 5px; font-weight: 800; color: #334155; }
.week-chip input { display: none; }
.week-chip.active { border-color: #2563eb; background: #eff6ff; color: #1d4ed8; }
.bulk-ca-picker .ca-list { grid-template-columns: repeat(4, minmax(0, 1fr)); }
.form-card { max-height: calc(100vh - 24px); overflow: auto; }
.bulk-actions { display: flex; gap: 8px; justify-content: flex-end; }
.bulk-result { border: 1px solid #bfdbfe; background: #eff6ff; border-radius: 12px; padding: 12px; color: #1e3a8a; }
.bulk-result p { margin: 4px 0 8px; }
.bulk-result ul { margin: 0; padding-left: 18px; }
.bulk-result li.ok { color: #047857; }
.bulk-result li.fail { color: #b91c1c; }

.auto-scheduler-card { border: 1px solid var(--color-border, #e2e8f0); background: #fff; border-radius: var(--radius, 14px); padding: 16px; display: grid; gap: 14px; }
.auto-head { display: flex; justify-content: space-between; gap: 12px; align-items: flex-start; }
.auto-head h2 { margin: 0; color: #0f172a; }
.auto-head p { margin: 6px 0 0; color: #64748b; }
.auto-mode-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }
.mode-group { display: grid; gap: 8px; }
.mode-group > span { font-size: 13px; font-weight: 800; color: #334155; }
.segmented { display: inline-flex; border: 1px solid #cbd5e1; border-radius: 12px; overflow: hidden; background: #fff; width: fit-content; }
.segmented button { border: 0; background: #fff; padding: 9px 14px; cursor: pointer; font-weight: 800; color: #475569; }
.segmented button.active { background: #2563eb; color: #fff; }
.auto-grid { display: grid; grid-template-columns: minmax(280px, 1.4fr) repeat(3, minmax(150px, .7fr)); gap: 12px; align-items: start; }
.auto-config-grid { display: grid; grid-template-columns: minmax(250px, .7fr) minmax(0, 1.3fr); gap: 14px; align-items: start; }
.lop-picker { border: 1px dashed #cbd5e1; border-radius: 12px; padding: 10px; display: grid; gap: 8px; }
.lop-picker-head { display: flex; justify-content: space-between; align-items: center; gap: 8px; }
.lop-picker-head > span { font-size: 13px; font-weight: 800; color: #334155; }
.link-btn { border: 0; background: transparent; color: #2563eb; font-weight: 800; cursor: pointer; padding: 4px 6px; }
.lop-list { max-height: 210px; overflow: auto; display: grid; gap: 8px; padding-right: 4px; }
.lop-check { display: grid; grid-template-columns: auto 1fr; align-items: start; gap: 8px; border: 1px solid #e2e8f0; border-radius: 10px; padding: 8px; background: #f8fafc; }
.lop-check input { margin-top: 3px; }
.lop-check span { display: grid; gap: 2px; }
.lop-check small { color: #64748b; }
.auto-manual-box { border: 1px solid #e2e8f0; border-radius: 12px; padding: 12px; display: grid; gap: 12px; }
.auto-manual-box.muted { background: #f8fafc; }
.manual-head { display: flex; justify-content: space-between; gap: 10px; align-items: baseline; }
.manual-head span { font-size: 13px; font-weight: 900; color: #334155; }
.manual-head small { color: #64748b; }
.manual-grid { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 12px; }

@media (max-width: 1500px) {
  .toolbar-card, .workspace, .summary-grid, .auto-grid, .auto-config-grid, .auto-mode-grid, .manual-grid { grid-template-columns: 1fr; }
  .form-card { position: static; }
}
@media (max-width: 720px) {
  .page-head, .matrix-head { flex-direction: column; }
  .ca-list, .bulk-ca-picker .ca-list { grid-template-columns: 1fr; }
}
</style>
