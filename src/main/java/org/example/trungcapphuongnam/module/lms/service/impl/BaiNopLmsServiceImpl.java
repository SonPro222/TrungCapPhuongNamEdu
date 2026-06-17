package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiNopLms;
import org.example.trungcapphuongnam.module.lms.mapper.BaiNopLmsMapper;
import org.example.trungcapphuongnam.module.lms.repository.BaiNopLmsRepository;
import org.example.trungcapphuongnam.module.lms.service.BaiNopLmsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BaiNopLmsServiceImpl implements BaiNopLmsService {

    private final BaiNopLmsRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiNopLmsResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BaiNopLmsMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiNopLmsResponse findById(Long id) {
        return BaiNopLmsMapper.toResponse(getEntity(id));
    }

    @Override
    public BaiNopLmsResponse create(BaiNopLmsRequest request) {
        BaiNopLms entity = BaiNopLmsMapper.toEntity(request);
        return BaiNopLmsMapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiNopLmsResponse update(Long id, BaiNopLmsRequest request) {
        BaiNopLms entity = getEntity(id);
        BaiNopLmsMapper.updateEntity(entity, request);
        return BaiNopLmsMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        BaiNopLms entity = getEntity(id);
        repository.delete(entity);
    }

    private BaiNopLms getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy BaiNopLms với id = " + id));
    }
}
