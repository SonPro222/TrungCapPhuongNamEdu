package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTuChonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MonTuChonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.MON_TU_CHON)
@RequiredArgsConstructor
public class MonTuChonController {

    private final MonTuChonService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<MonTuChonResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long nhomId,
            @RequestParam(required = false) Long chuongTrinhMonId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(nhomId, chuongTrinhMonId, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<MonTuChonResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MonTuChonResponse>> create(@Valid @RequestBody MonTuChonRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<MonTuChonResponse>> update(@PathVariable Long id, @Valid @RequestBody MonTuChonRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }


    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
