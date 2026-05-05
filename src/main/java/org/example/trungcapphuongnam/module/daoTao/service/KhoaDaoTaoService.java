package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KhoaDaoTaoService {
    Page<KhoaDaoTaoResponse> findAll(Pageable pageable);
    KhoaDaoTaoResponse findById(Long id);
    KhoaDaoTaoResponse create(KhoaDaoTaoRequest request);
    KhoaDaoTaoResponse update(Long id, KhoaDaoTaoRequest request);
    void delete(Long id);
}
