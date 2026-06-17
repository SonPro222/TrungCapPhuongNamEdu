package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienMonHocRequest;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienMonHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiaoVienMonHocValidator {
    private final GiaoVienMonHocRepository repository;
    private final GiaoVienRepository giaoVienRepository;
    private final MonHocRepository monHocRepository;

    public void validateCreate(GiaoVienMonHocRequest request) {
        validateCommon(request);
        if (repository.existsByGiaoVienIdAndMonHocId(request.getGiaoVienId(), request.getMonHocId())) {
            throw new GiangDayException("Giảng viên đã được cấu hình dạy môn học này");
        }
    }

    public void validateUpdate(Long id, GiaoVienMonHocRequest request) {
        if (id == null || !repository.existsById(id)) throw new GiangDayException("Cấu hình giảng viên dạy môn không tồn tại");
        validateCommon(request);
        if (repository.existsByGiaoVienIdAndMonHocIdAndIdNot(request.getGiaoVienId(), request.getMonHocId(), id)) {
            throw new GiangDayException("Giảng viên đã được cấu hình dạy môn học này");
        }
    }

    private void validateCommon(GiaoVienMonHocRequest request) {
        if (request == null) throw new GiangDayException("Dữ liệu không hợp lệ");
        if (request.getGiaoVienId() == null) throw new GiangDayException("Giảng viên không được để trống");
        if (request.getMonHocId() == null) throw new GiangDayException("Môn học không được để trống");
        if (!giaoVienRepository.existsById(request.getGiaoVienId())) throw new GiangDayException("Giảng viên không tồn tại");
        if (!monHocRepository.existsById(request.getMonHocId())) throw new GiangDayException("Môn học không tồn tại");
    }
}
