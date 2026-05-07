package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChuongTrinhMonService {
    Page<ChuongTrinhMonResponse> findAll(Pageable pageable);
    Page<ChuongTrinhMonResponse> findAllByChuongTrinhVersionId(Long chuongTrinhVersionId, Pageable pageable);
    ChuongTrinhMonResponse findById(Long id);
    ChuongTrinhMonResponse create(ChuongTrinhMonRequest request);
    ChuongTrinhMonResponse update(Long id, ChuongTrinhMonRequest request);
    void delete(Long id);
}
