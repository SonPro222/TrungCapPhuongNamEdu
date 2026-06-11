package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_CHUONG_TRINH_MAU)
@RequiredArgsConstructor
public class SyllabusChuongTrinhMauController {

    private final SyllabusChuongTrinhMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusChuongTrinhMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(chuongTrinhId, ma, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhMauResponse>> create(
            @RequestBody SyllabusChuongTrinhMauRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhMauResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusChuongTrinhMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}