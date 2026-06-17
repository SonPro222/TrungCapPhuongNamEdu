package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauDieuKienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_MAU_DIEU_KIEN)
@RequiredArgsConstructor
public class SyllabusMonHocMauDieuKienController {

    private final SyllabusMonHocMauDieuKienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocMauDieuKienResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonHocMauId,
            @RequestParam(required = false) Long dieuKienMauId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusMonHocMauId, dieuKienMauId, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauDieuKienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocMauDieuKienResponse>> create(
            @RequestBody SyllabusMonHocMauDieuKienRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauDieuKienResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocMauDieuKienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}