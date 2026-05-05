package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.service.LichSuBaiTapLopService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.LICH_SU_BAI_TAP_LOP)
@RequiredArgsConstructor
public class LichSuBaiTapLopController {

    private final LichSuBaiTapLopService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LichSuBaiTapLopResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LichSuBaiTapLopResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LichSuBaiTapLopResponse>> create(@Valid @RequestBody LichSuBaiTapLopRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LichSuBaiTapLopResponse>> update(@PathVariable Long id, @Valid @RequestBody LichSuBaiTapLopRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
