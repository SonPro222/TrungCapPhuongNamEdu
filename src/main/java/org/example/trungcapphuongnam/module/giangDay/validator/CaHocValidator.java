package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CaHocValidator {

    private final CaHocRepository repository;

    public void validateCreate(CaHocRequest request) {
        validateCommon(request);

        if (repository.existsByMaCaIgnoreCase(request.getMaCa())) {
            throw new GiangDayException("Mã ca học đã tồn tại");
        }
    }

    public void validateUpdate(Long id, CaHocRequest request) {
        if (id == null) {
            throw new GiangDayException("Ca học không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy ca học cần cập nhật");
        }

        validateCommon(request);

        if (repository.existsByMaCaIgnoreCaseAndIdNot(request.getMaCa(), id)) {
            throw new GiangDayException("Mã ca học đã tồn tại");
        }
    }

    private void validateCommon(CaHocRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu ca học không hợp lệ");
        }

        if (request.getMaCa() == null || request.getMaCa().isBlank()) {
            throw new GiangDayException("Mã ca học không được để trống");
        }

        if (request.getTenCa() == null || request.getTenCa().isBlank()) {
            throw new GiangDayException("Tên ca học không được để trống");
        }

        if (request.getGioBatDau() == null) {
            throw new GiangDayException("Giờ bắt đầu không được để trống");
        }

        if (request.getGioKetThuc() == null) {
            throw new GiangDayException("Giờ kết thúc không được để trống");
        }

        if (!request.getGioKetThuc().isAfter(request.getGioBatDau())) {
            throw new GiangDayException("Giờ kết thúc phải sau giờ bắt đầu");
        }
    }
}