package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;

public interface ChuongTrinhMonQuyDoiDiemMauService {
    Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAll(Long chuongTrinhMonId, Long quyDoiDiemMauId, String keyword, Pageable pageable);
    ChuongTrinhMonQuyDoiDiemMauResponse findById(Long id);

    ChuongTrinhMonQuyDoiDiemMauResponse create(ChuongTrinhMonQuyDoiDiemMauRequest request);

    ChuongTrinhMonQuyDoiDiemMauResponse update(Long id, ChuongTrinhMonQuyDoiDiemMauRequest request);

    void delete(Long id);
}
