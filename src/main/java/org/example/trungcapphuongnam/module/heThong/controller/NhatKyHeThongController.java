package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhatKyHeThongRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhatKyHeThongResponse;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.Map;

@RestController
@RequestMapping(HeThongPath.NHAT_KY_HE_THONG)
@RequiredArgsConstructor
public class NhatKyHeThongController {

    private final NhatKyHeThongService nhatKyHeThongService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhatKyHeThongResponse>>> getAll(@RequestParam Map<String, String> filters) {
        return ResponseEntity.ok(
                ApiResponse.ok(nhatKyHeThongService.getAll(filters))
        );
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<NhatKyHeThongResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(nhatKyHeThongService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhatKyHeThongResponse>> create(@RequestBody NhatKyHeThongRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(nhatKyHeThongService.create(request)));
    }

    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<NhatKyHeThongResponse>> update(@PathVariable Long id,
                                                                     @RequestBody NhatKyHeThongRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(nhatKyHeThongService.update(id, request))
        );
    }

    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        nhatKyHeThongService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}