package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiDiemDanh;
import org.example.trungcapphuongnam.module.giangDay.service.DiemDanhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.DIEM_DANH)
@RequiredArgsConstructor
public class DiemDanhController {

    private final DiemDanhService diemDanhService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DiemDanhResponse>>> getAll(
            @RequestParam(required = false) Long lichHocId,
            @RequestParam(required = false) Long lopHocPhanId,
            @RequestParam(required = false) Long sinhVienId,
            @RequestParam(required = false) String keywordSinhVien,
            @RequestParam(required = false) TrangThaiDiemDanh trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                diemDanhService.getAll(
                        lichHocId,
                        lopHocPhanId,
                        sinhVienId,
                        keywordSinhVien,
                        trangThai,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<DiemDanhResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(diemDanhService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DiemDanhResponse>> create(@RequestBody DiemDanhRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(diemDanhService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<DiemDanhResponse>> update(
            @PathVariable Long id,
            @RequestBody DiemDanhRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(diemDanhService.update(id, request)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        diemDanhService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "lichHocId", "sinhVienId", "trangThai", "thoiGianDiemDanh" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}