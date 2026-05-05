package org.example.trungcapphuongnam.module.hocPhi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HocPhiPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.hocPhi.dto.request.GiaoDichHocPhiRequest;
import org.example.trungcapphuongnam.module.hocPhi.dto.response.GiaoDichHocPhiResponse;
import org.example.trungcapphuongnam.module.hocPhi.service.GiaoDichHocPhiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(HocPhiPath.GIAO_DICH_HOC_PHI)
@RequiredArgsConstructor
public class GiaoDichHocPhiController {

    private final GiaoDichHocPhiService giaoDichHocPhiService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<GiaoDichHocPhiResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoDichHocPhiService.getAll())
        );
    }

    @GetMapping(HocPhiPath.ID)
    public ResponseEntity<ApiResponse<GiaoDichHocPhiResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoDichHocPhiService.getById(id))
        );
    }

    @GetMapping(HocPhiPath.HOC_PHI+HocPhiPath.ID)
    public ResponseEntity<ApiResponse<List<GiaoDichHocPhiResponse>>> getByHocPhiId(@PathVariable Long hocPhiId) {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoDichHocPhiService.getByHocPhiId(hocPhiId))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<GiaoDichHocPhiResponse>> create(@Valid @RequestBody GiaoDichHocPhiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(giaoDichHocPhiService.create(request)));
    }

    @PutMapping(HocPhiPath.ID)
    public ResponseEntity<ApiResponse<GiaoDichHocPhiResponse>> update(@PathVariable Long id,
                                                                      @Valid @RequestBody GiaoDichHocPhiRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(giaoDichHocPhiService.update(id, request))
        );
    }

    @DeleteMapping(HocPhiPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        giaoDichHocPhiService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
