package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhMonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
@RestController
@RequestMapping(ChuongTrinhPath.MON)
@RequiredArgsConstructor
public class ChuongTrinhMonController {

    private final ChuongTrinhMonService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhMonResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ChuongTrinhMonResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhMonResponse>> create(@Valid @RequestBody ChuongTrinhMonRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ChuongTrinhMonResponse>> update(@PathVariable Long id, @Valid @RequestBody ChuongTrinhMonRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
