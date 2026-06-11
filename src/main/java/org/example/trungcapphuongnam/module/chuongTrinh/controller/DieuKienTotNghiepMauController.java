package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienTotNghiepGocService;

@RestController
@RequestMapping("/api/chuongTrinh/dieu-kien-tot-nghiep-goc")
@RequiredArgsConstructor
public class DieuKienTotNghiepGocController {

    private final DieuKienTotNghiepGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DieuKienTotNghiepGocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DieuKienTotNghiepGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DieuKienTotNghiepGocResponse>> create(@RequestBody DieuKienTotNghiepGocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DieuKienTotNghiepGocResponse>> update(
            @PathVariable Long id,
            @RequestBody DieuKienTotNghiepGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
