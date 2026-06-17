package org.example.trungcapphuongnam.module.donTu.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DonTuPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuDuyetBaoLuuRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuDuyetKhoiPhucDiemDanhRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuXuLyRequest;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.example.trungcapphuongnam.module.donTu.enums.DonTuTrangThai;
import org.example.trungcapphuongnam.module.donTu.service.AdminDonTuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DonTuPath.ADMIN_BASE)
@RequiredArgsConstructor
public class AdminDonTuController {

    private final AdminDonTuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DonTuResponse>>> findAll(
            @RequestParam(required = false) Long sinhVienId,
            @RequestParam(required = false) String loaiDonTuMa,
            @RequestParam(required = false) DonTuTrangThai trangThai,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(sinhVienId, loaiDonTuMa, trangThai, pageable)));
    }

    @GetMapping(DonTuPath.ID)
    public ResponseEntity<ApiResponse<DonTuDetailResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PutMapping(DonTuPath.ID + "/tiep-nhan")
    public ResponseEntity<ApiResponse<DonTuResponse>> tiepNhan(
            @PathVariable Long id,
            @RequestBody(required = false) DonTuXuLyRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.tiepNhan(id, request)));
    }

    @PutMapping(DonTuPath.ID + "/yeu-cau-bo-sung")
    public ResponseEntity<ApiResponse<DonTuResponse>> yeuCauBoSung(
            @PathVariable Long id,
            @RequestBody(required = false) DonTuXuLyRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.yeuCauBoSung(id, request)));
    }

    @PutMapping(DonTuPath.ID + "/tu-choi")
    public ResponseEntity<ApiResponse<DonTuResponse>> tuChoi(
            @PathVariable Long id,
            @RequestBody(required = false) DonTuXuLyRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.tuChoi(id, request)));
    }

    @PutMapping(DonTuPath.ID + "/duyet-bao-luu")
    public ResponseEntity<ApiResponse<DonTuResponse>> duyetBaoLuu(
            @PathVariable Long id,
            @RequestBody DonTuDuyetBaoLuuRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.duyetBaoLuu(id, request)));
    }

    @PutMapping(DonTuPath.ID + "/duyet-khoi-phuc-diem-danh-gmail")
    public ResponseEntity<ApiResponse<DonTuResponse>> duyetKhoiPhucDiemDanhGmail(
            @PathVariable Long id,
            @RequestBody DonTuDuyetKhoiPhucDiemDanhRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.duyetKhoiPhucDiemDanhGmail(id, request)));
    }
}
