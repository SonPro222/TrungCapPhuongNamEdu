package org.example.trungcapphuongnam.module.chuongTrinh.controller;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.TaiLieuGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.TaiLieuGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.TaiLieuGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.TAI_LIEU_GOC)
@RequiredArgsConstructor
public class TaiLieuGocController {

    private final TaiLieuGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaiLieuGocResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String loai,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, loai, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<TaiLieuGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaiLieuGocResponse>> create(@RequestBody TaiLieuGocRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<TaiLieuGocResponse>> update(
            @PathVariable Long id,
            @RequestBody TaiLieuGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}