package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuDayDiemLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuDayDiemLmsResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuDayDiemLms;
import org.example.trungcapphuongnam.module.lms.mapper.LichSuDayDiemLmsMapper;
import org.example.trungcapphuongnam.module.lms.repository.LichSuDayDiemLmsRepository;
import org.example.trungcapphuongnam.module.lms.service.LichSuDayDiemLmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LichSuDayDiemLmsServiceImpl implements LichSuDayDiemLmsService {

    private final LichSuDayDiemLmsRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LichSuDayDiemLmsResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LichSuDayDiemLmsMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LichSuDayDiemLmsResponse findById(Long id) {
        return LichSuDayDiemLmsMapper.toResponse(getEntity(id));
    }

    @Override
    public LichSuDayDiemLmsResponse create(LichSuDayDiemLmsRequest request) {
        LichSuDayDiemLms entity = LichSuDayDiemLmsMapper.toEntity(request);
        return LichSuDayDiemLmsMapper.toResponse(repository.save(entity));
    }

    @Override
    public LichSuDayDiemLmsResponse update(Long id, LichSuDayDiemLmsRequest request) {
        LichSuDayDiemLms entity = getEntity(id);
        LichSuDayDiemLmsMapper.updateEntity(entity, request);
        return LichSuDayDiemLmsMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LichSuDayDiemLms entity = getEntity(id);
        repository.delete(entity);
    }

    private LichSuDayDiemLms getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LichSuDayDiemLms với id = " + id));
    }
}
