<template>
  <section class="sv-flow-page">

    <!-- BREADCRUMB -->
    <nav class="sv-breadcrumb">
      <span
          v-for="(crumb, i) in breadcrumbs" :key="i"
          class="sv-crumb"
          :class="{ active: i === breadcrumbs.length - 1, clickable: i < breadcrumbs.length - 1 }"
          @click="i < breadcrumbs.length - 1 && quayVeBuoc(crumb.step)"
      >
        <span v-if="i > 0" class="sv-crumb-sep">›</span>
        {{ crumb.label }}
      </span>
    </nav>

    <div
        v-if="thongBao && thongBaoBuoc === buoc"
        class="sv-message"
        :class="thongBaoLoai"
    >
      {{ thongBao }}
    </div>

    <!-- ===== BƯỚC 1: NGÀNH ===== -->
    <template v-if="buoc === 'nganh'">
      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>Chọn ngành</h2>
            <p>Ngành được tạo tại module Đào tạo. Chọn ngành để xem chương trình.</p></div>
          <span class="so-ban-ghi">{{ danhSachNganh.length }} ngành</span>
        </div>
        <div class="sv-table-wrap">
          <table class="sv-table">
            <thead>
            <tr>
              <th>STT</th>
              <th>Mã ngành</th>
              <th>Tên ngành</th>
              <th>Ghi chú</th>
              <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(ng, i) in danhSachNganh" :key="ng.id">
              <td>{{ i + 1 }}</td>
              <td>{{ ng.maNganh }}</td>
              <td>{{ ng.tenNganh }}</td>
              <td>{{ ng.ghiChu }}</td>
              <td>
                <button type="button" class="small" @click="chonNganh(ng)">Chọn →</button>
              </td>
            </tr>
            <tr v-if="!danhSachNganh.length">
              <td colspan="5" class="empty">Chưa có ngành nào.</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </template>

    <!-- ===== BƯỚC 2: CHƯƠNG TRÌNH ===== -->
    <template v-if="buoc === 'chuongTrinh'">
      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>Chọn chương trình</h2>
            <p>Ngành: <strong>{{ nganhDangChon?.tenNganh }}</strong> — Chương trình được tạo tại module Đào tạo.</p>
          </div>
          <span class="so-ban-ghi">{{ chuongTrinhTheoNganh.length }} chương trình</span>
        </div>
        <div class="sv-table-wrap">
          <table class="sv-table">
            <thead>
            <tr>
              <th>STT</th>
              <th>Mã CT</th>
              <th>Tên chương trình</th>
              <th>Ghi chú</th>
              <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(ct, i) in chuongTrinhTheoNganh" :key="ct.id">
              <td>{{ i + 1 }}</td>
              <td>{{ ct.maChuongTrinh }}</td>
              <td>{{ ct.tenChuongTrinh }}</td>
              <td>{{ ct.ghiChu }}</td>
              <td>
                <button type="button" class="small" @click="chonChuongTrinh(ct)">Chọn →</button>
              </td>
            </tr>
            <tr v-if="!chuongTrinhTheoNganh.length">
              <td colspan="5" class="empty">Chưa có chương trình nào cho ngành này.</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </template>

    <!-- ===== BƯỚC 3: VERSION ===== -->
    <template v-if="buoc === 'version'">
      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>Chọn version chương trình</h2>
            <p>Chương trình: <strong>{{ chuongTrinhDangChon?.tenChuongTrinh }}</strong> — Version được tạo tại module
              Đào tạo.</p></div>
          <span class="so-ban-ghi">{{ versionTheoCT.length }} version</span>
        </div>
        <div class="sv-table-wrap">
          <table class="sv-table">
            <thead>
            <tr>
              <th>STT</th>
              <th>Mã version</th>
              <th>Tên version</th>
              <th>Hiện hành</th>
              <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(v, i) in versionTheoCT" :key="v.id">
              <td>{{ i + 1 }}</td>
              <td>{{ v.maVersion }}</td>
              <td>{{ v.tenVersion || '—' }}</td>
              <td>{{ v.laHienHanh ? '✓ Hiện hành' : '' }}</td>
              <td>
                <button type="button" class="small" @click="chonVersion(v)">Chọn →</button>
              </td>
            </tr>
            <tr v-if="!versionTheoCT.length">
              <td colspan="5" class="empty">Chưa có version nào cho chương trình này.</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </template>

    <!-- ===== BƯỚC 4: LỚP HÀNH CHÍNH ===== -->
    <template v-if="buoc === 'lopHanhChinh'">
      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>{{ idLHCSua ? 'Sửa lớp hành chính' : 'Tạo lớp hành chính' }}</h2>
            <p>Version: <strong>{{ versionDangChon?.maVersion }} - {{ versionDangChon?.tenVersion }}</strong></p></div>
        </div>
        <form class="sv-grid sv-grid-4" @submit.prevent="luuLopHanhChinh">
          <label>Mã lớp<input v-model.trim="formLHC.maLop" required/></label>
          <label>Tên lớp<input v-model.trim="formLHC.tenLop" required/></label>
          <label>Sĩ số tối đa<input v-model.number="formLHC.siSo" type="number" min="0"/></label>
          <label>Trạng thái
            <select v-model="formLHC.trangThai">
              <option value="du_kien">Dự kiến</option>
              <option value="dang_hoc">Đang học</option>
              <option value="tam_dung">Tạm dừng</option>
              <option value="da_tot_nghiep">Đã tốt nghiệp</option>
              <option value="huy">Hủy</option>
            </select>
          </label>
          <label class="sv-span-2">Ghi chú<input v-model.trim="formLHC.ghiChu"/></label>
          <div class="sv-actions sv-span-4">
            <button type="submit" :disabled="dangLuu">{{
                dangLuu ? 'Đang lưu...' : (idLHCSua ? 'Cập nhật' : 'Tạo lớp')
              }}
            </button>
            <button type="button" class="secondary" @click="resetFormLHC">Làm mới</button>
          </div>
        </form>
      </div>
      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>Danh sách lớp hành chính</h2>
            <p>Chọn lớp → Tiếp nhận sinh viên (Nhánh 1) hoặc Mở lớp học phần (Nhánh 2).</p></div>
          <span class="so-ban-ghi">{{ danhSachLHC.length }} lớp</span>
        </div>
        <div class="sv-table-wrap">
          <table class="sv-table">
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
            <tr v-for="(lop, i) in danhSachLHC" :key="lop.id">
              <td>{{ i + 1 }}</td>
              <td>{{ lop.maLop }}</td>
              <td>{{ lop.tenLop }}</td>
              <td>{{ lop.siSo || 0 }}</td>
              <td>{{ nhanTrangThaiLHC(lop.trangThai) }}</td>
              <td>{{ lop.ghiChu }}</td>
              <td>
                <div class="sv-row-actions">
                  <button type="button" class="small btn-nhanh1" @click="chonLHCNhanhTiepNhan(lop)">Tiếp nhận SV →
                  </button>
                  <button type="button" class="small btn-nhanh2" @click="chonLHCNhanhLHP(lop)">Mở LHP →</button>
                  <button type="button" class="secondary small" @click="suaLopHanhChinh(lop)">Sửa</button>
                  <button type="button" class="danger small" @click="xoaLopHanhChinh(lop)">Xóa</button>
                </div>
              </td>
            </tr>
            <tr v-if="!danhSachLHC.length">
              <td colspan="7" class="empty">Chưa có lớp hành chính theo version này.</td>
            </tr>
            </tbody>
          </table>
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
      <div class="sv-card">
        <div class="sv-card-title">
          <div>
            <h2>{{ idSVSua ? 'Cập nhật hồ sơ sinh viên' : 'Tiếp nhận hồ sơ sinh viên' }}</h2>
            <p>Lớp HC: <strong>{{ lopHanhChinhDangChon?.maLop }} - {{ lopHanhChinhDangChon?.tenLop }}</strong> | Hệ
              thống tự sinh mã SV và cấp tài khoản.</p>
          </div>
        </div>
        <form @submit.prevent="luuSinhVien">
          <div class="sv-grid sv-grid-3">
            <label>Họ tên<input v-model.trim="formSV.hoTen" required/></label>
            <label>Gmail<input v-model.trim="formSV.email" type="email" required/></label>
            <label>Số điện thoại<input v-model.trim="formSV.soDienThoai" required/></label>
            <label>Ngày sinh<input v-model="formSV.ngaySinh" type="date" required/></label>
            <label>Giới tính
              <select v-model="formSV.gioiTinh" required>
                <option value="">Chọn</option>
                <option value="nam">Nam</option>
                <option value="nu">Nữ</option>
                <option value="khac">Khác</option>
              </select>
            </label>
            <label>Ngày nhập học<input v-model="formSV.ngayNhapHoc" type="date"/></label>
          </div>
          <div class="sv-grid sv-grid-2" style="margin-top:10px">
            <label>Địa chỉ liên hệ<textarea v-model.trim="formSV.diaChi" rows="2" required></textarea></label>
            <label>Địa chỉ thường trú<textarea v-model.trim="formSV.diaChiThuongTru" rows="2"
                                               required></textarea></label>
          </div>
          <h3>CCCD &amp; Bằng cấp</h3>
          <div class="sv-grid sv-grid-3">
            <label>Số CCCD<input v-model.trim="formSV.soCccd" required/></label>
            <label>Ngày cấp<input v-model="formSV.ngayCapCccd" type="date" required/></label>
            <label>Nơi cấp<input v-model.trim="formSV.noiCapCccd" required/></label>
            <label>Bằng cấp<input v-model.trim="formSV.bangCap" required/></label>
            <label>Năm tốt nghiệp <span class="bat-buoc">*</span>
              <input v-model.number="formSV.namTotNghiep" type="number" min="1950" required/>
            </label>
            <label>Trường tốt nghiệp<input v-model.trim="formSV.truongTotNghiep" required/></label>
          </div>
          <h3>Thông tin cha</h3>
          <div class="sv-grid sv-grid-4">
            <label>Họ tên cha <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.hoTenCha" required/>
            </label>

            <label>Nghề nghiệp cha
              <input v-model.trim="formSV.ngheNghiepCha"/>
            </label>

            <label>Số điện thoại cha <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.sdtCha" required/>
            </label>

            <label>Gmail cha
              <input v-model.trim="formSV.emailCha" type="email" placeholder="Không bắt buộc"/>
            </label>
          </div>

          <h3>Thông tin mẹ</h3>
          <div class="sv-grid sv-grid-4">
            <label>Họ tên mẹ <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.hoTenMe" required/>
            </label>

            <label>Nghề nghiệp mẹ
              <input v-model.trim="formSV.ngheNghiepMe"/>
            </label>

            <label>Số điện thoại mẹ <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.sdtMe" required/>
            </label>

            <label>Gmail mẹ
              <input v-model.trim="formSV.emailMe" type="email" placeholder="Không bắt buộc"/>
            </label>
          </div>

          <h3>Người thân liên hệ</h3>
          <div class="sv-grid sv-grid-4">
            <label>Họ tên người thân <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.hoTenNguoiThan" required/>
            </label>

            <label>Quan hệ người thân <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.quanHeNguoiThan" required/>
            </label>

            <label>Số điện thoại người thân <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.sdtNguoiThan" required/>
            </label>

            <label>Gmail người thân <span class="bat-buoc">*</span>
              <input v-model.trim="formSV.emailNguoiThan" type="email" required/>
            </label>
          </div>
          <h3>Hình ảnh &amp; giấy tờ</h3>
          <div class="sv-grid sv-grid-4">
            <label>Ảnh chân dung<input type="file" accept="image/*" @change="chonTep($event,'anhChanDung')"/></label>
            <label>CCCD mặt trước<input type="file" accept="image/*,.pdf"
                                        @change="chonTep($event,'cccdTruoc')"/></label>
            <label>CCCD mặt sau<input type="file" accept="image/*,.pdf" @change="chonTep($event,'cccdSau')"/></label>
            <label>Bằng cấp<input type="file" accept="image/*,.pdf" @change="chonTep($event,'bangCap')"/></label>
          </div>
          <label style="margin-top:8px">Giấy tờ khác<input type="file" multiple @change="chonTepKhac"/></label>
          <label style="margin-top:8px">Ghi chú hồ sơ<textarea v-model.trim="formSV.ghiChuHoSo"
                                                               rows="2"></textarea></label>
          <div class="sv-actions" style="margin-top:12px">
            <button type="submit" :disabled="dangLuu">
              {{ dangLuu ? 'Đang lưu...' : (idSVSua ? 'Cập nhật sinh viên' : 'Tiếp nhận sinh viên') }}
            </button>
            <button type="button" class="secondary" @click="resetFormSV">Làm mới</button>
          </div>
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

      <div class="sv-card">
        <div class="sv-card-title">
          <div><h2>Danh sách lớp học phần theo version</h2>
            <p>Chọn lớp để phân bổ sinh viên.</p></div>
          <span class="so-ban-ghi">{{ danhSachLHPTheoVersion.length }} lớp</span>
        </div>
        <div class="sv-table-wrap">
          <table class="sv-table">
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
              <td>{{ lhp.loaiLopHocPhan === 'HOC_CHUNG' ? 'Học chung' : 'Chuyên ngành' }}</td>
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
                      :to="{ path: '/giang-day', query: { lopHocPhanId: lhp.id } }"
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
            <RouterLink class="btn-giangday" :to="{ path: '/giang-day' }">→ Sang module Giảng dạy (gán GV)</RouterLink>
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
// ─── COMPUTED ─────────────────────────────────────────────────────────────────
const breadcrumbs = computed(() => {
  const list = [{label: 'Ngành', step: 'nganh'}]
  if (nganhDangChon.value) list.push({label: nganhDangChon.value.tenNganh, step: 'chuongTrinh'})
  if (chuongTrinhDangChon.value) list.push({label: chuongTrinhDangChon.value.maChuongTrinh, step: 'version'})
  if (versionDangChon.value) list.push({label: versionDangChon.value.maVersion, step: 'lopHanhChinh'})
  if (lopHanhChinhDangChon.value) {
    if (buoc.value === 'tiepNhan') list.push({
      label: lopHanhChinhDangChon.value.maLop,
      step: 'lopHanhChinh'
    }, {label: 'Tiếp nhận SV', step: 'tiepNhan'})
    if (buoc.value === 'lopHocPhan') list.push({
      label: lopHanhChinhDangChon.value.maLop,
      step: 'lopHanhChinh'
    }, {label: 'Mở LHP', step: 'lopHocPhan'})
    if (buoc.value === 'phanBo') list.push({
      label: lopHanhChinhDangChon.value.maLop,
      step: 'lopHanhChinh'
    }, {label: 'Mở LHP', step: 'lopHocPhan'}, {label: 'Phân bổ SV', step: 'phanBo'})
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

  return danhSachLHP.value.filter(lhp =>
      chuongTrinhMonIdsTrongVersion.has(String(lhp.chuongTrinhMonId))
  )
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
      const [lhc, lhp] = await Promise.all([
        sinhVienService.layLopHanhChinhTheoVersion(versionDangChon.value.id),
        sinhVienService.layLopHocPhan()
      ])

      danhSachLHC.value = lhc
      danhSachLHP.value = lhp
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

// ─── BƯỚC 3: VERSION ──────────────────────────────────────────────────────────
async function chonVersion(v) {
  versionDangChon.value = v
  buoc.value = 'lopHanhChinh'
  xoaThongBao()
  try {
    const [lhc, lhp] = await Promise.all([
      sinhVienService.layLopHanhChinhTheoVersion(v.id),
      sinhVienService.layLopHocPhan()
    ])
    danhSachLHC.value = lhc
    danhSachLHP.value = lhp
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

  return mon?.tenMonHoc
      || mon?.monHocTen
      || mon?.tenMon
      || mon?.ghiChu
      || lhp?.tenMonHoc
      || lhp?.monHocTen
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

function lamSach(payload) {
  const r = {}
  Object.entries(payload).forEach(([k, v]) => {
    if (v === '' || v === undefined || v === null) return
    if (['id', 'createdAt', 'updatedAt'].includes(k)) return
    r[k] = v
  })
  return r
}

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
.sv-flow-page, .sv-flow-page * {
  font-family: inherit;
  letter-spacing: normal;
  box-sizing: border-box;
}

.sv-flow-page {
  display: grid;
  gap: 14px;
}

/* Breadcrumb */
.sv-breadcrumb {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  align-items: center;
  background: #eff6ff;
  border: 1px solid #bfdbfe;
  border-radius: 10px;
  padding: 10px 14px;
}

.sv-crumb {
  font-size: 13px;
  font-weight: 600;
  color: #1d4ed8;
}

.sv-crumb.active {
  color: #0f172a;
}

.sv-crumb.clickable {
  cursor: pointer;
  text-decoration: underline;
}

.sv-crumb-sep {
  margin: 0 4px;
  color: #94a3b8;
  font-weight: 400;
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
</style>