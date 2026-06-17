package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MaTranDeService {
    Page<MaTranDeResponse> findAll(Pageable pageable);
    MaTranDeResponse findById(Long id);
    MaTranDeResponse create(MaTranDeRequest request);
    MaTranDeResponse update(Long id, MaTranDeRequest request);
    void delete(Long id);
}
