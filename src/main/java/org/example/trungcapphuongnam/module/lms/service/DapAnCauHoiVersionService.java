package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiVersionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DapAnCauHoiVersionService {
    Page<DapAnCauHoiVersionResponse> findAll(Pageable pageable);
    DapAnCauHoiVersionResponse findById(Long id);
    DapAnCauHoiVersionResponse create(DapAnCauHoiVersionRequest request);
    DapAnCauHoiVersionResponse update(Long id, DapAnCauHoiVersionRequest request);
    void delete(Long id);
}
