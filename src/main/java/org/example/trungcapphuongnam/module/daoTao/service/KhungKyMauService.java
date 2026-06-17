package org.example.trungcapphuongnam.module.daoTao.service;


import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyMauRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhungKyMauService {

    Page<KhungKyMauResponse> findAll(String keyword, Pageable pageable);

    KhungKyMauResponse findById(Long id);

    KhungKyMauResponse create(KhungKyMauRequest request);

    KhungKyMauResponse update(Long id, KhungKyMauRequest request);

    void delete(Long id);
}