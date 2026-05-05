package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineCauHoiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiTapOnlineCauHoiService {
    Page<BaiTapOnlineCauHoiResponse> findAll(Pageable pageable);
    BaiTapOnlineCauHoiResponse findById(Long id);
    BaiTapOnlineCauHoiResponse create(BaiTapOnlineCauHoiRequest request);
    BaiTapOnlineCauHoiResponse update(Long id, BaiTapOnlineCauHoiRequest request);
    void delete(Long id);
}
