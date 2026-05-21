package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ViTriViecLamService {
    Page<ViTriViecLamResponse> findAll(Long chuongTrinhVersionId, String keyword, Pageable pageable);
    ViTriViecLamResponse findById(Long id);
    ViTriViecLamResponse create(ViTriViecLamRequest request);
    ViTriViecLamResponse update(Long id, ViTriViecLamRequest request);
    void delete(Long id);
}
