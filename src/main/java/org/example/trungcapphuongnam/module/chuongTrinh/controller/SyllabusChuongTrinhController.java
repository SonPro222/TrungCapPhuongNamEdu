package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhDongBoTuMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_CHUONG_TRINH)
@RequiredArgsConstructor
public class SyllabusChuongTrinhController {

    private final SyllabusChuongTrinhService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusChuongTrinhResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(required = false) Long syllabusChuongTrinhMauId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(chuongTrinhVersionId, syllabusChuongTrinhMauId, keyword, pageable)
        ));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping("/dong-bo-tu-mau")
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhResponse>> dongBoTuMau(
            @Valid @RequestBody SyllabusChuongTrinhDongBoTuMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.dongBoTuSyllabusMau(
                        request.getSyllabusChuongTrinhMauId(),
                        request.getChuongTrinhVersionId()
                )
        ));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhResponse>> create(@Valid @RequestBody SyllabusChuongTrinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusChuongTrinhResponse>> update(@PathVariable Long id, @Valid @RequestBody SyllabusChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
