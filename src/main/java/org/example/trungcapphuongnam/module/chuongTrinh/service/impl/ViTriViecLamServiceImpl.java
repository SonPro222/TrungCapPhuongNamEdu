package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLam;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ViTriViecLamMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ViTriViecLamRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ViTriViecLamService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ViTriViecLamServiceImpl implements ViTriViecLamService {

    private final ViTriViecLamRepository repository;
    private final ViTriViecLamMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ViTriViecLamResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ViTriViecLamResponse findById(Long id) {
        ViTriViecLam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViTriViecLam không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public ViTriViecLamResponse create(ViTriViecLamRequest request) {
        ViTriViecLam entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ViTriViecLamResponse update(Long id, ViTriViecLamRequest request) {
        ViTriViecLam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViTriViecLam không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ViTriViecLam không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
