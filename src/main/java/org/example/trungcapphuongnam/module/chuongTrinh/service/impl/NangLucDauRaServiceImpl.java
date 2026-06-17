package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NangLucDauRaRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NangLucDauRaResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NangLucDauRa;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NangLucDauRaMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NangLucDauRaRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NangLucDauRaService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NangLucDauRaServiceImpl implements NangLucDauRaService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final NangLucDauRaRepository repository;
    private final NangLucDauRaMapper mapper;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<NangLucDauRaResponse> findAll(Long syllabusChuongTrinhId, Long chuongTrinhVersionId, String ma, String loai, String keyword, Pageable pageable) {
        Long resolvedSyllabusChuongTrinhId = resolveSyllabusChuongTrinhId(syllabusChuongTrinhId, chuongTrinhVersionId);
        return repository.findAll(
                LocJpa.<NangLucDauRa>empty()
                    .and(LocJpa.eq("syllabusChuongTrinhId", resolvedSyllabusChuongTrinhId))
                    .and(LocJpa.like("ma", ma))
                    .and(LocJpa.like("loai", loai))
                    .and(LocJpa.keyword(keyword, "ma", "loai", "noiDung", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NangLucDauRaResponse findById(Long id) {
        NangLucDauRa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NangLucDauRa không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public NangLucDauRaResponse create(NangLucDauRaRequest request) {
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        NangLucDauRa entity = mapper.toEntity(request);
        validator.validateNangLucDauRa(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NangLucDauRaResponse update(Long id, NangLucDauRaRequest request) {
        NangLucDauRa entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NangLucDauRa không tồn tại: " + id));
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        validator.validateNangLucDauRa(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("NangLucDauRa không tồn tại: " + id);
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
