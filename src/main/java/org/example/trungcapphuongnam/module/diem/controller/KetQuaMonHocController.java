package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaMonHocRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaMonHocResponse;
import org.example.trungcapphuongnam.module.diem.service.KetQuaMonHocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DiemPath.KET_QUA_MON_HOC)
@RequiredArgsConstructor
public class KetQuaMonHocController {

    private final KetQuaMonHocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KetQuaMonHocResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<KetQuaMonHocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KetQuaMonHocResponse>> create(@Valid @RequestBody KetQuaMonHocRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<KetQuaMonHocResponse>> update(@PathVariable Long id, @Valid @RequestBody KetQuaMonHocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
