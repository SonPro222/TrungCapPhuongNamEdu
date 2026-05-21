package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongBaiRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongBaiResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongBai;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusChuongBaiMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongBaiRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongBaiService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusChuongBaiServiceImpl implements SyllabusChuongBaiService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final SyllabusChuongBaiRepository repository;
    private final SyllabusChuongBaiMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusChuongBaiResponse> findAll(Long syllabusMonId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<SyllabusChuongBai>empty()
                    .and(LocJpa.eq("syllabusMonId", syllabusMonId))
                    .and(LocJpa.keyword(keyword, "ten", "noiDung", "mucTieu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusChuongBaiResponse findById(Long id) {
        SyllabusChuongBai entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusChuongBai không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusChuongBaiResponse create(SyllabusChuongBaiRequest request) {
        SyllabusChuongBai entity = mapper.toEntity(request);
        validator.validateSyllabusChuongBai(request, null);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusChuongBaiResponse update(Long id, SyllabusChuongBaiRequest request) {
        SyllabusChuongBai entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusChuongBai không tồn tại: " + id));
        validator.validateSyllabusChuongBai(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SyllabusChuongBai không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
