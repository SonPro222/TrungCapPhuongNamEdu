package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsTepRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsTepResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiNopLmsTepService {
    Page<BaiNopLmsTepResponse> findAll(Pageable pageable);
    BaiNopLmsTepResponse findById(Long id);
    BaiNopLmsTepResponse create(BaiNopLmsTepRequest request);
    BaiNopLmsTepResponse update(Long id, BaiNopLmsTepRequest request);
    void delete(Long id);
}
