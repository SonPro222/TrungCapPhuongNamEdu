package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauDieuKien;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocMauDieuKienMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocMauDieuKienRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauDieuKienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocMauDieuKienServiceImpl implements SyllabusMonHocMauDieuKienService {

    private final SyllabusMonHocMauDieuKienRepository repository;
    private final SyllabusMonHocMauDieuKienMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocMauDieuKienResponse> findAll(
            Long syllabusMonHocMauId,
            Long dieuKienMauId,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocMauDieuKien>empty()
                        .and(LocJpa.eq("syllabusMonHocMauId", syllabusMonHocMauId))
                        .and(LocJpa.eq("dieuKienMauId", dieuKienMauId)),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauDieuKienResponse findById(Long id) {
        SyllabusMonHocMauDieuKien entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện syllabus gốc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocMauDieuKienResponse create(SyllabusMonHocMauDieuKienRequest request) {
        validator.validateSyllabusMonHocMauDieuKien(request, null);

        SyllabusMonHocMauDieuKien entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocMauDieuKienResponse update(Long id, SyllabusMonHocMauDieuKienRequest request) {
        SyllabusMonHocMauDieuKien entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện syllabus gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocMauDieuKien(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Điều kiện syllabus gốc không tồn tại: " + id);
        }

        repository.deleteById(id);
    }
}