package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiep;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.DieuKienTotNghiepMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.DieuKienTotNghiepRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienTotNghiepService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class DieuKienTotNghiepServiceImpl implements DieuKienTotNghiepService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final DieuKienTotNghiepRepository repository;
    private final DieuKienTotNghiepMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<DieuKienTotNghiepResponse> findAll(Long chuongTrinhVersionId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<DieuKienTotNghiep>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.keyword(keyword, "noiDung")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public DieuKienTotNghiepResponse findById(Long id) {
        DieuKienTotNghiep entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DieuKienTotNghiep không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public DieuKienTotNghiepResponse create(DieuKienTotNghiepRequest request) {
        DieuKienTotNghiep entity = mapper.toEntity(request);
        validator.validateDieuKienTotNghiep(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DieuKienTotNghiepResponse update(Long id, DieuKienTotNghiepRequest request) {
        DieuKienTotNghiep entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DieuKienTotNghiep không tồn tại: " + id));
        validator.validateDieuKienTotNghiep(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DieuKienTotNghiep không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
