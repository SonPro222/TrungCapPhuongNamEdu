package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.LichSuBaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.LichSuBaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.entity.LichSuBaiTapLop;
import org.example.trungcapphuongnam.module.lms.mapper.LichSuBaiTapLopMapper;
import org.example.trungcapphuongnam.module.lms.repository.LichSuBaiTapLopRepository;
import org.example.trungcapphuongnam.module.lms.service.LichSuBaiTapLopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LichSuBaiTapLopServiceImpl implements LichSuBaiTapLopService {

    private final LichSuBaiTapLopRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<LichSuBaiTapLopResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(LichSuBaiTapLopMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LichSuBaiTapLopResponse findById(Long id) {
        return LichSuBaiTapLopMapper.toResponse(getEntity(id));
    }

    @Override
    public LichSuBaiTapLopResponse create(LichSuBaiTapLopRequest request) {
        LichSuBaiTapLop entity = LichSuBaiTapLopMapper.toEntity(request);
        return LichSuBaiTapLopMapper.toResponse(repository.save(entity));
    }

    @Override
    public LichSuBaiTapLopResponse update(Long id, LichSuBaiTapLopRequest request) {
        LichSuBaiTapLop entity = getEntity(id);
        LichSuBaiTapLopMapper.updateEntity(entity, request);
        return LichSuBaiTapLopMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LichSuBaiTapLop entity = getEntity(id);
        repository.delete(entity);
    }

    private LichSuBaiTapLop getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy LichSuBaiTapLop với id = " + id));
    }
}
