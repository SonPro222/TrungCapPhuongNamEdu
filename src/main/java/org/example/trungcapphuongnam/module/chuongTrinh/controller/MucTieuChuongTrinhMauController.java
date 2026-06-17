package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MucTieuChuongTrinhMauService;

@RestController
@RequestMapping("/api/chuongTrinh/muc-tieu-chuong-trinh-mau")
@RequiredArgsConstructor
public class MucTieuChuongTrinhMauController {

    private final MucTieuChuongTrinhMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MucTieuChuongTrinhMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusChuongTrinhMauId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String loai,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusChuongTrinhMauId, ma, loai, keyword, pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MucTieuChuongTrinhMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MucTieuChuongTrinhMauResponse>> create(@RequestBody MucTieuChuongTrinhMauRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MucTieuChuongTrinhMauResponse>> update(
            @PathVariable Long id,
            @RequestBody MucTieuChuongTrinhMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
