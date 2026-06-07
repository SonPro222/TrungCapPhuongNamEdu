package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.DotHocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.DotHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DotHocService {
    Page<DotHocResponse> findAll(String keyword, Long khoaDaoTaoId, Long lopHanhChinhId, String trangThai, Pageable pageable);
    DotHocResponse findById(Long id);
    DotHocResponse create(DotHocRequest request);
    DotHocResponse update(Long id, DotHocRequest request);
    void delete(Long id);
}
