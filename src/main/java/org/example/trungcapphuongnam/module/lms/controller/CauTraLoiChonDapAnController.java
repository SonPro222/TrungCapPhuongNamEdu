package org.example.trungcapphuongnam.module.lms.controller;

import org.example.trungcapphuongnam.common.constant.Path.LmsPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiChonDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiChonDapAnResponse;
import org.example.trungcapphuongnam.module.lms.service.CauTraLoiChonDapAnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(LmsPath.CAU_TRA_LOI_CHON_DAP_AN)
@RequiredArgsConstructor
public class CauTraLoiChonDapAnController {

    private final CauTraLoiChonDapAnService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CauTraLoiChonDapAnResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauTraLoiChonDapAnResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CauTraLoiChonDapAnResponse>> create(@Valid @RequestBody CauTraLoiChonDapAnRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<CauTraLoiChonDapAnResponse>> update(@PathVariable Long id, @Valid @RequestBody CauTraLoiChonDapAnRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(LmsPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
