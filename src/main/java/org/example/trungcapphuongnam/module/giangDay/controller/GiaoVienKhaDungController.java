package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienKhaDung;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienKhaDungRepository;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.BASE + "/giao-vien-kha-dung")
@RequiredArgsConstructor
public class GiaoVienKhaDungController {

    private final GiaoVienKhaDungRepository giaoVienKhaDungRepository;
    private final GiaoVienRepository giaoVienRepository;
    private final CaHocRepository caHocRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GiaoVienKhaDung>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                giaoVienKhaDungRepository.findAll(taoPageRequest(page, size, sortBy, sortDir))
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienKhaDung>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(timTheoId(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoVienKhaDung>> create(@RequestBody GiaoVienKhaDung request) {
        kiemTraHopLe(request);
        request.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(giaoVienKhaDungRepository.save(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienKhaDung>> update(
            @PathVariable Long id,
            @RequestBody GiaoVienKhaDung request
    ) {
        GiaoVienKhaDung entity = timTheoId(id);
        kiemTraHopLe(request);

        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setThuTrongTuan(request.getThuTrongTuan());
        entity.setCaHocId(request.getCaHocId());
        entity.setTuNgay(request.getTuNgay());
        entity.setDenNgay(request.getDenNgay());
        entity.setKhaDung(request.getKhaDung());
        entity.setGhiChu(request.getGhiChu());

        return ResponseEntity.ok(ApiResponse.ok(giaoVienKhaDungRepository.save(entity)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        GiaoVienKhaDung entity = timTheoId(id);
        giaoVienKhaDungRepository.delete(entity);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private GiaoVienKhaDung timTheoId(Long id) {
        return giaoVienKhaDungRepository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Không tìm thấy ràng buộc giáo viên khả dụng"));
    }

    private void kiemTraHopLe(GiaoVienKhaDung request) {
        if (request.getGiaoVienId() == null) {
            throw new IllegalArgumentException("Phải chọn giáo viên");
        }

        if (!giaoVienRepository.existsById(request.getGiaoVienId())) {
            throw new IllegalArgumentException("Giáo viên không tồn tại");
        }

        if (request.getCaHocId() != null && !caHocRepository.existsById(request.getCaHocId())) {
            throw new IllegalArgumentException("Ca học không tồn tại");
        }

        Integer thuTrongTuan = request.getThuTrongTuan();
        if (thuTrongTuan != null && (thuTrongTuan < 1 || thuTrongTuan > 7)) {
            throw new IllegalArgumentException("Thứ trong tuần phải từ 1 đến 7");
        }

        if (request.getTuNgay() != null
                && request.getDenNgay() != null
                && request.getTuNgay().isAfter(request.getDenNgay())) {
            throw new IllegalArgumentException("Từ ngày không được sau đến ngày");
        }

        if (request.getKhaDung() == null) {
            request.setKhaDung(Boolean.FALSE);
        }
    }

    private PageRequest taoPageRequest(int page, int size, String sortBy, String sortDir) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 1000);

        String sortField = switch (sortBy) {
            case "giaoVienId", "thuTrongTuan", "caHocId", "tuNgay", "denNgay", "khaDung", "createdAt", "updatedAt" -> sortBy;
            default -> "id";
        };

        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        return PageRequest.of(pageSafe, sizeSafe, Sort.by(direction, sortField));
    }
}
