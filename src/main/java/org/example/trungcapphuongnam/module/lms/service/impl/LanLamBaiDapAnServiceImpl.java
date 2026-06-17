package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiDapAnResponse;
import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiDapAn;
import org.example.trungcapphuongnam.module.lms.mapper.LanLamBaiDapAnMapper;
import org.example.trungcapphuongnam.module.lms.repository.LanLamBaiDapAnRepository;
import org.example.trungcapphuongnam.module.lms.service.LanLamBaiDapAnService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LanLamBaiDapAnServiceImpl implements LanLamBaiDapAnService {

    private final LanLamBaiDapAnRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LanLamBaiDapAnResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LanLamBaiDapAnMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LanLamBaiDapAnResponse findById(Long id) {
        return LanLamBaiDapAnMapper.toResponse(getEntity(id));
    }

    @Override
    public LanLamBaiDapAnResponse create(LanLamBaiDapAnRequest request) {
        LanLamBaiDapAn entity = LanLamBaiDapAnMapper.toEntity(request);
        return LanLamBaiDapAnMapper.toResponse(repository.save(entity));
    }

    @Override
    public LanLamBaiDapAnResponse update(Long id, LanLamBaiDapAnRequest request) {
        LanLamBaiDapAn entity = getEntity(id);
        LanLamBaiDapAnMapper.updateEntity(entity, request);
        return LanLamBaiDapAnMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LanLamBaiDapAn entity = getEntity(id);
        repository.delete(entity);
    }

    private LanLamBaiDapAn getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LanLamBaiDapAn với id = " + id));
    }
}
