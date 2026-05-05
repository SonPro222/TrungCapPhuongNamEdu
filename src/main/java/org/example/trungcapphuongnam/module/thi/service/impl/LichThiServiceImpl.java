package org.example.trungcapphuongnam.module.thi.service.impl;

import org.example.trungcapphuongnam.module.thi.dto.request.LichThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.LichThiResponse;
import org.example.trungcapphuongnam.module.thi.entity.LichThi;
import org.example.trungcapphuongnam.common.exception.ThiException;
import org.example.trungcapphuongnam.common.exception.ThiNotFoundException;
import org.example.trungcapphuongnam.module.thi.mapper.LichThiMapper;
import org.example.trungcapphuongnam.module.thi.repository.LichThiRepository;
import org.example.trungcapphuongnam.module.thi.service.LichThiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class LichThiServiceImpl implements LichThiService {
    private final LichThiRepository repository;
    private final LichThiMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<LichThiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LichThiResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public LichThiResponse create(LichThiRequest request) {
        if (request.getNgayThi() == null) {
            throw new ThiException("Ngày thi không được để trống");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public LichThiResponse update(Long id, LichThiRequest request) {
        LichThi entity = getById(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private LichThi getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ThiNotFoundException("LichThi", id));
    }
}
