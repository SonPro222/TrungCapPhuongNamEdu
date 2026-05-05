package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.NganHangCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NganHangCauHoiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NganHangCauHoiService {
    Page<NganHangCauHoiResponse> findAll(Pageable pageable);
    NganHangCauHoiResponse findById(Long id);
    NganHangCauHoiResponse create(NganHangCauHoiRequest request);
    NganHangCauHoiResponse update(Long id, NganHangCauHoiRequest request);
    void delete(Long id);
}
