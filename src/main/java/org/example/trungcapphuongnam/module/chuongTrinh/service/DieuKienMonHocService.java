package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DieuKienMonHocService {
    Page<DieuKienMonHocResponse> findAll(Pageable pageable);
    DieuKienMonHocResponse findById(Long id);
    DieuKienMonHocResponse create(DieuKienMonHocRequest request);
    DieuKienMonHocResponse update(Long id, DieuKienMonHocRequest request);
    void delete(Long id);
}
