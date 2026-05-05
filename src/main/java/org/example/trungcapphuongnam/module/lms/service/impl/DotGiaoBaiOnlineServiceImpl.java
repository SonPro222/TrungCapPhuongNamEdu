package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.DotGiaoBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DotGiaoBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.DotGiaoBaiOnline;
import org.example.trungcapphuongnam.module.lms.mapper.DotGiaoBaiOnlineMapper;
import org.example.trungcapphuongnam.module.lms.repository.DotGiaoBaiOnlineRepository;
import org.example.trungcapphuongnam.module.lms.service.DotGiaoBaiOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DotGiaoBaiOnlineServiceImpl implements DotGiaoBaiOnlineService {

    private final DotGiaoBaiOnlineRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<DotGiaoBaiOnlineResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(DotGiaoBaiOnlineMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DotGiaoBaiOnlineResponse findById(Long id) {
        return DotGiaoBaiOnlineMapper.toResponse(getEntity(id));
    }

    @Override
    public DotGiaoBaiOnlineResponse create(DotGiaoBaiOnlineRequest request) {
        DotGiaoBaiOnline entity = DotGiaoBaiOnlineMapper.toEntity(request);
        return DotGiaoBaiOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public DotGiaoBaiOnlineResponse update(Long id, DotGiaoBaiOnlineRequest request) {
        DotGiaoBaiOnline entity = getEntity(id);
        DotGiaoBaiOnlineMapper.updateEntity(entity, request);
        return DotGiaoBaiOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        DotGiaoBaiOnline entity = getEntity(id);
        repository.delete(entity);
    }

    private DotGiaoBaiOnline getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DotGiaoBaiOnline với id = " + id));
    }
}
