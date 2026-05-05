package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.service.SinhVienLopHocPhanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.SINH_VIEN_LOP_HOC_PHAN)
@RequiredArgsConstructor
public class SinhVienLopHocPhanController {

    private final SinhVienLopHocPhanService sinhVienLopHocPhanService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SinhVienLopHocPhanResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(sinhVienLopHocPhanService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<SinhVienLopHocPhanResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(sinhVienLopHocPhanService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienLopHocPhanResponse>> create(@RequestBody SinhVienLopHocPhanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(sinhVienLopHocPhanService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<SinhVienLopHocPhanResponse>> update(@PathVariable Long id,
                                                                          @RequestBody SinhVienLopHocPhanRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(sinhVienLopHocPhanService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        sinhVienLopHocPhanService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}