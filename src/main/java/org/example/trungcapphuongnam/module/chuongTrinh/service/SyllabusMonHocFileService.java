package org.example.trungcapphuongnam.module.chuongTrinh.service;

import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocParsePreviewResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SyllabusMonHocFileService {

    SyllabusMonHocFileResponse upload(Long syllabusMonHocId, MultipartFile file,
                                      String loaiTaiLieu, Boolean laFileNguon, String ghiChu);

    SyllabusMonHocParsePreviewResponse parsePreview(Long fileId);

    SyllabusMonHocFile layFileEntity(Long fileId);

    List<SyllabusMonHocFileResponse> findBySyllabusMonHocId(Long syllabusMonHocId);

    void delete(Long fileId);

    SyllabusMonHocFileResponse copyFromMauFile(Long syllabusMonHocId, Long mauFileId);

    SyllabusMonHocFileResponse replace(Long fileId, MultipartFile file, String loaiTaiLieu, String ghiChu);
}
