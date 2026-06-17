package org.example.trungcapphuongnam.module.sinhVien.service;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienChuongTrinhRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienChuongTrinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SinhVienChuongTrinhService {
    Page<SinhVienChuongTrinhResponse> findAll(Pageable pageable);
    SinhVienChuongTrinhResponse findById(Long id);
    List<SinhVienChuongTrinhResponse> findBySinhVienId(Long sinhVienId);
    SinhVienChuongTrinhResponse create(SinhVienChuongTrinhRequest request);
    SinhVienChuongTrinhResponse update(Long id, SinhVienChuongTrinhRequest request);
    void delete(Long id);
}