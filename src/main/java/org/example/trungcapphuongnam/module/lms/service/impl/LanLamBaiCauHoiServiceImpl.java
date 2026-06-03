package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LanLamBaiCauHoiRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LanLamBaiCauHoiResponse;
import org.example.trungcapphuongnam.module.lms.entity.LanLamBaiCauHoi;
import org.example.trungcapphuongnam.module.lms.mapper.LanLamBaiCauHoiMapper;
import org.example.trungcapphuongnam.module.lms.repository.LanLamBaiCauHoiRepository;
import org.example.trungcapphuongnam.module.lms.service.LanLamBaiCauHoiService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LanLamBaiCauHoiServiceImpl implements LanLamBaiCauHoiService {

    private final LanLamBaiCauHoiRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LanLamBaiCauHoiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LanLamBaiCauHoiMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LanLamBaiCauHoiResponse findById(Long id) {
        return LanLamBaiCauHoiMapper.toResponse(getEntity(id));
    }

    @Override
    public LanLamBaiCauHoiResponse create(LanLamBaiCauHoiRequest request) {
        LanLamBaiCauHoi entity = LanLamBaiCauHoiMapper.toEntity(request);
        return LanLamBaiCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public LanLamBaiCauHoiResponse update(Long id, LanLamBaiCauHoiRequest request) {
        LanLamBaiCauHoi entity = getEntity(id);
        LanLamBaiCauHoiMapper.updateEntity(entity, request);
        return LanLamBaiCauHoiMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LanLamBaiCauHoi entity = getEntity(id);
        repository.delete(entity);
    }

    private LanLamBaiCauHoi getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LanLamBaiCauHoi với id = " + id));
    }
}
