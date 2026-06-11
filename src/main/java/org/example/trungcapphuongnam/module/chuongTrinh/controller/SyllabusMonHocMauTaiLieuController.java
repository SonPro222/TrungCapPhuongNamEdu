package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauTaiLieuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_MAU_TAI_LIEU)
@RequiredArgsConstructor
public class SyllabusMonHocMauTaiLieuController {

    private final SyllabusMonHocMauTaiLieuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocMauTaiLieuResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonHocMauId,
            @RequestParam(required = false) Long taiLieuMauId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusMonHocMauId, taiLieuMauId, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauTaiLieuResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocMauTaiLieuResponse>> create(
            @RequestBody SyllabusMonHocMauTaiLieuRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauTaiLieuResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocMauTaiLieuRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}