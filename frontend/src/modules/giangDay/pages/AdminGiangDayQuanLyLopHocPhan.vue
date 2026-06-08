<template>
  <section class="gd-page">
    <!-- TOAST -->
    <div class="toast-wrap">
      <div
          v-for="toast in toasts"
          :key="toast.id"
          :class="['toast', toast.type === 'error' ? 'toast-error' : 'toast-success']"
      >
        <span>{{ toast.message }}</span>
      </div>
    </div>

    <!-- HEADER -->
    <header class="gd-header">
      <div class="gd-title">
        <div class="gd-title-icon">GD</div>
        <div>
          <h1>Vận hành giảng dạy / Quản lý lớp học phần</h1>
          <p>Màn hình gộp để quản lý lớp học phần, phân công giảng viên, điều phối lịch và theo dõi buổi học.</p>
        </div>
      </div>

      <div class="gd-header-actions">
        <button type="button" class="btn" @click="taiDuLieu">Tải lại</button>
        <button type="button" class="btn ghost-blue" @click="moTabDieuPhoi">Xem trước lịch</button>
        <button type="button" class="btn success" :disabled="dangXuLy" @click="luuSinhLich">
          Lưu lịch tự động
        </button>
      </div>
    </header>

    <div v-if="loi" class="alert error">{{ loi }}</div>

    <!-- STAT CARDS -->
    <section class="stat-grid">
      <article class="stat-card">
        <div class="stat-ico blue">▦</div>
        <div>
          <span>Tổng lớp học phần</span>
          <strong>{{ thongKe.tongLop }} <em>Lớp</em></strong>
        </div>
      </article>

      <article class="stat-card">
        <div class="stat-ico orange">◍</div>
        <div>
          <span>Chưa phân công GV chính</span>
          <strong class="orange">{{ thongKe.thieuGvChinh }} <em>Lớp</em></strong>
        </div>
      </article>

      <article class="stat-card">
        <div class="stat-ico amber">▤</div>
        <div>
          <span>Chưa xếp đủ buổi</span>
          <strong class="amber">{{ thongKe.thieuBuoi }} <em>Lớp</em></strong>
        </div>
      </article>

      <article class="stat-card">
        <div class="stat-ico red">⚠</div>
        <div>
          <span>Xung đột lịch</span>
          <strong class="red">{{ thongKe.xungDot }} <em>Lớp</em></strong>
        </div>
      </article>
    </section>

    <!-- WORKSPACE -->
    <div class="gd-workspace">
      <!-- LEFT -->
      <div class="gd-left">
        <!-- FILTERS -->
        <section class="card filter-card">
          <div class="card-head sm">
            <span class="step">1</span>
            <h2>Bộ lọc học vụ</h2>
          </div>

          <div class="filter-grid">
            <label class="span-2">
              <span>Từ khóa</span>
              <input v-model.trim="boLoc.keyword" type="text" placeholder="Mã lớp, tên lớp, môn học..." />
            </label>

            <label>
              <span>Ngành</span>
              <select v-model="boLoc.nganhId" @change="khiDoiNganh">
                <option value="">Tất cả ngành</option>
                <option v-for="item in danhSachNganh" :key="item.id" :value="String(item.id)">
                  {{ hienThiNganh(item) }}
                </option>
              </select>
            </label>

            <label>
              <span>Chương trình</span>
              <select v-model="boLoc.chuongTrinhId" @change="khiDoiChuongTrinh">
                <option value="">Tất cả chương trình</option>
                <option v-for="item in chuongTrinhTheoNganh" :key="item.id" :value="String(item.id)">
                  {{ hienThiChuongTrinh(item) }}
                </option>
              </select>
            </label>

            <label>
              <span>Version chương trình</span>
              <select v-model="boLoc.versionId" @change="khiDoiVersion">
                <option value="">Tất cả version</option>
                <option v-for="item in versionTheoChuongTrinh" :key="item.id" :value="String(item.id)">
                  {{ hienThiVersion(item) }}
                </option>
              </select>
            </label>

            <label>
              <span>Kỳ</span>
              <select v-model="boLoc.khungKyId">
                <option value="">Tất cả kỳ</option>
                <option v-for="item in khungKyTheoBoLoc" :key="item.id" :value="String(item.id)">
                  {{ hienThiKhungKy(item) }}
                </option>
              </select>
            </label>

            <label>
              <span>Môn học</span>
              <select v-model="boLoc.monHoc">
                <option value="">Tất cả môn học</option>
                <option v-for="ten in danhSachMonHocLoc" :key="ten" :value="ten">{{ ten }}</option>
              </select>
            </label>

            <label>
              <span>Loại lớp</span>
              <select v-model="boLoc.loaiLopHocPhan">
                <option value="">Tất cả</option>
                <option value="CHUYEN_NGANH">Chuyên ngành</option>
                <option value="HOC_CHUNG">Học chung</option>
              </select>
            </label>

            <label>
              <span>Trạng thái</span>
              <select v-model="boLoc.trangThaiXep">
                <option value="">Tất cả</option>
                <option value="Đang xếp lịch">Đang xếp lịch</option>
                <option value="Hoàn thành">Hoàn thành</option>
                <option value="Chưa xếp">Chưa xếp</option>
              </select>
            </label>
          </div>

          <div class="filter-foot">
            <button type="button" class="btn small" @click="xoaBoLoc">Xóa lọc</button>
          </div>
        </section>

        <!-- CLASS LIST -->
        <section class="card list-card">
          <div class="card-head">
            <div class="card-head-left">
              <span class="step">2</span>
              <h2>Danh sách lớp học phần</h2>
            </div>
            <span class="count-badge">
              {{ dangTai ? 'Đang tải...' : `${danhSachHienThi.length} lớp` }}
            </span>
          </div>

          <div class="list-sub">{{ moTaBoLoc }}</div>

          <div class="table-wrap list-scroll">
            <table class="data-table list-table">
              <thead>
              <tr>
                <th>Mã lớp</th>
                <th>Tên lớp</th>
                <th>Môn học</th>
                <th>Giáo viên</th>
                <th>Chương trình / Version / Kỳ</th>
                <th class="center">Loại</th>
                <th class="center">Sĩ số</th>
                <th class="center">Buổi</th>
                <th class="center">Trạng thái</th>
                <th class="right">Thao tác</th>
              </tr>
              </thead>

              <tbody>
              <tr v-if="dangTai">
                <td colspan="10" class="empty">Đang tải danh sách lớp học phần...</td>
              </tr>

              <tr v-else-if="danhSachHienThi.length === 0">
                <td colspan="10" class="empty">Không có lớp học phần phù hợp với bộ lọc hiện tại.</td>
              </tr>

              <tr
                  v-for="lop in danhSachHienThi"
                  v-else
                  :key="lop.id"
                  :class="['row-click', { selected: lopDangChon && lopDangChon.id === lop.id }]"
                  @click="chonLop(lop)"
              >
                <td><strong class="code-text">{{ lop.maLop || '-' }}</strong></td>
                <td class="ellipsis">{{ lop.tenLop || '-' }}</td>
                <td class="muted">{{ thongTinMonHocTheoLop(lop).ten }}</td>
                <td>
                  <template v-if="giaoVienCuaLop(lop)">
                    <div class="stack-text">
                      <span class="gv-name">{{ giaoVienCuaLop(lop).ten }}</span>
                      <small v-if="!giaoVienCuaLop(lop).coChinh" class="gv-warn">Chưa có GV chính</small>
                      <small v-else-if="giaoVienCuaLop(lop).them > 0">+{{ giaoVienCuaLop(lop).them }} người khác</small>
                    </div>
                  </template>
                  <span v-else class="muted">-</span>
                </td>
                <td>
                  <div class="stack-text">
                    <span>{{ tenChuongTrinhTheoLop(lop) }}</span>
                    <small>v{{ tenVersionTheoLop(lop) }} · {{ tenKyTheoLop(lop) }}</small>
                  </div>
                </td>
                <td class="center">
                  <span :class="['type-pill', lop.loaiLopHocPhan === 'HOC_CHUNG' ? 'purple' : 'indigo']">
                    {{ hienThiLoaiLop(lop.loaiLopHocPhan) }}
                  </span>
                </td>
                <td class="center bold">{{ lop.soLuongHienTai ?? 0 }}/{{ lop.soLuongToiDa ?? '-' }}</td>
                <td class="center">
                  <span class="bold">{{ soBuoiDaXepCuaLop(lop) }}</span>/{{ lop.soBuoiHoc ?? '-' }}
                </td>
                <td class="center">
                  <span :class="['status-pill', classTrangThaiXep(trangThaiXepLich(lop))]">
                    {{ trangThaiXepLich(lop) }}
                  </span>
                </td>
                <td class="right" @click.stop>
                  <div class="row-actions">
                    <button type="button" class="icon-btn" title="Điều phối lịch" @click="moDieuPhoi(lop)">⟳</button>
                    <button type="button" class="icon-btn" title="Phân công" @click="moPhanCong(lop)">✎</button>
                  </div>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
        </section>
      </div>

      <!-- RIGHT -->
      <div class="gd-right">
        <!-- OVERVIEW -->
        <section class="card">
          <div class="card-head sm">
            <span class="step">3</span>
            <h2>Tổng quan lớp đang chọn</h2>
          </div>

          <div v-if="!lopDangChon" class="empty-box">Chọn một lớp học phần ở danh sách bên trái để xem chi tiết.</div>

          <div v-else class="overview-grid">
            <div class="ov"><span>Mã lớp</span><strong class="code-text">{{ lopDangChon.maLop || '-' }}</strong></div>
            <div class="ov"><span>Tên lớp</span><strong class="ellipsis">{{ lopDangChon.tenLop || '-' }}</strong></div>
            <div class="ov"><span>Môn</span><strong class="ellipsis">{{ thongTinMonHocTheoLop(lopDangChon).ten }}</strong></div>
            <div class="ov"><span>Kỳ</span><strong>{{ tenKyTheoLop(lopDangChon) }}</strong></div>
            <div class="ov"><span>Loại</span><strong>{{ hienThiLoaiLop(lopDangChon.loaiLopHocPhan) }}</strong></div>
            <div class="ov"><span>Sĩ số</span><strong>{{ lopDangChon.soLuongHienTai ?? 0 }}/{{ lopDangChon.soLuongToiDa ?? '-' }}</strong></div>
            <div class="ov"><span>Buổi đã xếp</span><strong class="green">{{ soBuoiDaXep }} / {{ lopDangChon.soBuoiHoc ?? '-' }}</strong></div>
            <div class="ov"><span>Còn thiếu</span><strong class="amber">{{ soBuoiConLai }}</strong></div>
            <div class="ov"><span>Cột điểm</span><strong>{{ cotDiemTheoLop.length }}</strong></div>
          </div>
        </section>

        <!-- TABS -->
        <section class="card no-pad">
          <div class="tab-row">
            <button
                v-for="tab in tabs"
                :key="tab"
                type="button"
                :class="['tab-btn', { active: activeTab === tab }]"
                @click="activeTab = tab"
            >
              {{ tab }}
              <span
                  v-if="tab === 'Cảnh báo' && lopDangChon && canhBaoTheoLop.length"
                  class="tab-warn"
                  :title="`${canhBaoTheoLop.length} cảnh báo`"
              >⚠</span>
            </button>
          </div>

          <div v-if="!lopDangChon" class="tab-body">
            <div class="empty-box">Chưa chọn lớp học phần.</div>
          </div>

          <!-- TAB: PHÂN CÔNG GIẢNG VIÊN -->
          <div v-else-if="activeTab === 'Phân công giảng viên'" class="tab-body">
            <div class="tab-head">
              <div class="card-head-left">
                <span class="step">4</span>
                <h3>A. Phân công giảng viên</h3>
              </div>
              <span v-if="coGiaoVienChinh" class="badge green">✓ Đã có giảng viên chính</span>
              <span v-else class="badge amber">Chưa có giảng viên chính</span>
            </div>

            <div class="form-box">
              <label>
                <span>Chuyên môn giảng dạy</span>
                <select v-model="formLecturer.chuyenMon" @change="formLecturer.giaoVienId = ''">
                  <option value="">-- Chọn chuyên môn --</option>
                  <option v-for="cm in danhSachChuyenMon" :key="cm" :value="cm">{{ cm }}</option>
                </select>
              </label>

              <label>
                <span>Họ và tên Giáo viên</span>
                <select v-model="formLecturer.giaoVienId" :disabled="!formLecturer.chuyenMon">
                  <option value="">-- Chọn giáo viên --</option>
                  <option v-for="gv in giaoVienTheoChuyenMon" :key="gv.id" :value="gv.id">
                    {{ hienThiGiaoVienOption(gv) }}
                  </option>
                </select>
              </label>

              <label>
                <span>Vai trò</span>
                <select v-model="formLecturer.vaiTro">
                  <option value="giang_vien_chinh">Giảng viên chính</option>
                  <option value="tro_giang">Trợ giảng</option>
                  <option value="coi_thi">Coi thi</option>
                  <option value="cham_thi">Chấm thi</option>
                </select>
              </label>

              <div class="form-box-action">
                <button type="button" class="btn primary" :disabled="dangXuLy" @click="themPhanCong">+ Phân công</button>
              </div>
            </div>

            <div v-if="phanCongLoi" class="alert error">{{ phanCongLoi }}</div>
            <div v-else-if="phanCongOk" class="alert success">{{ phanCongOk }}</div>

            <div class="table-wrap">
              <table class="data-table">
                <thead>
                <tr>
                  <th>STT</th>
                  <th>Giáo viên</th>
                  <th>Vai trò</th>
                  <th>Chuyên môn</th>
                  <th class="right">Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="phanCongTheoLop.length === 0">
                  <td colspan="5" class="empty">Chưa phân công giảng viên nào cho lớp này.</td>
                </tr>
                <tr v-for="(gv, idx) in phanCongTheoLop" v-else :key="gv.id">
                  <td class="muted">{{ idx + 1 }}</td>
                  <td>
                    <div class="cell-main">
                      <strong>{{ gv.tenGiaoVien || '-' }}</strong>
                      <small>{{ gv.maGiaoVien || '' }}{{ gv.email ? ' · ' + gv.email : '' }}</small>
                    </div>
                  </td>
                  <td>
                    <span :class="['status-pill', gv.vaiTro === 'giang_vien_chinh' ? 'done' : 'active']">
                      {{ hienThiVaiTro(gv.vaiTro) }}
                    </span>
                  </td>
                  <td class="muted">{{ gv.chuyenMon || '-' }}</td>
                  <td class="right">
                    <button type="button" class="btn small danger" :disabled="dangXuLy" @click="xoaPhanCong(gv)">Xóa</button>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- TAB: ĐIỀU PHỐI LỊCH HỌC -->
          <div v-else-if="activeTab === 'Điều phối lịch học'" class="tab-body">
            <div class="tab-head">
              <div class="card-head-left">
                <span class="step">5</span>
                <h3>B. Điều phối lịch / Sinh lịch học tự động</h3>
              </div>
            </div>

            <div class="assigned-card" :class="{ warning: !coGiaoVienChinh }">
              <div>
                <span>Giảng viên đã phân công</span>
                <strong>{{ coGiaoVienChinh ? giaoVienChinh.tenGiaoVien : 'Chưa có giảng viên chính' }}</strong>
                <small v-if="coGiaoVienChinh">{{ giaoVienChinh.email || 'Chưa có email' }}</small>
                <small v-else>Cần phân công giảng viên chính trước khi sinh lịch.</small>
              </div>
              <button type="button" class="btn small" @click="activeTab = 'Phân công giảng viên'">
                {{ coGiaoVienChinh ? 'Đổi phân công' : 'Phân công giáo viên' }}
              </button>
            </div>

            <div class="form-box four">
              <label>
                <span>Phòng học ấn định</span>
                <select v-model.number="formSinhLich.phongHocId">
                  <option :value="null">-- Chọn phòng --</option>
                  <option v-for="p in danhSachPhongHoc" :key="p.id" :value="p.id">{{ hienThiPhongHoc(p) }}</option>
                </select>
              </label>
              <div class="form-field ca-field">
                <span>Ca học ấn định</span>
                <div class="ca-chip-grid">
                  <button
                    v-for="c in danhSachCaHocSapXep"
                    :key="c.id"
                    type="button"
                    :class="['ca-chip', { active: laCaDangChon(c.id) }]"
                    @click="chonCaSinhLich(c.id)"
                  >
                    {{ hienThiCaHoc(c) }}
                  </button>
                </div>
                <small class="hint">Chọn tối đa 2 ca liền kề trong danh sách. Ví dụ: Ca 1 + Ca 2, Ca 2 + Ca 3.</small>
              </div>
              <label>
                <span>Từ ngày khởi đầu</span>
                <input v-model="formSinhLich.tuNgay" type="date" />
              </label>
              <label>
                <span>Đến ngày kết thúc</span>
                <input v-model="formSinhLich.denNgay" type="date" />
              </label>
            </div>

            <div class="weekday-card">
              <strong>Chọn ngày học định kỳ trong tuần</strong>
              <div class="weekday-row">
                <label v-for="thu in danhSachThu" :key="thu.value">
                  <input type="checkbox" :value="thu.value" v-model="formSinhLich.thuTrongTuan" />
                  {{ thu.label }}
                </label>
              </div>
            </div>

            <div v-if="!coGiaoVienChinh" class="alert error mt">
              Lớp học phần chưa có giảng viên chính. Hãy phân công giáo viên trước khi sinh lịch.
            </div>
            <div v-else-if="soBuoiConLai <= 0" class="alert success mt">
              Lớp học phần đã được xếp đủ số buổi học.
            </div>

            <div v-if="dieuPhoiLoi" class="alert error mt">{{ dieuPhoiLoi }}</div>
            <div v-else-if="dieuPhoiOk" class="alert success mt">{{ dieuPhoiOk }}</div>

            <div class="tab-foot">
              <button
                  type="button"
                  class="btn"
                  :disabled="dangXuLy || !coGiaoVienChinh || soBuoiConLai <= 0"
                  @click="xemTruocLich"
              >
                {{ dangXuLy ? 'Đang xử lý...' : 'Xem trước lịch' }}
              </button>
              <button
                  type="button"
                  class="btn success"
                  :disabled="dangXuLy || !coGiaoVienChinh || soBuoiConLai <= 0"
                  @click="luuSinhLich"
              >
                Lưu và Sinh lịch tự động
              </button>
            </div>
          </div>

          <!-- TAB: LỊCH HỌC -->
          <div v-else-if="activeTab === 'Lịch học'" class="tab-body">
            <div class="tab-head">
              <div class="card-head-left">
                <h3>Lịch học tổng quan của lớp học phần</h3>
              </div>
            </div>

            <div class="chip-grid">
              <div class="chip blue"><span>Phòng phân bổ chính</span><strong>{{ phongChinh }}</strong></div>
              <div class="chip indigo"><span>Ca định kỳ</span><strong>{{ caChinh }}</strong></div>
              <div class="chip green"><span>Số buổi đã xếp / tổng</span><strong>{{ soBuoiDaXep }} / {{ lopDangChon.soBuoiHoc ?? '-' }}</strong></div>
            </div>

            <div class="table-wrap">
              <table class="data-table">
                <thead>
                <tr>
                  <th>Thứ tự</th><th>Thứ</th><th>Ngày giảng dạy</th><th>Ca học</th><th>Phòng</th><th>Giảng viên</th><th>Trạng thái</th>
                </tr>
                </thead>
                <tbody>
                <tr v-if="lichHocTheoLop.length === 0">
                  <td colspan="7" class="empty">Lớp chưa có buổi học. Sinh lịch ở tab Điều phối.</td>
                </tr>
                <tr v-for="(buoi, idx) in lichHocTheoLop" v-else :key="buoi.id">
                  <td class="bold">B{{ idx + 1 }}</td>
                  <td><span class="thu-pill">{{ tenThu(buoi.ngayHoc) }}</span></td>
                  <td>{{ dinhDangNgay(buoi.ngayHoc) }}</td>
                  <td class="muted">{{ buoi.tenCa || tenCaTheoId(buoi.caHocId) }}</td>
                  <td>{{ buoi.tenPhong || tenPhongTheoId(buoi.phongHocId) }}</td>
                  <td><strong>{{ buoi.tenGiaoVien || tenGiaoVienTheoId(buoi.giaoVienId) }}</strong></td>
                  <td>
                    <span :class="['status-pill', classTrangThaiLichHoc(buoi.trangThai)]">
                      {{ hienThiTrangThaiLichHoc(buoi.trangThai) }}
                    </span>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- TAB: CẢNH BÁO -->
          <div v-else-if="activeTab === 'Cảnh báo'" class="tab-body">
            <div class="tab-head">
              <div class="card-head-left">
                <h3>Cảnh báo liên quan đến lớp {{ lopDangChon.maLop }}</h3>
              </div>
            </div>

            <div v-if="canhBaoTheoLop.length === 0" class="empty-box success-box">
              Tuyệt vời! Không có cảnh báo tồn đọng nào cho lớp này.
            </div>

            <div v-else class="alert-list">
              <div
                  v-for="alert in canhBaoTheoLop"
                  :key="alert.id"
                  :class="['alert-item', alert.type]"
              >
                <div class="alert-item-main">
                  <div :class="['alert-dot', alert.type]">⚠</div>
                  <div>
                    <h5>{{ alert.title }}</h5>
                    <p>{{ alert.desc }}</p>
                  </div>
                </div>
                <button type="button" class="btn small" @click="activeTab = alert.tab">Giải quyết ngay</button>
              </div>
            </div>
          </div>

          <!-- TAB: REVIEW LỊCH HỌC -->
          <div v-else-if="activeTab === 'Review lịch học'" class="tab-body">
            <div class="tab-head">
              <div class="card-head-left">
                <span class="step">6</span>
                <h3>C. Preview lịch học đề xuất</h3>
              </div>
              <span v-if="previewLich" :class="['badge', previewLich.hopLe ? 'green' : 'amber']">
                {{ previewLich.hopLe ? 'Đủ điều kiện xếp' : 'Chưa hợp lệ' }}
              </span>
            </div>

            <div v-if="!previewLich" class="empty-box">
              Chưa có dữ liệu xem trước. Vào tab <b>Điều phối lịch học</b> và bấm <b>Xem trước lịch</b>.
            </div>

            <template v-else>
              <p class="preview-note">Xếp được {{ previewLich.soBuoiXepDuoc }}/{{ previewLich.soBuoiConLai }} buổi còn lại.</p>

              <div v-if="previewLich.canhBao && previewLich.canhBao.length" class="alert error mt">
                <div v-for="msg in previewLich.canhBao" :key="msg">{{ msg }}</div>
              </div>

              <div class="table-wrap mt">
                <table class="data-table">
                  <thead>
                  <tr><th>Buổi</th><th>Ngày đề xuất</th><th>Ca học</th><th>Phòng học</th><th>Giáo viên</th><th>Nội dung</th></tr>
                  </thead>
                  <tbody>
                  <tr v-if="!previewLich.items || previewLich.items.length === 0">
                    <td colspan="6" class="empty">Không có buổi học nào được xếp.</td>
                  </tr>
                  <tr v-for="item in previewLich.items" v-else :key="`${item.stt}-${item.ngayHoc}`">
                    <td class="bold">B{{ item.stt }}</td>
                    <td>{{ tenThu(item.ngayHoc) }} · {{ dinhDangNgay(item.ngayHoc) }}</td>
                    <td class="muted">{{ tenCaTheoId(item.caHocId) }}</td>
                    <td>{{ tenPhongTheoId(item.phongHocId) }}</td>
                    <td><strong class="code-text">{{ tenGiaoVienTheoId(item.giaoVienId) }}</strong></td>
                    <td class="ellipsis">{{ item.noiDungBuoiHoc || '-' }}</td>
                  </tr>
                  </tbody>
                </table>
              </div>

              <div class="tab-foot">
                <button type="button" class="btn success" :disabled="dangXuLy || !previewLich.hopLe" @click="luuSinhLich">
                  Lưu lịch tự động
                </button>
              </div>
            </template>
          </div>

          <!-- TAB: BUỔI HỌC CỦA LỚP -->
          <div v-else-if="activeTab === 'Buổi học của lớp'" class="tab-body">
            <div class="tab-head">
              <div class="card-head-left">
                <span class="step">7</span>
                <h3>D. Danh sách buổi học và tiến độ thực tế</h3>
              </div>
            </div>

            <div v-if="lichHocTheoLop.length === 0" class="empty-box">
              Lớp học phần này chưa có buổi học.
            </div>

            <div v-else class="buoi-list">
              <article v-for="(buoi, idx) in lichHocTheoLop" :key="buoi.id" class="buoi-item">
                <div class="buoi-left">
                  <div :class="['buoi-ico', buoi.trangThai === 'da_day' ? 'blue' : 'amber']">📅</div>
                  <div>
                    <div class="buoi-title">
                      <strong>B{{ idx + 1 }}</strong>
                      <span class="buoi-date">{{ tenThu(buoi.ngayHoc) }} · {{ dinhDangNgay(buoi.ngayHoc) }}</span>
                    </div>
                    <div class="buoi-meta">
                      Ca: <b>{{ buoi.tenCa || tenCaTheoId(buoi.caHocId) }}</b> ·
                      Phòng: <b>{{ buoi.tenPhong || tenPhongTheoId(buoi.phongHocId) }}</b> ·
                      GV: <b class="code-text">{{ buoi.tenGiaoVien || tenGiaoVienTheoId(buoi.giaoVienId) }}</b>
                    </div>
                  </div>
                </div>
                <div class="buoi-right">
                  <span :class="['status-pill', classTrangThaiLichHoc(buoi.trangThai)]">
                    {{ hienThiTrangThaiLichHoc(buoi.trangThai) }}
                  </span>
                  <button type="button" class="btn small" @click="xemBuoiHoc(buoi)">Xem buổi học</button>
                </div>
              </article>
            </div>
          </div>
        </section>
      </div>
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { giangDayService } from '../services/giangDayService'

const route = useRoute()
const router = useRouter()

/* ---------- STATE ---------- */
const dangTai = ref(false)
const dangXuLy = ref(false)
const loi = ref('')
const toasts = ref([])

const tabs = ['Phân công giảng viên', 'Điều phối lịch học', 'Lịch học', 'Cảnh báo', 'Review lịch học', 'Buổi học của lớp']
const activeTab = ref('Phân công giảng viên')

const danhSachThu = [
  { value: 1, label: 'Thứ 2' }, { value: 2, label: 'Thứ 3' }, { value: 3, label: 'Thứ 4' },
  { value: 4, label: 'Thứ 5' }, { value: 5, label: 'Thứ 6' }, { value: 6, label: 'Thứ 7' },
  { value: 7, label: 'Chủ nhật' }
]

const boLoc = reactive({
  keyword: '',
  nganhId: giaTriQuery(route.query.nganhId),
  chuongTrinhId: giaTriQuery(route.query.chuongTrinhId),
  versionId: giaTriQuery(route.query.versionId),
  khungKyId: giaTriQuery(route.query.khungKyId),
  monHoc: '',
  loaiLopHocPhan: '',
  trangThaiXep: ''
})

const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachMonHoc = ref([])
const danhSachLopHocPhan = ref([])
const danhSachLopHocPhanChuongTrinhMon = ref([])
const danhSachLichHoc = ref([])
const danhSachGiaoVien = ref([])
const danhSachPhongHoc = ref([])
const danhSachCaHoc = ref([])
const danhSachPhanCong = ref([])
const danhSachCauHinhDanhGia = ref([])

const lopDangChonId = ref(null)
const previewLich = ref(null)

// Thông báo cục bộ theo từng khu vực để báo lỗi ngay tại chỗ
const phanCongLoi = ref('')
const phanCongOk = ref('')
const dieuPhoiLoi = ref('')
const dieuPhoiOk = ref('')

const formLecturer = reactive({ chuyenMon: '', giaoVienId: '', vaiTro: 'giang_vien_chinh' })
const formSinhLich = reactive({ phongHocId: null, caHocIds: [], tuNgay: '', denNgay: '', thuTrongTuan: [] })

const danhSachCaHocSapXep = computed(() => [...danhSachCaHoc.value].sort((a, b) => thuTuCaHoc(a) - thuTuCaHoc(b)))

function layCaHocIdsSinhLich() {
  return [...new Set((formSinhLich.caHocIds || []).filter((id) => id != null).map(Number))]
}

function laCaDangChon(caHocId) {
  return layCaHocIdsSinhLich().includes(Number(caHocId))
}

function chonCaSinhLich(caHocId) {
  const id = Number(caHocId)
  const hienTai = layCaHocIdsSinhLich()

  if (hienTai.includes(id)) {
    formSinhLich.caHocIds = hienTai.filter((x) => x !== id)
    previewLich.value = null
    return
  }

  const moi = [...hienTai, id]
  const loi = kiemTraCaLienTucSinhLich(moi)
  if (loi) {
    showToast(loi, 'error')
    return
  }

  formSinhLich.caHocIds = moi
  previewLich.value = null
}

function kiemTraCaLienTucSinhLich(idsInput = layCaHocIdsSinhLich()) {
  const ids = [...new Set((idsInput || []).filter((id) => id != null).map(Number))]
  if (ids.length > 2) return 'Chỉ được chọn tối đa 2 ca trong một ngày'
  if (ids.length === 2) {
    const viTri = ids.map(viTriCaHoc).sort((a, b) => a - b)
    if (viTri.some((idx) => idx < 0)) return 'Ca học không tồn tại'
    if (viTri[1] - viTri[0] !== 1) return 'Chỉ được chọn 2 ca liền kề trong danh sách ca học'
  }
  return ''
}

function viTriCaHoc(caHocId) {
  return danhSachCaHocSapXep.value.findIndex((ca) => bangId(ca.id, caHocId))
}

function thuTuCaHoc(ca) {
  if (!ca) return 0
  const text = `${ca.maCa || ''} ${ca.tenCa || ''}`
  const match = text.match(/\d+/)
  if (match) return Number(match[0])
  return Number(ca.thuTu || ca.id || 0)
}


/* ---------- CASCADING FILTER OPTIONS ---------- */
const chuongTrinhTheoNganh = computed(() => {
  if (!boLoc.nganhId) return danhSachChuongTrinh.value
  return danhSachChuongTrinh.value.filter((item) => bangId(item.nganhId, boLoc.nganhId))
})

const versionTheoChuongTrinh = computed(() => {
  const ctIds = chuongTrinhTheoNganh.value.map((item) => Number(item.id))
  return danhSachVersion.value.filter((item) => {
    if (boLoc.chuongTrinhId) return bangId(item.chuongTrinhId, boLoc.chuongTrinhId)
    if (boLoc.nganhId) return ctIds.includes(Number(item.chuongTrinhId))
    return true
  })
})

const versionIdsDangLoc = computed(() => {
  if (boLoc.versionId) return [Number(boLoc.versionId)]
  return versionTheoChuongTrinh.value.map((item) => Number(item.id))
})

const coBoLocChuongTrinh = computed(() => Boolean(boLoc.nganhId || boLoc.chuongTrinhId || boLoc.versionId))

const khungKyTheoBoLoc = computed(() => {
  const versionIds = versionIdsDangLoc.value
  return danhSachKhungKy.value
      .filter((item) => {
        if (!coBoLocChuongTrinh.value) return true
        if (versionIds.length === 0) return false
        const vId = item.chuongTrinhVersionId ?? item.versionId
        if (vId) return versionIds.includes(Number(vId))
        return danhSachChuongTrinhMon.value.some((ctm) => bangId(ctm.khungKyId, item.id) && versionIds.includes(Number(ctm.chuongTrinhVersionId)))
      })
      .sort((a, b) => Number(a.thuTu ?? a.id ?? 0) - Number(b.thuTu ?? b.id ?? 0))
})

const danhSachMonHocLoc = computed(() => {
  const set = new Set()
  danhSachLopHocPhan.value.forEach((lop) => {
    if (lopDaGanMonHoc(lop)) set.add(thongTinMonHocTheoLop(lop).ten)
  })
  return [...set].filter((x) => x && x !== '-').sort((a, b) => a.localeCompare(b))
})

/* ---------- LIST + STATS ---------- */
const danhSachDaGan = computed(() => danhSachLopHocPhan.value.filter((lop) => lopDaGanMonHoc(lop)))

const danhSachHienThi = computed(() => {
  const tuKhoa = boLoc.keyword.toLowerCase()
  return danhSachDaGan.value.filter((lop) => {
    if (boLoc.nganhId && !lopThuocNganh(lop, boLoc.nganhId)) return false
    if (boLoc.chuongTrinhId && !lopThuocChuongTrinh(lop, boLoc.chuongTrinhId)) return false
    if (boLoc.versionId && !lopThuocVersion(lop, boLoc.versionId)) return false
    if (boLoc.khungKyId && !lopThuocKhungKy(lop, boLoc.khungKyId)) return false
    if (boLoc.loaiLopHocPhan && lop.loaiLopHocPhan !== boLoc.loaiLopHocPhan) return false
    if (boLoc.monHoc && thongTinMonHocTheoLop(lop).ten !== boLoc.monHoc) return false
    if (boLoc.trangThaiXep && trangThaiXepLich(lop) !== boLoc.trangThaiXep) return false
    if (tuKhoa) {
      const mh = thongTinMonHocTheoLop(lop)
      const txt = `${lop.maLop || ''} ${lop.tenLop || ''} ${mh.ma || ''} ${mh.ten || ''}`.toLowerCase()
      if (!txt.includes(tuKhoa)) return false
    }
    return true
  })
})

const conflictClassIds = computed(() => {
  const map = new Map()
  danhSachLichHoc.value.forEach((l) => {
    if (!l.ngayHoc || l.caHocId == null || l.phongHocId == null) return
    const key = `${l.ngayHoc}__${l.caHocId}__${l.phongHocId}`
    if (!map.has(key)) map.set(key, new Set())
    map.get(key).add(Number(l.lopHocPhanId))
  })
  const ids = new Set()
  map.forEach((set) => { if (set.size > 1) set.forEach((id) => ids.add(id)) })
  return ids
})

const thongKe = computed(() => {
  const list = danhSachDaGan.value
  return {
    tongLop: list.length,
    thieuGvChinh: list.filter((lop) => !coGiaoVienChinhCuaLop(lop)).length,
    thieuBuoi: list.filter((lop) => {
      const tong = Number(lop.soBuoiHoc || 0)
      return tong > 0 && soBuoiDaXepCuaLop(lop) < tong
    }).length,
    xungDot: list.filter((lop) => conflictClassIds.value.has(Number(lop.id))).length
  }
})

const moTaBoLoc = computed(() => {
  const phan = []
  if (boLoc.nganhId) phan.push(timTen(danhSachNganh.value, boLoc.nganhId, hienThiNganh))
  if (boLoc.versionId) phan.push(timTen(danhSachVersion.value, boLoc.versionId, hienThiVersion))
  if (boLoc.khungKyId) phan.push(timTen(danhSachKhungKy.value, boLoc.khungKyId, hienThiKhungKy))
  if (boLoc.monHoc) phan.push(boLoc.monHoc)
  if (boLoc.loaiLopHocPhan) phan.push(hienThiLoaiLop(boLoc.loaiLopHocPhan))
  if (boLoc.trangThaiXep) phan.push(boLoc.trangThaiXep)
  if (boLoc.keyword) phan.push(`Từ khóa: ${boLoc.keyword}`)
  return phan.length ? `Đang lọc theo: ${phan.join(' · ')}` : 'Đang hiển thị tất cả lớp đã gán môn học.'
})

/* ---------- SELECTED CLASS ---------- */
const lopDangChon = computed(() => danhSachLopHocPhan.value.find((lop) => Number(lop.id) === Number(lopDangChonId.value)) || null)

const lichHocTheoLop = computed(() => {
  if (!lopDangChon.value) return []
  return danhSachLichHoc.value
      .filter((item) => bangId(item.lopHocPhanId, lopDangChon.value.id))
      .sort((a, b) => {
        const na = a.ngayHoc || '', nb = b.ngayHoc || ''
        if (na !== nb) return na.localeCompare(nb)
        return Number(a.caHocId || 0) - Number(b.caHocId || 0)
      })
})

const soBuoiDaXep = computed(() => lichHocTheoLop.value.filter((item) => item.trangThai !== 'nghi').length)
const soBuoiConLai = computed(() => Math.max(0, Number(lopDangChon.value?.soBuoiHoc || 0) - soBuoiDaXep.value))

const phanCongTheoLop = computed(() => {
  if (!lopDangChon.value) return []
  return danhSachPhanCong.value
      .filter((item) => bangId(item.lopHocPhanId, lopDangChon.value.id))
      .map((item) => chuanHoaPhanCong(item))
})

const giaoVienChinh = computed(() => phanCongTheoLop.value.find((item) => item.vaiTro === 'giang_vien_chinh') || null)
const coGiaoVienChinh = computed(() => Boolean(giaoVienChinh.value))

const cotDiemTheoLop = computed(() => {
  if (!lopDangChon.value) return []
  return danhSachCauHinhDanhGia.value.filter((item) => {
    const id = item.lopHocPhanId ?? item.lopHocPhan?.id
    return id == null || bangId(id, lopDangChon.value.id)
  })
})

const phongChinh = computed(() => {
  const buoi = lichHocTheoLop.value.find((b) => b.phongHocId != null)
  if (buoi) return buoi.tenPhong || tenPhongTheoId(buoi.phongHocId)
  return formSinhLich.phongHocId ? tenPhongTheoId(formSinhLich.phongHocId) : 'Chưa ấn định'
})

const caChinh = computed(() => {
  const buoi = lichHocTheoLop.value.find((b) => b.caHocId != null)
  if (buoi) return buoi.tenCa || tenCaTheoId(buoi.caHocId)
  return layCaHocIdsSinhLich().length ? layCaHocIdsSinhLich().map(tenCaTheoId).join(' + ') : 'Chưa ấn định'
})

/* ---------- LECTURER FORM OPTIONS ---------- */
const giaoVienDangDay = computed(() => danhSachGiaoVien.value.filter((gv) => gv.trangThai === 'dang_day'))

const danhSachChuyenMon = computed(() => {
  const set = new Set()
  giaoVienDangDay.value.forEach((gv) => set.add(chuanHoaChuyenMon(gv.chuyenMon)))
  return [...set].sort((a, b) => a.localeCompare(b))
})

const giaoVienTheoChuyenMon = computed(() => {
  if (!formLecturer.chuyenMon) return []
  return giaoVienDangDay.value.filter((gv) => chuanHoaChuyenMon(gv.chuyenMon) === formLecturer.chuyenMon)
})

/* ---------- ALERTS ---------- */
const canhBaoTheoLop = computed(() => {
  if (!lopDangChon.value) return []
  const lop = lopDangChon.value
  const out = []

  if (!coGiaoVienChinh.value) {
    out.push({ id: 'gv', type: 'warning', tab: 'Phân công giảng viên', title: 'Chưa có giảng viên chính', desc: `Lớp ${lop.maLop || ''} cần phân công giảng viên chính trước khi sinh lịch.` })
  }
  if (soBuoiConLai.value > 0) {
    out.push({ id: 'buoi', type: 'danger', tab: 'Điều phối lịch học', title: `Thiếu ${soBuoiConLai.value} buổi học`, desc: `Đã xếp ${soBuoiDaXep.value}/${lop.soBuoiHoc || '-'} buổi theo quy định.` })
  }
  if (conflictClassIds.value.has(Number(lop.id))) {
    const tt = timXungDot(lop.id)
    out.push({ id: 'xungdot', type: 'danger', tab: 'Lịch học', title: 'Nguy cơ trùng lịch phòng', desc: tt || 'Có buổi học bị trùng phòng / ca với lớp khác.' })
  }
  return out
})

/* ---------- LOAD ---------- */
onMounted(taiDuLieu)

async function taiDuLieu() {
  dangTai.value = true
  loi.value = ''
  phanCongLoi.value = ''
  phanCongOk.value = ''
  dieuPhoiLoi.value = ''
  dieuPhoiOk.value = ''

  const nguon = [
    ['danh sách ngành', () => giangDayService.layDanhSachNganh(), (v) => (danhSachNganh.value = v)],
    ['danh sách chương trình', () => giangDayService.layDanhSachChuongTrinh(), (v) => (danhSachChuongTrinh.value = v)],
    ['version chương trình', () => giangDayService.layDanhSachChuongTrinhVersion(), (v) => (danhSachVersion.value = v)],
    ['khung kỳ', () => giangDayService.layDanhSachKhungKy(), (v) => (danhSachKhungKy.value = v)],
    ['chương trình - môn', () => giangDayService.layDanhSachChuongTrinhMon(), (v) => (danhSachChuongTrinhMon.value = v)],
    ['môn học', () => giangDayService.layDanhSachMonHoc(), (v) => (danhSachMonHoc.value = v)],
    ['lớp học phần', () => giangDayService.layTrangLopHocPhan({ size: 500 }), (v) => (danhSachLopHocPhan.value = v.content)],
    ['lớp - CT môn', () => giangDayService.layTrangLopHocPhanChuongTrinhMon({ size: 500 }), (v) => (danhSachLopHocPhanChuongTrinhMon.value = v.content)],
    ['lịch học', () => giangDayService.layTrangLichHoc({ size: 500 }), (v) => (danhSachLichHoc.value = v.content)],
    ['giáo viên', () => giangDayService.layDanhSachGiaoVien(), (v) => (danhSachGiaoVien.value = v)],
    ['phòng học', () => giangDayService.layDanhSachPhongHoc(), (v) => (danhSachPhongHoc.value = v)],
    ['ca học', () => giangDayService.layDanhSachCaHoc(), (v) => (danhSachCaHoc.value = v)],
    ['phân công giảng dạy', () => giangDayService.layTrangPhanCongGiangDay({ size: 500 }), (v) => (danhSachPhanCong.value = v.content)],
    ['cấu hình đánh giá', () => giangDayService.layTrangCauHinhDanhGia({ size: 1000 }), (v) => (danhSachCauHinhDanhGia.value = v.content)]
  ]

  try {
    const ketQua = await Promise.allSettled(nguon.map(([, goi]) => goi()))
    const loiTai = []

    ketQua.forEach((kq, i) => {
      if (kq.status === 'fulfilled') {
        try { nguon[i][2](kq.value) } catch (e) { loiTai.push(`${nguon[i][0]} (xử lý dữ liệu lỗi)`) }
      } else {
        loiTai.push(`${nguon[i][0]}: ${layThongBaoLoi(kq.reason, 'không tải được')}`)
      }
    })

    if (loiTai.length) {
      loi.value = `Một số dữ liệu tải lỗi → ${loiTai.join(' | ')}`
    }

    chuanHoaLuaChon()

    if (!lopDangChon.value && danhSachHienThi.value.length) {
      chonLop(danhSachHienThi.value[0], true)
    }
  } catch (error) {
    loi.value = layThongBaoLoi(error, 'Không tải được dữ liệu lớp học phần')
  } finally {
    dangTai.value = false
  }
}

async function napLaiPhanCong() {
  const page = await giangDayService.layTrangPhanCongGiangDay({ size: 500 })
  danhSachPhanCong.value = page.content
}

async function napLaiLichHoc() {
  const page = await giangDayService.layTrangLichHoc({ size: 500 })
  danhSachLichHoc.value = page.content
}

/* ---------- SELECT + NAV ---------- */
function chonLop(lop, imLang = false) {
  if (!lop) return
  lopDangChonId.value = lop.id
  previewLich.value = null
  phanCongLoi.value = ''
  phanCongOk.value = ''
  dieuPhoiLoi.value = ''
  dieuPhoiOk.value = ''
  formLecturer.chuyenMon = ''
  formLecturer.giaoVienId = ''
  formLecturer.vaiTro = 'giang_vien_chinh'
  formSinhLich.phongHocId = null
  formSinhLich.caHocIds = []
  formSinhLich.tuNgay = lop.ngayBatDau || ''
  formSinhLich.denNgay = lop.ngayKetThuc || ''
  formSinhLich.thuTrongTuan = []

  // Nếu lớp đã có lịch: suy ngược phòng/ca/thứ đã dùng để hiển thị sẵn lên form điều phối
  const cauHinhCu = suyRaCauHinhTuLich(lop.id)
  if (cauHinhCu) {
    if (cauHinhCu.phongHocId != null) formSinhLich.phongHocId = cauHinhCu.phongHocId
    if (cauHinhCu.caHocIds?.length) formSinhLich.caHocIds = [...cauHinhCu.caHocIds]
    else if (cauHinhCu.caHocId != null) formSinhLich.caHocIds = [cauHinhCu.caHocId]
    if (cauHinhCu.thu.length) formSinhLich.thuTrongTuan = cauHinhCu.thu
  }

  if (!imLang) showToast(`Đã chọn lớp ${lop.maLop || lop.id}`)
}

function moDieuPhoi(lop) { chonLop(lop); activeTab.value = 'Điều phối lịch học' }
function moPhanCong(lop) { chonLop(lop); activeTab.value = 'Phân công giảng viên' }
function moTabDieuPhoi() { if (lopDangChon.value) activeTab.value = 'Điều phối lịch học' }

function xemBuoiHoc(buoi) {
  router.push({
    name: 'GiangDay.ChiTietBuoiHoc',
    params: { lopHocPhanId: lopDangChon.value.id, lichHocId: buoi.id },
    query: queryFlow()
  })
}

/* ---------- LECTURER ACTIONS (API) ---------- */
// Kiểm tra toàn bộ các trường hợp ở FE để báo ngay tại chỗ (đồng bộ với validator BE)
function kiemTraPhanCong() {
  const lop = lopDangChon.value
  if (!lop) return 'Chưa chọn lớp học phần'
  if (!formLecturer.chuyenMon) return 'Vui lòng chọn chuyên môn giảng dạy'
  if (!formLecturer.giaoVienId) return 'Vui lòng chọn giáo viên'
  if (!formLecturer.vaiTro) return 'Vui lòng chọn vai trò giảng dạy'

  if (lop.trangThai === 'huy') return 'Lớp học phần đã hủy, không được phân công giảng dạy'
  if (lop.trangThai === 'da_ket_thuc') return 'Lớp học phần đã kết thúc, không được phân công giảng dạy'

  const gv = danhSachGiaoVien.value.find((g) => bangId(g.id, formLecturer.giaoVienId))
  if (!gv) return 'Giáo viên không tồn tại'
  if (gv.trangThai !== 'dang_day') return 'Chỉ giáo viên đang dạy mới được phân công'

  const trungVaiTro = phanCongTheoLop.value.some(
      (item) => bangId(item.giaoVienId, formLecturer.giaoVienId) && item.vaiTro === formLecturer.vaiTro)
  if (trungVaiTro) return 'Giáo viên đã được phân công vai trò này trong lớp học phần'

  if (formLecturer.vaiTro === 'giang_vien_chinh' && coGiaoVienChinh.value) {
    return 'Lớp học phần đã có giảng viên chính'
  }
  return ''
}

async function themPhanCong() {
  phanCongLoi.value = ''
  phanCongOk.value = ''

  const loiKiemTra = kiemTraPhanCong()
  if (loiKiemTra) { phanCongLoi.value = loiKiemTra; showToast(loiKiemTra, 'error'); return }

  dangXuLy.value = true
  try {
    await giangDayService.taoPhanCongGiangDay({
      lopHocPhanId: Number(lopDangChon.value.id),
      giaoVienId: Number(formLecturer.giaoVienId),
      vaiTro: formLecturer.vaiTro
    })
    await napLaiPhanCong()
    formLecturer.giaoVienId = ''
    phanCongOk.value = 'Đã phân công giảng viên thành công'
    showToast(phanCongOk.value)
  } catch (error) {
    phanCongLoi.value = layThongBaoLoi(error, 'Phân công thất bại')
    showToast(phanCongLoi.value, 'error')
  } finally {
    dangXuLy.value = false
  }
}

async function xoaPhanCong(gv) {
  phanCongLoi.value = ''
  phanCongOk.value = ''
  if (!window.confirm(`Gỡ phân công ${gv.tenGiaoVien || ''} khỏi lớp ${lopDangChon.value?.maLop || ''}?`)) return
  dangXuLy.value = true
  try {
    await giangDayService.xoaPhanCongGiangDay(gv.id)
    await napLaiPhanCong()
    phanCongOk.value = 'Đã gỡ phân công'
    showToast(phanCongOk.value)
  } catch (error) {
    phanCongLoi.value = layThongBaoLoi(error, 'Gỡ phân công thất bại')
    showToast(phanCongLoi.value, 'error')
  } finally {
    dangXuLy.value = false
  }
}

/* ---------- SCHEDULE ACTIONS (API) ---------- */
function taoPayloadSinhLich() {
  const caHocIds = layCaHocIdsSinhLich()
  return {
    giaoVienId: giaoVienChinh.value?.giaoVienId || null,
    phongHocId: formSinhLich.phongHocId || null,
    caHocId: caHocIds.length === 1 ? caHocIds[0] : null,
    caHocIds,
    tuNgay: formSinhLich.tuNgay || null,
    denNgay: formSinhLich.denNgay || null,
    thuTrongTuan: formSinhLich.thuTrongTuan.map((x) => Number(x)),
    tuDongGanNoiDungSyllabus: true
  }
}

// Kiểm tra toàn bộ trường hợp sinh lịch ở FE (đồng bộ với DieuPhoiGiangDayServiceImpl)
function kiemTraSinhLich() {
  const lop = lopDangChon.value
  if (!lop) return 'Chưa chọn lớp học phần'
  if (lop.trangThai === 'huy') return 'Lớp học phần đã hủy, không được sinh lịch'
  if (lop.trangThai === 'da_ket_thuc') return 'Lớp học phần đã kết thúc, không được sinh lịch'
  if (!lop.soBuoiHoc || Number(lop.soBuoiHoc) < 1) return 'Lớp học phần chưa cấu hình số buổi học'
  if (!coGiaoVienChinh.value) return 'Lớp học phần chưa có giảng viên chính, hãy phân công trước'
  if (soBuoiConLai.value <= 0) return 'Lớp học phần đã xếp đủ số buổi học'
  if (!formSinhLich.phongHocId) return 'Vui lòng chọn phòng học'
  if (!layCaHocIdsSinhLich().length) return 'Vui lòng chọn ca học'
  const loiCa = kiemTraCaLienTucSinhLich()
  if (loiCa) return loiCa
  if (!formSinhLich.tuNgay || !formSinhLich.denNgay) return 'Vui lòng chọn từ ngày và đến ngày'
  if (formSinhLich.tuNgay > formSinhLich.denNgay) return 'Từ ngày không được sau đến ngày'
  if (lop.ngayBatDau && formSinhLich.tuNgay < lop.ngayBatDau) return 'Từ ngày không được trước ngày bắt đầu của lớp học phần'
  if (lop.ngayKetThuc && formSinhLich.denNgay > lop.ngayKetThuc) return 'Đến ngày không được sau ngày kết thúc của lớp học phần'
  if (!formSinhLich.thuTrongTuan.length) return 'Phải chọn ít nhất một thứ trong tuần'

  // Phòng học: chỉ phòng đang sử dụng + đủ sức chứa
  const phong = danhSachPhongHoc.value.find((p) => bangId(p.id, formSinhLich.phongHocId))
  if (phong) {
    if (phong.trangThai && phong.trangThai !== 'dang_su_dung') return 'Chỉ phòng học đang sử dụng mới được xếp lịch'
    if (phong.sucChua != null && lop.soLuongHienTai != null && Number(phong.sucChua) < Number(lop.soLuongHienTai)) {
      return 'Phòng học không đủ sức chứa'
    }
  }
  return ''
}

async function xemTruocLich() {
  dieuPhoiLoi.value = ''
  dieuPhoiOk.value = ''
  const loiKiemTra = kiemTraSinhLich()
  if (loiKiemTra) { dieuPhoiLoi.value = loiKiemTra; showToast(loiKiemTra, 'error'); return }

  dangXuLy.value = true
  previewLich.value = null
  try {
    previewLich.value = await giangDayService.previewSinhLich(lopDangChon.value.id, taoPayloadSinhLich())
    activeTab.value = 'Review lịch học'
    if (previewLich.value?.hopLe) {
      dieuPhoiOk.value = 'Đã tạo bản xem trước lịch học hợp lệ'
      showToast(dieuPhoiOk.value)
    } else {
      const cb = (previewLich.value?.canhBao || []).join(' | ') || 'Chưa xếp đủ buổi trong khoảng ngày đã chọn'
      dieuPhoiLoi.value = cb
      showToast('Bản xem trước chưa hợp lệ', 'error')
    }
  } catch (error) {
    dieuPhoiLoi.value = layThongBaoLoi(error, 'Không xem trước được lịch học')
    showToast(dieuPhoiLoi.value, 'error')
  } finally {
    dangXuLy.value = false
  }
}

async function luuSinhLich() {
  dieuPhoiLoi.value = ''
  dieuPhoiOk.value = ''
  if (!lopDangChon.value) { showToast('Chưa chọn lớp học phần', 'error'); return }
  if (activeTab.value !== 'Điều phối lịch học' && activeTab.value !== 'Review lịch học') {
    activeTab.value = 'Điều phối lịch học'
  }

  const loiKiemTra = kiemTraSinhLich()
  if (loiKiemTra) { dieuPhoiLoi.value = loiKiemTra; showToast(loiKiemTra, 'error'); return }

  dangXuLy.value = true
  try {
    await giangDayService.taoLichTuDong(lopDangChon.value.id, taoPayloadSinhLich())
    previewLich.value = null
    await napLaiLichHoc()
    dieuPhoiOk.value = 'Đã lưu và sinh lịch học tự động'
    showToast(dieuPhoiOk.value)
  } catch (error) {
    dieuPhoiLoi.value = layThongBaoLoi(error, 'Không lưu được lịch học tự động')
    showToast(dieuPhoiLoi.value, 'error')
  } finally {
    dangXuLy.value = false
  }
}

/* ---------- FILTER CASCADE ---------- */
function khiDoiNganh() {
  if (boLoc.chuongTrinhId && !chuongTrinhTheoNganh.value.some((item) => bangId(item.id, boLoc.chuongTrinhId))) boLoc.chuongTrinhId = ''
  khiDoiChuongTrinh()
}
function khiDoiChuongTrinh() {
  if (boLoc.versionId && !versionTheoChuongTrinh.value.some((item) => bangId(item.id, boLoc.versionId))) boLoc.versionId = ''
  khiDoiVersion()
}
function khiDoiVersion() {
  if (boLoc.khungKyId && !khungKyTheoBoLoc.value.some((item) => bangId(item.id, boLoc.khungKyId))) boLoc.khungKyId = ''
}
function chuanHoaLuaChon() { khiDoiNganh(); khiDoiChuongTrinh(); khiDoiVersion() }

function xoaBoLoc() {
  boLoc.keyword = ''
  boLoc.nganhId = ''
  boLoc.chuongTrinhId = ''
  boLoc.versionId = ''
  boLoc.khungKyId = ''
  boLoc.monHoc = ''
  boLoc.loaiLopHocPhan = ''
  boLoc.trangThaiXep = ''
  showToast('Đã xóa bộ lọc')
}

function queryFlow() {
  const q = {}
  if (boLoc.nganhId) q.nganhId = boLoc.nganhId
  if (boLoc.chuongTrinhId) q.chuongTrinhId = boLoc.chuongTrinhId
  if (boLoc.versionId) q.versionId = boLoc.versionId
  if (boLoc.khungKyId) q.khungKyId = boLoc.khungKyId
  return q
}

/* ---------- DERIVED HELPERS ---------- */
function demLichHoc(lopId) {
  return danhSachLichHoc.value.filter((item) => bangId(item.lopHocPhanId, lopId)).length
}
function soBuoiDaXepCuaLop(lop) {
  return danhSachLichHoc.value.filter((item) => bangId(item.lopHocPhanId, lop.id) && item.trangThai !== 'nghi').length
}
function coGiaoVienChinhCuaLop(lop) {
  return danhSachPhanCong.value.some((item) => bangId(item.lopHocPhanId, lop.id) && item.vaiTro === 'giang_vien_chinh')
}
function giaoVienCuaLop(lop) {
  const ds = danhSachPhanCong.value.filter((item) => bangId(item.lopHocPhanId, lop.id))
  if (ds.length === 0) return null
  const chinh = ds.find((item) => item.vaiTro === 'giang_vien_chinh')
  const dat = chinh || ds[0]
  const gv = danhSachGiaoVien.value.find((g) => bangId(g.id, dat.giaoVienId))
  const ten = dat.tenGiaoVien || dat.hoTen || gv?.hoTen || gv?.tenGiaoVien || (dat.maGiaoVien || gv?.maGiaoVien) || '—'
  return { ten, coChinh: Boolean(chinh), them: Math.max(0, ds.length - 1) }
}
function trangThaiXepLich(lop) {
  const tong = Number(lop.soBuoiHoc || 0)
  const daXep = soBuoiDaXepCuaLop(lop)
  if (tong > 0 && daXep >= tong) return 'Hoàn thành'
  if (daXep === 0) return 'Chưa xếp'
  return 'Đang xếp lịch'
}
function classTrangThaiXep(value) {
  if (value === 'Hoàn thành') return 'done'
  if (value === 'Đang xếp lịch') return 'active'
  return 'pending'
}
function timXungDot(lopId) {
  for (const l of danhSachLichHoc.value) {
    if (!bangId(l.lopHocPhanId, lopId)) continue
    if (!l.ngayHoc || l.caHocId == null || l.phongHocId == null) continue
    const trung = danhSachLichHoc.value.find((o) =>
        !bangId(o.lopHocPhanId, lopId) && o.ngayHoc === l.ngayHoc &&
        Number(o.caHocId) === Number(l.caHocId) && Number(o.phongHocId) === Number(l.phongHocId))
    if (trung) {
      return `Phòng ${tenPhongTheoId(l.phongHocId)} trùng lịch ngày ${l.ngayHoc} (${tenCaTheoId(l.caHocId)}).`
    }
  }
  return ''
}

function chuanHoaPhanCong(item) {
  const gv = danhSachGiaoVien.value.find((g) => bangId(g.id, item.giaoVienId))
  return {
    id: item.id,
    giaoVienId: item.giaoVienId,
    vaiTro: item.vaiTro,
    maGiaoVien: item.maGiaoVien || gv?.maGiaoVien || '',
    tenGiaoVien: item.tenGiaoVien || item.hoTen || gv?.hoTen || gv?.tenGiaoVien || 'Chưa có tên',
    email: item.emailGiaoVien || item.email || gv?.email || '',
    chuyenMon: item.chuyenMon || gv?.chuyenMon || '-'
  }
}

/* ---------- CHUONG TRINH MON RESOLUTION ---------- */
function layChuongTrinhMonIdsTheoLop(lop) {
  if (!lop) return []
  const ids = []
  if (lop.chuongTrinhMonId) ids.push(Number(lop.chuongTrinhMonId))
  danhSachLopHocPhanChuongTrinhMon.value
      .filter((item) => bangId(item.lopHocPhanId, lop.id))
      .forEach((item) => { if (item.chuongTrinhMonId) ids.push(Number(item.chuongTrinhMonId)) })
  return [...new Set(ids)]
}
function layChuongTrinhMonTheoLop(lop) {
  const ids = layChuongTrinhMonIdsTheoLop(lop)
  return danhSachChuongTrinhMon.value.filter((item) => ids.includes(Number(item.id)))
}
function lopDaGanMonHoc(lop) {
  return Boolean(lop?.chuongTrinhMonId || lop?.monHocId || lop?.maMonHoc || lop?.tenMonHoc || layChuongTrinhMonIdsTheoLop(lop).length > 0)
}
function lopThuocNganh(lop, id) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => {
    const v = timTheoId(danhSachVersion.value, ctm.chuongTrinhVersionId)
    const ct = timTheoId(danhSachChuongTrinh.value, v?.chuongTrinhId)
    return bangId(ct?.nganhId, id)
  })
}
function lopThuocChuongTrinh(lop, id) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => bangId(timTheoId(danhSachVersion.value, ctm.chuongTrinhVersionId)?.chuongTrinhId, id))
}
function lopThuocVersion(lop, id) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => bangId(ctm.chuongTrinhVersionId, id))
}
function lopThuocKhungKy(lop, id) {
  return layChuongTrinhMonTheoLop(lop).some((ctm) => bangId(ctm.khungKyId, id))
}
function thongTinMonHocTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const monHocId = lop?.monHocId || ctm?.monHocId
  const mh = timTheoId(danhSachMonHoc.value, monHocId)
  return {
    ma: lop?.maMonHoc || ctm?.maMonHoc || mh?.maMon || mh?.maMonHoc || '-',
    ten: lop?.tenMonHoc || ctm?.tenMonHoc || mh?.tenMon || mh?.tenMonHoc || (monHocId ? String(monHocId) : '-')
  }
}
function tenChuongTrinhTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const v = timTheoId(danhSachVersion.value, ctm?.chuongTrinhVersionId)
  return hienThiChuongTrinh(timTheoId(danhSachChuongTrinh.value, v?.chuongTrinhId)) || '-'
}
function tenVersionTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const v = timTheoId(danhSachVersion.value, ctm?.chuongTrinhVersionId)
  return v?.maVersion || v?.ten || v?.tenVersion || (v ? `${v.id}` : '-')
}
function tenKyTheoLop(lop) {
  const ctm = layChuongTrinhMonTheoLop(lop)[0]
  const k = timTheoId(danhSachKhungKy.value, ctm?.khungKyId)
  return k?.tenKy || k?.maKy || ctm?.khungKyId || '-'
}

/* ---------- DISPLAY HELPERS ---------- */
function hienThiLoaiLop(value) {
  return { CHUYEN_NGANH: 'Chuyên ngành', HOC_CHUNG: 'Học chung' }[value] || value || '-'
}
function hienThiVaiTro(value) {
  return { giang_vien_chinh: 'Giảng viên chính', tro_giang: 'Trợ giảng', coi_thi: 'Coi thi', cham_thi: 'Chấm thi' }[value] || value || '-'
}
function hienThiTrangThaiLichHoc(value) {
  return { du_kien: 'Dự kiến', da_day: 'Đã dạy', nghi: 'Nghỉ', day_bu: 'Dạy bù' }[value] || value || 'Dự kiến'
}
function classTrangThaiLichHoc(value) {
  return { du_kien: 'pending', da_day: 'done', nghi: 'cancel', day_bu: 'active' }[value] || 'pending'
}
function hienThiNganh(item) { return item ? `${item.maNganh || item.ma || '-'} - ${item.tenNganh || item.ten || item.id}` : '' }
function hienThiChuongTrinh(item) { return item ? `${item.maChuongTrinh || item.ma || '-'} - ${item.tenChuongTrinh || item.ten || item.id}` : '' }
function hienThiVersion(item) { return item ? `${item.maVersion || item.ma || '-'} - ${item.tenVersion || item.ten || ('Version ' + item.id)}` : '' }
function hienThiKhungKy(item) { return item ? `${item.maKy || item.ma || '-'} - ${item.tenKy || item.ten || ('Kỳ ' + item.id)}` : '' }
function hienThiPhongHoc(item) {
  if (!item) return '-'
  const sc = item.sucChua ? ` - ${item.sucChua} chỗ` : ''
  return `${item.maPhong ? item.maPhong + ' - ' : ''}${item.tenPhong || item.id}${sc}`
}
function hienThiCaHoc(item) {
  if (!item) return '-'
  const gio = item.gioBatDau && item.gioKetThuc ? ` (${item.gioBatDau} - ${item.gioKetThuc})` : ''
  return `${item.maCa ? item.maCa + ' - ' : ''}${item.tenCa || item.id}${gio}`
}
function hienThiGiaoVienOption(gv) {
  if (!gv) return '-'
  return `${gv.maGiaoVien || ('GV' + gv.id)} - ${gv.email || 'Chưa có email'} | ${gv.hoTen || gv.tenGiaoVien || 'Chưa có tên'}`
}
function tenGiaoVienTheoId(id) {
  const gv = danhSachGiaoVien.value.find((g) => bangId(g.id, id))
  return gv ? (gv.hoTen || gv.tenGiaoVien || gv.maGiaoVien || id) : (id || '-')
}
function tenPhongTheoId(id) {
  const p = danhSachPhongHoc.value.find((x) => bangId(x.id, id))
  return p ? (p.maPhong || p.tenPhong || id) : (id || '-')
}
function tenCaTheoId(id) {
  const c = danhSachCaHoc.value.find((x) => bangId(x.id, id))
  return c ? (c.tenCa || c.maCa || id) : (id || '-')
}

/* ---------- NGÀY / THỨ ---------- */
// Lấy đối tượng Date theo giờ local từ chuỗi 'yyyy-MM-dd' (tránh lệch múi giờ)
function parseNgay(ngay) {
  if (!ngay) return null
  const s = String(ngay).slice(0, 10)
  const [y, m, d] = s.split('-').map(Number)
  if (!y || !m || !d) return null
  return new Date(y, m - 1, d)
}
// JS getDay(): 0=CN..6=T7  ->  1=Thứ 2 ... 7=Chủ nhật (đồng bộ thuTrongTuan của BE)
function soThuTrongTuan(ngay) {
  const dt = parseNgay(ngay)
  if (!dt) return null
  const js = dt.getDay()
  return js === 0 ? 7 : js
}
function tenThu(ngay) {
  const map = { 1: 'Thứ 2', 2: 'Thứ 3', 3: 'Thứ 4', 4: 'Thứ 5', 5: 'Thứ 6', 6: 'Thứ 7', 7: 'Chủ nhật' }
  return map[soThuTrongTuan(ngay)] || '-'
}
function dinhDangNgay(ngay) {
  const dt = parseNgay(ngay)
  if (!dt) return ngay || '-'
  const dd = String(dt.getDate()).padStart(2, '0')
  const mm = String(dt.getMonth() + 1).padStart(2, '0')
  return `${dd}/${mm}/${dt.getFullYear()}`
}
// Suy ngược cấu hình điều phối từ lịch đã xếp: phòng/ca hay gặp nhất + các thứ đã có
function suyRaCauHinhTuLich(lopId) {
  const ds = danhSachLichHoc.value.filter((item) => bangId(item.lopHocPhanId, lopId) && item.trangThai !== 'nghi')
  if (ds.length === 0) return null

  const phongHocId = phoBienNhat(ds.map((x) => x.phongHocId))
  const caHocId = phoBienNhat(ds.map((x) => x.caHocId))
  const thu = [...new Set(ds.map((x) => soThuTrongTuan(x.ngayHoc)).filter((x) => x != null))].sort((a, b) => a - b)
  return { phongHocId, caHocId, caHocIds: caHocId != null ? [caHocId] : [], thu }
}
function phoBienNhat(arr) {
  const dem = new Map()
  arr.filter((x) => x != null).forEach((x) => dem.set(Number(x), (dem.get(Number(x)) || 0) + 1))
  let best = null, max = 0
  dem.forEach((v, k) => { if (v > max) { max = v; best = k } })
  return best
}

/* ---------- UTILS ---------- */
function chuanHoaChuyenMon(value) { return String(value || '').trim() || 'Chưa có chuyên môn' }
function timTheoId(list, id) { return id == null ? null : (list.find((item) => bangId(item.id, id)) || null) }
function timTen(list, id, fn) { return fn(timTheoId(list, id)) || `ID ${id}` }
function bangId(a, b) {
  if (a == null || b == null || a === '' || b === '') return false
  return Number(a) === Number(b)
}
function giaTriQuery(value) {
  if (Array.isArray(value)) return value[0] ? String(value[0]) : ''
  return value ? String(value) : ''
}
function layThongBaoLoi(error, fallback) {
  return error?.response?.data?.message || error?.response?.data?.error || error?.message || fallback
}
function showToast(message, type = 'success') {
  const id = Date.now() + Math.random()
  toasts.value.push({ id, message, type })
  setTimeout(() => { toasts.value = toasts.value.filter((t) => t.id !== id) }, 3000)
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700;900&display=swap');

.gd-page {
  --bd: #e2e8f0;
  --muted: #64748b;
  --text: #0f172a;
  --primary: var(--color-primary, #2563eb);
  display: flex;
  flex-direction: column;
  gap: 16px;
  color: var(--text);
  font-family: 'Roboto', Arial, sans-serif;
  font-size: 13px;
}

/* TOAST */
.toast-wrap { position: fixed; top: 16px; right: 16px; z-index: 60; display: flex; flex-direction: column; gap: 8px; }
.toast { padding: 10px 14px; border-radius: 10px; font-size: 13px; font-weight: 700; box-shadow: 0 6px 18px rgba(15, 23, 42, .12); border: 1px solid; }
.toast-success { background: #ecfdf5; color: #047857; border-color: #a7f3d0; }
.toast-error { background: #fef2f2; color: #b91c1c; border-color: #fecaca; }

/* HEADER */
.gd-header { display: flex; justify-content: space-between; align-items: center; gap: 12px; flex-wrap: wrap; }
.gd-title { display: flex; align-items: center; gap: 12px; }
.gd-title-icon { width: 44px; height: 44px; border-radius: 12px; background: var(--primary); color: #fff; display: grid; place-items: center; font-weight: 900; box-shadow: 0 6px 14px rgba(37, 99, 235, .25); }
.gd-title h1 { margin: 0; font-size: 18px; font-weight: 800; letter-spacing: -.02em; }
.gd-title p { margin: 3px 0 0; color: var(--muted); font-size: 12px; }
.gd-header-actions { display: flex; gap: 8px; flex-wrap: wrap; }

/* STATS */
.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 14px; }
.stat-card { display: flex; align-items: center; gap: 14px; background: #fff; border: 1px solid var(--bd); border-radius: 14px; padding: 14px; }
.stat-ico { width: 46px; height: 46px; border-radius: 12px; display: grid; place-items: center; font-size: 20px; }
.stat-ico.blue { background: #eff6ff; color: #2563eb; }
.stat-ico.orange { background: #fff7ed; color: #ea580c; }
.stat-ico.amber { background: #fffbeb; color: #d97706; }
.stat-ico.red { background: #fef2f2; color: #dc2626; }
.stat-card span { display: block; font-size: 11px; font-weight: 700; text-transform: uppercase; color: #94a3b8; letter-spacing: .02em; }
.stat-card strong { font-size: 22px; font-weight: 800; color: var(--text); }
.stat-card strong em { font-size: 12px; font-weight: 600; color: var(--muted); font-style: normal; }
.stat-card strong.orange { color: #ea580c; } .stat-card strong.amber { color: #d97706; } .stat-card strong.red { color: #dc2626; }

/* WORKSPACE */
.gd-workspace { display: grid; grid-template-columns: 5fr 7fr; gap: 16px; align-items: start; }
.gd-left, .gd-right { display: flex; flex-direction: column; gap: 14px; }

/* CARDS */
.card { background: #fff; border: 1px solid var(--bd); border-radius: 14px; padding: 14px; }
.card.no-pad { padding: 0; overflow: hidden; }
.card-head { display: flex; justify-content: space-between; align-items: center; gap: 10px; padding-bottom: 10px; border-bottom: 1px solid #f1f5f9; margin-bottom: 12px; }
.card-head.sm { margin-bottom: 12px; }
.card-head-left { display: flex; align-items: center; gap: 8px; }
.card-head h2, .card-head-left h2 { margin: 0; font-size: 13px; font-weight: 800; text-transform: uppercase; letter-spacing: .03em; color: #475569; }
.card-head-left h3 { margin: 0; font-size: 13px; font-weight: 800; text-transform: uppercase; letter-spacing: .03em; color: #334155; }
.step { background: #eff6ff; color: #2563eb; font-weight: 800; font-size: 11px; padding: 2px 7px; border-radius: 6px; }
.count-badge { background: #f1f5f9; color: #475569; padding: 4px 10px; border-radius: 999px; font-size: 12px; font-weight: 700; }

/* FILTERS */
.filter-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; }
.filter-grid .span-2 { grid-column: 1 / -1; }
.filter-foot { display: flex; justify-content: flex-end; margin-top: 10px; }
.list-sub { font-size: 11px; font-weight: 600; color: #64748b; background: #f8fafc; border: 1px solid #f1f5f9; border-radius: 8px; padding: 6px 10px; margin-bottom: 10px; }

label { display: flex; flex-direction: column; gap: 5px; min-width: 0; }
label > span { font-size: 10px; font-weight: 700; text-transform: uppercase; color: #94a3b8; letter-spacing: .02em; }
input, select { width: 100%; min-height: 34px; border: 1px solid var(--bd); border-radius: 9px; background: #f8fafc; color: var(--text); padding: 6px 9px; font: inherit; outline: none; }
input:focus, select:focus { border-color: var(--primary); background: #fff; box-shadow: 0 0 0 3px rgba(37, 99, 235, .1); }

/* TABLES */
.table-wrap { overflow: auto; border: 1px solid #eef2f7; border-radius: 10px; }
.list-scroll { max-height: 560px; }
.data-table { width: 100%; border-collapse: collapse; }
.list-table { min-width: 1040px; }
.gv-name { font-weight: 600; color: #1f2937; }
.gv-warn { color: #b45309 !important; font-weight: 700; }
.data-table th { position: sticky; top: 0; z-index: 1; background: #f8fafc; color: #64748b; font-size: 10px; font-weight: 800; text-transform: uppercase; letter-spacing: .03em; padding: 9px 10px; text-align: left; border-bottom: 1px solid #eef2f7; }
.data-table td { padding: 9px 10px; border-bottom: 1px solid #f1f5f9; font-size: 12.5px; color: #1f2937; vertical-align: middle; }
.data-table tr:last-child td { border-bottom: 0; }
.data-table .center { text-align: center; }
.data-table .right { text-align: right; }
.data-table .muted { color: var(--muted); }
.data-table .bold { font-weight: 700; }
.ellipsis { max-width: 170px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.code-text { color: var(--primary); font-weight: 800; }
.stack-text { display: grid; gap: 2px; }
.stack-text small { color: var(--muted); font-size: 11px; }
.row-click { cursor: pointer; transition: background .12s; }
.row-click:hover { background: #f8fafc; }
.row-click.selected { background: #eff6ff; }
.row-click.selected td { font-weight: 600; }
.empty { text-align: center; color: var(--muted); padding: 26px 10px; }

.row-actions { display: flex; gap: 6px; justify-content: flex-end; }
.icon-btn { width: 28px; height: 28px; border: 1px solid var(--bd); border-radius: 8px; background: #fff; cursor: pointer; color: #475569; font-size: 13px; }
.icon-btn:hover { background: #f1f5f9; color: var(--primary); }

/* PILLS */
.type-pill { display: inline-block; padding: 2px 8px; border-radius: 6px; font-size: 10px; font-weight: 800; border: 1px solid; }
.type-pill.indigo { background: #eef2ff; color: #4338ca; border-color: #c7d2fe; }
.type-pill.purple { background: #faf5ff; color: #7e22ce; border-color: #e9d5ff; }
.status-pill { display: inline-block; padding: 2px 9px; border-radius: 999px; font-size: 10px; font-weight: 800; border: 1px solid; background: #f1f5f9; color: #475569; border-color: #e2e8f0; white-space: nowrap; }
.status-pill.active { background: #eff6ff; color: #1d4ed8; border-color: #bfdbfe; }
.status-pill.done { background: #ecfdf5; color: #047857; border-color: #a7f3d0; }
.status-pill.pending { background: #fffbeb; color: #b45309; border-color: #fde68a; }
.status-pill.cancel { background: #fef2f2; color: #b91c1c; border-color: #fecaca; }

/* OVERVIEW */
.overview-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px 16px; }
.ov { display: grid; gap: 2px; }
.ov span { font-size: 10px; font-weight: 700; text-transform: uppercase; color: #94a3b8; }
.ov strong { font-size: 13px; font-weight: 700; color: var(--text); }
.ov strong.green { color: #047857; } .ov strong.amber { color: #d97706; }

/* TABS */
.tab-row { display: flex; flex-wrap: wrap; border-bottom: 1px solid #f1f5f9; background: #f8fafc; }
.tab-btn { flex: 1; min-width: 120px; padding: 12px 10px; font-size: 12px; font-weight: 800; color: #64748b; background: none; border: 0; border-bottom: 2px solid transparent; cursor: pointer; display: inline-flex; align-items: center; justify-content: center; gap: 6px; }
.tab-warn { display: inline-grid; place-items: center; width: 18px; height: 18px; border-radius: 999px; background: #fef3c7; color: #d97706; font-size: 11px; line-height: 1; border: 1px solid #fde68a; }
.thu-pill { display: inline-block; padding: 1px 8px; border-radius: 6px; font-size: 11px; font-weight: 800; background: #eef2ff; color: #4338ca; border: 1px solid #e0e7ff; white-space: nowrap; }
.tab-btn:hover { color: #334155; background: #f1f5f9; }
.tab-btn.active { color: var(--primary); background: #fff; border-bottom-color: var(--primary); }
.tab-body { padding: 18px; }
.tab-head { display: flex; justify-content: space-between; align-items: center; gap: 10px; margin-bottom: 14px; }
.tab-foot { display: flex; justify-content: flex-end; gap: 8px; border-top: 1px solid #f1f5f9; padding-top: 14px; margin-top: 14px; }

.badge { font-size: 11px; font-weight: 800; padding: 3px 10px; border-radius: 999px; border: 1px solid; }
.badge.green { background: #ecfdf5; color: #047857; border-color: #a7f3d0; }
.badge.amber { background: #fffbeb; color: #b45309; border-color: #fde68a; }

/* FORM BOX */
.form-box { display: grid; grid-template-columns: repeat(2, 1fr); gap: 12px; background: #f8fafc; border: 1px solid #f1f5f9; border-radius: 12px; padding: 14px; margin-bottom: 16px; align-items: end; }
.form-box.four { grid-template-columns: repeat(2, 1fr); }
.form-box .form-box-action { display: flex; align-items: end; }
.form-box input, .form-box select { background: #fff; }

/* ASSIGNED CARD */
.assigned-card { display: flex; justify-content: space-between; align-items: center; gap: 12px; border: 1px solid #bfdbfe; background: #eff6ff; border-radius: 12px; padding: 12px 14px; margin-bottom: 16px; }
.assigned-card.warning { border-color: #fed7aa; background: #fff7ed; }
.assigned-card span { font-size: 11px; font-weight: 700; color: #64748b; text-transform: uppercase; }
.assigned-card strong { display: block; font-size: 15px; font-weight: 800; color: var(--text); margin-top: 2px; }
.assigned-card small { color: var(--muted); }

/* WEEKDAY */
.weekday-card { border: 1px solid #eef2f7; border-radius: 12px; padding: 14px; }
.weekday-card > strong { display: block; font-size: 10px; font-weight: 800; text-transform: uppercase; color: #94a3b8; margin-bottom: 10px; }
.weekday-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.weekday-row label { flex-direction: row; align-items: center; gap: 7px; background: #f8fafc; border: 1px solid #f1f5f9; border-radius: 9px; padding: 8px 10px; font-size: 12px; font-weight: 700; color: #334155; cursor: pointer; }
.weekday-row input { width: auto; min-height: auto; }

/* CHIPS */
.chip-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 14px; }
.chip { display: flex; justify-content: space-between; align-items: center; gap: 8px; border-radius: 12px; padding: 10px 12px; border: 1px solid; }
.chip span { font-size: 11px; font-weight: 700; }
.chip strong { font-size: 12px; font-weight: 800; }
.chip.blue { background: #eff6ff; border-color: #dbeafe; color: #1d4ed8; }
.chip.indigo { background: #eef2ff; border-color: #e0e7ff; color: #4338ca; }
.chip.green { background: #ecfdf5; border-color: #d1fae5; color: #047857; }

/* ALERT LIST */
.alert-list { display: flex; flex-direction: column; gap: 12px; }
.alert-item { display: flex; justify-content: space-between; align-items: center; gap: 12px; border-radius: 12px; border: 1px solid; padding: 14px; }
.alert-item.danger { border-color: #fecaca; background: #fef2f2; }
.alert-item.warning { border-color: #fde68a; background: #fffbeb; }
.alert-item-main { display: flex; gap: 12px; align-items: flex-start; }
.alert-dot { width: 32px; height: 32px; border-radius: 9px; display: grid; place-items: center; font-size: 15px; }
.alert-dot.danger { background: #fee2e2; color: #dc2626; }
.alert-dot.warning { background: #fef3c7; color: #d97706; }
.alert-item h5 { margin: 0; font-size: 13px; font-weight: 800; color: var(--text); }
.alert-item p { margin: 3px 0 0; font-size: 12px; color: var(--muted); }

/* BUOI LIST */
.buoi-list { display: flex; flex-direction: column; gap: 12px; }
.buoi-item { display: flex; justify-content: space-between; align-items: center; gap: 12px; border: 1px solid #eef2f7; background: #f8fafc; border-radius: 12px; padding: 14px; }
.buoi-item:hover { border-color: #bfdbfe; }
.buoi-left { display: flex; gap: 12px; align-items: center; }
.buoi-ico { width: 42px; height: 42px; border-radius: 11px; display: grid; place-items: center; color: #fff; font-size: 16px; }
.buoi-ico.blue { background: #2563eb; } .buoi-ico.amber { background: #f59e0b; }
.buoi-title { display: flex; align-items: center; gap: 8px; }
.buoi-title strong { font-size: 14px; font-weight: 800; }
.buoi-date { font-size: 11px; font-weight: 700; background: #f1f5f9; color: #475569; padding: 2px 7px; border-radius: 6px; }
.buoi-meta { font-size: 12px; color: var(--muted); margin-top: 4px; }
.buoi-meta b { color: var(--text); }
.buoi-right { display: flex; align-items: center; gap: 10px; }

/* BUTTONS */
.btn { min-height: 36px; border: 1px solid var(--bd); border-radius: 9px; background: #fff; color: #334155; padding: 7px 14px; cursor: pointer; font: inherit; font-size: 12px; font-weight: 700; transition: .15s; }
.btn:hover:not(:disabled) { background: #f8fafc; border-color: #cbd5e1; }
.btn:disabled { opacity: .55; cursor: not-allowed; }
.btn.small { min-height: 30px; padding: 5px 11px; font-size: 11px; }
.btn.primary { background: var(--primary); border-color: var(--primary); color: #fff; }
.btn.primary:hover:not(:disabled) { filter: brightness(.95); }
.btn.success { background: #16a34a; border-color: #16a34a; color: #fff; }
.btn.success:hover:not(:disabled) { background: #15803d; }
.btn.ghost-blue { background: #eff6ff; border-color: #bfdbfe; color: #1d4ed8; }
.btn.danger { color: #b91c1c; border-color: #fecaca; background: #fff5f5; }

/* ALERTS / EMPTY */
.alert { border-radius: 10px; padding: 10px 12px; font-size: 12px; font-weight: 700; }
.alert.mt { margin-top: 12px; }
.alert.error { border: 1px solid #fecaca; background: #fef2f2; color: #b91c1c; }
.alert.success { border: 1px solid #a7f3d0; background: #ecfdf5; color: #047857; }
.empty-box { text-align: center; color: var(--muted); border: 1px dashed #d0d5dd; border-radius: 12px; background: #f8fafc; padding: 22px; font-weight: 600; }
.empty-box.success-box { color: #047857; border-color: #a7f3d0; background: #ecfdf5; }
.preview-note { margin: 0; font-size: 12px; color: var(--muted); font-weight: 600; }
.mt { margin-top: 12px; }
.cell-main { display: grid; gap: 2px; }
.cell-main strong { color: #0f172a; }
.cell-main small { color: var(--muted); font-size: 11px; }
.green { color: #047857; }

/* RESPONSIVE */
@media (max-width: 1200px) {
  .gd-workspace { grid-template-columns: 1fr; }
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 760px) {
  .filter-grid, .form-box, .form-box.four, .overview-grid, .chip-grid { grid-template-columns: 1fr; }
  .weekday-row { grid-template-columns: repeat(2, 1fr); }
}

.ca-field { display: flex; flex-direction: column; gap: 8px; }
.ca-chip-grid { display: flex; flex-wrap: wrap; gap: 8px; }
.ca-chip { border: 1px solid var(--border); background: #fff; border-radius: 12px; padding: 8px 10px; font-weight: 700; cursor: pointer; color: var(--text); }
.ca-chip:hover { border-color: #2563eb; color: #1d4ed8; }
.ca-chip.active { background: #2563eb; border-color: #2563eb; color: #fff; box-shadow: 0 8px 18px rgba(37, 99, 235, .22); }
.hint { color: var(--muted); font-size: 12px; font-weight: 600; }

</style>