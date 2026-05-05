package org.example.trungcapphuongnam.module.heThong.service;

import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import java.util.List;

public interface NhatKyHeThongService {
    List<NhatKyHeThongResponse> getAll();
    NhatKyHeThongResponse getById(Long id);
    NhatKyHeThongResponse create(NhatKyHeThongRequest request);
    NhatKyHeThongResponse update(Long id, NhatKyHeThongRequest request);
    void delete(Long id);
}
