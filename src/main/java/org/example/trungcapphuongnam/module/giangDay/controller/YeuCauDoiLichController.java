package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;
import org.example.trungcapphuongnam.module.giangDay.service.YeuCauDoiLichService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.YEU_CAU_DOI_LICH)
@RequiredArgsConstructor
public class YeuCauDoiLichController {

    private final YeuCauDoiLichService yeuCauDoiLichService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<YeuCauDoiLichResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(yeuCauDoiLichService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<YeuCauDoiLichResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(yeuCauDoiLichService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<YeuCauDoiLichResponse>> create(@RequestBody YeuCauDoiLichRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(yeuCauDoiLichService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<YeuCauDoiLichResponse>> update(@PathVariable Long id,
                                                                     @RequestBody YeuCauDoiLichRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(yeuCauDoiLichService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        yeuCauDoiLichService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}