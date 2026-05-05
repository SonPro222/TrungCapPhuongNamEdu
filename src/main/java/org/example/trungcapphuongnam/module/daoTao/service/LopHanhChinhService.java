package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LopHanhChinhService {
    Page<LopHanhChinhResponse> findAll(Pageable pageable);
    LopHanhChinhResponse findById(Long id);
    LopHanhChinhResponse create(LopHanhChinhRequest request);
    LopHanhChinhResponse update(Long id, LopHanhChinhRequest request);
    void delete(Long id);
}
