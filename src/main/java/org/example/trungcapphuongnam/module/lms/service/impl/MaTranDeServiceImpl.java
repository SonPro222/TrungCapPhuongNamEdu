package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeResponse;
import org.example.trungcapphuongnam.module.lms.entity.MaTranDe;
import org.example.trungcapphuongnam.module.lms.mapper.MaTranDeMapper;
import org.example.trungcapphuongnam.module.lms.repository.MaTranDeRepository;
import org.example.trungcapphuongnam.module.lms.service.MaTranDeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaTranDeServiceImpl implements MaTranDeService {

    private final MaTranDeRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<MaTranDeResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(MaTranDeMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MaTranDeResponse findById(Long id) {
        return MaTranDeMapper.toResponse(getEntity(id));
    }

    @Override
    public MaTranDeResponse create(MaTranDeRequest request) {
        MaTranDe entity = MaTranDeMapper.toEntity(request);
        return MaTranDeMapper.toResponse(repository.save(entity));
    }

    @Override
    public MaTranDeResponse update(Long id, MaTranDeRequest request) {
        MaTranDe entity = getEntity(id);
        MaTranDeMapper.updateEntity(entity, request);
        return MaTranDeMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        MaTranDe entity = getEntity(id);
        repository.delete(entity);
    }

    private MaTranDe getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy MaTranDe với id = " + id));
    }
}
