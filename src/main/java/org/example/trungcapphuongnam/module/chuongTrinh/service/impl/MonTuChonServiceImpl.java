package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTuChonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTuChon;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MonTuChonMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonTuChonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MonTuChonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MonTuChonServiceImpl implements MonTuChonService {

    private final MonTuChonRepository repository;
    private final MonTuChonMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MonTuChonResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MonTuChonResponse findById(Long id) {
        MonTuChon entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MonTuChon không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public MonTuChonResponse create(MonTuChonRequest request) {
        MonTuChon entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MonTuChonResponse update(Long id, MonTuChonRequest request) {
        MonTuChon entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MonTuChon không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MonTuChon không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
