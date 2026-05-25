package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LopHocPhanChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LopHocPhanChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.giangDay.service.LopHocPhanChuongTrinhMonService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.BASE + "/lop-hoc-phan-chuong-trinh-mon")
@RequiredArgsConstructor
public class LopHocPhanChuongTrinhMonController {

    private final LopHocPhanChuongTrinhMonService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LopHocPhanChuongTrinhMonResponse>>> getAll(
            @RequestParam(required = false) Long lopHocPhanId,
            @RequestParam(required = false) Long chuongTrinhMonId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.getAll(lopHocPhanId, chuongTrinhMonId, taoPageRequest(page, size, sortBy, sortDir))
        ));
    }

    @GetMapping("/theo-lop-hoc-phan/{lopHocPhanId}")
    public ResponseEntity<ApiResponse<Page<LopHocPhanChuongTrinhMonResponse>>> getByLopHocPhan(
            @PathVariable Long lopHocPhanId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                service.getAll(lopHocPhanId, null, PageRequest.of(
                        Math.max(page, 0),
                        Math.min(Math.max(size, 1), 100),
                        Sort.by(Sort.Direction.DESC, "id")
                ))
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LopHocPhanChuongTrinhMonResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LopHocPhanChuongTrinhMonResponse>> create(
            @RequestBody LopHocPhanChuongTrinhMonRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(service.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LopHocPhanChuongTrinhMonResponse>> update(
            @PathVariable Long id,
            @RequestBody LopHocPhanChuongTrinhMonRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "lopHocPhanId", "chuongTrinhMonId", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}