package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHinhDanhGiaService {
    Page<CauHinhDanhGiaResponse> findAll(Pageable pageable);
    CauHinhDanhGiaResponse findById(Long id);
    CauHinhDanhGiaResponse create(CauHinhDanhGiaRequest request);
    CauHinhDanhGiaResponse update(Long id, CauHinhDanhGiaRequest request);
    void delete(Long id);
}
