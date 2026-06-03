package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiDapAnResponse;
import org.example.trungcapphuongnam.module.lms.service.LanLamBaiDapAnService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.LAN_LAM_BAI_DAP_AN)
@RequiredArgsConstructor
public class LanLamBaiDapAnController {

    private final LanLamBaiDapAnService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LanLamBaiDapAnResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LanLamBaiDapAnResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LanLamBaiDapAnResponse>> create(@Valid @RequestBody LanLamBaiDapAnRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LanLamBaiDapAnResponse>> update(@PathVariable Long id, @Valid @RequestBody LanLamBaiDapAnRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
