package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DapAnCauHoiService {
    Page<DapAnCauHoiResponse> findAll(Pageable pageable);
    DapAnCauHoiResponse findById(Long id);
    DapAnCauHoiResponse create(DapAnCauHoiRequest request);
    DapAnCauHoiResponse update(Long id, DapAnCauHoiRequest request);
    void delete(Long id);
}
