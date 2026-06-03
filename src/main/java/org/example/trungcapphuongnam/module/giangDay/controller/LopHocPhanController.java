package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.LoaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(GiangDayPath.LOP_HOC_PHAN)
@RequiredArgsConstructor
public class LopHocPhanController {

    private final LopHocPhanService lopHocPhanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LopHocPhanResponse>>> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long chuongTrinhMonId,
            @RequestParam(required = false) Long monHocId,
            @RequestParam(required = false) LoaiLopHocPhan loaiLopHocPhan,
            @RequestParam(required = false) TrangThaiLopHocPhan trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                lopHocPhanService.getAll(
                        keyword,
                        chuongTrinhMonId,
                        monHocId,
                        loaiLopHocPhan,
                        trangThai,
                        tuNgay,
                        denNgay,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
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
    public ResponseEntity<ApiResponse<LopHocPhanResponse>> update(
            @PathVariable Long id,
            @RequestBody LopHocPhanRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(lopHocPhanService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        lopHocPhanService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 1000);

        String sortField = switch (sortBy) {
            case "maLop", "tenLop", "loaiLopHocPhan", "soLuongToiDa", "soLuongHienTai",
                 "ngayBatDau", "ngayKetThuc", "trangThai", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}