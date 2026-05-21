package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MonTienQuyetRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MonTienQuyetResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MonTienQuyet;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MonTienQuyetMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MonTienQuyetRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MonTienQuyetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class MonTienQuyetServiceImpl implements MonTienQuyetService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final MonTienQuyetRepository repository;
    private final MonTienQuyetMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MonTienQuyetResponse> findAll(Long monId, Long monDieuKienId, String loai, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<MonTienQuyet>empty()
                    .and(LocJpa.eq("monId", monId))
                    .and(LocJpa.eq("monDieuKienId", monDieuKienId))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.keyword(keyword, "loai", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MonTienQuyetResponse findById(Long id) {
        MonTienQuyet entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MonTienQuyet không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public MonTienQuyetResponse create(MonTienQuyetRequest request) {
        validator.validateMonTienQuyet(request, null);

        MonTienQuyet entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MonTienQuyetResponse update(Long id, MonTienQuyetRequest request) {
        validator.validateMonTienQuyet(request, id);

        MonTienQuyet entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MonTienQuyet không tồn tại: " + id));

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }
    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MonTienQuyet không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
