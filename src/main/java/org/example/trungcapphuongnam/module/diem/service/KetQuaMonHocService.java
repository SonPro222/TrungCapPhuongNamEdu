package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaMonHocRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaMonHocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface KetQuaMonHocService {
    Page<KetQuaMonHocResponse> findAll(Pageable pageable);

    Page<KetQuaMonHocResponse> findTheoBoLoc(
            Long sinhVienId,
            Long chuongTrinhMonId,
            Long lopHocPhanId,
            Pageable pageable
    );

    KetQuaMonHocResponse findById(Long id);

    KetQuaMonHocResponse create(KetQuaMonHocRequest request);

    KetQuaMonHocResponse update(Long id, KetQuaMonHocRequest request);

    void delete(Long id);
}