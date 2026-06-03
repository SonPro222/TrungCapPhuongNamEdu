package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiGocRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHoiGocService {
    Page<CauHoiGocResponse> findAll(Pageable pageable);
    CauHoiGocResponse findById(Long id);
    CauHoiGocResponse create(CauHoiGocRequest request);
    CauHoiGocResponse update(Long id, CauHoiGocRequest request);
    void delete(Long id);
}
