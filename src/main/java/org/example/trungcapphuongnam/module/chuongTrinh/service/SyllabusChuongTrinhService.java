package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusChuongTrinhService {
    Page<SyllabusChuongTrinhResponse> findAll(Pageable pageable);
    SyllabusChuongTrinhResponse findById(Long id);
    SyllabusChuongTrinhResponse create(SyllabusChuongTrinhRequest request);
    SyllabusChuongTrinhResponse update(Long id, SyllabusChuongTrinhRequest request);
    void delete(Long id);
}
