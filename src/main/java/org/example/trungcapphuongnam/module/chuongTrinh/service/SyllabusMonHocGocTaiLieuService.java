package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocTaiLieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocGocTaiLieuService {

    Page<SyllabusMonHocGocTaiLieuResponse> findAll(Long syllabusMonHocGocId, Long taiLieuGocId, Pageable pageable);

    SyllabusMonHocGocTaiLieuResponse findById(Long id);

    SyllabusMonHocGocTaiLieuResponse create(SyllabusMonHocGocTaiLieuRequest request);

    SyllabusMonHocGocTaiLieuResponse update(Long id, SyllabusMonHocGocTaiLieuRequest request);

    void delete(Long id);
}