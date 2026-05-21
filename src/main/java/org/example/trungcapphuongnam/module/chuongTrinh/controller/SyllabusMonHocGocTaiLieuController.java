package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocGocTaiLieuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_GOC_TAI_LIEU)
@RequiredArgsConstructor
public class SyllabusMonHocGocTaiLieuController {

    private final SyllabusMonHocGocTaiLieuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocGocTaiLieuResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonHocGocId,
            @RequestParam(required = false) Long taiLieuGocId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusMonHocGocId, taiLieuGocId, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocGocTaiLieuResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocGocTaiLieuResponse>> create(
            @RequestBody SyllabusMonHocGocTaiLieuRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocGocTaiLieuResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocGocTaiLieuRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}