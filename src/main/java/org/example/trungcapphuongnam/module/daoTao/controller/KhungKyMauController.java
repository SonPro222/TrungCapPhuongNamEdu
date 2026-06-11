package org.example.trungcapphuongnam.module.daoTao.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhungKyGocResponse;
import org.example.trungcapphuongnam.module.daoTao.service.KhungKyGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.KHUNG_KY_GOC)
@RequiredArgsConstructor
public class KhungKyGocController {

    private final KhungKyGocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KhungKyGocResponse>>> findAll(
            @RequestParam(required = false) String keyword,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(keyword, pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<KhungKyGocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KhungKyGocResponse>> create(
            @Valid @RequestBody KhungKyGocRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<KhungKyGocResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody KhungKyGocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}