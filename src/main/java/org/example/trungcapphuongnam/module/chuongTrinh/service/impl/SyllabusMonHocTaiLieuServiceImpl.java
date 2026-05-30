package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocTaiLieu;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocTaiLieuMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocTaiLieuRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocTaiLieuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocTaiLieuServiceImpl implements SyllabusMonHocTaiLieuService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final SyllabusMonHocTaiLieuRepository repository;
    private final SyllabusMonHocTaiLieuMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocTaiLieuResponse> findAll(
            Long syllabusMonId,
            Long taiLieuGocId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocTaiLieu>empty()
                        .and(LocJpa.eq("syllabusMonId", syllabusMonId))
                        .and(LocJpa.eq("taiLieuGocId", taiLieuGocId))
                        .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocTaiLieuResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu gán vào syllabus môn học không tồn tại: " + id));
    }

    @Override
    public SyllabusMonHocTaiLieuResponse create(SyllabusMonHocTaiLieuRequest request) {
        validator.validateSyllabusMonHocTaiLieu(request, null);
        SyllabusMonHocTaiLieu entity = mapper.toEntity(request);

        if (entity.getBatBuoc() == null) {
            entity.setBatBuoc(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocTaiLieuResponse update(Long id, SyllabusMonHocTaiLieuRequest request) {
        SyllabusMonHocTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tài liệu gán vào syllabus môn học không tồn tại: " + id));

        validator.validateSyllabusMonHocTaiLieu(request, id);

        mapper.updateEntity(entity, request);

        if (entity.getBatBuoc() == null) {
            entity.setBatBuoc(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Tài liệu gán vào syllabus môn học không tồn tại: " + id);
        }

        repository.deleteById(id);
    }



}