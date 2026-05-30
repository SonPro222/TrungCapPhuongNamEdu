package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.CauHinhDanhGiaMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.CauHinhDanhGiaMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHinhDanhGiaMauService {

    Page<CauHinhDanhGiaMauResponse> findAll(Long chuongTrinhMonId, String keyword, Pageable pageable);

    CauHinhDanhGiaMauResponse findById(Long id);

    CauHinhDanhGiaMauResponse create(CauHinhDanhGiaMauRequest request);

    CauHinhDanhGiaMauResponse update(Long id, CauHinhDanhGiaMauRequest request);

    void delete(Long id);
}