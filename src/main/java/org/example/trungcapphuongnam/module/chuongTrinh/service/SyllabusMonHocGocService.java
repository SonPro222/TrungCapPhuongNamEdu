package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocGocService {

    Page<SyllabusMonHocGocResponse> findAll(Long monHocId, String ma, String keyword, Pageable pageable);

    SyllabusMonHocGocResponse findById(Long id);

    SyllabusMonHocGocResponse create(SyllabusMonHocGocRequest request);

    SyllabusMonHocGocResponse update(Long id, SyllabusMonHocGocRequest request);

    void delete(Long id);
}