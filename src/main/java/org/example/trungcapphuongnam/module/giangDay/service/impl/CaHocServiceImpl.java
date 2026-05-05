package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.CaHocRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.CaHocResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.CaHoc;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.CaHocMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.CaHocRepository;
import org.example.trungcapphuongnam.module.giangDay.service.CaHocService;
import org.example.trungcapphuongnam.common.util.GiangDayValidateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CaHocServiceImpl implements CaHocService {

    private final CaHocRepository repository;
    private final CaHocMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CaHocResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CaHocResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public CaHocResponse create(CaHocRequest request) {
        validate(request);
        CaHoc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public CaHocResponse update(Long id, CaHocRequest request) {
        validate(request);
        CaHoc entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CaHoc entity = findEntity(id);
        repository.delete(entity);
    }

    private CaHoc findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("CaHoc không tồn tại với id = " + id));
    }

    private void validate(CaHocRequest request) {
        GiangDayValidateUtil.requireEndTimeAfterStart(request.getGioBatDau(), request.getGioKetThuc(), "Giờ kết thúc phải sau giờ bắt đầu");
    }
}
