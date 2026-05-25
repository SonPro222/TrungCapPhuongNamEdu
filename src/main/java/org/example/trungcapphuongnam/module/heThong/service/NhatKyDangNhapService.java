package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyDangNhapRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyDangNhapResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NhatKyDangNhapService {
    Page<NhatKyDangNhapResponse> getAll(Pageable pageable);
    NhatKyDangNhapResponse getById(Long id);
    NhatKyDangNhapResponse create(NhatKyDangNhapRequest request);
    NhatKyDangNhapResponse update(Long id, NhatKyDangNhapRequest request);
    void delete(Long id);
}