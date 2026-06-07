package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CauHinhDanhGiaService {
    Page<CauHinhDanhGiaResponse> findAll(Pageable pageable);

    CauHinhDanhGiaResponse findById(Long id);

    /** Trả toàn bộ cột điểm của syllabus môn học, sắp theo thuTu. */
    List<CauHinhDanhGiaResponse> findBySyllabusMonHocId(Long syllabusMonHocId);

    /** Tương thích màn hình lớp học phần: lớp chỉ tham chiếu cột điểm qua syllabus môn. */
    List<CauHinhDanhGiaResponse> findByLopHocPhanId(Long lopHocPhanId);

    CauHinhDanhGiaResponse create(CauHinhDanhGiaRequest request);

    CauHinhDanhGiaResponse update(Long id, CauHinhDanhGiaRequest request);

    void delete(Long id);
}
