package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.BaiNopSinhVienRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiNopSinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiNopSinhVienService {
    Page<BaiNopSinhVienResponse> findAll(Pageable pageable);
    BaiNopSinhVienResponse findById(Long id);
    BaiNopSinhVienResponse create(BaiNopSinhVienRequest request);
    BaiNopSinhVienResponse update(Long id, BaiNopSinhVienRequest request);
    void delete(Long id);
}
