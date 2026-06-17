package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.CauHinhMaSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.CauHinhMaSinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHinhMaSinhVienService {

    Page<CauHinhMaSinhVienResponse> findAll(Long nganhId, Long chuongTrinhVersionId, Pageable pageable);

    CauHinhMaSinhVienResponse findById(Long id);

    CauHinhMaSinhVienResponse create(CauHinhMaSinhVienRequest request);

    CauHinhMaSinhVienResponse update(Long id, CauHinhMaSinhVienRequest request);

    void delete(Long id);
}