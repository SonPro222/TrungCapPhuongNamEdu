package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ViTriViecLamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.VI_TRI_VIEC_LAM)
@RequiredArgsConstructor
public class ViTriViecLamController {

    private final ViTriViecLamService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ViTriViecLamResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusChuongTrinhId,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusChuongTrinhId, chuongTrinhVersionId, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ViTriViecLamResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ViTriViecLamResponse>> create(@Valid @RequestBody ViTriViecLamRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ViTriViecLamResponse>> update(@PathVariable Long id, @Valid @RequestBody ViTriViecLamRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
