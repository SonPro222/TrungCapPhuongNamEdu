package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ViTriViecLamMauService;

@RestController
@RequestMapping("/api/chuongTrinh/vi-tri-viec-lam-mau")
@RequiredArgsConstructor
public class ViTriViecLamMauController {

    private final ViTriViecLamMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ViTriViecLamMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusChuongTrinhMauId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusChuongTrinhMauId, ma, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ViTriViecLamMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ViTriViecLamMauResponse>> create(@RequestBody ViTriViecLamMauRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ViTriViecLamMauResponse>> update(
            @PathVariable Long id,
            @RequestBody ViTriViecLamMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
