package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.service.LichHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.LICH_HOC)
@RequiredArgsConstructor
public class LichHocController {

    private final LichHocService lichHocService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<LichHocResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(lichHocService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<LichHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(lichHocService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LichHocResponse>> create(@RequestBody LichHocRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(lichHocService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<LichHocResponse>> update(@PathVariable Long id,
                                                               @RequestBody LichHocRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(lichHocService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        lichHocService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}