package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuCauHoi;
import org.example.trungcapphuongnam.module.lms.mapper.LichSuCauHoiMapper;
import org.example.trungcapphuongnam.module.lms.repository.LichSuCauHoiRepository;
import org.example.trungcapphuongnam.module.lms.service.LichSuCauHoiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LichSuCauHoiServiceImpl implements LichSuCauHoiService {

    private final LichSuCauHoiRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LichSuCauHoiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LichSuCauHoiMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LichSuCauHoiResponse findById(Long id) {
        return LichSuCauHoiMapper.toResponse(getEntity(id));
    }

    @Override
    public LichSuCauHoiResponse create(LichSuCauHoiRequest request) {
        LichSuCauHoi entity = LichSuCauHoiMapper.toEntity(request);
        return LichSuCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public LichSuCauHoiResponse update(Long id, LichSuCauHoiRequest request) {
        LichSuCauHoi entity = getEntity(id);
        LichSuCauHoiMapper.updateEntity(entity, request);
        return LichSuCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LichSuCauHoi entity = getEntity(id);
        repository.delete(entity);
    }

    private LichSuCauHoi getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LichSuCauHoi với id = " + id));
    }
}
