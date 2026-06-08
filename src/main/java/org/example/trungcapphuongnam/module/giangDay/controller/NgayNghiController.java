package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.entity.NgayNghi;
import org.example.trungcapphuongnam.module.giangDay.repository.NgayNghiRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.BASE + "/ngay-nghi")
@RequiredArgsConstructor
public class NgayNghiController {

    private final NgayNghiRepository ngayNghiRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NgayNghi>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "ngay") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                ngayNghiRepository.findAll(taoPageRequest(page, size, sortBy, sortDir))
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<NgayNghi>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(timTheoId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NgayNghi>> create(@RequestBody NgayNghi request) {
        kiemTraHopLe(request, null);
        request.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(ngayNghiRepository.save(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<NgayNghi>> update(
            @PathVariable Long id,
            @RequestBody NgayNghi request
    ) {
        NgayNghi entity = timTheoId(id);
        kiemTraHopLe(request, id);

        entity.setNgay(request.getNgay());
        entity.setTenNgayNghi(request.getTenNgayNghi());
        entity.setGhiChu(request.getGhiChu());

        return ResponseEntity.ok(ApiResponse.ok(ngayNghiRepository.save(entity)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        NgayNghi entity = timTheoId(id);
        ngayNghiRepository.delete(entity);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private NgayNghi timTheoId(Long id) {
        return ngayNghiRepository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Không tìm thấy ngày nghỉ"));
    }

    private void kiemTraHopLe(NgayNghi request, Long idDangCapNhat) {
        if (request.getNgay() == null) {
            throw new IllegalArgumentException("Phải chọn ngày nghỉ");
        }

        boolean trungNgay = ngayNghiRepository.findAll().stream()
                .anyMatch(item -> item.getNgay().equals(request.getNgay())
                        && (idDangCapNhat == null || !item.getId().equals(idDangCapNhat)));

        if (trungNgay) {
            throw new IllegalArgumentException("Ngày nghỉ này đã tồn tại");
        }
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 1000);

        String sortField = switch (sortBy) {
            case "ngay", "tenNgayNghi", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}
