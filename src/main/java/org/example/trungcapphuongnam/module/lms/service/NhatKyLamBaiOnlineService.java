package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.NhatKyLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NhatKyLamBaiOnlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhatKyLamBaiOnlineService {
    Page<NhatKyLamBaiOnlineResponse> findAll(Pageable pageable);
    NhatKyLamBaiOnlineResponse findById(Long id);
    NhatKyLamBaiOnlineResponse create(NhatKyLamBaiOnlineRequest request);
    NhatKyLamBaiOnlineResponse update(Long id, NhatKyLamBaiOnlineRequest request);
    void delete(Long id);
}
