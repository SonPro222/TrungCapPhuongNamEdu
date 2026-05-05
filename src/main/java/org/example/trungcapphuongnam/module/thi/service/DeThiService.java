package org.example.trungcapphuongnam.module.thi.service;

import org.example.trungcapphuongnam.module.thi.dto.request.DeThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.DeThiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeThiService {
    Page<DeThiResponse> findAll(Pageable pageable);
    DeThiResponse findById(Long id);
    DeThiResponse create(DeThiRequest request);
    DeThiResponse update(Long id, DeThiRequest request);
    void delete(Long id);
}
