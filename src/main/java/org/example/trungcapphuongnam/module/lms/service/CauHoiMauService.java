package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiMauRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHoiMauService {
    Page<CauHoiMauResponse> findAll(Pageable pageable);
    CauHoiMauResponse findById(Long id);
    CauHoiMauResponse create(CauHoiMauRequest request);
    CauHoiMauResponse update(Long id, CauHoiMauRequest request);
    void delete(Long id);
}
