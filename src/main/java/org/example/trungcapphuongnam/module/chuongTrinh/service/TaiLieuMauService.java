package org.example.trungcapphuongnam.module.chuongTrinh.service;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.TaiLieuGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.TaiLieuGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaiLieuGocService {

    Page<TaiLieuGocResponse> findAll(String ma, String loai, String keyword, Pageable pageable);

    TaiLieuGocResponse findById(Long id);

    TaiLieuGocResponse create(TaiLieuGocRequest request);

    TaiLieuGocResponse update(Long id, TaiLieuGocRequest request);

    void delete(Long id);
}