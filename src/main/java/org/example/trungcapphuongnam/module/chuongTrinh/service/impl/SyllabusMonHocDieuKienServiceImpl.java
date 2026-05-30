package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;


import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocDieuKienRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocDieuKienResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHocDieuKien;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocDieuKienMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocDieuKienRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocDieuKienService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocDieuKienServiceImpl implements SyllabusMonHocDieuKienService {

    private final SyllabusMonHocDieuKienRepository repository;

    private final SyllabusMonHocDieuKienMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocDieuKienResponse> findAll(
            Long syllabusMonId,
            Long dieuKienGocId,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<SyllabusMonHocDieuKien>empty()
                        .and(LocJpa.eq("syllabusMonId", syllabusMonId))
                        .and(LocJpa.eq("dieuKienGocId", dieuKienGocId))
                        .and(LocJpa.keyword(keyword, "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocDieuKienResponse findById(Long id) {
        return repository.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện gán vào syllabus môn học không tồn tại: " + id));
    }

    @Override
    public SyllabusMonHocDieuKienResponse create(SyllabusMonHocDieuKienRequest request) {
        validator.validateSyllabusMonHocDieuKien(request, null);

        SyllabusMonHocDieuKien entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocDieuKienResponse update(Long id, SyllabusMonHocDieuKienRequest request) {
        SyllabusMonHocDieuKien entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Điều kiện gán vào syllabus môn học không tồn tại: " + id));

        validator.validateSyllabusMonHocDieuKien(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Điều kiện gán vào syllabus môn học không tồn tại: " + id);
        }

        repository.deleteById(id);
    }

}