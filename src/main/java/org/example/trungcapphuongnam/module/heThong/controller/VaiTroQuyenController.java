package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroQuyenRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroQuyenResponse;
import org.example.trungcapphuongnam.module.heThong.service.VaiTroQuyenService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.VAI_TRO_QUYEN)
@RequiredArgsConstructor
public class VaiTroQuyenController {
    private final VaiTroQuyenService vaiTroQuyenService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VaiTroQuyenResponse>>> getAll(@RequestParam(required = false) Long vaiTroId, @RequestParam(required = false) Long quyenId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(vaiTroQuyenService.search(vaiTroId, quyenId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<VaiTroQuyenResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.ok(vaiTroQuyenService.getById(id))); }
    @PostMapping
    public ResponseEntity<ApiResponse<VaiTroQuyenResponse>> create(@RequestBody VaiTroQuyenRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(vaiTroQuyenService.create(request))); }
    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<VaiTroQuyenResponse>> update(@PathVariable Long id, @RequestBody VaiTroQuyenRequest request) { return ResponseEntity.ok(ApiResponse.ok(vaiTroQuyenService.update(id, request))); }
    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { vaiTroQuyenService.delete(id); return ResponseEntity.ok(ApiResponse.deleted()); }
}
