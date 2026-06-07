package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SinhVienLopHocPhanService {

    Page<SinhVienLopHocPhanResponse> getAll(
            Long lopHocPhanId,
            Long sinhVienId,
            TrangThaiSinhVienLopHocPhan trangThai,
            Pageable pageable
    );

    SinhVienLopHocPhanResponse getById(Long id);

    Page<SinhVienLopHocPhanResponse> getByLopHocPhan(Long lopHocPhanId, Pageable pageable);

    Page<SinhVienResponse> getSinhVienTrongLopHocPhan(Long lopHocPhanId, Pageable pageable);

    Page<SinhVienResponse> getSinhVienChuaVaoLopHocPhan(
            Long chuongTrinhVersionId,
            Long lopHocPhanId,
            Pageable pageable
    );

    SinhVienLopHocPhanResponse create(SinhVienLopHocPhanRequest request);

    SinhVienLopHocPhanResponse update(Long id, SinhVienLopHocPhanRequest request);

    void delete(Long id);
}