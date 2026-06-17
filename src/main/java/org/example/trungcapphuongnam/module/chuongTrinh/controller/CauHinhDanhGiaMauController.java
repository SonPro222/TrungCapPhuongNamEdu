package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.CauHinhDanhGiaMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.CauHinhDanhGiaMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.CauHinhDanhGiaMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.CAU_HINH_DANH_GIA_MAU)
@RequiredArgsConstructor
public class CauHinhDanhGiaMauController {

    private final CauHinhDanhGiaMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauHinhDanhGiaMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonHocMauId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(syllabusMonHocMauId, keyword, pageable)
        ));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<CauHinhDanhGiaMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauHinhDanhGiaMauResponse>> create(
            @Valid @RequestBody CauHinhDanhGiaMauRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<CauHinhDanhGiaMauResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CauHinhDanhGiaMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
