package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiCauHoiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LanLamBaiCauHoiService {
    Page<LanLamBaiCauHoiResponse> findAll(Pageable pageable);
    LanLamBaiCauHoiResponse findById(Long id);
    LanLamBaiCauHoiResponse create(LanLamBaiCauHoiRequest request);
    LanLamBaiCauHoiResponse update(Long id, LanLamBaiCauHoiRequest request);
    void delete(Long id);
}
