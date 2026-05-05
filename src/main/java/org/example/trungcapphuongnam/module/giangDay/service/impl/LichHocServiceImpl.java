package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.LichHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.LichHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.LichHoc;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.LichHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.LichHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.LichHocService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LichHocServiceImpl implements LichHocService {

    private final LichHocRepository repository;
    private final LichHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<LichHocResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LichHocResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public LichHocResponse create(LichHocRequest request) {
        validate(request);
        LichHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public LichHocResponse update(Long id, LichHocRequest request) {
        validate(request);
        LichHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        LichHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private LichHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("LichHoc không tồn tại với id = " + id));
    }

    private void validate(LichHocRequest request) {
        // Không có rule validate đặc biệt.
    }
}
