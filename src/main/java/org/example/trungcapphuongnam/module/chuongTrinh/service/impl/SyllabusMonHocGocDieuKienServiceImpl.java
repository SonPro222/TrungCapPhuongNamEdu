package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocDieuKien;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocGocDieuKienMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocGocDieuKienRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocGocDieuKienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocGocDieuKienServiceImpl implements SyllabusMonHocGocDieuKienService {

    private final SyllabusMonHocGocDieuKienRepository repository;
    private final SyllabusMonHocGocDieuKienMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocGocDieuKienResponse> findAll(
            Long syllabusMonHocGocId,
            Long dieuKienGocId,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocGocDieuKien>empty()
                        .and(LocJpa.eq("syllabusMonHocGocId", syllabusMonHocGocId))
                        .and(LocJpa.eq("dieuKienGocId", dieuKienGocId)),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocGocDieuKienResponse findById(Long id) {
        SyllabusMonHocGocDieuKien entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện syllabus gốc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocGocDieuKienResponse create(SyllabusMonHocGocDieuKienRequest request) {
        validator.validateSyllabusMonHocGocDieuKien(request, null);

        SyllabusMonHocGocDieuKien entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocGocDieuKienResponse update(Long id, SyllabusMonHocGocDieuKienRequest request) {
        SyllabusMonHocGocDieuKien entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện syllabus gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocGocDieuKien(request, id);

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