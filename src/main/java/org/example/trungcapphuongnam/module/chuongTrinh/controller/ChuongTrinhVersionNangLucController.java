package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionNangLucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionNangLucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionNangLucService;

@RestController
@RequestMapping("/api/chuongTrinh/chuong-trinh-version-nang-luc")
@RequiredArgsConstructor
public class ChuongTrinhVersionNangLucController {

    private final ChuongTrinhVersionNangLucService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhVersionNangLucResponse>>> findAll(
        Pageable pageable,
        @RequestParam(required = false) Long chuongTrinhVersionId,
        @RequestParam(required = false) Long nangLucGocId
    ) {
        Page<ChuongTrinhVersionNangLucResponse> result;

        if (chuongTrinhVersionId != null) {
            result = service.findAllByChuongTrinhVersionId(chuongTrinhVersionId, pageable);
        }         else if (nangLucGocId != null) {
            result = service.findAllByNangLucGocId(nangLucGocId, pageable);
        } else {
            result = service.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionNangLucResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhVersionNangLucResponse>> create(@RequestBody ChuongTrinhVersionNangLucRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhVersionNangLucResponse>> update(
            @PathVariable Long id,
            @RequestBody ChuongTrinhVersionNangLucRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
