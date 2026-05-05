package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.example.trungcapphuongnam.module.giangDay.service.DiemDanhService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.DIEM_DANH)
@RequiredArgsConstructor
public class DiemDanhController {

    private final DiemDanhService diemDanhService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiemDanhResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(diemDanhService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<DiemDanhResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(diemDanhService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiemDanhResponse>> create(@RequestBody DiemDanhRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(diemDanhService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<DiemDanhResponse>> update(@PathVariable Long id,
                                                                @RequestBody DiemDanhRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(diemDanhService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        diemDanhService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}