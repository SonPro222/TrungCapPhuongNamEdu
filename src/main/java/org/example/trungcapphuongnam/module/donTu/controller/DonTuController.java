package org.example.trungcapphuongnam.module.donTu.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DonTuPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuCreateRequest;
import org.example.trungcapphuongnam.module.donTu.dto.request.DonTuXuLyRequest;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuDetailResponse;
import org.example.trungcapphuongnam.module.donTu.dto.response.DonTuResponse;
import org.example.trungcapphuongnam.module.donTu.service.DonTuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DonTuPath.BASE)
@RequiredArgsConstructor
public class DonTuController {

    private final DonTuService service;

    @GetMapping("/cua-sinh-vien/{sinhVienId}")
    public ResponseEntity<ApiResponse<Page<DonTuResponse>>> findCuaSinhVien(
            @PathVariable Long sinhVienId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findCuaSinhVien(sinhVienId, pageable)));
    }

    @GetMapping(DonTuPath.ID)
    public ResponseEntity<ApiResponse<DonTuDetailResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DonTuResponse>> create(@RequestBody DonTuCreateRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DonTuPath.ID + "/huy")
    public ResponseEntity<ApiResponse<DonTuResponse>> huy(
            @PathVariable Long id,
            @RequestBody(required = false) DonTuXuLyRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.huy(id, request)));
    }
}
