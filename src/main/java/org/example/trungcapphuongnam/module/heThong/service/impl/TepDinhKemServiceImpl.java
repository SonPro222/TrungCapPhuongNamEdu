package org.example.trungcapphuongnam.module.heThong.service.impl;


import lombok.RequiredArgsConstructor;

import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.constant.LoaiNoiDungTep;
import org.example.trungcapphuongnam.module.heThong.constant.TrangThaiTepDinhKem;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TepDinhKem;
import org.example.trungcapphuongnam.module.heThong.mapper.TepDinhKemMapper;
import org.example.trungcapphuongnam.module.heThong.repository.TepDinhKemRepository;
import org.example.trungcapphuongnam.module.heThong.service.TepDinhKemService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TepDinhKemServiceImpl implements TepDinhKemService {

    private final TepDinhKemRepository repository;
    private final TepDinhKemMapper mapper;

    @Value("${app.upload.root:uploads/tep-dinh-kem}")
    private String uploadRoot;

    @Value("${app.upload.max-file-size:209715200}")
    private long maxFileSize;

    @Override
    public TepDinhKemResponse upload(MultipartFile file, TepDinhKemRequest request) {
        validateFile(file);
        validateRequest(request);

        String tenGoc = StringUtils.cleanPath(file.getOriginalFilename() == null ? "file" : file.getOriginalFilename());

        if (tenGoc.contains("..")) {
            throw new BadRequestException("Tên file không hợp lệ");
        }

        String extension = layExtension(tenGoc);
        LoaiNoiDungTep loaiNoiDung = phanLoaiNoiDung(file.getContentType(), extension);

        LocalDate now = LocalDate.now();

        String nguoiGuiFolder = toFolderName(request.getNguoiGuiLoai().name());
        String moduleFolder = toFolderName(request.getModule());
        String loaiNoiDungFolder = toFolderName(loaiNoiDung.name());

        String tenLuu = UUID.randomUUID() + extension;

        Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();

        Path folderPath = rootPath
                .resolve(moduleFolder)
                .resolve(nguoiGuiFolder)
                .resolve(String.valueOf(request.getNguoiGuiId() == null ? 0 : request.getNguoiGuiId()))
                .resolve(String.valueOf(now.getYear()))
                .resolve(String.format("%02d", now.getMonthValue()))
                .resolve(loaiNoiDungFolder)
                .normalize();

        if (!folderPath.startsWith(rootPath)) {
            throw new BadRequestException("Đường dẫn lưu file không hợp lệ");
        }

        try {
            Files.createDirectories(folderPath);

            Path filePath = folderPath.resolve(tenLuu).normalize();

            if (!filePath.startsWith(rootPath)) {
                throw new BadRequestException("Đường dẫn file không hợp lệ");
            }

            file.transferTo(filePath.toFile());

            String duongDanTuongDoi = rootPath.relativize(filePath).toString().replace("\\", "/");

            TepDinhKem entity = TepDinhKem.builder()
                    .tenGoc(tenGoc)
                    .tenLuu(tenLuu)
                    .duongDanTuongDoi(duongDanTuongDoi)
                    .duongDanDayDu(filePath.toString())
                    .loaiNoiDung(loaiNoiDung)
                    .contentType(file.getContentType())
                    .extension(extension)
                    .dungLuong(file.getSize())
                    .module(request.getModule())
                    .nghiepVu(request.getNghiepVu())
                    .doiTuongId(request.getDoiTuongId())
                    .nguoiGuiLoai(request.getNguoiGuiLoai())
                    .nguoiGuiId(request.getNguoiGuiId())
                    .nguoiGuiTen(request.getNguoiGuiTen())
                    .moTa(request.getMoTa())
                    .trangThai(TrangThaiTepDinhKem.DANG_SU_DUNG)
                    .build();

            return mapper.toResponse(repository.save(entity));
        } catch (Exception e) {
            throw new BadRequestException("Không thể lưu file: " + e.getMessage());
        }
    }

    @Override
    public List<TepDinhKemResponse> getAll() {
        return repository.findByTrangThaiOrderByNgayTaoDesc(TrangThaiTepDinhKem.DANG_SU_DUNG)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<TepDinhKemResponse> getByDoiTuong(String module, String nghiepVu, Long doiTuongId) {
        if (module == null || module.trim().isEmpty()) {
            throw new BadRequestException("module không được để trống");
        }

        if (nghiepVu == null || nghiepVu.trim().isEmpty()) {
            throw new BadRequestException("nghiepVu không được để trống");
        }

        if (doiTuongId == null) {
            throw new BadRequestException("doiTuongId không được để trống");
        }

        return repository.findByModuleAndNghiepVuAndDoiTuongIdAndTrangThaiOrderByNgayTaoDesc(
                        module,
                        nghiepVu,
                        doiTuongId,
                        TrangThaiTepDinhKem.DANG_SU_DUNG
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<TepDinhKemResponse> getByNguoiGui(LoaiNguoiGui nguoiGuiLoai, Long nguoiGuiId) {
        if (nguoiGuiLoai == null) {
            throw new BadRequestException("nguoiGuiLoai không được để trống");
        }

        if (nguoiGuiId == null) {
            throw new BadRequestException("nguoiGuiId không được để trống");
        }

        return repository.findByNguoiGuiLoaiAndNguoiGuiIdAndTrangThaiOrderByNgayTaoDesc(
                        nguoiGuiLoai,
                        nguoiGuiId,
                        TrangThaiTepDinhKem.DANG_SU_DUNG
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public TepDinhKemResponse getById(Long id) {
        return mapper.toResponse(findActiveById(id));
    }

    @Override
    public Resource download(Long id) {
        TepDinhKem tep = findActiveById(id);

        try {
            Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();
            Path filePath = rootPath.resolve(tep.getDuongDanTuongDoi()).normalize();

            if (!filePath.startsWith(rootPath)) {
                throw new BadRequestException("Đường dẫn file không hợp lệ");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new ResourceNotFoundException("Không tìm thấy file vật lý: " + tep.getTenGoc());
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new BadRequestException("Không thể tải file: " + e.getMessage());
        }
    }

    @Override
    public TepDinhKemResponse deleteMem(Long id) {
        TepDinhKem tep = findActiveById(id);
        tep.setTrangThai(TrangThaiTepDinhKem.DA_XOA);
        return mapper.toResponse(repository.save(tep));
    }

    private TepDinhKem findActiveById(Long id) {
        TepDinhKem tep = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tệp đính kèm không tồn tại: " + id));

        if (tep.getTrangThai() == TrangThaiTepDinhKem.DA_XOA) {
            throw new ResourceNotFoundException("Tệp đính kèm đã bị xóa: " + id);
        }

        return tep;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File không được để trống");
        }

        if (file.getSize() > maxFileSize) {
            throw new BadRequestException("File vượt quá dung lượng cho phép");
        }
    }

    private void validateRequest(TepDinhKemRequest request) {
        if (request == null) {
            throw new BadRequestException("Thông tin upload không hợp lệ");
        }

        if (request.getNguoiGuiLoai() == null) {
            throw new BadRequestException("nguoiGuiLoai không được để trống");
        }

        if (request.getModule() == null || request.getModule().trim().isEmpty()) {
            throw new BadRequestException("module không được để trống");
        }

        if (request.getNghiepVu() == null || request.getNghiepVu().trim().isEmpty()) {
            throw new BadRequestException("nghiepVu không được để trống");
        }
    }

    private String layExtension(String tenFile) {
        int index = tenFile.lastIndexOf(".");
        if (index < 0) {
            return "";
        }

        return tenFile.substring(index).toLowerCase(Locale.ROOT);
    }

    private LoaiNoiDungTep phanLoaiNoiDung(String contentType, String extension) {
        String type = contentType == null ? "" : contentType.toLowerCase(Locale.ROOT);

        if (type.startsWith("image/")) {
            return LoaiNoiDungTep.HINH_ANH;
        }

        if (type.startsWith("video/")) {
            return LoaiNoiDungTep.VIDEO;
        }

        if (type.startsWith("audio/")) {
            return LoaiNoiDungTep.AM_THANH;
        }

        if (
                extension.equals(".pdf")
                        || extension.equals(".doc")
                        || extension.equals(".docx")
                        || extension.equals(".xls")
                        || extension.equals(".xlsx")
                        || extension.equals(".ppt")
                        || extension.equals(".pptx")
                        || extension.equals(".txt")
                        || extension.equals(".csv")
        ) {
            return LoaiNoiDungTep.TAI_LIEU;
        }

        return LoaiNoiDungTep.FILE_KHAC;
    }

    private String toFolderName(String value) {
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
}