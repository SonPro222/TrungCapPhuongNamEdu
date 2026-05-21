package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamGocResponse;

public interface ViTriViecLamGocService {
    Page<ViTriViecLamGocResponse> findAll(String ma, String keyword, Pageable pageable);
    ViTriViecLamGocResponse findById(Long id);

    ViTriViecLamGocResponse create(ViTriViecLamGocRequest request);

    ViTriViecLamGocResponse update(Long id, ViTriViecLamGocRequest request);

    void delete(Long id);
}
