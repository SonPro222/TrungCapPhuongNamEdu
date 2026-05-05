package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuDayDiemLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuDayDiemLmsResponse;
import org.example.trungcapphuongnam.module.lms.service.LichSuDayDiemLmsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.LICH_SU_DAY_DIEM_LMS)
@RequiredArgsConstructor
public class LichSuDayDiemLmsController {

    private final LichSuDayDiemLmsService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LichSuDayDiemLmsResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LichSuDayDiemLmsResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LichSuDayDiemLmsResponse>> create(@Valid @RequestBody LichSuDayDiemLmsRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LichSuDayDiemLmsResponse>> update(@PathVariable Long id, @Valid @RequestBody LichSuDayDiemLmsRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
