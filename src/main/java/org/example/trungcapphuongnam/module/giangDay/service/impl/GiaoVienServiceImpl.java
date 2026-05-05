package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.GiaoVienRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.GiaoVienResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.GiaoVien;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.GiaoVienMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.GiaoVienRepository;
import org.example.trungcapphuongnam.module.giangDay.service.GiaoVienService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class GiaoVienServiceImpl implements GiaoVienService {

    private final GiaoVienRepository repository;
    private final GiaoVienMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<GiaoVienResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GiaoVienResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public GiaoVienResponse create(GiaoVienRequest request) {
        validate(request);
        GiaoVien entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public GiaoVienResponse update(Long id, GiaoVienRequest request) {
        validate(request);
        GiaoVien entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        GiaoVien entity = findEntity(id);
        repository.delete(entity);
    }

    private GiaoVien findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("GiaoVien không tồn tại với id = " + id));
    }

    private void validate(GiaoVienRequest request) {
        // Không có rule validate đặc biệt.
    }
}
