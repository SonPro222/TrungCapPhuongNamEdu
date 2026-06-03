package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.RubricCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.RubricCauHoiVersionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RubricCauHoiVersionService {
    Page<RubricCauHoiVersionResponse> findAll(Pageable pageable);
    RubricCauHoiVersionResponse findById(Long id);
    RubricCauHoiVersionResponse create(RubricCauHoiVersionRequest request);
    RubricCauHoiVersionResponse update(Long id, RubricCauHoiVersionRequest request);
    void delete(Long id);
}
