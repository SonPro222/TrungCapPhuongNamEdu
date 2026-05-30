package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomKienThucService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.NHOM_KIEN_THUC)
@RequiredArgsConstructor
public class NhomKienThucController {

    private final NhomKienThucService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhomKienThucResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(required = false) Long nhomKienThucGocId,
            @RequestParam(required = false) String loaiNhom,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(chuongTrinhVersionId, nhomKienThucGocId, loaiNhom, keyword, pageable)
        ));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomKienThucResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhomKienThucResponse>> create(@Valid @RequestBody NhomKienThucRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomKienThucResponse>> update(@PathVariable Long id, @Valid @RequestBody NhomKienThucRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
