package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NangLucDauRaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;

@RestController
@RequestMapping(ChuongTrinhPath.NANG_LUC_DAU_RA)
@RequiredArgsConstructor
public class NangLucDauRaController {

    private final NangLucDauRaService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NangLucDauRaResponse>>> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping("/theo-version/{chuongTrinhVersionId}")
    public ResponseEntity<ApiResponse<Page<NangLucDauRaResponse>>> findAllByChuongTrinhVersionId(
            @PathVariable Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAllByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
        ));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NangLucDauRaResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NangLucDauRaResponse>> create(@Valid @RequestBody NangLucDauRaRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<NangLucDauRaResponse>> update(@PathVariable Long id, @Valid @RequestBody NangLucDauRaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }


    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
