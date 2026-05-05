package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LanLamBaiOnlineService {
    Page<LanLamBaiOnlineResponse> findAll(Pageable pageable);
    LanLamBaiOnlineResponse findById(Long id);
    LanLamBaiOnlineResponse create(LanLamBaiOnlineRequest request);
    LanLamBaiOnlineResponse update(Long id, LanLamBaiOnlineRequest request);
    void delete(Long id);
}
