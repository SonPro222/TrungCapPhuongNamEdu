package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocMauService {

    Page<SyllabusMonHocMauResponse> findAll(Long monHocId, String ma, String keyword, Pageable pageable);

    SyllabusMonHocMauResponse findById(Long id);

    SyllabusMonHocMauResponse create(SyllabusMonHocMauRequest request);

    SyllabusMonHocMauResponse update(Long id, SyllabusMonHocMauRequest request);

    void delete(Long id);
}