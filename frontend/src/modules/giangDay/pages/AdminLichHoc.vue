<template>
  <section class="schedule-page">
    <header class="page-head compact-panel">
      <div class="page-title-block">
        <p class="eyebrow">Admin đào tạo</p>
        <h1>Quản lý ma trận lịch học</h1>
        <p>Xếp lịch theo lớp/môn, giảng viên, phòng, ca học, thứ trong tuần và ngày trong tháng.</p>
      </div>

      <div class="head-actions">
        <button type="button" class="btn soft" @click="veTuanHienTai">Tuần này</button>
        <button type="button" class="btn primary" @click="taiTatCaDuLieu">Tải lại</button>
      </div>
    </header>

    <section class="toolbar-card compact-panel">
      <div class="week-switcher toolbar-field">
        <button type="button" class="btn icon ghost" aria-label="Tuần trước" @click="doiTuan(-1)">‹</button>
        <label>
          <span>Tuần bắt đầu</span>
          <input v-model="boLoc.tuNgay" type="date" @change="chonTuanTuNgay" />
        </label>
        <button type="button" class="btn icon ghost" aria-label="Tuần sau" @click="doiTuan(1)">›</button>
      </div>

      <label class="toolbar-field">
        <span>Lớp học phần</span>
        <select v-model="boLoc.lopHocPhanId">
          <option value="">Tất cả lớp</option>
          <option v-for="lop in danhSachLopHocPhan" :key="lop.id" :value="lop.id">
            {{ hienThiLop(lop) }}
          </option>
        </select>
      </label>

      <label class="toolbar-field">
        <span>Giảng viên</span>
        <select v-model="boLoc.giaoVienId">
          <option value="">Tất cả giảng viên</option>
          <option v-for="gv in danhSachGiaoVienDangDay" :key="gv.id" :value="gv.id">
            {{ hienThiGiaoVien(gv) }}
          </option>
        </select>
      </label>

      <label class="toolbar-field">
        <span>Phòng học</span>
        <select v-model="boLoc.phongHocId">
          <option value="">Tất cả phòng</option>
          <option v-for="phong in danhSachPhongHoc" :key="phong.id" :value="phong.id">
            {{ hienThiPhong(phong) }}
          </option>
        </select>
      </label>

      <label class="toolbar-field status-filter">
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
        <button type="button" class="btn soft" @click="xoaLoc">Xóa lọc</button>
      </div>
    </section>

    <section class="summary-strip compact-panel">
      <div class="summary-item">
        <span>Lịch trong tuần</span>
        <strong>{{ danhSachTheoBoLoc.length }}</strong>
      </div>
      <div class="summary-item wide">
        <span>Ngày đang xem</span>
        <strong>{{ danhSachNgayTrongTuan[0]?.label }} - {{ danhSachNgayTrongTuan[6]?.label }}</strong>
      </div>
      <div class="summary-item">
        <span>Phân bố</span>
        <strong>{{ soBuoiDaXepLop }}/{{ tongSoBuoiLop }} buổi</strong>
      </div>
      <div class="summary-item">
        <span>Còn lại</span>
        <strong>{{ soBuoiConLaiLop }} buổi</strong>
      </div>
      <div class="summary-item rule">
        <span>Quy tắc ca</span>
        <strong>Nhiều ca/ngày</strong>
      </div>
    </section>

    <div v-if="thongBao" class="alert success">{{ thongBao }}</div>
    <div v-if="loi" class="alert error">{{ loi }}</div>

    <section class="auto-scheduler-card compact-panel">
      <div class="auto-head">
        <div class="auto-title">
          <h2>Xếp lịch học tự động</h2>
          <p>Gộp xếp 1 lớp và nhiều lớp vào một luồng. Có thể chọn thủ công hoặc để hệ thống tự tìm phương án phù hợp.</p>
        </div>
        <button type="button" class="btn soft" @click="layDuLieuTuBoLocChoHangLoat">Lấy theo bộ lọc</button>
      </div>

      <div class="auto-topline">
        <div class="mode-group">
          <span>Phạm vi</span>
          <div class="segmented">
            <button type="button" :class="{ active: autoForm.phamVi === 'mot_lop' }" @click="autoForm.phamVi = 'mot_lop'">Một lớp</button>
            <button type="button" :class="{ active: autoForm.phamVi === 'nhieu_lop' }" @click="autoForm.phamVi = 'nhieu_lop'">Nhiều lớp</button>
          </div>
        </div>

        <div class="mode-group">
          <span>Chế độ</span>
          <div class="segmented">
            <button type="button" :class="{ active: autoForm.cheDo === 'thu_cong' }" @click="autoForm.cheDo = 'thu_cong'">Thủ công</button>
            <button type="button" :class="{ active: autoForm.cheDo === 'tu_dong' }" @click="autoForm.cheDo = 'tu_dong'">Tự động</button>
          </div>
        </div>

        <label class="auto-class-field" v-if="autoForm.phamVi === 'mot_lop'">
          <span>Lớp học phần</span>
          <select v-model="autoForm.lopHocPhanId">
            <option value="">-- Chọn lớp --</option>
            <option v-for="lop in danhSachLopHocPhan" :key="lop.id" :value="lop.id">
              {{ hienThiLop(lop) }}
            </option>
          </select>
        </label>

        <div v-else class="lop-picker auto-class-field">
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

        <label class="plan-count-field">
          <span>Số PA/lớp</span>
          <input v-model.number="autoForm.soPhuongAn" type="number" min="1" max="20" />
        </label>
      </div>

      <div class="auto-config-grid">
        <div class="weekday-picker">
          <span>Thứ được phép xếp</span>
          <div class="week-chip-row">
            <label v-for="thu in danhSachThuHangLoat" :key="thu.value" class="week-chip" :class="{ active: autoForm.thuTrongTuan.includes(thu.value) }">
              <input v-model="autoForm.thuTrongTuan" type="checkbox" :value="thu.value" />
              {{ thu.label }}
            </label>
          </div>
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
            <div class="ca-list compact-ca-list">
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
            <small class="hint">Có thể chọn nhiều ca. Ở chế độ tự động có thể bỏ trống để hệ thống tự chọn ca.</small>
          </div>
        </div>
      </div>

      <div class="bulk-action-bar">
        <div class="bulk-action-text">
          <strong>Sẵn sàng xếp {{ soLopAutoDaChon }} lớp</strong>
          <span>{{ autoForm.tuNgay }} → {{ autoForm.denNgay }}</span>
        </div>
        <div class="bulk-actions">
          <button type="button" class="btn soft" :disabled="dangXuLyHangLoat" @click="xepLichHangLoat(true)">Xem trước lịch</button>
          <button type="button" class="btn primary" :disabled="dangXuLyHangLoat" @click="xepLichHangLoat(false)">Xác nhận xếp lịch</button>
        </div>
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
      <div class="matrix-card compact-panel">
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
                <div class="mobile-cell-meta">
                  <b>{{ ngay.tenThu }} · {{ ngay.label }}</b>
                  <span>{{ ca.tenCa || ca.maCa || ('Ca ' + thuTuCa(ca)) }} · {{ hienThiGioCa(ca) }}</span>
                </div>

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

      <aside class="form-card compact-panel">
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
              <span>Tổng buổi</span>
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
            <small>Mỗi ca học được tính là 1 buổi.</small>
          </div>

          <div class="form-two-col">
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
          </div>

          <label>
            <span>Ngày học</span>
            <input v-model="form.ngayHoc" type="date" required />
            <small v-if="form.ngayHoc">{{ hienThiThuNgay(form.ngayHoc) }}</small>
          </label>

          <div class="ca-picker form-ca-picker">
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
            <small class="hint">Thêm mới có thể chọn nhiều ca. Cập nhật chỉ đổi một ca cho buổi đang sửa.</small>
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

          <div class="form-actions sticky-actions">
            <button type="submit" class="btn primary" :disabled="dangXuLy">
              {{ form.id ? 'Cập nhật lịch' : 'Thêm lịch' }}
            </button>
            <button type="button" class="btn soft" @click="resetForm">Làm mới</button>
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
.schedule-page {
  --pn-primary: #077149;
  --pn-primary-dark: #045d3b;
  --pn-primary-soft: #e8f6ef;
  --pn-primary-softer: #f5fbf8;
  --pn-border: #d7e6df;
  --pn-border-strong: #b8d8ca;
  --pn-text: #0b241a;
  --pn-muted: #5d7488;
  --pn-danger: #dc2626;
  --pn-warning: #f59e0b;
  --pn-blue: #2563eb;
  --pn-green: #059669;
  --pn-shadow: 0 8px 18px rgba(7, 113, 73, .055);

  width: 100%;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding: 6px;
  overflow-x: hidden;
  color: var(--pn-text);
  font-family: Roboto, Arial, sans-serif;
  font-size: 13px;
  background: linear-gradient(180deg, #f6fbf8 0%, #ffffff 38%);
}

.compact-panel {
  border: 1px solid var(--pn-border);
  border-radius: 12px;
  background: rgba(255, 255, 255, .98);
  box-shadow: var(--pn-shadow);
}

.page-head {
  min-height: 58px;
  padding: 8px 12px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  border-color: rgba(7, 113, 73, .18);
  background: linear-gradient(90deg, #ffffff 0%, #fbfffd 74%, #f2faf6 100%);
}

.page-title-block {
  min-width: 0;
  display: grid;
  gap: 2px;
}

.eyebrow {
  margin: 0;
  color: var(--pn-primary);
  font-size: 10px;
  line-height: 1;
  font-weight: 900;
  letter-spacing: .055em;
  text-transform: uppercase;
}

.page-head h1,
.auto-head h2,
.matrix-head h2,
.form-head h2 {
  margin: 0;
  color: #061d15;
  font-weight: 900;
  letter-spacing: -.025em;
}

.page-head h1 { font-size: clamp(18px, 1.65vw, 24px); line-height: 1.05; }
.auto-head h2,
.matrix-head h2,
.form-head h2 { font-size: 17px; line-height: 1.1; }

.page-head p,
.auto-head p,
.matrix-head p,
.form-head p {
  margin: 0;
  color: var(--pn-muted);
  font-size: 12px;
  line-height: 1.25;
}

.head-actions,
.toolbar-actions,
.form-actions,
.legend,
.bulk-actions {
  display: flex;
  align-items: center;
  gap: 7px;
}

label,
.toolbar-field,
.mode-group,
.ca-picker {
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

label span,
.mode-group > span,
.ca-picker > span,
.weekday-picker > span,
.lop-picker-head > span {
  color: #263f35;
  font-size: 11.5px;
  font-weight: 900;
  line-height: 1.15;
}

input,
select,
textarea {
  width: 100%;
  min-width: 0;
  height: 34px;
  border: 1px solid #cfdfd8;
  border-radius: 9px;
  background: #fff;
  color: #10251d;
  padding: 6px 9px;
  font: inherit;
  outline: none;
  transition: border-color .15s ease, box-shadow .15s ease, background .15s ease;
}

textarea {
  height: auto;
  min-height: 54px;
  resize: vertical;
}

input:focus,
select:focus,
textarea:focus {
  border-color: var(--pn-primary);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, .12);
}

select { text-overflow: ellipsis; white-space: nowrap; }

.btn {
  min-height: 32px;
  border: 1px solid #c9ded5;
  border-radius: 9px;
  background: #fff;
  color: #10251d;
  padding: 6px 10px;
  cursor: pointer;
  font-weight: 900;
  font-size: 12px;
  white-space: nowrap;
  transition: transform .12s ease, box-shadow .12s ease, background .12s ease, border-color .12s ease;
}

.btn:hover:not(:disabled) {
  transform: translateY(-1px);
  border-color: var(--pn-border-strong);
  box-shadow: 0 7px 15px rgba(7, 113, 73, .11);
}

.btn.primary {
  border-color: var(--pn-primary);
  background: linear-gradient(180deg, #098455, var(--pn-primary));
  color: #fff;
}

.btn.soft {
  border-color: #cfe3da;
  background: var(--pn-primary-softer);
  color: var(--pn-primary-dark);
}

.btn.ghost {
  background: #fff;
  color: var(--pn-primary);
}

.btn.danger {
  border-color: #fecaca;
  background: #fff5f5;
  color: #b91c1c;
}

.btn.small { min-height: 30px; padding: 5px 9px; font-size: 11px; }
.btn.icon { width: 34px; min-width: 34px; height: 34px; padding: 0; font-size: 20px; line-height: 1; }
.btn:disabled { opacity: .62; cursor: not-allowed; transform: none; box-shadow: none; }

.toolbar-card {
  padding: 9px;
  display: grid;
  grid-template-columns: minmax(190px, .9fr) repeat(3, minmax(150px, 1fr)) minmax(120px, .58fr) auto;
  gap: 8px;
  align-items: end;
}

.week-switcher.toolbar-field {
  display: grid;
  grid-template-columns: 34px minmax(128px, 1fr) 34px;
  align-items: end;
  gap: 6px;
  min-width: 0;
}

.week-switcher.toolbar-field label { min-width: 0; }
.toolbar-actions { justify-content: flex-end; align-self: end; }

.summary-strip {
  display: grid;
  grid-template-columns: repeat(5, minmax(110px, 1fr));
  overflow: hidden;
}
.summary-item {
  min-width: 0;
  padding: 8px 11px;
  border-right: 1px solid var(--pn-border);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}
.summary-item:last-child { border-right: 0; }
.summary-item span { color: var(--pn-muted); font-size: 11.5px; font-weight: 900; white-space: nowrap; }
.summary-item strong { color: #061c14; font-size: 16px; font-weight: 900; line-height: 1; text-align: right; white-space: nowrap; }
.summary-item.rule strong { font-size: 13px; }

.alert { border-radius: 10px; padding: 8px 11px; font-weight: 800; line-height: 1.35; }
.alert.success { border: 1px solid #bbf7d0; background: #ecfdf5; color: #047857; }
.alert.error { border: 1px solid #fecaca; background: #fff1f2; color: #b91c1c; }

.auto-scheduler-card { padding: 10px; display: grid; gap: 9px; }
.auto-head,
.matrix-head,
.form-head,
.manual-head,
.lop-picker-head,
.bulk-action-bar {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 9px;
}
.auto-title { min-width: 0; }

.auto-topline {
  display: grid;
  grid-template-columns: auto auto minmax(250px, 1.45fr) 136px 136px 82px;
  gap: 8px;
  align-items: end;
}

.segmented {
  width: fit-content;
  max-width: 100%;
  display: inline-flex;
  overflow: hidden;
  border: 1px solid #cfe0d8;
  border-radius: 9px;
  background: #fff;
}
.segmented button {
  border: 0;
  background: transparent;
  color: #476158;
  cursor: pointer;
  font-weight: 900;
  padding: 7px 10px;
  white-space: nowrap;
}
.segmented button.active { background: var(--pn-primary); color: #fff; }

.lop-picker {
  min-width: 0;
  padding: 7px;
  display: grid;
  gap: 6px;
  border: 1px dashed #bed8ce;
  border-radius: 10px;
  background: var(--pn-primary-softer);
}
.link-btn { border: 0; background: transparent; color: var(--pn-primary); cursor: pointer; font-weight: 900; padding: 2px 4px; white-space: nowrap; }
.lop-list { max-height: 116px; overflow: auto; display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 5px; padding-right: 2px; }
.lop-check { display: grid; grid-template-columns: auto 1fr; gap: 6px; align-items: start; border: 1px solid #dfeae5; border-radius: 8px; background: #fff; padding: 6px; }
.lop-check input { width: 14px; height: 14px; margin-top: 1px; }
.lop-check span { min-width: 0; display: grid; gap: 1px; }
.lop-check b,
.lop-check small { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.lop-check small { color: var(--pn-muted); }

.auto-config-grid {
  display: grid;
  grid-template-columns: minmax(214px, 214px) minmax(0, 1fr);
  gap: 9px;
  align-items: start;
}

.weekday-picker {
  min-height: 0;
  padding: 9px;
  display: grid;
  align-content: start;
  gap: 7px;
  border: 1px solid #e1ede8;
  border-radius: 11px;
  background: #fff;
}

.week-chip-row {
  display: grid;
  grid-template-columns: repeat(4, max-content);
  gap: 7px;
  align-items: center;
}

.week-chip {
  min-width: 0;
  width: fit-content;
  min-height: 30px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #cfe0d8;
  border-radius: 999px;
  background: #fff;
  color: #334d44;
  cursor: pointer;
  font-weight: 900;
  padding: 5px 10px;
  white-space: nowrap;
}
.week-chip input { display: none; }
.week-chip.active { border-color: var(--pn-primary); background: var(--pn-primary); color: #fff; }
.week-chip:nth-child(4) { grid-column: 1; grid-row: 2; }
.week-chip:nth-child(5) { grid-column: 2; grid-row: 2; }
.week-chip:nth-child(6) { grid-column: 3; grid-row: 2; }
.week-chip:nth-child(7) { grid-column: 4; grid-row: 2; }

.auto-manual-box {
  min-width: 0;
  padding: 9px;
  display: grid;
  gap: 9px;
  border: 1px solid #e1ede8;
  border-radius: 11px;
  background: #fff;
}
.auto-manual-box.muted { background: linear-gradient(180deg, #f8fcfa, #fff); }
.manual-head span { color: #233f35; font-size: 12px; font-weight: 900; }
.manual-head small { color: var(--pn-muted); text-align: right; font-size: 11px; }
.manual-grid,
.form-two-col { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); gap: 8px; }

.ca-list { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); gap: 6px; }
.form-ca-picker .ca-list { grid-template-columns: repeat(2, minmax(0, 1fr)); }
.ca-chip { min-width: 0; padding: 7px 8px; display: grid; gap: 2px; border: 1px solid #cfe0d8; border-radius: 9px; background: #fff; cursor: pointer; transition: border-color .15s ease, background .15s ease, box-shadow .15s ease; }
.ca-chip input { display: none; }
.ca-chip b,
.ca-chip small { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.ca-chip b { color: #14372b; font-size: 12px; }
.ca-chip small,
.hint { color: var(--pn-muted); font-size: 10.5px; line-height: 1.3; }
.ca-chip.active { border-color: var(--pn-primary); background: var(--pn-primary-soft); box-shadow: inset 0 0 0 1px rgba(7, 113, 73, .16); }
.ca-chip.active b { color: var(--pn-primary-dark); }

.bulk-action-bar { align-items: center; padding: 8px 9px; border: 1px solid #d9e9e2; border-radius: 11px; background: linear-gradient(90deg, var(--pn-primary-softer), #fff); }
.bulk-action-text { min-width: 0; display: flex; align-items: center; gap: 9px; }
.bulk-action-text strong { color: var(--pn-primary-dark); white-space: nowrap; }
.bulk-action-text span { color: var(--pn-muted); white-space: nowrap; }
.bulk-result { padding: 9px 11px; border: 1px solid var(--pn-border-strong); border-radius: 11px; background: var(--pn-primary-softer); color: #114434; }
.bulk-result p { margin: 3px 0 7px; }
.bulk-result ul { margin: 0; padding-left: 18px; max-height: 150px; overflow: auto; }
.bulk-result li.ok { color: #047857; }
.bulk-result li.fail { color: #b91c1c; }

.workspace { display: grid; grid-template-columns: minmax(0, 1fr) minmax(320px, 370px); gap: 9px; align-items: start; }
.matrix-card { min-width: 0; overflow: hidden; }
.matrix-head { padding: 10px 12px; border-bottom: 1px solid var(--pn-border); background: linear-gradient(180deg, #fff, #fbfffd); }
.legend { flex-wrap: wrap; justify-content: flex-end; row-gap: 4px; }
.legend span { color: #486257; font-size: 10.5px; font-weight: 900; white-space: nowrap; }
.dot { display: inline-block; width: 8px; height: 8px; border-radius: 50%; margin-right: 4px; vertical-align: -1px; }
.dot.planned { background: var(--pn-blue); }
.dot.done { background: var(--pn-green); }
.dot.warning { background: var(--pn-warning); }
.matrix-wrap { width: 100%; overflow-x: hidden; overflow-y: visible; }
.matrix-grid { display: grid; width: 100%; min-width: 0; background: #eef5f1; }
.matrix-corner,
.matrix-day,
.matrix-slot,
.matrix-cell { border-right: 1px solid #dce8e2; border-bottom: 1px solid #dce8e2; }
.matrix-corner,
.matrix-day { position: sticky; top: 0; z-index: 2; min-width: 0; background: #f3faf6; padding: 8px 9px; }
.matrix-corner { left: 0; z-index: 4; color: #315347; font-weight: 900; }
.matrix-day { display: grid; gap: 1px; align-content: center; }
.matrix-day strong,
.matrix-day span,
.matrix-slot strong,
.matrix-slot span { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.matrix-day strong,
.matrix-slot strong { color: #0a241a; font-size: 12px; }
.matrix-day span,
.matrix-slot span { color: var(--pn-muted); font-size: 10.5px; }
.matrix-slot { position: sticky; left: 0; z-index: 1; min-width: 0; background: #f3faf6; padding: 8px 9px; display: grid; gap: 1px; align-content: start; }
.matrix-cell { min-width: 0; min-height: 76px; padding: 5px; background: #fff; cursor: pointer; display: grid; gap: 5px; align-content: stretch; transition: background .14s ease, box-shadow .14s ease; }
.matrix-cell:hover { background: #f7fcfa; }
.matrix-cell.selected { background: #e5f6ee; box-shadow: inset 0 0 0 2px var(--pn-primary); }
.matrix-cell.conflict { background: #fff7ed; box-shadow: inset 0 0 0 2px #fdba74; }
.mobile-cell-meta { display: none; }
.empty-cell { width: 100%; min-height: 50px; height: 100%; border: 1px dashed #c8dbd2; border-radius: 9px; background: #fbfefd; color: #517166; cursor: pointer; font-weight: 900; font-size: 11.5px; white-space: nowrap; }
.empty-cell:hover { border-color: var(--pn-primary); background: var(--pn-primary-softer); color: var(--pn-primary-dark); }
.empty-cell.selected { border-color: var(--pn-primary); background: var(--pn-primary); color: #fff; }
.schedule-card { min-width: 0; padding: 6px 7px; display: grid; gap: 1px; border-left: 4px solid var(--pn-blue); border-radius: 9px; background: #eff6ff; box-shadow: 0 5px 12px rgba(15, 23, 42, .055); }
.schedule-card.done { border-left-color: var(--pn-green); background: #ecfdf5; }
.schedule-card.warning { border-left-color: var(--pn-warning); background: #fffbeb; }
.card-title,
.card-line,
.card-status { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-title { color: #10251d; font-weight: 900; font-size: 11.5px; }
.card-line { color: #4d655b; font-size: 10.5px; }
.card-status { margin-top: 1px; color: #385349; font-size: 9.5px; font-weight: 900; text-transform: uppercase; }

.form-card { position: sticky; top: 6px; max-height: calc(100vh - 12px); overflow: auto; padding: 10px; }
.form-head { margin-bottom: 8px; }
.form-grid { display: grid; gap: 8px; }
.progress-box { padding: 8px; display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 6px; border: 1px solid var(--pn-border-strong); border-radius: 11px; background: var(--pn-primary-softer); }
.progress-box > div { display: grid; gap: 1px; }
.progress-box span { color: var(--pn-muted); font-size: 10.5px; font-weight: 900; }
.progress-box strong { color: var(--pn-primary-dark); font-size: 16px; line-height: 1; }
.progress-box small { grid-column: 1 / -1; color: var(--pn-muted); }
.warning-box { padding: 8px 9px; border: 1px solid #fdba74; border-radius: 11px; background: #fff7ed; color: #9a3412; }
.warning-box ul { margin: 5px 0 0; padding-left: 18px; }
.sticky-actions { position: sticky; bottom: -10px; margin: 0 -10px -10px; padding: 9px 10px; background: linear-gradient(180deg, rgba(255,255,255,.88), #fff 36%); border-top: 1px solid var(--pn-border); justify-content: flex-end; }

@media (max-width: 1380px) {
  .toolbar-card { grid-template-columns: minmax(190px, 1fr) repeat(2, minmax(150px, 1fr)) minmax(150px, 1fr) minmax(120px, .7fr); }
  .toolbar-actions { grid-column: 1 / -1; justify-content: flex-end; }
  .auto-topline { grid-template-columns: auto auto minmax(230px, 1fr) repeat(3, minmax(112px, .5fr)); }
  .workspace { grid-template-columns: minmax(0, 1fr) 335px; }
  .ca-list { grid-template-columns: repeat(3, minmax(0, 1fr)); }
}

@media (max-width: 1180px) {
  .summary-strip { grid-template-columns: repeat(3, minmax(0, 1fr)); }
  .summary-item { border-bottom: 1px solid var(--pn-border); }
  .summary-item:nth-child(3n) { border-right: 0; }
  .summary-item:nth-last-child(-n + 2) { border-bottom: 0; }
  .auto-topline { grid-template-columns: repeat(2, max-content) repeat(3, minmax(120px, 1fr)); }
  .auto-class-field { grid-column: 1 / -1; }
  .auto-config-grid { grid-template-columns: 1fr; }
  .week-chip-row { grid-template-columns: repeat(7, max-content); }
  .week-chip:nth-child(n) { grid-column: auto; grid-row: auto; }
  .workspace { grid-template-columns: 1fr; }
  .form-card { position: static; max-height: none; }
  .form-ca-picker .ca-list { grid-template-columns: repeat(4, minmax(0, 1fr)); }
}

@media (max-width: 860px) {
  .schedule-page { padding: 5px; gap: 7px; }
  .page-head,
  .auto-head,
  .matrix-head,
  .bulk-action-bar { align-items: stretch; flex-direction: column; }
  .head-actions,
  .bulk-actions,
  .toolbar-actions { width: 100%; }
  .head-actions .btn,
  .bulk-actions .btn,
  .toolbar-actions .btn { flex: 1; }
  .toolbar-card { grid-template-columns: 1fr 1fr; }
  .week-switcher { grid-column: 1 / -1; }
  .toolbar-actions { grid-column: 1 / -1; }
  .summary-strip { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .summary-item,
  .summary-item:nth-child(3n) { border-right: 1px solid var(--pn-border); border-bottom: 1px solid var(--pn-border); }
  .summary-item:nth-child(2n) { border-right: 0; }
  .summary-item:last-child { border-bottom: 0; }
  .auto-topline { grid-template-columns: 1fr 1fr; }
  .auto-class-field { grid-column: 1 / -1; }
  .lop-list { grid-template-columns: 1fr; }
  .manual-grid,
  .form-two-col { grid-template-columns: 1fr; }
  .ca-list,
  .form-ca-picker .ca-list { grid-template-columns: repeat(2, minmax(0, 1fr)); }
  .week-chip-row { grid-template-columns: repeat(4, max-content); }
  .week-chip:nth-child(4) { grid-column: 1; grid-row: 2; }
  .week-chip:nth-child(5) { grid-column: 2; grid-row: 2; }
  .week-chip:nth-child(6) { grid-column: 3; grid-row: 2; }
  .week-chip:nth-child(7) { grid-column: 4; grid-row: 2; }
  .matrix-wrap { overflow-x: hidden; }
  .matrix-grid { display: block !important; width: 100%; }
  .matrix-corner,
  .matrix-day,
  .matrix-slot { display: none; }
  .matrix-cell { min-height: auto; margin: 6px; border: 1px solid #dce8e2; border-radius: 11px; padding: 7px; }
  .mobile-cell-meta { display: flex; justify-content: space-between; gap: 8px; padding-bottom: 6px; border-bottom: 1px dashed #dce8e2; }
  .mobile-cell-meta b,
  .mobile-cell-meta span { min-width: 0; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  .mobile-cell-meta b { color: var(--pn-primary-dark); font-size: 11.5px; }
  .mobile-cell-meta span { color: var(--pn-muted); font-size: 10.5px; text-align: right; }
  .empty-cell { min-height: 40px; }
}

@media (max-width: 560px) {
  .page-head,
  .toolbar-card,
  .auto-scheduler-card,
  .matrix-head,
  .form-card { border-radius: 11px; }
  .page-head p,
  .auto-head p,
  .matrix-head p,
  .form-head p { display: none; }
  .toolbar-card,
  .auto-topline,
  .summary-strip { grid-template-columns: 1fr; }
  .summary-item,
  .summary-item:nth-child(2n),
  .summary-item:nth-child(3n) { border-right: 0; border-bottom: 1px solid var(--pn-border); }
  .summary-item:last-child { border-bottom: 0; }
  .week-switcher.toolbar-field { grid-template-columns: 34px 1fr 34px; }
  .segmented,
  .segmented button { width: 100%; }
  .mode-group { width: 100%; }
  .week-chip-row { grid-template-columns: repeat(2, max-content); }
  .week-chip:nth-child(n) { grid-column: auto; grid-row: auto; }
  .ca-list,
  .form-ca-picker .ca-list { grid-template-columns: 1fr; }
  .bulk-action-text { align-items: flex-start; flex-direction: column; gap: 2px; }
  .progress-box { grid-template-columns: 1fr; }
  .sticky-actions { display: grid; grid-template-columns: 1fr; }
}

/* ===== FINAL FIX: weekday layout + compact header ===== */
.page-head {
  min-height: auto !important;
  padding: 8px 12px !important;
  gap: 10px !important;
}
.page-head h1 {
  font-size: clamp(18px, 1.55vw, 23px) !important;
  line-height: 1.05 !important;
}
.page-head p {
  margin-top: 2px !important;
  font-size: 11px !important;
  line-height: 1.2 !important;
}
.eyebrow {
  margin-bottom: 2px !important;
  font-size: 10px !important;
}
.head-actions .btn {
  min-height: 32px !important;
  padding: 6px 10px !important;
  border-radius: 9px !important;
  font-size: 12px !important;
}

.auto-config-grid {
  grid-template-columns: minmax(0, 1fr) !important;
  align-items: start !important;
}
.weekday-picker {
  width: 100% !important;
  min-width: 0 !important;
  min-height: 0 !important;
  padding: 8px 10px !important;
  gap: 6px !important;
  overflow: visible !important;
}
.week-chip-row {
  display: grid !important;
  grid-template-columns: repeat(4, max-content) !important;
  justify-content: start !important;
  align-items: center !important;
  gap: 6px 8px !important;
  max-width: 100% !important;
  overflow: visible !important;
}
.week-chip {
  min-height: 28px !important;
  padding: 4px 10px !important;
  font-size: 12px !important;
  line-height: 1 !important;
}
.week-chip:nth-child(1) { grid-column: 1 !important; grid-row: 1 !important; }
.week-chip:nth-child(2) { grid-column: 2 !important; grid-row: 1 !important; }
.week-chip:nth-child(3) { grid-column: 3 !important; grid-row: 1 !important; }
.week-chip:nth-child(4) { grid-column: 1 !important; grid-row: 2 !important; }
.week-chip:nth-child(5) { grid-column: 2 !important; grid-row: 2 !important; }
.week-chip:nth-child(6) { grid-column: 3 !important; grid-row: 2 !important; }
.week-chip:nth-child(7) { grid-column: 4 !important; grid-row: 2 !important; }
.auto-manual-box { width: 100% !important; }

@media (min-width: 861px) {
  .weekday-picker {
    display: grid !important;
    grid-template-columns: max-content 1fr !important;
    align-items: center !important;
    column-gap: 14px !important;
  }
  .weekday-picker > span { white-space: nowrap !important; }
}
@media (max-width: 560px) {
  .week-chip-row { grid-template-columns: repeat(2, max-content) !important; }
  .week-chip:nth-child(n) { grid-column: auto !important; grid-row: auto !important; }
}


/* ===== FIX FINAL: gop "Thu duoc phep xep" va "Thong tin thu cong" vao chung 1 block ===== */
.auto-scheduler-card {
  gap: 8px !important;
  padding: 10px !important;
}

.auto-head {
  align-items: center !important;
}

.auto-head p {
  max-width: 860px !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

.auto-topline {
  gap: 8px !important;
}

.auto-config-grid {
  display: grid !important;
  grid-template-columns: minmax(250px, .38fr) minmax(0, 1.62fr) !important;
  gap: 12px !important;
  align-items: start !important;
  padding: 10px !important;
  border: 1px solid #d9e9e2 !important;
  border-radius: 12px !important;
  background: linear-gradient(180deg, #fff, var(--pn-primary-softer)) !important;
  box-shadow: none !important;
  overflow: hidden !important;
}

.weekday-picker,
.auto-manual-box {
  width: 100% !important;
  min-width: 0 !important;
  min-height: 0 !important;
  border: 0 !important;
  border-radius: 0 !important;
  background: transparent !important;
  box-shadow: none !important;
  padding: 0 !important;
  overflow: visible !important;
}

.weekday-picker {
  display: grid !important;
  grid-template-columns: max-content 1fr !important;
  align-items: center !important;
  column-gap: 12px !important;
  row-gap: 4px !important;
  align-content: start !important;
  padding-top: 2px !important;
}

.weekday-picker > span {
  white-space: nowrap !important;
  font-size: 12px !important;
}

.week-chip-row {
  display: grid !important;
  grid-template-columns: repeat(4, max-content) !important;
  justify-content: start !important;
  align-items: center !important;
  gap: 6px 8px !important;
  max-width: 100% !important;
  overflow: visible !important;
}

.week-chip {
  min-height: 28px !important;
  padding: 4px 10px !important;
  font-size: 12px !important;
  line-height: 1 !important;
  white-space: nowrap !important;
}

.week-chip:nth-child(1) { grid-column: 1 !important; grid-row: 1 !important; }
.week-chip:nth-child(2) { grid-column: 2 !important; grid-row: 1 !important; }
.week-chip:nth-child(3) { grid-column: 3 !important; grid-row: 1 !important; }
.week-chip:nth-child(4) { grid-column: 1 !important; grid-row: 2 !important; }
.week-chip:nth-child(5) { grid-column: 2 !important; grid-row: 2 !important; }
.week-chip:nth-child(6) { grid-column: 3 !important; grid-row: 2 !important; }
.week-chip:nth-child(7) { grid-column: 4 !important; grid-row: 2 !important; }

.auto-manual-box {
  display: grid !important;
  gap: 8px !important;
  border-left: 1px solid #d9e9e2 !important;
  padding-left: 12px !important;
}

.auto-manual-box.muted {
  background: transparent !important;
}

.manual-head {
  align-items: center !important;
  min-height: 22px !important;
}

.manual-head small {
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  max-width: 520px !important;
}

.manual-grid {
  gap: 8px !important;
}

.bulk-ca-picker {
  gap: 5px !important;
}

.compact-ca-list,
.bulk-ca-picker .ca-list {
  grid-template-columns: repeat(4, minmax(0, 1fr)) !important;
  gap: 6px !important;
}

.ca-chip {
  min-height: 44px !important;
  padding: 6px 8px !important;
}

.bulk-action-bar {
  margin-top: 0 !important;
}

@media (max-width: 1180px) {
  .auto-config-grid {
    grid-template-columns: 1fr !important;
  }

  .weekday-picker {
    grid-template-columns: max-content 1fr !important;
  }

  .auto-manual-box {
    border-left: 0 !important;
    border-top: 1px solid #d9e9e2 !important;
    padding-left: 0 !important;
    padding-top: 10px !important;
  }
}

@media (max-width: 860px) {
  .auto-head p {
    white-space: normal !important;
  }

  .auto-config-grid {
    padding: 9px !important;
  }

  .weekday-picker {
    grid-template-columns: 1fr !important;
    gap: 7px !important;
  }

  .week-chip-row {
    grid-template-columns: repeat(4, max-content) !important;
  }

  .manual-head {
    align-items: flex-start !important;
  }

  .manual-head small {
    max-width: none !important;
    white-space: normal !important;
    text-align: left !important;
  }

  .compact-ca-list,
  .bulk-ca-picker .ca-list {
    grid-template-columns: repeat(2, minmax(0, 1fr)) !important;
  }
}

@media (max-width: 560px) {
  .week-chip-row {
    grid-template-columns: repeat(2, max-content) !important;
  }

  .week-chip:nth-child(n) {
    grid-column: auto !important;
    grid-row: auto !important;
  }

  .compact-ca-list,
  .bulk-ca-picker .ca-list {
    grid-template-columns: 1fr !important;
  }
}

/* ===== FIX DESKTOP CO GIAN: auto-config khong cat CN, khong cuon ngang ===== */
.schedule-page .auto-config-grid {
  display: grid !important;
  grid-template-columns: minmax(0, 34%) minmax(0, 1fr) !important;
  gap: 10px !important;
  align-items: start !important;
  width: 100% !important;
  max-width: 100% !important;
  min-width: 0 !important;
  overflow: visible !important;
  padding: 10px !important;
  border: 1px solid #d9e9e2 !important;
  border-radius: 12px !important;
  background: linear-gradient(180deg, #fff, var(--pn-primary-softer)) !important;
  box-sizing: border-box !important;
}

.schedule-page .weekday-picker,
.schedule-page .auto-manual-box {
  min-width: 0 !important;
  max-width: 100% !important;
  width: 100% !important;
  overflow: visible !important;
  box-sizing: border-box !important;
  border: 0 !important;
  border-radius: 0 !important;
  background: transparent !important;
  box-shadow: none !important;
  padding: 0 !important;
}

.schedule-page .weekday-picker {
  display: flex !important;
  align-items: flex-start !important;
  gap: 10px !important;
  flex-wrap: wrap !important;
  align-content: flex-start !important;
}

.schedule-page .weekday-picker > span {
  flex: 0 0 auto !important;
  white-space: nowrap !important;
  line-height: 28px !important;
}

.schedule-page .week-chip-row {
  flex: 1 1 220px !important;
  min-width: 0 !important;
  max-width: 100% !important;
  display: flex !important;
  flex-wrap: wrap !important;
  align-items: center !important;
  gap: 6px 8px !important;
  overflow: visible !important;
}

.schedule-page .week-chip,
.schedule-page .week-chip:nth-child(n) {
  grid-column: auto !important;
  grid-row: auto !important;
  flex: 0 0 auto !important;
  width: auto !important;
  min-width: 0 !important;
  max-width: 100% !important;
  min-height: 28px !important;
  padding: 4px 10px !important;
  font-size: 12px !important;
  line-height: 1 !important;
  white-space: nowrap !important;
  box-sizing: border-box !important;
}

.schedule-page .auto-manual-box {
  display: grid !important;
  gap: 8px !important;
  border-left: 1px solid #d9e9e2 !important;
  padding-left: 10px !important;
}

.schedule-page .manual-head,
.schedule-page .manual-grid,
.schedule-page .bulk-ca-picker,
.schedule-page .compact-ca-list {
  min-width: 0 !important;
  max-width: 100% !important;
}

.schedule-page .manual-head small {
  min-width: 0 !important;
  max-width: 100% !important;
  white-space: normal !important;
  overflow: visible !important;
  text-overflow: clip !important;
  text-align: right !important;
}

.schedule-page .compact-ca-list,
.schedule-page .bulk-ca-picker .ca-list {
  display: grid !important;
  grid-template-columns: repeat(auto-fit, minmax(170px, 1fr)) !important;
  gap: 6px !important;
}

.schedule-page .auto-topline {
  min-width: 0 !important;
  max-width: 100% !important;
  overflow: visible !important;
}

@media (max-width: 1280px) {
  .schedule-page .auto-config-grid {
    grid-template-columns: 1fr !important;
  }

  .schedule-page .auto-manual-box {
    border-left: 0 !important;
    border-top: 1px solid #d9e9e2 !important;
    padding-left: 0 !important;
    padding-top: 10px !important;
  }

  .schedule-page .manual-head small {
    text-align: left !important;
  }
}

@media (max-width: 860px) {
  .schedule-page .weekday-picker {
    display: grid !important;
    grid-template-columns: 1fr !important;
    gap: 7px !important;
  }

  .schedule-page .week-chip-row {
    display: flex !important;
    flex-wrap: wrap !important;
  }

  .schedule-page .compact-ca-list,
  .schedule-page .bulk-ca-picker .ca-list {
    grid-template-columns: repeat(2, minmax(0, 1fr)) !important;
  }
}

@media (max-width: 560px) {
  .schedule-page .week-chip {
    flex: 1 1 calc(50% - 8px) !important;
  }

  .schedule-page .compact-ca-list,
  .schedule-page .bulk-ca-picker .ca-list {
    grid-template-columns: 1fr !important;
  }
}

/* ===== FIX MATRIX DESKTOP CO GIAN: khong cat layout, khong dung cuon ngang =====
   Dan patch nay CUOI <style scoped>, sau tat ca CSS hien tai.
   Ly do: matrixGridStyle trong script dang set minmax(145px), can override bang !important.
*/

.schedule-page,
.schedule-page * {
  box-sizing: border-box;
}

.schedule-page .workspace,
.schedule-page .matrix-card,
.schedule-page .matrix-wrap,
.schedule-page .matrix-grid {
  min-width: 0 !important;
  max-width: 100% !important;
}

/* Cho khu ma tran du khong gian hon khi desktop bi hep/zoom lon */
.schedule-page .workspace {
  grid-template-columns: minmax(0, 1fr) clamp(300px, 26vw, 360px) !important;
  overflow: visible !important;
}

/* Quan trong: ghi de inline :style="matrixGridStyle" de bo minmax(145px) gay tran ngang */
.schedule-page .matrix-grid {
  width: 100% !important;
  min-width: 0 !important;
  grid-template-columns: minmax(82px, .58fr) repeat(7, minmax(0, 1fr)) !important;
  table-layout: fixed !important;
}

.schedule-page .matrix-wrap {
  width: 100% !important;
  overflow-x: hidden !important;
  overflow-y: visible !important;
}

.schedule-page .matrix-corner,
.schedule-page .matrix-day,
.schedule-page .matrix-slot,
.schedule-page .matrix-cell {
  min-width: 0 !important;
  max-width: 100% !important;
}

.schedule-page .matrix-corner,
.schedule-page .matrix-day {
  padding: 7px 8px !important;
}

.schedule-page .matrix-slot {
  padding: 7px 8px !important;
}

.schedule-page .matrix-day strong,
.schedule-page .matrix-day span,
.schedule-page .matrix-slot strong,
.schedule-page .matrix-slot span {
  display: block !important;
  min-width: 0 !important;
  max-width: 100% !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}

.schedule-page .matrix-day strong,
.schedule-page .matrix-slot strong {
  font-size: clamp(10.5px, .72vw, 12px) !important;
}

.schedule-page .matrix-day span,
.schedule-page .matrix-slot span {
  font-size: clamp(9.5px, .64vw, 10.5px) !important;
}

.schedule-page .matrix-cell {
  min-height: clamp(58px, 6.2vw, 76px) !important;
  padding: clamp(3px, .42vw, 5px) !important;
  gap: 4px !important;
}

.schedule-page .empty-cell {
  min-width: 0 !important;
  min-height: clamp(38px, 4.6vw, 50px) !important;
  padding: 4px 5px !important;
  font-size: clamp(10px, .7vw, 11.5px) !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

.schedule-page .schedule-card {
  min-width: 0 !important;
  padding: 5px 6px !important;
}

.schedule-page .card-title,
.schedule-page .card-line,
.schedule-page .card-status {
  min-width: 0 !important;
  max-width: 100% !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}

/* Khi desktop/zoom lam sidebar chen ep ma tran, cho form xuong duoi de ma tran khong bi vo */
@media (max-width: 1320px) {
  .schedule-page .workspace {
    grid-template-columns: 1fr !important;
  }

  .schedule-page .form-card {
    position: static !important;
    max-height: none !important;
  }

  .schedule-page .form-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr)) !important;
    align-items: start !important;
  }

  .schedule-page .form-grid > label,
  .schedule-page .form-grid > .ca-picker,
  .schedule-page .form-grid > .warning-box,
  .schedule-page .form-grid > .form-actions,
  .schedule-page .form-grid > .progress-box {
    min-width: 0 !important;
  }

  .schedule-page .form-grid > .ca-picker,
  .schedule-page .form-grid > .warning-box,
  .schedule-page .form-grid > .form-actions,
  .schedule-page .form-grid > .progress-box {
    grid-column: 1 / -1 !important;
  }
}

/* Desktop hep van giu 8 cot, nhung thu gon hon nua thay vi cuon ngang */
@media (max-width: 1100px) and (min-width: 861px) {
  .schedule-page .matrix-grid {
    grid-template-columns: minmax(72px, .5fr) repeat(7, minmax(0, 1fr)) !important;
  }

  .schedule-page .matrix-corner,
  .schedule-page .matrix-day,
  .schedule-page .matrix-slot {
    padding: 6px !important;
  }

  .schedule-page .matrix-cell {
    min-height: 54px !important;
    padding: 3px !important;
  }

  .schedule-page .empty-cell {
    min-height: 36px !important;
    font-size: 10px !important;
  }
}

/* Mobile giu logic card doc hien tai */
@media (max-width: 860px) {
  .schedule-page .matrix-grid {
    display: block !important;
    width: 100% !important;
  }

  .schedule-page .matrix-wrap {
    overflow-x: hidden !important;
  }

  .schedule-page .matrix-cell {
    min-height: auto !important;
  }

  .schedule-page .form-grid {
    grid-template-columns: 1fr !important;
  }
}

</style>



