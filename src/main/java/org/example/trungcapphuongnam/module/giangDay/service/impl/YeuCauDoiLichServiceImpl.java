package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.YeuCauDoiLichRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.YeuCauDoiLichResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.YeuCauDoiLich;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.YeuCauDoiLichMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.YeuCauDoiLichRepository;
import org.example.trungcapphuongnam.module.giangDay.service.YeuCauDoiLichService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class YeuCauDoiLichServiceImpl implements YeuCauDoiLichService {

    private final YeuCauDoiLichRepository repository;
    private final YeuCauDoiLichMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<YeuCauDoiLichResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public YeuCauDoiLichResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public YeuCauDoiLichResponse create(YeuCauDoiLichRequest request) {
        validate(request);
        YeuCauDoiLich entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public YeuCauDoiLichResponse update(Long id, YeuCauDoiLichRequest request) {
        validate(request);
        YeuCauDoiLich entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        YeuCauDoiLich entity = findEntity(id);
        repository.delete(entity);
    }

    private YeuCauDoiLich findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("YeuCauDoiLich không tồn tại với id = " + id));
    }

    private void validate(YeuCauDoiLichRequest request) {
        // Không có rule validate đặc biệt.
    }
}
