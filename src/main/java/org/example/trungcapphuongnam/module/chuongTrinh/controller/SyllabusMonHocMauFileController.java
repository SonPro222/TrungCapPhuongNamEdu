package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauParsePreviewResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauFile;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFullResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauFileService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauImportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * API quản lý file gốc của syllabus môn học mẫu.
 * Base path: /api/chuongTrinh/syllabus-mon-hoc-mau-files
 *
 * Lưu ý:
 * - Upload file được đặt ở SyllabusMonHocMauController (POST /{id}/files) để gắn với syllabusId.
 * - Controller này xử lý các thao tác theo fileId: view, download, parse-preview, delete.
 */
@RestController
@RequestMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_MAU_FILE)
@RequiredArgsConstructor
public class SyllabusMonHocMauFileController {

    private final SyllabusMonHocMauFileService fileService;
    private final SyllabusMonHocMauImportService importService;

    /**
     * Re-import từ file đã lưu trong DB: parse lại + cập nhật dữ liệu DB của syllabus.
     * Trả về full response để FE load lên form.
     */
    @PostMapping(ChuongTrinhPath.ID + "/import")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFullResponse>> importFromExistingFile(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ApiResponse.ok(importService.importByFileId(id)));
    }

    /**
     * Parse preview — chỉ trả dữ liệu để FE đổ lên form, KHÔNG lưu vào bảng nghiệp vụ.
     * Người dùng kiểm tra / sửa rồi bấm Lưu mới gọi POST /full hoặc PUT /{id}/full.
     */
    @PostMapping(ChuongTrinhPath.ID + "/parse-preview")
    public ResponseEntity<ApiResponse<SyllabusMonHocMauParsePreviewResponse>> parsePreview(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ApiResponse.ok(fileService.parsePreview(id)));
    }

    /** Xem file inline trong browser */
    @GetMapping(ChuongTrinhPath.ID + "/view")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        SyllabusMonHocMauFile file = fileService.layFileEntity(id);
        return ResponseEntity.ok()
                .contentType(resolveMediaType(file.getLoaiFile()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + encodeFilename(file.getTenFile()) + "\"")
                .body(file.getDuLieu());
    }

    /** Download file về máy */
    @GetMapping(ChuongTrinhPath.ID + "/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        SyllabusMonHocMauFile file = fileService.layFileEntity(id);
        return ResponseEntity.ok()
                .contentType(resolveMediaType(file.getLoaiFile()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodeFilename(file.getTenFile()) + "\"")
                .body(file.getDuLieu());
    }

    /** Thông tin file (không trả binary) */
    @GetMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocMauFileResponse>> getInfo(@PathVariable Long id) {
        SyllabusMonHocMauFile file = fileService.layFileEntity(id);
        SyllabusMonHocMauFileResponse response = SyllabusMonHocMauFileResponse.builder()
                .id(file.getId())
                .syllabusMonHocMauId(file.getSyllabusMonHocMauId())
                .tenFile(file.getTenFile())
                .loaiFile(file.getLoaiFile())
                .kichThuoc(file.getKichThuoc())
                .loaiTaiLieu(file.getLoaiTaiLieu())
                .laFileNguon(file.getLaFileNguon())
                .trangThaiDoc(file.getTrangThaiDoc())
                .loiDoc(file.getLoiDoc())
                .checksum(file.getChecksum())
                .ghiChu(file.getGhiChu())
                .createdAt(file.getCreatedAt())
                .updatedAt(file.getUpdatedAt())
                .build();
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    /** Xóa file gốc */
    @DeleteMapping(ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        fileService.delete(id);
        return ResponseEntity.ok(ApiResponse.deleted());
    }

    // ==================== Helper ====================

    private MediaType resolveMediaType(String loaiFile) {
        if (loaiFile == null) return MediaType.APPLICATION_OCTET_STREAM;
        return switch (loaiFile.toLowerCase()) {
            case "application/pdf" -> MediaType.APPLICATION_PDF;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" ->
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            case "application/msword" ->
                    MediaType.parseMediaType("application/msword");
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    private String encodeFilename(String filename) {
        if (filename == null) return "file";
        return URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
    }
}
