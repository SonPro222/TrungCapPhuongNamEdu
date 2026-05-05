package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyDangNhapRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyDangNhapResponse;
import java.util.List;

public interface NhatKyDangNhapService {
    List<NhatKyDangNhapResponse> getAll();
    NhatKyDangNhapResponse getById(Long id);
    NhatKyDangNhapResponse create(NhatKyDangNhapRequest request);
    NhatKyDangNhapResponse update(Long id, NhatKyDangNhapRequest request);
    void delete(Long id);
}
