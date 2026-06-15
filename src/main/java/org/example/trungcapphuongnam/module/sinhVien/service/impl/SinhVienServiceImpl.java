package org.example.trungcapphuongnam.module.sinhVien.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiNguoiGui;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.daoTao.entity.LopHanhChinh;
import org.example.trungcapphuongnam.module.daoTao.repository.LopHanhChinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienException;
import org.example.trungcapphuongnam.module.sinhVien.SinhVienNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.heThong.dto.request.TepDinhKemRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TepDinhKemResponse;
import org.example.trungcapphuongnam.module.heThong.entity.CauHinhMaSinhVien;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.repository.CauHinhMaSinhVienRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.TepDinhKemService;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.SinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.request.TiepNhanSinhVienRequest;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.SinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.dto.response.TiepNhanSinhVienResponse;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.mapper.SinhVienMapper;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.service.SinhVienService;
import org.example.trungcapphuongnam.module.sinhVien.validator.SinhVienValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongWriter;
@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienServiceImpl implements SinhVienService {

    private static final String ROLE_SINH_VIEN = "SINH_VIEN";
    private static final String ROLE_SINH_VIEN_PREFIX = "ROLE_SINH_VIEN";
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final SinhVienRepository repository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final LopHanhChinhRepository lopHanhChinhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final CauHinhMaSinhVienRepository cauHinhMaSinhVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordEncoder passwordEncoder;
    private final TepDinhKemService tepDinhKemService;
    private final SinhVienMapper mapper;
    private final SinhVienValidator sinhVienValidator;
    private final ObjectMapper objectMapper;
    private final NhatKyHeThongWriter nhatKyHeThongWriter;
    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienResponse> findTheoNganhChuongTrinhVersion(
            Long nganhId,
            Long chuongTrinhId,
            Long chuongTrinhVersionId,
            Pageable pageable
    ) {
        return repository
                .findTheoNganhChuongTrinhVersion(nganhId, chuongTrinhId, chuongTrinhVersionId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public SinhVienResponse create(SinhVienRequest request) {
        sinhVienValidator.validateCreateThuCong(request);
        SinhVien saved = repository.save(mapper.toEntity(request));
        SinhVienResponse response = mapper.toResponse(saved);
        ghiLogSinhVien("TAO_MOI", saved, null, response, "Thêm sinh viên " + moTaSinhVien(saved));
        return response;
    }
    @Override
    public TiepNhanSinhVienResponse tiepNhan(TiepNhanSinhVienRequest request) {
        SinhVienValidator.ThongTinChuongTrinh thongTin = sinhVienValidator.validateTiepNhan(request);
        return taoSinhVienTheoNganhChuongTrinhVersion(request, thongTin.getChuongTrinh(), thongTin.getVersion(), null, null, null, null, null);
    }

    @Override
    public TiepNhanSinhVienResponse tiepNhanCoFile(
            TiepNhanSinhVienRequest request,
            MultipartFile anhChanDung,
            MultipartFile cccdTruoc,
            MultipartFile cccdSau,
            MultipartFile bangCap,
            List<MultipartFile> giayToKhac
    ) {
        SinhVienValidator.ThongTinChuongTrinh thongTin = sinhVienValidator.validateTiepNhan(request);
        sinhVienValidator.validateTepTiepNhan(anhChanDung, cccdTruoc, cccdSau, bangCap);
        if (giayToKhac != null) {
            for (MultipartFile file : giayToKhac) {
                sinhVienValidator.validateTepKhac(file, "Giấy tờ khác");
            }
        }
        return taoSinhVienTheoNganhChuongTrinhVersion(request, thongTin.getChuongTrinh(), thongTin.getVersion(), anhChanDung, cccdTruoc, cccdSau, bangCap, giayToKhac);
    }

    @Override
    public SinhVienResponse update(Long id, SinhVienRequest request) {
        SinhVien entity = getById(id);
        SinhVienResponse noiDungCu = mapper.toResponse(entity);
        sinhVienValidator.validateUpdate(id, request);
        mapper.updateEntity(entity, request);
        SinhVien saved = repository.save(entity);
        SinhVienResponse response = mapper.toResponse(saved);
        ghiLogSinhVien("CAP_NHAT", saved, noiDungCu, response, "Cập nhật sinh viên " + moTaSinhVien(saved));
        return response;
    }

    @Override
    public void delete(Long id) {
        SinhVien entity = getById(id);
        SinhVienResponse noiDungCu = mapper.toResponse(entity);
        repository.delete(entity);
        ghiLogSinhVien("XOA", entity, noiDungCu, null, "Xóa sinh viên " + moTaSinhVien(entity));
    }
    private void ghiLogSinhVien(String hanhDong, SinhVien sinhVien, Object noiDungCu, Object noiDungMoi, String moTa) {
        try {
            nhatKyHeThongWriter.ghiChiTiet(
                    "SINH_VIEN",
                    "SINH_VIEN",
                    hanhDong,
                    "SINH_VIEN",
                    sinhVien == null ? null : sinhVien.getId(),
                    sinhVien == null ? null : sinhVien.getMaSinhVien(),
                    sinhVien == null ? null : sinhVien.getHoTen(),
                    toJson(noiDungCu),
                    toJson(noiDungMoi),
                    moTa
            );
        } catch (Exception ignored) {
            // Không cho lỗi ghi nhật ký làm hỏng nghiệp vụ sinh viên
        }
    }

    private String moTaSinhVien(SinhVien sinhVien) {
        if (sinhVien == null) {
            return "";
        }
        return String.join(" - ",
                nullToBlank(sinhVien.getId() == null ? null : "ID " + sinhVien.getId()),
                nullToBlank(sinhVien.getMaSinhVien()),
                nullToBlank(sinhVien.getHoTen()),
                nullToBlank(sinhVien.getEmail())
        );
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (Exception ex) {
            return null;
        }
    }

    private String nullToBlank(String value) {
        return value == null ? "" : value;
    }
    private TiepNhanSinhVienResponse taoSinhVienTheoNganhChuongTrinhVersion(
            TiepNhanSinhVienRequest request,
            ChuongTrinh chuongTrinh,
            ChuongTrinhVersion version,
            MultipartFile anhChanDung,
            MultipartFile cccdTruoc,
            MultipartFile cccdSau,
            MultipartFile bangCap,
            List<MultipartFile> giayToKhac
    ) {
        String maSinhVien = sinhMaSinhVien(request.getNganhId(), request.getChuongTrinhVersionId());
        String matKhauTam = taoMatKhauTam();

        TaiKhoan taiKhoan = TaiKhoan.builder()
                .email(request.getEmail())
                .matKhauHash(passwordEncoder.encode(matKhauTam))
                .loaiTaiKhoan(LoaiTaiKhoan.sinh_vien)
                .trangThai(TrangThaiTaiKhoan.da_kich_hoat)
                .taiKhoanVaiTros(new LinkedHashSet<>())
                .build();
        ganVaiTroSinhVien(taiKhoan);
        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

        SinhVien sinhVien = mapper.toEntity(request, maSinhVien, savedTaiKhoan.getId());
        SinhVien savedSinhVien = repository.save(sinhVien);

        if (anhChanDung != null && !anhChanDung.isEmpty()) {
            TepDinhKemResponse tep = uploadTepSinhVien(anhChanDung, savedSinhVien, "ANH_CHAN_DUNG", "Ảnh chân dung sinh viên");
            savedSinhVien.setAnhChanDungTepId(tep.getId());
        }
        if (cccdTruoc != null && !cccdTruoc.isEmpty()) {
            TepDinhKemResponse tep = uploadTepSinhVien(cccdTruoc, savedSinhVien, "CCCD_TRUOC", "CCCD mặt trước");
            savedSinhVien.setCccdTruocTepId(tep.getId());
        }
        if (cccdSau != null && !cccdSau.isEmpty()) {
            TepDinhKemResponse tep = uploadTepSinhVien(cccdSau, savedSinhVien, "CCCD_SAU", "CCCD mặt sau");
            savedSinhVien.setCccdSauTepId(tep.getId());
        }
        if (bangCap != null && !bangCap.isEmpty()) {
            TepDinhKemResponse tep = uploadTepSinhVien(bangCap, savedSinhVien, "BANG_CAP", "Bằng cấp sinh viên");
            savedSinhVien.setBangCapTepId(tep.getId());
        }
        if (giayToKhac != null) {
            for (MultipartFile file : giayToKhac) {
                if (file != null && !file.isEmpty()) {
                    uploadTepSinhVien(file, savedSinhVien, "GIAY_TO_KHAC", "Giấy tờ khác của sinh viên");
                }
            }
        }
        savedSinhVien = repository.save(savedSinhVien);

        // Validate sĩ số tối đa: không tiếp nhận SV nếu LHC đã đầy
        if (request.getLopHanhChinhId() != null) {
            lopHanhChinhRepository.findById(request.getLopHanhChinhId()).ifPresent(lhc -> {
                Integer siSo = lhc.getSiSo();
                if (siSo != null && siSo > 0) {
                    long svHienTai = sinhVienChuongTrinhRepository.countByLopHanhChinhId(lhc.getId());
                    if (svHienTai >= siSo) {
                        throw new SinhVienException(
                                "Lớp hành chính " + lhc.getMaLop() + " đã đủ sĩ số tối đa ("
                                + siSo + " sinh viên). Không thể tiếp nhận thêm.");
                    }
                }
            });
        }

        SinhVienChuongTrinh sinhVienChuongTrinh = SinhVienChuongTrinh.builder()
                .sinhVien(savedSinhVien)
                .chuongTrinhVersionId(version.getId())
                .lopHanhChinhId(request.getLopHanhChinhId())
                .ngayDangKy(LocalDate.now())
                .ngayNhapHoc(request.getNgayNhapHoc())
                .trangThai(SinhVienChuongTrinhTrangThai.da_dang_ky)
                .ghiChu(request.getGhiChuChuongTrinh())
                .build();
        SinhVienChuongTrinh savedDangKy = sinhVienChuongTrinhRepository.save(sinhVienChuongTrinh);

        return TiepNhanSinhVienResponse.builder()
                .sinhVien(mapper.toResponse(savedSinhVien))
                .sinhVienChuongTrinhId(savedDangKy.getId())
                .nganhId(request.getNganhId())
                .chuongTrinhId(chuongTrinh.getId())
                .chuongTrinhVersionId(version.getId())
                .emailTaiKhoan(request.getEmail())
                .matKhauTam(matKhauTam)
                .build();
    }

    private TepDinhKemResponse uploadTepSinhVien(MultipartFile file, SinhVien sinhVien, String nghiepVu, String moTa) {
        TepDinhKemRequest request = TepDinhKemRequest.builder()
                .module("sinh_vien")
                .nghiepVu(nghiepVu)
                .doiTuongId(sinhVien.getId())
                .sinhVienId(sinhVien.getId())
                .nguoiGuiLoai(LoaiNguoiGui.NHAN_VIEN)
                .nguoiGuiId(0L)
                .nguoiGuiTen(sinhVien.getHoTen())
                .moTa(moTa)
                .build();
        return tepDinhKemService.upload(file, request);
    }

    private SinhVien getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new SinhVienNotFoundException("Sinh viên", id));
    }

    private String sinhMaSinhVien(Long nganhId, Long versionId) {
        CauHinhMaSinhVien cauHinh = cauHinhMaSinhVienRepository
                .findWithLockByNganhIdAndChuongTrinhVersionId(nganhId, versionId)
                .orElseThrow(() -> new SinhVienException("Chưa cấu hình mã sinh viên cho ngành và version này"));

        int soHienTai = cauHinh.getSoHienTai() == null ? 0 : cauHinh.getSoHienTai();
        int doDai = cauHinh.getDoDaiSoThuTu() == null ? 3 : cauHinh.getDoDaiSoThuTu();
        int soTiepTheo = soHienTai + 1;
        String maSinhVien = cauHinh.getTienTo()
                + cauHinh.getMaDau()
                + String.format("%0" + doDai + "d", soTiepTheo);

        if (repository.existsByMaSinhVien(maSinhVien)) {
            throw new SinhVienException("Mã sinh viên sinh tự động đã tồn tại: " + maSinhVien);
        }

        cauHinh.setSoHienTai(soTiepTheo);
        cauHinhMaSinhVienRepository.save(cauHinh);
        return maSinhVien;
    }

    private void ganVaiTroSinhVien(TaiKhoan taiKhoan) {
        VaiTro vaiTro = vaiTroRepository.findByMaVaiTro(ROLE_SINH_VIEN)
                .or(() -> vaiTroRepository.findByMaVaiTro(ROLE_SINH_VIEN_PREFIX))
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò SINH_VIEN"));

        Set<TaiKhoanVaiTro> vaiTros = taiKhoan.getTaiKhoanVaiTros();
        if (vaiTros == null) {
            vaiTros = new LinkedHashSet<>();
            taiKhoan.setTaiKhoanVaiTros(vaiTros);
        }

        boolean daCoRole = vaiTros.stream()
                .anyMatch(item -> item.getVaiTro() != null && item.getVaiTro().getId().equals(vaiTro.getId()));

        if (daCoRole) return;

        TaiKhoanVaiTro taiKhoanVaiTro = new TaiKhoanVaiTro();
        taiKhoanVaiTro.setTaiKhoan(taiKhoan);
        taiKhoanVaiTro.setVaiTro(vaiTro);
        vaiTros.add(taiKhoanVaiTro);
    }

    private ChuongTrinh layChuongTrinhTheoVersion(Long versionId) {
        if (versionId == null) return null;
        ChuongTrinhVersion version = chuongTrinhVersionRepository.findById(versionId).orElse(null);
        if (version == null) return null;
        return chuongTrinhRepository.findById(version.getChuongTrinhId()).orElse(null);
    }

    private String taoMatKhauTam() {
        StringBuilder builder = new StringBuilder("PN@");
        for (int i = 0; i < 8; i++) {
            builder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }
        return builder.toString();
    }
}
