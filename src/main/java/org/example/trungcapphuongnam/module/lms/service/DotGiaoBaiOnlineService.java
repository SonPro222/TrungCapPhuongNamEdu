package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.DotGiaoBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DotGiaoBaiOnlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DotGiaoBaiOnlineService {
    Page<DotGiaoBaiOnlineResponse> findAll(Pageable pageable);
    DotGiaoBaiOnlineResponse findById(Long id);
    DotGiaoBaiOnlineResponse create(DotGiaoBaiOnlineRequest request);
    DotGiaoBaiOnlineResponse update(Long id, DotGiaoBaiOnlineRequest request);
    void delete(Long id);
}
