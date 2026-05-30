package org.example.trungcapphuongnam.module.chuongTrinh.service;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DieuKienMonHocGocService {

    Page<DieuKienMonHocGocResponse> findAll(String ma, String loai, String keyword, Pageable pageable);

    DieuKienMonHocGocResponse findById(Long id);

    DieuKienMonHocGocResponse create(DieuKienMonHocGocRequest request);

    DieuKienMonHocGocResponse update(Long id, DieuKienMonHocGocRequest request);

    void delete(Long id);
}