package org.example.trungcapphuongnam.module.diem.service;

import org.example.trungcapphuongnam.module.diem.dto.request.BaiKiemTraRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiKiemTraResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaiKiemTraService {
    Page<BaiKiemTraResponse> findAll(Pageable pageable);
    BaiKiemTraResponse findById(Long id);

    /** Trả toàn bộ bài kiểm tra của lớp học phần. */
    List<BaiKiemTraResponse> findByLopHocPhanId(Long lopHocPhanId);

    BaiKiemTraResponse create(BaiKiemTraRequest request);
    BaiKiemTraResponse update(Long id, BaiKiemTraRequest request);
    void delete(Long id);
}