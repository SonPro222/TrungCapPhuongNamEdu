package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface LichHocService {
    Page<LichHocResponse> getAll(
            Long lopHocPhanId,
            String keywordLop,
            String keywordGiaoVien,
            String keywordPhong,
            String keywordCa,
            TrangThaiLichHoc trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    );

    LichHocResponse getById(Long id);

    LichHocResponse create(LichHocRequest request);

    LichHocResponse update(Long id, LichHocRequest request);

    void delete(Long id);

}