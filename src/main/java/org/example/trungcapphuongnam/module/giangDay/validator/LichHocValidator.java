package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.entity.LopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LopHocPhanRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LichHocValidator {

    private final LopHocPhanRepository lopHocPhanRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final PhongHocRepository phongHocRepository;
    private final CaHocRepository caHocRepository;

    public void validateCreate(LichHocRequest request) {
        validateCommon(request);
    }

    public void validateUpdate(Long id, LichHocRequest request) {
        if (id == null) {
            throw new GiangDayException("Lịch học không hợp lệ");
        }

        validateCommon(request);
    }

    private void validateCommon(LichHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu lịch học không hợp lệ");
        }

        if (request.getLopHocPhanId() == null) {
            throw new GiangDayException("Lớp học phần không được để trống");
        }

        if (request.getNgayHoc() == null) {
            throw new GiangDayException("Ngày học không được để trống");
        }

        LopHocPhan lopHocPhan = lopHocPhanRepository.findById(request.getLopHocPhanId())
                .orElseThrow(() -> new GiangDayException("Lớp học phần không tồn tại"));

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.huy) {
            throw new GiangDayException("Lớp học phần đã hủy, không được tạo lịch học");
        }

        if (lopHocPhan.getTrangThai() == TrangThaiLopHocPhan.da_ket_thuc) {
            throw new GiangDayException("Lớp học phần đã kết thúc, không được tạo lịch học");
        }

        if (request.getGiaoVienId() != null) {
            GiaoVien giaoVien = giaoVienRepository.findById(request.getGiaoVienId())
                    .orElseThrow(() -> new GiangDayException("Giáo viên không tồn tại"));

            if (giaoVien.getTrangThai() != TrangThaiGiaoVien.dang_day) {
                throw new GiangDayException("Chỉ giáo viên đang dạy mới được xếp lịch");
            }
        }

        if (request.getPhongHocId() != null && !phongHocRepository.existsById(request.getPhongHocId())) {
            throw new GiangDayException("Phòng học không tồn tại");
        }

        if (request.getCaHocId() != null && !caHocRepository.existsById(request.getCaHocId())) {
            throw new GiangDayException("Ca học không tồn tại");
        }

        if (request.getTrangThai() == null) {
            throw new GiangDayException("Trạng thái lịch học không được để trống");
        }
    }
}