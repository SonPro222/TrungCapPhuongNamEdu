package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauParsePreviewResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SyllabusMonHocMauFileService {

    SyllabusMonHocMauFileResponse upload(Long syllabusMonHocMauId, MultipartFile file,
                                         String loaiTaiLieu, Boolean laFileNguon, String ghiChu);

    /**
     * Upload file theo monHocId.
     * Nếu môn chưa có syllabus mẫu → tự tạo syllabus tối thiểu.
     * Lưu file ngay. Response kèm noiDungText + canhBao.
     * KHÔNG tự lưu dữ liệu parse vào bảng nghiệp vụ chính.
     */
    SyllabusMonHocMauFileResponse uploadByMonHocId(Long monHocId, MultipartFile file,
                                                   String loaiTaiLieu, Boolean laFileNguon, String ghiChu);

    SyllabusMonHocMauParsePreviewResponse parsePreview(Long fileId);

    SyllabusMonHocMauFile layFileEntity(Long fileId);

    List<SyllabusMonHocMauFileResponse> findBySyllabusMonHocMauId(Long syllabusMonHocMauId);

    void delete(Long fileId);
}
