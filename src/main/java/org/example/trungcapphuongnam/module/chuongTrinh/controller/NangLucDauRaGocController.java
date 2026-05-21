package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NangLucDauRaGocService;

@RestController
@RequestMapping("/api/chuongTrinh/nang-luc-dau-ra-goc")
@RequiredArgsConstructor
public class NangLucDauRaGocController {

    private final NangLucDauRaGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NangLucDauRaGocResponse>>> findAll(
        Pageable pageable,
        @RequestParam(required = false) String ma
    ) {
        Page<NangLucDauRaGocResponse> result;

        if (ma != null) {
            result = service.findAllByMa(ma, pageable);
        } else {
            result = service.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NangLucDauRaGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NangLucDauRaGocResponse>> create(@RequestBody NangLucDauRaGocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NangLucDauRaGocResponse>> update(
            @PathVariable Long id,
            @RequestBody NangLucDauRaGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
