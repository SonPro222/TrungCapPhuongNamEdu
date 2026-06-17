package org.example.trungcapphuongnam.module.heThong.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.CapTaiKhoanSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.GuiOtpDoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.KhoaNhieuTaiKhoanSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanSinhVienResponse;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanSinhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/he-thong/tai-khoan-sinh-vien")
@RequiredArgsConstructor
public class TaiKhoanSinhVienController {

    private final TaiKhoanSinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaiKhoanSinhVienResponse>>> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(required = false) String trangThaiTaiKhoan,
            Pageable pageable
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.findAll(
                        keyword,
                        nganhId,
                        chuongTrinhVersionId,
                        trangThaiTaiKhoan,
                        pageable
                )
        ));
    }

    @PostMapping("/{sinhVienId}/cap-tai-khoan")
    public ResponseEntity<ApiResponse<TaiKhoanSinhVienResponse>> capTaiKhoan(
            @PathVariable Long sinhVienId,
            @Valid @RequestBody CapTaiKhoanSinhVienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.capTaiKhoan(sinhVienId, request)));
    }

    @PutMapping("/{sinhVienId}/cap-nhat-gmail")
    public ResponseEntity<ApiResponse<TaiKhoanSinhVienResponse>> capNhatGmail(
            @PathVariable Long sinhVienId,
            @Valid @RequestBody CapTaiKhoanSinhVienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.capNhatGmail(sinhVienId, request)));
    }

    @PutMapping("/{sinhVienId}/dat-lai-mat-khau")
    public ResponseEntity<ApiResponse<TaiKhoanSinhVienResponse>> datLaiMatKhau(
            @PathVariable Long sinhVienId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.datLaiMatKhau(sinhVienId)));
    }

    @PutMapping("/{sinhVienId}/khoa-tai-khoan")
    public ResponseEntity<ApiResponse<TaiKhoanSinhVienResponse>> khoaTaiKhoan(
            @PathVariable Long sinhVienId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.khoaTaiKhoan(sinhVienId)));
    }

    @PutMapping("/{sinhVienId}/mo-khoa-tai-khoan")
    public ResponseEntity<ApiResponse<TaiKhoanSinhVienResponse>> moKhoaTaiKhoan(
            @PathVariable Long sinhVienId
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.moKhoaTaiKhoan(sinhVienId)));
    }

    @PutMapping("/tu-dong-khoa")
    public ResponseEntity<ApiResponse<Integer>> tuDongKhoaTaiKhoanTheoNghiepVu() {
        return ResponseEntity.ok(ApiResponse.ok(service.tuDongKhoaTaiKhoanTheoNghiepVu()));
    }

    @PutMapping("/khoa-nhieu")
    public ResponseEntity<ApiResponse<Integer>> khoaNhieuTaiKhoan(
            @Valid @RequestBody KhoaNhieuTaiKhoanSinhVienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.khoaNhieuTaiKhoan(request.getSinhVienIds())));
    }

    @PutMapping("/mo-khoa-nhieu")
    public ResponseEntity<ApiResponse<Integer>> moKhoaNhieuTaiKhoan(
            @Valid @RequestBody KhoaNhieuTaiKhoanSinhVienRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.moKhoaNhieuTaiKhoan(request.getSinhVienIds())));
    }
    @PostMapping("/doi-mat-khau/gui-otp")
    public ResponseEntity<ApiResponse<Void>> guiOtpDoiMatKhau(
            @RequestBody GuiOtpDoiMatKhauSinhVienRequest request
    ) {
        service.guiOtpDoiMatKhau(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @PostMapping("/doi-mat-khau/xac-nhan")
    public ResponseEntity<ApiResponse<Void>> xacNhanDoiMatKhau(
            @RequestBody DoiMatKhauSinhVienRequest request
    ) {
        service.xacNhanDoiMatKhau(request);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

}