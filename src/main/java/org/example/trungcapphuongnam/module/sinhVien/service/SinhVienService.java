package org.example.trungcapphuongnam.module.sinhVien.service;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SinhVienService {
    Page<SinhVienResponse> findAll(Pageable pageable);
    SinhVienResponse findById(Long id);
    SinhVienResponse create(SinhVienRequest request);
    SinhVienResponse update(Long id, SinhVienRequest request);
    void delete(Long id);
}
