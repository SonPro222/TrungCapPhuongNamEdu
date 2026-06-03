package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.MaTranDeChiTietRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.MaTranDeChiTietResponse;
import org.example.trungcapphuongnam.module.lms.entity.MaTranDeChiTiet;
import org.example.trungcapphuongnam.module.lms.mapper.MaTranDeChiTietMapper;
import org.example.trungcapphuongnam.module.lms.repository.MaTranDeChiTietRepository;
import org.example.trungcapphuongnam.module.lms.service.MaTranDeChiTietService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MaTranDeChiTietServiceImpl implements MaTranDeChiTietService {

    private final MaTranDeChiTietRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<MaTranDeChiTietResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(MaTranDeChiTietMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MaTranDeChiTietResponse findById(Long id) {
        return MaTranDeChiTietMapper.toResponse(getEntity(id));
    }

    @Override
    public MaTranDeChiTietResponse create(MaTranDeChiTietRequest request) {
        MaTranDeChiTiet entity = MaTranDeChiTietMapper.toEntity(request);
        return MaTranDeChiTietMapper.toResponse(repository.save(entity));
    }

    @Override
    public MaTranDeChiTietResponse update(Long id, MaTranDeChiTietRequest request) {
        MaTranDeChiTiet entity = getEntity(id);
        MaTranDeChiTietMapper.updateEntity(entity, request);
        return MaTranDeChiTietMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        MaTranDeChiTiet entity = getEntity(id);
        repository.delete(entity);
    }

    private MaTranDeChiTiet getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy MaTranDeChiTiet với id = " + id));
    }
}
