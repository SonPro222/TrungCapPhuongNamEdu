package org.example.trungcapphuongnam.module.chuongTrinh.controller;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocTaiLieuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_TAI_LIEU)
@RequiredArgsConstructor
public class SyllabusMonHocTaiLieuController {

    private final SyllabusMonHocTaiLieuService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocTaiLieuResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long syllabusMonId,
            @RequestParam(required = false) Long taiLieuGocId,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(syllabusMonId, taiLieuGocId, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocTaiLieuResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocTaiLieuResponse>> create(@RequestBody SyllabusMonHocTaiLieuRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocTaiLieuResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocTaiLieuRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}