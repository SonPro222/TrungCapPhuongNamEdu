package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiPhong;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiPhongHoc;
import org.example.trungcapphuongnam.module.giangDay.service.PhongHocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.PHONG_HOC)
@RequiredArgsConstructor
public class PhongHocController {

    private final PhongHocService phongHocService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PhongHocResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LoaiPhong loaiPhong,
            @RequestParam(required = false) TrangThaiPhongHoc trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                phongHocService.getAll(
                        keyword,
                        loaiPhong,
                        trangThai,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
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
    public ResponseEntity<ApiResponse<PhongHocResponse>> update(
            @PathVariable Long id,
            @RequestBody PhongHocRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(phongHocService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        phongHocService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "maPhong", "tenPhong", "loaiPhong", "sucChua", "diaDiem", "trangThai", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}