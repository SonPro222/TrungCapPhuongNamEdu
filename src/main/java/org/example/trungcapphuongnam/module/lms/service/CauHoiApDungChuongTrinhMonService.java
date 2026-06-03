package org.example.trungcapphuongnam.module.lms.service;

import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiApDungChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiApDungChuongTrinhMonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CauHoiApDungChuongTrinhMonService {
    Page<CauHoiApDungChuongTrinhMonResponse> findAll(Pageable pageable);
    CauHoiApDungChuongTrinhMonResponse findById(Long id);
    CauHoiApDungChuongTrinhMonResponse create(CauHoiApDungChuongTrinhMonRequest request);
    CauHoiApDungChuongTrinhMonResponse update(Long id, CauHoiApDungChuongTrinhMonRequest request);
    void delete(Long id);
}
