package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusTaiLieuRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusTaiLieuResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusTaiLieu;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusTaiLieuMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusTaiLieuRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusTaiLieuService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusTaiLieuServiceImpl implements SyllabusTaiLieuService {

    private final SyllabusTaiLieuRepository repository;
    private final SyllabusTaiLieuMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusTaiLieuResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusTaiLieuResponse findById(Long id) {
        SyllabusTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusTaiLieu không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusTaiLieuResponse create(SyllabusTaiLieuRequest request) {
        SyllabusTaiLieu entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusTaiLieuResponse update(Long id, SyllabusTaiLieuRequest request) {
        SyllabusTaiLieu entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusTaiLieu không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SyllabusTaiLieu không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
