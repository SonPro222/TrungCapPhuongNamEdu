package org.example.trungcapphuongnam.module.thi.service;

import org.example.trungcapphuongnam.module.thi.dto.request.LichThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.LichThiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LichThiService {
    Page<LichThiResponse> findAll(Pageable pageable);
    LichThiResponse findById(Long id);
    LichThiResponse create(LichThiRequest request);
    LichThiResponse update(Long id, LichThiRequest request);
    void delete(Long id);
}
