package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomTuChonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.NHOM_TU_CHON)
@RequiredArgsConstructor
public class NhomTuChonController {

    private final NhomTuChonService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhomTuChonResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(required = false) Long nhomTuChonGocId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(chuongTrinhVersionId, nhomTuChonGocId, keyword, pageable)
        ));
    }
    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomTuChonResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhomTuChonResponse>> create(@Valid @RequestBody NhomTuChonRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NhomTuChonResponse>> update(@PathVariable Long id, @Valid @RequestBody NhomTuChonRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
