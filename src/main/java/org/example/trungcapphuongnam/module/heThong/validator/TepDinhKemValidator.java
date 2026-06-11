package org.example.trungcapphuongnam.module.heThong.validator;

import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNoiDungTep;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Set;

@Component
public class TepDinhKemValidator {

    @Value("${app.upload.max-image-size:5242880}")
    private long maxImageSize;

    @Value("${app.upload.max-document-size:26214400}")
    private long maxDocumentSize;

    @Value("${app.upload.max-video-size:104857600}")
    private long maxVideoSize;

    @Value("${app.upload.max-audio-size:52428800}")
    private long maxAudioSize;

    @Value("${app.upload.max-other-size:10485760}")
    private long maxOtherSize;
    @Value("${app.upload.sinh-vien.max-file-size:33554432}")
    private long maxSinhVienFileSize;
    private static final Set<String> IMAGE_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".webp"
    );

    private static final Set<String> DOCUMENT_EXTENSIONS = Set.of(
            ".pdf", ".doc", ".docx", ".xls", ".xlsx", ".ppt", ".pptx", ".txt", ".csv"
    );

    private static final Set<String> VIDEO_EXTENSIONS = Set.of(
            ".mp4", ".mov", ".avi", ".mkv", ".webm"
    );

    private static final Set<String> AUDIO_EXTENSIONS = Set.of(
            ".mp3", ".wav", ".m4a", ".ogg"
    );

    public ThongTinTep validateUpload(MultipartFile file, TepDinhKemRequest request) {
        validateRequest(request);
        validateFileCoBan(file);

        String tenMau = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());

        if (tenMau.contains("..") || tenMau.contains("/") || tenMau.contains("\\")) {
            throw new HeThongException("Tên file không hợp lệ");
        }

        String extension = layExtension(tenMau);
        if (extension.isBlank()) {
            throw new HeThongException("File phải có phần mở rộng");
        }

        LoaiNoiDungTep loaiNoiDung = phanLoaiNoiDung(file.getContentType(), extension);
        validateDungLuongTheoNguoiGui(file, request);
        validateDungLuongTheoLoai(file, loaiNoiDung);
        validateLoaiFileChoPhep(extension, loaiNoiDung);

        return new ThongTinTep(tenMau, extension, loaiNoiDung);
    }

    public void validateLayTheoDoiTuong(String module, String nghiepVu, Long doiTuongId) {
        requireText(module, "Module");
        requireText(nghiepVu, "Nghiệp vụ");
        requireId(doiTuongId, "Đối tượng nghiệp vụ");
    }

    public void validateLayTheoNguoiGui(Object nguoiGuiLoai, Long nguoiGuiId) {
        if (nguoiGuiLoai == null) {
            throw new HeThongException("Loại người gửi không được để trống");
        }
        requireId(nguoiGuiId, "Người gửi");
    }

    private void validateFileCoBan(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new HeThongException("File không được để trống");
        }

        if (file.getSize() <= 0) {
            throw new HeThongException("File không hợp lệ");
        }
    }

    private void validateRequest(TepDinhKemRequest request) {
        if (request == null) {
            throw new HeThongException("Thông tin upload không hợp lệ");
        }

        if (request.getNguoiGuiLoai() == null) {
            throw new HeThongException("Loại người gửi không được để trống");
        }

        requireText(request.getModule(), "Module");
        requireText(request.getNghiepVu(), "Nghiệp vụ");
        requireId(request.getDoiTuongId(), "Đối tượng nghiệp vụ");
    }

    private void validateDungLuongTheoLoai(MultipartFile file, LoaiNoiDungTep loaiNoiDung) {
        long dungLuong = file.getSize();

        if (loaiNoiDung == LoaiNoiDungTep.HINH_ANH && dungLuong > maxImageSize) {
            throw new HeThongException("Hình ảnh vượt quá dung lượng cho phép. Tối đa " + formatDungLuong(maxImageSize));
        }

        if (loaiNoiDung == LoaiNoiDungTep.TAI_LIEU && dungLuong > maxDocumentSize) {
            throw new HeThongException("Tài liệu vượt quá dung lượng cho phép. Tối đa " + formatDungLuong(maxDocumentSize));
        }

        if (loaiNoiDung == LoaiNoiDungTep.VIDEO && dungLuong > maxVideoSize) {
            throw new HeThongException("Video vượt quá dung lượng cho phép. Tối đa " + formatDungLuong(maxVideoSize));
        }

        if (loaiNoiDung == LoaiNoiDungTep.AM_THANH && dungLuong > maxAudioSize) {
            throw new HeThongException("Âm thanh vượt quá dung lượng cho phép. Tối đa " + formatDungLuong(maxAudioSize));
        }

        if (loaiNoiDung == LoaiNoiDungTep.FILE_KHAC && dungLuong > maxOtherSize) {
            throw new HeThongException("File vượt quá dung lượng cho phép. Tối đa " + formatDungLuong(maxOtherSize));
        }
    }

    private void validateLoaiFileChoPhep(String extension, LoaiNoiDungTep loaiNoiDung) {
        if (loaiNoiDung == LoaiNoiDungTep.HINH_ANH && !IMAGE_EXTENSIONS.contains(extension)) {
            throw new HeThongException("Chỉ cho phép hình ảnh jpg, jpeg, png, webp");
        }

        if (loaiNoiDung == LoaiNoiDungTep.TAI_LIEU && !DOCUMENT_EXTENSIONS.contains(extension)) {
            throw new HeThongException("Chỉ cho phép tài liệu pdf, doc, docx, xls, xlsx, ppt, pptx, txt, csv");
        }

        if (loaiNoiDung == LoaiNoiDungTep.VIDEO && !VIDEO_EXTENSIONS.contains(extension)) {
            throw new HeThongException("Chỉ cho phép video mp4, mov, avi, mkv, webm");
        }

        if (loaiNoiDung == LoaiNoiDungTep.AM_THANH && !AUDIO_EXTENSIONS.contains(extension)) {
            throw new HeThongException("Chỉ cho phép âm thanh mp3, wav, m4a, ogg");
        }
    }

    private LoaiNoiDungTep phanLoaiNoiDung(String contentType, String extension) {
        String type = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);

        if (type.startsWith("image/") || IMAGE_EXTENSIONS.contains(extension)) {
            return LoaiNoiDungTep.HINH_ANH;
        }

        if (type.startsWith("video/") || VIDEO_EXTENSIONS.contains(extension)) {
            return LoaiNoiDungTep.VIDEO;
        }

        if (type.startsWith("audio/") || AUDIO_EXTENSIONS.contains(extension)) {
            return LoaiNoiDungTep.AM_THANH;
        }

        if (DOCUMENT_EXTENSIONS.contains(extension)) {
            return LoaiNoiDungTep.TAI_LIEU;
        }

        return LoaiNoiDungTep.FILE_KHAC;
    }

    private String layExtension(String tenFile) {
        int index = tenFile.lastIndexOf(".");
        if (index < 0) {
            return "";
        }

        return tenFile.substring(index).toLowerCase(Locale.ROOT);
    }

    private void requireText(String value, String ten) {
        if (value == null || value.trim().isEmpty()) {
            throw new HeThongException(ten + " không được để trống");
        }
    }

    private void requireId(Long value, String ten) {
        if (value == null) {
            throw new HeThongException(ten + " không được để trống");
        }
        if (value <= 0) {
            throw new HeThongException(ten + " không hợp lệ");
        }
    }

    private String formatDungLuong(long bytes) {
        double mb = bytes / 1024.0 / 1024.0;
        return String.format(Locale.ROOT, "%.0fMB", mb);
    }

    public String toFolderName(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "khac";
        }

        String normalized = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replace("_", "-")
                .replaceAll("[^a-z0-9-]", "-")
                .replaceAll("-+", "-")
                .replaceAll("^-|-$", "");

        return normalized.isEmpty() ? "khac" : normalized;
    }

    public record ThongTinTep(String tenMau, String extension, LoaiNoiDungTep loaiNoiDung) {
    }
    private void validateDungLuongTheoNguoiGui(MultipartFile file, TepDinhKemRequest request) {
        if (request.getNguoiGuiLoai() == null) {
            return;
        }

        String module = request.getModule() == null ? "" : request.getModule().trim().toLowerCase(Locale.ROOT);

        boolean laSinhVien = request.getNguoiGuiLoai().name().equals("SINH_VIEN")
                || module.equals("sinh-vien")
                || module.equals("sinh_vien")
                || module.equals("sinhvien");

        if (laSinhVien && file.getSize() > maxSinhVienFileSize) {
            throw new HeThongException("Sinh viên chỉ được tải lên tối đa " + formatDungLuong(maxSinhVienFileSize) + " cho mỗi file. Nếu bài lớn hơn, hãy chia thành nhiều file nhỏ.");
        }
    }
}
