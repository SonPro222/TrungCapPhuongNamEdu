<template>
  <div class="syllabus-mon-hoc-mau-page">
    <section class="khoi-dau">
      <div>
        <h2>Quản lý syllabus môn học mẫu</h2>
        <p>Mỗi môn học mẫu chỉ có một syllabus mẫu. Chọn môn là hiển thị form cập nhật ngay, không qua danh sách syllabus trung gian.</p>
      </div>
      <button type="button" class="nut" :disabled="dangTai" @click="taiDuLieuNen">Tải lại dữ liệu</button>
    </section>

    <section class="luoi-trang">
      <aside class="the cot-mon">
        <div class="tieu-de-the compact">
          <h3>Danh sách môn học mẫu toàn trường</h3>
          <p>Chọn môn để cập nhật syllabus, chương/bài, điều kiện/đánh giá và tài liệu mẫu.</p>
        </div>
        <ThongBaoKhuVuc :state="thongBaoKhuVuc.mon" />
        <div class="bo-loc">
          <input v-model.trim="tuKhoaMon" type="text" placeholder="Tìm mã môn hoặc tên môn">
        </div>
        <div class="danh-sach-mon">
          <button
              v-for="mon in danhSachMonLoc"
              :key="mon.id"
              type="button"
              :class="['dong-mon', { 'dang-chon': String(mon.id || '') === String(monHocIdDangChon || '') }]"
              @click="chonMonHoc(mon)"
          >
            <b>{{ tenMonHoc(mon) }}</b>
            <small>{{ maMonHoc(mon) }} · ID {{ mon.id }}</small>
          </button>
          <div v-if="!danhSachMonLoc.length" class="rong">Không có môn học mẫu phù hợp.</div>
        </div>
      </aside>

      <main class="noi-dung-chinh">
        <section v-if="!monHocDangChon" class="hop-rong">Chọn một môn học mẫu để cập nhật syllabus mẫu.</section>

        <template v-else>
          <section class="thanh-mon">
            <div>
              <b>Môn học mẫu đang chọn:</b>
              <span>{{ maMonHoc(monHocDangChon) }} - {{ tenMonHoc(monHocDangChon) }}</span>
            </div>
            <div class="badge">{{ formSyllabus.id ? 'Đã có syllabus mẫu' : 'Chưa có syllabus mẫu' }}</div>
          </section>

          <section class="luoi-chinh">
            <div class="the khoi-syllabus">
              <div class="tieu-de-the">
                <h3>{{ formSyllabus.id ? 'Cập nhật syllabus mẫu' : 'Tạo syllabus mẫu' }}</h3>
                <p>Syllabus mẫu luôn thuộc đúng môn học mẫu đang chọn.</p>
              </div>
              <ThongBaoKhuVuc :state="thongBaoKhuVuc.syllabus" />

              <form class="form-grid form-syllabus" @submit.prevent="luuSyllabus">
                <label>
                  <span>Mã syllabus</span>
                  <input v-model.trim="formSyllabus.ma" type="text" placeholder="VD: SYL-mau-MH28-BTSCMB-2026">
                </label>
                <label>
                  <span>Tên syllabus <b>*</b></span>
                  <input v-model.trim="formSyllabus.ten" type="text" placeholder="VD: Syllabus mẫu - An toàn an ninh dân dụng">
                </label>
                <label class="cot-rong">
                  <span>Vị trí</span>
                  <textarea v-model.trim="formSyllabus.viTri" rows="3" placeholder="Vị trí của môn học trong chương trình"></textarea>
                </label>
                <label>
                  <span>Tính chất</span>
                  <input v-model.trim="formSyllabus.tinhChat" type="text" placeholder="VD: Bắt buộc / Tự chọn">
                </label>
                <label>
                  <span>Số buổi học <b>*</b></span>
                  <input v-model="formSyllabus.soBuoiHoc" type="number" min="1" step="1">
                </label>
                <label>
                  <span>Số tín chỉ</span>
                  <input v-model="formSyllabus.soTinChi" type="number" min="0" step="0.1">
                </label>
                <label>
                  <span>Số tiết mỗi buổi <b>*</b></span>
                  <input v-model="formSyllabus.soTietMoiBuoi" type="number" min="0.1" step="0.1">
                </label>
                <div class="cot-rong tong-hop-syllabus">
                  <div class="tieu-de-tong-hop">Tổng hợp giờ từ chương/bài mẫu - không nhập tay</div>
                  <label>
                    <span>Tổng giờ</span>
                    <input :value="hienThiSo(tongHopGioSyllabusmau.tongGio)" type="text" readonly>
                  </label>
                  <label>
                    <span>LT</span>
                    <input :value="hienThiSo(tongHopGioSyllabusmau.gioLyThuyet)" type="text" readonly>
                  </label>
                  <label>
                    <span>TH</span>
                    <input :value="hienThiSo(tongHopGioSyllabusmau.gioThucHanh)" type="text" readonly>
                  </label>
                  <label>
                    <span>KT</span>
                    <input :value="hienThiSo(tongHopGioSyllabusmau.gioKiemTra)" type="text" readonly>
                  </label>
                </div>
                <label>
                  <span>Số phút một tiết <b>*</b></span>
                  <input v-model="formSyllabus.soPhutMotTiet" type="number" min="1" step="1">
                </label>
                <label>
                  <span>Điểm đạt tối thiểu</span>
                  <input v-model="formSyllabus.diemDatToiThieu" type="number" min="0" step="0.1">
                </label>
                <label>
                  <span>Đơn vị điểm</span>
                  <select v-model="formSyllabus.donViDiem">
                    <option value="">-- Không chọn --</option>
                    <option value="thang_10">Thang 10</option>
                    <option value="phan_tram">Phần trăm (%)</option>
                  </select>
                </label>
                <label>
                  <span>Tỷ lệ chuyên cần tối thiểu (%)</span>
                  <input v-model="formSyllabus.tyLeChuyenCanToiThieu" type="number" min="0" max="100" step="0.1">
                </label>
                <label class="check-line">
                  <input v-model="formSyllabus.batBuocDuThi" type="checkbox">
                  <span>Bắt buộc đủ điều kiện dự thi</span>
                </label>
                <label class="cot-rong">
                  <span>Mục tiêu</span>
                  <textarea v-model.trim="formSyllabus.mucTieu" rows="8"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Phương pháp đánh giá</span>
                  <textarea v-model.trim="formSyllabus.phuongPhapDanhGia" rows="7"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Điều kiện hoàn thành</span>
                  <textarea v-model.trim="formSyllabus.dieuKienHoanThanh" rows="3"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Hướng dẫn</span>
                  <textarea v-model.trim="formSyllabus.huongDan" rows="3"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Công thức quy đổi</span>
                  <textarea v-model.trim="formSyllabus.congThucQuyDoi" rows="3"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Ghi chú</span>
                  <textarea v-model.trim="formSyllabus.ghiChu" rows="2" placeholder="VD: Syllabus mẫu nhập từ tài liệu chương trình đào tạo năm 2026."></textarea>
                </label>
                <div class="hang-nut cot-rong">
                  <button type="submit" class="nut chinh" :disabled="dangLuu">{{ formSyllabus.id ? 'Cập nhật syllabus' : 'Lưu syllabus' }}</button>
                  <button type="button" class="nut" :disabled="dangLuu" @click="lamMoiFormSyllabus">Làm mới form</button>
                  <button v-if="formSyllabus.id" type="button" class="nut nguy-hiem" :disabled="dangLuu" @click="xoaSyllabusDangMo">Xóa syllabus</button>
                </div>
              </form>
            </div>

            <div class="the khoi-chuong-bai">
              <div class="tieu-de-the">
                <h3>{{ formChuongBai.id ? 'Cập nhật chương/bài mẫu' : 'Thêm chương/bài mẫu' }}</h3>
                <p>Chương/bài thuộc syllabus mẫu của môn đang chọn.</p>
              </div>
              <ThongBaoKhuVuc :state="thongBaoKhuVuc.chuongBai" />

              <div v-if="!syllabusDangChon" class="canh-bao-nho">Lưu syllabus mẫu trước khi thêm chương/bài.</div>
              <form class="form-grid form-nho" @submit.prevent="luuChuongBai">
                <label>
                  <span>Mã chương</span>
                  <input v-model.trim="formChuongBai.maChuong" type="text" placeholder="VD: CH01">
                </label>
                <label>
                  <span>Tên chương/bài <b>*</b></span>
                  <input v-model.trim="formChuongBai.ten" type="text" placeholder="VD: Chương 1: Các định nghĩa và khái niệm">
                </label>
                <label>
                  <span>TT</span>
                  <input v-model="formChuongBai.thuTu" type="number" min="0">
                </label>
                <label>
                  <span>Tổng giờ</span>
                  <input v-model="formChuongBai.tongGio" type="number" min="0" step="0.1">
                </label>
                <label>
                  <span>LT</span>
                  <input v-model="formChuongBai.gioLyThuyet" type="number" min="0" step="0.1">
                </label>
                <label>
                  <span>TH</span>
                  <input v-model="formChuongBai.gioThucHanh" type="number" min="0" step="0.1">
                </label>
                <label>
                  <span>KT</span>
                  <input v-model="formChuongBai.gioKiemTra" type="number" min="0" step="0.1">
                </label>
                <label class="cot-rong">
                  <span>Mục tiêu chương/bài</span>
                  <textarea v-model.trim="formChuongBai.mucTieu" rows="2"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Nội dung</span>
                  <textarea v-model.trim="formChuongBai.noiDung" rows="2"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Ghi chú</span>
                  <textarea v-model.trim="formChuongBai.ghiChu" rows="2"></textarea>
                </label>
                <div class="hang-nut cot-rong">
                  <button type="submit" class="nut chinh" :disabled="dangLuu || !syllabusDangChon">{{ formChuongBai.id ? 'Cập nhật' : 'Thêm chương/bài' }}</button>
                  <button type="button" class="nut" :disabled="dangLuu" @click="resetFormChuongBai">Làm mới</button>
                </div>
              </form>

              <div class="bang-boc">
                <table class="bang-du-lieu bang-chuong-bai">
                  <thead>
                  <tr>
                    <th>TT</th>
                    <th>Mã chương</th>
                    <th>Tên chương/bài</th>
                    <th>Mục tiêu</th>
                    <th>Tổng giờ</th>
                    <th>LT</th>
                    <th>TH</th>
                    <th>KT</th>
                    <th>Nội dung</th>
                    <th>Ghi chú</th>
                    <th>Thao tác</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-if="!danhSachChuongBai.length"><td colspan="11" class="rong">Chưa có chương/bài mẫu.</td></tr>
                  <tr v-for="row in danhSachChuongBai" :key="row.id">
                    <td class="cot-so">{{ hienThi(row.thuTu) }}</td>
                    <td>{{ row.maChuong || '-' }}</td>
                    <td>{{ row.ten || '-' }}</td>
                    <td class="cot-noi-dung">{{ hienThi(row.mucTieu) }}</td>
                    <td class="cot-so">{{ hienThi(row.tongGio) }}</td>
                    <td class="cot-so">{{ hienThi(row.gioLyThuyet) }}</td>
                    <td class="cot-so">{{ hienThi(row.gioThucHanh) }}</td>
                    <td class="cot-so">{{ hienThi(row.gioKiemTra) }}</td>
                    <td class="cot-noi-dung">{{ hienThi(row.noiDung) }}</td>
                    <td class="cot-ghi-chu-bang">{{ hienThi(row.ghiChu) }}</td>
                    <td class="thao-tac">
                      <button type="button" class="nut nho" @click="suaChuongBai(row)">Sửa</button>
                      <button type="button" class="nut nho nguy-hiem" @click="xoaChuongBai(row)">Xóa</button>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </div>


            <div class="the khoi-danh-gia-mau">
              <div class="tieu-de-the">
                <h3>{{ formCauHinhDanhGiaMau.id ? 'Cập nhật cấu hình đánh giá mẫu' : 'Thêm cấu hình đánh giá mẫu' }}</h3>
                <p>Cấu hình đánh giá mẫu thuộc syllabus môn học mẫu đang chọn. Khi syllabus mẫu được áp dụng vào môn trong chương trình, các cột điểm mẫu này dùng để tạo/copy cấu hình đánh giá thật.</p>
              </div>
              <ThongBaoKhuVuc :state="thongBaoKhuVuc.cauHinhDanhGiaMau" />

              <div v-if="!syllabusDangChon" class="canh-bao-nho">Lưu syllabus mẫu trước khi thêm cấu hình đánh giá mẫu.</div>
              <div v-else class="dong-thong-tin-danh-gia">
                <span>Đang lấy dữ liệu từ bảng <b>cau_hinh_danh_gia_mau</b></span>
                <span>Syllabus mẫu ID: <b>{{ syllabusIdDangChon }}</b></span>
                <span>Số dòng: <b>{{ danhSachCauHinhDanhGiaMau.length }}</b></span>
              </div>
              <form class="form-grid form-nho form-danh-gia-mau" @submit.prevent="luuCauHinhDanhGiaMau">
                <label>
                  <span>ID</span>
                  <input :value="formCauHinhDanhGiaMau.id || 'Tự động'" type="text" readonly>
                </label>
                <label>
                  <span>Syllabus mẫu ID</span>
                  <input :value="syllabusIdDangChon || ''" type="text" readonly>
                </label>
                <label>
                  <span>Tên cột điểm <b>*</b></span>
                  <input v-model.trim="formCauHinhDanhGiaMau.tenCotDiem" type="text" placeholder="VD: Điểm chuyên cần">
                </label>
                <label>
                  <span>Loại điểm</span>
                  <select v-model="formCauHinhDanhGiaMau.loaiDiem">
                    <option value="">-- Không chọn --</option>
                    <option value="chuyen_can">Chuyên cần</option>
                    <option value="bai_tap">Bài tập</option>
                    <option value="bai_tap_online">Bài tập online</option>
                    <option value="kiem_tra_online">Kiểm tra online</option>
                    <option value="kiem_tra_tren_lop">Kiểm tra trên lớp</option>
                    <option value="giua_ky">Giữa kỳ</option>
                    <option value="cuoi_ky">Cuối kỳ</option>
                    <option value="thuc_hanh">Thực hành</option>
                    <option value="do_an">Đồ án</option>
                    <option value="khac">Khác</option>
                  </select>
                </label>
                <label>
                  <span>Tỷ lệ % <b>*</b></span>
                  <input v-model="formCauHinhDanhGiaMau.tyLe" type="number" min="0" max="100" step="0.01">
                </label>
                <label>
                  <span>Điểm tối đa</span>
                  <input v-model="formCauHinhDanhGiaMau.diemToiDa" type="number" min="0.01" step="0.01" placeholder="10">
                </label>
                <label>
                  <span>Thứ tự</span>
                  <input v-model="formCauHinhDanhGiaMau.thuTu" type="number" min="0" step="1">
                </label>
                <label class="check-line compact-check">
                  <input v-model="formCauHinhDanhGiaMau.batBuoc" type="checkbox">
                  <span>Bắt buộc</span>
                </label>
                <label class="cot-rong">
                  <span>Ghi chú</span>
                  <textarea v-model.trim="formCauHinhDanhGiaMau.ghiChu" rows="2"></textarea>
                </label>
                <div class="hang-nut cot-rong">
                  <button type="submit" class="nut chinh" :disabled="dangLuu || !syllabusDangChon">{{ formCauHinhDanhGiaMau.id ? 'Cập nhật cấu hình đánh giá mẫu' : 'Thêm cấu hình đánh giá mẫu' }}</button>
                  <button type="button" class="nut" :disabled="dangLuu" @click="resetFormCauHinhDanhGiaMau">Làm mới</button>
                </div>
              </form>

              <div class="tong-ty-le-danh-gia">
                Tổng tỷ lệ cấu hình đánh giá mẫu: <b>{{ hienThiSo(tongTyLeCauHinhDanhGiaMau) }}%</b>
                <span v-if="tongTyLeCauHinhDanhGiaMau > 100" class="canh-bao-inline">Vượt 100%, DB sẽ không cho lưu.</span>
              </div>

              <BangDonGian
                  :rows="danhSachCauHinhDanhGiaMauHienThi"
                  :columns="cotCauHinhDanhGiaMau"
                  empty-text="Chưa có cấu hình đánh giá mẫu cho syllabus mẫu này."
                  @edit="suaCauHinhDanhGiaMau"
                  @delete="xoaCauHinhDanhGiaMau"
              />
            </div>


            <div class="the khoi-quy-doi">
              <div class="tieu-de-the">
                <h3>{{ formQuyDoiDiemMau.id ? 'Cập nhật quy đổi điểm mẫu' : 'Thêm quy đổi điểm mẫu' }}</h3>
                <p>Quy đổi điểm mẫu thuộc syllabus mẫu đang chọn. Khi gán syllabus mẫu vào môn trong chương trình, mẫu này dùng để tái sử dụng/copy sang bảng quy đổi điểm thật.</p>
              </div>
              <ThongBaoKhuVuc :state="thongBaoKhuVuc.quyDoiDiemMau" />

              <div v-if="!syllabusDangChon" class="canh-bao-nho">Lưu syllabus mẫu trước khi thêm quy đổi điểm mẫu.</div>
              <form class="form-grid form-nho form-quy-doi" @submit.prevent="luuQuyDoiDiemMau">
                <label>
                  <span>Mã <b>*</b></span>
                  <input v-model.trim="formQuyDoiDiemMau.ma" type="text" placeholder="VD: QDD-DAT">
                </label>
                <label>
                  <span>Tên <b>*</b></span>
                  <input v-model.trim="formQuyDoiDiemMau.ten" type="text" placeholder="VD: Đạt">
                </label>
                <label>
                  <span>Loại mẫu</span>
                  <input v-model.trim="formQuyDoiDiemMau.loaiMau" type="text" readonly>
                </label>
                <label>
                  <span>Ngưỡng từ</span>
                  <input v-model="formQuyDoiDiemMau.nguongTu" type="number" min="0" step="0.01">
                </label>
                <label>
                  <span>Ngưỡng đến</span>
                  <input v-model="formQuyDoiDiemMau.nguongDen" type="number" min="0" step="0.01">
                </label>
                <label>
                  <span>Điểm quy đổi</span>
                  <input v-model="formQuyDoiDiemMau.diemQuyDoi" type="number" min="0" step="0.01">
                </label>
                <label>
                  <span>Kết quả</span>
                  <select v-model="formQuyDoiDiemMau.ketQua">
                    <option value="">-- Không chọn --</option>
                    <option value="dat">Đạt</option>
                    <option value="khong_dat">Không đạt</option>
                  </select>
                </label>
                <label>
                  <span>Tỷ lệ %</span>
                  <input v-model="formQuyDoiDiemMau.tyLe" type="number" min="0" max="100" step="0.01">
                </label>
                <label>
                  <span>Điểm tối đa</span>
                  <input v-model="formQuyDoiDiemMau.diemToiDa" type="number" min="0" step="0.01">
                </label>
                <label>
                  <span>Thứ tự</span>
                  <input v-model="formQuyDoiDiemMau.thuTu" type="number" min="0" step="1">
                </label>
                <label class="check-line compact-check">
                  <input v-model="formQuyDoiDiemMau.batBuoc" type="checkbox">
                  <span>Bắt buộc</span>
                </label>
                <label class="cot-rong">
                  <span>Công thức</span>
                  <textarea v-model.trim="formQuyDoiDiemMau.congThuc" rows="2" placeholder="VD: điểm >= 5 thì Đạt"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Ghi chú</span>
                  <textarea v-model.trim="formQuyDoiDiemMau.ghiChu" rows="2"></textarea>
                </label>
                <div class="hang-nut cot-rong">
                  <button type="submit" class="nut chinh" :disabled="dangLuu || !syllabusDangChon">{{ formQuyDoiDiemMau.id ? 'Cập nhật quy đổi điểm mẫu' : 'Thêm quy đổi điểm mẫu' }}</button>
                  <button type="button" class="nut" :disabled="dangLuu" @click="resetFormQuyDoiDiemMau">Làm mới</button>
                </div>
              </form>

              <BangDonGian
                  :rows="danhSachQuyDoiDiemMauHienThi"
                  :columns="cotQuyDoiDiemMau"
                  empty-text="Chưa có quy đổi điểm mẫu cho syllabus mẫu này."
                  @edit="suaQuyDoiDiemMau"
                  @delete="xoaQuyDoiDiemMau"
              />
            </div>


            <div class="the khoi-tai-lieu">
              <div class="tieu-de-the"><h3>Tài liệu mẫu của syllabus</h3></div>
              <ThongBaoKhuVuc :state="thongBaoKhuVuc.taiLieu" />
              <div v-if="!syllabusDangChon" class="canh-bao-nho">Lưu syllabus mẫu trước khi thêm tài liệu mẫu của syllabus.</div>

              <form class="form-grid form-nho" @submit.prevent="luuTaiLieumau">
                <label><span>Mã tài liệu</span><input v-model.trim="formTaiLieumau.ma" type="text" placeholder="Bỏ trống sẽ tự sinh mã"></label>
                <label><span>Tên tài liệu <b>*</b></span><input v-model.trim="formTaiLieumau.ten" type="text"></label>
                <label><span>Loại</span><input v-model.trim="formTaiLieumau.loai" type="text"></label>
                <label><span>Tác giả</span><input v-model.trim="formTaiLieumau.tacGia" type="text"></label>
                <label><span>Nhà xuất bản</span><input v-model.trim="formTaiLieumau.nhaXuatBan" type="text"></label>
                <label><span>Năm xuất bản</span><input v-model="formTaiLieumau.namXuatBan" type="number" min="1900" max="2100"></label>
                <label class="cot-rong">
                  <span>Đường dẫn đã lưu / danh sách file</span>
                  <textarea
                      v-model.trim="formTaiLieumau.duongDan"
                      rows="3"
                      readonly
                      placeholder="Tự sinh sau khi chọn file/thư mục và upload qua API, không nhập tay."
                  ></textarea>
                </label>
                <label>
                  <span>Thứ tự</span>
                  <input v-model="formGanTaiLieu.thuTu" type="number" min="0">
                </label>
                <label class="check-line compact-check">
                  <input v-model="formGanTaiLieu.batBuoc" type="checkbox">
                  <span>Bắt buộc</span>
                </label>
                <label class="cot-rong">
                  <span>Ghi chú tài liệu</span>
                  <textarea v-model.trim="formTaiLieumau.ghiChu" rows="2"></textarea>
                </label>
                <label class="cot-rong">
                  <span>Ghi chú gán trong syllabus mẫu</span>
                  <input v-model.trim="formGanTaiLieu.ghiChu" type="text" placeholder="Ghi chú riêng khi tài liệu nằm trong syllabus mẫu này">
                </label>
                <input ref="taiLieuFileInput" class="input-file-hidden" type="file" multiple accept=".doc,.docx,.pdf,.xls,.xlsx,.ppt,.pptx,.txt,.png,.jpg,.jpeg" @change="xuLyChonDuongDanTaiLieu">
                <input ref="taiLieuFolderInput" class="input-file-hidden" type="file" multiple webkitdirectory directory @change="xuLyChonDuongDanTaiLieu">
                <div class="hang-nut cot-rong">
                  <button type="submit" class="nut" :disabled="dangLuu">{{ formGanTaiLieu.id ? 'Cập nhật tài liệu mẫu của syllabus' : 'Thêm tài liệu mẫu vào syllabus' }}</button>
                  <button type="button" class="nut" @click="chonTepTaiLieu">Chọn nhiều file</button>
                  <button type="button" class="nut" @click="chonThuMucTaiLieu">Chọn thư mục</button>
                  <button type="button" class="nut" :disabled="!formTaiLieumau.duongDan" @click="moDuongDan(formTaiLieumau.duongDan)">Mở đường dẫn</button>
                  <button type="button" class="nut" @click="resetFormTaiLieumau">Làm mới</button>
                </div>
              </form>

              <BangDonGian :rows="danhSachGanTaiLieuHienThi" :columns="cotTaiLieu" empty-text="Chưa gán tài liệu mẫu." @edit="suaGanTaiLieu" @delete="xoaGanTaiLieu" />
            </div>

            <div class="the khoi-dieu-kien">
              <div class="tieu-de-the"><h3>Điều kiện mẫu của syllabus</h3><p>Dữ liệu này có trong DB: điều kiện phòng học, thiết bị, học liệu, dụng cụ, nguyên vật liệu hoặc điều kiện khác. Đánh giá điểm nằm ở form syllabus phía trên.</p></div>
              <ThongBaoKhuVuc :state="thongBaoKhuVuc.dieuKien" />
              <div v-if="!syllabusDangChon" class="canh-bao-nho">Lưu syllabus mẫu trước khi gán điều kiện.</div>

              <form class="form-grid form-nho" @submit.prevent="luuDieuKienmau">
                <label>
                  <span>Mã điều kiện</span>
                  <input v-model.trim="formDieuKienmau.ma" type="text">
                </label>

                <label>
                  <span>Loại <b>*</b></span>
                  <select v-model="formDieuKienmau.loai">
                    <option value="phong_hoc">Phòng học</option>
                    <option value="thiet_bi">Thiết bị</option>
                    <option value="hoc_lieu">Học liệu</option>
                    <option value="dung_cu">Dụng cụ</option>
                    <option value="nguyen_vat_lieu">Nguyên vật liệu</option>
                    <option value="khac">Khác</option>
                  </select>
                </label>

                <label>
                  <span>Thứ tự</span>
                  <input v-model="formGanDieuKien.thuTu" type="number" min="0">
                </label>

                <label class="cot-rong">
                  <span>Nội dung <b>*</b></span>
                  <textarea v-model.trim="formDieuKienmau.noiDung" rows="2"></textarea>
                </label>

                <label class="cot-rong">
                  <span>Ghi chú điều kiện</span>
                  <textarea v-model.trim="formDieuKienmau.ghiChu" rows="2"></textarea>
                </label>

                <label class="cot-rong">
                  <span>Ghi chú trong syllabus mẫu</span>
                  <input v-model.trim="formGanDieuKien.ghiChu" type="text">
                </label>

                <div class="hang-nut cot-rong">
                  <button type="submit" class="nut chinh" :disabled="dangLuu || !syllabusDangChon">
                    {{ formGanDieuKien.id ? 'Cập nhật điều kiện mẫu của syllabus' : 'Lưu điều kiện mẫu của syllabus' }}
                  </button>
                  <button type="button" class="nut" @click="resetFormDieuKienmau">Làm mới</button>
                </div>
              </form>

              <!--              <form class="form-grid form-gan" @submit.prevent="luuGanDieuKien">-->
              <!--                <label class="cot-rong">-->
              <!--                  <span>Gán điều kiện mẫu vào syllabus</span>-->
              <!--                  <select v-model="formGanDieuKien.dieuKienmauId">-->
              <!--                    <option value="">&#45;&#45; Chọn điều kiện &#45;&#45;</option>-->
              <!--                    <option v-for="dieuKien in danhSachDieuKienmau" :key="dieuKien.id" :value="dieuKien.id">{{ dieuKien.ma || dieuKien.id }} - {{ dieuKien.noiDung }}</option>-->
              <!--                  </select>-->
              <!--                </label>-->
              <!--                <label><span>TT</span><input v-model="formGanDieuKien.thuTu" type="number" min="0"></label>-->
              <!--                <label><span>Ghi chú</span><input v-model.trim="formGanDieuKien.ghiChu" type="text"></label>-->
              <!--                <div class="hang-nut cot-rong">-->
              <!--                  <button type="submit" class="nut chinh" :disabled="dangLuu || !syllabusDangChon">{{ formGanDieuKien.id ? 'Cập nhật gán' : 'Gán điều kiện' }}</button>-->
              <!--                  <button type="button" class="nut" @click="resetFormGanDieuKien">Làm mới gán</button>-->
              <!--                </div>-->
              <!--              </form>-->

              <BangDonGian
                  :rows="danhSachGanDieuKienHienThi"
                  :columns="cotDieuKien"
                  empty-text="Chưa có điều kiện mẫu của syllabus."
                  @edit="suaGanDieuKien"
                  @delete="xoaGanDieuKien"
              />
            </div>

          </section>
        </template>
      </main>
    </section>
  </div>
</template>

<script setup>
import { computed, defineComponent, h, onMounted, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { daoTaoService as services } from '@/modules/daoTao/services/daoTaoService'
import { layThongBaoLoi } from '@/modules/daoTao/utils/layThongBaoLoi'
import { tepDinhKemUploadService } from '@/modules/daoTao/services/tepDinhKemUploadService'

const ThongBaoKhuVuc = defineComponent({
  name: 'ThongBaoKhuVuc',
  props: {
    state: { type: Object, required: true }
  },
  setup(props) {
    return () => h('div', { class: ['thong-bao-khu-vuc', props.state?.type || 'info'] }, props.state?.message || 'Chưa có thông báo.')
  }
})

const BangDonGian = defineComponent({
  name: 'BangDonGian',
  props: {
    rows: { type: Array, default: () => [] },
    columns: { type: Array, default: () => [] },
    emptyText: { type: String, default: 'Chưa có dữ liệu.' }
  },
  emits: ['edit', 'delete'],
  setup(props, { emit }) {
    function renderNoiDungO(col, row) {
      const value = col.render ? col.render(row) : (row[col.key] ?? '-')
      if (Array.isArray(value) || (value && typeof value === 'object')) return value
      return String(value ?? '-')
    }

    return () => h('div', { class: 'bang-boc bang-con' }, [
      h('table', { class: 'bang-du-lieu' }, [
        h('thead', [h('tr', [...props.columns.map((col) => h('th', { key: col.key, class: col.class || '' }, col.label)), h('th', { class: 'cot-thao-tac' }, 'Thao tác')])]),
        h('tbody', [
          !props.rows.length
              ? h('tr', [h('td', { class: 'rong', colspan: props.columns.length + 1 }, props.emptyText)])
              : props.rows.map((row) => h('tr', { key: row.id }, [
                ...props.columns.map((col) => h('td', { key: col.key, class: col.class || '' }, renderNoiDungO(col, row))),
                h('td', { class: 'thao-tac' }, [
                  h('button', { type: 'button', class: 'nut nho nut-sua', onClick: () => emit('edit', row) }, 'Sửa'),
                  h('button', { type: 'button', class: 'nut nho nguy-hiem nut-xoa', onClick: () => emit('delete', row) }, 'Xóa')
                ])
              ]))
        ])
      ])
    ])
  }
})

const thongBaoKhuVuc = reactive({
  mon: { message: 'Chọn môn học mẫu để bắt đầu.', type: 'info' },
  syllabus: { message: 'Form syllabus sẵn sàng.', type: 'info' },
  chuongBai: { message: 'Chương/bài mẫu sẵn sàng.', type: 'info' },
  cauHinhDanhGiaMau: { message: 'Cấu hình đánh giá mẫu của syllabus mẫu sẵn sàng.', type: 'info' },
  quyDoiDiemMau: { message: 'Quy đổi điểm mẫu của syllabus mẫu sẵn sàng.', type: 'info' },
  taiLieu: { message: 'Tài liệu mẫu của syllabus sẵn sàng.', type: 'info' },
  dieuKien: { message: 'Điều kiện mẫu sẵn sàng.', type: 'info' }
})
const router = useRouter()
const dangTai = ref(false)
const dangLuu = ref(false)
const tuKhoaMon = ref('')
const taiLieuFileInput = ref(null)
const taiLieuFolderInput = ref(null)
const monHocIdDangChon = ref(null)
const syllabusIdDangChon = ref(null)
const syllabusTrungTheoMon = ref([])

const danhSachMonHocmau = ref([])
const danhSachSyllabus = ref([])
const danhSachChuongBai = ref([])
const danhSachCauHinhDanhGiaMau = ref([])
const danhSachQuyDoiDiemMau = ref([])
const danhSachTaiLieumau = ref([])
const danhSachGanTaiLieu = ref([])
const danhSachDieuKienmau = ref([])
const danhSachGanDieuKien = ref([])

const formSyllabus = reactive({ id: null, ma: '', ten: '', viTri: '', tinhChat: '', mucTieu: '', phuongPhapDanhGia: '', dieuKienHoanThanh: '', huongDan: '', diemDatToiThieu: '', donViDiem: '', tyLeChuyenCanToiThieu: '', batBuocDuThi: false, congThucQuyDoi: '', ghiChu: '' })
const formChuongBai = reactive({ id: null, maChuong: '', ten: '', mucTieu: '', noiDung: '', thuTu: '', tongGio: '', gioLyThuyet: '', gioThucHanh: '', gioKiemTra: '', ghiChu: '' })
const formCauHinhDanhGiaMau = reactive({ id: null, syllabusMonHocMauId: '', tenCotDiem: '', loaiDiem: '', tyLe: '', diemToiDa: '', thuTu: '', batBuoc: true, ghiChu: '' })
const formQuyDoiDiemMau = reactive({ id: null, syllabusMonHocMauId: '', ma: '', ten: '', nguongTu: '', nguongDen: '', diemQuyDoi: '', ketQua: '', congThuc: '', ghiChu: '', loaiMau: 'QUY_DOI_KET_QUA', tyLe: '', diemToiDa: '', thuTu: '', batBuoc: true })
const formTaiLieumau = reactive({ id: null, ma: '', ten: '', loai: '', tacGia: '', nhaXuatBan: '', namXuatBan: '', duongDan: '', ghiChu: '' })
const formGanTaiLieu = reactive({ id: null, taiLieumauId: '', thuTu: '', batBuoc: false, ghiChu: '' })
const formDieuKienmau = reactive({ id: null, ma: '', loai: 'khac', noiDung: '', ghiChu: '' })
const formGanDieuKien = reactive({ id: null, dieuKienmauId: '', thuTu: '', ghiChu: '' })

const cotTaiLieu = [
  { key: 'thuTu', label: 'TT', class: 'cot-tt cot-can-giua' },
  { key: 'maTaiLieu', label: 'Mã', class: 'cot-ma-tai-lieu' },
  { key: 'tenTaiLieu', label: 'Tên tài liệu', class: 'cot-tai-lieu-ten' },
  { key: 'loaiTaiLieu', label: 'Loại', class: 'cot-loai-tai-lieu cot-can-giua' },
  { key: 'tacGiaTaiLieu', label: 'Tác giả', class: 'cot-tac-gia' },
  { key: 'nhaXuatBanTaiLieu', label: 'NXB', class: 'cot-nxb' },
  { key: 'namXuatBanTaiLieu', label: 'Năm', class: 'cot-nam-xb cot-can-giua' },
  { key: 'batBuocText', label: 'Bắt buộc', class: 'cot-bat-buoc cot-can-giua' },
  { key: 'xemFileText', label: 'Xem file', class: 'cot-xem-file cot-can-giua' },
  { key: 'duongDanText', label: 'Mở đọc', class: 'cot-mo-doc cot-can-giua' },
  { key: 'ghiChuTaiLieu', label: 'Ghi chú TL', class: 'cot-ghi-chu' },
  { key: 'ghiChuGan', label: 'Ghi chú gán', class: 'cot-ghi-chu' }
]
const cotDieuKien = [
  { key: 'thuTu', label: 'TT', class: 'cot-tt cot-can-giua' },
  { key: 'maDieuKien', label: 'Mã điều kiện', class: 'cot-dieu-kien-ma' },
  { key: 'noiDungDieuKien', label: 'Điều kiện mẫu', class: 'cot-dieu-kien-noi-dung' },
  { key: 'loaiDieuKien', label: 'Loại', class: 'cot-dieu-kien-loai cot-can-giua' },
  { key: 'ghiChuDieuKien', label: 'Ghi chú điều kiện', class: 'cot-ghi-chu' },
  { key: 'ghiChuGan', label: 'Ghi chú trong syllabus', class: 'cot-ghi-chu' }

]

const cotCauHinhDanhGiaMau = [
  { key: 'id', label: 'ID', class: 'cot-id cot-can-giua' },
  { key: 'syllabusMonHocMauIdText', label: 'Syllabus mẫu ID', class: 'cot-syllabus-id cot-can-giua' },
  { key: 'thuTu', label: 'TT', class: 'cot-tt cot-can-giua' },
  { key: 'tenCotDiem', label: 'Tên cột điểm', class: 'cot-ten-danh-gia' },
  { key: 'loaiDiemText', label: 'Loại điểm', class: 'cot-loai-diem cot-can-giua' },
  { key: 'tyLeText', label: 'Tỷ lệ %', class: 'cot-so-danh-gia cot-can-giua' },
  { key: 'diemToiDaText', label: 'Điểm tối đa', class: 'cot-so-danh-gia cot-can-giua' },
  { key: 'batBuocText', label: 'Bắt buộc', class: 'cot-bat-buoc cot-can-giua' },
  { key: 'ghiChu', label: 'Ghi chú', class: 'cot-ghi-chu' },
  { key: 'createdAtText', label: 'Ngày tạo', class: 'cot-ngay' },
  { key: 'updatedAtText', label: 'Ngày cập nhật', class: 'cot-ngay' }
]

const cotQuyDoiDiemMau = [
  { key: 'thuTu', label: 'TT', class: 'cot-tt cot-can-giua' },
  { key: 'ma', label: 'Mã', class: 'cot-ma-quy-doi' },
  { key: 'ten', label: 'Tên', class: 'cot-ten-quy-doi' },
  { key: 'nguongTuText', label: 'Ngưỡng từ', class: 'cot-can-giua' },
  { key: 'nguongDenText', label: 'Ngưỡng đến', class: 'cot-can-giua' },
  { key: 'diemQuyDoiText', label: 'Điểm quy đổi', class: 'cot-can-giua' },
  { key: 'ketQuaText', label: 'Kết quả', class: 'cot-can-giua' },
  { key: 'tyLeText', label: 'Tỷ lệ %', class: 'cot-can-giua' },
  { key: 'diemToiDaText', label: 'Điểm tối đa', class: 'cot-can-giua' },
  { key: 'batBuocText', label: 'Bắt buộc', class: 'cot-can-giua' },
  { key: 'congThuc', label: 'Công thức', class: 'cot-ghi-chu' },
  { key: 'ghiChu', label: 'Ghi chú', class: 'cot-ghi-chu' }
]

const monHocDangChon = computed(() => danhSachMonHocmau.value.find((mon) => String(mon.id || '') === String(monHocIdDangChon.value || '')) || null)
const syllabusDangChon = computed(() => danhSachSyllabus.value.find((item) => String(item.id || '') === String(syllabusIdDangChon.value || '')) || null)
const danhSachMonLoc = computed(() => {
  const keyword = chuanHoa(tuKhoaMon.value)
  const rows = [...danhSachMonHocmau.value].sort((a, b) => tenMonHoc(a).localeCompare(tenMonHoc(b), 'vi'))
  if (!keyword) return rows
  return rows.filter((mon) => chuanHoa([maMonHoc(mon), tenMonHoc(mon), mon.moTa].filter(Boolean).join(' ')).includes(keyword))
})
const danhSachGanTaiLieuHienThi = computed(() => danhSachGanTaiLieu.value.map((row) => {
  const taiLieu = danhSachTaiLieumau.value.find((item) => String(item.id || '') === String(row.taiLieuMauId || ''))
  const duongDan = taiLieu?.duongDan || ''
  return {
    ...row,
    maTaiLieu: taiLieu?.ma || '-',
    tenTaiLieu: taiLieu?.ten || `ID ${row.taiLieuMauId}`,
    loaiTaiLieu: taiLieu?.loai || '-',
    tacGiaTaiLieu: taiLieu?.tacGia || '-',
    nhaXuatBanTaiLieu: taiLieu?.nhaXuatBan || '-',
    namXuatBanTaiLieu: taiLieu?.namXuatBan ?? '-',
    ghiChuTaiLieu: taiLieu?.ghiChu || '-',
    ghiChuGan: row.ghiChu || '-',
    batBuocText: row.batBuoc ? 'Có' : 'Không',
    duongDan,
    xemFileText: h('button', { type: 'button', class: 'nut nho nut-xem-file', onClick: () => xemTepTaiLieumau(row) }, 'Xem file'),
    duongDanText: duongDan
        ? h('button', { type: 'button', class: 'nut nho nut-mo-doc', onClick: () => moDuongDan(duongDan) }, 'Mở')
        : '-'
  }
}))

const danhSachGanDieuKienHienThi = computed(() => danhSachGanDieuKien.value.map((row) => {
  const dieuKien = danhSachDieuKienmau.value.find((item) => {
    return String(item.id || '') === String(row.dieuKienMauId || '')
  })

  return {
    ...row,
    maDieuKien: row.ma || dieuKien?.ma || '-',
    noiDungDieuKien: row.noiDung || dieuKien?.noiDung || `ID ${row.dieuKienMauId}`,
    loaiDieuKien: row.loai || dieuKien?.loai || '-',
    ghiChuDieuKien: row.ghiChuDieuKien || dieuKien?.ghiChu || '-',
    ghiChuGan: row.ghiChu || '-'
  }
}))

const nhanLoaiDiemDanhGia = {
  chuyen_can: 'Chuyên cần',
  bai_tap: 'Bài tập',
  bai_tap_online: 'Bài tập online',
  kiem_tra_online: 'Kiểm tra online',
  kiem_tra_tren_lop: 'Kiểm tra trên lớp',
  giua_ky: 'Giữa kỳ',
  cuoi_ky: 'Cuối kỳ',
  thuc_hanh: 'Thực hành',
  do_an: 'Đồ án',
  khac: 'Khác'
}

const danhSachCauHinhDanhGiaMauHienThi = computed(() => danhSachCauHinhDanhGiaMau.value.map((row) => ({
  ...row,
  syllabusMonHocMauIdText: hienThi(row.syllabusMonHocMauId),
  loaiDiemText: nhanLoaiDiemDanhGia[row.loaiDiem] || hienThi(row.loaiDiem),
  tyLeText: hienThi(row.tyLe),
  diemToiDaText: hienThi(row.diemToiDa),
  batBuocText: row.batBuoc ? 'Có' : 'Không',
  ghiChu: row.ghiChu || '-',
  createdAtText: hienThiNgay(row.createdAt),
  updatedAtText: hienThiNgay(row.updatedAt)
})))

const tongTyLeCauHinhDanhGiaMau = computed(() => danhSachCauHinhDanhGiaMau.value.reduce((sum, row) => sum + Number(row.tyLe || 0), 0))

const danhSachQuyDoiDiemMauHienThi = computed(() => danhSachQuyDoiDiemMau.value.map((row) => ({
  ...row,
  nguongTuText: hienThi(row.nguongTu),
  nguongDenText: hienThi(row.nguongDen),
  diemQuyDoiText: hienThi(row.diemQuyDoi),
  ketQuaText: row.ketQua === 'dat' || row.ketQua === 'DAT' ? 'Đạt' : (row.ketQua === 'khong_dat' || row.ketQua === 'KHONG_DAT' ? 'Không đạt' : hienThi(row.ketQua)),
  tyLeText: hienThi(row.tyLe),
  diemToiDaText: hienThi(row.diemToiDa),
  batBuocText: row.batBuoc ? 'Có' : 'Không',
  congThuc: row.congThuc || '-',
  ghiChu: row.ghiChu || '-'
})))

const tongHopGioSyllabusmau = computed(() => {
  const tongHopTuChuongBai = danhSachChuongBai.value.reduce((result, row) => {
    result.tongGio += Number(row.tongGio || 0)
    result.gioLyThuyet += Number(row.gioLyThuyet || 0)
    result.gioThucHanh += Number(row.gioThucHanh || 0)
    result.gioKiemTra += Number(row.gioKiemTra || 0)
    return result
  }, { tongGio: 0, gioLyThuyet: 0, gioThucHanh: 0, gioKiemTra: 0 })

  if (danhSachChuongBai.value.length) return tongHopTuChuongBai

  return {
    tongGio: Number(syllabusDangChon.value?.tongGio || 0),
    gioLyThuyet: Number(syllabusDangChon.value?.gioLyThuyet || 0),
    gioThucHanh: Number(syllabusDangChon.value?.gioThucHanh || 0),
    gioKiemTra: Number(syllabusDangChon.value?.gioKiemTra || 0)
  }
})

function chuanHoa(value) { return String(value || '').trim().toLowerCase() }
function maMonHoc(mon) { return mon?.maMon || mon?.ma || '-' }
function tenMonHoc(mon) { return mon?.tenMon || mon?.ten || '-' }
function hienThi(value) { return value === null || value === undefined || value === '' ? '-' : value }
function hienThiNgay(value) { return value ? String(value).replace('T', ' ').slice(0, 19) : '-' }
function hienThiSo(value) {
  const number = Number(value || 0)
  if (!Number.isFinite(number)) return '0'
  return Number.isInteger(number) ? String(number) : String(Number(number.toFixed(2)))
}
function parseNumber(value) { if (value === '' || value === null || value === undefined) return null; const number = Number(value); return Number.isFinite(number) ? number : null }
function layItems(page) { return Array.isArray(page?.items) ? page.items : [] }
function layIdSauKhiLuu(res) {
  return res?.id || res?.data?.id || res?.data?.data?.id || null
}
function baoTin(message, type = 'success', khuVuc = 'syllabus') {
  const key = thongBaoKhuVuc[khuVuc] ? khuVuc : 'syllabus'
  thongBaoKhuVuc[key].message = message
  thongBaoKhuVuc[key].type = type
}
function datThongBaoNhieuKhuVuc(message, type, khuVucList) {
  khuVucList.forEach((khuVuc) => baoTin(message, type, khuVuc))
}
function taoMaTuDong(prefix) {
  const monPart = monHocIdDangChon.value ? `MH${monHocIdDangChon.value}` : 'MH'
  return `${prefix}-${monPart}-${Date.now().toString().slice(-6)}`
}
function moDuongDan(duongDan) {
  const danhSach = String(duongDan || '').split(/\r?\n/).map((item) => item.trim()).filter(Boolean)
  const url = danhSach[0] || ''
  if (!url) return baoTin('Tài liệu chưa có đường dẫn để mở.', 'error', 'taiLieu')
  window.open(url, '_blank', 'noopener,noreferrer')
  if (danhSach.length > 1) baoTin(`Đã mở đường dẫn đầu tiên. Ô Đường dẫn đang có ${danhSach.length} file/thư mục.`, 'info', 'taiLieu')
}
function xemTepTaiLieumau(row) {
  const taiLieumauId = row?.taiLieuMauId || row?.id || null
  if (!taiLieumauId) return baoTin('Không tìm thấy ID tài liệu mẫu để xem file.', 'error', 'taiLieu')
  router.push({
    name: 'DaoTao.XemChuongTrinh.SyllabusTep',
    params: {
      nganhId: 0,
      chuongTrinhId: 0,
      versionId: 0,
      chuongTrinhMonId: 0
    },
    query: {
      tepNguon: 'taiLieumau',
      taiLieumauId
    }
  })
}

function chuanHoaDonViDiem(value) {
  const text = String(value || '').trim().toLowerCase()
  if (!text) return null
  if (['thang_10', 'thang 10', 'thang10', '10'].includes(text)) return 'thang_10'
  if (['phan_tram', 'phần trăm', 'phan tram', '%'].includes(text)) return 'phan_tram'
  return text
}

async function taiDuLieuNen() {
  dangTai.value = true
  try {
    const [monHoc, taiLieumau, dieuKienmau] = await Promise.all([services.monHoc.getAll({ size: 500 }), services.taiLieumau.getAll({ size: 500 }), services.dieuKienMonHocmau.getAll({ size: 500 })])
    danhSachMonHocmau.value = layItems(monHoc)
    danhSachTaiLieumau.value = layItems(taiLieumau)
    danhSachDieuKienmau.value = layItems(dieuKienmau)
    if (monHocIdDangChon.value) await taiSyllabusTheoMonVaMoForm()
    baoTin('Đã tải lại dữ liệu mẫu.', 'success', 'mon')
  } catch (error) { baoTin(layThongBaoLoi(error, 'Không tải được dữ liệu mẫu.'), 'error', 'mon') }
  finally { dangTai.value = false }
}

async function chonMonHoc(mon) {
  monHocIdDangChon.value = mon?.id || null
  resetTatCaFormChiTiet()
  await taiSyllabusTheoMonVaMoForm()
}

async function taiSyllabusTheoMonVaMoForm() {
  if (!monHocIdDangChon.value) return
  const page = await services.syllabusMonHocmau.getAll({ size: 500, monHocId: monHocIdDangChon.value })
  const rows = layItems(page).filter((row) => String(row.monHocId || '') === String(monHocIdDangChon.value || ''))
  syllabusTrungTheoMon.value = rows
  danhSachSyllabus.value = rows.slice(0, 1)
  if (rows.length) {
    const syllabus = rows[0]
    syllabusIdDangChon.value = syllabus.id
    suaSyllabus(syllabus)
    await taiChiTietSyllabus()
    if (rows.length > 1) baoTin('Môn này đang có nhiều hơn 1 syllabus mẫu trong dữ liệu cũ. Trang chỉ mở bản đầu tiên; nên xử lý trùng trong DB.', 'error', 'syllabus')
  } else {
    syllabusIdDangChon.value = null
    resetFormSyllabus()
    resetTatCaFormChiTiet()
  }
}

async function taiChiTietSyllabus() {
  if (!syllabusIdDangChon.value) return
  try {
    const params = { size: 500, syllabusMonHocMauId: syllabusIdDangChon.value }
    const [chuongBai, cauHinhDanhGiaMau, quyDoiDiemMau, ganTaiLieu, ganDieuKien] = await Promise.all([services.syllabusMonHocmauChuongBai.getAll(params), services.cauHinhDanhGiaMau.getAll(params), services.quyDoiDiemMau.getAll(params), services.syllabusMonHocmauTaiLieu.getAll(params), services.syllabusMonHocmauDieuKien.getAll(params)])
    danhSachChuongBai.value = layItems(chuongBai).sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
    let rowsCauHinhDanhGiaMau = layItems(cauHinhDanhGiaMau)
    // BE chuẩn đã filter theo syllabusMonHocMauId. Dòng dưới vẫn lọc lại ở FE để tránh hiển thị nhầm nếu API trả nhiều dữ liệu.
    rowsCauHinhDanhGiaMau = rowsCauHinhDanhGiaMau.filter((row) => String(row.syllabusMonHocMauId || '') === String(syllabusIdDangChon.value || ''))
    danhSachCauHinhDanhGiaMau.value = rowsCauHinhDanhGiaMau.sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
    danhSachQuyDoiDiemMau.value = layItems(quyDoiDiemMau).sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
    danhSachGanTaiLieu.value = layItems(ganTaiLieu).sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
    danhSachGanDieuKien.value = layItems(ganDieuKien).sort((a, b) => Number(a.thuTu || 0) - Number(b.thuTu || 0))
  } catch (error) { datThongBaoNhieuKhuVuc(layThongBaoLoi(error, 'Không tải được chi tiết syllabus.'), 'error', ['chuongBai', 'cauHinhDanhGiaMau', 'quyDoiDiemMau', 'taiLieu', 'dieuKien']) }
}

function resetFormSyllabus() {
  Object.assign(formSyllabus, {
    id: null,
    ma: '',
    ten: '',
    viTri: '',
    tinhChat: '',
    soTinChi: '',
    soBuoiHoc: '',
    soTietMoiBuoi: '',
    soPhutMotTiet: '',
    mucTieu: '',
    phuongPhapDanhGia: '',
    dieuKienHoanThanh: '',
    huongDan: '',
    diemDatToiThieu: '',
    donViDiem: '',
    tyLeChuyenCanToiThieu: '',
    batBuocDuThi: false,
    congThucQuyDoi: '',
    ghiChu: ''
  })
}
function lamMoiFormSyllabus() { if (syllabusDangChon.value) suaSyllabus(syllabusDangChon.value); else resetFormSyllabus() }
function suaSyllabus(row) {
  Object.assign(formSyllabus, {
    id: row.id,
    ma: row.ma || '',
    ten: row.ten || '',
    viTri: row.viTri || '',
    tinhChat: row.tinhChat || '',
    soTinChi: row.soTinChi ?? '',
    soBuoiHoc: row.soBuoiHoc ?? '',
    soTietMoiBuoi: row.soTietMoiBuoi ?? '',
    soPhutMotTiet: row.soPhutMotTiet ?? '',
    mucTieu: row.mucTieu || '',
    phuongPhapDanhGia: row.phuongPhapDanhGia || '',
    dieuKienHoanThanh: row.dieuKienHoanThanh || '',
    huongDan: row.huongDan || '',
    diemDatToiThieu: row.diemDatToiThieu ?? '',
    donViDiem: row.donViDiem || '',
    tyLeChuyenCanToiThieu: row.tyLeChuyenCanToiThieu ?? '',
    batBuocDuThi: Boolean(row.batBuocDuThi),
    congThucQuyDoi: row.congThucQuyDoi || '',
    ghiChu: row.ghiChu || ''
  })
}
function payloadSyllabus() {
  return {
    monHocId: monHocIdDangChon.value,
    ma: formSyllabus.ma || taoMaTuDong('SYL-mau'),
    ten: formSyllabus.ten,
    viTri: formSyllabus.viTri || null,
    tinhChat: formSyllabus.tinhChat || null,
    soTinChi: parseNumber(formSyllabus.soTinChi),
    soBuoiHoc: parseNumber(formSyllabus.soBuoiHoc),
    soTietMoiBuoi: parseNumber(formSyllabus.soTietMoiBuoi),
    soPhutMotTiet: parseNumber(formSyllabus.soPhutMotTiet),
    mucTieu: formSyllabus.mucTieu || null,
    phuongPhapDanhGia: formSyllabus.phuongPhapDanhGia || null,
    dieuKienHoanThanh: formSyllabus.dieuKienHoanThanh || null,
    huongDan: formSyllabus.huongDan || null,
    diemDatToiThieu: parseNumber(formSyllabus.diemDatToiThieu),
    donViDiem: chuanHoaDonViDiem(formSyllabus.donViDiem),
    tyLeChuyenCanToiThieu: parseNumber(formSyllabus.tyLeChuyenCanToiThieu),
    batBuocDuThi: Boolean(formSyllabus.batBuocDuThi),
    congThucQuyDoi: formSyllabus.congThucQuyDoi || null,
    ghiChu: formSyllabus.ghiChu || null
  }
}

async function luuSyllabus() {
  if (!monHocIdDangChon.value) return baoTin('Cần chọn môn học mẫu.', 'error', 'syllabus')
  if (!formSyllabus.ten) return baoTin('Cần nhập tên syllabus.', 'error', 'syllabus')
  if (parseNumber(formSyllabus.soTinChi) !== null && parseNumber(formSyllabus.soTinChi) < 0) {
    return baoTin('Số tín chỉ không được âm.', 'error', 'syllabus')
  }
  if (!parseNumber(formSyllabus.soBuoiHoc) || parseNumber(formSyllabus.soBuoiHoc) < 1) return baoTin('Cần nhập số buổi học lớn hơn 0.', 'error', 'syllabus')
  if (!parseNumber(formSyllabus.soTietMoiBuoi) || parseNumber(formSyllabus.soTietMoiBuoi) <= 0) return baoTin('Cần nhập số tiết mỗi buổi lớn hơn 0.', 'error', 'syllabus')
  if (!parseNumber(formSyllabus.soPhutMotTiet) || parseNumber(formSyllabus.soPhutMotTiet) < 1) return baoTin('Cần nhập số phút một tiết lớn hơn 0.', 'error', 'syllabus')
  dangLuu.value = true
  try {
    if (formSyllabus.id) await services.syllabusMonHocmau.update(formSyllabus.id, payloadSyllabus())
    else await services.syllabusMonHocmau.create(payloadSyllabus())
    await taiSyllabusTheoMonVaMoForm()
    baoTin('Đã lưu syllabus mẫu.', 'success', 'syllabus')
  } catch (error) { baoTin(layThongBaoLoi(error, 'Không lưu được syllabus mẫu.'), 'error', 'syllabus') }
  finally { dangLuu.value = false }
}

async function xoaSyllabusDangMo() {
  if (!formSyllabus.id) return
  if (!window.confirm(`Xóa syllabus mẫu "${formSyllabus.ten || formSyllabus.ma || formSyllabus.id}"?`)) return
  dangLuu.value = true
  try {
    await services.syllabusMonHocmau.delete(formSyllabus.id)
    await taiSyllabusTheoMonVaMoForm()
    baoTin('Đã xóa syllabus mẫu.', 'success', 'syllabus')
  } catch (error) { baoTin(layThongBaoLoi(error, 'Không xóa được syllabus mẫu. Kiểm tra dữ liệu con đang dùng.'), 'error', 'syllabus') }
  finally { dangLuu.value = false }
}

function resetFormChuongBai() { Object.assign(formChuongBai, { id: null, maChuong: '', ten: '', mucTieu: '', noiDung: '', thuTu: '', tongGio: '', gioLyThuyet: '', gioThucHanh: '', gioKiemTra: '', ghiChu: '' }) }
function suaChuongBai(row) { Object.assign(formChuongBai, { id: row.id, maChuong: row.maChuong || '', ten: row.ten || '', mucTieu: row.mucTieu || '', noiDung: row.noiDung || '', thuTu: row.thuTu ?? '', tongGio: row.tongGio ?? '', gioLyThuyet: row.gioLyThuyet ?? '', gioThucHanh: row.gioThucHanh ?? '', gioKiemTra: row.gioKiemTra ?? '', ghiChu: row.ghiChu || '' }) }
function payloadChuongBai() { return { syllabusMonHocMauId: syllabusIdDangChon.value, maChuong: formChuongBai.maChuong || null, ten: formChuongBai.ten, mucTieu: formChuongBai.mucTieu || null, noiDung: formChuongBai.noiDung || null, thuTu: parseNumber(formChuongBai.thuTu), tongGio: parseNumber(formChuongBai.tongGio), gioLyThuyet: parseNumber(formChuongBai.gioLyThuyet), gioThucHanh: parseNumber(formChuongBai.gioThucHanh), gioKiemTra: parseNumber(formChuongBai.gioKiemTra), ghiChu: formChuongBai.ghiChu || null } }
async function luuChuongBai() { if (!syllabusIdDangChon.value) return baoTin('Cần lưu syllabus mẫu trước.', 'error', 'chuongBai'); if (!formChuongBai.ten) return baoTin('Cần nhập tên chương/bài.', 'error', 'chuongBai'); await luuChiTiet(services.syllabusMonHocmauChuongBai, formChuongBai.id, payloadChuongBai(), resetFormChuongBai, 'chương/bài mẫu', 'chuongBai') }
async function xoaChuongBai(row) { await xoaChiTiet(services.syllabusMonHocmauChuongBai, row, 'chương/bài mẫu', 'chuongBai') }

function resetFormCauHinhDanhGiaMau() {
  Object.assign(formCauHinhDanhGiaMau, { id: null, syllabusMonHocMauId: syllabusIdDangChon.value || '', tenCotDiem: '', loaiDiem: '', tyLe: '', diemToiDa: '', thuTu: '', batBuoc: true, ghiChu: '' })
}
function suaCauHinhDanhGiaMau(row) {
  Object.assign(formCauHinhDanhGiaMau, {
    id: row.id,
    syllabusMonHocMauId: row.syllabusMonHocMauId || syllabusIdDangChon.value || '',
    tenCotDiem: row.tenCotDiem || '',
    loaiDiem: row.loaiDiem || '',
    tyLe: row.tyLe ?? '',
    diemToiDa: row.diemToiDa ?? '',
    thuTu: row.thuTu ?? '',
    batBuoc: row.batBuoc !== false,
    ghiChu: row.ghiChu || ''
  })
}
function payloadCauHinhDanhGiaMau() {
  return {
    syllabusMonHocMauId: syllabusIdDangChon.value,
    tenCotDiem: formCauHinhDanhGiaMau.tenCotDiem,
    loaiDiem: formCauHinhDanhGiaMau.loaiDiem || null,
    tyLe: parseNumber(formCauHinhDanhGiaMau.tyLe),
    diemToiDa: parseNumber(formCauHinhDanhGiaMau.diemToiDa),
    thuTu: parseNumber(formCauHinhDanhGiaMau.thuTu),
    batBuoc: Boolean(formCauHinhDanhGiaMau.batBuoc),
    ghiChu: formCauHinhDanhGiaMau.ghiChu || null
  }
}
async function luuCauHinhDanhGiaMau() {
  if (!syllabusIdDangChon.value) return baoTin('Cần lưu syllabus mẫu trước.', 'error', 'cauHinhDanhGiaMau')
  if (!formCauHinhDanhGiaMau.tenCotDiem) return baoTin('Cần nhập tên cột điểm mẫu.', 'error', 'cauHinhDanhGiaMau')
  const tyLe = parseNumber(formCauHinhDanhGiaMau.tyLe)
  if (tyLe === null || tyLe < 0 || tyLe > 100) return baoTin('Tỷ lệ phải nằm trong khoảng 0 - 100.', 'error', 'cauHinhDanhGiaMau')
  await luuChiTiet(services.cauHinhDanhGiaMau, formCauHinhDanhGiaMau.id, payloadCauHinhDanhGiaMau(), resetFormCauHinhDanhGiaMau, 'cấu hình đánh giá mẫu', 'cauHinhDanhGiaMau')
}
async function xoaCauHinhDanhGiaMau(row) { await xoaChiTiet(services.cauHinhDanhGiaMau, row, 'cấu hình đánh giá mẫu', 'cauHinhDanhGiaMau') }

function resetFormQuyDoiDiemMau() {
  Object.assign(formQuyDoiDiemMau, { id: null, syllabusMonHocMauId: syllabusIdDangChon.value || '', ma: '', ten: '', nguongTu: '', nguongDen: '', diemQuyDoi: '', ketQua: '', congThuc: '', ghiChu: '', loaiMau: 'QUY_DOI_KET_QUA', tyLe: '', diemToiDa: '', thuTu: '', batBuoc: true })
}
function suaQuyDoiDiemMau(row) {
  Object.assign(formQuyDoiDiemMau, {
    id: row.id,
    syllabusMonHocMauId: row.syllabusMonHocMauId || syllabusIdDangChon.value || '',
    ma: row.ma || '',
    ten: row.ten || '',
    nguongTu: row.nguongTu ?? '',
    nguongDen: row.nguongDen ?? '',
    diemQuyDoi: row.diemQuyDoi ?? '',
    ketQua: row.ketQua || '',
    congThuc: row.congThuc || '',
    ghiChu: row.ghiChu || '',
    loaiMau: row.loaiMau || 'QUY_DOI_KET_QUA',
    tyLe: row.tyLe ?? '',
    diemToiDa: row.diemToiDa ?? '',
    thuTu: row.thuTu ?? '',
    batBuoc: row.batBuoc !== false
  })
}
function payloadQuyDoiDiemMau() {
  return {
    syllabusMonHocMauId: syllabusIdDangChon.value,
    ma: formQuyDoiDiemMau.ma || taoMaTuDong('QDD-MAU'),
    ten: formQuyDoiDiemMau.ten,
    nguongTu: parseNumber(formQuyDoiDiemMau.nguongTu),
    nguongDen: parseNumber(formQuyDoiDiemMau.nguongDen),
    diemQuyDoi: parseNumber(formQuyDoiDiemMau.diemQuyDoi),
    ketQua: formQuyDoiDiemMau.ketQua || null,
    congThuc: formQuyDoiDiemMau.congThuc || null,
    ghiChu: formQuyDoiDiemMau.ghiChu || null,
    loaiMau: 'QUY_DOI_KET_QUA',
    tyLe: parseNumber(formQuyDoiDiemMau.tyLe),
    diemToiDa: parseNumber(formQuyDoiDiemMau.diemToiDa),
    thuTu: parseNumber(formQuyDoiDiemMau.thuTu),
    batBuoc: Boolean(formQuyDoiDiemMau.batBuoc)
  }
}
async function luuQuyDoiDiemMau() {
  if (!syllabusIdDangChon.value) return baoTin('Cần lưu syllabus mẫu trước.', 'error', 'quyDoiDiemMau')
  if (!formQuyDoiDiemMau.ten) return baoTin('Cần nhập tên quy đổi điểm mẫu.', 'error', 'quyDoiDiemMau')
  if (!formQuyDoiDiemMau.ma) formQuyDoiDiemMau.ma = taoMaTuDong('QDD-MAU')
  await luuChiTiet(services.quyDoiDiemMau, formQuyDoiDiemMau.id, payloadQuyDoiDiemMau(), resetFormQuyDoiDiemMau, 'quy đổi điểm mẫu', 'quyDoiDiemMau')
}
async function xoaQuyDoiDiemMau(row) { await xoaChiTiet(services.quyDoiDiemMau, row, 'quy đổi điểm mẫu', 'quyDoiDiemMau') }


function chonTepTaiLieu() {
  taiLieuFileInput.value?.click()
}

function chonThuMucTaiLieu() {
  taiLieuFolderInput.value?.click()
}

async function xuLyChonDuongDanTaiLieu(event) {
  const files = Array.from(event?.target?.files || [])
  if (!files.length) return
  if (!syllabusIdDangChon.value) {
    event.target.value = ''
    return baoTin('Cần lưu syllabus mẫu trước khi upload file tài liệu.', 'error', 'taiLieu')
  }
  dangLuu.value = true
  try {
    const danhSachFileInfo = await tepDinhKemUploadService.uploadMany({
      files,
      module: 'dao-tao',
      nghiepVu: 'syllabus_mon_hoc_mau_tai_lieu',
      doiTuongId: syllabusIdDangChon.value,
      nguoiGuiLoai: 'DAO_TAO',
      nguoiGuiId: 1,
      nguoiGuiTen: 'Đào tạo',
      moTa: formTaiLieumau.ten || '',
      monHocId: monHocIdDangChon.value || null,
      syllabusMonHocMauId: syllabusIdDangChon.value,
      tangNghiepVu: 'syllabus-mon-hoc-mau-tai-lieu'
    })

    const duongDanMoi = danhSachFileInfo.map((fileInfo, index) => {
      const file = files[index]
      const duongDan = tepDinhKemUploadService.taoDuongDanTaiFile(fileInfo)
      return duongDan || file?.webkitRelativePath || file?.name || ''
    }).filter(Boolean)

    const hienTai = String(formTaiLieumau.duongDan || '').split(/\r?\n/).map((item) => item.trim()).filter(Boolean)
    formTaiLieumau.duongDan = Array.from(new Set([...hienTai, ...duongDanMoi])).join('\n')
    if (!formTaiLieumau.ten && files.length === 1) formTaiLieumau.ten = files[0].name.replace(/\.[^.]+$/, '')
    if (!formTaiLieumau.loai && files.length === 1) formTaiLieumau.loai = (files[0].name.split('.').pop() || '').toLowerCase()
    baoTin(`Đã upload và lưu ${files.length} file/thư mục vào ô Đường dẫn đã lưu.`, 'success', 'taiLieu')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không upload được file tài liệu.'), 'error', 'taiLieu')
  } finally {
    dangLuu.value = false
    event.target.value = ''
  }
}

function resetFormTaiLieumau() { Object.assign(formTaiLieumau, { id: null, ma: '', ten: '', loai: '', tacGia: '', nhaXuatBan: '', namXuatBan: '', duongDan: '', ghiChu: '' }); resetFormGanTaiLieu() }
function payloadTaiLieumau() { return { ma: formTaiLieumau.ma || taoMaTuDong('TL-mau'), ten: formTaiLieumau.ten, loai: formTaiLieumau.loai || null, tacGia: formTaiLieumau.tacGia || null, nhaXuatBan: formTaiLieumau.nhaXuatBan || null, namXuatBan: parseNumber(formTaiLieumau.namXuatBan), duongDan: formTaiLieumau.duongDan || null, ghiChu: formTaiLieumau.ghiChu || null } }
async function luuTaiLieumau() {
  if (!syllabusIdDangChon.value) return baoTin('Cần lưu syllabus mẫu trước khi thêm tài liệu.', 'error', 'taiLieu')
  if (!formTaiLieumau.ten) return baoTin('Cần nhập tên tài liệu mẫu.', 'error', 'taiLieu')
  dangLuu.value = true
  try {
    const payload = payloadTaiLieumau()
    const res = formTaiLieumau.id ? await services.taiLieumau.update(formTaiLieumau.id, payload) : await services.taiLieumau.create(payload)
    const savedId = layIdSauKhiLuu(res) || formTaiLieumau.id
    const linkPayload = {
      syllabusMonHocMauId: syllabusIdDangChon.value,
      taiLieuMauId: savedId,
      thuTu: parseNumber(formGanTaiLieu.thuTu),
      batBuoc: Boolean(formGanTaiLieu.batBuoc),
      ghiChu: formGanTaiLieu.ghiChu || null,
      ma: payload.ma,
      ten: payload.ten,
      loai: payload.loai,
      tacGia: payload.tacGia,
      nhaXuatBan: payload.nhaXuatBan,
      namXuatBan: payload.namXuatBan,
      duongDan: payload.duongDan
    }
    if (formGanTaiLieu.id) await services.syllabusMonHocmauTaiLieu.update(formGanTaiLieu.id, linkPayload)
    else await services.syllabusMonHocmauTaiLieu.create(linkPayload)
    danhSachTaiLieumau.value = layItems(await services.taiLieumau.getAll({ size: 500 }))
    await taiChiTietSyllabus()
    resetFormTaiLieumau()
    baoTin('Đã lưu tài liệu mẫu trực tiếp vào syllabus mẫu.', 'success', 'taiLieu')
  } catch (error) { baoTin(layThongBaoLoi(error, 'Không lưu được tài liệu mẫu của syllabus.'), 'error', 'taiLieu') }
  finally { dangLuu.value = false }
}
function resetFormGanTaiLieu() { Object.assign(formGanTaiLieu, { id: null, taiLieumauId: '', thuTu: '', batBuoc: false, ghiChu: '' }) }
function suaGanTaiLieu(row) {
  Object.assign(formGanTaiLieu, { id: row.id, taiLieumauId: row.taiLieuMauId, thuTu: row.thuTu ?? '', batBuoc: Boolean(row.batBuoc), ghiChu: row.ghiChu || '' })
  const taiLieu = danhSachTaiLieumau.value.find((item) => String(item.id || '') === String(row.taiLieuMauId || ''))
  Object.assign(formTaiLieumau, {
    id: taiLieu?.id || row.taiLieuMauId || null,
    ma: row.ma || taiLieu?.ma || '',
    ten: row.ten || taiLieu?.ten || '',
    loai: row.loai || taiLieu?.loai || '',
    tacGia: row.tacGia || taiLieu?.tacGia || '',
    nhaXuatBan: row.nhaXuatBan || taiLieu?.nhaXuatBan || '',
    namXuatBan: row.namXuatBan ?? taiLieu?.namXuatBan ?? '',
    duongDan: row.duongDan || taiLieu?.duongDan || '',
    ghiChu: row.ghiChuTaiLieu || taiLieu?.ghiChu || ''
  })
}
function payloadGanTaiLieu() { return { syllabusMonHocMauId: syllabusIdDangChon.value, taiLieuMauId: parseNumber(formGanTaiLieu.taiLieumauId), thuTu: parseNumber(formGanTaiLieu.thuTu), batBuoc: Boolean(formGanTaiLieu.batBuoc), ghiChu: formGanTaiLieu.ghiChu || null } }
async function luuGanTaiLieu() { if (!formGanTaiLieu.taiLieumauId) return baoTin('Cần chọn tài liệu mẫu để gán.', 'error', 'taiLieu'); await luuChiTiet(services.syllabusMonHocmauTaiLieu, formGanTaiLieu.id, payloadGanTaiLieu(), resetFormGanTaiLieu, 'gán tài liệu mẫu', 'taiLieu') }
async function xoaGanTaiLieu(row) { await xoaChiTiet(services.syllabusMonHocmauTaiLieu, row, 'gán tài liệu mẫu', 'taiLieu') }

function resetFormDieuKienmau() { Object.assign(formDieuKienmau, { id: null, ma: '', loai: 'khac', noiDung: '', ghiChu: '' }) }
function payloadDieuKienmau() { return { ma: formDieuKienmau.ma || taoMaTuDong('DK-mau'), loai: formDieuKienmau.loai || 'khac', noiDung: formDieuKienmau.noiDung, ghiChu: formDieuKienmau.ghiChu || null } }
async function luuDieuKienmau() {
  if (!syllabusIdDangChon.value) {
    return baoTin('Cần lưu syllabus mẫu trước khi thêm điều kiện.', 'error', 'dieuKien')
  }

  if (!formDieuKienmau.noiDung) {
    return baoTin('Cần nhập nội dung điều kiện mẫu.', 'error', 'dieuKien')
  }

  dangLuu.value = true

  try {
    const payload = payloadDieuKienmau()

    const res = formDieuKienmau.id
        ? await services.dieuKienMonHocmau.update(formDieuKienmau.id, payload)
        : await services.dieuKienMonHocmau.create(payload)

    const savedId = layIdSauKhiLuu(res) || formDieuKienmau.id

    const linkPayload = {
      syllabusMonHocMauId: syllabusIdDangChon.value,
      dieuKienMauId: savedId,
      thuTu: parseNumber(formGanDieuKien.thuTu),
      ghiChu: formGanDieuKien.ghiChu || null
    }

    if (formGanDieuKien.id) {
      await services.syllabusMonHocmauDieuKien.update(formGanDieuKien.id, linkPayload)
    } else {
      await services.syllabusMonHocmauDieuKien.create(linkPayload)
    }

    danhSachDieuKienmau.value = layItems(await services.dieuKienMonHocmau.getAll({ size: 500 }))
    await taiChiTietSyllabus()
    resetFormDieuKienmau()
    baoTin('Đã lưu điều kiện mẫu trực tiếp vào syllabus mẫu.', 'success', 'dieuKien')
  } catch (error) {
    baoTin(layThongBaoLoi(error, 'Không lưu được điều kiện mẫu của syllabus.'), 'error', 'dieuKien')
  } finally {
    dangLuu.value = false
  }
}
function resetFormGanDieuKien() { Object.assign(formGanDieuKien, { id: null, dieuKienmauId: '', thuTu: '', ghiChu: '' }) }
function suaGanDieuKien(row) {
  Object.assign(formGanDieuKien, {
    id: row.id,
    dieuKienmauId: row.dieuKienMauId,
    thuTu: row.thuTu ?? '',
    ghiChu: row.ghiChu || ''
  })

  const dieuKien = danhSachDieuKienmau.value.find((item) => {
    return String(item.id || '') === String(row.dieuKienMauId || '')
  })

  Object.assign(formDieuKienmau, {
    id: dieuKien?.id || row.dieuKienmauId || null,
    ma: dieuKien?.ma || '',
    loai: dieuKien?.loai || 'khac',
    noiDung: dieuKien?.noiDung || '',
    ghiChu: dieuKien?.ghiChu || ''
  })
}
function payloadGanDieuKien() { return { syllabusMonHocMauId: syllabusIdDangChon.value, dieuKienMauId: parseNumber(formGanDieuKien.dieuKienmauId), thuTu: parseNumber(formGanDieuKien.thuTu), ghiChu: formGanDieuKien.ghiChu || null } }
async function luuGanDieuKien() { if (!formGanDieuKien.dieuKienmauId) return baoTin('Cần chọn điều kiện mẫu để gán.', 'error', 'dieuKien'); await luuChiTiet(services.syllabusMonHocmauDieuKien, formGanDieuKien.id, payloadGanDieuKien(), resetFormGanDieuKien, 'gán điều kiện mẫu', 'dieuKien') }
async function xoaGanDieuKien(row) { await xoaChiTiet(services.syllabusMonHocmauDieuKien, row, 'gán điều kiện mẫu', 'dieuKien') }

async function luuChiTiet(service, id, payload, resetFn, ten, khuVuc = 'syllabus') { if (!syllabusIdDangChon.value) return baoTin('Cần lưu syllabus mẫu trước.', 'error', khuVuc); dangLuu.value = true; try { if (id) await service.update(id, payload); else await service.create(payload); await taiChiTietSyllabus(); resetFn(); baoTin(`Đã lưu ${ten}.`, 'success', khuVuc) } catch (error) { baoTin(layThongBaoLoi(error, `Không lưu được ${ten}.`), 'error', khuVuc) } finally { dangLuu.value = false } }
async function xoaChiTiet(service, row, ten, khuVuc = 'syllabus') { if (!row?.id) return; if (!window.confirm(`Xóa ${ten} này?`)) return; dangLuu.value = true; try { await service.delete(row.id); await taiChiTietSyllabus(); baoTin(`Đã xóa ${ten}.`, 'success', khuVuc) } catch (error) { baoTin(layThongBaoLoi(error, `Không xóa được ${ten}.`), 'error', khuVuc) } finally { dangLuu.value = false } }
function resetTatCaFormChiTiet() { danhSachChuongBai.value = []; danhSachCauHinhDanhGiaMau.value = []; danhSachQuyDoiDiemMau.value = []; danhSachGanTaiLieu.value = []; danhSachGanDieuKien.value = []; resetFormChuongBai(); resetFormCauHinhDanhGiaMau(); resetFormQuyDoiDiemMau(); resetFormGanTaiLieu(); resetFormGanDieuKien() }

onMounted(taiDuLieuNen)
</script>

<style scoped>
.syllabus-mon-hoc-mau-page { display: grid; gap: 10px; padding: 10px; font-family: Roboto, Arial, sans-serif; color: #111827; }
.thong-bao { padding: 9px 11px; border: 1px solid #bbf7d0; border-radius: 5px; background: #f0fdf4; color: #166534; font-size: 13px; }
.thong-bao.error { border-color: #fecaca; background: #fef2f2; color: #b91c1c; }
.khoi-dau, .the, .hop-rong, .thanh-mon { border: 1px solid #d7dde6; border-radius: 6px; background: #fff; box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04); }
.khoi-dau, .thanh-mon { display: flex; align-items: center; justify-content: space-between; gap: 10px; padding: 10px 12px; }
.khoi-dau h2, .tieu-de-the h3 { margin: 0; font-size: 16px; color: #0f172a; }
.khoi-dau p, .tieu-de-the p { margin: 3px 0 0; color: #64748b; font-size: 12px; line-height: 1.35; }
.luoi-trang { display: grid; grid-template-columns: 285px minmax(0, 1fr); gap: 10px; align-items: start; }
.noi-dung-chinh { display: grid; gap: 10px; min-width: 0; }
.luoi-chinh { display: grid; grid-template-columns: minmax(0, 1fr); gap: 10px; align-items: start; }
.khoi-syllabus, .khoi-chuong-bai, .khoi-quy-doi, .khoi-tai-lieu, .khoi-dieu-kien { width: 100%; min-width: 0; }
.khoi-chuong-bai,
.khoi-quy-doi,
.khoi-tai-lieu { border-color: #cbd5e1; }
.khoi-tai-lieu .tieu-de-the { background: #f7fbff; }
.tieu-de-the { padding: 9px 11px; border-bottom: 1px solid #e5e7eb; background: #f8fafc; }
.tieu-de-the.compact h3 { font-size: 15px; }
.bo-loc { padding: 10px; border-bottom: 1px solid #eef2f7; }
.danh-sach-mon { display: grid; gap: 5px; padding: 10px; max-height: calc(100vh - 155px); min-height: 680px; overflow: auto; scrollbar-width: thin; }
.dong-mon { display: grid; gap: 2px; width: 100%; border: 1px solid #e5e7eb; border-radius: 5px; background: #fff; padding: 7px 9px; text-align: left; cursor: pointer; font-family: Roboto, Arial, sans-serif; }
.dong-mon:hover, .dong-mon.dang-chon { border-color: #2563eb; background: #eff6ff; }
.dong-mon b { font-size: 13px; }
.dong-mon small, td small { display: block; margin-top: 2px; color: #64748b; }
.thanh-mon b { font-size: 12px; color: #475569; }
.thanh-mon span { font-size: 13px; font-weight: 700; }
.badge { padding: 5px 9px; border-radius: 999px; border: 1px solid #bfdbfe; background: #eff6ff; color: #1d4ed8; font-size: 12px; font-weight: 700; }
.form-grid { display: grid; grid-template-columns: repeat(2, minmax(150px, 1fr)); gap: 8px; padding: 10px; }
.form-syllabus { grid-template-columns: repeat(2, minmax(200px, 1fr)); }
.form-nho { grid-template-columns: repeat(3, minmax(90px, 1fr)); }
.form-gan { border-top: 1px dashed #cbd5e1; background: #fbfdff; }
label { display: grid; gap: 3px; }
label span { color: #374151; font-size: 12px; font-weight: 700; }
label b { color: #dc2626; }
input, select, textarea { width: 100%; min-height: 30px; border: 1px solid #cbd5e1; border-radius: 4px; padding: 5px 7px; font-size: 13px; font-family: Roboto, Arial, sans-serif; background: #fff; box-sizing: border-box; }
textarea { resize: vertical; line-height: 1.35; }
input[readonly], textarea[readonly] { background: #f8fafc; color: #334155; cursor: not-allowed; }
.tong-hop-syllabus {
  display: grid;
  grid-template-columns: repeat(4, minmax(100px, 1fr));
  gap: 8px;
  padding: 9px;
  border: 1px dashed #bfdbfe;
  border-radius: 6px;
  background: #f8fbff;
}
.tieu-de-tong-hop {
  grid-column: 1 / -1;
  color: #1e40af;
  font-size: 12px;
  font-weight: 800;
}
.check-line { display: flex; align-items: center; gap: 7px; padding-top: 19px; }
.check-line input { width: auto; min-height: auto; }
.compact-check { padding-top: 19px; }
.cot-rong { grid-column: 1 / -1; }
.hang-nut { display: flex; flex-wrap: wrap; gap: 6px; }
.canh-bao-nho { margin: 10px 10px 0; padding: 8px 10px; border: 1px solid #fed7aa; border-radius: 5px; background: #fff7ed; color: #9a3412; font-size: 12px; }
.input-file-hidden { display: none; }

.thong-bao-khu-vuc {
  min-height: 31px;
  display: flex;
  align-items: center;
  margin: 8px 10px 0;
  padding: 7px 10px;
  border: 1px solid #dbeafe;
  border-radius: 6px;
  background: #f8fbff;
  color: #334155;
  font-size: 12px;
  font-weight: 700;
  line-height: 1.35;
}
.thong-bao-khu-vuc.info { border-color: #dbeafe; background: #f8fbff; color: #1e40af; }
.thong-bao-khu-vuc.success { border-color: #bbf7d0; background: #f0fdf4; color: #166534; }
.thong-bao-khu-vuc.error { border-color: #fecaca; background: #fef2f2; color: #b91c1c; }
.thong-bao-khu-vuc.warning { border-color: #fed7aa; background: #fff7ed; color: #9a3412; }

.hop-rong, .rong { color: #6b7280; text-align: center; }
.hop-rong { padding: 26px; }
.bang-boc { max-height: 430px; overflow: auto; border-top: 1px solid #e5e7eb; }
.bang-du-lieu { width: 100%; min-width: 620px; border-collapse: collapse; font-size: 12.5px; }
.bang-chuong-bai { min-width: 980px; }
.khoi-tai-lieu .form-nho { grid-template-columns: repeat(2, minmax(150px, 1fr)); }
.khoi-tai-lieu .form-gan { padding-top: 8px; }
.khoi-tai-lieu .bang-boc { max-height: 300px; }

.khoi-chuong-bai .bang-boc { max-height: 360px; }
th, td { border: 1px solid #e5e7eb; padding: 7px 8px; text-align: left; vertical-align: top; }
th { position: sticky; top: 0; z-index: 5; background: #f1f5f9; color: #334155; font-size: 12px; font-weight: 800; white-space: nowrap; }
td { background: #fff; }
tr:nth-child(even) td { background: #fcfcfd; }
.cot-so { width: 64px; text-align: center; white-space: nowrap; }
.cot-noi-dung { min-width: 220px; color: #334155; }
.cot-ghi-chu-bang { min-width: 160px; color: #475569; }
.cot-thao-tac { width: 128px; text-align: center; }
.thao-tac { display: flex; flex-wrap: wrap; justify-content: center; align-items: center; gap: 5px; }
.khoi-tai-lieu .bang-boc { border: 1px solid #dbe4f0; border-radius: 0 0 6px 6px; background: #fff; }
.khoi-tai-lieu .bang-du-lieu { min-width: 640px; table-layout: fixed; }
.khoi-tai-lieu th { background: #eaf3ff; color: #1e3a8a; border-color: #d4e5fb; }
.khoi-tai-lieu td { border-color: #e2e8f0; }
.khoi-tai-lieu tbody tr:hover td { background: #f8fbff; }
.khoi-tai-lieu .cot-tt { width: 44px; }
.khoi-tai-lieu .cot-tai-lieu-ten { width: 34%; font-weight: 600; color: #0f172a; }
.khoi-tai-lieu .cot-bat-buoc { width: 82px; }
.khoi-tai-lieu .cot-mo-doc { width: 80px; }
.khoi-tai-lieu .cot-ghi-chu { width: 26%; color: #475569; }
.cot-can-giua { text-align: center; vertical-align: middle; }
.nut { min-height: 29px; border: 1px solid #cbd5e1; border-radius: 4px; background: #fff; color: #111827; padding: 5px 9px; font-size: 12px; cursor: pointer; font-family: Roboto, Arial, sans-serif; }
.nut:hover { background: #f8fafc; }
.nut:disabled { cursor: not-allowed; opacity: .6; }
.nut.chinh { border-color: #2563eb; background: #2563eb; color: #fff; }
.nut.nguy-hiem { border-color: #fecaca; background: #fff1f2; color: #b91c1c; }
.nut.nho { min-height: 25px; padding: 3px 8px; font-size: 11px; font-weight: 700; }
.nut-sua { border-color: #bfdbfe; background: #eff6ff; color: #1d4ed8; }
.nut-sua:hover { background: #dbeafe; }
.nut-xoa { border-color: #fecaca; background: #fff1f2; color: #be123c; }
.nut-xoa:hover { background: #ffe4e6; }
.nut-mo-doc { border-color: #bbf7d0; background: #f0fdf4; color: #15803d; }
.nut-mo-doc:hover { background: #dcfce7; }
.nut-xem-file { border-color: #bfdbfe; background: #eff6ff; color: #1d4ed8; }
.nut-xem-file:hover { background: #dbeafe; }


.khoi-danh-gia-mau { border-color: #bfdbfe; }
.khoi-danh-gia-mau .tieu-de-the { background: #eff6ff; }
.khoi-danh-gia-mau .form-danh-gia-mau { grid-template-columns: repeat(4, minmax(120px, 1fr)); }
.dong-thong-tin-danh-gia {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin: 10px 10px 0;
  padding: 8px 10px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #f8fbff;
  color: #1e3a8a;
  font-size: 12px;
}
.tong-ty-le-danh-gia {
  margin: 10px;
  padding: 8px 10px;
  border: 1px solid #bfdbfe;
  border-radius: 6px;
  background: #f8fbff;
  font-size: 13px;
}
.canh-bao-inline {
  margin-left: 8px;
  font-weight: 700;
  color: #b91c1c;
}
.khoi-danh-gia-mau :deep(.bang-boc) {
  margin: 0 10px 10px;
  max-height: 360px;
  overflow: auto;
  border: 1px solid #bfdbfe;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.06);
}
.khoi-danh-gia-mau :deep(.bang-du-lieu) {
  width: 100%;
  min-width: 1280px;
  table-layout: fixed;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 12.5px;
}
.khoi-danh-gia-mau :deep(th) {
  position: sticky;
  top: 0;
  z-index: 6;
  border: 0;
  border-bottom: 1px solid #bfdbfe;
  background: linear-gradient(180deg, #eff6ff 0%, #dbeafe 100%);
  color: #1e40af;
  font-size: 12px;
  font-weight: 800;
  text-align: left;
  white-space: nowrap;
}
.khoi-danh-gia-mau :deep(td) {
  border: 0;
  border-bottom: 1px solid #dbeafe;
  background: #ffffff;
  color: #1f2937;
  line-height: 1.45;
  word-break: break-word;
}
.khoi-danh-gia-mau :deep(tbody tr:last-child td) { border-bottom: 0; }
.khoi-danh-gia-mau :deep(tbody tr:hover td) { background: #f8fbff; }
.khoi-danh-gia-mau :deep(.cot-id) { width: 58px; font-weight: 800; color: #1d4ed8; }
.khoi-danh-gia-mau :deep(.cot-syllabus-id) { width: 112px; }
.khoi-danh-gia-mau :deep(.cot-tt) { width: 54px; font-weight: 700; }
.khoi-danh-gia-mau :deep(.cot-ten-danh-gia) { width: 220px; color: #0f172a; font-weight: 800; }
.khoi-danh-gia-mau :deep(.cot-loai-diem) { width: 135px; }
.khoi-danh-gia-mau :deep(.cot-so-danh-gia) { width: 95px; }
.khoi-danh-gia-mau :deep(.cot-bat-buoc) { width: 85px; }
.khoi-danh-gia-mau :deep(.cot-ghi-chu) { width: 180px; color: #475569; }
.khoi-danh-gia-mau :deep(.cot-ngay) { width: 145px; color: #475569; }
.khoi-danh-gia-mau :deep(.cot-thao-tac) { width: 118px; text-align: center; }
.khoi-danh-gia-mau :deep(.thao-tac) { display: flex; justify-content: center; align-items: center; gap: 6px; min-width: 108px; }
.khoi-danh-gia-mau :deep(.nut) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 26px;
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 11.5px;
  font-weight: 800;
  line-height: 1;
}
.khoi-danh-gia-mau :deep(.nut-sua) { border: 1px solid #bfdbfe; background: #eff6ff; color: #1d4ed8; }
.khoi-danh-gia-mau :deep(.nut-sua:hover) { background: #dbeafe; }
.khoi-danh-gia-mau :deep(.nut-xoa) { border: 1px solid #fecaca; background: #fff1f2; color: #be123c; }
.khoi-danh-gia-mau :deep(.nut-xoa:hover) { background: #ffe4e6; }

.khoi-quy-doi .tieu-de-the { background: #fff7ed; }
.khoi-quy-doi .form-quy-doi { grid-template-columns: repeat(4, minmax(120px, 1fr)); }
.khoi-quy-doi :deep(.bang-boc) {
  margin: 0 10px 10px;
  max-height: 320px;
  overflow: auto;
  border: 1px solid #fed7aa;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.06);
}
.khoi-quy-doi :deep(.bang-du-lieu) {
  width: 100%;
  min-width: 1180px;
  table-layout: fixed;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 12.5px;
}
.khoi-quy-doi :deep(th) {
  position: sticky;
  top: 0;
  z-index: 6;
  border: 0;
  border-bottom: 1px solid #fed7aa;
  background: linear-gradient(180deg, #fffaf3 0%, #ffedd5 100%);
  color: #9a3412;
  font-size: 12px;
  font-weight: 800;
  text-align: left;
  white-space: nowrap;
}
.khoi-quy-doi :deep(td) {
  border: 0;
  border-bottom: 1px solid #ffedd5;
  background: #ffffff;
  color: #1f2937;
  line-height: 1.45;
}
.khoi-quy-doi :deep(tbody tr:hover td) { background: #fff7ed; }
.khoi-quy-doi :deep(.cot-tt) { width: 44px; text-align: center; color: #92400e; font-weight: 700; }
.khoi-quy-doi :deep(.cot-ma-quy-doi) { width: 120px; color: #9a3412; font-weight: 800; word-break: break-word; }
.khoi-quy-doi :deep(.cot-ten-quy-doi) { width: 200px; color: #111827; font-weight: 700; word-break: break-word; }
.khoi-quy-doi :deep(.cot-ghi-chu) { width: 170px; color: #475569; word-break: break-word; }
.khoi-quy-doi :deep(.cot-thao-tac) { width: 118px; text-align: center; }
.khoi-quy-doi :deep(.thao-tac) { display: flex; justify-content: center; align-items: center; gap: 6px; min-width: 108px; }

/* Bảng tài liệu mẫu: dùng :deep vì bảng được render trong component con BangDonGian */
.khoi-tai-lieu :deep(.bang-boc) {
  margin: 0 10px 10px;
  max-height: 340px;
  overflow: auto;
  border: 1px solid #d7e3f3;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.06);
}

.khoi-tai-lieu :deep(.bang-du-lieu) {
  width: 100%;
  min-width: 1180px;
  table-layout: fixed;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 12.5px;
}

.khoi-tai-lieu :deep(th) {
  position: sticky;
  top: 0;
  z-index: 6;
  border: 0;
  border-bottom: 1px solid #cfe0f5;
  background: linear-gradient(180deg, #f4f9ff 0%, #eaf3ff 100%);
  color: #1e3a8a;
  font-size: 12px;
  font-weight: 800;
  text-align: left;
  white-space: nowrap;
}

.khoi-tai-lieu :deep(td) {
  border: 0;
  border-bottom: 1px solid #edf2f7;
  background: #ffffff;
  color: #1f2937;
  line-height: 1.45;
}

.khoi-tai-lieu :deep(tbody tr:last-child td) {
  border-bottom: 0;
}

.khoi-tai-lieu :deep(tbody tr:hover td) {
  background: #f8fbff;
}

.khoi-tai-lieu :deep(.cot-tt) {
  width: 42px;
  text-align: center;
  color: #475569;
  font-weight: 700;
}

.khoi-tai-lieu :deep(.cot-ma-tai-lieu) {
  width: 110px;
  color: #1e3a8a;
  font-weight: 800;
  word-break: break-word;
}

.khoi-tai-lieu :deep(.cot-tai-lieu-ten) {
  width: 260px;
  color: #0f172a;
  font-weight: 700;
  word-break: break-word;
}

.khoi-tai-lieu :deep(.cot-loai-tai-lieu) {
  width: 90px;
}

.khoi-tai-lieu :deep(.cot-tac-gia) {
  width: 130px;
  word-break: break-word;
}

.khoi-tai-lieu :deep(.cot-nxb) {
  width: 130px;
  word-break: break-word;
}

.khoi-tai-lieu :deep(.cot-nam-xb) {
  width: 64px;
}

.khoi-tai-lieu :deep(.cot-bat-buoc) {
  width: 76px;
  text-align: center;
}

.khoi-tai-lieu :deep(.cot-xem-file) {
  width: 92px;
  text-align: center;
}

.khoi-tai-lieu :deep(.cot-mo-doc) {
  width: 78px;
  text-align: center;
}

.khoi-tai-lieu :deep(.cot-ghi-chu) {
  width: 170px;
  color: #475569;
  word-break: break-word;
}

.khoi-tai-lieu :deep(.cot-thao-tac) {
  width: 118px;
  text-align: center;
}

.khoi-tai-lieu :deep(.thao-tac) {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  min-width: 108px;
}

.khoi-tai-lieu :deep(.nut) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 26px;
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 11.5px;
  font-weight: 800;
  line-height: 1;
  cursor: pointer;
  transition: background .15s ease, border-color .15s ease, transform .15s ease;
}

.khoi-tai-lieu :deep(.nut:hover) {
  transform: translateY(-1px);
}

.khoi-tai-lieu :deep(.nut-sua) {
  border: 1px solid #bfdbfe;
  background: #eff6ff;
  color: #1d4ed8;
}

.khoi-tai-lieu :deep(.nut-sua:hover) {
  background: #dbeafe;
}

.khoi-tai-lieu :deep(.nut-xoa) {
  border: 1px solid #fecaca;
  background: #fff1f2;
  color: #be123c;
}

.khoi-tai-lieu :deep(.nut-xoa:hover) {
  background: #ffe4e6;
}

.khoi-tai-lieu :deep(.nut-xem-file) {
  border: 1px solid #bfdbfe;
  background: #eff6ff;
  color: #1d4ed8;
}

.khoi-tai-lieu :deep(.nut-xem-file:hover) {
  background: #dbeafe;
}

.khoi-tai-lieu :deep(.nut-mo-doc) {
  border: 1px solid #bbf7d0;
  background: #f0fdf4;
  color: #15803d;
}

.khoi-tai-lieu :deep(.nut-mo-doc:hover) {
  background: #dcfce7;
}



/* Bảng điều kiện mẫu: gọn, rõ cột, đồng bộ bảng tài liệu */
.khoi-dieu-kien :deep(.bang-boc) {
  margin: 0 10px 10px;
  max-height: 320px;
  overflow: auto;
  border: 1px solid #e2ddf5;
  border-radius: 8px;
  background: #ffffff;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.06);
}

.khoi-dieu-kien :deep(.bang-du-lieu) {
  width: 100%;
  min-width: 720px;
  table-layout: fixed;
  border-collapse: separate;
  border-spacing: 0;
  font-size: 12.5px;
}

.khoi-dieu-kien :deep(th) {
  position: sticky;
  top: 0;
  z-index: 6;
  border: 0;
  border-bottom: 1px solid #ddd6fe;
  background: linear-gradient(180deg, #faf7ff 0%, #f1ecff 100%);
  color: #4c1d95;
  font-size: 12px;
  font-weight: 800;
  text-align: left;
  white-space: nowrap;
}

.khoi-dieu-kien :deep(td) {
  border: 0;
  border-bottom: 1px solid #f1eefb;
  background: #ffffff;
  color: #1f2937;
  line-height: 1.45;
}

.khoi-dieu-kien :deep(tbody tr:last-child td) {
  border-bottom: 0;
}

.khoi-dieu-kien :deep(tbody tr:hover td) {
  background: #fbfaff;
}

.khoi-dieu-kien :deep(.cot-tt) {
  width: 42px;
  text-align: center;
  color: #6b7280;
  font-weight: 700;
}

.khoi-dieu-kien :deep(.cot-dieu-kien-noi-dung) {
  width: 48%;
  color: #111827;
  font-weight: 600;
  word-break: break-word;
}

.khoi-dieu-kien :deep(.cot-dieu-kien-loai) {
  width: 100px;
  text-align: center;
  color: #4c1d95;
  font-weight: 700;
}

.khoi-dieu-kien :deep(.cot-ghi-chu) {
  width: 24%;
  color: #475569;
  word-break: break-word;
}

.khoi-dieu-kien :deep(.cot-thao-tac) {
  width: 118px;
  text-align: center;
}

.khoi-dieu-kien :deep(.thao-tac) {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 6px;
  min-width: 108px;
}

.khoi-dieu-kien :deep(.nut) {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  min-height: 26px;
  border-radius: 999px;
  padding: 4px 10px;
  font-size: 11.5px;
  font-weight: 800;
  line-height: 1;
  cursor: pointer;
  transition: background .15s ease, border-color .15s ease, transform .15s ease;
}

.khoi-dieu-kien :deep(.nut:hover) {
  transform: translateY(-1px);
}

.khoi-dieu-kien :deep(.nut-sua) {
  border: 1px solid #c4b5fd;
  background: #f5f3ff;
  color: #6d28d9;
}

.khoi-dieu-kien :deep(.nut-sua:hover) {
  background: #ede9fe;
}

.khoi-dieu-kien :deep(.nut-xoa) {
  border: 1px solid #fecaca;
  background: #fff1f2;
  color: #be123c;
}

.khoi-dieu-kien :deep(.nut-xoa:hover) {
  background: #ffe4e6;
}
.khoi-dieu-kien :deep(.cot-dieu-kien-ma) {
  width: 120px;
  color: #4c1d95;
  font-weight: 800;
  word-break: break-word;
}
@media (max-width: 1350px) { .luoi-chinh { grid-template-columns: 1fr; } }
@media (max-width: 980px) { .luoi-trang { grid-template-columns: 1fr; } .danh-sach-mon { min-height: 0; max-height: 420px; } }
@media (max-width: 720px) { .khoi-dau, .thanh-mon { flex-direction: column; align-items: stretch; } .form-grid, .form-syllabus, .form-nho { grid-template-columns: 1fr; } }
</style>
