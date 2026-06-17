package org.example.trungcapphuongnam.module.chuongTrinh.controller;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienMonHocMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ChuongTrinhPath.DIEU_KIEN_MON_HOC_MAU)
@RequiredArgsConstructor
public class DieuKienMonHocMauController {

    private final DieuKienMonHocMauService service;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<DieuKienMonHocMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String loai,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(ma, loai, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<DieuKienMonHocMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<DieuKienMonHocMauResponse>> create(@RequestBody DieuKienMonHocMauRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<DieuKienMonHocMauResponse>> update(
            @PathVariable Long id,
            @RequestBody DieuKienMonHocMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}