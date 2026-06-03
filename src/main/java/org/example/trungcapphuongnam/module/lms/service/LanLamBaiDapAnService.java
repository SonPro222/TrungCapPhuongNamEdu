package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiDapAnResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LanLamBaiDapAnService {
    Page<LanLamBaiDapAnResponse> findAll(Pageable pageable);
    LanLamBaiDapAnResponse findById(Long id);
    LanLamBaiDapAnResponse create(LanLamBaiDapAnRequest request);
    LanLamBaiDapAnResponse update(Long id, LanLamBaiDapAnRequest request);
    void delete(Long id);
}
