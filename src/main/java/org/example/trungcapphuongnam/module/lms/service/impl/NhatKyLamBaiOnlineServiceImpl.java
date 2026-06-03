package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.NhatKyLamBaiOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.NhatKyLamBaiOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.NhatKyLamBaiOnline;
import org.example.trungcapphuongnam.module.lms.mapper.NhatKyLamBaiOnlineMapper;
import org.example.trungcapphuongnam.module.lms.repository.NhatKyLamBaiOnlineRepository;
import org.example.trungcapphuongnam.module.lms.service.NhatKyLamBaiOnlineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NhatKyLamBaiOnlineServiceImpl implements NhatKyLamBaiOnlineService {

    private final NhatKyLamBaiOnlineRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<NhatKyLamBaiOnlineResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(NhatKyLamBaiOnlineMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhatKyLamBaiOnlineResponse findById(Long id) {
        return NhatKyLamBaiOnlineMapper.toResponse(getEntity(id));
    }

    @Override
    public NhatKyLamBaiOnlineResponse create(NhatKyLamBaiOnlineRequest request) {
        NhatKyLamBaiOnline entity = NhatKyLamBaiOnlineMapper.toEntity(request);
        return NhatKyLamBaiOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public NhatKyLamBaiOnlineResponse update(Long id, NhatKyLamBaiOnlineRequest request) {
        NhatKyLamBaiOnline entity = getEntity(id);
        NhatKyLamBaiOnlineMapper.updateEntity(entity, request);
        return NhatKyLamBaiOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        NhatKyLamBaiOnline entity = getEntity(id);
        repository.delete(entity);
    }

    private NhatKyLamBaiOnline getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy NhatKyLamBaiOnline với id = " + id));
    }
}
