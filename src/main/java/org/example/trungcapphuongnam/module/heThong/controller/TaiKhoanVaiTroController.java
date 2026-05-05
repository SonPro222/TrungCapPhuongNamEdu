package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanVaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanVaiTroResponse;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanVaiTroService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.TAI_KHOAN_VAI_TRO)
@RequiredArgsConstructor
public class TaiKhoanVaiTroController {
    private final TaiKhoanVaiTroService taiKhoanVaiTroService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaiKhoanVaiTroResponse>>> getAll(@RequestParam(required = false) Long taiKhoanId, @RequestParam(required = false) Long vaiTroId, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(taiKhoanVaiTroService.search(taiKhoanId, vaiTroId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<TaiKhoanVaiTroResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.ok(taiKhoanVaiTroService.getById(id))); }
    @PostMapping
    public ResponseEntity<ApiResponse<TaiKhoanVaiTroResponse>> create(@RequestBody TaiKhoanVaiTroRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(taiKhoanVaiTroService.create(request))); }
    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<TaiKhoanVaiTroResponse>> update(@PathVariable Long id, @RequestBody TaiKhoanVaiTroRequest request) { return ResponseEntity.ok(ApiResponse.ok(taiKhoanVaiTroService.update(id, request))); }
    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { taiKhoanVaiTroService.delete(id); return ResponseEntity.ok(ApiResponse.deleted()); }
}
