package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.daoTao.repository.KhungKyRepository;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiangVienDangKyGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.entity.GiangVienDangKyGiangDay;
import org.example.trungcapphuongnam.module.giangDay.repository.GiangVienDangKyGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GiangVienDangKyGiangDayValidator {
    private final GiangVienDangKyGiangDayRepository repository;
    private final GiaoVienRepository giaoVienRepository;
    private final KhungKyRepository khungKyRepository;

    public void validateCreate(GiangVienDangKyGiangDayRequest request) {
        validateCommon(request);
        if (repository.existsByGiaoVienIdAndKhungKyId(request.getGiaoVienId(), request.getKhungKyId())) {
            throw new GiangDayException("Giảng viên đã có đăng ký số tiết trong kỳ này");
        }
    }

    public void validateUpdate(Long id, GiangVienDangKyGiangDayRequest request) {
        if (id == null || !repository.existsById(id)) throw new GiangDayException("Đăng ký giảng dạy không tồn tại");
        validateCommon(request);
        if (repository.existsByGiaoVienIdAndKhungKyIdAndIdNot(request.getGiaoVienId(), request.getKhungKyId(), id)) {
            throw new GiangDayException("Giảng viên đã có đăng ký số tiết trong kỳ này");
        }
        GiangVienDangKyGiangDay current = repository.findById(id).orElseThrow();
        int daPhanCong = current.getSoTietDaPhanCong() != null ? current.getSoTietDaPhanCong() : 0;
        if (request.getSoTietDangKy() < daPhanCong) {
            throw new GiangDayException("Không thể giảm số tiết đăng ký xuống dưới số tiết đã phân công (" + daPhanCong + " tiết)");
        }
    }

    public void validateDelete(Long id) {
        GiangVienDangKyGiangDay entity = repository.findById(id)
                .orElseThrow(() -> new GiangDayException("Đăng ký giảng dạy không tồn tại"));
        int daPhanCong = entity.getSoTietDaPhanCong() != null ? entity.getSoTietDaPhanCong() : 0;
        if (daPhanCong > 0) {
            throw new GiangDayException("Không thể xóa đăng ký giảng dạy vì giảng viên đã được phân công tiết trong kỳ này.");
        }
    }

    private void validateCommon(GiangVienDangKyGiangDayRequest request) {
        if (request == null) throw new GiangDayException("Dữ liệu không hợp lệ");
        if (request.getGiaoVienId() == null) throw new GiangDayException("Giảng viên không được để trống");
        if (request.getKhungKyId() == null) throw new GiangDayException("Khung kỳ không được để trống");
        if (request.getSoTietDangKy() == null || request.getSoTietDangKy() <= 0) throw new GiangDayException("Số tiết đăng ký phải lớn hơn 0");
        if (!giaoVienRepository.existsById(request.getGiaoVienId())) throw new GiangDayException("Giảng viên không tồn tại");
        if (!khungKyRepository.existsById(request.getKhungKyId())) throw new GiangDayException("Khung kỳ không tồn tại");
    }
}
