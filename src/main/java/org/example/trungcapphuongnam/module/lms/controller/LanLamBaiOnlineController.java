package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.service.LanLamBaiOnlineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.LAN_LAM_BAI_ONLINE)
@RequiredArgsConstructor
public class LanLamBaiOnlineController {

    private final LanLamBaiOnlineService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LanLamBaiOnlineResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LanLamBaiOnlineResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<LanLamBaiOnlineResponse>> create(@Valid @RequestBody LanLamBaiOnlineRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<LanLamBaiOnlineResponse>> update(@PathVariable Long id, @Valid @RequestBody LanLamBaiOnlineRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
