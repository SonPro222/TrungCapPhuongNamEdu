package org.example.trungcapphuongnam.module.giangDay.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.daoTao.dto.DotHocRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.DotHocResponse;
import org.example.trungcapphuongnam.module.daoTao.service.DotHocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(GiangDayPath.BASE + "/dot-hoc")
@RequiredArgsConstructor
public class DotHocGiangDayController {

    private final DotHocService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DotHocResponse>>> findAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long khoaDaoTaoId,
            @RequestParam(required = false) Long lopHanhChinhId,
            @RequestParam(required = false) String trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            @RequestParam(defaultValue = "tuNgay") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir
    ) {
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(
                keyword,
                khoaDaoTaoId,
                lopHanhChinhId,
                trangThai,
                PageRequest.of(Math.max(page, 0), Math.min(Math.max(size, 1), 1000), Sort.by(direction, sortBy))
        )));
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<DotHocResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DotHocResponse>> create(@Valid @RequestBody DotHocRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<DotHocResponse>> update(@PathVariable Long id, @Valid @RequestBody DotHocRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
