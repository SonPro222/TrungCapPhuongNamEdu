package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocService {
    Page<SyllabusMonHocResponse> findAll(
            Long chuongTrinhMonId,
            Long syllabusMonHocGocId,
            Boolean batBuocDuThi,
            String keyword,
            Pageable pageable
    );
    SyllabusMonHocResponse findById(Long id);
    SyllabusMonHocResponse create(SyllabusMonHocRequest request);
    SyllabusMonHocResponse update(Long id, SyllabusMonHocRequest request);
    void delete(Long id);
}
