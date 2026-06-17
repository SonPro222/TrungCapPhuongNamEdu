package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.RubricCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.RubricCauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.service.RubricCauHoiVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.RUBRIC_CAU_HOI_VERSION)
@RequiredArgsConstructor
public class RubricCauHoiVersionController {

    private final RubricCauHoiVersionService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<RubricCauHoiVersionResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<RubricCauHoiVersionResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RubricCauHoiVersionResponse>> create(@Valid @RequestBody RubricCauHoiVersionRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<RubricCauHoiVersionResponse>> update(@PathVariable Long id, @Valid @RequestBody RubricCauHoiVersionRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
