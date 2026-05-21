package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;

public interface ChuongTrinhMonQuyDoiDiemMauService {
    Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAll(Pageable pageable);

    ChuongTrinhMonQuyDoiDiemMauResponse findById(Long id);

    ChuongTrinhMonQuyDoiDiemMauResponse create(ChuongTrinhMonQuyDoiDiemMauRequest request);

    ChuongTrinhMonQuyDoiDiemMauResponse update(Long id, ChuongTrinhMonQuyDoiDiemMauRequest request);

    void delete(Long id);

    Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAllByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);

    Page<ChuongTrinhMonQuyDoiDiemMauResponse> findAllByQuyDoiDiemMauId(Long quyDoiDiemMauId, Pageable pageable);
}
