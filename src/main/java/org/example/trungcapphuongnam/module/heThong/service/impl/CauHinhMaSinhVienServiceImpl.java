package org.example.trungcapphuongnam.module.heThong.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.heThong.HeThongNotFoundException;
import org.example.trungcapphuongnam.module.heThong.dto.request.CauHinhMaSinhVienRequest;
import org.example.trungcapphuongnam.module.heThong.dto.response.CauHinhMaSinhVienResponse;
import org.example.trungcapphuongnam.module.heThong.entity.CauHinhMaSinhVien;
import org.example.trungcapphuongnam.module.heThong.repository.CauHinhMaSinhVienRepository;
import org.example.trungcapphuongnam.module.heThong.service.CauHinhMaSinhVienService;
import org.example.trungcapphuongnam.module.heThong.validator.CauHinhMaSinhVienValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
@Service
@RequiredArgsConstructor
@Transactional
public class CauHinhMaSinhVienServiceImpl implements CauHinhMaSinhVienService {

    private final CauHinhMaSinhVienRepository repository;
    private final NganhRepository nganhRepository;
    private final ChuongTrinhVersionRepository chuongTrinhVersionRepository;
    private final CauHinhMaSinhVienValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHinhMaSinhVienResponse> findAll(Long nganhId, Long chuongTrinhVersionId, Pageable pageable) {
        Specification<CauHinhMaSinhVien> spec = Specification.where(null);

        if (nganhId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("nganhId"), nganhId));
        }

        if (chuongTrinhVersionId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("chuongTrinhVersionId"), chuongTrinhVersionId));
        }

        return repository.findAll(spec, pageable).map(this::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public CauHinhMaSinhVienResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public CauHinhMaSinhVienResponse create(CauHinhMaSinhVienRequest request) {
        validator.validateCreate(request);

        CauHinhMaSinhVien entity = CauHinhMaSinhVien.builder()
                .nganhId(request.getNganhId())
                .chuongTrinhVersionId(request.getChuongTrinhVersionId())
                .tienTo(validator.chuanHoaTienTo(request.getTienTo()))
                .maDau(validator.chuanHoaMaDau(request.getMaDau()))
                .soHienTai(validator.chuanHoaSoHienTai(request.getSoHienTai()))
                .doDaiSoThuTu(validator.chuanHoaDoDaiSoThuTu(request.getDoDaiSoThuTu()))
                .ghiChu(request.getGhiChu())
                .build();

        return toResponse(repository.save(entity));
    }

    @Override
    public CauHinhMaSinhVienResponse update(Long id, CauHinhMaSinhVienRequest request) {
        validator.validateUpdate(id, request);

        CauHinhMaSinhVien entity = getById(id);

        entity.setNganhId(request.getNganhId());
        entity.setChuongTrinhVersionId(request.getChuongTrinhVersionId());
        entity.setTienTo(validator.chuanHoaTienTo(request.getTienTo()));
        entity.setMaDau(validator.chuanHoaMaDau(request.getMaDau()));
        entity.setSoHienTai(validator.chuanHoaSoHienTai(request.getSoHienTai()));
        entity.setDoDaiSoThuTu(validator.chuanHoaDoDaiSoThuTu(request.getDoDaiSoThuTu()));
        entity.setGhiChu(request.getGhiChu());

        return toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        validator.validateDelete(id);
        repository.delete(getById(id));
    }

    private CauHinhMaSinhVien getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new HeThongNotFoundException("Không tìm thấy cấu hình mã sinh viên với id = " + id));
    }

    private CauHinhMaSinhVienResponse toResponse(CauHinhMaSinhVien entity) {
        Nganh nganh = nganhRepository.findById(entity.getNganhId()).orElse(null);
        ChuongTrinhVersion version = chuongTrinhVersionRepository.findById(entity.getChuongTrinhVersionId()).orElse(null);

        int soTiepTheo = entity.getSoHienTai() == null ? 1 : entity.getSoHienTai() + 1;
        String maTiepTheo = entity.getTienTo()
                + entity.getMaDau()
                + String.format("%0" + entity.getDoDaiSoThuTu() + "d", soTiepTheo);

        return CauHinhMaSinhVienResponse.builder()
                .id(entity.getId())
                .nganhId(entity.getNganhId())
                .maNganh(nganh == null ? null : nganh.getMaNganh())
                .tenNganh(nganh == null ? null : nganh.getTenNganh())
                .chuongTrinhVersionId(entity.getChuongTrinhVersionId())
                .maVersion(version == null ? null : version.getMaVersion())
                .tenVersion(version == null ? null : version.getTenVersion())
                .tienTo(entity.getTienTo())
                .maDau(entity.getMaDau())
                .soHienTai(entity.getSoHienTai())
                .doDaiSoThuTu(entity.getDoDaiSoThuTu())
                .ghiChu(entity.getGhiChu())
                .maSinhVienTiepTheo(maTiepTheo)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}