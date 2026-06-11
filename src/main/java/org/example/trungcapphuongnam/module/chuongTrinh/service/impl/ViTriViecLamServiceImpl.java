package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.ViTriViecLamRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.ViTriViecLamResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.ViTriViecLam;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.ViTriViecLamMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.ViTriViecLamRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ViTriViecLamService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ViTriViecLamServiceImpl implements ViTriViecLamService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final ViTriViecLamRepository repository;
    private final ViTriViecLamMapper mapper;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<ViTriViecLamResponse> findAll(Long syllabusChuongTrinhId, Long chuongTrinhVersionId, String keyword, Pageable pageable) {
        Long resolvedSyllabusChuongTrinhId = resolveSyllabusChuongTrinhId(syllabusChuongTrinhId, chuongTrinhVersionId);
        return repository.findAll(
                LocJpa.<ViTriViecLam>empty()
                    .and(LocJpa.eq("syllabusChuongTrinhId", resolvedSyllabusChuongTrinhId))
                    .and(LocJpa.keyword(keyword, "ma", "ten", "moTa", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ViTriViecLamResponse findById(Long id) {
        ViTriViecLam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViTriViecLam không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public ViTriViecLamResponse create(ViTriViecLamRequest request) {
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        ViTriViecLam entity = mapper.toEntity(request);
        validator.validateViTriViecLam(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public ViTriViecLamResponse update(Long id, ViTriViecLamRequest request) {
        ViTriViecLam entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ViTriViecLam không tồn tại: " + id));
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        validator.validateViTriViecLam(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ViTriViecLam không tồn tại: " + id);
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
