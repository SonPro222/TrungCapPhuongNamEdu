package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiaoVienValidator {

    private final GiaoVienRepository repository;

    public void validateCreate(GiaoVienRequest request) {
        validateCommon(request);

        if (repository.existsByMaGiaoVienIgnoreCase(request.getMaGiaoVien())) {
            throw new GiangDayException("Mã giáo viên đã tồn tại");
        }

        if (repository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new GiangDayException("Email giáo viên đã tồn tại");
        }

        if (request.getTaiKhoanId() != null && repository.existsByTaiKhoanId(request.getTaiKhoanId())) {
            throw new GiangDayException("Tài khoản này đã được gắn với giáo viên khác");
        }
    }

    public void validateUpdate(Long id, GiaoVienRequest request) {
        if (id == null) {
            throw new GiangDayException("Giáo viên không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy giáo viên cần cập nhật");
        }

        validateCommon(request);

        if (repository.existsByMaGiaoVienIgnoreCaseAndIdNot(request.getMaGiaoVien(), id)) {
            throw new GiangDayException("Mã giáo viên đã tồn tại");
        }

        if (repository.existsByEmailIgnoreCaseAndIdNot(request.getEmail(), id)) {
            throw new GiangDayException("Email giáo viên đã tồn tại");
        }

        if (request.getTaiKhoanId() != null && repository.existsByTaiKhoanIdAndIdNot(request.getTaiKhoanId(), id)) {
            throw new GiangDayException("Tài khoản này đã được gắn với giáo viên khác");
        }
    }

    private void validateCommon(GiaoVienRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu giáo viên không hợp lệ");
        }

        if (request.getMaGiaoVien() == null || request.getMaGiaoVien().isBlank()) {
            throw new GiangDayException("Mã giáo viên không được để trống");
        }

        if (request.getHoTen() == null || request.getHoTen().isBlank()) {
            throw new GiangDayException("Họ tên giáo viên không được để trống");
        }

        if (request.getEmail() == null || request.getEmail().isBlank()) {
            throw new GiangDayException("Email giáo viên không được để trống");
        }

        if (request.getTrangThai() == null) {
            throw new GiangDayException("Trạng thái giáo viên không được để trống");
        }
    }
}