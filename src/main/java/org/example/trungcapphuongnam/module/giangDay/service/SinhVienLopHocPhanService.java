package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;

import java.util.List;

public interface SinhVienLopHocPhanService {
    List<SinhVienLopHocPhanResponse> getAll();
    SinhVienLopHocPhanResponse getById(Long id);
    SinhVienLopHocPhanResponse create(SinhVienLopHocPhanRequest request);
    SinhVienLopHocPhanResponse update(Long id, SinhVienLopHocPhanRequest request);
    void delete(Long id);
}
