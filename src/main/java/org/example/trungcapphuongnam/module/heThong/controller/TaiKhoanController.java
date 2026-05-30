package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauTaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanResponse;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.TAI_KHOAN)
@RequiredArgsConstructor
public class TaiKhoanController {

    private final TaiKhoanService taiKhoanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaiKhoanResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiTaiKhoan,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String vaiTro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(ApiResponse.ok(
                taiKhoanService.search(keyword, loaiTaiKhoan, trangThai, vaiTro, pageable)
        ));
    }

    @GetMapping("/page")
    public ResponseEntity<ApiResponse<Page<TaiKhoanResponse>>> getPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String loaiTaiKhoan,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) String vaiTro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(ApiResponse.ok(
                taiKhoanService.search(keyword, loaiTaiKhoan, trangThai, vaiTro, pageable)
        ));
    }

    @GetMapping("/theo-gmail")
    public ResponseEntity<ApiResponse<TaiKhoanResponse>> getByEmail(@RequestParam String email) {
        return ResponseEntity.ok(ApiResponse.ok(taiKhoanService.getByEmail(email)));
    }

    @PutMapping("/doi-mat-khau-theo-gmail")
    public ResponseEntity<ApiResponse<TaiKhoanResponse>> doiMatKhauTheoGmail(
            @RequestParam String email,
            @RequestBody DoiMatKhauTaiKhoanRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(taiKhoanService.doiMatKhauTheoGmail(email, request)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TaiKhoanResponse>> create(@RequestBody TaiKhoanRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created(taiKhoanService.create(request)));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ApiResponse<TaiKhoanResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(taiKhoanService.getById(id)));
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<ApiResponse<TaiKhoanResponse>> update(
            @PathVariable Long id,
            @RequestBody TaiKhoanRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(taiKhoanService.update(id, request)));
    }

    @PutMapping("/{id:\\d+}/doi-mat-khau")
    public ResponseEntity<ApiResponse<TaiKhoanResponse>> doiMatKhauQuanTri(
            @PathVariable Long id,
            @RequestBody DoiMatKhauTaiKhoanRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(taiKhoanService.doiMatKhauQuanTri(id, request)));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        taiKhoanService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}