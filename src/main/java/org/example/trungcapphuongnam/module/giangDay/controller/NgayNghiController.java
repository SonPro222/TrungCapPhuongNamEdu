package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
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
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return ResponseEntity.ok(ApiResponse.ok(
                ngayNghiRepository.findAll(PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 500), Sort.by(direction, sortBy)))
        ));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<NgayNghi>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NgayNghi>> create(@RequestBody NgayNghi request) {
        request.setId(null);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(ngayNghiRepository.save(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<NgayNghi>> update(@PathVariable Long id, @RequestBody NgayNghi request) {
        NgayNghi entity = findById(id);
        entity.setNgay(request.getNgay());
        entity.setTenNgayNghi(request.getTenNgayNghi());
        entity.setGhiChu(request.getGhiChu());
        return ResponseEntity.ok(ApiResponse.ok(ngayNghiRepository.save(entity)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        ngayNghiRepository.delete(findById(id));
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    private NgayNghi findById(Long id) {
        return ngayNghiRepository.findById(id)
                .orElseThrow(() -> new GiangDayException("Ngày nghỉ không tồn tại"));
    }
}
