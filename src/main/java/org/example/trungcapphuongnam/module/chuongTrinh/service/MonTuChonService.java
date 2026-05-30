package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTuChonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MonTuChonService {
    Page<MonTuChonResponse> findAll(Long nhomId, Long chuongTrinhMonId, Pageable pageable);
    MonTuChonResponse findById(Long id);
    MonTuChonResponse create(MonTuChonRequest request);
    MonTuChonResponse update(Long id, MonTuChonRequest request);
    void delete(Long id);
}
