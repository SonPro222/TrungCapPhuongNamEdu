package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusChuongTrinhMauService {

    Page<SyllabusChuongTrinhMauResponse> findAll(Long chuongTrinhId, String ma, String keyword, Pageable pageable);

    SyllabusChuongTrinhMauResponse findById(Long id);

    SyllabusChuongTrinhMauResponse create(SyllabusChuongTrinhMauRequest request);

    SyllabusChuongTrinhMauResponse update(Long id, SyllabusChuongTrinhMauRequest request);

    void delete(Long id);
}