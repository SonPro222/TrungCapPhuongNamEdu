package org.example.trungcapphuongnam.module.sinhVien.service;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienDiHocLaiRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienBaoLuuTrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SinhVienBaoLuuService {

    Page<SinhVienBaoLuuResponse> findAll(Long sinhVienId, SinhVienBaoLuuTrangThai trangThai, Pageable pageable);

    SinhVienBaoLuuResponse findById(Long id);

    SinhVienBaoLuuResponse baoLuu(SinhVienBaoLuuRequest request);

    SinhVienBaoLuuResponse diHocLai(Long id, SinhVienDiHocLaiRequest request);

    void huy(Long id);
}