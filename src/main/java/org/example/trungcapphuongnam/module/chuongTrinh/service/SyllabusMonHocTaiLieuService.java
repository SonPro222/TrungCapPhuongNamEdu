package org.example.trungcapphuongnam.module.chuongTrinh.service;


import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocTaiLieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocTaiLieuService {

    Page<SyllabusMonHocTaiLieuResponse> findAll(Long syllabusMonId, Long taiLieuGocId, String keyword, Pageable pageable);

    SyllabusMonHocTaiLieuResponse findById(Long id);

    SyllabusMonHocTaiLieuResponse create(SyllabusMonHocTaiLieuRequest request);

    SyllabusMonHocTaiLieuResponse update(Long id, SyllabusMonHocTaiLieuRequest request);

    void delete(Long id);
}