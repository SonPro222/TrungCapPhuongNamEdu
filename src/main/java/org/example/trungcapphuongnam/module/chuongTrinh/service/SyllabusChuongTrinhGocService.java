package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusChuongTrinhGocService {

    Page<SyllabusChuongTrinhGocResponse> findAll(String ma, String keyword, Pageable pageable);

    SyllabusChuongTrinhGocResponse findById(Long id);

    SyllabusChuongTrinhGocResponse create(SyllabusChuongTrinhGocRequest request);

    SyllabusChuongTrinhGocResponse update(Long id, SyllabusChuongTrinhGocRequest request);

    void delete(Long id);
}