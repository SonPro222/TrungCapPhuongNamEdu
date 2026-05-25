package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.heThong.dto.request.CapTaiKhoanSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.GuiOtpDoiMatKhauSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanSinhVienResponse;
import org.example.trungcapphuongnam.module.heThong.entity.CauHinhMaSinhVien;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.repository.CauHinhMaSinhVienRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongWriter;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanSinhVienService;
import org.example.trungcapphuongnam.module.heThong.validator.TaiKhoanValidator;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVien;
import org.example.trungcapphuongnam.module.sinhVien.entity.SinhVienChuongTrinh;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienChuongTrinhRepository;
import org.example.trungcapphuongnam.module.sinhVien.repository.SinhVienRepository;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienTrangThai;
import org.example.trungcapphuongnam.module.sinhVien.enums.SinhVienChuongTrinhTrangThai;
import org.springframework.data.domain.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiKhoanSinhVienServiceImpl implements TaiKhoanSinhVienService {

    private static final String ROLE_SINH_VIEN = "SINH_VIEN";
    private static final String ROLE_SINH_VIEN_PREFIX = "ROLE_SINH_VIEN";
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int OTP_HET_HAN_PHUT = 5;
    private final SinhVienRepository sinhVienRepository;
    private final SinhVienChuongTrinhRepository sinhVienChuongTrinhRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final ChuongTrinhRepository chuongTrinhRepository;
    private final NganhRepository nganhRepository;
    private final CauHinhMaSinhVienRepository cauHinhMaSinhVienRepository;
    private final PasswordEncoder passwordEncoder;
    private final NhatKyHeThongWriter nhatKyHeThongWriter;
    private final TaiKhoanValidator taiKhoanValidator;
    private final JavaMailSender javaMailSender;
    private final Map<String, OtpDoiMatKhau> otpDoiMatKhauMap = new ConcurrentHashMap<>();

    @Override
    @Transactional(readOnly = true)
    public Page<TaiKhoanSinhVienResponse> findAll(
            String keyword,
            Long nganhId,
            Long chuongTrinhVersionId,
            String trangThaiTaiKhoan,
            Pageable pageable
    ) {
        List<SinhVienChuongTrinh> danhSach = sinhVienChuongTrinhRepository.findAll();

        Map<Long, ChuongTrinhVersion> versionMap = chuongTrinhVersionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinhVersion::getId, Function.identity()));

        Map<Long, ChuongTrinh> chuongTrinhMap = chuongTrinhRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinh::getId, Function.identity()));

        Map<Long, Nganh> nganhMap = nganhRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nganh::getId, Function.identity()));

        List<Long> taiKhoanIds = danhSach.stream()
                .map(SinhVienChuongTrinh::getSinhVien)
                .filter(Objects::nonNull)
                .map(SinhVien::getTaiKhoanId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        Map<Long, TaiKhoan> taiKhoanMap = taiKhoanRepository.findAllById(taiKhoanIds)
                .stream()
                .collect(Collectors.toMap(TaiKhoan::getId, Function.identity()));

        List<TaiKhoanSinhVienResponse> filtered = danhSach.stream()
                .map(item -> toResponse(item, versionMap, chuongTrinhMap, nganhMap, taiKhoanMap))
                .filter(item -> matchKeyword(item, keyword))
                .filter(item -> nganhId == null || Objects.equals(item.getNganhId(), nganhId))
                .filter(item -> chuongTrinhVersionId == null || Objects.equals(item.getChuongTrinhVersionId(), chuongTrinhVersionId))
                .filter(item -> matchTrangThaiTaiKhoan(item, trangThaiTaiKhoan))
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());

        List<TaiKhoanSinhVienResponse> content = start >= filtered.size()
                ? List.of()
                : filtered.subList(start, end);

        return new PageImpl<>(content, pageable, filtered.size());
    }
    @Override
    public TaiKhoanSinhVienResponse capTaiKhoan(Long sinhVienId, CapTaiKhoanSinhVienRequest request) {
        SinhVien sinhVien = getSinhVien(sinhVienId);
        SinhVienChuongTrinh sinhVienChuongTrinh = getSinhVienChuongTrinhChinh(sinhVienId);

        if (sinhVien.getTaiKhoanId() != null) {
            throw new RuntimeException("Sinh viên đã có tài khoản đăng nhập");
        }

        String email = normalizeEmail(request.getEmail());
        String matKhauTam = taoMatKhauTam();

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(email).orElse(null);

        if (taiKhoan == null) {
            taiKhoan = TaiKhoan.builder()
                    .email(email)
                    .matKhauHash(passwordEncoder.encode(matKhauTam))
                    .loaiTaiKhoan(LoaiTaiKhoan.sinh_vien)
                    .trangThai(parseTrangThai(request.getTrangThai()))
                    .taiKhoanVaiTros(new LinkedHashSet<>())
                    .build();
        } else {
            if (sinhVienRepository.existsByTaiKhoanId(taiKhoan.getId())) {
                throw new RuntimeException("Tài khoản email này đã được gắn với sinh viên khác");
            }

            taiKhoan.setLoaiTaiKhoan(LoaiTaiKhoan.sinh_vien);
            taiKhoan.setTrangThai(parseTrangThai(request.getTrangThai()));
            taiKhoan.setMatKhauHash(passwordEncoder.encode(matKhauTam));

            if (taiKhoan.getTaiKhoanVaiTros() == null) {
                taiKhoan.setTaiKhoanVaiTros(new LinkedHashSet<>());
            }
        }

        ganVaiTroSinhVien(taiKhoan);

        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

        if (sinhVien.getMaSinhVien() == null || sinhVien.getMaSinhVien().isBlank()) {
            sinhVien.setMaSinhVien(sinhMaSinhVien(sinhVienChuongTrinh));
        }

        sinhVien.setEmail(email);
        sinhVien.setTaiKhoanId(savedTaiKhoan.getId());
        sinhVienRepository.save(sinhVien);

        nhatKyHeThongWriter.ghi(
                "CAP_TAI_KHOAN_SINH_VIEN",
                "sinh_vien",
                sinhVien.getId(),
                null,
                "{\"sinhVienId\":" + sinhVien.getId()
                        + ",\"maSinhVien\":\"" + sinhVien.getMaSinhVien()
                        + "\",\"email\":\"" + email
                        + "\",\"taiKhoanId\":" + savedTaiKhoan.getId() + "}"
        );

        TaiKhoanSinhVienResponse response = layResponseTheoSinhVien(sinhVien.getId());
        response.setMatKhauTam(matKhauTam);

        return response;
    }
    @Override
    public TaiKhoanSinhVienResponse khoaTaiKhoan(Long sinhVienId) {
        SinhVien sinhVien = getSinhVien(sinhVienId);
        TaiKhoan taiKhoan = getTaiKhoanSinhVien(sinhVien);

        String trangThaiCu = taiKhoan.getTrangThai() == null ? null : taiKhoan.getTrangThai().name();

        taiKhoan.setTrangThai(TrangThaiTaiKhoan.bi_khoa);
        taiKhoanRepository.save(taiKhoan);

        nhatKyHeThongWriter.ghi(
                "KHOA_TAI_KHOAN_SINH_VIEN",
                "tai_khoan",
                taiKhoan.getId(),
                "{\"trangThai\":\"" + trangThaiCu + "\"}",
                "{\"trangThai\":\"bi_khoa\"}"
        );

        return layResponseTheoSinhVien(sinhVienId);
    }

    @Override
    public TaiKhoanSinhVienResponse moKhoaTaiKhoan(Long sinhVienId) {
        SinhVien sinhVien = getSinhVien(sinhVienId);
        TaiKhoan taiKhoan = getTaiKhoanSinhVien(sinhVien);

        String trangThaiCu = taiKhoan.getTrangThai() == null ? null : taiKhoan.getTrangThai().name();

        taiKhoan.setTrangThai(TrangThaiTaiKhoan.da_kich_hoat);
        taiKhoanRepository.save(taiKhoan);

        nhatKyHeThongWriter.ghi(
                "MO_KHOA_TAI_KHOAN_SINH_VIEN",
                "tai_khoan",
                taiKhoan.getId(),
                "{\"trangThai\":\"" + trangThaiCu + "\"}",
                "{\"trangThai\":\"da_kich_hoat\"}"
        );

        return layResponseTheoSinhVien(sinhVienId);
    }

    private String sinhMaSinhVien(SinhVienChuongTrinh sinhVienChuongTrinh) {
        Long versionId = sinhVienChuongTrinh.getChuongTrinhVersionId();

        ChuongTrinhVersion version = chuongTrinhVersionRepository.findById(versionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy version chương trình"));

        ChuongTrinh chuongTrinh = chuongTrinhRepository.findById(version.getChuongTrinhId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chương trình"));

        Long nganhId = chuongTrinh.getNganhId();

        CauHinhMaSinhVien cauHinh = cauHinhMaSinhVienRepository
                .findWithLockByNganhIdAndChuongTrinhVersionId(nganhId, versionId)
                .orElseThrow(() -> new RuntimeException("Chưa cấu hình mã sinh viên cho ngành và version này"));

        int soTiepTheo = cauHinh.getSoHienTai() + 1;
        String maSinhVien = cauHinh.getTienTo()
                + cauHinh.getMaDau()
                + String.format("%0" + cauHinh.getDoDaiSoThuTu() + "d", soTiepTheo);

        if (sinhVienRepository.existsByMaSinhVien(maSinhVien)) {
            throw new RuntimeException("Mã sinh viên sinh tự động đã tồn tại: " + maSinhVien);
        }

        cauHinh.setSoHienTai(soTiepTheo);
        cauHinhMaSinhVienRepository.save(cauHinh);

        return maSinhVien;
    }

    private SinhVienChuongTrinh getSinhVienChuongTrinhChinh(Long sinhVienId) {
        List<SinhVienChuongTrinh> list = sinhVienChuongTrinhRepository.findBySinhVienId(sinhVienId);

        if (list.isEmpty()) {
            throw new RuntimeException("Sinh viên chưa được gắn version chương trình");
        }

        return list.get(0);
    }

    private TaiKhoanSinhVienResponse layResponseTheoSinhVien(Long sinhVienId) {
        List<SinhVienChuongTrinh> list = sinhVienChuongTrinhRepository.findBySinhVienId(sinhVienId);

        if (list.isEmpty()) {
            throw new RuntimeException("Sinh viên chưa được gắn chương trình đào tạo");
        }

        Map<Long, ChuongTrinhVersion> versionMap = chuongTrinhVersionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinhVersion::getId, Function.identity()));

        Map<Long, ChuongTrinh> chuongTrinhMap = chuongTrinhRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinh::getId, Function.identity()));

        Map<Long, Nganh> nganhMap = nganhRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Nganh::getId, Function.identity()));

        SinhVien sinhVien = list.get(0).getSinhVien();

        Map<Long, TaiKhoan> taiKhoanMap = sinhVien.getTaiKhoanId() == null
                ? Map.of()
                : taiKhoanRepository.findById(sinhVien.getTaiKhoanId())
                .stream()
                .collect(Collectors.toMap(TaiKhoan::getId, Function.identity()));

        return toResponse(list.get(0), versionMap, chuongTrinhMap, nganhMap, taiKhoanMap);
    }

    private TaiKhoanSinhVienResponse toResponse(
            SinhVienChuongTrinh item,
            Map<Long, ChuongTrinhVersion> versionMap,
            Map<Long, ChuongTrinh> chuongTrinhMap,
            Map<Long, Nganh> nganhMap,
            Map<Long, TaiKhoan> taiKhoanMap
    ) {
        SinhVien sinhVien = item.getSinhVien();
        ChuongTrinhVersion version = versionMap.get(item.getChuongTrinhVersionId());
        ChuongTrinh chuongTrinh = version == null ? null : chuongTrinhMap.get(version.getChuongTrinhId());
        Nganh nganh = chuongTrinh == null ? null : nganhMap.get(chuongTrinh.getNganhId());
        TaiKhoan taiKhoan = sinhVien == null || sinhVien.getTaiKhoanId() == null
                ? null
                : taiKhoanMap.get(sinhVien.getTaiKhoanId());

        return TaiKhoanSinhVienResponse.builder()
                .sinhVienChuongTrinhId(item.getId())
                .sinhVienId(sinhVien == null ? null : sinhVien.getId())
                .maSinhVien(sinhVien == null ? null : sinhVien.getMaSinhVien())
                .hoTen(sinhVien == null ? null : sinhVien.getHoTen())
                .emailSinhVien(sinhVien == null ? null : sinhVien.getEmail())
                .soDienThoai(sinhVien == null ? null : sinhVien.getSoDienThoai())
                .trangThaiSinhVien(sinhVien == null || sinhVien.getTrangThai() == null ? null : sinhVien.getTrangThai().name())
                .taiKhoanId(taiKhoan == null ? null : taiKhoan.getId())
                .emailTaiKhoan(taiKhoan == null ? null : taiKhoan.getEmail())
                .trangThaiTaiKhoan(taiKhoan == null || taiKhoan.getTrangThai() == null ? "chua_co_tai_khoan" : taiKhoan.getTrangThai().name())
                .loaiTaiKhoan(taiKhoan == null || taiKhoan.getLoaiTaiKhoan() == null ? null : taiKhoan.getLoaiTaiKhoan().name())
                .nganhId(nganh == null ? null : nganh.getId())
                .maNganh(nganh == null ? null : nganh.getMaNganh())
                .tenNganh(nganh == null ? null : nganh.getTenNganh())
                .chuongTrinhId(chuongTrinh == null ? null : chuongTrinh.getId())
                .maChuongTrinh(chuongTrinh == null ? null : chuongTrinh.getMaChuongTrinh())
                .tenChuongTrinh(chuongTrinh == null ? null : chuongTrinh.getTenChuongTrinh())
                .chuongTrinhVersionId(version == null ? null : version.getId())
                .maVersion(version == null ? null : version.getMaVersion())
                .tenVersion(version == null ? null : version.getTenVersion())
                .build();
    }

    private void ganVaiTroSinhVien(TaiKhoan taiKhoan) {
        VaiTro vaiTro = vaiTroRepository.findByMaVaiTro(ROLE_SINH_VIEN)
                .or(() -> vaiTroRepository.findByMaVaiTro(ROLE_SINH_VIEN_PREFIX))
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò SINH_VIEN"));

        boolean daCoRole = taiKhoan.getTaiKhoanVaiTros()
                .stream()
                .anyMatch(item -> item.getVaiTro() != null && item.getVaiTro().getId().equals(vaiTro.getId()));

        if (daCoRole) {
            return;
        }

        TaiKhoanVaiTro taiKhoanVaiTro = new TaiKhoanVaiTro();
        taiKhoanVaiTro.setTaiKhoan(taiKhoan);
        taiKhoanVaiTro.setVaiTro(vaiTro);

        taiKhoan.getTaiKhoanVaiTros().add(taiKhoanVaiTro);
    }

    private boolean matchKeyword(TaiKhoanSinhVienResponse item, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return true;
        }

        String value = keyword.trim().toLowerCase();

        return contains(item.getMaSinhVien(), value)
                || contains(item.getHoTen(), value)
                || contains(item.getEmailSinhVien(), value)
                || contains(item.getEmailTaiKhoan(), value)
                || contains(item.getTenNganh(), value)
                || contains(item.getTenChuongTrinh(), value)
                || contains(item.getTenVersion(), value);
    }

    private boolean matchTrangThaiTaiKhoan(TaiKhoanSinhVienResponse item, String trangThaiTaiKhoan) {
        if (trangThaiTaiKhoan == null || trangThaiTaiKhoan.isBlank()) {
            return true;
        }

        return trangThaiTaiKhoan.trim().equalsIgnoreCase(item.getTrangThaiTaiKhoan());
    }

    private boolean contains(String source, String value) {
        return source != null && source.toLowerCase().contains(value);
    }

    private SinhVien getSinhVien(Long id) {
        return sinhVienRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với id = " + id));
    }

    private TaiKhoan getTaiKhoanSinhVien(SinhVien sinhVien) {
        if (sinhVien.getTaiKhoanId() == null) {
            throw new RuntimeException("Sinh viên chưa có tài khoản đăng nhập");
        }

        return taiKhoanRepository.findById(sinhVien.getTaiKhoanId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản sinh viên"));
    }

    private TrangThaiTaiKhoan parseTrangThai(String value) {
        if (value == null || value.isBlank()) {
            return TrangThaiTaiKhoan.da_kich_hoat;
        }

        return TrangThaiTaiKhoan.valueOf(value.trim().toLowerCase());
    }

    private String normalizeEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
    @Override
    public TaiKhoanSinhVienResponse capNhatGmail(Long sinhVienId, CapTaiKhoanSinhVienRequest request) {
        SinhVien sinhVien = getSinhVien(sinhVienId);
        TaiKhoan taiKhoan = getTaiKhoanSinhVien(sinhVien);

        String emailCu = taiKhoan.getEmail();
        String emailMoi = normalizeEmail(request.getEmail());

        TaiKhoan taiKhoanTrungEmail = taiKhoanRepository.findByEmail(emailMoi).orElse(null);

        if (taiKhoanTrungEmail != null && !taiKhoanTrungEmail.getId().equals(taiKhoan.getId())) {
            throw new RuntimeException("Email này đã được tài khoản khác sử dụng");
        }

        taiKhoan.setEmail(emailMoi);
        taiKhoanRepository.save(taiKhoan);

        sinhVien.setEmail(emailMoi);
        sinhVienRepository.save(sinhVien);

        nhatKyHeThongWriter.ghi(
                "CAP_NHAT_GMAIL_TAI_KHOAN_SINH_VIEN",
                "tai_khoan",
                taiKhoan.getId(),
                "{\"email\":\"" + emailCu + "\"}",
                "{\"email\":\"" + emailMoi + "\"}"
        );

        return layResponseTheoSinhVien(sinhVienId);
    }

    @Override
    public TaiKhoanSinhVienResponse datLaiMatKhau(Long sinhVienId) {
        SinhVien sinhVien = getSinhVien(sinhVienId);
        TaiKhoan taiKhoan = getTaiKhoanSinhVien(sinhVien);

        String matKhauTam = taoMatKhauTam();

        taiKhoan.setMatKhauHash(passwordEncoder.encode(matKhauTam));
        taiKhoanRepository.save(taiKhoan);

        nhatKyHeThongWriter.ghi(
                "DAT_LAI_MAT_KHAU_SINH_VIEN",
                "tai_khoan",
                taiKhoan.getId(),
                null,
                "{\"taiKhoanId\":" + taiKhoan.getId() + ",\"email\":\"" + taiKhoan.getEmail() + "\"}"
        );

        TaiKhoanSinhVienResponse response = layResponseTheoSinhVien(sinhVienId);
        response.setMatKhauTam(matKhauTam);

        return response;
    }

    @Override
    public int khoaNhieuTaiKhoan(List<Long> sinhVienIds) {
        int count = 0;

        for (Long sinhVienId : sinhVienIds) {
            SinhVien sinhVien = getSinhVien(sinhVienId);

            if (sinhVien.getTaiKhoanId() == null) {
                continue;
            }

            TaiKhoan taiKhoan = getTaiKhoanSinhVien(sinhVien);
            String trangThaiCu = taiKhoan.getTrangThai() == null ? null : taiKhoan.getTrangThai().name();

            taiKhoan.setTrangThai(TrangThaiTaiKhoan.bi_khoa);
            taiKhoanRepository.save(taiKhoan);

            nhatKyHeThongWriter.ghi(
                    "KHOA_NHIEU_TAI_KHOAN_SINH_VIEN",
                    "tai_khoan",
                    taiKhoan.getId(),
                    "{\"trangThai\":\"" + trangThaiCu + "\"}",
                    "{\"trangThai\":\"bi_khoa\"}"
            );

            count++;
        }

        return count;
    }

    @Override
    public int moKhoaNhieuTaiKhoan(List<Long> sinhVienIds) {
        int count = 0;

        for (Long sinhVienId : sinhVienIds) {
            SinhVien sinhVien = getSinhVien(sinhVienId);

            if (sinhVien.getTaiKhoanId() == null) {
                continue;
            }

            TaiKhoan taiKhoan = getTaiKhoanSinhVien(sinhVien);
            String trangThaiCu = taiKhoan.getTrangThai() == null ? null : taiKhoan.getTrangThai().name();

            taiKhoan.setTrangThai(TrangThaiTaiKhoan.da_kich_hoat);
            taiKhoanRepository.save(taiKhoan);

            nhatKyHeThongWriter.ghi(
                    "MO_KHOA_NHIEU_TAI_KHOAN_SINH_VIEN",
                    "tai_khoan",
                    taiKhoan.getId(),
                    "{\"trangThai\":\"" + trangThaiCu + "\"}",
                    "{\"trangThai\":\"da_kich_hoat\"}"
            );

            count++;
        }

        return count;
    }

    @Override
    public int tuDongKhoaTaiKhoanTheoNghiepVu() {
        List<SinhVienChuongTrinh> danhSach = sinhVienChuongTrinhRepository.findAll();

        Map<Long, ChuongTrinhVersion> versionMap = chuongTrinhVersionRepository.findAll()
                .stream()
                .collect(Collectors.toMap(ChuongTrinhVersion::getId, Function.identity()));

        int count = 0;
        LocalDate homNay = LocalDate.now();

        for (SinhVienChuongTrinh sinhVienChuongTrinh : danhSach) {
            SinhVien sinhVien = sinhVienChuongTrinh.getSinhVien();

            if (sinhVien == null || sinhVien.getTaiKhoanId() == null) {
                continue;
            }

            TaiKhoan taiKhoan = taiKhoanRepository.findById(sinhVien.getTaiKhoanId()).orElse(null);

            if (taiKhoan == null || taiKhoan.getTrangThai() == TrangThaiTaiKhoan.bi_khoa) {
                continue;
            }

            ChuongTrinhVersion version = versionMap.get(sinhVienChuongTrinh.getChuongTrinhVersionId());
            String lyDo = layLyDoCanKhoaTuDong(sinhVien, sinhVienChuongTrinh, version, homNay);

            if (lyDo == null) {
                continue;
            }

            String trangThaiCu = taiKhoan.getTrangThai() == null ? null : taiKhoan.getTrangThai().name();
            taiKhoan.setTrangThai(TrangThaiTaiKhoan.bi_khoa);
            taiKhoanRepository.save(taiKhoan);

            nhatKyHeThongWriter.ghi(
                    "TU_DONG_KHOA_TAI_KHOAN_SINH_VIEN",
                    "tai_khoan",
                    taiKhoan.getId(),
                    "{\"trangThai\":\"" + trangThaiCu + "\"}",
                    "{\"trangThai\":\"bi_khoa\",\"lyDo\":\"" + lyDo + "\"}"
            );

            count++;
        }

        return count;
    }

    private String layLyDoCanKhoaTuDong(
            SinhVien sinhVien,
            SinhVienChuongTrinh sinhVienChuongTrinh,
            ChuongTrinhVersion version,
            LocalDate homNay
    ) {
        if (sinhVien.getTrangThai() == SinhVienTrangThai.tot_nghiep) {
            return "Sinh viên đã tốt nghiệp";
        }

        if (sinhVien.getTrangThai() == SinhVienTrangThai.nghi_hoc) {
            return "Sinh viên đã nghỉ học hoặc bỏ học";
        }

        if (sinhVienChuongTrinh.getTrangThai() == SinhVienChuongTrinhTrangThai.hoan_thanh) {
            return "Sinh viên đã hoàn thành khóa học";
        }

        if (sinhVienChuongTrinh.getTrangThai() == SinhVienChuongTrinhTrangThai.huy) {
            return "Sinh viên đã hủy chương trình học";
        }

        if (version != null && version.getNgayHetHieuLuc() != null && version.getNgayHetHieuLuc().isBefore(homNay)) {
            return "Khóa học hoặc version chương trình đã hết hạn";
        }

        return null;
    }

    @Override
    public void guiOtpDoiMatKhau(GuiOtpDoiMatKhauSinhVienRequest request) {
        taiKhoanValidator.validateGuiOtpDoiMatKhauSinhVien(request);

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản sinh viên theo Gmail này"));

        if (taiKhoan.getLoaiTaiKhoan() != LoaiTaiKhoan.sinh_vien) {
            throw new HeThongException("Gmail này không phải tài khoản sinh viên");
        }

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.bi_khoa) {
            throw new HeThongException("Tài khoản sinh viên đang bị khóa, không thể đổi mật khẩu");
        }

        String otp = taoOtp();
        otpDoiMatKhauMap.put(
                request.getEmail(),
                new OtpDoiMatKhau(otp, LocalDateTime.now().plusMinutes(OTP_HET_HAN_PHUT))
        );

        guiMailOtp(request.getEmail(), otp);
    }

    @Override
    public void xacNhanDoiMatKhau(DoiMatKhauSinhVienRequest request) {
        taiKhoanValidator.validateDoiMatKhauSinhVien(request);

        OtpDoiMatKhau otpDaGui = otpDoiMatKhauMap.get(request.getEmail());

        if (otpDaGui == null) {
            throw new HeThongException("Chưa gửi OTP hoặc OTP đã hết hạn. Vui lòng gửi lại OTP");
        }

        if (otpDaGui.hetHan()) {
            otpDoiMatKhauMap.remove(request.getEmail());
            throw new HeThongException("OTP đã hết hạn. Vui lòng gửi lại OTP");
        }

        if (!otpDaGui.maOtp().equals(request.getOtp())) {
            throw new HeThongException("OTP không đúng");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản sinh viên theo Gmail này"));

        if (taiKhoan.getLoaiTaiKhoan() != LoaiTaiKhoan.sinh_vien) {
            throw new HeThongException("Gmail này không phải tài khoản sinh viên");
        }

        if (taiKhoan.getTrangThai() == TrangThaiTaiKhoan.bi_khoa) {
            throw new HeThongException("Tài khoản sinh viên đang bị khóa, không thể đổi mật khẩu");
        }

        taiKhoan.setMatKhauHash(passwordEncoder.encode(request.getMatKhauMoi()));
        taiKhoan.setTrangThai(TrangThaiTaiKhoan.da_kich_hoat);
        taiKhoanRepository.save(taiKhoan);

        otpDoiMatKhauMap.remove(request.getEmail());

        nhatKyHeThongWriter.ghi(
                "DOI_MAT_KHAU_SINH_VIEN_BANG_OTP",
                "tai_khoan",
                taiKhoan.getId(),
                null,
                "{\"taiKhoanId\":" + taiKhoan.getId() + ",\"email\":\"" + taiKhoan.getEmail() + "\"}"
        );
    }

    private String taoMatKhauTam() {
        StringBuilder builder = new StringBuilder("PN@");

        for (int i = 0; i < 8; i++) {
            builder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }

        return builder.toString();
    }

    private String taoOtp() {
        int value = RANDOM.nextInt(1_000_000);
        return String.format("%06d", value);
    }

    private void guiMailOtp(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Mã OTP đổi mật khẩu sinh viên");
        message.setText("Mã OTP đổi mật khẩu của bạn là: " + otp
                + "\nMã này có hiệu lực trong " + OTP_HET_HAN_PHUT + " phút."
                + "\nNếu bạn không yêu cầu đổi mật khẩu, vui lòng bỏ qua email này.");
        javaMailSender.send(message);
    }

    private record OtpDoiMatKhau(String maOtp, LocalDateTime hetHanLuc) {
        private boolean hetHan() {
            return LocalDateTime.now().isAfter(hetHanLuc);
        }
    }
}
