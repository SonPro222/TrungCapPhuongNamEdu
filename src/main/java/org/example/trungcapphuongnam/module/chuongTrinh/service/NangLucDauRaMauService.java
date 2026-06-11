package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaMauResponse;

public interface NangLucDauRaMauService {
    Page<NangLucDauRaMauResponse> findAll(Long syllabusChuongTrinhMauId, String ma, String loai, String keyword, Pageable pageable);
    NangLucDauRaMauResponse findById(Long id);

    NangLucDauRaMauResponse create(NangLucDauRaMauRequest request);

    NangLucDauRaMauResponse update(Long id, NangLucDauRaMauRequest request);

    void delete(Long id);
}
