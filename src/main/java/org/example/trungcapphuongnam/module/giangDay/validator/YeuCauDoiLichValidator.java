package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class YeuCauDoiLichValidator {

    private final LichHocRepository lichHocRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final CaHocRepository caHocRepository;
    private final PhongHocRepository phongHocRepository;

    public void validateCreate(YeuCauDoiLichRequest request) {
        validateCommon(request);
    }

    public void validateUpdate(Long id, YeuCauDoiLichRequest request) {
        if (id == null) {
            throw new GiangDayException("Yêu cầu đổi lịch không hợp lệ");
        }

        validateCommon(request);
    }

    private void validateCommon(YeuCauDoiLichRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu yêu cầu đổi lịch không hợp lệ");
        }

        if (request.getLichHocCuId() == null) {
            throw new GiangDayException("Lịch học cũ không được để trống");
        }

        if (!lichHocRepository.existsById(request.getLichHocCuId())) {
            throw new GiangDayException("Lịch học cũ không tồn tại");
        }

        if (request.getGiaoVienId() == null) {
            throw new GiangDayException("Giáo viên yêu cầu không được để trống");
        }

        if (!giaoVienRepository.existsById(request.getGiaoVienId())) {
            throw new GiangDayException("Giáo viên yêu cầu không tồn tại");
        }

        if (request.getNgayMoi() == null) {
            throw new GiangDayException("Ngày mới không được để trống");
        }

        if (request.getCaHocMoiId() != null && !caHocRepository.existsById(request.getCaHocMoiId())) {
            throw new GiangDayException("Ca học mới không tồn tại");
        }

        if (request.getPhongHocMoiId() != null && !phongHocRepository.existsById(request.getPhongHocMoiId())) {
            throw new GiangDayException("Phòng học mới không tồn tại");
        }

        if (request.getLyDo() == null || request.getLyDo().isBlank()) {
            throw new GiangDayException("Lý do đổi lịch không được để trống");
        }

        if (request.getTrangThai() == null) {
            throw new GiangDayException("Trạng thái yêu cầu đổi lịch không được để trống");
        }
    }
}