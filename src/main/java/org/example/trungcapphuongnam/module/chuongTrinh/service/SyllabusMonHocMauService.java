package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauFullRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFullResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SyllabusMonHocMauService {

    Page<SyllabusMonHocMauResponse> findAll(Long monHocId, String ma, String keyword, Pageable pageable);

    SyllabusMonHocMauResponse findById(Long id);

    SyllabusMonHocMauResponse create(SyllabusMonHocMauRequest request);

    SyllabusMonHocMauResponse update(Long id, SyllabusMonHocMauRequest request);

    void delete(Long id);

    /** Tạo full syllabus mẫu (thông tin + chương bài + điều kiện + tài liệu + cấu hình đánh giá + quy đổi điểm) trong 1 transaction */
    SyllabusMonHocMauFullResponse createFull(SyllabusMonHocMauFullRequest request);

    /** Cập nhật full syllabus mẫu trong 1 transaction */
    SyllabusMonHocMauFullResponse updateFull(Long id, SyllabusMonHocMauFullRequest request);

    /** Lấy full syllabus mẫu bao gồm tất cả dữ liệu con */
    SyllabusMonHocMauFullResponse getFull(Long id);
}