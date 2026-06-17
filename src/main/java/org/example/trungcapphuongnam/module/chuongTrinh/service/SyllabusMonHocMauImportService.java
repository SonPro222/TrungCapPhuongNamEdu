package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFullResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service xử lý import file PDF/DOCX syllabus mẫu.
 * Parse text → lưu dữ liệu vào DB → trả về full response để FE load lên form.
 */
public interface SyllabusMonHocMauImportService {

    /**
     * Upload file + parse + lưu DB theo syllabusId đã có.
     */
    SyllabusMonHocMauFullResponse importFileBySyllabusId(Long syllabusId, MultipartFile file);

    /**
     * Upload file + parse + lưu DB theo monHocId.
     * Tự tạo syllabus tối thiểu nếu môn chưa có syllabus.
     */
    SyllabusMonHocMauFullResponse importFileByMonHocId(Long monHocId, MultipartFile file);

    /**
     * Re-parse từ file đã lưu trong DB (theo fileId) và cập nhật lại dữ liệu syllabus.
     */
    SyllabusMonHocMauFullResponse importByFileId(Long fileId);
}
