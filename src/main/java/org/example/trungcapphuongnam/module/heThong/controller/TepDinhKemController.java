package org.example.trungcapphuongnam.module.heThong.controller;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.HeThongPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.service.TepDinhKemService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
@RestController
@RequiredArgsConstructor
@RequestMapping(HeThongPath.TEP_DINH_KEM)
public class TepDinhKemController {

    private final TepDinhKemService service;

    private PageRequest taoPageRequest(int page, int size) {
        int pageSafe = Math.max(page, 0);
        int sizeSafe = Math.min(Math.max(size, 1), 100);
        return PageRequest.of(pageSafe, sizeSafe, Sort.by(Sort.Direction.DESC, "id"));
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<TepDinhKemResponse> upload(
            @RequestPart("file") MultipartFile file,
            @RequestPart("data") TepDinhKemRequest request
    ) {
        return ApiResponse.created(service.upload(file, request));
    }

    @GetMapping
    public ApiResponse<Page<TepDinhKemResponse>> getAll(
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String nghiepVu,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ApiResponse.ok(service.getByModule(module, nghiepVu, taoPageRequest(page, size)));
    }
    @GetMapping("/{id}")
    public ApiResponse<TepDinhKemResponse> getById(@PathVariable Long id) {
        return ApiResponse.ok(service.getById(id));
    }

    @GetMapping("/doi-tuong")
    public ApiResponse<Page<TepDinhKemResponse>> getByDoiTuong(
            @RequestParam String module,
            @RequestParam String nghiepVu,
            @RequestParam Long doiTuongId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ApiResponse.ok(service.getByDoiTuong(
                module,
                nghiepVu,
                doiTuongId,
                taoPageRequest(page, size)
        ));
    }

    @GetMapping("/nguoi-gui")
    public ApiResponse<Page<TepDinhKemResponse>> getByNguoiGui(
            @RequestParam LoaiNguoiGui nguoiGuiLoai,
            @RequestParam Long nguoiGuiId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ApiResponse.ok(service.getByNguoiGui(
                nguoiGuiLoai,
                nguoiGuiId,
                taoPageRequest(page, size)
        ));
    }
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> download(@PathVariable Long id) {
        TepDinhKemResponse tep = service.getById(id);
        Resource resource = service.download(id);

        String fileName = URLEncoder.encode(tep.getTenGoc(), StandardCharsets.UTF_8)
                .replace("+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        tep.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : tep.getContentType()
                ))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + fileName)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<TepDinhKemResponse> deleteMem(@PathVariable Long id) {
        return ApiResponse.ok(service.deleteMem(id));
    }

}