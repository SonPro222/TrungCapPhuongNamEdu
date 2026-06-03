package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeChiTietRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeChiTietResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaTranDeChiTietService {
    Page<MaTranDeChiTietResponse> findAll(Pageable pageable);
    MaTranDeChiTietResponse findById(Long id);
    MaTranDeChiTietResponse create(MaTranDeChiTietRequest request);
    MaTranDeChiTietResponse update(Long id, MaTranDeChiTietRequest request);
    void delete(Long id);
}
