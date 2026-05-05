package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaLopHocPhanRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaLopHocPhanResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KetQuaLopHocPhanService {
    Page<KetQuaLopHocPhanResponse> findAll(Pageable pageable);
    KetQuaLopHocPhanResponse findById(Long id);
    KetQuaLopHocPhanResponse create(KetQuaLopHocPhanRequest request);
    KetQuaLopHocPhanResponse update(Long id, KetQuaLopHocPhanRequest request);
    void delete(Long id);
}
