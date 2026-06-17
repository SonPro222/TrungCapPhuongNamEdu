package org.example.trungcapphuongnam.module.chuongTrinh.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.trungcapphuongnam.common.exception.ResourceNotFoundException;
import org.example.trungcapphuongnam.common.spec.LocJpa;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.request.NhomKienThucMauRequest;
import org.example.trungcapphuongnam.module.chuongTrinh.dto.response.NhomKienThucMauResponse;
import org.example.trungcapphuongnam.module.chuongTrinh.entity.NhomKienThucMau;
import org.example.trungcapphuongnam.module.chuongTrinh.mapper.NhomKienThucMauMapper;
import org.example.trungcapphuongnam.module.chuongTrinh.repository.NhomKienThucMauRepository;
import org.example.trungcapphuongnam.module.chuongTrinh.validator.ChuongTrinhNghiepVuValidator;
import org.example.trungcapphuongnam.module.chuongTrinh.service.NhomKienThucMauService;
import org.example.trungcapphuongnam.module.chuongTrinh.service.XoaChuongTrinhCascadeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NhomKienThucMauServiceImpl implements NhomKienThucMauService {

    private final NhomKienThucMauRepository repository;
    private final NhomKienThucMauMapper mapper;
    private final ChuongTrinhNghiepVuValidator validator;
    private final XoaChuongTrinhCascadeService xoaChuongTrinhCascadeService;

    @Override
    @Transactional(readOnly = true)
    public Page<NhomKienThucMauResponse> findAll(String ma, String loaiNhom, String keyword, Pageable pageable) {
        return repository.findAll(
                LocJpa.<NhomKienThucMau>empty()
                        .and(LocJpa.like("ma", ma))
                        .and(LocJpa.like("loaiNhom", loaiNhom))
                        .and(LocJpa.keyword(keyword, "ma", "ten", "loaiNhom", "moTa")),
                pageable
        ).map(mapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public NhomKienThucMauResponse findById(Long id) {
        NhomKienThucMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm kiến thức gốc không tồn tại: " + id));

        return mapper.toResponse(entity);
    }

    @Override
    public NhomKienThucMauResponse create(NhomKienThucMauRequest request) {
        validator.validateNhomKienThucMau(request, null);

        NhomKienThucMau entity = mapper.toEntity(request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public NhomKienThucMauResponse update(Long id, NhomKienThucMauRequest request) {
        NhomKienThucMau entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhóm kiến thức gốc không tồn tại: " + id));

        validator.validateNhomKienThucMau(request, id);

        mapper.updateEntity(entity, request);
        return mapper.toResponse(repository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Nhóm kiến thức gốc không tồn tại: " + id);
        }

        xoaChuongTrinhCascadeService.xoaTheoNhomKienThucMauId(id);

        repository.deleteById(id);
    }
}