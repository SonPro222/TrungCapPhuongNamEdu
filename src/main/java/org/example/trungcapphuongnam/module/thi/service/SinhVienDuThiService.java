package org.example.trungcapphuongnam.module.thi.service;

import org.example.trungcapphuongnam.module.thi.dto.request.SinhVienDuThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.SinhVienDuThiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SinhVienDuThiService {
    Page<SinhVienDuThiResponse> findAll(Pageable pageable);
    SinhVienDuThiResponse findById(Long id);
    SinhVienDuThiResponse create(SinhVienDuThiRequest request);
    SinhVienDuThiResponse update(Long id, SinhVienDuThiRequest request);
    void delete(Long id);
}
