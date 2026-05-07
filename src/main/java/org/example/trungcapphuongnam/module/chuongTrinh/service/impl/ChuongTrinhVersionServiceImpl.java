package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhVersionRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhVersionResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinhVersion;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhVersionMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhVersionRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhVersionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhVersionServiceImpl implements ChuongTrinhVersionService {

    private final ChuongTrinhVersionRepository repository;
    private final ChuongTrinhVersionMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhVersionResponse> findAllByChuongTrinhId(Long chuongTrinhId, Pageable pageable) {
        return repository.findByChuongTrinhId(chuongTrinhId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhVersionResponse findById(Long id) {
        ChuongTrinhVersion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public ChuongTrinhVersionResponse create(ChuongTrinhVersionRequest request) {
        ChuongTrinhVersion entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhVersionResponse update(Long id, ChuongTrinhVersionRequest request) {
        ChuongTrinhVersion entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChuongTrinhVersion không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
