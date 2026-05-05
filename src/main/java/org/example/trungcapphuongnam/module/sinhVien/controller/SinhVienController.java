package org.example.trungcapphuongnam.module.sinhVien.controller;

import org.example.trungcapphuongnam.common.constant.Path.SinhVienPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SinhVienPath.SINH_VIEN)
@RequiredArgsConstructor
public class SinhVienController {

    private final SinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SinhVienResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienResponse>> create(@Valid @RequestBody SinhVienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienResponse>> update(@PathVariable Long id, @Valid @RequestBody SinhVienRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
