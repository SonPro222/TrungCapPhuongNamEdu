package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.BaiKiemTraRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.BaiKiemTraResponse;
import org.example.trungcapphuongnam.module.diem.service.BaiKiemTraService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DiemPath.BAI_KIEM_TRA)
@RequiredArgsConstructor
public class BaiKiemTraController {

    private final BaiKiemTraService service;

    /**
     * GET /api/diem/bai-kiem-tra
     * Nếu có ?lopHocPhanId=X → trả bài kiểm tra của lớp đó.
     * Không có → trả toàn bộ (phân trang).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<BaiKiemTraResponse>>> findAll(
            @RequestParam(required = false) Long lopHocPhanId,
            Pageable pageable
    ) {
        if (lopHocPhanId != null) {
            List<BaiKiemTraResponse> list = service.findByLopHocPhanId(lopHocPhanId);
            return ResponseEntity.ok(ApiResponse.ok(new PageImpl<>(list, pageable, list.size())));
        }
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<BaiKiemTraResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaiKiemTraResponse>> create(
            @Valid @RequestBody BaiKiemTraRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<BaiKiemTraResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody BaiKiemTraRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}