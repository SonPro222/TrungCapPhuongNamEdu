package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.service.LoaiChuongTrinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.LOAI_CHUONG_TRINH)
@RequiredArgsConstructor
public class LoaiChuongTrinhController {

    private final LoaiChuongTrinhService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LoaiChuongTrinhResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<LoaiChuongTrinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LoaiChuongTrinhResponse>> create(@Valid @RequestBody LoaiChuongTrinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<LoaiChuongTrinhResponse>> update(@PathVariable Long id, @Valid @RequestBody LoaiChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
