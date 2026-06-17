package org.example.trungcapphuongnam.module.giangDay.validator;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiemDanhValidator {

    private final DiemDanhRepository repository;
    private final LichHocRepository lichHocRepository;
    private final SinhVienRepository sinhVienRepository;

    public void validateCreate(DiemDanhRequest request) {
        validateCommon(request);

        if (repository.existsByLichHocIdAndSinhVienId(request.getLichHocId(), request.getSinhVienId())) {
            throw new GiangDayException("Sinh viên đã được điểm danh trong buổi học này");
        }
    }

    public void validateUpdate(Long id, DiemDanhRequest request) {
        if (id == null) {
            throw new GiangDayException("Điểm danh không hợp lệ");
        }

        if (!repository.existsById(id)) {
            throw new GiangDayException("Không tìm thấy bản ghi điểm danh cần cập nhật");
        }

        validateCommon(request);

        if (repository.existsByLichHocIdAndSinhVienIdAndIdNot(
                request.getLichHocId(),
                request.getSinhVienId(),
                id
        )) {
            throw new GiangDayException("Sinh viên đã được điểm danh trong buổi học này");
        }
    }

    private void validateCommon(DiemDanhRequest request) {
        if (request == null) {
            throw new GiangDayException("Dữ liệu điểm danh không hợp lệ");
        }

        if (request.getLichHocId() == null) {
            throw new GiangDayException("Lịch học không được để trống");
        }

        if (request.getSinhVienId() == null) {
            throw new GiangDayException("Sinh viên không được để trống");
        }

        if (request.getTrangThai() == null) {
            throw new GiangDayException("Trạng thái điểm danh không được để trống");
        }

        LichHoc lichHoc = lichHocRepository.findById(request.getLichHocId())
                .orElseThrow(() -> new GiangDayException("Lịch học không tồn tại"));

        if (!sinhVienRepository.existsById(request.getSinhVienId())) {
            throw new GiangDayException("Sinh viên không tồn tại");
        }

        if (lichHoc.getLopHocPhanId() == null) {
            throw new GiangDayException("Lịch học chưa gắn lớp học phần");
        }
    }
}