package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocParsePreviewResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocFile;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauFile;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocFileRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocMauFileRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocFileServiceImpl implements SyllabusMonHocFileService {

    private static final String TRANG_THAI_CHUA_DOC  = "CHUA_DOC";
    private static final String TRANG_THAI_DA_DOC    = "DA_DOC";
    private static final String TRANG_THAI_DOC_LOI   = "DOC_LOI";
    private static final String LOAI_TAI_LIEU_DEFAULT = "FILE_NGUON_IMPORT";

    private final SyllabusMonHocFileRepository fileRepository;
    private final SyllabusMonHocRepository syllabusMonHocRepository;
    private final SyllabusMonHocMauFileRepository mauFileRepository;

    @Override
    public SyllabusMonHocFileResponse upload(Long syllabusMonHocId, MultipartFile file,
                                             String loaiTaiLieu, Boolean laFileNguon, String ghiChu) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File upload không được rỗng");
        }

        if (!syllabusMonHocRepository.existsById(syllabusMonHocId)) {
            throw new ResourceNotFoundException("Syllabus môn học không tồn tại: " + syllabusMonHocId);
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc nội dung file upload", e);
        }

        String checksum = tinhChecksum(bytes);
        String contentType = file.getContentType();
        String tenFile = file.getOriginalFilename() != null ? file.getOriginalFilename() : file.getName();

        String noiDungText = null;
        String trangThaiDoc = TRANG_THAI_CHUA_DOC;
        String loiDoc = null;

        try {
            noiDungText = bocTextTuFile(bytes, contentType, tenFile);
            if (noiDungText != null) {
                trangThaiDoc = TRANG_THAI_DA_DOC;
            }
        } catch (Exception e) {
            trangThaiDoc = TRANG_THAI_DOC_LOI;
            loiDoc = e.getMessage();
            log.warn("Không thể bóc text từ file syllabus môn học: {}", e.getMessage());
        }

        SyllabusMonHocFile entity = SyllabusMonHocFile.builder()
                .syllabusMonHocId(syllabusMonHocId)
                .tenFile(tenFile)
                .loaiFile(contentType)
                .kichThuoc((long) bytes.length)
                .duLieu(bytes)
                .noiDungText(noiDungText)
                .loaiTaiLieu(loaiTaiLieu != null ? loaiTaiLieu : LOAI_TAI_LIEU_DEFAULT)
                .laFileNguon(laFileNguon != null ? laFileNguon : Boolean.TRUE)
                .trangThaiDoc(trangThaiDoc)
                .loiDoc(loiDoc)
                .checksum(checksum)
                .ghiChu(ghiChu)
                .build();

        return toResponse(fileRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocParsePreviewResponse parsePreview(Long fileId) {
        SyllabusMonHocFile file = layFileEntity(fileId);

        String noiDungText = file.getNoiDungText();
        List<String> canhBao = new ArrayList<>();

        if ((noiDungText == null || noiDungText.isBlank()) && file.getDuLieu() != null) {
            try {
                noiDungText = bocTextTuFile(file.getDuLieu(), file.getLoaiFile(), file.getTenFile());
            } catch (Exception e) {
                canhBao.add("Không thể bóc text từ file: " + e.getMessage());
            }
        }

        if (noiDungText == null || noiDungText.isBlank()) {
            canhBao.add("Không bóc được text từ file. Vui lòng nhập liệu thủ công.");
        } else {
            canhBao.add("Dữ liệu parse chỉ là preview. Vui lòng kiểm tra, sửa form rồi bấm Lưu để lưu vào hệ thống.");
        }

        return SyllabusMonHocParsePreviewResponse.builder()
                .syllabusMonHoc(null)
                .chuongBaiList(Collections.emptyList())
                .dieuKienList(Collections.emptyList())
                .taiLieuList(Collections.emptyList())
                .noiDungText(noiDungText)
                .canhBao(canhBao)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocFile layFileEntity(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File syllabus môn học không tồn tại: " + fileId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusMonHocFileResponse> findBySyllabusMonHocId(Long syllabusMonHocId) {
        return fileRepository.findBySyllabusMonHocIdOrderByCreatedAtDesc(syllabusMonHocId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new ResourceNotFoundException("File syllabus môn học không tồn tại: " + fileId);
        }
        fileRepository.deleteById(fileId);
    }

    @Override
    public SyllabusMonHocFileResponse replace(Long fileId, MultipartFile file, String loaiTaiLieu, String ghiChu) {
        SyllabusMonHocFile entity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File syllabus môn học không tồn tại: " + fileId));

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File upload không được rỗng");
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("Không thể đọc nội dung file upload", e);
        }

        String checksum = tinhChecksum(bytes);
        String contentType = file.getContentType();
        String tenFile = file.getOriginalFilename() != null ? file.getOriginalFilename() : file.getName();

        String noiDungText = null;
        String trangThaiDoc = TRANG_THAI_CHUA_DOC;
        String loiDoc = null;
        try {
            noiDungText = bocTextTuFile(bytes, contentType, tenFile);
            if (noiDungText != null) trangThaiDoc = TRANG_THAI_DA_DOC;
        } catch (Exception e) {
            trangThaiDoc = TRANG_THAI_DOC_LOI;
            loiDoc = e.getMessage();
        }

        entity.setTenFile(tenFile);
        entity.setLoaiFile(contentType);
        entity.setKichThuoc((long) bytes.length);
        entity.setDuLieu(bytes);
        entity.setNoiDungText(noiDungText);
        if (loaiTaiLieu != null) entity.setLoaiTaiLieu(loaiTaiLieu);
        entity.setTrangThaiDoc(trangThaiDoc);
        entity.setLoiDoc(loiDoc);
        entity.setChecksum(checksum);
        if (ghiChu != null) entity.setGhiChu(ghiChu);

        return toResponse(fileRepository.save(entity));
    }

    @Override
    public SyllabusMonHocFileResponse copyFromMauFile(Long syllabusMonHocId, Long mauFileId) {
        if (!syllabusMonHocRepository.existsById(syllabusMonHocId)) {
            throw new ResourceNotFoundException("Syllabus môn học không tồn tại: " + syllabusMonHocId);
        }
        SyllabusMonHocMauFile mauFile = mauFileRepository.findById(mauFileId)
                .orElseThrow(() -> new ResourceNotFoundException("File syllabus mẫu không tồn tại: " + mauFileId));

        SyllabusMonHocFile entity = SyllabusMonHocFile.builder()
                .syllabusMonHocId(syllabusMonHocId)
                .tenFile(mauFile.getTenFile())
                .loaiFile(mauFile.getLoaiFile())
                .kichThuoc(mauFile.getKichThuoc())
                .duLieu(mauFile.getDuLieu())
                .noiDungText(mauFile.getNoiDungText())
                .loaiTaiLieu(mauFile.getLoaiTaiLieu() != null ? mauFile.getLoaiTaiLieu() : LOAI_TAI_LIEU_DEFAULT)
                .laFileNguon(Boolean.TRUE)
                .trangThaiDoc(mauFile.getTrangThaiDoc())
                .loiDoc(mauFile.getLoiDoc())
                .checksum(mauFile.getChecksum())
                .ghiChu(mauFile.getGhiChu())
                .build();

        return toResponse(fileRepository.save(entity));
    }

    // ==================== Helper ====================

    private String bocTextTuFile(byte[] bytes, String contentType, String tenFile) throws IOException {
        if (bytes == null || bytes.length == 0) return null;

        String loaiFile = contentType != null ? contentType.toLowerCase() : "";
        String tenFileLower = tenFile != null ? tenFile.toLowerCase() : "";

        if (loaiFile.contains("pdf") || tenFileLower.endsWith(".pdf")) {
            return bocTextTuPdf(bytes);
        }

        if (loaiFile.contains("wordprocessingml") || loaiFile.contains("docx")
                || tenFileLower.endsWith(".docx")) {
            return bocTextTuDocx(bytes);
        }

        return null;
    }

    private String bocTextTuPdf(byte[] bytes) throws IOException {
        try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(bytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    private String bocTextTuDocx(byte[] bytes) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(bytes));
             XWPFWordExtractor extractor = new XWPFWordExtractor(doc)) {
            return extractor.getText();
        }
    }

    private String tinhChecksum(byte[] bytes) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(bytes);
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            log.warn("Không tính được checksum: {}", e.getMessage());
            return null;
        }
    }

    private SyllabusMonHocFileResponse toResponse(SyllabusMonHocFile entity) {
        if (entity == null) return null;
        return SyllabusMonHocFileResponse.builder()
                .id(entity.getId())
                .syllabusMonHocId(entity.getSyllabusMonHocId())
                .tenFile(entity.getTenFile())
                .loaiFile(entity.getLoaiFile())
                .kichThuoc(entity.getKichThuoc())
                .loaiTaiLieu(entity.getLoaiTaiLieu())
                .laFileNguon(entity.getLaFileNguon())
                .trangThaiDoc(entity.getTrangThaiDoc())
                .loiDoc(entity.getLoiDoc())
                .checksum(entity.getChecksum())
                .ghiChu(entity.getGhiChu())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
