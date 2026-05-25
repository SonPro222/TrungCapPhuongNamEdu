package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNoiDungTep;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTepDinhKem;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TepDinhKem;
import org.example.trungcapphuongnam.module.heThong.mapper.TepDinhKemMapper;
import org.example.trungcapphuongnam.module.heThong.repository.TepDinhKemRepository;
import org.example.trungcapphuongnam.module.heThong.service.TepDinhKemService;
import org.example.trungcapphuongnam.module.heThong.validator.TepDinhKemValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
@RequiredArgsConstructor
public class TepDinhKemServiceImpl implements TepDinhKemService {

    private final TepDinhKemRepository repository;
    private final TepDinhKemMapper mapper;
    private final TepDinhKemValidator validator;

    @Value("${app.upload.root:uploads/tep-dinh-kem}")
    private String uploadRoot;

    @Override
    public TepDinhKemResponse upload(MultipartFile file, TepDinhKemRequest request) {
        TepDinhKemValidator.ThongTinTep thongTinTep = validator.validateUpload(file, request);

        LocalDate now = LocalDate.now();

        String moduleFolder = validator.toFolderName(request.getModule());
        String nghiepVuFolder = validator.toFolderName(request.getNghiepVu());
        String loaiNoiDungFolder = validator.toFolderName(thongTinTep.loaiNoiDung().name());

        String tenLuu = UUID.randomUUID() + thongTinTep.extension();

        Path rootPath = Paths.get(uploadRoot).toAbsolutePath().normalize();
        Path folderPath = rootPath
                .resolve(moduleFolder)
                .resolve(String.valueOf(request.getDoiTuongId()))
                .resolve(nghiepVuFolder)
                .resolve(String.valueOf(now.getYear()))
                .resolve(String.format("%02d", now.getMonthValue()))
                .resolve(loaiNoiDungFolder)
                .normalize();

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
}
