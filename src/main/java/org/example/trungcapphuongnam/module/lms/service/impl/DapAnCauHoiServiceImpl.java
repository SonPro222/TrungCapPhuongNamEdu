package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.DapAnCauHoi;
import org.example.trungcapphuongnam.module.lms.mapper.DapAnCauHoiMapper;
import org.example.trungcapphuongnam.module.lms.repository.DapAnCauHoiRepository;
import org.example.trungcapphuongnam.module.lms.service.DapAnCauHoiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DapAnCauHoiServiceImpl implements DapAnCauHoiService {

    private final DapAnCauHoiRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<DapAnCauHoiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(DapAnCauHoiMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DapAnCauHoiResponse findById(Long id) {
        return DapAnCauHoiMapper.toResponse(getEntity(id));
    }

    @Override
    public DapAnCauHoiResponse create(DapAnCauHoiRequest request) {
        DapAnCauHoi entity = DapAnCauHoiMapper.toEntity(request);
        return DapAnCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public DapAnCauHoiResponse update(Long id, DapAnCauHoiRequest request) {
        DapAnCauHoi entity = getEntity(id);
        DapAnCauHoiMapper.updateEntity(entity, request);
        return DapAnCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        DapAnCauHoi entity = getEntity(id);
        repository.delete(entity);
    }

    private DapAnCauHoi getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DapAnCauHoi với id = " + id));
    }
}
