package org.example.trungcapphuongnam.module.thi.service;

import org.example.trungcapphuongnam.module.thi.dto.request.BaiThiSinhVienRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.BaiThiSinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiThiSinhVienService {
    Page<BaiThiSinhVienResponse> findAll(Pageable pageable);
    BaiThiSinhVienResponse findById(Long id);
    BaiThiSinhVienResponse create(BaiThiSinhVienRequest request);
    BaiThiSinhVienResponse update(Long id, BaiThiSinhVienRequest request);
    void delete(Long id);
}
