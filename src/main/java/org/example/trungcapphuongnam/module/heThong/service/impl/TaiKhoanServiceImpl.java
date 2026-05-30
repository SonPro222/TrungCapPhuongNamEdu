package org.example.trungcapphuongnam.module.heThong.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.dto.request.DoiMatKhauTaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.mapper.TaiKhoanMapper;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanService;
import org.example.trungcapphuongnam.module.heThong.validator.TaiKhoanValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiKhoanServiceImpl implements TaiKhoanService {

    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final TaiKhoanMapper taiKhoanMapper;
    private final PasswordEncoder passwordEncoder;
    private final TaiKhoanValidator taiKhoanValidator;

    @Override
    @Transactional(readOnly = true)
    public List<TaiKhoanResponse> getAll() {
        return taiKhoanRepository.findAll().stream().map(taiKhoanMapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaiKhoanResponse> search(String keyword, String loaiTaiKhoan, String trangThai, String vaiTro, Pageable pageable) {
        Specification<TaiKhoan> spec = Specification.where(null);

        if (keyword != null && !keyword.isBlank()) {
            String value = "%" + keyword.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("email")), value));
        }

        if (loaiTaiKhoan != null && !loaiTaiKhoan.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("loaiTaiKhoan").as(String.class)), loaiTaiKhoan.trim().toLowerCase()));
        }

        if (trangThai != null && !trangThai.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("trangThai").as(String.class)), trangThai.trim().toLowerCase()));
        }

        if (vaiTro != null && !vaiTro.isBlank()) {
            String value = "%" + vaiTro.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                Join<TaiKhoan, TaiKhoanVaiTro> tkv = root.join("taiKhoanVaiTros", JoinType.LEFT);
                Join<TaiKhoanVaiTro, VaiTro> vt = tkv.join("vaiTro", JoinType.LEFT);
                return cb.or(
                        cb.like(cb.lower(vt.get("maVaiTro")), value),
                        cb.like(cb.lower(vt.get("tenVaiTro")), value)
                );
            });
        }

        return taiKhoanRepository.findAll(spec, pageable).map(taiKhoanMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public TaiKhoanResponse getById(Long id) {
        return taiKhoanMapper.toResponse(findByIdOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public TaiKhoanResponse getByEmail(String email) {
        taiKhoanValidator.validateTimTaiKhoanTheoEmail(email);
        return taiKhoanMapper.toResponse(findByEmailOrThrow(email));
    }

    @Override
    public TaiKhoanResponse create(TaiKhoanRequest request) {
        taiKhoanValidator.validateCreate(request);

        TaiKhoan entity = taiKhoanMapper.toEntity(request);
        entity.setEmail(chuanHoaEmail(request.getEmail()));
        entity.setMatKhauHash(passwordEncoder.encode(request.getMatKhau()));
        capNhatVaiTro(entity, request.getRoles());

        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    public TaiKhoanResponse update(Long id, TaiKhoanRequest request) {
        taiKhoanValidator.validateUpdate(id, request);

        TaiKhoan entity = findByIdOrThrow(id);
        taiKhoanMapper.updateEntity(entity, request);
        entity.setEmail(chuanHoaEmail(request.getEmail()));

        capNhatVaiTro(entity, request.getRoles());

        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    public TaiKhoanResponse doiMatKhauQuanTri(Long id, DoiMatKhauTaiKhoanRequest request) {
        taiKhoanValidator.validateDoiMatKhauTaiKhoan(request);

        TaiKhoan entity = findByIdOrThrow(id);
        entity.setMatKhauHash(passwordEncoder.encode(request.getMatKhauMoi()));

        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    public TaiKhoanResponse doiMatKhauTheoGmail(String email, DoiMatKhauTaiKhoanRequest request) {
        taiKhoanValidator.validateDoiMatKhauTaiKhoanTheoGmail(email, request);

        TaiKhoan entity = findByEmailOrThrow(email);
        entity.setMatKhauHash(passwordEncoder.encode(request.getMatKhauMoi()));

        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        taiKhoanRepository.delete(findByIdOrThrow(id));
    }

    private TaiKhoan findByIdOrThrow(Long id) {
        return taiKhoanRepository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với id = " + id));
    }

    private TaiKhoan findByEmailOrThrow(String email) {
        return taiKhoanRepository.findByEmailIgnoreCase(chuanHoaEmail(email))
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với Gmail: " + email));
    }

    private void capNhatVaiTro(TaiKhoan taiKhoan, List<String> roleCodes) {
        Set<String> roleCodesMoi = chuanHoaRoleCodes(roleCodes);

        taiKhoan.getTaiKhoanVaiTros().removeIf(taiKhoanVaiTro -> {
            String maVaiTro = taiKhoanVaiTro.getVaiTro() == null ? null : taiKhoanVaiTro.getVaiTro().getMaVaiTro();
            return maVaiTro == null || !roleCodesMoi.contains(maVaiTro.trim().toUpperCase());
        });

        Set<String> roleCodesDangCo = taiKhoan.getTaiKhoanVaiTros()
                .stream()
                .filter(taiKhoanVaiTro -> taiKhoanVaiTro.getVaiTro() != null)
                .map(taiKhoanVaiTro -> taiKhoanVaiTro.getVaiTro().getMaVaiTro())
                .filter(maVaiTro -> maVaiTro != null && !maVaiTro.isBlank())
                .map(maVaiTro -> maVaiTro.trim().toUpperCase())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (String roleCode : roleCodesMoi) {
            if (roleCodesDangCo.contains(roleCode)) {
                continue;
            }

            VaiTro vaiTro = vaiTroRepository.findByMaVaiTro(roleCode)
                    .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò: " + roleCode));

            TaiKhoanVaiTro taiKhoanVaiTro = new TaiKhoanVaiTro();
            taiKhoanVaiTro.setTaiKhoan(taiKhoan);
            taiKhoanVaiTro.setVaiTro(vaiTro);
            taiKhoan.getTaiKhoanVaiTros().add(taiKhoanVaiTro);
        }
    }

    private Set<String> chuanHoaRoleCodes(List<String> roleCodes) {
        if (roleCodes == null) {
            return new LinkedHashSet<>();
        }

        return roleCodes.stream()
                .filter(role -> role != null && !role.isBlank())
                .map(role -> role.trim().toUpperCase())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private String chuanHoaEmail(String email) {
        return email == null ? null : email.trim().toLowerCase();
    }
}