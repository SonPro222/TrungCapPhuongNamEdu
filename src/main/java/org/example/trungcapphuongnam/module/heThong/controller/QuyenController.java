package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.QuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.QuyenResponse;
import org.example.trungcapphuongnam.module.heThong.service.QuyenService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.QUYEN)
@RequiredArgsConstructor
public class QuyenController {
    private final QuyenService quyenService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<QuyenResponse>>> getAll(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(quyenService.search(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<QuyenResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.ok(quyenService.getById(id))); }
    @PostMapping
    public ResponseEntity<ApiResponse<QuyenResponse>> create(@RequestBody QuyenRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(quyenService.create(request))); }
    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<QuyenResponse>> update(@PathVariable Long id, @RequestBody QuyenRequest request) { return ResponseEntity.ok(ApiResponse.ok(quyenService.update(id, request))); }
    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { quyenService.delete(id); return ResponseEntity.ok(ApiResponse.deleted()); }
}
