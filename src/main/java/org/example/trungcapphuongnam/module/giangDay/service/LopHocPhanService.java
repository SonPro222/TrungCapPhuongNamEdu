package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.AutoTaoLopHocPhanTheoKyRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.AutoTaoLopHocPhanTheoKyResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LopHocPhanService {

    Page<LopHocPhanResponse> getAll(
            String keyword,
            Long chuongTrinhVersionId,
            Long chuongTrinhMonId,
            Long monHocId,
            LoaiLopHocPhan loaiLopHocPhan,
            TrangThaiLopHocPhan trangThai,
            Pageable pageable
    );

    LopHocPhanResponse getById(Long id);

    LopHocPhanResponse create(LopHocPhanRequest request);

    LopHocPhanResponse update(Long id, LopHocPhanRequest request);

    void delete(Long id);

    AutoTaoLopHocPhanTheoKyResponse autoTaoLopHocPhanTheoKy(AutoTaoLopHocPhanTheoKyRequest request);
}