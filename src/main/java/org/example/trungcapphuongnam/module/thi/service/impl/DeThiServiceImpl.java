package org.example.trungcapphuongnam.module.thi.service.impl;

import org.example.trungcapphuongnam.module.thi.dto.request.DeThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.DeThiResponse;
import org.example.trungcapphuongnam.module.thi.entity.DeThi;
import org.example.trungcapphuongnam.common.exception.ThiNotFoundException;
import org.example.trungcapphuongnam.module.thi.mapper.DeThiMapper;
import org.example.trungcapphuongnam.module.thi.repository.DeThiRepository;
import org.example.trungcapphuongnam.module.thi.service.DeThiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DeThiServiceImpl implements DeThiService {
    private final DeThiRepository repository;
    private final DeThiMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DeThiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DeThiResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public DeThiResponse create(DeThiRequest request) {
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public DeThiResponse update(Long id, DeThiRequest request) {
        DeThi entity = getById(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private DeThi getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ThiNotFoundException("DeThi", id));
    }
}
