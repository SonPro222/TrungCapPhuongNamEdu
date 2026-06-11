package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhGocResponse;

public interface MucTieuChuongTrinhGocService {
    Page<MucTieuChuongTrinhGocResponse> findAll(String ma, String loai, String keyword, Pageable pageable);
    MucTieuChuongTrinhGocResponse findById(Long id);

    MucTieuChuongTrinhGocResponse create(MucTieuChuongTrinhGocRequest request);

    MucTieuChuongTrinhGocResponse update(Long id, MucTieuChuongTrinhGocRequest request);

    void delete(Long id);
}
