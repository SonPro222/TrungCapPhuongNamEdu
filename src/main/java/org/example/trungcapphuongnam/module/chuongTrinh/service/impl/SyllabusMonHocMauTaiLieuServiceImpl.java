package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauTaiLieu;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocMauTaiLieuMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocMauTaiLieuRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauTaiLieuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocMauTaiLieuServiceImpl implements SyllabusMonHocMauTaiLieuService {

    private final SyllabusMonHocMauTaiLieuRepository repository;
    private final SyllabusMonHocMauTaiLieuMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocMauTaiLieuResponse> findAll(
            Long syllabusMonHocMauId,
            Long taiLieuMauId,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocMauTaiLieu>empty()
                        .and(LocJpa.eq("syllabusMonHocMauId", syllabusMonHocMauId))
                        .and(LocJpa.eq("taiLieuMauId", taiLieuMauId)),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauTaiLieuResponse findById(Long id) {
        SyllabusMonHocMauTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu syllabus gốc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocMauTaiLieuResponse create(SyllabusMonHocMauTaiLieuRequest request) {
        validator.validateSyllabusMonHocMauTaiLieu(request, null);

        SyllabusMonHocMauTaiLieu entity = mapper.toEntity(request);

        if (entity.getBatBuoc() == null) {
            entity.setBatBuoc(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocMauTaiLieuResponse update(Long id, SyllabusMonHocMauTaiLieuRequest request) {
        SyllabusMonHocMauTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu syllabus gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocMauTaiLieu(request, id);

        mapper.updateEntity(entity, request);

        if (entity.getBatBuoc() == null) {
            entity.setBatBuoc(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tài liệu syllabus gốc không tồn tại: " + id);
        }

        repository.deleteById(id);
    }
}