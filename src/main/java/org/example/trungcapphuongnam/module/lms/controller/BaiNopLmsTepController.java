package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsTepRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsTepResponse;
import org.example.trungcapphuongnam.module.lms.service.BaiNopLmsTepService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.BAI_NOP_LMS_TEP)
@RequiredArgsConstructor
public class BaiNopLmsTepController {

    private final BaiNopLmsTepService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BaiNopLmsTepResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<BaiNopLmsTepResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaiNopLmsTepResponse>> create(@Valid @RequestBody BaiNopLmsTepRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<BaiNopLmsTepResponse>> update(@PathVariable Long id, @Valid @RequestBody BaiNopLmsTepRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
