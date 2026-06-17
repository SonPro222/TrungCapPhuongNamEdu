package org.example.trungcapphuongnam.module.giangDay.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.giangDay.GiangDayException;
import org.example.trungcapphuongnam.module.giangDay.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.enums.TrangThaiGiaoVien;
import org.example.trungcapphuongnam.module.giangDay.mapper.GiaoVienMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienService;
import org.example.trungcapphuongnam.module.giangDay.validator.GiaoVienValidator;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.enums.LoaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.enums.TrangThaiTaiKhoan;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhatKyHeThongWriter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class GiaoVienServiceImpl implements GiaoVienService {

    private static final String ROLE_GIAO_VIEN = "GIAO_VIEN";
    private static final String ROLE_GIAO_VIEN_PREFIX = "ROLE_GIAO_VIEN";
    private static final String CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final GiaoVienRepository repository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final PasswordEncoder passwordEncoder;
    private final GiaoVienMapper mapper;
    private final GiaoVienValidator validator;
    private final NhatKyHeThongWriter nhatKyHeThongWriter;

    @Override
    @Transactional(readOnly = true)
    public Page<GiaoVienResponse> getAll(String keyword, TrangThaiGiaoVien trangThai, Pageable pageable) {
        String tuKhoa = keyword == null ? "" : keyword.trim();

        if (!tuKhoa.isBlank() && trangThai != null) {
            return repository
                    .findByTrangThaiAndMaGiaoVienContainingIgnoreCaseOrTrangThaiAndHoTenContainingIgnoreCaseOrTrangThaiAndEmailContainingIgnoreCase(
                            trangThai,
                            tuKhoa,
                            trangThai,
                            tuKhoa,
                            trangThai,
                            tuKhoa,
                            pageable
                    )
                    .map(mapper::toResponse);
        }

        if (!tuKhoa.isBlank()) {
            return repository
                    .findByMaGiaoVienContainingIgnoreCaseOrHoTenContainingIgnoreCaseOrEmailContainingIgnoreCase(
                            tuKhoa,
                            tuKhoa,
                            tuKhoa,
                            pageable
                    )
                    .map(mapper::toResponse);
        }

        if (trangThai != null) {
            return repository.findByTrangThai(trangThai, pageable)
                    .map(mapper::toResponse);
        }

        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public GiaoVienResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public GiaoVienResponse create(GiaoVienRequest request) {
        validator.validateCreate(request);

        String email = chuanHoaEmail(request.getEmail());
        String matKhauTam = taoMatKhauTam();

        if (taiKhoanRepository.existsByEmailIgnoreCase(email)) {
            throw new GiangDayException("Gmail này đã có tài khoản trong hệ thống");
        }

        TaiKhoan taiKhoan = mapper.toTaiKhoanGiaoVien(
                email,
                passwordEncoder.encode(matKhauTam)
        );

        ganVaiTroGiaoVien(taiKhoan);

        TaiKhoan savedTaiKhoan = taiKhoanRepository.save(taiKhoan);

        GiaoVien entity = mapper.toEntity(request);
        entity.setEmail(email);
        entity.setTaiKhoanId(savedTaiKhoan.getId());

        GiaoVien saved = repository.save(entity);

        ghiLogCapTaiKhoan(saved, savedTaiKhoan);

        GiaoVienResponse response = mapper.toResponse(saved);
        response.setMatKhauTam(matKhauTam);

        return response;
    }

    @Override
    public GiaoVienResponse update(Long id, GiaoVienRequest request) {
        validator.validateUpdate(id, request);

        GiaoVien entity = findEntity(id);
        String emailMoi = chuanHoaEmail(request.getEmail());

        if (entity.getTaiKhoanId() != null) {
            TaiKhoan taiKhoan = taiKhoanRepository.findById(entity.getTaiKhoanId())
                    .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản giáo viên với id = " + entity.getTaiKhoanId()));

            if (!taiKhoan.getEmail().equalsIgnoreCase(emailMoi)
                    && taiKhoanRepository.existsByEmailIgnoreCaseAndIdNot(emailMoi, taiKhoan.getId())) {
                throw new GiangDayException("Gmail này đã có tài khoản trong hệ thống");
            }

            taiKhoan.setEmail(emailMoi);
            taiKhoan.setLoaiTaiKhoan(LoaiTaiKhoan.giao_vien);
            taiKhoan.setTrangThai(quyDoiTrangThaiTaiKhoan(request.getTrangThai()));
            ganVaiTroGiaoVien(taiKhoan);
            taiKhoanRepository.save(taiKhoan);
        }

        mapper.updateEntity(entity, request);
        entity.setEmail(emailMoi);

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        GiaoVien entity = findEntity(id);
        repository.delete(entity);
    }

    private GiaoVien findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("Giáo viên không tồn tại với id = " + id));
    }

    private void ganVaiTroGiaoVien(TaiKhoan taiKhoan) {
        VaiTro vaiTro = vaiTroRepository.findByMaVaiTro(ROLE_GIAO_VIEN)
                .or(() -> vaiTroRepository.findByMaVaiTro(ROLE_GIAO_VIEN_PREFIX))
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò GIAO_VIEN"));

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

    private TrangThaiTaiKhoan quyDoiTrangThaiTaiKhoan(TrangThaiGiaoVien trangThaiGiaoVien) {
        if (trangThaiGiaoVien == TrangThaiGiaoVien.nghi_viec) {
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

    private void ghiLogCapTaiKhoan(GiaoVien giaoVien, TaiKhoan taiKhoan) {
        try {
            nhatKyHeThongWriter.ghi(
                    "CAP_TAI_KHOAN_GIAO_VIEN",
                    "giao_vien",
                    giaoVien.getId(),
                    null,
                    "{\"giaoVienId\":" + giaoVien.getId()
                            + ",\"maGiaoVien\":\"" + giaoVien.getMaGiaoVien()
                            + "\",\"email\":\"" + giaoVien.getEmail()
                            + "\",\"taiKhoanId\":" + taiKhoan.getId() + "}"
            );
        } catch (Exception ignored) {
            // Không cho lỗi ghi nhật ký làm hỏng nghiệp vụ tạo giáo viên
        }
    }
}