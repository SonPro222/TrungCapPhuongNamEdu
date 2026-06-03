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

          <!-- THẺ THỐNG KÊ -->
          <div class="sv-stat-cards">
            <div class="sv-stat-card">
              <div class="sv-stat-icon stat-blue">👥</div>
              <div class="sv-stat-body">
                <span class="sv-stat-label">Tổng sinh viên</span>
                <strong class="sv-stat-value">{{ soSinhVienToanTruong }}</strong>
                <small class="sv-stat-sub">Toàn hệ thống</small>
              </div>
            </div>
            <div class="sv-stat-card">
              <div class="sv-stat-icon stat-violet">🏫</div>
              <div class="sv-stat-body">
                <span class="sv-stat-label">Lớp hành chính</span>
                <strong class="sv-stat-value">{{ danhSachLHC.length }}</strong>
                <small class="sv-stat-sub">Đang hoạt động</small>
              </div>
            </div>
            <div class="sv-stat-card">
              <div class="sv-stat-icon stat-green">🎓</div>
              <div class="sv-stat-body">
                <span class="sv-stat-label">Sinh viên toàn trường</span>
                <strong class="sv-stat-value sv-stat-value-green">{{ soSinhVienToanTruong }}</strong>
                <small class="sv-stat-sub">Đang theo học</small>
              </div>
            </div>
            <div class="sv-stat-card">
              <div class="sv-stat-icon stat-orange">📌</div>
              <div class="sv-stat-body">
                <span class="sv-stat-label">Sinh viên bảo lưu</span>
                <strong class="sv-stat-value">{{ soSinhVienBaoLuu }}</strong>
                <small class="sv-stat-sub">Hiện tại</small>
              </div>
            </div>
          </div>

          <!-- BỘ LỌC DROPDOWN -->
          <div class="sv-filter-card">
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
                <span>Version</span>
                <select v-model="versionSelectId" :disabled="!chuongTrinhDangChon">
                  <option value="">Chọn version...</option>
                  <option v-for="v in versionTheoCT" :key="v.id" :value="v.id">
                    {{ v.maVersion }}{{ v.tenVersion ? ' - ' + v.tenVersion : '' }}
                  </option>
                </select>
              </label>

              <label class="sv-filter-field">
                <span>Lớp hành chính</span>
                <select v-model="lhcSelectId" :disabled="!versionDangChon">
                  <option value="">Chưa chọn</option>
                  <option v-for="lop in danhSachLHC" :key="lop.id" :value="lop.id">
                    {{ lop.maLop }} - {{ lop.tenLop }}
                  </option>
                </select>
              </label>
            </div>

            <div class="sv-filter-hint">
              <span class="sv-filter-hint-ico">ℹ️</span>
              <span>
                Chọn lần lượt <strong>Ngành → Chương trình → Version</strong> để mở danh sách lớp hành chính,
                sau đó <strong>Tiếp nhận SV</strong> hoặc <strong>Mở LHP</strong>.
              </span>
            </div>
          </div>

          <!-- TRẠNG THÁI: CHƯA CHỌN ĐỦ -->
          <div v-if="!versionDangChon" class="sv-card sv-empty-state">
            <div class="sv-empty-ico">🗂️</div>
            <h2>Chọn ngành, chương trình và version</h2>
            <p>Hãy chọn đủ <strong>Ngành → Chương trình → Version</strong> ở bộ lọc phía trên để tạo và quản lý lớp hành chính.</p>
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
                  <th>Mã lớp</th>
                  <th>Tên lớp</th>
                  <th>Sĩ số</th>
                  <th>Trạng thái</th>
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
                    <span class="sv-note-text">{{ lop.ghiChu || '—' }}</span>
                  </td>

                  <td>
                    <div class="sv-row-actions sv-row-actions-nowrap">
                      <button type="button" class="small btn-nhanh1" @click="chonLHCNhanhTiepNhan(lop)">
                        Tiếp nhận SV →
                      </button>

                      <button type="button" class="small btn-nhanh2" @click="chonLHCNhanhLHP(lop)">
                        Mở LHP →
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
                  <td colspan="7" class="empty">
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
        <span>Mã SV: {{ taiKhoanMoi.maSinhVien }}</span>
        <span>Gmail: {{ taiKhoanMoi.emailTaiKhoan }}</span>
        <span>Mật khẩu tạm: {{ taiKhoanMoi.matKhauTam }}</span>
      </div>
      <div class="sv-card sv-student-receive-card">
        <div class="sv-card-title">
          <div>
            <h2>{{ idSVSua ? 'Cập nhật hồ sơ sinh viên' : 'Tiếp nhận hồ sơ sinh viên' }}</h2>
            <p>
              Lớp HC:
              <strong>{{ lopHanhChinhDangChon?.maLop }} - {{ lopHanhChinhDangChon?.tenLop }}</strong>
              | Hệ thống tự sinh mã SV và cấp tài khoản.
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
                <label>SĐT cha <span class="bat-buoc">*</span>
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
                <label>SĐT mẹ <span class="bat-buoc">*</span>
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
                <label>SĐT người thân <span class="bat-buoc">*</span>
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
          <input v-model.trim="tuKhoaSV" placeholder="Tìm mã SV, họ tên, gmail..." style="max-width:260px"/>
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
      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>{{ idLHPSua ? 'Sửa lớp học phần' : 'Mở lớp học phần' }}</h2>
            <p>Version: <strong>{{ versionDangChon?.maVersion }}</strong> | Lớp HC:
              <strong>{{ lopHanhChinhDangChon?.maLop }} - {{ lopHanhChinhDangChon?.tenLop }}</strong></p></div>
        </div>
        <form class="sv-grid sv-grid-3" @submit.prevent="luuLopHocPhan">
          <label>Khung kỳ
            <select v-model="formLHP.khungKyId" required @change="khiDoiKhungKyMoLop">
              <option value="">Chọn kỳ</option>
              <option v-for="ky in khungKyTheoVersion" :key="ky.id" :value="ky.id">
                {{ ky.maKy }} - {{ ky.tenKy }}
              </option>
            </select>
          </label>

          <label>Môn trong kỳ
            <select v-model="formLHP.chuongTrinhMonId" required :disabled="!formLHP.khungKyId">
              <option value="">Chọn môn trong kỳ</option>
              <option v-for="mon in chuongTrinhMonTheoKhungKyDangChon" :key="mon.id" :value="mon.id">
                {{ mon.maMonTrongCt || mon.maMon || mon.monHocMa || mon.id }} -
                {{ mon.tenMonHoc || mon.monHocTen || mon.ghiChu || 'Môn thuộc kỳ đã chọn' }}
              </option>
            </select>
          </label>

          <label>Loại lớp
            <select v-model="formLHP.loaiLopHocPhan">
              <option value="CHUYEN_NGANH">Chuyên ngành</option>
              <option value="HOC_CHUNG">Học chung</option>
            </select>
          </label>

          <label>Trạng thái
            <select v-model="formLHP.trangThai">
              <option value="du_kien">Dự kiến</option>
              <option value="dang_mo">Đang mở</option>
              <option value="dang_hoc">Đang học</option>
              <option value="da_ket_thuc">Đã kết thúc</option>
              <option value="huy">Hủy</option>
            </select>
          </label>

          <label>Mã lớp học phần
            <input v-model.trim="formLHP.maLop" required/>
          </label>

          <label>Tên lớp học phần
            <input v-model.trim="formLHP.tenLop" required/>
          </label>

          <label>Sĩ số tối thiểu
            <input v-model.number="formLHP.siSoToiThieu" type="number" min="1"/>
          </label>

          <label>Sĩ số tối đa
            <input v-model.number="formLHP.soLuongToiDa" type="number" min="1"/>
          </label>

          <label>Số buổi học
            <input v-model.number="formLHP.soBuoiHoc" type="number" min="1" required/>
          </label>

          <label>Ngày bắt đầu
            <input v-model="formLHP.ngayBatDau" type="date"/>
          </label>

          <label>Ngày kết thúc
            <input v-model="formLHP.ngayKetThuc" type="date"/>
          </label>

          <div class="sv-actions sv-span-3">
            <button type="submit" :disabled="dangLuu">
              {{ dangLuu ? 'Đang lưu...' : (idLHPSua ? 'Cập nhật' : 'Mở lớp') }}
            </button>
            <button type="button" class="secondary" @click="resetFormLHP">Làm mới</button>
          </div>
        </form>
      </div>

      <div class="sv-card sv-lhp-list-card">
        <div class="sv-card-title sv-card-title-toolbar">
          <div>
            <h2>Danh sách lớp học phần theo version</h2>
            <p>Chọn lớp để phân bổ sinh viên. Danh sách được sắp xếp theo kỳ.</p>
          </div>

          <div class="sv-title-tools">
            <input
                v-model.trim="tuKhoaLHP"
                class="sv-search-input"
                placeholder="Lọc mã môn, tên môn, mã lớp, tên lớp..."
            />
            <span class="so-ban-ghi">
      {{ danhSachLHPTheoVersion.length }} lớp
    </span>
          </div>
        </div>
        <div class="sv-table-wrap sv-lhp-table-wrap">
          <table class="sv-table sv-lhp-table">
            <thead>
            <tr>
              <th>STT</th>
              <th>Kỳ</th>
              <th>Mã môn</th>
              <th>Tên môn</th>
              <th>Mã lớp</th>
              <th>Tên lớp</th>
              <th>Loại</th>
              <th>Sĩ số</th>
              <th>Ngày bắt đầu</th>
              <th>Ngày kết thúc</th>
              <th>Trạng thái</th>
              <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(lhp, i) in danhSachLHPTheoVersion" :key="lhp.id">
              <td>{{ i + 1 }}</td>
              <td>{{ layTenKyCuaLHP(lhp) }}</td>
              <td>{{ layMaMonCuaLHP(lhp) }}</td>
              <td>{{ layTenMonCuaLHP(lhp) }}</td>
              <td>{{ lhp.maLopHocPhan || lhp.maLop }}</td>
              <td>{{ lhp.tenLopHocPhan || lhp.tenLop }}</td>
              <td>{{ layLoaiCuaLHP(lhp) }}</td>
              <td>{{ laySiSoHienTaiLHP(lhp) }} / {{ lhp.soLuongToiDa || lhp.siSoToiDa || 0 }}</td>
              <td>{{ dinhDangNgay(lhp.ngayBatDau) }}</td>
              <td>{{ dinhDangNgay(lhp.ngayKetThuc) }}</td>
              <td>{{ lhp.trangThai }}</td>
              <td>
                <div class="sv-row-actions">
                  <RouterLink
                      class="small btn-xem-sv"
                      :to="{ path: '/giang-day', query: { lopHocPhanId: lhp.id, tab: 'sinh-vien' } }"
                  >
                    Xem SV
                  </RouterLink>

                  <button type="button" class="small btn-nhanh2" @click="chonLHPPhanBo(lhp)">
                    Phân bổ SV →
                  </button>

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
                    Giảng dạy →
                  </RouterLink>

                  <button type="button" class="secondary small" @click="suaLopHocPhan(lhp)">Sửa</button>
                  <button type="button" class="danger small" @click="xoaLopHocPhan(lhp)">Xóa</button>
                </div>
              </td>
            </tr>
            <tr v-if="!danhSachLHPTheoVersion.length">
              <td colspan="12" class="empty">Chưa có lớp học phần theo version này.</td>
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
            <p>LHP: <strong>{{ lhpDangPhanBo?.maLopHocPhan || lhpDangPhanBo?.maLop }}</strong> —
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
              <th>Mã SV</th>
              <th>Họ tên</th>
              <th>Gmail</th>
              <th>SĐT</th>
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

// Dữ liệu theo context
const danhSachLHC = ref([])
const danhSachSVTrongLop = ref([])
const danhSachSVChuongTrinh = ref([])
const danhSachLHP = ref([])
const svDaPhanBoIds = ref(new Set())
const siSoTheoLopHocPhan = ref({})
// Context đang chọn
const nganhDangChon = ref(null)
const chuongTrinhDangChon = ref(null)
const versionDangChon = ref(null)
const lopHanhChinhDangChon = ref(null)
const lhpDangPhanBo = ref(null)

// Form ngành
const idNganhSua = ref(null)
const formNganh = reactive({maNganh: '', tenNganh: '', ghiChu: ''})

// Form chương trình
const idCTSua = ref(null)
const formCT = reactive({maChuongTrinh: '', tenChuongTrinh: '', ghiChu: ''})

// Form lớp hành chính
const idLHCSua = ref(null)
const formLHC = reactive({maLop: '', tenLop: '', siSo: 0, trangThai: 'du_kien', ghiChu: ''})

// Form sinh viên
const idSVSua = ref(null)
const formSV = reactive(taoFormSVMacDinh())
const filesSV = reactive({anhChanDung: null, cccdTruoc: null, cccdSau: null, bangCap: null, giayToKhac: []})

// Form lớp học phần
const idLHPSua = ref(null)
const formLHP = reactive(taoFormLHPMacDinh())

// Phân bổ
const svDaChon = ref([])
const daBamChonTatCaSV = ref(false)
const tuKhoaSV = ref('')
const tuKhoaLHC = ref('')
const tuKhoaLHP = ref('')

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

const lhcSelectId = computed({
  get: () => lopHanhChinhDangChon.value?.id ?? '',
  set: (id) => {
    lopHanhChinhDangChon.value =
        danhSachLHC.value.find(x => String(x.id) === String(id)) || null
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
      clickable: ['tiepNhan', 'lopHocPhan', 'phanBo'].includes(buoc.value)
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

  if (buoc.value === 'phanBo') {
    list.push({
      name: 'Lớp học phần',
      value: lhpDangPhanBo.value
          ? `${lhpDangPhanBo.value.maLopHocPhan || lhpDangPhanBo.value.maLop || ''} - ${lhpDangPhanBo.value.tenLopHocPhan || lhpDangPhanBo.value.tenLop || ''}`.trim()
          : 'Chưa chọn',
      step: 'lopHocPhan',
      clickable: true
    })

    list.push({
      name: 'Chức năng',
      value: 'Phân bổ sinh viên',
      step: 'phanBo',
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

  const kw = tuKhoaLHP.value.trim().toLowerCase()

  return danhSachLHP.value
      .filter(lhp => chuongTrinhMonIdsTrongVersion.has(String(lhp.chuongTrinhMonId)))
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

const danhSachSVChuaVaoLop = computed(() =>
    danhSachSVTrongLop.value.filter(sv => !svDaPhanBoIds.value.has(String(sv.id)))
)

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
      lhpDangPhanBo
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
    lopHanhChinhId: lopHanhChinhDangChon.value?.id || null,
    lopHocPhanId: lhpDangPhanBo.value?.id || null
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
      const [lhc, lhp, svCT] = await Promise.all([
        sinhVienService.layLopHanhChinhTheoVersion(versionDangChon.value.id),
        sinhVienService.layLopHocPhan(),
        sinhVienService.laySinhVienChuongTrinh({chuongTrinhVersionId: versionDangChon.value.id})
      ])

      danhSachLHC.value = lhc
      danhSachLHP.value = lhp
      danhSachSVChuongTrinh.value = svCT

      await taiSiSoLopHocPhanTheoVersion()

      lopHanhChinhDangChon.value = danhSachLHC.value.find(x => String(x.id) === String(state.lopHanhChinhId)) || null
      lhpDangPhanBo.value = danhSachLHP.value.find(x => String(x.id) === String(state.lopHocPhanId)) || null
    }

    if (state.buoc && coTheDungOBuoc(state.buoc)) {
      buoc.value = state.buoc
    }

    if (buoc.value === 'tiepNhan' || buoc.value === 'phanBo') {
      await taiSinhVienTrongLop()
    }

    if (buoc.value === 'phanBo' && lhpDangPhanBo.value) {
      const svTrongLHP = await sinhVienService.laySinhVienLopHocPhan({lopHocPhanId: lhpDangPhanBo.value.id})
      const danhSachDangKy = layDanhSachTuResponse(svTrongLHP)
      svDaPhanBoIds.value = new Set(danhSachDangKy.map(x => String(x.sinhVienId)))
      siSoTheoLopHocPhan.value = {
        ...siSoTheoLopHocPhan.value,
        [String(lhpDangPhanBo.value.id)]: danhSachDangKy.length
      }
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
  if (step === 'phanBo') return !!lopHanhChinhDangChon.value && !!lhpDangPhanBo.value
  return false
}


// ─── ĐIỀU HƯỚNG ───────────────────────────────────────────────────────────────
function quayVeBuoc(step) {
  if (step === 'nganh') {
    nganhDangChon.value = null;
    chuongTrinhDangChon.value = null;
    versionDangChon.value = null;
    lopHanhChinhDangChon.value = null;
    lhpDangPhanBo.value = null
  }
  if (step === 'chuongTrinh') {
    chuongTrinhDangChon.value = null;
    versionDangChon.value = null;
    lopHanhChinhDangChon.value = null;
    lhpDangPhanBo.value = null
  }
  if (step === 'version') {
    versionDangChon.value = null;
    lopHanhChinhDangChon.value = null;
    lhpDangPhanBo.value = null
  }
  if (step === 'lopHanhChinh') {
    lopHanhChinhDangChon.value = null;
    lhpDangPhanBo.value = null
  }
  if (step === 'lopHocPhan') {
    lhpDangPhanBo.value = null
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

  try {
    const [lhc, lhp, svCT] = await Promise.all([
      sinhVienService.layLopHanhChinhTheoVersion(v.id),
      sinhVienService.layLopHocPhan(),
      sinhVienService.laySinhVienChuongTrinh({chuongTrinhVersionId: v.id})
    ])

    danhSachLHC.value = lhc
    danhSachLHP.value = lhp
    danhSachSVChuongTrinh.value = svCT

    await taiSiSoLopHocPhanTheoVersion()
  } catch (e) {
    baoLoi(e.message)
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
    danhSachLHP.value = await sinhVienService.layLopHocPhan()
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
    ghiChu: lop.ghiChu || ''
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
  idLHCSua.value = null;
  Object.assign(formLHC, {maLop: '', tenLop: '', siSo: 0, trangThai: 'du_kien', ghiChu: ''})
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

function layChuongTrinhMonCuaLHP(lhp) {
  return danhSachChuongTrinhMon.value.find(mon =>
      String(mon.id) === String(lhp?.chuongTrinhMonId)
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
      soLuongHienTai: 0,
      soBuoiHoc: Number(formLHP.soBuoiHoc || 1),
      ngayBatDau: formLHP.ngayBatDau,
      ngayKetThuc: formLHP.ngayKetThuc
    })
    idLHPSua.value ? await sinhVienService.capNhatLopHocPhan(idLHPSua.value, p) : await sinhVienService.taoLopHocPhan(p)
    baoThanhCong(idLHPSua.value ? 'Đã cập nhật lớp học phần' : 'Đã mở lớp học phần')
    resetFormLHP()
    danhSachLHP.value = await sinhVienService.layLopHocPhan()
    await taiSiSoLopHocPhanTheoVersion()
  } catch (e) {
    baoLoi(e.message)
    try {
      danhSachLHP.value = await sinhVienService.layLopHocPhan()
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
    soBuoiHoc: lhp.soBuoiHoc || 1,
    ngayBatDau: lhp.ngayBatDau || '',
    ngayKetThuc: lhp.ngayKetThuc || ''
  })
}

async function xoaLopHocPhan(lhp) {
  if (!confirm(`Xóa lớp ${lhp.maLopHocPhan || lhp.maLop}?`)) return
  try {
    await sinhVienService.xoaLopHocPhan(lhp.id);
    danhSachLHP.value = await sinhVienService.layLopHocPhan();
    baoThanhCong('Đã xóa')
  } catch (e) {
    baoLoi(e.message)
  }
}

function resetFormLHP() {
  idLHPSua.value = null
  Object.assign(formLHP, taoFormLHPMacDinh())
}

async function chonLHPPhanBo(lhp) {
  lhpDangPhanBo.value = lhp
  buoc.value = 'phanBo'
  svDaChon.value = []
  daBamChonTatCaSV.value = false
  xoaThongBao()

  try {
    await taiSinhVienTrongLop()

    const svTrongLHP = await sinhVienService.laySinhVienLopHocPhan({
      lopHocPhanId: lhp.id
    })

    const danhSachDangKy = layDanhSachTuResponse(svTrongLHP)

    svDaPhanBoIds.value = new Set(
        danhSachDangKy.map(x => String(x.sinhVienId))
    )

    siSoTheoLopHocPhan.value = {
      ...siSoTheoLopHocPhan.value,
      [String(lhp.id)]: danhSachDangKy.length
    }
  } catch (e) {
    baoLoi(e.message, 'phanBo')
  }
}

// ─── BƯỚC 4.3: PHÂN BỔ ───────────────────────────────────────────────────────
function chonTatCaSV(e) {
  daBamChonTatCaSV.value = e.target.checked
  svDaChon.value = e.target.checked
      ? danhSachSVChuaVaoLop.value.map(sv => sv.id)
      : []
}

function khiChonTungSinhVien() {
  daBamChonTatCaSV.value = false
}

async function phanBoSinhVien() {
  if (!lhpDangPhanBo.value || !svDaChon.value.length) return

  dangLuu.value = true

  try {
    await Promise.all(
        svDaChon.value.map(svId =>
            sinhVienService.dangKyLopHocPhan({
              sinhVienId: svId,
              lopHocPhanId: lhpDangPhanBo.value.id
            })
        )
    )

    svDaChon.value.forEach(id => svDaPhanBoIds.value.add(String(id)))
    svDaChon.value = []
    daBamChonTatCaSV.value = false

    siSoTheoLopHocPhan.value = {
      ...siSoTheoLopHocPhan.value,
      [String(lhpDangPhanBo.value.id)]: svDaPhanBoIds.value.size
    }

    danhSachLHP.value = await sinhVienService.layLopHocPhan()
    await taiSiSoLopHocPhanTheoVersion()

    const lopHocPhanMoi = danhSachLHP.value.find(lhp =>
        String(lhp.id) === String(lhpDangPhanBo.value.id)
    )

    if (lopHocPhanMoi) {
      lhpDangPhanBo.value = lopHocPhanMoi
    }

    baoThanhCong('Đã phân bổ sinh viên vào lớp học phần thành công', 'phanBo')
  } catch (e) {
    baoLoi(e.message, 'phanBo')
  } finally {
    dangLuu.value = false
  }
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
    soBuoiHoc: 1,
    ngayBatDau: '',
    ngayKetThuc: ''
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
@import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;600;700;800&display=swap');
.sv-flow-page,
.sv-flow-page * {
  font-family: 'Roboto', Arial, sans-serif;
  letter-spacing: normal;
  box-sizing: border-box;
}

.sv-flow-page {
  display: grid;
  gap: 16px;
}
.sv-content {
  display: grid;
  gap: 16px;
  min-width: 0;
}

/* ===== CỘT TRÁI: ACCORDION CHUYỂN TAB ===== */
.sv-side-nav {
  position: sticky;
  top: 16px;
  align-self: start;
  display: grid;
  gap: 8px;
}
.sv-acc-item {
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
  overflow: hidden;
}
.sv-acc-item.active { border-color: #bfdbfe; }
.sv-acc-head {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 12px;
  background: none;
  border: none;
  cursor: pointer;
  font-size: 13px;
  font-weight: 600;
  color: #334155;
  text-align: left;
}
.sv-acc-item.active .sv-acc-head { color: #2563eb; background: #eff6ff; }
.sv-acc-head:hover { background: #f8fafc; }
.sv-acc-item.active .sv-acc-head:hover { background: #e0edff; }
.sv-acc-ico { font-size: 15px; flex-shrink: 0; }
.sv-acc-label { flex: 1; line-height: 1.25; }
.sv-acc-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 17px;
  padding: 0 6px;
  border-radius: 999px;
  background: #e2e8f0;
  color: #475569;
  font-size: 11px;
  font-weight: 700;
}
.sv-acc-arrow { font-size: 11px; color: #94a3b8; transition: transform .15s; }
.sv-acc-item.open .sv-acc-arrow { transform: rotate(180deg); }
.sv-acc-body {
  padding: 0 12px 12px;
  display: grid;
  gap: 8px;
}
.sv-acc-body p { margin: 0; font-size: 12.5px; color: #64748b; line-height: 1.4; }
.sv-acc-link {
  font-size: 12.5px;
  font-weight: 600;
  color: #2563eb;
  text-decoration: none;
}
.sv-acc-link:hover { text-decoration: underline; }

@media (max-width: 900px) {
  .sv-flow-page { grid-template-columns: 1fr; }
  .sv-side-nav { position: static; }
}

/* ===== HEADER ===== */
.sv-page-header h1 {
  margin: 0;
  font-size: 26px;
  font-weight: 800;
  color: #0f172a;
}
.sv-page-header p {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 14px;
}

/* ===== TABS (dọc, bên trái, nhỏ gọn) ===== */
.sv-tabs {
  display: inline-flex;
  flex-direction: column;
  align-items: stretch;
  align-self: flex-start;
  gap: 4px;
  width: fit-content;
  max-width: 260px;
  padding: 4px;
  background: #f1f5f9;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}
.sv-tab {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
  background: none;
  border: none;
  border-radius: 8px;
  padding: 7px 10px;
  font-size: 12.5px;
  font-weight: 600;
  color: #64748b;
  cursor: pointer;
  text-decoration: none;
  text-align: left;
  line-height: 1.25;
  white-space: nowrap;
}
.sv-tab:hover { color: #2563eb; background: #e2e8f0; }
.sv-tab.active {
  color: #2563eb;
  background: #fff;
  box-shadow: 0 1px 3px rgba(15, 23, 42, .08);
}
.sv-tab-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-width: 18px;
  height: 17px;
  padding: 0 6px;
  border-radius: 999px;
  background: #e2e8f0;
  color: #475569;
  font-size: 11px;
  font-weight: 700;
}
.sv-tab.active .sv-tab-badge,
.sv-tab:hover .sv-tab-badge { background: #dbeafe; color: #2563eb; }

/* ===== LAYOUT 2 CỘT: NỘI DUNG TRÁI – GỢI Ý PHẢI ===== */
.sv-manage-layout {
  display: grid;
  grid-template-columns: 1fr;
  gap: 16px;
  align-items: start;
}
.sv-manage-main {
  display: grid;
  gap: 16px;
  min-width: 0;
}

/* ===== THẺ THỐNG KÊ ===== */
.sv-stat-cards {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 16px;
}
.sv-stat-card {
  display: flex;
  align-items: center;
  gap: 14px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 4px 16px rgba(15, 23, 42, .05);
}
.sv-stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  flex-shrink: 0;
}
.stat-blue   { background: #dbeafe; }
.stat-violet { background: #ede9fe; }
.stat-green  { background: #d1fae5; }
.stat-orange { background: #ffedd5; }
.sv-stat-body { display: flex; flex-direction: column; min-width: 0; }
.sv-stat-label { font-size: 13px; color: #64748b; font-weight: 600; }
.sv-stat-value { font-size: 26px; font-weight: 800; color: #0f172a; line-height: 1.15; }
.sv-stat-value-green { color: #059669; }
.sv-stat-sub { font-size: 12px; color: #94a3b8; }

/* ===== BỘ LỌC ===== */
.sv-filter-card {
  background: #f8fafc;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 16px;
}
.sv-filter-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 14px;
}
.sv-filter-field { display: flex; flex-direction: column; gap: 6px; }
.sv-filter-field > span {
  font-size: 13px;
  font-weight: 600;
  color: #334155;
}
.sv-filter-field select {
  height: 40px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 0 12px;
  background: #fff;
  color: #0f172a;
  font-size: 14px;
}
.sv-filter-field select:disabled { background: #f1f5f9; color: #94a3b8; cursor: not-allowed; }
.sv-filter-field select:focus { outline: none; border-color: #2563eb; box-shadow: 0 0 0 3px rgba(37,99,235,.15); }
.sv-filter-hint {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  margin-top: 14px;
  font-size: 13px;
  color: #1d4ed8;
}
.sv-filter-hint-ico { flex-shrink: 0; }

/* ===== EMPTY STATE ===== */
.sv-empty-state { text-align: center; padding: 40px 20px; }
.sv-empty-ico { font-size: 40px; }
.sv-empty-state h2 { margin: 12px 0 6px; font-size: 18px; color: #0f172a; }
.sv-empty-state p { margin: 0; color: #64748b; }

/* ===== NÚT PRIMARY (toolbar tạo lớp) ===== */
.sv-btn-primary {
  height: 38px;
  padding: 0 16px;
  border: none;
  border-radius: 10px;
  background: #2563eb;
  color: #fff;
  font-weight: 700;
  font-size: 14px;
  cursor: pointer;
}
.sv-btn-primary:hover { background: #1d4ed8; }
.sv-btn-primary:disabled { opacity: .6; cursor: not-allowed; }

/* ===== FOOTER BẢNG ===== */
.sv-table-footer {
  padding: 12px 4px 2px;
  font-size: 13px;
  color: #64748b;
}

/* ===== PANEL GỢI Ý ===== */
.sv-tips-panel {
  position: sticky;
  top: 16px;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 16px;
  padding: 18px;
  box-shadow: 0 4px 16px rgba(15, 23, 42, .05);
}
.sv-tips-head { display: flex; align-items: center; gap: 8px; margin-bottom: 14px; }
.sv-tips-head h3 { margin: 0; font-size: 16px; font-weight: 700; color: #0f172a; }
.sv-tips-list { list-style: none; margin: 0; padding: 0; display: grid; gap: 16px; }
.sv-tips-list li { display: flex; gap: 12px; align-items: flex-start; font-size: 14px; color: #475569; line-height: 1.4; }
.sv-tip-dot {
  width: 30px; height: 30px;
  border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 14px; flex-shrink: 0; color: #fff;
}
.dot-blue   { background: #3b82f6; }
.dot-green  { background: #10b981; }
.dot-orange { background: #f59e0b; }

/* ===== RESPONSIVE ===== */
@media (max-width: 1100px) {
  .sv-manage-layout { grid-template-columns: 1fr; }
  .sv-tips-panel { position: static; }
  .sv-stat-cards { grid-template-columns: repeat(2, 1fr); }
  .sv-filter-grid { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 640px) {
  .sv-stat-cards, .sv-filter-grid { grid-template-columns: 1fr; }
  .sv-tabs { gap: 16px; overflow-x: auto; }
}

/* Breadcrumb / Summary sticky */
/* Summary sticky */
.sv-summary-bar {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  align-items: center;
  background: rgba(255, 255, 255, 0.96);
  border: 1px solid #bfdbfe;
  border-radius: 16px;
  padding: 10px 12px;
  box-shadow: 0 8px 22px rgba(15, 23, 42, 0.08);
  backdrop-filter: blur(10px);
}

.sv-breadcrumb-sticky {
  position: sticky;
  top: 60px;
  z-index: 999;
}

.sv-summary-chip {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  min-height: 34px;
  border: 1px solid #dbeafe;
  border-radius: 999px;
  padding: 6px 12px;
  background: #eff6ff;
  color: #0f172a;
  box-shadow: none;
  transform: none;
  cursor: default;
}

.sv-summary-chip:hover {
  filter: none;
  transform: none;
}

.sv-summary-chip.clickable {
  cursor: pointer;
}

.sv-summary-chip.clickable:hover {
  border-color: #93c5fd;
  background: #dbeafe;
}

.sv-summary-chip.active {
  border-color: #bbf7d0;
  background: #ecfdf5;
}

.sv-summary-name {
  color: #64748b;
  font-size: 12px;
  font-weight: 800;
  text-transform: uppercase;
}

.sv-summary-value {
  color: #0f172a;
  font-size: 13px;
  font-weight: 800;
}

.sv-summary-chip.active .sv-summary-value {
  color: #047857;
}

/* Card */
.sv-card {
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  padding: 16px;
  box-shadow: 0 4px 16px rgba(15, 23, 42, .05);
}

.sv-card-title {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 12px;
  margin-bottom: 12px;
}

.sv-card-title h2 {
  margin: 0;
  color: #0f172a;
  font-size: 17px;
  font-weight: 700;
}

.sv-card-title p {
  margin: 4px 0 0;
  color: #64748b;
  font-size: 13px;
}

.so-ban-ghi {
  color: #64748b;
  font-size: 13px;
  font-weight: 600;
  white-space: nowrap;
}

/* Grid */
.sv-grid {
  display: grid;
  gap: 10px;
  margin-bottom: 10px;
}

.sv-grid-2 {
  grid-template-columns: repeat(2, 1fr);
}

.sv-grid-3 {
  grid-template-columns: repeat(3, 1fr);
}

.sv-grid-4 {
  grid-template-columns: repeat(4, 1fr);
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

/* Form */
label {
  display: grid;
  gap: 5px;
  color: #334155;
  font-size: 13px;
  font-weight: 600;
}

input, select, textarea {
  width: 100%;
  min-height: 38px;
  border: 1px solid #cbd5e1;
  border-radius: 10px;
  padding: 8px 10px;
  background: #fff;
  color: #0f172a;
  font: inherit;
  font-size: 14px;
  outline: none;
  transition: border-color .15s, box-shadow .15s;
}

textarea {
  min-height: 72px;
  resize: vertical;
}

input:focus, select:focus, textarea:focus {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, .12);
}

h3 {
  margin: 14px 0 6px;
  color: #334155;
  font-size: 14px;
  font-weight: 700;
}

/* Buttons */
button, .btn-giangday, .btn-xem-sv {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 36px;
  border: 0;
  border-radius: 10px;
  padding: 8px 14px;
  background: #1d4ed8;
  color: #fff;
  font-size: 14px;
  font-weight: 700;
  text-decoration: none;
  cursor: pointer;
  white-space: nowrap;
  transition: filter .12s, transform .12s;
}

button:hover, .btn-giangday:hover, .btn-xem-sv:hover {
  filter: brightness(.94);
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
  background: #dc2626;
}

button.small,
.btn-giangday.small,
.btn-xem-sv.small {
  min-height: 30px;
  padding: 5px 10px;
  font-size: 13px;
}

.btn-nhanh1 {
  background: #1d4ed8;
}

.btn-nhanh2 {
  background: #059669;
}

.btn-giangday {
  background: #f59e0b;
}

/* Actions */
.sv-actions {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.sv-row-actions {
  display: flex;
  align-items: center;
  gap: 6px;
  flex-wrap: wrap;
}

/* Table */
.sv-table-wrap {
  width: 100%;
  overflow: auto;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
}

.sv-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 13px;
  min-width: 600px;
}

.sv-table th, .sv-table td {
  border-bottom: 1px solid #e2e8f0;
  padding: 8px 10px;
  text-align: left;
  vertical-align: middle;
}

.sv-table th {
  background: #f8fafc;
  color: #334155;
  font-weight: 700;
  white-space: nowrap;
  position: sticky;
  top: 0;
  z-index: 1;
}

.sv-table tbody tr:hover td {
  background: #f8fafc;
}

.sv-table tr.da-phan-bo td {
  color: #94a3b8;
}

.empty {
  text-align: center;
  color: #64748b;
  padding: 20px;
}

/* Avatar */
.sv-avatar {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 8px;
  border: 1px solid #e2e8f0;
}

/* Status */
.sv-status {
  display: inline-flex;
  align-items: center;
  border-radius: 999px;
  padding: 3px 9px;
  font-size: 12px;
  font-weight: 600;
}

.sv-status.done {
  background: #ecfdf5;
  color: #047857;
}

.sv-status.pending {
  background: #fef2f2;
  color: #b91c1c;
}

/* Messages */
.sv-message {
  border-radius: 12px;
  padding: 10px 14px;
  font-size: 13px;
  font-weight: 600;
}

.sv-message.success {
  background: #ecfdf5;
  color: #047857;
  border: 1px solid #a7f3d0;
}

.sv-message.error {
  background: #fef2f2;
  color: #b91c1c;
  border: 1px solid #fecaca;
}

.sv-account-box {
  display: flex;
  gap: 14px;
  flex-wrap: wrap;
  background: #fffbeb;
  border: 1px solid #fbbf24;
  border-radius: 12px;
  padding: 12px 16px;
  color: #78350f;
  font-size: 13px;
}

/* Responsive */
@media (max-width: 1100px) {
  .sv-grid-4 {
    grid-template-columns: repeat(2, 1fr);
  }

  .sv-grid-3 {
    grid-template-columns: repeat(2, 1fr);
  }

  .sv-span-3, .sv-span-4 {
    grid-column: span 2;
  }
}

@media (max-width: 680px) {
  .sv-grid-2, .sv-grid-3, .sv-grid-4 {
    grid-template-columns: 1fr;
  }

  .sv-span-2, .sv-span-3, .sv-span-4 {
    grid-column: span 1;
  }

  .sv-card-title {
    flex-direction: column;
  }
}

.bat-buoc {
  color: #dc2626;
  font-weight: 700;
}

.sv-check {
  width: 16px;
  min-width: 16px;
  height: 16px;
  min-height: 16px;
  padding: 0;
  margin: 0;
  cursor: pointer;
  accent-color: #1d4ed8;
}

.sv-check-all {
  width: 17px;
  min-width: 17px;
  height: 17px;
  min-height: 17px;
}

.sv-check-row {
  width: 15px;
  min-width: 15px;
  height: 15px;
  min-height: 15px;
}

.sv-check:disabled {
  cursor: not-allowed;
  opacity: 0.45;
}

.sv-table th:first-child,
.sv-table td:first-child {
  width: 44px;
  text-align: center;
}

.btn-xem-sv {
  background: #0f766e;
}

/* ===== BẢNG LỚP HÀNH CHÍNH ===== */

.sv-lhc-list-card {
  width: 100%;
  max-width: none;
  padding: 0;
  overflow: hidden;
}

.sv-lhc-list-card .sv-card-title {
  padding: 16px 18px 12px;
  margin-bottom: 0;
  border-bottom: 1px solid #e2e8f0;
  background: linear-gradient(180deg, #ffffff 0%, #f8fafc 100%);
}

.sv-card-title-toolbar {
  align-items: center;
}

.sv-title-tools {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 10px;
  flex-wrap: wrap;
  min-width: 360px;
}

.sv-search-input {
  width: 280px;
  min-width: 240px;
  min-height: 38px;
  border-radius: 999px;
  background: #ffffff;
  padding-left: 14px;
}

.sv-table-wrap-full {
  border: 0;
  border-radius: 0;
}

.sv-lhc-table-wrap {
  max-height: none;
  overflow-x: auto;
  overflow-y: visible;
}

.sv-lhc-table {
  min-width: 1120px;
}

.sv-lhc-table th {
  position: sticky;
  top: 0;
  z-index: 3;
  background: #f1f5f9;
  box-shadow: inset 0 -1px 0 #e2e8f0;
}

.sv-lhc-table th:first-child,
.sv-lhc-table td:first-child {
  width: 64px;
  text-align: center;
}

.sv-lhc-table th:nth-child(2),
.sv-lhc-table td:nth-child(2) {
  width: 150px;
}

.sv-lhc-table th:nth-child(3),
.sv-lhc-table td:nth-child(3) {
  min-width: 260px;
}

.sv-lhc-table th:nth-child(4),
.sv-lhc-table td:nth-child(4) {
  width: 130px;
  text-align: center;
}

.sv-lhc-table th:nth-child(5),
.sv-lhc-table td:nth-child(5) {
  width: 140px;
}

.sv-lhc-table th:nth-child(7),
.sv-lhc-table td:nth-child(7) {
  width: 360px;
}

.sv-code {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  border-radius: 999px;
  padding: 4px 10px;
  background: #eff6ff;
  color: #1d4ed8;
  font-size: 13px;
  font-weight: 800;
}

.sv-main-text {
  color: #0f172a;
  font-weight: 700;
  line-height: 1.35;
}

.sv-note-text {
  display: inline-block;
  max-width: 360px;
  color: #64748b;
  line-height: 1.35;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-capacity-badge {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
  min-width: 78px;
  border-radius: 999px;
  padding: 5px 10px;
  background: #ecfdf5;
  color: #047857;
  font-size: 13px;
  font-weight: 700;
}

.sv-capacity-badge strong {
  font-size: 14px;
}

.sv-status-pill {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 28px;
  border-radius: 999px;
  padding: 4px 10px;
  background: #f1f5f9;
  color: #475569;
  font-size: 12px;
  font-weight: 800;
  white-space: nowrap;
}

.sv-status-pill.status-dang_hoc {
  background: #ecfdf5;
  color: #047857;
}

.sv-status-pill.status-du_kien {
  background: #eff6ff;
  color: #1d4ed8;
}

.sv-status-pill.status-tam_dung {
  background: #fffbeb;
  color: #b45309;
}

.sv-status-pill.status-da_tot_nghiep {
  background: #f5f3ff;
  color: #6d28d9;
}

.sv-status-pill.status-huy {
  background: #fef2f2;
  color: #b91c1c;
}

.sv-row-actions-nowrap {
  flex-wrap: nowrap;
}

/* ===== BẢNG LỚP HỌC PHẦN THEO VERSION ===== */

.sv-lhp-list-card {
  width: 100%;
  max-width: none;
  overflow: hidden;
}

.sv-lhp-table-wrap {
  width: 100%;
  max-height: calc(100vh - 330px);
  overflow: auto;
  border-radius: 12px;
}

.sv-lhp-table {
  width: 100%;
  min-width: 1180px;
  table-layout: fixed;
  border-collapse: separate;
  border-spacing: 0;
}

.sv-lhp-table thead th {
  position: sticky;
  top: 0;
  z-index: 20;
  background: #f1f5f9;
  box-shadow: inset 0 -1px 0 #cbd5e1;
}

.sv-lhp-table th,
.sv-lhp-table td {
  padding: 6px 7px;
  font-size: 12px;
  line-height: 1.3;
  vertical-align: middle;
}

.sv-lhp-table th {
  white-space: nowrap;
}

.sv-lhp-table td {
  overflow: hidden;
  text-overflow: ellipsis;
}

/* STT */
.sv-lhp-table th:nth-child(1),
.sv-lhp-table td:nth-child(1) {
  width: 44px;
  text-align: center;
}

/* Kỳ */
.sv-lhp-table th:nth-child(2),
.sv-lhp-table td:nth-child(2) {
  width: 92px;
}

/* Mã môn */
.sv-lhp-table th:nth-child(3),
.sv-lhp-table td:nth-child(3) {
  width: 70px;
}

/* Tên môn */
.sv-lhp-table th:nth-child(4),
.sv-lhp-table td:nth-child(4) {
  width: 150px;
  white-space: normal;
}

/* Mã lớp */
.sv-lhp-table th:nth-child(5),
.sv-lhp-table td:nth-child(5) {
  width: 145px;
}

/* Tên lớp */
.sv-lhp-table th:nth-child(6),
.sv-lhp-table td:nth-child(6) {
  width: 190px;
  white-space: normal;
}

/* Loại */
.sv-lhp-table th:nth-child(7),
.sv-lhp-table td:nth-child(7) {
  width: 120px;
  white-space: normal;
}

/* Sĩ số */
.sv-lhp-table th:nth-child(8),
.sv-lhp-table td:nth-child(8) {
  width: 72px;
  text-align: center;
  white-space: nowrap;
}

/* Ngày bắt đầu */
.sv-lhp-table th:nth-child(9),
.sv-lhp-table td:nth-child(9) {
  width: 92px;
  white-space: nowrap;
}

/* Ngày kết thúc */
.sv-lhp-table th:nth-child(10),
.sv-lhp-table td:nth-child(10) {
  width: 92px;
  white-space: nowrap;
}

/* Trạng thái */
.sv-lhp-table th:nth-child(11),
.sv-lhp-table td:nth-child(11) {
  width: 82px;
  white-space: nowrap;
}

/* Thao tác */
.sv-lhp-table th:nth-child(12),
.sv-lhp-table td:nth-child(12) {
  width: 130px;
  min-width: 130px;
  max-width: 130px;
  overflow: visible;
}

.sv-lhp-table td:nth-child(12) .sv-row-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 3px;
  align-items: center;
  justify-content: flex-start;
}

.sv-lhp-table td:nth-child(12) .small,
.sv-lhp-table td:nth-child(12) .btn-xem-sv,
.sv-lhp-table td:nth-child(12) .btn-giangday {
  width: auto;
  min-width: 46px;
  max-width: 86px;
  min-height: 22px;
  padding: 2px 6px;
  border-radius: 6px;
  font-size: 10px;
  line-height: 1.15;
  font-weight: 600;
  justify-content: center;
}

.sv-lhp-table td:nth-child(12) .btn-xem-sv {
  background: #0f766e;
}

.sv-lhp-table td:nth-child(12) .btn-giangday {
  background: #f59e0b;
}

.sv-lhp-list-card .sv-card-title-toolbar {
  align-items: center;
}

.sv-lhp-list-card .sv-title-tools {
  min-width: 420px;
}

.sv-lhp-list-card .sv-search-input {
  width: 340px;
  min-width: 260px;
}

@media (max-width: 900px) {
  .sv-lhp-list-card .sv-title-tools {
    width: 100%;
    min-width: 0;
    justify-content: stretch;
  }

  .sv-lhp-list-card .sv-search-input {
    width: 100%;
    min-width: 0;
  }
}/* ===== TIẾP NHẬN SINH VIÊN: LAYOUT 3/7 ===== */

.sv-student-receive-card {
  padding: 14px;
}

.sv-receive-layout {
  display: grid;
  grid-template-columns: minmax(260px, 3fr) minmax(0, 7fr);
  gap: 14px;
  align-items: start;
}

.sv-file-panel {
  position: sticky;
  top: 142px;
  align-self: start;
  border: 1px solid #dbeafe;
  border-radius: 14px;
  padding: 12px;
  background: #f8fafc;
}

.sv-file-panel-title h3,
.sv-section-box h3 {
  margin: 0 0 4px;
  color: #0f172a;
  font-size: 14px;
  font-weight: 800;
}

.sv-file-panel-title p {
  margin: 0 0 10px;
  color: #64748b;
  font-size: 12px;
}.sv-document-grid {
   display: grid;
   gap: 8px;
 }
.sv-document-row {
  display: grid;
  grid-template-columns: 200px minmax(0, 1fr);
  gap: 8px;
  align-items: center;
}

.sv-document-row-main {
  grid-template-columns: 200px minmax(0, 1fr);
}
.sv-document-row-other {
  grid-template-columns: 1fr;
  background: #f8fafc;
}

.sv-document-left {
  position: relative;
}

.sv-document-right {
  display: grid;
  gap: 5px;
  min-width: 0;
}

.sv-document-right-full {
  width: 100%;
}

.sv-document-title {
  color: #0f172a;
  font-size: 12px;
  font-weight: 900;
  line-height: 1.2;
}

.sv-document-preview {
  width: 180px;
  aspect-ratio: 4 / 3;
  height: auto;
  border: 1px dashed #cbd5e1;
  border-radius: 10px;
  overflow: hidden;
  background: #f8fafc;
  display: grid;
  place-items: center;
}

.sv-portrait-preview {
  width: 180px;
  aspect-ratio: 4 / 3;
  height: auto;
}
.sv-document-preview img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  background: #ffffff;
}

.sv-document-placeholder {
  padding: 4px;
  color: #64748b;
  font-size: 10px;
  font-weight: 700;
  text-align: center;
}

.sv-document-pdf {
  display: grid;
  place-items: center;
  width: 42px;
  height: 52px;
  border-radius: 8px;
  background: #fee2e2;
  color: #b91c1c;
  font-size: 12px;
  font-weight: 900;
}

.sv-remove-file {
  position: absolute;
  top: 4px;
  right: 4px;
  min-height: 20px;
  padding: 2px 6px;
  border-radius: 999px;
  background: rgba(220, 38, 38, 0.92);
  color: #ffffff;
  font-size: 10px;
  font-weight: 800;
  line-height: 1;
  z-index: 2;
}

.sv-remove-file:hover {
  transform: none;
  filter: brightness(0.95);
}

.sv-file-picker {
  display: inline-flex;
  width: fit-content;
  max-width: 100%;
  margin-top: 0;
}

.sv-file-picker input[type="file"] {
  display: none;
}

.sv-file-picker span {
  display: inline-flex;
  justify-content: center;
  align-items: center;
  min-width: 76px;
  width: auto;
  min-height: 24px;
  border-radius: 7px;
  padding: 3px 9px;
  background: #1d4ed8;
  color: #ffffff;
  font-size: 10px;
  font-weight: 800;
  cursor: pointer;
}

.sv-file-picker-small span {
  min-width: 96px;
}

.sv-file-meta {
  display: grid;
  gap: 1px;
  min-width: 0;
}

.sv-file-meta strong {
  max-width: 100%;
  color: #0f172a;
  font-size: 10px;
  font-weight: 700;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-file-meta small {
  min-height: 10px;
  color: #64748b;
  font-size: 9px;
}

.sv-other-files {
  display: grid;
  gap: 4px;
  margin-top: 4px;
  color: #334155;
  font-size: 10px;
  font-weight: 600;
}

.sv-other-files > span {
  padding: 5px 7px;
  border-radius: 8px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
}

.sv-other-file-item {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto auto;
  gap: 5px;
  align-items: center;
}

.sv-other-file-item strong {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sv-other-file-item small {
  color: #64748b;
  white-space: nowrap;
}

.sv-other-file-item button {
  min-height: 20px;
  padding: 2px 6px;
  border-radius: 999px;
  background: #dc2626;
  color: #ffffff;
  font-size: 10px;
}

.sv-other-files span {
  padding: 5px 7px;
  border-radius: 8px;
  background: #ffffff;
  border: 1px solid #e2e8f0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.sv-other-files small {
  color: #64748b;
}
.sv-photo-placeholder {
  width: 100%;
  height: 100%;
  display: grid;
  place-items: center;
  color: #1d4ed8;
  font-size: 13px;
  font-weight: 800;
}

.sv-file-list {
  display: grid;
  gap: 8px;
}

.sv-file-item {
  display: grid;
  gap: 5px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  padding: 8px;
  background: #ffffff;
}

.sv-file-label {
  color: #334155;
  font-size: 12px;
  font-weight: 800;
}

.sv-file-item input[type="file"] {
  min-height: 30px;
  padding: 4px;
  border-radius: 8px;
  font-size: 11px;
}

.sv-file-name {
  display: block;
  max-width: 100%;
  color: #0f172a;
  font-size: 11px;
  font-weight: 600;
  line-height: 1.25;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sv-file-item small {
  min-height: 12px;
  color: #64748b;
  font-size: 10px;
}

.sv-mini-preview {
  width: 100%;
  height: 72px;
  border-radius: 10px;
  overflow: hidden;
  background: #f1f5f9;
}

.sv-mini-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.sv-info-panel {
  display: grid;
  gap: 10px;
  min-width: 0;
}

.sv-section-box {
  border: 1px solid #e2e8f0;
  border-radius: 14px;
  padding: 12px;
  background: #ffffff;
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

.sv-form-table label {
  gap: 4px;
  color: #334155;
  font-size: 12px;
  font-weight: 700;
}

.sv-form-table input,
.sv-form-table select,
.sv-form-table textarea {
  min-height: 32px;
  border-radius: 8px;
  padding: 6px 8px;
  font-size: 12px;
}

.sv-form-table textarea {
  min-height: 58px;
}

.sv-col-span-3 {
  grid-column: span 3;
}

.sv-receive-actions {
  position: sticky;
  bottom: 0;
  z-index: 20;
  padding: 10px;
  border: 1px solid #dbeafe;
  border-radius: 14px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 -8px 20px rgba(15, 23, 42, 0.08);
}

@media (max-width: 1180px) {
  .sv-receive-layout {
    grid-template-columns: 1fr;
  }

  .sv-file-panel {
    position: static;
  }

  .sv-photo-preview-main {
    max-height: 220px;
    aspect-ratio: 16 / 9;
  }

  .sv-form-table-4 {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .sv-form-table-3,
  .sv-form-table-4 {
    grid-template-columns: 1fr;
  }

  .sv-col-span-3 {
    grid-column: span 1;
  }
}
</style>