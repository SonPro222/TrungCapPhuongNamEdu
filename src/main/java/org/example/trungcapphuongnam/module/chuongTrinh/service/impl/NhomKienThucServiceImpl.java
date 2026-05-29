package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThuc;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NhomKienThucMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NhomKienThucRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomKienThucService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.trungcapphuongnam.common.spec.LocJpa;

@Service
@RequiredArgsConstructor
@Transactional
public class NhomKienThucServiceImpl implements NhomKienThucService {
    private final ChuongTrinhNghiepVuValidator validator;
    private final NhomKienThucRepository repository;
    private final NhomKienThucMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public Page<NhomKienThucResponse> findAll(
            Long chuongTrinhVersionId,
            Long nhomKienThucGocId,
            String loaiNhom,
            String keyword,
            Pageable pageable
    ) {
        return repository.findAll(
                LocJpa.<NhomKienThuc>empty()
                        .and(LocJpa.eq("chuongTrinhVersionId", chuongTrinhVersionId))
                        .and(LocJpa.eq("nhomKienThucGocId", nhomKienThucGocId))
                        .and(LocJpa.like("loaiNhom", loaiNhom))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "loaiNhom", "ghiChu")),
                pageable
        ).map(mapper::toResponse);
    }
    @Override
    @Transactional(readOnly = true)
    public NhomKienThucResponse findById(Long id) {
        NhomKienThuc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NhomKienThuc không tồn tại: " + id));
        return mapper.toResponse(entity);
    }

    @Override
    public NhomKienThucResponse create(NhomKienThucRequest request) {
        validator.validateNhomKienThuc(request, null);
        NhomKienThuc entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NhomKienThucResponse update(Long id, NhomKienThucRequest request) {
        NhomKienThuc entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("NhomKienThuc không tồn tại: " + id));
        validator.validateNhomKienThuc(request, id);
        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("NhomKienThuc không tồn tại: " + id);
        }
        repository.deleteById(id);
    }
}
