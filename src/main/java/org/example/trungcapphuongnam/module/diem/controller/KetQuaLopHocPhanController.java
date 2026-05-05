package org.example.trungcapphuongnam.module.diem.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DiemPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.diem.dto.request.KetQuaLopHocPhanRequest;
import org.example.trungcapphuongnam.module.diem.dto.response.KetQuaLopHocPhanResponse;
import org.example.trungcapphuongnam.module.diem.service.KetQuaLopHocPhanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DiemPath.KET_QUA_LOP_HOC_PHAN)
@RequiredArgsConstructor
public class KetQuaLopHocPhanController {

    private final KetQuaLopHocPhanService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KetQuaLopHocPhanResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<KetQuaLopHocPhanResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KetQuaLopHocPhanResponse>> create(@Valid @RequestBody KetQuaLopHocPhanRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<KetQuaLopHocPhanResponse>> update(@PathVariable Long id, @Valid @RequestBody KetQuaLopHocPhanRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DiemPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
