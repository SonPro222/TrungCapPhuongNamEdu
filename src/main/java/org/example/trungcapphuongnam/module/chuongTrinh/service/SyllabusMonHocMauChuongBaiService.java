package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocChuongBaiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocGocChuongBaiService {

    Page<SyllabusMonHocGocChuongBaiResponse> findAll(Long syllabusMonHocGocId, String keyword, Pageable pageable);

    SyllabusMonHocGocChuongBaiResponse findById(Long id);

    SyllabusMonHocGocChuongBaiResponse create(SyllabusMonHocGocChuongBaiRequest request);

    SyllabusMonHocGocChuongBaiResponse update(Long id, SyllabusMonHocGocChuongBaiRequest request);

    void delete(Long id);
}