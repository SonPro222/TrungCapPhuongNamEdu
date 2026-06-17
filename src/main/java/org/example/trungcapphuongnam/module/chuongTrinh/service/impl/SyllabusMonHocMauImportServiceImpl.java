package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.*;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.*;
import org.example.trungcapphuongnam.module.chuongTrinh.enums.LoaiDieuKienMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.*;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.*;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauFileService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocMauImportServiceImpl implements SyllabusMonHocMauImportService {

    private final MonHocRepository monHocRepository;
    private final SyllabusMonHocMauRepository syllabusRepository;
    private final SyllabusMonHocMauChuongBaiRepository chuongBaiRepository;
    private final SyllabusMonHocMauDieuKienRepository dieuKienGanRepository;
    private final SyllabusMonHocMauTaiLieuRepository taiLieuGanRepository;
    private final DieuKienMonHocMauRepository dieuKienMauRepository;
    private final TaiLieuMauRepository taiLieuMauRepository;
    private final SyllabusMonHocMauFileRepository fileRepository;
    private final SyllabusMonHocMauFileService fileService;

    private final SyllabusMonHocMauMapper syllabusMapper;
    private final SyllabusMonHocMauChuongBaiMapper chuongBaiMapper;
    private final SyllabusMonHocMauDieuKienMapper dieuKienMapper;
    private final SyllabusMonHocMauTaiLieuMapper taiLieuMapper;
    private final CauHinhDanhGiaMauMapper cauHinhDanhGiaMauMapper;
    private final QuyDoiDiemMauMapper quyDoiDiemMauMapper;
    private final CauHinhDanhGiaMauRepository cauHinhDanhGiaMauRepository;
    private final QuyDoiDiemMauRepository quyDoiDiemMauRepository;

    // ==================== Public API ====================

    @Override
    public SyllabusMonHocMauFullResponse importFileByMonHocId(Long monHocId, MultipartFile file) {
        MonHoc monHoc = monHocRepository.findById(monHocId)
                .orElseThrow(() -> new ResourceNotFoundException("Môn học không tồn tại: " + monHocId));

        SyllabusMonHocMau syllabus = syllabusRepository.findFirstByMonHocIdOrderByIdAsc(monHocId)
                .orElseGet(() -> taoChuoiSyllabusToiThieu(monHoc));

        return importFileToSyllabus(syllabus, file);
    }

    @Override
    public SyllabusMonHocMauFullResponse importFileBySyllabusId(Long syllabusId, MultipartFile file) {
        SyllabusMonHocMau syllabus = syllabusRepository.findById(syllabusId)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus không tồn tại: " + syllabusId));
        return importFileToSyllabus(syllabus, file);
    }

    @Override
    public SyllabusMonHocMauFullResponse importByFileId(Long fileId) {
        SyllabusMonHocMauFile fileEntity = fileRepository.findById(fileId)
                .orElseThrow(() -> new ResourceNotFoundException("File không tồn tại: " + fileId));

        SyllabusMonHocMau syllabus = syllabusRepository.findById(fileEntity.getSyllabusMonHocMauId())
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus của file này không tồn tại"));

        List<String> canhBao = new ArrayList<>();

        if (fileEntity.getDuLieu() == null) {
            canhBao.add("File không có dữ liệu binary. Import bị hủy.");
            return buildFullResponse(syllabus, canhBao);
        }

        String text = bocText(fileEntity.getDuLieu(), fileEntity.getLoaiFile(), fileEntity.getTenFile(), canhBao);
        if (text == null || text.isBlank()) {
            canhBao.add("Không bóc được text. Dữ liệu DB chưa được cập nhật.");
            return buildFullResponse(syllabus, canhBao);
        }

        applyParsedToSyllabus(syllabus, text, canhBao);
        return buildFullResponse(syllabus, canhBao);
    }

    // ==================== Private helpers ====================

    private SyllabusMonHocMauFullResponse importFileToSyllabus(SyllabusMonHocMau syllabus, MultipartFile file) {
        List<String> canhBao = new ArrayList<>();

        if (file == null || file.isEmpty()) {
            canhBao.add("File upload rỗng.");
            return buildFullResponse(syllabus, canhBao);
        }

        byte[] bytes;
        try {
            bytes = file.getBytes();
        } catch (IOException e) {
            canhBao.add("Không đọc được nội dung file: " + e.getMessage());
            return buildFullResponse(syllabus, canhBao);
        }

        try {
            fileService.upload(syllabus.getId(), file, "FILE_NGUON_IMPORT", true, null);
        } catch (Exception e) {
            log.warn("Không lưu được file vào DB: {}", e.getMessage());
            canhBao.add("File đã parse nhưng không lưu vào bảng file: " + e.getMessage());
        }

        String text = bocText(bytes, file.getContentType(), file.getOriginalFilename(), canhBao);
        if (text == null || text.isBlank()) {
            canhBao.add("Không bóc được text từ file. Dữ liệu DB chưa được cập nhật từ file này.");
            return buildFullResponse(syllabus, canhBao);
        }

        applyParsedToSyllabus(syllabus, text, canhBao);
        return buildFullResponse(syllabus, canhBao);
    }

    private void applyParsedToSyllabus(SyllabusMonHocMau syllabus, String text, List<String> canhBao) {
        int canhBaoStartIndex = canhBao != null ? canhBao.size() : 0;

        ParsedSyllabus parsed = parseTextForSyllabus(text, syllabus, canhBao);
        if (parsed == null) {
            addLoiNghiemTrong(canhBao, "Không parse được cấu trúc syllabus từ file.");
            return;
        }

        validateParsedSyllabus(parsed, canhBao);

        if (coLoiNghiemTrong(canhBao, canhBaoStartIndex)) {
            canhBao.add("Import bị hủy. Dữ liệu cũ trong DB được giữ nguyên vì file/paste có lỗi nghiêm trọng.");
            return;
        }

        updateSyllabusMain(syllabus, parsed, canhBao);

        chuongBaiRepository.deleteBySyllabusMonHocMauId(syllabus.getId());
        saveChuongBai(syllabus.getId(), parsed.chuongBaiList, canhBao);

        dieuKienGanRepository.deleteBySyllabusMonHocMauId(syllabus.getId());
        saveDieuKien(syllabus.getId(), parsed.dieuKienList, canhBao);

        taiLieuGanRepository.deleteBySyllabusMonHocMauId(syllabus.getId());
        saveTaiLieu(syllabus.getId(), parsed.taiLieuList, canhBao);
    }

    private void updateSyllabusMain(SyllabusMonHocMau syllabus, ParsedSyllabus parsed, List<String> canhBao) {
        if (parsed.ma != null && !parsed.ma.isBlank()) {
            if (!syllabusRepository.existsByMaAndIdNot(parsed.ma, syllabus.getId())) {
                syllabus.setMa(cleanTextForDb(parsed.ma));
            } else {
                canhBao.add("Mã '" + parsed.ma + "' đã tồn tại, giữ mã cũ: " + syllabus.getMa());
            }
        }

        if (parsed.ten != null && !parsed.ten.isBlank()) {
            if (!syllabusRepository.existsByMonHocIdAndTenAndIdNot(syllabus.getMonHocId(), parsed.ten, syllabus.getId())) {
                syllabus.setTen(cleanTextForDb(parsed.ten));
            } else {
                canhBao.add("Tên '" + parsed.ten + "' đã tồn tại cho môn này, giữ tên cũ.");
            }
        }

        if (parsed.viTri != null) {
            syllabus.setViTri(cleanTextForDb(parsed.viTri));
        }
        if (parsed.tinhChat != null) {
            syllabus.setTinhChat(cleanTextForDb(parsed.tinhChat));
        }
        if (parsed.mucTieu != null) {
            syllabus.setMucTieu(cleanTextForDb(parsed.mucTieu));
        }
        if (parsed.phuongPhapDanhGia != null) {
            syllabus.setPhuongPhapDanhGia(cleanTextForDb(parsed.phuongPhapDanhGia));
        }
        if (parsed.huongDan != null) {
            syllabus.setHuongDan(cleanTextForDb(parsed.huongDan));
        }

        syllabusRepository.save(syllabus);
    }

    private void saveChuongBai(Long syllabusId, List<ParsedChuongBai> list, List<String> canhBao) {
        if (list == null || list.isEmpty()) {
            canhBao.add("Không parse được danh sách chương/bài từ file.");
            return;
        }

        for (ParsedChuongBai cb : list) {
            SyllabusMonHocMauChuongBai entity = SyllabusMonHocMauChuongBai.builder()
                    .syllabusMonHocMauId(syllabusId)
                    .maChuong(cb.maChuong != null && !cb.maChuong.isBlank() ? cleanTextForDb(cb.maChuong) : String.valueOf(cb.thuTu))
                    .ten(cleanTextForDb(cb.ten))
                    .thuTu(cb.thuTu)
                    .tongGio(nullToZero(cb.tongGio))
                    .gioLyThuyet(nullToZero(cb.gioLyThuyet))
                    .gioThucHanh(nullToZero(cb.gioThucHanh))
                    .gioKiemTra(nullToZero(cb.gioKiemTra))
                    .mucTieu(cleanTextForDb(cb.mucTieu))
                    .noiDung(cleanTextForDb(cb.noiDung))
                    .ghiChu(cleanTextForDb(cb.ghiChu))
                    .build();

            chuongBaiRepository.save(entity);
        }
    }

    private void saveDieuKien(Long syllabusId, List<ParsedDieuKien> list, List<String> canhBao) {
        if (list == null || list.isEmpty()) {
            canhBao.add("Không parse được điều kiện thực hiện từ file.");
            return;
        }

        long ts = System.currentTimeMillis();
        int thuTu = 1;

        for (ParsedDieuKien dk : list) {
            String ma = "DK-" + dk.loai.getValue().toUpperCase() + "-S" + syllabusId + "-" + (ts % 100000) + "-" + thuTu;

            DieuKienMonHocMau dkMau = DieuKienMonHocMau.builder()
                    .ma(ma)
                    .loai(dk.loai)
                    .noiDung(cleanTextForDb(dk.noiDung))
                    .build();

            dkMau = dieuKienMauRepository.save(dkMau);

            SyllabusMonHocMauDieuKien gan = SyllabusMonHocMauDieuKien.builder()
                    .syllabusMonHocMauId(syllabusId)
                    .dieuKienMauId(dkMau.getId())
                    .thuTu(thuTu++)
                    .build();

            dieuKienGanRepository.save(gan);
        }
    }

    private void saveTaiLieu(Long syllabusId, List<ParsedTaiLieu> list, List<String> canhBao) {
        if (list == null || list.isEmpty()) {
            canhBao.add("Không parse được tài liệu tham khảo từ file.");
            return;
        }

        long ts = System.currentTimeMillis();

        for (ParsedTaiLieu tl : list) {
            String ma = "TL-S" + syllabusId + "-" + tl.thuTu + "-" + (ts % 100000);

            TaiLieuMau tlMau = TaiLieuMau.builder()
                    .ma(ma)
                    .ten(cleanTextForDb(tl.ten))
                    .loai(tl.loai)
                    .tacGia(cleanTextForDb(tl.tacGia))
                    .nhaXuatBan(cleanTextForDb(tl.nhaXuatBan))
                    .namXuatBan(tl.namXuatBan)
                    .build();

            tlMau = taiLieuMauRepository.save(tlMau);

            SyllabusMonHocMauTaiLieu gan = SyllabusMonHocMauTaiLieu.builder()
                    .syllabusMonHocMauId(syllabusId)
                    .taiLieuMauId(tlMau.getId())
                    .ma(ma)
                    .ten(cleanTextForDb(tl.ten))
                    .loai(tl.loai)
                    .tacGia(cleanTextForDb(tl.tacGia))
                    .nhaXuatBan(cleanTextForDb(tl.nhaXuatBan))
                    .namXuatBan(tl.namXuatBan)
                    .thuTu(tl.thuTu)
                    .batBuoc(false)
                    .build();

            taiLieuGanRepository.save(gan);
        }
    }

    private SyllabusMonHocMauFullResponse buildFullResponse(SyllabusMonHocMau syllabus, List<String> canhBao) {
        Long id = syllabus.getId();

        List<SyllabusMonHocMauChuongBaiResponse> chuongBai = chuongBaiRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream()
                .map(chuongBaiMapper::toResponse)
                .collect(Collectors.toList());

        List<SyllabusMonHocMauDieuKienResponse> dieuKien = dieuKienGanRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream()
                .map(dieuKienMapper::toResponse)
                .collect(Collectors.toList());

        List<SyllabusMonHocMauTaiLieuResponse> taiLieu = taiLieuGanRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream()
                .map(taiLieuMapper::toResponse)
                .collect(Collectors.toList());

        List<CauHinhDanhGiaMauResponse> cauHinh = cauHinhDanhGiaMauRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream()
                .map(cauHinhDanhGiaMauMapper::toResponse)
                .collect(Collectors.toList());

        List<QuyDoiDiemMauResponse> quyDoi = quyDoiDiemMauRepository
                .findBySyllabusMonHocMauIdOrderByThuTuAscIdAsc(id)
                .stream()
                .map(quyDoiDiemMauMapper::toResponse)
                .collect(Collectors.toList());

        List<SyllabusMonHocMauFileResponse> files = fileService.findBySyllabusMonHocMauId(id);

        SyllabusMonHocMauResponse syllabusResponse = syllabusMapper.toResponse(syllabus);

        syllabusResponse.setTongGio(chuongBai.stream()
                .map(c -> c.getTongGio() != null ? c.getTongGio() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        syllabusResponse.setGioLyThuyet(chuongBai.stream()
                .map(c -> c.getGioLyThuyet() != null ? c.getGioLyThuyet() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        syllabusResponse.setGioThucHanh(chuongBai.stream()
                .map(c -> c.getGioThucHanh() != null ? c.getGioThucHanh() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        syllabusResponse.setGioKiemTra(chuongBai.stream()
                .map(c -> c.getGioKiemTra() != null ? c.getGioKiemTra() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return SyllabusMonHocMauFullResponse.builder()
                .syllabusMonHocMau(syllabusResponse)
                .chuongBaiList(chuongBai)
                .dieuKienList(dieuKien)
                .taiLieuList(taiLieu)
                .cauHinhDanhGiaList(cauHinh)
                .quyDoiDiemList(quyDoi)
                .fileList(files)
                .canhBao(canhBao == null || canhBao.isEmpty() ? null : canhBao)
                .build();
    }

    // ==================== Text extraction ====================

    private String bocText(byte[] bytes, String contentType, String tenFile, List<String> canhBao) {
        try {
            String loai = contentType != null ? contentType.toLowerCase() : "";
            String ten = tenFile != null ? tenFile.toLowerCase() : "";

            if (loai.contains("pdf") || ten.endsWith(".pdf")) {
                return bocTextPdf(bytes);
            }

            if (loai.contains("wordprocessingml") || loai.contains("docx") || ten.endsWith(".docx")) {
                return bocTextDocx(bytes);
            }

            canhBao.add("Định dạng file chưa được hỗ trợ parse. Chỉ hỗ trợ PDF và DOCX.");
            return null;
        } catch (Exception e) {
            canhBao.add("Lỗi đọc text từ file: " + e.getMessage());
            return null;
        }
    }

    private String bocTextPdf(byte[] bytes) throws IOException {
        try (PDDocument doc = PDDocument.load(new ByteArrayInputStream(bytes))) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(doc);
        }
    }

    private String bocTextDocx(byte[] bytes) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(bytes));
             XWPFWordExtractor ext = new XWPFWordExtractor(doc)) {
            return ext.getText();
        }
    }

    // ==================== Parser ====================

    private ParsedSyllabus parseTextForSyllabus(String text, SyllabusMonHocMau syllabus, List<String> canhBao) {
        if (text == null || text.isBlank()) {
            return null;
        }

        String selectedText = chonKhoiSyllabusPhuHop(text, syllabus, canhBao);
        return parseText(selectedText, canhBao);
    }

    private String chonKhoiSyllabusPhuHop(String text, SyllabusMonHocMau syllabus, List<String> canhBao) {
        String normalizedText = normalizeText(text);
        List<String> blocks = tachKhoiChuongTrinhMonHoc(normalizedText);

        if (blocks.size() <= 1) {
            return normalizedText;
        }

        String maMon = null;
        String tenMon = null;
        if (syllabus != null && syllabus.getMonHocId() != null) {
            Optional<MonHoc> monHocOpt = monHocRepository.findById(syllabus.getMonHocId());
            if (monHocOpt.isPresent()) {
                MonHoc monHoc = monHocOpt.get();
                maMon = monHoc.getMaMon();
                tenMon = monHoc.getTenMon();
            }
        }

        String maNeedle = chuanHoaTimKiem(maMon);
        String tenNeedle = chuanHoaTimKiem(tenMon);

        for (String block : blocks) {
            String b = chuanHoaTimKiem(block);
            if (maNeedle != null && !maNeedle.isBlank()
                    && (b.contains("ma mon hoc " + maNeedle) || b.contains("ma mo dun " + maNeedle) || b.contains("ma mo-dun " + maNeedle))) {
                canhBao.add("File có nhiều chương trình môn học/mô đun. Đã chọn đúng block theo mã: " + maMon + ".");
                return block;
            }
        }

        for (String block : blocks) {
            String b = chuanHoaTimKiem(block);
            if (tenNeedle != null && !tenNeedle.isBlank()
                    && (b.contains("ten mon hoc " + tenNeedle) || b.contains("ten mo dun " + tenNeedle) || b.contains("ten mo-dun " + tenNeedle))) {
                canhBao.add("File có nhiều chương trình môn học/mô đun. Đã chọn đúng block theo tên môn/mô đun: " + tenMon + ".");
                return block;
            }
        }

        addLoiNghiemTrong(canhBao, "File có " + blocks.size() + " chương trình môn học/mô đun nhưng không tìm thấy block khớp mã/tên môn hiện tại. Import bị hủy để tránh lấy nhầm môn khác.");
        return normalizedText;
    }

    private List<String> tachKhoiChuongTrinhMonHoc(String normalizedText) {
        List<String> blocks = new ArrayList<>();
        if (normalizedText == null || normalizedText.isBlank()) {
            return blocks;
        }

        Pattern startPattern = Pattern.compile("(?im)^\\s*CHƯƠNG\\s+TRÌNH\\s+(?:MÔN\\s+HỌC|MÔ\\s*ĐUN|MÔ-ĐUN)\\s*$", Pattern.UNICODE_CASE);
        Matcher matcher = startPattern.matcher(normalizedText);
        List<Integer> starts = new ArrayList<>();
        while (matcher.find()) {
            starts.add(matcher.start());
        }

        if (starts.isEmpty()) {
            blocks.add(normalizedText);
            return blocks;
        }

        for (int i = 0; i < starts.size(); i++) {
            int start = starts.get(i);
            int end = i + 1 < starts.size() ? starts.get(i + 1) : normalizedText.length();
            String block = normalizedText.substring(start, end).trim();
            if (!block.isBlank()) {
                blocks.add(block);
            }
        }
        return blocks;
    }

    private String chuanHoaTimKiem(String value) {
        if (value == null) {
            return null;
        }
        String noAccent = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}+", "")
                .replace('Đ', 'D')
                .replace('đ', 'd');
        return noAccent.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private ParsedSyllabus parseText(String text, List<String> canhBao) {
        if (text == null || text.isBlank()) {
            return null;
        }

        ParsedSyllabus result = new ParsedSyllabus();
        String normalizedText = normalizeText(text);

        List<String> lines = Arrays.stream(normalizedText.split("\n"))
                .map(String::trim)
                .collect(Collectors.toList());

        for (String line : lines) {
            if (line.startsWith("Tên môn học:")) {
                result.ten = cleanTextForDb(line.substring("Tên môn học:".length()).trim());
            } else if (line.startsWith("Tên mô đun:")) {
                result.ten = cleanTextForDb(line.substring("Tên mô đun:".length()).trim());
            } else if (line.startsWith("Tên mô-đun:")) {
                result.ten = cleanTextForDb(line.substring("Tên mô-đun:".length()).trim());
            } else if (line.startsWith("Mã môn học:")) {
                result.ma = cleanTextForDb(line.substring("Mã môn học:".length()).trim());
            } else if (line.startsWith("Mã mô đun:")) {
                result.ma = cleanTextForDb(line.substring("Mã mô đun:".length()).trim());
            } else if (line.startsWith("Mã mô-đun:")) {
                result.ma = cleanTextForDb(line.substring("Mã mô-đun:".length()).trim());
            }

            if (line.matches("^I\\..*")) {
                break;
            }
        }

        String sectionI = extractSection(lines, "I", "II");
        String sectionII = extractSection(lines, "II", "III");
        String sectionIII = extractSection(lines, "III", "IV");
        String sectionIV = extractSection(lines, "IV", "V");
        String sectionV = extractSection(lines, "V", "VI");
        String sectionVI = extractSectionToEnd(lines, "VI");

        parseViTriTinhChat(sectionI, result);

        if (sectionII != null && !sectionII.isBlank()) {
            result.mucTieu = cleanTextForDb(sectionII);
        }

        parseTableSection(sectionIII, result, canhBao);
        parseDieuKien(sectionIV, result, canhBao);

        if (sectionV != null && !sectionV.isBlank()) {
            result.phuongPhapDanhGia = cleanTextForDb(sectionV);
        }

        parseSectionVI(sectionVI, result, canhBao);

        return result;
    }

    private String normalizeText(String text) {
        if (text == null) {
            return "";
        }

        return text
                .replace("\\n", "\n")
                .replace("\\r", "\n")
                .replace("\r\n", "\n")
                .replace("\r", "\n")
                .replace('\u00A0', ' ')
                .replaceAll("[ \\t]+", " ")
                .replaceAll("\n{3,}", "\n\n")
                .trim();
    }

    private String extractSection(List<String> lines, String from, String to) {
        Pattern pFrom = sectionPattern(from);
        Pattern pTo = sectionPattern(to);

        boolean inSection = false;
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            if (!inSection && pFrom.matcher(line).matches()) {
                inSection = true;
                continue;
            }

            if (inSection && pTo.matcher(line).matches()) {
                break;
            }

            if (inSection) {
                sb.append(line).append("\n");
            }
        }

        return sb.toString().trim();
    }

    private String extractSectionToEnd(List<String> lines, String from) {
        Pattern pFrom = sectionPattern(from);

        boolean inSection = false;
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            if (!inSection && pFrom.matcher(line).matches()) {
                inSection = true;
                continue;
            }

            if (inSection) {
                sb.append(line).append("\n");
            }
        }

        return sb.toString().trim();
    }

    private Pattern sectionPattern(String roman) {
        return Pattern.compile("^" + roman + "\\..*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
    }

    private void parseViTriTinhChat(String sectionI, ParsedSyllabus result) {
        if (sectionI == null || sectionI.isBlank()) {
            return;
        }

        StringBuilder viTri = new StringBuilder();
        StringBuilder tinhChat = new StringBuilder();
        String current = null;

        for (String rawLine : sectionI.split("\n")) {
            String line = rawLine.trim();
            if (line.isEmpty()) {
                continue;
            }

            String lower = line.toLowerCase();

            if (lower.startsWith("- vị trí:") || lower.startsWith("-vị trí:")) {
                current = "viTri";
                String val = line.replaceFirst("(?iu)-\\s*vị\\s*trí\\s*:", "").trim();
                if (!val.isEmpty()) {
                    viTri.append(" ").append(val);
                }
            } else if (lower.startsWith("- tính chất:") || lower.startsWith("-tính chất:")) {
                current = "tinhChat";
                String val = line.replaceFirst("(?iu)-\\s*tính\\s*chất\\s*:", "").trim();
                if (!val.isEmpty()) {
                    tinhChat.append(" ").append(val);
                }
            } else if ("viTri".equals(current) && !line.startsWith("-")) {
                viTri.append(" ").append(line);
            } else if ("tinhChat".equals(current) && !line.startsWith("-")) {
                tinhChat.append(" ").append(line);
            } else if (line.startsWith("-") && "viTri".equals(current)) {
                current = null;
            }
        }

        if (viTri.length() > 0) {
            result.viTri = cleanTextForDb(viTri.toString());
        }

        if (tinhChat.length() > 0) {
            result.tinhChat = cleanTextForDb(tinhChat.toString());
        }
    }

    private void parseTableSection(String sectionIII, ParsedSyllabus result, List<String> canhBao) {
        if (sectionIII == null || sectionIII.isBlank()) {
            return;
        }

        String tableBlock = extractTableBlock(sectionIII);
        if (tableBlock == null || tableBlock.isBlank()) {
            tableBlock = sectionIII;
        }

        String detailBlock = extractDetailBlock(sectionIII);

        log.debug("[IMPORT-PARSER] tableBlock:\n{}", tableBlock);
        log.debug("[IMPORT-PARSER] detailBlock:\n{}", detailBlock);

        List<ParsedChuongBai> rows = parseTableBlock(tableBlock, canhBao);

        if (rows.isEmpty()) {
            addLoiNghiemTrong(canhBao, "Không tìm thấy danh sách chương/bài trong file.");
            return;
        }

        List<ParsedChiTiet> chiTietList = parseDetailBlock(detailBlock, canhBao);
        Map<String, ParsedChiTiet> chiTietTheoKey = taoMapChiTietTheoKey(chiTietList, canhBao);

        log.debug("[IMPORT-PARSER] rows: {}, chiTiet: {}", rows.size(), chiTietList.size());

        for (ParsedChuongBai cb : rows) {
            if (cb == null || isDongKhongCanChiTiet(cb.ten)) {
                continue;
            }

            String key = layKeyDauMuc(cb.ten);
            if (key == null) {
                canhBao.add("Không xác định được key chương/bài từ dòng: " + cb.maChuong + " - " + cb.ten + ". Dòng này vẫn được lưu nếu các kiểm tra khác hợp lệ.");
                continue;
            }

            ParsedChiTiet ct = chiTietTheoKey.get(key);
            if (ct == null) {
                canhBao.add("Không tìm thấy nội dung chi tiết khớp cho dòng " + cb.maChuong + " - " + cb.ten + ". Nếu đây là dòng cha/tự chọn/ôn tập thì có thể bỏ qua; nếu là chương/bài thật cần kiểm tra lại file.");
                continue;
            }

            cb.mucTieu = cleanTextForDb(ct.mucTieu);
            cb.noiDung = cleanTextForDb(ct.noiDung);
        }

        result.chuongBaiList = rows;
    }

    private List<ParsedChuongBai> parseTableBlock(String tableBlock, List<String> canhBao) {
        List<ParsedChuongBai> result = new ArrayList<>();

        if (tableBlock == null || tableBlock.isBlank()) {
            return result;
        }

        String[] rawLines = tableBlock.split("\n");

        String sttRegex = "(\\d+(?:\\.\\d+)*)";
        String numRegex = "(\\d+(?:[\\.,]\\d+)?)";

        Pattern fullRowPattern = Pattern.compile(
                "^\\s*" + sttRegex + "\\s+(.+?)\\s+" + numRegex + "\\s+" + numRegex + "\\s+" + numRegex + "\\s+" + numRegex + "\\s*$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Pattern sttWithTextPattern = Pattern.compile(
                "^\\s*" + sttRegex + "\\s+(.+)$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Pattern sttOnlyPattern = Pattern.compile("^\\s*" + sttRegex + "\\s*$");

        Pattern tailHoursPattern = Pattern.compile(
                "^(.+?)\\s+" + numRegex + "\\s+" + numRegex + "\\s+" + numRegex + "\\s+" + numRegex + "\\s*$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        String pendingStt = null;
        StringBuilder pendingText = new StringBuilder();

        for (String raw : rawLines) {
            String line = normalizeParseLine(raw);

            if (line.isBlank()) {
                continue;
            }

            String lower = chuanHoaKhongDau(line);

            if (isTableHeaderLine(lower) || isUiTokenLine(lower)) {
                continue;
            }

            if (isStopAfterTableLine(lower)) {
                flushPendingChuongBai(pendingStt, pendingText, result, canhBao, true);
                pendingStt = null;
                pendingText.setLength(0);
                break;
            }

            if (isCongLine(lower)) {
                flushPendingChuongBai(pendingStt, pendingText, result, canhBao, true);
                doiChieuDongCong(line, result, canhBao);
                pendingStt = null;
                pendingText.setLength(0);
                continue;
            }

            if (pendingStt != null) {
                String candidate = pendingText.length() == 0
                        ? line
                        : pendingText + " " + line;
                candidate = candidate.trim().replaceAll("\\s+", " ");

                Matcher tailMatcher = tailHoursPattern.matcher(candidate);
                if (tailMatcher.matches()) {
                    addParsedChuongBai(result, pendingStt, tailMatcher.group(1), tailMatcher.group(2), tailMatcher.group(3), tailMatcher.group(4), tailMatcher.group(5));
                    pendingStt = null;
                    pendingText.setLength(0);
                    continue;
                }

                if (isClearlyNewTableRowStart(line, fullRowPattern, sttOnlyPattern, sttWithTextPattern)) {
                    flushPendingChuongBai(pendingStt, pendingText, result, canhBao, true);
                    pendingStt = null;
                    pendingText.setLength(0);
                } else {
                    if (pendingText.length() > 0) {
                        pendingText.append(" ");
                    }
                    pendingText.append(line);
                    continue;
                }
            }

            Matcher fullMatcher = fullRowPattern.matcher(line);
            if (fullMatcher.matches()) {
                addParsedChuongBai(result, fullMatcher.group(1), fullMatcher.group(2), fullMatcher.group(3), fullMatcher.group(4), fullMatcher.group(5), fullMatcher.group(6));
                continue;
            }

            Matcher sttOnlyMatcher = sttOnlyPattern.matcher(line);
            if (sttOnlyMatcher.matches()) {
                pendingStt = sttOnlyMatcher.group(1);
                pendingText.setLength(0);
                continue;
            }

            Matcher sttWithTextMatcher = sttWithTextPattern.matcher(line);
            if (sttWithTextMatcher.matches()) {
                pendingStt = sttWithTextMatcher.group(1);
                pendingText.setLength(0);
                pendingText.append(sttWithTextMatcher.group(2).trim());

                String candidate = pendingText.toString().trim().replaceAll("\\s+", " ");
                Matcher tailMatcher = tailHoursPattern.matcher(candidate);

                if (tailMatcher.matches()) {
                    addParsedChuongBai(result, pendingStt, tailMatcher.group(1), tailMatcher.group(2), tailMatcher.group(3), tailMatcher.group(4), tailMatcher.group(5));
                    pendingStt = null;
                    pendingText.setLength(0);
                }

                continue;
            }

            Matcher noSttTailMatcher = tailHoursPattern.matcher(line);
            if (noSttTailMatcher.matches() && laTenChuongBai(noSttTailMatcher.group(1))) {
                String sttTam = String.valueOf(result.size() + 1);
                addParsedChuongBai(result, sttTam, noSttTailMatcher.group(1), noSttTailMatcher.group(2), noSttTailMatcher.group(3), noSttTailMatcher.group(4), noSttTailMatcher.group(5));
                addLoiNghiemTrong(canhBao, "Có dòng chương/bài thiếu STT nên hệ thống chỉ đọc tạm để hiển thị lỗi, không được lưu: " + line);
                continue;
            }

            if (looksLikeDataLine(line)) {
                canhBao.add("Không nhận diện được dòng trong bảng chương/bài, đã bỏ qua: " + line);
            }
        }

        flushPendingChuongBai(pendingStt, pendingText, result, canhBao, true);

        if (result.isEmpty()) {
            addLoiNghiemTrong(canhBao, "Không parse được bảng chương/bài. Cần kiểm tra lại định dạng bảng PDF/paste.");
        } else {
            canhBao.add("Đã parse được " + result.size() + " dòng chương/bài từ bảng phân bổ thời gian.");
        }

        return result;
    }

    private void flushPendingChuongBai(
            String pendingStt,
            StringBuilder pendingText,
            List<ParsedChuongBai> result,
            List<String> canhBao,
            boolean nghiemTrongNeuThieuGio
    ) {
        if (pendingStt == null || pendingStt.isBlank() || pendingText == null || pendingText.length() == 0) {
            return;
        }

        String text = pendingText.toString().trim().replaceAll("\\s+", " ");

        Pattern tailHoursPattern = Pattern.compile(
                "^(.+?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s*$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Matcher matcher = tailHoursPattern.matcher(text);

        if (matcher.matches()) {
            addParsedChuongBai(result, pendingStt, matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
        } else {
            String message = "Dòng chương/bài STT " + pendingStt + " chưa đủ 4 cột giờ nên không thể lưu: " + pendingStt + " " + text;
            if (nghiemTrongNeuThieuGio) {
                addLoiNghiemTrong(canhBao, message);
            } else {
                canhBao.add(message);
            }
        }
    }

    private String normalizeParseLine(String raw) {
        if (raw == null) {
            return "";
        }

        return raw
                .replace("\\n", " ")
                .replace("\\r", " ")
                .replace('\u00A0', ' ')
                .replace("\t", " ")
                .trim()
                .replaceAll("\\s+", " ");
    }

    private boolean isUiTokenLine(String lower) {
        if (lower == null) {
            return false;
        }

        String v = lower.trim();
        return v.equals("sửa")
                || v.equals("xóa")
                || v.equals("xoá")
                || v.equals("thêm")
                || v.equals("lưu")
                || v.equals("hủy")
                || v.equals("huỷ")
                || v.equals("-");
    }

    private boolean isClearlyNewTableRowStart(
            String line,
            Pattern fullRowPattern,
            Pattern sttOnlyPattern,
            Pattern sttWithTextPattern
    ) {
        if (line == null || line.isBlank()) {
            return false;
        }

        if (fullRowPattern.matcher(line).matches() || sttOnlyPattern.matcher(line).matches()) {
            return true;
        }

        Matcher matcher = sttWithTextPattern.matcher(line);
        if (!matcher.matches()) {
            return false;
        }

        String textAfterStt = matcher.group(2);
        return laTenChuongBai(textAfterStt);
    }

    private boolean looksLikeDataLine(String line) {
        if (line == null || line.isBlank()) {
            return false;
        }

        String lower = line.toLowerCase();
        return lower.matches(".*\\d.*")
                || lower.contains("chương")
                || lower.contains("bài")
                || lower.contains("kiểm tra")
                || lower.contains("mô đun")
                || lower.contains("module")
                || lower.contains("lab");
    }

    private void addLoiNghiemTrong(List<String> canhBao, String message) {
        if (canhBao == null) {
            return;
        }
        canhBao.add("[LỖI NGHIÊM TRỌNG] " + message);
    }

    private boolean coLoiNghiemTrong(List<String> canhBao, int startIndex) {
        if (canhBao == null || canhBao.isEmpty()) {
            return false;
        }

        int from = Math.max(0, startIndex);
        for (int i = from; i < canhBao.size(); i++) {
            String msg = canhBao.get(i);
            if (msg != null && msg.startsWith("[LỖI NGHIÊM TRỌNG]")) {
                return true;
            }
        }
        return false;
    }

    private boolean isKiemTraRow(String ten) {
        if (ten == null) {
            return false;
        }
        String lower = chuanHoaKhongDau(ten);
        return lower.contains("kiem tra");
    }

    private boolean isOnTapRow(String ten) {
        if (ten == null) {
            return false;
        }
        String lower = chuanHoaKhongDau(ten);
        return lower.contains("on tap") || lower.contains("consolidation");
    }

    private boolean isDongKhongCanChiTiet(String ten) {
        return isKiemTraRow(ten) || isOnTapRow(ten);
    }

    private String chuanHoaKhongDau(String value) {
        String normalized = chuanHoaTimKiem(value);
        return normalized == null ? "" : normalized;
    }

    private Integer laySoChuong(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        Matcher matcher = Pattern.compile("(?iu)\\bchương\\s+(\\d+)\\b").matcher(text.trim());
        if (!matcher.find()) {
            return null;
        }

        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String layKeyDauMuc(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        String raw = text.trim();
        String lower = chuanHoaKhongDau(raw);

        if (lower.startsWith("bai mo dau")) {
            return "BAI_MO_DAU";
        }

        Matcher matcher = Pattern.compile("(?iu)\\b(chương|bài|chuyên\\s*đề|phần|lab|module|mô\\s*đun|mô-đun)\\s+(\\d+)\\b")
                .matcher(raw);
        if (!matcher.find()) {
            return null;
        }

        String type = chuanHoaKhongDau(matcher.group(1)).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);
        String number = matcher.group(2);
        if (type.startsWith("CHUONG")) {
            type = "CHUONG";
        } else if (type.startsWith("BAI")) {
            type = "BAI";
        } else if (type.startsWith("CHUYEN")) {
            type = "CHUYEN_DE";
        } else if (type.startsWith("MO")) {
            type = "MO_DUN";
        }
        return type + ":" + number;
    }

    private Integer laySoTheoLoai(String text, String loaiKhongDau) {
        if (text == null || loaiKhongDau == null) {
            return null;
        }
        String lower = chuanHoaKhongDau(text);
        Matcher matcher = Pattern.compile("\\b" + Pattern.quote(loaiKhongDau) + "\\s+(\\d+)\\b").matcher(lower);
        if (!matcher.find()) {
            return null;
        }
        try {
            return Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Map<String, ParsedChiTiet> taoMapChiTietTheoKey(List<ParsedChiTiet> chiTietList, List<String> canhBao) {
        Map<String, ParsedChiTiet> map = new LinkedHashMap<>();
        if (chiTietList == null || chiTietList.isEmpty()) {
            return map;
        }

        for (ParsedChiTiet ct : chiTietList) {
            if (ct == null || ct.key == null || ct.key.isBlank()) {
                continue;
            }

            if (map.containsKey(ct.key)) {
                addLoiNghiemTrong(canhBao, "Nội dung chi tiết bị trùng key " + ct.key + ".");
                continue;
            }

            map.put(ct.key, ct);
        }

        return map;
    }

    private void validateParsedSyllabus(ParsedSyllabus parsed, List<String> canhBao) {
        if (parsed == null) {
            addLoiNghiemTrong(canhBao, "Dữ liệu parse rỗng.");
            return;
        }

        List<ParsedChuongBai> rows = parsed.chuongBaiList;
        if (rows == null || rows.isEmpty()) {
            addLoiNghiemTrong(canhBao, "Không có dòng chương/bài nào để lưu.");
            return;
        }

        validateThuTuLienTuc(rows, canhBao);
        validateSoChuongLienTuc(rows, canhBao);
        validateGioTungDong(rows, canhBao);
        validateNoiDungChuong(rows, canhBao);
    }

    private void validateThuTuLienTuc(List<ParsedChuongBai> rows, List<String> canhBao) {
        Set<String> seen = new HashSet<>();
        Set<Integer> topLevel = new TreeSet<>();

        for (int i = 0; i < rows.size(); i++) {
            ParsedChuongBai row = rows.get(i);
            if (row == null) {
                continue;
            }

            if (row.maChuong == null || row.maChuong.isBlank()) {
                addLoiNghiemTrong(canhBao, "Dòng " + (i + 1) + " thiếu STT/maChuong.");
                continue;
            }

            String stt = row.maChuong.trim();
            if (!seen.add(stt)) {
                addLoiNghiemTrong(canhBao, "STT bị trùng: " + stt + ".");
            }

            if (!stt.matches("\\d+(?:\\.\\d+)*")) {
                addLoiNghiemTrong(canhBao, "STT không hợp lệ: " + stt + ".");
                continue;
            }

            if (stt.matches("\\d+")) {
                topLevel.add(Integer.parseInt(stt));
            } else {
                String parent = stt.substring(0, stt.lastIndexOf('.'));
                boolean parentExists = rows.stream().anyMatch(r -> r != null && parent.equals(r.maChuong));
                if (!parentExists) {
                    canhBao.add("STT " + stt + " là dòng con nhưng không thấy dòng cha " + parent + ". Vui lòng kiểm tra cấu trúc bảng.");
                }
            }
        }

        if (!topLevel.isEmpty()) {
            int max = topLevel.stream().mapToInt(Integer::intValue).max().orElse(0);
            for (int i = 1; i <= max; i++) {
                if (!topLevel.contains(i)) {
                    addLoiNghiemTrong(canhBao, "Thiếu STT cấp chính " + i + ". Dữ liệu có thể bị thiếu dòng hoặc lệch cột.");
                }
            }
        }
    }

    private void validateSoChuongLienTuc(List<ParsedChuongBai> rows, List<String> canhBao) {
        validateSoTheoLoaiLienTuc(rows, canhBao, "chuong", "Chương");
        validateSoTheoLoaiLienTuc(rows, canhBao, "bai", "Bài");
    }

    private void validateSoTheoLoaiLienTuc(List<ParsedChuongBai> rows, List<String> canhBao, String loaiKhongDau, String tenLoai) {
        Set<Integer> values = new TreeSet<>();

        for (ParsedChuongBai row : rows) {
            if (row == null || isDongKhongCanChiTiet(row.ten)) {
                continue;
            }

            Integer so = laySoTheoLoai(row.ten, loaiKhongDau);
            if (so != null) {
                values.add(so);
            }
        }

        if (values.isEmpty()) {
            return;
        }

        int max = values.stream().mapToInt(Integer::intValue).max().orElse(0);
        for (int i = 1; i <= max; i++) {
            if (!values.contains(i)) {
                addLoiNghiemTrong(canhBao, "Thiếu " + tenLoai + " " + i + " trong bảng tổng quát. Dữ liệu có thể đã paste thiếu dòng.");
            }
        }
    }

    private void validateGioTungDong(List<ParsedChuongBai> rows, List<String> canhBao) {
        for (ParsedChuongBai row : rows) {
            if (row == null) {
                continue;
            }

            BigDecimal tong = nullToZero(row.tongGio);
            BigDecimal lt = nullToZero(row.gioLyThuyet);
            BigDecimal th = nullToZero(row.gioThucHanh);
            BigDecimal kt = nullToZero(row.gioKiemTra);
            BigDecimal sum = lt.add(th).add(kt);

            if (tong.compareTo(sum) != 0) {
                addLoiNghiemTrong(canhBao, "Dòng STT " + row.thuTu + " tổng giờ sai: Tổng=" + tong + " nhưng LT+TH+KT=" + sum + ".");
            }

            if (tong.compareTo(BigDecimal.ZERO) < 0 || lt.compareTo(BigDecimal.ZERO) < 0
                    || th.compareTo(BigDecimal.ZERO) < 0 || kt.compareTo(BigDecimal.ZERO) < 0) {
                addLoiNghiemTrong(canhBao, "Dòng STT " + row.thuTu + " có số giờ âm.");
            }

            if (isKiemTraRow(row.ten) && kt.compareTo(BigDecimal.ZERO) <= 0) {
                canhBao.add("Dòng STT " + row.thuTu + " là Kiểm tra nhưng giờ kiểm tra đang bằng 0. Vui lòng kiểm tra lại.");
            }
        }
    }

    private void validateNoiDungChuong(List<ParsedChuongBai> rows, List<String> canhBao) {
        for (int i = 0; i < rows.size(); i++) {
            ParsedChuongBai row = rows.get(i);
            if (row == null || isDongKhongCanChiTiet(row.ten)) {
                continue;
            }

            if (row.ten == null || row.ten.isBlank()) {
                addLoiNghiemTrong(canhBao, "Dòng STT " + row.maChuong + " thiếu tên chương/bài.");
                continue;
            }

            if (laDongChaCoDongCon(rows, i)) {
                continue;
            }

            String key = layKeyDauMuc(row.ten);
            if (key != null) {
                boolean thieuMucTieu = row.mucTieu == null || row.mucTieu.isBlank();
                boolean thieuNoiDung = row.noiDung == null || row.noiDung.isBlank();

                if (thieuMucTieu && thieuNoiDung) {
                    canhBao.add("Dòng " + row.maChuong + " - " + row.ten + " chưa ghép được mục tiêu/nội dung chi tiết. Nếu đây là dòng tổng hợp/nhóm thì có thể bỏ qua; nếu là bài/chương thật cần kiểm tra file.");
                }
            }
        }
    }

    private boolean laDongChaCoDongCon(List<ParsedChuongBai> rows, int index) {
        if (rows == null || index < 0 || index >= rows.size()) {
            return false;
        }
        ParsedChuongBai row = rows.get(index);
        if (row == null || row.maChuong == null || row.maChuong.isBlank()) {
            return false;
        }
        String prefix = row.maChuong.trim() + ".";
        for (ParsedChuongBai other : rows) {
            if (other != null && other.maChuong != null && other.maChuong.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTableHeaderLine(String lower) {
        if (lower == null) {
            return false;
        }

        String v = lower.trim();

        return v.contains("so tt")
                || v.contains("ten chuong")
                || v.contains("ten bai")
                || v.contains("ten muc")
                || v.contains("ten mon hoc")
                || v.contains("ten mo dun")
                || v.contains("ma mh")
                || v.contains("ma md")
                || v.contains("ma mon")
                || v.contains("thoi gian")
                || v.equals("tong so")
                || v.equals("ly thuyet")
                || v.equals("thuc hanh")
                || v.equals("thao luan")
                || v.equals("bai tap")
                || v.equals("kiem tra")
                || v.equals("thi kiem tra")
                || v.equals("lt")
                || v.equals("th")
                || v.equals("kt")
                || v.equals("trong do");
    }

    private boolean isCongLine(String lower) {
        if (lower == null) {
            return false;
        }

        String v = lower.trim();

        return v.startsWith("cộng")
                || v.startsWith("cong")
                || v.startsWith("tổng cộng")
                || v.startsWith("tong cong");
    }

    private void addParsedChuongBai(
            List<ParsedChuongBai> result,
            String thuTuRaw,
            String ten,
            String tongGio,
            String gioLyThuyet,
            String gioThucHanh,
            String gioKiemTra
    ) {
        if (result == null) {
            return;
        }
        ParsedChuongBai row = taoParsedChuongBai(result.size() + 1, thuTuRaw, ten, tongGio, gioLyThuyet, gioThucHanh, gioKiemTra);
        result.add(row);
    }

    private boolean isStopAfterTableLine(String lower) {
        if (lower == null) {
            return false;
        }
        String v = lower.trim();
        return v.startsWith("2 noi dung chi tiet")
                || v.startsWith("iv dieu kien")
                || v.startsWith("v noi dung")
                || v.startsWith("v phuong phap")
                || v.startsWith("vi huong dan");
    }

    private ParsedChuongBai taoParsedChuongBai(
            Integer thuTu,
            String thuTuRaw,
            String ten,
            String tongGio,
            String gioLyThuyet,
            String gioThucHanh,
            String gioKiemTra
    ) {
        ParsedChuongBai row = new ParsedChuongBai();
        row.thuTu = thuTu != null ? thuTu : 0;
        row.maChuong = thuTuRaw != null && !thuTuRaw.isBlank() ? cleanTextForDb(thuTuRaw) : String.valueOf(row.thuTu);
        row.ten = cleanTextForDb(normalizeTen(ten));
        row.tongGio = toBigDecimalSafe(tongGio);
        row.gioLyThuyet = toBigDecimalSafe(gioLyThuyet);
        row.gioThucHanh = toBigDecimalSafe(gioThucHanh);
        row.gioKiemTra = toBigDecimalSafe(gioKiemTra);
        return row;
    }

    private void doiChieuDongCong(String line, List<ParsedChuongBai> rows, List<String> canhBao) {
        if (line == null || rows == null || rows.isEmpty()) {
            return;
        }

        String normalized = line.trim().replaceAll("\\s+", " ");

        Pattern congPattern = Pattern.compile(
                "^(?:Cộng|Cong|Tổng cộng|Tong cong)\\s+(\\d+(?:[\\.,]\\d+)?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s+(\\d+(?:[\\.,]\\d+)?)\\s*$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Matcher matcher = congPattern.matcher(normalized);
        if (!matcher.matches()) {
            return;
        }

        BigDecimal tongCong = toBigDecimalSafe(matcher.group(1));
        BigDecimal ltCong = toBigDecimalSafe(matcher.group(2));
        BigDecimal thCong = toBigDecimalSafe(matcher.group(3));
        BigDecimal ktCong = toBigDecimalSafe(matcher.group(4));

        BigDecimal tong = BigDecimal.ZERO;
        BigDecimal lt = BigDecimal.ZERO;
        BigDecimal th = BigDecimal.ZERO;
        BigDecimal kt = BigDecimal.ZERO;

        for (ParsedChuongBai row : rows) {
            tong = tong.add(nullToZero(row.tongGio));
            lt = lt.add(nullToZero(row.gioLyThuyet));
            th = th.add(nullToZero(row.gioThucHanh));
            kt = kt.add(nullToZero(row.gioKiemTra));
        }

        if (tong.compareTo(tongCong) != 0
                || lt.compareTo(ltCong) != 0
                || th.compareTo(thCong) != 0
                || kt.compareTo(ktCong) != 0) {
            canhBao.add("Tổng dòng Cộng trong file khác tổng các dòng chương/bài đã parse. File: "
                    + tongCong + "/" + ltCong + "/" + thCong + "/" + ktCong
                    + ", parse: "
                    + tong + "/" + lt + "/" + th + "/" + kt
                    + ". Vui lòng kiểm tra lại bảng giờ.");
        }
    }

    private boolean laTenChuongBai(String value) {
        if (value == null) {
            return false;
        }

        String lower = chuanHoaKhongDau(value);

        return lower.startsWith("chuong")
                || lower.startsWith("bai")
                || lower.startsWith("bai mo dau")
                || lower.startsWith("lab")
                || lower.startsWith("mo dun")
                || lower.startsWith("mo-dun")
                || lower.startsWith("module")
                || lower.startsWith("phan")
                || lower.startsWith("chuyen de")
                || lower.startsWith("kiem tra")
                || lower.startsWith("on tap")
                || lower.contains("consolidation");
    }

    private BigDecimal toBigDecimalSafe(String value) {
        if (value == null || value.isBlank()) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(value.trim().replace(",", "."));
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal nullToZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private String extractTableBlock(String sectionIII) {
        Pattern p1 = Pattern.compile(".*nội dung tổng quát.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Pattern p2 = Pattern.compile("^2\\.\\s*Nội\\s*dung\\s*chi\\s*tiết.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        String[] lines = sectionIII.split("\n");
        boolean started = false;
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();

            if (!started && p1.matcher(trimmed).matches()) {
                started = true;
                continue;
            }

            if (started && p2.matcher(trimmed).matches()) {
                break;
            }

            if (started) {
                sb.append(trimmed).append("\n");
            }
        }

        String result = sb.toString().trim();
        return result.isBlank() ? null : result;
    }

    private String extractDetailBlock(String sectionIII) {
        Pattern p2 = Pattern.compile("^2\\.\\s*Nội\\s*dung\\s*chi\\s*tiết.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        String[] lines = sectionIII.split("\n");
        boolean started = false;
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            String trimmed = line.trim();

            if (!started && p2.matcher(trimmed).matches()) {
                started = true;
                continue;
            }

            if (started) {
                sb.append(trimmed).append("\n");
            }
        }

        String result = sb.toString().trim();
        return result.isBlank() ? null : result;
    }

    private List<ParsedChiTiet> parseDetailBlock(String detailBlock, List<String> canhBao) {
        List<ParsedChiTiet> list = new ArrayList<>();
        if (detailBlock == null || detailBlock.isBlank()) {
            return list;
        }

        Pattern pHeader = Pattern.compile(
                "^(?:Chương|Bài|Chuyên\\s*đề|Phần|Lab|Module|Mô\\s*đun|Mô-đun)\\s+\\d+\\s*[:\\-–].*$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Pattern pHeaderSpecial = Pattern.compile(
                "^BÀI\\s+MỞ\\s+ĐẦU.*$",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        Pattern pThoiGian = Pattern.compile(
                "Thời\\s+gian\\s*:\\s*(\\d+(?:[\\.,]\\d+)?)\\s*(?:giờ)?",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE
        );

        ParsedChiTiet current = null;
        String phase = null;

        for (String raw : detailBlock.split("\n")) {
            String line = normalizeParseLine(raw);
            if (line.isEmpty() || isUiTokenLine(chuanHoaKhongDau(line))) {
                continue;
            }

            if (pHeader.matcher(line).matches() || pHeaderSpecial.matcher(line).matches()) {
                if (current != null) {
                    trimParsedChiTiet(current);
                    list.add(current);
                }

                current = new ParsedChiTiet();
                current.key = layKeyDauMuc(line);
                current.soChuong = laySoChuong(line);
                current.ten = normalizeChiTietTen(line);

                Matcher headerTimeMatcher = pThoiGian.matcher(line);
                if (headerTimeMatcher.find()) {
                    current.tongGio = toBigDecimalSafe(headerTimeMatcher.group(1));
                    current.ten = current.ten
                            .replaceAll("(?iu)\\s*Thời\\s+gian\\s*:\\s*\\d+(?:[\\.,]\\d+)?\\s*(?:giờ)?.*$", "")
                            .trim();
                }

                phase = null;
                continue;
            }

            if (current == null) {
                continue;
            }

            Matcher mTG = pThoiGian.matcher(line);
            if (mTG.find()) {
                current.tongGio = toBigDecimalSafe(mTG.group(1));
                continue;
            }

            if (isMucTieuHeaderLine(line)) {
                phase = "muctieu";
                String rest = layNoiDungSauTieuDeSo(line, "muc_tieu");
                if (!rest.isBlank()) {
                    current.mucTieu = appendOutlineLine(current.mucTieu, rest);
                }
                continue;
            }

            if (isNoiDungHeaderLine(line)) {
                phase = "noidung";
                String rest = layNoiDungSauTieuDeSo(line, "noi_dung");
                if (!rest.isBlank()) {
                    current.noiDung = appendOutlineLine(current.noiDung, rest);
                }
                continue;
            }

            if (isStopDetailSubSection(line)) {
                phase = null;
                continue;
            }

            if ("muctieu".equals(phase)) {
                current.mucTieu = appendOutlineLine(current.mucTieu, line);
            } else if ("noidung".equals(phase)) {
                current.noiDung = appendOutlineLine(current.noiDung, line);
            }
        }

        if (current != null) {
            trimParsedChiTiet(current);
            list.add(current);
        }

        if (list.isEmpty()) {
            canhBao.add("Không parse được phần nội dung chi tiết chương/bài.");
        }

        return list;
    }

    private boolean isMucTieuHeaderLine(String line) {
        if (line == null) {
            return false;
        }
        String lower = chuanHoaKhongDau(line);
        return lower.matches("^(?:1|i)\\s+muc\\s+tieu(?:\\s+.*)?$");
    }

    private boolean isNoiDungHeaderLine(String line) {
        if (line == null) {
            return false;
        }
        String lower = chuanHoaKhongDau(line);
        return lower.matches("^(?:2|ii)\\s+noi\\s+dung(?:\\s+.*)?$");
    }

    private boolean isStopDetailSubSection(String line) {
        if (line == null) {
            return false;
        }
        String lower = chuanHoaKhongDau(line);
        return lower.matches("^3\\s+.*$")
                || lower.startsWith("iv dieu kien")
                || lower.startsWith("v phuong phap")
                || lower.startsWith("v noi dung")
                || lower.startsWith("vi huong dan");
    }

    private String layNoiDungSauTieuDeSo(String line, String loai) {
        if (line == null || line.isBlank()) {
            return "";
        }

        String cleaned = line.trim();
        String before = cleaned;
        String labelRegex = "muc_tieu".equals(loai)
                ? "(?:mục\\s*tiêu|muc\\s*tieu)"
                : "(?:nội\\s*dung|noi\\s*dung)";

        cleaned = cleaned.replaceFirst(
                "(?iu)^\\s*(?:[0-9]+|[IVX]+)\\s*\\.\\s*" + labelRegex + "\\s*[:\\-–]?\\s*",
                ""
        ).trim();

        return cleaned.equals(before) ? "" : cleaned;
    }

    private String appendOutlineLine(String current, String line) {
        if (line == null) {
            return current;
        }

        String cleaned = cleanTextForDb(line);
        if (cleaned.isBlank()) {
            return current;
        }

        if (current == null || current.isBlank()) {
            return cleaned;
        }

        return cleanTextForDb(current + " " + cleaned);
    }

    private void trimParsedChiTiet(ParsedChiTiet ct) {
        if (ct == null) {
            return;
        }
        if (ct.mucTieu != null) {
            ct.mucTieu = cleanTextForDb(ct.mucTieu);
        }
        if (ct.noiDung != null) {
            ct.noiDung = cleanTextForDb(ct.noiDung);
        }
        if (ct.ten != null) {
            ct.ten = cleanTextForDb(ct.ten);
        }
    }

    private String normalizeChiTietTen(String headerLine) {
        if (headerLine == null) {
            return "";
        }

        return cleanTextForDb(headerLine
                .replaceFirst("(?iu)(?:Chương|Bài|Phần|Lab|Module|Mô\\s*đun|Mô-đun)\\s+\\d+\\s*[:\\-–]\\s*", "")
                .replaceFirst("(?iu)BÀI\\s+MỞ\\s+ĐẦU\\s*[:\\-–]?\\s*", "Bài mở đầu "));
    }

    private String extractAfterColon(String line) {
        int idx = line.indexOf(':');
        return (idx >= 0 && idx < line.length() - 1) ? line.substring(idx + 1).trim() : "";
    }

    private void parseDieuKien(String sectionIV, ParsedSyllabus result, List<String> canhBao) {
        if (sectionIV == null || sectionIV.isBlank()) {
            return;
        }

        result.dieuKienList = new ArrayList<>();
        String[] lines = sectionIV.split("\n");

        LoaiDieuKienMonHoc currentLoai = null;
        StringBuilder currentContent = new StringBuilder();

        Pattern pPhong = Pattern.compile("^1\\..*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Pattern pThietBi = Pattern.compile("^2\\..*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        Pattern pHocLieu = Pattern.compile("^3\\..*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (pPhong.matcher(line).matches()) {
                flushDieuKien(result.dieuKienList, currentLoai, currentContent);
                currentLoai = LoaiDieuKienMonHoc.PHONG_HOC;
                currentContent = new StringBuilder(extractAfterDot(line));
            } else if (pThietBi.matcher(line).matches()) {
                flushDieuKien(result.dieuKienList, currentLoai, currentContent);
                currentLoai = LoaiDieuKienMonHoc.THIET_BI;
                currentContent = new StringBuilder(extractAfterDot(line));
            } else if (pHocLieu.matcher(line).matches()) {
                flushDieuKien(result.dieuKienList, currentLoai, currentContent);
                currentLoai = LoaiDieuKienMonHoc.HOC_LIEU;
                currentContent = new StringBuilder(extractAfterDot(line));
            } else if (currentLoai != null) {
                if (currentContent.length() > 0) {
                    currentContent.append(" ");
                }
                currentContent.append(line);
            }
        }

        flushDieuKien(result.dieuKienList, currentLoai, currentContent);

        if (result.dieuKienList.isEmpty()) {
            canhBao.add("Không parse được điều kiện thực hiện từ file.");
        }
    }

    private void flushDieuKien(List<ParsedDieuKien> list, LoaiDieuKienMonHoc loai, StringBuilder content) {
        if (loai == null || content == null || content.length() == 0) {
            return;
        }

        String noiDung = cleanTextForDb(content.toString());

        if (!noiDung.isBlank()) {
            ParsedDieuKien dk = new ParsedDieuKien();
            dk.loai = loai;
            dk.noiDung = noiDung;
            list.add(dk);
        }
    }

    private void parseSectionVI(String sectionVI, ParsedSyllabus result, List<String> canhBao) {
        if (sectionVI == null || sectionVI.isBlank()) {
            return;
        }

        Pattern pTaiLieu = Pattern.compile(".*tài liệu tham khảo.*", Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
        String[] lines = sectionVI.split("\n");

        boolean inTaiLieu = false;
        StringBuilder huongDanBuf = new StringBuilder();
        List<ParsedTaiLieu> taiLieuList = new ArrayList<>();
        int thuTu = 1;

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            if (pTaiLieu.matcher(line).matches()) {
                inTaiLieu = true;
                continue;
            }

            if (inTaiLieu && line.startsWith("-")) {
                String content = line.substring(1).trim();

                if (!content.isBlank()) {
                    taiLieuList.add(parseTaiLieuLine(content, thuTu++));
                }
            } else if (!inTaiLieu) {
                if (huongDanBuf.length() > 0) {
                    huongDanBuf.append(" ");
                }
                huongDanBuf.append(line);
            }
        }

        if (huongDanBuf.length() > 0) {
            result.huongDan = cleanTextForDb(huongDanBuf.toString());
        }

        result.taiLieuList = taiLieuList;

        if (taiLieuList.isEmpty()) {
            canhBao.add("Không parse được tài liệu tham khảo từ file.");
        }
    }

    private ParsedTaiLieu parseTaiLieuLine(String line, int thuTu) {
        ParsedTaiLieu tl = new ParsedTaiLieu();
        tl.thuTu = thuTu;
        tl.loai = "tai_lieu_tham_khao";

        String cleanedLine = cleanTextForDb(line);
        Matcher yearMatcher = Pattern.compile(",?\\s*(\\d{4})\\.?\\s*$").matcher(cleanedLine);

        if (yearMatcher.find()) {
            try {
                tl.namXuatBan = Integer.parseInt(yearMatcher.group(1));
            } catch (NumberFormatException ignored) {
                // ignore
            }

            cleanedLine = cleanedLine.substring(0, yearMatcher.start()).trim();
        }

        String[] parts = cleanedLine.split(",", 2);
        tl.ten = cleanTextForDb(parts[0]);

        if (parts.length > 1) {
            tl.nhaXuatBan = cleanTextForDb(parts[1]);
        }

        return tl;
    }

    private String extractAfterDot(String line) {
        int colonIdx = line.indexOf(':');
        if (colonIdx >= 0 && colonIdx < line.length() - 1) {
            return line.substring(colonIdx + 1).trim();
        }

        int dotIdx = line.indexOf('.');
        if (dotIdx >= 0 && dotIdx < line.length() - 1) {
            return line.substring(dotIdx + 1).trim();
        }

        return line.trim();
    }

    private String normalizeTen(String ten) {
        if (ten == null) {
            return "";
        }

        return cleanTextForDb(ten)
                .replaceAll("\\s{2,}", " ")
                .replaceAll("\\s+:", ":")
                .trim();
    }

    private String cleanTextForDb(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("\\n", " ")
                .replace("\\r", " ")
                .replace("\r\n", " ")
                .replace("\n", " ")
                .replace("\r", " ")
                .replace("\t", " ")
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }



    // ==================== Syllabus tối thiểu ====================

    private SyllabusMonHocMau taoChuoiSyllabusToiThieu(MonHoc monHoc) {
        String maMon = monHoc.getMaMon() != null ? monHoc.getMaMon().trim() : "";
        String tenMon = monHoc.getTenMon() != null ? monHoc.getTenMon().trim() : "";
        Long monHocId = monHoc.getId();

        String maBase = maMon.isEmpty() ? "SYL-MH" + monHocId : "SYL-" + maMon;
        String ma = maBase;

        if (syllabusRepository.existsByMa(ma)) {
            ma = maBase + "-" + System.currentTimeMillis() % 100000;
        }

        String tenBase = tenMon.isEmpty() ? "Syllabus mẫu môn " + monHocId : "Syllabus mẫu - " + tenMon;
        String ten = tenBase;

        if (syllabusRepository.existsByMonHocIdAndTen(monHocId, ten)) {
            ten = tenBase + " (" + System.currentTimeMillis() % 100000 + ")";
        }

        return syllabusRepository.save(SyllabusMonHocMau.builder()
                .monHocId(monHocId)
                .ma(ma)
                .ten(ten)
                .soBuoiHoc(1)
                .soTietMoiBuoi(BigDecimal.ONE)
                .soPhutMotTiet(45)
                .build());
    }

    // ==================== Parsed data classes ====================

    static class ParsedSyllabus {
        String ma;
        String ten;
        String viTri;
        String tinhChat;
        String mucTieu;
        String phuongPhapDanhGia;
        String huongDan;
        List<ParsedChuongBai> chuongBaiList = new ArrayList<>();
        List<ParsedDieuKien> dieuKienList = new ArrayList<>();
        List<ParsedTaiLieu> taiLieuList = new ArrayList<>();
    }

    static class ParsedChuongBai {
        int thuTu;
        String maChuong;
        String ten;
        BigDecimal tongGio = BigDecimal.ZERO;
        BigDecimal gioLyThuyet = BigDecimal.ZERO;
        BigDecimal gioThucHanh = BigDecimal.ZERO;
        BigDecimal gioKiemTra = BigDecimal.ZERO;
        String mucTieu;
        String noiDung;
        String ghiChu;
    }

    static class ParsedChiTiet {
        Integer soChuong;
        String key;
        String ten;
        BigDecimal tongGio = BigDecimal.ZERO;
        String mucTieu;
        String noiDung;
        String ghiChu;
    }

    static class ParsedDieuKien {
        LoaiDieuKienMonHoc loai;
        String noiDung;
    }

    static class ParsedTaiLieu {
        int thuTu;
        String maChuong;
        String ten;
        String loai;
        String tacGia;
        String nhaXuatBan;
        Integer namXuatBan;
    }
}