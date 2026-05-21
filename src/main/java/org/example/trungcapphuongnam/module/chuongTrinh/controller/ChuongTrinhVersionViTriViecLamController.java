package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionViTriViecLamService;

@RestController
@RequestMapping("/api/chuongTrinh/chuong-trinh-version-vi-tri-viec-lam")
@RequiredArgsConstructor
public class ChuongTrinhVersionViTriViecLamController {

    private final ChuongTrinhVersionViTriViecLamService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhVersionViTriViecLamResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(required = false) Long viTriGocId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(chuongTrinhVersionId, viTriGocId, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionViTriViecLamResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhVersionViTriViecLamResponse>> create(@RequestBody ChuongTrinhVersionViTriViecLamRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionViTriViecLamResponse>> update(
            @PathVariable Long id,
            @RequestBody ChuongTrinhVersionViTriViecLamRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
