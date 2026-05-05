package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.QuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.QuyenResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuyenService {
    List<QuyenResponse> getAll();
    Page<QuyenResponse> search(String keyword, Pageable pageable);
    QuyenResponse getById(Long id);
    QuyenResponse create(QuyenRequest request);
    QuyenResponse update(Long id, QuyenRequest request);
    void delete(Long id);
}
