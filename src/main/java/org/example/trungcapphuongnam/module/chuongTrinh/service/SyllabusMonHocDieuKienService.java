package org.example.trungcapphuongnam.module.chuongTrinh.service;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocDieuKienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocDieuKienService {

    Page<SyllabusMonHocDieuKienResponse> findAll(Long syllabusMonId, Long dieuKienGocId, String keyword, Pageable pageable);

    SyllabusMonHocDieuKienResponse findById(Long id);

    SyllabusMonHocDieuKienResponse create(SyllabusMonHocDieuKienRequest request);

    SyllabusMonHocDieuKienResponse update(Long id, SyllabusMonHocDieuKienRequest request);

    void delete(Long id);
}