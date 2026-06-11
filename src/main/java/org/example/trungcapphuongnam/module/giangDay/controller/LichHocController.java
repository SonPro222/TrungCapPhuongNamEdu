package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiLichHoc;
import org.example.trungcapphuongnam.module.giangDay.service.LichHocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(GiangDayPath.LICH_HOC)
@RequiredArgsConstructor
public class LichHocController {

    private final LichHocService lichHocService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LichHocResponse>>> getAll(
            @RequestParam(required = false) Long lopHocPhanId,
            @RequestParam(required = false) String keywordLop,
            @RequestParam(required = false) String keywordGiaoVien,
            @RequestParam(required = false) String keywordPhong,
            @RequestParam(required = false) String keywordCa,
            @RequestParam(required = false) TrangThaiLichHoc trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "ngayHoc") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                lichHocService.getAll(
                        lopHocPhanId,
                        keywordLop,
                        keywordGiaoVien,
                        keywordPhong,
                        keywordCa,
                        trangThai,
                        tuNgay,
                        denNgay,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }


    @GetMapping("/admin")
    public ResponseEntity<ApiResponse<Page<LichHocResponse>>> getAdmin(
            @RequestParam(required = false) Long lopHocPhanId,
            @RequestParam(required = false) String keywordLop,
            @RequestParam(required = false) String keywordGiaoVien,
            @RequestParam(required = false) String keywordPhong,
            @RequestParam(required = false) String keywordCa,
            @RequestParam(required = false) TrangThaiLichHoc trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "ngayHoc") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return getAll(lopHocPhanId, keywordLop, keywordGiaoVien, keywordPhong, keywordCa, trangThai, tuNgay, denNgay, page, size, sortBy, sortDir);
    }

    @GetMapping("/sinh-vien/me")
    public ResponseEntity<ApiResponse<Page<LichHocResponse>>> getLichHocSinhVienMe(
            @RequestParam(required = false) TrangThaiLichHoc trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "ngayHoc") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                lichHocService.getLichHocSinhVienHienTai(
                        trangThai,
                        tuNgay,
                        denNgay,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }

    @GetMapping("/giang-vien/me")
    public ResponseEntity<ApiResponse<Page<LichHocResponse>>> getLichDayGiangVienMe(
            @RequestParam(required = false) TrangThaiLichHoc trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgay,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgay,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "ngayHoc") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                lichHocService.getLichDayGiangVienHienTai(
                        trangThai,
                        tuNgay,
                        denNgay,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<LichHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(lichHocService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LichHocResponse>> create(@RequestBody LichHocRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(lichHocService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<LichHocResponse>> update(
            @PathVariable Long id,
            @RequestBody LichHocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(lichHocService.update(id, request)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        lichHocService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "lopHocPhanId", "giaoVienId", "phongHocId", "caHocId", "ngayHoc", "trangThai", "createdAt", "updatedAt" -> sortBy;
            default -> "ngayHoc";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}