package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.DiemChiTietRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.DiemChiTietResponse;
import org.example.trungcapphuongnam.module.diem.service.DiemChiTietService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DiemPath.DIEM_CHI_TIET)
@RequiredArgsConstructor
public class DiemChiTietController {

    private final DiemChiTietService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DiemChiTietResponse>>> findAll(
            @RequestParam(required = false) Long sinhVienId,
            @RequestParam(required = false) Long lopHocPhanId,
            @RequestParam(required = false) Long baiKiemTraId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findTheoBoLoc(sinhVienId, lopHocPhanId, baiKiemTraId, pageable)
        ));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<DiemChiTietResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiemChiTietResponse>> create(@Valid @RequestBody DiemChiTietRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<DiemChiTietResponse>> update(@PathVariable Long id, @Valid @RequestBody DiemChiTietRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
