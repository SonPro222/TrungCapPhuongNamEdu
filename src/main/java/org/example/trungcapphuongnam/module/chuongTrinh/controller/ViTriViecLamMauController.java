package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ViTriViecLamGocService;

@RestController
@RequestMapping("/api/chuongTrinh/vi-tri-viec-lam-goc")
@RequiredArgsConstructor
public class ViTriViecLamGocController {

    private final ViTriViecLamGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ViTriViecLamGocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(chuongTrinhId, ma, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ViTriViecLamGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ViTriViecLamGocResponse>> create(@RequestBody ViTriViecLamGocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ViTriViecLamGocResponse>> update(
            @PathVariable Long id,
            @RequestBody ViTriViecLamGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
