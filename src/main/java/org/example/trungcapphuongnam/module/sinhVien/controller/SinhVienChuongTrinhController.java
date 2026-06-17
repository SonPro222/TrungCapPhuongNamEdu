package org.example.trungcapphuongnam.module.sinhVien.controller;

import org.example.trungcapphuongnam.common.constant.Path.SinhVienPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienChuongTrinhRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienChuongTrinhResponse;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienChuongTrinhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(SinhVienPath.SINH_VIEN_CHUONG_TRINH)
@RequiredArgsConstructor
public class SinhVienChuongTrinhController {

    private final SinhVienChuongTrinhService service;

    /**
     * GET /api/sinh-vien/chuong-trinh
     * Nếu có ?sinhVienId=X thì trả danh sách theo sinh viên đó,
     * ngược lại trả toàn bộ (phân trang).
     */
    @GetMapping
    public ResponseEntity<ApiResponse<Page<SinhVienChuongTrinhResponse>>> findAll(
            @RequestParam(required = false) Long sinhVienId,
            Pageable pageable
    ) {
        if (sinhVienId != null) {
            List<SinhVienChuongTrinhResponse> list = service.findBySinhVienId(sinhVienId);
            Page<SinhVienChuongTrinhResponse> page = new PageImpl<>(list, pageable, list.size());
            return ResponseEntity.ok(ApiResponse.ok(page));
        }
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienChuongTrinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienChuongTrinhResponse>> create(@Valid @RequestBody SinhVienChuongTrinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienChuongTrinhResponse>> update(@PathVariable Long id, @Valid @RequestBody SinhVienChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}