package org.example.trungcapphuongnam.module.heThong.controller;

import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyDangNhapRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyDangNhapResponse;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyDangNhapService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(HeThongPath.NHAT_KY_DANG_NHAP)
@RequiredArgsConstructor
public class NhatKyDangNhapController {

    private final NhatKyDangNhapService nhatKyDangNhapService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NhatKyDangNhapResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(nhatKyDangNhapService.getAll())
        );
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<NhatKyDangNhapResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(nhatKyDangNhapService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhatKyDangNhapResponse>> create(@RequestBody NhatKyDangNhapRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(nhatKyDangNhapService.create(request)));
    }

    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<NhatKyDangNhapResponse>> update(@PathVariable Long id,
                                                             @RequestBody NhatKyDangNhapRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(nhatKyDangNhapService.update(id, request))
        );
    }

    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        nhatKyDangNhapService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
