package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.service.LanLamBaiCauHoiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.LAN_LAM_BAI_CAU_HOI)
@RequiredArgsConstructor
public class LanLamBaiCauHoiController {

    private final LanLamBaiCauHoiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LanLamBaiCauHoiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LanLamBaiCauHoiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LanLamBaiCauHoiResponse>> create(@Valid @RequestBody LanLamBaiCauHoiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LanLamBaiCauHoiResponse>> update(@PathVariable Long id, @Valid @RequestBody LanLamBaiCauHoiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
