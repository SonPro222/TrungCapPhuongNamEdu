package org.example.trungcapphuongnam.module.thi.controller;

import org.example.trungcapphuongnam.common.constant.Path.ThiPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.thi.dto.request.SinhVienDuThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.SinhVienDuThiResponse;
import org.example.trungcapphuongnam.module.thi.service.SinhVienDuThiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ThiPath.SINH_VIEN_DU_THI)
@RequiredArgsConstructor
public class SinhVienDuThiController {
    private final SinhVienDuThiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SinhVienDuThiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<SinhVienDuThiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienDuThiResponse>> create(@Valid @RequestBody SinhVienDuThiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<SinhVienDuThiResponse>> update(@PathVariable Long id, @Valid @RequestBody SinhVienDuThiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
