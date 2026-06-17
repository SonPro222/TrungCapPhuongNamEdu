package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauTaiLieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocMauTaiLieuService {

    Page<SyllabusMonHocMauTaiLieuResponse> findAll(Long syllabusMonHocMauId, Long taiLieuMauId, Pageable pageable);

    SyllabusMonHocMauTaiLieuResponse findById(Long id);

    SyllabusMonHocMauTaiLieuResponse create(SyllabusMonHocMauTaiLieuRequest request);

    SyllabusMonHocMauTaiLieuResponse update(Long id, SyllabusMonHocMauTaiLieuRequest request);

    void delete(Long id);
}