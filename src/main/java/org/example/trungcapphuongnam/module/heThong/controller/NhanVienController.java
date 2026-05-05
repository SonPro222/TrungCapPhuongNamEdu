package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhanVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhanVienResponse;
import org.example.trungcapphuongnam.module.heThong.service.NhanVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.NHAN_VIEN)
@RequiredArgsConstructor
public class NhanVienController {
    private final NhanVienService nhanVienService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhanVienResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String phongBan,
            @RequestParam(required = false) String chucVu,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Integer namBatDau,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(ApiResponse.ok(nhanVienService.search(keyword, phongBan, chucVu, trangThai, namBatDau, pageable)));
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<NhanVienResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(nhanVienService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhanVienResponse>> create(@RequestBody NhanVienRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(nhanVienService.create(request)));
    }

    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<NhanVienResponse>> update(@PathVariable Long id, @RequestBody NhanVienRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(nhanVienService.update(id, request)));
    }

    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        nhanVienService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
