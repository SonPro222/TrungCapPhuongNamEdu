package org.example.trungcapphuongnam.module.giangDay.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.GiangDayPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.request.SinhLichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.dto.response.SinhLichHocPreviewResponse;
import org.example.trungcapphuongnam.module.giangDay.service.DieuPhoiGiangDayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(GiangDayPath.LOP_HOC_PHAN + "/{lopHocPhanId}/dieu-phoi")
@RequiredArgsConstructor
public class DieuPhoiGiangDayController {

    private final DieuPhoiGiangDayService dieuPhoiGiangDayService;

    @PostMapping("/lich-hoc/preview")
    public ResponseEntity<ApiResponse<SinhLichHocPreviewResponse>> previewSinhLich(
            @PathVariable Long lopHocPhanId,
            @RequestBody SinhLichHocRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(
                dieuPhoiGiangDayService.previewSinhLich(lopHocPhanId, request)
        ));
    }

    @PostMapping("/lich-hoc/tao-tu-dong")
    public ResponseEntity<ApiResponse<List<LichHocResponse>>> taoLichTuDong(
            @PathVariable Long lopHocPhanId,
            @RequestBody SinhLichHocRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(
                dieuPhoiGiangDayService.taoLichTuDong(lopHocPhanId, request)
        ));
    }
}
