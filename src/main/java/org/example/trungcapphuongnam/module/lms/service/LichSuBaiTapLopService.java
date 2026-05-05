package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LichSuBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuBaiTapLopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LichSuBaiTapLopService {
    Page<LichSuBaiTapLopResponse> findAll(Pageable pageable);
    LichSuBaiTapLopResponse findById(Long id);
    LichSuBaiTapLopResponse create(LichSuBaiTapLopRequest request);
    LichSuBaiTapLopResponse update(Long id, LichSuBaiTapLopRequest request);
    void delete(Long id);
}
