package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemMauResponse;

public interface QuyDoiDiemMauService {
    Page<QuyDoiDiemMauResponse> findAll(String ma, String ketQua, String keyword, Pageable pageable);
    QuyDoiDiemMauResponse findById(Long id);

    QuyDoiDiemMauResponse create(QuyDoiDiemMauRequest request);

    QuyDoiDiemMauResponse update(Long id, QuyDoiDiemMauRequest request);

    void delete(Long id);
}
