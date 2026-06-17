package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhMauResponse;

public interface MucTieuChuongTrinhMauService {
    Page<MucTieuChuongTrinhMauResponse> findAll(Long syllabusChuongTrinhMauId, String ma, String loai, String keyword, Pageable pageable);
    MucTieuChuongTrinhMauResponse findById(Long id);

    MucTieuChuongTrinhMauResponse create(MucTieuChuongTrinhMauRequest request);

    MucTieuChuongTrinhMauResponse update(Long id, MucTieuChuongTrinhMauRequest request);

    void delete(Long id);
}
