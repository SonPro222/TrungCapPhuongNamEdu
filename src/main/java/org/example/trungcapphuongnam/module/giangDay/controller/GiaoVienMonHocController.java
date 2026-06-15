package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienMonHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienMonHocResponse;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienMonHocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.BASE + "/giao-vien-mon-hoc")
@RequiredArgsConstructor
public class GiaoVienMonHocController {

    private final GiaoVienMonHocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GiaoVienMonHocResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long giaoVienId,
            @RequestParam(required = false) Long monHocId,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction dir = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return ResponseEntity.ok(ApiResponse.ok(
                service.getAll(keyword, giaoVienId, monHocId, trangThai,
                        PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 200), Sort.by(dir, sortBy)))
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienMonHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoVienMonHocResponse>> create(@RequestBody GiaoVienMonHocRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienMonHocResponse>> update(@PathVariable Long id, @RequestBody GiaoVienMonHocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
