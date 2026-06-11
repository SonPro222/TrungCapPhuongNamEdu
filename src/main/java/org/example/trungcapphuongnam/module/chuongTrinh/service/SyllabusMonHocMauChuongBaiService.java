package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauChuongBaiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocMauChuongBaiService {

    Page<SyllabusMonHocMauChuongBaiResponse> findAll(Long syllabusMonHocMauId, String keyword, Pageable pageable);

    SyllabusMonHocMauChuongBaiResponse findById(Long id);

    SyllabusMonHocMauChuongBaiResponse create(SyllabusMonHocMauChuongBaiRequest request);

    SyllabusMonHocMauChuongBaiResponse update(Long id, SyllabusMonHocMauChuongBaiRequest request);

    void delete(Long id);
}