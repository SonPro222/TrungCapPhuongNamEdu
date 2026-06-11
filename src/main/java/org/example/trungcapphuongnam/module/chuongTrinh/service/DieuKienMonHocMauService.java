package org.example.trungcapphuongnam.module.chuongTrinh.service;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DieuKienMonHocMauService {

    Page<DieuKienMonHocMauResponse> findAll(String ma, String loai, String keyword, Pageable pageable);

    DieuKienMonHocMauResponse findById(Long id);

    DieuKienMonHocMauResponse create(DieuKienMonHocMauRequest request);

    DieuKienMonHocMauResponse update(Long id, DieuKienMonHocMauRequest request);

    void delete(Long id);
}