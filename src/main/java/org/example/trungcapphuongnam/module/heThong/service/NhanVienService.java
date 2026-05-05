package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhanVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhanVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NhanVienService {
    List<NhanVienResponse> getAll();
    Page<NhanVienResponse> search(String keyword, String phongBan, String chucVu, String trangThai, Integer namBatDau, Pageable pageable);
    NhanVienResponse getById(Long id);
    NhanVienResponse create(NhanVienRequest request);
    NhanVienResponse update(Long id, NhanVienRequest request);
    void delete(Long id);
}
