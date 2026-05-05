package org.example.trungcapphuongnam.module.heThong.service.impl;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.TaiKhoanRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.TaiKhoanResponse;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoanVaiTro;
import org.example.trungcapphuongnam.module.heThong.entity.VaiTro;
import org.example.trungcapphuongnam.module.heThong.exception.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.TaiKhoanMapper;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.repository.VaiTroRepository;
import org.example.trungcapphuongnam.module.heThong.service.TaiKhoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class TaiKhoanServiceImpl implements TaiKhoanService {
    private final TaiKhoanRepository taiKhoanRepository;
    private final VaiTroRepository vaiTroRepository;
    private final TaiKhoanMapper taiKhoanMapper;
    private final PasswordEncoder passwordEncoder;

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
    public TaiKhoanResponse create(TaiKhoanRequest request) {
        validateRequest(request, true);
        String email = request.getEmail().trim().toLowerCase();
        if (taiKhoanRepository.existsByEmail(email)) throw new RuntimeException("Email đã tồn tại");
        TaiKhoan entity = taiKhoanMapper.toEntity(request);
        entity.setEmail(email);
        entity.setMatKhauHash(passwordEncoder.encode(request.getMatKhau()));
        ganVaiTro(entity, request.getRoles());
        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    public TaiKhoanResponse update(Long id, TaiKhoanRequest request) {
        validateRequest(request, false);
        TaiKhoan entity = findByIdOrThrow(id);
        String email = request.getEmail().trim().toLowerCase();
        taiKhoanRepository.findByEmail(email).filter(existing -> !existing.getId().equals(id)).ifPresent(existing -> { throw new RuntimeException("Email đã tồn tại"); });
        taiKhoanMapper.updateEntity(entity, request);
        entity.setEmail(email);
        if (request.getMatKhau() != null && !request.getMatKhau().isBlank()) entity.setMatKhauHash(passwordEncoder.encode(request.getMatKhau()));
        ganVaiTro(entity, request.getRoles());
        return taiKhoanMapper.toResponse(taiKhoanRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        taiKhoanRepository.delete(findByIdOrThrow(id));
    }

    private TaiKhoan findByIdOrThrow(Long id) {
        return taiKhoanRepository.findById(id).orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với id = " + id));
    }

    private void validateRequest(TaiKhoanRequest request, boolean createMode) {
        if (request == null) throw new RuntimeException("Dữ liệu tài khoản không hợp lệ");
        if (request.getEmail() == null || request.getEmail().isBlank()) throw new RuntimeException("Email không được để trống");
        if (createMode && (request.getMatKhau() == null || request.getMatKhau().isBlank())) throw new RuntimeException("Mật khẩu không được để trống");
        if (request.getLoaiTaiKhoan() == null || request.getLoaiTaiKhoan().isBlank()) throw new RuntimeException("Loại tài khoản không được để trống");
        if (request.getRoles() == null || request.getRoles().isEmpty()) throw new RuntimeException("Tài khoản phải có ít nhất một vai trò");
    }

    private void ganVaiTro(TaiKhoan taiKhoan, List<String> roleCodes) {
        taiKhoan.getTaiKhoanVaiTros().clear();
        if (roleCodes == null || roleCodes.isEmpty()) return;
        Set<String> uniqueRoleCodes = new LinkedHashSet<>(roleCodes.stream().filter(role -> role != null && !role.isBlank()).map(role -> role.trim().toUpperCase()).toList());
        for (String roleCode : uniqueRoleCodes) {
            VaiTro vaiTro = vaiTroRepository.findByMaVaiTro(roleCode).orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy vai trò: " + roleCode));
            TaiKhoanVaiTro taiKhoanVaiTro = new TaiKhoanVaiTro();
            taiKhoanVaiTro.setTaiKhoan(taiKhoan);
            taiKhoanVaiTro.setVaiTro(vaiTro);
            taiKhoan.getTaiKhoanVaiTros().add(taiKhoanVaiTro);
        }
    }
}
