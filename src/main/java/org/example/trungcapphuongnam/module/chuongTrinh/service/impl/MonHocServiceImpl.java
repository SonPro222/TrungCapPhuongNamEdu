package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonHocRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonHocResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonHoc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MonHocMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonHocRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MonHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class MonHocServiceImpl implements MonHocService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final MonHocRepository repository;
    private final MonHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MonHocResponse> findAll(String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.keyword(keyword, "maMon", "tenMon", "moTa"),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MonHocResponse findById(Long id) {
        MonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MonHoc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public MonHocResponse create(MonHocRequest request) {
        validator.validateMonHoc(request, null);
        MonHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MonHocResponse update(Long id, MonHocRequest request) {
        validator.validateMonHoc(request, id);
        MonHoc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MonHoc không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MonHoc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
