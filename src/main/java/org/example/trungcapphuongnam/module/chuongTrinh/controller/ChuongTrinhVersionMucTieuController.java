package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionMucTieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionMucTieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionMucTieuService;

@RestController
@RequestMapping("/api/chuongTrinh/chuong-trinh-version-muc-tieu")
@RequiredArgsConstructor
public class ChuongTrinhVersionMucTieuController {

    private final ChuongTrinhVersionMucTieuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhVersionMucTieuResponse>>> findAll(
        Pageable pageable,
        @RequestParam(required = false) Long chuongTrinhVersionId,
        @RequestParam(required = false) Long mucTieuGocId
    ) {
        Page<ChuongTrinhVersionMucTieuResponse> result;

        if (chuongTrinhVersionId != null) {
            result = service.findAllByChuongTrinhVersionId(chuongTrinhVersionId, pageable);
        }         else if (mucTieuGocId != null) {
            result = service.findAllByMucTieuGocId(mucTieuGocId, pageable);
        } else {
            result = service.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionMucTieuResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhVersionMucTieuResponse>> create(@RequestBody ChuongTrinhVersionMucTieuRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionMucTieuResponse>> update(
            @PathVariable Long id,
            @RequestBody ChuongTrinhVersionMucTieuRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
