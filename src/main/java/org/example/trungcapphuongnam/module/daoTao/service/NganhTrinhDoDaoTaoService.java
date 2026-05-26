package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NganhTrinhDoDaoTaoService {

    Page<NganhTrinhDoDaoTaoResponse> findAll(Long nganhId, Long trinhDoId, String trangThai, Pageable pageable);

    NganhTrinhDoDaoTaoResponse findById(Long id);

    NganhTrinhDoDaoTaoResponse create(NganhTrinhDoDaoTaoRequest request);

    NganhTrinhDoDaoTaoResponse update(Long id, NganhTrinhDoDaoTaoRequest request);

    NganhTrinhDoDaoTaoResponse luu(NganhTrinhDoDaoTaoRequest request);

    void delete(Long id);
}
