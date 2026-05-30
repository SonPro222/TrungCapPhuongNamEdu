package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.HeThongException;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhanVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhanVienResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhanVien;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiNhanVien;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.mapper.NhanVienMapper;
import org.example.trungcapphuongnam.module.heThong.repository.NhanVienRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhanVienService;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongWriter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class NhanVienServiceImpl implements NhanVienService {

    private static final String ROLE_DAO_TAO = "DAO_TAO";
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordEncoder passwordEncoder;
    private final NhanVienMapper nhanVienMapper;
    private final NhatKyHeThongWriter nhatKyHeThongWriter;

    @Override
    @Transactional(readOnly = true)
    public List<NhanVienResponse> getAll() {
        return search(
                null,
                null,
                null,
                null,
                null,
                PageRequest.of(0, 1000, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NhanVienResponse> search(
            String keyword,
            String phongBan,
            String chucVu,
            String trangThai,
            Integer namBatDau,
            Pageable pageable
    ) {
        Specification<NhanVien> spec = Specification.where(null);

        if (keyword != null && !keyword.isBlank()) {
            String value = "%" + keyword.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("maNhanVien")), value),
                    cb.like(cb.lower(root.get("hoTen")), value),
                    cb.like(cb.lower(root.get("email")), value),
                    cb.like(cb.lower(root.get("soDienThoai")), value)
            ));
        }

        if (phongBan != null && !phongBan.isBlank()) {
            String value = "%" + phongBan.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("phongBan")), value));
        }

        if (chucVu != null && !chucVu.isBlank()) {
            String value = "%" + chucVu.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("chucVu")), value));
        }

        if (trangThai != null && !trangThai.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    cb.lower(root.get("trangThai").as(String.class)),
                    trangThai.trim().toLowerCase()
            ));
        }

        if (namBatDau != null) {
            spec = spec.and((root, query, cb) -> cb.equal(
                    cb.function("year", Integer.class, root.get("createdAt")),
                    namBatDau
            ));
        }

        return nhanVienRepository.findAll(spec, pageable).map(nhanVienMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhanVienResponse getById(Long id) {
        return nhanVienMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    public NhanVienResponse create(NhanVienRequest request) {
        validateCreate(request);

        String email = chuanHoaEmail(request.getEmail());
        String matKhauTam = taoMatKhauTam();

        TaiKhoan taiKhoan = TaiKhoan.builder()
                .email(email)
                .matKhauHash(passwordEncoder.encode(matKhauTam))
                .loaiTaiKhoan(LoaiTaiKhoan.dao_tao)
                .trangThai(quyDoiTrangThaiTaiKhoan(request.getTrangThai()))
                .taiKhoanVaiTros(new LinkedHashSet<>())
                .build();

        ganVaiTroDaoTao(taiKhoan);

        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

        NhanVien entity = nhanVienMapper.toEntity(request);
        entity.setEmail(email);
        entity.setTaiKhoan(savedTaiKhoan);

        NhanVien saved = nhanVienRepository.save(entity);

        ghiLogCapTaiKhoan(saved, savedTaiKhoan);

        NhanVienResponse response = nhanVienMapper.toResponse(saved);
        response.setMatKhauTam(matKhauTam);

        return response;
    }

    @Override
    public NhanVienResponse update(Long id, NhanVienRequest request) {
        validateUpdate(id, request);

        NhanVien entity = findByIdOrThrow(id);
        String emailMoi = chuanHoaEmail(request.getEmail());

        TaiKhoan taiKhoan = entity.getTaiKhoan();

        if (taiKhoan == null) {
            String matKhauTam = taoMatKhauTam();

            taiKhoan = TaiKhoan.builder()
                    .email(emailMoi)
                    .matKhauHash(passwordEncoder.encode(matKhauTam))
                    .loaiTaiKhoan(LoaiTaiKhoan.dao_tao)
                    .trangThai(quyDoiTrangThaiTaiKhoan(request.getTrangThai()))
                    .taiKhoanVaiTros(new LinkedHashSet<>())
                    .build();

            ganVaiTroDaoTao(taiKhoan);

            TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

            nhanVienMapper.updateEntity(entity, request);
            entity.setEmail(emailMoi);
            entity.setTaiKhoan(savedTaiKhoan);

            NhanVien saved = nhanVienRepository.save(entity);

            ghiLogCapTaiKhoan(saved, savedTaiKhoan);

            NhanVienResponse response = nhanVienMapper.toResponse(saved);
            response.setMatKhauTam(matKhauTam);

            return response;
        }

        if (!taiKhoan.getEmail().equalsIgnoreCase(emailMoi)
                && taiKhoanRepository.existsByEmailIgnoreCaseAndIdNot(emailMoi, taiKhoan.getId())) {
            throw new HeThongException("Gmail này đã được sử dụng bởi tài khoản khác");
        }

        taiKhoan.setEmail(emailMoi);
        taiKhoan.setLoaiTaiKhoan(LoaiTaiKhoan.dao_tao);
        taiKhoan.setTrangThai(quyDoiTrangThaiTaiKhoan(request.getTrangThai()));
        ganVaiTroDaoTao(taiKhoan);
        taiKhoanRepository.save(taiKhoan);

        nhanVienMapper.updateEntity(entity, request);
        entity.setEmail(emailMoi);
        entity.setTaiKhoan(taiKhoan);

        return nhanVienMapper.toResponse(nhanVienRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        nhanVienRepository.delete(findByIdOrThrow(id));
    }

    private NhanVien findByIdOrThrow(Long id) {
        return nhanVienRepository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy nhân viên với id = " + id));
    }

    private void validateCreate(NhanVienRequest request) {
        validateCommon(request);

        String email = chuanHoaEmail(request.getEmail());
        if (taiKhoanRepository.existsByEmailIgnoreCase(email)) {
            throw new HeThongException("Gmail này đã được sử dụng bởi tài khoản khác");
        }
    }

    private void validateUpdate(Long id, NhanVienRequest request) {
        if (id == null) {
            throw new HeThongException("Nhân viên cần cập nhật không hợp lệ");
        }

        if (!nhanVienRepository.existsById(id)) {
            throw new HeThongException("Không tìm thấy nhân viên cần cập nhật");
        }

        validateCommon(request);
    }

    private void validateCommon(NhanVienRequest request) {
        if (request == null) {
            throw new HeThongException("Dữ liệu nhân viên không hợp lệ");
        }

        if (isBlank(request.getMaNhanVien())) {
            throw new HeThongException("Mã nhân viên không được để trống");
        }

        if (isBlank(request.getHoTen())) {
            throw new HeThongException("Họ tên không được để trống");
        }

        if (isBlank(request.getEmail())) {
            throw new HeThongException("Gmail không được để trống để cấp tài khoản đào tạo");
        }

        String email = chuanHoaEmail(request.getEmail());
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new HeThongException("Gmail không đúng định dạng");
        }

        if (!laNhanSuDaoTao(request)) {
            throw new HeThongException("Nhân viên đào tạo phải có phòng ban hoặc chức vụ là Đào tạo");
        }
    }

    private boolean laNhanSuDaoTao(NhanVienRequest request) {
        String phongBan = boDau(request.getPhongBan());
        String chucVu = boDau(request.getChucVu());

        return phongBan.contains("dao tao") || chucVu.contains("dao tao");
    }

    private void ganVaiTroDaoTao(TaiKhoan taiKhoan) {
        VaiTro vaiTro = vaiTroRepository.findByMaVaiTro(ROLE_DAO_TAO)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò DAO_TAO"));

        Set<TaiKhoanVaiTro> vaiTros = taiKhoan.getTaiKhoanVaiTros();

        if (vaiTros == null) {
            vaiTros = new LinkedHashSet<>();
            taiKhoan.setTaiKhoanVaiTros(vaiTros);
        }

        boolean daCoRole = vaiTros.stream()
                .anyMatch(item -> item.getVaiTro() != null && item.getVaiTro().getId().equals(vaiTro.getId()));

        if (daCoRole) {
            return;
        }

        TaiKhoanVaiTro taiKhoanVaiTro = new TaiKhoanVaiTro();
        taiKhoanVaiTro.setTaiKhoan(taiKhoan);
        taiKhoanVaiTro.setVaiTro(vaiTro);
        vaiTros.add(taiKhoanVaiTro);
    }

    private TrangThaiTaiKhoan quyDoiTrangThaiTaiKhoan(TrangThaiNhanVien trangThaiNhanVien) {
        if (trangThaiNhanVien == TrangThaiNhanVien.nghi_viec) {
            return TrangThaiTaiKhoan.bi_khoa;
        }

        return TrangThaiTaiKhoan.da_kich_hoat;
    }

    private String taoMatKhauTam() {
        StringBuilder builder = new StringBuilder("PN@");

        for (int i = 0; i < 8; i++) {
            builder.append(CHARS.charAt(RANDOM.nextInt(CHARS.length())));
        }

        return builder.toString();
    }

    private String chuanHoaEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }

    private String boDau(String value) {
        if (value == null) {
            return "";
        }

        String normalized = Normalizer.normalize(value.trim().toLowerCase(), Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").replace('đ', 'd');
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void ghiLogCapTaiKhoan(NhanVien nhanVien, TaiKhoan taiKhoan) {
        try {
            nhatKyHeThongWriter.ghi(
                    "CAP_TAI_KHOAN_DAO_TAO",
                    "nhan_vien",
                    nhanVien.getId(),
                    null,
                    "{\"nhanVienId\":" + nhanVien.getId()
                            + ",\"maNhanVien\":\"" + nhanVien.getMaNhanVien()
                            + "\",\"email\":\"" + nhanVien.getEmail()
                            + "\",\"taiKhoanId\":" + taiKhoan.getId()
                            + ",\"role\":\"DAO_TAO\"}"
            );
        } catch (Exception ignored) {
            // Không cho lỗi ghi nhật ký làm hỏng nghiệp vụ tạo tài khoản đào tạo
        }
    }
}