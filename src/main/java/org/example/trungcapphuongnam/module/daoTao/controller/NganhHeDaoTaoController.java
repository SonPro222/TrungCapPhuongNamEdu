package org.example.trungcapphuongnam.module.daoTao.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.DaoTaoPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhHeDaoTaoResponse;
import org.example.trungcapphuongnam.module.daoTao.service.NganhHeDaoTaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(DaoTaoPath.NGANH_HE_DAO_TAO)
@RequiredArgsConstructor
public class NganhHeDaoTaoController {

    private final NganhHeDaoTaoService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NganhHeDaoTaoResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long trinhDoId,
            @RequestParam(required = false) Long loaiChuongTrinhId,
            @RequestParam(required = false) String trangThai
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(nganhId, trinhDoId, loaiChuongTrinhId, trangThai, pageable)
        ));
    }

    @GetMapping("/options")
    public ResponseEntity<ApiResponse<List<NganhHeDaoTaoResponse>>> findOptions(
            @RequestParam(required = false) Long nganhId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findOptions(nganhId)));
    }

    @GetMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhHeDaoTaoResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NganhHeDaoTaoResponse>> create(@Valid @RequestBody NganhHeDaoTaoRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<NganhHeDaoTaoResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody NganhHeDaoTaoRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(DaoTaoPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
