package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiGocRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiGocResponse;
import org.example.trungcapphuongnam.module.lms.service.CauHoiGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.CAU_HOI_GOC)
@RequiredArgsConstructor
public class CauHoiGocController {

    private final CauHoiGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauHoiGocResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauHoiGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauHoiGocResponse>> create(@Valid @RequestBody CauHoiGocRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauHoiGocResponse>> update(@PathVariable Long id, @Valid @RequestBody CauHoiGocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
