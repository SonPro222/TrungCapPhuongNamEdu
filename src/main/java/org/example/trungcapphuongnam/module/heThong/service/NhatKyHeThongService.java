package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface NhatKyHeThongService {
    Page<NhatKyHeThongResponse> getAll(Map<String, String> filters);
    NhatKyHeThongResponse getById(Long id);
    NhatKyHeThongResponse create(NhatKyHeThongRequest request);
    NhatKyHeThongResponse update(Long id, NhatKyHeThongRequest request);
    void delete(Long id);
}