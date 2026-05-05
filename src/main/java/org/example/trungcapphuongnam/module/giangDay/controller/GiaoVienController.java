package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.GIAO_VIEN)
@RequiredArgsConstructor
public class GiaoVienController {

    private final GiaoVienService giaoVienService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GiaoVienResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoVienService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoVienService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoVienResponse>> create(@RequestBody GiaoVienRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(giaoVienService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienResponse>> update(@PathVariable Long id,
                                                                @RequestBody GiaoVienRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoVienService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        giaoVienService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}