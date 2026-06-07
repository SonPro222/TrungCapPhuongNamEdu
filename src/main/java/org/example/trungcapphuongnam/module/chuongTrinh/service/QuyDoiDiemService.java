package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuyDoiDiemService {

    Page<QuyDoiDiemResponse> findAll(
            Long chuongTrinhMonId,
            Long syllabusMonHocId,
            String ketQua,
            String keyword,
            Pageable pageable
    );

    QuyDoiDiemResponse findById(Long id);

    QuyDoiDiemResponse create(QuyDoiDiemRequest request);

    QuyDoiDiemResponse update(Long id, QuyDoiDiemRequest request);

    void delete(Long id);
}