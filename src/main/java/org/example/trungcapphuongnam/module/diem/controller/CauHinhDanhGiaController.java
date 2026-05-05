package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.example.trungcapphuongnam.module.diem.service.CauHinhDanhGiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DiemPath.CAU_HINH_DANH_GIA)
@RequiredArgsConstructor
public class CauHinhDanhGiaController {

    private final CauHinhDanhGiaService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauHinhDanhGiaResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<CauHinhDanhGiaResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauHinhDanhGiaResponse>> create(@Valid @RequestBody CauHinhDanhGiaRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<CauHinhDanhGiaResponse>> update(@PathVariable Long id, @Valid @RequestBody CauHinhDanhGiaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
