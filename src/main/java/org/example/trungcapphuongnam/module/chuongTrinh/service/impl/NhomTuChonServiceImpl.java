package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomTuChonRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomTuChonResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomTuChon;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NhomTuChonMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NhomTuChonRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomTuChonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NhomTuChonServiceImpl implements NhomTuChonService {

    private final NhomTuChonRepository repository;
    private final NhomTuChonMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NhomTuChonResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhomTuChonResponse findById(Long id) {
        NhomTuChon entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NhomTuChon không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public NhomTuChonResponse create(NhomTuChonRequest request) {
        NhomTuChon entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NhomTuChonResponse update(Long id, NhomTuChonRequest request) {
        NhomTuChon entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NhomTuChon không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("NhomTuChon không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
