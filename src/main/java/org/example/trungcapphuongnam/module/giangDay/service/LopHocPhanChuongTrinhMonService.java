package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanChuongTrinhMonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LopHocPhanChuongTrinhMonService {

    Page<LopHocPhanChuongTrinhMonResponse> getAll(
            Long lopHocPhanId,
            Long chuongTrinhMonId,
            Pageable pageable
    );

    LopHocPhanChuongTrinhMonResponse getById(Long id);

    LopHocPhanChuongTrinhMonResponse create(LopHocPhanChuongTrinhMonRequest request);

    LopHocPhanChuongTrinhMonResponse update(Long id, LopHocPhanChuongTrinhMonRequest request);

    void delete(Long id);
}