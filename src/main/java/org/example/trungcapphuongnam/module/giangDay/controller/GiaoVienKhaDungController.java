package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVienKhaDung;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienKhaDungRepository;
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

    @GetMapping
    public ResponseEntity<ApiResponse<Page<GiaoVienKhaDung>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return ResponseEntity.ok(ApiResponse.ok(
                giaoVienKhaDungRepository.findAll(PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 500), Sort.by(direction, sortBy)))
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienKhaDung>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoVienKhaDung>> create(@RequestBody GiaoVienKhaDung request) {
        request.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(giaoVienKhaDungRepository.save(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<GiaoVienKhaDung>> update(@PathVariable Long id, @RequestBody GiaoVienKhaDung request) {
        GiaoVienKhaDung entity = findById(id);
        entity.setGiaoVienId(request.getGiaoVienId());
        entity.setThuTrongTuan(request.getThuTrongTuan());
        entity.setCaHocId(request.getCaHocId());
        entity.setTuNgay(request.getTuNgay());
        entity.setDenNgay(request.getDenNgay());
        entity.setKhaDung(request.getKhaDung());
        entity.setKhungKyId(request.getKhungKyId());
        entity.setLoaiDangKy(request.getLoaiDangKy());
        entity.setMucDoUuTien(request.getMucDoUuTien());
        entity.setGhiChu(request.getGhiChu());
        return ResponseEntity.ok(ApiResponse.ok(giaoVienKhaDungRepository.save(entity)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        giaoVienKhaDungRepository.delete(findById(id));
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private GiaoVienKhaDung findById(Long id) {
        return giaoVienKhaDungRepository.findById(id)
                .orElseThrow(() -> new GiangDayException("Cấu hình giáo viên khả dụng không tồn tại"));
    }
}
