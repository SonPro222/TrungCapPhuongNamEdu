package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.LOP_HOC_PHAN)
@RequiredArgsConstructor
public class LopHocPhanController {

    private final LopHocPhanService lopHocPhanService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LopHocPhanResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(lopHocPhanService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<LopHocPhanResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(lopHocPhanService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LopHocPhanResponse>> create(@RequestBody LopHocPhanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(lopHocPhanService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<LopHocPhanResponse>> update(@PathVariable Long id,
                                                                  @RequestBody LopHocPhanRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(lopHocPhanService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        lopHocPhanService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}