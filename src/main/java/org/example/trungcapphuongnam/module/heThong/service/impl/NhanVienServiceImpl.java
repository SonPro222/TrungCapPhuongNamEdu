package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.heThong.dto.request.NhanVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.NhanVienResponse;
import org.example.trungcapphuongnam.module.heThong.entity.NhanVien;
import org.example.trungcapphuongnam.module.heThong.entity.TaiKhoan;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.mapper.NhanVienMapper;
import org.example.trungcapphuongnam.module.heThong.repository.NhanVienRepository;
import org.example.trungcapphuongnam.module.heThong.repository.TaiKhoanRepository;
import org.example.trungcapphuongnam.module.heThong.service.NhanVienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
@Service
@RequiredArgsConstructor
@Transactional
public class NhanVienServiceImpl implements NhanVienService {
    private final NhanVienRepository nhanVienRepository;
    private final TaiKhoanRepository taiKhoanRepository;
    private final NhanVienMapper nhanVienMapper;

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
    public Page<NhanVienResponse> search(String keyword, String phongBan, String chucVu, String trangThai, Integer namBatDau, Pageable pageable) {
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
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("trangThai").as(String.class)), trangThai.trim().toLowerCase()));
        }
        if (namBatDau != null) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.function("year", Integer.class, root.get("createdAt")), namBatDau));
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
        NhanVien entity = nhanVienMapper.toEntity(request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhanVienMapper.toResponse(nhanVienRepository.save(entity));
    }

    @Override
    public NhanVienResponse update(Long id, NhanVienRequest request) {
        NhanVien entity = findByIdOrThrow(id);
        nhanVienMapper.updateEntity(entity, request);
        entity.setTaiKhoan(resolveTaiKhoan(request == null ? null : request.getTaiKhoanId()));
        return nhanVienMapper.toResponse(nhanVienRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        nhanVienRepository.delete(findByIdOrThrow(id));
    }

    private NhanVien findByIdOrThrow(Long id) {
        return nhanVienRepository.findById(id).orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy nhân viên với id = " + id));
    }

    private TaiKhoan resolveTaiKhoan(Long taiKhoanId) {
        if (taiKhoanId == null) return null;
        return taiKhoanRepository.findById(taiKhoanId).orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy tài khoản với id = " + taiKhoanId));
    }
}
