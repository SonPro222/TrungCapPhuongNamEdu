package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomTuChonGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.NHOM_TU_CHON_GOC)
@RequiredArgsConstructor
public class NhomTuChonGocController {

    private final NhomTuChonGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhomTuChonGocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomTuChonGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhomTuChonGocResponse>> create(
            @RequestBody NhomTuChonGocRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomTuChonGocResponse>> update(
            @PathVariable Long id,
            @RequestBody NhomTuChonGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}