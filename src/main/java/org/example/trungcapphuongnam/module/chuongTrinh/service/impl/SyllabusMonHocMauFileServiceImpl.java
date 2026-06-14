package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauFileResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauParsePreviewResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMau;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauFile;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocMauFileRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
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
public class SyllabusMonHocMauFileServiceImpl implements SyllabusMonHocMauFileService {

    private static final String TRANG_THAI_CHUA_DOC = "CHUA_DOC";
    private static final String TRANG_THAI_DA_DOC    = "DA_DOC";
    private static final String TRANG_THAI_DOC_LOI   = "DOC_LOI";
    private static final String LOAI_TAI_LIEU_DEFAULT = "FILE_NGUON_IMPORT";

    private final SyllabusMonHocMauFileRepository fileRepository;
    private final SyllabusMonHocMauRepository syllabusRepository;
    private final MonHocRepository monHocRepository;

    @Override
    public SyllabusMonHocMauFileResponse upload(Long syllabusMonHocMauId, MultipartFile file,
                                                String loaiTaiLieu, Boolean laFileNguon, String ghiChu) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File upload không được rỗng");
        }

        if (!syllabusRepository.existsById(syllabusMonHocMauId)) {
            throw new ResourceNotFoundException("Syllabus môn học mẫu không tồn tại: " + syllabusMonHocMauId);
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
            log.warn("Không thể bóc text từ file syllabus mẫu: {}", e.getMessage());
        }

        SyllabusMonHocMauFile entity = SyllabusMonHocMauFile.builder()
                .syllabusMonHocMauId(syllabusMonHocMauId)
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

    /**
     * Upload file theo monHocId.
     * - Nếu môn chưa có syllabus mẫu: tự tạo syllabus tối thiểu (không tạo chương/bài/tài liệu).
     * - Lưu file ngay vào syllabus_mon_hoc_mau_file.
     * - Trả response kèm noiDungText + canhBao.
     * - KHÔNG tự lưu dữ liệu parse vào bảng nghiệp vụ chính.
     */
    @Override
    public SyllabusMonHocMauFileResponse uploadByMonHocId(Long monHocId, MultipartFile file,
                                                           String loaiTaiLieu, Boolean laFileNguon,
                                                           String ghiChu) {
        MonHoc monHoc = monHocRepository.findById(monHocId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn học không tồn tại: " + monHocId));

        // Tìm hoặc tạo syllabus mẫu tối thiểu
        SyllabusMonHocMau syllabus = syllabusRepository
                .findFirstByMonHocIdOrderByIdAsc(monHocId)
                .orElseGet(() -> taoChuoiSyllabusToiThieu(monHoc));

        // Gọi lại upload logic dùng syllabusId
        SyllabusMonHocMauFileResponse response = upload(syllabus.getId(), file, loaiTaiLieu, laFileNguon, ghiChu);

        // Bổ sung canhBao cho FE
        List<String> canhBao = new ArrayList<>();
        if (response.getNoiDungText() == null || response.getNoiDungText().isBlank()) {
            canhBao.add("File đã upload nhưng chưa bóc được text (có thể là file scan). Vui lòng nhập tay theo nội dung.");
        } else {
            canhBao.add("File đã đọc được text nhưng chưa bóc được cấu trúc tự động. Vui lòng nhập tay theo nội dung text.");
        }
        response.setCanhBao(canhBao);
        return response;
    }

    /**
     * Tạo SyllabusMonHocMau tối thiểu để có FK lưu file.
     * Chỉ đặt các field NOT NULL bắt buộc. Không tạo chương/bài hay bảng nghiệp vụ khác.
     */
    private SyllabusMonHocMau taoChuoiSyllabusToiThieu(MonHoc monHoc) {
        String maMon   = monHoc.getMaMon()  != null ? monHoc.getMaMon().trim()  : "";
        String tenMon  = monHoc.getTenMon() != null ? monHoc.getTenMon().trim() : "";
        Long   monHocId = monHoc.getId();

        // Sinh ma không trùng
        String maBase = maMon.isEmpty() ? "SYL-MH" + monHocId : "SYL-" + maMon;
        String ma = maBase;
        if (syllabusRepository.existsByMa(ma)) {
            ma = maBase + "-" + System.currentTimeMillis() % 100000;
        }

        // Sinh ten không trùng trong cùng monHocId
        String tenBase = tenMon.isEmpty() ? "Syllabus mẫu môn " + monHocId : "Syllabus mẫu - " + tenMon;
        String ten = tenBase;
        if (syllabusRepository.existsByMonHocIdAndTen(monHocId, ten)) {
            ten = tenBase + " (" + System.currentTimeMillis() % 100000 + ")";
        }

        SyllabusMonHocMau entity = SyllabusMonHocMau.builder()
                .monHocId(monHocId)
                .ma(ma)
                .ten(ten)
                .soBuoiHoc(1)
                .soTietMoiBuoi(BigDecimal.ONE)
                .soPhutMotTiet(45)
                .build();

        return syllabusRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauParsePreviewResponse parsePreview(Long fileId) {
        SyllabusMonHocMauFile file = layFileEntity(fileId);

        String noiDungText = file.getNoiDungText();
        List<String> canhBao = new ArrayList<>();

        // Nếu chưa có text, thử bóc lại từ du_lieu
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

        // Phase này chưa parse cấu trúc tự động — trả preview rỗng, chỉ có noiDungText
        return SyllabusMonHocMauParsePreviewResponse.builder()
                .syllabusMonHocMau(null)
                .chuongBaiList(Collections.emptyList())
                .dieuKienList(Collections.emptyList())
                .taiLieuList(Collections.emptyList())
                .cauHinhDanhGiaList(Collections.emptyList())
                .quyDoiDiemList(Collections.emptyList())
                .noiDungText(noiDungText)
                .canhBao(canhBao)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauFile layFileEntity(Long fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File syllabus mẫu không tồn tại: " + fileId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SyllabusMonHocMauFileResponse> findBySyllabusMonHocMauId(Long syllabusMonHocMauId) {
        return fileRepository.findBySyllabusMonHocMauIdOrderByCreatedAtDesc(syllabusMonHocMauId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new ResourceNotFoundException("File syllabus mẫu không tồn tại: " + fileId);
        }
        fileRepository.deleteById(fileId);
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

    private SyllabusMonHocMauFileResponse toResponse(SyllabusMonHocMauFile entity) {
        if (entity == null) return null;
        return SyllabusMonHocMauFileResponse.builder()
                .id(entity.getId())
                .syllabusMonHocMauId(entity.getSyllabusMonHocMauId())
                .tenFile(entity.getTenFile())
                .loaiFile(entity.getLoaiFile())
                .kichThuoc(entity.getKichThuoc())
                .loaiTaiLieu(entity.getLoaiTaiLieu())
                .laFileNguon(entity.getLaFileNguon())
                .trangThaiDoc(entity.getTrangThaiDoc())
                .loiDoc(entity.getLoiDoc())
                .checksum(entity.getChecksum())
                .ghiChu(entity.getGhiChu())
                .noiDungText(entity.getNoiDungText())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
