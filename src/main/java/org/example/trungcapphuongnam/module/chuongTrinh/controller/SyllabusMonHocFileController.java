package org.example.trungcapphuongnam.module.chuongTrinh.controller;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.constant.Path.ChuongTrinhPath;
import org.example.trungcapphuongnam.common.response.ApiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocParsePreviewResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocFile;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * API quản lý file gốc của syllabus môn học (trong chương trình).
 *
 * Upload + list:  POST/GET /api/chuongTrinh/syllabus-mon-hoc/{id}/files  (nested trong SyllabusMonHocController)
 * Theo fileId:    /api/chuongTrinh/syllabus-mon-hoc-files/{id}/...
 */
@RestController
@RequiredArgsConstructor
public class SyllabusMonHocFileController {

    private final SyllabusMonHocFileService fileService;

    // ==================== Nested endpoints (dưới syllabusMonHocId) ====================

    /** Upload file gốc cho syllabus môn học */
    @PostMapping(ChuongTrinhPath.SYLLABUS_MON_HOC + ChuongTrinhPath.ID + "/files")
    public ResponseEntity<ApiResponse<SyllabusMonHocFileResponse>> uploadFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "loaiTaiLieu", defaultValue = "FILE_NGUON_IMPORT") String loaiTaiLieu,
            @RequestParam(value = "laFileNguon", defaultValue = "true") Boolean laFileNguon,
            @RequestParam(value = "ghiChu", required = false) String ghiChu
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                fileService.upload(id, file, loaiTaiLieu, laFileNguon, ghiChu)));
    }

    /** Danh sách file của syllabus môn học */
    @GetMapping(ChuongTrinhPath.SYLLABUS_MON_HOC + ChuongTrinhPath.ID + "/files")
    public ResponseEntity<ApiResponse<List<SyllabusMonHocFileResponse>>> listFiles(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.ok(fileService.findBySyllabusMonHocId(id)));
    }

    /** Copy file từ syllabus mẫu sang syllabus môn học áp dụng */
    @PostMapping(ChuongTrinhPath.SYLLABUS_MON_HOC + ChuongTrinhPath.ID + "/copy-from-mau-file/{mauFileId}")
    public ResponseEntity<ApiResponse<SyllabusMonHocFileResponse>> copyFromMauFile(
            @PathVariable Long id,
            @PathVariable Long mauFileId
    ) {
        return ResponseEntity.status(201).body(ApiResponse.created(
                fileService.copyFromMauFile(id, mauFileId)));
    }

    // ==================== Endpoints theo fileId ====================

    /**
     * Parse preview — chỉ trả dữ liệu để FE đổ lên form, KHÔNG lưu vào bảng nghiệp vụ.
     */
    @PostMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_FILE + ChuongTrinhPath.ID + "/parse-preview")
    public ResponseEntity<ApiResponse<SyllabusMonHocParsePreviewResponse>> parsePreview(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(ApiResponse.ok(fileService.parsePreview(id)));
    }

    /** Xem file inline trong browser */
    @GetMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_FILE + ChuongTrinhPath.ID + "/view")
    public ResponseEntity<byte[]> viewFile(@PathVariable Long id) {
        SyllabusMonHocFile file = fileService.layFileEntity(id);
        return ResponseEntity.ok()
                .contentType(resolveMediaType(file.getLoaiFile()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + encodeFilename(file.getTenFile()) + "\"")
                .body(file.getDuLieu());
    }

    /** Download file về máy */
    @GetMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_FILE + ChuongTrinhPath.ID + "/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Long id) {
        SyllabusMonHocFile file = fileService.layFileEntity(id);
        return ResponseEntity.ok()
                .contentType(resolveMediaType(file.getLoaiFile()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodeFilename(file.getTenFile()) + "\"")
                .body(file.getDuLieu());
    }

    /** Thông tin file (không trả binary) */
    @GetMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_FILE + ChuongTrinhPath.ID)
    public ResponseEntity<ApiResponse<SyllabusMonHocFileResponse>> getInfo(@PathVariable Long id) {
        SyllabusMonHocFile file = fileService.layFileEntity(id);
        SyllabusMonHocFileResponse response = SyllabusMonHocFileResponse.builder()
                .id(file.getId())
                .syllabusMonHocId(file.getSyllabusMonHocId())
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

    /** Thay thế nội dung file — cập nhật binary + metadata, giữ nguyên id */
    @PostMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_FILE + ChuongTrinhPath.ID + "/replace")
    public ResponseEntity<ApiResponse<SyllabusMonHocFileResponse>> replaceFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "loaiTaiLieu", required = false) String loaiTaiLieu,
            @RequestParam(value = "ghiChu", required = false) String ghiChu
    ) {
        return ResponseEntity.ok(ApiResponse.ok(fileService.replace(id, file, loaiTaiLieu, ghiChu)));
    }

    /** Xóa file gốc */
    @DeleteMapping(ChuongTrinhPath.SYLLABUS_MON_HOC_FILE + ChuongTrinhPath.ID)
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
