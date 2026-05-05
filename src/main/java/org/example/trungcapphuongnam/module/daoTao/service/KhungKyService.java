package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhungKyService {
    Page<KhungKyResponse> findAll(Pageable pageable);
    KhungKyResponse findById(Long id);
    KhungKyResponse create(KhungKyRequest request);
    KhungKyResponse update(Long id, KhungKyRequest request);
    void delete(Long id);
}
