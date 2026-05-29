package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionService;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionServiceImpl implements ChuongTrinhVersionService {
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final ChuongTrinhVersionRepository repository;
    private final ChuongTrinhVersionMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionResponse> findAll(Long chuongTrinhId, Boolean laHienHanh, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<ChuongTrinhVersion>empty()
                    .and(LocJpa.eq("chuongTrinhId", chuongTrinhId))
                    .and(LocJpa.eq("laHienHanh", laHienHanh))
                    .and(LocJpa.keyword(keyword, "maVersion", "tenVersion", "soQuyetDinh", "nguoiKy", "coQuanBanHanh", "fileQuyetDinh")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionResponse findById(Long id) {
        ChuongTrinhVersion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public ChuongTrinhVersionResponse create(ChuongTrinhVersionRequest request) {
        validator.validateChuongTrinhVersion(request, null);

        ChuongTrinhVersion entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }
    @Override
    public ChuongTrinhVersionResponse update(Long id, ChuongTrinhVersionRequest request) {
        validator.validateChuongTrinhVersion(request, id);

        ChuongTrinhVersion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id));

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoChuongTrinhVersionId(id);
    }
}
