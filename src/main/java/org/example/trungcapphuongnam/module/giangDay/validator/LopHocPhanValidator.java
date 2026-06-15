package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.daoTao.entity.KhungKy;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LopHocPhanValidator {

    private final LopHocPhanRepository lopHocPhanRepository;
    private final ChuongTrinhMonRepository chuongTrinhMonRepository;
    private final LopHocPhanChuongTrinhMonRepository lopHocPhanChuongTrinhMonRepository;
    private final KhungKyRepository khungKyRepository;

    public void validateCreate(LopHocPhanRequest request) {
        validateCommon(request);

        if (lopHocPhanRepository.existsByMaLopIgnoreCase(request.getMaLop().trim())) {
            throw new GiangDayException("Mã lớp học phần đã tồn tại");
        }

        validateMotChuongTrinhMonChiCoMotLopHocPhan(null, request);
    }

    public void validateUpdate(Long id, LopHocPhanRequest request) {
        if (id == null) {
            throw new GiangDayException("Lớp học phần cần cập nhật không hợp lệ");
        }

        if (!lopHocPhanRepository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy lớp học phần cần cập nhật");
        }

        validateCommon(request);

        if (lopHocPhanRepository.existsByMaLopIgnoreCaseAndIdNot(request.getMaLop().trim(), id)) {
            throw new GiangDayException("Mã lớp học phần đã tồn tại");
        }

        validateMotChuongTrinhMonChiCoMotLopHocPhan(id, request);
    }

    private void validateCommon(LopHocPhanRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu lớp học phần không hợp lệ");
        }
        if (request.getLoaiLopHocPhan() == null) {
            throw new GiangDayException("Loại lớp học phần không được để trống");
        }

        if (request.getMaLop() == null || request.getMaLop().trim().isEmpty()) {
            throw new GiangDayException("Mã lớp học phần không được để trống");
        }

        if (request.getTenLop() == null || request.getTenLop().trim().isEmpty()) {
            throw new GiangDayException("Tên lớp học phần không được để trống");
        }

        Integer siSoToiThieu = request.getSiSoToiThieu();
        Integer siSoToiDa = request.getSoLuongToiDa();

        if (siSoToiThieu == null || siSoToiThieu < 1) {
            throw new GiangDayException("Sĩ số tối thiểu phải lớn hơn 0");
        }

        if (siSoToiDa == null || siSoToiDa < 1) {
            throw new GiangDayException("Sĩ số tối đa phải lớn hơn 0");
        }

        if (siSoToiDa < siSoToiThieu) {
            throw new GiangDayException("Sĩ số tối đa phải lớn hơn hoặc bằng sĩ số tối thiểu");
        }

        if (request.getSoLuongHienTai() != null && request.getSoLuongHienTai() < 0) {
            throw new GiangDayException("Số lượng hiện tại không được nhỏ hơn 0");
        }

        if (request.getSoLuongHienTai() != null && request.getSoLuongHienTai() > siSoToiDa) {
            throw new GiangDayException("Số lượng hiện tại không được vượt quá sĩ số tối đa");
        }

        // ngayBatDau/ngayKetThuc không còn là dữ liệu gốc của LHP - không validate
        // Thời gian học sẽ được tính từ bảng lịch học

        if (request.getLoaiLopHocPhan() == LoaiLopHocPhan.CHUYEN_NGANH) {
            validateLopChuyenNganh(request);
        }

        if (request.getLoaiLopHocPhan() == LoaiLopHocPhan.HOC_CHUNG) {
            validateLopHocChung(request);
        }
    }

    private void validateLopChuyenNganh(LopHocPhanRequest request) {
        if (request.getChuongTrinhMonId() == null) {
            throw new GiangDayException("Lớp chuyên ngành phải chọn chương trình môn");
        }

        ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(request.getChuongTrinhMonId())
                .orElseThrow(() -> new GiangDayException("Chương trình môn không tồn tại"));

        if (request.getMonHocId() != null && !request.getMonHocId().equals(chuongTrinhMon.getMonHocId())) {
            throw new GiangDayException("Môn học của lớp không khớp với chương trình môn chuyên ngành");
        }
    }

    private void validateLopHocChung(LopHocPhanRequest request) {
        if (request.getMonHocId() == null) {
            throw new GiangDayException("Lớp học chung phải chọn môn học chung");
        }

        Integer soBuoiHoc = request.getSoBuoiHoc();
        if (soBuoiHoc == null || soBuoiHoc < 1) {
            throw new GiangDayException("Lớp học chung phải cấu hình số buổi học lớn hơn 0");
        }
    }

    private void validateNgayTrongKhungKy(LopHocPhanRequest request) {
        ChuongTrinhMon chuongTrinhMon = chuongTrinhMonRepository.findById(request.getChuongTrinhMonId())
                .orElse(null);
        if (chuongTrinhMon == null || chuongTrinhMon.getKhungKyId() == null) {
            return; // Chưa gán vào kỳ → không cần validate
        }

        KhungKy khungKy = khungKyRepository.findById(chuongTrinhMon.getKhungKyId())
                .orElse(null);
        if (khungKy == null) {
            return;
        }


    }

    private void validateMotChuongTrinhMonChiCoMotLopHocPhan(Long idDangCapNhat, LopHocPhanRequest request) {
        if (request.getLoaiLopHocPhan() != LoaiLopHocPhan.CHUYEN_NGANH) {
            return;
        }

        if (request.getChuongTrinhMonId() == null) {
            return;
        }

        // Cho phép 1 ChuongTrinhMon có nhiều LopHocPhan chuyên ngành (đã bỏ giới hạn 1 lớp)

        if (lopHocPhanChuongTrinhMonRepository.existsByChuongTrinhMonId(request.getChuongTrinhMonId())) {
            throw new GiangDayException("Môn này đã được gắn vào lớp học chung, không được tạo thêm lớp chuyên ngành");
        }
    }
}