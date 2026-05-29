package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CauHinhDanhGiaService {
    Page<CauHinhDanhGiaResponse> findAll(Pageable pageable);
    CauHinhDanhGiaResponse findById(Long id);

    /** Trả toàn bộ cột điểm của lớp học phần, sắp theo thuTu. */
    List<CauHinhDanhGiaResponse> findByLopHocPhanId(Long lopHocPhanId);

    CauHinhDanhGiaResponse create(CauHinhDanhGiaRequest request);
    CauHinhDanhGiaResponse update(Long id, CauHinhDanhGiaRequest request);
    void delete(Long id);
}