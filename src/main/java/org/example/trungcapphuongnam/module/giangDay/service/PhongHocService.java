package org.example.trungcapphuongnam.module.giangDay.service;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiPhong;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PhongHocService {

    Page<PhongHocResponse> getAll(
            String keyword,
            LoaiPhong loaiPhong,
            TrangThaiPhongHoc trangThai,
            Pageable pageable
    );

    PhongHocResponse getById(Long id);

    PhongHocResponse create(PhongHocRequest request);

    PhongHocResponse update(Long id, PhongHocRequest request);

    void delete(Long id);
}