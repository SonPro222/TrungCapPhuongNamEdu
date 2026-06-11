package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.SyllabusMonHocCauTrucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhCauTrucService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC)
@RequiredArgsConstructor
public class SyllabusMonHocController {

    private final SyllabusMonHocService service;
    private final ChuongTrinhCauTrucService cauTrucService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhMonId,
            @RequestParam(required = false) Long syllabusMonHocMauId,
            @RequestParam(required = false) Boolean batBuocDuThi,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(chuongTrinhMonId, syllabusMonHocMauId, batBuocDuThi, keyword, pageable)
        ));
    }

    @GetMapping("/{id}/xem")
    public ResponseEntity<ApiResponse<SyllabusMonHocCauTrucResponse>> findChiTietDeXem(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(cauTrucService.findSyllabusChiTietById(id)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocResponse>> create(@Valid @RequestBody SyllabusMonHocRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocResponse>> update(@PathVariable Long id, @Valid @RequestBody SyllabusMonHocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
