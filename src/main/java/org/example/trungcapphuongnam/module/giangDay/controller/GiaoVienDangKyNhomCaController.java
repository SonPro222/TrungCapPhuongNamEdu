package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienDangKyNhomCaRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienDangKyNhomCaResponse;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienDangKyNhomCaService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.BASE + "/giao-vien-dang-ky-nhom-ca")
@RequiredArgsConstructor
public class GiaoVienDangKyNhomCaController {

    private final GiaoVienDangKyNhomCaService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GiaoVienDangKyNhomCaResponse>>> getAll(
            @RequestParam(required = false) Long giaoVienId,
            @RequestParam(required = false) Long khungKyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return ResponseEntity.ok(ApiResponse.ok(
                service.getAll(giaoVienId, khungKyId,
                        PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 500), Sort.by(direction, sortBy)))
        ));
    }

    @GetMapping("/by-giao-vien/{giaoVienId}")
    public ResponseEntity<ApiResponse<List<GiaoVienDangKyNhomCaResponse>>> getByGiaoVien(@PathVariable Long giaoVienId) {
        return ResponseEntity.ok(ApiResponse.ok(service.getByGiaoVien(giaoVienId)));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienDangKyNhomCaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoVienDangKyNhomCaResponse>> create(
            @RequestBody GiaoVienDangKyNhomCaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienDangKyNhomCaResponse>> update(
            @PathVariable Long id,
            @RequestBody GiaoVienDangKyNhomCaRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
