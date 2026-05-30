package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NganhLoaiChuongTrinhService {

    Page<NganhLoaiChuongTrinhResponse> findAll(Long nganhId, Long loaiChuongTrinhId, String trangThai, Pageable pageable);

    NganhLoaiChuongTrinhResponse findById(Long id);

    NganhLoaiChuongTrinhResponse create(NganhLoaiChuongTrinhRequest request);

    NganhLoaiChuongTrinhResponse update(Long id, NganhLoaiChuongTrinhRequest request);

    NganhLoaiChuongTrinhResponse luu(NganhLoaiChuongTrinhRequest request);

    void delete(Long id);
}
