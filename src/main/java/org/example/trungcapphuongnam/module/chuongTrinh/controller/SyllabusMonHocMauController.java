package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauFullRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFullResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauFileService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauImportService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_MAU)
@RequiredArgsConstructor
public class SyllabusMonHocMauController {

    private final SyllabusMonHocMauService service;
    private final SyllabusMonHocMauFileService fileService;
    private final SyllabusMonHocMauImportService importService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<SyllabusMonHocMauResponse>>> findAll(
            Pageable pageable,
            @RequestParam(required = false) Long monHocId,
            @RequestParam(required = false) String ma,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.findAll(monHocId, ma, keyword, pageable)));
    }

    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauResponse>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SyllabusMonHocMauResponse>> create(@RequestBody SyllabusMonHocMauRequest request) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.create(request)));
    }

    @PutMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauResponse>> update(
            @PathVariable Long id,
            @RequestBody SyllabusMonHocMauRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.update(id, request)));
    }

    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    // ==================== Full CRUD ====================

    /** Tạo full syllabus mẫu (thông tin + chương/bài + điều kiện + tài liệu + cấu hình đánh giá + quy đổi điểm) */
    @PostMapping("/full")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFullResponse>> createFull(
            @Valid @RequestBody SyllabusMonHocMauFullRequest request
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(service.createFull(request)));
    }

    /** Cập nhật full syllabus mẫu. File gốc không bị xóa khi gọi API này. */
    @PutMapping(ChuongTrinhPath.ID + "/full")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFullResponse>> updateFull(
            @PathVariable Long id,
            @Valid @RequestBody SyllabusMonHocMauFullRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.ok(service.updateFull(id, request)));
    }

    /** Lấy full syllabus mẫu bao gồm tất cả dữ liệu con và danh sách file */
    @GetMapping(ChuongTrinhPath.ID + "/full")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFullResponse>> getFull(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(service.getFull(id)));
    }

    // ==================== File endpoints (nested) ====================

    /** Upload file gốc syllabus mẫu */
    @PostMapping(ChuongTrinhPath.ID + "/files")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFileResponse>> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "loaiTaiLieu", defaultValue = "FILE_NGUON_IMPORT") String loaiTaiLieu,
            @RequestParam(value = "laFileNguon", defaultValue = "true") Boolean laFileNguon,
            @RequestParam(value = "ghiChu", required = false) String ghiChu
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                fileService.upload(id, file, loaiTaiLieu, laFileNguon, ghiChu)));
    }

    /** Danh sách file theo syllabus mẫu */
    @GetMapping(ChuongTrinhPath.ID + "/files")
    public ResponseEntity<ApiResponse<List<SyllabusMonHocMauFileResponse>>> listFiles(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(fileService.findBySyllabusMonHocMauId(id)));
    }

    // ==================== Upload theo monHocId (tự tạo syllabus nếu chưa có) ====================

    // ==================== Import endpoints (upload + parse + save DB) ====================

    /**
     * Upload file + parse + lưu dữ liệu vào DB theo syllabusId.
     * Trả về full response để FE load lên form.
     */
    @PostMapping(ChuongTrinhPath.ID + "/import-file")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFullResponse>> importFileBySyllabusId(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(ApiResponse.ok(importService.importFileBySyllabusId(id, file)));
    }

    /**
     * Upload file + parse + lưu dữ liệu vào DB theo monHocId.
     * Tự tạo syllabus tối thiểu nếu chưa có.
     */
    @PostMapping("/by-mon-hoc/{monHocId}/import-file")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFullResponse>> importFileByMonHocId(
            @PathVariable Long monHocId,
            @RequestParam("file") MultipartFile file
    ) {
        return ResponseEntity.ok(ApiResponse.ok(importService.importFileByMonHocId(monHocId, file)));
    }

    /**
     * Upload file gốc syllabus mẫu theo monHocId.
     * Nếu môn chưa có syllabus mẫu → tự tạo syllabus tối thiểu.
     * Response kèm noiDungText + canhBao để FE populate form (preview only).
     */
    @PostMapping("/by-mon-hoc/{monHocId}/files")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFileResponse>> uploadByMonHocId(
            @PathVariable Long monHocId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "loaiTaiLieu", defaultValue = "FILE_NGUON_IMPORT") String loaiTaiLieu,
            @RequestParam(value = "laFileNguon", defaultValue = "true") Boolean laFileNguon,
            @RequestParam(value = "ghiChu", required = false) String ghiChu
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                fileService.uploadByMonHocId(monHocId, file, loaiTaiLieu, laFileNguon, ghiChu)));
    }
}