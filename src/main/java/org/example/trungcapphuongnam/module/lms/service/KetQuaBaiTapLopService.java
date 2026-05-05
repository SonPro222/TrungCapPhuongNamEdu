package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.KetQuaBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.KetQuaBaiTapLopResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KetQuaBaiTapLopService {
    Page<KetQuaBaiTapLopResponse> findAll(Pageable pageable);
    KetQuaBaiTapLopResponse findById(Long id);
    KetQuaBaiTapLopResponse create(KetQuaBaiTapLopRequest request);
    KetQuaBaiTapLopResponse update(Long id, KetQuaBaiTapLopRequest request);
    void delete(Long id);
}
