package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.DieuKienTotNghiepRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.DieuKienTotNghiepResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.DieuKienTotNghiep;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.DieuKienTotNghiepMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.DieuKienTotNghiepRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.DieuKienTotNghiepService;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DieuKienTotNghiepServiceImpl implements DieuKienTotNghiepService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final DieuKienTotNghiepRepository repository;
    private final DieuKienTotNghiepMapper mapper;
    private final SyllabusChuongTrinhRepository syllabusChuongTrinhRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<DieuKienTotNghiepResponse> findAll(Long syllabusChuongTrinhId, Long chuongTrinhVersionId, String keyword, Pageable pageable) {
        Long resolvedSyllabusChuongTrinhId = resolveSyllabusChuongTrinhId(syllabusChuongTrinhId, chuongTrinhVersionId);
        return repository.findAll(
                LocJpa.<DieuKienTotNghiep>empty()
                    .and(LocJpa.eq("syllabusChuongTrinhId", resolvedSyllabusChuongTrinhId))
                    .and(LocJpa.keyword(keyword, "ma", "noiDung", "ghiChu")),
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
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
        DieuKienTotNghiep entity = mapper.toEntity(request);
        validator.validateDieuKienTotNghiep(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public DieuKienTotNghiepResponse update(Long id, DieuKienTotNghiepRequest request) {
        DieuKienTotNghiep entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DieuKienTotNghiep không tồn tại: " + id));
        request.setSyllabusChuongTrinhId(resolveSyllabusChuongTrinhId(request.getSyllabusChuongTrinhId(), request.getChuongTrinhVersionId()));
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
