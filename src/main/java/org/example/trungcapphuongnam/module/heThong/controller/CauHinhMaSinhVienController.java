package org.example.trungcapphuongnam.module.heThong.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.CauHinhMaSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.CauHinhMaSinhVienResponse;
import org.example.trungcapphuongnam.module.heThong.service.CauHinhMaSinhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/he-thong/cau-hinh-ma-sinh-vien")
@RequiredArgsConstructor
public class CauHinhMaSinhVienController {

    private final CauHinhMaSinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauHinhMaSinhVienResponse>>> findAll(
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(nganhId, chuongTrinhVersionId, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CauHinhMaSinhVienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauHinhMaSinhVienResponse>> create(
            @Valid @RequestBody CauHinhMaSinhVienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CauHinhMaSinhVienResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CauHinhMaSinhVienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}