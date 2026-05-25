package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyDangNhapRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyDangNhapResponse;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyDangNhapService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.NHAT_KY_DANG_NHAP)
@RequiredArgsConstructor
public class NhatKyDangNhapController {

    private final NhatKyDangNhapService nhatKyDangNhapService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhatKyDangNhapResponse>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        nhatKyDangNhapService.getAll(
                                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
                        )
                )
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
    public ResponseEntity<ApiResponse<NhatKyDangNhapResponse>> update(
            @PathVariable Long id,
            @RequestBody NhatKyDangNhapRequest request
    ) {
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