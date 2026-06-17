<template>
  <section class="sv-flow-page">

    <div class="sv-content">

      <!-- ===== HEADER ===== -->
      <header class="sv-page-header">
        <h1>Quản lý sinh viên</h1>
        <p>Quản lý hồ sơ sinh viên, lớp hành chính, danh sách sinh viên toàn trường và sinh viên bảo lưu.</p>
      </header>

      <!-- ===== BREADCRUMB cho các bước sâu (giữ điều hướng quay lại) ===== -->
      <nav v-if="!dangOBuocQuanLy" class="sv-summary-bar sv-breadcrumb-sticky">
        <button
            v-for="(item, i) in summaryItems"
            :key="i"
            type="button"
            class="sv-summary-chip"
            :class="{ active: i === summaryItems.length - 1, clickable: item.clickable }"
            @click="item.clickable && quayVeBuoc(item.step)"
        >
          <span class="sv-summary-name">{{ item.name }}:</span>
          <strong class="sv-summary-value">{{ item.value }}</strong>
        </button>
      </nav>

      <!-- ===== THÔNG BÁO ===== -->
      <div
          v-if="thongBao && thongBaoBuoc === buoc"
          class="sv-message"
          :class="thongBaoLoai"
      >
        {{ thongBao }}
      </div>

      <!-- ===================================================================== -->
      <!-- KHU QUẢN LÝ: nội dung bên trái – panel gợi ý bên phải (như ảnh)        -->
      <!-- ===================================================================== -->
      <template v-if="dangOBuocQuanLy">
        <div class="sv-manage-layout">

          <!-- CỘT TRÁI: NỘI DUNG -->
          <div class="sv-manage-main">

            <!-- BỘ LỌC + THỐNG KÊ GỌN -->
            <div class="sv-filter-card sv-filter-card-with-stats">
              <div class="sv-filter-grid">
                <label class="sv-filter-field">
                  <span>Ngành</span>
                  <select v-model="nganhSelectId">
                    <option value="">Chọn ngành...</option>
                    <option v-for="ng in danhSachNganh" :key="ng.id" :value="ng.id">
                      {{ ng.tenNganh }}
                    </option>
                  </select>
                </label>

                <label class="sv-filter-field">
                  <span>Chương trình</span>
                  <select v-model="chuongTrinhSelectId" :disabled="!nganhDangChon">
                    <option value="">Chọn chương trình...</option>
                    <option v-for="ct in chuongTrinhTheoNganh" :key="ct.id" :value="ct.id">
                      {{ ct.maChuongTrinh }}
                    </option>
                  </select>
                </label>

                <label class="sv-filter-field">
                  <span>Phiên bản</span>
                  <select v-model="versionSelectId" :disabled="!chuongTrinhDangChon">
                    <option value="">Chọn phiên bản...</option>
                    <option v-for="v in versionTheoCT" :key="v.id" :value="v.id">
                      {{ v.maVersion }}{{ v.tenVersion ? ' - ' + v.tenVersion : '' }}
                    </option>
                  </select>
                </label>
              </div>

              <div class="sv-filter-bottom-row">
                <div class="sv-filter-hint">
                  <span class="sv-filter-hint-ico">ℹ️</span>
                  <span>
                  Chọn lần lượt <strong>Ngành → Chương trình → Phiên bản</strong> để mở danh sách lớp hành chính,
                  sau đó <strong>Tiếp nhận sinh viên</strong> hoặc <strong>Mở lớp học phần</strong>.
                </span>
                </div>

                <div class="sv-mini-stats" aria-label="Thống kê nhanh">
                  <div class="sv-mini-stat">
                    <span class="sv-mini-stat-icon stat-blue">👥</span>
                    <span class="sv-mini-stat-text">Tổng sinh viên</span>
                    <strong>{{ soSinhVienToanTruong }}</strong>
                  </div>
                  <div class="sv-mini-stat">
                    <span class="sv-mini-stat-icon stat-violet">🏫</span>
                    <span class="sv-mini-stat-text">Lớp hành chính</span>
                    <strong>{{ danhSachLHC.length }}</strong>
                  </div>
                  <div class="sv-mini-stat">
                    <span class="sv-mini-stat-icon stat-orange">📌</span>
                    <span class="sv-mini-stat-text">Bảo lưu</span>
                    <strong>{{ soSinhVienBaoLuu }}</strong>
                  </div>
                </div>
              </div>
            </div>

            <!-- TRẠNG THÁI: CHƯA CHỌN ĐỦ -->
            <div v-if="!versionDangChon" class="sv-card sv-empty-state">
              <div class="sv-empty-ico">🗂️</div>
              <h2>Chọn ngành, chương trình và phiên bản</h2>
              <p>Hãy chọn đủ <strong>Ngành → Chương trình → Phiên bản</strong> ở bộ lọc phía trên để tạo và quản lý lớp hành chính.</p>
            </div>

            <!-- FORM TẠO / SỬA LỚP HÀNH CHÍNH -->
            <div v-if="versionDangChon" class="sv-card">
              <div class="sv-card-title sv-card-title-toolbar">
                <div>
                  <h2>{{ idLHCSua ? 'Sửa lớp hành chính' : 'Tạo lớp hành chính' }}</h2>
                  <p>Tạo lớp hành chính để tiếp nhận sinh viên và mở lớp học phần.</p>
                </div>
                <div class="sv-title-tools">
                  <button type="submit" form="form-lhc" class="sv-btn-primary" :disabled="dangLuu">
                    + {{ dangLuu ? 'Đang lưu...' : (idLHCSua ? 'Cập nhật' : 'Tạo lớp') }}
                  </button>
                  <button type="button" class="secondary" @click="resetFormLHC">↻ Làm mới</button>
                </div>
              </div>
              <form id="form-lhc" class="sv-grid sv-grid-4" @submit.prevent="luuLopHanhChinh">
                <label>Mã lớp<input v-model.trim="formLHC.maLop" placeholder="Nhập mã lớp" required/></label>
                <label>Tên lớp<input v-model.trim="formLHC.tenLop" placeholder="Nhập tên lớp" required/></label>
                <label>Sĩ số tối đa<input v-model.number="formLHC.siSo" type="number" min="0" placeholder="Nhập số lượng"/></label>
                <label>Trạng thái
                  <select v-model="formLHC.trangThai">
                    <option value="du_kien">Dự kiến</option>
                    <option value="dang_hoc">Đang học</option>
                    <option value="tam_dung">Tạm dừng</option>
                    <option value="da_tot_nghiep">Đã tốt nghiệp</option>
                    <option value="huy">Hủy</option>
                  </select>
                </label>
                <label>Ngày bắt đầu nhận sinh viên<input v-model="formLHC.ngayBatDauNhanSinhVien" type="date"/></label>
                <label>Ngày kết thúc nhận sinh viên<input v-model="formLHC.ngayKetThucNhanSinhVien" type="date"/></label>
                <label v-if="idLHCSua" class="sv-span-2">
                  Chốt tuyển sinh
                  <div class="sv-chot-tuyen-sinh-info">
                    <span v-if="formLHC.daChotTuyenSinh" class="sv-status-pill status-da_chot">✔ Đã chốt</span>
                    <span v-else class="sv-status-pill status-chua_chot">Chưa chốt</span>
                    <span v-if="formLHC.ngayChotTuyenSinh" class="sv-note-text">&nbsp;{{ dinhDangNgay(formLHC.ngayChotTuyenSinh) }}</span>
                  </div>
                </label>
                <label class="sv-span-4">Ghi chú<input v-model.trim="formLHC.ghiChu" placeholder="Nhập ghi chú (nếu có)"/></label>
              </form>
            </div>

            <!-- DANH SÁCH LỚP HÀNH CHÍNH -->
            <div v-if="versionDangChon" class="sv-card sv-lhc-list-card">
              <div class="sv-card-title sv-card-title-toolbar">
                <div>
                  <h2>Danh sách lớp hành chính</h2>
                  <p>
                    Chọn lớp để tiếp nhận sinh viên hoặc mở lớp học phần.
                    Sĩ số hiển thị theo dạng <strong>hiện tại / tối đa</strong>.
                  </p>
                </div>
                <div class="sv-title-tools">
                  <input
                      v-model.trim="tuKhoaLHC"
                      class="sv-search-input"
                      placeholder="Tìm mã lớp, tên lớp..."
                  />
                  <span class="so-ban-ghi">
                  {{ danhSachLHCHienThi.length }} / {{ danhSachLHC.length }} lớp
                </span>
                </div>
              </div>

              <div class="sv-table-wrap sv-table-wrap-full sv-lhc-table-wrap">
                <table class="sv-table sv-lhc-table">
                  <thead>
                  <tr>
                    <th>STT</th>
                    <th>Mã lớp học phần</th>
                    <th>Tên lớp học phần</th>
                    <th>Sĩ số hiện tại / tối đa</th>
                    <th>Trạng thái</th>
                    <th>Ngày nhận sinh viên</th>
                    <th>Tuyển sinh</th>
                    <th>Ghi chú</th>
                    <th>Thao tác</th>
                  </tr>
                  </thead>

                  <tbody>
                  <tr v-for="(lop, i) in danhSachLHCHienThi" :key="lop.id">
                    <td>{{ i + 1 }}</td>

                    <td>
                      <strong class="sv-code">{{ lop.maLop }}</strong>
                    </td>

                    <td>
                      <div class="sv-main-text">{{ lop.tenLop }}</div>
                    </td>

                    <td>
                    <span class="sv-capacity-badge">
                      <strong>{{ demSiSoHienTaiLHC(lop) }}</strong>
                      <span>/</span>
                      <span>{{ laySiSoToiDaLHC(lop) }}</span>
                    </span>
                    </td>

                    <td>
                    <span class="sv-status-pill" :class="`status-${lop.trangThai || 'none'}`">
                      {{ nhanTrangThaiLHC(lop.trangThai) }}
                    </span>
                    </td>

                    <td>
                      <div v-if="lop.ngayBatDauNhanSinhVien || lop.ngayKetThucNhanSinhVien" class="sv-note-text">
                        <div v-if="lop.ngayBatDauNhanSinhVien">Từ: {{ dinhDangNgay(lop.ngayBatDauNhanSinhVien) }}</div>
                        <div v-if="lop.ngayKetThucNhanSinhVien">Đến: {{ dinhDangNgay(lop.ngayKetThucNhanSinhVien) }}</div>
                      </div>
                      <span v-else class="sv-note-text">—</span>
                    </td>

                    <td>
                      <span v-if="lop.daChotTuyenSinh" class="sv-status-pill status-da_chot">✔ Đã chốt</span>
                      <span v-else class="sv-status-pill status-chua_chot">Chưa chốt</span>
                      <div v-if="lop.ngayChotTuyenSinh" class="sv-note-text" style="font-size:11px;margin-top:2px">
                        {{ dinhDangNgay(lop.ngayChotTuyenSinh) }}
                      </div>
                    </td>

                    <td>
                      <span class="sv-note-text">{{ lop.ghiChu || '—' }}</span>
                    </td>

                    <td>
                      <div class="sv-row-actions sv-row-actions-nowrap">
                        <button type="button" class="small btn-nhanh1" @click="chonLHCNhanhTiepNhan(lop)">
                          Tiếp nhận sinh viên →
                        </button>

                        <button type="button" class="small btn-nhanh2" @click="chonLHCNhanhLHP(lop)">
                          Mở lớp học phần →
                        </button>

                        <button
                            v-if="!lop.daChotTuyenSinh"
                            type="button"
                            class="small btn-chot-ts"
                            @click="chotTuyenSinhLHC(lop)"
                        >
                          Chốt tuyển sinh
                        </button>

                        <button
                            v-if="lop.daChotTuyenSinh"
                            type="button"
                            class="small btn-huy-chot-ts"
                            @click="huyChoTuyenSinhLHC(lop)"
                        >
                          Hủy chốt
                        </button>

                        <button type="button" class="secondary small" @click="suaLopHanhChinh(lop)">
                          Sửa
                        </button>

                        <button type="button" class="danger small" @click="xoaLopHanhChinh(lop)">
                          Xóa
                        </button>
                      </div>
                    </td>
                  </tr>

                  <tr v-if="!danhSachLHCHienThi.length">
                    <td colspan="9" class="empty">
                      Không tìm thấy lớp hành chính phù hợp.
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>

              <div class="sv-table-footer">
                Hiển thị {{ danhSachLHCHienThi.length }} / {{ danhSachLHC.length }} lớp
              </div>
            </div>
          </div>

        </div>
      </template>

      <!-- ===== BƯỚC 4.1: TIẾP NHẬN SINH VIÊN ===== -->
      <template v-if="buoc === 'tiepNhan'">
        <div v-if="taiKhoanMoi && taiKhoanMoi.lopHanhChinhId === lopHanhChinhDangChon?.id" class="sv-account-box">
          <strong>Tài khoản vừa cấp:</strong>
          <span>Mã sinh viên: {{ taiKhoanMoi.maSinhVien }}</span>
          <span>Gmail: {{ taiKhoanMoi.emailTaiKhoan }}</span>
          <span>Mật khẩu tạm: {{ taiKhoanMoi.matKhauTam }}</span>
        </div>
        <div class="sv-card sv-student-receive-card">
          <div class="sv-card-title">
            <div>
              <h2>{{ idSVSua ? 'Cập nhật hồ sơ sinh viên' : 'Tiếp nhận hồ sơ sinh viên' }}</h2>
              <p>
                Lớp hành chính:
                <strong>{{ lopHanhChinhDangChon?.maLop }} - {{ lopHanhChinhDangChon?.tenLop }}</strong>
                | Hệ thống tự sinh mã sinh viên và cấp tài khoản.
              </p>
            </div>
          </div>

          <form class="sv-receive-layout" @submit.prevent="luuSinhVien">
            <!-- CỘT TRÁI: HÌNH ẢNH & GIẤY TỜ -->
            <aside class="sv-file-panel">
              <div class="sv-file-panel-title">
                <h3>Hình ảnh & giấy tờ</h3>
                <p>Kiểm tra ảnh trước khi lưu hồ sơ.</p>
              </div>

              <div class="sv-document-grid">
                <div class="sv-document-row sv-document-row-main">
                  <div class="sv-document-left">
                    <button
                        v-if="filesSV.anhChanDung"
                        type="button"
                        class="sv-remove-file"
                        @click="boTep('anhChanDung')"
                    >
                      Bỏ
                    </button>

                    <div class="sv-document-preview sv-portrait-preview">
                      <img
                          v-if="previewAnhChanDung"
                          :src="previewAnhChanDung"
                          alt="Ảnh chân dung"
                      />
                      <div v-else class="sv-document-placeholder">
                        Chưa có ảnh
                      </div>
                    </div>
                  </div>

                  <div class="sv-document-right">
                    <div class="sv-document-title">Ảnh chân dung</div>

                    <label class="sv-file-picker">
                      <input type="file" accept="image/*" @change="chonTep($event,'anhChanDung')" />
                      <span>Chọn ảnh</span>
                    </label>

                    <div class="sv-file-meta">
                      <strong>{{ layTenFile(filesSV.anhChanDung) }}</strong>
                      <small>{{ layDungLuongFile(filesSV.anhChanDung) }}</small>
                    </div>
                  </div>
                </div>

                <div class="sv-document-row">
                  <div class="sv-document-left">
                    <button
                        v-if="filesSV.cccdTruoc"
                        type="button"
                        class="sv-remove-file"
                        @click="boTep('cccdTruoc')"
                    >
                      Bỏ
                    </button>

                    <div class="sv-document-preview">
                      <img
                          v-if="previewCccdTruoc"
                          :src="previewCccdTruoc"
                          alt="CCCD mặt trước"
                      />
                      <div v-else-if="filesSV.cccdTruoc" class="sv-document-pdf">
                        PDF
                      </div>
                      <div v-else class="sv-document-placeholder">
                        Chưa có tệp
                      </div>
                    </div>
                  </div>

                  <div class="sv-document-right">
                    <div class="sv-document-title">CCCD mặt trước</div>

                    <label class="sv-file-picker">
                      <input type="file" accept="image/*,.pdf" @change="chonTep($event,'cccdTruoc')" />
                      <span>Chọn tệp</span>
                    </label>

                    <div class="sv-file-meta">
                      <strong>{{ layTenFile(filesSV.cccdTruoc) }}</strong>
                      <small>{{ layDungLuongFile(filesSV.cccdTruoc) }}</small>
                    </div>
                  </div>
                </div>

                <div class="sv-document-row">
                  <div class="sv-document-left">
                    <button
                        v-if="filesSV.cccdSau"
                        type="button"
                        class="sv-remove-file"
                        @click="boTep('cccdSau')"
                    >
                      Bỏ
                    </button>

                    <div class="sv-document-preview">
                      <img
                          v-if="previewCccdSau"
                          :src="previewCccdSau"
                          alt="CCCD mặt sau"
                      />
                      <div v-else-if="filesSV.cccdSau" class="sv-document-pdf">
                        PDF
                      </div>
                      <div v-else class="sv-document-placeholder">
                        Chưa có tệp
                      </div>
                    </div>
                  </div>

                  <div class="sv-document-right">
                    <div class="sv-document-title">CCCD mặt sau</div>

                    <label class="sv-file-picker">
                      <input type="file" accept="image/*,.pdf" @change="chonTep($event,'cccdSau')" />
                      <span>Chọn tệp</span>
                    </label>

                    <div class="sv-file-meta">
                      <strong>{{ layTenFile(filesSV.cccdSau) }}</strong>
                      <small>{{ layDungLuongFile(filesSV.cccdSau) }}</small>
                    </div>
                  </div>
                </div>

                <div class="sv-document-row">
                  <div class="sv-document-left">
                    <button
                        v-if="filesSV.bangCap"
                        type="button"
                        class="sv-remove-file"
                        @click="boTep('bangCap')"
                    >
                      Bỏ
                    </button>

                    <div class="sv-document-preview">
                      <img
                          v-if="previewBangCap"
                          :src="previewBangCap"
                          alt="Bằng cấp"
                      />
                      <div v-else-if="filesSV.bangCap" class="sv-document-pdf">
                        PDF
                      </div>
                      <div v-else class="sv-document-placeholder">
                        Chưa có tệp
                      </div>
                    </div>
                  </div>

                  <div class="sv-document-right">
                    <div class="sv-document-title">Bằng cấp</div>

                    <label class="sv-file-picker">
                      <input type="file" accept="image/*,.pdf" @change="chonTep($event,'bangCap')" />
                      <span>Chọn tệp</span>
                    </label>

                    <div class="sv-file-meta">
                      <strong>{{ layTenFile(filesSV.bangCap) }}</strong>
                      <small>{{ layDungLuongFile(filesSV.bangCap) }}</small>
                    </div>
                  </div>
                </div>

                <div class="sv-document-row sv-document-row-other">
                  <div class="sv-document-right sv-document-right-full">
                    <div class="sv-document-title">Giấy tờ khác</div>

                    <label class="sv-file-picker sv-file-picker-small">
                      <input type="file" multiple @change="chonTepKhac" />
                      <span>Chọn nhiều tệp</span>
                    </label>

                    <div class="sv-other-files">
                      <span v-if="!filesSV.giayToKhac.length">Chưa chọn tệp</span>

                      <span
                          v-for="(file, index) in filesSV.giayToKhac"
                          :key="file.name"
                          class="sv-other-file-item"
                      >
            <strong>{{ file.name }}</strong>
            <small>{{ layDungLuongFile(file) }}</small>
            <button type="button" @click="boGiayToKhac(index)">Bỏ</button>
          </span>
                    </div>
                  </div>
                </div>
              </div>
            </aside>

            <!-- CỘT PHẢI: THÔNG TIN SINH VIÊN -->
            <section class="sv-info-panel">
              <div class="sv-section-box">
                <h3>Thông tin sinh viên</h3>

                <div class="sv-form-table sv-form-table-3">
                  <label>Họ tên<input v-model.trim="formSV.hoTen" required /></label>
                  <label>Gmail<input v-model.trim="formSV.email" type="email" required /></label>
                  <label>Số điện thoại<input v-model.trim="formSV.soDienThoai" required /></label>

                  <label>Ngày sinh<input v-model="formSV.ngaySinh" type="date" required /></label>
                  <label>Giới tính
                    <select v-model="formSV.gioiTinh" required>
                      <option value="">Chọn</option>
                      <option value="nam">Nam</option>
                      <option value="nu">Nữ</option>
                      <option value="khac">Khác</option>
                    </select>
                  </label>
                  <label>Ngày nhập học<input v-model="formSV.ngayNhapHoc" type="date" /></label>

                  <label class="sv-col-span-3">Địa chỉ liên hệ
                    <textarea v-model.trim="formSV.diaChi" rows="2" required></textarea>
                  </label>

                  <label class="sv-col-span-3">Địa chỉ thường trú
                    <textarea v-model.trim="formSV.diaChiThuongTru" rows="2" required></textarea>
                  </label>
                </div>
              </div>

              <div class="sv-section-box">
                <h3>CCCD & bằng cấp</h3>

                <div class="sv-form-table sv-form-table-3">
                  <label>Số CCCD<input v-model.trim="formSV.soCccd" required /></label>
                  <label>Ngày cấp<input v-model="formSV.ngayCapCccd" type="date" required /></label>
                  <label>Nơi cấp<input v-model.trim="formSV.noiCapCccd" required /></label>

                  <label>Bằng cấp<input v-model.trim="formSV.bangCap" required /></label>
                  <label>Năm tốt nghiệp <span class="bat-buoc">*</span>
                    <input v-model.number="formSV.namTotNghiep" type="number" min="1950" required />
                  </label>
                  <label>Trường tốt nghiệp<input v-model.trim="formSV.truongTotNghiep" required /></label>
                </div>
              </div>

              <div class="sv-section-box">
                <h3>Thông tin cha</h3>

                <div class="sv-form-table sv-form-table-4">
                  <label>Họ tên cha <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.hoTenCha" required />
                  </label>
                  <label>Nghề nghiệp cha
                    <input v-model.trim="formSV.ngheNghiepCha" />
                  </label>
                  <label>Số điện thoại cha <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.sdtCha" required />
                  </label>
                  <label>Gmail cha
                    <input v-model.trim="formSV.emailCha" type="email" placeholder="Không bắt buộc" />
                  </label>
                </div>
              </div>

              <div class="sv-section-box">
                <h3>Thông tin mẹ</h3>

                <div class="sv-form-table sv-form-table-4">
                  <label>Họ tên mẹ <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.hoTenMe" required />
                  </label>
                  <label>Nghề nghiệp mẹ
                    <input v-model.trim="formSV.ngheNghiepMe" />
                  </label>
                  <label>Số điện thoại mẹ <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.sdtMe" required />
                  </label>
                  <label>Gmail mẹ
                    <input v-model.trim="formSV.emailMe" type="email" placeholder="Không bắt buộc" />
                  </label>
                </div>
              </div>

              <div class="sv-section-box">
                <h3>Người thân liên hệ</h3>

                <div class="sv-form-table sv-form-table-4">
                  <label>Họ tên người thân <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.hoTenNguoiThan" required />
                  </label>
                  <label>Quan hệ <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.quanHeNguoiThan" required />
                  </label>
                  <label>Số điện thoại người thân <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.sdtNguoiThan" required />
                  </label>
                  <label>Gmail người thân <span class="bat-buoc">*</span>
                    <input v-model.trim="formSV.emailNguoiThan" type="email" required />
                  </label>
                </div>
              </div>

              <div class="sv-section-box">
                <label>Ghi chú hồ sơ
                  <textarea v-model.trim="formSV.ghiChuHoSo" rows="2"></textarea>
                </label>
              </div>

              <div class="sv-actions sv-receive-actions">
                <button type="submit" :disabled="dangLuu">
                  {{ dangLuu ? 'Đang lưu...' : (idSVSua ? 'Cập nhật sinh viên' : 'Tiếp nhận sinh viên') }}
                </button>
                <button type="button" class="secondary" @click="resetFormSV">Làm mới</button>
              </div>
            </section>
          </form>
        </div>

        <div class="sv-card">
          <div class="sv-card-title">
            <div><h2>Danh sách sinh viên trong lớp</h2>
              <p>Sĩ số: {{ danhSachSVTrongLop.length }} / {{ lopHanhChinhDangChon?.siSo || '?' }}</p></div>
            <input v-model.trim="tuKhoaSV" placeholder="Tìm mã sinh viên, họ tên, gmail..." style="max-width:260px"/>
          </div>
          <div class="sv-table-wrap">
            <table class="sv-table">
              <thead>
              <tr>
                <th>Ảnh</th>
                <th>Mã sinh viên</th>
                <th>Họ tên</th>
                <th>Gmail</th>
                <th>Số điện thoại</th>
                <th>CCCD</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="sv in svHienThi" :key="sv.id">
                <td>
                  <img
                      v-if="layIdAnhChanDung(sv)"
                      class="sv-avatar"
                      :src="urlTep(layIdAnhChanDung(sv))"
                      alt="Ảnh sinh viên"
                      @error="anAnhLoi"
                  />
                  <span v-else>—</span>
                </td>
                <td>{{ sv.maSinhVien }}</td>
                <td>{{ sv.hoTen }}</td>
                <td>{{ sv.email }}</td>
                <td>{{ sv.soDienThoai }}</td>
                <td>{{ sv.soCccd }}</td>
                <td>{{ sv.trangThai }}</td>
                <td>
                  <div class="sv-row-actions">
                    <button type="button" class="secondary small" @click="suaSinhVien(sv)">Sửa</button>
                    <button type="button" class="danger small" @click="xoaSinhVien(sv)">Xóa</button>
                  </div>
                </td>
              </tr>
              <tr v-if="!svHienThi.length">
                <td colspan="8" class="empty">Chưa có sinh viên trong lớp này.</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>

      <!-- ===== BƯỚC 4.2: MỞ LỚP HỌC PHẦN ===== -->
      <template v-if="buoc === 'lopHocPhan'">

        <!-- AUTO MỞ LỚP HỌC PHẦN THEO KỲ / PHIÊN BẢN -->
        <div class="sv-card sv-auto-tao-card sv-auto-compact-card sv-lhp-auto-card">
          <div class="sv-card-title sv-card-title-toolbar sv-lhp-auto-title">
            <div class="sv-lhp-title-left">
              <h2>⚡ Tự động mở lớp học phần theo kỳ / phiên bản</h2>
              <p>Tự động tính toán và tạo lớp học phần theo kỳ học, sĩ số và lớp hành chính đã chọn.</p>
            </div>

            <div class="sv-auto-tao-actions sv-lhp-auto-actions">
              <button
                  type="button"
                  class="secondary"
                  :disabled="dangAutoTao"
                  @click="xuLyAutoTao('PREVIEW')"
              >
                🔍 Xem trước
              </button>

              <button
                  type="button"
                  class="sv-btn-du-kien"
                  :disabled="dangAutoTao"
                  @click="xuLyAutoTao('DU_KIEN')"
              >
                📋 Tạo dự kiến
              </button>

              <button
                  type="button"
                  class="sv-btn-chinh-thuc"
                  :disabled="dangAutoTao"
                  @click="xuLyAutoTao('CHINH_THUC')"
              >
                ✅ Chốt chính thức
              </button>

              <span v-if="dangAutoTao" class="sv-note-text sv-lhp-loading">Đang xử lý...</span>
            </div>
          </div>

          <form class="sv-auto-tao-form sv-lhp-auto-form" @submit.prevent>
            <label class="sv-auto-field sv-auto-field-ky">
              Kỳ học <span class="sv-required">*</span>
              <select v-model="formAutoTao.khungKyId">
                <option value="">Chọn kỳ học</option>
                <option v-for="ky in khungKyTheoVersion" :key="ky.id" :value="ky.id">
                  {{ ky.maKy }} - {{ ky.tenKy }}
                </option>
              </select>
            </label>

            <label class="sv-auto-field sv-auto-field-min">
              Sĩ số tối thiểu <span class="sv-required">*</span>
              <input v-model.number="formAutoTao.siSoToiThieu" type="number" min="1" placeholder="Nhập sĩ số tối thiểu"/>
            </label>

            <label class="sv-auto-field sv-auto-field-max">
              Sĩ số tối đa <span class="sv-required">*</span>
              <input v-model.number="formAutoTao.siSoToiDa" type="number" min="1" placeholder="Nhập sĩ số tối đa"/>
            </label>

            <label class="sv-auto-field sv-auto-field-prefix">
              Tiền tố mã lớp
              <input v-model.trim="formAutoTao.tienToMaLop" placeholder="Ví dụ: LHP"/>
            </label>

            <label class="sv-auto-check sv-checkbox-label">
              <input type="checkbox" class="sv-check" v-model="formAutoTao.tuDongPhanBoSinhVien"/>
              Tự phân bổ sinh viên
            </label>

            <details class="sv-auto-lhc-dropdown sv-lhc-chon-wrap">
              <summary>
                <span class="sv-auto-lhc-summary-left">
                  <strong>Lớp hành chính</strong>
                  <span class="sv-required">*</span>
                </span>
                <span class="sv-auto-lhc-summary-count">
                  {{ formAutoTao.lopHanhChinhIds.length }} / {{ danhSachLHC.length }}
                </span>
              </summary>

              <div class="sv-lhc-checkbox-list sv-lhc-checkbox-dropdown-list">
                <label
                    v-for="lhc in danhSachLHC"
                    :key="lhc.id"
                    class="sv-lhc-chon-item"
                    :class="{ 'sv-lhc-chon-item--chot': lhc.daChotTuyenSinh }"
                >
                  <input
                      type="checkbox"
                      class="sv-check"
                      :value="lhc.id"
                      v-model="formAutoTao.lopHanhChinhIds"
                  />
                  <span class="sv-lhc-name">{{ lhc.maLop }} - {{ lhc.tenLop }}</span>
                  <span v-if="lhc.daChotTuyenSinh" class="sv-status-pill status-da_chot">✔ Đã chốt</span>
                  <span v-else class="sv-status-pill status-chua_chot">Chưa chốt</span>
                </label>

                <span v-if="!danhSachLHC.length" class="sv-note-text">Chưa có lớp hành chính nào.</span>
              </div>
            </details>
          </form>

          <div v-if="ketQuaAutoTao" class="sv-auto-tao-result">
            <div class="sv-auto-tao-summary">
              <span>Chế độ: <strong>{{ ketQuaAutoTao.mode }}</strong></span>
              <span>Tổng sinh viên: <strong>{{ ketQuaAutoTao.tongSinhVien }}</strong></span>
              <span>Số môn học: <strong>{{ ketQuaAutoTao.tongMonTrongKy }}</strong></span>
              <span>Lớp học phần dự kiến: <strong>{{ ketQuaAutoTao.tongLopHocPhanDuKien }}</strong></span>
              <span v-if="ketQuaAutoTao.tongLopHocPhanDaTao !== undefined">
                Đã tạo hoặc cập nhật: <strong>{{ ketQuaAutoTao.tongLopHocPhanDaTao }}</strong>
              </span>
            </div>

            <div v-if="ketQuaAutoTao.canhBao?.length" class="sv-auto-tao-warnings">
              <strong>⚠ Cảnh báo:</strong>
              <ul>
                <li v-for="(cb, i) in ketQuaAutoTao.canhBao" :key="i">{{ cb }}</li>
              </ul>
            </div>

            <div v-if="ketQuaAutoTao.loi?.length" class="sv-auto-tao-errors">
              <strong>✘ Lỗi:</strong>
              <ul>
                <li v-for="(l, i) in ketQuaAutoTao.loi" :key="i">{{ l }}</li>
              </ul>
            </div>

            <div v-if="ketQuaAutoTao.monResults?.length" class="sv-table-wrap sv-auto-result-table-wrap">
              <table class="sv-table">
                <thead>
                <tr>
                  <th>Môn học</th>
                  <th>Tổng sinh viên</th>
                  <th>Số lớp cần tạo</th>
                  <th>Lớp học phần</th>
                  <th>Cảnh báo / Lỗi</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="mon in ketQuaAutoTao.monResults" :key="mon.chuongTrinhMonId">
                  <td>
                    <strong class="sv-code">{{ mon.maMon }}</strong>
                    <div class="sv-main-text">{{ mon.tenMon }}</div>
                  </td>
                  <td>{{ mon.tongSinhVien }}</td>
                  <td>{{ mon.soLopCanTao }}</td>
                  <td>
                    <div v-for="lhp in mon.lopHocPhanResults" :key="lhp.maLop" class="sv-auto-lhp-result-line">
                      <span class="sv-code">{{ lhp.maLop }}</span>
                      <span class="sv-note-text">({{ lhp.siSoDaPhanBo || lhp.siSoDuKien }} sinh viên)</span>
                      <span
                          v-if="lhp.trangThai"
                          class="sv-status-pill"
                          :class="`status-${lhp.trangThai}`"
                      >
                        {{ ({du_kien: 'Dự kiến', dang_mo: 'Đang mở', dang_hoc: 'Đang học', da_ket_thuc: 'Đã kết thúc', huy: 'Hủy'}[lhp.trangThai]) || lhp.trangThai }}
                      </span>
                    </div>
                  </td>
                  <td>
                    <div v-if="mon.canhBao" class="sv-note-text sv-text-warning">⚠ {{ mon.canhBao }}</div>
                    <div v-if="mon.loi" class="sv-note-text sv-text-danger">✘ {{ mon.loi }}</div>
                    <span v-if="!mon.canhBao && !mon.loi" class="sv-note-text">—</span>
                  </td>
                </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>

        <!-- FORM MỞ / SỬA LỚP HỌC PHẦN -->
        <div class="sv-card sv-lhp-open-card">
          <div class="sv-card-title sv-lhp-open-title">
            <div>
              <h2>{{ idLHPSua ? 'Sửa lớp học phần' : 'Mở lớp học phần' }}</h2>
              <p>
                Phiên bản: <strong>{{ versionDangChon?.maVersion }}</strong>
                <span>|</span>
                Lớp hành chính:
                <strong>{{ lopHanhChinhDangChon?.maLop }} - {{ lopHanhChinhDangChon?.tenLop }}</strong>
              </p>
            </div>
          </div>

          <form class="sv-lhp-open-form" @submit.prevent="luuLopHocPhan">
            <label class="sv-lhp-field-ky">
              Kỳ học
              <select v-model="formLHP.khungKyId" required @change="khiDoiKhungKyMoLop">
                <option value="">Chọn kỳ học</option>
                <option v-for="ky in khungKyTheoVersion" :key="ky.id" :value="ky.id">
                  {{ ky.maKy }} - {{ ky.tenKy }}
                </option>
              </select>
            </label>

            <label class="sv-lhp-field-mon">
              Môn học
              <select v-model="formLHP.chuongTrinhMonId" required :disabled="!formLHP.khungKyId">
                <option value="">Chọn môn học</option>
                <option v-for="mon in chuongTrinhMonTheoKhungKyDangChon" :key="mon.id" :value="mon.id">
                  {{ mon.maMonTrongCt || mon.maMon || mon.monHocMa || mon.id }} -
                  {{ mon.tenMonHoc || mon.monHocTen || mon.ghiChu || 'Môn thuộc kỳ đã chọn' }}
                </option>
              </select>
            </label>

            <label class="sv-lhp-field-loai">
              Loại lớp học phần
              <select v-model="formLHP.loaiLopHocPhan">
                <option value="CHUYEN_NGANH">Chuyên ngành</option>
                <option value="HOC_CHUNG">Học chung</option>
              </select>
            </label>

            <label class="sv-lhp-field-trangthai">
              Trạng thái
              <select v-model="formLHP.trangThai">
                <option value="du_kien">Dự kiến</option>
                <option value="dang_mo">Đang mở</option>
                <option value="dang_hoc">Đang học</option>
                <option value="da_ket_thuc">Đã kết thúc</option>
                <option value="huy">Hủy</option>
              </select>
            </label>

            <label class="sv-lhp-field-ma">
              Mã lớp học phần
              <input v-model.trim="formLHP.maLop" required/>
            </label>

            <label class="sv-lhp-field-ten">
              Tên lớp học phần
              <input v-model.trim="formLHP.tenLop" required/>
            </label>

            <label class="sv-lhp-field-number">
              Sĩ số tối thiểu
              <input v-model.number="formLHP.siSoToiThieu" type="number" min="1"/>
            </label>

            <label class="sv-lhp-field-number">
              Sĩ số tối đa
              <input v-model.number="formLHP.soLuongToiDa" type="number" min="1"/>
            </label>

            <label class="sv-lhp-field-buoi">
              Số buổi học
              <input
                  :value="soBuoiHocTuSyllabusDangChon"
                  type="number"
                  readonly
                  disabled
                  placeholder="Tự động"
              />
              <small class="sv-field-hint">Tự lấy từ đề cương môn học.</small>
            </label>

            <div class="sv-actions sv-lhp-open-actions">
              <button type="submit" :disabled="dangLuu">
                {{ dangLuu ? 'Lưu...' : (idLHPSua ? 'Cập nhật' : 'Mở lớp') }}
              </button>
              <button type="button" class="secondary" @click="resetFormLHP">Làm mới</button>
            </div>
          </form>
        </div>

        <!-- DANH SÁCH LỚP HỌC PHẦN THEO PHIÊN BẢN -->
        <div class="sv-card sv-lhp-list-card">
          <div class="sv-card-title sv-card-title-toolbar">
            <div>
              <h2>Danh sách lớp học phần theo phiên bản</h2>
              <p>Chọn lớp để phân bổ sinh viên. Danh sách được sắp xếp theo kỳ học.</p>
            </div>

            <div class="sv-title-tools">
              <select v-model="filterKhungKyId" class="sv-search-input">
                <option value="">Tất cả kỳ học</option>
                <option v-for="ky in khungKyTheoVersion" :key="ky.id" :value="String(ky.id)">
                  {{ ky.maKy }}{{ ky.tenKy ? ' - ' + ky.tenKy : '' }}
                </option>
              </select>
              <input
                  v-model.trim="tuKhoaLHP"
                  class="sv-search-input"
                  placeholder="Lọc mã môn, tên môn, mã lớp..."
              />
              <span class="so-ban-ghi">
                {{ danhSachLHPTheoVersion.length }} lớp
              </span>
            </div>
          </div>

          <div v-if="versionDangChon && thongKeCanhBaoLopHocPhan.tongMon > 0" class="sv-canh-bao-lhp-wrap">
            <div class="sv-canh-bao-lhp-panel" :class="{ 'is-ok': !thongKeCanhBaoLopHocPhan.thieu }">
              <div class="sv-canh-bao-lhp-head">
                <div class="sv-canh-bao-lhp-title">
                  <span class="sv-canh-bao-lhp-icon">🔔</span>
                  <div>
                    <strong>Cảnh báo lớp học phần</strong>
                    <p>
                      Có <b>{{ thongKeCanhBaoLopHocPhan.soMonChuaCoLop }}</b> môn chưa tạo lớp học phần
                      trên tổng <b>{{ thongKeCanhBaoLopHocPhan.tongMon }}</b> môn.
                    </p>
                  </div>
                </div>

                <div class="sv-canh-bao-lhp-total" :class="{ 'is-zero': !thongKeCanhBaoLopHocPhan.thieu }">
                  <span>Tổng lỗi</span>
                  <strong>{{ thongKeCanhBaoLopHocPhan.soMonChuaCoLop }}</strong>
                </div>
              </div>

              <div class="sv-hk-warning-buttons" aria-label="Cảnh báo lỗi theo học kỳ">
                <button
                    v-for="item in canhBaoLopHocPhanTheoKy"
                    :key="item.key"
                    type="button"
                    class="sv-hk-warning-btn"
                    :class="{
                      active: canhBaoHocKyDangChon?.key === item.key,
                      'has-error': item.soLoi > 0,
                      'is-ok': item.soLoi === 0
                    }"
                    @click="chonCanhBaoHocKy(item)"
                >
                  <span class="sv-hk-warning-bell">🔔</span>
                  <span class="sv-hk-warning-text">Cãnh báo HK {{ item.soThuTuKy }}</span>
                  <span class="sv-hk-warning-badge">{{ item.soLoi }}</span>
                </button>
              </div>

              <div v-if="canhBaoHocKyDangChon" class="sv-hk-warning-detail">
                <template v-if="canhBaoHocKyDangChon.soLoi > 0">
                  <div class="sv-hk-warning-detail-title">
                    <strong>Cảnh báo HK {{ canhBaoHocKyDangChon.soThuTuKy }}</strong>
                    <span>{{ canhBaoHocKyDangChon.soLoi }} môn chưa tạo lớp học phần</span>
                  </div>

                  <ul>
                    <li v-for="mon in canhBaoHocKyDangChon.danhSachMonThieu.slice(0, 6)" :key="mon.id">
                      <span class="sv-hk-warning-mon-code">{{ mon.maMonHoc || mon.monHocMa || mon.maMon || '—' }}</span>
                      <span class="sv-hk-warning-mon-name">{{ mon.tenMonHoc || mon.monHocTen || mon.tenMon || '—' }}</span>
                    </li>
                    <li v-if="canhBaoHocKyDangChon.soLoi > 6" class="sv-hk-warning-more">
                      ... và {{ canhBaoHocKyDangChon.soLoi - 6 }} môn khác
                    </li>
                  </ul>
                </template>

                <template v-else>
                  <div class="sv-hk-warning-empty">
                    ✓ HK {{ canhBaoHocKyDangChon.soThuTuKy }} chưa phát hiện lỗi thiếu lớp học phần.
                  </div>
                </template>
              </div>
            </div>
          </div>

          <div class="sv-table-wrap sv-lhp-table-wrap">
            <table class="sv-table sv-lhp-table">
              <thead>
              <tr>
                <th>STT</th>
                <th>Kỳ học</th>
                <th>Mã môn học</th>
                <th>Tên môn học</th>
                <th>Mã lớp học phần</th>
                <th>Tên lớp học phần</th>
                <th>Loại lớp</th>
                <th>Sĩ số hiện tại / tối đa</th>
                <th>Số buổi học</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(lhp, i) in danhSachLHPTheoVersion" :key="lhp.id">
                <td data-label="STT">{{ i + 1 }}</td>
                <td data-label="Kỳ học">{{ layTenKyCuaLHP(lhp) }}</td>
                <td data-label="Mã môn học">{{ layMaMonCuaLHP(lhp) }}</td>
                <td data-label="Tên môn học">{{ layTenMonCuaLHP(lhp) }}</td>
                <td data-label="Mã lớp học phần">{{ lhp.maLopHocPhan || lhp.maLop }}</td>
                <td data-label="Tên lớp học phần">{{ lhp.tenLopHocPhan || lhp.tenLop }}</td>
                <td data-label="Loại lớp">{{ layLoaiCuaLHP(lhp) }}</td>
                <td data-label="Sĩ số hiện tại / tối đa">{{ laySiSoHienTaiLHP(lhp) }} / {{ lhp.soLuongToiDa || lhp.siSoToiDa || 0 }}</td>
                <td data-label="Số buổi học">{{ lhp.soBuoiHoc || '—' }}</td>
                <td data-label="Trạng thái">
                  <span class="sv-status-pill" :class="`status-${lhp.trangThai || 'none'}`">
                    {{ ({du_kien: 'Dự kiến', dang_mo: 'Đang mở', dang_hoc: 'Đang học', da_ket_thuc: 'Đã kết thúc', huy: 'Hủy'}[lhp.trangThai]) || lhp.trangThai || '—' }}
                  </span>
                </td>
                <td data-label="Thao tác">
                  <div class="sv-row-actions sv-lhp-row-actions">
                    <RouterLink
                        class="small btn-xem-sv"
                        :to="{ path: '/giang-day', query: { lopHocPhanId: lhp.id, tab: 'sinh-vien' } }"
                    >
                      Sinh viên
                    </RouterLink>

                    <button type="button" class="small btn-nhanh2" @click="chonLHPPhanBo(lhp)">Phân bổ</button>

                    <RouterLink
                        class="small btn-giangday"
                        :to="{
                          name: 'GiangDay.ChiTietLopHocPhan',
                          params: { id: lhp.id },
                          query: {
                            nganhId: nganhDangChon?.id,
                            chuongTrinhId: chuongTrinhDangChon?.id,
                            versionId: versionDangChon?.id,
                            khungKyId: layKhungKyIdCuaChuongTrinhMon(layChuongTrinhMonCuaLHP(lhp))
                          }
                        }"
                    >
                      Giảng dạy
                    </RouterLink>

                    <button type="button" class="secondary small" @click="suaLopHocPhan(lhp)">Sửa</button>
                    <button type="button" class="danger small" @click="xoaLopHocPhan(lhp)">Xóa</button>
                  </div>
                </td>
              </tr>

              <tr v-if="!danhSachLHPTheoVersion.length">
                <td colspan="11" class="empty">Chưa có lớp học phần theo phiên bản này.</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>
      <!-- ===== BƯỚC 4.3: PHÂN BỔ SINH VIÊN ===== -->
      <template v-if="buoc === 'phanBo'">
        <div class="sv-card">
          <div class="sv-card-title">
            <div>
              <h2>Phân bổ sinh viên vào lớp học phần</h2>
              <p>Lớp học phần: <strong>{{ lhpDangPhanBo?.maLopHocPhan || lhpDangPhanBo?.maLop }}</strong> —
                {{ lhpDangPhanBo?.tenLopHocPhan || lhpDangPhanBo?.tenLop }}</p>
            </div>
            <div class="sv-row-actions">
              <button type="button" @click="phanBoSinhVien" :disabled="dangLuu || svDaChon.length === 0">
                {{ dangLuu ? 'Đang phân bổ...' : `Phân bổ ${svDaChon.length} sinh viên` }}
              </button>
              <RouterLink
                  v-if="lhpDangPhanBo"
                  class="btn-giangday"
                  :to="{
    name: 'GiangDay.ChiTietLopHocPhan',
    params: { id: lhpDangPhanBo.id },
    query: {
      nganhId: nganhDangChon?.id,
      chuongTrinhId: chuongTrinhDangChon?.id,
      versionId: versionDangChon?.id,
      khungKyId: layKhungKyIdCuaChuongTrinhMon(layChuongTrinhMonCuaLHP(lhpDangPhanBo))
    }
  }"
              >
                → Điều phối giảng dạy
              </RouterLink>
            </div>
          </div>
          <div class="sv-table-wrap">
            <table class="sv-table">
              <thead>
              <tr>
                <th>
                  <input
                      type="checkbox"
                      class="sv-check sv-check-all"
                      title="Chọn tất cả sinh viên chưa phân bổ"
                      @change="chonTatCaSV($event)"
                      :checked="daBamChonTatCaSV"
                  />
                </th>
                <th>Mã sinh viên</th>
                <th>Họ tên</th>
                <th>Gmail</th>
                <th>Số điện thoại</th>
                <th>Trạng thái phân bổ</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="sv in danhSachSVTrongLop" :key="sv.id"
                  :class="{ 'da-phan-bo': svDaPhanBoIds.has(String(sv.id)) }">
                <td>
                  <input
                      type="checkbox"
                      class="sv-check sv-check-row"
                      title="Chọn sinh viên này"
                      :value="sv.id"
                      v-model="svDaChon"
                      :disabled="svDaPhanBoIds.has(String(sv.id))"
                      @change="khiChonTungSinhVien"
                  />
                </td>
                <td>{{ sv.maSinhVien }}</td>
                <td>{{ sv.hoTen }}</td>
                <td>{{ sv.email }}</td>
                <td>{{ sv.soDienThoai }}</td>
                <td>
                  <span v-if="svDaPhanBoIds.has(String(sv.id))" class="sv-status done">Đã phân bổ</span>
                  <span v-else class="sv-status pending">Chưa phân bổ</span>
                </td>
              </tr>
              <tr v-if="!danhSachSVTrongLop.length">
                <td colspan="6" class="empty">Không có sinh viên trong lớp hành chính này.</td>
              </tr>
              </tbody>
            </table>
          </div>
        </div>
      </template>

    </div>
  </section>
</template>


<script setup>
import {computed, onMounted, reactive, ref, watch} from 'vue'
import {sinhVienService} from '../services/sinhVienService'
import {sinhVienApi} from '../api/sinhVienApi'

// ─── STATE ────────────────────────────────────────────────────────────────────
const buoc = ref('nganh')
const dangLuu = ref(false)
const thongBao = ref('')
const thongBaoLoai = ref('success')
const thongBaoBuoc = ref('')
const taiKhoanMoi = ref(null)
const SINH_VIEN_FLOW_STORAGE_KEY = 'sinh_vien_flow_state'

// Dữ liệu nền (load 1 lần)
const danhSachNganh = ref([])
const danhSachChuongTrinh = ref([])
const danhSachVersion = ref([])
const danhSachKhungKy = ref([])
const danhSachChuongTrinhMon = ref([])
const danhSachSyllabusMonHoc = ref([])

// Dữ liệu theo context
const danhSachLHC = ref([])
const danhSachSVTrongLop = ref([])
const danhSachSVChuongTrinh = ref([])
const danhSachLHP = ref([])
const danhSachLHPChuongTrinhMon = ref([])
const siSoTheoLopHocPhan = ref({})
// Context đang chọn
const nganhDangChon = ref(null)
const chuongTrinhDangChon = ref(null)
const versionDangChon = ref(null)
const lopHanhChinhDangChon = ref(null)

// Form ngành
const idNganhSua = ref(null)
const formNganh = reactive({maNganh: '', tenNganh: '', ghiChu: ''})

// Form chương trình
const idCTSua = ref(null)
const formCT = reactive({maChuongTrinh: '', tenChuongTrinh: '', ghiChu: ''})

// Form lớp hành chính
const idLHCSua = ref(null)
const formLHC = reactive({
  maLop: '', tenLop: '', siSo: 0, trangThai: 'du_kien', ghiChu: '',
  ngayBatDauNhanSinhVien: '', ngayKetThucNhanSinhVien: '',
  daChotTuyenSinh: false, ngayChotTuyenSinh: null
})

// Auto tạo lớp học phần
const formAutoTao = reactive({
  khungKyId: '',
  lopHanhChinhIds: [],
  siSoToiThieu: 10,
  siSoToiDa: 40,
  tienToMaLop: 'LHP',
  tuDongPhanBoSinhVien: true
})
const ketQuaAutoTao = ref(null)
const dangAutoTao = ref(false)

// Form sinh viên
const idSVSua = ref(null)
const formSV = reactive(taoFormSVMacDinh())
const filesSV = reactive({anhChanDung: null, cccdTruoc: null, cccdSau: null, bangCap: null, giayToKhac: []})

// Form lớp học phần
const idLHPSua = ref(null)
const formLHP = reactive(taoFormLHPMacDinh())

const tuKhoaSV = ref('')
const tuKhoaLHC = ref('')
const tuKhoaLHP = ref('')
const filterKhungKyId = ref('')
const khungKyCanhBaoDangChon = ref('')

const danhSachLHCHienThi = computed(() => {
  const kw = tuKhoaLHC.value.trim().toLowerCase()

  if (!kw) return danhSachLHC.value

  return danhSachLHC.value.filter(lop =>
      [
        lop.maLop,
        lop.tenLop,
        lop.ghiChu,
        nhanTrangThaiLHC(lop.trangThai)
      ].some(v => String(v || '').toLowerCase().includes(kw))
  )
})

// ─── BINDING GIAO DIỆN (chỉ phục vụ layout dropdown – KHÔNG đổi logic/API) ─────
const dangOBuocQuanLy = computed(() =>
    ['nganh', 'chuongTrinh', 'version', 'lopHanhChinh'].includes(buoc.value)
)

const nganhSelectId = computed({
  get: () => nganhDangChon.value?.id ?? '',
  set: (id) => {
    const ng = danhSachNganh.value.find(x => String(x.id) === String(id))
    ng ? chonNganh(ng) : quayVeBuoc('nganh')
  }
})

const chuongTrinhSelectId = computed({
  get: () => chuongTrinhDangChon.value?.id ?? '',
  set: (id) => {
    const ct = chuongTrinhTheoNganh.value.find(x => String(x.id) === String(id))
    ct ? chonChuongTrinh(ct) : quayVeBuoc('chuongTrinh')
  }
})

const versionSelectId = computed({
  get: () => versionDangChon.value?.id ?? '',
  set: (id) => {
    const v = versionTheoCT.value.find(x => String(x.id) === String(id))
    v ? chonVersion(v) : quayVeBuoc('version')
  }
})


const soSinhVienToanTruong = computed(() => danhSachSVChuongTrinh.value.length)
const soSinhVienBaoLuu = computed(() =>
    danhSachSVChuongTrinh.value.filter(s => String(s.trangThai || '') === 'bao_luu').length
)


// ─── COMPUTED ─────────────────────────────────────────────────────────────────
const summaryItems = computed(() => {
  const list = []

  list.push({
    name: 'Ngành',
    value: nganhDangChon.value?.tenNganh || 'Chưa chọn',
    step: 'nganh',
    clickable: buoc.value !== 'nganh'
  })

  if (nganhDangChon.value) {
    list.push({
      name: 'Chương trình',
      value: chuongTrinhDangChon.value?.maChuongTrinh
          || chuongTrinhDangChon.value?.tenChuongTrinh
          || 'Chưa chọn',
      step: 'chuongTrinh',
      clickable: buoc.value !== 'chuongTrinh'
    })
  }

  if (chuongTrinhDangChon.value) {
    list.push({
      name: 'Version',
      value: versionDangChon.value?.maVersion
          || versionDangChon.value?.tenVersion
          || 'Chưa chọn',
      step: 'version',
      clickable: buoc.value !== 'version'
    })
  }

  if (versionDangChon.value) {
    list.push({
      name: 'Lớp hành chính',
      value: lopHanhChinhDangChon.value
          ? `${lopHanhChinhDangChon.value.maLop || ''} - ${lopHanhChinhDangChon.value.tenLop || ''}`.trim()
          : 'Chưa chọn',
      step: 'lopHanhChinh',
      clickable: ['tiepNhan', 'lopHocPhan'].includes(buoc.value)
    })
  }

  if (buoc.value === 'tiepNhan') {
    list.push({
      name: 'Chức năng',
      value: 'Tiếp nhận sinh viên',
      step: 'tiepNhan',
      clickable: false
    })
  }

  if (buoc.value === 'lopHocPhan') {
    list.push({
      name: 'Chức năng',
      value: 'Mở lớp học phần',
      step: 'lopHocPhan',
      clickable: false
    })
  }

  return list
})

const chuongTrinhTheoNganh = computed(() =>
    !nganhDangChon.value ? [] :
        danhSachChuongTrinh.value.filter(ct => String(ct.nganhId) === String(nganhDangChon.value.id))
)

const versionTheoCT = computed(() =>
    !chuongTrinhDangChon.value ? [] :
        danhSachVersion.value.filter(v => String(v.chuongTrinhId) === String(chuongTrinhDangChon.value.id))
)

const chuongTrinhMonTheoVersion = computed(() =>
    !versionDangChon.value ? [] :
        danhSachChuongTrinhMon.value.filter(m => String(m.chuongTrinhVersionId) === String(versionDangChon.value.id))
)

const chuongTrinhMonTheoKhungKyDangChon = computed(() => {
  if (!versionDangChon.value || !formLHP.khungKyId) return []

  return danhSachChuongTrinhMon.value.filter(mon =>
      String(mon.chuongTrinhVersionId) === String(versionDangChon.value.id)
      && String(layKhungKyIdCuaChuongTrinhMon(mon)) === String(formLHP.khungKyId)
  )
})

const syllabusMonHocDangChon = computed(() => {
  if (!formLHP.chuongTrinhMonId) return null

  return danhSachSyllabusMonHoc.value.find(syllabus =>
      String(syllabus.chuongTrinhMonId) === String(formLHP.chuongTrinhMonId)
  ) || null
})

const soBuoiHocTuSyllabusDangChon = computed(() =>
    syllabusMonHocDangChon.value?.soBuoiHoc || ''
)

function laySoBuoiHocTuSyllabus(chuongTrinhMonId) {
  if (!chuongTrinhMonId) return null

  const syllabus = danhSachSyllabusMonHoc.value.find(item =>
      String(item.chuongTrinhMonId) === String(chuongTrinhMonId)
  )

  return syllabus?.soBuoiHoc || null
}


const khungKyTheoVersion = computed(() =>
    !versionDangChon.value ? [] :
        danhSachKhungKy.value.filter(k => String(k.chuongTrinhVersionId) === String(versionDangChon.value.id))
)

const danhSachLHPTheoVersion = computed(() => {
  if (!versionDangChon.value) return []

  const chuongTrinhMonIdsTrongVersion = new Set(
      danhSachChuongTrinhMon.value
          .filter(mon => String(mon.chuongTrinhVersionId) === String(versionDangChon.value.id))
          .map(mon => String(mon.id))
  )

  const lopHocPhanIdsHocChungTrongVersion = new Set(
      danhSachLHPChuongTrinhMon.value
          .filter(item => chuongTrinhMonIdsTrongVersion.has(String(item.chuongTrinhMonId)))
          .map(item => String(item.lopHocPhanId))
  )

  const kw = tuKhoaLHP.value.trim().toLowerCase()
  const filterKy = filterKhungKyId.value

  return danhSachLHP.value
      .filter(lhp =>
          chuongTrinhMonIdsTrongVersion.has(String(lhp.chuongTrinhMonId))
          || lopHocPhanIdsHocChungTrongVersion.has(String(lhp.id))
      )
      .filter(lhp => {
        if (!filterKy) return true
        const mon = layChuongTrinhMonCuaLHP(lhp)
        return String(layKhungKyIdCuaChuongTrinhMon(mon)) === filterKy
      })
      .filter(lhp => {
        if (!kw) return true

        return [
          layMaMonCuaLHP(lhp),
          layTenMonCuaLHP(lhp),
          lhp.maLopHocPhan,
          lhp.maLop,
          lhp.tenLopHocPhan,
          lhp.tenLop
        ].some(v => String(v || '').toLowerCase().includes(kw))
      })
      .sort((a, b) => {
        const kyA = layThuTuKyCuaLHP(a)
        const kyB = layThuTuKyCuaLHP(b)

        if (kyA !== kyB) return kyA - kyB

        const maMonA = layMaMonCuaLHP(a)
        const maMonB = layMaMonCuaLHP(b)
        const cmpMaMon = maMonA.localeCompare(maMonB, 'vi', {numeric: true})
        if (cmpMaMon !== 0) return cmpMaMon

        const maLopA = String(a.maLopHocPhan || a.maLop || '')
        const maLopB = String(b.maLopHocPhan || b.maLop || '')
        return maLopA.localeCompare(maLopB, 'vi', {numeric: true})
      })
})

const svHienThi = computed(() => {
  const kw = tuKhoaSV.value.trim().toLowerCase()
  if (!kw) return danhSachSVTrongLop.value
  return danhSachSVTrongLop.value.filter(sv =>
      [sv.maSinhVien, sv.hoTen, sv.email, sv.soDienThoai].some(v => String(v || '').toLowerCase().includes(kw))
  )
})

// ─── CẢNH BÁO THIẾU LỚP HỌC PHẦN ────────────────────────────────────────────
const danhSachMonCanCoLopHocPhan = computed(() => {
  if (!versionDangChon.value) return []
  const versionId = versionDangChon.value.id
  const filterKy = filterKhungKyId.value
  return danhSachChuongTrinhMon.value
      .filter(mon => {
        if (String(mon.chuongTrinhVersionId) !== String(versionId)) return false
        if (filterKy && String(layKhungKyIdCuaChuongTrinhMon(mon)) !== filterKy) return false
        return true
      })
      .sort((a, b) => {
        const kyA = layThuTuKyCuaMon(a)
        const kyB = layThuTuKyCuaMon(b)
        if (kyA !== kyB) return kyA - kyB
        const maA = String(a.maMonHoc || a.monHocMa || a.maMon || '')
        const maB = String(b.maMonHoc || b.monHocMa || b.maMon || '')
        return maA.localeCompare(maB, 'vi', { numeric: true })
      })
})

const chuongTrinhMonIdsDaCoLopHocPhan = computed(() => {
  if (!versionDangChon.value) return new Set()
  const versionId = versionDangChon.value.id
  const filterKy = filterKhungKyId.value

  const monIdsTrongScope = new Set(
      danhSachChuongTrinhMon.value
          .filter(mon => {
            if (String(mon.chuongTrinhVersionId) !== String(versionId)) return false
            if (filterKy && String(layKhungKyIdCuaChuongTrinhMon(mon)) !== filterKy) return false
            return true
          })
          .map(mon => String(mon.id))
  )

  const lhpKhongHuy = new Set(
      danhSachLHP.value
          .filter(lhp => lhp.trangThai !== 'huy')
          .map(lhp => String(lhp.id))
  )

  const result = new Set()

  // 1. LHP có chuongTrinhMonId trực tiếp
  for (const lhp of danhSachLHP.value) {
    if (lhp.trangThai === 'huy') continue
    const monId = String(lhp.chuongTrinhMonId || '')
    if (monId && monIdsTrongScope.has(monId)) result.add(monId)
  }

  // 2. LHP học chung qua bảng danhSachLHPChuongTrinhMon
  for (const item of danhSachLHPChuongTrinhMon.value) {
    const monId = String(item.chuongTrinhMonId || '')
    if (!monIdsTrongScope.has(monId)) continue
    if (!lhpKhongHuy.has(String(item.lopHocPhanId))) continue
    result.add(monId)
  }

  return result
})

const danhSachMonChuaCoLopHocPhan = computed(() =>
    danhSachMonCanCoLopHocPhan.value.filter(mon =>
        !chuongTrinhMonIdsDaCoLopHocPhan.value.has(String(mon.id))
    )
)

const thongKeCanhBaoLopHocPhan = computed(() => {
  const tongMon = danhSachMonCanCoLopHocPhan.value.length
  const soMonChuaCoLop = danhSachMonChuaCoLopHocPhan.value.length
  return {
    tongMon,
    soMonDaCoLop: tongMon - soMonChuaCoLop,
    soMonChuaCoLop,
    thieu: soMonChuaCoLop > 0
  }
})

const canhBaoLopHocPhanTheoKy = computed(() => {
  return khungKyTheoVersion.value
      .map((ky, index) => {
        const soThuTuKy = layThuTuTuKhungKy(ky, index)
        const khungKyId = ky?.id || null

        const danhSachMonThieu = danhSachMonChuaCoLopHocPhan.value.filter(mon =>
            String(layKhungKyIdCuaChuongTrinhMon(mon)) === String(khungKyId)
        )

        return {
          key: khungKyId ? `ky-${khungKyId}` : `hk-${soThuTuKy}`,
          khungKyId,
          soThuTuKy,
          tenKy: `${ky?.maKy || `HK${soThuTuKy}`} - ${ky?.tenKy || `Học kỳ ${soThuTuKy}`}`,
          soLoi: danhSachMonThieu.length,
          danhSachMonThieu
        }
      })
      .sort((a, b) => a.soThuTuKy - b.soThuTuKy)
})

const canhBaoHocKyDangChon = computed(() => {
  const danhSach = canhBaoLopHocPhanTheoKy.value
  if (!danhSach.length) return null

  const dangChon = danhSach.find(item => item.key === khungKyCanhBaoDangChon.value)
  if (dangChon) return dangChon

  return danhSach.find(item => item.soLoi > 0) || danhSach[0]
})

function chonCanhBaoHocKy(item) {
  if (!item) return

  khungKyCanhBaoDangChon.value = item.key

  if (item.khungKyId) {
    filterKhungKyId.value = String(item.khungKyId)
  }
}

function layThuTuTuKhungKy(ky, index = 0) {
  const raw = ky?.thuTu
      ?? ky?.soThuTu
      ?? ky?.hocKy
      ?? ky?.ky
      ?? ky?.maKy
      ?? ''

  const match = String(raw).match(/\d+/)
  return match ? Number(match[0]) : index + 1
}

// ─── MOUNTED ──────────────────────────────────────────────────────────────────
onMounted(async () => {
  await taiDuLieuNen()
  await khoiPhucTrangThaiFlow()
})

watch(
    [
      buoc,
      nganhDangChon,
      chuongTrinhDangChon,
      versionDangChon,
      lopHanhChinhDangChon,
    ],
    luuTrangThaiFlow,
    {deep: true}
)

async function taiDuLieuNen() {
  try {
    const [nganh, ct, ver, ky, mon] = await Promise.all([
      sinhVienService.layNganh(),
      sinhVienService.layChuongTrinh(),
      sinhVienService.layVersion(),
      sinhVienService.layKhungKy(),
      sinhVienService.layChuongTrinhMon()
    ])
    danhSachNganh.value = nganh
    danhSachChuongTrinh.value = ct
    danhSachVersion.value = ver
    danhSachKhungKy.value = ky
    danhSachChuongTrinhMon.value = mon
  } catch (e) {
    baoLoi('Không tải được dữ liệu: ' + e.message)
  }
}

function luuTrangThaiFlow() {
  const state = {
    buoc: buoc.value,
    nganhId: nganhDangChon.value?.id || null,
    chuongTrinhId: chuongTrinhDangChon.value?.id || null,
    versionId: versionDangChon.value?.id || null,
    lopHanhChinhId: lopHanhChinhDangChon.value?.id || null
  }

  localStorage.setItem(SINH_VIEN_FLOW_STORAGE_KEY, JSON.stringify(state))
}

async function khoiPhucTrangThaiFlow() {
  const raw = localStorage.getItem(SINH_VIEN_FLOW_STORAGE_KEY)
  if (!raw) return

  try {
    const state = JSON.parse(raw)

    nganhDangChon.value = danhSachNganh.value.find(x => String(x.id) === String(state.nganhId)) || null
    chuongTrinhDangChon.value = danhSachChuongTrinh.value.find(x => String(x.id) === String(state.chuongTrinhId)) || null
    versionDangChon.value = danhSachVersion.value.find(x => String(x.id) === String(state.versionId)) || null

    if (versionDangChon.value) {
      const versionId = versionDangChon.value.id

      // Tải LHC + SV — dữ liệu cốt lõi
      try {
        const [lhc, svCT] = await Promise.all([
          sinhVienService.layLopHanhChinhTheoVersion(versionId),
          sinhVienService.laySinhVienChuongTrinh({ chuongTrinhVersionId: versionId })
        ])
        danhSachLHC.value = lhc
        danhSachSVChuongTrinh.value = svCT
      } catch (e) {
        baoLoi('Không tải được lớp hành chính/sinh viên: ' + e.message)
      }

      // Tải LHP riêng — lỗi không làm mất LHC/SV
      try {
        const [lhp, lhpCTM] = await Promise.all([
          sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionId }),
          sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionId })
        ])
        danhSachLHP.value = lhp
        danhSachLHPChuongTrinhMon.value = lhpCTM
      } catch (e) {
        danhSachLHP.value = []
        danhSachLHPChuongTrinhMon.value = []
        baoLoi('Không tải được lớp học phần: ' + e.message)
      }

      // Syllabus + sĩ số
      try {
        const syllabus = await sinhVienService.laySyllabusMonHoc()
        danhSachSyllabusMonHoc.value = layDanhSachTuResponse(syllabus)
        await taiSiSoLopHocPhanTheoVersion()
      } catch (e) {
        console.warn('Không tải được syllabus/sĩ số:', e)
      }

      lopHanhChinhDangChon.value = danhSachLHC.value.find(x => String(x.id) === String(state.lopHanhChinhId)) || null
    }

    if (state.buoc && coTheDungOBuoc(state.buoc)) {
      buoc.value = state.buoc
    }

    if (buoc.value === 'tiepNhan') {
      await taiSinhVienTrongLop()
    }
  } catch (e) {
    localStorage.removeItem(SINH_VIEN_FLOW_STORAGE_KEY)
  }
}

function coTheDungOBuoc(step) {
  if (step === 'nganh') return true
  if (step === 'chuongTrinh') return !!nganhDangChon.value
  if (step === 'version') return !!chuongTrinhDangChon.value
  if (step === 'lopHanhChinh') return !!versionDangChon.value
  if (step === 'tiepNhan') return !!lopHanhChinhDangChon.value
  if (step === 'lopHocPhan') return !!lopHanhChinhDangChon.value
  return false
}


// ─── ĐIỀU HƯỚNG ───────────────────────────────────────────────────────────────
function quayVeBuoc(step) {
  if (step === 'nganh') {
    nganhDangChon.value = null;
    chuongTrinhDangChon.value = null;
    versionDangChon.value = null;
    lopHanhChinhDangChon.value = null;
  }
  if (step === 'chuongTrinh') {
    chuongTrinhDangChon.value = null;
    versionDangChon.value = null;
    lopHanhChinhDangChon.value = null;
  }
  if (step === 'version') {
    versionDangChon.value = null;
    lopHanhChinhDangChon.value = null;
  }
  if (step === 'lopHanhChinh') {
    lopHanhChinhDangChon.value = null;
  }
  buoc.value = step
  xoaThongBao()

  if (step === 'nganh') {
    localStorage.removeItem(SINH_VIEN_FLOW_STORAGE_KEY)
  }

  if (step !== 'tiepNhan') {
    taiKhoanMoi.value = null
  }
}

// ─── BƯỚC 1: NGÀNH ────────────────────────────────────────────────────────────
function chonNganh(ng) {
  nganhDangChon.value = ng;
  buoc.value = 'chuongTrinh';
  xoaThongBao()
}

async function luuNganh() {
  dangLuu.value = true
  try {
    const p = lamSach({maNganh: formNganh.maNganh, tenNganh: formNganh.tenNganh, ghiChu: formNganh.ghiChu})
    idNganhSua.value ? await sinhVienApi.nganh.update(idNganhSua.value, p) : await sinhVienApi.nganh.create(p)
    baoThanhCong(idNganhSua.value ? 'Đã cập nhật ngành' : 'Đã tạo ngành')
    resetFormNganh()
    danhSachNganh.value = await sinhVienService.layNganh()
  } catch (e) {
    baoLoi(e.message)
  } finally {
    dangLuu.value = false
  }
}

function suaNganh(ng) {
  idNganhSua.value = ng.id;
  Object.assign(formNganh, {maNganh: ng.maNganh, tenNganh: ng.tenNganh, ghiChu: ng.ghiChu || ''})
}

async function xoaNganh(ng) {
  if (!confirm(`Xóa ngành ${ng.tenNganh}?`)) return
  try {
    await sinhVienApi.nganh.delete(ng.id);
    danhSachNganh.value = await sinhVienService.layNganh();
    baoThanhCong('Đã xóa ngành')
  } catch (e) {
    baoLoi(e.message)
  }
}

function resetFormNganh() {
  idNganhSua.value = null;
  Object.assign(formNganh, {maNganh: '', tenNganh: '', ghiChu: ''})
}

// ─── BƯỚC 2: CHƯƠNG TRÌNH ─────────────────────────────────────────────────────
function chonChuongTrinh(ct) {
  chuongTrinhDangChon.value = ct;
  buoc.value = 'version';
  xoaThongBao()
}

async function luuChuongTrinh() {
  dangLuu.value = true
  try {
    const p = lamSach({
      maChuongTrinh: formCT.maChuongTrinh,
      tenChuongTrinh: formCT.tenChuongTrinh,
      ghiChu: formCT.ghiChu,
      nganhId: nganhDangChon.value.id
    })
    idCTSua.value ? await sinhVienApi.chuongTrinh.update(idCTSua.value, p) : await sinhVienApi.chuongTrinh.create(p)
    baoThanhCong(idCTSua.value ? 'Đã cập nhật chương trình' : 'Đã tạo chương trình')
    resetFormCT()
    danhSachChuongTrinh.value = await sinhVienService.layChuongTrinh()
  } catch (e) {
    baoLoi(e.message)
  } finally {
    dangLuu.value = false
  }
}

function suaChuongTrinh(ct) {
  idCTSua.value = ct.id;
  Object.assign(formCT, {maChuongTrinh: ct.maChuongTrinh, tenChuongTrinh: ct.tenChuongTrinh, ghiChu: ct.ghiChu || ''})
}

async function xoaChuongTrinh(ct) {
  if (!confirm(`Xóa chương trình ${ct.tenChuongTrinh}?`)) return
  try {
    await sinhVienApi.chuongTrinh.delete(ct.id);
    danhSachChuongTrinh.value = await sinhVienService.layChuongTrinh();
    baoThanhCong('Đã xóa')
  } catch (e) {
    baoLoi(e.message)
  }
}

function resetFormCT() {
  idCTSua.value = null;
  Object.assign(formCT, {maChuongTrinh: '', tenChuongTrinh: '', ghiChu: ''})
}


async function chonVersion(v) {
  versionDangChon.value = v
  buoc.value = 'lopHanhChinh'
  xoaThongBao()
  filterKhungKyId.value = ''
  khungKyCanhBaoDangChon.value = ''

  const versionId = v.id

  // Tải LHC + SV (dữ liệu cốt lõi — lỗi ở đây sẽ hiện cảnh báo nhưng không chặn bước tiếp)
  try {
    const [lhc, svCT] = await Promise.all([
      sinhVienService.layLopHanhChinhTheoVersion(versionId),
      sinhVienService.laySinhVienChuongTrinh({ chuongTrinhVersionId: versionId })
    ])
    danhSachLHC.value = lhc
    danhSachSVChuongTrinh.value = svCT
  } catch (e) {
    baoLoi('Không tải được lớp hành chính/sinh viên: ' + e.message)
  }

  // Tải LHP riêng — lỗi ở đây không làm mất dữ liệu LHC/SV bên trên
  try {
    const [lhp, lhpCTM] = await Promise.all([
      sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionId }),
      sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionId })
    ])
    danhSachLHP.value = lhp
    danhSachLHPChuongTrinhMon.value = lhpCTM
  } catch (e) {
    danhSachLHP.value = []
    danhSachLHPChuongTrinhMon.value = []
    baoLoi('Không tải được lớp học phần: ' + e.message)
  }

  // Syllabus + sĩ số — không chặn nếu lỗi
  try {
    const syllabus = await sinhVienService.laySyllabusMonHoc()
    danhSachSyllabusMonHoc.value = layDanhSachTuResponse(syllabus)
    await taiSiSoLopHocPhanTheoVersion()
  } catch (e) {
    console.warn('Không tải được syllabus/sĩ số:', e)
  }
}

// ─── BƯỚC 4: LỚP HÀNH CHÍNH ───────────────────────────────────────────────────
function chonLHCNhanhTiepNhan(lop) {
  lopHanhChinhDangChon.value = lop
  buoc.value = 'tiepNhan'
  xoaThongBao()

  if (taiKhoanMoi.value?.lopHanhChinhId !== lop.id) {
    taiKhoanMoi.value = null
  }

  taiSinhVienTrongLop()
}

async function chonLHCNhanhLHP(lop) {
  lopHanhChinhDangChon.value = lop
  buoc.value = 'lopHocPhan'
  xoaThongBao()

  try {
    const versionId = versionDangChon.value?.id
    const [lhp, lhpCTM] = await Promise.all([
      sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionId }),
      sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionId })
    ])

    danhSachLHP.value = lhp
    danhSachLHPChuongTrinhMon.value = lhpCTM

    const syllabus = await sinhVienService.laySyllabusMonHoc()
    danhSachSyllabusMonHoc.value = layDanhSachTuResponse(syllabus)

    await taiSiSoLopHocPhanTheoVersion()
  } catch (e) {
    baoLoi(e.message)
  }
}

async function luuLopHanhChinh() {
  dangLuu.value = true
  try {
    const p = lamSach({...formLHC, chuongTrinhVersionId: versionDangChon.value.id})
    idLHCSua.value ? await sinhVienService.capNhatLopHanhChinh(idLHCSua.value, p) : await sinhVienService.taoLopHanhChinh(p)
    baoThanhCong(idLHCSua.value ? 'Đã cập nhật lớp hành chính' : 'Đã tạo lớp hành chính')
    resetFormLHC()
    danhSachLHC.value = await sinhVienService.layLopHanhChinhTheoVersion(versionDangChon.value.id)
  } catch (e) {
    baoLoi(e.message)
  } finally {
    dangLuu.value = false
  }
}

function suaLopHanhChinh(lop) {
  idLHCSua.value = lop.id
  Object.assign(formLHC, {
    maLop: lop.maLop,
    tenLop: lop.tenLop,
    siSo: lop.siSo || 0,
    trangThai: lop.trangThai || 'du_kien',
    ghiChu: lop.ghiChu || '',
    ngayBatDauNhanSinhVien: lop.ngayBatDauNhanSinhVien || '',
    ngayKetThucNhanSinhVien: lop.ngayKetThucNhanSinhVien || '',
    daChotTuyenSinh: lop.daChotTuyenSinh || false,
    ngayChotTuyenSinh: lop.ngayChotTuyenSinh || null
  })
}

async function xoaLopHanhChinh(lop) {
  if (!confirm(`Xóa lớp ${lop.tenLop}?`)) return
  try {
    await sinhVienService.xoaLopHanhChinh(lop.id)
    danhSachLHC.value = await sinhVienService.layLopHanhChinhTheoVersion(versionDangChon.value.id)
    baoThanhCong('Đã xóa')
  } catch (e) {
    baoLoi(e.message)
  }
}

function resetFormLHC() {
  idLHCSua.value = null
  Object.assign(formLHC, {
    maLop: '', tenLop: '', siSo: 0, trangThai: 'du_kien', ghiChu: '',
    ngayBatDauNhanSinhVien: '', ngayKetThucNhanSinhVien: '',
    daChotTuyenSinh: false, ngayChotTuyenSinh: null
  })
}

async function chotTuyenSinhLHC(lop) {
  if (!confirm(`Chốt tuyển sinh lớp "${lop.tenLop}"?\nSau khi chốt, lớp này đủ điều kiện dùng cho Auto mở LHP chính thức.`)) return
  try {
    await sinhVienService.chotTuyenSinh(lop.id)
    danhSachLHC.value = await sinhVienService.layLopHanhChinhTheoVersion(versionDangChon.value.id)
    baoThanhCong('Đã chốt tuyển sinh: ' + lop.tenLop)
  } catch (e) {
    baoLoi(e.response?.data?.message || e.message)
  }
}

async function huyChoTuyenSinhLHC(lop) {
  if (!confirm(`Hủy chốt tuyển sinh lớp "${lop.tenLop}"?\nChỉ được hủy nếu lớp chưa phát sinh lớp học phần.`)) return
  try {
    await sinhVienService.huyChoTuyenSinhLopHanhChinh(lop.id)
    danhSachLHC.value = await sinhVienService.layLopHanhChinhTheoVersion(versionDangChon.value.id)
    baoThanhCong('Đã hủy chốt tuyển sinh: ' + lop.tenLop)
  } catch (e) {
    baoLoi(e.response?.data?.message || e.message)
  }
}

function validateAutoTao() {
  if (!versionDangChon.value) return 'Phải chọn version'
  if (!formAutoTao.khungKyId) return 'Phải chọn kỳ học'
  if (!formAutoTao.lopHanhChinhIds.length) return 'Phải chọn ít nhất 1 lớp hành chính'
  if (!formAutoTao.siSoToiThieu || formAutoTao.siSoToiThieu < 1) return 'Sĩ số tối thiểu phải > 0'
  if (!formAutoTao.siSoToiDa || formAutoTao.siSoToiDa < 1) return 'Sĩ số tối đa phải > 0'
  if (formAutoTao.siSoToiDa < formAutoTao.siSoToiThieu) return 'Sĩ số tối đa phải >= sĩ số tối thiểu'
  // ngayBatDau/ngayKetThuc đã bỏ: thời gian học thuộc lịch học
  return null
}

async function xuLyAutoTao(mode) {
  const loi = validateAutoTao()
  if (loi) { baoLoi(loi); return }
  if (mode === 'CHINH_THUC' && !confirm(
      'Bạn đang xác nhận lớp học phần CHÍNH THỨC.\n' +
      '- Nếu đã có lớp dự kiến an toàn (chưa có lịch/điểm): sẽ được chốt sang dang_mo.\n' +
      '- Lớp dư (không đủ SV) sẽ bị hủy.\n' +
      'Lớp hành chính chưa chốt tuyển sinh sẽ báo lỗi.\nTiếp tục?'
  )) return

  dangAutoTao.value = true
  ketQuaAutoTao.value = null
  try {
    const payload = {
      chuongTrinhVersionId: versionDangChon.value.id,
      khungKyId: formAutoTao.khungKyId,
      lopHanhChinhIds: formAutoTao.lopHanhChinhIds,
      siSoToiThieu: formAutoTao.siSoToiThieu,
      siSoToiDa: formAutoTao.siSoToiDa,
      tuDongPhanBoSinhVien: formAutoTao.tuDongPhanBoSinhVien,
      choPhepMoDuKien: true,
      mode,
      tienToMaLop: formAutoTao.tienToMaLop || 'LHP'
    }
    const data = await sinhVienService.autoTaoLopHocPhanTheoKy(payload)
    ketQuaAutoTao.value = data
    if (mode !== 'PREVIEW') {
      danhSachLHP.value = await sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionDangChon.value.id })
      danhSachLHPChuongTrinhMon.value = await sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionDangChon.value.id })
      baoThanhCong(mode === 'DU_KIEN' ? 'Auto xếp dự kiến hoàn tất!' : 'Auto xếp chính thức hoàn tất!')
    } else {
      baoThanhCong('Xem trước hoàn tất!')
    }
  } catch (e) {
    baoLoi(e.response?.data?.message || e.message)
  } finally {
    dangAutoTao.value = false
  }
}

const NHAN_TRANG_THAI_LHC = {
  du_kien: 'Dự kiến',
  dang_hoc: 'Đang học',
  tam_dung: 'Tạm dừng',
  da_tot_nghiep: 'Đã tốt nghiệp',
  huy: 'Hủy'
}

function nhanTrangThaiLHC(val) {
  return NHAN_TRANG_THAI_LHC[val] || val || '—'
}

function demSiSoHienTaiLHC(lop) {
  if (!lop?.id || !versionDangChon.value?.id) return 0

  const lopId = String(lop.id)
  const versionId = String(versionDangChon.value.id)

  return danhSachSVChuongTrinh.value.filter(item =>
      String(item.lopHanhChinhId) === lopId
      && String(item.chuongTrinhVersionId) === versionId
      && ['da_dang_ky', 'da_dong_tien', 'dang_hoc'].includes(String(item.trangThai || ''))
  ).length
}

function laySiSoToiDaLHC(lop) {
  return Number(lop?.siSo || lop?.siSoToiDa || 0)
}

// ─── BƯỚC 4.1: TIẾP NHẬN SV ───────────────────────────────────────────────────
async function taiSinhVienTrongLop() {
  try {
    const [svList, svCT] = await Promise.all([
      sinhVienService.laySinhVienTheoNganhChuongTrinhVersion(
          nganhDangChon.value?.id, chuongTrinhDangChon.value?.id, versionDangChon.value?.id
      ),
      sinhVienService.laySinhVienChuongTrinh()
    ])
    danhSachSVChuongTrinh.value = svCT
    const lhcId = String(lopHanhChinhDangChon.value?.id)
    const verId = String(versionDangChon.value?.id)
    const ids = new Set(
        svCT
            .filter(x => String(x.lopHanhChinhId) === lhcId && String(x.chuongTrinhVersionId) === verId)
            .map(x => String(x.sinhVienId))
    )
    danhSachSVTrongLop.value = svList.filter(sv => ids.has(String(sv.id)))
  } catch (e) {
    baoLoi(e.message)
  }
}

async function luuSinhVien() {
  if (!lopHanhChinhDangChon.value) {
    baoLoi('Phải chọn lớp hành chính')
    return
  }

  const loi = kiemTraFormSinhVien()
  if (loi) {
    baoLoi(loi)
    return
  }

  dangLuu.value = true
  taiKhoanMoi.value = null
  try {
    const payload = lamSach({
      ...formSV,
      nganhId: nganhDangChon.value.id,
      chuongTrinhId: chuongTrinhDangChon.value.id,
      chuongTrinhVersionId: versionDangChon.value.id,
      lopHanhChinhId: lopHanhChinhDangChon.value.id
    })
    if (idSVSua.value) {
      await sinhVienService.capNhatSinhVien(idSVSua.value, payload)
      baoThanhCong('Đã cập nhật hồ sơ sinh viên')
    } else {
      const res = await sinhVienService.tiepNhanSinhVienCoFile(payload, filesSV)
      const sv = res?.sinhVien || res?.data?.sinhVien
      if (sv) {
        taiKhoanMoi.value = {
          lopHanhChinhId: lopHanhChinhDangChon.value.id,
          sinhVienId: sv.id,
          maSinhVien: sv.maSinhVien,
          emailTaiKhoan: res.emailTaiKhoan || res?.data?.emailTaiKhoan,
          matKhauTam: res.matKhauTam || res?.data?.matKhauTam
        }
      }
      baoThanhCong('Đã tiếp nhận sinh viên thành công')
    }
    resetFormSV()
    await taiSinhVienTrongLop()
  } catch (e) {
    baoLoi(e.message)
  } finally {
    dangLuu.value = false
  }
}

function suaSinhVien(sv) {
  idSVSua.value = sv.id
  Object.assign(formSV, taoFormSVMacDinh(), sv)
  window.scrollTo({top: 0, behavior: 'smooth'})
}

async function xoaSinhVien(sv) {
  if (!confirm(`Xóa sinh viên ${sv.maSinhVien}?`)) return
  try {
    await sinhVienService.xoaSinhVien(sv.id);
    await taiSinhVienTrongLop();
    baoThanhCong('Đã xóa')
  } catch (e) {
    baoLoi(e.message)
  }
}

function resetFormSV() {
  idSVSua.value = null
  Object.assign(formSV, taoFormSVMacDinh())
  Object.assign(filesSV, {anhChanDung: null, cccdTruoc: null, cccdSau: null, bangCap: null, giayToKhac: []})
}

function chonTep(e, key) {
  filesSV[key] = e.target.files?.[0] || null
}

function chonTepKhac(e) {
  filesSV.giayToKhac = Array.from(e.target.files || [])
}
function boTep(key) {
  filesSV[key] = null
}

function boGiayToKhac(index) {
  filesSV.giayToKhac.splice(index, 1)
}
function urlTep(id) {
  return sinhVienService.layUrlXemTep(id)
}

function layIdAnhChanDung(sv) {
  return sv?.anhChanDungTepId
      || sv?.anhChanDungId
      || sv?.anhDaiDienTepId
      || sv?.tepAnhChanDungId
      || null
}

function anAnhLoi(event) {
  event.target.style.display = 'none'
}

function layKhungKyIdCuaChuongTrinhMon(mon) {
  return mon?.khungKyId
      || mon?.khungKy?.id
      || mon?.kyId
      || mon?.hocKyId
      || null
}

function layThuTuKyCuaMon(mon) {
  const khungKyId = layKhungKyIdCuaChuongTrinhMon(mon)
  const ky = danhSachKhungKy.value.find(k => String(k.id) === String(khungKyId))
  if (!ky) return 9999
  const raw = ky.thuTu ?? ky.soThuTu ?? ky.hocKy ?? ky.ky ?? ky.maKy ?? ''
  const match = String(raw).match(/\d+/)
  return match ? Number(match[0]) : 9999
}

function layTenKyCuaMon(mon) {
  const khungKyId = layKhungKyIdCuaChuongTrinhMon(mon)
  const ky = danhSachKhungKy.value.find(k => String(k.id) === String(khungKyId))
  return ky ? `${ky.maKy || ''} - ${ky.tenKy || ''}` : '—'
}

function layChuongTrinhMonIdCuaLHP(lhp) {
  if (lhp?.chuongTrinhMonId) {
    return lhp.chuongTrinhMonId
  }

  const gan = danhSachLHPChuongTrinhMon.value.find(item =>
      String(item.lopHocPhanId) === String(lhp?.id)
  )

  return gan?.chuongTrinhMonId || null
}

function layChuongTrinhMonCuaLHP(lhp) {
  const chuongTrinhMonId = layChuongTrinhMonIdCuaLHP(lhp)

  return danhSachChuongTrinhMon.value.find(mon =>
      String(mon.id) === String(chuongTrinhMonId)
  ) || null
}

function layTenMonCuaLHP(lhp) {
  const mon = layChuongTrinhMonCuaLHP(lhp)

  const tenTuLopHocPhan = lhp?.tenLopHocPhan || lhp?.tenLop || ''
  const matchTenLop = String(tenTuLopHocPhan).match(/Lớp học phần\s+.+?\s+-\s+(.+?)\s+-\s+TC/i)

  return mon?.tenMonHoc
      || mon?.monHocTen
      || mon?.tenMon
      || lhp?.tenMonHoc
      || lhp?.monHocTen
      || matchTenLop?.[1]
      || '—'
}

function layMaMonCuaLHP(lhp) {
  const mon = layChuongTrinhMonCuaLHP(lhp)

  return mon?.maMonHoc
      || mon?.monHocMa
      || mon?.maMon
      || mon?.maMonTrongCt
      || lhp?.maMonHoc
      || '—'
}

function layTenKyCuaLHP(lhp) {
  const mon = layChuongTrinhMonCuaLHP(lhp)
  const khungKyId = layKhungKyIdCuaChuongTrinhMon(mon)

  const ky = danhSachKhungKy.value.find(k =>
      String(k.id) === String(khungKyId)
  )

  return ky ? `${ky.maKy || ''} - ${ky.tenKy || ''}` : '—'
}

function layThuTuKyCuaLHP(lhp) {
  const mon = layChuongTrinhMonCuaLHP(lhp)
  const khungKyId = layKhungKyIdCuaChuongTrinhMon(mon)

  const ky = danhSachKhungKy.value.find(k =>
      String(k.id) === String(khungKyId)
  )

  if (!ky) return 9999

  const raw = ky.thuTu
      ?? ky.soThuTu
      ?? ky.hocKy
      ?? ky.ky
      ?? ky.maKy
      ?? ''

  const match = String(raw).match(/\d+/)
  return match ? Number(match[0]) : 9999
}

function laySiSoHienTaiLHP(lhp) {
  const demTuDangKy = siSoTheoLopHocPhan.value[String(lhp.id)]

  if (demTuDangKy !== undefined && demTuDangKy !== null) {
    return demTuDangKy
  }

  return lhp.soLuongHienTai || 0
}

function layDanhSachTuResponse(res) {
  if (Array.isArray(res)) return res
  if (Array.isArray(res?.content)) return res.content
  if (Array.isArray(res?.data)) return res.data
  if (Array.isArray(res?.data?.content)) return res.data.content
  return []
}

async function taiSiSoLopHocPhanTheoVersion() {
  const lopList = danhSachLHPTheoVersion.value || []
  const ketQua = {}

  await Promise.all(
      lopList.map(async (lhp) => {
        try {
          const dsDangKy = await sinhVienService.laySinhVienLopHocPhan({
            lopHocPhanId: lhp.id
          })

          ketQua[String(lhp.id)] = layDanhSachTuResponse(dsDangKy).length
        } catch (_) {
          ketQua[String(lhp.id)] = lhp.soLuongHienTai || 0
        }
      })
  )

  siSoTheoLopHocPhan.value = ketQua
}

function khiDoiKhungKyMoLop() {
  formLHP.chuongTrinhMonId = ''
}

// ─── BƯỚC 4.2: LỚP HỌC PHẦN ──────────────────────────────────────────────────
async function luuLopHocPhan() {
  if (!formLHP.khungKyId) {
    baoLoi('Phải chọn kỳ trước khi mở lớp học phần')
    return
  }

  if (!formLHP.chuongTrinhMonId) {
    baoLoi('Phải chọn môn trong kỳ trước khi mở lớp học phần')
    return
  }
  const soBuoiHocSyllabus = laySoBuoiHocTuSyllabus(formLHP.chuongTrinhMonId)

  if (!soBuoiHocSyllabus) {
    baoLoi('Môn này chưa có syllabus đã lưu vào version hoặc syllabus chưa có số buổi học')
    return
  }

  dangLuu.value = true
  try {
    const p = lamSach({
      chuongTrinhMonId: Number(formLHP.chuongTrinhMonId),
      loaiLopHocPhan: formLHP.loaiLopHocPhan,
      trangThai: formLHP.trangThai,
      maLop: formLHP.maLop,
      tenLop: formLHP.tenLop,
      siSoToiThieu: Number(formLHP.siSoToiThieu || 1),
      soLuongToiDa: Number(formLHP.soLuongToiDa || 40),
      soLuongHienTai: idLHPSua.value ? undefined : 0,
      soBuoiHoc: Number(soBuoiHocSyllabus)
    })
    idLHPSua.value ? await sinhVienService.capNhatLopHocPhan(idLHPSua.value, p) : await sinhVienService.taoLopHocPhan(p)
    baoThanhCong(idLHPSua.value ? 'Đã cập nhật lớp học phần' : 'Đã mở lớp học phần')
    resetFormLHP()
    const versionId = versionDangChon.value?.id
    danhSachLHP.value = await sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionId })
    danhSachLHPChuongTrinhMon.value = await sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionId })
    await taiSiSoLopHocPhanTheoVersion()
  } catch (e) {
    baoLoi(e.message)
    try {
      const versionId = versionDangChon.value?.id
      danhSachLHP.value = await sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionId })
      danhSachLHPChuongTrinhMon.value = await sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionId })
      await taiSiSoLopHocPhanTheoVersion()
    } catch (_) {
    }
  } finally {
    dangLuu.value = false
  }
}

function suaLopHocPhan(lhp) {
  idLHPSua.value = lhp.id

  const chuongTrinhMon = danhSachChuongTrinhMon.value.find(mon =>
      String(mon.id) === String(lhp.chuongTrinhMonId)
  )

  Object.assign(formLHP, taoFormLHPMacDinh(), {
    khungKyId: layKhungKyIdCuaChuongTrinhMon(chuongTrinhMon),
    chuongTrinhMonId: lhp.chuongTrinhMonId || '',
    loaiLopHocPhan: lhp.loaiLopHocPhan || 'CHUYEN_NGANH',
    trangThai: lhp.trangThai || 'du_kien',
    maLop: lhp.maLop || lhp.maLopHocPhan || '',
    tenLop: lhp.tenLop || lhp.tenLopHocPhan || '',
    siSoToiThieu: lhp.siSoToiThieu || 1,
    soLuongToiDa: lhp.soLuongToiDa || lhp.siSoToiDa || 40,
    soBuoiHoc: lhp.soBuoiHoc || laySoBuoiHocTuSyllabus(lhp.chuongTrinhMonId) || ''
  })
}

async function xoaLopHocPhan(lhp) {
  if (!confirm(`Xóa lớp ${lhp.maLopHocPhan || lhp.maLop}?`)) return
  try {
    await sinhVienService.xoaLopHocPhan(lhp.id)
    const versionId = versionDangChon.value?.id
    danhSachLHP.value = await sinhVienService.layLopHocPhan({ chuongTrinhVersionId: versionId })
    danhSachLHPChuongTrinhMon.value = await sinhVienService.layLopHocPhanChuongTrinhMon({ chuongTrinhVersionId: versionId })
    await taiSiSoLopHocPhanTheoVersion()
    if (danhSachMonChuaCoLopHocPhan.value.length > 0) {
      baoLoi(`Đã xóa lớp học phần. Hiện còn thiếu lớp học phần cho ${danhSachMonChuaCoLopHocPhan.value.length} môn trong phạm vi đang lọc.`)
    } else {
      baoThanhCong('Đã xóa. Danh sách lớp học phần vẫn đủ theo môn trong phạm vi đang lọc.')
    }
  } catch (e) {
    baoLoi(e.message)
  }
}

function resetFormLHP() {
  idLHPSua.value = null
  Object.assign(formLHP, taoFormLHPMacDinh())
}

function kiemTraFormSinhVien() {
  const truongBatBuoc = [
    ['hoTen', 'Họ tên sinh viên'],
    ['email', 'Gmail sinh viên'],
    ['soDienThoai', 'Số điện thoại sinh viên'],
    ['ngaySinh', 'Ngày sinh'],
    ['gioiTinh', 'Giới tính'],
    ['diaChi', 'Địa chỉ liên hệ'],
    ['diaChiThuongTru', 'Địa chỉ thường trú'],
    ['soCccd', 'Số CCCD'],
    ['ngayCapCccd', 'Ngày cấp CCCD'],
    ['noiCapCccd', 'Nơi cấp CCCD'],
    ['bangCap', 'Bằng cấp'],
    ['namTotNghiep', 'Năm tốt nghiệp'],
    ['truongTotNghiep', 'Trường tốt nghiệp'],
    ['hoTenCha', 'Họ tên cha'],
    ['sdtCha', 'Số điện thoại cha'],
    ['hoTenMe', 'Họ tên mẹ'],
    ['sdtMe', 'Số điện thoại mẹ'],
    ['hoTenNguoiThan', 'Họ tên người thân'],
    ['quanHeNguoiThan', 'Quan hệ người thân'],
    ['sdtNguoiThan', 'Số điện thoại người thân'],
    ['emailNguoiThan', 'Gmail người thân']
  ]

  for (const [key, label] of truongBatBuoc) {
    const value = formSV[key]
    if (value === null || value === undefined || String(value).trim() === '') {
      return `${label} không được để trống`
    }
  }

  if (!laEmailHopLe(formSV.email)) {
    return 'Gmail sinh viên không đúng định dạng'
  }

  if (formSV.emailCha && !laEmailHopLe(formSV.emailCha)) {
    return 'Gmail cha không đúng định dạng'
  }

  if (formSV.emailMe && !laEmailHopLe(formSV.emailMe)) {
    return 'Gmail mẹ không đúng định dạng'
  }

  if (!laEmailHopLe(formSV.emailNguoiThan)) {
    return 'Gmail người thân không đúng định dạng'
  }

  const homNay = new Date()
  homNay.setHours(0, 0, 0, 0)

  const ngaySinh = new Date(formSV.ngaySinh)
  ngaySinh.setHours(0, 0, 0, 0)
  if (ngaySinh >= homNay) {
    return 'Ngày sinh phải nhỏ hơn ngày hiện tại'
  }

  const ngayCapCccd = new Date(formSV.ngayCapCccd)
  ngayCapCccd.setHours(0, 0, 0, 0)
  if (ngayCapCccd > homNay) {
    return 'Ngày cấp CCCD không được lớn hơn ngày hiện tại'
  }

  const namHienTai = new Date().getFullYear()
  const namTotNghiep = Number(formSV.namTotNghiep)
  if (!Number.isInteger(namTotNghiep) || namTotNghiep < 1950 || namTotNghiep > namHienTai) {
    return `Năm tốt nghiệp phải từ 1950 đến ${namHienTai}`
  }

  const loiTep = kiemTraTepSinhVien()
  if (loiTep) {
    return loiTep
  }

  return ''
}

function laEmailHopLe(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(String(email || '').trim())
}

function kiemTraTepSinhVien() {
  const maxAnh = 10 * 1024 * 1024
  const maxTaiLieu = 25 * 1024 * 1024

  if (filesSV.anhChanDung) {
    if (!filesSV.anhChanDung.type.startsWith('image/')) {
      return 'Ảnh chân dung phải là file hình ảnh'
    }
    if (filesSV.anhChanDung.size > maxAnh) {
      return 'Ảnh chân dung vượt quá dung lượng cho phép 10MB'
    }
  }

  const danhSachTepTaiLieu = [
    [filesSV.cccdTruoc, 'CCCD mặt trước'],
    [filesSV.cccdSau, 'CCCD mặt sau'],
    [filesSV.bangCap, 'Bằng cấp']
  ]

  for (const [file, ten] of danhSachTepTaiLieu) {
    if (!file) continue

    const tenFile = String(file.name || '').toLowerCase()
    const laPdf = file.type === 'application/pdf' || tenFile.endsWith('.pdf')
    const laAnh = String(file.type || '').startsWith('image/')

    if (!laAnh && !laPdf) {
      return `${ten} phải là hình ảnh hoặc PDF`
    }

    if (file.size > maxTaiLieu) {
      return `${ten} vượt quá dung lượng cho phép 25MB`
    }
  }

  for (const file of filesSV.giayToKhac || []) {
    if (file.size > maxTaiLieu) {
      return `${file.name || 'Giấy tờ khác'} vượt quá dung lượng cho phép 25MB`
    }
  }

  return ''
}

function dinhDangNgay(value) {
  if (!value) return '—'

  const ngay = new Date(value)
  if (Number.isNaN(ngay.getTime())) return value

  const dd = String(ngay.getDate()).padStart(2, '0')
  const mm = String(ngay.getMonth() + 1).padStart(2, '0')
  const yyyy = ngay.getFullYear()

  return `${dd}/${mm}/${yyyy}`
}

// ─── HELPERS ──────────────────────────────────────────────────────────────────
function taoFormSVMacDinh() {
  return {
    hoTen: '', email: '', soDienThoai: '', ngaySinh: '', gioiTinh: '', ngayNhapHoc: '',
    diaChi: '', diaChiThuongTru: '', soCccd: '', ngayCapCccd: '', noiCapCccd: '',
    bangCap: '', namTotNghiep: null, truongTotNghiep: '',
    hoTenCha: '', ngheNghiepCha: '', sdtCha: '', emailCha: '',
    hoTenMe: '', ngheNghiepMe: '', sdtMe: '', emailMe: '',
    hoTenNguoiThan: '', quanHeNguoiThan: '', sdtNguoiThan: '', emailNguoiThan: '',
    ghiChuHoSo: ''
  }
}

function taoFormLHPMacDinh() {
  return {
    khungKyId: '',
    chuongTrinhMonId: '',
    loaiLopHocPhan: 'CHUYEN_NGANH',
    trangThai: 'du_kien',
    maLop: '',
    tenLop: '',
    siSoToiThieu: 1,
    soLuongToiDa: 40,
    soBuoiHoc: ''
  }
}

function layLoaiCuaLHP(lhp) {
  const mon = layChuongTrinhMonCuaLHP(lhp)

  const loaiPhamVi = mon?.loaiPhamVi || mon?.loai_pham_vi
  const loaiHocPhan = mon?.loaiHocPhan || mon?.loai_hoc_phan

  const mapPhamVi = {
    mon_chung: 'Môn chung',
    mon_co_so: 'Môn cơ sở',
    mon_chuyen_nganh: 'Môn chuyên ngành',
    mon_tu_chon_nang_cao: 'Tự chọn nâng cao'
  }

  const mapHocPhan = {
    mon_hoc: 'Môn học',
    mo_dun: 'Mô đun'
  }

  const tenPhamVi = mapPhamVi[loaiPhamVi] || ''
  const tenHocPhan = mapHocPhan[loaiHocPhan] || ''

  if (tenPhamVi && tenHocPhan) {
    return `${tenPhamVi} / ${tenHocPhan}`
  }

  return tenPhamVi
      || tenHocPhan
      || (lhp.loaiLopHocPhan === 'HOC_CHUNG' ? 'Học chung' : 'Chuyên ngành')
}
function layGhiChuNganh(nganh) {
  return nganh?.ghiChu
      || nganh?.ghi_chu
      || nganh?.moTa
      || nganh?.mo_ta
      || '—'
}

function layGhiChuChuongTrinh(chuongTrinh) {
  return chuongTrinh?.ghiChu
      || chuongTrinh?.ghi_chu
      || chuongTrinh?.moTa
      || chuongTrinh?.mo_ta
      || '—'
}
function lamSach(payload) {
  const r = {}
  Object.entries(payload).forEach(([k, v]) => {
    if (v === '' || v === undefined || v === null) return
    if (['id', 'createdAt', 'updatedAt'].includes(k)) return
    r[k] = v
  })
  return r
}
function layPreviewFile(file) {
  if (!file) return ''

  if (String(file.type || '').startsWith('image/')) {
    return URL.createObjectURL(file)
  }

  return ''
}

function layTenFile(file) {
  return file?.name || 'Chưa chọn tệp'
}

function layDungLuongFile(file) {
  if (!file?.size) return ''

  const kb = file.size / 1024
  if (kb < 1024) return `${kb.toFixed(0)} KB`

  return `${(kb / 1024).toFixed(2)} MB`
}

const previewAnhChanDung = computed(() => layPreviewFile(filesSV.anhChanDung))
const previewCccdTruoc = computed(() => layPreviewFile(filesSV.cccdTruoc))
const previewCccdSau = computed(() => layPreviewFile(filesSV.cccdSau))
const previewBangCap = computed(() => layPreviewFile(filesSV.bangCap))
function baoThanhCong(msg, step = buoc.value) {
  thongBao.value = msg
  thongBaoLoai.value = 'success'
  thongBaoBuoc.value = step
}

function baoLoi(msg, step = buoc.value) {
  thongBao.value = msg
  thongBaoLoai.value = 'error'
  thongBaoBuoc.value = step
}

function xoaThongBao() {
  thongBao.value = ''
  thongBaoLoai.value = 'success'
  thongBaoBuoc.value = ''
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800;900&display=swap');

/* =========================================================
   QUẢN LÝ SINH VIÊN - STYLE FULL FINAL
   - Form mở lớp học phần luôn 1 dòng.
   - Không cuộn ngang ở form mở lớp học phần.
   - Không cuộn ngang ở bảng danh sách lớp học phần.
   - Các ô nhập tự co gọn theo màn hình/zoom.
   - Ô số buổi học và nút thao tác nhỏ gọn.
   - Mobile bảng lớp học phần vẫn dạng thẻ.
   ========================================================= */

/* =========================
   1. BASE
   ========================= */

.sv-flow-page,
.sv-flow-page * {
  box-sizing: border-box;
  font-family: 'Roboto', Arial, sans-serif;
  letter-spacing: 0;
}

.sv-flow-page {
  --pn-green: #077149;
  --pn-green-dark: #045f3d;
  --pn-green-soft: #e8f6ef;
  --pn-green-line: #b7e1cc;
  --pn-text: #0f172a;
  --pn-muted: #64748b;
  --pn-border: #d8e2ee;
  --pn-bg: #f6f9fc;
  --pn-card: #ffffff;
  --pn-danger: #dc2626;
  --pn-warning: #b45309;
  --pn-blue: #1d4ed8;
  --pn-shadow: 0 8px 24px rgba(15, 23, 42, .055);

  display: block;
  width: 100%;
  min-width: 0;
  max-width: 100%;
  overflow-x: hidden;
  background: var(--pn-bg);
  color: var(--pn-text);
  font-size: 13px;
}

.sv-content {
  display: grid;
  gap: 10px;
  width: 100%;
  max-width: 100%;
  min-width: 0;
  padding: 0;
  overflow-x: hidden;
}

.sv-manage-layout,
.sv-manage-main {
  display: grid;
  grid-template-columns: 1fr;
  gap: 10px;
  width: 100%;
  max-width: 100%;
  min-width: 0;
  overflow-x: hidden;
}

/* =========================
   2. HEADER + BREADCRUMB
   ========================= */

.sv-page-header {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 12px;
  padding: 2px 0 0;
  margin-bottom: -2px;
  min-width: 0;
}

.sv-page-header h1 {
  margin: 0;
  color: var(--pn-text);
  font-size: 22px;
  line-height: 1.15;
  font-weight: 900;
}

.sv-page-header p {
  margin: 3px 0 0;
  color: var(--pn-muted);
  font-size: 12.5px;
  line-height: 1.35;
}

.sv-summary-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  align-items: center;
  padding: 8px 10px;
  border: 1px solid var(--pn-green-line);
  border-radius: 12px;
  background: rgba(255, 255, 255, .96);
  box-shadow: var(--pn-shadow);
  backdrop-filter: blur(10px);
}

.sv-breadcrumb-sticky {
  position: sticky;
  top: 55px;
  z-index: 20;
  margin-bottom: 14px;
}

.sv-lhp-auto-card {
  margin-top: 8px;
}

.sv-summary-chip {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  min-height: 28px;
  padding: 4px 10px;
  border: 1px solid #dbe7df;
  border-radius: 999px;
  background: #fff;
  color: var(--pn-text);
  cursor: default;
}

.sv-summary-chip.clickable {
  cursor: pointer;
}

.sv-summary-chip.clickable:hover,
.sv-summary-chip.active {
  border-color: var(--pn-green-line);
  background: var(--pn-green-soft);
}

.sv-summary-name {
  color: var(--pn-muted);
  font-size: 11px;
  font-weight: 800;
  text-transform: uppercase;
}

.sv-summary-value {
  color: var(--pn-text);
  font-size: 12px;
  font-weight: 800;
}

.sv-summary-chip.active .sv-summary-value {
  color: var(--pn-green);
}

/* =========================
   3. MESSAGE
   ========================= */

.sv-message {
  padding: 9px 12px;
  border-radius: 11px;
  font-size: 12.5px;
  font-weight: 700;
  line-height: 1.35;
}

.sv-message.success {
  border: 1px solid var(--pn-green-line);
  background: var(--pn-green-soft);
  color: var(--pn-green);
}

.sv-message.error {
  border: 1px solid #fecaca;
  background: #fef2f2;
  color: #b91c1c;
}

/* =========================
   4. FILTER + MINI STATS
   ========================= */

.sv-filter-card {
  padding: 10px 12px 9px;
  border: 1px solid #cfe1dc;
  border-radius: 14px;
  background: linear-gradient(180deg, #fbfefd 0%, #f6faf8 100%);
  box-shadow: 0 4px 14px rgba(15, 23, 42, .035);
  min-width: 0;
}

.sv-filter-grid {
  display: grid;
  grid-template-columns: minmax(0, 1.1fr) minmax(0, 1.1fr) minmax(0, 1.1fr);
  gap: 9px;
  align-items: end;
}

.sv-filter-field,
label {
  display: grid;
  gap: 4px;
  min-width: 0;
  color: #21314a;
  font-size: 12px;
  font-weight: 700;
}

.sv-filter-field > span {
  color: #21314a;
  font-size: 11.5px;
  font-weight: 800;
  line-height: 1.15;
}

.sv-filter-card select,
.sv-filter-card input {
  min-height: 32px;
  height: 32px;
  padding: 5px 9px;
  border-radius: 9px;
  font-size: 12.5px;
  font-weight: 700;
}

.sv-filter-bottom-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 10px;
  margin-top: 7px;
}

.sv-filter-hint {
  display: flex;
  align-items: center;
  gap: 7px;
  min-width: 0;
  margin: 0;
  color: var(--pn-green);
  font-size: 11.5px;
  line-height: 1.25;
}

.sv-filter-hint-ico {
  flex: 0 0 auto;
}

.sv-mini-stats {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
  min-width: 0;
  flex-wrap: wrap;
}

.sv-mini-stat {
  display: grid;
  grid-template-columns: 22px minmax(72px, auto) auto;
  align-items: center;
  gap: 5px;
  min-height: 28px;
  padding: 3px 7px 3px 4px;
  border: 1px solid #d9e8e3;
  border-radius: 999px;
  background: #fff;
  box-shadow: 0 1px 5px rgba(15, 23, 42, .035);
  white-space: nowrap;
}

.sv-mini-stat-icon {
  width: 22px;
  height: 22px;
  display: inline-grid;
  place-items: center;
  border-radius: 999px;
  font-size: 12px;
}

.stat-blue {
  background: #e8f6ef;
  color: var(--pn-green);
}

.stat-violet {
  background: #eef2ff;
  color: #4f46e5;
}

.stat-orange {
  background: #fff1dd;
  color: #c2410c;
}

.sv-mini-stat-text {
  color: #41556c;
  font-size: 11px;
  font-weight: 800;
}

.sv-mini-stat strong {
  color: #071827;
  font-size: 17px;
  line-height: 1;
  font-weight: 900;
}

/* =========================
   5. CARD + FORM CHUNG
   ========================= */

.sv-card {
  padding: 11px 13px;
  border: 1px solid var(--pn-border);
  border-radius: 14px;
  background: var(--pn-card);
  box-shadow: var(--pn-shadow);
  min-width: 0;
  max-width: 100%;
}

.sv-card-title {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 8px;
  min-width: 0;
}

.sv-card-title-toolbar {
  align-items: center;
}

.sv-card-title > div {
  min-width: 0;
}

.sv-card-title h2 {
  margin: 0;
  color: var(--pn-text);
  font-size: 15px;
  line-height: 1.25;
  font-weight: 900;
}

.sv-card-title p {
  margin: 3px 0 0;
  color: var(--pn-muted);
  font-size: 11.5px;
  line-height: 1.35;
}

.sv-title-tools {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  flex-wrap: nowrap;
  min-width: 0;
}

.sv-grid {
  display: grid;
  gap: 8px;
  margin: 0;
}

.sv-grid-2 {
  grid-template-columns: repeat(2, minmax(0, 1fr));
}

.sv-grid-3 {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.sv-grid-4 {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.sv-span-2 {
  grid-column: span 2;
}

.sv-span-3 {
  grid-column: span 3;
}

.sv-span-4 {
  grid-column: span 4;
}

#form-lhc.sv-grid,
form#form-lhc {
  display: grid;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1.25fr) minmax(0, .62fr) minmax(0, .9fr) minmax(0, 1fr) minmax(0, 1fr) minmax(0, 1.35fr);
  gap: 6px;
  align-items: end;
}

#form-lhc .sv-span-2,
#form-lhc .sv-span-4 {
  grid-column: auto;
}

#form-lhc label,
.sv-grid label {
  min-width: 0;
  gap: 3px;
  font-size: 11.5px;
}

#form-lhc .sv-chot-tuyen-sinh-info {
  min-height: 31px;
  display: flex;
  align-items: center;
  gap: 5px;
}

/* =========================
   6. INPUT / BUTTON
   ========================= */

input,
select,
textarea {
  width: 100%;
  min-width: 0;
  max-width: 100%;
  min-height: 31px;
  padding: 5px 8px;
  border: 1px solid #cbd7e4;
  border-radius: 8px;
  background: #fff;
  color: var(--pn-text);
  font: inherit;
  font-size: 12.5px;
  line-height: 1.25;
  outline: none;
  transition: border-color .15s, box-shadow .15s, background .15s;
}

textarea {
  min-height: 62px;
  resize: vertical;
}

input::placeholder,
textarea::placeholder {
  color: #8aa0b5;
}

input:focus,
select:focus,
textarea:focus {
  border-color: var(--pn-green);
  box-shadow: 0 0 0 3px rgba(7, 113, 73, .12);
}

select:disabled,
input:disabled {
  background: #f1f5f9;
  color: #94a3b8;
  cursor: not-allowed;
}

button,
.btn-giangday,
.btn-xem-sv {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 29px;
  padding: 5px 9px;
  border: 0;
  border-radius: 8px;
  background: var(--pn-blue);
  color: #fff;
  font-size: 11.5px;
  font-weight: 800;
  line-height: 1.2;
  text-decoration: none;
  white-space: nowrap;
  cursor: pointer;
  transition: filter .12s, transform .12s, box-shadow .12s;
}

button:hover,
.btn-giangday:hover,
.btn-xem-sv:hover {
  filter: brightness(.96);
  transform: translateY(-1px);
}

button:disabled {
  opacity: .6;
  cursor: not-allowed;
  transform: none;
}

button.secondary {
  background: #e2e8f0;
  color: #334155;
}

button.danger {
  background: var(--pn-danger);
  color: #fff;
}

button.small,
.btn-giangday.small,
.btn-xem-sv.small,
.sv-row-actions .small {
  min-height: 24px;
  padding: 3px 6px;
  border-radius: 7px;
  font-size: 10.8px;
  line-height: 1.1;
}

.sv-btn-primary {
  background: var(--pn-green);
  color: #fff;
}

.sv-btn-primary:hover {
  background: var(--pn-green-dark);
}

.btn-nhanh1 {
  background: var(--pn-blue);
}

.btn-nhanh2 {
  background: #059669;
}

.btn-giangday {
  background: #f59e0b;
}

.btn-xem-sv {
  background: #0f766e;
}

.btn-chot-ts {
  background: #16a34a;
}

.btn-huy-chot-ts {
  background: #f97316;
}

.sv-actions,
.sv-row-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 4px;
  flex-wrap: wrap;
}

.sv-row-actions-nowrap {
  flex-wrap: nowrap;
  justify-content: flex-start;
}

/* =========================
   7. TABLE CHUNG
   ========================= */

.sv-table-wrap {
  width: 100%;
  max-width: 100%;
  min-width: 0;
  overflow: auto;
  border: 1px solid var(--pn-border);
  border-radius: 12px;
  background: #fff;
}

.sv-table-wrap-full {
  border: 0;
  border-radius: 0;
}

.sv-table {
  width: 100%;
  min-width: 900px;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 11.5px;
  line-height: 1.18;
}

.sv-table th,
.sv-table td {
  height: 34px;
  padding: 4px 6px;
  border-bottom: 1px solid #e6edf4;
  text-align: left;
  vertical-align: middle;
  overflow: hidden;
}

.sv-table th {
  position: sticky;
  top: 0;
  z-index: 5;
  background: #edf3f0;
  color: #1f3347;
  font-size: 11px;
  font-weight: 800;
  white-space: nowrap;
  box-shadow: inset 0 -1px 0 #d7e2ec;
}

.sv-table tbody tr:hover td {
  background: #f8fbfa;
}

.sv-table tr.da-phan-bo td {
  color: #94a3b8;
}

.sv-table th:first-child,
.sv-table td:first-child {
  width: 44px;
  text-align: center;
}

.empty {
  padding: 18px 12px !important;
  color: var(--pn-muted);
  text-align: center;
}

/* =========================
   8. LỚP HÀNH CHÍNH
   ========================= */

.sv-lhc-list-card {
  width: 100%;
  max-width: none;
  padding: 0;
  overflow: hidden;
}

.sv-lhc-list-card .sv-card-title {
  margin: 0;
  padding: 9px 12px 8px;
  border-bottom: 1px solid var(--pn-border);
  background: linear-gradient(180deg, #fff 0%, #f8fbfa 100%);
}

.sv-lhc-table-wrap {
  max-height: calc(100vh - 238px);
  overflow: auto;
}

.sv-lhc-table {
  min-width: 1120px;
  table-layout: fixed;
}

.sv-lhc-table th:nth-child(1),
.sv-lhc-table td:nth-child(1) {
  width: 40px;
}

.sv-lhc-table th:nth-child(2),
.sv-lhc-table td:nth-child(2) {
  width: 86px;
}

.sv-lhc-table th:nth-child(3),
.sv-lhc-table td:nth-child(3) {
  width: 190px;
}

.sv-lhc-table th:nth-child(4),
.sv-lhc-table td:nth-child(4) {
  width: 74px;
  text-align: center;
}

.sv-lhc-table th:nth-child(5),
.sv-lhc-table td:nth-child(5) {
  width: 84px;
}

.sv-lhc-table th:nth-child(6),
.sv-lhc-table td:nth-child(6) {
  width: 110px;
}

.sv-lhc-table th:nth-child(7),
.sv-lhc-table td:nth-child(7) {
  width: 102px;
}

.sv-lhc-table th:nth-child(8),
.sv-lhc-table td:nth-child(8) {
  width: 220px;
}

.sv-lhc-table th:nth-child(9),
.sv-lhc-table td:nth-child(9) {
  width: 314px;
}

.sv-lhc-table td:nth-child(8) {
  white-space: nowrap;
}

.sv-lhc-table td:nth-child(8) .sv-note-text {
  display: block;
  width: 100%;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-table-footer {
  padding: 7px 10px;
  color: var(--pn-muted);
  font-size: 11.5px;
}

/* =========================
   9. SEARCH + BADGE + TEXT
   ========================= */

.sv-search-input {
  width: 220px;
  min-width: 180px;
  min-height: 31px;
  padding-left: 13px;
  border-radius: 999px;
  font-size: 12px;
}

.so-ban-ghi {
  color: #52677d;
  font-size: 12px;
  font-weight: 700;
  white-space: nowrap;
}

.sv-code {
  display: inline-flex;
  align-items: center;
  max-width: 100%;
  min-height: 21px;
  padding: 2px 7px;
  border-radius: 999px;
  background: #eff6ff;
  color: var(--pn-blue);
  font-size: 11.3px;
  font-weight: 800;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-main-text {
  max-width: 100%;
  color: var(--pn-text);
  font-size: 11.8px;
  font-weight: 800;
  line-height: 1.18;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-note-text {
  display: inline-block;
  max-width: 100%;
  color: var(--pn-muted);
  font-size: 11px;
  line-height: 1.18;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-capacity-badge,
.sv-status,
.sv-status-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 21px;
  padding: 2px 7px;
  border-radius: 999px;
  font-size: 10.8px;
  line-height: 1.15;
  font-weight: 800;
  white-space: nowrap;
}

.sv-capacity-badge {
  gap: 4px;
  min-width: 56px;
  background: #e8f6ef;
  color: var(--pn-green);
}

.sv-capacity-badge strong {
  font-size: 11.5px;
}

.sv-status-pill {
  background: #f1f5f9;
  color: #475569;
}

.sv-status-pill.status-dang_hoc,
.sv-status-pill.status-dang_mo,
.sv-status.done,
.sv-status-pill.status-da_chot {
  background: #e8f6ef;
  color: var(--pn-green);
  border: 1px solid var(--pn-green-line);
}

.sv-status-pill.status-du_kien {
  background: #eff6ff;
  color: var(--pn-blue);
}

.sv-status-pill.status-tam_dung {
  background: #fffbeb;
  color: var(--pn-warning);
}

.sv-status-pill.status-da_tot_nghiep {
  background: #f5f3ff;
  color: #6d28d9;
}

.sv-status-pill.status-da_ket_thuc {
  background: #f1f5f9;
  color: #334155;
}

.sv-status-pill.status-huy,
.sv-status.pending {
  background: #fef2f2;
  color: #b91c1c;
}

.sv-status-pill.status-chua_chot {
  background: #fff7ed;
  color: #c2410c;
  border: 1px solid #fed7aa;
}

/* =========================
   10. TIẾP NHẬN SINH VIÊN
   ========================= */

.sv-account-box {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
  padding: 10px 12px;
  border: 1px solid #fbbf24;
  border-radius: 12px;
  background: #fffbeb;
  color: #78350f;
  font-size: 12px;
}

.sv-receive-layout {
  display: grid;
  grid-template-columns: 300px minmax(0, 1fr);
  gap: 12px;
  align-items: start;
}

.sv-file-panel,
.sv-info-panel,
.sv-section-box {
  min-width: 0;
}

.sv-file-panel {
  position: sticky;
  top: 76px;
  display: grid;
  gap: 10px;
  padding: 12px;
  border: 1px solid var(--pn-border);
  border-radius: 12px;
  background: #f8fbfa;
}

.sv-file-panel-title h3,
.sv-section-box h3,
h3 {
  margin: 0 0 8px;
  color: #21314a;
  font-size: 13px;
  font-weight: 800;
}

.sv-file-panel-title p {
  margin: 2px 0 0;
  color: var(--pn-muted);
  font-size: 12px;
}

.sv-document-grid {
  display: grid;
  gap: 8px;
}

.sv-document-row {
  display: grid;
  grid-template-columns: 70px 1fr;
  gap: 8px;
  align-items: center;
  padding: 8px;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  background: #fff;
}

.sv-document-row-main {
  grid-template-columns: 88px 1fr;
}

.sv-document-left {
  position: relative;
  min-width: 0;
}

.sv-document-preview {
  width: 70px;
  height: 48px;
  display: grid;
  place-items: center;
  border: 1px dashed #cbd5e1;
  border-radius: 9px;
  overflow: hidden;
  background: #f8fafc;
  color: #94a3b8;
  font-size: 11px;
  text-align: center;
}

.sv-portrait-preview {
  width: 88px;
  height: 88px;
  border-radius: 12px;
}

.sv-document-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sv-document-title {
  color: var(--pn-text);
  font-size: 12px;
  font-weight: 800;
}

.sv-file-picker {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: fit-content;
  min-height: 28px;
  padding: 5px 9px;
  border-radius: 8px;
  background: var(--pn-green);
  color: #fff;
  font-size: 12px;
  font-weight: 800;
  cursor: pointer;
}

.sv-file-picker input {
  display: none;
}

.sv-file-meta {
  display: grid;
  gap: 1px;
  margin-top: 4px;
  color: var(--pn-muted);
  font-size: 11px;
}

.sv-file-meta strong {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sv-remove-file {
  position: absolute;
  top: -6px;
  right: -6px;
  min-height: 22px;
  padding: 2px 6px;
  border-radius: 999px;
  background: #ef4444;
  font-size: 10px;
  z-index: 2;
}

.sv-other-files {
  display: grid;
  gap: 5px;
  margin-top: 5px;
  color: var(--pn-muted);
  font-size: 11px;
}

.sv-other-file-item {
  display: flex;
  align-items: center;
  gap: 5px;
  justify-content: space-between;
  padding: 5px 6px;
  border-radius: 8px;
  background: #f1f5f9;
}

.sv-document-pdf {
  font-weight: 800;
  color: var(--pn-danger);
}

.sv-info-panel {
  display: grid;
  gap: 10px;
}

.sv-section-box {
  padding: 12px;
  border: 1px solid var(--pn-border);
  border-radius: 12px;
  background: #fff;
}

.sv-form-table {
  display: grid;
  gap: 8px;
}

.sv-form-table-3 {
  grid-template-columns: repeat(3, minmax(0, 1fr));
}

.sv-form-table-4 {
  grid-template-columns: repeat(4, minmax(0, 1fr));
}

.sv-col-span-3 {
  grid-column: span 3;
}

.sv-receive-actions {
  justify-content: flex-end;
  padding-top: 4px;
}

.sv-avatar {
  width: 34px;
  height: 34px;
  object-fit: cover;
  border: 1px solid var(--pn-border);
  border-radius: 8px;
}

/* =========================
   11. AUTO MỞ LỚP HỌC PHẦN
   Không cuộn ngang, tự co gọn.
   ========================= */

.sv-auto-tao-card {
  border-left: 4px solid var(--pn-green);
  overflow: visible;
}

.sv-auto-compact-card {
  padding: 8px 10px;
}

.sv-lhp-auto-title {
  margin-bottom: 7px;
}

.sv-lhp-auto-title h2,
.sv-lhp-open-title h2,
.sv-lhp-list-card h2 {
  font-size: 15px;
  line-height: 1.2;
}

.sv-lhp-auto-title p,
.sv-lhp-open-title p,
.sv-lhp-list-card p {
  margin-top: 2px;
  font-size: 11.5px;
  line-height: 1.3;
}

.sv-lhp-auto-actions {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 6px;
  flex-wrap: nowrap;
  min-width: 0;
}

.sv-lhp-auto-actions button {
  min-height: 28px;
  padding: 4px 9px;
  font-size: 10.8px;
}

.sv-btn-du-kien {
  background: #2563eb;
}

.sv-btn-chinh-thuc {
  background: var(--pn-green);
}

.sv-auto-tao-form,
.sv-lhp-auto-form {
  display: grid !important;
  grid-template-columns:
    minmax(0, 1.15fr)
    minmax(0, .72fr)
    minmax(0, .72fr)
    minmax(0, .9fr)
    minmax(0, 1.05fr)
    minmax(0, 1.32fr) !important;
  gap: 7px !important;
  align-items: end !important;
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  overflow: visible !important;
}

.sv-auto-field,
.sv-auto-check,
.sv-auto-lhc-dropdown {
  min-width: 0 !important;
  width: 100% !important;
  max-width: 100% !important;
}

.sv-lhp-auto-form label,
.sv-lhp-auto-form .sv-auto-check {
  gap: 3px;
  color: #1f3347;
  font-size: 10.6px;
  font-weight: 800;
}

.sv-lhp-auto-form input,
.sv-lhp-auto-form select {
  height: 31px;
  min-height: 31px;
  padding: 4px 8px;
  border-radius: 9px;
  font-size: 11.5px;
  font-weight: 700;
}

.sv-auto-check {
  display: flex !important;
  align-items: center !important;
  gap: 7px !important;
  height: 31px !important;
  min-height: 31px !important;
  padding: 4px 8px !important;
  border: 1px solid #cbd7e4 !important;
  border-radius: 9px !important;
  background: #fff !important;
  color: #1f3347 !important;
  font-size: 11.5px !important;
  font-weight: 800 !important;
  line-height: 1.2 !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

.sv-auto-check input,
.sv-check {
  width: 15px !important;
  height: 15px !important;
  min-width: 15px !important;
  min-height: 15px !important;
  flex: 0 0 auto !important;
  padding: 0 !important;
  accent-color: var(--pn-green);
}

.sv-auto-lhc-dropdown {
  position: relative;
  display: block;
}

.sv-auto-lhc-dropdown summary {
  display: flex !important;
  align-items: center !important;
  justify-content: space-between !important;
  gap: 8px !important;
  width: 100% !important;
  height: 31px !important;
  min-height: 31px !important;
  padding: 4px 9px !important;
  border: 1px solid var(--pn-green-line) !important;
  border-radius: 9px !important;
  background: #f7fffb !important;
  color: var(--pn-green) !important;
  font-size: 11px !important;
  font-weight: 900 !important;
  cursor: pointer;
  list-style: none;
}

.sv-auto-lhc-dropdown summary::-webkit-details-marker {
  display: none;
}

.sv-auto-lhc-summary-left,
.sv-auto-lhc-summary-count {
  min-width: 0;
  white-space: nowrap;
}

.sv-auto-lhc-summary-left {
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-auto-lhc-summary-count {
  flex: 0 0 auto;
  color: #475569;
  font-weight: 900;
}

.sv-lhc-checkbox-dropdown-list {
  position: absolute;
  top: calc(100% + 6px);
  right: 0;
  z-index: 80;
  width: min(440px, 92vw);
  max-height: 280px;
  padding: 8px;
  overflow: auto;
  border: 1px solid var(--pn-green-line);
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 14px 30px rgba(15, 23, 42, .14);
}

.sv-lhc-chon-item {
  display: grid !important;
  grid-template-columns: 18px minmax(0, 1fr) auto;
  align-items: center;
  gap: 7px;
  min-height: 34px;
  padding: 6px 8px;
  border-radius: 9px;
  font-size: 12px;
}

.sv-lhc-chon-item:hover {
  background: #f8fbfa;
}

.sv-lhc-name {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* =========================
   12. FORM MỞ LỚP HỌC PHẦN
   Luôn 1 dòng, không cuộn ngang.
   Các cột tự co theo container.
   ========================= */

.sv-lhp-open-card {
  padding: 8px 10px;
  overflow: visible !important;
}

.sv-lhp-open-card .sv-card-title {
  margin-bottom: 6px;
}

.sv-lhp-open-title {
  min-width: 0 !important;
  max-width: 100% !important;
}

.sv-lhp-open-form {
  display: grid !important;
  grid-template-columns:
    minmax(0, .78fr)
    minmax(0, 1.28fr)
    minmax(0, .78fr)
    minmax(0, .72fr)
    minmax(0, .86fr)
    minmax(0, 1.12fr)
    minmax(0, .62fr)
    minmax(0, .62fr)
    minmax(0, .46fr)
    minmax(0, .78fr) !important;
  gap: 6px !important;
  align-items: end !important;
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  overflow: visible !important;
  white-space: nowrap !important;
}

.sv-lhp-open-form label {
  min-width: 0 !important;
  max-width: 100% !important;
  gap: 2px !important;
  color: #1f3347 !important;
  font-size: clamp(7px, .62vw, 10.2px) !important;
  font-weight: 800 !important;
  line-height: 1.08 !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

.sv-lhp-open-form input,
.sv-lhp-open-form select {
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  height: clamp(22px, 2.05vw, 29px) !important;
  min-height: clamp(22px, 2.05vw, 29px) !important;
  padding: 2px clamp(3px, .45vw, 7px) !important;
  border-radius: 7px !important;
  font-size: clamp(7.5px, .7vw, 11px) !important;
  font-weight: 700 !important;
}

.sv-lhp-field-ky,
.sv-lhp-field-mon,
.sv-lhp-field-loai,
.sv-lhp-field-trangthai,
.sv-lhp-field-ma,
.sv-lhp-field-ten,
.sv-lhp-field-number,
.sv-lhp-field-buoi {
  min-width: 0 !important;
  max-width: 100% !important;
  grid-column: auto !important;
}

.sv-lhp-field-buoi input {
  text-align: center !important;
  padding-left: 2px !important;
  padding-right: 2px !important;
  color: #64748b !important;
  font-size: clamp(7px, .62vw, 10px) !important;
}

.sv-lhp-field-buoi .sv-field-hint,
.sv-field-hint {
  display: none !important;
}

.sv-lhp-open-actions {
  display: grid !important;
  grid-template-columns: minmax(0, 1fr) minmax(0, 1fr) !important;
  align-items: end !important;
  gap: 4px !important;
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  justify-content: stretch !important;
  grid-column: auto !important;
}

.sv-lhp-open-actions button {
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  height: clamp(22px, 2.05vw, 29px) !important;
  min-height: clamp(22px, 2.05vw, 29px) !important;
  padding: 2px 3px !important;
  border-radius: 7px !important;
  font-size: clamp(6.8px, .58vw, 10px) !important;
  font-weight: 900 !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
}

/* =========================
   13. CẢNH BÁO LỚP HỌC PHẦN
   ========================= */

.sv-canh-bao-lhp-wrap {
  width: 100%;
  max-width: 100%;
  padding: 9px 12px 5px;
}

.sv-canh-bao-lhp-panel {
  display: grid;
  gap: 8px;
  max-width: 100%;
  padding: 10px 12px;
  border: 1px solid #f59e0b;
  border-left: 5px solid #f97316;
  border-radius: 12px;
  background: linear-gradient(180deg, #fff7ed 0%, #fffbeb 100%);
  color: #7c2d12;
  box-shadow: 0 8px 18px rgba(245, 158, 11, .12);
}

.sv-canh-bao-lhp-panel.is-ok {
  border-color: #86efac;
  border-left-color: #16a34a;
  background: linear-gradient(180deg, #f0fdf4 0%, #ecfdf5 100%);
  color: #14532d;
}

.sv-canh-bao-lhp-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  min-width: 0;
}

.sv-canh-bao-lhp-title {
  display: flex;
  align-items: center;
  gap: 9px;
  min-width: 0;
}

.sv-canh-bao-lhp-icon {
  flex: 0 0 auto;
  display: inline-grid;
  place-items: center;
  width: 32px;
  height: 32px;
  border-radius: 999px;
  background: #fed7aa;
  color: #c2410c;
  font-size: 16px;
  box-shadow: inset 0 0 0 1px #fdba74;
}

.sv-canh-bao-lhp-title strong {
  display: block;
  color: #7c2d12;
  font-size: 13px;
  font-weight: 900;
  line-height: 1.2;
}

.sv-canh-bao-lhp-panel.is-ok .sv-canh-bao-lhp-title strong {
  color: #14532d;
}

.sv-canh-bao-lhp-title p {
  margin: 2px 0 0;
  color: #9a3412;
  font-size: 11.5px;
  font-weight: 700;
  line-height: 1.25;
}

.sv-canh-bao-lhp-panel.is-ok .sv-canh-bao-lhp-title p {
  color: #166534;
}

.sv-canh-bao-lhp-total {
  flex: 0 0 auto;
  display: grid;
  grid-template-columns: auto auto;
  align-items: center;
  gap: 6px;
  min-height: 28px;
  padding: 4px 9px;
  border-radius: 999px;
  background: #fff;
  border: 1px solid #fecaca;
  color: #991b1b;
  box-shadow: 0 2px 8px rgba(220, 38, 38, .12);
}

.sv-canh-bao-lhp-total.is-zero {
  border-color: #bbf7d0;
  color: #166534;
  box-shadow: none;
}

.sv-canh-bao-lhp-total span {
  font-size: 10.5px;
  font-weight: 900;
  text-transform: uppercase;
}

.sv-canh-bao-lhp-total strong {
  display: inline-grid;
  place-items: center;
  min-width: 21px;
  height: 21px;
  padding: 0 6px;
  border-radius: 999px;
  background: #dc2626;
  color: #fff;
  font-size: 12px;
  font-weight: 900;
  line-height: 1;
}

.sv-canh-bao-lhp-total.is-zero strong {
  background: #16a34a;
}

.sv-hk-warning-buttons {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
  min-width: 0;
}

.sv-hk-warning-btn {
  position: relative;
  display: grid;
  grid-template-columns: 24px minmax(0, 1fr) auto;
  align-items: center;
  gap: 7px;
  width: 100%;
  min-width: 0;
  min-height: 38px;
  padding: 6px 9px;
  border: 1px solid #fed7aa;
  border-radius: 11px;
  background: #fff;
  color: #9a3412;
  font-size: 12px;
  font-weight: 900;
  text-align: left;
  box-shadow: 0 2px 8px rgba(15, 23, 42, .045);
}

.sv-hk-warning-btn.has-error {
  border-color: #fb923c;
  background: #fff7ed;
  color: #9a3412;
}

.sv-hk-warning-btn.active,
.sv-hk-warning-btn.has-error.active {
  border-color: #dc2626;
  background: linear-gradient(180deg, #fff1f2 0%, #fff7ed 100%);
  box-shadow: 0 0 0 3px rgba(220, 38, 38, .12);
}

.sv-hk-warning-btn.is-ok {
  border-color: #bbf7d0;
  background: #f8fff9;
  color: #166534;
}

.sv-hk-warning-bell {
  display: inline-grid;
  place-items: center;
  width: 24px;
  height: 24px;
  border-radius: 999px;
  background: #ffedd5;
  font-size: 13px;
}

.sv-hk-warning-btn.is-ok .sv-hk-warning-bell {
  background: #dcfce7;
}

.sv-hk-warning-text {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sv-hk-warning-badge {
  display: inline-grid;
  place-items: center;
  min-width: 24px;
  height: 24px;
  padding: 0 7px;
  border-radius: 999px;
  background: #dc2626;
  color: #fff;
  font-size: 11.5px;
  font-weight: 900;
  line-height: 1;
  box-shadow: 0 2px 7px rgba(220, 38, 38, .24);
}

.sv-hk-warning-btn.is-ok .sv-hk-warning-badge {
  background: #16a34a;
  box-shadow: none;
}

.sv-hk-warning-detail {
  padding: 8px 10px;
  border: 1px dashed #fdba74;
  border-radius: 10px;
  background: rgba(255, 255, 255, .76);
}

.sv-hk-warning-detail-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  margin-bottom: 5px;
  color: #7c2d12;
  font-size: 11.8px;
  font-weight: 800;
}

.sv-hk-warning-detail-title span {
  color: #b91c1c;
  font-size: 11px;
  font-weight: 900;
}

.sv-hk-warning-detail ul {
  display: grid;
  gap: 4px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.sv-hk-warning-detail li {
  display: grid;
  grid-template-columns: minmax(92px, auto) minmax(0, 1fr);
  align-items: center;
  gap: 7px;
  min-width: 0;
  font-size: 11.5px;
  line-height: 1.2;
}

.sv-hk-warning-mon-code {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 20px;
  padding: 2px 6px;
  border-radius: 999px;
  background: #fee2e2;
  color: #b91c1c;
  font-size: 10.8px;
  font-weight: 900;
  white-space: nowrap;
}

.sv-hk-warning-mon-name {
  min-width: 0;
  color: #7c2d12;
  font-weight: 800;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-hk-warning-more,
.sv-hk-warning-empty {
  color: #92400e;
  font-size: 11.5px;
  font-weight: 900;
}

.sv-hk-warning-empty {
  color: #166534;
}

/* =========================
   14. DANH SÁCH LỚP HỌC PHẦN
   Không dùng thanh cuộn ngang.
   ========================= */

.sv-lhp-list-card {
  width: 100%;
  max-width: none;
  min-width: 0;
  padding: 0;
  overflow: hidden;
  border-radius: 14px;
}

.sv-lhp-list-card .sv-card-title {
  margin: 0;
  padding: 11px 13px 9px;
  border-bottom: 1px solid var(--pn-border);
  background: linear-gradient(180deg, #fff 0%, #f8fbfa 100%);
}

.sv-lhp-list-card .sv-title-tools {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
  flex-wrap: wrap;
  min-width: 0;
}

.sv-lhp-list-card .sv-search-input {
  width: 230px;
  min-width: 190px;
  min-height: 32px;
  font-size: 12px;
}

.sv-lhp-table-wrap {
  width: 100% !important;
  max-width: 100% !important;
  min-width: 0 !important;
  max-height: calc(100vh - 315px);
  overflow-x: hidden !important;
  overflow-y: auto !important;
  border: 0 !important;
  border-radius: 0 0 14px 14px;
  background: #fff;
  -webkit-overflow-scrolling: touch;
}

.sv-lhp-table {
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  table-layout: fixed !important;
  border-collapse: separate;
  border-spacing: 0;
  font-size: clamp(8px, .72vw, 10.8px);
  line-height: 1.22;
}

.sv-lhp-table th,
.sv-lhp-table td {
  height: 35px;
  min-width: 0 !important;
  padding: 4px 5px;
  border-bottom: 1px solid #e6edf4;
  text-align: left;
  vertical-align: middle;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}

.sv-lhp-table th {
  position: sticky;
  top: 0;
  z-index: 5;
  background: #edf3f0;
  color: #1f3347;
  font-size: clamp(7px, .68vw, 9.8px);
  font-weight: 900;
  line-height: 1.15;
}

.sv-lhp-table td {
  color: var(--pn-text);
  font-size: clamp(7.4px, .7vw, 10.5px);
}

.sv-lhp-table th:nth-child(1),
.sv-lhp-table td:nth-child(1) {
  width: 3.2% !important;
  text-align: center !important;
}

.sv-lhp-table th:nth-child(2),
.sv-lhp-table td:nth-child(2) {
  width: 8.5% !important;
}

.sv-lhp-table th:nth-child(3),
.sv-lhp-table td:nth-child(3) {
  width: 6% !important;
}

.sv-lhp-table th:nth-child(4),
.sv-lhp-table td:nth-child(4) {
  width: 12% !important;
}

.sv-lhp-table th:nth-child(5),
.sv-lhp-table td:nth-child(5) {
  width: 9% !important;
}

.sv-lhp-table th:nth-child(6),
.sv-lhp-table td:nth-child(6) {
  width: 12% !important;
}

.sv-lhp-table th:nth-child(7),
.sv-lhp-table td:nth-child(7) {
  width: 11.5% !important;
}

.sv-lhp-table th:nth-child(8),
.sv-lhp-table td:nth-child(8) {
  width: 7% !important;
  text-align: center !important;
}

.sv-lhp-table th:nth-child(9),
.sv-lhp-table td:nth-child(9) {
  width: 5.3% !important;
  text-align: center !important;
}

.sv-lhp-table th:nth-child(10),
.sv-lhp-table td:nth-child(10) {
  width: 7.5% !important;
  text-align: center !important;
}

.sv-lhp-table th:nth-child(11),
.sv-lhp-table td:nth-child(11) {
  width: 18% !important;
}

.sv-lhp-table .sv-code,
.sv-lhp-table .sv-main-text,
.sv-lhp-table .sv-note-text,
.sv-lhp-table .sv-status-pill {
  max-width: 100% !important;
  min-width: 0 !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  white-space: nowrap !important;
}

.sv-lhp-table .sv-code {
  padding: 1px 5px;
  font-size: clamp(7px, .62vw, 9.6px);
}

.sv-lhp-table .sv-status-pill {
  min-height: 20px !important;
  padding: 2px 6px !important;
  font-size: clamp(7px, .62vw, 9.8px) !important;
}

.sv-lhp-table td:nth-child(11) .sv-row-actions {
  display: grid !important;
  grid-template-columns: 1fr 1fr 1fr .72fr .72fr !important;
  align-items: center !important;
  justify-content: stretch !important;
  gap: 3px !important;
  width: 100% !important;
  max-width: 100% !important;
  min-width: 0 !important;
  flex-wrap: nowrap !important;
}

.sv-lhp-table td:nth-child(11) .small,
.sv-lhp-table td:nth-child(11) button.small,
.sv-lhp-table td:nth-child(11) a.small,
.sv-lhp-table td:nth-child(11) .btn-xem-sv.small,
.sv-lhp-table td:nth-child(11) .btn-giangday.small {
  width: 100% !important;
  min-width: 0 !important;
  max-width: 100% !important;
  height: 23px !important;
  min-height: 23px !important;
  padding: 0 2px !important;
  border-radius: 6px !important;
  font-size: clamp(6px, .52vw, 8.4px) !important;
  font-weight: 800 !important;
  line-height: 1 !important;
  white-space: nowrap !important;
  overflow: hidden !important;
  text-overflow: ellipsis !important;
  transform: none !important;
  box-shadow: none !important;
}

/* =========================
   15. KẾT QUẢ TỰ ĐỘNG TẠO
   ========================= */

.sv-auto-tao-result {
  display: grid;
  gap: 8px;
  margin-top: 10px;
}

.sv-auto-tao-summary {
  display: flex;
  align-items: center;
  gap: 7px;
  flex-wrap: wrap;
  padding: 8px 10px;
  border: 1px solid #dbe7df;
  border-radius: 11px;
  background: #f8fbfa;
  font-size: 12px;
}

.sv-auto-tao-summary span {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 8px;
  border-radius: 999px;
  background: #fff;
  color: #475569;
  white-space: nowrap;
}

.sv-auto-tao-summary strong {
  color: var(--pn-green);
}

.sv-auto-tao-warnings,
.sv-auto-tao-errors {
  padding: 8px 10px;
  border-radius: 11px;
  font-size: 12px;
  line-height: 1.4;
}

.sv-auto-tao-warnings {
  border: 1px solid #fde68a;
  background: #fffbeb;
  color: #92400e;
}

.sv-auto-tao-errors {
  border: 1px solid #fecaca;
  background: #fef2f2;
  color: #991b1b;
}

.sv-auto-result-table-wrap {
  max-height: 360px;
}

.sv-auto-result-table-wrap .sv-table {
  min-width: 760px;
}

/* =========================
   16. PHÂN BỔ SINH VIÊN
   ========================= */

.sv-check-all,
.sv-check-row {
  cursor: pointer;
}

/* =========================
   17. RESPONSIVE TABLET
   Không làm form mở lớp học phần xuống dòng.
   Không ép bảng lớp học phần width max-content.
   ========================= */

@media (max-width: 1180px) {
  .sv-page-header {
    display: grid;
    grid-template-columns: 1fr;
    gap: 4px;
  }

  .sv-filter-grid {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .sv-filter-bottom-row {
    grid-template-columns: 1fr;
  }

  .sv-mini-stats {
    justify-content: flex-start;
  }

  .sv-lhp-auto-title,
  .sv-lhp-open-title,
  .sv-lhp-list-card .sv-card-title {
    display: grid;
    grid-template-columns: 1fr;
    align-items: start;
  }

  .sv-lhp-auto-actions {
    justify-content: flex-start;
  }

  .sv-lhp-auto-form {
    grid-template-columns:
      minmax(0, 1.08fr)
      minmax(0, .62fr)
      minmax(0, .62fr)
      minmax(0, .78fr)
      minmax(0, .92fr)
      minmax(0, 1.15fr) !important;
    gap: 6px !important;
  }

  .sv-lhp-open-form {
    grid-template-columns:
      minmax(0, .72fr)
      minmax(0, 1.18fr)
      minmax(0, .72fr)
      minmax(0, .66fr)
      minmax(0, .8fr)
      minmax(0, 1.02fr)
      minmax(0, .56fr)
      minmax(0, .56fr)
      minmax(0, .4fr)
      minmax(0, .72fr) !important;
    gap: 5px !important;
  }

  .sv-lhp-open-form label {
    font-size: clamp(6.4px, .6vw, 9.6px) !important;
  }

  .sv-lhp-open-form input,
  .sv-lhp-open-form select,
  .sv-lhp-open-actions button {
    height: clamp(21px, 2vw, 28px) !important;
    min-height: clamp(21px, 2vw, 28px) !important;
    font-size: clamp(6.8px, .65vw, 10.2px) !important;
  }

  .sv-lhp-list-card .sv-title-tools {
    justify-content: flex-start;
  }

  .sv-lhp-table-wrap {
    max-height: calc(100vh - 290px);
    overflow-x: hidden !important;
    overflow-y: auto !important;
  }

  .sv-lhp-table {
    width: 100% !important;
    min-width: 0 !important;
    max-width: 100% !important;
    table-layout: fixed !important;
  }

  .sv-lhp-table th,
  .sv-lhp-table td {
    padding: 3px 4px !important;
    font-size: clamp(6.5px, .68vw, 9px) !important;
  }

  .sv-lhp-table .sv-status-pill {
    padding: 1px 4px !important;
    font-size: clamp(6px, .55vw, 8px) !important;
  }

  .sv-lhp-table td:nth-child(11) .small,
  .sv-lhp-table td:nth-child(11) button.small,
  .sv-lhp-table td:nth-child(11) a.small {
    height: 21px !important;
    min-height: 21px !important;
    font-size: clamp(5.4px, .5vw, 7.2px) !important;
  }
}

/* =========================
   18. RESPONSIVE MOBILE LỚN
   Form mở lớp học phần vẫn 1 dòng, không cuộn ngang.
   ========================= */

@media (max-width: 900px) {
  .sv-flow-page {
    font-size: 12px;
  }

  .sv-content {
    gap: 8px;
  }

  .sv-page-header h1 {
    font-size: 19px;
  }

  .sv-page-header p {
    font-size: 11.5px;
  }

  .sv-summary-bar {
    padding: 7px 8px;
    gap: 5px;
  }

  .sv-summary-chip {
    max-width: 100%;
  }

  .sv-summary-value {
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .sv-filter-grid {
    grid-template-columns: 1fr;
  }

  .sv-card {
    padding: 10px;
    border-radius: 12px;
  }

  #form-lhc.sv-grid,
  form#form-lhc {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  #form-lhc .sv-span-4 {
    grid-column: span 2;
  }

  .sv-title-tools {
    flex-wrap: wrap;
    justify-content: flex-start;
  }

  .sv-search-input {
    width: 100%;
    min-width: 0;
  }

  .sv-receive-layout {
    grid-template-columns: 1fr;
  }

  .sv-file-panel {
    position: static;
  }

  .sv-form-table-3,
  .sv-form-table-4 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .sv-col-span-3 {
    grid-column: span 2;
  }

  .sv-lhp-auto-title,
  .sv-lhp-open-title,
  .sv-lhp-list-card .sv-card-title {
    gap: 8px;
  }

  .sv-lhp-auto-actions {
    display: grid;
    grid-template-columns: repeat(3, minmax(0, 1fr));
    width: 100%;
    gap: 6px;
  }

  .sv-lhp-auto-actions button {
    width: 100%;
    min-width: 0;
    padding: 5px 4px;
    font-size: 10.5px;
  }

  .sv-lhp-loading {
    grid-column: span 3;
  }

  .sv-lhc-checkbox-dropdown-list {
    left: 0;
    right: auto;
    width: min(100%, 92vw);
  }

  .sv-lhp-open-card {
    padding: 8px;
  }

  .sv-lhp-open-form {
    grid-template-columns:
      minmax(0, .66fr)
      minmax(0, 1.08fr)
      minmax(0, .64fr)
      minmax(0, .58fr)
      minmax(0, .72fr)
      minmax(0, .92fr)
      minmax(0, .5fr)
      minmax(0, .5fr)
      minmax(0, .36fr)
      minmax(0, .68fr) !important;
    gap: 4px !important;
  }

  .sv-lhp-open-form label {
    font-size: clamp(5.8px, 1.1vw, 8.6px) !important;
  }

  .sv-lhp-open-form input,
  .sv-lhp-open-form select,
  .sv-lhp-open-actions button {
    height: clamp(19px, 3.5vw, 26px) !important;
    min-height: clamp(19px, 3.5vw, 26px) !important;
    font-size: clamp(5.8px, 1.15vw, 8.8px) !important;
    border-radius: 5px !important;
  }

  .sv-lhp-open-actions {
    gap: 2px !important;
  }

  .sv-lhp-open-actions button {
    padding: 1px 2px !important;
  }

  .sv-canh-bao-lhp-wrap {
    padding: 8px 8px 0;
  }

  .sv-canh-bao-lhp-panel {
    padding: 9px;
  }

  .sv-canh-bao-lhp-head {
    align-items: flex-start;
  }

  .sv-hk-warning-buttons {
    grid-template-columns: 1fr;
  }

  .sv-hk-warning-detail li {
    grid-template-columns: minmax(82px, auto) minmax(0, 1fr);
  }

  .sv-lhp-list-card .sv-card-title {
    padding: 10px;
  }

  .sv-lhp-list-card .sv-title-tools {
    display: grid;
    grid-template-columns: 1fr;
    width: 100%;
    gap: 7px;
  }

  .sv-lhp-list-card .sv-search-input,
  .sv-lhp-list-card select.sv-search-input {
    width: 100%;
    min-width: 0;
  }

  .sv-lhp-table-wrap {
    overflow-x: hidden !important;
  }

  .sv-lhp-table {
    width: 100% !important;
    min-width: 0 !important;
    max-width: 100% !important;
  }
}

/* =========================
   19. MOBILE NHỎ
   Bảng lớp học phần chuyển dạng thẻ.
   ========================= */

@media (max-width: 640px) {
  .sv-flow-page {
    overflow-x: hidden !important;
  }

  .sv-content {
    width: 100%;
    max-width: 100%;
    overflow-x: hidden !important;
  }

  .sv-page-header {
    padding: 0;
  }

  .sv-page-header h1 {
    font-size: 18px;
  }

  .sv-card-title {
    display: grid;
    grid-template-columns: 1fr;
    gap: 6px;
  }

  .sv-filter-card,
  .sv-card,
  .sv-lhp-list-card {
    border-radius: 12px;
  }

  #form-lhc.sv-grid,
  form#form-lhc,
  .sv-form-table-3,
  .sv-form-table-4 {
    grid-template-columns: 1fr !important;
  }

  #form-lhc .sv-span-4,
  .sv-col-span-3 {
    grid-column: span 1 !important;
  }

  .sv-lhp-auto-actions {
    grid-template-columns: 1fr !important;
  }

  .sv-lhp-loading {
    grid-column: span 1;
  }

  .sv-lhc-checkbox-dropdown-list {
    position: static;
    width: 100%;
    max-height: 240px;
    margin-top: 6px;
    box-shadow: none;
  }

  .sv-auto-lhc-dropdown[open] summary {
    border-radius: 9px 9px 0 0 !important;
  }

  .sv-auto-tao-summary {
    display: grid;
    grid-template-columns: 1fr;
  }

  .sv-auto-tao-summary span {
    justify-content: space-between;
    width: 100%;
  }

  .sv-lhp-table-wrap {
    width: 100% !important;
    max-width: 100% !important;
    max-height: none !important;
    overflow: visible !important;
    border: 0 !important;
    border-radius: 0 !important;
    background: transparent !important;
  }

  .sv-lhp-table {
    display: block !important;
    width: 100% !important;
    min-width: 0 !important;
    max-width: 100% !important;
    table-layout: auto !important;
    border-collapse: separate !important;
    border-spacing: 0 !important;
    background: transparent !important;
  }

  .sv-lhp-table thead {
    display: none !important;
  }

  .sv-lhp-table tbody {
    display: grid !important;
    grid-template-columns: 1fr !important;
    gap: 10px !important;
    width: 100% !important;
    padding: 10px !important;
  }

  .sv-lhp-table tr {
    display: grid !important;
    grid-template-columns: 1fr !important;
    gap: 0 !important;
    width: 100% !important;
    min-width: 0 !important;
    max-width: 100% !important;
    height: auto !important;
    min-height: 0 !important;
    padding: 10px 12px !important;
    border: 1px solid var(--pn-border) !important;
    border-radius: 12px !important;
    background: #fff !important;
    box-shadow: 0 4px 14px rgba(15, 23, 42, .045) !important;
    overflow: visible !important;
  }

  .sv-lhp-table tr:hover td {
    background: transparent !important;
  }

  .sv-lhp-table th,
  .sv-lhp-table td,
  .sv-lhp-table th:nth-child(n),
  .sv-lhp-table td:nth-child(n) {
    position: static !important;
    display: grid !important;
    grid-template-columns: 126px minmax(0, 1fr) !important;
    align-items: start !important;
    column-gap: 10px !important;
    width: 100% !important;
    min-width: 0 !important;
    max-width: 100% !important;
    height: auto !important;
    min-height: 32px !important;
    padding: 7px 0 !important;
    border-bottom: 1px dashed #e2e8f0 !important;
    background: transparent !important;
    color: var(--pn-text) !important;
    font-size: 12px !important;
    line-height: 1.35 !important;
    text-align: left !important;
    vertical-align: top !important;
    white-space: normal !important;
    overflow: visible !important;
    text-overflow: clip !important;
  }

  .sv-lhp-table td:last-child {
    border-bottom: 0 !important;
  }

  .sv-lhp-table td::before {
    content: attr(data-label);
    display: block !important;
    min-width: 0 !important;
    color: var(--pn-muted) !important;
    font-size: 11.5px !important;
    font-weight: 900 !important;
    line-height: 1.35 !important;
    white-space: normal !important;
    overflow: visible !important;
    text-overflow: clip !important;
  }

  .sv-lhp-table td:empty::after {
    content: "—";
    color: var(--pn-muted);
  }

  .sv-lhp-table td.empty {
    display: block !important;
    padding: 18px 12px !important;
    text-align: center !important;
  }

  .sv-lhp-table td.empty::before {
    content: "" !important;
    display: none !important;
  }

  .sv-lhp-table td > * {
    min-width: 0 !important;
    max-width: 100% !important;
  }

  .sv-lhp-table .sv-code,
  .sv-lhp-table .sv-main-text,
  .sv-lhp-table .sv-note-text,
  .sv-lhp-table .sv-status-pill {
    display: inline-flex !important;
    width: fit-content !important;
    max-width: 100% !important;
    white-space: normal !important;
    overflow: visible !important;
    text-overflow: clip !important;
    font-size: 11.5px !important;
    line-height: 1.25 !important;
  }

  .sv-lhp-table td:nth-child(11) .sv-row-actions,
  .sv-lhp-table td[data-label="Thao tác"] .sv-row-actions {
    display: grid !important;
    grid-template-columns: 1fr 1fr !important;
    gap: 6px !important;
    width: 100% !important;
    max-width: 100% !important;
    align-items: stretch !important;
    justify-content: stretch !important;
  }

  .sv-lhp-table td:nth-child(11) .small,
  .sv-lhp-table td:nth-child(11) button.small,
  .sv-lhp-table td:nth-child(11) a.small,
  .sv-lhp-table td[data-label="Thao tác"] .small,
  .sv-lhp-table td[data-label="Thao tác"] button.small,
  .sv-lhp-table td[data-label="Thao tác"] a.small {
    width: 100% !important;
    height: 32px !important;
    min-height: 32px !important;
    padding: 5px 6px !important;
    border-radius: 8px !important;
    font-size: 11px !important;
    line-height: 1.15 !important;
    white-space: nowrap !important;
    overflow: hidden !important;
    text-overflow: ellipsis !important;
  }
}

/* =========================
   20. MOBILE RẤT NHỎ
   ========================= */

@media (max-width: 420px) {
  .sv-flow-page {
    font-size: 12px;
  }

  .sv-page-header h1 {
    font-size: 17px;
  }

  .sv-summary-chip {
    width: 100%;
    justify-content: flex-start;
  }

  .sv-mini-stat {
    width: 100%;
    grid-template-columns: 22px minmax(0, 1fr) auto;
  }

  .sv-lhp-auto-actions button {
    min-height: 34px;
  }

  .sv-lhp-open-form {
    gap: 3px !important;
  }

  .sv-lhp-open-form label {
    font-size: 5.5px !important;
  }

  .sv-lhp-open-form input,
  .sv-lhp-open-form select,
  .sv-lhp-open-actions button {
    height: 18px !important;
    min-height: 18px !important;
    padding: 1px 2px !important;
    font-size: 5.8px !important;
    border-radius: 4px !important;
  }

  .sv-lhp-open-actions {
    gap: 1px !important;
  }

  .sv-lhp-table tbody {
    padding: 8px !important;
    gap: 8px !important;
  }

  .sv-lhp-table tr {
    padding: 9px 10px !important;
  }

  .sv-lhp-table th,
  .sv-lhp-table td,
  .sv-lhp-table th:nth-child(n),
  .sv-lhp-table td:nth-child(n) {
    grid-template-columns: 110px minmax(0, 1fr) !important;
    column-gap: 8px !important;
    font-size: 11.5px !important;
  }

  .sv-lhp-table td::before {
    font-size: 11px !important;
  }

  .sv-lhp-table td:nth-child(11) .sv-row-actions,
  .sv-lhp-table td[data-label="Thao tác"] .sv-row-actions {
    grid-template-columns: 1fr !important;
  }
}
</style>













`

