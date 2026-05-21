package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTienQuyetRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTienQuyetResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonTienQuyetService {
    Page<MonTienQuyetResponse> findAll(Long monId, Long monDieuKienId, String loai, String keyword, Pageable pageable);
    MonTienQuyetResponse findById(Long id);
    MonTienQuyetResponse create(MonTienQuyetRequest request);
    MonTienQuyetResponse update(Long id, MonTienQuyetRequest request);
    void delete(Long id);
}
