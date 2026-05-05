package org.example.trungcapphuongnam.module.thi.controller;

import org.example.trungcapphuongnam.common.constant.Path.ThiPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.thi.dto.request.DeThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.DeThiResponse;
import org.example.trungcapphuongnam.module.thi.service.DeThiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ThiPath.DE_THI)
@RequiredArgsConstructor
public class DeThiController {
    private final DeThiService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DeThiResponse>>> findAll(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(pageable)));
    }

    @GetMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<DeThiResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DeThiResponse>> create(@Valid @RequestBody DeThiRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<DeThiResponse>> update(@PathVariable Long id, @Valid @RequestBody DeThiRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ThiPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }
}
