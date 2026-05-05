package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;
import org.example.trungcapphuongnam.module.giangDay.service.PhongHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.PHONG_HOC)
@RequiredArgsConstructor
public class PhongHocController {

    private final PhongHocService phongHocService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PhongHocResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(phongHocService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<PhongHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(phongHocService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhongHocResponse>> create(@RequestBody PhongHocRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(phongHocService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<PhongHocResponse>> update(@PathVariable Long id,
                                                                @RequestBody PhongHocRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(phongHocService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        phongHocService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}