package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChuongTrinhService {
    Page<ChuongTrinhResponse> findAll(
            Long nganhLoaiChuongTrinhId,
            Long nganhId,
            Long trinhDoId,
            Long loaiChuongTrinhId,
            String keyword,
            Pageable pageable
    );

    ChuongTrinhResponse findById(Long id);

    ChuongTrinhResponse create(ChuongTrinhRequest request);

    ChuongTrinhResponse update(Long id, ChuongTrinhRequest request);

    void delete(Long id);
}
