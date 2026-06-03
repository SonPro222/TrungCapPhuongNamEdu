package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiGocRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiGocResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiGoc;
import org.example.trungcapphuongnam.module.lms.mapper.CauHoiGocMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauHoiGocRepository;
import org.example.trungcapphuongnam.module.lms.service.CauHoiGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHoiGocServiceImpl implements CauHoiGocService {

    private final CauHoiGocRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHoiGocResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CauHoiGocMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHoiGocResponse findById(Long id) {
        return CauHoiGocMapper.toResponse(getEntity(id));
    }

    @Override
    public CauHoiGocResponse create(CauHoiGocRequest request) {
        CauHoiGoc entity = CauHoiGocMapper.toEntity(request);
        return CauHoiGocMapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHoiGocResponse update(Long id, CauHoiGocRequest request) {
        CauHoiGoc entity = getEntity(id);
        CauHoiGocMapper.updateEntity(entity, request);
        return CauHoiGocMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CauHoiGoc entity = getEntity(id);
        repository.delete(entity);
    }

    private CauHoiGoc getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauHoiGoc với id = " + id));
    }
}
