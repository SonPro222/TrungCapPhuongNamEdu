package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.DiemDanhRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.DiemDanhResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.DiemDanh;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.DiemDanhMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.DiemDanhRepository;
import org.example.trungcapphuongnam.module.giangDay.service.DiemDanhService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiemDanhServiceImpl implements DiemDanhService {

    private final DiemDanhRepository repository;
    private final DiemDanhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DiemDanhResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DiemDanhResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public DiemDanhResponse create(DiemDanhRequest request) {
        validate(request);
        DiemDanh entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DiemDanhResponse update(Long id, DiemDanhRequest request) {
        validate(request);
        DiemDanh entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        DiemDanh entity = findEntity(id);
        repository.delete(entity);
    }

    private DiemDanh findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("DiemDanh không tồn tại với id = " + id));
    }

    private void validate(DiemDanhRequest request) {
        // Không có rule validate đặc biệt.
    }
}
