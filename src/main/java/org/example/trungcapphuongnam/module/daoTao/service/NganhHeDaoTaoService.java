package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NganhHeDaoTaoService {
    Page<NganhHeDaoTaoResponse> findAll(Long nganhId, Long trinhDoId, Long loaiChuongTrinhId, String trangThai, Pageable pageable);
    List<NganhHeDaoTaoResponse> findOptions(Long nganhId);
    NganhHeDaoTaoResponse findById(Long id);
    NganhHeDaoTaoResponse create(NganhHeDaoTaoRequest request);
    NganhHeDaoTaoResponse update(Long id, NganhHeDaoTaoRequest request);
    void delete(Long id);
}
