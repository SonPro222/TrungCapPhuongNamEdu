package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapLopRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapLopResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiTapLop;
import org.example.trungcapphuongnam.module.lms.mapper.BaiTapLopMapper;
import org.example.trungcapphuongnam.module.lms.repository.BaiTapLopRepository;
import org.example.trungcapphuongnam.module.lms.service.BaiTapLopService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BaiTapLopServiceImpl implements BaiTapLopService {

    private final BaiTapLopRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiTapLopResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BaiTapLopMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiTapLopResponse findById(Long id) {
        return BaiTapLopMapper.toResponse(getEntity(id));
    }

    @Override
    public BaiTapLopResponse create(BaiTapLopRequest request) {
        BaiTapLop entity = BaiTapLopMapper.toEntity(request);
        return BaiTapLopMapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiTapLopResponse update(Long id, BaiTapLopRequest request) {
        BaiTapLop entity = getEntity(id);
        BaiTapLopMapper.updateEntity(entity, request);
        return BaiTapLopMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        BaiTapLop entity = getEntity(id);
        repository.delete(entity);
    }

    private BaiTapLop getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy BaiTapLop với id = " + id));
    }
}
