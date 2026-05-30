package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
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
        Integer soBuoiHoc = request.getSoBuoiHoc();

        if (soBuoiHoc == null || soBuoiHoc < 1) {
            throw new GiangDayException("Số buổi học phải lớn hơn 0");
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

        if (request.getNgayBatDau() != null
                && request.getNgayKetThuc() != null
                && request.getNgayKetThuc().isBefore(request.getNgayBatDau())) {
            throw new GiangDayException("Ngày kết thúc không được trước ngày bắt đầu");
        }

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
    }

    private void validateMotChuongTrinhMonChiCoMotLopHocPhan(Long idDangCapNhat, LopHocPhanRequest request) {
        if (request.getLoaiLopHocPhan() != LoaiLopHocPhan.CHUYEN_NGANH) {
            return;
        }

        if (request.getChuongTrinhMonId() == null) {
            return;
        }

        boolean daCoLopChuyenNganh = idDangCapNhat == null
                ? lopHocPhanRepository.existsByChuongTrinhMonId(request.getChuongTrinhMonId())
                : lopHocPhanRepository.existsByChuongTrinhMonIdAndIdNot(request.getChuongTrinhMonId(), idDangCapNhat);

        if (daCoLopChuyenNganh) {
            throw new GiangDayException("Môn này đã có lớp học phần chuyên ngành, không được tạo thêm lớp mới");
        }

        if (lopHocPhanChuongTrinhMonRepository.existsByChuongTrinhMonId(request.getChuongTrinhMonId())) {
            throw new GiangDayException("Môn này đã được gắn vào lớp học chung, không được tạo thêm lớp chuyên ngành");
        }
    }
}