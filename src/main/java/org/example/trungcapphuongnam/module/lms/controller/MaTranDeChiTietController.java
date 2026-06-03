package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeChiTietRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeChiTietResponse;
import org.example.trungcapphuongnam.module.lms.service.MaTranDeChiTietService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.MA_TRAN_DE_CHI_TIET)
@RequiredArgsConstructor
public class MaTranDeChiTietController {

    private final MaTranDeChiTietService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MaTranDeChiTietResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<MaTranDeChiTietResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MaTranDeChiTietResponse>> create(@Valid @RequestBody MaTranDeChiTietRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<MaTranDeChiTietResponse>> update(@PathVariable Long id, @Valid @RequestBody MaTranDeChiTietRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
