package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuChinhDiemBaiTapRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuChinhDiemBaiTapResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LichSuChinhDiemBaiTapService {
    Page<LichSuChinhDiemBaiTapResponse> findAll(Pageable pageable);
    LichSuChinhDiemBaiTapResponse findById(Long id);
    LichSuChinhDiemBaiTapResponse create(LichSuChinhDiemBaiTapRequest request);
    LichSuChinhDiemBaiTapResponse update(Long id, LichSuChinhDiemBaiTapRequest request);
    void delete(Long id);
}
