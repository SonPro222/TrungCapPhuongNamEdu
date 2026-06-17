package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienDangKyNhomCaRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienDangKyNhomCaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GiaoVienDangKyNhomCaService {
    Page<GiaoVienDangKyNhomCaResponse> getAll(Long giaoVienId, Long khungKyId, Pageable pageable);
    List<GiaoVienDangKyNhomCaResponse> getByGiaoVien(Long giaoVienId);
    GiaoVienDangKyNhomCaResponse getById(Long id);
    GiaoVienDangKyNhomCaResponse create(GiaoVienDangKyNhomCaRequest request);
    GiaoVienDangKyNhomCaResponse update(Long id, GiaoVienDangKyNhomCaRequest request);
    void delete(Long id);
}
