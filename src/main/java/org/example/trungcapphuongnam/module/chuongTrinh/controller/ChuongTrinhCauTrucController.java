package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.cauTruc.ChuongTrinhCauTrucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhCauTrucService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.CHUONG_TRINH_VERSION)
@RequiredArgsConstructor
public class ChuongTrinhCauTrucController {

    private final ChuongTrinhCauTrucService service;

    @GetMapping("/{id}/cau-truc")
    public ResponseEntity<ApiResponse<ChuongTrinhCauTrucResponse>> findCauTrucByVersionId(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                ApiResponse.ok(service.findCauTrucByVersionId(id))
        );
    }
}