package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepGocResponse;

public interface DieuKienTotNghiepGocService {
    Page<DieuKienTotNghiepGocResponse> findAll(String ma, String keyword, Pageable pageable);
    DieuKienTotNghiepGocResponse findById(Long id);

    DieuKienTotNghiepGocResponse create(DieuKienTotNghiepGocRequest request);

    DieuKienTotNghiepGocResponse update(Long id, DieuKienTotNghiepGocRequest request);

    void delete(Long id);
}
