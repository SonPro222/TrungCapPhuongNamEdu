package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;

import java.util.List;

public interface LopHocPhanService {
    List<LopHocPhanResponse> getAll();
    LopHocPhanResponse getById(Long id);
    LopHocPhanResponse create(LopHocPhanRequest request);
    LopHocPhanResponse update(Long id, LopHocPhanRequest request);
    void delete(Long id);
}
