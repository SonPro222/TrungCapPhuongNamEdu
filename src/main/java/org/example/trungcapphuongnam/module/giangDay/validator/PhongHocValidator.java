package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhongHocValidator {

    private final PhongHocRepository repository;

    public void validateCreate(PhongHocRequest request) {
        validateCommon(request);

        if (repository.existsByMaPhongIgnoreCase(request.getMaPhong())) {
            throw new GiangDayException("Mã phòng học đã tồn tại");
        }
    }

    public void validateUpdate(Long id, PhongHocRequest request) {
        if (id == null) {
            throw new GiangDayException("Phòng học không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy phòng học cần cập nhật");
        }

        validateCommon(request);

        if (repository.existsByMaPhongIgnoreCaseAndIdNot(request.getMaPhong(), id)) {
            throw new GiangDayException("Mã phòng học đã tồn tại");
        }
    }

    private void validateCommon(PhongHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu phòng học không hợp lệ");
        }

        if (request.getMaPhong() == null || request.getMaPhong().isBlank()) {
            throw new GiangDayException("Mã phòng không được để trống");
        }

        if (request.getTenPhong() == null || request.getTenPhong().isBlank()) {
            throw new GiangDayException("Tên phòng không được để trống");
        }

        if (request.getSucChua() != null && request.getSucChua() < 0) {
            throw new GiangDayException("Sức chứa không hợp lệ");
        }

        if (request.getTrangThai() == null) {
            throw new GiangDayException("Trạng thái phòng học không được để trống");
        }
    }
}