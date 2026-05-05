package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoaiChuongTrinhService {
    Page<LoaiChuongTrinhResponse> findAll(Pageable pageable);
    LoaiChuongTrinhResponse findById(Long id);
    LoaiChuongTrinhResponse create(LoaiChuongTrinhRequest request);
    LoaiChuongTrinhResponse update(Long id, LoaiChuongTrinhRequest request);
    void delete(Long id);
}
