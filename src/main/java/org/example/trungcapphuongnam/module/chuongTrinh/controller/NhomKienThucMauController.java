package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomKienThucMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.NHOM_KIEN_THUC_MAU)
@RequiredArgsConstructor
public class NhomKienThucMauController {

    private final NhomKienThucMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhomKienThucMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String loaiNhom,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, loaiNhom, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomKienThucMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhomKienThucMauResponse>> create(
            @RequestBody NhomKienThucMauRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomKienThucMauResponse>> update(
            @PathVariable Long id,
            @RequestBody NhomKienThucMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}