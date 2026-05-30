package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhMonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhMonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhMon;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMonMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhMonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhMonService;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.common.spec.LocJpa;
@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhMonServiceImpl implements ChuongTrinhMonService {
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final ChuongTrinhMonRepository repository;
    private final ChuongTrinhMonMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhMonResponse> findAll(Long chuongTrinhVersionId, Long khungKyId, Long monHocId, Long nhomKienThucId, String loai, String loaiHocPhan, Boolean batBuoc, Boolean laMonDieuKien, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhMon>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.eq("khungKyId", khungKyId))
                    .and(LocJpa.eq("monHocId", monHocId))
                    .and(LocJpa.eq("nhomKienThucId", nhomKienThucId))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.like("loaiHocPhan", loaiHocPhan))
                    .and(LocJpa.eq("batBuoc", batBuoc))
                    .and(LocJpa.eq("laMonDieuKien", laMonDieuKien))
                    .and(LocJpa.keyword(keyword, "maMonTrongCt", "loai", "loaiHocPhan", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhMonResponse findById(Long id) {
        ChuongTrinhMon entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhMon không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public ChuongTrinhMonResponse create(ChuongTrinhMonRequest request) {
        validator.validateChuongTrinhMon(request, null);

        ChuongTrinhMon entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhMonResponse update(Long id, ChuongTrinhMonRequest request) {
        validator.validateChuongTrinhMon(request, id);

        ChuongTrinhMon entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhMon không tồn tại: " + id));

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChuongTrinhMon không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoChuongTrinhMonId(id);
    }
}
