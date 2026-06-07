package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.enums.VaiTroGiangDay;
import org.example.trungcapphuongnam.module.giangDay.service.PhanCongGiangDayService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.PHAN_CONG_GIANG_DAY)
@RequiredArgsConstructor
public class PhanCongGiangDayController {

    private final PhanCongGiangDayService phanCongGiangDayService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PhanCongGiangDayResponse>>> getAll(
            @RequestParam(required = false) String keywordLop,
            @RequestParam(required = false) String keywordGiaoVien,
            @RequestParam(required = false) VaiTroGiangDay vaiTro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                phanCongGiangDayService.getAll(
                        keywordLop,
                        keywordGiaoVien,
                        vaiTro,
                        taoPageRequest(page, size, sortBy, sortDir)
                )
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<PhanCongGiangDayResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(phanCongGiangDayService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhanCongGiangDayResponse>> create(@RequestBody PhanCongGiangDayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(phanCongGiangDayService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<PhanCongGiangDayResponse>> update(
            @PathVariable Long id,
            @RequestBody PhanCongGiangDayRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(phanCongGiangDayService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        phanCongGiangDayService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);

        String sortField = switch (sortBy) {
            case "lopHocPhanId", "giaoVienId", "vaiTro", "createdAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}