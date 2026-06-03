package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.service.DapAnCauHoiVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.DAP_AN_CAU_HOI_VERSION)
@RequiredArgsConstructor
public class DapAnCauHoiVersionController {

    private final DapAnCauHoiVersionService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DapAnCauHoiVersionResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<DapAnCauHoiVersionResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DapAnCauHoiVersionResponse>> create(@Valid @RequestBody DapAnCauHoiVersionRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<DapAnCauHoiVersionResponse>> update(@PathVariable Long id, @Valid @RequestBody DapAnCauHoiVersionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
