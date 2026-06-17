package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.CauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.CauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiVersion;
import org.example.trungcapphuongnam.module.lms.mapper.CauHoiVersionMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauHoiVersionRepository;
import org.example.trungcapphuongnam.module.lms.service.CauHoiVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CauHoiVersionServiceImpl implements CauHoiVersionService {

    private final CauHoiVersionRepository repository;

    @Override
    @Transactional(readOnly = true)
    public Page<CauHoiVersionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(CauHoiVersionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public CauHoiVersionResponse findById(Long id) {
        return CauHoiVersionMapper.toResponse(getEntity(id));
    }

    @Override
    public CauHoiVersionResponse create(CauHoiVersionRequest request) {
        CauHoiVersion entity = CauHoiVersionMapper.toEntity(request);
        if (entity.getIsLocked() == null) {
            entity.setIsLocked(false);
        }
        return CauHoiVersionMapper.toResponse(repository.save(entity));
    }

    @Override
    public CauHoiVersionResponse update(Long id, CauHoiVersionRequest request) {
        CauHoiVersion entity = getEntity(id);
        if (Boolean.TRUE.equals(entity.getIsLocked())) {
            throw new BadRequestException("Câu hỏi version đã được sử dụng, không được sửa trực tiếp. Hãy tạo version mới.");
        }
        CauHoiVersionMapper.updateEntity(entity, request);
        return CauHoiVersionMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        CauHoiVersion entity = getEntity(id);
        if (Boolean.TRUE.equals(entity.getIsLocked())) {
            throw new BadRequestException("Câu hỏi version đã được sử dụng, không được xóa trực tiếp");
        }
        repository.delete(entity);
    }

    private CauHoiVersion getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauHoiVersion với id = " + id));
    }
}
