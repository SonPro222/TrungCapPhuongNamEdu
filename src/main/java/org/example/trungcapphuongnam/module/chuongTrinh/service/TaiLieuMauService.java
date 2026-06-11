package org.example.trungcapphuongnam.module.chuongTrinh.service;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.TaiLieuMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.TaiLieuMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaiLieuMauService {

    Page<TaiLieuMauResponse> findAll(String ma, String loai, String keyword, Pageable pageable);

    TaiLieuMauResponse findById(Long id);

    TaiLieuMauResponse create(TaiLieuMauRequest request);

    TaiLieuMauResponse update(Long id, TaiLieuMauRequest request);

    void delete(Long id);
}