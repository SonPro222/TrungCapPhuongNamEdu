package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhService;
import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChuongTrinhServiceImpl implements ChuongTrinhService {
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;
    private final ChuongTrinhRepository repository;
    private final ChuongTrinhMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChuongTrinhResponse> findAllByNganhId(Long nganhId, Pageable pageable) {
        return repository.findByNganhId(nganhId, pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhResponse findById(Long id) {
        ChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinh không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public ChuongTrinhResponse create(ChuongTrinhRequest request) {
        validator.validateChuongTrinh(request, null);

        ChuongTrinh entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ChuongTrinhResponse update(Long id, ChuongTrinhRequest request) {
        validator.validateChuongTrinh(request, id);

        ChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChuongTrinh không tồn tại: " + id));

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChuongTrinh không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoChuongTrinhId(id);
    }
}
