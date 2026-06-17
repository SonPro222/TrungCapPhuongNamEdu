package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauDieuKienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocMauDieuKienService {

    Page<SyllabusMonHocMauDieuKienResponse> findAll(Long syllabusMonHocMauId, Long dieuKienMauId, Pageable pageable);

    SyllabusMonHocMauDieuKienResponse findById(Long id);

    SyllabusMonHocMauDieuKienResponse create(SyllabusMonHocMauDieuKienRequest request);

    SyllabusMonHocMauDieuKienResponse update(Long id, SyllabusMonHocMauDieuKienRequest request);

    void delete(Long id);
}