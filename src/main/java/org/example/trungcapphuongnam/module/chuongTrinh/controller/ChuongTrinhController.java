package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.tongThe.ChuongTrinhTongTheResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhTongTheService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ChuongTrinhPath.CHUONG_TRINH)
@RequiredArgsConstructor
public class ChuongTrinhController {

    private final ChuongTrinhService service;
    private final ChuongTrinhTongTheService tongTheService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ChuongTrinhResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long nganhLoaiChuongTrinhId,
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long trinhDoId,
            @RequestParam(required = false) Long loaiChuongTrinhId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(
                nganhLoaiChuongTrinhId,
                nganhId,
                trinhDoId,
                loaiChuongTrinhId,
                keyword,
                pageable
        )));
    }

    @GetMapping("/{chuongTrinhId}/tong-the")
    public ResponseEntity<ApiResponse<ChuongTrinhTongTheResponse>> findTongTheByChuongTrinhId(
            @PathVariable Long chuongTrinhId,
            @RequestParam(required = false) Long versionId,
            @RequestParam(required = false) Long khungKyId,
            @RequestParam(defaultValue = "false") Boolean includeSyllabusDetail
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                tongTheService.findTongTheByChuongTrinhId(
                        chuongTrinhId,
                        versionId,
                        khungKyId,
                        includeSyllabusDetail
                )
        ));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ChuongTrinhResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ChuongTrinhResponse>> create(@Valid @RequestBody ChuongTrinhRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<ChuongTrinhResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ChuongTrinhRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}