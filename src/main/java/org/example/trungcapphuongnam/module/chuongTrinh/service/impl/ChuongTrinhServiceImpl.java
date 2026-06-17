package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhService;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhHeDaoTao;
import org.example.trungcapphuongnam.module.daoTao.entity.NganhLoaiChuongTrinh;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhHeDaoTaoRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhLoaiChuongTrinhRepository;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhServiceImpl implements ChuongTrinhService {
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final ChuongTrinhRepository repository;
    private final ChuongTrinhMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    private final NganhLoaiChuongTrinhRepository nganhLoaiChuongTrinhRepository;
    private final NganhHeDaoTaoRepository nganhHeDaoTaoRepository;
    private final NganhRepository nganhRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhResponse> findAll(
            Long nganhHeDaoTaoId,
            Long nganhId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<ChuongTrinh>empty()
                        .and(LocJpa.eq("nganhHeDaoTaoId", nganhHeDaoTaoId))
                        .and(LocJpa.eq("nganhId", nganhId))
                        .and(LocJpa.keyword(keyword, "maChuongTrinh", "tenChuongTrinh", "doiTuongTuyenSinh", "thoiGianDaoTao")),
                pageable
        ).map(this::toResponseEnriched);
    }

    public Page<ChuongTrinhResponse> findAllByNganhHeDaoTao(
            Long nganhHeDaoTaoId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<ChuongTrinh>empty()
                        .and(LocJpa.eq("nganhHeDaoTaoId", nganhHeDaoTaoId))
                        .and(LocJpa.keyword(keyword, "maChuongTrinh", "tenChuongTrinh", "doiTuongTuyenSinh", "thoiGianDaoTao")),
                pageable
        ).map(this::toResponseEnriched);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhResponse findById(Long id) {
        ChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinh không tồn tại: " + id));
        return toResponseEnriched(entity);
    }

    @Override
    public ChuongTrinhResponse create(ChuongTrinhRequest request) {
        validator.validateChuongTrinh(request, null);
        ChuongTrinh entity = mapper.toEntity(request);
        ganThongTinNganh(entity, request);
        return toResponseEnriched(repository.save(entity));
    }

    @Override
    public ChuongTrinhResponse update(Long id, ChuongTrinhRequest request) {
        validator.validateChuongTrinh(request, id);
        ChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinh không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        ganThongTinNganh(entity, request);
        return toResponseEnriched(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChuongTrinh không tồn tại: " + id);
        }
        xoaChuongTrinhCascadeService.xoaTheoChuongTrinhId(id);
    }

    // ---- helpers ----

    private void ganThongTinNganh(ChuongTrinh entity, ChuongTrinhRequest request) {
        if (request.getNganhHeDaoTaoId() == null) {
            throw new BadRequestException("nganhHeDaoTaoId không được để trống");
        }

        ganNganhHeDaoTao(entity, request.getNganhHeDaoTaoId());
    }

    private void ganNganhHeDaoTao(ChuongTrinh entity, Long nganhHeDaoTaoId) {
        NganhHeDaoTao nganhHe = nganhHeDaoTaoRepository.findById(nganhHeDaoTaoId)
                .orElseThrow(() -> new ResourceNotFoundException("Ngành hệ đào tạo không tồn tại: " + nganhHeDaoTaoId));

        entity.setNganhHeDaoTaoId(nganhHe.getId());

        entity.setNganhId(nganhHe.getNganhId());

        if (entity.getThoiGianDaoTao() == null || entity.getThoiGianDaoTao().isBlank()) {
            if (nganhHe.getSoThang() != null && nganhHe.getSoKy() != null) {
                entity.setThoiGianDaoTao(nganhHe.getSoThang() + " tháng / " + nganhHe.getSoKy() + " kỳ");
            }
        }
    }


    private ChuongTrinhResponse toResponseEnriched(ChuongTrinh entity) {
        NganhHeDaoTao nganhHe = entity.getNganhHeDaoTaoId() != null
                ? nganhHeDaoTaoRepository.findById(entity.getNganhHeDaoTaoId()).orElse(null)
                : null;

        Nganh nganh = null;
        if (nganhHe != null && nganhHe.getNganhId() != null) {
            nganh = nganhRepository.findById(nganhHe.getNganhId()).orElse(null);
        }

        return mapper.toResponseEnriched(entity, nganh, nganhHe);
    }
}
