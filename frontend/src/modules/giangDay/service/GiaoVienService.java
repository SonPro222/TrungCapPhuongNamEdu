package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiaoVienService {

    Page<GiaoVienResponse> getAll(String keyword, TrangThaiGiaoVien trangThai, Pageable pageable);

    GiaoVienResponse getById(Long id);

    GiaoVienResponse create(GiaoVienRequest request);

    GiaoVienResponse update(Long id, GiaoVienRequest request);

    void delete(Long id);
}