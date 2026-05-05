package org.example.trungcapphuongnam.module.sinhVien.service;

import org.example.trungcapphuongnam.module.sinhVien.dto.request.MienTruBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.MienTruBaoLuuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MienTruBaoLuuService {
    Page<MienTruBaoLuuResponse> findAll(Pageable pageable);
    MienTruBaoLuuResponse findById(Long id);
    MienTruBaoLuuResponse create(MienTruBaoLuuRequest request);
    MienTruBaoLuuResponse update(Long id, MienTruBaoLuuRequest request);
    void delete(Long id);
}
