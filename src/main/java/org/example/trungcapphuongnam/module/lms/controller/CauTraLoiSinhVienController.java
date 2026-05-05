package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiSinhVienRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiSinhVienResponse;
import org.example.trungcapphuongnam.module.lms.service.CauTraLoiSinhVienService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.CAU_TRA_LOI_SINH_VIEN)
@RequiredArgsConstructor
public class CauTraLoiSinhVienController {

    private final CauTraLoiSinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauTraLoiSinhVienResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauTraLoiSinhVienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauTraLoiSinhVienResponse>> create(@Valid @RequestBody CauTraLoiSinhVienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauTraLoiSinhVienResponse>> update(@PathVariable Long id, @Valid @RequestBody CauTraLoiSinhVienRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
