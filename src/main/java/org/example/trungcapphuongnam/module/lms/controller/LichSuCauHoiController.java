package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.service.LichSuCauHoiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.LICH_SU_CAU_HOI)
@RequiredArgsConstructor
public class LichSuCauHoiController {

    private final LichSuCauHoiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LichSuCauHoiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LichSuCauHoiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LichSuCauHoiResponse>> create(@Valid @RequestBody LichSuCauHoiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LichSuCauHoiResponse>> update(@PathVariable Long id, @Valid @RequestBody LichSuCauHoiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
