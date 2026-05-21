package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NangLucDauRaService {
    Page<NangLucDauRaResponse> findAll(Pageable pageable);
    NangLucDauRaResponse findById(Long id);
    NangLucDauRaResponse create(NangLucDauRaRequest request);
    NangLucDauRaResponse update(Long id, NangLucDauRaRequest request);
    void delete(Long id);
    Page<NangLucDauRaResponse> findAllByChuongTrinhVersionId(
            Long chuongTrinhVersionId,
            Pageable pageable
    );

}
