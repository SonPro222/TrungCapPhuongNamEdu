package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiNopLmsService {
    Page<BaiNopLmsResponse> findAll(Pageable pageable);
    BaiNopLmsResponse findById(Long id);
    BaiNopLmsResponse create(BaiNopLmsRequest request);
    BaiNopLmsResponse update(Long id, BaiNopLmsRequest request);
    void delete(Long id);
}
