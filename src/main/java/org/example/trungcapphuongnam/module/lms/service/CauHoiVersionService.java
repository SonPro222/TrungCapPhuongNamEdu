package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiVersionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHoiVersionService {
    Page<CauHoiVersionResponse> findAll(Pageable pageable);
    CauHoiVersionResponse findById(Long id);
    CauHoiVersionResponse create(CauHoiVersionRequest request);
    CauHoiVersionResponse update(Long id, CauHoiVersionRequest request);
    void delete(Long id);
}
