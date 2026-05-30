package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhLoaiChuongTrinhResponse;
import org.example.trungcapphuongnam.module.daoTao.service.NganhLoaiChuongTrinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.NGANH_LOAI_CHUONG_TRINH)
@RequiredArgsConstructor
public class NganhLoaiChuongTrinhController {

    private final NganhLoaiChuongTrinhService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NganhLoaiChuongTrinhResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long loaiChuongTrinhId,
            @RequestParam(required = false) String trangThai
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(nganhId, loaiChuongTrinhId, trangThai, pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhLoaiChuongTrinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NganhLoaiChuongTrinhResponse>> create(@Valid @RequestBody NganhLoaiChuongTrinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PostMapping("/luu")
    public ResponseEntity<ApiResponse<NganhLoaiChuongTrinhResponse>> luu(@Valid @RequestBody NganhLoaiChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.luu(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhLoaiChuongTrinhResponse>> update(@PathVariable Long id, @Valid @RequestBody NganhLoaiChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
