package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.MucTieuChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.MucTieuChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.MucTieuChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.MucTieuChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.MucTieuChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.MucTieuChuongTrinhService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MucTieuChuongTrinhServiceImpl implements MucTieuChuongTrinhService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final MucTieuChuongTrinhRepository repository;
    private final MucTieuChuongTrinhMapper mapper;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<MucTieuChuongTrinhResponse> findAll(Long syllabusChuongTrinhId, Long chuongTrinhVersionId, String loai, String keyword, Pageable pageable) {
        Long resolvedSyllabusChuongTrinhId = resolveSyllabusChuongTrinhId(syllabusChuongTrinhId, chuongTrinhVersionId);
        return repository.findAll(
                LocJpa.<MucTieuChuongTrinh>empty()
                    .and(LocJpa.eq("syllabusChuongTrinhId", resolvedSyllabusChuongTrinhId))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.keyword(keyword, "loai", "noiDung", "ma", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public MucTieuChuongTrinhResponse findById(Long id) {
        MucTieuChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MucTieuChuongTrinh không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public MucTieuChuongTrinhResponse create(MucTieuChuongTrinhRequest request) {
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        MucTieuChuongTrinh entity = mapper.toEntity(request);
        validator.validateMucTieuChuongTrinh(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public MucTieuChuongTrinhResponse update(Long id, MucTieuChuongTrinhRequest request) {
        MucTieuChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MucTieuChuongTrinh không tồn tại: " + id));
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        validator.validateMucTieuChuongTrinh(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MucTieuChuongTrinh không tồn tại: " + id);
        }
        repository.deleteById(id);
    }

    private Long resolveSyllabusChuongTrinhId(Long syllabusChuongTrinhId, Long chuongTrinhVersionId) {
        if (syllabusChuongTrinhId != null) {
            return syllabusChuongTrinhId;
        }
        if (chuongTrinhVersionId == null) {
            return null;
        }
        return syllabusChuongTrinhRepository.findFirstByChuongTrinhVersionIdOrderByIdDesc(chuongTrinhVersionId)
                .map(SyllabusChuongTrinh::getId)
                .orElseThrow(() -> new ResourceNotFoundException("Version chương trình chưa có syllabus chương trình: " + chuongTrinhVersionId));
    }
}
