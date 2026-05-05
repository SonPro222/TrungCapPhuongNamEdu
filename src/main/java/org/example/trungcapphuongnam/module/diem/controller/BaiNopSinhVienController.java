package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.BaiNopSinhVienRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiNopSinhVienResponse;
import org.example.trungcapphuongnam.module.diem.service.BaiNopSinhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DiemPath.BAI_NOP_SINH_VIEN)
@RequiredArgsConstructor
public class BaiNopSinhVienController {

    private final BaiNopSinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BaiNopSinhVienResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<BaiNopSinhVienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaiNopSinhVienResponse>> create(@Valid @RequestBody BaiNopSinhVienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<BaiNopSinhVienResponse>> update(@PathVariable Long id, @Valid @RequestBody BaiNopSinhVienRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
