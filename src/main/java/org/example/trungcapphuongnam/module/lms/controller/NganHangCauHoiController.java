package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.NganHangCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NganHangCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.service.NganHangCauHoiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.NGAN_HANG_CAU_HOI)
@RequiredArgsConstructor
public class NganHangCauHoiController {

    private final NganHangCauHoiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NganHangCauHoiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<NganHangCauHoiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NganHangCauHoiResponse>> create(@Valid @RequestBody NganHangCauHoiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<NganHangCauHoiResponse>> update(@PathVariable Long id, @Valid @RequestBody NganHangCauHoiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
