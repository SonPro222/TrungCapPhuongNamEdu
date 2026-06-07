package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhVienLopHocPhanRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhVienLopHocPhanResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiSinhVienLopHocPhan;
import org.example.trungcapphuongnam.module.giangDay.service.SinhVienLopHocPhanService;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.SINH_VIEN_LOP_HOC_PHAN)
@RequiredArgsConstructor
public class SinhVienLopHocPhanController {

    private final SinhVienLopHocPhanService sinhVienLopHocPhanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SinhVienLopHocPhanResponse>>> getAll(
            @RequestParam(required = false) Long lopHocPhanId,
            @RequestParam(required = false) Long sinhVienId,
            @RequestParam(required = false) TrangThaiSinhVienLopHocPhan trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                sinhVienLopHocPhanService.getAll(
                        lopHocPhanId,
                        sinhVienId,
                        trangThai,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }

    @GetMapping("/theo-lop-hoc-phan/{lopHocPhanId}")
    public ResponseEntity<ApiResponse<Page<SinhVienLopHocPhanResponse>>> getByLopHocPhan(
            @PathVariable Long lopHocPhanId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                sinhVienLopHocPhanService.getByLopHocPhan(
                        lopHocPhanId,
                        PageRequest.of(
                                Math.max(page, 0),
                                Math.min(Math.max(size, 1), 100),
                                Sort.by(Sort.Direction.DESC, "id")
                        )
                )
        ));
    }

    @GetMapping("/lop-hoc-phan/{lopHocPhanId}/sinh-vien")
    public ResponseEntity<ApiResponse<Page<SinhVienResponse>>> getSinhVienTrongLopHocPhan(
            @PathVariable Long lopHocPhanId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                sinhVienLopHocPhanService.getSinhVienTrongLopHocPhan(
                        lopHocPhanId,
                        PageRequest.of(
                                Math.max(page, 0),
                                Math.min(Math.max(size, 1), 100),
                                Sort.by(Sort.Direction.DESC, "id")
                        )
                )
        ));
    }

    @GetMapping("/lop-hoc-phan/{lopHocPhanId}/sinh-vien-chua-vao-lop")
    public ResponseEntity<ApiResponse<Page<SinhVienResponse>>> getSinhVienChuaVaoLopHocPhan(
            @PathVariable Long lopHocPhanId,
            @RequestParam(required = false) Long chuongTrinhVersionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                sinhVienLopHocPhanService.getSinhVienChuaVaoLopHocPhan(
                        chuongTrinhVersionId,
                        lopHocPhanId,
                        PageRequest.of(
                                Math.max(page, 0),
                                Math.min(Math.max(size, 1), 100),
                                Sort.by(Sort.Direction.ASC, "maSinhVien")
                        )
                )
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<SinhVienLopHocPhanResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(sinhVienLopHocPhanService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SinhVienLopHocPhanResponse>> create(@RequestBody SinhVienLopHocPhanRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(sinhVienLopHocPhanService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<SinhVienLopHocPhanResponse>> update(
            @PathVariable Long id,
            @RequestBody SinhVienLopHocPhanRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(sinhVienLopHocPhanService.update(id, request)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        sinhVienLopHocPhanService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "sinhVienId", "lopHocPhanId", "ngayDangKy", "trangThai", "laHocLai", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}