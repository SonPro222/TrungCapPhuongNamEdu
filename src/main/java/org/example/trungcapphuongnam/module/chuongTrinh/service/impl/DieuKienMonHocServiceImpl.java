package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.DieuKienMonHocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.DieuKienMonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienMonHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DieuKienMonHocServiceImpl implements DieuKienMonHocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final DieuKienMonHocRepository repository;
    private final DieuKienMonHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DieuKienMonHocResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<DieuKienMonHocResponse> findAllBySyllabusMonId(
            Long syllabusMonId,
            Pageable pageable
    ) {
        return repository.findBySyllabusMonId(
                syllabusMonId,
                pageable
        ).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public DieuKienMonHocResponse findById(Long id) {
        DieuKienMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DieuKienMonHoc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public DieuKienMonHocResponse create(DieuKienMonHocRequest request) {
        DieuKienMonHoc entity = mapper.toEntity(request);
        validator.validateDieuKienMonHoc(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DieuKienMonHocResponse update(Long id, DieuKienMonHocRequest request) {
        DieuKienMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DieuKienMonHoc không tồn tại: " + id));
        validator.validateDieuKienMonHoc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DieuKienMonHoc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
