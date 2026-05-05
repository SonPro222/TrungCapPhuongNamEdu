package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.TrinhDoDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.service.TrinhDoDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.TRINH_DO_DAO_TAO)
@RequiredArgsConstructor
public class TrinhDoDaoTaoController {

    private final TrinhDoDaoTaoService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TrinhDoDaoTaoResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<TrinhDoDaoTaoResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TrinhDoDaoTaoResponse>> create(@Valid @RequestBody TrinhDoDaoTaoRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<TrinhDoDaoTaoResponse>> update(@PathVariable Long id, @Valid @RequestBody TrinhDoDaoTaoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
