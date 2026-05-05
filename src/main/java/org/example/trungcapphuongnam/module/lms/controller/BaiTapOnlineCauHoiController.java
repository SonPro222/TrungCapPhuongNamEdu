package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.service.BaiTapOnlineCauHoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.BAI_TAP_ONLINE_CAU_HOI)
@RequiredArgsConstructor
public class BaiTapOnlineCauHoiController {

    private final BaiTapOnlineCauHoiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<BaiTapOnlineCauHoiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<BaiTapOnlineCauHoiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BaiTapOnlineCauHoiResponse>> create(@Valid @RequestBody BaiTapOnlineCauHoiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<BaiTapOnlineCauHoiResponse>> update(@PathVariable Long id, @Valid @RequestBody BaiTapOnlineCauHoiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
