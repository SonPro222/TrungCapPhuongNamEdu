package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuCauHoiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LichSuCauHoiService {
    Page<LichSuCauHoiResponse> findAll(Pageable pageable);
    LichSuCauHoiResponse findById(Long id);
    LichSuCauHoiResponse create(LichSuCauHoiRequest request);
    LichSuCauHoiResponse update(Long id, LichSuCauHoiRequest request);
    void delete(Long id);
}
