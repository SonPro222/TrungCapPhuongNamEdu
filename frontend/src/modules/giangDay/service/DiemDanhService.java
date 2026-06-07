package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DiemDanhService {

    Page<DiemDanhResponse> getAll(
            Long lichHocId,
            Long lopHocPhanId,
            Long sinhVienId,
            String keywordSinhVien,
            TrangThaiDiemDanh trangThai,
            Pageable pageable
    );

    DiemDanhResponse getById(Long id);

    DiemDanhResponse create(DiemDanhRequest request);

    DiemDanhResponse update(Long id, DiemDanhRequest request);

    void delete(Long id);
}