package org.example.trungcapphuongnam.module.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.NhatKyLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NhatKyLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.service.NhatKyLamBaiOnlineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.NHAT_KY_LAM_BAI_ONLINE)
@RequiredArgsConstructor
public class NhatKyLamBaiOnlineController {

    private final NhatKyLamBaiOnlineService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NhatKyLamBaiOnlineResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<NhatKyLamBaiOnlineResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NhatKyLamBaiOnlineResponse>> create(@Valid @RequestBody NhatKyLamBaiOnlineRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<NhatKyLamBaiOnlineResponse>> update(@PathVariable Long id, @Valid @RequestBody NhatKyLamBaiOnlineRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
