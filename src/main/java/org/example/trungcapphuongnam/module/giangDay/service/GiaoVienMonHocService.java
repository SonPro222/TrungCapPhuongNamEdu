package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienMonHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienMonHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiaoVienMonHocService {
    Page<GiaoVienMonHocResponse> getAll(String keyword, Long giaoVienId, Long monHocId, String trangThai, Pageable pageable);
    GiaoVienMonHocResponse getById(Long id);
    GiaoVienMonHocResponse create(GiaoVienMonHocRequest request);
    GiaoVienMonHocResponse update(Long id, GiaoVienMonHocRequest request);
    void delete(Long id);
}
