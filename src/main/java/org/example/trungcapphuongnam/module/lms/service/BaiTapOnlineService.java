package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BaiTapOnlineService {
    Page<BaiTapOnlineResponse> findAll(Pageable pageable);
    BaiTapOnlineResponse findById(Long id);
    BaiTapOnlineResponse create(BaiTapOnlineRequest request);
    BaiTapOnlineResponse update(Long id, BaiTapOnlineRequest request);
    void delete(Long id);
}
