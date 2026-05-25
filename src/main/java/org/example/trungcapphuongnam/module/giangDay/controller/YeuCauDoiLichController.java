package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiYeuCauDoiLich;
import org.example.trungcapphuongnam.module.giangDay.service.YeuCauDoiLichService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping(GiangDayPath.YEU_CAU_DOI_LICH)
@RequiredArgsConstructor
public class YeuCauDoiLichController {

    private final YeuCauDoiLichService yeuCauDoiLichService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<YeuCauDoiLichResponse>>> getAll(
            @RequestParam(required = false) Long lichHocCuId,
            @RequestParam(required = false) Long giaoVienId,
            @RequestParam(required = false) Long nguoiDuyetId,
            @RequestParam(required = false) TrangThaiYeuCauDoiLich trangThai,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate tuNgayMoi,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate denNgayMoi,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                yeuCauDoiLichService.getAll(
                        lichHocCuId,
                        giaoVienId,
                        nguoiDuyetId,
                        trangThai,
                        tuNgayMoi,
                        denNgayMoi,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<YeuCauDoiLichResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(yeuCauDoiLichService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<YeuCauDoiLichResponse>> create(@RequestBody YeuCauDoiLichRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(yeuCauDoiLichService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<YeuCauDoiLichResponse>> update(
            @PathVariable Long id,
            @RequestBody YeuCauDoiLichRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(yeuCauDoiLichService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        yeuCauDoiLichService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "lichHocCuId", "giaoVienId", "ngayMoi", "caHocMoiId", "phongHocMoiId",
                 "trangThai", "nguoiDuyetId", "ngayDuyet", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}