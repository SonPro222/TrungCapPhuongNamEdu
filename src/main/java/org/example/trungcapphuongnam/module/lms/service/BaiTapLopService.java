package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapLopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiTapLopService {
    Page<BaiTapLopResponse> findAll(Pageable pageable);
    BaiTapLopResponse findById(Long id);
    BaiTapLopResponse create(BaiTapLopRequest request);
    BaiTapLopResponse update(Long id, BaiTapLopRequest request);
    void delete(Long id);
}
