package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuDayDiemLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuDayDiemLmsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LichSuDayDiemLmsService {
    Page<LichSuDayDiemLmsResponse> findAll(Pageable pageable);
    LichSuDayDiemLmsResponse findById(Long id);
    LichSuDayDiemLmsResponse create(LichSuDayDiemLmsRequest request);
    LichSuDayDiemLmsResponse update(Long id, LichSuDayDiemLmsRequest request);
    void delete(Long id);
}
