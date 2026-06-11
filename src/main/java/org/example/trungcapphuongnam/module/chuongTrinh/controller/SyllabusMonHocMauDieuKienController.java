package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocGocDieuKienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_GOC_DIEU_KIEN)
@RequiredArgsConstructor
public class SyllabusMonHocGocDieuKienController {

    private final SyllabusMonHocGocDieuKienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocGocDieuKienResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonHocGocId,
            @RequestParam(required = false) Long dieuKienGocId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusMonHocGocId, dieuKienGocId, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocGocDieuKienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocGocDieuKienResponse>> create(
            @RequestBody SyllabusMonHocGocDieuKienRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocGocDieuKienResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocGocDieuKienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}