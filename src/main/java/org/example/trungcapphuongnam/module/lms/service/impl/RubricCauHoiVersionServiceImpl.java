package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.RubricCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.RubricCauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.entity.RubricCauHoiVersion;
import org.example.trungcapphuongnam.module.lms.mapper.RubricCauHoiVersionMapper;
import org.example.trungcapphuongnam.module.lms.repository.RubricCauHoiVersionRepository;
import org.example.trungcapphuongnam.module.lms.service.RubricCauHoiVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RubricCauHoiVersionServiceImpl implements RubricCauHoiVersionService {

    private final RubricCauHoiVersionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<RubricCauHoiVersionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(RubricCauHoiVersionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public RubricCauHoiVersionResponse findById(Long id) {
        return RubricCauHoiVersionMapper.toResponse(getEntity(id));
    }

    @Override
    public RubricCauHoiVersionResponse create(RubricCauHoiVersionRequest request) {
        RubricCauHoiVersion entity = RubricCauHoiVersionMapper.toEntity(request);
        return RubricCauHoiVersionMapper.toResponse(repository.save(entity));
    }

    @Override
    public RubricCauHoiVersionResponse update(Long id, RubricCauHoiVersionRequest request) {
        RubricCauHoiVersion entity = getEntity(id);
        RubricCauHoiVersionMapper.updateEntity(entity, request);
        return RubricCauHoiVersionMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        RubricCauHoiVersion entity = getEntity(id);
        repository.delete(entity);
    }

    private RubricCauHoiVersion getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy RubricCauHoiVersion với id = " + id));
    }
}
