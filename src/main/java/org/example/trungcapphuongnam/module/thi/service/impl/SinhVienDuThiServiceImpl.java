package org.example.trungcapphuongnam.module.thi.service.impl;

import org.example.trungcapphuongnam.module.thi.dto.request.SinhVienDuThiRequest;
import org.example.trungcapphuongnam.module.thi.dto.response.SinhVienDuThiResponse;
import org.example.trungcapphuongnam.module.thi.entity.SinhVienDuThi;
import org.example.trungcapphuongnam.common.exception.ThiException;
import org.example.trungcapphuongnam.common.exception.ThiNotFoundException;
import org.example.trungcapphuongnam.module.thi.mapper.SinhVienDuThiMapper;
import org.example.trungcapphuongnam.module.thi.repository.SinhVienDuThiRepository;
import org.example.trungcapphuongnam.module.thi.service.SinhVienDuThiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SinhVienDuThiServiceImpl implements SinhVienDuThiService {
    private final SinhVienDuThiRepository repository;
    private final SinhVienDuThiMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SinhVienDuThiResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SinhVienDuThiResponse findById(Long id) {
        return mapper.toResponse(getById(id));
    }

    @Override
    public SinhVienDuThiResponse create(SinhVienDuThiRequest request) {
        if (repository.existsByLichThiIdAndSinhVienId(request.getLichThiId(), request.getSinhVienId())) {
            throw new ThiException("Sinh viên đã có trong lịch thi này");
        }
        return mapper.toResponse(repository.save(mapper.toEntity(request)));
    }

    @Override
    public SinhVienDuThiResponse update(Long id, SinhVienDuThiRequest request) {
        SinhVienDuThi entity = getById(id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private SinhVienDuThi getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ThiNotFoundException("SinhVienDuThi", id));
    }
}
