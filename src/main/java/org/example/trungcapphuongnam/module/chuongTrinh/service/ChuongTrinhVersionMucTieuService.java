package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionMucTieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionMucTieuResponse;

public interface ChuongTrinhVersionMucTieuService {
    Page<ChuongTrinhVersionMucTieuResponse> findAll(Pageable pageable);

    ChuongTrinhVersionMucTieuResponse findById(Long id);

    ChuongTrinhVersionMucTieuResponse create(ChuongTrinhVersionMucTieuRequest request);

    ChuongTrinhVersionMucTieuResponse update(Long id, ChuongTrinhVersionMucTieuRequest request);

    void delete(Long id);

    Page<ChuongTrinhVersionMucTieuResponse> findAllByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);

    Page<ChuongTrinhVersionMucTieuResponse> findAllByMucTieuGocId(Long mucTieuGocId, Pageable pageable);
}
