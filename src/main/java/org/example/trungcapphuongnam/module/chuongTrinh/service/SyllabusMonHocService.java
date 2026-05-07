package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocService {
    Page<SyllabusMonHocResponse> findAll(Pageable pageable);
    Page<SyllabusMonHocResponse> findAllByChuongTrinhMonId(Long chuongTrinhMonId, Pageable pageable);
    SyllabusMonHocResponse findById(Long id);
    SyllabusMonHocResponse create(SyllabusMonHocRequest request);
    SyllabusMonHocResponse update(Long id, SyllabusMonHocRequest request);
    void delete(Long id);
}
