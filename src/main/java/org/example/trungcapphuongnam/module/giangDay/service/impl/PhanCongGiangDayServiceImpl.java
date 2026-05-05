package org.example.trungcapphuongnam.module.giangDay.service.impl;

import org.example.trungcapphuongnam.module.giangDay.dto.request.PhanCongGiangDayRequest;
import org.example.trungcapphuongnam.module.giangDay.dto.response.PhanCongGiangDayResponse;
import org.example.trungcapphuongnam.module.giangDay.entity.PhanCongGiangDay;
import org.example.trungcapphuongnam.module.giangDay.exception.GiangDayNotFoundException;
import org.example.trungcapphuongnam.module.giangDay.mapper.PhanCongGiangDayMapper;
import org.example.trungcapphuongnam.module.giangDay.repository.PhanCongGiangDayRepository;
import org.example.trungcapphuongnam.module.giangDay.service.PhanCongGiangDayService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhanCongGiangDayServiceImpl implements PhanCongGiangDayService {

    private final PhanCongGiangDayRepository repository;
    private final PhanCongGiangDayMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<PhanCongGiangDayResponse> getAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PhanCongGiangDayResponse getById(Long id) {
        return mapper.toResponse(findEntity(id));
    }

    @Override
    public PhanCongGiangDayResponse create(PhanCongGiangDayRequest request) {
        validate(request);
        PhanCongGiangDay entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public PhanCongGiangDayResponse update(Long id, PhanCongGiangDayRequest request) {
        validate(request);
        PhanCongGiangDay entity = findEntity(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        PhanCongGiangDay entity = findEntity(id);
        repository.delete(entity);
    }

    private PhanCongGiangDay findEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new GiangDayNotFoundException("PhanCongGiangDay không tồn tại với id = " + id));
    }

    private void validate(PhanCongGiangDayRequest request) {
        // Không có rule validate đặc biệt.
    }
}
