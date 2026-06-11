package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_MAU)
@RequiredArgsConstructor
public class SyllabusMonHocMauController {

    private final SyllabusMonHocMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long monHocId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(monHocId, ma, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocMauResponse>> create(@RequestBody SyllabusMonHocMauRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}