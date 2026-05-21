package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocDieuKienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_DIEU_KIEN)
@RequiredArgsConstructor
public class SyllabusMonHocDieuKienController {

    private final SyllabusMonHocDieuKienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocDieuKienResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonId,
            @RequestParam(required = false) Long dieuKienGocId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusMonId, dieuKienGocId, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocDieuKienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocDieuKienResponse>> create(@RequestBody SyllabusMonHocDieuKienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocDieuKienResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocDieuKienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}