package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonQuyDoiDiemMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonQuyDoiDiemMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhMonQuyDoiDiemMauService;

@RestController
@RequestMapping("/api/chuongTrinh/chuong-trinh-mon-quy-doi-diem-mau")
@RequiredArgsConstructor
public class ChuongTrinhMonQuyDoiDiemMauController {

    private final ChuongTrinhMonQuyDoiDiemMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhMonQuyDoiDiemMauResponse>>> findAll(
        Pageable pageable,
        @RequestParam(required = false) Long chuongTrinhMonId,
        @RequestParam(required = false) Long quyDoiDiemMauId
    ) {
        Page<ChuongTrinhMonQuyDoiDiemMauResponse> result;

        if (chuongTrinhMonId != null) {
            result = service.findAllByChuongTrinhMonId(chuongTrinhMonId, pageable);
        }         else if (quyDoiDiemMauId != null) {
            result = service.findAllByQuyDoiDiemMauId(quyDoiDiemMauId, pageable);
        } else {
            result = service.findAll(pageable);
        }
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhMonQuyDoiDiemMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhMonQuyDoiDiemMauResponse>> create(@RequestBody ChuongTrinhMonQuyDoiDiemMauRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhMonQuyDoiDiemMauResponse>> update(
            @PathVariable Long id,
            @RequestBody ChuongTrinhMonQuyDoiDiemMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
