package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienTotNghiepMauService;

@RestController
@RequestMapping("/api/chuongTrinh/dieu-kien-tot-nghiep-mau")
@RequiredArgsConstructor
public class DieuKienTotNghiepMauController {

    private final DieuKienTotNghiepMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DieuKienTotNghiepMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusChuongTrinhMauId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusChuongTrinhMauId, ma, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DieuKienTotNghiepMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DieuKienTotNghiepMauResponse>> create(@RequestBody DieuKienTotNghiepMauRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DieuKienTotNghiepMauResponse>> update(
            @PathVariable Long id,
            @RequestBody DieuKienTotNghiepMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
