package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface LopHocPhanService {

    Page<LopHocPhanResponse> getAll(
            String keyword,
            Long chuongTrinhMonId,
            Long monHocId,
            LoaiLopHocPhan loaiLopHocPhan,
            TrangThaiLopHocPhan trangThai,
            LocalDate tuNgay,
            LocalDate denNgay,
            Pageable pageable
    );

    LopHocPhanResponse getById(Long id);

    LopHocPhanResponse create(LopHocPhanRequest request);

    LopHocPhanResponse update(Long id, LopHocPhanRequest request);

    void delete(Long id);
}