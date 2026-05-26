package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhTrinhDoDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.service.NganhTrinhDoDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.NGANH_TRINH_DO_DAO_TAO)
@RequiredArgsConstructor
public class NganhTrinhDoDaoTaoController {

    private final NganhTrinhDoDaoTaoService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NganhTrinhDoDaoTaoResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long trinhDoId,
            @RequestParam(required = false) String trangThai
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(nganhId, trinhDoId, trangThai, pageable)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhTrinhDoDaoTaoResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NganhTrinhDoDaoTaoResponse>> create(@Valid @RequestBody NganhTrinhDoDaoTaoRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PostMapping("/luu")
    public ResponseEntity<ApiResponse<NganhTrinhDoDaoTaoResponse>> luu(@Valid @RequestBody NganhTrinhDoDaoTaoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.luu(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhTrinhDoDaoTaoResponse>> update(@PathVariable Long id, @Valid @RequestBody NganhTrinhDoDaoTaoRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
