package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MucTieuChuongTrinhGocService;

@RestController
@RequestMapping("/api/chuongTrinh/muc-tieu-chuong-trinh-goc")
@RequiredArgsConstructor
public class MucTieuChuongTrinhGocController {

    private final MucTieuChuongTrinhGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MucTieuChuongTrinhGocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String loai,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, loai, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MucTieuChuongTrinhGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MucTieuChuongTrinhGocResponse>> create(@RequestBody MucTieuChuongTrinhGocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MucTieuChuongTrinhGocResponse>> update(
            @PathVariable Long id,
            @RequestBody MucTieuChuongTrinhGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
