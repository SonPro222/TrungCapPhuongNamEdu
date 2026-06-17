package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiangVienDangKyGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiangVienDangKyGiangDayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GiangVienDangKyGiangDayService {
    Page<GiangVienDangKyGiangDayResponse> getAll(String keyword, Long giaoVienId, Long khungKyId, String trangThai, Pageable pageable);
    GiangVienDangKyGiangDayResponse getById(Long id);
    GiangVienDangKyGiangDayResponse create(GiangVienDangKyGiangDayRequest request);
    GiangVienDangKyGiangDayResponse update(Long id, GiangVienDangKyGiangDayRequest request);
    void delete(Long id);
}
