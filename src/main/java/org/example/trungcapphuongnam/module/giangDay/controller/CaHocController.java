package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.CaHocResponse;
import org.example.trungcapphuongnam.module.giangDay.service.CaHocService;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping(GiangDayPath.CA_HOC)
@RequiredArgsConstructor
public class CaHocController {

    private final CaHocService caHocService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CaHocResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(caHocService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<CaHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(caHocService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CaHocResponse>> create(@RequestBody CaHocRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(caHocService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<CaHocResponse>> update(@PathVariable Long id,
                                                             @RequestBody CaHocRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(caHocService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        caHocService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
