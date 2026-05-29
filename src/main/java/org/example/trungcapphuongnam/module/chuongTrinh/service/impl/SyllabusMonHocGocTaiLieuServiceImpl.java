package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocTaiLieu;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocGocTaiLieuMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocGocTaiLieuRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocGocTaiLieuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocGocTaiLieuServiceImpl implements SyllabusMonHocGocTaiLieuService {

    private final SyllabusMonHocGocTaiLieuRepository repository;
    private final SyllabusMonHocGocTaiLieuMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocGocTaiLieuResponse> findAll(
            Long syllabusMonHocGocId,
            Long taiLieuGocId,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocGocTaiLieu>empty()
                        .and(LocJpa.eq("syllabusMonHocGocId", syllabusMonHocGocId))
                        .and(LocJpa.eq("taiLieuGocId", taiLieuGocId)),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocGocTaiLieuResponse findById(Long id) {
        SyllabusMonHocGocTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu syllabus gốc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocGocTaiLieuResponse create(SyllabusMonHocGocTaiLieuRequest request) {
        validator.validateSyllabusMonHocGocTaiLieu(request, null);

        SyllabusMonHocGocTaiLieu entity = mapper.toEntity(request);

        if (entity.getBatBuoc() == null) {
            entity.setBatBuoc(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocGocTaiLieuResponse update(Long id, SyllabusMonHocGocTaiLieuRequest request) {
        SyllabusMonHocGocTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu syllabus gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocGocTaiLieu(request, id);

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