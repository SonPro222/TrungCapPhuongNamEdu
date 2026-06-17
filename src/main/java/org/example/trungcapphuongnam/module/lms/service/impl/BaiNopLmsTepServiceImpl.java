package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiNopLmsTepRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiNopLmsTepResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiNopLmsTep;
import org.example.trungcapphuongnam.module.lms.mapper.BaiNopLmsTepMapper;
import org.example.trungcapphuongnam.module.lms.repository.BaiNopLmsTepRepository;
import org.example.trungcapphuongnam.module.lms.service.BaiNopLmsTepService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BaiNopLmsTepServiceImpl implements BaiNopLmsTepService {

    private final BaiNopLmsTepRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiNopLmsTepResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BaiNopLmsTepMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiNopLmsTepResponse findById(Long id) {
        return BaiNopLmsTepMapper.toResponse(getEntity(id));
    }

    @Override
    public BaiNopLmsTepResponse create(BaiNopLmsTepRequest request) {
        BaiNopLmsTep entity = BaiNopLmsTepMapper.toEntity(request);
        return BaiNopLmsTepMapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiNopLmsTepResponse update(Long id, BaiNopLmsTepRequest request) {
        BaiNopLmsTep entity = getEntity(id);
        BaiNopLmsTepMapper.updateEntity(entity, request);
        return BaiNopLmsTepMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        BaiNopLmsTep entity = getEntity(id);
        repository.delete(entity);
    }

    private BaiNopLmsTep getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy BaiNopLmsTep với id = " + id));
    }
}
