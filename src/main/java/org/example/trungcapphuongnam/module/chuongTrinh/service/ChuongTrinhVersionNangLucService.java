package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionNangLucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionNangLucResponse;

public interface ChuongTrinhVersionNangLucService {
    Page<ChuongTrinhVersionNangLucResponse> findAll(Long chuongTrinhVersionId, Long nangLucGocId, String keyword, Pageable pageable);
    ChuongTrinhVersionNangLucResponse findById(Long id);

    ChuongTrinhVersionNangLucResponse create(ChuongTrinhVersionNangLucRequest request);

    ChuongTrinhVersionNangLucResponse update(Long id, ChuongTrinhVersionNangLucRequest request);

    void delete(Long id);
}
