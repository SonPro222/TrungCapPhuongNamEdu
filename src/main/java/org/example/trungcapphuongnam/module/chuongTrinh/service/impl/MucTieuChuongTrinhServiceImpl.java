package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MucTieuChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MucTieuChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MucTieuChuongTrinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MucTieuChuongTrinhServiceImpl implements MucTieuChuongTrinhService {

    private final MucTieuChuongTrinhRepository repository;
    private final MucTieuChuongTrinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MucTieuChuongTrinhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MucTieuChuongTrinhResponse findById(Long id) {
        MucTieuChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MucTieuChuongTrinh không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public MucTieuChuongTrinhResponse create(MucTieuChuongTrinhRequest request) {
        MucTieuChuongTrinh entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MucTieuChuongTrinhResponse update(Long id, MucTieuChuongTrinhRequest request) {
        MucTieuChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MucTieuChuongTrinh không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MucTieuChuongTrinh không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
