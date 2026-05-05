package org.example.trungcapphuongnam.module.giangDay.controller;

import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.service.PhanCongGiangDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.PHAN_CONG_GIANG_DAY)
@RequiredArgsConstructor
public class PhanCongGiangDayController {

    private final PhanCongGiangDayService phanCongGiangDayService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PhanCongGiangDayResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.ok(phanCongGiangDayService.getAll())
        );
    }

    @GetMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<PhanCongGiangDayResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.ok(phanCongGiangDayService.getById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PhanCongGiangDayResponse>> create(@RequestBody PhanCongGiangDayRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(phanCongGiangDayService.create(request)));
    }

    @PutMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<PhanCongGiangDayResponse>> update(@PathVariable Long id,
                                                                        @RequestBody PhanCongGiangDayRequest request) {
        return ResponseEntity.ok(
                ApiResponse.ok(phanCongGiangDayService.update(id, request))
        );
    }

    @DeleteMapping(GiangDayPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        phanCongGiangDayService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}