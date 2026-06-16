<template>
  <div class="gv-page">
    <!-- Header -->
    <div class="gv-header-bar">
      <div>
        <h1>Quản lý giáo viên giảng dạy</h1>
        <p>Quản lý hồ sơ, môn có thể dạy, số tiết đăng ký và thời gian giảng dạy của giáo viên.</p>
      </div>
      <button class="btn btn-primary" @click="batDauThemMoi">+ Thêm giáo viên</button>
    </div>

    <div v-if="thongBao" class="gv-alert success">{{ thongBao }}</div>
    <div v-if="loi" class="gv-alert error">{{ loi }}</div>

    <!-- Stats -->
    <div class="gv-stats-row">
      <div class="stat-card">
        <div class="stat-icon-wrap blue"><span>👤</span></div>
        <div><div class="stat-lbl">Tổng giáo viên</div><div class="stat-num blue">{{ thongKe.tong }}</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon-wrap green"><span>✅</span></div>
        <div><div class="stat-lbl">Đang hoạt động</div><div class="stat-num green">{{ thongKe.dangHoatDong }}</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon-wrap purple"><span>📚</span></div>
        <div><div class="stat-lbl">Đã cấu hình môn dạy</div><div class="stat-num purple">{{ thongKe.coMonDay }}</div></div>
      </div>
      <div class="stat-card">
        <div class="stat-icon-wrap orange"><span>📋</span></div>
        <div><div class="stat-lbl">Có đăng ký số tiết</div><div class="stat-num orange">{{ thongKe.coDangKyTiet }}</div></div>
      </div>
    </div>

    <!-- Tab bar -->
    <div class="gv-tab-nav">
      <button v-for="t in tabs" :key="t.key" :class="['tab-btn', { active: activeTab === t.key }]" @click="doiTab(t.key)">
        {{ t.label }}
      </button>
    </div>

    <!-- ============ Tab Hồ sơ ============ -->
    <template v-if="activeTab === 'ho-so'">
      <!-- Add / Edit form -->
      <div v-if="formMode" class="form-card">
        <div class="form-card-hdr">
          <span>{{ formMode === 'add' ? 'Thêm giáo viên mới' : 'Chỉnh sửa: ' + form.hoTen }}</span>
          <button class="btn-close" @click="dongFormGV">×</button>
        </div>
        <form class="form-grid" @submit.prevent="luuGiaoVien">
          <label class="ff"><span>Mã GV *</span><input v-model.trim="form.maGiaoVien" required /></label>
          <label class="ff"><span>Họ tên *</span><input v-model.trim="form.hoTen" required /></label>
          <label class="ff"><span>Email *</span><input v-model.trim="form.email" type="email" required /></label>
          <label class="ff"><span>SĐT</span><input v-model.trim="form.soDienThoai" /></label>
          <label class="ff"><span>Trạng thái</span>
            <select v-model="form.trangThai">
              <option value="dang_day">Đang dạy</option>
              <option value="tam_nghi">Tạm nghỉ</option>
              <option value="nghi_viec">Nghỉ việc</option>
            </select>
          </label>
          <label class="ff"><span>Chuyên môn</span><input v-model.trim="form.chuyenMon" /></label>
          <div class="ff-actions ff-span">
            <button type="button" class="btn btn-secondary" @click="dongFormGV">Hủy</button>
            <button type="submit" class="btn btn-primary" :disabled="dangLuuGV">
              {{ dangLuuGV ? '...' : (formMode === 'add' ? 'Thêm mới' : 'Cập nhật') }}
            </button>
          </div>
        </form>
      </div>

      <!-- Teacher table -->
      <div class="table-card">
        <div class="tbl-toolbar">
          <div class="tbl-filters">
            <input class="search-inp" v-model.trim="boLoc.keyword" @input="locDuLieu" placeholder="Tìm theo mã, tên, email, SĐT..." />
            <select class="filter-sel" v-model="boLoc.trangThai" @change="locDuLieu">
              <option value="">Tất cả trạng thái</option>
              <option value="dang_day">Đang dạy</option>
              <option value="tam_nghi">Tạm nghỉ</option>
              <option value="nghi_viec">Nghỉ việc</option>
            </select>
            <button class="btn-icon-sm" @click="taiDuLieu" title="Tải lại">⟳</button>
          </div>
          <div class="pg-info">{{ tongBanGhi }} giáo viên</div>
        </div>
        <div v-if="dangTai" class="loading-txt">Đang tải...</div>
        <div v-else class="tbl-wrap">
          <table class="data-tbl">
            <thead>
              <tr>
                <th>Mã GV</th><th>Họ tên</th><th>Email</th><th>SĐT</th>
                <th>Chuyên môn</th><th>Trạng thái</th><th>Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in danhSach" :key="item.id" :class="{ 'row-sel': selectedGiaoVien?.id === item.id }">
                <td><code class="code">{{ item.maGiaoVien }}</code></td>
                <td>{{ item.hoTen }}</td>
                <td>{{ item.email }}</td>
                <td>{{ item.soDienThoai || '—' }}</td>
                <td>{{ item.chuyenMon || '—' }}</td>
                <td><span :class="['badge', item.trangThai]">{{ hienThiTrangThai(item.trangThai) }}</span></td>
                <td class="td-acts">
                  <button class="btn btn-xs btn-outline" @click="quanLyGiaoVien(item)">Quản lý</button>
                  <button class="btn btn-xs btn-secondary" @click="suaGiaoVien(item)">Sửa</button>
                  <button class="btn btn-xs btn-danger" @click="xoaGiaoVien(item)">Xóa</button>
                </td>
              </tr>
              <tr v-if="!danhSach.length"><td colspan="7" class="empty-cell">Không có dữ liệu</td></tr>
            </tbody>
          </table>
        </div>
        <div class="pg-nav">
          <button @click="doiTrang(trangHienTai - 1)" :disabled="trangHienTai <= 0">‹</button>
          <span>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</span>
          <button @click="doiTrang(trangHienTai + 1)" :disabled="trangHienTai + 1 >= tongTrang">›</button>
        </div>
      </div>
    </template>

    <!-- ============ Tabs 2-5 ============ -->
    <template v-else>
      <!-- Đang quản lý bar -->
      <div class="managed-bar" :class="{ 'no-sel': !selectedGiaoVien }">
        <template v-if="selectedGiaoVien">
          <span class="mg-icon">👤</span>
          <span class="mg-text">Đang quản lý: <strong>{{ selectedGiaoVien.maGiaoVien }}</strong> – {{ selectedGiaoVien.hoTen }}{{ selectedGiaoVien.chuyenMon ? ' – ' + selectedGiaoVien.chuyenMon : '' }}</span>
          <div class="mg-actions">
            <button class="btn btn-xs btn-secondary" @click="doiGiaoVien">↩ Đổi giáo viên</button>
            <button class="btn btn-xs btn-outline" @click="xemHoSo">📋 Xem hồ sơ</button>
          </div>
        </template>
        <template v-else>
          <span class="mg-icon">👤</span>
          <span class="mg-text" style="color:#9ca3af">Chưa chọn giáo viên — bấm "Quản lý" trong danh sách bên dưới</span>
        </template>
      </div>

      <!-- Main split: teacher list left + tab content right -->
      <div class="content-split">
        <!-- LEFT: Teacher list -->
        <div class="split-left">
          <div class="split-left-hdr">
            <span class="split-left-title">Danh sách giáo viên</span>
            <div class="split-left-filters">
              <input class="search-sm" v-model.trim="boLoc.keyword" @input="locDuLieu" placeholder="Tìm theo mã, tên..." />
              <select class="sel-sm" v-model="boLoc.trangThai" @change="locDuLieu">
                <option value="">Tất cả trạng thái</option>
                <option value="dang_day">Đang dạy</option>
                <option value="tam_nghi">Tạm nghỉ</option>
                <option value="nghi_viec">Nghỉ việc</option>
              </select>
              <button class="btn-icon-sm" @click="taiDuLieu">⟳</button>
            </div>
          </div>
          <div v-if="dangTai" class="loading-txt">Đang tải...</div>
          <div v-else class="tbl-wrap-scroll">
            <table class="data-tbl data-tbl-sm">
              <thead>
                <tr>
                  <th>Mã GV</th><th>Họ tên</th><th>Email</th><th>SĐT</th>
                  <th>Chuyên môn</th><th>Trạng thái</th><th></th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="item in danhSach" :key="item.id" :class="{ 'row-sel': selectedGiaoVien?.id === item.id }">
                  <td><code class="code">{{ item.maGiaoVien }}</code></td>
                  <td>{{ item.hoTen }}</td>
                  <td>{{ item.email }}</td>
                  <td>{{ item.soDienThoai || '—' }}</td>
                  <td>{{ item.chuyenMon || '—' }}</td>
                  <td><span :class="['badge', item.trangThai]">{{ hienThiTrangThai(item.trangThai) }}</span></td>
                  <td class="td-acts">
                    <button class="btn btn-xs btn-outline" @click="quanLyGiaoVien(item)">Quản lý</button>
                    <button class="btn btn-xs btn-secondary" @click="suaGiaoVien(item)">Sửa</button>
                    <button class="btn btn-xs btn-danger" @click="xoaGiaoVien(item)">Xóa</button>
                  </td>
                </tr>
                <tr v-if="!danhSach.length"><td colspan="7" class="empty-cell">Không có dữ liệu</td></tr>
              </tbody>
            </table>
          </div>
          <div class="pg-nav sm">
            <button @click="doiTrang(trangHienTai - 1)" :disabled="trangHienTai <= 0">‹</button>
            <span>{{ trangHienTai + 1 }} / {{ tongTrang || 1 }}</span>
            <button @click="doiTrang(trangHienTai + 1)" :disabled="trangHienTai + 1 >= tongTrang">›</button>
          </div>
        </div>

        <!-- RIGHT: Tab content -->
        <div class="split-right">
          <div v-if="!selectedGiaoVien" class="empty-state">
            <div class="es-icon">📅</div>
            <p>Chọn một giáo viên để quản lý thông tin giảng dạy.</p>
          </div>

          <!-- === Tab: Môn có thể dạy === -->
          <div v-else-if="activeTab === 'mon-day'" class="tab-pane">
            <div class="pane-toolbar">
              <div class="pane-title-row">
                <span class="pane-title">Môn có thể dạy</span>
                <span class="pane-sub">Giáo viên: <strong>{{ selectedGiaoVien.maGiaoVien }} – {{ selectedGiaoVien.hoTen }}</strong></span>
              </div>
              <button class="btn btn-primary" @click="moFormMonDay">+ Thêm môn</button>
            </div>
            <div v-if="hienFormMonDay" class="inline-form">
              <label class="ff ff-span"><span>Môn học *</span>
                <select v-model="formMonDay.monHocId">
                  <option value="">-- Chọn môn --</option>
                  <option v-for="mh in danhSachMonHoc" :key="mh.id" :value="mh.id">{{ mh.maMon }} – {{ mh.tenMon }}</option>
                </select>
              </label>
              <label class="ff"><span>Trạng thái</span>
                <select v-model="formMonDay.trangThai">
                  <option value="dang_hieu_luc">Đang hiệu lực</option>
                  <option value="ngung_ap_dung">Ngưng áp dụng</option>
                </select>
              </label>
              <label class="ff"><span>Ghi chú</span><input v-model="formMonDay.ghiChu" /></label>
              <div v-if="loiMonDay" class="err-msg ff-span">{{ loiMonDay }}</div>
              <div class="ff-actions ff-span">
                <button class="btn btn-secondary" @click="huyFormMonDay">Hủy</button>
                <button class="btn btn-primary" @click="luuMonDay" :disabled="dangLuuMonDay">
                  {{ dangLuuMonDay ? '...' : (formMonDay.id ? 'Cập nhật' : 'Thêm mới') }}
                </button>
              </div>
            </div>
            <div v-if="dangTaiMonDay" class="loading-txt">Đang tải...</div>
            <div v-else-if="loiMonDay && !hienFormMonDay" class="err-msg" style="margin: 8px 0">{{ loiMonDay }}</div>
            <table v-else class="data-tbl">
              <thead><tr><th>Mã môn</th><th>Tên môn</th><th>Số TC</th><th>Trạng thái</th><th>Ghi chú</th><th></th></tr></thead>
              <tbody>
                <tr v-for="item in danhSachMonDay" :key="item.id">
                  <td><code class="code">{{ item.maMon }}</code></td>
                  <td>{{ item.tenMon }}</td>
                  <td class="tc">{{ item.soTinChi || '—' }}</td>
                  <td><span :class="['badge', item.trangThai]">{{ item.trangThai === 'dang_hieu_luc' ? 'Đang dạy' : 'Ngưng' }}</span></td>
                  <td>{{ item.ghiChu || '—' }}</td>
                  <td class="td-acts">
                    <button class="btn btn-xs btn-secondary" @click="chonSuaMonDay(item)">Sửa</button>
                    <button class="btn btn-xs btn-danger" @click="xoaMonDay(item.id)">Xóa</button>
                  </td>
                </tr>
                <tr v-if="!danhSachMonDay.length"><td colspan="6" class="empty-cell">Chưa có dữ liệu</td></tr>
              </tbody>
            </table>
            <div class="tbl-footer">{{ danhSachMonDay.length ? `1 – ${danhSachMonDay.length} / ${danhSachMonDay.length}` : '' }}</div>
          </div>

          <!-- === Tab: Đăng ký số tiết === -->
          <div v-else-if="activeTab === 'so-tiet'" class="tab-pane">
            <div class="pane-toolbar">
              <div class="pane-title-row">
                <span class="pane-title">Đăng ký số tiết</span>
                <span class="pane-sub">Giáo viên: <strong>{{ selectedGiaoVien.maGiaoVien }} – {{ selectedGiaoVien.hoTen }}</strong></span>
              </div>
              <button class="btn btn-primary" @click="moFormSoTiet">+ Thêm đăng ký</button>
            </div>
            <div v-if="hienFormSoTiet" class="inline-form">
              <label class="ff">
                <span>Ngành *</span>
                <select v-model="chonSoTiet.nganhId" @change="khiChonNganhSoTiet">
                  <option value="">-- Chọn ngành --</option>
                  <option v-for="nganh in danhSachNganhSoTiet" :key="nganh.id" :value="nganh.id">
                    {{ nganh.tenNganh || nganh.ten || nganh.maNganh }}
                  </option>
                </select>
              </label>

              <label class="ff">
                <span>Chương trình *</span>
                <select
                    v-model="chonSoTiet.chuongTrinhId"
                    @change="khiChonChuongTrinhSoTiet"
                    :disabled="!chonSoTiet.nganhId"
                >
                  <option value="">-- Chọn chương trình --</option>
                  <option v-for="ct in danhSachChuongTrinhSoTiet" :key="ct.id" :value="ct.id">
                    {{ ct.tenChuongTrinh || ct.ten || ct.maChuongTrinh }}
                  </option>
                </select>
              </label>

              <label class="ff">
                <span>Version *</span>
                <select
                    v-model="chonSoTiet.versionId"
                    @change="khiChonVersionSoTiet"
                    :disabled="!chonSoTiet.chuongTrinhId"
                >
                  <option value="">-- Chọn version --</option>
                  <option v-for="ver in danhSachVersionSoTiet" :key="ver.id" :value="ver.id">
                    {{ ver.tenVersion || ver.maVersion || ('Version ' + ver.id) }}
                  </option>
                </select>
              </label>

              <label class="ff">
                <span>Kỳ *</span>
                <select
                    v-model="formSoTiet.khungKyId"
                    :disabled="!chonSoTiet.versionId"
                >
                  <option value="">-- Chọn kỳ --</option>
                  <option v-for="ky in danhSachKhungKySoTiet" :key="ky.id" :value="ky.id">
                    {{ ky.tenKy || ky.maKy || ('Kỳ ' + ky.id) }}
                  </option>
                </select>
              </label>
              <label class="ff"><span>Số tiết đăng ký *</span><input type="number" v-model.number="formSoTiet.soTietDangKy" min="1" /></label>
              <label class="ff"><span>Trạng thái</span>
                <select v-model="formSoTiet.trangThai">
                  <option value="dang_hieu_luc">Đang hiệu lực</option>
                  <option value="ngung_ap_dung">Ngưng áp dụng</option>
                </select>
              </label>
              <label class="ff ff-span"><span>Ghi chú</span><input v-model="formSoTiet.ghiChu" /></label>
              <div v-if="loiSoTiet" class="err-msg ff-span">{{ loiSoTiet }}</div>
              <div class="ff-actions ff-span">
                <button class="btn btn-secondary" @click="huyFormSoTiet">Hủy</button>
                <button class="btn btn-primary" @click="luuSoTiet" :disabled="dangLuuSoTiet">
                  {{ dangLuuSoTiet ? '...' : (formSoTiet.id ? 'Cập nhật' : 'Thêm mới') }}
                </button>
              </div>
            </div>
            <div v-if="dangTaiSoTiet" class="loading-txt">Đang tải...</div>
            <table v-else class="data-tbl">
              <thead><tr><th>Kỳ/version</th><th>Số tiết ĐK</th><th>Đã PC</th><th>Còn lại</th><th>Trạng thái</th><th></th></tr></thead>
              <tbody>
                <tr v-for="item in danhSachSoTiet" :key="item.id">
                  <td>{{ item.labelKhungKyDayDu || item.maKy || layTenKhungKy(item.khungKyId) }}</td>
                  <td class="tc">{{ item.soTietDangKy }}</td>
                  <td class="tc">{{ item.soTietDaPhanCong ?? '—' }}</td>
                  <td class="tc" :class="{ 'txt-warn': (item.soTietDangKy - (item.soTietDaPhanCong || 0)) < 10 }">
                    {{ item.soTietDangKy - (item.soTietDaPhanCong || 0) }}
                  </td>
                  <td><span :class="['badge', item.trangThai]">{{ item.trangThai === 'dang_hieu_luc' ? 'Hiệu lực' : 'Ngưng' }}</span></td>
                  <td class="td-acts">
                    <button class="btn btn-xs btn-secondary" @click="chonSuaSoTiet(item)">Sửa</button>
                    <button class="btn btn-xs btn-danger" @click="xoaSoTiet(item.id)">Xóa</button>
                  </td>
                </tr>
                <tr v-if="!danhSachSoTiet.length"><td colspan="6" class="empty-cell">Chưa có dữ liệu</td></tr>
              </tbody>
            </table>
          </div>

          <!-- === Tab: Ca khả dụng === -->
          <div v-else-if="activeTab === 'ca-kha-dung'" class="tab-pane">
            <div class="pane-toolbar">
              <div class="pane-title-row">
                <span class="pane-title">Ca khả dụng</span>
                <span class="pane-sub">Giáo viên: <strong>{{ selectedGiaoVien.maGiaoVien }} – {{ selectedGiaoVien.hoTen }}</strong></span>
              </div>
              <button class="btn btn-primary" @click="moFormCaKhaDung">+ Thêm ca</button>
            </div>
            <div v-if="hienFormCaKhaDung" class="inline-form">
              <label class="ff"><span>Loại đăng ký</span>
                <select v-model="formCaKhaDung.loaiDangKy">
                  <option value="">-- Chọn loại --</option>
                  <option value="kha_dung">Khả dụng</option>
                  <option value="ban">Bận</option>
                  <option value="uu_tien">Ưu tiên</option>
                </select>
              </label>
              <label class="ff"><span>Mức độ ưu tiên</span><input type="number" v-model.number="formCaKhaDung.mucDoUuTien" min="1" max="10" placeholder="1–10" /></label>
              <label class="ff"><span>Khung kỳ</span>
                <select v-model="formCaKhaDung.khungKyId">
                  <option value="">Tất cả kỳ</option>
                  <option v-for="ky in danhSachKhungKy" :key="ky.id" :value="ky.id">{{ ky.maKy }}</option>
                </select>
              </label>
              <label class="ff"><span>Thứ trong tuần</span>
                <select v-model="formCaKhaDung.thuTrongTuan">
                  <option value="">Tất cả thứ</option>
                  <option v-for="t in thuList" :key="t.val" :value="t.val">{{ t.lbl }}</option>
                </select>
              </label>
              <label class="ff"><span>Ca học</span>
                <select v-model="formCaKhaDung.caHocId">
                  <option value="">Tất cả ca</option>
                  <option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">{{ ca.tenCa }}</option>
                </select>
              </label>
              <label class="ff"><span>Ghi chú</span><input v-model="formCaKhaDung.ghiChu" /></label>
              <label class="ff"><span>Từ ngày</span><input type="date" v-model="formCaKhaDung.tuNgay" /></label>
              <label class="ff"><span>Đến ngày</span><input type="date" v-model="formCaKhaDung.denNgay" /></label>
              <div v-if="loiCaKhaDung" class="err-msg ff-span">{{ loiCaKhaDung }}</div>
              <div class="ff-actions ff-span">
                <button class="btn btn-secondary" @click="huyFormCaKhaDung">Hủy</button>
                <button class="btn btn-primary" @click="luuCaKhaDung" :disabled="dangLuuCaKhaDung">
                  {{ dangLuuCaKhaDung ? '...' : (formCaKhaDung.id ? 'Cập nhật' : 'Thêm mới') }}
                </button>
              </div>
            </div>
            <div v-if="dangTaiCaKhaDung" class="loading-txt">Đang tải...</div>
            <table v-else class="data-tbl">
              <thead><tr><th>Loại</th><th>Kỳ</th><th>Thứ</th><th>Ca học</th><th>Từ ngày</th><th>Đến ngày</th><th>UT</th><th></th></tr></thead>
              <tbody>
                <tr v-for="item in danhSachCaKhaDung" :key="item.id">
                  <td><span :class="['badge', item.loaiDangKy || 'kha_dung']">{{ hienThiLoaiDangKy(item) }}</span></td>
                  <td>{{ item.khungKyId ? layTenKhungKy(item.khungKyId) : 'Tất cả' }}</td>
                  <td>{{ item.thuTrongTuan ? 'T' + item.thuTrongTuan : 'Tất cả' }}</td>
                  <td>{{ item.caHocId ? layTenCaHoc(item.caHocId) : 'Tất cả' }}</td>
                  <td>{{ item.tuNgay || '—' }}</td>
                  <td>{{ item.denNgay || '—' }}</td>
                  <td class="tc">{{ item.mucDoUuTien || '—' }}</td>
                  <td class="td-acts">
                    <button class="btn btn-xs btn-secondary" @click="chonSuaCaKhaDung(item)">Sửa</button>
                    <button class="btn btn-xs btn-danger" @click="xoaCaKhaDung(item.id)">Xóa</button>
                  </td>
                </tr>
                <tr v-if="!danhSachCaKhaDung.length"><td colspan="8" class="empty-cell">Chưa có dữ liệu</td></tr>
              </tbody>
            </table>
          </div>

          <!-- === Tab: Nhóm ca liên tục === -->
          <div v-else-if="activeTab === 'nhom-ca'" class="tab-pane">
            <div class="pane-toolbar">
              <div class="pane-title-row">
                <span class="pane-title">Nhóm ca liên tục</span>
                <span class="pane-sub">Giáo viên: <strong>{{ selectedGiaoVien.maGiaoVien }} – {{ selectedGiaoVien.hoTen }}</strong></span>
              </div>
              <button class="btn btn-primary" @click="moFormNhomCa">+ Thêm nhóm ca</button>
            </div>
            <div v-if="hienFormNhomCa" class="inline-form">
              <label class="ff"><span>Khung kỳ</span>
                <select v-model="formNhomCa.khungKyId">
                  <option value="">Tất cả kỳ</option>
                  <option v-for="ky in danhSachKhungKy" :key="ky.id" :value="ky.id">{{ ky.maKy }}</option>
                </select>
              </label>
              <label class="ff"><span>Thứ trong tuần</span>
                <select v-model="formNhomCa.thuTrongTuan">
                  <option value="">Tất cả thứ</option>
                  <option v-for="t in thuList" :key="t.val" :value="t.val">{{ t.lbl }}</option>
                </select>
              </label>
              <label class="ff"><span>Ca bắt đầu *</span>
                <select v-model="formNhomCa.caBatDauId">
                  <option :value="null">-- Chọn ca --</option>
                  <option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">{{ ca.tenCa }}</option>
                </select>
              </label>
              <label class="ff"><span>Ca kết thúc *</span>
                <select v-model="formNhomCa.caKetThucId">
                  <option :value="null">-- Chọn ca --</option>
                  <option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">{{ ca.tenCa }}</option>
                </select>
              </label>
              <label class="ff ff-span"><span>Ghi chú</span><input v-model="formNhomCa.ghiChu" /></label>
              <p class="note-sm ff-span">⚠ Chỉ cho 1 ca hoặc 2 ca liền kề</p>
              <div v-if="loiNhomCa" class="err-msg ff-span">{{ loiNhomCa }}</div>
              <div class="ff-actions ff-span">
                <button class="btn btn-secondary" @click="huyFormNhomCa">Hủy</button>
                <button class="btn btn-primary" @click="luuNhomCa" :disabled="dangLuuNhomCa">
                  {{ dangLuuNhomCa ? '...' : (formNhomCa.id ? 'Cập nhật' : 'Thêm mới') }}
                </button>
              </div>
            </div>
            <div v-if="dangTaiNhomCa" class="loading-txt">Đang tải...</div>
            <table v-else class="data-tbl">
              <thead><tr><th>Kỳ</th><th>Thứ</th><th>Ca bắt đầu</th><th>Ca kết thúc</th><th>Ghi chú</th><th></th></tr></thead>
              <tbody>
                <tr v-for="item in danhSachNhomCa" :key="item.id">
                  <td>{{ item.khungKyId ? layTenKhungKy(item.khungKyId) : 'Tất cả' }}</td>
                  <td>{{ item.thuTrongTuan ? 'T' + item.thuTrongTuan : 'Tất cả' }}</td>
                  <td>{{ item.tenCaBatDau || layTenCaHoc(item.caBatDauId) }}</td>
                  <td>{{ item.tenCaKetThuc || layTenCaHoc(item.caKetThucId) }}</td>
                  <td>{{ item.ghiChu || '—' }}</td>
                  <td class="td-acts">
                    <button class="btn btn-xs btn-secondary" @click="chonSuaNhomCa(item)">Sửa</button>
                    <button class="btn btn-xs btn-danger" @click="xoaNhomCa(item.id)">Xóa</button>
                  </td>
                </tr>
                <tr v-if="!danhSachNhomCa.length"><td colspan="6" class="empty-cell">Chưa có dữ liệu</td></tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <!-- Bottom 3 mini panels (khi đã chọn giáo viên) -->
      <div v-if="selectedGiaoVien" class="bottom-row">
        <!-- Mini: Đăng ký số tiết (ẩn khi tab đang là so-tiet) -->
        <div v-if="activeTab !== 'so-tiet'" class="mini-panel">
          <div class="mini-hdr">📋 Đăng ký số tiết</div>
          <div class="mini-form">
            <label class="ff"><span>Giáo viên</span><input :value="selectedGiaoVien.maGiaoVien + ' – ' + selectedGiaoVien.hoTen" disabled /></label>
            <label class="ff"><span>Khung kỳ/version</span>
              <select v-model="miniSoTiet.khungKyId">
                <option value="">-- Chọn kỳ --</option>
                <option v-for="ky in danhSachKhungKy" :key="ky.id" :value="ky.id">{{ layLabelKhungKyDayDu(ky) }}</option>
              </select>
            </label>
            <label class="ff"><span>Số tiết đăng ký</span><input type="number" v-model.number="miniSoTiet.soTietDangKy" min="1" /></label>
            <label class="ff"><span>Ghi chú</span><input v-model="miniSoTiet.ghiChu" /></label>
            <label class="ff"><span>Trạng thái</span>
              <select v-model="miniSoTiet.trangThai">
                <option value="dang_hieu_luc">Đang hiệu lực</option>
                <option value="ngung_ap_dung">Ngưng áp dụng</option>
              </select>
            </label>
            <div class="mini-form-actions">
              <button class="btn btn-primary" @click="luuMiniSoTiet" :disabled="dangLuuMiniSoTiet">{{ dangLuuMiniSoTiet ? '...' : 'Lưu' }}</button>
              <button class="btn btn-secondary" @click="resetMiniSoTiet">Làm mới</button>
            </div>
          </div>
          <div v-if="loiMiniSoTiet" class="err-msg">{{ loiMiniSoTiet }}</div>
          <table class="data-tbl mini-tbl">
            <thead><tr><th>Kỳ/version</th><th>Số tiết ĐK</th><th>Đã PC</th><th>Còn lại</th><th>Trạng thái</th><th></th></tr></thead>
            <tbody>
              <tr v-for="item in danhSachSoTiet" :key="item.id">
                <td>{{ item.labelKhungKyDayDu || item.maKy || layTenKhungKy(item.khungKyId) }}</td>
                <td class="tc">{{ item.soTietDangKy }}</td>
                <td class="tc">{{ item.soTietDaPhanCong ?? '—' }}</td>
                <td class="tc">{{ item.soTietDangKy - (item.soTietDaPhanCong || 0) }}</td>
                <td><span :class="['badge', item.trangThai]">{{ item.trangThai === 'dang_hieu_luc' ? 'Hiệu lực' : 'Ngưng' }}</span></td>
                <td class="td-acts">
                  <button class="btn btn-xs btn-secondary" @click="chonSuaSoTiet(item); doiTab('so-tiet')">Sửa</button>
                  <button class="btn btn-xs btn-danger" @click="xoaSoTiet(item.id)">Xóa</button>
                </td>
              </tr>
              <tr v-if="!danhSachSoTiet.length"><td colspan="6" class="empty-cell">Chưa có dữ liệu</td></tr>
            </tbody>
          </table>
          <div class="mini-footer">1 – {{ danhSachSoTiet.length }} / {{ danhSachSoTiet.length }}</div>
        </div>

        <!-- Mini: Ca khả dụng (ẩn khi tab đang là ca-kha-dung) -->
        <div v-if="activeTab !== 'ca-kha-dung'" class="mini-panel">
          <div class="mini-hdr">📅 Ca khả dụng</div>
          <div v-if="dangTaiCaKhaDung" class="loading-txt">Đang tải...</div>
          <div v-else-if="!danhSachCaKhaDung.length" class="empty-state-sm">
            <div class="es-icon">📅</div>
            <p>Chọn một giáo viên để quản lý thông tin giảng dạy.</p>
            <button class="btn btn-xs btn-outline" @click="doiTab('ca-kha-dung')">Quản lý ca khả dụng →</button>
          </div>
          <table v-else class="data-tbl mini-tbl">
            <thead><tr><th>Loại</th><th>Thứ</th><th>Ca</th><th>UT</th><th></th></tr></thead>
            <tbody>
              <tr v-for="item in danhSachCaKhaDung.slice(0, 5)" :key="item.id">
                <td><span :class="['badge', item.loaiDangKy || 'kha_dung']">{{ hienThiLoaiDangKy(item) }}</span></td>
                <td>{{ item.thuTrongTuan ? 'T' + item.thuTrongTuan : 'Tất cả' }}</td>
                <td>{{ item.caHocId ? layTenCaHoc(item.caHocId) : 'Tất cả' }}</td>
                <td class="tc">{{ item.mucDoUuTien || '—' }}</td>
                <td class="td-acts">
                  <button class="btn btn-xs btn-secondary" @click="chonSuaCaKhaDung(item); doiTab('ca-kha-dung')">Sửa</button>
                  <button class="btn btn-xs btn-danger" @click="xoaCaKhaDung(item.id)">Xóa</button>
                </td>
              </tr>
            </tbody>
          </table>
          <div v-if="danhSachCaKhaDung.length" class="mini-footer">
            Hiển thị {{ Math.min(5, danhSachCaKhaDung.length) }} / {{ danhSachCaKhaDung.length }}
            <button class="btn btn-xs btn-outline" @click="doiTab('ca-kha-dung')">Xem tất cả →</button>
          </div>
        </div>

        <!-- Mini: Nhóm ca liên tục (ẩn khi tab đang là nhom-ca) -->
        <div v-if="activeTab !== 'nhom-ca'" class="mini-panel">
          <div class="mini-hdr">🔗 Nhóm ca liên tục</div>
          <div class="mini-form">
            <label class="ff"><span>Thứ</span>
              <select v-model="miniNhomCa.thuTrongTuan">
                <option value="">Tất cả thứ</option>
                <option v-for="t in thuList" :key="t.val" :value="t.val">{{ t.lbl }}</option>
              </select>
            </label>
            <label class="ff"><span>Ca bắt đầu</span>
              <select v-model="miniNhomCa.caBatDauId">
                <option :value="null">-- Chọn ca --</option>
                <option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">{{ ca.tenCa }}</option>
              </select>
            </label>
            <label class="ff"><span>Ca kết thúc</span>
              <select v-model="miniNhomCa.caKetThucId">
                <option :value="null">-- Chọn ca --</option>
                <option v-for="ca in danhSachCaHoc" :key="ca.id" :value="ca.id">{{ ca.tenCa }}</option>
              </select>
            </label>
            <label class="ff"><span>Mức ưu tiên</span>
              <select v-model="miniNhomCa.mucDoUuTien">
                <option value="">-- --</option>
                <option value="1">Ưu tiên 1</option>
                <option value="2">Ưu tiên 2</option>
                <option value="3">Ưu tiên 3</option>
              </select>
            </label>
            <p class="note-sm" style="grid-column:span 2">⚠ Chỉ cho 1 ca hoặc 2 ca liên kề</p>
            <div class="mini-form-actions">
              <button class="btn btn-primary" @click="luuMiniNhomCa" :disabled="dangLuuMiniNhomCa">{{ dangLuuMiniNhomCa ? '...' : 'Lưu' }}</button>
              <button class="btn btn-secondary" @click="resetMiniNhomCa">Làm mới</button>
            </div>
          </div>
          <div v-if="loiMiniNhomCa" class="err-msg">{{ loiMiniNhomCa }}</div>
          <table class="data-tbl mini-tbl">
            <thead><tr><th>Thứ</th><th>Ca bắt đầu</th><th>Ca kết thúc</th><th>Mức ưu tiên</th><th>Ghi chú</th><th></th></tr></thead>
            <tbody>
              <tr v-for="item in danhSachNhomCa" :key="item.id">
                <td>{{ item.thuTrongTuan ? 'T' + item.thuTrongTuan : 'Tất cả' }}</td>
                <td>{{ item.tenCaBatDau || layTenCaHoc(item.caBatDauId) }}</td>
                <td>{{ item.tenCaKetThuc || layTenCaHoc(item.caKetThucId) }}</td>
                <td>{{ item.mucDoUuTien ? 'Ưu tiên ' + item.mucDoUuTien : '—' }}</td>
                <td>{{ item.ghiChu || '—' }}</td>
                <td class="td-acts">
                  <button class="btn btn-xs btn-secondary" @click="chonSuaNhomCa(item); doiTab('nhom-ca')">Sửa</button>
                  <button class="btn btn-xs btn-danger" @click="xoaNhomCa(item.id)">Xóa</button>
                </td>
              </tr>
              <tr v-if="!danhSachNhomCa.length"><td colspan="6" class="empty-cell">Chưa có dữ liệu</td></tr>
            </tbody>
          </table>
          <div class="mini-footer">1 – {{ danhSachNhomCa.length }} / {{ danhSachNhomCa.length }}</div>
        </div>
      </div>
    </template>
  </div>
</template>

<script setup>
import { onMounted, reactive, ref, watch } from 'vue'
import { giangDayService } from '../services/giangDayService'

// ===== Tabs =====
const activeTab = ref('ho-so')
const tabs = [
  { key: 'ho-so', label: 'Hồ sơ' },
  { key: 'mon-day', label: 'Môn có thể dạy' },
  { key: 'so-tiet', label: 'Đăng ký số tiết' },
  { key: 'ca-kha-dung', label: 'Ca khả dụng' },
  { key: 'nhom-ca', label: 'Nhóm ca liên tục' },
]
const thuList = [
  { val: '2', lbl: 'Thứ 2' }, { val: '3', lbl: 'Thứ 3' }, { val: '4', lbl: 'Thứ 4' },
  { val: '5', lbl: 'Thứ 5' }, { val: '6', lbl: 'Thứ 6' }, { val: '7', lbl: 'Thứ 7' },
  { val: '8', lbl: 'Chủ nhật' },
]

// ===== Stats =====
const thongKe = reactive({ tong: 0, dangHoatDong: 0, coMonDay: 0, coDangKyTiet: 0 })

// ===== Teacher list =====
const danhSach = ref([])
const dangTai = ref(false)
const trangHienTai = ref(0)
const tongTrang = ref(0)
const tongBanGhi = ref(0)
const kichThuocTrang = 20
const boLoc = reactive({ keyword: '', trangThai: '', sortBy: 'id', sortDir: 'desc' })

// ===== Selected =====
const selectedGiaoVien = ref(null)

// ===== GiaoVien form =====
const formMode = ref('') // 'add' | 'edit' | ''
const dangLuuGV = ref(false)
const form = reactive({ id: null, maGiaoVien: '', hoTen: '', email: '', soDienThoai: '', chuyenMon: '', trangThai: 'dang_day', taiKhoanId: '' })

// ===== Background data =====
const danhSachMonHoc = ref([])
const danhSachKhungKy = ref([])
const danhSachCaHoc = ref([])
const danhSachNganhSoTiet = ref([])
const danhSachChuongTrinhSoTiet = ref([])
const danhSachVersionSoTiet = ref([])
const danhSachKhungKySoTiet = ref([])

const chonSoTiet = reactive({
  nganhId: '',
  chuongTrinhId: '',
  versionId: ''
})
// ===== Tab 2: Mon day =====
const danhSachMonDay = ref([])
const dangTaiMonDay = ref(false)
const hienFormMonDay = ref(false)
const dangLuuMonDay = ref(false)
const loiMonDay = ref('')
const formMonDay = reactive({ id: null, giaoVienId: null, monHocId: '', trangThai: 'dang_hieu_luc', ghiChu: '' })

// ===== Tab 3: So tiet =====
const danhSachSoTiet = ref([])
const dangTaiSoTiet = ref(false)
const hienFormSoTiet = ref(false)
const dangLuuSoTiet = ref(false)
const loiSoTiet = ref('')
const formSoTiet = reactive({ id: null, giaoVienId: null, khungKyId: '', soTietDangKy: null, trangThai: 'dang_hieu_luc', ghiChu: '' })

// ===== Tab 4: Ca kha dung =====
const danhSachCaKhaDung = ref([])
const dangTaiCaKhaDung = ref(false)
const hienFormCaKhaDung = ref(false)
const dangLuuCaKhaDung = ref(false)
const loiCaKhaDung = ref('')
const formCaKhaDung = reactive({ id: null, giaoVienId: null, khungKyId: '', thuTrongTuan: '', caHocId: '', tuNgay: '', denNgay: '', loaiDangKy: '', mucDoUuTien: null, ghiChu: '' })

// ===== Tab 5: Nhom ca =====
const danhSachNhomCa = ref([])
const dangTaiNhomCa = ref(false)
const hienFormNhomCa = ref(false)
const dangLuuNhomCa = ref(false)
const loiNhomCa = ref('')
const formNhomCa = reactive({ id: null, giaoVienId: null, khungKyId: '', thuTrongTuan: '', caBatDauId: null, caKetThucId: null, mucDoUuTien: null, ghiChu: '' })

// ===== Mini forms =====
const miniSoTiet = reactive({ khungKyId: '', soTietDangKy: null, trangThai: 'dang_hieu_luc', ghiChu: '' })
const dangLuuMiniSoTiet = ref(false)
const loiMiniSoTiet = ref('')
const miniNhomCa = reactive({ khungKyId: '', thuTrongTuan: '', caBatDauId: null, caKetThucId: null, mucDoUuTien: '', ghiChu: '' })
const dangLuuMiniNhomCa = ref(false)
const loiMiniNhomCa = ref('')

// ===== Alerts =====
const thongBao = ref('')
const loi = ref('')

// ===== Mount =====
onMounted(async () => {
  await Promise.allSettled([taiDuLieu(), taiThongKe(), taiDuLieuNen()])
})

async function taiThongKe() {
  try {
    const [r1, r2, r3, r4] = await Promise.allSettled([
      giangDayService.layTrangGiaoVien({ size: 1 }),
      giangDayService.layTrangGiaoVien({ trangThai: 'dang_day', size: 1 }),
      giangDayService.layTrangGiaoVienMonHoc({ size: 1 }),
      giangDayService.layTrangGiangVienDangKyGiangDay({ size: 1 })
    ])
    if (r1.status === 'fulfilled') thongKe.tong = r1.value?.totalElements || 0
    if (r2.status === 'fulfilled') thongKe.dangHoatDong = r2.value?.totalElements || 0
    if (r3.status === 'fulfilled') {
      const d = r3.value?.data?.data
      thongKe.coMonDay = d?.totalElements || d?.total || 0
    }
    if (r4.status === 'fulfilled') {
      const d = r4.value?.data?.data
      thongKe.coDangKyTiet = d?.totalElements || d?.total || 0
    }
  } catch (e) { /* stats không ảnh hưởng chức năng */ }
}

async function taiDuLieuNen() {
  try {
    const [monHoc, khungKy, caHoc, nganh] = await Promise.all([
      giangDayService.layDanhSachMonHoc(),
      giangDayService.layDanhSachKhungKy(),
      giangDayService.layDanhSachCaHoc(),
      giangDayService.layDanhSachNganh()
    ])

    danhSachMonHoc.value = monHoc
    danhSachKhungKy.value = khungKy
    danhSachCaHoc.value = caHoc
    danhSachNganhSoTiet.value = nganh
  } catch (e) {
    console.warn('Tải dữ liệu nền thất bại', e)
  }
}

async function taiDuLieu() {
  dangTai.value = true; loi.value = ''
  try {
    const page = await giangDayService.layTrangGiaoVien({
      page: trangHienTai.value, size: kichThuocTrang,
      keyword: boLoc.keyword || undefined,
      trangThai: boLoc.trangThai || undefined,
      sortBy: boLoc.sortBy, sortDir: boLoc.sortDir
    })
    danhSach.value = page.content
    trangHienTai.value = page.number
    tongTrang.value = page.totalPages
    tongBanGhi.value = page.totalElements
    thongKe.tong = page.totalElements
  } catch (e) {
    loi.value = e?.message || 'Không tải được danh sách giáo viên'
  } finally { dangTai.value = false }
}

function locDuLieu() { trangHienTai.value = 0; taiDuLieu() }
function doiTrang(p) {
  if (p < 0 || (tongTrang.value && p >= tongTrang.value)) return
  trangHienTai.value = p; taiDuLieu()
}
function doiTab(key) { activeTab.value = key }
function doiGiaoVien() { selectedGiaoVien.value = null }
function xemHoSo() {
  if (!selectedGiaoVien.value) return
  suaGiaoVien(selectedGiaoVien.value)
}

function quanLyGiaoVien(item) {
  selectedGiaoVien.value = item
  if (activeTab.value === 'ho-so') activeTab.value = 'mon-day'
}

function suaGiaoVien(item) {
  formMode.value = 'edit'
  Object.assign(form, { id: item.id, maGiaoVien: item.maGiaoVien || '', hoTen: item.hoTen || '', email: item.email || '', soDienThoai: item.soDienThoai || '', chuyenMon: item.chuyenMon || '', trangThai: item.trangThai || 'dang_day', taiKhoanId: item.taiKhoanId || '' })
  activeTab.value = 'ho-so'
}

function batDauThemMoi() {
  formMode.value = 'add'
  Object.assign(form, { id: null, maGiaoVien: '', hoTen: '', email: '', soDienThoai: '', chuyenMon: '', trangThai: 'dang_day', taiKhoanId: '' })
  activeTab.value = 'ho-so'
  loi.value = ''; thongBao.value = ''
}

function dongFormGV() { formMode.value = '' }

async function luuGiaoVien() {
  dangLuuGV.value = true; loi.value = ''; thongBao.value = ''
  const payload = { maGiaoVien: form.maGiaoVien, hoTen: form.hoTen, email: form.email, soDienThoai: form.soDienThoai || null, chuyenMon: form.chuyenMon || null, trangThai: form.trangThai }
  try {
    if (formMode.value === 'add') {
      const response = await giangDayService.taoGiaoVien(payload)
      const matKhauTam = response?.data?.data?.matKhauTam || response?.data?.matKhauTam
      thongBao.value = matKhauTam ? 'Thêm thành công. Mật khẩu tạm: ' + matKhauTam : 'Thêm giáo viên thành công'
      formMode.value = ''
    } else {
      await giangDayService.capNhatGiaoVien(form.id, payload)
      thongBao.value = 'Cập nhật thành công'
      if (selectedGiaoVien.value?.id === form.id) {
        selectedGiaoVien.value = { ...selectedGiaoVien.value, ...payload }
      }
      formMode.value = ''
    }
    await taiDuLieu()
    await taiThongKe()
    setTimeout(() => { thongBao.value = '' }, 4000)
  } catch (e) { loi.value = e?.response?.data?.message || e?.message || 'Lưu thất bại'
  } finally { dangLuuGV.value = false }
}

async function xoaGiaoVien(item) {
  if (!confirm('Xóa giáo viên "' + item.hoTen + '"?')) return
  try {
    await giangDayService.xoaGiaoVien(item.id)
    if (selectedGiaoVien.value?.id === item.id) selectedGiaoVien.value = null
    thongBao.value = 'Đã xóa giáo viên'
    await taiDuLieu(); await taiThongKe()
    setTimeout(() => { thongBao.value = '' }, 3000)
  } catch (e) { loi.value = e?.response?.data?.message || e?.message || 'Xóa thất bại' }
}

// ===== Load all sub-data when teacher selected =====
watch(selectedGiaoVien, async (gv) => {
  if (!gv) {
    danhSachMonDay.value = []; danhSachSoTiet.value = []
    danhSachCaKhaDung.value = []; danhSachNhomCa.value = []
    return
  }
  hienFormMonDay.value = false; hienFormSoTiet.value = false
  hienFormCaKhaDung.value = false; hienFormNhomCa.value = false
  await Promise.allSettled([taiMonDay(), taiSoTiet(), taiCaKhaDung(), taiNhomCa()])
})

// ===== Tab 2: Mon day =====
async function taiMonDay() {
  if (!selectedGiaoVien.value) return
  dangTaiMonDay.value = true; loiMonDay.value = ''
  try {
    danhSachMonDay.value = await giangDayService.layDanhSachGiaoVienMonHoc({ giaoVienId: selectedGiaoVien.value.id })
  } catch (e) {
    console.error('Lỗi tải môn có thể dạy:', e)
    loiMonDay.value = e?.response?.data?.message || e?.message || 'Không tải được danh sách môn có thể dạy'
    danhSachMonDay.value = []
  } finally { dangTaiMonDay.value = false }
}
function moFormMonDay() {
  Object.assign(formMonDay, { id: null, giaoVienId: selectedGiaoVien.value.id, monHocId: '', trangThai: 'dang_hieu_luc', ghiChu: '' })
  loiMonDay.value = ''; hienFormMonDay.value = true
}
function huyFormMonDay() { hienFormMonDay.value = false; loiMonDay.value = '' }
function chonSuaMonDay(item) {
  Object.assign(formMonDay, { id: item.id, giaoVienId: item.giaoVienId, monHocId: item.monHocId, trangThai: item.trangThai || 'dang_hieu_luc', ghiChu: item.ghiChu || '' })
  loiMonDay.value = ''; hienFormMonDay.value = true
}
async function luuMonDay() {
  if (!formMonDay.monHocId) { loiMonDay.value = 'Vui lòng chọn môn học'; return }
  dangLuuMonDay.value = true
  try {
    const payload = {
      giaoVienId: formMonDay.giaoVienId || selectedGiaoVien.value?.id,
      monHocId: formMonDay.monHocId,
      trangThai: formMonDay.trangThai || 'dang_hieu_luc',
      ghiChu: formMonDay.ghiChu || null
    }
    if (formMonDay.id) await giangDayService.capNhatGiaoVienMonHoc(formMonDay.id, payload)
    else await giangDayService.taoGiaoVienMonHoc(payload)
    hienFormMonDay.value = false
    await taiMonDay()
    thongBao.value = 'Đã lưu môn có thể dạy'
    setTimeout(() => { thongBao.value = '' }, 3000)
  } catch (e) { loiMonDay.value = e?.response?.data?.message || e?.message || 'Lỗi'
  } finally { dangLuuMonDay.value = false }
}
async function xoaMonDay(id) {
  if (!confirm('Xóa cấu hình này?')) return
  try { await giangDayService.xoaGiaoVienMonHoc(id); await taiMonDay() }
  catch (e) { alert(e?.response?.data?.message || e?.message || 'Lỗi') }
}

// ===== Tab 3: So tiet =====
async function taiSoTiet() {
  if (!selectedGiaoVien.value) return
  dangTaiSoTiet.value = true
  try {
    danhSachSoTiet.value = await giangDayService.layDanhSachGiangVienDangKyGiangDay({ giaoVienId: selectedGiaoVien.value.id })
  } catch (e) { danhSachSoTiet.value = [] } finally { dangTaiSoTiet.value = false }
}
function moFormSoTiet() {
  Object.assign(formSoTiet, { id: null, giaoVienId: selectedGiaoVien.value.id, khungKyId: '', soTietDangKy: null, trangThai: 'dang_hieu_luc', ghiChu: '' })
  loiSoTiet.value = ''; hienFormSoTiet.value = true
}
function huyFormSoTiet() { hienFormSoTiet.value = false; loiSoTiet.value = '' }
function chonSuaSoTiet(item) {
  Object.assign(formSoTiet, { id: item.id, giaoVienId: item.giaoVienId, khungKyId: item.khungKyId, soTietDangKy: item.soTietDangKy, trangThai: item.trangThai || 'dang_hieu_luc', ghiChu: item.ghiChu || '' })
  loiSoTiet.value = ''; hienFormSoTiet.value = true
}
async function luuSoTiet() {
  if (!formSoTiet.khungKyId) { loiSoTiet.value = 'Vui lòng chọn kỳ'; return }
  if (!formSoTiet.soTietDangKy || formSoTiet.soTietDangKy <= 0) { loiSoTiet.value = 'Số tiết phải > 0'; return }
  dangLuuSoTiet.value = true
  try {
    const payload = { giaoVienId: formSoTiet.giaoVienId, khungKyId: formSoTiet.khungKyId, soTietDangKy: formSoTiet.soTietDangKy, trangThai: formSoTiet.trangThai, ghiChu: formSoTiet.ghiChu }
    if (formSoTiet.id) await giangDayService.capNhatGiangVienDangKyGiangDay(formSoTiet.id, payload)
    else await giangDayService.taoGiangVienDangKyGiangDay(payload)
    hienFormSoTiet.value = false; await taiSoTiet()
  } catch (e) { loiSoTiet.value = e?.response?.data?.message || e?.message || 'Lỗi'
  } finally { dangLuuSoTiet.value = false }
}
async function xoaSoTiet(id) {
  if (!confirm('Xóa đăng ký này?')) return
  try { await giangDayService.xoaGiangVienDangKyGiangDay(id); await taiSoTiet() }
  catch (e) { alert(e?.response?.data?.message || e?.message || 'Lỗi') }
}
// Mini so tiet
function resetMiniSoTiet() { Object.assign(miniSoTiet, { khungKyId: '', soTietDangKy: null, trangThai: 'dang_hieu_luc', ghiChu: '' }); loiMiniSoTiet.value = '' }
async function luuMiniSoTiet() {
  if (!miniSoTiet.khungKyId) { loiMiniSoTiet.value = 'Vui lòng chọn kỳ'; return }
  if (!miniSoTiet.soTietDangKy || miniSoTiet.soTietDangKy <= 0) { loiMiniSoTiet.value = 'Số tiết phải > 0'; return }
  dangLuuMiniSoTiet.value = true; loiMiniSoTiet.value = ''
  try {
    await giangDayService.taoGiangVienDangKyGiangDay({ giaoVienId: selectedGiaoVien.value.id, khungKyId: miniSoTiet.khungKyId, soTietDangKy: miniSoTiet.soTietDangKy, trangThai: miniSoTiet.trangThai, ghiChu: miniSoTiet.ghiChu })
    resetMiniSoTiet(); await taiSoTiet()
  } catch (e) { loiMiniSoTiet.value = e?.response?.data?.message || e?.message || 'Lỗi'
  } finally { dangLuuMiniSoTiet.value = false }
}

// ===== Tab 4: Ca kha dung =====
async function taiCaKhaDung() {
  if (!selectedGiaoVien.value) return
  dangTaiCaKhaDung.value = true
  try {
    const page = await giangDayService.layTrangGiaoVienKhaDung({ giaoVienId: selectedGiaoVien.value.id, size: 500 })
    danhSachCaKhaDung.value = page.content
  } catch (e) { danhSachCaKhaDung.value = [] } finally { dangTaiCaKhaDung.value = false }
}
function moFormCaKhaDung() {
  Object.assign(formCaKhaDung, { id: null, giaoVienId: selectedGiaoVien.value.id, khungKyId: '', thuTrongTuan: '', caHocId: '', tuNgay: '', denNgay: '', loaiDangKy: '', mucDoUuTien: null, ghiChu: '' })
  loiCaKhaDung.value = ''; hienFormCaKhaDung.value = true
}
function huyFormCaKhaDung() { hienFormCaKhaDung.value = false; loiCaKhaDung.value = '' }
function chonSuaCaKhaDung(item) {
  Object.assign(formCaKhaDung, { id: item.id, giaoVienId: item.giaoVienId, khungKyId: item.khungKyId || '', thuTrongTuan: item.thuTrongTuan || '', caHocId: item.caHocId || '', tuNgay: item.tuNgay || '', denNgay: item.denNgay || '', loaiDangKy: item.loaiDangKy || '', mucDoUuTien: item.mucDoUuTien || null, ghiChu: item.ghiChu || '' })
  loiCaKhaDung.value = ''; hienFormCaKhaDung.value = true
}
async function luuCaKhaDung() {
  dangLuuCaKhaDung.value = true
  try {
    const payload = { giaoVienId: formCaKhaDung.giaoVienId, khungKyId: formCaKhaDung.khungKyId || null, thuTrongTuan: formCaKhaDung.thuTrongTuan || null, caHocId: formCaKhaDung.caHocId || null, tuNgay: formCaKhaDung.tuNgay || null, denNgay: formCaKhaDung.denNgay || null, loaiDangKy: formCaKhaDung.loaiDangKy || null, mucDoUuTien: formCaKhaDung.mucDoUuTien || null, ghiChu: formCaKhaDung.ghiChu || null }
    if (formCaKhaDung.id) await giangDayService.capNhatGiaoVienKhaDung(formCaKhaDung.id, payload)
    else await giangDayService.taoGiaoVienKhaDung(payload)
    hienFormCaKhaDung.value = false; await taiCaKhaDung()
  } catch (e) { loiCaKhaDung.value = e?.response?.data?.message || e?.message || 'Lỗi'
  } finally { dangLuuCaKhaDung.value = false }
}
async function xoaCaKhaDung(id) {
  if (!confirm('Xóa cấu hình ca này?')) return
  try { await giangDayService.xoaGiaoVienKhaDung(id); await taiCaKhaDung() }
  catch (e) { alert(e?.response?.data?.message || e?.message || 'Lỗi') }
}

// ===== Tab 5: Nhom ca =====
async function taiNhomCa() {
  if (!selectedGiaoVien.value) return
  dangTaiNhomCa.value = true
  try {
    const res = await giangDayService.layDanhSachNhomCaTheoGiaoVien(selectedGiaoVien.value.id)
    danhSachNhomCa.value = Array.isArray(res) ? res : []
  } catch (e) { danhSachNhomCa.value = [] } finally { dangTaiNhomCa.value = false }
}
function moFormNhomCa() {
  Object.assign(formNhomCa, { id: null, giaoVienId: selectedGiaoVien.value.id, khungKyId: '', thuTrongTuan: '', caBatDauId: null, caKetThucId: null, mucDoUuTien: null, ghiChu: '' })
  loiNhomCa.value = ''; hienFormNhomCa.value = true
}
function huyFormNhomCa() { hienFormNhomCa.value = false; loiNhomCa.value = '' }
function chonSuaNhomCa(item) {
  Object.assign(formNhomCa, { id: item.id, giaoVienId: item.giaoVienId, khungKyId: item.khungKyId || '', thuTrongTuan: item.thuTrongTuan || '', caBatDauId: item.caBatDauId, caKetThucId: item.caKetThucId, mucDoUuTien: item.mucDoUuTien || null, ghiChu: item.ghiChu || '' })
  loiNhomCa.value = ''; hienFormNhomCa.value = true
}
function validateNhomCa(caBatDauId, caKetThucId) {
  if (!caBatDauId) return 'Ca bắt đầu không được để trống'
  if (!caKetThucId) return 'Ca kết thúc không được để trống'
  const bd = danhSachCaHoc.value.find(c => c.id === caBatDauId)
  const kt = danhSachCaHoc.value.find(c => c.id === caKetThucId)
  if (bd?.thuTu != null && kt?.thuTu != null) {
    if (kt.thuTu < bd.thuTu) return 'Ca kết thúc phải >= ca bắt đầu'
    if ((kt.thuTu - bd.thuTu) > 1) return 'Chỉ cho phép 1 ca hoặc 2 ca liền kề'
  }
  return ''
}
async function luuNhomCa() {
  const err = validateNhomCa(formNhomCa.caBatDauId, formNhomCa.caKetThucId)
  if (err) { loiNhomCa.value = err; return }
  dangLuuNhomCa.value = true
  try {
    const payload = { giaoVienId: formNhomCa.giaoVienId, khungKyId: formNhomCa.khungKyId || null, thuTrongTuan: formNhomCa.thuTrongTuan || null, caBatDauId: formNhomCa.caBatDauId, caKetThucId: formNhomCa.caKetThucId, mucDoUuTien: formNhomCa.mucDoUuTien || null, ghiChu: formNhomCa.ghiChu || null }
    if (formNhomCa.id) await giangDayService.capNhatNhomCa(formNhomCa.id, payload)
    else await giangDayService.taoNhomCa(payload)
    hienFormNhomCa.value = false; await taiNhomCa()
  } catch (e) { loiNhomCa.value = e?.response?.data?.message || e?.message || 'Lỗi'
  } finally { dangLuuNhomCa.value = false }
}
async function xoaNhomCa(id) {
  if (!confirm('Xóa nhóm ca này?')) return
  try { await giangDayService.xoaNhomCa(id); await taiNhomCa() }
  catch (e) { alert(e?.response?.data?.message || e?.message || 'Lỗi') }
}
// Mini nhom ca
function resetMiniNhomCa() { Object.assign(miniNhomCa, { khungKyId: '', thuTrongTuan: '', caBatDauId: null, caKetThucId: null, mucDoUuTien: '', ghiChu: '' }); loiMiniNhomCa.value = '' }
async function luuMiniNhomCa() {
  const err = validateNhomCa(miniNhomCa.caBatDauId, miniNhomCa.caKetThucId)
  if (err) { loiMiniNhomCa.value = err; return }
  dangLuuMiniNhomCa.value = true; loiMiniNhomCa.value = ''
  try {
    await giangDayService.taoNhomCa({ giaoVienId: selectedGiaoVien.value.id, khungKyId: miniNhomCa.khungKyId || null, thuTrongTuan: miniNhomCa.thuTrongTuan || null, caBatDauId: miniNhomCa.caBatDauId, caKetThucId: miniNhomCa.caKetThucId, mucDoUuTien: miniNhomCa.mucDoUuTien || null, ghiChu: miniNhomCa.ghiChu || null })
    resetMiniNhomCa(); await taiNhomCa()
  } catch (e) { loiMiniNhomCa.value = e?.response?.data?.message || e?.message || 'Lỗi'
  } finally { dangLuuMiniNhomCa.value = false }
}

// ===== Helpers =====
function hienThiTrangThai(val) {
  return { dang_day: 'Hoạt động', tam_nghi: 'Tạm nghỉ', nghi_viec: 'Nghỉ việc' }[val] || val || '—'
}
function hienThiLoaiDangKy(item) {
  if (item.loaiDangKy === 'kha_dung') return 'Khả dụng'
  if (item.loaiDangKy === 'ban') return 'Bận'
  if (item.loaiDangKy === 'uu_tien') return 'Ưu tiên'
  return item.khaDung === false ? 'Bận (cũ)' : 'KD (cũ)'
}
function layTenKhungKy(id) {
  const ky = danhSachKhungKy.value.find(k => k.id === id)
  return ky?.labelDayDu || ky?.maKy || ('KK ' + id)
}
function layLabelKhungKyDayDu(ky) {
  if (!ky) return ''
  return ky.labelDayDu || [ky.tenNganh, ky.tenChuongTrinh, ky.tenVersion, ky.tenKy].filter(Boolean).join(' / ') || ky.maKy || ''
}
function layTenCaHoc(id) { return danhSachCaHoc.value.find(c => c.id === id)?.tenCa || ('Ca ' + id) }
function layTenCaTheoThuTu(thuTu) {
  if (!thuTu) return '—'
  const ca = danhSachCaHoc.value.find(c => (c.thuTu ?? c.id) === thuTu)
  return ca?.tenCa || ('TT ' + thuTu)
}
</script>

<style scoped>
.gv-page { display: flex; flex-direction: column; gap: 10px; padding: 0; }

/* Header */
.gv-header-bar { display: flex; justify-content: space-between; align-items: center; background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; padding: 10px 16px; }
.gv-header-bar h1 { font-size: 16px; font-weight: 800; margin: 0 0 2px; color: #111827; }
.gv-header-bar p { font-size: 12px; color: #6b7280; margin: 0; }

/* Alerts */
.gv-alert { padding: 7px 12px; border-radius: 8px; font-size: 12px; }
.gv-alert.success { background: #ecfdf5; color: #047857; border: 1px solid #a7f3d0; }
.gv-alert.error { background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5; }

/* Stats */
.gv-stats-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 10px; }
.stat-card { background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; padding: 10px 14px; display: flex; align-items: center; gap: 12px; }
.stat-icon-wrap { width: 38px; height: 38px; border-radius: 9px; display: flex; align-items: center; justify-content: center; font-size: 18px; flex-shrink: 0; }
.stat-icon-wrap.blue { background: #eff6ff; }
.stat-icon-wrap.green { background: #ecfdf5; }
.stat-icon-wrap.purple { background: #f5f3ff; }
.stat-icon-wrap.orange { background: #fff7ed; }
.stat-lbl { font-size: 11px; color: #6b7280; font-weight: 500; }
.stat-num { font-size: 22px; font-weight: 800; line-height: 1.2; }
.stat-num.blue { color: #1d4ed8; }
.stat-num.green { color: #16a34a; }
.stat-num.purple { color: #7c3aed; }
.stat-num.orange { color: #ea580c; }

/* Tab nav */
.gv-tab-nav { display: flex; background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; overflow: hidden; }
.tab-btn { padding: 9px 16px; font-size: 12.5px; font-weight: 600; border: none; background: transparent; cursor: pointer; color: #6b7280; border-bottom: 2px solid transparent; white-space: nowrap; font-family: inherit; transition: color 0.15s; }
.tab-btn:hover { color: #1d4ed8; background: #f9fafb; }
.tab-btn.active { color: #1d4ed8; border-bottom-color: #1d4ed8; background: #fafbff; }

/* Form card */
.form-card { background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; overflow: hidden; }
.form-card-hdr { display: flex; justify-content: space-between; align-items: center; padding: 9px 14px; background: #f8fafc; border-bottom: 1px solid #e5e7eb; font-size: 13px; font-weight: 700; }
.btn-close { background: none; border: none; font-size: 18px; cursor: pointer; color: #6b7280; line-height: 1; padding: 0 4px; }
.form-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; padding: 14px; }
.ff { display: flex; flex-direction: column; gap: 4px; }
.ff span { font-size: 11.5px; font-weight: 600; color: #374151; }
.ff input, .ff select, .ff textarea { border: 1px solid #e5e7eb; border-radius: 7px; padding: 5px 8px; font-size: 12px; font-family: inherit; outline: none; }
.ff input:focus, .ff select:focus { border-color: #93c5fd; }
.ff input:disabled { background: #f9fafb; color: #9ca3af; }
.ff-span { grid-column: span 2; }
.ff-actions { display: flex; gap: 8px; justify-content: flex-end; }

/* Table card */
.table-card { background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; overflow: hidden; }
.tbl-toolbar { display: flex; justify-content: space-between; align-items: center; padding: 9px 12px; border-bottom: 1px solid #f3f4f6; background: #f8fafc; gap: 10px; flex-wrap: wrap; }
.tbl-filters { display: flex; gap: 7px; align-items: center; }
.search-inp { border: 1px solid #e5e7eb; border-radius: 7px; padding: 5px 9px; font-size: 12px; width: 240px; outline: none; }
.filter-sel { border: 1px solid #e5e7eb; border-radius: 7px; padding: 5px 8px; font-size: 12px; }
.pg-info { font-size: 11.5px; color: #9ca3af; white-space: nowrap; }
.tbl-wrap { overflow-x: auto; }
.tbl-wrap-scroll { overflow-x: auto; max-height: calc(100vh - 370px); overflow-y: auto; }
.data-tbl { width: 100%; border-collapse: collapse; font-size: 12.5px; }
.data-tbl th { background: #f9fafb; font-weight: 700; font-size: 11px; text-transform: uppercase; padding: 7px 9px; text-align: left; border-bottom: 1px solid #e5e7eb; color: #374151; white-space: nowrap; }
.data-tbl td { padding: 7px 9px; border-bottom: 1px solid #f3f4f6; vertical-align: middle; }
.data-tbl tr:hover td { background: #fafafa; }
.data-tbl.data-tbl-sm th, .data-tbl.data-tbl-sm td { padding: 5px 7px; font-size: 12px; }
.row-sel td { background: #eff6ff !important; }
.td-acts { white-space: nowrap; }
.empty-cell { text-align: center; color: #9ca3af; padding: 20px; }
.tc { text-align: center; }
.txt-warn { color: #dc2626; font-weight: 700; }
.loading-txt { text-align: center; color: #9ca3af; padding: 16px; font-size: 12px; }
.pg-nav { display: flex; align-items: center; justify-content: center; gap: 8px; padding: 8px; font-size: 12px; }
.pg-nav button { border: 1px solid #e5e7eb; background: #fff; border-radius: 6px; padding: 3px 9px; cursor: pointer; font-size: 13px; }
.pg-nav button:disabled { opacity: 0.35; cursor: not-allowed; }
.pg-nav.sm button { padding: 2px 7px; font-size: 12px; }

/* Managed bar */
.managed-bar { display: flex; align-items: center; gap: 10px; background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; padding: 8px 14px; flex-wrap: wrap; }
.managed-bar.no-sel { background: #fafafa; }
.mg-icon { font-size: 16px; }
.mg-text { font-size: 12.5px; color: #374151; flex: 1; min-width: 200px; }
.mg-text strong { color: #1d4ed8; }
.mg-actions { display: flex; gap: 6px; margin-left: auto; }

/* Content split */
.content-split { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; align-items: start; }
.split-left { background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; overflow: hidden; }
.split-left-hdr { padding: 9px 12px; background: #f8fafc; border-bottom: 1px solid #e5e7eb; display: flex; align-items: center; justify-content: space-between; flex-wrap: wrap; gap: 7px; }
.split-left-title { font-size: 13px; font-weight: 700; color: #111827; }
.split-left-filters { display: flex; gap: 5px; align-items: center; }
.search-sm { border: 1px solid #e5e7eb; border-radius: 6px; padding: 4px 8px; font-size: 11.5px; width: 140px; outline: none; }
.sel-sm { border: 1px solid #e5e7eb; border-radius: 6px; padding: 4px 7px; font-size: 11.5px; }
.split-right { background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; overflow: hidden; min-height: 320px; }

/* Empty state */
.empty-state { display: flex; flex-direction: column; align-items: center; justify-content: center; padding: 50px 20px; color: #6b7280; text-align: center; gap: 8px; }
.es-icon { font-size: 36px; opacity: 0.4; }
.empty-state p { font-size: 13px; margin: 0; }

/* Tab pane */
.tab-pane { padding: 12px 14px; display: flex; flex-direction: column; gap: 10px; }
.pane-toolbar { display: flex; align-items: flex-start; justify-content: space-between; gap: 10px; }
.pane-title-row { display: flex; flex-direction: column; gap: 2px; }
.pane-title { font-size: 13px; font-weight: 700; color: #111827; }
.pane-sub { font-size: 11.5px; color: #6b7280; }

/* Inline form */
.inline-form { display: grid; grid-template-columns: 1fr 1fr; gap: 9px; background: #f8fafc; border: 1px solid #e5e7eb; border-radius: 8px; padding: 12px; }
.err-msg { background: #fef2f2; border: 1px solid #fca5a5; border-radius: 6px; padding: 5px 10px; font-size: 12px; color: #dc2626; }
.note-sm { font-size: 11px; color: #92400e; background: #fffbeb; padding: 4px 8px; border-radius: 5px; margin: 0; }
.tbl-footer { font-size: 11px; color: #9ca3af; padding: 5px 9px; border-top: 1px solid #f3f4f6; }

/* Bottom row - 3 panels */
.bottom-row { display: grid; grid-template-columns: repeat(3, 1fr); gap: 10px; }
.mini-panel { background: #fff; border: 1px solid #e5e7eb; border-radius: 10px; overflow: hidden; display: flex; flex-direction: column; }
.mini-hdr { padding: 8px 12px; background: #f8fafc; border-bottom: 1px solid #e5e7eb; font-size: 12.5px; font-weight: 700; color: #111827; }
.mini-form { display: grid; grid-template-columns: 1fr 1fr; gap: 8px; padding: 10px 12px; background: #fafafa; border-bottom: 1px solid #f3f4f6; }
.mini-form-actions { grid-column: span 2; display: flex; gap: 6px; }
.mini-tbl { font-size: 11.5px; }
.mini-tbl th { font-size: 10.5px; padding: 5px 7px; }
.mini-tbl td { padding: 5px 7px; }
.mini-footer { font-size: 11px; color: #9ca3af; padding: 5px 10px; display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #f3f4f6; }
.empty-state-sm { display: flex; flex-direction: column; align-items: center; padding: 20px; gap: 6px; color: #9ca3af; text-align: center; }
.empty-state-sm .es-icon { font-size: 28px; }
.empty-state-sm p { font-size: 12px; margin: 0; }

/* Code */
.code { font-family: monospace; background: #f3f4f6; padding: 1px 5px; border-radius: 4px; font-size: 11px; }

/* Badges */
.badge { display: inline-block; padding: 2px 8px; border-radius: 999px; font-size: 11px; font-weight: 600; background: #e5e7eb; color: #374151; }
.badge.dang_day, .badge.hoat_dong { background: #dcfce7; color: #16a34a; }
.badge.tam_nghi { background: #fef9c3; color: #854d0e; }
.badge.nghi_viec { background: #fee2e2; color: #991b1b; }
.badge.dang_hieu_luc { background: #dcfce7; color: #16a34a; }
.badge.ngung_ap_dung { background: #f3f4f6; color: #6b7280; }
.badge.kha_dung { background: #dbeafe; color: #1d4ed8; }
.badge.ban { background: #fee2e2; color: #991b1b; }
.badge.uu_tien { background: #fef3c7; color: #92400e; }

/* Buttons */
.btn { border: none; border-radius: 7px; padding: 6px 12px; font-size: 12px; font-weight: 600; cursor: pointer; font-family: inherit; }
.btn-primary { background: #1d4ed8; color: #fff; }
.btn-primary:hover { background: #1e40af; }
.btn-secondary { background: #f3f4f6; color: #374151; border: 1px solid #e5e7eb; }
.btn-secondary:hover { background: #e5e7eb; }
.btn-outline { background: transparent; color: #1d4ed8; border: 1px solid #93c5fd; }
.btn-outline:hover { background: #eff6ff; }
.btn-danger { background: #fef2f2; color: #dc2626; border: 1px solid #fca5a5; }
.btn-danger:hover { background: #fee2e2; }
.btn-xs { padding: 3px 8px; font-size: 11px; border-radius: 5px; margin-right: 2px; }
.btn-icon-sm { background: #f3f4f6; border: 1px solid #e5e7eb; border-radius: 6px; padding: 4px 8px; font-size: 13px; cursor: pointer; }
.btn:disabled { opacity: 0.55; cursor: not-allowed; }

@media (max-width: 1100px) {
  .content-split { grid-template-columns: 1fr; }
  .bottom-row { grid-template-columns: 1fr; }
  .gv-stats-row { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 700px) {
  .gv-stats-row { grid-template-columns: 1fr 1fr; }
  .form-grid { grid-template-columns: 1fr 1fr; }
}
</style>
