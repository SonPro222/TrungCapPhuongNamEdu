package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRa;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NangLucDauRaMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NangLucDauRaRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NangLucDauRaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NangLucDauRaServiceImpl implements NangLucDauRaService {

    private final NangLucDauRaRepository repository;
    private final NangLucDauRaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NangLucDauRaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NangLucDauRaResponse findById(Long id) {
        NangLucDauRa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NangLucDauRa không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public NangLucDauRaResponse create(NangLucDauRaRequest request) {
        NangLucDauRa entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NangLucDauRaResponse update(Long id, NangLucDauRaRequest request) {
        NangLucDauRa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NangLucDauRa không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("NangLucDauRa không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
