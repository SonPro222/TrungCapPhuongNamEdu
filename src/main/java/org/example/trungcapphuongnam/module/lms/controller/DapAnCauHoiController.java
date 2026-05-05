package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.service.DapAnCauHoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.DAP_AN_CAU_HOI)
@RequiredArgsConstructor
public class DapAnCauHoiController {

    private final DapAnCauHoiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DapAnCauHoiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<DapAnCauHoiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DapAnCauHoiResponse>> create(@Valid @RequestBody DapAnCauHoiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<DapAnCauHoiResponse>> update(@PathVariable Long id, @Valid @RequestBody DapAnCauHoiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
