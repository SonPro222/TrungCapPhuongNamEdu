package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.QuyDoiDiemRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.QuyDoiDiemResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.QuyDoiDiem;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.QuyDoiDiemMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.QuyDoiDiemRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.QuyDoiDiemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class QuyDoiDiemServiceImpl implements QuyDoiDiemService {

    private final QuyDoiDiemRepository repository;
    private final QuyDoiDiemMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<QuyDoiDiemResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public QuyDoiDiemResponse findById(Long id) {
        QuyDoiDiem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public QuyDoiDiemResponse create(QuyDoiDiemRequest request) {
        QuyDoiDiem entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public QuyDoiDiemResponse update(Long id, QuyDoiDiemRequest request) {
        QuyDoiDiem entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id));
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("QuyDoiDiem không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
