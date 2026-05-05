package org.example.trungcapphuongnam.module.sinhVien.controller;

import org.example.trungcapphuongnam.common.constant.Path.SinhVienPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.MienTruBaoLuuRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.MienTruBaoLuuResponse;
import org.example.trungcapphuongnam.module.sinhVien.service.MienTruBaoLuuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(SinhVienPath.MIEN_TRU_BAO_LUU)
@RequiredArgsConstructor
public class MienTruBaoLuuController {

    private final MienTruBaoLuuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MienTruBaoLuuResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<MienTruBaoLuuResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MienTruBaoLuuResponse>> create(@Valid @RequestBody MienTruBaoLuuRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<MienTruBaoLuuResponse>> update(@PathVariable Long id, @Valid @RequestBody MienTruBaoLuuRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
