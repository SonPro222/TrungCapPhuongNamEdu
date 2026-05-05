package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhongHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhongHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.PhongHoc;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.PhongHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.PhongHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.PhongHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhongHocServiceImpl implements PhongHocService {

    private final PhongHocRepository repository;
    private final PhongHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PhongHocResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PhongHocResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public PhongHocResponse create(PhongHocRequest request) {
        validate(request);
        PhongHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public PhongHocResponse update(Long id, PhongHocRequest request) {
        validate(request);
        PhongHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        PhongHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private PhongHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("PhongHoc không tồn tại với id = " + id));
    }

    private void validate(PhongHocRequest request) {
        // Không có rule validate đặc biệt.
    }
}
