package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepMauResponse;

public interface DieuKienTotNghiepMauService {
    Page<DieuKienTotNghiepMauResponse> findAll(Long syllabusChuongTrinhMauId, String ma, String keyword, Pageable pageable);
    DieuKienTotNghiepMauResponse findById(Long id);

    DieuKienTotNghiepMauResponse create(DieuKienTotNghiepMauRequest request);

    DieuKienTotNghiepMauResponse update(Long id, DieuKienTotNghiepMauRequest request);

    void delete(Long id);
}
