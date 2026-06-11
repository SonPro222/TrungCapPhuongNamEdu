package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionViTriViecLamResponse;

public interface ChuongTrinhVersionViTriViecLamService {
    Page<ChuongTrinhVersionViTriViecLamResponse> findAll(Long chuongTrinhVersionId, Long viTriMauId, String keyword, Pageable pageable);
    ChuongTrinhVersionViTriViecLamResponse findById(Long id);

    ChuongTrinhVersionViTriViecLamResponse create(ChuongTrinhVersionViTriViecLamRequest request);

    ChuongTrinhVersionViTriViecLamResponse update(Long id, ChuongTrinhVersionViTriViecLamRequest request);

    void delete(Long id);
}
