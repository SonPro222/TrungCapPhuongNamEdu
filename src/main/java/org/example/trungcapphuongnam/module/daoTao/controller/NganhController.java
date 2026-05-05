package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhResponse;
import org.example.trungcapphuongnam.module.daoTao.service.NganhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.NGANH)
@RequiredArgsConstructor
public class NganhController {

    private final NganhService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NganhResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NganhResponse>> create(@Valid @RequestBody NganhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhResponse>> update(@PathVariable Long id, @Valid @RequestBody NganhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
