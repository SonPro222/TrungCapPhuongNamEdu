package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocGocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocGocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocGoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocGocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocGocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocGocService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocGocServiceImpl implements SyllabusMonHocGocService {

    private final SyllabusMonHocGocRepository repository;
    private final SyllabusMonHocGocMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocGocResponse> findAll(Long monHocId, String ma, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocGoc>empty()
                        .and(LocJpa.eq("monHocId", monHocId))
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "mucTieu", "phuongPhapDanhGia", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocGocResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id));
    }

    @Override
    public SyllabusMonHocGocResponse create(SyllabusMonHocGocRequest request) {
        validator.validateSyllabusMonHocGoc(request, null);

        SyllabusMonHocGoc entity = mapper.toEntity(request);

        if (entity.getBatBuocDuThi() == null) {
            entity.setBatBuocDuThi(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocGocResponse update(Long id, SyllabusMonHocGocRequest request) {
        SyllabusMonHocGoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id));

        validator.validateSyllabusMonHocGoc(request, id);

        mapper.updateEntity(entity, request);

        if (entity.getBatBuocDuThi() == null) {
            entity.setBatBuocDuThi(false);
        }

        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Syllabus môn học gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoSyllabusMonHocGocId(id);

        repository.deleteById(id);
    }
}