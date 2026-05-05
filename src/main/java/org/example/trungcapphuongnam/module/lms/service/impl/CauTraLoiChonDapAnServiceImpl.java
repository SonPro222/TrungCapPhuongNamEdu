package org.example.trungcapphuongnam.module.lms.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.CauTraLoiChonDapAnRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauTraLoiChonDapAnResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauTraLoiChonDapAn;
import org.example.trungcapphuongnam.module.lms.mapper.CauTraLoiChonDapAnMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauTraLoiChonDapAnRepository;
import org.example.trungcapphuongnam.module.lms.service.CauTraLoiChonDapAnService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CauTraLoiChonDapAnServiceImpl implements CauTraLoiChonDapAnService {

    private final CauTraLoiChonDapAnRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CauTraLoiChonDapAnResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CauTraLoiChonDapAnMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauTraLoiChonDapAnResponse findById(Long id) {
        return CauTraLoiChonDapAnMapper.toResponse(getEntity(id));
    }

    @Override
    public CauTraLoiChonDapAnResponse create(CauTraLoiChonDapAnRequest request) {
        CauTraLoiChonDapAn entity = CauTraLoiChonDapAnMapper.toEntity(request);
        return CauTraLoiChonDapAnMapper.toResponse(repository.save(entity));
    }

    @Override
    public CauTraLoiChonDapAnResponse update(Long id, CauTraLoiChonDapAnRequest request) {
        CauTraLoiChonDapAn entity = getEntity(id);
        CauTraLoiChonDapAnMapper.updateEntity(entity, request);
        return CauTraLoiChonDapAnMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CauTraLoiChonDapAn entity = getEntity(id);
        repository.delete(entity);
    }

    private CauTraLoiChonDapAn getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauTraLoiChonDapAn với id = " + id));
    }
}
