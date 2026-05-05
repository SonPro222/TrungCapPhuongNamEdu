package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusMonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusMonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusMonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusMonHocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusMonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusMonHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusMonHocServiceImpl implements SyllabusMonHocService {

    private final SyllabusMonHocRepository repository;
    private final SyllabusMonHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusMonHocResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusMonHocResponse findById(Long id) {
        SyllabusMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusMonHocResponse create(SyllabusMonHocRequest request) {
        SyllabusMonHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusMonHocResponse update(Long id, SyllabusMonHocRequest request) {
        SyllabusMonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SyllabusMonHoc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
