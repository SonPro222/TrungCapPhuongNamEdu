package org.example.trungcapphuongnam.module.sinhVien.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.SinhVienPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienDiHocLaiRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienBaoLuuTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienBaoLuuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SinhVienPath.BAO_LUU)
@RequiredArgsConstructor
public class SinhVienBaoLuuController {

    private final SinhVienBaoLuuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SinhVienBaoLuuResponse>>> findAll(
            @RequestParam(required = false) Long sinhVienId,
            @RequestParam(required = false) SinhVienBaoLuuTrangThai trangThai,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(sinhVienId, trangThai, pageable)));
    }

    @GetMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienBaoLuuResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienBaoLuuResponse>> baoLuu(
            @Valid @RequestBody SinhVienBaoLuuRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.baoLuu(request)));
    }

    @PutMapping(SinhVienPath.ID + "/di-hoc-lai")
    public ResponseEntity<ApiResponse<SinhVienBaoLuuResponse>> diHocLai(
            @PathVariable Long id,
            @Valid @RequestBody SinhVienDiHocLaiRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.diHocLai(id, request)));
    }

    @PutMapping(SinhVienPath.ID + "/huy")
    public ResponseEntity<ApiResponse<Void>> huy(@PathVariable Long id) {
        service.huy(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}