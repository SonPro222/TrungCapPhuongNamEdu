package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienMonHocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.DIEU_KIEN_MON_HOC)
@RequiredArgsConstructor
public class DieuKienMonHocController {

    private final DieuKienMonHocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DieuKienMonHocResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<DieuKienMonHocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DieuKienMonHocResponse>> create(@Valid @RequestBody DieuKienMonHocRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<DieuKienMonHocResponse>> update(@PathVariable Long id, @Valid @RequestBody DieuKienMonHocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }


    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
