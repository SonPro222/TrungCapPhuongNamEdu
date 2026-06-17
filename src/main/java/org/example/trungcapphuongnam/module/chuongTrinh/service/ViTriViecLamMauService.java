package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamMauResponse;

public interface ViTriViecLamMauService {
    Page<ViTriViecLamMauResponse> findAll(Long syllabusChuongTrinhMauId, String ma, String keyword, Pageable pageable);
    ViTriViecLamMauResponse findById(Long id);

    ViTriViecLamMauResponse create(ViTriViecLamMauRequest request);

    ViTriViecLamMauResponse update(Long id, ViTriViecLamMauRequest request);

    void delete(Long id);
}
