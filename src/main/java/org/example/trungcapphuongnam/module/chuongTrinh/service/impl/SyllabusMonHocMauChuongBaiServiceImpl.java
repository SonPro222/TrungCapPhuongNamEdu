package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocMauChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocMauChuongBaiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocMauChuongBai;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocMauChuongBaiMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocMauChuongBaiRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocMauChuongBaiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocMauChuongBaiServiceImpl implements SyllabusMonHocMauChuongBaiService {

    private final SyllabusMonHocMauChuongBaiRepository repository;
    private final SyllabusMonHocMauChuongBaiMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocMauChuongBaiResponse> findAll(
            Long syllabusMonHocMauId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocMauChuongBai>empty()
                        .and(LocJpa.eq("syllabusMonHocMauId", syllabusMonHocMauId))
                        .and(LocJpa.keyword(keyword, "ten", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocMauChuongBaiResponse findById(Long id) {
        SyllabusMonHocMauChuongBai entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chương bài syllabus gốc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocMauChuongBaiResponse create(SyllabusMonHocMauChuongBaiRequest request) {
        validator.validateSyllabusMonHocMauChuongBai(request, null);

        SyllabusMonHocMauChuongBai entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocMauChuongBaiResponse update(Long id, SyllabusMonHocMauChuongBaiRequest request) {
        SyllabusMonHocMauChuongBai entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chương bài syllabus gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocMauChuongBai(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Chương bài syllabus gốc không tồn tại: " + id);
        }

        repository.deleteById(id);
    }
}