package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuyenTrangThaiChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.TrangThaiChuongTrinhVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChuongTrinhVersionService {
    Page<ChuongTrinhVersionResponse> findAll(
            Long chuongTrinhId,
            Boolean laHienHanh,
            TrangThaiChuongTrinhVersion trangThai,
            String keyword,
            Pageable pageable
    );

    ChuongTrinhVersionResponse findById(Long id);

    ChuongTrinhVersionResponse create(ChuongTrinhVersionRequest request);

    ChuongTrinhVersionResponse update(Long id, ChuongTrinhVersionRequest request);

    ChuongTrinhVersionResponse chuyenTrangThai(Long id, ChuyenTrangThaiChuongTrinhVersionRequest request);

    void delete(Long id);
}
