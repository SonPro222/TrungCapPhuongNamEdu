package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
@RestController
@RequestMapping(ChuongTrinhPath.CHUONG_TRINH)
@RequiredArgsConstructor
public class ChuongTrinhController {

    private final ChuongTrinhService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhResponse>>> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping("/theo-nganh/{nganhId}")
    public ResponseEntity<ApiResponse<Page<ChuongTrinhResponse>>> findAllByNganhId(
            @PathVariable Long nganhId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAllByNganhId(nganhId, pageable)
        ));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ChuongTrinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhResponse>> create(@Valid @RequestBody ChuongTrinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ChuongTrinhResponse>> update(@PathVariable Long id, @Valid @RequestBody ChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
