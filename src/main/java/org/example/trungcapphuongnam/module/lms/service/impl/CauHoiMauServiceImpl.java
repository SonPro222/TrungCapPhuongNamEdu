package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiGocRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiGocResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiMau;
import org.example.trungcapphuongnam.module.lms.mapper.CauHoiMauMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauHoiMauRepository;
import org.example.trungcapphuongnam.module.lms.service.CauHoiGocService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHoiGocServiceImpl implements CauHoiGocService {

    private final CauHoiMauRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHoiGocResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CauHoiMauMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHoiGocResponse findById(Long id) {
        return CauHoiMauMapper.toResponse(getEntity(id));
    }

    @Override
    public CauHoiGocResponse create(CauHoiGocRequest request) {
        CauHoiMau entity = CauHoiMauMapper.toEntity(request);
        return CauHoiMauMapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHoiGocResponse update(Long id, CauHoiGocRequest request) {
        CauHoiMau entity = getEntity(id);
        CauHoiMauMapper.updateEntity(entity, request);
        return CauHoiMauMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CauHoiMau entity = getEntity(id);
        repository.delete(entity);
    }

    private CauHoiMau getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauHoiMau với id = " + id));
    }
}
