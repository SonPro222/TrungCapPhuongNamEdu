package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LoaiChuongTrinhService {
    Page<LoaiChuongTrinhResponse> findAll(Pageable pageable);
    List<LoaiChuongTrinhResponse> findOptions();
    LoaiChuongTrinhResponse findById(Long id);
    LoaiChuongTrinhResponse create(LoaiChuongTrinhRequest request);
    LoaiChuongTrinhResponse update(Long id, LoaiChuongTrinhRequest request);
    void delete(Long id);
}
