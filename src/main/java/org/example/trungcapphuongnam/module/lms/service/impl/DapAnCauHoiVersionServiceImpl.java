package org.example.trungcapphuongnam.module.lms.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.BadRequestException;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.lms.dto.request.DapAnCauHoiVersionRequest;
import org.example.trungcapphuongnam.module.lms.dto.response.DapAnCauHoiVersionResponse;
import org.example.trungcapphuongnam.module.lms.entity.CauHoiVersion;
import org.example.trungcapphuongnam.module.lms.entity.DapAnCauHoiVersion;
import org.example.trungcapphuongnam.module.lms.mapper.DapAnCauHoiVersionMapper;
import org.example.trungcapphuongnam.module.lms.repository.CauHoiVersionRepository;
import org.example.trungcapphuongnam.module.lms.repository.DapAnCauHoiVersionRepository;
import org.example.trungcapphuongnam.module.lms.service.DapAnCauHoiVersionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DapAnCauHoiVersionServiceImpl implements DapAnCauHoiVersionService {

    private final DapAnCauHoiVersionRepository repository;
    private final CauHoiVersionRepository cauHoiVersionRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DapAnCauHoiVersionResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(DapAnCauHoiVersionMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DapAnCauHoiVersionResponse findById(Long id) {
        return DapAnCauHoiVersionMapper.toResponse(getEntity(id));
    }

    @Override
    public DapAnCauHoiVersionResponse create(DapAnCauHoiVersionRequest request) {
        validateQuestionNotLocked(request.getCauHoiVersionId());
        DapAnCauHoiVersion entity = DapAnCauHoiVersionMapper.toEntity(request);
        return DapAnCauHoiVersionMapper.toResponse(repository.save(entity));
    }

    @Override
    public DapAnCauHoiVersionResponse update(Long id, DapAnCauHoiVersionRequest request) {
        DapAnCauHoiVersion entity = getEntity(id);
        validateQuestionNotLocked(entity.getCauHoiVersionId());
        validateQuestionNotLocked(request.getCauHoiVersionId());
        DapAnCauHoiVersionMapper.updateEntity(entity, request);
        return DapAnCauHoiVersionMapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        DapAnCauHoiVersion entity = getEntity(id);
        validateQuestionNotLocked(entity.getCauHoiVersionId());
        repository.delete(entity);
    }

    private DapAnCauHoiVersion getEntity(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DapAnCauHoiVersion với id = " + id));
    }

    private void validateQuestionNotLocked(Long cauHoiVersionId) {
        if (cauHoiVersionId == null) {
            return;
        }
        CauHoiVersion cauHoiVersion = cauHoiVersionRepository.findById(cauHoiVersionId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy CauHoiVersion với id = " + cauHoiVersionId));
        if (Boolean.TRUE.equals(cauHoiVersion.getIsLocked())) {
            throw new BadRequestException("Câu hỏi version đã được sử dụng, không được sửa đáp án trực tiếp. Hãy tạo version mới.");
        }
    }
}
