package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocDieuKienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocGocDieuKienService {

    Page<SyllabusMonHocGocDieuKienResponse> findAll(Long syllabusMonHocGocId, Long dieuKienGocId, Pageable pageable);

    SyllabusMonHocGocDieuKienResponse findById(Long id);

    SyllabusMonHocGocDieuKienResponse create(SyllabusMonHocGocDieuKienRequest request);

    SyllabusMonHocGocDieuKienResponse update(Long id, SyllabusMonHocGocDieuKienRequest request);

    void delete(Long id);
}