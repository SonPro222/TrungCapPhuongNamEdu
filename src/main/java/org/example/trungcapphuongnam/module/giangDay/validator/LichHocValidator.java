package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienKhaDungRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.NgayNghiRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhanCongGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.SinhVienLopHocPhanRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LichHocValidator {

    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final PhongHocRepository phongHocRepository;
    private final CaHocRepository caHocRepository;
    private final LichHocRepository lichHocRepository;
    private final SinhVienLopHocPhanRepository sinhVienLopHocPhanRepository;
    private final PhanCongGiangDayRepository phanCongGiangDayRepository;
    private final NgayNghiRepository ngayNghiRepository;
    private final GiaoVienKhaDungRepository giaoVienKhaDungRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;

    public void validateCreate(LichHocRequest request) {
        validateCommon(null, request);
    }

    public void validateUpdate(Long id, LichHocRequest request) {
        if (id == null) {
            throw new GiangDayException("Lịch học không hợp lệ");
        }

        if (!lichHocRepository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy lịch học cần cập nhật");
        }

        validateCommon(id, request);
    }

    private void validateCommon(Long idDangCapNhat, LichHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu lịch học không hợp lệ");
        }

        if (request.getLopHocPhanId() == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        if (request.getNgayHoc() == null) {
            throw new GiangDayException("Ngày học không được để trống");
        }

        if (request.getTrangThai() == null) {
            throw new GiangDayException("Trạng thái lịch học không được để trống");
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(request.getLopHocPhanId())
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được tạo lịch học");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được tạo lịch học");
        }

        long soSinhVien = sinhVienLopHocPhanRepository.countByLopHocPhanIdAndTrangThaiIn(
                lopHocPhan.getId(),
                List.of(
                        TrangThaiSinhVienLopHocPhan.da_dang_ky,
                        TrangThaiSinhVienLopHocPhan.dang_hoc,
                        TrangThaiSinhVienLopHocPhan.hoc_lai
                )
        );
        if (soSinhVien < 1) {
            throw new GiangDayException("Lớp học phần chưa có sinh viên đang học, không được xếp lịch");
        }



        if (request.getGiaoVienId() != null) {
            GiaoVien giaoVien = giaoVienRepository.findById(request.getGiaoVienId())
                    .orElseThrow(() -> new GiangDayException("Giáo viên không tồn tại"));

            if (giaoVien.getTrangThai() != TrangThaiGiaoVien.dang_day) {
                throw new GiangDayException("Chỉ giáo viên đang dạy mới được xếp lịch");
            }

            boolean daPhanCong = phanCongGiangDayRepository.existsByLopHocPhanIdAndGiaoVienId(
                    request.getLopHocPhanId(),
                    request.getGiaoVienId()
            );
            if (!daPhanCong) {
                throw new GiangDayException("Giáo viên chưa được phân công cho lớp học phần này");
            }
        }

        if (request.getPhongHocId() != null) {
            PhongHoc phongHoc = phongHocRepository.findById(request.getPhongHocId())
                    .orElseThrow(() -> new GiangDayException("Phòng học không tồn tại"));

            if (phongHoc.getTrangThai() != TrangThaiPhongHoc.dang_su_dung) {
                throw new GiangDayException("Chỉ phòng học đang sử dụng mới được xếp lịch");
            }

            if (phongHoc.getSucChua() != null
                    && lopHocPhan.getSoLuongHienTai() != null
                    && phongHoc.getSucChua() < lopHocPhan.getSoLuongHienTai()) {
                throw new GiangDayException("Phòng học không đủ sức chứa");
            }
        }

        if (request.getCaHocId() != null && !caHocRepository.existsById(request.getCaHocId())) {
            throw new GiangDayException("Ca học không tồn tại");
        }

        if (request.getTrangThai() == TrangThaiLichHoc.nghi) {
            return;
        }

        if (request.getCaHocId() == null) {
            throw new GiangDayException("Ca học không được để trống khi xếp lịch học");
        }

        // Lấy version/kỳ của lớp học phần để check ngày nghỉ đúng phạm vi
        Long[] versionKy = layChuongTrinhVersionVaKhungKy(lopHocPhan);
        if (ngayNghiRepository.existsNgayNghiApDungTheoPhamVi(
                request.getNgayHoc(), versionKy[0], versionKy[1])) {
            throw new GiangDayException("Ngày học rơi vào ngày nghỉ áp dụng cho lớp học phần này, không thể xếp lịch.");
        }

        Integer soBuoiHoc = lopHocPhan.getSoBuoiHoc();
        if (soBuoiHoc == null || soBuoiHoc < 1) {
            throw new GiangDayException("Lớp học phần chưa cấu hình số buổi học");
        }

        long soBuoiDaXep = lichHocRepository.countSoBuoiDangTinh(
                request.getLopHocPhanId(),
                TrangThaiLichHoc.nghi,
                idDangCapNhat
        );

        if (soBuoiDaXep >= soBuoiHoc) {
            throw new GiangDayException("Lớp học phần đã xếp đủ số buổi học");
        }

        boolean trungLop = idDangCapNhat == null
                ? lichHocRepository.existsByLopHocPhanIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                request.getLopHocPhanId(), request.getNgayHoc(), request.getCaHocId(), TrangThaiLichHoc.nghi)
                : lichHocRepository.existsByLopHocPhanIdAndNgayHocAndCaHocIdAndTrangThaiNotAndIdNot(
                request.getLopHocPhanId(), request.getNgayHoc(), request.getCaHocId(), TrangThaiLichHoc.nghi, idDangCapNhat);

        if (trungLop) {
            throw new GiangDayException("Lớp học phần đã có lịch học trong ngày và ca này");
        }

        if (request.getGiaoVienId() != null) {
            // Kiểm tra giáo viên đã đăng ký bận (loaiDangKy = ban) cho slot này
            if (giaoVienKhaDungRepository.existsGiaoVienBan(
                    request.getGiaoVienId(),
                    tinhThuTrongTuan(request.getNgayHoc()),
                    request.getCaHocId(),
                    request.getNgayHoc()
            )) {
                throw new GiangDayException("Giáo viên đã đăng ký bận trong thời điểm này, không được xếp lịch");
            }

            boolean trungGiaoVien = idDangCapNhat == null
                    ? lichHocRepository.existsByGiaoVienIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                    request.getGiaoVienId(), request.getNgayHoc(), request.getCaHocId(), TrangThaiLichHoc.nghi)
                    : lichHocRepository.existsByGiaoVienIdAndNgayHocAndCaHocIdAndTrangThaiNotAndIdNot(
                    request.getGiaoVienId(), request.getNgayHoc(), request.getCaHocId(), TrangThaiLichHoc.nghi, idDangCapNhat);

            if (trungGiaoVien) {
                throw new GiangDayException("Giáo viên đã có lịch dạy trong ngày và ca này");
            }
        }

        if (request.getPhongHocId() != null) {
            boolean trungPhong = idDangCapNhat == null
                    ? lichHocRepository.existsByPhongHocIdAndNgayHocAndCaHocIdAndTrangThaiNot(
                    request.getPhongHocId(), request.getNgayHoc(), request.getCaHocId(), TrangThaiLichHoc.nghi)
                    : lichHocRepository.existsByPhongHocIdAndNgayHocAndCaHocIdAndTrangThaiNotAndIdNot(
                    request.getPhongHocId(), request.getNgayHoc(), request.getCaHocId(), TrangThaiLichHoc.nghi, idDangCapNhat);

            if (trungPhong) {
                throw new GiangDayException("Phòng học đã có lịch học trong ngày và ca này");
            }
        }

        if (lichHocRepository.existsTrungLichSinhVien(
                request.getLopHocPhanId(),
                request.getNgayHoc(),
                request.getCaHocId(),
                idDangCapNhat
        )) {
            throw new GiangDayException("Có sinh viên trong lớp bị trùng lịch học ở ngày và ca này");
        }
    }

    private int tinhThuTrongTuan(LocalDate ngayHoc) {
        if (ngayHoc == null) return 0;
        int dayOfWeek = ngayHoc.getDayOfWeek().getValue();
        return dayOfWeek == 7 ? 8 : dayOfWeek + 1;
    }

    /**
     * Lấy [chuongTrinhVersionId, khungKyId] của lớp học phần.
     * Ưu tiên lấy từ chuongTrinhMonId trực tiếp trên LopHocPhan.
     * Nếu không có, trả về [null, null] → chỉ check nghỉ toàn trường.
     */
    private Long[] layChuongTrinhVersionVaKhungKy(LopHocPhan lop) {
        if (lop.getChuongTrinhMonId() != null) {
            Optional<ChuongTrinhMon> ctm = chuongTrinhMonRepository.findById(lop.getChuongTrinhMonId());
            if (ctm.isPresent()) {
                return new Long[]{ctm.get().getChuongTrinhVersionId(), ctm.get().getKhungKyId()};
            }
        }
        return new Long[]{null, null};
    }
}
