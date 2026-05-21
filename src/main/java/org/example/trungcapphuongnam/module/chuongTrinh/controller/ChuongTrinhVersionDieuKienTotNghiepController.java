package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionDieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionDieuKienTotNghiepResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionDieuKienTotNghiepService;

@RestController
@RequestMapping("/api/chuongTrinh/chuong-trinh-version-dieu-kien-tot-nghiep")
@RequiredArgsConstructor
public class ChuongTrinhVersionDieuKienTotNghiepController {

    private final ChuongTrinhVersionDieuKienTotNghiepService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhVersionDieuKienTotNghiepResponse>>> findAll(
        Pageable pageable,
        @RequestParam(required = false) Long chuongTrinhVersionId,
        @RequestParam(required = false) Long dieuKienGocId
    ) {
        Page<ChuongTrinhVersionDieuKienTotNghiepResponse> result;

        if (chuongTrinhVersionId != null) {
            result = service.findAllByChuongTrinhVersionId(chuongTrinhVersionId, pageable);
        }         else if (dieuKienGocId != null) {
            result = service.findAllByDieuKienGocId(dieuKienGocId, pageable);
        } else {
            result = service.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionDieuKienTotNghiepResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhVersionDieuKienTotNghiepResponse>> create(@RequestBody ChuongTrinhVersionDieuKienTotNghiepRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionDieuKienTotNghiepResponse>> update(
            @PathVariable Long id,
            @RequestBody ChuongTrinhVersionDieuKienTotNghiepRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
