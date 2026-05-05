package org.example.trungcapphuongnam.module.daoTao.service;

import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrinhDoDaoTaoService {
    Page<TrinhDoDaoTaoResponse> findAll(Pageable pageable);
    TrinhDoDaoTaoResponse findById(Long id);
    TrinhDoDaoTaoResponse create(TrinhDoDaoTaoRequest request);
    TrinhDoDaoTaoResponse update(Long id, TrinhDoDaoTaoRequest request);
    void delete(Long id);
}
