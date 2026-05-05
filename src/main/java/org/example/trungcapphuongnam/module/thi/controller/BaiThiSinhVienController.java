package org.example.trungcapphuongnam.module.thi.controller;

import org.example.trungcapphuongnam.common.constant.Path.ThiPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.thi.dto.request.BaiThiSinhVienRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.BaiThiSinhVienResponse;
import org.example.trungcapphuongnam.module.thi.service.BaiThiSinhVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ThiPath.BAI_THI_SINH_VIEN)
@RequiredArgsConstructor
public class BaiThiSinhVienController {
    private final BaiThiSinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BaiThiSinhVienResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<BaiThiSinhVienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaiThiSinhVienResponse>> create(@Valid @RequestBody BaiThiSinhVienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<BaiThiSinhVienResponse>> update(@PathVariable Long id, @Valid @RequestBody BaiThiSinhVienRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
