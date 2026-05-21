package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.SyllabusChuongTrinhRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.SyllabusChuongTrinhResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.SyllabusChuongTrinh;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.SyllabusChuongTrinhMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.SyllabusChuongTrinhRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.service.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.SyllabusChuongTrinhService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class SyllabusChuongTrinhServiceImpl implements SyllabusChuongTrinhService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final SyllabusChuongTrinhRepository repository;
    private final SyllabusChuongTrinhMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<SyllabusChuongTrinhResponse> findAll(Long chuongTrinhVersionId, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<SyllabusChuongTrinh>empty()
                    .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                    .and(LocJpa.keyword(keyword, "moTaTongQuan", "mucDich", "yeuCauDaoTao", "phuongPhapDaoTao", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public SyllabusChuongTrinhResponse findById(Long id) {
        SyllabusChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusChuongTrinh không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public SyllabusChuongTrinhResponse create(SyllabusChuongTrinhRequest request) {
        validator.validateSyllabusChuongTrinh(request, null);

        SyllabusChuongTrinh entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public SyllabusChuongTrinhResponse update(Long id, SyllabusChuongTrinhRequest request) {
        SyllabusChuongTrinh entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SyllabusChuongTrinh không tồn tại: " + id));
        validator.validateSyllabusChuongTrinh(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SyllabusChuongTrinh không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
