package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.CaHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CaHocService {

    Page<CaHocResponse> getAll(String keyword, Pageable pageable);

    CaHocResponse getById(Long id);

    CaHocResponse create(CaHocRequest request);

    CaHocResponse update(Long id, CaHocRequest request);

    void delete(Long id);
}