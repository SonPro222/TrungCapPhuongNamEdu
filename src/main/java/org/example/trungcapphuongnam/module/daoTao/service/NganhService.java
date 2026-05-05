package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NganhService {
    Page<NganhResponse> findAll(Pageable pageable);
    NganhResponse findById(Long id);
    NganhResponse create(NganhRequest request);
    NganhResponse update(Long id, NganhRequest request);
    void delete(Long id);
}
