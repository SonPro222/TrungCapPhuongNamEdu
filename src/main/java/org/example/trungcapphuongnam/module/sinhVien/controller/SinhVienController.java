package org.example.trungcapphuongnam.module.sinhVien.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.SinhVienPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.TiepNhanSinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.TiepNhanSinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(SinhVienPath.SINH_VIEN)
@RequiredArgsConstructor
public class SinhVienController {

    private final SinhVienService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SinhVienResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping("/loc/theo-nganh-chuong-trinh-version")
    public ResponseEntity<ApiResponse<Page<SinhVienResponse>>> findTheoNganhChuongTrinhVersion(
            @RequestParam(required = false) Long nganhId,
            @RequestParam(required = false) Long chuongTrinhId,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(
                        service.findTheoNganhChuongTrinhVersion(
                                nganhId,
                                chuongTrinhId,
                                chuongTrinhVersionId,
                                pageable
                        )
                )
        );
    }

    @GetMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienResponse>> create(@Valid @RequestBody SinhVienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PostMapping("/tiep-nhan")
    public ResponseEntity<ApiResponse<TiepNhanSinhVienResponse>> tiepNhan(@Valid @RequestBody TiepNhanSinhVienRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.tiepNhan(request)));
    }

    @PostMapping(value = "/tiep-nhan-co-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TiepNhanSinhVienResponse>> tiepNhanCoFile(
            @Valid @RequestPart("data") TiepNhanSinhVienRequest request,
            @RequestPart(value = "anhChanDung", required = false) MultipartFile anhChanDung,
            @RequestPart(value = "cccdTruoc", required = false) MultipartFile cccdTruoc,
            @RequestPart(value = "cccdSau", required = false) MultipartFile cccdSau,
            @RequestPart(value = "bangCap", required = false) MultipartFile bangCap,
            @RequestPart(value = "giayToKhac", required = false) List<MultipartFile> giayToKhac
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                service.tiepNhanCoFile(request, anhChanDung, cccdTruoc, cccdSau, bangCap, giayToKhac)
        ));
    }

    @PutMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<SinhVienResponse>> update(@PathVariable Long id, @Valid @RequestBody SinhVienRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(SinhVienPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
