package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonHocService {
    Page<MonHocResponse> findAll(Pageable pageable);
    MonHocResponse findById(Long id);
    MonHocResponse create(MonHocRequest request);
    MonHocResponse update(Long id, MonHocRequest request);
    void delete(Long id);
}
