package org.example.trungcapphuongnam.module.daoTao.service;


import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhungKyGocService {

    Page<KhungKyGocResponse> findAll(String keyword, Pageable pageable);

    KhungKyGocResponse findById(Long id);

    KhungKyGocResponse create(KhungKyGocRequest request);

    KhungKyGocResponse update(Long id, KhungKyGocRequest request);

    void delete(Long id);
}