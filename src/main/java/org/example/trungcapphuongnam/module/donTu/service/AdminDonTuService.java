package org.example.trungcapphuongnam.module.donTu.service;

import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuDuyetBaoLuuRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuDuyetKhoiPhucDiemDanhRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuXuLyRequest;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminDonTuService {

    Page<DonTuResponse> findAll(Long sinhVienId, String loaiDonTuMa, DonTuTrangThai trangThai, Pageable pageable);

    DonTuDetailResponse findById(Long id);

    DonTuResponse tiepNhan(Long id, DonTuXuLyRequest request);

    DonTuResponse yeuCauBoSung(Long id, DonTuXuLyRequest request);

    DonTuResponse tuChoi(Long id, DonTuXuLyRequest request);

    DonTuResponse duyetBaoLuu(Long id, DonTuDuyetBaoLuuRequest request);

    DonTuResponse duyetKhoiPhucDiemDanhGmail(Long id, DonTuDuyetKhoiPhucDiemDanhRequest request);
}
