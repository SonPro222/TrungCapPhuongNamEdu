package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.CauHinhDanhGiaRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.CauHinhDanhGiaResponse;
import org.example.trungcapphuongnam.module.diem.service.CauHinhDanhGiaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DiemPath.CAU_HINH_DANH_GIA)
@RequiredArgsConstructor
public class CauHinhDanhGiaController {

    private final CauHinhDanhGiaService service;

    /**
     * GET /api/diem/cau-hinh-danh-gia
     * Nếu có ?lopHocPhanId=X → trả cột điểm của lớp đó (dùng cho trang chi tiết SV).
     * Không có → trả toàn bộ (phân trang).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauHinhDanhGiaResponse>>> findAll(
            @RequestParam(required = false) Long lopHocPhanId,
            Pageable pageable
    ) {
        if (lopHocPhanId != null) {
            List<CauHinhDanhGiaResponse> list = service.findByLopHocPhanId(lopHocPhanId);
            return ResponseEntity.ok(ApiResponse.ok(new PageImpl<>(list, pageable, list.size())));
        }
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<CauHinhDanhGiaResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauHinhDanhGiaResponse>> create(
            @Valid @RequestBody CauHinhDanhGiaRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<CauHinhDanhGiaResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CauHinhDanhGiaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}