package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TepDinhKem;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNoiDungTep;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTepDinhKem;
import org.example.trungcapphuongnam.module.heThong.mapper.TepDinhKemMapper;
import org.example.trungcapphuongnam.module.heThong.repository.TepDinhKemRepository;
import org.example.trungcapphuongnam.module.heThong.service.TepDinhKemService;
import org.example.trungcapphuongnam.module.heThong.validator.TepDinhKemValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TepDinhKemServiceImpl implements TepDinhKemService {

    private final TepDinhKemRepository repository;
    private final TepDinhKemMapper mapper;
    private final TepDinhKemValidator validator;

    @Value("${app.upload.root:uploads/tep-dinh-kem}")
    private String uploadRoot;
    @Value("${app.libreoffice.soffice-path:soffice}")
    private String sofficePath;
    @Override
    public TepDinhKemResponse upload(MultipartFile file, TepDinhKemRequest request) {
        TepDinhKemValidator.ThongTinTep thongTinTep = validator.validateUpload(file, request);

        LocalDate now = LocalDate.now();
        String tenLuu = UUID.randomUUID() + thongTinTep.extension();

        Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();
        Path folderPath = taoFolderLuuFile(rootPath, request, thongTinTep, now);

        if (!folderPath.startsWith(rootPath)) {
            throw new HeThongException("Đường dẫn lưu file không hợp lệ");
        }

        try {
            Files.createDirectories(folderPath);

            Path filePath = folderPath.resolve(tenLuu).normalize();

            if (!filePath.startsWith(rootPath)) {
                throw new HeThongException("Đường dẫn file không hợp lệ");
            }

            file.transferTo(filePath.toFile());

            String duongDanTuongDoi = rootPath.relativize(filePath).toString().replace("\\", "/");
            String duongDanNghiepVu = rootPath.relativize(folderPath).toString().replace("\\", "/");

            LoaiNoiDungTep loaiNoiDung = thongTinTep.loaiNoiDung();

            TepDinhKem entity = TepDinhKem.builder()
                    .tenGoc(thongTinTep.tenGoc())
                    .tenLuu(tenLuu)
                    .duongDanTuongDoi(duongDanTuongDoi)
                    .duongDanDayDu(filePath.toString())
                    .loaiNoiDung(loaiNoiDung)
                    .contentType(file.getContentType())
                    .extension(thongTinTep.extension())
                    .dungLuong(file.getSize())
                    .module(request.getModule())
                    .nghiepVu(request.getNghiepVu())
                    .doiTuongId(request.getDoiTuongId())
                    .nguoiGuiLoai(request.getNguoiGuiLoai())
                    .nguoiGuiId(request.getNguoiGuiId())
                    .nguoiGuiTen(request.getNguoiGuiTen())
                    .moTa(request.getMoTa())

                    .nganhId(request.getNganhId())
                    .chuongTrinhId(request.getChuongTrinhId())
                    .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                    .namHocId(request.getNamHocId())
                    .hocKyId(request.getHocKyId())
                    .lopHanhChinhId(request.getLopHanhChinhId())
                    .lopHocPhanId(request.getLopHocPhanId())
                    .monHocId(request.getMonHocId())
                    .chuongTrinhMonId(request.getChuongTrinhMonId())
                    .giangVienId(request.getGiangVienId())
                    .sinhVienId(request.getSinhVienId())
                    .baiHocId(request.getBaiHocId())
                    .baiTapId(request.getBaiTapId())
                    .labId(request.getLabId())
                    .baiNopId(request.getBaiNopId())
                    .lanNop(request.getLanNop())
                    .syllabusChuongTrinhId(request.getSyllabusChuongTrinhId())
                    .syllabusMonHocId(request.getSyllabusMonHocId())
                    .taiLieuGocId(request.getTaiLieuGocId())
                    .tangNghiepVu(request.getTangNghiepVu())
                    .duongDanNghiepVu(duongDanNghiepVu)

                    .trangThai(TrangThaiTepDinhKem.DANG_SU_DUNG)
                    .build();

            return mapper.toResponse(repository.save(entity));
        } catch (HeThongException e) {
            throw e;
        } catch (Exception e) {
            throw new HeThongException("Không thể lưu file. Vui lòng kiểm tra dung lượng file và thử lại");
        }
    }

    @Override
    public Page<TepDinhKemResponse> getAll(Pageable pageable) {
        return repository.findByTrangThai(TrangThaiTepDinhKem.DANG_SU_DUNG, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public Page<TepDinhKemResponse> getByDoiTuong(String module, String nghiepVu, Long doiTuongId, Pageable pageable) {
        validator.validateLayTheoDoiTuong(module, nghiepVu, doiTuongId);

        return repository.findByModuleAndNghiepVuAndDoiTuongIdAndTrangThai(
                        module,
                        nghiepVu,
                        doiTuongId,
                        TrangThaiTepDinhKem.DANG_SU_DUNG,
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    public Page<TepDinhKemResponse> getByNguoiGui(LoaiNguoiGui nguoiGuiLoai, Long nguoiGuiId, Pageable pageable) {
        validator.validateLayTheoNguoiGui(nguoiGuiLoai, nguoiGuiId);

        return repository.findByNguoiGuiLoaiAndNguoiGuiIdAndTrangThai(
                        nguoiGuiLoai,
                        nguoiGuiId,
                        TrangThaiTepDinhKem.DANG_SU_DUNG,
                        pageable
                )
                .map(mapper::toResponse);
    }

    @Override
    public Page<TepDinhKemResponse> getByModule(String module, String nghiepVu, Pageable pageable) {
        if (module == null || module.isBlank()) {
            return getAll(pageable);
        }

        if (nghiepVu == null || nghiepVu.isBlank()) {
            return repository.findByModuleAndTrangThai(
                            module,
                            TrangThaiTepDinhKem.DANG_SU_DUNG,
                            pageable
                    )
                    .map(mapper::toResponse);
        }

        return repository.findByModuleAndNghiepVuAndTrangThai(
                        module,
                        nghiepVu,
                        TrangThaiTepDinhKem.DANG_SU_DUNG,
                        pageable
                )
                .map(mapper::toResponse);
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
                throw new HeThongException("Đường dẫn file không hợp lệ");
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new HeThongNotFoundException("Không tìm thấy file vật lý: " + tep.getTenGoc());
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new HeThongException("Không thể tải file. Vui lòng thử lại");
        }
    }

    @Override
    public TepDinhKemResponse deleteMem(Long id) {
        TepDinhKem tep = findActiveById(id);
        tep.setTrangThai(TrangThaiTepDinhKem.DA_XOA);
        return mapper.toResponse(repository.save(tep));
    }

    private Path taoFolderLuuFile(
            Path rootPath,
            TepDinhKemRequest request,
            TepDinhKemValidator.ThongTinTep thongTinTep,
            LocalDate now
    ) {
        List<String> parts = new ArrayList<>();

        String moduleFolder = validator.toFolderName(request.getModule());
        String nghiepVuFolder = validator.toFolderName(request.getNghiepVu());
        String loaiNoiDungFolder = validator.toFolderName(thongTinTep.loaiNoiDung().name());

        parts.add(moduleFolder);

        if (laModuleSinhVien(request.getModule(), request.getNguoiGuiLoai())) {
            addPart(parts, "nganh", request.getNganhId());
            addPart(parts, "version", request.getChuongTrinhVersionId());
            addPart(parts, "nam-hoc", request.getNamHocId());
            addPart(parts, "hoc-ky", request.getHocKyId());
            addPart(parts, "lop-hanh-chinh", request.getLopHanhChinhId());
            addPart(parts, "lop-hoc-phan", request.getLopHocPhanId());
            addPart(parts, "mon", request.getMonHocId());
            addPart(parts, "bai-tap", request.getBaiTapId());
            addPart(parts, "lab", request.getLabId());
            addPart(parts, "sinh-vien", request.getSinhVienId() == null ? request.getNguoiGuiId() : request.getSinhVienId());
            addPart(parts, "lan-nop", request.getLanNop());
        } else if (laModuleGiangDay(request.getModule(), request.getNguoiGuiLoai())) {
            addPart(parts, "nam-hoc", request.getNamHocId());
            addPart(parts, "hoc-ky", request.getHocKyId());
            addPart(parts, "lop-hoc-phan", request.getLopHocPhanId());
            addPart(parts, "mon", request.getMonHocId());
            addPart(parts, "giang-vien", request.getGiangVienId() == null ? request.getNguoiGuiId() : request.getGiangVienId());
            addPart(parts, "bai-hoc", request.getBaiHocId());
            addPart(parts, "bai-tap", request.getBaiTapId());
            addPart(parts, "lab", request.getLabId());
        } else if (laModuleDaoTao(request.getModule(), request.getNguoiGuiLoai())) {
            addPart(parts, "nganh", request.getNganhId());
            addPart(parts, "chuong-trinh", request.getChuongTrinhId());
            addPart(parts, "version", request.getChuongTrinhVersionId());

            if (request.getTangNghiepVu() != null && !request.getTangNghiepVu().isBlank()) {
                parts.add("tang-" + validator.toFolderName(request.getTangNghiepVu()));
            }

            addPart(parts, "mon", request.getMonHocId());
            addPart(parts, "chuong-trinh-mon", request.getChuongTrinhMonId());
            addPart(parts, "syllabus-chuong-trinh", request.getSyllabusChuongTrinhId());
            addPart(parts, "syllabus-mon", request.getSyllabusMonHocId());
            addPart(parts, "tai-lieu-goc", request.getTaiLieuGocId());
        } else {
            addPart(parts, "doi-tuong", request.getDoiTuongId());
        }

        parts.add(nghiepVuFolder);
        parts.add("nam-" + now.getYear());
        parts.add("thang-" + String.format("%02d", now.getMonthValue()));
        parts.add(loaiNoiDungFolder);

        Path folderPath = rootPath;

        for (String part : parts) {
            if (part != null && !part.isBlank()) {
                folderPath = folderPath.resolve(part);
            }
        }

        return folderPath.normalize();
    }

    private void addPart(List<String> parts, String prefix, Long value) {
        if (value != null && value > 0) {
            parts.add(prefix + "-" + value);
        }
    }

    private void addPart(List<String> parts, String prefix, Integer value) {
        if (value != null && value > 0) {
            parts.add(prefix + "-" + value);
        }
    }

    private boolean laModuleSinhVien(String module, LoaiNguoiGui nguoiGuiLoai) {
        String value = module == null ? "" : module.trim().toLowerCase();

        return nguoiGuiLoai == LoaiNguoiGui.SINH_VIEN
                || value.equals("sinh-vien")
                || value.equals("sinh_vien")
                || value.equals("sinhvien");
    }

    private boolean laModuleGiangDay(String module, LoaiNguoiGui nguoiGuiLoai) {
        String value = module == null ? "" : module.trim().toLowerCase();

        return nguoiGuiLoai == LoaiNguoiGui.GIANG_VIEN
                || value.equals("giang-day")
                || value.equals("giang_day")
                || value.equals("giangday");
    }

    private boolean laModuleDaoTao(String module, LoaiNguoiGui nguoiGuiLoai) {
        String value = module == null ? "" : module.trim().toLowerCase();

        return nguoiGuiLoai == LoaiNguoiGui.DAO_TAO
                || value.equals("dao-tao")
                || value.equals("dao_tao")
                || value.equals("daotao");
    }

    private TepDinhKem findActiveById(Long id) {
        if (id == null || id <= 0) {
            throw new HeThongException("Tệp đính kèm không hợp lệ");
        }

        TepDinhKem tep = repository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Tệp đính kèm không tồn tại: " + id));

        if (tep.getTrangThai() == TrangThaiTepDinhKem.DA_XOA) {
            throw new HeThongNotFoundException("Tệp đính kèm đã bị xóa: " + id);
        }

        return tep;
    }
    @Override
    public Resource preview(Long id) {
        TepDinhKem tep = findActiveById(id);

        try {
            Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();
            Path filePath = rootPath.resolve(tep.getDuongDanTuongDoi()).normalize();

            if (!filePath.startsWith(rootPath)) {
                throw new HeThongException("Đường dẫn file không hợp lệ");
            }

            if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
                throw new HeThongNotFoundException("Không tìm thấy file vật lý: " + tep.getTenGoc());
            }

            String extension = layExtensionTep(tep);

            if (laFileOffice(extension)) {
                return taoPreviewOfficePdf(rootPath, tep, filePath);
            }

            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new HeThongNotFoundException("Không đọc được file: " + tep.getTenGoc());
            }

            return resource;
        } catch (HeThongException e) {
            throw e;
        } catch (MalformedURLException e) {
            throw new HeThongException("Không thể xem file. Vui lòng thử lại");
        }
    }
    private Resource taoPreviewOfficePdf(Path rootPath, TepDinhKem tep, Path filePath) {
        try {
            Path previewRoot = rootPath
                    .resolve("_preview")
                    .resolve("office-pdf")
                    .normalize();

            if (!previewRoot.startsWith(rootPath)) {
                throw new HeThongException("Đường dẫn preview không hợp lệ");
            }

            Files.createDirectories(previewRoot);

            String tenPdf = "tep-" + tep.getId() + ".pdf";
            Path pdfPath = previewRoot.resolve(tenPdf).normalize();

            if (!pdfPath.startsWith(rootPath)) {
                throw new HeThongException("Đường dẫn file preview không hợp lệ");
            }

            if (Files.exists(pdfPath) && Files.size(pdfPath) > 0) {
                return new UrlResource(pdfPath.toUri());
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                    sofficePath,
                    "--headless",
                    "--convert-to",
                    "pdf",
                    "--outdir",
                    previewRoot.toString(),
                    filePath.toString()
            );

            processBuilder.redirectErrorStream(true);

            Process process = processBuilder.start();
            boolean finished = process.waitFor(90, TimeUnit.SECONDS);

            if (!finished) {
                process.destroyForcibly();
                throw new HeThongException("Quá thời gian chuyển file Office sang PDF để xem trước");
            }

            if (process.exitValue() != 0) {
                throw new HeThongException("Không chuyển được file Office sang PDF. Vui lòng kiểm tra LibreOffice trên server");
            }

            Path filePdfLibreOffice = previewRoot.resolve(doiExtensionThanhPdf(filePath.getFileName().toString())).normalize();

            if (!Files.exists(filePdfLibreOffice) || Files.size(filePdfLibreOffice) == 0) {
                throw new HeThongException("Không tạo được file PDF xem trước từ file Office");
            }

            if (!filePdfLibreOffice.equals(pdfPath)) {
                Files.move(filePdfLibreOffice, pdfPath);
            }

            return new UrlResource(pdfPath.toUri());
        } catch (HeThongException e) {
            throw e;
        } catch (Exception e) {
            throw new HeThongException("Không xem trước được file Word/Excel/PowerPoint. Cần cài LibreOffice trên server");
        }
    }

    private String doiExtensionThanhPdf(String fileName) {
        String ten = fileName == null ? "preview" : fileName;

        int index = ten.lastIndexOf('.');

        if (index > 0) {
            ten = ten.substring(0, index);
        }

        return ten + ".pdf";
    }

    private String layExtensionTep(TepDinhKem tep) {
        String extension = tep.getExtension();

        if (extension == null || extension.isBlank()) {
            String tenGoc = tep.getTenGoc() == null ? "" : tep.getTenGoc();
            int index = tenGoc.lastIndexOf('.');

            if (index >= 0 && index < tenGoc.length() - 1) {
                extension = tenGoc.substring(index + 1);
            }
        }

        return extension == null
                ? ""
                : extension.replace(".", "").toLowerCase(Locale.ROOT);
    }

    private boolean laFileOffice(String extension) {
        return extension.equals("doc")
                || extension.equals("docx")
                || extension.equals("xls")
                || extension.equals("xlsx")
                || extension.equals("ppt")
                || extension.equals("pptx");
    }

}