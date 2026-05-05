package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.KhoaDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.service.KhoaDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.KHOA_DAO_TAO)
@RequiredArgsConstructor
public class KhoaDaoTaoController {

    private final KhoaDaoTaoService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<KhoaDaoTaoResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<KhoaDaoTaoResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<KhoaDaoTaoResponse>> create(@Valid @RequestBody KhoaDaoTaoRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<KhoaDaoTaoResponse>> update(@PathVariable Long id, @Valid @RequestBody KhoaDaoTaoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
