package org.example.trungcapphuongnam.module.daoTao.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.DuplicateResourceException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.util.TextUtil;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhRequest;
import org.example.trungcapphuongnam.module.daoTao.dto.NganhResponse;
import org.example.trungcapphuongnam.module.daoTao.entity.Nganh;
import org.example.trungcapphuongnam.module.daoTao.mapper.NganhMapper;
import org.example.trungcapphuongnam.module.daoTao.repository.NganhRepository;
import org.example.trungcapphuongnam.module.daoTao.service.NganhService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NganhServiceImpl implements NganhService {

    private final NganhRepository repository;
    private final NganhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NganhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NganhResponse findById(Long id) {
        return mapper.toResponse(getEntity(id));
    }

    @Override
    public NganhResponse create(NganhRequest request) {
        request.setMaNganh(TextUtil.trimRequired(request.getMaNganh()));
        request.setTenNganh(TextUtil.trimRequired(request.getTenNganh()));
        if (repository.existsByMaNganh(request.getMaNganh())) {
            throw new DuplicateResourceException("Mã ngành đã tồn tại: " + request.getMaNganh());
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public NganhResponse update(Long id, NganhRequest request) {
        Nganh entity = getEntity(id);
        request.setMaNganh(TextUtil.trimRequired(request.getMaNganh()));
        request.setTenNganh(TextUtil.trimRequired(request.getTenNganh()));
        if (repository.existsByMaNganhAndIdNot(request.getMaNganh(), id)) {
            throw new DuplicateResourceException("Mã ngành đã tồn tại: " + request.getMaNganh());
        }
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Ngành không tồn tại: " + id);
        repository.deleteById(id);
    }

    private Nganh getEntity(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ngành không tồn tại: " + id));
    }
}
