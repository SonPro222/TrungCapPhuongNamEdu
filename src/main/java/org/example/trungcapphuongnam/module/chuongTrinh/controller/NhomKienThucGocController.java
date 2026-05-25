package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomKienThucGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.NHOM_KIEN_THUC_GOC)
@RequiredArgsConstructor
public class NhomKienThucGocController {

    private final NhomKienThucGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhomKienThucGocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String loaiNhom,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, loaiNhom, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomKienThucGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhomKienThucGocResponse>> create(
            @RequestBody NhomKienThucGocRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomKienThucGocResponse>> update(
            @PathVariable Long id,
            @RequestBody NhomKienThucGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}