package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocChuongBaiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGocChuongBai;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocGocChuongBaiMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocGocChuongBaiRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocGocChuongBaiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocGocChuongBaiServiceImpl implements SyllabusMonHocGocChuongBaiService {

    private final SyllabusMonHocGocChuongBaiRepository repository;
    private final SyllabusMonHocGocChuongBaiMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocGocChuongBaiResponse> findAll(
            Long syllabusMonHocGocId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocGocChuongBai>empty()
                        .and(LocJpa.eq("syllabusMonHocGocId", syllabusMonHocGocId))
                        .and(LocJpa.keyword(keyword, "ten", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocGocChuongBaiResponse findById(Long id) {
        SyllabusMonHocGocChuongBai entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chương bài syllabus gốc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocGocChuongBaiResponse create(SyllabusMonHocGocChuongBaiRequest request) {
        validator.validateSyllabusMonHocGocChuongBai(request, null);

        SyllabusMonHocGocChuongBai entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocGocChuongBaiResponse update(Long id, SyllabusMonHocGocChuongBaiRequest request) {
        SyllabusMonHocGocChuongBai entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chương bài syllabus gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocGocChuongBai(request, id);

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