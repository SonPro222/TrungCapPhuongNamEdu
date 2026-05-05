package org.example.trungcapphuongnam.module.heThong.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.VaiTroRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.VaiTroResponse;
import org.example.trungcapphuongnam.module.heThong.service.VaiTroService;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(HeThongPath.VAI_TRO)
@RequiredArgsConstructor
public class VaiTroController {
    private final VaiTroService vaiTroService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<VaiTroResponse>>> getAll(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(vaiTroService.search(keyword, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")))));
    }

    @GetMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<VaiTroResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.ok(vaiTroService.getById(id))); }
    @PostMapping
    public ResponseEntity<ApiResponse<VaiTroResponse>> create(@RequestBody VaiTroRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(vaiTroService.create(request))); }
    @PutMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<VaiTroResponse>> update(@PathVariable Long id, @RequestBody VaiTroRequest request) { return ResponseEntity.ok(ApiResponse.ok(vaiTroService.update(id, request))); }
    @DeleteMapping(HeThongPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { vaiTroService.delete(id); return ResponseEntity.ok(ApiResponse.deleted()); }
}
