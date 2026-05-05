package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiOnline;
import org.example.trungcapphuongnam.module.lms.mapper.LanLamBaiOnlineMapper;
import org.example.trungcapphuongnam.module.lms.repository.LanLamBaiOnlineRepository;
import org.example.trungcapphuongnam.module.lms.service.LanLamBaiOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LanLamBaiOnlineServiceImpl implements LanLamBaiOnlineService {

    private final LanLamBaiOnlineRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LanLamBaiOnlineResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LanLamBaiOnlineMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LanLamBaiOnlineResponse findById(Long id) {
        return LanLamBaiOnlineMapper.toResponse(getEntity(id));
    }

    @Override
    public LanLamBaiOnlineResponse create(LanLamBaiOnlineRequest request) {
        LanLamBaiOnline entity = LanLamBaiOnlineMapper.toEntity(request);
        return LanLamBaiOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public LanLamBaiOnlineResponse update(Long id, LanLamBaiOnlineRequest request) {
        LanLamBaiOnline entity = getEntity(id);
        LanLamBaiOnlineMapper.updateEntity(entity, request);
        return LanLamBaiOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LanLamBaiOnline entity = getEntity(id);
        repository.delete(entity);
    }

    private LanLamBaiOnline getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LanLamBaiOnline với id = " + id));
    }
}
