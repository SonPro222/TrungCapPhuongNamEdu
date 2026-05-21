package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionDieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionDieuKienTotNghiepResponse;

public interface ChuongTrinhVersionDieuKienTotNghiepService {
    Page<ChuongTrinhVersionDieuKienTotNghiepResponse> findAll(Pageable pageable);

    ChuongTrinhVersionDieuKienTotNghiepResponse findById(Long id);

    ChuongTrinhVersionDieuKienTotNghiepResponse create(ChuongTrinhVersionDieuKienTotNghiepRequest request);

    ChuongTrinhVersionDieuKienTotNghiepResponse update(Long id, ChuongTrinhVersionDieuKienTotNghiepRequest request);

    void delete(Long id);

    Page<ChuongTrinhVersionDieuKienTotNghiepResponse> findAllByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);

    Page<ChuongTrinhVersionDieuKienTotNghiepResponse> findAllByDieuKienGocId(Long dieuKienGocId, Pageable pageable);
}
