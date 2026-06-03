package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiApDungChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiApDungChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.lms.service.CauHoiApDungChuongTrinhMonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.CAU_HOI_AP_DUNG)
@RequiredArgsConstructor
public class CauHoiApDungChuongTrinhMonController {

    private final CauHoiApDungChuongTrinhMonService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauHoiApDungChuongTrinhMonResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauHoiApDungChuongTrinhMonResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauHoiApDungChuongTrinhMonResponse>> create(@Valid @RequestBody CauHoiApDungChuongTrinhMonRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauHoiApDungChuongTrinhMonResponse>> update(@PathVariable Long id, @Valid @RequestBody CauHoiApDungChuongTrinhMonRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
