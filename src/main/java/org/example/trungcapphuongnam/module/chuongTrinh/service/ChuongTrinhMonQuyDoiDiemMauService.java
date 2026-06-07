package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChuongTrinhMonQuyDoiDiemMauService {

    Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAll(
            Long chuongTrinhMonId,
            Long syllabusMonHocId,
            Long quyDoiDiemMauId,
            String keyword,
            Pageable pageable
    );

    ChuongTrinhMonQuyDoiDiemMauResponse findById(Long id);

    ChuongTrinhMonQuyDoiDiemMauResponse create(ChuongTrinhMonQuyDoiDiemMauRequest request);

    ChuongTrinhMonQuyDoiDiemMauResponse update(Long id, ChuongTrinhMonQuyDoiDiemMauRequest request);

    void delete(Long id);
}