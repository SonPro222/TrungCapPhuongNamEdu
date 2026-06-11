package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DieuKienTotNghiepService {
    Page<DieuKienTotNghiepResponse> findAll(Long syllabusChuongTrinhId, Long chuongTrinhVersionId, String keyword, Pageable pageable);
    DieuKienTotNghiepResponse findById(Long id);
    DieuKienTotNghiepResponse create(DieuKienTotNghiepRequest request);
    DieuKienTotNghiepResponse update(Long id, DieuKienTotNghiepRequest request);
    void delete(Long id);
}
