package org.example.trungcapphuongnam.module.lms.controller.nghiepvu;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.nghiepvu.NopBaiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.service.nghiepvu.LmsNghiepVuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.BASE + "/sinh-vien")
@RequiredArgsConstructor
public class LmsSinhVienController {

    private final LmsNghiepVuService lmsNghiepVuService;

    @PostMapping("/bai-tap-lop/{baiTapLopId}/bat-dau")
    public ResponseEntity<ApiResponse<LanLamBaiOnlineResponse>> batDauLamBai(@PathVariable Long baiTapLopId) {
        return ResponseEntity.status(201).body(ApiResponse.created(lmsNghiepVuService.batDauLamBai(baiTapLopId)));
    }

    @PostMapping("/lan-lam-bai/{lanLamBaiId}/nop-bai")
    public ResponseEntity<ApiResponse<LanLamBaiOnlineResponse>> nopBai(
            @PathVariable Long lanLamBaiId,
            @Valid @RequestBody NopBaiRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(lmsNghiepVuService.nopBai(lanLamBaiId, request)));
    }
}
