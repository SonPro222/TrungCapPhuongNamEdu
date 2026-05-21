package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.LopHanhChinhResponse;
import org.example.trungcapphuongnam.module.daoTao.service.LopHanhChinhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(DaoTaoPath.LOP_HANH_CHINH)
@RequiredArgsConstructor
public class LopHanhChinhController {

    private final LopHanhChinhService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LopHanhChinhResponse>>> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping("/theo-version/{chuongTrinhVersionId}")
    public ResponseEntity<ApiResponse<Page<LopHanhChinhResponse>>> findAllByChuongTrinhVersionId(
            @PathVariable Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAllByChuongTrinhVersionId(chuongTrinhVersionId, pageable)
        ));
    }

    @GetMapping("/theo-khoa/{khoaDaoTaoId}")
    public ResponseEntity<ApiResponse<Page<LopHanhChinhResponse>>> findAllByKhoaDaoTaoId(
            @PathVariable Long khoaDaoTaoId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAllByKhoaDaoTaoId(khoaDaoTaoId, pageable)
        ));
    }

    @GetMapping("/theo-version/{chuongTrinhVersionId}/theo-khoa/{khoaDaoTaoId}")
    public ResponseEntity<ApiResponse<Page<LopHanhChinhResponse>>> findAllByChuongTrinhVersionIdAndKhoaDaoTaoId(
            @PathVariable Long chuongTrinhVersionId,
            @PathVariable Long khoaDaoTaoId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAllByChuongTrinhVersionIdAndKhoaDaoTaoId(
                        chuongTrinhVersionId,
                        khoaDaoTaoId,
                        pageable
                )
        ));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<LopHanhChinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LopHanhChinhResponse>> create(@Valid @RequestBody LopHanhChinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<LopHanhChinhResponse>> update(@PathVariable Long id, @Valid @RequestBody LopHanhChinhRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
