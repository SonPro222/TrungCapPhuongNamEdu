package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaGocResponse;

public interface NangLucDauRaGocService {
    Page<NangLucDauRaGocResponse> findAll(Long chuongTrinhId, String ma, String loai, String keyword, Pageable pageable);
    NangLucDauRaGocResponse findById(Long id);

    NangLucDauRaGocResponse create(NangLucDauRaGocRequest request);

    NangLucDauRaGocResponse update(Long id, NangLucDauRaGocRequest request);

    void delete(Long id);
}
