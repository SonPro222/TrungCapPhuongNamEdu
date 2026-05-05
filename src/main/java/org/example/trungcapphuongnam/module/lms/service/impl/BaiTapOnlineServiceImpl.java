package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.BaiTapOnlineRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.BaiTapOnlineResponse;
import org.example.trungcapphuongnam.module.lms.entity.BaiTapOnline;
import org.example.trungcapphuongnam.module.lms.mapper.BaiTapOnlineMapper;
import org.example.trungcapphuongnam.module.lms.repository.BaiTapOnlineRepository;
import org.example.trungcapphuongnam.module.lms.service.BaiTapOnlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BaiTapOnlineServiceImpl implements BaiTapOnlineService {

    private final BaiTapOnlineRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<BaiTapOnlineResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(BaiTapOnlineMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BaiTapOnlineResponse findById(Long id) {
        return BaiTapOnlineMapper.toResponse(getEntity(id));
    }

    @Override
    public BaiTapOnlineResponse create(BaiTapOnlineRequest request) {
        BaiTapOnline entity = BaiTapOnlineMapper.toEntity(request);
        return BaiTapOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public BaiTapOnlineResponse update(Long id, BaiTapOnlineRequest request) {
        BaiTapOnline entity = getEntity(id);
        BaiTapOnlineMapper.updateEntity(entity, request);
        return BaiTapOnlineMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        BaiTapOnline entity = getEntity(id);
        repository.delete(entity);
    }

    private BaiTapOnline getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy BaiTapOnline với id = " + id));
    }
}
