package org.example.trungcapphuongnam.module.hocPhi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HocPhiPath;

import org.example.trungcapphuongnam.common.enums.TrangThaiHocPhi;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.HocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.HocPhiResponse;
import org.example.trungcapphuongnam.module.hocPhi.service.HocPhiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(HocPhiPath.HOC_PHI)
@RequiredArgsConstructor
public class HocPhiController {

    private final HocPhiService hocPhiService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<HocPhiResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(hocPhiService.getAll())
        );
    }

    @GetMapping(HocPhiPath.ID)
    public ResponseEntity<ApiResponse<HocPhiResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(hocPhiService.getById(id))
        );
    }

    @GetMapping("/sinh-vien-chuong-trinh/{sinhVienChuongTrinhId}")
    public ResponseEntity<ApiResponse<List<HocPhiResponse>>> getBySinhVienChuongTrinhId(@PathVariable Long sinhVienChuongTrinhId) {
        return ResponseEntity.ok(
                ApiResponse.ok(hocPhiService.getBySinhVienChuongTrinhId(sinhVienChuongTrinhId))
        );
    }

    @GetMapping("/trang-thai/{trangThai}")
    public ResponseEntity<ApiResponse<List<HocPhiResponse>>> getByTrangThai(@PathVariable TrangThaiHocPhi trangThai) {
        return ResponseEntity.ok(
                ApiResponse.ok(hocPhiService.getByTrangThai(trangThai))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<HocPhiResponse>> create(@Valid @RequestBody HocPhiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(hocPhiService.create(request)));
    }

    @PutMapping(HocPhiPath.ID)
    public ResponseEntity<ApiResponse<HocPhiResponse>> update(@PathVariable Long id,
                                                              @Valid @RequestBody HocPhiRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(hocPhiService.update(id, request))
        );
    }

    @DeleteMapping(HocPhiPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        hocPhiService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
