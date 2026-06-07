package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiYeuCauDoiLich;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface YeuCauDoiLichService {

    Page<YeuCauDoiLichResponse> getAll(
            Long lichHocCuId,
            Long giaoVienId,
            Long nguoiDuyetId,
            TrangThaiYeuCauDoiLich trangThai,
            LocalDate tuNgayMoi,
            LocalDate denNgayMoi,
            Pageable pageable
    );

    YeuCauDoiLichResponse getById(Long id);

    YeuCauDoiLichResponse create(YeuCauDoiLichRequest request);

    YeuCauDoiLichResponse update(Long id, YeuCauDoiLichRequest request);

    void delete(Long id);
}